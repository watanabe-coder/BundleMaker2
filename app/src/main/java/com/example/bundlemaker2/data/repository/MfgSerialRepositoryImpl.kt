package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.local.entity.MfgSerialMapping
import com.example.bundlemaker2.data.local.entity.MappingStatus
import com.example.bundlemaker2.data.model.api.MappingsBulkRequest
import com.example.bundlemaker2.data.model.api.SerialItem
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MfgSerialMappingRepositoryImpl @Inject constructor(
    private val dao: MfgSerialMappingDao,
    private val remoteRepository: MfgSerialRemoteRepository
) : MfgSerialMappingRepository {

    override suspend fun insert(mapping: MfgSerialMapping): Long = dao.insert(mapping)
    override suspend fun update(mapping: MfgSerialMapping) = dao.update(mapping)
    override suspend fun delete(mapping: MfgSerialMapping) = dao.delete(mapping)
    override fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>> = dao.getByMfgId(mfgId)
    
    override fun getByMfgIdAndStatuses(
        mfgId: String,
        statuses: List<MappingStatus>
    ): Flow<List<MfgSerialMapping>> = dao.getByMfgIdAndStatuses(mfgId, statuses)
    
    override fun countByStatus(status: MappingStatus): Flow<Int> = dao.countByStatus(status)
    
    override suspend fun updateStatuses(ids: List<Long>, status: MappingStatus): Result<Unit> =
        try {
            dao.updateStatuses(ids, status)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getById(id: Long): MfgSerialMapping? = dao.getById(id)

    override suspend fun getUnsyncedMappings(): List<MfgSerialMapping> {
        return dao.getByStatus(MappingStatus.DRAFT)
    }

    override suspend fun syncMappings(token: String, mfgId: String): Result<Unit> {
        return try {
            // 未送信のマッピングを取得
            val unsyncedMappings = getUnsyncedMappings()
                .filter { it.mfgId == mfgId }
                .takeIf { it.isNotEmpty() } ?: return Result.success(Unit)

            // リクエストオブジェクトを作成
            val request = MappingsBulkRequest(
                requestId = java.util.UUID.randomUUID().toString(),
                deviceId = getDeviceId(), // デバイスIDを取得するメソッドを実装する必要があります
                mfgId = mfgId,
                items = unsyncedMappings.map { mapping ->
                    SerialItem(
                        serialId = mapping.serialId,
                        scannedAt = mapping.scannedAt.toString()
                    )
                }
            )

            // APIを呼び出してマッピングを送信
            val result = remoteRepository.sendMappings(token, request)

            result.fold(
                onSuccess = { response ->
                    // 成功した場合はステータスを更新
                    val syncedIds = unsyncedMappings.map { it.id }
                    updateStatuses(syncedIds, MappingStatus.SENT)
                    Result.success(Unit)
                },
                onFailure = { exception ->
                    // 失敗した場合はエラーを返す
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDeviceId(): String {
        // TODO: デバイス固有のIDを返す実装に置き換える
        return android.os.Build.SERIAL
    }

    private fun Long.toIso8601String(): String {
        val instant = Instant.ofEpochMilli(this)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
        return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime)
    }
}
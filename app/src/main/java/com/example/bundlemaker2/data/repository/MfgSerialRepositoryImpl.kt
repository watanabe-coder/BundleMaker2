package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MfgSerialRepositoryImpl @Inject constructor(
    private val dao: MfgSerialDao,
    private val mapper: MappingMapper
) : MfgSerialRepository {

    override suspend fun insert(mapping: MfgSerialMapping): Result<Long> = runCatching {
        val entity = mapper.toEntity(mapping)
        dao.insert(entity)
    }

    override suspend fun update(mapping: MfgSerialMapping): Result<Unit> = runCatching {
        val entity = mapper.toEntity(mapping)
        dao.update(entity)
    }

    override suspend fun delete(mapping: MfgSerialMapping): Result<Unit> = runCatching {
        val entity = mapper.toEntity(mapping)
        dao.delete(entity)
    }

    override fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>> {
        return dao.getByMfgId(mfgId).map { list ->
            list.map { mapper.toDomain(it) }
        }
    }

    override fun getByMfgIdAndStatuses(
        mfgId: String,
        statuses: List<MappingStatus>
    ): Flow<List<MfgSerialMapping>> {
        val statusNames = statuses.map { it.name }
        return dao.getByMfgIdAndStatuses(mfgId, statusNames).map { list ->
            list.map { mapper.toDomain(it) }
        }
    }

    override fun countByStatus(status: MappingStatus): Flow<Int> {
        return dao.countByStatus(status.name)
    }

    override suspend fun getById(id: Long): Result<MfgSerialMapping> = runCatching {
        val entity = dao.getById(id) ?: throw NoSuchElementException("Mapping not found with id: $id")
        mapper.toDomain(entity)
    }

    override suspend fun updateStatuses(ids: List<Long>, status: MappingStatus): Result<Unit> = runCatching {
        dao.updateStatuses(ids, status.name)
    }

    override suspend fun getUnsyncedMappings(): Result<List<MfgSerialMapping>> = runCatching {
        val entities = dao.getUnsyncedMappings()
        entities.map { mapper.toDomain(it) }
    }
    
    override suspend fun getAllMappings(): List<MfgSerialMapping> {
        return dao.getAll().map { mapper.toDomain(it) }
    }

    override suspend fun insertAll(mappings: List<MfgSerialMapping>): Result<Unit> = runCatching {
        val entities = mappings.map { mapper.toEntity(it) }
        dao.insertAll(entities)
    }

    override suspend fun syncMappings(token: String, mfgId: String): Result<Unit> = runCatching {
        // 未同期のマッピングを取得
        val unsynced = dao.getUnsyncedMappings()

        if (unsynced.isNotEmpty()) {
            // 同期処理を実装...

            // 同期が完了したらマーク
            val ids = unsynced.map { it.id }
            dao.markAsSynced(ids)
        }
    }

    override suspend fun getReadyToSync(): List<MfgSerialMapping> {
        return dao.getByStatus(MappingStatus.READY.name).map { mapper.toDomain(it) }
    }

    override suspend fun updateStatus(ids: List<Long>, status: MappingStatus) {
        if (ids.isNotEmpty()) {
            dao.updateStatus(ids, status.name)
        }
    }

    override suspend fun deleteSynced(ids: List<Long>) {
        if (ids.isNotEmpty()) {
            dao.deleteByIds(ids)
        }
    }
}
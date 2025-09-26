package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.entity.MappingStatus
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MfgSerialRepositoryImpl @Inject constructor(
    private val mfgSerialMappingDao: MfgSerialMappingDao
) : MfgSerialRepository {

    override suspend fun getMapping(id: Long): MfgSerialMapping? {
        return try {
            mfgSerialMappingDao.getById(id)
        } catch (e: Exception) {
            Result.failure<MfgSerialMapping>(e)
            null
        }
    }

    override suspend fun getMapping(mfgId: String, serialId: String): MfgSerialMapping? {
        return try {
            mfgSerialMappingDao.getByMfgIdAndSerialId(mfgId, serialId)
        } catch (e: Exception) {
            Result.failure<MfgSerialMapping>(e)
            null
        }
    }

    override fun getMappingsByMfgId(mfgId: String): Flow<List<MfgSerialMapping>> {
        return mfgSerialMappingDao.getByMfgId(mfgId)
    }

    override fun getMappingsByStatus(statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>> {
        return mfgSerialMappingDao.getByStatus(statuses)
    }

    override suspend fun addMapping(
        mfgId: String, 
        serialId: String, 
        scannedAt: Instant
    ): Result<Long> {
        return try {
            // 重複チェック
            val existing = mfgSerialMappingDao.getByMfgIdAndSerialId(mfgId, serialId)
            if (existing != null) {
                return Result.failure(Exception("既に登録されているシリアル番号です"))
            }
            
            val mapping = MfgSerialMapping(
                mfgId = mfgId,
                serialId = serialId,
                scannedAt = scannedAt,
                status = MappingStatus.DRAFT
            )
            val id = mfgSerialMappingDao.insert(mapping)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMapping(mapping: MfgSerialMapping): Result<Unit> {
        return try {
            mfgSerialMappingDao.update(mapping)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMapping(mapping: MfgSerialMapping): Result<Unit> {
        return try {
            mfgSerialMappingDao.delete(mapping)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatus(
        id: Long, 
        status: MappingStatus, 
        errorCode: String?
    ): Result<Unit> {
        return try {
            mfgSerialMappingDao.updateStatus(id, status, errorCode)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun countByStatus(status: MappingStatus): Flow<Int> {
        return mfgSerialMappingDao.countByStatus(status)
    }

    override suspend fun count(): Int {
        return try {
            mfgSerialMappingDao.count()
        } catch (e: Exception) {
            -1
        }
    }
}

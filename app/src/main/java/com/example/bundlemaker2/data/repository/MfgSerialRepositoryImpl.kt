package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.data.entity.MappingStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MfgSerialMappingRepositoryImpl @Inject constructor(
    private val dao: MfgSerialMappingDao
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
}
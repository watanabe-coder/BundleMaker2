package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MfgSerialMappingRepositoryImpl @Inject constructor(
    private val dao: MfgSerialMappingDao,
    private val mapper: MappingMapper
) : MfgSerialMappingRepository {

    override suspend fun insert(mapping: MfgSerialMapping): Long {
        val entity = mapper.toEntity(mapping)
        return dao.insert(entity)
    }

    override suspend fun update(mapping: MfgSerialMapping) {
        val entity = mapper.toEntity(mapping)
        dao.update(entity)
    }

    override suspend fun delete(mapping: MfgSerialMapping) {
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
        val statusStrings = statuses.map { it.name }
        return dao.getByMfgIdAndStatuses(mfgId, statusStrings).map { list ->
            list.map { mapper.toDomain(it) }
        }
    }
    
    override fun countByStatus(status: MappingStatus): Flow<Int> {
        return dao.countByStatus(status.name)
    }
    
    override suspend fun updateStatuses(ids: List<Long>, status: MappingStatus): Result<Unit> =
        try {
            dao.updateStatuses(ids, status.name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getById(id: Long): MfgSerialMapping? {
        return dao.getById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun getUnsyncedMappings(): List<MfgSerialMapping> {
        return dao.getByStatus(MappingStatus.DRAFT.name).map { mapper.toDomain(it) }
    }

    override suspend fun syncMappings(token: String, mfgId: String): Result<Unit> {
        // ローカルでのみ動作するため、常に成功を返す
        return Result.success(Unit)
    }
}
package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MappingRepositoryImpl @Inject constructor(
    private val dao: MfgSerialMappingDao,
    private val mapper: MappingMapper
) : MappingRepository {
    override suspend fun insert(mapping: MfgSerialMapping): Long {
        return dao.insert(mapper.toEntity(mapping))
    }

    override suspend fun update(mapping: MfgSerialMapping) {
        dao.update(mapper.toEntity(mapping))
    }

    override suspend fun delete(mapping: MfgSerialMapping) {
        dao.delete(mapper.toEntity(mapping))
    }

    override fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>> {
        return dao.getByMfgId(mfgId).map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override fun getByMfgIdAndStatuses(mfgId: String, statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>> {
        return dao.getByMfgIdAndStatuses(mfgId, statuses.map { it.name }).map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override fun countByStatus(status: MappingStatus): Flow<Int> {
        return dao.countByStatus(status.name)
    }

    override suspend fun updateStatuses(ids: List<Long>, status: MappingStatus) {
        dao.updateStatuses(ids, status.name)
    }

    override suspend fun getById(id: Long): MfgSerialMapping? {
        return dao.getById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun getByStatus(status: MappingStatus): List<MfgSerialMapping> {
        return mapper.toDomainList(dao.getByStatus(status.name))
    }
}
package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.domain.dao.MfgSerialMappingDao
import com.example.bundlemaker2.domain.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MfgSerialMappingRepositoryImpl @Inject constructor(
    private val dao: MfgSerialMappingDao
) : MfgSerialMappingRepository {
    
    override suspend fun insertMapping(mapping: MfgSerialMappingEntity) {
        dao.insert(mapping)
    }
    
    override fun getAllMappings(): Flow<List<MfgSerialMappingEntity>> {
        return dao.getAll()
    }
    
    override suspend fun updateMapping(mapping: MfgSerialMappingEntity) {
        dao.update(mapping)
    }
    
    override suspend fun deleteMapping(mapping: MfgSerialMappingEntity) {
        dao.delete(mapping)
    }
    
    override suspend fun getMappingsByMfgId(mfgId: String): List<MfgSerialMappingEntity> {
        return dao.findByMfgId(mfgId)
    }
}

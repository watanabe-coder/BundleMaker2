package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.entity.MfgSerialMapping

interface MappingRepository {
    suspend fun confirmMappings(mfgId: String): Result<Unit>
    suspend fun deleteMapping(mappingId: Long): Result<Unit>
    suspend fun appendMapping(mfgId: String, serialId: String): Result<Unit>
    suspend fun getMappingsByMfgId(mfgId: String): List<MfgSerialMapping>

}
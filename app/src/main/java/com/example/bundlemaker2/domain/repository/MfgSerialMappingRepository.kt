package com.example.bundlemaker2.domain.repository

import com.example.bundlemaker2.domain.entity.MfgSerialMapping

interface MfgSerialMappingRepository {
    suspend fun insert(mapping: MfgSerialMapping)
    suspend fun getAll(): List<MfgSerialMapping>
    suspend fun update(mapping: MfgSerialMapping)
    suspend fun delete(mapping: MfgSerialMapping)
    suspend fun findByMfgId(mfgId: String): List<MfgSerialMapping>
}
package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.entity.MfgSerialMapping
import com.example.bundlemaker2.data.local.entity.MappingStatus
import kotlinx.coroutines.flow.Flow

interface MfgSerialMappingRepository {
    suspend fun insert(mapping: MfgSerialMapping): Long
    suspend fun update(mapping: MfgSerialMapping)
    suspend fun delete(mapping: MfgSerialMapping)
    fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>
    fun getByMfgIdAndStatuses(mfgId: String, statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>>
    fun countByStatus(status: MappingStatus): Flow<Int>
    suspend fun updateStatuses(ids: List<Long>, status: MappingStatus): Result<Unit>
    suspend fun getById(id: Long): MfgSerialMapping?
    suspend fun syncMappings(token: String, mfgId: String): Result<Unit>
    suspend fun getUnsyncedMappings(): List<MfgSerialMapping>
}
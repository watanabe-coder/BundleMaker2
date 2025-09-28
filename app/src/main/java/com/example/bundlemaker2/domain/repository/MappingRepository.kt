package com.example.bundlemaker2.domain.repository

import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun insert(mapping: MfgSerialMapping): Long
    suspend fun update(mapping: MfgSerialMapping)
    suspend fun delete(mapping: MfgSerialMapping)
    fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>
    fun getByMfgIdAndStatuses(mfgId: String, statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>>
    fun countByStatus(status: MappingStatus): Flow<Int>
    suspend fun updateStatuses(ids: List<Long>, status: MappingStatus)
    suspend fun getById(id: Long): MfgSerialMapping?
    suspend fun getByStatus(status: MappingStatus): List<MfgSerialMapping>
}
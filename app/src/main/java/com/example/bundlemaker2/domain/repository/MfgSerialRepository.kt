package com.example.bundlemaker2.domain.repository

import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.model.MappingStatus
import kotlinx.coroutines.flow.Flow

interface MfgSerialRepository {
    // 基本CRUD操作
    suspend fun insert(mapping: MfgSerialMapping): Result<Long>
    suspend fun update(mapping: MfgSerialMapping): Result<Unit>
    suspend fun delete(mapping: MfgSerialMapping): Result<Unit>

    // クエリ
    fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>
    fun getByMfgIdAndStatuses(mfgId: String, statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>>
    fun countByStatus(status: MappingStatus): Flow<Int>
    suspend fun getById(id: Long): Result<MfgSerialMapping>

    // バッチ操作
    suspend fun updateStatuses(ids: List<Long>, status: MappingStatus): Result<Unit>
    suspend fun getUnsyncedMappings(): Result<List<MfgSerialMapping>>

    // 同期関連（必要に応じて別のRepositoryに分けても良い）
    suspend fun syncMappings(token: String, mfgId: String): Result<Unit>


    suspend fun insertAll(mappings: List<MfgSerialMapping>): Result<Unit>
}
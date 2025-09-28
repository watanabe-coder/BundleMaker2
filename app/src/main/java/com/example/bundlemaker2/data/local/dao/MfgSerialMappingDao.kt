package com.example.bundlemaker2.data.local.dao

import androidx.room.*
import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.domain.model.MappingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MfgSerialMappingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: MfgSerialMappingEntity): Long

    @Update
    suspend fun update(mapping: MfgSerialMappingEntity)

    @Delete
    suspend fun delete(mapping: MfgSerialMappingEntity)

    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId")
    fun getByMfgId(mfgId: String): Flow<List<MfgSerialMappingEntity>>

    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId AND status IN (:statuses)")
    fun getByMfgIdAndStatuses(mfgId: String, statuses: List<String>): Flow<List<MfgSerialMappingEntity>>

    @Query("SELECT COUNT(*) FROM mfg_serial_mappings WHERE status = :status")
    fun countByStatus(status: String): Flow<Int>

    @Query("UPDATE mfg_serial_mappings SET status = :status WHERE id IN (:ids)")
    suspend fun updateStatuses(ids: List<Long>, status: String)

    @Query("SELECT * FROM mfg_serial_mappings WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MfgSerialMappingEntity?

    @Query("SELECT * FROM mfg_serial_mappings WHERE status = :status")
    suspend fun getByStatus(status: String): List<MfgSerialMappingEntity>
    
    @Query("SELECT * FROM mfg_serial_mappings WHERE status = :status")
    fun observeByStatus(status: String): Flow<List<MfgSerialMappingEntity>>
    
    @Query("SELECT * FROM mfg_serial_mappings")
    fun observeAll(): Flow<List<MfgSerialMappingEntity>>
    
    @Query("DELETE FROM mfg_serial_mappings")
    suspend fun deleteAll()
}

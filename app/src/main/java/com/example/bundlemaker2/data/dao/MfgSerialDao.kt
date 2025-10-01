package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MfgSerialDao {
    @Insert
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

    @Query("SELECT * FROM mfg_serial_mappings WHERE id = :id")
    suspend fun getById(id: Long): MfgSerialMappingEntity?

    @Query("UPDATE mfg_serial_mappings SET status = :status WHERE id IN (:ids)")
    suspend fun updateStatuses(ids: List<Long>, status: String)

    @Query("SELECT * FROM mfg_serial_mappings WHERE synced = 0")
    suspend fun getUnsyncedMappings(): List<MfgSerialMappingEntity>

    @Query("UPDATE mfg_serial_mappings SET synced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mappings: List<MfgSerialMappingEntity>)
    
    @Query("SELECT * FROM mfg_serial_mappings ORDER BY scannedAt DESC")
    suspend fun getAll(): List<MfgSerialMappingEntity>

    @Query("SELECT * FROM mfg_serial_mappings WHERE status = :status")
    suspend fun getByStatus(status: String): List<MfgSerialMappingEntity>

    @Query("UPDATE mfg_serial_mappings SET status = :status WHERE id IN (:ids)")
    suspend fun updateStatus(ids: List<Long>, status: String)

    @Query("DELETE FROM mfg_serial_mappings WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)
}
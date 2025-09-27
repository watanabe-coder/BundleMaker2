package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.data.entity.MappingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MfgSerialMappingDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(mapping: MfgSerialMapping): Long

    @Update
    suspend fun update(mapping: MfgSerialMapping)

    @Delete
    suspend fun delete(mapping: MfgSerialMapping)

    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId")
    fun getMappingsByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>

    @Query("SELECT * FROM mfg_serial_mappings WHERE status = :status")
    fun getMappingsByStatus(status: MappingStatus): Flow<List<MfgSerialMapping>>

    @Query("SELECT COUNT(*) FROM mfg_serial_mappings WHERE status = :status")
    suspend fun countByStatus(status: MappingStatus): Int

    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId AND serialId = :serialId LIMIT 1")
    suspend fun findByMfgAndSerial(mfgId: String, serialId: String): MfgSerialMapping?
}


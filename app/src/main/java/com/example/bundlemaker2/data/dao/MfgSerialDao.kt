package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.model.MfgSerialMapping
import kotlinx.coroutines.flow.Flow

@Dao
interface MfgSerialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: MfgSerialMapping): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mappings: List<MfgSerialMapping>)

    @Query("SELECT * FROM mfg_serial_mapping WHERE mfgId = :mfgId AND serialId = :serialId")
    suspend fun getMapping(mfgId: String, serialId: String): MfgSerialMapping?

    @Query("SELECT * FROM mfg_serial_mapping WHERE mfgId = :mfgId")
    fun getMappingsByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>

    @Query("SELECT * FROM mfg_serial_mapping WHERE serialId = :serialId")
    suspend fun getMappingBySerialId(serialId: String): MfgSerialMapping?

    @Query("SELECT COUNT(*) FROM mfg_serial_mapping WHERE mfgId = :mfgId")
    suspend fun countByMfgId(mfgId: String): Int

    @Query("SELECT COUNT(*) FROM mfg_serial_mapping WHERE serialId = :serialId")
    suspend fun countBySerialId(serialId: String): Int

    @Query("SELECT * FROM mfg_serial_mapping WHERE synced = 0")
    suspend fun getUnsyncedMappings(): List<MfgSerialMapping>

    @Update
    suspend fun update(mapping: MfgSerialMapping)

    @Query("UPDATE mfg_serial_mapping SET synced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)
    
    @Delete
    suspend fun delete(mapping: MfgSerialMapping)
}

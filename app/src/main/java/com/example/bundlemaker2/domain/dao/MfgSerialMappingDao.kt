package com.example.bundlemaker2.domain.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.MfgSerialMappingEntity

@Dao
interface MfgSerialMappingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: MfgSerialMappingEntity)

    @Query("SELECT * FROM mfg_serial_mapping")
    suspend fun getAll(): List<MfgSerialMappingEntity>

    @Update
    suspend fun update(mapping: MfgSerialMappingEntity)

    @Delete
    suspend fun delete(mapping: MfgSerialMappingEntity)

    @Query("SELECT * FROM mfg_serial_mapping WHERE mfgId = :mfgId")
    suspend fun findByMfgId(mfgId: String): List<MfgSerialMappingEntity>
}
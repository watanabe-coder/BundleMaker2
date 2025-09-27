package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.data.entity.MappingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MfgSerialMappingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: MfgSerialMapping): Long

    @Update
    suspend fun update(mapping: MfgSerialMapping)

    @Delete
    suspend fun delete(mapping: MfgSerialMapping)

    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId")
    fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>

    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId AND status IN (:statuses)")
    fun getByMfgIdAndStatuses(mfgId: String, statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>>

    @Query("SELECT COUNT(*) FROM mfg_serial_mappings WHERE status = :status")
    fun countByStatus(status: MappingStatus): Flow<Int>

    @Query("UPDATE mfg_serial_mappings SET status = :status WHERE id IN (:ids)")
    suspend fun updateStatuses(ids: List<Long>, status: MappingStatus)

    @Query("SELECT * FROM mfg_serial_mappings WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MfgSerialMapping?
}

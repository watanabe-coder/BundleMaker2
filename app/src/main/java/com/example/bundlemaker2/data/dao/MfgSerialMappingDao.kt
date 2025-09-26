package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.MappingStatus
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface MfgSerialMappingDao {
    @Query("SELECT * FROM mfg_serial_mappings WHERE id = :id")
    suspend fun getById(id: Long): MfgSerialMapping?
    
    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId AND serialId = :serialId")
    suspend fun getByMfgIdAndSerialId(mfgId: String, serialId: String): MfgSerialMapping?
    
    @Query("SELECT * FROM mfg_serial_mappings WHERE mfgId = :mfgId ORDER BY scannedAt DESC")
    fun getByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>
    
    @Query("SELECT * FROM mfg_serial_mappings WHERE status IN (:statuses) ORDER BY scannedAt DESC")
    fun getByStatus(statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>>
    
    @Query("SELECT COUNT(*) FROM mfg_serial_mappings WHERE status = :status")
    fun countByStatus(status: MappingStatus): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: MfgSerialMapping): Long
    
    @Update
    suspend fun update(mapping: MfgSerialMapping)
    
    @Delete
    suspend fun delete(mapping: MfgSerialMapping)
    
    @Query("UPDATE mfg_serial_mappings SET status = :status, errorCode = :errorCode, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: MappingStatus, errorCode: String?, updatedAt: Instant = Instant.now())
    
    @Query("SELECT COUNT(*) FROM mfg_serial_mappings")
    suspend fun count(): Int
}

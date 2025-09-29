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

    // バッチ挿入
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mappings: List<MfgSerialMappingEntity>): List<Long>

    // バッチ更新
    @Update
    suspend fun updateAll(mappings: List<MfgSerialMappingEntity>)

    // バッチ削除
    @Delete
    suspend fun deleteAll(mappings: List<MfgSerialMappingEntity>)

    // 重複チェック用
    @Query("SELECT COUNT(*) FROM mfg_serial_mappings WHERE mfgId = :mfgId AND serialId = :serialId")
    suspend fun countByMfgIdAndSerialId(mfgId: String, serialId: String): Int
}

// トランザクション処理や複雑な操作は拡張関数として実装
suspend fun MfgSerialMappingDao.insertWithValidation(mapping: MfgSerialMappingEntity): Result<Long> {
    return try {
        val id = insert(mapping)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// バッチ挿入のトランザクション処理
suspend fun MfgSerialMappingDao.insertAllWithValidation(
    mappings: List<MfgSerialMappingEntity>
): Result<List<Long>> {
    return try {
        val ids = insertAll(mappings)
        Result.success(ids)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
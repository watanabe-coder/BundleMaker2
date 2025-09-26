package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.WorkSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface WorkSessionDao {
    @Query("SELECT * FROM work_sessions WHERE id = :id")
    suspend fun getById(id: Long): WorkSession?
    
    @Query("SELECT * FROM work_sessions WHERE mfgId = :mfgId ORDER BY startedAt DESC LIMIT 1")
    suspend fun getLatestByMfgId(mfgId: String): WorkSession?
    
    @Query("SELECT * FROM work_sessions ORDER BY startedAt DESC")
    fun getAll(): Flow<List<WorkSession>>
    
    @Query("SELECT * FROM work_sessions WHERE endedAt IS NULL ORDER BY startedAt DESC")
    fun getActiveSessions(): Flow<List<WorkSession>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WorkSession): Long
    
    @Update
    suspend fun update(session: WorkSession)
    
    @Delete
    suspend fun delete(session: WorkSession)
    
    @Query("UPDATE work_sessions SET endedAt = :endTime, updatedAt = :updatedAt WHERE id = :id")
    suspend fun endSession(id: Long, endTime: Instant = Instant.now(), updatedAt: Instant = Instant.now())
    
    @Query("SELECT COUNT(*) FROM work_sessions")
    suspend fun count(): Int
}

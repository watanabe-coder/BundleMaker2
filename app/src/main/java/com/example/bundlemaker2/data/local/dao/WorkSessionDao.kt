package com.example.bundlemaker2.data.local.dao

import androidx.room.*
import com.example.bundlemaker2.data.local.entity.WorkSessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface WorkSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WorkSessionEntity): Long

    @Update
    suspend fun update(session: WorkSessionEntity)

    @Delete
    suspend fun delete(session: WorkSessionEntity)

    @Query("SELECT * FROM work_sessions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): WorkSessionEntity?

    @Query("SELECT * FROM work_sessions WHERE mfgId = :mfgId ORDER BY startedAt DESC")
    fun getByMfgId(mfgId: String): Flow<List<WorkSessionEntity>>
    
    @Query("SELECT * FROM work_sessions ORDER BY startedAt DESC")
    fun observeAll(): Flow<List<WorkSessionEntity>>
    
    @Query("SELECT * FROM work_sessions WHERE endedAt IS NULL ORDER BY startedAt DESC LIMIT 1")
    fun getActiveSession(): Flow<WorkSessionEntity?>
    
    @Query("UPDATE work_sessions SET endedAt = :endTime WHERE id = :sessionId")
    suspend fun endSession(sessionId: Long, endTime: Instant = Instant.now())
    
    @Query("DELETE FROM work_sessions")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM work_sessions WHERE startedAt >= :startTime AND (endedAt <= :endTime OR endedAt IS NULL)")
    fun getSessionsInTimeRange(startTime: Instant, endTime: Instant): Flow<List<WorkSessionEntity>>
}

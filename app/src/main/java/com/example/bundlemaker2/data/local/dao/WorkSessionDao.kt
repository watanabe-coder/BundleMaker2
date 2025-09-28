package com.example.bundlemaker2.data.local.dao

import androidx.room.*
import com.example.bundlemaker2.data.local.entity.WorkSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface WorkSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WorkSession): Long

    @Update
    suspend fun update(session: WorkSession)

    @Delete
    suspend fun delete(session: WorkSession)

    @Query("SELECT * FROM work_sessions WHERE mfgId = :mfgId")
    fun getSessionsByMfgId(mfgId: String): Flow<List<WorkSession>>

    @Query("SELECT * FROM work_sessions ORDER BY startedAt DESC")
    fun getAllSessions(): Flow<List<WorkSession>>

    @Query("SELECT * FROM work_sessions WHERE id = :id")
    suspend fun getById(id: Long): WorkSession?

    @Query("SELECT * FROM work_sessions WHERE mfgId = :mfgId ORDER BY startedAt DESC LIMIT 1")
    suspend fun getLatestByMfgId(mfgId: String): WorkSession?

    @Query("SELECT * FROM work_sessions WHERE endedAt IS NULL")
    fun getActiveSessions(): Flow<List<WorkSession>>

    @Query("UPDATE work_sessions SET endedAt = :endTime WHERE id = :sessionId")
    suspend fun endSession(sessionId: Long, endTime: Instant)

    @Query("SELECT COUNT(*) FROM work_sessions")
    suspend fun count(): Int
}

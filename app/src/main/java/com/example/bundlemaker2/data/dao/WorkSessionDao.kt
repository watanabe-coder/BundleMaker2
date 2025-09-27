package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.WorkSession
import kotlinx.coroutines.flow.Flow

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
}


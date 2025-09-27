package com.example.bundlemaker2.data.dao

import androidx.room.*
import com.example.bundlemaker2.data.entity.Outbox
import com.example.bundlemaker2.data.entity.OutboxState
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outbox: Outbox): Long

    @Update
    suspend fun update(outbox: Outbox)

    @Delete
    suspend fun delete(outbox: Outbox)

    @Query("SELECT * FROM outbox WHERE state = :state ORDER BY createdAt ASC")
    fun getByState(state: OutboxState): Flow<List<Outbox>>

    @Query("SELECT * FROM outbox ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Outbox>>

    @Query("SELECT COUNT(*) FROM outbox WHERE state = :state")
    suspend fun countByState(state: OutboxState): Int
}


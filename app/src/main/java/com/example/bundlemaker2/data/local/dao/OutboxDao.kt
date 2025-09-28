package com.example.bundlemaker2.data.local.dao

import androidx.room.*
import com.example.bundlemaker2.data.local.entity.Outbox
import com.example.bundlemaker2.data.local.entity.OutboxState
import kotlinx.coroutines.flow.Flow
import java.time.Instant

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

    @Query("UPDATE outbox SET state = :state, lastTriedAt = :lastTriedAt WHERE outboxId = :outboxId")
    suspend fun updateState(outboxId: Long, state: OutboxState, lastTriedAt: Instant?)

    @Query("SELECT * FROM outbox WHERE outboxId = :outboxId LIMIT 1")
    suspend fun getById(outboxId: Long): Outbox?
}


package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.entity.Outbox
import com.example.bundlemaker2.data.local.entity.OutboxState
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface OutboxRepository {
    suspend fun insert(outbox: Outbox): Long
    suspend fun update(outbox: Outbox)
    suspend fun delete(outbox: Outbox)
    fun getByState(state: OutboxState): Flow<List<Outbox>>
    suspend fun updateState(outboxId: Long, state: OutboxState, lastTriedAt: Instant? = null)
    suspend fun getById(outboxId: Long): Outbox?
}
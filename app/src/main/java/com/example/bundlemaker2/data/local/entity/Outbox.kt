package com.example.bundlemaker2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "outbox")
data class Outbox(
    @PrimaryKey(autoGenerate = true)
    val outboxId: Long = 0L,
    val payloadJson: String,
    val createdAt: Instant,
    val lastTriedAt: Instant? = null,
    val state: OutboxState
)

enum class OutboxState {
    PENDING, SENDING, DONE, FAILED
}
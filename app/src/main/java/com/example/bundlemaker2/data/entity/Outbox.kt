package com.example.bundlemaker2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

enum class OutboxState {
    PENDING,
    SENDING,
    DONE,
    FAILED
}

@Entity(tableName = "outbox")
data class Outbox(
    @PrimaryKey(autoGenerate = true)
    val outboxId: Long = 0,
    val payloadJson: String,
    val createdAt: Instant = Instant.now(),
    val lastTriedAt: Instant? = null,
    val tryCount: Int = 0,
    val state: OutboxState = OutboxState.PENDING
)


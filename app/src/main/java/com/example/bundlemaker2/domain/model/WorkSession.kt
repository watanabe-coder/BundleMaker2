package com.example.bundlemaker2.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "work_sessions")
data class WorkSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val mfgId: String,
    val startedAt: Instant,
    val endedAt: Instant? = null,
    val note: String? = null
)

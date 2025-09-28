package com.example.bundlemaker2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * 作業セッションを表すデータベースエンティティ
 */
@Entity(tableName = "work_sessions")
data class WorkSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mfgId: String,
    val startedAt: Instant,
    val endedAt: Instant? = null,
    val note: String? = null
)

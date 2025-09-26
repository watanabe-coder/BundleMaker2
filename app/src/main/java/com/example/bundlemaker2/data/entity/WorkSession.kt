package com.example.bundlemaker2.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "work_sessions",
    indices = [
        Index(value = ["mfgId"], unique = false)
    ]
)
data class WorkSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // 製造番号
    val mfgId: String,
    
    // セッション開始時刻
    val startedAt: Instant = Instant.now(),
    
    // セッション終了時刻（nullの場合は進行中）
    val endedAt: Instant? = null,
    
    // メモ
    val note: String? = null,
    
    // 最終更新日時
    val updatedAt: Instant = Instant.now()
)

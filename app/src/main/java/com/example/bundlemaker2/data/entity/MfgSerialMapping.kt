package com.example.bundlemaker2.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

enum class MappingStatus {
    DRAFT,      // 下書き
    READY,      // 送信準備完了
    SUCCESS,    // 送信成功
    SENT,       // 送信済み
    ERROR       // エラー
}

@Entity(
    tableName = "mfg_serial_mappings",
    indices = [
        Index(value = ["mfgId", "serialId"], unique = true),
        Index(value = ["status"])
    ]
)
data class MfgSerialMapping(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // 製造番号
    val mfgId: String,
    
    // シリアル番号
    val serialId: String,
    
    // スキャン日時
    val scannedAt: Instant,
    
    // ステータス
    val status: MappingStatus = MappingStatus.DRAFT,
    
    // エラーコード（エラー時のみ）
    val errorCode: String? = null,
    
    // 最終更新日時
    val updatedAt: Instant = Instant.now()
) {
    companion object {
        // ステータスが送信可能かどうか
        fun isSendable(status: MappingStatus): Boolean {
            return status == MappingStatus.DRAFT || status == MappingStatus.ERROR
        }
    }
}

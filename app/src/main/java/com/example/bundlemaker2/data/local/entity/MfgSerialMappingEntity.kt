package com.example.bundlemaker2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * 製造番号とシリアル番号のマッピングを表すデータベースエンティティ
 */
// In MfgSerialMappingEntity.kt
@Entity(tableName = "mfg_serial_mappings")
data class MfgSerialMappingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mfgId: String,
    val serialId: String,
    val scannedAt: Instant,
    val status: String,
    val errorCode: String? = null,
    @ColumnInfo(defaultValue = "NULL")
    val syncedAt: Instant? = null
) {
    @Ignore
    constructor(
        id: Long = 0,
        mfgId: String,
        serialId: String,
        scannedAt: Instant,
        status: String,
        errorCode: String? = null
    ) : this(id, mfgId, serialId, scannedAt, status, errorCode, null)
}
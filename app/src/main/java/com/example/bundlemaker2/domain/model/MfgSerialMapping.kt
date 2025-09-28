package com.example.bundlemaker2.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.time.Instant

@Entity(
    tableName = "mfg_serial_mappings",
    indices = [Index(value = ["mfgId", "serialId"], unique = true)]
)
data class MfgSerialMapping(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val mfgId: String,
    val serialId: String,
    val scannedAt: Instant,
    val status: MappingStatus,
    val errorCode: String? = null
)

enum class MappingStatus {
    DRAFT, READY, SENT, ERROR
}
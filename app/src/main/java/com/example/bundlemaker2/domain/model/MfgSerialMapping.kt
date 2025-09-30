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
    val id: Long = 0,
    val mfgId: String,
    val serialId: String,
    val status: MappingStatus,
    val scannedAt: Instant = Instant.now(),
    val synced: Boolean = false
)

enum class MappingStatus {
    DRAFT,CONFIRMED, READY, SENT, ERROR
}
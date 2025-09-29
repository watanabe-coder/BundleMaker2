package com.example.bundlemaker2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "mfg_serial_mapping")
data class MfgSerialMapping(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mfgId: String,
    val serialId: String,
    val status: String = "READY",
    val scannedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)

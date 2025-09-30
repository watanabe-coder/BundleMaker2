package com.example.bundlemaker2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mfg_serial_mappings")
data class MfgSerialMappingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mfgId: String,
    val serialId: String,
    val status: String,
    val synced: Boolean = false
)
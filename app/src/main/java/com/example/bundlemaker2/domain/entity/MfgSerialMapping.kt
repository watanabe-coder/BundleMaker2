package com.example.bundlemaker2.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "mfg_serial_mapping",
    indices = [Index(value = ["mfgId", "serialId"], unique = true)]
)
data class MfgSerialMappingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mfgId: String,
    val serialId: String,
    val scannedAt: Long,
    val status: String = "READY",
    val errorCode: String? = null
)
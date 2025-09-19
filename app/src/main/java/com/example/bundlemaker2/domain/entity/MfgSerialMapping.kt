package com.example.bundlemaker2.domain.entity

data class MfgSerialMapping(
    val id: Long = 0,
    val mfgId: String,
    val serialId: String,
    val scannedAt: Long,
    val status: String = "READY",
    val errorCode: String? = null
)
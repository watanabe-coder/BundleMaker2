package com.example.bundlemaker2.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class SyncRequest(
    @field:Json(name = "mfgId") val mfgId: String,
    @field:Json(name = "serials") val serials: List<SerialItemDto>
)

@JsonClass(generateAdapter = true)
data class SerialItemDto(
    @field:Json(name = "serialId") val serialId: String,
    @field:Json(name = "scannedAt") val scannedAt: String
)

// レスポンス用のDTO
@JsonClass(generateAdapter = true)
data class SyncResponse(
    @field:Json(name = "success") val success: Boolean,
    @field:Json(name = "message") val message: String? = null,
    @field:Json(name = "syncedAt") val syncedAt: String? = null
)
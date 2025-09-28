package com.example.bundlemaker2.data.remote.model

import androidx.annotation.Keep

/**
 * API Request model for bulk mapping submission.
 * 
 * This class represents a bulk mapping request, containing a unique request ID, 
 * device identifier, manufacturing ID, and a list of serial items to be mapped.
 * 
 * @property requestId Unique identifier for the request (UUID)
 * @property deviceId Device identifier
 * @property mfgId Manufacturing ID
 * @property items List of serial items to be mapped
 */
@Keep
data class MappingsBulkRequest(
    val requestId: String,
    val deviceId: String,
    val mfgId: String,
    val items: List<SerialItem>
)

/**
 * Represents a single serial item in the bulk mapping request.
 * 
 * This class contains the unique serial number and the ISO 8601 formatted 
 * timestamp when the item was scanned.
 * 
 * @property serialId Unique serial number
 * @property scannedAt ISO 8601 formatted timestamp when the item was scanned
 */
@Keep
data class SerialItem(
    val serialId: String,
    val scannedAt: String // ISO 8601 format: "2025-09-19T12:34:56Z"
)
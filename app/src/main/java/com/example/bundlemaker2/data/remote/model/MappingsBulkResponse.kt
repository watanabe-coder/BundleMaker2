package com.example.bundlemaker2.data.remote.model

/**
 * API Response model for bulk mapping submission
 * @property ok Indicates if the overall operation was successful
 * @property mfgId The manufacturing ID this response is for
 * @property accepted Number of successfully processed items
 * @property rejected List of items that were rejected with reasons
 */
data class MappingsBulkResponse(
    val ok: Boolean,
    val mfgId: String,
    val accepted: Int,
    val rejected: List<RejectedItem>
)

/**
 * Represents a rejected item in the bulk mapping response
 * @property serialId The serial number that was rejected
 * @property code Error code indicating the reason for rejection
 * @property message Human-readable error message
 */
data class RejectedItem(
    val serialId: String,
    val code: String,
    val message: String
)

/**
 * Common error codes for rejected items
 */
object ErrorCodes {
    const val DUPLICATE = "DUPLICATE"
    const val FORMAT_ERROR = "FORMAT_ERROR"
    const val POLICY_VIOLATION = "POLICY_VIOLATION"
    const val AUTH_INVALID = "AUTH_INVALID"
    const val AUTH_EXPIRED = "AUTH_EXPIRED"
    const val NETWORK_ERROR = "NETWORK_ERROR"
}
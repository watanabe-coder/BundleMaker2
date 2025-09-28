// RemoteRepositoryImpl.kt
package com.example.bundlemaker2.domain.repository

import com.example.bundlemaker2.data.remote.api.MfgSerialApi
import com.example.bundlemaker2.data.remote.model.MappingsBulkRequest
import com.example.bundlemaker2.data.remote.model.MappingsBulkResponse
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val api: MfgSerialApi
) : MfgSerialRemoteRepository {
    
    override suspend fun sendMappings(token: String, request: MappingsBulkRequest): Result<MappingsBulkResponse> {
        return try {
            val response = api.postMappingsBulk(token, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMappingsByStatus(
        token: String,
        status: String
    ): Result<List<MappingsBulkResponse>> {
        return try {
            val response = api.getMappingsByStatus(token, status)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it)
                } ?: Result.success(emptyList())
            } else {
                Result.failure(Exception("Failed to fetch mappings by status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.model.api.MappingsBulkRequest
import com.example.bundlemaker2.data.model.api.MappingsBulkResponse

interface RemoteRepository {
    suspend fun sendMappingsBulk(token: String, request: MappingsBulkRequest): Result<MappingsBulkResponse>
}
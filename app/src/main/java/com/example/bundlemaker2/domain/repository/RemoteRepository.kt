package com.example.bundlemaker2.domain.repository

import com.example.bundlemaker2.data.remote.model.MappingsBulkRequest
import com.example.bundlemaker2.data.remote.model.MappingsBulkResponse

interface RemoteRepository {
    suspend fun sendMappingsBulk(token: String, request: MappingsBulkRequest): Result<MappingsBulkResponse>
}
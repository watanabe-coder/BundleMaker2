package com.example.bundlemaker2.data.api

import com.example.bundlemaker2.data.api.dto.SyncRequest
import com.example.bundlemaker2.data.api.dto.SyncResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SyncService {
    @POST("api/sync/mfg-serial")
    suspend fun syncMfgSerials(@Body request: SyncRequest): Response<SyncResponse>
}
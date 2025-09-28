package com.example.bundlemaker2.data.remote.api

import com.example.bundlemaker2.data.model.api.MappingsBulkRequest
import com.example.bundlemaker2.data.model.api.MappingsBulkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MfgSerialApi {
    @POST("/api/v1/mappings/bulk")
    suspend fun postMappingsBulk(
        @Header("Authorization") token: String,
        @Body request: MappingsBulkRequest
    ): Response<MappingsBulkResponse>
    
    @GET("/api/v1/mappings")
    suspend fun getMappingsByStatus(
        @Header("Authorization") token: String,
        @Query("status") status: String
    ): Response<List<MappingsBulkResponse>>
}
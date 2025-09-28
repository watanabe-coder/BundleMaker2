package com.example.bundlemaker2.data.remote.api

import com.example.bundlemaker2.data.model.api.MappingsBulkRequest
import com.example.bundlemaker2.data.model.api.MappingsBulkResponse
import com.example.bundlemaker2.data.model.api.RejectedItem
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

class MfgSerialApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MfgSerialApi
    private val gson = Gson()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MfgSerialApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `postMappingsBulk should return success response`() = runBlocking {
        // Given
        val request = MappingsBulkRequest(
            requestId = "test-request-id",
            deviceId = "test-device-id",
            mfgId = "test-mfg-id",
            items = listOf()
        )

        val expectedResponse = MappingsBulkResponse(
            ok = true,
            mfgId = "test-mfg-id",
            accepted = 1,
            rejected = emptyList()
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(expectedResponse))
        )

        // When
        val response = api.postMappingsBulk("test-token", request)

        // Then
        assertTrue(response.isSuccessful)
        assertEquals(expectedResponse, response.body())

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/api/v1/mappings/bulk", recordedRequest.path)
        assertEquals("Bearer test-token", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun `getMappingsByStatus should return list of mappings`() = runBlocking {
        // Given
        val expectedResponse = listOf(
            MappingsBulkResponse(
                ok = true,
                mfgId = "test-mfg-1",
                accepted = 1,
                rejected = emptyList()
            ),
            MappingsBulkResponse(
                ok = true,
                mfgId = "test-mfg-2",
                accepted = 2,
                rejected = listOf(RejectedItem("serial-1", "DUPLICATE"))
            )
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(expectedResponse))
        )

        // When
        val response = api.getMappingsByStatus("test-token", "PENDING")

        // Then
        assertTrue(response.isSuccessful)
        assertEquals(expectedResponse, response.body())

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/v1/mappings?status=PENDING", recordedRequest.requestUrl?.encodedPath + "?" + recordedRequest.requestUrl?.query)
        assertEquals("Bearer test-token", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun `postMappingsBulk should handle error response`() = runBlocking {
        // Given
        mockWebServer.enqueue(
            MockResponse().setResponseCode(400)
        )

        // When
        val response = api.postMappingsBulk(
            "test-token",
            MappingsBulkRequest("id", "device", "mfg", emptyList())
        )

        // Then
        assertFalse(response.isSuccessful)
        assertEquals(400, response.code())
    }
}

// Helper function to read JSON file from resources
private fun getJson(path: String): String {
    val inputStream = javaClass.classLoader?.getResourceAsStream(path)
    return inputStream?.source()?.buffer()?.readString(StandardCharsets.UTF_8) ?: ""
}
package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.remote.api.MfgSerialApiService
import com.example.bundlemaker2.data.model.api.MappingsBulkRequest
import com.example.bundlemaker2.data.model.api.MappingsBulkResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * MfgSerialRemoteRepositoryの実装クラス
 */
class MfgSerialRemoteRepositoryImpl @Inject constructor(
    private val apiService: MfgSerialApiService
) : MfgSerialRemoteRepository {

    override suspend fun sendMappings(
        token: String,
        request: MappingsBulkRequest
    ): Result<MappingsBulkResponse> {
        return try {
            val response = apiService.sendMappingsBulk("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Result.success(body)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(
                    HttpException(response).also { exception ->
                        // エラーログを出力する場合はここに記述
                        // Log.e(TAG, "API Error: ${exception.message()}")
                    }
                )
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMappingsByStatus(
        token: String,
        status: String
    ): Result<List<MappingsBulkResponse>> {
        // 必要に応じて実装
        return Result.failure(NotImplementedError("getMappingsByStatus is not implemented yet"))
    }

    companion object {
        private const val TAG = "MfgSerialRemoteRepo"
    }
}

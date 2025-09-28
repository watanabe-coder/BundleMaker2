package com.example.bundlemaker2.data.remote.api

import com.example.bundlemaker2.data.remote.model.MappingsBulkRequest
import com.example.bundlemaker2.data.remote.model.MappingsBulkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 製造番号とシリアル番号のマッピングを行うためのAPIインターフェース
 */
interface MfgSerialApiService {

    /**
     * 製造番号とシリアル番号のマッピングを一括送信する
     *
     * @param token 認証用のBearerトークン
     * @param request マッピングリクエストデータ
     * @return マッピング処理結果のレスポンス
     */
    @POST("api/v1/mappings/bulk")
    suspend fun sendMappingsBulk(
        @Header("Authorization") token: String,
        @Body request: MappingsBulkRequest
    ): Response<MappingsBulkResponse>

    companion object {
        /**
         * ベースURL
         * 実際のアプリではBuildConfigや設定から取得することを推奨
         */
        const val BASE_URL = "https://your-api-base-url.com/"
    }
}

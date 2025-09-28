package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.model.api.MappingsBulkRequest
import com.example.bundlemaker2.data.model.api.MappingsBulkResponse

/**
 * 製造番号とシリアル番号のマッピングをサーバーと同期するためのリモートリポジトリ
 */
interface MfgSerialRemoteRepository {
    /**
     * マッピングデータをサーバーに送信する
     * @param token 認証トークン
     * @param request 送信するマッピングデータ
     * @return 送信結果
     */
    suspend fun sendMappings(
        token: String,
        request: MappingsBulkRequest
    ): Result<MappingsBulkResponse>

    /**
     * 指定されたステータスのマッピングを取得する
     * @param token 認証トークン
     * @param status 取得するマッピングのステータス
     * @return マッピングデータのリスト
     */
    suspend fun getMappingsByStatus(
        token: String,
        status: String
    ): Result<List<MappingsBulkResponse>>
}

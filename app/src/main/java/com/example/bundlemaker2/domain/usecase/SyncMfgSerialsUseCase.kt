package com.example.bundlemaker2.domain.usecase

import com.example.bundlemaker2.data.api.SyncService
import com.example.bundlemaker2.data.api.dto.SyncRequest
import com.example.bundlemaker2.data.mapper.SyncDataMapper
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Resultクラスを定義
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class SyncMfgSerialsUseCase @Inject constructor(
    private val repository: MfgSerialRepository,
    private val syncService: SyncService,
    private val mapper: SyncDataMapper
) {

    /**
     * 同期処理を実行します。
     * 1. 同期対象のデータを取得
     * 2. データをAPIリクエスト用の形式に変換
     * 3. APIに送信
     * 4. 結果に応じてステータスを更新
     */
    suspend operator fun invoke(): Result<SyncResult> = withContext(Dispatchers.IO) {
        try {
            // 1. 同期対象のデータを取得
            val readyMappings = repository.getReadyToSync()
            if (readyMappings.isEmpty()) {
                return@withContext Result.Success(SyncResult(0, 0, "同期対象のデータがありません"))
            }

            // 2. データをAPIリクエスト用の形式に変換
            val syncRequests = mapper.toSyncRequest(readyMappings)
            var successCount = 0
            val failedIds = mutableListOf<Long>()

            // 3. 各リクエストを順番に送信
            for (request in syncRequests) {
                try {
                    val response = syncService.syncMfgSerials(request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        // 同期成功したIDを記録
                        val syncedIds = readyMappings
                            .filter { it.mfgId == request.mfgId }
                            .map { it.id }

                        // 4. 同期済みとしてマーク
                        repository.updateStatus(syncedIds, MappingStatus.SYNCED)
                        successCount += syncedIds.size
                    } else {
                        // 失敗したIDを記録
                        val failedMappings = readyMappings.filter { it.mfgId == request.mfgId }
                        failedIds.addAll(failedMappings.map { it.id })
                    }
                } catch (e: Exception) {
                    // エラーが発生したリクエストのIDを記録
                    val failedMappings = readyMappings.filter { it.mfgId == request.mfgId }
                    failedIds.addAll(failedMappings.map { it.id })
                }
            }

            // 失敗したレコードのステータスを更新
            if (failedIds.isNotEmpty()) {
                repository.updateStatus(failedIds, MappingStatus.FAILED)
            }

            // 結果を返す
            val message = when {
                successCount > 0 && failedIds.isNotEmpty() -> "一部のデータの同期に失敗しました"
                successCount > 0 -> "${successCount}件のデータを同期しました"
                else -> "データの同期に失敗しました"
            }

            Result.Success(SyncResult(successCount, failedIds.size, message))

        } catch (e: HttpException) {
            Result.Error("サーバーエラーが発生しました: ${e.code()}")
        } catch (e: IOException) {
            Result.Error("ネットワーク接続に失敗しました")
        } catch (e: Exception) {
            Result.Error("予期せぬエラーが発生しました: ${e.message}")
        }
    }

    data class SyncResult(
        val successCount: Int,
        val failureCount: Int,
        val message: String
    )
}
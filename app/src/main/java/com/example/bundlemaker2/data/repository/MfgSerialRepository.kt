package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.entity.MappingStatus
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * 製造番号とシリアル番号のマッピングを管理するリポジトリインターフェース
 */
interface MfgSerialRepository {
    // マッピングを取得
    suspend fun getMapping(id: Long): MfgSerialMapping?
    
    // 製造番号とシリアル番号でマッピングを取得
    suspend fun getMapping(mfgId: String, serialId: String): MfgSerialMapping?
    
    // 製造番号に紐づくマッピングのリストを取得（Flow版）
    fun getMappingsByMfgId(mfgId: String): Flow<List<MfgSerialMapping>>
    
    // ステータスに基づいてマッピングを取得
    fun getMappingsByStatus(statuses: List<MappingStatus>): Flow<List<MfgSerialMapping>>
    
    // マッピングを追加
    suspend fun addMapping(mfgId: String, serialId: String, scannedAt: Instant = Instant.now()): Result<Long>
    
    // マッピングを更新
    suspend fun updateMapping(mapping: MfgSerialMapping): Result<Unit>
    
    // マッピングを削除
    suspend fun deleteMapping(mapping: MfgSerialMapping): Result<Unit>
    
    // ステータスを更新
    suspend fun updateStatus(id: Long, status: MappingStatus, errorCode: String? = null): Result<Unit>
    
    // 指定したステータスのマッピング数を取得
    fun countByStatus(status: MappingStatus): Flow<Int>
    
    // 全マッピング数を取得
    suspend fun count(): Int
}

package com.example.bundlemaker2.data.mapper

import com.example.bundlemaker2.data.api.dto.SerialItemDto
import com.example.bundlemaker2.data.api.dto.SyncRequest
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncDataMapper @Inject constructor() {

    /**
     * 同期用のリクエストDTOに変換します。
     * 製造番号ごとにグループ化されたマッピングを1つのリクエストに変換します。
     */
    fun toSyncRequest(mappings: List<MfgSerialMapping>): List<SyncRequest> {
        return mappings
            .groupBy { it.mfgId }
            .map { (mfgId, mappingsForMfg) ->
                SyncRequest(
                    mfgId = mfgId,
                    serials = mappingsForMfg.map { mapping ->
                        SerialItemDto(
                            serialId = mapping.serialId,
                            scannedAt = mapping.scannedAt.toString() // ISO-8601形式でフォーマット
                        )
                    }
                )
            }
    }

    /**
     * 同期に使用するためのフィルタリングを行います。
     * ステータスが READY のマッピングのみを返します。
     */
    fun filterReadyMappings(mappings: List<MfgSerialMapping>): List<MfgSerialMapping> {
        return mappings.filter { it.status == MappingStatus.READY }
    }
}
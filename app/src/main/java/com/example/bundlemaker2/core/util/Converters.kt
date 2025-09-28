package com.example.bundlemaker2.core.util

import androidx.room.TypeConverter
import com.example.bundlemaker2.data.local.entity.MappingStatus
import java.time.Instant

/**
 * Room の型コンバーター
 * データベースに保存する型と、アプリで使用する型の変換を行う
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromMappingStatus(value: String?): MappingStatus? {
        return value?.let { MappingStatus.valueOf(it) }
    }

    @TypeConverter
    fun toMappingStatus(status: MappingStatus?): String? {
        return status?.name
    }
}

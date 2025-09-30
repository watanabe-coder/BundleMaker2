package com.example.bundlemaker2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity

@Database(
    entities = [
        MfgSerialMappingEntity::class,
        // 他のエンティティがあればここに追加
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mfgSerialDao(): MfgSerialDao
}
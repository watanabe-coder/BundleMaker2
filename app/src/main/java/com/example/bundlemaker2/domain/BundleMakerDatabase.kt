package com.example.bundlemaker2.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bundlemaker2.data.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.data.dao.MfgSerialMappingDao

@Database(
    entities = [MfgSerialMappingEntity::class],
    version = 1
)
abstract class BundleMakerDatabase : RoomDatabase() {
    abstract fun mfgSerialMappingDao(): MfgSerialMappingDao
}
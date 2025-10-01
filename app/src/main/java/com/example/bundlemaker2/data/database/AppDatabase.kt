package com.example.bundlemaker2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity

@Database(
    entities = [MfgSerialMappingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mfgSerialDao(): MfgSerialDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bundle_maker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
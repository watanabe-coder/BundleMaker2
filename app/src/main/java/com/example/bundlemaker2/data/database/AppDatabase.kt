package com.example.bundlemaker2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bundlemaker2.core.util.Converters
import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.local.dao.WorkSessionDao
import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.data.local.entity.WorkSessionEntity

@Database(
    entities = [
        MfgSerialMappingEntity::class,
        WorkSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mfgSerialMappingDao(): MfgSerialMappingDao
    abstract fun workSessionDao(): WorkSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bundle_maker_db"
                )
                    .fallbackToDestructiveMigration() // 開発中は簡易的に追加
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
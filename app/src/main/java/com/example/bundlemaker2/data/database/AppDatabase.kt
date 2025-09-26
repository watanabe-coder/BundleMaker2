package com.example.bundlemaker2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bundlemaker2.data.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.dao.WorkSessionDao
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.data.entity.WorkSession
import com.example.bundlemaker2.util.Converters

@Database(
    entities = [
        MfgSerialMapping::class,
        WorkSession::class
    ],
    version = 1,
    exportSchema = true
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
                .fallbackToDestructiveMigration() // マイグレーションが設定されていない場合は再作成
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}

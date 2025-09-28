package com.example.bundlemaker2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bundlemaker2.data.local.dao.WorkSessionDao
import com.example.bundlemaker2.data.local.dao.OutboxDao
import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.local.entity.MfgSerialMapping
import com.example.bundlemaker2.data.local.entity.WorkSession
import com.example.bundlemaker2.data.local.entity.Outbox
import com.example.bundlemaker2.core.util.Converters

@Database(
    entities = [
        WorkSession::class,
        Outbox::class,
        MfgSerialMapping::class  // 追加
    ],
    version = 3,  // バージョンを1つ上げる
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workSessionDao(): WorkSessionDao
    abstract fun outboxDao(): OutboxDao
    abstract fun mfgSerialMappingDao(): MfgSerialMappingDao  // 追加

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
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
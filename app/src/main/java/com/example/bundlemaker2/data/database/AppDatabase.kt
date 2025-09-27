package com.example.bundlemaker2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bundlemaker2.data.dao.WorkSessionDao
import com.example.bundlemaker2.data.dao.OutboxDao
import com.example.bundlemaker2.data.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.data.entity.WorkSession
import com.example.bundlemaker2.data.entity.Outbox
import com.example.bundlemaker2.util.Converters

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
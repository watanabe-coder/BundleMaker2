package com.example.bundlemaker2.di

import android.content.Context
import androidx.room.Room
import com.example.bundlemaker2.data.local.BundleMakerDatabase
import com.example.bundlemaker2.data.repository.MfgSerialMappingRepositoryImpl
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BundleMakerDatabase {
        return Room.databaseBuilder(
            context,
            BundleMakerDatabase::class.java,
            "bundle_maker_db"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideMfgSerialMappingDao(database: BundleMakerDatabase) = database.mfgSerialMappingDao()
    
    @Provides
    @Singleton
    fun provideMfgSerialMappingRepository(
        dao: com.example.bundlemaker2.domain.dao.MfgSerialMappingDao
    ): MfgSerialMappingRepository {
        return MfgSerialMappingRepositoryImpl(dao)
    }
}

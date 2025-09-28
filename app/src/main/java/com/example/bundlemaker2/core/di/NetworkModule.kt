package com.example.bundlemaker2.core.di

import com.example.bundlemaker2.BuildConfig
import com.example.bundlemaker2.data.remote.api.MfgSerialApi
import com.example.bundlemaker2.data.remote.api.MfgSerialApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * ネットワーク関連の依存性を提供するHiltモジュール
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_SECONDS = 30L

    /**
     * Gsonのシングルトンインスタンスを提供
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    /**
     * ロギングインターセプターを提供
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * OkHttpClientのインスタンスを提供
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Retrofitのインスタンスを提供
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MfgSerialApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * MfgSerialApiServiceのインスタンスを提供
     */
    @Provides
    @Singleton
    fun provideMfgSerialApiService(retrofit: Retrofit): MfgSerialApiService {
        return retrofit.create(MfgSerialApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMfgSerialApi(): MfgSerialApi {
        // Retrofitビルダーで生成
        return Retrofit.Builder()
            .baseUrl("https://your.api.endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MfgSerialApi::class.java)
    }
}

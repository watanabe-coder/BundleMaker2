package com.example.bundlemaker2.data.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            // 必要に応じて認証ヘッダーを追加
            // addHeader("Authorization", "Bearer $token")
        }.build()
        return chain.proceed(request)
    }
}
package com.example.bundlemaker2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BundleMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // アプリケーションの初期化処理
    }
}
package com.example.bundlemaker2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BundleMakerApplication : Application() {
    // 必要に応じて初期化処理を追加できます
    // 例: ロギングライブラリの初期化など
    override fun onCreate() {
        super.onCreate()
        // アプリ起動時の初期化処理をここに記述
    }
}
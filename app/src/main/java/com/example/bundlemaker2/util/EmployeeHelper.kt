package com.example.bundlemaker2.util

import android.content.Context
import android.util.Log
import com.example.bundlemaker2.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object EmployeeHelper {
    private const val TAG = "EmployeeHelper"
    private val employeeMap = mutableMapOf<String, String>()

    /**
     * 従業員データをCSVファイルから読み込む
     */
    fun initialize(context: Context) {
        try {
            context.assets.open("employees.csv").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                    // ヘッダー行をスキップ
                    reader.readLine()
                    
                    // 各行を処理
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val parts = line?.split(",")
                        if (parts?.size == 2) {
                            employeeMap[parts[0]] = parts[1]
                        }
                    }
                }
            }
            Log.d(TAG, "Loaded ${employeeMap.size} employees")
        } catch (e: IOException) {
            Log.e(TAG, "Error reading employees.csv", e)
        }
    }

    /**
     * ユーザーコードからユーザー名を取得する
     * @param userCode ユーザーコード
     * @return ユーザー名（見つからない場合はデフォルト値を返す）
     */
    fun getUserName(userCode: String?): String {
        if (userCode.isNullOrBlank()) {
            return "不明なユーザー"
        }
        return employeeMap[userCode] ?: "ユーザー: $userCode"
    }
}

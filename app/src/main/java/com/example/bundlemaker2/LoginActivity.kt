package com.example.bundlemaker2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.collections.forEach

class LoginActivity : AppCompatActivity() {
    private lateinit var userMap: Map<String, String>

    private fun loadUserMapFromAssets(): Map<String, String> {
        val userMap = mutableMapOf<String, String>()
        assets.open("employees.csv").bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.size >= 2) {
                    val code = parts[0].trim()
                    val name = parts[1].trim()
                    userMap[code] = name
                }
            }
        }
        return userMap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // assets/employees.csv からユーザー情報を読み込む
        userMap = loadUserMapFromAssets()

        val userIdEdit = findViewById<EditText>(R.id.userIdEdit)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorText = findViewById<TextView>(R.id.errorText)

        loginButton.setOnClickListener {
            val userId = userIdEdit.text.toString().trim()
            if (userId.isBlank()) {
                errorText.text = "ユーザーIDを入力してください"
                errorText.visibility = TextView.VISIBLE
            } else if (userMap.containsKey(userId)) {
                errorText.visibility = TextView.GONE
                val userName = userMap[userId] ?: ""
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("EXTRA_USER_NAME", userName)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            } else {
                errorText.text = "ユーザーIDが見つかりません"
                errorText.visibility = TextView.VISIBLE
            }
        }
    }
}
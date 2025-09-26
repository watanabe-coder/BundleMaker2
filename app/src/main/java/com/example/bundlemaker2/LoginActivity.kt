package com.example.bundlemaker2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var userCodes: Set<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // assets/employee.csv から user_code を読み込む
        userCodes = loadUserCodesFromAssets()

        val userIdEdit = findViewById<EditText>(R.id.userIdEdit)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorText = findViewById<TextView>(R.id.errorText)

        loginButton.setOnClickListener {
            val userId = userIdEdit.text.toString().trim()
            if (userId.isBlank()) {
                errorText.text = "ユーザーIDを入力してください"
                errorText.visibility = TextView.VISIBLE
            } else if (userCodes.contains(userId)) {
                errorText.visibility = TextView.GONE
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                errorText.text = "ユーザーIDが見つかりません"
                errorText.visibility = TextView.VISIBLE
            }
        }
    }

    private fun loadUserCodesFromAssets(): Set<String> {
        val codes = mutableSetOf<String>()
        assets.open("employees.csv").bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.isNotEmpty()) {
                    codes.add(parts[0].trim()) // 1列目が user_code の場合
                }
            }
        }
        return codes
    }
}
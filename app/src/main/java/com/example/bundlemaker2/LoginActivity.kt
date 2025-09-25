package com.example.bundlemaker2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userIdEdit = findViewById<EditText>(R.id.userIdEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorText = findViewById<TextView>(R.id.errorText)

        loginButton.setOnClickListener {
            val userId = userIdEdit.text.toString()
            val password = passwordEdit.text.toString()
            if (userId.isBlank() || password.isBlank()) {
                errorText.text = "ユーザーIDとパスワードを入力してください"
                errorText.visibility = TextView.VISIBLE
                return@setOnClickListener
            }
            // 仮認証処理（本番はAPI連携）
            if (userId == "admin" && password == "password") {
                errorText.visibility = TextView.GONE
                // メイン画面へ遷移
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                errorText.text = "認証に失敗しました"
                errorText.visibility = TextView.VISIBLE
            }
        }
    }
}
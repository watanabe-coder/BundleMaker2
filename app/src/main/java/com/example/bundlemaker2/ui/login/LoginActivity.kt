package com.example.bundlemaker2.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bundlemaker2.ui.main.MainActivity
import com.example.bundlemaker2.R

class LoginActivity : AppCompatActivity() {
    private lateinit var userMap: Map<String, String>
    private lateinit var userIdEdit: EditText
    private lateinit var errorText: TextView

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

        // キーボードを表示しないように設定
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        // assets/employees.csv からユーザー情報を読み込む
        userMap = loadUserMapFromAssets()

        userIdEdit = findViewById(R.id.userIdEdit)
        val loginButton = findViewById<Button>(R.id.loginButton)
        errorText = findViewById(R.id.errorText)

        // EditTextをクリックしたときの処理
        userIdEdit.setOnClickListener {
            // キーボードは表示されないが、フォーカスは持たせる
            it.clearFocus()
            it.requestFocus()
        }

        // ログインボタンのクリックリスナー
        loginButton.setOnClickListener {
            val userId = userIdEdit.text.toString().trim()
            attemptLogin(userId)
        }
    }

    private fun attemptLogin(userId: String) {
        if (userId.isBlank()) {
            showError("ユーザーIDを入力してください")
        } else if (userMap.containsKey(userId)) {
            errorText.visibility = View.GONE
            val userName = userMap[userId] ?: ""
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_USER_NAME", userName)
                Intent.setFlags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        } else {
            showError("ユーザーIDが見つかりません")
        }
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE
    }
}
package com.example.bundlemaker2.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bundlemaker2.R
import com.example.bundlemaker2.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userIdEdit = findViewById<EditText>(R.id.userIdEdit)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorText = findViewById<TextView>(R.id.errorText)

        loginButton.setOnClickListener {
            val userCode = userIdEdit.text.toString().trim()
            if (userCode.isEmpty()) {
                errorText.text = "ユーザーIDを入力してください"
                errorText.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            if (isUserCodeValid(userCode)) {
                errorText.visibility = TextView.GONE
                Toast.makeText(this, "ログイン成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                errorText.text = "ユーザーIDが見つかりません"
                errorText.visibility = TextView.VISIBLE
            }
        }
    }

    private fun isUserCodeValid(userCode: String): Boolean {
        return try {
            val inputStream = assets.open("employees.csv")
            inputStream.bufferedReader().useLines { lines ->
                lines.any { line ->
                    val code = line.split(",").firstOrNull()?.trim()
                    code == userCode
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}
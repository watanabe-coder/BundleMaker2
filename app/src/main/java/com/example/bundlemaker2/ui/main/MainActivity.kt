package com.example.bundlemaker2.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bundlemaker2.R
import com.example.bundlemaker2.ui.common.ScanInputDialog
import com.example.bundlemaker2.ui.login.LoginActivity

// Constants for intent extras
private const val EXTRA_MFG_ID = "extra_mfg_id"
private const val EXTRA_SERIAL_IDS = "extra_serial_ids"
private const val EXTRA_CONFIRMED_SERIAL_IDS = "extra_confirmed_serial_ids"

class MainActivity : AppCompatActivity() {
    private var currentMfgId: String = ""
    private val serialIds = mutableListOf<String>()
    private var isBundleMode = false

    private fun handleConfirmedSerials(confirmedSerials: ArrayList<String>?) {
        confirmedSerials?.let {
            showToast("${it.size}件のシリアル番号を確定しました")
            serialIds.clear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val userName = intent.getStringExtra("EXTRA_USER_NAME") ?: ""
        val workerTextView = findViewById<TextView>(R.id.workerInfoText)
        workerTextView.text = "作業者：$userName"

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply padding to the root view
            findViewById<View>(android.R.id.content).setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Set up button click listeners
        findViewById<View>(R.id.bundleButton).setOnClickListener {
            isBundleMode = true
            showScanInputDialog(
                title = getString(R.string.bundle_manufacturing_number),
                hint = getString(R.string.input_manufacturer_id_hint)
            ) { input ->
                currentMfgId = input
                showToast("製造番号が設定されました: $input")
                // シリアル番号入力に進む
                showScanInputDialog(
                    title = getString(R.string.input_serial_number),
                    hint = getString(R.string.input_serial_number_hint)
                ) { serial ->
                    serialIds.add(serial)
                    showToast("シリアル番号が追加されました: $serial")
                }
            }
        }

        findViewById<View>(R.id.unitButton).setOnClickListener {
            isBundleMode = false
            showScanInputDialog(
                title = getString(R.string.unit_serial_number),
                hint = getString(R.string.input_serial_number_hint)
            ) { input ->
                currentMfgId = "" // ユニットモードでは製造番号は不要
                serialIds.clear()
                serialIds.add(input)
                showToast("ユニットシリアル番号が設定されました: $input")
            }
        }

        findViewById<View>(R.id.confirmButton).setOnClickListener {
            if (currentMfgId.isBlank() || serialIds.isEmpty()) {
                showToast("製造番号とシリアル番号を入力してください")
                return@setOnClickListener
            }
            navigateToConfirm()
        }

        // Set up menu and refresh button click listeners
        findViewById<View>(R.id.menuButton).setOnClickListener { view ->
            showPopupMenu(view)
        }

        findViewById<View>(R.id.refreshButton).setOnClickListener {
            // TODO: Implement refresh button click action
            showToast("Refreshing...")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToConfirm() {
        // In the minimal version, we'll just show a toast
        showToast("${serialIds.size}件のシリアル番号を確認しました")
        serialIds.clear()
    }

    private fun showScanInputDialog(
        title: String,
        hint: String,
        onInput: (String) -> Unit
    ) {
        val dialog = ScanInputDialog(title, hint) { input ->
            onInput(input)
        }
        dialog.show(supportFragmentManager, ScanInputDialog.Companion.TAG)
    }

    private fun showPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        // メニューアイテムが選択されたときの処理
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    // 設定画面への遷移処理（未実装）
                    showToast("設定画面を表示します")
                    true
                }
                R.id.action_logout -> {
                    // ログアウト処理
                    showToast("ログアウトします")
                    // ログイン画面に遷移
                    val intent = Intent(this@MainActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        // メニューを表示
        popupMenu.show()
    }
}
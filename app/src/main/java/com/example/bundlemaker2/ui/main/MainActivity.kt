package com.example.bundlemaker2.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bundlemaker2.R
import com.example.bundlemaker2.ui.common.ScanInputDialog
import com.example.bundlemaker2.ui.login.LoginActivity
import com.example.bundlemaker2.util.EmployeeHelper

// Constants for intent extras
private const val EXTRA_MFG_ID = "extra_mfg_id"
private const val EXTRA_SERIAL_IDS = "extra_serial_ids"
private const val EXTRA_CONFIRMED_SERIAL_IDS = "extra_confirmed_serial_ids"
private const val REQUEST_CODE_CONFIRM = 1001

class MainActivity : AppCompatActivity() {
    private var currentMfgId: String = ""
    private val serialIds = mutableListOf<String>()
    private var isBundleMode = false
    private var isWaitingForSerials = false

    private fun handleConfirmedSerials(confirmedSerials: ArrayList<String>?) {
        confirmedSerials?.let {
            showToast("${it.size}件のシリアル番号を確定しました")
            // 確定後の処理（必要に応じてデータベースへの保存などを行う）
            saveConfirmedSerials(it)
            // 状態をリセット
            currentMfgId = ""
            serialIds.clear()
        }
    }

    private fun saveConfirmedSerials(serials: List<String>) {
        // TODO: データベースに保存する処理を実装
        // 例: repository.saveConfirmedSerials(currentMfgId, serials)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize EmployeeHelper
        EmployeeHelper.initialize(applicationContext)
        
        val userCode = intent.getStringExtra("EXTRA_USER_CODE") ?: ""
        val userName = EmployeeHelper.getUserName(userCode)
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
            isWaitingForSerials = false
            showScanInputDialog(
                title = getString(R.string.bundle_manufacturing_number),
                hint = getString(R.string.input_manufacturer_id_hint),
                onInput = { input ->
                    currentMfgId = input
                    isWaitingForSerials = true
                    showToast("製造番号が設定されました: $input")
                    // シリアル番号入力に進む
                    showNextSerialInputDialog()
                },
                showCancel = false,
                onCancel = null
            )
        }

        findViewById<View>(R.id.unitButton).setOnClickListener {
            isBundleMode = false
            showScanInputDialog(
                title = getString(R.string.unit_serial_number),
                hint = getString(R.string.input_serial_number_hint),
                onInput = { input ->
                    currentMfgId = "" // ユニットモードでは製造番号は不要
                    serialIds.clear()
                    serialIds.add(input)
                    showToast("ユニットシリアル番号が設定されました: $input")
                },
                showCancel = false,
                onCancel = null
            )
        }

        findViewById<View>(R.id.confirmButton).setOnClickListener {
            if (!isWaitingForSerials && serialIds.isEmpty()) {
                showToast("シリアル番号が入力されていません")
                return@setOnClickListener
            }
            if (isBundleMode && currentMfgId.isBlank()) {
                showToast("製造番号が設定されていません")
                return@setOnClickListener
            }
            if (serialIds.isNotEmpty()) {
                navigateToConfirm()
            } else {
                showToast("シリアル番号を入力してください")
            }
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

    private val confirmActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val confirmedSerials = result.data?.getStringArrayListExtra(EXTRA_CONFIRMED_SERIAL_IDS)
                handleConfirmedSerials(confirmedSerials)
            }
            Activity.RESULT_CANCELED -> {
                // 確認画面でキャンセルされた場合の処理
                showToast("キャンセルされました")
            }
        }
    }

    private fun navigateToConfirm() {
        if (currentMfgId.isBlank()) {
            showToast("製造番号が設定されていません")
            return
        }
        if (serialIds.isEmpty()) {
            showToast("シリアル番号が入力されていません")
            return
        }

        // TODO: ConfirmActivityが実装されたら有効化する
        // val intent = Intent(this, ConfirmActivity::class.java).apply {
        //     putExtra(EXTRA_MFG_ID, currentMfgId)
        //     putStringArrayListExtra(EXTRA_SERIAL_IDS, ArrayList(serialIds))
        // }
        // confirmActivityLauncher.launch(intent)
        
        // 暫定実装: ConfirmActivityが実装されるまでトーストで代用
        showToast("${serialIds.size}件のシリアル番号を確認しました")
        handleConfirmedSerials(ArrayList(serialIds))
    }

    private fun showNextSerialInputDialog() {
        showScanInputDialog(
            title = getString(R.string.input_serial_number),
            hint = getString(R.string.input_serial_number_hint),
            onInput = { serial ->
                if (serial.isNotBlank()) {
                    serialIds.add(serial)
                    showToast("シリアル番号が追加されました: $serial (${serialIds.size}件)")
                    // 次の入力を待つ
                    showNextSerialInputDialog()
                }
            },
            showCancel = serialIds.isNotEmpty(),
            onCancel = {
                // キャンセルされたら確認に進む
                if (serialIds.isNotEmpty()) {
                    navigateToConfirm()
                } else {
                    showToast("シリアル番号が入力されていません")
                }
            }
        )
    }

    private fun showScanInputDialog(
        title: String,
        hint: String,
        onInput: (String) -> Unit,
        showCancel: Boolean = false,
        onCancel: (() -> Unit)? = null
    ) {
        val dialog = ScanInputDialog(title, hint, showCancel) { input, isCancelled ->
            if (isCancelled) {
                onCancel?.invoke()
            } else {
                onInput(input)
            }
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
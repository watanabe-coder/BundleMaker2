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
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.repository.MfgSerialRepository
import com.example.bundlemaker2.ui.common.ScanInputDialog
import com.example.bundlemaker2.ui.confirm.ConfirmActivity
import com.example.bundlemaker2.ui.login.LoginActivity
import com.example.bundlemaker2.util.EmployeeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.bundlemaker2.util.Constants.EXTRA_CONFIRMED_SERIAL_IDS
import com.example.bundlemaker2.util.Constants.EXTRA_MFG_ID
import com.example.bundlemaker2.util.Constants.EXTRA_SERIAL_IDS

class MainActivity : AppCompatActivity() {
    private var currentMfgId: String = ""
    private val serialIds = mutableListOf<String>()
    private var isBundleMode = false
    private var isWaitingForSerials = false

    private fun handleConfirmedSerials(confirmedSerials: ArrayList<String>?) {
        confirmedSerials?.let {
            if (it.isNotEmpty()) {
                showToast("${it.size}件のシリアル番号を確定しました")
                // データベースに保存
                saveConfirmedSerials(it)
                // 状態をリセット
                serialIds.clear()
                if (isBundleMode) {
                    currentMfgId = ""
                    isWaitingForSerials = false
                }
            }
        }
    }

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val mfgSerialDao by lazy { database.mfgSerialDao() }
    private val mfgSerialRepository by lazy { MfgSerialRepository(mfgSerialDao) }

    private fun saveConfirmedSerials(serials: List<String>) {
        if (serials.isEmpty()) return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = mfgSerialRepository.saveMfgSerials(currentMfgId, serials)
                withContext(Dispatchers.Main) {
                    if (success) {
                        showToast("${serials.size}件のシリアル番号を保存しました")
                    } else {
                        showToast("シリアル番号の保存に失敗しました")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("エラーが発生しました: ${e.message}")
                }
            }
        }
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
        if (isBundleMode && currentMfgId.isBlank()) {
            showToast("製造番号が設定されていません")
            return
        }
        if (serialIds.isEmpty()) {
            showToast("シリアル番号が入力されていません")
            return
        }

        // 重複チェック
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val duplicates = serialIds.filter { serialId ->
                    mfgSerialRepository.isSerialExists(serialId)
                }
                
                withContext(Dispatchers.Main) {
                    if (duplicates.isNotEmpty()) {
                        showToast("以下のシリアル番号は既に登録されています: ${duplicates.take(3).joinToString()}")
                        if (duplicates.size > 3) {
                            showToast("他に${duplicates.size - 3}件の重複があります")
                        }
                        return@withContext
                    }
                    
                    // 重複がなければ確認画面へ
                    val mfgIdToPass = if (isBundleMode) currentMfgId else ""
                    val intent = Intent(this@MainActivity, ConfirmActivity::class.java).apply {
                        putExtra(EXTRA_MFG_ID, mfgIdToPass)
                        putStringArrayListExtra(EXTRA_SERIAL_IDS, ArrayList(serialIds))
                    }
                    confirmActivityLauncher.launch(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("エラーが発生しました: ${e.message}")
                }
            }
        }
    }

    private fun showNextSerialInputDialog() {
        showScanInputDialog(
            title = getString(R.string.input_serial_number),
            hint = getString(R.string.input_serial_number_hint),
            onInput = { serial ->
                if (serial.isNotBlank()) {
                    // 重複チェック
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val isDuplicate = mfgSerialRepository.isSerialExists(serial)
                            withContext(Dispatchers.Main) {
                                if (isDuplicate) {
                                    showToast("このシリアル番号は既に登録されています: $serial")
                                } else {
                                    serialIds.add(serial)
                                    showToast("シリアル番号が追加されました: $serial (${serialIds.size}件)")
                                }
                                // 次の入力を待つ
                                showNextSerialInputDialog()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            withContext(Dispatchers.Main) {
                                showToast("エラーが発生しました: ${e.message}")
                            }
                        }
                    }
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
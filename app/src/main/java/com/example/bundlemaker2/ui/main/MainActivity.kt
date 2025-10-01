package com.example.bundlemaker2.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.bundlemaker2.R
import com.example.bundlemaker2.databinding.ActivityMainBinding
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.presentation.sync.SyncViewModel
import com.example.bundlemaker2.presentation.sync.SyncState
import com.example.bundlemaker2.ui.common.ScanInputDialog
import com.example.bundlemaker2.ui.confirm.ConfirmActivity
import com.example.bundlemaker2.ui.db.DbRecordsActivity
import com.example.bundlemaker2.ui.login.LoginActivity
import com.example.bundlemaker2.util.EmployeeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

import com.example.bundlemaker2.util.Constants.EXTRA_CONFIRMED_SERIAL_IDS
import com.example.bundlemaker2.util.Constants.EXTRA_MFG_ID
import com.example.bundlemaker2.util.Constants.EXTRA_SERIAL_IDS
import com.example.bundlemaker2.utils.LogUtils
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var currentMfgId: String = ""
    private val workerInfoText: TextView by lazy { binding.workerInfoText }
    // Track each serial with its manufacturing number
    private val serialEntries = mutableListOf<Pair<String, String>>() // Pair of (mfgId, serialId)
    private val syncViewModel: SyncViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()
    private var isBundleMode = false
    private var isWaitingForSerials = false

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mfgSerialRepository: MfgSerialRepository

    // 同期中かどうかを示すフラグ
    private var isSyncing = false

    private fun updateWorkerInfo(userName: String) {
        workerInfoText.text = getString(R.string.worker_info, userName)
    }
    
    private fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun handleConfirmedSerials(confirmedSerials: ArrayList<String>?) {
        confirmedSerials?.let { serials ->
            if (serials.isNotEmpty()) {
                showToast("${serials.size}件のシリアル番号を確定しました")
                
                // データベースに保存 (各シリアル番号は既に正しい製造番号と紐づいている)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // 型パラメータを明示的に指定
                        val serialsByMfgId = serialEntries.groupBy(
                            keySelector = { it.first },
                            valueTransform = { it.second }
                        )
                        var totalSaved = 0

                        // 各製造番号ごとの処理
                        for ((mfgId, serialsForMfg) in serialsByMfgId) {
                            val currentTime = Date()
                            val dataMappings = serialsForMfg.map { serialId ->
                                MfgSerialMapping(
                                    mfgId = mfgId,
                                    serialId = serialId,
                                    scannedAt = currentTime.toInstant(),
                                    status = MappingStatus.CONFIRMED
                                )
                            }

                            try {
                                mfgSerialRepository.insertAll(dataMappings)
                                totalSaved += dataMappings.size
                                Log.d("SaveConfirmedSerials", "Saved ${dataMappings.size} serials for mfgId: $mfgId")
                            } catch (e: Exception) {
                                Log.e("SaveConfirmedSerials", "Error saving serials for mfgId $mfgId", e)
                                withContext(Dispatchers.Main) {
                                    showToast("${mfgId}のシリアル番号の保存中にエラーが発生しました")
                                }
                                return@launch
                            }
                        }

                        withContext(Dispatchers.Main) {
                            showToast("${totalSaved}件のシリアル番号を保存しました")
                            // 状態をリセット
                            serialEntries.clear()
                            if (isBundleMode) {
                                currentMfgId = ""
                                isWaitingForSerials = false
                            }
                            
                            // メイン画面に遷移
                            val intent = Intent(this@MainActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            showToast("エラーが発生しました: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is logged in
        val sharedPref = getSharedPreferences("login_prefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        if (username.isNullOrEmpty()) {
            // If not logged in, start LoginActivity
            showLoginScreen()
            return
        }

        // Initialize EmployeeHelper
        EmployeeHelper.initialize(applicationContext)
        
        // Get user name from EmployeeHelper
        val userName = EmployeeHelper.getUserName(username)
        updateWorkerInfo(userName)

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
                showCancel = true,
                onCancel = {
                    // キャンセルされたらメイン画面に戻る
                    currentMfgId = ""
                    isWaitingForSerials = false
                    showToast("製造番号の入力をキャンセルしました")
                }
            )
        }

        findViewById<View>(R.id.unitButton).setOnClickListener {
            isBundleMode = false
            showScanInputDialog(
                title = getString(R.string.unit_serial_number),
                hint = getString(R.string.input_serial_number_hint),
                onInput = { input ->
                    // ユニットモードでは製造番号は不要（空文字で登録）
                    currentMfgId = ""
                    serialEntries.clear()
                    serialEntries.add("" to input) // 空の製造番号で登録
                    showToast("ユニットシリアル番号が設定されました: $input")
                },
                showCancel = false,
                onCancel = null
            )
        }

        findViewById<View>(R.id.confirmButton).setOnClickListener {
            if (isBundleMode) {
                // バンドルモードの場合は、現在の製造番号でシリアル番号が登録されているか確認
                val hasEntries = serialEntries.any { it.first == currentMfgId && it.first.isNotBlank() }
                if (!hasEntries) {
                    showToast("シリアル番号が入力されていません")
                    return@setOnClickListener
                }
            } else {
                // ユニットモードの場合は、製造番号が空のエントリを確認
                val hasUnitEntries = serialEntries.any { it.first.isEmpty() && it.second.isNotBlank() }
                if (!hasUnitEntries) {
                    showToast("シリアル番号が入力されていません")
                    return@setOnClickListener
                }
            }
            
            // 確認画面に遷移
            navigateToConfirm()
        }

        // Set up menu and refresh button click listeners
        findViewById<View>(R.id.menuButton).setOnClickListener { view ->
            showPopupMenu(view)
        }

        setupViews()
        observeSyncState()
    }

    private fun setupViews() {
        // リフレッシュボタンのクリックリスナーを設定
        findViewById<View>(R.id.refreshButton).setOnClickListener {
            if (!isSyncing) {
                startSync()
            }
        }
    }

    private fun startSync() {
        // 同期中フラグをセット
        isSyncing = true
        // ボタンを無効化（必要に応じてローディング表示なども追加可能）
        findViewById<View>(R.id.refreshButton).isEnabled = false
        
        // ここに同期処理を実装（後で実装）
        Toast.makeText(this, "同期を開始します...", Toast.LENGTH_SHORT).show()
        
        // テスト用に3秒後に同期完了をシミュレート
        findViewById<View>(R.id.refreshButton).postDelayed({
            isSyncing = false
            findViewById<View>(R.id.refreshButton).isEnabled = true
            Toast.makeText(this, "同期が完了しました", Toast.LENGTH_SHORT).show()
        }, 3000)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun showDuplicateConfirmDialog(duplicates: Set<String>, onConfirm: () -> Unit) {
        // 重複確認ダイアログを表示
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("重複するシリアル番号があります")
            .setMessage("以下のシリアル番号が重複しています:\n\n${duplicates.joinToString("\n")}\n\nこのまま続行しますか？")
            .setPositiveButton("続行") { _, _ -> onConfirm() }
            .setNegativeButton("キャンセル", null)
            .show()
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
        if (isBundleMode && currentMfgId.isBlank() && serialEntries.none { it.first.isNotBlank() }) {
            showToast("製造番号が設定されていません")
            return
        }
        
        // シリアル番号が1つもない場合はエラー
        if (serialEntries.isEmpty()) {
            showToast("シリアル番号が入力されていません")
            return
        }

        // 重複チェック
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allSerials = serialEntries.map { it.second }
                val duplicates = allSerials.groupBy { it }.filter { it.value.size > 1 }.keys
                
                withContext(Dispatchers.Main) {
                    if (duplicates.isNotEmpty()) {
                        // 重複がある場合は確認ダイアログを表示
                        showDuplicateConfirmDialog(duplicates) {
                            // 重複を承知で進む場合
                            launchConfirmActivity()
                        }
                    } else {
                        // 重複がない場合はそのまま確認画面へ
                        launchConfirmActivity()
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

    private fun launchConfirmActivity() {
        // 現在の製造番号でフィルタリング
        val currentSerials = if (isBundleMode && currentMfgId.isNotBlank()) {
            serialEntries.filter { it.first == currentMfgId }.map { it.second }
        } else {
            serialEntries.map { it.second }
        }
        
        val intent = Intent(this, ConfirmActivity::class.java).apply {
            // バンドルモードの場合は現在の製造番号を渡す
            if (isBundleMode && currentMfgId.isNotBlank()) {
                putExtra(EXTRA_MFG_ID, currentMfgId)
            }
            putStringArrayListExtra(EXTRA_SERIAL_IDS, ArrayList(currentSerials))
        }
        confirmActivityLauncher.launch(intent)
    }

    private fun showNextSerialInputDialog() {
        if (currentMfgId.isBlank()) {
            showToast("製造番号が設定されていません")
            return
        }
        
        // すでに1つ以上のシリアル番号が入力されている場合は「入力完了」ボタンを表示
        val hasEntries = serialEntries.any { it.first == currentMfgId }
        
        showScanInputDialog(
            title = getString(R.string.input_serial_number),
            hint = getString(R.string.input_serial_number_hint),
            onInput = { input ->
                if (input.isNotBlank()) {
                    // 現在の製造番号とシリアル番号のペアを追加
                    serialEntries.add(currentMfgId to input)
                    showToast("シリアル番号を追加しました: $input (製造番号: $currentMfgId)")
                    // 次のシリアル番号入力に進む（入力完了ボタン付き）
                    showNextSerialInputDialog()
                }
            },
            showCancel = true, // 常にキャンセルボタンを表示
            showFinishButton = hasEntries,
            onCancel = {
                // キャンセルボタンが押された場合、この製造番号で入力したシリアル番号をすべて削除
                val beforeCount = serialEntries.count { it.first == currentMfgId }
                serialEntries.removeAll { it.first == currentMfgId }
                val removedCount = beforeCount
                if (removedCount > 0) {
                    showToast("${currentMfgId}のシリアル番号${removedCount}件をキャンセルしました")
                } else {
                    showToast("キャンセルしました")
                }
                // 状態をリセット
                currentMfgId = ""
                isWaitingForSerials = false
            },
            onFinish = {
                // 入力完了ボタンが押された場合
                if (serialEntries.any { it.first == currentMfgId }) {
                    showToast("シリアル番号の入力を終了します")
                    navigateToConfirm()
                } else {
                    // シリアル番号が1つも入力されていない場合はエラーメッセージを表示
                    // ただし、ダイアログは閉じない
                    showToast("シリアル番号が入力されていません。シリアル番号を入力するか、「キャンセル」で入力を終了してください。")
                }
            }
        )
    }

    private fun showScanInputDialog(
        title: String,
        hint: String,
        onInput: (String) -> Unit,
        showCancel: Boolean = false,
        showFinishButton: Boolean = false,
        onCancel: (() -> Unit)? = null,
        onFinish: (() -> Unit)? = null
    ) {
        val dialog = ScanInputDialog(
            title = title,
            hint = hint,
            showCancel = showCancel,
            showFinishButton = showFinishButton,
            onInput = { input, isCancelled ->
                if (isCancelled) {
                    onCancel?.invoke()
                } else {
                    onInput(input)
                }
            },
            onFinish = onFinish
        )
        dialog.show(supportFragmentManager, ScanInputDialog.Companion.TAG)
    }

    // 同期状態を監視
    private fun observeSyncState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                syncViewModel.syncState.collect { state ->
                    when (state) {
                        is SyncState.Success -> {
                            showLoading(false)
                            val logMessage = "同期成功: ${state.message}, " +
                                    "処理件数: ${state.processedCount}, " +
                                    "成功: ${state.successCount}, " +
                                    "失敗: ${state.failedCount}"
                            LogUtils.logSyncEvent(this@MainActivity, logMessage)
                            showToast("同期が完了しました")
                            refreshData()
                        }
                        is SyncState.Error -> {
                            showLoading(false)
                            val logMessage = "同期エラー: ${state.message}"
                            LogUtils.logSyncEvent(this@MainActivity, logMessage)
                            showToast(logMessage)
                        }
                        is SyncState.Loading -> {
                            showLoading(true)
                            updateProgress(state.current, state.total)
                            if (state.current == 1) {
                                LogUtils.logSyncEvent(this@MainActivity, "同期を開始します...")
                            }
                        }
                        else -> {
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            // 必要に応じて他のUI要素の有効/無効を切り替え
            refreshButton.isEnabled = !show
        }
    }

    private fun updateProgress(current: Int, total: Int) {
        binding.progressText.text = "$current / $total"
    }
    
    // データを更新する関数
    private fun refreshData() {
        // ここにデータ更新のロジックを実装
        showToast("データを更新しました")
    }
    
    // メニューアイテム選択ハンドラ
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshButton -> {
                syncViewModel.syncState
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                R.id.action_view_db -> {
                    // データベースレコード画面に遷移
                    val intent = Intent(this@MainActivity, DbRecordsActivity::class.java)
                    startActivity(intent)
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

    private fun showLogs() {
        val logs = LogUtils.readLogs(this)
        AlertDialog.Builder(this)
            .setTitle("同期ログ")
            .setMessage(if (logs.isNotEmpty()) logs else "ログがありません")
            .setPositiveButton("閉じる", null)
            .setNeutralButton("クリア") { _, _ ->
                LogUtils.clearLogs(this)
                showToast("ログをクリアしました")
            }
            .show()
    }
}
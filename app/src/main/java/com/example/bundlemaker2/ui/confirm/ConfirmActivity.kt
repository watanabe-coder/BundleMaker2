package com.example.bundlemaker2.ui.confirm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bundlemaker2.R
import com.example.bundlemaker2.databinding.ActivityConfirmBinding
import com.example.bundlemaker2.util.Constants.EXTRA_CONFIRMED_SERIAL_IDS
import com.example.bundlemaker2.util.Constants.EXTRA_MFG_ID
import com.example.bundlemaker2.util.Constants.EXTRA_SERIAL_IDS

class ConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding
    private lateinit var adapter: ConfirmListAdapter
    private val serialIds = mutableListOf<String>()
    private var mfgId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // ツールバーの設定
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // インテントからデータを取得
        mfgId = intent.getStringExtra(EXTRA_MFG_ID) ?: ""
        val serials = intent.getStringArrayListExtra(EXTRA_SERIAL_IDS) ?: arrayListOf()
        serialIds.addAll(serials)

        // UIの初期化
        initViews()
        setupRecyclerView()
    }

    private fun initViews() {
        // 製造番号を表示
        binding.mfgIdText.text = mfgId
        // シリアル番号の件数を表示
        binding.serialCountText.text = resources.getQuantityString(
            R.plurals.serial_count, serialIds.size, serialIds.size
        )

        // 確定ボタンのクリックリスナー
        binding.confirmButton.setOnClickListener {
            confirmAndFinish()
        }

        // キャンセルボタンのクリックリスナー
        binding.cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = ConfirmListAdapter(serialIds.toList()) { position ->
            // アイテム削除時の処理
            serialIds.removeAt(position)
            adapter.updateList(serialIds.toList())
            // 件数を更新
            binding.serialCountText.text = resources.getQuantityString(
                R.plurals.serial_count, serialIds.size, serialIds.size
            )
            // アイテムが0件の場合は確定ボタンを無効化
            binding.confirmButton.isEnabled = serialIds.isNotEmpty()
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun confirmAndFinish() {
        if (serialIds.isEmpty()) {
            Toast.makeText(this, R.string.error_no_serials, Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent().apply {
            putExtra(EXTRA_CONFIRMED_SERIAL_IDS, ArrayList(serialIds))
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

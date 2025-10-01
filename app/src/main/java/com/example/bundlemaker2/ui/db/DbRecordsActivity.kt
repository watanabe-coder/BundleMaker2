package com.example.bundlemaker2.ui.db

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bundlemaker2.R
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.databinding.ActivityDbRecordsBinding
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

fun MfgSerialMapping.toDbRecordItem(dateFormat: SimpleDateFormat): DbRecordItem {
    return DbRecordItem(
        mfgId = mfgId,
        serialId = serialId,
        scannedAt = dateFormat.format(Date(scannedAt.toEpochMilli())),
        status = status.toString()
    )
}

@AndroidEntryPoint
class DbRecordsActivity : AppCompatActivity() {

    @Inject
    lateinit var mfgSerialRepository: MfgSerialRepository

    private lateinit var binding: ActivityDbRecordsBinding
    private lateinit var adapter: DbRecordsAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupDeleteButton()
        loadData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "登録済みレコード一覧"
    }

    private fun setupRecyclerView() {
        adapter = DbRecordsAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DbRecordsActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@DbRecordsActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            this.adapter = this@DbRecordsActivity.adapter
        }
    }

    private fun setupDeleteButton() {
        binding.deleteAllButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_all_records)
            .setMessage(R.string.delete_all_records_confirmation)
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteAllRecords()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun deleteAllRecords() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                // Get all records first
                val allMappings = mfgSerialRepository.getAllMappings()
                // Delete each record one by one since we don't have a deleteAll method
                allMappings.forEach { mapping ->
                    mfgSerialRepository.delete(mapping)
                }
                loadData()
                Toast.makeText(
                    this@DbRecordsActivity,
                    R.string.all_records_deleted,
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@DbRecordsActivity,
                    R.string.error_deleting_records,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun loadData() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val records = mfgSerialRepository.getAllMappings()
                if (records.isEmpty()) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                    adapter.submitList(records.map { it.toDbRecordItem(dateFormat) })
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@DbRecordsActivity,
                    R.string.error_loading_records,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(isEmpty: Boolean) {
        // You can add an empty state view here if needed
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

data class DbRecordItem(
    val mfgId: String,
    val serialId: String,
    val scannedAt: String,
    val status: String
)

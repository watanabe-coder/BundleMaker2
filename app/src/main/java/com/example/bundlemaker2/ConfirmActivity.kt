package com.example.bundlemaker2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bundlemaker2.core.util.Constants
import com.google.android.material.button.MaterialButton

enum class Action { EDIT, DELETE }

class ConfirmActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MfgSerialAdapter
    private val mfgSerialList = mutableListOf<Pair<String, String>>()
    private lateinit var mfgId: String
    private val serialIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        // Set up toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.confirm_title)

        // Get data from intent
        mfgId = intent.getStringExtra(Constants.EXTRA_MFG_ID) ?: run {
            showToast("製造番号が指定されていません")
            finish()
            return
        }

        val serials = intent.getStringArrayListExtra(Constants.EXTRA_SERIAL_IDS)
        if (serials.isNullOrEmpty()) {
            showToast("シリアル番号が指定されていません")
            finish()
            return
        }
        serialIds.addAll(serials)

        // Initialize UI
        val mfgIdText = findViewById<TextView>(R.id.mfgIdText)
        mfgIdText.text = mfgId
        val serialCountText = findViewById<TextView>(R.id.serialCountText)
        serialCountText.text = "${serials.size}件"

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        mfgSerialList.addAll(serials.map { Pair(mfgId, it) })
        
        adapter = MfgSerialAdapter(mfgSerialList) { position, action ->
            when (action) {
                Action.EDIT -> editItem(position)
                Action.DELETE -> deleteItem(position)
                else -> { /* Do nothing */ }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Set up buttons
        findViewById<MaterialButton>(R.id.confirmButton).setOnClickListener {
            // Return confirmed serials to MainActivity
            val resultIntent = Intent().apply {
                putStringArrayListExtra(
                    Constants.EXTRA_CONFIRMED_SERIAL_IDS,
                    ArrayList(serialIds)
                )
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        findViewById<MaterialButton>(R.id.cancelButton).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
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

    private fun editItem(position: Int) {
        // TODO: Implement edit functionality
        val (mfg, serial) = mfgSerialList[position]
        showToast("編集: $mfg - $serial")
    }

    private fun deleteItem(position: Int) {
        if (position in 0 until mfgSerialList.size) {
            val (_, serial) = mfgSerialList[position]
            mfgSerialList.removeAt(position)
            serialIds.remove(serial)
            adapter.notifyItemRemoved(position)
            
            // Update count
            val serialCountText = findViewById<TextView>(R.id.serialCountText)
            serialCountText.text = getString(R.string.item_count, mfgSerialList.size)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

class MfgSerialAdapter(
    private val items: List<Pair<String, String>>,
    private val onItemAction: (Int, Action) -> Unit
) : RecyclerView.Adapter<MfgSerialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mfgText: TextView = view.findViewById(R.id.mfgTextView)
        val serialText: TextView = view.findViewById(R.id.serialTextView)
        val editButton: View = view.findViewById(R.id.editButton)
        val deleteButton: View = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mfg_serial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (mfg, serial) = items[position]
        holder.mfgText.text = mfg
        holder.serialText.text = serial
        
        holder.editButton.setOnClickListener { onItemAction(position, Action.EDIT) }
        holder.deleteButton.setOnClickListener { onItemAction(position, Action.DELETE) }
    }

    override fun getItemCount() = items.size
}

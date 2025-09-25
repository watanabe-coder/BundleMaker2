package com.example.bundlemaker2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

enum class Action { EDIT, DELETE }

class ConfirmActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MfgSerialAdapter
    private val mfgSerialList = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        // サンプルデータ（実際には前の画面から受け取る）
        mfgSerialList.add(Pair("MFG001", "SN12345"))
        mfgSerialList.add(Pair("MFG002", "SN67890"))

        // RecyclerViewの設定
        recyclerView = findViewById(R.id.recyclerView)
        adapter = MfgSerialAdapter(mfgSerialList) { position, action ->
            when (action) {
                Action.EDIT -> editItem(position)
                Action.DELETE -> deleteItem(position)
                // else分を追加して網羅性を担保
                else -> { /* 何もしない */ }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 確定ボタン
        findViewById<Button>(R.id.confirmButton).setOnClickListener {
            // データベースに保存する処理をここに実装
            Toast.makeText(this, "データを保存しました", Toast.LENGTH_SHORT).show()
            finish()
        }

        // キャンセルボタン
        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
    }

    private fun editItem(position: Int) {
        // 編集画面に遷移する処理を実装
        Toast.makeText(this, "編集: ${position + 1}番目の項目", Toast.LENGTH_SHORT).show()
    }

    private fun deleteItem(position: Int) {
        mfgSerialList.removeAt(position)
        adapter.notifyItemRemoved(position)
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

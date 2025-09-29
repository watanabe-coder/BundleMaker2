package com.example.bundlemaker2.ui.confirm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bundlemaker2.R

typealias MfgSerialPair = Pair<String, String>  // First is mfgId, second is serialId

class ConfirmListAdapter(
    private var items: List<MfgSerialPair>,
    private val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<ConfirmListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mfgIdText: TextView = view.findViewById(R.id.mfgIdText)
        val serialNumberText: TextView = view.findViewById(R.id.serialNumberText)
        val deleteButton: View = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_confirm_serial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (mfgId, serialId) = items[position]
        holder.mfgIdText.text = mfgId
        holder.serialNumberText.text = serialId
        holder.deleteButton.setOnClickListener {
            onItemDelete(position)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<MfgSerialPair>) {
        items = newItems
        notifyDataSetChanged()
    }
}

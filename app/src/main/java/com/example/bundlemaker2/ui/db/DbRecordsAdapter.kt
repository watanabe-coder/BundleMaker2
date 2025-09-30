package com.example.bundlemaker2.ui.db

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bundlemaker2.R
import com.example.bundlemaker2.databinding.ItemDbRecordBinding

class DbRecordsAdapter : ListAdapter<DbRecordItem, DbRecordsAdapter.DbRecordViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DbRecordViewHolder {
        val binding = ItemDbRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DbRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DbRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DbRecordViewHolder(
        private val binding: ItemDbRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DbRecordItem) {
            binding.apply {
                mfgIdText.text = item.mfgId.ifEmpty { root.context.getString(R.string.not_available) }
                serialIdText.text = item.serialId
                scannedAtText.text = item.scannedAt
                statusText.text = item.status
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DbRecordItem>() {
        override fun areItemsTheSame(oldItem: DbRecordItem, newItem: DbRecordItem): Boolean {
            return oldItem.serialId == newItem.serialId && oldItem.mfgId == newItem.mfgId
        }

        override fun areContentsTheSame(oldItem: DbRecordItem, newItem: DbRecordItem): Boolean {
            return oldItem == newItem
        }
    }
}

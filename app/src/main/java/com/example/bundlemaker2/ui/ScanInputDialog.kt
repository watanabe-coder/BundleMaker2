package com.example.bundlemaker2.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.bundlemaker2.R

class ScanInputDialog(
    private val title: String,
    private val hint: String,
    private val onPositiveClick: (String) -> Unit
) : DialogFragment() {

    private lateinit var editText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_scan_input, null)
        
        // Initialize views
        val titleText = view.findViewById<TextView>(R.id.titleText)
        editText = view.findViewById(R.id.inputEditText)
        
        // Set title and hint
        titleText.text = title
        editText.hint = hint

        // Request focus and show keyboard
        editText.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                val input = editText.text.toString().trim()
                if (input.isNotEmpty()) {
                    onPositiveClick(input)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .setNeutralButton(R.string.scan) { _, _ ->
                // TODO: Implement barcode scanning
                showToast("バーコードスキャンを開始します")
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun showToast(message: String) {
        context?.let {
            android.widget.Toast.makeText(it, message, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "ScanInputDialog"
    }
}

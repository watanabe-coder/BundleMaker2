package com.example.bundlemaker2.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.bundlemaker2.R

class ScanInputDialog(
    private val title: String,
    private val hint: String,
    private val showCancel: Boolean = false,
    private val showFinishButton: Boolean = false,
    private val onInput: (String, Boolean) -> Unit,
    private val onFinish: (() -> Unit)? = null
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

        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                val input = editText.text.toString().trim()
                if (input.isNotEmpty()) {
                    onInput(input, false)
                } else {
                    showToast("入力が空です。シリアル番号を入力してください。")
                    // ダイアログを閉じないようにする
                    try {
                        (dialog as? AlertDialog)?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        
        // 入力完了ボタンを追加
        if (showFinishButton) {
            builder.setNeutralButton(R.string.finish_input) { _, _ ->
                onFinish?.invoke()
            }
        }
        
        if (showCancel) {
            builder.setNegativeButton(R.string.cancel) { _, _ ->
                onInput("", true)
            }
        }
        
        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // ダイアログが表示されたら自動でキーボードを表示
        editText.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    companion object {
        const val TAG = "ScanInputDialog"
    }
}
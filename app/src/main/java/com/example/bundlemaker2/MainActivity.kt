package com.example.bundlemaker2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bundlemaker2.util.Constants

class MainActivity : AppCompatActivity() {
    private var currentMfgId: String = ""
    private val serialIds = mutableListOf<String>()

    private val confirmActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val confirmedSerials = result.data?.getStringArrayListExtra(Constants.EXTRA_CONFIRMED_SERIAL_IDS)
            confirmedSerials?.let {
                // Handle confirmed serials (save to database, etc.)
                showToast("${it.size}件のシリアル番号を確定しました")
                serialIds.clear()
            }
        } else {
            showToast("キャンセルされました")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
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
            // TODO: Implement bundle button click action
            showToast("Bundle button clicked")
            // For testing, add sample data
            currentMfgId = "MFG12345"
            serialIds.addAll(listOf("SER001", "SER002", "SER003"))
        }
        
        findViewById<View>(R.id.unitButton).setOnClickListener {
            // TODO: Implement unit button click action
            showToast("Unit button clicked")
            // For testing, add sample data
            currentMfgId = "MFG54321"
            serialIds.add("SER999")
        }
        
        findViewById<View>(R.id.confirmButton).setOnClickListener {
            if (currentMfgId.isBlank() || serialIds.isEmpty()) {
                showToast("製造番号とシリアル番号を入力してください")
                return@setOnClickListener
            }
            navigateToConfirm()
        }
        
        // Set up menu and refresh button click listeners
        findViewById<View>(R.id.menuButton).setOnClickListener {
            // TODO: Implement menu button click action
            showToast("Menu button clicked")
        }
        
        findViewById<View>(R.id.refreshButton).setOnClickListener {
            // TODO: Implement refresh button click action
            showToast("Refreshing...")
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun navigateToConfirm() {
        val intent = Intent(this, ConfirmActivity::class.java).apply {
            putExtra(Constants.EXTRA_MFG_ID, currentMfgId)
            putStringArrayListExtra(Constants.EXTRA_SERIAL_IDS, ArrayList(serialIds))
        }
        confirmActivityResult.launch(intent)
    }
}
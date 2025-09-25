package com.example.bundlemaker2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
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
        }
        
        findViewById<View>(R.id.unitButton).setOnClickListener {
            // TODO: Implement unit button click action
            showToast("Unit button clicked")
        }
        
        findViewById<View>(R.id.confirmButton).setOnClickListener {
            // TODO: Implement confirm button click action
            showToast("Confirm button clicked")
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
}
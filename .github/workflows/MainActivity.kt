package com.nazirbhahrami.smartlock

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView

    private val scannerLauncher =
        registerForActivityResult(ScanContract()) { result ->
            if (result.contents == null) {
                status.text = "Scan cancelled"
            } else if (result.contents == "NAZIR-BAHRAMI-2024") {
                status.text = "✓ Identity verified"
            } else {
                status.text = "✕ QR code not recognized"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)

        findViewById<Button>(R.id.scanButton).setOnClickListener {
            startScanner()
        }
    }

    private fun startScanner() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        } else {
            launchScanner()
        }
    }

    private fun launchScanner() {
        val options = ScanOptions().apply {
            setPrompt("Scan Nazir Bahrami QR")
            setBeepEnabled(true)
            setOrientationLocked(true)
        }
        scannerLauncher.launch(options)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                launchScanner()
            } else {
                status.text = "Camera permission is required"
            }
        }
    }
}

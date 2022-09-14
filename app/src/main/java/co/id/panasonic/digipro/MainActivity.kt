package co.id.panasonic.digipro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import co.id.panasonic.digipro.GlobalValue.Companion.token

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inspectionProcess   = findViewById<Button>(R.id.inspection_button);
        val transfersProcess    = findViewById<Button>(R.id.transfers_button);
        val lotcardStatus       = findViewById<Button>(R.id.lotcard_check);
        val logoutProcess       = findViewById<Button>(R.id.logout_button);
        inspectionProcess.setOnClickListener {
            startActivity(Intent(this, ScanInspection::class.java))
        }
        transfersProcess.setOnClickListener {
            startActivity(Intent(this, ScanTransfers::class.java))
        }
        lotcardStatus.setOnClickListener {
            startActivity(Intent(this, LotcardStatus::class.java))
        }
        logoutProcess.setOnClickListener {
            token = "not_login"
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onStart() {
        if (token == "not_login") {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        super.onStart()
    }
}
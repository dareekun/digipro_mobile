package co.id.panasonic.digipro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ScanTransfers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_transfers)
        val editText   = findViewById<EditText>(R.id.transfers_input);
        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val volleyQueue = Volley.newRequestQueue(this)
                val url = "http://158.118.35.160/api/scantransfers_mobile"
                val params = JSONObject()
                params.put("id", editText.text)
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, params,
                    { response ->
                        val status = response.get("status")
                        if (status == 200) {
                            val dataResponse = response.get("data")
                            Toast.makeText(this, dataResponse.toString(), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Opps Looks Like Something Wrong", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                )
                volleyQueue.add(jsonObjectRequest)
            }
            false
        }
    }
}
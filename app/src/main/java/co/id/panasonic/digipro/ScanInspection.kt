package co.id.panasonic.digipro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ScanInspection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_inspection)
        val resetBtn   = findViewById<Button>(R.id.inspection_process_button);
        val editText   = findViewById<EditText>(R.id.inspection_input);
        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                go()
            }
            false
        }
        resetBtn.setOnClickListener {
            go()
        }
    }
    protected fun go() {
        val volleyQueue = Volley.newRequestQueue(this)
        val url = "http://158.118.35.160/api/scaninspection_mobile"
        val params = JSONObject()
        val barcode = findViewById<EditText>(R.id.inspection_input).text
        GlobalValue.inspectionID = barcode.toString()
        params.put("id", GlobalValue.inspectionID)
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    val intent = Intent(this, process_inspection::class.java)
                    intent.putExtra("data", response.get("data").toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Opps Looks Like Something Wrong", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )
        findViewById<EditText>(R.id.inspection_input).setText("")
        findViewById<EditText>(R.id.inspection_input).requestFocus();
        volleyQueue.add(jsonObjectRequest)
    }

    override fun onResume() {
        findViewById<EditText>(R.id.inspection_input).setText("")
        findViewById<EditText>(R.id.inspection_input).requestFocus();
        super.onResume()
    }
}
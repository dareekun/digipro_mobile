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
    val params = JSONObject()
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
        val goQueue = Volley.newRequestQueue(this)
        val goUrl = GlobalValue.server + "scaninspection_mobile"
        val barcode = findViewById<EditText>(R.id.inspection_input).text
        params.put("id", barcode.toString())
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, goUrl, params,
            { response ->
                if (response.get("status") == 200) {
                    if (response.get("post") == 0) {
                        val intent = Intent(this, process_inspection::class.java)
                        intent.putExtra("data", response.get("data").toString())
                        startActivity(intent)
                    } else {
                        showinspection(barcode.toString())
                    }
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
        goQueue.add(jsonObjectRequest)
    }

    protected fun showinspection(input: String) {
        val volleyQueue = Volley.newRequestQueue(this)
        val url = GlobalValue.server + "showinspection_mobile"
        params.put("id", input)
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    val intent = Intent(this, ShowInspection::class.java)
                    intent.putExtra("data", response.get("data").toString())
                    startActivity(intent)
                } else if (status == 403) {
                    GlobalValue.token = "not_login"
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(this, "Opps Looks Like Something Wrong", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ScanInspection::class.java)
                    startActivity(intent)
                }
            },
            { error ->
                Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ScanInspection::class.java)
                startActivity(intent)
            }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    override fun onResume() {
        findViewById<EditText>(R.id.inspection_input).setText("")
        findViewById<EditText>(R.id.inspection_input).requestFocus();
        super.onResume()
    }
}
package co.id.panasonic.digipro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import co.id.panasonic.digipro.GlobalValue.Companion.token
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LotcardStatus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lotcard_status)
        val resetBtn = findViewById<Button>(R.id.reset_input_lotcard);
        val editText = findViewById<EditText>(R.id.lotcard_input);
        editText.requestFocus();
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
        val url = "http://158.118.35.160/api/lotcard_mobile"
        val params = JSONObject()
        params.put("id", findViewById<EditText>(R.id.lotcard_input).text)
        params.put("token", token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    val intent = Intent(this, LotCardShow::class.java)
                    intent.putExtra("data", response.get("data").toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Opps Looks Like Something Wrong", Toast.LENGTH_SHORT).show()
                    Log.i("Token Value", token)
                    Log.i("Status Value", response.get("status").toString())
                }
            },
            { error ->
                Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )
        findViewById<EditText>(R.id.lotcard_input).setText("")
        findViewById<EditText>(R.id.lotcard_input).requestFocus();
        volleyQueue.add(jsonObjectRequest)
    }

    override fun onResume() {
        findViewById<EditText>(R.id.lotcard_input).setText("")
        findViewById<EditText>(R.id.lotcard_input).requestFocus();
        super.onResume()
    }
}
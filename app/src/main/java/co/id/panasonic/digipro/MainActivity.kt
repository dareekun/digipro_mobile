package co.id.panasonic.digipro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.id.panasonic.digipro.GlobalValue.Companion.depart
import co.id.panasonic.digipro.GlobalValue.Companion.token
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuSuper     = findViewById<Button>(R.id.menu_super);
        val generatedata   = findViewById<Button>(R.id.data_generate);
        val logoutProcess = findViewById<Button>(R.id.logout_button);
        if (depart == "Quality Control") {
            menuSuper.setText("Inspection Process")
        } else {
            menuSuper.setText("Transfers Process")
        }
        menuSuper.setOnClickListener {
            if (depart == "Quality Control") {
                startActivity(Intent(this, ScanInspection::class.java))
            } else {
                startActivity(Intent(this, ScanTransfers::class.java))
            }
        }
        generatedata.setOnClickListener {
            data_transaction()
        }
        logoutProcess.setOnClickListener {
            depart = "department"
            token  = "not_login"
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    protected fun data_transaction() {
        val volleyQueue = Volley.newRequestQueue(this)
        val url = GlobalValue.server + "datatransaction_mobile"
        val params = JSONObject()
        params.put("token", token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    val intent = Intent(this, TransfersProcess::class.java)
                    intent.putExtra("data", response.get("data").toString())
                    startActivity(intent)
                } else if (status == 403) {
                    Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                    depart = "department"
                    token = "not_login"
                    startActivity(Intent(this, LoginActivity::class.java))
                } else if (status == 500) {
                    Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "Opps Looks Like Something Wrong", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    override fun onResume() {
        if (token == "not_login") {
            depart = "department"
            startActivity(Intent(this, LoginActivity::class.java))
        }
        super.onResume()
    }
}
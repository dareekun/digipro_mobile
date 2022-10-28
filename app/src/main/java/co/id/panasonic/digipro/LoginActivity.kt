package co.id.panasonic.digipro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.id.panasonic.digipro.GlobalValue.Companion.depart
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import co.id.panasonic.digipro.GlobalValue.Companion.token

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username   = findViewById<EditText>(R.id.input_nik);
        val password   = findViewById<EditText>(R.id.input_password);
        val showButton = findViewById<Button>(R.id.login_button);

        showButton.setOnClickListener {
            val volleyQueue = Volley.newRequestQueue(this)
            val url = GlobalValue.server + "login"
            val params = JSONObject()
            params.put("username", username.text)
            params.put("password", password.text)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, params,
                { response ->
                    val status = response.get("status")
                    if (status == 200) {
                        token = response.get("token").toString()
                        depart = response.get("depart").toString()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("name_user", response.get("user").toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            )
            volleyQueue.add(jsonObjectRequest)
        }
    }

    override fun onStart() {
        if (token != "not_login") {
            startActivity(Intent(this, MainActivity::class.java))
        }
        super.onStart()
    }

}
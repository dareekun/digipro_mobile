package co.id.panasonic.digipro

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    val params = JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // EditText
        val currents = findViewById<EditText>(R.id.current_password);
        val new_pass = findViewById<EditText>(R.id.new_password_1);
        val conf_pas = findViewById<EditText>(R.id.new_password_2);
        // Button
        val btnAction    = findViewById<TextView>(R.id.save_button);

        btnAction.setOnClickListener {
            val actionQueue = Volley.newRequestQueue(this)
            val actionUrl = GlobalValue.server + "changepassword_mobile"
            params.put("token", GlobalValue.token)
            params.put("pass0", currents)
            params.put("pass1", new_pass)
            params.put("pass2", conf_pas)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, actionUrl, params,
                { response ->
                    if (response.get("status") == 200) {
                        Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else if (response.get("status") == 500) {
                        Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                    } else if (response.get("status") == 403) {
                        GlobalValue.depart = "department"
                        GlobalValue.token = "not_login"
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            )
            actionQueue.add(jsonObjectRequest)
        }
    }
}
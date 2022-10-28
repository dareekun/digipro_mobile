package co.id.panasonic.digipro

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class process_inspection : AppCompatActivity() {
    var radioval: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process_inspection)
        val data     = JSONObject(getIntent().getStringExtra("data").toString())
        val procBtn  = findViewById<Button>(R.id.process_inpection_data);
        val pcsBox   = findViewById<EditText>(R.id.pcs_per_box);
        val lotNumbr = findViewById<EditText>(R.id.input_lotnumber);
        val model_no = findViewById<TextView>(R.id.model_number_inspection);
        pcsBox.setText(data.get("packing").toString());
        lotNumbr.setText(data.get("lotno").toString());
        model_no.setText(data.get("model_no").toString());
        lotNumbr.setEnabled(false);
        val rg = findViewById<RadioGroup>(R.id.radio_group1)

        rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_ok -> {
                    radioval = 1
                }
                R.id.radio_ng -> {
                    radioval = 2
                }
                R.id.radio_hold -> {
                    radioval = 3
                }
            }
        }
        procBtn.setOnClickListener {
            go()
        }
    }

    protected fun focused(input: String) {
        if (input == "pcs_box") {
            findViewById<EditText>(R.id.pcs_per_box).requestFocus();
        }
        if (input == "totsbox") {
            findViewById<EditText>(R.id.total_box).requestFocus();
        }
        if (input == "lotsize") {
            findViewById<EditText>(R.id.input_lotsize).requestFocus();
        }
    }

    protected fun go() {
        val data     = JSONObject(getIntent().getStringExtra("data").toString())
        val volleyQueue = Volley.newRequestQueue(this)
        val url = GlobalValue.server + "processinspection_mobile"
        val params = JSONObject()
        params.put("id", data.get("barcode").toString())
        params.put("model_no", data.get("model_no").toString())
        params.put("pcs_box", findViewById<EditText>(R.id.pcs_per_box).text)
        params.put("totsbox", findViewById<EditText>(R.id.total_box).text)
        params.put("lotsize", findViewById<EditText>(R.id.input_lotsize).text)
        params.put("remarks", findViewById<EditText>(R.id.input_remark).text)
        params.put("judgeme", radioval)
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    val intent = Intent(this, ShowInspection::class.java)
                    intent.putExtra("data", response.get("data").toString())
                    startActivity(intent)
                } else if (status == 502) {
                    focused(response.get("type").toString())
                    Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                } else if (status == 403) {
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
        volleyQueue.add(jsonObjectRequest)
    }
}
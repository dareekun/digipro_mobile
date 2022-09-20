package co.id.panasonic.digipro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class process_inspection : AppCompatActivity() {
    val dataLotcard = getIntent().getStringExtra("data")
    val data     = JSONObject(dataLotcard.toString())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process_inspection)

        val resetBtn = findViewById<Button>(R.id.reset_input_lotcard);
        val prodName = findViewById<EditText>(R.id.product_name);
        val pcsBox   = findViewById<EditText>(R.id.pcs_per_box);
        val lotNumbr = findViewById<EditText>(R.id.input_lotnumber);
        val section  = findViewById<EditText>(R.id.input_section);
        val line     = findViewById<EditText>(R.id.input_line);
        prodName.setText(data.get("model_no").toString());
        pcsBox.setText(data.get("packing").toString());
        lotNumbr.setText(data.get("lotno").toString());
        section.setText(data.get("section").toString());
        line.setText(data.get("line").toString());
        prodName.setEnabled(false);
        lotNumbr.setEnabled(false);
        section.setEnabled(false);
        line.setEnabled(false);

        resetBtn.setOnClickListener {
            go()
        }
    }

    protected fun focused(input: String) {
        if (input == "pcs") {
            findViewById<EditText>(R.id.pcs_per_box).requestFocus();
        }
        if (input == "total_box") {
            findViewById<EditText>(R.id.total_box).requestFocus();
        }
        if (input == "input_date") {
            findViewById<EditText>(R.id.input_date).requestFocus();
        }
        if (input == "lot_size") {
            findViewById<EditText>(R.id.input_lotsize).requestFocus();
        }
        if (input == "remark") {
            findViewById<EditText>(R.id.input_remark).requestFocus();
        }
    }
    protected fun go() {
        val volleyQueue = Volley.newRequestQueue(this)
        val url = "http://158.118.35.160/api/processinspection_mobile"
        val params = JSONObject()
        params.put("barcode", data.get("barcode").toString())
        params.put("pcs_box", findViewById<EditText>(R.id.pcs_per_box).text)
        params.put("totsbox", findViewById<EditText>(R.id.total_box).text)
        params.put("inpdate", findViewById<EditText>(R.id.input_date).text)
        params.put("lotsize", findViewById<EditText>(R.id.input_lotsize).text)
        params.put("remarks", findViewById<EditText>(R.id.input_remark).text)
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    showinspection()
                } else {
                    focused(response.get("types").toString())
                    Toast.makeText(this, "Opps Looks Like Something Wrong", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "connection Fail : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    protected fun showinspection() {
        val volleyQueue = Volley.newRequestQueue(this)
        val url = "http://158.118.35.160/api/showinspection_mobile"
        val params = JSONObject()
        params.put("id", data.get("barcode").toString())
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    val intent = Intent(this, ShowInspection::class.java)
                    intent.putExtra("data", response.get("data").toString())
                    startActivity(intent)
                } else {
                    focused(response.get("types").toString())
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
        if (Integer.valueOf(data.get("status").toString()) >= 1) {
            showinspection()
        }
        super.onResume()
    }
}
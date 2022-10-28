package co.id.panasonic.digipro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class QtyModifier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qty_modifier)
        val data    = JSONObject(getIntent().getStringExtra("data").toString());
        val procBtn = findViewById<Button>(R.id.btn_process);
        // TextView
        val model_no = findViewById<TextView>(R.id.model_qty);
        val lot_numb = findViewById<TextView>(R.id.lotno_qty);
        val shift_pr = findViewById<TextView>(R.id.shift_qty);
        val status_p = findViewById<TextView>(R.id.status_qty);
        val inspecto = findViewById<TextView>(R.id.checker_qty);
        // EditText
        val pcs_box = findViewById<EditText>(R.id.packing_qty);
        val lotsize = findViewById<EditText>(R.id.lotsize_qty);
        val totsbox = findViewById<EditText>(R.id.total_box_qty);

        var judgestat   = ""

        if (data.get("judgement").toString() == "1") {
            judgestat = "OK"
        } else if (data.get("judgement").toString() == "2") {
            judgestat = "NG"
        } else {
            judgestat = "HOLD"
        }

        // EditText
        model_no.setText(data.get("model_no").toString());
        lot_numb.setText(data.get("lotno").toString());
        shift_pr.setText(data.get("shift").toString());
        status_p.setText(judgestat);
        inspecto.setText(data.get("checker").toString());

        pcs_box.setText(data.get("packing").toString());
        lotsize.setText(data.get("finish_goods_1").toString());
        totsbox.setText(data.get("finish_goods_2").toString());

        procBtn.setOnClickListener {
            go()
        }
    }

    protected fun focused(input: String) {
        if (input == "packing") {
            findViewById<EditText>(R.id.packing_qty).requestFocus();
        }
        if (input == "totsbox") {
            findViewById<EditText>(R.id.total_box_qty).requestFocus();
        }
        if (input == "lotsize") {
            findViewById<EditText>(R.id.lotsize_qty).requestFocus();
        }
    }

    protected fun go() {
        val data     = JSONObject(getIntent().getStringExtra("data").toString())
        val volleyQueue = Volley.newRequestQueue(this)
        val url = GlobalValue.server + "processtransfers_mobile"
        val params = JSONObject()
        params.put("id", data.get("barcode").toString())
        params.put("packing", findViewById<EditText>(R.id.packing_qty).text)
        params.put("totsbox", findViewById<EditText>(R.id.total_box_qty).text)
        params.put("lotsize", findViewById<EditText>(R.id.lotsize_qty).text)
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val status = response.get("status")
                if (status == 200) {
                    Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ScanTransfers::class.java)
                    startActivity(intent)
                } else if (status == 502) {
                    focused(response.get("type").toString())
                    Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                } else if (status == 403) {
                    Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
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
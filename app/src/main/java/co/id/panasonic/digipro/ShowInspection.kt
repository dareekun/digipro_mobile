package co.id.panasonic.digipro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ShowInspection : AppCompatActivity() {
    val params = JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_inspection)
        val dataLotcard = getIntent().getStringExtra("data")
        val printBtn    = findViewById<Button>(R.id.print_inspection);
        val data        = JSONObject(dataLotcard.toString())
        var judgestat   = ""

        if (data.get("judgement").toString() == "1") {
            judgestat = "OK"
        } else if (data.get("judgement").toString() == "2") {
            judgestat = "NG"
        } else {
            judgestat = "HOLD"
        }

        val nopro = findViewById<TextView>(R.id.nopro_inspection);
        val pcbox = findViewById<TextView>(R.id.packing_inspection);
        val tobox = findViewById<TextView>(R.id.tobox_inspection);
        val losiz = findViewById<TextView>(R.id.lotsize_inspection);
        val lonum = findViewById<TextView>(R.id.lotnumber_inspection);
        val shift = findViewById<TextView>(R.id.shift_inspection);
        val secti = findViewById<TextView>(R.id.section_inspection);
        val lines = findViewById<TextView>(R.id.line_inspection);
        val remrk = findViewById<TextView>(R.id.remark_inspection);
        val ceker = findViewById<TextView>(R.id.checker_inspection);
        val judge = findViewById<TextView>(R.id.status_inspection);

        nopro.setText(data.get("model_no").toString());
        pcbox.setText(data.get("packing").toString());
        tobox.setText(data.get("finish_goods_2").toString());
        losiz.setText(data.get("finish_goods_1").toString());
        lonum.setText(data.get("lotno").toString());
        shift.setText(data.get("shift").toString());
        secti.setText(data.get("section").toString());
        lines.setText(data.get("line").toString());
        remrk.setText(data.get("remark").toString());
        ceker.setText(data.get("checker_name").toString());
        judge.setText(judgestat);
        printBtn.setOnClickListener {
            print(data.get("barcode").toString())
        }
    }

    protected fun print(input: String) {
        val printQueue = Volley.newRequestQueue(this)
        val printUrl = GlobalValue.server + "printinspection_mobile"
        params.put("id", input)
        params.put("token", GlobalValue.token)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, printUrl, params,
            { response ->
                if (response.get("status") == 200) {
                    Toast.makeText(this, "Printing Command Already Succesfully Sended", Toast.LENGTH_SHORT).show()
                } else if (response.get("status") == 403) {
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
        printQueue.add(jsonObjectRequest)
    }
}
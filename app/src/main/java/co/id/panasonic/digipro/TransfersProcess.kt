package co.id.panasonic.digipro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class TransfersProcess : AppCompatActivity() {
    val params = JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfers_process)

        val datarray = getIntent().getStringExtra("data")
        val data     = JSONArray(datarray.toString())

        val btnAction    = findViewById<TextView>(R.id.transfers_action);

        val masterTable = findViewById<TableLayout>(R.id.data_table);

        for (i in 0 until data.length()) {
            val tvl = TextView(this)
            val tvr = TextView(this)
            val row = TableRow(this)
            val params: TableRow.LayoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            tvl.setText(data.getJSONObject(i).get("model_no").toString())
            tvr.setText(data.getJSONObject(i).get("total_box").toString() + " / " + data.getJSONObject(i).get("total_qty").toString())
            tvl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            tvr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            tvr.layoutParams = params
            tvr.gravity = Gravity.RIGHT
            row.addView(tvl)
            row.addView(tvr)
            masterTable.addView(row)
        }

        btnAction.setOnClickListener {
            val actionQueue = Volley.newRequestQueue(this)
            val actionUrl = GlobalValue.server + "closed_transaction"
            params.put("token", GlobalValue.token)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, actionUrl, params,
                { response ->
                    if (response.get("status") == 200) {
                        Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
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
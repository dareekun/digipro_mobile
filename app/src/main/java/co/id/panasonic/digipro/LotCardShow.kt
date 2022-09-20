package co.id.panasonic.digipro

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class LotCardShow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lot_card_show)

        val dataLotcard = getIntent().getStringExtra("data")
        val data     = JSONObject(dataLotcard.toString())
        val parts    = JSONArray(data.get("parts").toString())
        val model_no = findViewById<TextView>(R.id.model_modifier);
        val lot_no   = findViewById<TextView>(R.id.lotno_modifier);
        val shift    = findViewById<TextView>(R.id.shift_modifier);

        val input_value1 = findViewById<TextView>(R.id.input_modifier1);
        val ng_value1    = findViewById<TextView>(R.id.ng_modifier1);
        val date_value1  = findViewById<TextView>(R.id.date_modifier1);
        val name_value1  = findViewById<TextView>(R.id.name_modifier1);
        val input_value2 = findViewById<TextView>(R.id.input_modifier2);
        val ng_value2    = findViewById<TextView>(R.id.ng_modifier2);
        val date_value2  = findViewById<TextView>(R.id.date_modifier2);
        val name_value2  = findViewById<TextView>(R.id.name_modifier2);
        val judgement    = findViewById<TextView>(R.id.status_product);
        val ceker        = findViewById<TextView>(R.id.checker_product);

        val masterTable = findViewById<TableLayout>(R.id.data_table);

        for (i in 0 until parts.length()) {
            val tvl = TextView(this)
            val tvr = TextView(this)
            val row = TableRow(this)
            val params: TableRow.LayoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            tvl.setText(parts.getJSONObject(i).get("parts").toString())
            tvr.setText(parts.getJSONObject(i).get("lot_parts").toString())
            tvl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            tvr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            tvr.layoutParams = params
            tvr.gravity = Gravity.RIGHT
            row.addView(tvl)
            row.addView(tvr)
            masterTable.addView(row)
        }

        model_no.setText(": " + data.get("model_no").toString())
        lot_no.setText(": " + data.get("lotno").toString())
        shift.setText(": Shift " + data.get("shift").toString())

        val input1 = if (data.get("finish_goods_1").toString() != "null") data.get("finish_goods_1").toString() else ""
        val input2 = if (data.get("finish_goods_2").toString() != "null") data.get("finish_goods_2").toString() else ""
        val ng1    = if (data.get("no_goods_1").toString() != "null") data.get("no_goods_1").toString() else ""
        val ng2    = if (data.get("no_goods_2").toString() != "null") data.get("no_goods_2").toString() else ""
        val check  = if (data.get("checker_name").toString() != "null") data.get("checker_name").toString() else ""
        val judge  =
            if (data.get("judgement") == 1) "OK"
             else if (data.get("judgement") == 2) "NG"
                else if (data.get("judgement") == 3) "HOLD"
                    else "ON PRODUCTION"

        input_value1.setText(input1)
        ng_value1.setText(ng1)
        date_value1.setText(data.get("date_1").toString())
        name_value1.setText(data.get("name_1").toString())
        input_value2.setText(input2)
        ng_value2.setText(ng2)
        date_value2.setText(data.get("date_2").toString())
        name_value2.setText(data.get("name_2").toString())
        judgement.setText(judge)
        ceker.setText(check)
    }
}
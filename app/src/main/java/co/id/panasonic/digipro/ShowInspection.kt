package co.id.panasonic.digipro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONObject

class ShowInspection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_inspection)
        val dataLotcard = getIntent().getStringExtra("data")
        val data     = JSONObject(dataLotcard.toString())

        val nopro = findViewById<TextView>(R.id.date_inspection);
        val pcbox = findViewById<TextView>(R.id.date_inspection);
        val tobox = findViewById<TextView>(R.id.date_inspection);
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
        judge.setText(data.get("judgement").toString());
    }
}
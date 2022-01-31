package com.ayubo.life.ayubolife.prochat.appointment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.health.MedicineView_PaymentActivity
import kotlinx.android.synthetic.main.activity_health_net_buy_.*

class HealthNetBuy_Activity : AppCompatActivity() {


    private var media_url: String? = null
    private var external_expert_id: String = ""
    private var button_type: String = ""
    private var params: DocSearchParameters? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_net_buy_)


        media_url = intent.getStringExtra("media_url")
        button_type = intent.getStringExtra("button_type").toString()

//        btn_backImgBtn_layout
//        lay_btnBack
//
        btn_backImgBtn_kt.setOnClickListener {

            finish()
        }
        txtNextToHealthNetBuy.setOnClickListener {

            if (button_type.equals("CHANNELING")) {
//                val intent = Intent(this, MedicineView_PaymentActivity::class.java)
//                intent.putExtra("media_url", media_url)
//                intent.putExtra("from", "chat")
//                startActivity(intent)

                external_expert_id = intent.getStringExtra("external_expert_id").toString()
                val intent = Intent(this, SearchActivity::class.java)
                params = DocSearchParameters()

                params!!.setUser_id("7fa9082c-b5e8-d522-de15-5a314edac766")
                params!!.setDate("")
                params!!.setSpecializationId("")
                params!!.setDoctorId(external_expert_id)
                params!!.setLocationId("")

                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(params))
                startActivity(intent)


            } else if (button_type.equals("DELIVERY")) {
                val intent = Intent(this, MedicineView_PaymentActivity::class.java)
                intent.putExtra("media_url", media_url)
                intent.putExtra("from", "chat")
                startActivity(intent)
            }


//            Intent i = new Intent(Medicine_ViewActivity.this, MedicineView_PaymentActivity.class);
//            Ram.setImageAbsoulutePath(uri);
//            startActivity(i);
        }

    }
}

package com.ayubo.life.ayubolife.programs.viewexperts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.EXTRA_PAYMENT_META
import com.ayubo.life.ayubolife.payment.EXTRA_PROGRAM_EXPERTS
import com.ayubo.life.ayubolife.payment.activity.OtherPaymentActivity
import com.ayubo.life.ayubolife.payment.adapter.OtherPaymentOptionsAdapter
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewAdapter
import com.ayubo.life.ayubolife.reports.getareview.ReviewExperts
import kotlinx.android.synthetic.main.activity_ask.*
import kotlinx.android.synthetic.main.activity_view_experts.*

class ViewExpertsActivity : BaseActivity() , ViewExpertsAdapter.OnItemClickListener{
    override fun onProcessAction(action: String, meta: String) {
        processAction(action,meta)
    }


    companion object {
        fun startActivity(context: Activity,experts: ArrayList<Experts>) {
            val intent = Intent(context, ViewExpertsActivity::class.java)
            intent.putExtra(EXTRA_PROGRAM_EXPERTS, experts)
            context.startActivity(intent)
        }
    }
    private fun readExtras():ArrayList<Experts> {
        lateinit var dataList:ArrayList<Experts>
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_PROGRAM_EXPERTS)) {
            dataList = bundle.getSerializable(EXTRA_PROGRAM_EXPERTS) as ArrayList<Experts>
        }
        return dataList
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_experts)


        loadView(readExtras())

        // BACK EVENT START
        img_backBtn_ExpertNewView.setOnClickListener {finish()}
        // BACK BUTTON EVENT END
    }


    fun loadView(dataList:ArrayList<Experts>){

        val timelineAdapter = ViewExpertsAdapter(this@ViewExpertsActivity, dataList)
         timelineAdapter.onitemClickListener = this@ViewExpertsActivity

        val mlayoutManager = LinearLayoutManager(this@ViewExpertsActivity)
        ask_recycler_viewexp.apply {

            if(adapter==null){
                layoutManager = mlayoutManager
                adapter = timelineAdapter
            }else{
                adapter!!.notifyDataSetChanged()
            }

        }
    }
}

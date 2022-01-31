package com.ayubo.life.ayubolife.map_challange.activity


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.map_challange.adapter.JournalAdapter
import com.ayubo.life.ayubolife.map_challange.model.LocationData
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_CITY_LIST
import com.ayubo.life.ayubolife.payment.EXTRA_CURRENT_STEPS
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import kotlinx.android.synthetic.main.activity_journal.*


class JournalActivity : BaseActivity(), JournalAdapter.OnItemClickListener {


    override fun onProcessAction(action: String, meta: String) {
        processAction(action, meta)
    }

    var currentSteps: Int = 0
    var roadTrees: ArrayList<LocationData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        UIinit()

        readExtras()


    }

    private fun readExtras() {
        roadTrees = ArrayList<LocationData>()
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_CHALLANGE_CITY_LIST)) {

            try {

                currentSteps = intent.getIntExtra(EXTRA_CURRENT_STEPS, -1)
                roadTrees = bundle.getSerializable(EXTRA_CHALLANGE_CITY_LIST) as ArrayList<LocationData>

                if (roadTrees!!.isNotEmpty()) {
                    val journalAdapter = JournalAdapter(this@JournalActivity, roadTrees!!, currentSteps)
                    journalAdapter.onItemClickListener = this@JournalActivity
                    val mlayoutManager = LinearLayoutManager(this@JournalActivity)
                    dropdown_items_recycler.apply {
                        if (adapter == null) {
                            layoutManager = mlayoutManager
                            adapter = journalAdapter
                        } else {
                            adapter!!.notifyDataSetChanged()
                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun UIinit() {

        btn_backImgBtn_layout_Diet.setOnClickListener {

            finish()
        }
        btn_backImgBtn_Diet.setOnClickListener {

            finish()
        }
    }
}

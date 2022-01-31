package com.ayubo.life.ayubolife.map_challenges.treasureview


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.EXTRA_TREASURE_KEY
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_treasure_view.*
import javax.inject.Inject
import android.graphics.drawable.GradientDrawable




class TreasureViewActivity :  BaseActivity() {


    @Inject
    lateinit var treasureViewVM: TreasureViewVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_view)

        App.getInstance().appComponent.inject(this)


        readExtras()

    }


    private fun getTreasureData(fullKey:String){

        Log.d(".....fullKey........",fullKey)
        val separated = fullKey.split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

      //  val separated = fullKey.split("\\_")
        val challengeid= separated[0] // this will contain "Fruit"
        val treasureid= separated[1]
        Log.d(".....chalgeid........",challengeid)
        Log.d(".....treasureid........",treasureid)

        subscription.add(treasureViewVM.getTreasureData(challengeid,treasureid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        val response=treasureViewVM.response!!.data

                        txt_header.text=response.header
                        txt_sub_header.text=response.sub_heading
                        txt_footer_text.text=response.banner_text

                        txt_header.setTextColor(Color.parseColor(response.font_color))
                        txt_sub_header.setTextColor(Color.parseColor(response.font_color))

                        val greenColorValue1 = Color.parseColor(response.gradient1)
                        val greenColorValue2 = Color.parseColor(response.gradient2)
                        val layout = findViewById<View>(R.id.main_layout_bg)
                        val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                intArrayOf(greenColorValue1, greenColorValue2))
                        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
                        gd.cornerRadius = 0f
                        layout.setBackgroundDrawable(gd)


                        Glide.with(this@TreasureViewActivity).load(response.treaure_image).into(img_main_icon)
                        Glide.with(this@TreasureViewActivity).load(response.banner_image).into(img_subimage)

                        cardview.setOnClickListener {
                            processAction(response.action,response.meta)
                        }

                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    progressBar.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }



    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_TREASURE_KEY)) {
            val treasureKey = bundle.getSerializable(EXTRA_TREASURE_KEY) as String

                getTreasureData(treasureKey)

        }
    }

//    companion object {
//        fun startActivity(context: Activity, treasureKey: String) {
//            val intent = Intent(context, TreasureViewActivity::class.java)
//            intent.putExtra(EXTRA_TREASURE_KEY, treasureKey)
//            context.startActivity(intent)
//        }
//    }

}

package com.ayubo.life.ayubolife.challenges

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.ask.AskAdapter
import com.ayubo.life.ayubolife.ask.AskVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ask.*
import javax.inject.Inject

class ChallangeActivity : BaseActivity() {


    @Inject
    lateinit var challangeVM: ChallangeVM

    companion object {
        fun startActivity(context: Context?) {
            context!!.startActivity(Intent(context, AskActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challange)
        App.getInstance().appComponent.inject(this)


        getAskData()

    }

    private fun getAskData() {


        // val challenge_id = "f7178c00-db85-11e9-bec6-000d3aa00fd6"
        val challenge_id = ""
        val userID = ""

        val jsonStr = "{" +
                "\"userID\": \"" + userID + "\"," +
                "\"challenge_id\": \"" + challenge_id + "\"" +
                "}"



        subscription.add(challangeVM.getChallangeData("getChallengeSteps", jsonStr).subscribeOn(
                Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { proDialog_ask.visibility = View.VISIBLE }
                .doOnTerminate { proDialog_ask.visibility = View.GONE }
                .doOnError { proDialog_ask.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        //  val dataList=  askVM.askDataList


                    } else {
                        showMessage("Failed loading data")
                    }
                }, {
                    showMessage("Failed loading data")
                })
        )
    }

}

package com.ayubo.life.ayubolife.janashakthionboarding.mood_calender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model.MoodMainData
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireVM
import com.ayubo.life.ayubolife.janashakthionboarding.title_clicked.TitleClickedActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_intro.progressBar_Intro
import kotlinx.android.synthetic.main.activity_mood_calender.*
import javax.inject.Inject

class MoodCalenderActivity : BaseActivity() {

    @Inject
    lateinit var janashakthiQuestionnaireVM: JanashakthiQuestionnaireVM

    var moodObj : MoodMainData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_calender)

        App.getInstance().appComponent.inject(this)

        lay_green.setOnClickListener {
            callService("good")
        }
        lay_yellow.setOnClickListener {
            callService("ok")
        }
        lay_red.setOnClickListener {
            callService("bad")
        }


    }

    fun onClickFinish_MoodCal(view:View){
       finish()
    }


    fun callService(mood_type:String){

        subscription.add(janashakthiQuestionnaireVM.getMoodCalenderData(mood_type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar_Intro.visibility = View.VISIBLE }
                .doOnTerminate { progressBar_Intro.visibility = View.GONE }
                .doOnError { progressBar_Intro.visibility = View.GONE }
                .subscribe({
                    if(it.isSuccess){
                        moodObj= janashakthiQuestionnaireVM.moodObj

                        if(moodObj!!.mediaType.equals("IMAGE")){
                            val intent = Intent(this, CalenderDetailsActivity::class.java)
                            intent.putExtra("media_url", moodObj!!.mediaUrl)
                            intent.putExtra("id", moodObj!!.id.toString())
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        showMessage("Loading failed")
                    }
                },{
                    progressBar_Intro.visibility = View.GONE
                    showMessage("Loading failed")
                })
        )


    }


}

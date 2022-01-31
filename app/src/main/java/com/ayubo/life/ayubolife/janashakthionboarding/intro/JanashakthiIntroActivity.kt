package com.ayubo.life.ayubolife.janashakthionboarding.intro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.janashakthionboarding.VideoPlayActivity
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Settings
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.SeaShellsQuestionnaireActivity
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireActivity
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireVM
import com.ayubo.life.ayubolife.janashakthionboarding.webview.JanashakthiWebViewActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.timeline.TimelineVideo_Activity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_janashakthi_web_view.*
import javax.inject.Inject

class JanashakthiIntroActivity : BaseActivity() {
    var settingsObj : Settings?=null
    var pref: PrefManager? =null

    @Inject
    lateinit var janashakthiQuestionnaireVM: JanashakthiQuestionnaireVM

    fun startActivityJanashakthiIntroActivity(context: Context){
        context.startActivity(Intent(context,JanashakthiIntroActivity::class.java))
        startActivity(intent)
    }

    companion object {
        fun startActivity(context: Context){
            context.startActivity(Intent(context, JanashakthiIntroActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janashakthi_intro)
        App.getInstance().appComponent.inject(this)

        pref = PrefManager(this)


        subscription.add(janashakthiQuestionnaireVM.getSeaShellsQuestionnaire("226").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar_Intro.visibility = View.VISIBLE }
                .doOnTerminate { progressBar_Intro.visibility = View.GONE }
                .doOnError { progressBar_Intro.visibility = View.GONE }
                .subscribe({
                    if(it.isSuccess){
                        settingsObj= janashakthiQuestionnaireVM.settingsObj

                      Log.d("terer","sdfsdfsdf")
                    }else{
                        showMessage("Failed loading questionnaire")
                    }
                },{
                    progressBar_Intro.visibility = View.GONE
                    showMessage("Failed loading questionnaire")
                })
        )
    }

    fun onBeginQuestionnaireClick(view:View){

        if(settingsObj!!.group_intro.equals("1")){
            IntroActivity.startActivity(this)
            finish()
        }else{
            SeaShellsQuestionnaireActivity.startActivity(this)
            finish()
        }
    }

    fun onPlayVideoClicked(view:View){
        val intent = Intent(this, VideoPlayActivity::class.java)
        intent.putExtra("URL", settingsObj!!.intro_video)
        startActivity(intent)
    }
}

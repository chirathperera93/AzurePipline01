package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.janashakthionboarding.VideoPlayActivity
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Settings
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.timeline.TimelineVideo_Activity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_kanashakthi_questionnaire.*
import kotlinx.android.synthetic.main.fragment_question.*
import javax.inject.Inject


class IntroActivity : BaseActivity() {
    var settingsObj: Settings? = null
    var pref: PrefManager? = null
    var list = ArrayList<QuestionDetails>()

    @Inject
    lateinit var janashakthiQuestionnaireVM: JanashakthiQuestionnaireVM

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        App.getInstance().appComponent.inject(this)

        pref = PrefManager(this)



        subscription.add(janashakthiQuestionnaireVM.getSeaShellsQuestionnaire(pref!!.relateID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar_Intro.visibility = View.VISIBLE }
                .doOnTerminate { progressBar_Intro.visibility = View.GONE }
                .doOnError { progressBar_Intro.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        settingsObj = janashakthiQuestionnaireVM.settingsObj
                        list = janashakthiQuestionnaireVM.list


                        if (settingsObj!!.group_intro == "0") {
                            btn_startquestions.visibility = View.GONE;
                            SeaShellsQuestionnaireActivity.startActivity(this);
                            finish();
                        } else {
                            btn_back_Button.visibility = View.VISIBLE;
                            btn_startquestions.visibility = View.VISIBLE;
                            txt_start_questions_title.text = settingsObj!!.Intro_title;
                            txt_start_questions_desc.text = settingsObj!!.intro_text;

                            if (AppConfig.APP_BRANDING_ID == "life_plus") {
                                btn_startquestions.setTextColor(ContextCompat.getColor(this, R.color.black));
                                btn_startquestions.setBackgroundResource(R.drawable.reports_button_green_selected);
                            }

                            if (settingsObj!!.intro_video.isNotEmpty()) {
                                Glide.with(this).load(settingsObj!!.intro_image).into(img_start_questions)
                                img_play_intro_video.visibility = View.VISIBLE
                            } else {
                                Glide.with(this).load(settingsObj!!.intro_image).into(img_start_questions)
                                img_play_intro_video.visibility = View.GONE
                            }
                        }


                    } else {
                        showMessage("Failed loading questionnaire")
                    }
                }, {
                    progressBar_Intro.visibility = View.GONE
                    showMessage("Failed loading questionnaire")
                })
        )


        btn_startquestions.setOnClickListener {
            SeaShellsQuestionnaireActivity.startActivity(this)
            finish()
        }

        img_start_questions.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Your code here
                if (settingsObj!!.intro_video.isNotEmpty()) {

                    onPlayVideoClicked2()

                }
            }
        })


    }


    fun onPlayVideoClicked2() {
        val intent = Intent(this, VideoPlayActivity::class.java)
        intent.putExtra("URL", settingsObj!!.intro_video)
        startActivity(intent)
    }

    fun onPlayVideoClicked(view: View) {
        val intent = Intent(this, VideoPlayActivity::class.java)
        intent.putExtra("URL", settingsObj!!.intro_video)
        startActivity(intent)
    }

    fun clickBack_introActivity(view: View) {
        finish()
    }

}

package com.ayubo.life.ayubolife.janashakthionboarding.mood_calender

import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_calender_details.*
import javax.inject.Inject

class CalenderDetailsActivity : BaseActivity() {

    var media_url: String? = null

    var id: String = ""

    @Inject
    lateinit var janashakthiQuestionnaireVM: JanashakthiQuestionnaireVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)

        setContentView(R.layout.activity_calender_details)

        media_url = intent.getStringExtra("media_url")

        id = intent.getStringExtra("id").toString()


        Glide.with(this).load(media_url).into(img_backgroud_image)


    }

    fun CalenderFinish(view: View) {
        finish()
    }

    fun onClickMoodNo(view: View) {

        subscription.add(janashakthiQuestionnaireVM.setMoodResponse(id, "0")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar_Cal2.visibility = View.VISIBLE }
            .doOnTerminate { progressBar_Cal2.visibility = View.GONE }
            .doOnError { progressBar_Cal2.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    finish()
                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar_Cal2.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )

    }

    fun onClickMoodYes(view: View) {

        subscription.add(janashakthiQuestionnaireVM.setMoodResponse(id, "1")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar_Cal2.visibility = View.VISIBLE }
            .doOnTerminate { progressBar_Cal2.visibility = View.GONE }
            .doOnError { progressBar_Cal2.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    finish()
                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar_Cal2.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )

    }

}

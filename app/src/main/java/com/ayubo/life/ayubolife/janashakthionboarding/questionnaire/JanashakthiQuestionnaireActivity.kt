package com.ayubo.life.ayubolife.janashakthionboarding.questionnaire

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.JanashakthiAnswer
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionOption
import com.ayubo.life.ayubolife.janashakthionboarding.experts.ExpertsActivity
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.question.QuestionFragment
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_kanashakthi_questionnaire.*
import javax.inject.Inject


class JanashakthiQuestionnaireActivity : BaseActivity() {
    val listFragments = ArrayList<QuestionFragment>()


    @Inject
    lateinit var janashakthiQuestionnaireVM: JanashakthiQuestionnaireVM

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, JanashakthiQuestionnaireActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanashakthi_questionnaire)
        App.getInstance().appComponent.inject(this)


        loadFragments()

    }

    private fun loadFragments() {
        listFragments.clear()
        janashakthiQuestionnaireVM.list.forEach {
            val fragment = QuestionFragment.newInstance(it)
            listFragments.add(fragment)
        }
        pagerViwer.adapter = PagerAdapter(supportFragmentManager, listFragments)
    }

    inner class PagerAdapter(fm: FragmentManager?, var list: List<QuestionFragment>) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int) = list[position]

        override fun getCount() = list.size

    }

    inner class PagerAdapterNew(fm: FragmentManager?, var list: List<QuestionDetails>) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int) = QuestionFragment().setQuestion(list[position])

        override fun getCount() = list.size
    }

    fun moveNext(questionOption: QuestionOption?) {
        //it doesn't matter if you're already in the last item
        if (pagerViwer.currentItem == listFragments.size - 1) {
            //last one, submit answers
            val answers = ArrayList<JanashakthiAnswer>()
            listFragments.forEach {
                answers.add((it.getAnswerObject()))
            }
            subscription.add(janashakthiQuestionnaireVM.submitAnswers(answers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        showMessage("Submited answers")
                        Thread.sleep(2000)
                        finish()
                        ExpertsActivity.startActivity(this@JanashakthiQuestionnaireActivity)
                    } else {
                        showMessage("Failed loading questionnaire")
                    }
                }, {
                    progressBar.visibility = View.GONE
                    showMessage("Failed loading questionnaire")
                })
            )
        } else {
            if (questionOption != null) {
                if (questionOption.id == 0 && !questionOption.skip.isNullOrEmpty()) {
                    janashakthiQuestionnaireVM.list.forEachIndexed { index, questionDetails ->
                        if (questionOption.skip[0].toInt() == questionDetails.id) {
                            pagerViwer.post {
                                pagerViwer.currentItem = index
                            }
                            return
                        }
                    }
                }
            }
            pagerViwer.post {
                pagerViwer.currentItem = pagerViwer.currentItem + 1
            }
        }
    }


    public fun resumQuestion() {
        pagerViwer.currentItem = pagerViwer.currentItem + 1
    }

}

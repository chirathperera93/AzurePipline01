package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.*
import com.ayubo.life.ayubolife.janashakthionboarding.experts.ExpertsActivity
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_kanashakthi_questionnaire.pagerViwer
import kotlinx.android.synthetic.main.activity_kanashakthi_questionnaire.progressBar
import kotlinx.android.synthetic.main.activity_sea_shells_questionnaire.*
import javax.inject.Inject


class SeaShellsQuestionnaireActivity : BaseActivity() {
    var dataObj: QuestionResponse? = null
    var list = ArrayList<QuestionDetails>()
    val listFragments = ArrayList<SeaShellsQuestionFragment>()
    var settingsObj: Settings? = null
    lateinit var pref: PrefManager

    @Inject
    lateinit var janashakthiQuestionnaireVM: JanashakthiQuestionnaireVM

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SeaShellsQuestionnaireActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sea_shells_questionnaire)
        App.getInstance().appComponent.inject(this)
        pref = PrefManager(this)

        settingsObj = Ram.getQuestionSetting()

        if (Ram.getQuestionList() != null) {
            list = Ram.getQuestionList()
            Ram.setQuestionCount(list.size)
            if (list.size > 0) {
                loadFragments()
                list = janashakthiQuestionnaireVM.list
            }
        }


    }

    fun hideAnswerView() {
        pagerViwer.visibility = View.VISIBLE
        fragment_container_seaquestion.visibility = View.GONE
    }

    fun displayAnswerView(optionQ: QuestionOption, isBackVisible: Boolean) {
        pagerViwer.visibility = View.GONE
        fragment_container_seaquestion.visibility = View.VISIBLE

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = NewAnswerFragment()
        val bundle = Bundle()
        bundle.putSerializable("optionQ", optionQ)
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.fragment_container_seaquestion, fragment)
        fragmentTransaction.commit()
    }

    private fun loadFragments() {
        listFragments.clear()

        list.forEach {
            val fragment = SeaShellsQuestionFragment.newInstance(it)
            listFragments.add(fragment)
        }
        pagerViwer.adapter = PagerAdapter(supportFragmentManager, listFragments)
    }

    inner class PagerAdapter(fm: FragmentManager?, var list: List<SeaShellsQuestionFragment>) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int) = list[position]
        override fun getCount() = list.size

    }

    inner class PagerAdapterNew(fm: FragmentManager?, var list: List<QuestionDetails>) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int) =
            SeaShellsQuestionFragment().setQuestion(list[position])

        override fun getCount() = list.size
    }

    public fun uploadQuestionResults() {

        //last one, submit answers
        val answers = ArrayList<JanashakthiAnswer>()
        val answersMulti = ArrayList<JanashakthiMultiOptionAnswer>()

        listFragments.forEach {
            //  answers.add((it.getAnswerObject()))
            if (it.getAnswerMultiObject().id != null && !it.getAnswerMultiObject().id.isEmpty())
                answersMulti.add((it.getAnswerMultiObject()))

        }


        submitMultipleQuestions(answersMulti)

        settingsObj = Ram.getQuestionSetting()


    }


    fun submitMultipleQuestions(answers: List<JanashakthiMultiOptionAnswer>) {


        Log.d("TEXT", ".............................")
        subscription.add(janashakthiQuestionnaireVM.submitAnswersMultiOptions(answers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {

                    val data = janashakthiQuestionnaireVM.submitResponse
                    if (settingsObj!!.group_ending == "1") {
                        FinalAnswerActivity.startActivity(this, data!!.action, data.meta)
                        finish()
                    }
                    if (settingsObj!!.group_ending == "0") {

                        if (janashakthiQuestionnaireVM.submitResponse != null) {
                            val data = janashakthiQuestionnaireVM.submitResponse
                            if (data!!.action.isNotEmpty()) {
                                processAction(data.action, data.meta)
                            } else {
                                if (settingsObj!!.group_ending == "1") {
                                    FinalAnswerActivity.startActivity(this, "", "")
                                    finish()
                                }
                                if (settingsObj!!.group_ending == "0") {

                                    finish()
                                }
                            }

                            finish()
                        } else {

                            showMessage("Submited answers")
                            Thread.sleep(2000)

                            if (pref.isFromJanashakthiDyanamic) {
                                if (settingsObj!!.group_ending == "1") {
                                    FinalAnswerActivity.startActivity(this, "", "")
                                    finish()
                                }
                                if (settingsObj!!.group_ending == "0") {

                                    finish()
                                }
                            } else if (pref.isFromJanashakthiWelcomee) {
                                if (settingsObj!!.group_ending == "1") {
                                    FinalAnswerActivity.startActivity(this, "", "")
                                    finish()
                                } else if (settingsObj!!.group_ending == "0") {
                                    ExpertsActivity.startActivity(this)
                                    finish()
                                }
                            }

                        }
                    }
                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )

    }

    fun submitNormalQuestions(answers: List<JanashakthiAnswer>) {

        Log.d("TEXT", ".............................")
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

                    if (pref.isFromJanashakthiDyanamic) {
                        if (settingsObj!!.group_ending == "1") {
                            FinalAnswerActivity.startActivity(this, "", "")
                            finish()
                        }
                        if (settingsObj!!.group_ending == "0") {

                            finish()
                        }
                    } else if (pref.isFromJanashakthiWelcomee) {
                        if (settingsObj!!.group_ending == "1") {
                            FinalAnswerActivity.startActivity(this, "", "")
                            finish()
                        } else if (settingsObj!!.group_ending == "0") {
                            ExpertsActivity.startActivity(this)
                            finish()
                        }
                    }


                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )
    }


    fun moveNextScreen() {
        //it doesn't matter if you're already in the last item

        list = Ram.getQuestionList()

        if (pagerViwer.currentItem == listFragments.size - 1) {

            uploadQuestionResults()

        } else {

            pagerViwer.post {
                pagerViwer.currentItem = pagerViwer.currentItem + 1
            }
        }
    }

    fun moveNext(questionOption: QuestionOption?) {
        //it doesn't matter if you're already in the last item

        list = Ram.getQuestionList()

        if (pagerViwer.currentItem == listFragments.size - 1) {

            uploadQuestionResults()

        } else {
            if (questionOption != null) {
                if (!questionOption.skip.isNullOrEmpty()) {
                    updateSkipItems(list, questionOption);
                    list = Ram.getQuestionList()
                    pagerViwer.post {
                        pagerViwer.currentItem = getPageView(list, pagerViwer.currentItem + 1)
                    }
                } else {
                    pagerViwer.post {
                        pagerViwer.currentItem = getPageView(list, pagerViwer.currentItem + 1)
                    }
                }
            } else {
                pagerViwer.post {
                    pagerViwer.currentItem = getPageView(list, pagerViwer.currentItem + 1)
                }
            }
        }
    }

    fun getPageView(questionDetailsList: ArrayList<QuestionDetails>, currentIndex: Int): Int {

        questionDetailsList.forEachIndexed { index, questionDetails ->
            if (index >= currentIndex) {
                if (!questionDetails.isSkip) {
                    return currentIndex
                } else {
                    return getPageView(questionDetailsList, currentIndex + 1)
                }
            }


        }

        return currentIndex;
    }

    fun updateSkipItems(
        questionDetailsList: ArrayList<QuestionDetails>,
        questionOption: QuestionOption
    ) {


        questionDetailsList.forEachIndexed { index, questionDetails ->
            questionDetails.isSkip = false;
            for (skipId in questionOption.skip) {

                if (skipId.toInt() == questionDetails.id) {
                    questionDetails.isSkip = true;
                }

            }

        }

        Ram.setQuestionList(questionDetailsList);

    }

    fun showAlert_onBackClick(c: Context, title: String, msg: String) {

        val builder = android.app.AlertDialog.Builder(c)
        val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false)
        builder.setView(layoutView)
        val dialog = builder.create()
        dialog.setCancelable(false)

        val lbl_alert_message = layoutView.findViewById(R.id.lbl_alert_message) as TextView
        lbl_alert_message.text = msg
        val btn_no = layoutView.findViewById(R.id.btn_no) as TextView
        btn_no.setOnClickListener {
            dialog.cancel()

            //   finish();
        }
        val btn_yes = layoutView.findViewById(R.id.btn_yes) as TextView
        btn_yes.setOnClickListener {
            dialog.cancel()
            finish()
            //=================================
        }
        dialog.show()
    }


}




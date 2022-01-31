package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.JanashakthiMultiOptionAnswer
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionOption
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Settings
import com.ayubo.life.ayubolife.utility.Ram
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.janashakthi_multi_options_submit_button.view.*
import kotlinx.android.synthetic.main.janashakthi_multi_options_view.view.*
import kotlinx.android.synthetic.main.janashakthi_option_button_new.view.*
import kotlinx.android.synthetic.main.janashakthi_option_button_new.view.img_new_button_image
import kotlinx.android.synthetic.main.janashakthi_option_button_new.view.main_lay_new_button_view
import kotlinx.android.synthetic.main.janashakthi_option_text.view.*
import kotlinx.android.synthetic.main.seachells_image_button.view.*


const val BUTTON = "button_old"
const val BUTTON_NEW = "button"
const val MULLTOPTION_BUTTON = "m"
const val TEXT = "text"
const val IMAGE = "image"

/**
 * A simple [Fragment] subclass.
 *
 */

class SeaShellsQuestionFragment : Fragment() {
    var settingsObj: Settings? = null
    val buttonSelected = 2
    lateinit var pref: PrefManager
    var oldTextView: TextView? = null
    var isVisibleView: Boolean = false;
    var isViewCreated: Boolean = false;
    //   val answer = JanashakthiAnswer(-1, "")

    //  val answerMulti = JanashakthiMultiOptionAnswer("","",-1, "")
    val answerMulti = JanashakthiMultiOptionAnswer("", "", -1, "", "", null)

    private var questionDetails: QuestionDetails? = null
    fun setQuestion(questionDetails: QuestionDetails): SeaShellsQuestionFragment {
        this.questionDetails = questionDetails
        return this
    }

    companion object {
        fun newInstance(questionDetails: QuestionDetails): SeaShellsQuestionFragment {
            val question = SeaShellsQuestionFragment()
            question.questionDetails = questionDetails
            return question
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_question, container, false)

        settingsObj = Ram.getQuestionSetting()

        //  answerMulti.answer=null


        return view
    }

    fun uploadQuestionResultsLocal() {

    }

    fun Int.toDpToPixel(resource: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resource.getDisplayMetrics()
        ).toInt()
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionsTextContainer.visibility = View.GONE
        optionsButtonContainer.visibility = View.GONE
        txtQuestion.visibility = View.GONE
        textScroll.visibility = View.GONE
        imageQuestionsScroll.visibility = View.GONE
        radioOptionScroll.visibility = View.GONE
        textScroll.visibility = View.GONE


        pref = PrefManager(context)

        answerMulti.user_id = pref.loginUser["uid"]!!

        answerMulti.related_id = settingsObj!!.related_id
        answerMulti.related_type = settingsObj!!.related_type
        isViewCreated = true;
        if (isVisibleView) {
            getData()
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleView = isVisibleToUser;
        if (isVisibleView && isViewCreated) {
            getData()
        }

    }

    //    fun getAnswerObject() : JanashakthiAnswer{
//        return answer
//    }
//       fun Int.toDpToPixel(resource: Resources): Int {
//    return TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            this.toFloat(),
//            resource.getDisplayMetrics()).toInt()
//}

    fun getData() {
        imageViewContainer.removeAllViews()
        optionsButtonContainer.removeAllViews()
        lay_multioption_menu.removeAllViews()
        optionsTextContainer.removeAllViews()
        if (questionDetails != null) {
            answerMulti.question_id = questionDetails?.id!!


            if (questionDetails?.image!!.length == 0) {
                imgQuestionImage.visibility = View.GONE
            } else {
                imgQuestionImage.visibility = View.VISIBLE
                Glide.with(requireContext()).load(questionDetails?.image).into(imgQuestionImage)
                Log.d("Question_Image", questionDetails?.image!!)
            }


            txtQuestion.text = questionDetails?.question

            var total: Int = Ram.getQuestionList().size

            var pos = questionDetails?.position.toString() + " of " + total.toString()
            txt_slider_count.text = pos


            if (questionDetails?.type?.toLowerCase().equals(IMAGE)) {

                radioOptionScroll.visibility = View.GONE
                textScroll.visibility = View.GONE
                imgQuestionImage.visibility = View.VISIBLE
                imageViewContainer.visibility = View.VISIBLE
                imageQuestionsScroll.visibility = View.VISIBLE
                txtQuestion.visibility = View.VISIBLE

                val list: ArrayList<String> = ArrayList()

                if (!questionDetails?.options.isNullOrEmpty()) {
                    questionDetails?.options?.forEachIndexed { index, questionOption ->

                        val imageQuestion = LayoutInflater.from(context)
                            .inflate(R.layout.seachells_image_button, null, false) as LinearLayout

                        val buttonLayoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        buttonLayoutParams.setMargins(0, 10, 0, 40)
                        imageQuestion.layoutParams = buttonLayoutParams

                        imageQuestion.img_answer.tag = questionOption
                        Glide.with(requireContext()).load(questionOption.button_image)
                            .into(imageQuestion.img_answer)


                        imageQuestion.img_answer.setOnClickListener {


                            if (Ram.getQuestionCount() == 1) {
                                uploadQuestionResultsLocal()
                                (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()
                            } else {


                                val optionQ = (it.tag as QuestionOption)


                                list.add(optionQ.id.toString())
                                answerMulti.answer = list
                                answerMulti.id = questionDetails!!.id.toString()
                                answerMulti.question_id = questionDetails!!.id

                                (activity as SeaShellsQuestionnaireActivity).displayAnswerView(
                                    optionQ,
                                    settingsObj!!.back_enabled
                                )

                            }  // closing click listner ..............
                        }
                        imageViewContainer.addView(imageQuestion)
                    }
                }


            }

            if ((!questionDetails?.question_type?.toLowerCase()
                    .equals(MULLTOPTION_BUTTON)) && (questionDetails?.type?.toLowerCase()
                    .equals(BUTTON_NEW))
            ) {

                radioOptionScroll.visibility = View.GONE
                textScroll.visibility = View.GONE
                imageViewContainer.visibility = View.VISIBLE
                imageQuestionsScroll.visibility = View.VISIBLE
                txtQuestion.visibility = View.VISIBLE

                val list: ArrayList<String> = ArrayList()


                if (!questionDetails?.options.isNullOrEmpty()) {
                    questionDetails?.options?.forEachIndexed { index, questionOption ->
                        val imageQuestion = LayoutInflater.from(context).inflate(
                            R.layout.janashakthi_option_button_new,
                            null,
                            false
                        ) as LinearLayout
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        // NORMAL BUTTONS ...
                        layoutParams.setMargins(0, 20, 0, 0)
                        imageQuestion.layoutParams = layoutParams

                        if (questionOption.button_image.isEmpty()) {
                            imageQuestion.img_new_button_image.visibility = View.GONE
                        }
                        Glide.with(requireContext()).load(questionOption.button_image)
                            .into(imageQuestion.img_new_button_image)
                        imageQuestion.img_new_button_text.tag = questionOption
                        imageQuestion.main_lay_new_button_view.tag = questionOption
                        imageQuestion.img_new_button_text.text = questionOption.option


                        //ReSelecting selected answeres Start...
                        if (answerMulti.answer != null) {
                            val dataList = answerMulti.answer

                            for (i in 0 until dataList!!.size) {

                                val answerNum = dataList[i]

                                if (answerNum == questionOption.id.toString()) {
                                    if ((answerMulti.answer!![0] == questionOption.id.toString())) {
                                        imageQuestion.img_new_button_text.setTextColor(
                                            Color.parseColor(
                                                "#FFFFFF"
                                            )
                                        )
                                        imageQuestion.img_new_button_text.text =
                                            questionOption.option
                                        imageQuestion.main_lay_new_button_view.setBackgroundDrawable(
                                            ContextCompat.getDrawable(
                                                imageQuestion.main_lay_new_button_view.context,
                                                R.drawable.reports_button_gradient_selected
                                            )
                                        )
                                    } else {
                                        imageQuestion.img_new_button_text.text =
                                            questionOption.option
                                        imageQuestion.img_new_button_text.visibility = View.VISIBLE
//                                        imageQuestion.main_lay_new_button_view.setBackgroundDrawable(ContextCompat.getDrawable(imageQuestion.main_lay_new_button_view.context, R.drawable.selector_janashakthi_radio_button))
                                        imageQuestion.img_new_button_text.setTextColor(
                                            Color.parseColor(
                                                "#666666"
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        //ReSelecting selected answeres End...


                        //===================================================
                        imageQuestion.main_lay_new_button_view.setOnClickListener {

                            val optionQ = (it.tag as QuestionOption)

                            list.add(optionQ.id.toString())
                            answerMulti.answer = list
                            answerMulti.id = questionDetails!!.id.toString()
                            answerMulti.question_id = questionDetails!!.id

                            if (answerMulti.answer != null) {
                                val dataList = answerMulti.answer

                                for (i in 0 until dataList!!.size) {

                                    val answerNum = dataList[i]

                                    if (answerNum == questionOption.id.toString()) {
                                        if ((answerMulti.answer!![list.size - 1] == questionOption.id.toString())) {
                                            imageQuestion.img_new_button_text.setTextColor(
                                                Color.parseColor(
                                                    "#FFFFFF"
                                                )
                                            )
                                            imageQuestion.img_new_button_text.text =
                                                questionOption.option
                                            imageQuestion.main_lay_new_button_view.setBackgroundDrawable(
                                                ContextCompat.getDrawable(
                                                    imageQuestion.main_lay_new_button_view.context,
                                                    R.drawable.reports_button_gradient_selected
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            if ((Ram.getQuestionCount() == 1) && (optionQ.output_tile.isEmpty())) {

                                uploadQuestionResultsLocal()
                                (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()
                            } else if ((Ram.getQuestionCount() == 1) && (optionQ.output_tile.isNotEmpty())) {
                                //Here is the fixed issue...
                                (activity as SeaShellsQuestionnaireActivity).displayAnswerView(
                                    optionQ,
                                    settingsObj!!.back_enabled
                                )
                            } else {
                                if (optionQ.output_tile.isEmpty()) {
                                    (activity as SeaShellsQuestionnaireActivity).moveNext(optionQ)
                                } else {
                                    (activity as SeaShellsQuestionnaireActivity).displayAnswerView(
                                        optionQ,
                                        settingsObj!!.back_enabled
                                    )
                                }
                            }
                        }
                        //===================================================
                        imageQuestion.img_new_button_text.setOnClickListener {

                            val optionQ = (it.tag as QuestionOption)

                            list.add(optionQ.id.toString())
                            answerMulti.answer = list
                            answerMulti.id = questionDetails!!.id.toString()
                            answerMulti.question_id = questionDetails!!.id
                            if (answerMulti.answer != null) {
                                val dataList = answerMulti.answer

                                for (i in 0 until dataList!!.size) {

                                    val answerNum = dataList[i]

                                    if (answerNum == questionOption.id.toString()) {
                                        if ((answerMulti.answer!![list.size - 1] == questionOption.id.toString())) {
                                            imageQuestion.img_new_button_text.setTextColor(
                                                Color.parseColor(
                                                    "#FFFFFF"
                                                )
                                            )
                                            imageQuestion.img_new_button_text.text =
                                                questionOption.option
                                            imageQuestion.main_lay_new_button_view.setBackgroundDrawable(
                                                ContextCompat.getDrawable(
                                                    imageQuestion.main_lay_new_button_view.context,
                                                    R.drawable.reports_button_gradient_selected
                                                )
                                            )
                                        }
                                    }
                                }
                            }


                            if ((Ram.getQuestionCount() == 1) && (optionQ.output_tile.isEmpty())) {

                                uploadQuestionResultsLocal()
                                (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()

                            } else if ((Ram.getQuestionCount() == 1) && (optionQ.output_tile.isNotEmpty())) {
                                //Here is the fixed issue...

                                (activity as SeaShellsQuestionnaireActivity).displayAnswerView(
                                    optionQ,
                                    settingsObj!!.back_enabled
                                )
                            } else {

                                if (optionQ.output_tile.isEmpty()) {
                                    (activity as SeaShellsQuestionnaireActivity).moveNext(optionQ)
                                } else {
                                    (activity as SeaShellsQuestionnaireActivity).displayAnswerView(
                                        optionQ,
                                        settingsObj!!.back_enabled
                                    )
                                }
                            }

                        }


                        imageQuestion.img_new_button_text.visibility = View.VISIBLE
                        imageQuestion.img_new_button_text.text = questionOption.option

                        imageViewContainer.addView(imageQuestion)

                    }
                }
            }

            if ((questionDetails?.question_type?.toLowerCase()
                    .equals(MULLTOPTION_BUTTON) && (questionDetails?.type?.toLowerCase()
                    .equals(BUTTON_NEW)))
            ) {
                imgQuestionImage.visibility = View.VISIBLE
                txtQuestion.visibility = View.VISIBLE
                textScroll.visibility = View.VISIBLE
                optionsButtonContainer.visibility = View.VISIBLE
                radioOptionScroll.visibility = View.VISIBLE

                val questSize = questionDetails!!.max_selection

                val list: ArrayList<String> = ArrayList()


                if (!questionDetails?.options.isNullOrEmpty()) {

                    questionDetails?.options?.forEachIndexed { index, questionOption ->
                        // val space = inflater.inflate(R.layout.row_item, null)
                        val space = LayoutInflater.from(context)
                            .inflate(R.layout.row_item, null, false) as LinearLayout
                        val radioBtn = LayoutInflater.from(context).inflate(
                            R.layout.janashakthi_multi_options_view,
                            null,
                            false
                        ) as LinearLayout
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        // Multi Option BUTTONS ...
                        //  layoutParams.setMargins(0, 20, 0, 20)

                        radioBtn.layoutParams = layoutParams
                        radioBtn.id = 0

                        questionOption.button_image = "0"
                        radioBtn.txt_multioption_button_text.text = questionOption.option

                        //ReSelecting selected answeres Start...
                        if (answerMulti.answer != null) {
                            val dataList = answerMulti.answer

                            for (i in 0 until dataList!!.size) {

                                val answerNum = dataList[i]

                                if (answerNum == questionOption.id.toString()) {
                                    radioBtn.setBackgroundResource(R.drawable.reports_button_gradient_selected)
                                    radioBtn.img_new_button_image.setBackgroundResource(R.drawable.question_selected_tick)
                                    radioBtn.txt_multioption_button_text.setTextColor(
                                        Color.parseColor(
                                            "#FFFFFF"
                                        )
                                    )
                                    radioBtn.txt_multioption_button_text.text =
                                        questionOption.option

                                } else {
                                    radioBtn.txt_multioption_button_text.text =
                                        questionOption.option
                                }
                            }
                        } else {
                            radioBtn.txt_multioption_button_text.text = questionOption.option

                        }

                        //ReSelecting selected answeres End...

                        radioBtn.tag = questionOption
                        radioBtn.setOnClickListener {

                            val optionQ = (it.tag as QuestionOption)
                            if (optionQ.button_image == "0") {

                                list.add(optionQ.id.toString())

                                radioBtn.setBackgroundResource(R.drawable.reports_button_gradient_selected)
                                radioBtn.img_new_button_image.setBackgroundResource(R.drawable.question_selected_tick)
                                radioBtn.txt_multioption_button_text.setTextColor(Color.parseColor("#FFFFFF"))
                                questionOption.button_image = "1"
                                radioBtn.tag = questionOption

                                if (questSize == "any") {

                                }
                                // Checking Question Count Start
                                else if (list.size == questSize.toInt()) {
                                    answerMulti.id = questionDetails!!.id.toString()
                                    answerMulti.question_id = questionDetails!!.id
                                    answerMulti.answer = list

                                    if (Ram.getQuestionCount() == 1) {
                                        uploadQuestionResultsLocal()
                                        (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()
                                    } else {
                                        (activity as SeaShellsQuestionnaireActivity).moveNextScreen()
                                    }
                                }
                                // Checking Question Count End

                            } else if (optionQ.button_image == "1") {

                                list.remove(optionQ.id.toString())

                                radioBtn.setBackgroundResource(R.drawable.reports_multioptions_button_unselected)
                                radioBtn.img_new_button_image.setBackgroundResource(R.drawable.question_selected_tick_white)
                                radioBtn.txt_multioption_button_text.setTextColor(Color.parseColor("#a1a194"))
                                questionOption.button_image = "0"
                                radioBtn.tag = questionOption


                                if (questSize == "any") {

                                }
                                // Checking Question Count Start
                                else if (list.size == questSize.toInt()) {
                                    answerMulti.id = questionDetails!!.id.toString()
                                    answerMulti.question_id = questionDetails!!.id
                                    answerMulti.answer = list

                                    if (Ram.getQuestionCount() == 1) {
                                        uploadQuestionResultsLocal()
                                        (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()
                                    } else {
                                        (activity as SeaShellsQuestionnaireActivity).moveNextScreen()
                                    }
                                }
                                // Checking Question Count End

                            }
                        }

                        optionsButtonContainer.addView(radioBtn)
                        optionsButtonContainer.addView(space)


                    }


                }

                if (questSize == "any") {

                    val radioBtnNextButton = LayoutInflater.from(context).inflate(
                        R.layout.janashakthi_multi_options_submit_button,
                        null,
                        false
                    ) as ConstraintLayout
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(0, 20, 0, 0)
                    radioBtnNextButton.layoutParams = layoutParams

                    radioBtnNextButton.btn_multioption_button_next.setOnClickListener {

                        answerMulti.id = questionDetails!!.id.toString()
                        answerMulti.question_id = questionDetails!!.id
                        answerMulti.answer = list

                        if (Ram.getQuestionCount() == 1) {
                            uploadQuestionResultsLocal()
                            (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()
                        } else {
                            (activity as SeaShellsQuestionnaireActivity).moveNextScreen()
                        }
                    }
                    lay_multioption_menu.addView(radioBtnNextButton)
                }


            }

            if (questionDetails?.type?.toLowerCase().equals(BUTTON)) {
                imgQuestionImage.visibility = View.VISIBLE
                txtQuestion.visibility = View.VISIBLE
                textScroll.visibility = View.VISIBLE
                optionsButtonContainer.visibility = View.VISIBLE
                radioOptionScroll.visibility = View.VISIBLE

                val list: ArrayList<String> = ArrayList()

                if (!questionDetails?.options.isNullOrEmpty()) {
                    questionDetails?.options?.forEachIndexed { index, questionOption ->
                        val radioBtn = LayoutInflater.from(context)
                            .inflate(R.layout.janashakthi_option_button, null, false) as RadioButton
                        val layoutParams = RadioGroup.LayoutParams(
                            RadioGroup.LayoutParams.MATCH_PARENT, 50.toDpToPixel(resources)
                        )
                        layoutParams.setMargins(0, 16.toDpToPixel(resources), 0, 0)
                        radioBtn.layoutParams = layoutParams
                        radioBtn.id = index
                        radioBtn.tag = questionOption
                        radioBtn.setOnClickListener {

                            if (Ram.getQuestionCount() == 1) {
                                uploadQuestionResultsLocal()
                                (activity as SeaShellsQuestionnaireActivity).uploadQuestionResults()
                            } else {

                                val optionQ = (it.tag as QuestionOption)
                                if ((it as RadioButton).isChecked) {
                                    //  answer.answer = optionQ.id.toString()
                                    list.add(optionQ.id.toString())
                                    answerMulti.answer = list
                                    answerMulti.id = questionDetails!!.id.toString()
                                    answerMulti.question_id = questionDetails!!.id
                                }
                                // CLICK EVENT CODE HERE
                                (activity as SeaShellsQuestionnaireActivity).displayAnswerView(
                                    optionQ,
                                    settingsObj!!.back_enabled
                                )

                            }


                        }
                        radioBtn.text = questionOption.option
                        optionsButtonContainer.addView(radioBtn)
                    }
                }
            }

            if (questionDetails?.type?.toLowerCase().equals(TEXT)) {
                imgQuestionImage.visibility = View.VISIBLE
                txtQuestion.visibility = View.VISIBLE
                optionsTextContainer.visibility = View.VISIBLE
                textScroll.visibility = View.VISIBLE

                val list: ArrayList<String> = ArrayList()

                val text = LayoutInflater.from(context)
                    .inflate(R.layout.janashakthi_option_text, null, false)
                text.btnSubmit.setOnClickListener {

                    list.add(text.etAnswer.text.toString())
                    answerMulti.answer = list
                    answerMulti.id = questionDetails!!.id.toString()
                    answerMulti.question_id = questionDetails!!.id

                    (activity as SeaShellsQuestionnaireActivity).moveNext(null)
                }
                optionsTextContainer.addView(text)
            } else {

            }
        }
    }


    fun getAnswerMultiObject(): JanashakthiMultiOptionAnswer {
        return answerMulti
    }


}

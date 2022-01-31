package com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.question


import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton

import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_question.*
import android.widget.RadioGroup
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.JanashakthiAnswer
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionOption
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireActivity

import kotlinx.android.synthetic.main.janashakthi_option_text.view.*


const val BUTTON = "button"
const val TEXT = "text"

/**
 * A simple [Fragment] subclass.
 *
 */
class QuestionFragment : Fragment() {

    val answer = JanashakthiAnswer(-1, "")
    private var questionDetails: QuestionDetails? = null

    fun setQuestion(questionDetails: QuestionDetails): QuestionFragment {
        this.questionDetails = questionDetails
        return this
    }

    companion object {
        fun newInstance(questionDetails: QuestionDetails): QuestionFragment {
            val question = QuestionFragment()
            question.questionDetails = questionDetails
            return question
        }
    }  fun Int.toDpToPixel(resource: Resources): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                resource.getDisplayMetrics()).toInt()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        optionsTextContainer.visibility = View.GONE
        optionsButtonContainer.visibility = View.GONE
        radioOptionScroll.visibility = View.GONE
        textScroll.visibility = View.GONE
        if (questionDetails != null) {
            answer.question_id = questionDetails?.id!!
            Glide.with(context!!).load(questionDetails?.image).into(imgQuestionImage)
            txtQuestion.text = questionDetails?.question

            if (questionDetails?.type?.toLowerCase().equals(BUTTON)) {
                optionsButtonContainer.visibility = View.VISIBLE
                radioOptionScroll.visibility = View.VISIBLE
                if (!questionDetails?.options.isNullOrEmpty()) {
                    questionDetails?.options?.forEachIndexed { index, questionOption ->
                        val radioBtn = LayoutInflater.from(context).inflate(R.layout.janashakthi_option_button, null, false) as RadioButton
                        val layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT, 50.toDpToPixel(resources))
                        layoutParams.setMargins(0, 16.toDpToPixel(resources), 0, 0)
                        radioBtn.layoutParams = layoutParams
                        radioBtn.id = index
                        radioBtn.tag = questionOption
                        radioBtn.setOnClickListener {
                            val optionQ = (it.tag as QuestionOption)
                            if((it as RadioButton).isChecked){
                                answer.answer = optionQ.id.toString()
                            }
                            (activity as JanashakthiQuestionnaireActivity).moveNext(optionQ)
                        }
                        radioBtn.text = questionOption.option
                        optionsButtonContainer.addView(radioBtn)
                    }
                }
            } else {
                optionsTextContainer.visibility = View.VISIBLE
                textScroll.visibility = View.VISIBLE
                val text = LayoutInflater.from(context).inflate(R.layout.janashakthi_option_text, null, false)
                text.btnSubmit.setOnClickListener {
                    answer.answer = text.etAnswer.text.toString()
                    (activity as JanashakthiQuestionnaireActivity).moveNext(null)
                }
                optionsTextContainer.addView(text)
            }
        }

    }

    fun getAnswerObject() : JanashakthiAnswer{
        return answer
    }


}

package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout

import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionOption
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.fragment_answer2.*
import kotlinx.android.synthetic.main.fragment_answer2.btn_backImgBtn_layout_popup
import kotlinx.android.synthetic.main.fragment_answer2.img_correct_tick
import kotlinx.android.synthetic.main.fragment_answer2.img_jana_answer_back
import kotlinx.android.synthetic.main.fragment_answer2.img_jana_answer_image
import kotlinx.android.synthetic.main.fragment_answer2.lay_back_menu_janas
import kotlinx.android.synthetic.main.fragment_answer2.txt_answerpage_desc
import kotlinx.android.synthetic.main.fragment_answer2.txt_answerpage_title
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.janashakthi_option_button_new.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "optionQ"
private const val ARG_PARAM2 = "param2"


class NewAnswerFragment : Fragment() {

    //var questionOption: QuestionOption? = null

    // TODO: Rename and change types of parameters
    private var questionOption: QuestionOption? = null
    private var isBackVisibale = false
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionOption = it.getSerializable(ARG_PARAM1) as QuestionOption?
            // param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_answer2, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isBackVisibale) {
            lay_back_menu_janas.visibility = View.VISIBLE
        } else {
            lay_back_menu_janas.visibility = View.INVISIBLE
        }

        Glide.with(context!!).load(questionOption!!.output_icon).into(img_correct_tick)
        txt_answerpage_title.text = questionOption!!.output_tile
        txt_answerpage_desc.text = questionOption!!.output_text
        Glide.with(context!!).load(questionOption!!.output_image).into(img_jana_answer_image)

        if (AppConfig.APP_BRANDING_ID == "life_plus") {
            btn_nextquestion.setTextColor(Color.parseColor("#000000"));
            btn_nextquestion.setBackgroundResource(R.drawable.reports_button_green_selected);
        }

        btn_nextquestion.setOnClickListener {
            (activity as SeaShellsQuestionnaireActivity).hideAnswerView()
            (activity as SeaShellsQuestionnaireActivity).moveNext(questionOption)

        }


        btn_backImgBtn_layout_popup.setOnClickListener {
            (activity as SeaShellsQuestionnaireActivity).hideAnswerView()
        }
        img_jana_answer_back.setOnClickListener {
            (activity as SeaShellsQuestionnaireActivity).hideAnswerView()
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        fun newInstance(questionDetails: QuestionOption, isBackVisibal: Boolean): NewAnswerFragment {
            val frag = NewAnswerFragment()
            frag.questionOption = questionDetails
            frag.isBackVisibale = isBackVisibal
            return frag
        }
    }
//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                AnswerFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
}

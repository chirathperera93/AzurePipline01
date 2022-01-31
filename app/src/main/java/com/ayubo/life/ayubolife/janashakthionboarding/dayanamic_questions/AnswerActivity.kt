package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireActivity

class AnswerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)
    }

    fun nextQ(v: View){
        val inst = JanashakthiQuestionnaireActivity()
        inst.resumQuestion()

        finish()
    }
}

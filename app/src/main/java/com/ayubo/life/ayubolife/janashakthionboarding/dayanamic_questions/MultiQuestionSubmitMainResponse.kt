package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import com.google.gson.annotations.SerializedName

data class MultiQuestionSubmitMainResponse

    (

            @SerializedName("result") val result : Int,
            @SerializedName("data") val data : MultiQuestionSubmit
    )


    data class MultiQuestionSubmit (

            @SerializedName("action") val action : String,
            @SerializedName("meta") val meta : String
    )


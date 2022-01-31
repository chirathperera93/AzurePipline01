package com.ayubo.life.ayubolife.janashakthionboarding.data.source.reponses

import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionResponse
import com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model.MoodMainData
import com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model.MoodSetData

data class ResponseGetQuestionnaire(var result:Int, var data:List<QuestionDetails>)

data class ResponseGetSeaShellQuestionnaire(var result:Int, var data: QuestionResponse)

data class ResponseGetMoodData(var result:Int, var data: MoodMainData)

data class ResponseSetMoodData(var result:Int, var data: MoodSetData)
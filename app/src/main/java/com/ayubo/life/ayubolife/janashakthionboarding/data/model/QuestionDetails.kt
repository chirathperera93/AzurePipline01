package com.ayubo.life.ayubolife.janashakthionboarding.data.model

import java.io.Serializable

data class QuestionResponse(var settings: Settings, var questions: List<QuestionDetails>)

data class QuestionDetails(var id: Int,
                           var question: String,
                           var type: String,
                           var options: List<QuestionOption>,
                           var image: String,
                           var question_type: String,
                           var max_selection: String,
                           var position: Int,
                           var isSkip: Boolean = false)

data class QuestionOption(var id: Int,
                          var option: String,
                          var skip: List<String>,
                          var correct: String,
                          var button_image: String,
                          var answer_image: String,
                          var output_image: String,
                          var output_tile: String,
                          var output_icon: String,
                          var output_text: String) : Serializable

data class JanashakthiMultiAnswer(var answer: List<String>)

data class JanashakthiAnswer(var question_id: Int, var answer: String)

//data class JanashakthiMultiOptionAnswer(var id:String,var user_id:String,var question_id:Int,var answer:String)
data class JanashakthiMultiOptionAnswer(var id: String, var user_id: String, var question_id: Int, var related_id: String,
                                        var related_type: String, var answer: List<String>?)

data class Settings(var group_id: Int,
                    var related_id: String,
                    var related_type: String,
                    var group_intro: String,
                    var Intro_title: String,
                    var intro_text: String,
                    var intro_image: String,
                    var intro_video: String,
                    var video_thumbnail: String,
                    var group_ending: String,
                    var ending_heading: String,
                    var ending_text: String,
                    var ending_image: String,
                    var back_enabled: Boolean)


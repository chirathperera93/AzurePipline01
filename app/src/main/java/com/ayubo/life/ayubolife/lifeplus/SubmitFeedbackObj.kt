package com.ayubo.life.ayubolife.lifeplus

class SubmitFeedbackObj(
        var rating: Int,
        var description: String,
        var cateogories: ArrayList<FeedBackObj>,
        var reference: String,
        var reference_id: String
) {
}
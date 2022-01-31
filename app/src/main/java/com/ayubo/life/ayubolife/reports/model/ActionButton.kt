package com.ayubo.life.ayubolife.reports.model


import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActionButton : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("conversation_id")
    @Expose
    var conversationId: String? = null
    @SerializedName("action")
    @Expose
    var action: String? = null
    @SerializedName("color")
    @Expose
    var color: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("meta")
    @Expose
    var meta: String? = null
    @SerializedName("post_action")
    @Expose
    var postAction: String? = null
    @SerializedName("question_id")
    @Expose
    var questionId: String? = null
    @SerializedName("answer_id")
    @Expose
    var answerId: String? = null
    @SerializedName("refresh")
    @Expose
    var refresh: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    companion object {
        private const val serialVersionUID = 4347569848767826680L
    }

}
package com.ayubo.life.ayubolife.book_videocall.model

import com.ayubo.life.ayubolife.channeling.model.Expert


data class BookVideoCustomObject(
        val title: String,
        val list: ArrayList<BookVideoCustomData>?)

data class BookVideoCustomData(
        val id: String,
        val title: String,
        val speciality: String,
        val name: String,
        val picture: String,
        val next: String,
        val locations: List<NewLocations>,

        val video: NewVideo,
        val channel: NewChannel,
        val review: NewReview,
        val ask: NewAsk)

data class NewLocations(
        val id: String,
        val name: String,
        val fee: String,
        val fee_value: Int,
        val next_available: String
)

data class NewVideo(
        val enable: Int,
        val meta: String
)

data class NewChannel(
        val enable: Int,
        val meta: String
)

data class NewReview(
        val enable: Int,
        val meta: String
)

data class NewAsk(
        val enable: Int,
        val meta: String

)

data class DoctorList(
        val speciality: String,
        val doctors: ArrayList<Expert>
)
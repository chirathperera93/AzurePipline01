package com.ayubo.life.ayubolife.programs.data.model

import com.google.gson.annotations.SerializedName

enum class ProgramTypes {

        @SerializedName("PROGRAMS_PAST") PROGRAMS_PAST,
        @SerializedName("PROGRAMS_PRESENT") PROGRAMS_PRESENT,
        @SerializedName("PROGRAMS_FUTURE") PROGRAMS_FUTURE,
        @SerializedName("PROGRAMS_HEADING") PROGRAMS_HEADING

}


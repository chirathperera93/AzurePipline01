package com.ayubo.life.ayubolife.prochat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PopulateValues(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var value: String,
    var valueType: Int
)
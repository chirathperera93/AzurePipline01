package com.ayubo.life.ayubolife.reports.upload

import android.graphics.Bitmap


data class AddedReportsEntity(
        var id: String?,
        var image: Bitmap?

) {
     constructor() : this("0",null)
}


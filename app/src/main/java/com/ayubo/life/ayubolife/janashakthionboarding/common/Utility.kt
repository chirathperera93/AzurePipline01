package com.ayubo.life.ayubolife.janashakthionboarding.common

import android.content.res.Resources
import android.util.TypedValue

class Utility {


    fun Int.toDpToPixel(resource: Resources): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                resource.getDisplayMetrics()).toInt()
    }

}
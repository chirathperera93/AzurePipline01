package com.ayubo.life.ayubolife.map_challange.model

data class JournalEntity(

        var lat: Double = 0.toDouble(),
        var longitude: Double = 0.toDouble(),
        var distance: String? = null,
        var steps: String? = null,
        var action: String? = null,
        var meta: String? = null,
        var flag_act: String? = null,
        var flag_deact: String? = null,
        var stepstonext: String? = null,
        var nextcity: String? = null,
        var city: String? = null,
        var citymsg: String? = null,
        var cityimg: String? = null,
        var disableimg: String? = null,
        var zooml: String? = null,
        var wc: String? = null,
        var bubble_txt: String? = null,
        var bubble_link: String? = null,
        var auto_hide: String? = null
) {
    constructor() : this(
            0.0, 0.0, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null, null,
            null
    )
}
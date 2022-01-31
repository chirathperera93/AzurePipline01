package com.ayubo.life.ayubolife.janashakthionboarding.data.source.reponses

import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Expert

data class ResponseExpertsList(var result:Int, var data:ResponseData)

data class ResponseData(var experts:List<Expert>)
package com.ayubo.life.ayubolife.programs.data.model


data class MainData(var header:Header,var setting:Setting,var list:List<Program>)


data class Setting(var image_url:String,var program_name:String,var status:String)
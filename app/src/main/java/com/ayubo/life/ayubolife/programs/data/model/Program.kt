package com.ayubo.life.ayubolife.programs.data.model



    data class Program(
            var id : Int,
            var heading : String,
            var sub_heading : String,
            var icon : String,
            var text : String,
            var is_read : String,
            var action : String,
            var meta : String,
            var type : String,
            var progress_status : String,
            var progress_progress : String,
            var timestamp : String,
            var disabled : String,
            var tags : List<Tag>,
            var created_at : String,
            var updated_at : String
          )




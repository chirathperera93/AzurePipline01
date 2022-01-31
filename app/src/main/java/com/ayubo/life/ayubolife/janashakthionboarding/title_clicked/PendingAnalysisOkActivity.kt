package com.ayubo.life.ayubolife.janashakthionboarding.title_clicked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import kotlinx.android.synthetic.main.activity_pending_analysis_ok_.*

class PendingAnalysisOkActivity : AppCompatActivity() {
    var pref: PrefManager? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_analysis_ok_)

        pref = PrefManager(this)

        var userName = pref!!.loginUser.get("name")

        var userid="Dear $userName"
        txt_user_name_pend.text=userid
    }

    fun clickOk(view: View){
        finish()
    }

    fun clickToFinish_PendingAnalysisOkActivity(view: View){
        finish()
    }

}

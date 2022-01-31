package com.ayubo.life.ayubolife.janashakthionboarding.webview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayubo.life.ayubolife.R
import kotlinx.android.synthetic.main.activity_janashakthi_web_view.*

class JanashakthiWebViewActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context:Context, url:String){
            val intent = Intent(context, JanashakthiWebViewActivity::class.java)
            intent.putExtra("url", "https://www.youtube.com/watch?v=iYOyzYONlBQ")
            context.startActivity(intent)
        }
    }

    private var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janashakthi_web_view)
        this.url = intent.getStringExtra("url")
        webView.loadUrl("https://www.youtube.com/watch?v=iYOyzYONlBQ")
    }
}

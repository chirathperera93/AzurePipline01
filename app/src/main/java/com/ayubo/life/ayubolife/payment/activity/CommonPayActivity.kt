package com.ayubo.life.ayubolife.payment.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.*
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import kotlinx.android.synthetic.main.activity_common_pay.*

class CommonPayActivity : BaseActivity() {

    var web_url: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_pay)



        web_url = intent.getStringExtra("URL").toString()

        val webSettings = webpay_commonview.settings
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        //  webpay_commonview.addJavascriptInterface(WebAppInterface(this), "Android")


        webpay_commonview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        webpay_commonview.webViewClient = Callback()
        webpay_commonview.isLongClickable = true
        webpay_commonview.requestFocus(View.FOCUS_DOWN)


        //    val newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), web_url)
        Log.d("URL..........", web_url)
        webpay_commonview.loadUrl(web_url)
    }


    inner class Callback : WebViewClient() {
        internal var timeout = true

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            super.shouldOverrideUrlLoading(view, url)

            println("=========start of comon web view Callback====================$url")

            val intent = Intent(this@CommonPayActivity, CommonPayActivity::class.java)
            intent.putExtra("URL", url)
            startActivity(intent)
            //   finish()
            return false


        }


        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {

            println("============onPageStarted================$url")
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            println("============onPageFinished================")
            timeout = false

        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            val htmlFilename = "error.html"
            val mgr = baseContext.assets


        }


    }
}

package com.ayubo.life.ayubolife.revamp.v1.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.*
import com.ayubo.life.ayubolife.body.MyHealthDataActivity
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.health.ExpertViewActivity
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity
import com.ayubo.life.ayubolife.health.RXViewActivity
import com.ayubo.life.ayubolife.payment.EXTRA_CAMPAIGN_ID
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS
import com.ayubo.life.ayubolife.revamp.v1.model.JoinCampaignRequestBody
import com.ayubo.life.ayubolife.revamp.v1.view_model.CampaignJoinViewModel
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_campaign_join.*
import javax.inject.Inject

class CampaignJoinActivity : BaseActivity() {

    @Inject
    lateinit var campaignJoinViewModel: CampaignJoinViewModel

    var campaignId: String = ""
    lateinit var pref: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign_join)
        App.getInstance().appComponent.inject(this)
        pref = PrefManager(this);
        readExtras()
        getCampaignDetail()

        btn_join_challenge.setOnClickListener {
            doJoinCampaign()
        }

    }

    fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_CAMPAIGN_ID)) {
            campaignId = bundle.getSerializable(EXTRA_CAMPAIGN_ID) as String
        }
    }

    private fun doJoinCampaign() {

        val joinCampaignRequestBody = JoinCampaignRequestBody(campaignId, "")

        subscription.add(
            campaignJoinViewModel.joinCampaign(joinCampaignRequestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { campaignJoinLoading.visibility = View.VISIBLE }
                .doOnTerminate { campaignJoinLoading.visibility = View.GONE }
                .doOnError { campaignJoinLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        finish()
                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    campaignJoinLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }


    private fun getCampaignDetail() {
        subscription.add(
            campaignJoinViewModel.getCampaignDetails(campaignId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { campaignJoinLoading.visibility = View.VISIBLE }
                .doOnTerminate { campaignJoinLoading.visibility = View.GONE }
                .doOnError { campaignJoinLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        val s = campaignJoinViewModel.campaignData

                        print(s)
                        join_campaign_actions.visibility = View.VISIBLE
                        Glide.with(applicationContext)
                            .load(campaignJoinViewModel.campaignData.image_url)
                            .into(challenge_image)

                        txt_challenge_heading.text = campaignJoinViewModel.campaignData.heading



                        webView_join_challange.loadDataWithBaseURL(
                            "",
                            campaignJoinViewModel.campaignData.htmlbody,
                            "text/html",
                            "UTF-8",
                            ""
                        );

//                        try {
//
//                            if (Utility.isInternetAvailable(this@CampaignJoinActivity)) {
//
//                                val webSettings: WebSettings = webView_join_challange.settings
//                                webSettings.domStorageEnabled = true
//                                webSettings.javaScriptEnabled = true
//                                webSettings.allowFileAccess = true
//
//                                webView_join_challange.settings
//                                    .setRenderPriority(WebSettings.RenderPriority.HIGH)
//                                webView_join_challange.webViewClient = Callback()
//                                webView_join_challange.isLongClickable = true
//
////                                webView.setOnLongClickListener(new View . OnLongClickListener () {
////                                    @Override
////                                    public boolean onLongClick(View v) {
////                                        return true;
////                                    }
////                                });
//
//
//                            } else {
//
//                            }
//                        } catch (e: Exception) {
//                            System.out.println("Timeline Webview....................." + e);
//
//                        }

                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    campaignJoinLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }

    inner class Callback : WebViewClient() {
        var timeout: Boolean = true

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            super.shouldOverrideUrlLoading(view, url)
            val perURL: String = url
            val perURL_flag: Boolean = false
            var condition: Boolean = false

            System.out.println("=============================")
            System.out.println("=========start of comon web view Callback====================")
            System.out.println("=============================")


            System.out.println("===========CommonView===Redireted URL====================" + url)
            if (url.contains(MAIN_URL_APPS)) {
                if (url.contains("openInView=true")) {
                    condition = true
                    val intent: Intent =
                        Intent(this@CampaignJoinActivity, CommonWebViewActivity::class.java)
                    intent.putExtra("URL", url)
                    startActivity(intent)
                    return condition
                } else if (url.contains("openInBrowser=true")) {
                    condition = true
                    val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                    return condition
                } else if (url.contains("group_adventure_invite")) {
                    condition = true
                    val intent: Intent =
                        Intent(this@CampaignJoinActivity, ContactDetailsActivity::class.java)
                    intent.putExtra("URL", url)
                    startActivity(intent)
                    return condition
                } else if (url.contains("enable_proximity")) {
                    return true
                } else {
                    condition = false
                    return condition
                }
            }

            if (url.contains("/disable_back")) {
                condition = true
                return condition
            } else if (url.contains("/enable_back")) {
                condition = true
                return condition
            } else if (url.contains("openInBrowser=true")) {
                condition = true
                val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
                return condition
            } else if (url.contains("openmedicaltrack")) {
                condition = true
                val intent: Intent = Intent(
                    this@CampaignJoinActivity,
                    MyHealthDataActivity::class.java
                )
                startActivity(intent)
                return condition
            } else if (url.contains("group_adventure_invite")) {
                condition = true
                val intent: Intent = Intent(
                    this@CampaignJoinActivity,
                    ContactDetailsActivity::class.java
                )
                intent.putExtra("URL", url)
                startActivity(intent)
                return condition
            } else if (url.contains("openInSame=true")) {
                condition = false
                return condition
            } else if (url.contains("soloAdventureMap")) {
            } else if (url.contains("PC_Hospitals&action=connect_user&type=mobile")) {
                System.out.println("========CommonView_Second_WebviewActivity===Redireted URL====================" + url)
                condition = true
                val intent: Intent = Intent(
                    baseContext,
                    CommonView_Second_WebviewActivity::class.java
                )
                intent.putExtra("URL", url)
                startActivity(intent)
                return condition
            } else if (url.contains("action=setFamilyMember")) {
                condition = true
                val intent: Intent =
                    Intent(baseContext, CommonView_Second_WebviewActivity::class.java)
                intent.putExtra("URL", url)
                startActivity(intent)
                return condition
            } else if (url.contains("sendToBaseView")) {
                condition = true
                pref.fromSendToBaseView = "yes"
                val intent: Intent = SetDiscoverPage().getDiscoverIntent(baseContext)
                startActivity(intent)
                return condition
            } else if (url.contains("water_intake")) {
                condition = true
                val intent: Intent = Intent(this@CampaignJoinActivity, WaterActivity::class.java)
                startActivity(intent)
                return condition
            } else {
                condition = true
                val intent: Intent = Intent(
                    this@CampaignJoinActivity,
                    CommonWebViewActivity::class.java
                )
                intent.putExtra("URL", url)
                startActivity(intent)
                return condition
            }

            if (url.contains("subscriptionView")) {
                condition = true
                val intent: Intent =
                    Intent(this@CampaignJoinActivity, Medicine_ViewActivity::class.java)
                startActivity(intent)
                return condition
            } else if (url.contains("medicineView")) {
                condition = true
                val intent: Intent = Intent(this@CampaignJoinActivity, RXViewActivity::class.java)
                startActivity(intent)
                return condition
            } else if (url.contains("scheduleExpertView")) {
                condition = true
                val intent: Intent =
                    Intent(this@CampaignJoinActivity, ExpertViewActivity::class.java)
                startActivity(intent)
                return condition
            } else if (url.contains(MAIN_URL_APPS)) {
                condition = true
                webView_join_challange.loadUrl(url)
                return condition
            } else if (url.contains("/water_intake")) {
                condition = true
                val intent: Intent = Intent(this@CampaignJoinActivity, WaterActivity::class.java)
                startActivity(intent)
                finish()
                return condition
            } else if (url.contains("/water_intake_window_call")) {
                condition = true
                val intent: Intent = Intent(this@CampaignJoinActivity, WaterActivity::class.java)
                startActivity(intent)
                return condition
            } else if (url.contains("/disable_back")) {
                condition = true
                return condition
            } else if (url.contains("/enable_back")) {
                condition = true
                return condition
            } else if (url.contains("openInBrowser=true")) {
                condition = true
                val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
                return condition
            } else if (url.contains(ApiClient.BASE_URL)) {
                condition = true
                val intent: Intent =
                    Intent(this@CampaignJoinActivity, CommonWebViewActivity::class.java)
                intent.putExtra("URL", url)
                startActivity(intent)
                return condition
            } else {
                condition = true
                val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
                return condition
            }
        }


        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
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


        }


    }
}
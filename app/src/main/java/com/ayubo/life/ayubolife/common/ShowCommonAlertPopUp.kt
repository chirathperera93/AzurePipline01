package com.ayubo.life.ayubolife.common

import android.app.Activity
import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.ActionMetaForFragment
import com.ayubo.life.ayubolife.timeline.models.PopupMainData
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowCommonAlertPopUp {


    fun showAlertPopUp(context: Context, activity: Activity, dataObj: PopupMainData) {
        val dialogView: android.app.AlertDialog

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView: View = inflater.inflate(R.layout.common_show_alert_popup, null, false)

        val display: Display = activity.windowManager.defaultDisplay
        val width: Int = display.width - 100
        val ratio: Double = ((width)) / 25.0
        val height: Int = (ratio * 40).toInt()

//        val layoutParams: RelativeLayout.LayoutParams =
//            RelativeLayout.LayoutParams(width, height)
//        layoutView.layoutParams = layoutParams


        builder.setView(layoutView)
        dialogView = builder.create()
        dialogView.setCancelable(false)


//        val requestOptionsSmall: RequestOptions =
//            RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
        val backgroundImageView: ImageView = layoutView.findViewById(R.id.backgroundImageView)
        val mainCardView: CardView = layoutView.findViewById(R.id.mainCardView)

        Glide.with(context).load(dataObj.imageUrl)
            .centerCrop()
            .into(backgroundImageView)

        val closePopUpLinearLayout: LinearLayout =
            layoutView.findViewById(R.id.closePopUpLinearLayout)


        closePopUpLinearLayout.setOnClickListener {
            dialogView.cancel()
        }

        mainCardView.setOnClickListener {
            ActionMetaForFragment().processAction(
                context,
                dataObj.button.action,
                dataObj.button.meta
            )
            dialogView.cancel()
        }

        dialogView.show()
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialogView.window?.attributes)
        layoutParams.width = width
        layoutParams.height = height

        dialogView.window!!.attributes = layoutParams
        setReadPopup(context, dataObj.id.toString())
    }

    fun setReadPopup(context: Context, id: String) {
        val pref: PrefManager = PrefManager(context)
        val apiService: ApiInterface = ApiClient.getNewApiClient().create(ApiInterface::class.java)
        val call: Call<Any>? = apiService.setReadPopup(pref.userToken, id)

        if (call != null) {
            call.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val status: Boolean = response.isSuccessful
                    if (status) {
                        if (response.body() != null) {

                        }
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    println("========t======" + t)
                }
            });
        }
    }
}
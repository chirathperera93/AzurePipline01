package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.app.ProgressDialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.bumptech.glide.Glide

class MyBadgeAdapter(
    private val context: Context,
    private val seriesList: ArrayList<MyBadge>,
    val windowManager: WindowManager
) : RecyclerView.Adapter<MyBadgeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBadgeAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_layout_for_badges, parent, false)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(context);

        val width = deviceScreenDimension.displayWidth
        v.layoutParams = ViewGroup.LayoutParams((width - 16) / 3, (width - 16) / 3)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, seriesList[position], windowManager)

    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, badgeImage: MyBadge, windowManager: WindowManager) {
            val progressDialogForSubscribe: ProgressDialog;
            progressDialogForSubscribe = ProgressDialog(context);


            val badgeImageView = itemView.findViewById(R.id.badgeImageView) as ImageView

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(context);
            val width = deviceScreenDimension.displayWidth

            val layparam: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams((width - 16) / 3, (width - 16) / 3);
            layparam.setMargins(0, 0, 0, 0);


            if (badgeImage.status.equals("active")) {
                Glide.with(context).load(badgeImage.image_url_active).into(badgeImageView)
            } else {
                Glide.with(context).load(badgeImage.image_url_inactive).into(badgeImageView)
            }



            itemView.setOnClickListener {
                System.out.println(badgeImage.id)



                progressDialogForSubscribe.show();
                progressDialogForSubscribe.getWindow()?.setGravity(Gravity.CENTER);
                progressDialogForSubscribe.setContentView(R.layout.badge_pop_up);

                progressDialogForSubscribe.getWindow()?.setBackgroundDrawableResource(
                    android.R.color.transparent
                );


                var actualImageString = "";
                var instructionDetail = "";
                var congratulationText = "";

                if (badgeImage.status.equals("active")) {
                    actualImageString = badgeImage.image_url_active
                    instructionDetail = "Continue good works"
                    congratulationText = "Congratulations !"
                    progressDialogForSubscribe.window?.findViewById<LinearLayout>(R.id.badge_share_btn)?.visibility =
                        View.VISIBLE


                } else {
                    instructionDetail = ""
                    congratulationText = ""
                    actualImageString = badgeImage.image_url_inactive
                    progressDialogForSubscribe.window?.findViewById<LinearLayout>(R.id.badge_share_btn)?.visibility =
                        View.GONE
                }
                progressDialogForSubscribe.window?.findViewById<ImageView>(R.id.badgeImageView)
                    ?.let { it1 -> Glide.with(context).load(actualImageString).into(it1) }
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para1)
                    ?.setText(badgeImage.description)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para2)
                    ?.setText(badgeImage.title)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_congratulation_textview)
                    ?.setText(congratulationText)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para3)
                    ?.setText(instructionDetail)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.view_my_badges)
                    ?.setOnClickListener {
                        progressDialogForSubscribe.dismiss()
                    }

            }

        }


    }
}
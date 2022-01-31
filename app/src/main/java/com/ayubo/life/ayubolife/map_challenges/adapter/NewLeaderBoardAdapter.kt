package com.ayubo.life.ayubolife.map_challenges.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.fragments.CircleTransform
import com.ayubo.life.ayubolife.map_challenges.model.MemberList
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.programs.ChatViewTypes
import com.ayubo.life.ayubolife.programs.PROGRAMS_SEND_A_KITE
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.leaderboard_cell.view.*


const val PROGRAMS_PAST = 1


class NewLeaderBoardAdapter(
    val context: Context,
    val list: List<MemberList>?,
    var releaseAll: Boolean = false
) :
    RecyclerView.Adapter<NewLeaderBoardAdapter.BaseHolder>() {


    var gson = Gson()

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, @ChatViewTypes viewType: Int): BaseHolder {

        when (viewType) {

            PROGRAMS_SEND_A_KITE -> {
                return LeaderBoardViewHolder(parent)
            }
            else -> {
                return LeaderBoardViewHolder(parent)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() = list!!.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (releaseAll) {
            holder.release()
        }
        holder.bind(list!![position])
    }


    inner class LeaderBoardViewHolder(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.leaderboard_cell)) {

        @SuppressLint("SetTextI18n")
        override fun bind(sessionObj: Any) = with(itemView) {

            val obj = sessionObj as MemberList

            if ((obj.highlight_color != null) && (obj.highlight_color.isNotEmpty())) {
                main_cell_background.setBackgroundColor(Color.parseColor(obj.highlight_color))
            }

            flag_image_view.visibility = View.GONE
            txt_position.text = obj.position.toString()
            txt_username.text = obj.full_name
            txt_points_text.text = obj.main_value
            txt_sub_points.text = obj.sub_value

            val splited: List<String> = txt_points_text.text.split(" ");

            if (splited[0] == "0") {
                flag_image_view.visibility = View.VISIBLE;
                flag_image_view.setImageResource(R.drawable.icon_material_flag)
                txt_green_tag.text = "Steps not synced successfully";
            }

            if (obj.main_value!!.isNotEmpty()) {
                System.out.println("==========mv=========" + obj.main_value)
                System.out.println("==========mi=========" + obj.main_icon)
                Glide.with(context).load(obj.main_icon).into(img_main_value_icon)
            }
            if (obj.sub_value!!.isNotEmpty()) {
                System.out.println("==========sv=========" + obj.sub_value)
                System.out.println("==========si=========" + obj.sub_icon)
                Glide.with(context).load(obj.sub_icon).into(img_sub_value_icon)
            }



            if (obj.tags.isEmpty()) {
                txt_green_tag.visibility = View.INVISIBLE
                txt_orange_tag.visibility = View.INVISIBLE
            } else {
                if (obj.tags.size == 1) {
                    if (obj.tags[0].text!!.isNotEmpty()) {
                        val gd = GradientDrawable()
                        gd.setColor(Color.parseColor(obj.tags[0].color))
                        gd.cornerRadius = 45f
                        txt_green_tag.setBackgroundDrawable(gd)
                        txt_green_tag.text = obj.tags[0].text
                        txt_green_tag.visibility = View.VISIBLE
                    } else {
                        txt_green_tag.visibility = View.INVISIBLE
                    }
                    txt_orange_tag.visibility = View.INVISIBLE
                } else if (obj.tags.size == 2) {
                    if (obj.tags[0].text!!.isNotEmpty()) {
                        val gd = GradientDrawable()
                        gd.setColor(Color.parseColor(obj.tags[0].color))
                        gd.cornerRadius = 45f
                        txt_green_tag.setBackgroundDrawable(gd)
                        txt_green_tag.text = obj.tags[0].text
                        txt_green_tag.visibility = View.VISIBLE
                    } else {
                        txt_green_tag.visibility = View.INVISIBLE
                    }
                    if (obj.tags[1].text!!.isNotEmpty()) {
                        val gd = GradientDrawable()
                        gd.setColor(Color.parseColor(obj.tags[1].color))
                        gd.cornerRadius = 45f
                        txt_orange_tag.setBackgroundDrawable(gd)
                        txt_orange_tag.text = obj.tags[1].text
                        txt_green_tag.visibility = View.VISIBLE
                    } else {
                        txt_orange_tag.visibility = View.INVISIBLE
                    }
                }
            }

            val options = RequestOptions
                .bitmapTransform(CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context).load(obj.picture)
                .apply(options)
                .into(img_userpicture)



            return@with
        }
    }

    override fun getItemViewType(position: Int): Int {

        return PROGRAMS_SEND_A_KITE

    }


    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(conversation: Any) = with(itemView) {
        }

        open fun release() {

        }
    }


}

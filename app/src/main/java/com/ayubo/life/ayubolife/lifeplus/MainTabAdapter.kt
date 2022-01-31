package com.ayubo.life.ayubolife.lifeplus

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import androidx.recyclerview.widget.RecyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lifeplus_program_tab_cell.view.*


const val MAIN_CELL = 1
const val SUB_CELL = 2

class MainTabAdapter(val context: Context, val list: ArrayList<ProgramList>) :
    RecyclerView.Adapter<MainTabAdapter.BaseHolder>() {



    override fun onBindViewHolder(holder: MainTabAdapter.BaseHolder, position: Int) {
        holder.bind(list[position])
    }


    fun release() {
        notifyDataSetChanged()
    }
    var onitemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onProcessAction(action: String,meta: String)
    //    fun onProcessAction(action: String,meta: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {
            MAIN_CELL -> {
                return MainProgressView(parent)
            }
            else -> {
                return MainProgressView(parent)
            }
        }
    }

    override fun getItemCount() = list.size

//    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
//        holder.bind(list[position])
//    }

    override fun getItemViewType(position: Int): Int {

//       if (list[position].specialPlace==null){
//            return MAIN_CELL
//        }else{
//            return SUB_CELL
//        }
        return MAIN_CELL


    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }
        open fun release() {
        }
    }

    inner class MainProgressView(parent: ViewGroup) :

        BaseHolder(parent.inflate(R.layout.lifeplus_program_tab_cell)) {

        @SuppressLint("SetTextI18n")
        override fun bind(item: Any) = with(itemView) {

          val data= item as ProgramList


           val mainImage = data.bg_img!!.replace("zoom_level", "xxxhdpi")
            Glide.with(context).load(mainImage).into(img_tab_program_icon)
            txt_tab_program_heading.text=data.title
            txt_tab_program_sub_heading.text=data.description
            click_action_btn.text=data.btn_text

            click_action_btn.setOnClickListener {
                if(onitemClickListener!=null){
                    onitemClickListener?.onProcessAction(data.action!!,data.meta!!)
                }
            }

            return@with
        }
    }




}

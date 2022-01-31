package com.ayubo.life.ayubolife.map_challange.adapter


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.map_challange.TYPE_BANNER
import com.ayubo.life.ayubolife.map_challange.TYPE_LOCATION
import com.ayubo.life.ayubolife.map_challange.model.LocationData
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.rest.ApiClient
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_challenge_journal.view.*


const val TYPE_LEADERBOARD = 1


class JournalAdapter(
    val context: Context,
    val list: ArrayList<LocationData>,
    val currentSteps: Int
) :
    RecyclerView.Adapter<JournalAdapter.BaseHolder>() {

    var onItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onProcessAction(action: String, meta: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {

            TYPE_LOCATION -> {
                return ActiveCityVH(parent)
            }
            TYPE_BANNER -> {
                return ActiveCityVH(parent)
            }
            else -> {
                return ActiveCityVH(parent)
            }


        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        val card = list[position]
        var cellType: Int = 1

//        if (card.type=="main_card") {
//            cellType= TYPE_MAIN
//        }
//        if (card.type=="location") {
//            cellType= TYPE_LOCATION
//        }


        return cellType
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }

        open fun release() {
        }
    }


    inner class ActiveCityVH(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.raw_challenge_journal)) {

        override fun bind(item: Any) = with(itemView) {

            val data: LocationData = item as LocationData


            header_title_journal.text = data.city
            child_location_desc.text = data.citymsg
            //  text_description_journal.text=data.citymsg
            val bitm: Bitmap
            if (data.steps <= currentSteps) {
                bitm = BitmapFactory.decodeResource(resources, R.drawable.landscape_blue_icon)
            } else {
                bitm = BitmapFactory.decodeResource(resources, R.drawable.landscape_grey_icon)
            }

            header_icon_journal.setImageBitmap(bitm)


            if (data.steps <= currentSteps) {
                Glide.with(context).load(data.cityimg).into(child_location_img)
            } else {
                val disLink =
                    ApiClient.MAIN_URL_APPS + "static/girls_vs_boys2/mount_lavinia/banner_disable.jpg"
                Glide.with(context).load(disLink).into(child_location_img)
            }
            header_title_journal.setOnClickListener {
                onItemClickListener!!.onProcessAction(data.action!!, data.link!!)
            }
            child_location_img.setOnClickListener {
                if (data.action != null) {
                    onItemClickListener!!.onProcessAction(
                        data.action,
                        if (data.link == null) "" else data.link
                    )
                }

            }
            more_journal.setOnClickListener {
                onItemClickListener!!.onProcessAction(data.action!!, data.link!!)
            }

            return@with
        }
    }


}



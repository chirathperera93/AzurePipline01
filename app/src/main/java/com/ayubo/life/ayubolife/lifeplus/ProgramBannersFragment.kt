package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_program_banners.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProgramBannersFragment : Fragment() {


    var nabber: BannerItem? = null

    companion object {
        fun newInstance(nabber: BannerItem): ProgramBannersFragment {
            val expertDetailsFragment = ProgramBannersFragment()
            expertDetailsFragment.nabber = nabber
            return expertDetailsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program_banners, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_banner_heading.text = nabber?.item_name
        txt_banner_desc.text = nabber?.item_short_description
        Glide.with(context!!).load(nabber?.banner_image).into(img_bg_image)
        txt_program_price.text = nabber?.item_sub_text
        main_content_frag.setOnClickListener {
            try {
                (activity as LifePlusProgramActivity).processDefaultAction(nabber!!.action, nabber!!.meta)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
            }

        }

    }

}

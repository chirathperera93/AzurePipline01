package com.ayubo.life.ayubolife.janashakthionboarding.experts.expertdetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Expert
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_expert_details.*
import kotlin.math.exp


class ExpertDetailsFragment : Fragment() {

    var expert:Expert? = null

    companion object {
        fun newInstance(expert: Expert): ExpertDetailsFragment {
            val expertDetailsFragment = ExpertDetailsFragment()
            expertDetailsFragment.expert = expert
            return expertDetailsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expert_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtName.text = expert?.name
        txtSpecialty.text = expert?.speciality
        Glide.with(context!!).load(expert?.profile_pic).into(imgProfile)


        if(expert?.slmc?.isNotEmpty()!!){
            txtSlmc.text = expert?.slmc
        }else{
            val emptySLMC:String=""
            txtSlmc.text = emptySLMC
        }

    }

}

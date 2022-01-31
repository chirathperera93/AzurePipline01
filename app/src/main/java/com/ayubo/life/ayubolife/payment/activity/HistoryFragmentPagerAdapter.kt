package com.ayubo.life.ayubolife.payment.activity

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ayubo.life.ayubolife.payment.fragment.DoctorServiceHistory
import com.ayubo.life.ayubolife.payment.fragment.MedicineHistory
import com.ayubo.life.ayubolife.payment.fragment.ProductsHistory
import com.ayubo.life.ayubolife.payment.fragment.ProgramsHistory

/**
 * Created by Chirath Perera on 2021-10-28.
 */
class HistoryFragmentPagerAdapter(
    context: Context,
    fm: FragmentManager,
    private var totalTabs: Int,
    private var changePosition: Int = -1
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {


        var currentPosition: Int = position

        if (changePosition != -1) {
            currentPosition = changePosition
        }



        when (currentPosition) {

            0 -> {
                return DoctorServiceHistory()
            }
            1 -> {
                return ProductsHistory()
            }
            2 -> {
                return ProgramsHistory()
            }
            3 -> {
                return MedicineHistory()

            }
            else -> return DoctorServiceHistory()
        }


    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
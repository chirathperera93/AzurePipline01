package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ayubo.life.ayubolife.revamp.v1.fragment.LastMonthStepFragment
import com.ayubo.life.ayubolife.revamp.v1.fragment.LastWeekMyStepFragment
import com.ayubo.life.ayubolife.revamp.v1.fragment.LastYearStepFragment
import com.ayubo.life.ayubolife.revamp.v1.fragment.TodayMyStepFragment

class MyStepViewPagerAdapter(
    context: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                TodayMyStepFragment()
            }
            1 -> {
                LastWeekMyStepFragment()
            }
            2 -> {
                LastMonthStepFragment()
            }
            3 -> {
                LastYearStepFragment()
            }
            else -> TodayMyStepFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
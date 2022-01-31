package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ayubo.life.ayubolife.revamp.v1.fragment.WellnessHeroesLeaderboard
import com.ayubo.life.ayubolife.revamp.v1.fragment.WellnessHeroesOverview

class WellnessHeroesViewPagerAdapter(
    context: Context,
    fm: FragmentManager,
    private var totalTabs: Int,
    var metaValue: String = ""
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val metaArray = metaValue.split(";")
        val str1 = metaArray[0]
        val str2 = metaArray[1]


        print(str1)
        print(str2)

        return when (position) {
            0 -> {
                return WellnessHeroesOverview(str1);
            }
            1 -> {
                WellnessHeroesLeaderboard(str2)
            }
            else -> {
                return WellnessHeroesOverview(str1);
            }
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
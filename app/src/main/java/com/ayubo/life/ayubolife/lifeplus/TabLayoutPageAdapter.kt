package com.ayubo.life.ayubolife.lifeplus


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.NewProfilePrivileges
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.NewProfileDashboard
import com.ayubo.life.ayubolife.lifeplus.NewRecords.NewProfileRecords
import com.ayubo.life.ayubolife.lifeplus.NewToDo.NewProfileToDo
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.revamp.v1.RevampV1DashboardFragment

class TabLayoutPageAdapter(
    context: Context,
    fm: FragmentManager,
    private var totalTabs: Int,
    var isMainDashboard: Boolean = true
) :
    FragmentPagerAdapter(fm) {


    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {

            0 -> {
                return if (isMainDashboard) {
                    RevampV1DashboardFragment()
                } else {
                    NewProfileDashboard()
                }

            }
            1 -> {
                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Home_Calendar", null)
                }
                return NewProfileToDo()
            }
            2 -> {
                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Home_Cards", null)
                }
                return NewProfilePrivileges()
            }
            3 -> {
                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Home_Records", null)
                }
                return NewProfileRecords()

            }
            else -> return NewProfileDashboard()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
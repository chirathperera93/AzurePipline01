package com.ayubo.life.ayubolife.lifeplus

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class TabsFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val tabNames: ArrayList<String>
    private val fragments: ArrayList<Fragment>
    private var dataList: List<ProgramList>? = null

    init {
        tabNames = ArrayList()
        fragments = ArrayList()
    }

    fun add(fragment: Fragment, title: String, datList: List<ProgramList>) {
        tabNames.add(title)
        fragments.add(fragment)
        dataList = datList
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabNames[position]
    }
}
package com.ayubo.life.ayubolife.revamp.v1.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.revamp.v1.adapter.MyStepViewPagerAdapter
import com.ayubo.life.ayubolife.revamp.v1.adapter.WellnessHeroesViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_my_step.*

class MyStepActivity : BaseActivity() {
    lateinit var tabLayout: TabLayout;
    lateinit var viewPager: ViewPager;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_step)

        tabLayout = findViewById<TabLayout>(R.id.mainTabLayoutForMyStep);
        viewPager = findViewById<ViewPager>(R.id.mainViewPagerForMyStep);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Month"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Year"));
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL;

        val myStepViewPagerAdapter: MyStepViewPagerAdapter =
            MyStepViewPagerAdapter(
                this,
                supportFragmentManager,
                tabLayout.tabCount
            );



        viewPager.adapter = myStepViewPagerAdapter;
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

        imageViewForMyStepBackBtn.setOnClickListener {
            finish()
        }
    }
}
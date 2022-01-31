package com.ayubo.life.ayubolife.revamp.v1.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.revamp.v1.YTD_DATA
import com.ayubo.life.ayubolife.revamp.v1.adapter.WellnessHeroesViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_wellness_heroes.*

class WellnessHeroesActivity : BaseActivity() {

    lateinit var tabLayout: TabLayout;
    lateinit var viewPager: ViewPager;
    var meta: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness_heroes)
        readExtras()


        tabLayout = findViewById<TabLayout>(R.id.mainTabLayout);
        viewPager = findViewById<ViewPager>(R.id.mainViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Leaderboard"));
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL;

        val wellnessHeroesViewPagerAdapter: WellnessHeroesViewPagerAdapter =
            WellnessHeroesViewPagerAdapter(
                this,
                supportFragmentManager,
                tabLayout.tabCount,
                meta
            );



        viewPager.adapter = wellnessHeroesViewPagerAdapter;
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


        imageViewForMainBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(YTD_DATA)) {
            meta = bundle.getSerializable(YTD_DATA) as String

        }
    }
}
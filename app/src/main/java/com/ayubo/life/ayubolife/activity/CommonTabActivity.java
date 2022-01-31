package com.ayubo.life.ayubolife.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.Challenges_Fragment;
import com.ayubo.life.ayubolife.fragments.HomeFragment;
import com.ayubo.life.ayubolife.fragments.MedicalReports_Fragment;
import com.ayubo.life.ayubolife.fragments.TimeLineFragment;
import com.ayubo.life.ayubolife.fragments.VedioGallery_Fragment;

import java.util.ArrayList;
import java.util.List;

public class CommonTabActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private com.ayubo.life.ayubolife.utility.NonSwipeableViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tab);

        viewPager = (com.ayubo.life.ayubolife.utility.NonSwipeableViewPager) findViewById(R.id.viewpager_nnn);

        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIconstttt();
        //   setupTabIcons();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
                // loadHomeFragment();

                int ss=tab.getPosition();
                System.out.println("===============SELECTED TABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB================"+ss);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int ss=tab.getPosition();
                System.out.println();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int ss=tab.getPosition();
                System.out.println();
            }
        });



    }
    private void setupTabIconstttt() {

        TextView tabOne = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText(R.string.topmenu_home);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab1_selector, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText(R.string.topmenu_gallery);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab2_selector, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabFour = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText(R.string.topmenu_challenge);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab3_selector, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_tab, null);
        tabFive.setText(R.string.topmenu_history);
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab5_selector, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFive);

    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("............................................."+position);
            System.out.println("............................................."+position);
            System.out.println("............................................."+position);
            System.out.println("............................................."+position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new TimeLineFragment(), "HOME");
        adapter.addFrag(new VedioGallery_Fragment(), "Blog");
        adapter.addFrag(new Challenges_Fragment(), "Challenge");
        adapter.addFrag(new MedicalReports_Fragment(), "History");

        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
    }
}

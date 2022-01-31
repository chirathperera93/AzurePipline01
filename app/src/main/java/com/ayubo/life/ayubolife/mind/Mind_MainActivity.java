package com.ayubo.life.ayubolife.mind;

import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;

import java.util.ArrayList;
import java.util.List;

public class Mind_MainActivity extends AppCompatActivity implements
        DeStressFragment.OnFragmentInteractionListener,
        ExploreFragment.OnFragmentInteractionListener{
    private TabLayout tabLayout;
    ImageButton btn_backImgBtn;
    private com.ayubo.life.ayubolife.utility.NonSwipeableViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind__main);


        try {
            viewPager = (com.ayubo.life.ayubolife.utility.NonSwipeableViewPager) findViewById(R.id.health_main_viewpager_nnn);
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setText("EXPLORE");
            tabLayout.getTabAt(1).setText("DE-STRESS");
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {


                    viewPager.setCurrentItem(tab.getPosition());
                    // loadHomeFragment();

                    int ss = tab.getPosition();
                    System.out.println("===============SELECTED TABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB================" + ss);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    int ss = tab.getPosition();
                    System.out.println();
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    int ss = tab.getPosition();
                    System.out.println();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        try {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new ExploreFragment(), "EXPLORE");
            adapter.addFrag(new DeStressFragment(), "DE-STRESS");
            adapter.notifyDataSetChanged();
            viewPager.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

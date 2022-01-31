package com.ayubo.life.ayubolife.health;

import android.content.Intent;
import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;

import java.util.ArrayList;
import java.util.List;

public class Health_MainActivity extends AppCompatActivity implements
        Health_TracksFragment.OnFragmentInteractionListener,
        Health_ReportsFragment.OnFragmentInteractionListener{
   ImageButton btn_image_expert_button,btn_image_medicine_button,btn_image_upload_button;
    Button btn_text_medicine_button,btn_text_expert_button,btn_text_upload_button;
    private TabLayout tabLayout;
    ImageButton btn_backImgBtn;

    private com.ayubo.life.ayubolife.utility.NonSwipeableViewPager viewPager;

    void localConstructor(){
        btn_image_expert_button=(ImageButton)findViewById(R.id.btn_image_expert_button);
        btn_image_medicine_button=(ImageButton)findViewById(R.id.btn_image_medicine_button);
        btn_image_upload_button=(ImageButton)findViewById(R.id.btn_image_upload_button);

        btn_text_expert_button=(Button)findViewById(R.id.btn_text_expert_button);
        btn_text_medicine_button=(Button)findViewById(R.id.btn_text_medicine_button);
        btn_text_upload_button=(Button)findViewById(R.id.btn_text_upload_button);

        btn_image_expert_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Health_MainActivity.this, ExpertViewActivity.class);
                startActivity(in);
            }
        });
        btn_text_expert_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Health_MainActivity.this, ExpertViewActivity.class);
                startActivity(in);
            }
        });
        btn_image_medicine_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Health_MainActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
            }
        });
        btn_text_medicine_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Health_MainActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
            }
        });
        btn_image_upload_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent in = new Intent(Health_MainActivity.this, RXViewActivity.class);
                startActivity(in);
            }
        });
        btn_text_upload_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Health_MainActivity.this, RXViewActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health__main);

        try {
            localConstructor();
            }
            catch(Exception e){
              e.printStackTrace();
        }

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_health__mainbackImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("=============Back Clicked=====in Health main activity==========");
                finish();
            }
        });

        try {
            viewPager = (com.ayubo.life.ayubolife.utility.NonSwipeableViewPager) findViewById(R.id.health_main_viewpager_nnn);
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setText("TRACK");
            tabLayout.getTabAt(1).setText("REPORTS");
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
        adapter.addFrag(new Health_TracksFragment(), "TRACK");
        adapter.addFrag(new Health_ReportsFragment(), "REPORTS");
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

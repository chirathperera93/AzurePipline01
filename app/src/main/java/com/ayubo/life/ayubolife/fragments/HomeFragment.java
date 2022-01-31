package com.ayubo.life.ayubolife.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.utility.Ram;

import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    boolean isSearchDoctor1_Fragment=false;
    boolean isSearchDoctor2_Fragment=false;
    boolean isSearchDoctor3_Fragment=false;
    boolean isSearchDoctor4_Fragment=false;
    boolean isSearchDoctor5_Fragment=false;
    boolean isSearchDoctor6_Fragment=false;

    boolean isPrescription1_Fragment=false;
    boolean isPrescription2_Fragment=false;
    boolean isPrescription3_Fragment=false;

    boolean isPres_Gallery_Fragment=false;

    String hasToken, loginType;
    SwipeRefreshLayout mySwipeRefreshLayout;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;

    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;
    DatabaseHandler db;
    String encodedHashToken;
    private boolean isCurrentCameraFront;
    private boolean isLocalVideoFullScreen;
    String url=null;
    protected Handler mainHandler;
    boolean st=false;
    private TextView connectionStatusLocal;
    String consultant_id,location_id;
    WebView browser = null;
    //  final String TAG = "WebviewActivity";
    String requestedURL = null;
    // private ConnectionTimeoutHandler timeoutHandler = null;
    int PAGE_LOAD_PROGRESS = 0;
    final String KEY_REQUESTED_URL = "requested_url";
    final String CALLBACK_URL = "success";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout underlin_1,underlin_2,underlin_3,underlin_4,underlin_5,underlin_6;
    ImageButton btn_1,btn_2,btn_3,btn_reports,btn_challenges;


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private com.ayubo.life.ayubolife.utility.NonSwipeableViewPager viewPager;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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
    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);

        if(position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(1.0F - Math.abs(position));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_simple_tabs,
                container, false);
//        View view = inflater.inflate(R.layout.fragment_home,
//                container, false);

        viewPager = (com.ayubo.life.ayubolife.utility.NonSwipeableViewPager) view.findViewById(R.id.viewpager_nnn);

        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);

        //Ram.setIsChallenge_Home(true);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs_in_home_fragment);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIconstttt();
        viewPager.setCurrentItem(0);
//        try {
//
//            String tabName = Ram.getTopMenuTabName();
////
//            if (tabName != null) {
//                if (tabName.equals("history")) {
//                    viewPager.setCurrentItem(3);
//                }
//                if (tabName.equals("home")) {
//                    viewPager.setCurrentItem(0);
//                }
//            }
//        }catch(Exception e)
//        {
//           e.printStackTrace();
//        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());


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
        return view;
    }

    private void setupTabIconstttt() {

     //   TextView textview = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
     //   textview.setText(R.string.topmenu_feed);
       // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab1_selector, 0, 0);
     //   tabLayout.getTabAt(0).setCustomView(textview);

      //  TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
      //  tabTwo.setText(R.string.topmenu_challenge);
       // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab2_selector, 0, 0);
     //   tabLayout.getTabAt(1).setCustomView(tabTwo);

//        TextView tabFour = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
//        tabFour.setText(R.string.topmenu_challenge);
//       // tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab3_selector, 0, 0);
//        tabLayout.getTabAt(2).setCustomView(tabFour);
//
//        TextView tabFive = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
//        tabFive.setText(R.string.topmenu_history);
//       // tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab5_selector, 0, 0);
//        tabLayout.getTabAt(3).setCustomView(tabFive);

    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFrag(new TimeLineFragment(), "FEED");
        adapter.addFrag(new Challenges_Fragment(), "CHALLENGES");
      //  adapter.addFrag(new Challenge_Holder_Fragment(), "Challenges");
     //   adapter.addFrag(new Challenges_Fragment(), "Challenges");
     //   adapter.addFrag(new MedicalReports_Fragment(), "History");

        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void loadHomeFragment() {
                // update the main content by replacing fragments
                HomeFragment fragment = new HomeFragment();
               // Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "home");
                fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

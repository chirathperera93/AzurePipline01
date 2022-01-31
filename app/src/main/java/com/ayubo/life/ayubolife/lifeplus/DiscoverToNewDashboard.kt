package com.ayubo.life.ayubolife.lifeplus


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import android.widget.ImageView
import android.widget.RelativeLayout
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.prochat.base.BaseActivity

class DiscoverToNewDashboard : BaseActivity() {

    var TAG_HOME: String = "DiscoverToNewDashboard";
    var CURRENT_TAG: String = TAG_HOME;
    lateinit var headerBar: RelativeLayout;
    lateinit var newBackBtnArrow: ImageView;
    lateinit var dashboard_meta: String;
    lateinit var pref: PrefManager;

    lateinit var fragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover_to_new_dashboard)
        val intent: Intent = getIntent();
        dashboard_meta = intent.getStringExtra("dashboard_meta").toString();
        headerBar = findViewById(R.id.header_bar);
        newBackBtnArrow = findViewById(R.id.new_back_btn_arrow);
        pref = PrefManager(this);
        newBackBtnArrow.setOnClickListener {
            finish()
        }
        loadDiscoverFragment();
    }

    private fun loadDiscoverFragment() {
        fragment = getDiscoverFragment();
        val fragmentTransaction: FragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

    }

    private fun getDiscoverFragment(): Fragment {
        val discoverFragment: ProfileNewFragment = ProfileNewFragment();
        val args: Bundle = Bundle();
        args.putBoolean("isShowBottomBar", false);
        args.putString("dashboard_meta", dashboard_meta);
        discoverFragment.arguments = args;
        return discoverFragment;
    }
}
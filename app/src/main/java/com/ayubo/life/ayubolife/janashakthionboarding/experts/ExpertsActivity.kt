package com.ayubo.life.ayubolife.janashakthionboarding.experts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.experts.expertdetails.ExpertDetailsFragment
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_experts.*
import javax.inject.Inject


class ExpertsActivity : BaseActivity() {
    @Inject
    lateinit var expertsVM: ExpertsVM

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ExpertsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experts)

        App.getInstance().appComponent.inject(this)

        subscription.add(expertsVM.getExpertList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }.subscribe({
                if (it.isSuccess) {
                    loadFragments()
                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )
    }

    val listFragments = ArrayList<ExpertDetailsFragment>()
    private fun loadFragments() {
        listFragments.clear()


        val tabLayout = findViewById(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(pager, true)

        expertsVM.list.forEach {
            val fragment = ExpertDetailsFragment.newInstance(it)
            listFragments.add(fragment)
        }
        pager.adapter = PagerAdapter(supportFragmentManager, listFragments)
    }

    inner class PagerAdapter(fm: FragmentManager?, var list: List<ExpertDetailsFragment>) :
        FragmentStatePagerAdapter(
            fm!!
        ) {
        override fun getItem(position: Int) = list[position]

        override fun getCount() = list.size

    }

    fun goToNextClick(view: View) {
        MedicalUpdateActivity.startActivity(this)
        finish()
    }

}

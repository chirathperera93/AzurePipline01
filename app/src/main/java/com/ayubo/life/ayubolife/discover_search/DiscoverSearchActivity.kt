package com.ayubo.life.ayubolife.discover_search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_discover_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DiscoverSearchActivity : BaseActivity(), DiscoverSearchAdapter.OnItemClickListener {
    override fun onDiscoverSearch(id: String, name: String, type: String) {
//        NewDiscoverActivity.startActivity(this, true, id, name, type)
        LifePlusProgramActivity.startActivity(this, true, id, name, type)
        finish()
    }


    lateinit var seachedDataList: ArrayList<DiscoverSearchData>

    @Inject
    lateinit var discoverSearchVM: DiscoverSearchVM

    private var discoverSearchAdapter: DiscoverSearchAdapter? = null

    companion object {
        fun startActivity(context: Context?) {
            try {
                context!!.startActivity(Intent(context, DiscoverSearchActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover_search)

        App.getInstance().appComponent.inject(this)

        var searchKey: String

        subscription.add(RxTextView.afterTextChangeEvents(edt_search_value_main_comp)
            .debounce(
                1000,
                TimeUnit.MILLISECONDS
            ) // Better store the value in a constant like Constant.DEBOUNCE_SEARCH_REQUEST_TIMEOUT
            .map { it.editable().toString() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchKey = it
                if (it.length > 1) {
                    search("tag", it)
                }
            })
        // search("tag","pressure")

        back_layoutview_search.setOnClickListener {
            finish()
        }
        img_backBtn_bvcds.setOnClickListener {
            finish()
        }


    }


    private fun loadSearchAction() {

        var searchKey: String

        subscription.add(RxTextView.afterTextChangeEvents(edt_search_value_main_comp)
            .debounce(
                3000,
                TimeUnit.MILLISECONDS
            ) // Better store the value in a constant like Constant.DEBOUNCE_SEARCH_REQUEST_TIMEOUT
            .map { it.editable().toString() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchKey = it
                if (it.length > 2) {
                    search("test", it)
                }
            })
    }


    private fun search(searchType: String, text: String) {

        subscription.add(discoverSearchVM.getSearchResults(searchType, text).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog_search_main.visibility = View.VISIBLE }
            .doOnTerminate { proDialog_search_main.visibility = View.GONE }
            .doOnError { proDialog_search_main.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {

                    seachedDataList =
                        discoverSearchVM.mainResponse!! as ArrayList<DiscoverSearchData>


                    if (seachedDataList.isNotEmpty()) {


                        for (c in 0 until seachedDataList.size) {

                            val name = seachedDataList[c].name
                            Log.d("............", name)

                            val mlayoutManager = LinearLayoutManager(this@DiscoverSearchActivity)


                            discoverSearchAdapter = DiscoverSearchAdapter(
                                this@DiscoverSearchActivity,
                                seachedDataList
                            )
                            discoverSearchAdapter!!.onitemClickListener =
                                this@DiscoverSearchActivity
                            recycleview_discover_search.apply {
                                layoutManager = mlayoutManager as RecyclerView.LayoutManager?
                                adapter = discoverSearchAdapter
                            }
                        }
                    } else {
                        Log.d("......No Data......", "Empty")
                        seachedDataList.clear()
                        discoverSearchAdapter =
                            DiscoverSearchAdapter(this@DiscoverSearchActivity, seachedDataList)


                        discoverSearchAdapter!!.notifyDataSetChanged()
                        recycleview_discover_search.apply {
                            adapter = discoverSearchAdapter
                        }

                    }
                } else {
                    showMessage("Failed loading data")
                }
            }, {
                it.printStackTrace()
                showMessage("Failed loading data")
            })
        )
    }

}

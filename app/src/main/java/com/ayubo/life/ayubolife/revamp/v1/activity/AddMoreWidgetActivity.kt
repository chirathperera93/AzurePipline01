package com.ayubo.life.ayubolife.revamp.v1.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.revamp.v1.adapter.AddMoreWidgetAdapter
import com.ayubo.life.ayubolife.revamp.v1.model.Widget
import com.ayubo.life.ayubolife.revamp.v1.view_model.AddMoreWidgetViewModal
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_more_widget.*
import javax.inject.Inject

class AddMoreWidgetActivity : BaseActivity(), AddMoreWidgetAdapter.OnWidgetItemClickListener {
    private var addMoreWidgetAdapter: AddMoreWidgetAdapter? = null

    @Inject
    lateinit var addMoreWidgetViewModal: AddMoreWidgetViewModal;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_widget)
        App.getInstance().appComponent.inject(this)

        mainImageViewForWidgetTopic.setOnClickListener {
            finish()
        }

        loadWidgets()
    }


    private fun loadWidgets() {

        subscription.add(
            addMoreWidgetViewModal.getWidgets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addMoreWidgetLoading.visibility = View.VISIBLE }
                .doOnTerminate { addMoreWidgetLoading.visibility = View.GONE }
                .doOnError { addMoreWidgetLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        textViewForTopic.text = addMoreWidgetViewModal.widgetData.main_heading


                        try {


                            add_more_widgets.layoutManager =
                                LinearLayoutManager(
                                    baseContext,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            var addMoreWidgetItemsArrayList = ArrayList<Widget>()

                            addMoreWidgetItemsArrayList = addMoreWidgetViewModal.widgetData.widgets




                            addMoreWidgetAdapter =
                                AddMoreWidgetAdapter(
                                    baseContext,
                                    addMoreWidgetItemsArrayList,
                                    false
                                )

                            try {
                                addMoreWidgetAdapter!!.onWidgetItemClickListener =
                                    this@AddMoreWidgetActivity
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                            add_more_widgets.adapter = addMoreWidgetAdapter


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    addMoreWidgetLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun widgetItemClick(widget: Widget) {
        print(widget)

        val activatedStatus = if (widget.activated_status) "1" else "0"


        subscription.add(
            addMoreWidgetViewModal.updateWidgetStatus(
                widget.id,
                activatedStatus
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addMoreWidgetLoading.visibility = View.VISIBLE }
                .doOnTerminate { addMoreWidgetLoading.visibility = View.GONE }
                .doOnError { addMoreWidgetLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {


//                        val filteredWidget =
//                            addMoreWidgetViewModal.widgetData.widgets.filter { ele -> ele.id == addMoreWidgetViewModal.postWidgetData.id.toInt() }
//
//                        print(filteredWidget)
//
//                        filteredWidget[0].activated_status =
//                            addMoreWidgetViewModal.postWidgetData.activated_status == "1"
//
//                        val x = addMoreWidgetViewModal.widgetData.widgets
//
//                        print(x)

//                        addMoreWidgetAdapter!!.notifyDataSetChanged()

                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    addMoreWidgetLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )


    }
}
package com.ayubo.life.ayubolife.payment.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.payment.adapter.PaymentSummaryAdapter
import com.ayubo.life.ayubolife.payment.vm.PaymentConfirmVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment_summary_view.*
import javax.inject.Inject

class PaymentSummaryViewActivity : BaseActivity() {

    @Inject
    override lateinit var paymentConfirmVM: PaymentConfirmVM;

    private var paymentSummaryAdapter: PaymentSummaryAdapter? = null;

    lateinit var pref: PrefManager;

    var actionMeta = "";
    var metaValue = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_summary_view)
        App.getInstance().appComponent.inject(this)

        activity_payment_done_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (actionMeta.isEmpty()) {
//                    val i = Intent(this@PaymentSummaryViewActivity, NewHomeWithSideMenuActivity::class.java);
//                    val i = Intent(this@PaymentSummaryViewActivity, LifePlusProgramActivity::class.java);
                    val i = SetDiscoverPage().getDiscoverIntent(baseContext);
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
                    startActivity(i);
                } else {
                    processAction(actionMeta, metaValue);
                }

            }

        })
    }

    override fun onResume() {
        super.onResume()
        readExtras()
    }

    private fun readExtras() {
        getData();
    }

    private fun getData() {
        val pref = PrefManager(this);

        subscription.add(paymentConfirmVM.getPaymentSummary(pref.paymentId.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { activity_payment_summary_progressBar.visibility = View.VISIBLE }
            .doOnTerminate { activity_payment_summary_progressBar.visibility = View.GONE }
            .doOnError { activity_payment_summary_progressBar.visibility = View.GONE }
            .subscribe({

                if (it.isSuccess) {

                    val mlayoutManagerNew = LinearLayoutManager(this@PaymentSummaryViewActivity)
                    actionMeta = paymentConfirmVM.paymentSummaryObj.data.action;
                    metaValue = paymentConfirmVM.paymentSummaryObj.data.meta;
                    payment_summary_image_layout.setBackgroundResource(0);
                    if (!paymentConfirmVM.paymentSummaryObj.data.payment_success) {
                        payment_summary_image_layout.setBackgroundResource(R.drawable.pay_success_fail);
                    } else {
                        payment_summary_image_layout.setBackgroundResource(R.drawable.pay_success);
                    }

                    if (paymentConfirmVM.paymentSummaryObj.data.heading.isNotEmpty()) {
                        payment_summary_success_text.text =
                            paymentConfirmVM.paymentSummaryObj.data.heading;
                    }

                    if (paymentConfirmVM.paymentSummaryObj.data.table.isNotEmpty()) {
                        paymentSummaryAdapter = PaymentSummaryAdapter(
                            this@PaymentSummaryViewActivity,
                            paymentConfirmVM.paymentSummaryObj
                        )
                        activity_payment_summary_list_recycler_view.apply {
                            layoutManager = mlayoutManagerNew as RecyclerView.LayoutManager?
                            adapter = paymentSummaryAdapter
                        }
                    }
                } else {
                    showMessage(R.string.service_loading_fail);
                }


            }, {
                activity_payment_summary_progressBar.visibility = View.GONE
                showMessage(R.string.service_loading_fail);
            })
        );
    }
}
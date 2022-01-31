package com.ayubo.life.ayubolife.payment.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.payment.EXTRA_PAYMENT_META
import com.ayubo.life.ayubolife.payment.adapter.OtherPaymentOptionsNewAdapter
import com.ayubo.life.ayubolife.payment.adapter.PaymentActivityAdapter
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.payment.vm.PaymentActivityVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment_options.listPaymentOptions
import javax.inject.Inject

class PaymentActivity : BaseActivity(), PaymentActivityAdapter.OnItemClickListener, OtherPaymentOptionsNewAdapter.OnItemClickListener {

    //    private lateinit var loggerFB: AppEventsLogger
    lateinit var pref: PrefManager;
    override fun onPaymentProcess(obj: PriceList) {
        onPaymentProcessed(obj)
    }


    //    override fun onPaymentProcess(obj: PriceList) {
//        onPaymentProcess(obj)
//    }
    override fun onProcessAction(action: String, meta: String) {
        processAction(action, meta)
    }


    @Inject
    lateinit var paymentActivityVM: PaymentActivityVM

    private var paymentActivityAdapter: PaymentActivityAdapter? = null

    private var otherPaymentOptionsNewAdapter: OtherPaymentOptionsNewAdapter? = null

    private var termsAction = "";
    private var termsMeta = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        App.getInstance().appComponent.inject(this)

        termsAndConditions.visibility = View.GONE
        pref = PrefManager(this);
        initView()

    }

    override fun onResume() {
        super.onResume()

        readExtras()

    }


    private fun getData(meta: String) {

        subscription.add(paymentActivityVM.getPaymentMethods(meta).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({

                    if (it.isSuccess) {

                        val mlayoutManager = LinearLayoutManager(this@PaymentActivity);
                        val mlayoutManagerNew = LinearLayoutManager(this@PaymentActivity);
                        val linearLayout = findViewById<LinearLayout>(R.id.main_payment_header_image_new_linear_textView);
                        linearLayout.removeAllViews();

                        ley_title.visibility = View.GONE;
                        other_payment_ley_title.visibility = View.GONE;
                        listPaymentOptions.visibility = View.GONE;
                        other_payment_list_recycler_view.visibility = View.GONE;

                        val r = paymentActivityVM.mainResponse

                        print(r)


                        if (
                                paymentActivityVM.mainResponse.image != ""
                                ||
                                paymentActivityVM.mainResponse.header != ""
                                ||
                                paymentActivityVM.mainResponse.sub_heading != ""
                                ||
                                paymentActivityVM.mainResponse.description != ""
                        ) {

                            main_payment_header_new.visibility = View.VISIBLE;




                            if (paymentActivityVM.mainResponse.image != null) {
                                val imageView = ImageView(this)
                                Glide.with(this).load(paymentActivityVM.mainResponse.image).into(imageView);
                                linearLayout.addView(imageView)
                            }


                            if (paymentActivityVM.mainResponse.header != null) {
                                main_payment_header_new_textView.text = paymentActivityVM.mainResponse.header;
                            }


                            if (paymentActivityVM.mainResponse.sub_heading != null) {
                                main_payment_sub_header_new_textView.text = paymentActivityVM.mainResponse.sub_heading;
                            }

                            if (paymentActivityVM.mainResponse.description != null) {
                                main_payment_description_header_new_textView.text = Html.fromHtml(paymentActivityVM.mainResponse.description);
                            }


                        }

                        termsAction = "";
                        termsMeta = "";

                        if (paymentActivityVM.mainResponse.terms_action != null && paymentActivityVM.mainResponse.terms_meta != null) {
                            termsAndConditions.visibility = View.VISIBLE
                            termsAction = paymentActivityVM.mainResponse.terms_action;
                            termsMeta = paymentActivityVM.mainResponse.terms_meta;
                        }

                        if ((paymentActivityVM.mainResponse.other_payments.isNotEmpty())) {
                            //  sdfsasfdas
                            // Ram.setIsPaymentShortCut(true)
//                            onProcessAction(paymentActivityVM.mainResponse[0].action,paymentActivityVM.mainResponse[0].meta)
//                            finish()
                            other_payment_ley_title.visibility = View.VISIBLE;
                            other_payment_list_recycler_view.visibility = View.VISIBLE;
                            otherPaymentOptionsNewAdapter = OtherPaymentOptionsNewAdapter(this@PaymentActivity, paymentActivityVM.mainResponse)
                            otherPaymentOptionsNewAdapter!!.onitemClickListener = this@PaymentActivity;
                            other_payment_list_recycler_view.apply {
                                layoutManager = mlayoutManagerNew as RecyclerView.LayoutManager?
                                adapter = otherPaymentOptionsNewAdapter
                            }

                        }


                        if ((paymentActivityVM.mainResponse.save_payments.isNotEmpty())) {
                            ley_title.visibility = View.VISIBLE;
                            listPaymentOptions.visibility = View.VISIBLE;
                            paymentActivityAdapter = PaymentActivityAdapter(this@PaymentActivity, paymentActivityVM.mainResponse)
                            paymentActivityAdapter!!.onitemClickListener = this@PaymentActivity
                            listPaymentOptions.apply {
                                layoutManager = mlayoutManager as RecyclerView.LayoutManager?
                                adapter = paymentActivityAdapter
                            }

                        }


                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                },
                        {
                            progressBar.visibility = View.GONE
                            showMessage(R.string.service_loading_fail)
                        })
        )

    }


    private fun initView() {
        // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }
        ley_title.visibility = View.GONE;
        other_payment_ley_title.visibility = View.GONE;
        listPaymentOptions.visibility = View.GONE;
        other_payment_list_recycler_view.visibility = View.GONE;
        // BACK BUTTON EVENT END

        redirect_terms_condition.setOnClickListener {
            processAction(termsAction, termsMeta);
        }
    }

    companion object {
        fun startActivity(context: Context?, meta: String) {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(EXTRA_PAYMENT_META, meta)
            context!!.startActivity(intent)
        }
    }

    private fun readExtras() {
        val bundle = intent.extras;
        if (bundle != null && bundle.containsKey(EXTRA_PAYMENT_META)) {
            val meta = bundle.getSerializable(EXTRA_PAYMENT_META) as String
            if (meta.isNotEmpty()) {
                pref.setFbEvenActionMeta("paynow", meta);
                getData(meta)
            }
        }
    }
}

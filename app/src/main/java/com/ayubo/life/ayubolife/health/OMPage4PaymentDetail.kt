package com.ayubo.life.ayubolife.health

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.bumptech.glide.Glide
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.android.synthetic.main.activity_om_page2_set_location.view.*
import kotlinx.android.synthetic.main.activity_om_page3_select_partner.view.*
import kotlinx.android.synthetic.main.activity_om_page4_payment_detail.view.*
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OMPage4PaymentDetail : Fragment(),
    MedMediaPreviewCardAdapter.OnMedMediaPreviewItemClickListener,
    OrderMedicineLocationAdapter.OnOrderMedicineLocationItemClickListener,
    OrderMedicinePaymentMethodAdapter.OnPaymentMethodItemClickListener,
    MedPartnerCardAdapter.MedPartnerCardItemClickListener {

    private var medMediaPreviewCardAdapter: MedMediaPreviewCardAdapter? = null
    private var orderMedicineLocationAdapter: OrderMedicineLocationAdapter? = null
    private var medPartnerCardAdapter: MedPartnerCardAdapter? = null
    private var calculatedPriceAdapter: CalculatedPriceAdapter? = null
    private var orderMedicinePaymentMethodAdapter: OrderMedicinePaymentMethodAdapter? = null


    var dotsImageViews: ArrayList<ImageView> = ArrayList<ImageView>();
    private var dotsCount: Int = 0;
    lateinit var mainView: View;
    var isShow: Boolean = true;

    lateinit var prefManager: PrefManager;

    lateinit var oMCreatedOrderObj: OMCreatedOrderObj

    lateinit var changeFragmentInterface: OMPage4PaymentDetailNextButtonInterface;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.activity_om_page4_payment_detail, container, false);

        mainView.pay_now_btn.setOnClickListener {

            val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;

            if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
                OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(jsonObject);
            }
            oMCreatedOrderObj =
                OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);
            if (oMCreatedOrderObj.status != null && oMCreatedOrderObj.status.equals("Payment Pending")) {
                print("do pay now")
            } else if (oMCreatedOrderObj.status != null && (oMCreatedOrderObj.status.equals(
                    "Order Confirmed"
                ) || oMCreatedOrderObj.status.equals(
                    "Payment Completed"
                )
                        || oMCreatedOrderObj.status.equals(
                    "Dispatched"
                ) || oMCreatedOrderObj.status.equals(
                    "Delivered"
                ) || oMCreatedOrderObj.status.equals(
                    "Order Confirmed"
                )

                        )
            ) {
                val intent = Intent(requireContext(), OMPage5TrackOrder::class.java)
                startActivity(intent)

            } else {
                openCreateOrderBottomSlider()

            }
        }

        mainView.cancel_order_main_text.setOnClickListener {

            openCancelOrderBottomSlider()
//            if (
//                oMCreatedOrderObj.status.equals("Payment Pending")
//                ||
//                oMCreatedOrderObj.status.equals(  "Order Confirmed" )
//            ) {
//                openCancelOrderBottomSlider()
//            } else {
//                goToHome()
//            }


        }

//        mainView.show_more_show_less_text.setOnClickListener {
//
//            if (isShow) {
//                mainView.waiting_price_media_preview_recycler_view.visibility = View.GONE
//                mainView.waiting_price_media_preview_indicators_layout.visibility = View.GONE
//                mainView.special_note_main.visibility = View.GONE
//                mainView.delivery_loaction_main.visibility = View.GONE
//                mainView.partner_main.visibility = View.GONE
//                mainView.show_more_show_less_text.text = "Show More"
//                mainView.show_more_show_less_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
//                isShow = false
//            } else {
//                mainView.waiting_price_media_preview_recycler_view.visibility = View.VISIBLE
//                mainView.waiting_price_media_preview_indicators_layout.visibility = View.VISIBLE
//                mainView.special_note_main.visibility = View.VISIBLE
//                mainView.delivery_loaction_main.visibility = View.VISIBLE
//                mainView.partner_main.visibility = View.VISIBLE
//                mainView.show_more_show_less_text.text = "Show Less"
//                mainView.show_more_show_less_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
//                isShow = true
//            }
//
//
//        }

        return mainView;
    }

    private fun goToHome() {
        val empty = OMCreatedOrderObj(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        val emptyJsonObject: JsonObject = Gson().toJsonTree(empty).asJsonObject;


        if (oMCreatedOrderObj.status == null) {
            OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(emptyJsonObject)
        }

        oMCreatedOrderObj =
            OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(emptyJsonObject)

        changeFragmentInterface =
            context as (OMPage4PaymentDetailNextButtonInterface);
        changeFragmentInterface.onOMPage4PaymentDetailNextButtonClick(1)
    }


    fun openCancelOrderBottomSlider() {


        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.om_delete_address_bottom_sheet);
        bottomSheetDialog.show();


        val address: TextView? =
            bottomSheetDialog.findViewById<TextView>(R.id.address)
        val delete_address: TextView? =
            bottomSheetDialog.findViewById<TextView>(R.id.delete_address)
        val delete_btn: Button? =
            bottomSheetDialog.findViewById<Button>(R.id.delete_btn)
        val not_now: TextView? =
            bottomSheetDialog.findViewById<TextView>(R.id.not_now)


        address?.text = "Cancel the Order";
        delete_address?.text = "Are you sure you want to cancel the order ?"

        delete_btn?.setOnClickListener {

            if (
                oMCreatedOrderObj.status.equals("Order Confirmed")
                ||
                oMCreatedOrderObj.status.equals("Payment Pending")

            ) {
                updateMainOrderStatus("Cancelled", "User cancelled the order", false, true)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Your Order Cancelled Successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
                goToHome()
            }
            bottomSheetDialog.dismiss()

        }

        not_now?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


//        val builder: BottomSheet.Builder? = this@OMPage4PaymentDetail.context?.let {
//            BottomSheet.Builder(
//                it
//            )
//        }
//
//        builder?.setTitle("Title")?.setView(R.layout.om_delete_address_bottom_sheet)
//            ?.setFullWidth(false)?.show();
//
//        val address: TextView? =
//            builder?.view?.findViewById<TextView>(R.id.address)
//        val delete_address: TextView? =
//            builder?.view?.findViewById<TextView>(R.id.delete_address)
//        val delete_btn: Button? =
//            builder?.view?.findViewById<Button>(R.id.delete_btn)
//        val not_now: TextView? =
//            builder?.view?.findViewById<TextView>(R.id.not_now)
//
//
//        address?.text = "Cancel the Order";
//        delete_address?.text = "Are you sure you want to cancel the order ?"
//
//        delete_btn?.setOnClickListener {
//
//            if (
//                oMCreatedOrderObj.status.equals("Order Confirmed")
//                ||
//                oMCreatedOrderObj.status.equals("Payment Pending")
//
//            ) {
//                updateMainOrderStatus("Cancelled", "User cancelled the order", false, true)
//
//            } else {
//                Toast.makeText(context!!, "Your Order Cancelled Successfully", Toast.LENGTH_SHORT)
//                    .show()
//                goToHome()
//            }
//            builder.dismiss()
//
//        }
//
//        not_now?.setOnClickListener {
//            builder.dismiss()
//        }


    }

    fun openCreateOrderBottomSlider() {


        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.om_delete_address_bottom_sheet);
        bottomSheetDialog.show();


        val address: TextView? =
            bottomSheetDialog.findViewById<TextView>(R.id.address)
        val delete_address: TextView? =
            bottomSheetDialog.findViewById<TextView>(R.id.delete_address)
        val delete_btn: Button? =
            bottomSheetDialog.findViewById<Button>(R.id.delete_btn)
        val not_now: TextView? =
            bottomSheetDialog.findViewById<TextView>(R.id.not_now)



        if (Constants.type == Constants.Type.LIFEPLUS) {
            delete_btn?.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            delete_btn?.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

        address?.text = "Confirm";
        delete_address?.text = "Are you sure you want to continue?"

        delete_btn?.setOnClickListener {
            createNewOrder()
            bottomSheetDialog.dismiss()
        }

        not_now?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


//        val builder: BottomSheet.Builder? = this@OMPage4PaymentDetail.context?.let {
//            BottomSheet.Builder(
//                it
//            )
//        }

//        builder?.setTitle("Title")?.setView(R.layout.om_delete_address_bottom_sheet)
//            ?.setFullWidth(false)?.show();
//
//        val address: TextView? =
//            builder?.view?.findViewById<TextView>(R.id.address)
//        val delete_address: TextView? =
//            builder?.view?.findViewById<TextView>(R.id.delete_address)
//        val delete_btn: Button? =
//            builder?.view?.findViewById<Button>(R.id.delete_btn)
//        val not_now: TextView? =
//            builder?.view?.findViewById<TextView>(R.id.not_now)
//
//
//
//        if (Constants.type == Constants.Type.LIFEPLUS) {
//            delete_btn?.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)
//
//        } else {
//            delete_btn?.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
//        }
//
//        address?.text = "Place Your Order";
//        delete_address?.text = "Are you sure you want to place the order ?"
//
//        delete_btn?.setOnClickListener {
//            createNewOrder()
//            builder.dismiss()
//        }
//
//        not_now?.setOnClickListener {
//            builder.dismiss()
//        }


    }

    interface OMPage4PaymentDetailNextButtonInterface {
        fun onOMPage4PaymentDetailNextButtonClick(position: Int);

    }

    override fun onResume() {
        super.onResume()
        prefManager = PrefManager(requireContext());

        oMCreatedOrderObj = OMCommon(requireContext()).retrieveFromCommonSingleton();

        setWaitingPriceMediaPreviewRecycler()

        setWaitingPriceMedicinesLocation()

        setWaitingPricePartnerRecyclerview()

        setPartnerPaymentMethods()

    }

    private fun setPartnerPaymentMethods() {


        if (oMCreatedOrderObj.payment !== null) {
//            if (oMCreatedOrderObj.payment!!.selected_payment_type!! == "Online") {


            if (oMCreatedOrderObj.status.equals("Processing")) {
                setOnlineProcessingUI()
            }


//            else if (oMCreatedOrderObj.status.equals("Payment Pending")) {
//                setOnlinePaymentPendingUI();
//            }

            else if (oMCreatedOrderObj.status.equals("Payment Completed")) {
                setOnlinePaymentCompletedUI();
            } else if (oMCreatedOrderObj.status.equals("Dispatched") || oMCreatedOrderObj.status.equals(
                    "Delivered"
                )
            ) {
                setOnlineDispatchedDeliveredUI();
            } else if (oMCreatedOrderObj.status.equals("Cancelled")) {
                setOnlineCancelledUI()
            } else {
                setDefaultUI()
            }


//            }


//            else {

//                if (oMCreatedOrderObj.status.equals("Processing")) {
//                    setOtherPaymentProcessingUI()
//                }


//                else if (oMCreatedOrderObj.status.equals("Order Confirmed")) {
//                    setOtherPaymentOrderConfirmedUI()
//                }


//            else if (oMCreatedOrderObj.status.equals("Dispatched") || oMCreatedOrderObj.status.equals(
//                                "Delivered"
//                        )
//                ) {
//                    setOnlineDispatchedDeliveredUI();
//                }


//            else if (oMCreatedOrderObj.status.equals("Cancelled")) {
//                    setOnlineCancelledUI()
//                } else {
//                    setDefaultUI()
//                }
//            }


        } else {
            setDefaultUI()
        }


    }

    private fun setOnlineProcessingUI() {
        mainView.payment_terms_condition_track_order.visibility = View.GONE
        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)
        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title
        mainView.pay_now_btn.text = "Proceed"
        mainView.pay_now_btn.isEnabled = false
        mainView.pay_now_btn.setTextColor(resources.getColor(R.color.white));
        mainView.payment_terms_condition.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_new.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.pay_now_btn.setBackgroundResource(R.drawable.disable_shape_rect_round)
        mainView.pay_now_btn.isEnabled = false
        mainView.payment_detail_main.visibility = View.VISIBLE
        mainView.select_payment_method_main.visibility = View.GONE

        mainView.cancel_order_main_text.text = "Go Home"
        mainView.cancel_order_main_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
        mainView.cancel_order_main_text.isEnabled = true
    }

    private fun setOnlinePaymentPendingUI() {
        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)

        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title

        mainView.pay_now_btn.text = "Proceed"
        mainView.payment_terms_condition_track_order.visibility = View.GONE

        mainView.pay_now_btn.setTextColor(resources.getColor(R.color.white));
        mainView.payment_terms_condition.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_new.text =
            oMCreatedOrderObj.payment!!.termsconditions



        mainView.pay_now_btn.isEnabled = true



        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }
        mainView.payment_detail_main.visibility = View.VISIBLE
        mainView.calculated_price_main.visibility = View.VISIBLE
        mainView.select_payment_method_main.visibility = View.GONE
        mainView.calculating_price_main_banner.visibility = View.GONE
        setCalculatedRecyclerview()
    }

    private fun setOnlinePaymentCompletedUI() {
        mainView.payment_terms_condition_track_order.visibility = View.GONE
        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)
        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title
        mainView.pay_now_btn.text = "Track Order"
        mainView.cancel_order_main_text.text = "Home"
        mainView.cancel_order_main_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
        mainView.pay_now_btn.setTextColor(resources.getColor(R.color.white));
        mainView.payment_terms_condition.visibility = View.GONE
//        mainView.payment_terms_condition.text =
//                oMCreatedOrderObj.payment!!.termsconditions
//
//
//        mainView.payment_terms_condition_new.text =
//                oMCreatedOrderObj.payment!!.termsconditions


        mainView.pay_now_btn.isEnabled = true


        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

        mainView.payment_detail_main.visibility = View.VISIBLE
        mainView.calculated_price_main.visibility = View.VISIBLE
        mainView.select_payment_method_main.visibility = View.GONE
        mainView.calculating_price_main_banner.visibility = View.GONE
        setCalculatedRecyclerview()
    }

    private fun setOnlineCancelledUI() {
        mainView.payment_terms_condition_track_order.visibility = View.GONE

        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)

        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title
        mainView.pay_now_btn.text = "Re-Order"


        mainView.pay_now_btn.setTextColor(resources.getColor(R.color.white));
        mainView.cancel_order_main_text.visibility = View.GONE

        mainView.payment_terms_condition.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_new.text =
            oMCreatedOrderObj.payment!!.termsconditions

        mainView.pay_now_btn.setBackgroundResource(R.drawable.disable_shape_rect_round)
        mainView.pay_now_btn.isEnabled = false

        mainView.payment_detail_main.visibility = View.VISIBLE
        mainView.select_payment_method_main.visibility = View.GONE
    }

    private fun setOnlineDispatchedDeliveredUI() {
        mainView.payment_terms_condition_track_order.visibility = View.VISIBLE
        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)

        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title
        mainView.pay_now_btn.text = "Track Order"
        mainView.cancel_order_main_text.text = "Home"
        mainView.cancel_order_main_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG;


//        termsAndConditions
        mainView.payment_terms_condition.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_track_order.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_new.text =
            oMCreatedOrderObj.payment!!.termsconditions
        ///////////////////////////////////////////


        mainView.pay_now_btn.isEnabled = true

        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

        mainView.payment_detail_main.visibility = View.GONE
        mainView.select_payment_method_main.visibility = View.GONE
    }

    private fun setWaitingPriceMediaPreviewRecycler() {
        try {
            mainView.waiting_price_media_preview_recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            val mediaPreviewCardItemsArrayList = ArrayList<OMMediaFiles>()

            for (i in 0 until oMCreatedOrderObj.files!!.size) {
                val mediaFile = oMCreatedOrderObj.files!![i]
                mediaPreviewCardItemsArrayList.add(
                    OMMediaFiles(
                        mediaFile.URL,
                        mediaFile.MediaName,
                        mediaFile.MediaFolder,
                        mediaFile.MediaType,
                        mediaFile.note,
                        false
                    )

                )
            }


            medMediaPreviewCardAdapter =
                MedMediaPreviewCardAdapter(requireContext(), mediaPreviewCardItemsArrayList, false)
            medMediaPreviewCardAdapter!!.onMedMediaPreviewItemClickListener =
                this@OMPage4PaymentDetail
            mainView.waiting_price_media_preview_recycler_view.adapter = medMediaPreviewCardAdapter

            dotsCount = medMediaPreviewCardAdapter!!.itemCount
            mainView.waiting_price_media_preview_indicators_layout.removeAllViewsInLayout()
            dotsImageViews = ArrayList<ImageView>()


            for (i in 0 until dotsCount) {
                dotsImageViews.add(ImageView(requireContext()))
                dotsImageViews.get(i)
                    .setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.nonactive_dot
                        )
                    );
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 0, 8, 0);
                val imageView: ImageView = dotsImageViews.get(i);
                mainView.waiting_price_media_preview_indicators_layout.addView(imageView, params);
            }

            dotsImageViews[0].setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.subscribe_slide_active_dot
                )
            );

            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    System.out.println(recyclerView)
                    System.out.println(newState)


//                    for (i in 0 until dotsCount) {
//                        dotsImageViews.get(i).setImageDrawable(
//                            ContextCompat.getDrawable(
//                                context!!,
//                                R.drawable.nonactive_dot
//                            )
//                        );
//                    }
//
//                    dotsImageViews.get(newState).setImageDrawable(
//                        ContextCompat.getDrawable(
//                            context!!,
//                            R.drawable.subscribe_slide_active_dot
//                        )
//                    );
                }
            }
            mainView.waiting_price_media_preview_recycler_view.addOnScrollListener(scrollListener)

            mainView.special_note_description.text = mediaPreviewCardItemsArrayList[0].note

            if (mediaPreviewCardItemsArrayList[0].note != "") {
                mainView.special_note_main.visibility = View.VISIBLE
            }


//            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setWaitingPriceMedicinesLocation() {
        mainView.waiting_price_set_location_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val orderMedicineLocationItemsArrayList = ArrayList<OMAddress>()

        //        for (i in 0 until todoArrayList.size()) {
//            val subscriptionCard = todoArrayList[i]


        orderMedicineLocationItemsArrayList.add(oMCreatedOrderObj.address!!)


//        }


        orderMedicineLocationAdapter =
            OrderMedicineLocationAdapter(
                requireContext(),
                orderMedicineLocationItemsArrayList,
                orderMedicineLocationItemsArrayList,
                false,
                false,
                false
            )

        try {
            orderMedicineLocationAdapter!!.onOrderMedicineLocationItemClickListener =
                this@OMPage4PaymentDetail
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mainView.waiting_price_set_location_recycler_view.adapter = orderMedicineLocationAdapter
    }

    private fun setWaitingPricePartnerRecyclerview() {
        try {

            mainView.waiting_price_partner_recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            val medPartnerCardItemsArrayList = ArrayList<OMPartner>()

            medPartnerCardItemsArrayList.add(oMCreatedOrderObj.partner!!)

            medPartnerCardAdapter =
                MedPartnerCardAdapter(requireContext(), medPartnerCardItemsArrayList, false, false)

            try {
                medPartnerCardAdapter!!.onOrderMedicineHistoryItemClickListener =
                    this@OMPage4PaymentDetail
            } catch (e: Exception) {
                e.printStackTrace()
            }


            mainView.waiting_price_partner_recycler_view.adapter = medPartnerCardAdapter


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOtherPaymentOrderConfirmedUI() {
        mainView.payment_terms_condition_track_order.visibility = View.GONE
        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)

        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title
        mainView.pay_now_btn.text = "Proceed"
        mainView.cancel_order_main_text.isClickable = true
//        mainView.cancel_order_main_text.setTextColor(resources.getColor(R.color.color_EAEAEA))
        mainView.pay_now_btn.setTextColor(resources.getColor(R.color.white));
        mainView.payment_terms_condition.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_new.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.pay_now_btn.isEnabled = true
        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }
        mainView.payment_detail_main.visibility = View.VISIBLE
        mainView.select_payment_method_main.visibility = View.GONE
    }

    private fun setOtherPaymentProcessingUI() {
        mainView.payment_terms_condition_track_order.visibility = View.GONE
        Glide.with(this).load(oMCreatedOrderObj.payment!!.image_url)
            .into(mainView.waiting_image_view)

        mainView.payment_calculating_title.text = oMCreatedOrderObj.payment!!.title
        mainView.pay_now_btn.text = "Proceed"
        mainView.pay_now_btn.setTextColor(resources.getColor(R.color.white));
        mainView.payment_terms_condition.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.payment_terms_condition_new.text =
            oMCreatedOrderObj.payment!!.termsconditions
        mainView.pay_now_btn.setBackgroundResource(R.drawable.disable_shape_rect_round)
        mainView.pay_now_btn.isEnabled = false;
        mainView.payment_detail_main.visibility = View.VISIBLE
        mainView.select_payment_method_main.visibility = View.GONE

        mainView.cancel_order_main_text.text = "Home"
        mainView.cancel_order_main_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
        mainView.cancel_order_main_text.isEnabled = true
    }

    private fun setDefaultUI() {
        mainView.payment_terms_condition_track_order.visibility = View.GONE
        if (oMCreatedOrderObj.partner!!.payment_methods!!.size > 0) {
            mainView.payment_methods_recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


            orderMedicinePaymentMethodAdapter =
                OrderMedicinePaymentMethodAdapter(
                    requireContext(),
                    oMCreatedOrderObj.partner!!.payment_methods,
                    false
                )

            try {
                orderMedicinePaymentMethodAdapter!!.onPaymentMethodItemClickListener =
                    this@OMPage4PaymentDetail
            } catch (e: Exception) {
                e.printStackTrace()
            }


            mainView.payment_methods_recycler_view.adapter =
                orderMedicinePaymentMethodAdapter

        }
    }

    private fun setCalculatedRecyclerview() {
        mainView.calculated_price_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        var calculatedPriceItemsArrayList = ArrayList<PaymentItems>()

        mainView.total_value.text = oMCreatedOrderObj.payment!!.total_amount
        mainView.total_currency.text = oMCreatedOrderObj.payment!!.currency

        calculatedPriceItemsArrayList = oMCreatedOrderObj.payment!!.items

        calculatedPriceAdapter =
            CalculatedPriceAdapter(requireContext(), calculatedPriceItemsArrayList, false)


        mainView.calculated_price_recycler_view.adapter = calculatedPriceAdapter
    }

    private fun createNewOrder() {
        mainView.paymentPageProgressBar.visibility = View.VISIBLE


        val orderMedicineCreateObject: OrderMedicineCreateObject = OrderMedicineCreateObject(
            oMCreatedOrderObj.files,
            oMCreatedOrderObj.address,
            oMCreatedOrderObj.partner,
            oMCreatedOrderObj.payment
        )


        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForOrderMedicine().create(ApiInterface::class.java);
//
        val createOrderMedicineCall: Call<ProfileDashboardResponseData> =
            apiService.createOrderMedicine(
                AppConfig.APP_BRANDING_ID, prefManager.userToken, orderMedicineCreateObject
            );
//
        createOrderMedicineCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                mainView.paymentPageProgressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    println(response)

                    val dashboardMainData: JsonObject? =
                        Gson().toJsonTree(response.body()!!.data).asJsonObject;

                    val createOrderOMCreatedOrderObj: OMCreatedOrderObj =
                        Gson().fromJson(dashboardMainData, OMCreatedOrderObj::class.java);

                    val jsonObject: JsonObject =
                        Gson().toJsonTree(createOrderOMCreatedOrderObj).asJsonObject;

//                    if (oMCreatedOrderObj.status == null) {
//                        val empty = OMCreatedOrderObj(
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null
//                        );
//                        val emptyJsonObject: JsonObject = Gson().toJsonTree(empty).asJsonObject;
//
//                        OMCommon(context!!).saveToDraftSingletonAndRetrieve(emptyJsonObject)
//                    }

                    OMCommon(context!!).saveToDraftSingletonAndRetrieve(jsonObject)
                    oMCreatedOrderObj =
                        OMCommon(context!!).saveToCommonSingletonAndRetrieve(jsonObject)


//                    prefManager.setOrderMedicineCreatedOrder(jsonObject)

//                    updateOrder()
                    updateMainOrderStatus("Processing", "User submitted order", true, true);


                } else {
                    Toast.makeText(
                        context,
                        "There is an issue when loading data, Please contact admin.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, throwable: Throwable) {
                mainView.paymentPageProgressBar.visibility = View.GONE
                throwable.printStackTrace()
            }
        })

    }

    private fun updateMainOrderStatus(
        status: String,
        reason: String,
        isShowPaymentMethods: Boolean,
        isGotoHome: Boolean
    ) {
        mainView.paymentPageProgressBar.visibility = View.VISIBLE
        val apiServiceUpdateOrderStatus: ApiInterface =
            ApiClient.getAzureApiClientV1ForOrderMedicine()
                .create(ApiInterface::class.java);

        val updateOrderStatusCall: Call<ProfileDashboardResponseData> =
            apiServiceUpdateOrderStatus.updateOrderStatus(
                AppConfig.APP_BRANDING_ID,
                prefManager.userToken,
                oMCreatedOrderObj.id,
                status,
                reason
            );

        updateOrderStatusCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                p0: Call<ProfileDashboardResponseData>,
                updateOrderStatusResponse: Response<ProfileDashboardResponseData>
            ) {
                mainView.paymentPageProgressBar.visibility = View.GONE

                if (updateOrderStatusResponse.isSuccessful) {

                    val updateOrderStatusResponseMainDataJsonObject: JsonObject =
                        Gson().toJsonTree(
                            Gson().fromJson(
                                Gson().toJsonTree(updateOrderStatusResponse.body()!!.data).asJsonObject,
                                OMCreatedOrderObj::class.java
                            )
                        ).asJsonObject;
                    val empty = OMCreatedOrderObj(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    );
                    val emptyJsonObject: JsonObject = Gson().toJsonTree(empty).asJsonObject;

                    if (isGotoHome) {
                        OMCommon(context!!).saveToDraftSingletonAndRetrieve(emptyJsonObject)
                        oMCreatedOrderObj =
                            OMCommon(context!!).saveToCommonSingletonAndRetrieve(emptyJsonObject)

                        var toastMsg = "Submitted your order successfully.";
                        if (status == "Cancelled") {
                            toastMsg = "Your order was cancelled."
                        }
                        Toast.makeText(context!!, toastMsg, Toast.LENGTH_SHORT)
                            .show()


                        changeFragmentInterface =
                            context as (OMPage4PaymentDetailNextButtonInterface);
                        changeFragmentInterface.onOMPage4PaymentDetailNextButtonClick(1)

                    } else {
                        oMCreatedOrderObj = OMCommon(context!!).saveToCommonSingletonAndRetrieve(
                            updateOrderStatusResponseMainDataJsonObject
                        );

                        if (isShowPaymentMethods) {
                            setPartnerPaymentMethods();
                        } else {
                            OMCommon(context!!).saveToDraftSingletonAndRetrieve(emptyJsonObject)
                            oMCreatedOrderObj =
                                OMCommon(context!!).saveToCommonSingletonAndRetrieve(emptyJsonObject)
                            changeFragmentInterface =
                                context as (OMPage4PaymentDetailNextButtonInterface);
                            changeFragmentInterface.onOMPage4PaymentDetailNextButtonClick(1)
                        }

                    }


                }

            }

            override fun onFailure(
                p0: Call<ProfileDashboardResponseData>,
                p1: Throwable
            ) {
                mainView.paymentPageProgressBar.visibility = View.GONE
                p1.printStackTrace()
            }


        })
    }


    override fun onMedMediaPreviewItemClick(
        medMediaPreviewCardItem: OMMediaFiles,
        isNotify: Boolean
    ) {
        mainView.special_note_description.text = medMediaPreviewCardItem.note
    }

    override fun orderMedicineLocationItemClick(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }

    override fun orderMedicineFavLocationItemClick(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }

    override fun orderMedicineLocationEditItemClick(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }

    override fun orderMedicineLocationLongPress(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }

    override fun medPartnerCardItemClick(medPartnerCardItem: OMPartner) {
        print(medPartnerCardItem)
    }

    override fun paymentMethodItemClick(paymentMethod: PaymentMethod) {
        print(paymentMethod)
        mainView.pay_now_btn.isEnabled = true

        val paymentItemsArrayList = ArrayList<PaymentItems>();


        val oMPayment: OMPayment =
            OMPayment(paymentMethod.id, "", "", paymentItemsArrayList, "", "", "")


        oMCreatedOrderObj.payment = oMPayment;

        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.pay_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }
    }

//    private fun updateOrder() {
//        mainView.paymentPageProgressBar.visibility = View.VISIBLE
//        val apiService: ApiInterface =
//            ApiClient.getAzureApiClientV1ForOrderMedicine().create(ApiInterface::class.java);
//
//        val oMUpdateOrder = OMUpdateOrder(
//            oMCreatedOrderObj.id,
//            oMCreatedOrderObj.files,
//            if (oMCreatedOrderObj.address == null) null else oMCreatedOrderObj.address,
//            if (oMCreatedOrderObj.partner == null) null else oMCreatedOrderObj.partner,
//            if (oMCreatedOrderObj.payment == null) null else oMCreatedOrderObj.payment
//        )
//
//        val updateOrderCall: Call<ProfileDashboardResponseData> = apiService.updateOrder(
//            AppConfig.APP_BRANDING_ID,
//            prefManager.userToken,
//            oMUpdateOrder
//        );
//
//        updateOrderCall.enqueue(object : Callback<ProfileDashboardResponseData> {
//            override fun onResponse(
//                p0: Call<ProfileDashboardResponseData>,
//                response: Response<ProfileDashboardResponseData>
//            ) {
//
//                if (response.isSuccessful) {
//                    val responseMainData: JsonObject? =
//                        Gson().toJsonTree(response.body()!!.data).asJsonObject;
//
//                    val oMCreatedOrderObjVal: OMCreatedOrderObj =
//                        Gson().fromJson(responseMainData, OMCreatedOrderObj::class.java);
//
//                    val jsonObject: JsonObject =
//                        Gson().toJsonTree(oMCreatedOrderObjVal).asJsonObject;
//
//                    oMCreatedOrderObj = OMCommon(context!!).saveToDraftSingletonAndRetrieve(
//                        jsonObject
//                    );
//
//
//                    updateOrderStatus("Processing", "User submitted order", true);
//
////                    setPartnerPaymentMethods();
//
////                    val apiServiceUpdateOrderStatus: ApiInterface =
////                        ApiClient.getAzureApiClientV1ForOrderMedicine()
////                            .create(ApiInterface::class.java);
////
////                    val updateOrderStatusCall: Call<ProfileDashboardResponseData> =
////                        apiServiceUpdateOrderStatus.updateOrderStatus(
////                            AppConfig.APP_BRANDING_ID,
////                            prefManager.userToken,
////                            oMCreatedOrderObj.id,
////                            ,
////
////                        );
////
////
////
////                    updateOrderStatusCall.enqueue(object : Callback<ProfileDashboardResponseData> {
////                        override fun onResponse(
////                            p0: Call<ProfileDashboardResponseData>,
////                            updateOrderStatusResponse: Response<ProfileDashboardResponseData>
////                        ) {
////                            mainView.paymentPageProgressBar.visibility = View.GONE
////
////                            if (updateOrderStatusResponse.isSuccessful) {
////                                print(updateOrderStatusResponse)
////
////
////                                val updateOrderStatusResponseMainData: JsonObject? =
////                                    Gson().toJsonTree(updateOrderStatusResponse.body()!!.data).asJsonObject;
////
////
////                                val updateOrderStatusResponseOMCreatedOrderObj: OMCreatedOrderObj =
////                                    Gson().fromJson(
////                                        updateOrderStatusResponseMainData,
////                                        OMCreatedOrderObj::class.java
////                                    );
////
////                                val updateOrderStatusResponseMainDataJsonObject: JsonObject =
////                                    Gson().toJsonTree(updateOrderStatusResponseOMCreatedOrderObj).asJsonObject;
////
////
////                                prefManager.setOrderMedicineCreatedOrder(
////                                    updateOrderStatusResponseMainDataJsonObject
////                                )
////
////
////                                oMCreatedOrderObj = Gson().fromJson(
////                                    prefManager.orderMedicineCreatedOrder,
////                                    OMCreatedOrderObj::class.java
////                                );
////
////
////                                setPartnerPaymentMethods();
////
////                            }
////
////                        }
////
////                        override fun onFailure(
////                            p0: Call<ProfileDashboardResponseData>,
////                            p1: Throwable
////                        ) {
////                            mainView.paymentPageProgressBar.visibility = View.GONE
////                            p1.printStackTrace()
////                        }
////
////
////                    })
//
//
//                } else {
//                    mainView.paymentPageProgressBar.visibility = View.GONE
//                }
//
//
//            }
//
//
//            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
//                mainView.paymentPageProgressBar.visibility = View.GONE
//            }
//
//        })
//    }
}
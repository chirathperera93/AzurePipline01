package com.ayubo.life.ayubolife.new_payment.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.health.CalculatedPriceAdapter
import com.ayubo.life.ayubolife.health.OMAddress
import com.ayubo.life.ayubolife.health.PaymentItems
import com.ayubo.life.ayubolife.new_payment.NewPaymentConstantsClass
import com.ayubo.life.ayubolife.new_payment.NewPaymentConstantsClass.*
import com.ayubo.life.ayubolife.new_payment.PAYHERE_PAYMENT_ID
import com.ayubo.life.ayubolife.new_payment.adapter.NewMyPaymentMethodAdapter
import com.ayubo.life.ayubolife.new_payment.model.*
import com.ayubo.life.ayubolife.new_payment.view_model.NewPaymentMainActivityViewModel
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_change_payment.*
import kotlinx.android.synthetic.main.activity_new_payment_main.*
import lk.payhere.androidsdk.PHConfigs
import lk.payhere.androidsdk.PHConstants
import lk.payhere.androidsdk.PHMainActivity
import lk.payhere.androidsdk.PHResponse
import lk.payhere.androidsdk.model.InitRequest
import lk.payhere.androidsdk.model.StatusResponse
import javax.inject.Inject

class NewPaymentMainActivity : BaseActivity(),
    NewMyPaymentMethodAdapter.OnNewMyPaymentMethodItemClickListener {

    @Inject
    lateinit var newPaymentMainActivityViewModel: NewPaymentMainActivityViewModel;

    private var newMyPaymentMethodAdapter: NewMyPaymentMethodAdapter? = null
    lateinit var prefManager: PrefManager;
    var payHerePaymentId: String = "";
    var payHerePaymentType: String = "";
    var userId: String = ""
    var newMyCardItem: NewMyCardItem? = null
    var mainOrder: JsonObject = JsonObject()
    var mainResponse: NewPaymentDetail? = null
    var address: OMAddress = OMAddress(
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
        null,
        null
    );
    var partner: JsonObject = JsonObject();
    var notes: JsonObject = JsonObject();
    var payment: JsonObject = JsonObject();
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_payment_main)
        App.getInstance().appComponent.inject(this)
        prefManager = PrefManager(this);
        userId = prefManager.loginUser["uid"].toString();
        val fullName = prefManager.loginUser["name"].toString().split(" ");
        lastName = fullName[1];
        email = prefManager.loginUser["email"].toString();
        phone = prefManager.loginUser["mobile"].toString();
        readExtras()
        initiateUI()
        getPaymentDetail()


    }

    private fun setCalculatedPrice(paymentItemsArrayList: ArrayList<PaymentItems>) {
        recyclerviewForCalculatedPrice.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        val calculatedPriceAdapter: CalculatedPriceAdapter =
            CalculatedPriceAdapter(baseContext, paymentItemsArrayList, false)


        recyclerviewForCalculatedPrice.adapter = calculatedPriceAdapter
    }

    private fun getPaymentDetail() {

        val createPaymentItem: CreatePaymentItem =
            CreatePaymentItem(payHerePaymentType, payHerePaymentId)

        subscription.add(
            newPaymentMainActivityViewModel.createNewPayment(createPaymentItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mainNewPaymentLoading.visibility = View.VISIBLE }
                .doOnTerminate { mainNewPaymentLoading.visibility = View.GONE }
                .doOnError { mainNewPaymentLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        mainResponse = newPaymentMainActivityViewModel.newPaymentDetailResponse


                        mainLinerLayoutForFirstTimeUser.visibility = View.GONE
                        recyclerViewForPaymentsMethod.visibility = View.GONE

                        val paymentDetailHeadings = mainResponse!!.headings
                        mainOrder = mainResponse!!.order
                        val res = mainResponse

                        print(res)

                        val paymentType =
                            mainResponse!!.payment.get("selected_payment_type").asString

                        if (paymentType == "savedcards") {
                            if (mainResponse!!.last_card!!.size() == 0 || mainResponse!!.last_card == null) {
                                prefManager.saveMyPaymentCard(JsonObject())
                            } else {
                                prefManager.saveMyPaymentCard(mainResponse!!.last_card)
                            }
                        } else {

                            // card on delivery , cash on delivery

                            val p = mainOrder.get("partner").asJsonObject

                            val paymentMethodsArray = p.get("payment_methods").asJsonArray

                            val filteredPaymentType =
                                paymentMethodsArray.filter { ele -> ele.asJsonObject.get("id").asString == paymentType }

                            print(filteredPaymentType)


                            val newPaymentType = NewMyCardItem(
                                filteredPaymentType[0].asJsonObject.get("id").asString,
                                "",
                                "",
                                "",
                                "",
                                filteredPaymentType[0].asJsonObject.get("title").asString,
                                filteredPaymentType[0].asJsonObject.get("description").asString,
                                "",
                                filteredPaymentType[0].asJsonObject.get("icon_url").asString,
                                true,
                                true
                            )

                            val newMyCardItemJsonObject: JsonObject =
                                Gson().toJsonTree(newPaymentType).asJsonObject;
                            prefManager.saveMyPaymentCard(newMyCardItemJsonObject);

                        }




                        setPaymentMethods();



                        partner = JsonObject();
                        notes = JsonObject();
                        payment = JsonObject();


                        if (mainOrder.get("address") != null) {


                            address = Gson().fromJson(
                                mainOrder.get("address").asJsonObject,
                                OMAddress::class.java
                            );


                        }

                        if (mainOrder.get("partner") != null) {
                            partner = mainOrder.get("partner").asJsonObject
                        }

                        if (mainOrder.get("notes") != null) {
                            notes = mainOrder.get("notes").asJsonObject
                        }

                        if (mainOrder.get("payment") != null) {
                            payment = mainOrder.get("payment").asJsonObject
                        }





                        if (paymentDetailHeadings != null) {
                            textViewForDelivery.text =
                                if (paymentDetailHeadings.get("delivery") != null) paymentDetailHeadings.get(
                                    "delivery"
                                ).asString else "Delivery location"
                        } else {
                            textViewForDelivery.text = "Delivery location"
                        }

                        if (address.display_name != null) {
                            mainLinearLayoutForDelivery.visibility = View.VISIBLE
                        }

                        textViewForDeliveryName.text = address.display_name
                        val house_number =
                            if (address.house_number != null) address.house_number + " " else ""
                        val street = if (address.street != null) address.street + " " else ""
                        val city = if (address.city != null) address.city + " " else ""
                        val address_line1 =
                            if (address.address_line1 != null) address.address_line1 + " " else ""
                        val address_line2 =
                            if (address.address_line2 != null) address.address_line2 + " " else ""
                        textViewForDeliveryAddress.text =
                            house_number + street + address_line1 + address_line2 + city


                        if (partner.size() > 0) {
                            mainLinearLayoutForPartner.visibility = View.VISIBLE

                            if (paymentDetailHeadings != null) {
                                textViewForPartner.text =
                                    if (paymentDetailHeadings.get("partner") != null) paymentDetailHeadings.get(
                                        "partner"
                                    ).asString else "Partner"
                            } else {
                                textViewForPartner.text = "Partner"
                            }

                            Glide.with(baseContext)
                                .load(partner.get("logo_url").asString)
                                .into(imageViewForPartnerImage);
                            textViewForPartnerTitle.text =
                                partner.get("name").asString
                            textViewForPartnerAddress.text =
                                partner.get("address_line").asString
                            textViewForPartnerOpenStatus.text =
                                partner.get("delivery_statement").asString
                        }

                        if (notes.size() > 0) {
                            mainLinearLayoutForSpecialNotes.visibility = View.VISIBLE

                            if (paymentDetailHeadings != null) {
                                textViewForNotesTopic.text =
                                    if (paymentDetailHeadings.get("notes") != null) paymentDetailHeadings.get(
                                        "notes"
                                    ).asString else "Special Notes"
                            } else {
                                textViewForNotesTopic.text = "Special Notes"
                            }


                            textViewForNoteDescription.text = notes.get("notes").asString
                        }

                        if (partner.size() > 0 || notes.size() > 0) {
                            horizontalLineForNotes.visibility = View.VISIBLE
                        }

                        if (payment.size() > 0) {
                            mainLinearLayoutForPaymentDetail.visibility = View.VISIBLE

                            if (paymentDetailHeadings != null) {
                                textViewForTopicPaymentDetail.text =
                                    if (paymentDetailHeadings.get("payment") != null) paymentDetailHeadings.get(
                                        "payment"
                                    ).asString else "Payment Details"
                            } else {
                                textViewForTopicPaymentDetail.text = "Payment Details"
                            }
                            val paymentItemsArrayList: ArrayList<PaymentItems> =
                                ArrayList<PaymentItems>()
                            if (payment.get("items").asJsonArray.size() > 0) {
                                for (i in 0 until payment.get("items").asJsonArray.size()) {
                                    val jsonElement: JsonElement =
                                        payment.get("items").asJsonArray.get(i)
                                    val paymentItems =
                                        Gson().fromJson(jsonElement, PaymentItems::class.java);
                                    paymentItemsArrayList.add(paymentItems);
                                }

                                setCalculatedPrice(paymentItemsArrayList)
                            }

                            if (!payment.has("total_amount")
                                ||
                                payment.get("total_amount")
                                    .equals(null) || payment.get("total_amount").asString.equals("")
                            ) {
                                mainRelativeLayoutForTotalAmount.visibility = View.GONE
                            } else {
                                textViewForTotalAmountValue.text =
                                    payment.get("total_amount").asString + payment.get("currency").asString
                            }


                            if (mainResponse!!.payment.has("terms_conditions")) {


                                mainLinerLayoutForRule.visibility = View.VISIBLE
                                textViewForRule.text =
                                    mainResponse!!.payment.get("terms_conditions").asJsonObject.get(
                                        "text"
                                    ).asString


                                mainLinerLayoutForTermsCondition.visibility = View.VISIBLE
                                textViewForTermsCondition.setOnClickListener {
                                    processAction(
                                        mainResponse!!.payment.get("terms_conditions").asJsonObject.get(
                                            "action"
                                        ).asString,
                                        mainResponse!!.payment.get("terms_conditions").asJsonObject.get(
                                            "meta"
                                        ).asString
                                    )
                                }

                            }


                        }


                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    mainNewPaymentLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                }

                )
        );
    }

    override fun onResume() {
        super.onResume()
        setPaymentMethods();
    }

    private fun initiateUI() {
        imageViewForMainPaymentBackBtn.setOnClickListener {
            finish()
        }

        buttonLayoutForPayNow.setOnClickListener {


            setChargePayment()


        }

        buttonLayoutForCancelPayment.setOnClickListener {
            finish()
        }
    }

    private fun setChargePayment() {
        if (newMyCardItem != null) {


            if (newMyCardItem!!.id != "otherpayments") {

                doChargePayment()


            } else {
                print(newMyCardItem)

                // One-time Payment Request

                payHereOneTimePaymentRequest()
            }


        } else {
            showMessage("Please select a payment method to proceed")
        }

    }

    private fun doChargePayment() {
        val chargePaymentItem: ChargePaymentItem =
            ChargePaymentItem(payHerePaymentId, newMyCardItem!!.id)


        subscription.add(
            newPaymentMainActivityViewModel.chargePayment(chargePaymentItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mainNewPaymentLoading.visibility = View.VISIBLE }
                .doOnTerminate { mainNewPaymentLoading.visibility = View.GONE }
                .doOnError { mainNewPaymentLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        val mainChargePaymentResponse: ChargePaymentDetail =
                            newPaymentMainActivityViewModel.chargePaymentResponse

                        print(mainChargePaymentResponse)

                        val mainChargePaymentResponseJsonObject: JsonObject =
                            Gson().toJsonTree(mainChargePaymentResponse).asJsonObject;

                        val intent = Intent(baseContext, PayNowResultActivity::class.java)
                        intent.putExtra(
                            "charge_payment_response",
                            mainChargePaymentResponseJsonObject.toString()
                        )
                        startActivity(intent)

                        if (mainChargePaymentResponse.status) {
                            finish()
                        }


                    } else {


//                                showMessage(R.string.service_loading_fail)
                        if (it.message != "" && it.message != null) {
                            showMessage(it.message)
                        } else {
                            showMessage(R.string.service_loading_fail)
                        }

                    }
                }, {
                    mainNewPaymentLoading.visibility = View.GONE
                    showMessage(it.message!!)
//                            showMessage(R.string.service_loading_fail)
                }

                ))
    }

    private fun payHereOneTimePaymentRequest() {
        val initRequest: InitRequest = InitRequest();

        initRequest.merchantId = PAYHERE_MERCHANT_ID;       // Your Merchant PayHere ID
        initRequest.merchantSecret =
            PAYHERE_MERCHANT_SECRET; // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
        initRequest.currency = "LKR";             // Currency code LKR/USD/GBP/EUR/AUD
        initRequest.amount =
            mainOrder.get("payment").asJsonObject.get("total_amount").asDouble;             // Final Amount to be charged
        initRequest.orderId = mainOrder.get("order_id").asString;        // Unique Reference ID
        initRequest.itemsDescription = mainResponse!!.payment_type  // Item description title
        initRequest.custom1 = userId;
        initRequest.custom2 = mainResponse!!.id;
        initRequest.customer.firstName = firstName;
        initRequest.customer.lastName = lastName;
        initRequest.customer.email = email;
        initRequest.customer.phone = phone;
        initRequest.customer.address.address = "";
        initRequest.customer.address.city = "";
        initRequest.customer.address.country = "";
        initRequest.notifyUrl = PAYHERE_PAYMENT_NOTIFY_URL

//Optional Params
        initRequest.customer.deliveryAddress.address =
            address.house_number + " " + address.street + " " + address.address_line1 + " " + address.address_line2 + " " + address.city;
        initRequest.customer.deliveryAddress.city = address.city
        initRequest.customer.deliveryAddress.country = "";
//        initRequest.items.add(Item(null, "Door bell wireless", 1, 1000.0));

        val intent: Intent = Intent(this, PHMainActivity::class.java);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, initRequest);
        PHConfigs.setBaseUrl(PAYHERE_BASE_URL);
        startActivityForResult(
            intent,
            PAYHERE_REQUEST
        ); //unique request ID like private final static int PAYHERE_REQUEST = 11010;

    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NewPaymentConstantsClass.PAYHERE_REQUEST && data != null && data.hasExtra(
                PHConstants.INTENT_EXTRA_RESULT
            )
        ) {

            val response: PHResponse<StatusResponse> =
                data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT) as (PHResponse<StatusResponse>);

            if (resultCode == RESULT_OK) {


                if (response != null)
                    if (response.isSuccess)
                        doChargePayment()
                    else
                        showMessage("Result:$response")
                else
                    showMessage("Result: no response")


            } else if (resultCode == RESULT_CANCELED) {

                if (response != null) {
                    if (response.status != -5) {
                        showMessage(response.toString());
                    }
                } else
                    showMessage("User canceled the request");
            } else {
                showMessage("no payhere working")
            }
        }

    }


    private fun setPaymentMethods() {


        recyclerViewForPaymentsMethod.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        val newPaymentMethodItemArrayList = ArrayList<NewMyCardItem>()

        if (prefManager.myPaymentCard != "") {
            val jsonParser: JsonParser = JsonParser();
            val myPaymentCard: JsonObject =
                jsonParser.parse(prefManager.myPaymentCard) as (JsonObject);


            if (myPaymentCard.size() > 0) {
                newMyCardItem =
                    Gson().fromJson(prefManager.myPaymentCard, NewMyCardItem::class.java);

                if (newMyCardItem != null) {
                    mainLinerLayoutForFirstTimeUser.visibility = View.GONE
                    recyclerViewForPaymentsMethod.visibility = View.VISIBLE
                    if (newMyCardItem!!.id == "cashondelivery" || newMyCardItem!!.id == "cardondelivery") {
                        buttonLayoutForPayNow.text = "Confirm Order"
                    } else {
                        buttonLayoutForPayNow.text = "Pay Now"
                    }

                    newPaymentMethodItemArrayList.add(newMyCardItem!!)
                }

            } else {
                mainLinerLayoutForFirstTimeUser.visibility = View.VISIBLE
                recyclerViewForPaymentsMethod.visibility = View.GONE
                mainLinerLayoutForFirstTimeUser.setOnClickListener {
                    val intent = Intent(baseContext, NewChangePaymentActivity::class.java)
                    intent.putExtra("payhere_payment_id", payHerePaymentId)
                    startActivity(intent)
                }
            }
        } else {
            mainLinerLayoutForFirstTimeUser.visibility = View.VISIBLE
            recyclerViewForPaymentsMethod.visibility = View.GONE
            mainLinerLayoutForFirstTimeUser.setOnClickListener {
                val intent = Intent(baseContext, NewChangePaymentActivity::class.java)
                intent.putExtra("payhere_payment_id", payHerePaymentId)
                startActivity(intent)
            }
        }



        newMyPaymentMethodAdapter =
            NewMyPaymentMethodAdapter(baseContext, newPaymentMethodItemArrayList, false, true)

        try {
            newMyPaymentMethodAdapter!!.onNewMyPaymentMethodItemClickListener =
                this@NewPaymentMainActivity
        } catch (e: Exception) {
            e.printStackTrace()
        }

        recyclerViewForPaymentsMethod.adapter = newMyPaymentMethodAdapter
    }

    override fun onMyPaymentMethodClick(newMyCardItem: NewMyCardItem) {}

    override fun onClickChangeCard() {
        val intent = Intent(baseContext, NewChangePaymentActivity::class.java)
        intent.putExtra("payhere_payment_id", payHerePaymentId)
        startActivity(intent)
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(PAYHERE_PAYMENT_ID)) {

            val typeWithReferenceId = bundle.getSerializable(PAYHERE_PAYMENT_ID) as String
            val typeWithReferenceIdArr = typeWithReferenceId.split(":")

            payHerePaymentType = typeWithReferenceIdArr[0]

            payHerePaymentId = typeWithReferenceIdArr[1]
            print(payHerePaymentId)
        }
    }
}
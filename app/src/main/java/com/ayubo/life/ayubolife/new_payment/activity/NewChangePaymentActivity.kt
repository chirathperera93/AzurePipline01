package com.ayubo.life.ayubolife.new_payment.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.new_payment.NewPaymentConstantsClass.*
import com.ayubo.life.ayubolife.new_payment.PAYHERE_PAYMENT_ID
import com.ayubo.life.ayubolife.new_payment.adapter.NewMyPaymentMethodAdapter
import com.ayubo.life.ayubolife.new_payment.adapter.NewPaymentTypesAdapter
import com.ayubo.life.ayubolife.new_payment.model.NewMyCardItem
import com.ayubo.life.ayubolife.new_payment.model.NewPaymentMethodTypeItem
import com.ayubo.life.ayubolife.new_payment.view_model.NewPaymentMainActivityViewModel
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_change_payment.*
import lk.payhere.androidsdk.PHConfigs
import lk.payhere.androidsdk.PHConstants
import lk.payhere.androidsdk.PHMainActivity
import lk.payhere.androidsdk.PHResponse
import lk.payhere.androidsdk.model.InitPreapprovalRequest
import lk.payhere.androidsdk.model.StatusResponse
import javax.inject.Inject

class NewChangePaymentActivity : BaseActivity(),
    NewMyPaymentMethodAdapter.OnNewMyPaymentMethodItemClickListener,
    NewPaymentTypesAdapter.OnPaymentTypeItemClickListener {


    @Inject
    lateinit var newPaymentMainActivityViewModel: NewPaymentMainActivityViewModel;


    private var newMyPaymentMethodAdapter: NewMyPaymentMethodAdapter? = null
    private var newPaymentTypesAdapter: NewPaymentTypesAdapter? = null
    private var initPreapprovalRequest: InitPreapprovalRequest? = null;
    lateinit var prefManager: PrefManager;

    var selectedNewPaymentMethodCard: String = ""
    var userId: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var phone: String = ""
    var payHerePaymentId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_change_payment)
        App.getInstance().appComponent.inject(this)
        prefManager = PrefManager(this);

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(PAYHERE_PAYMENT_ID)) {
            payHerePaymentId = bundle.getSerializable(PAYHERE_PAYMENT_ID) as String

        }


        initiateUI()

//        getMyCards();

        getPaymentMethods();


    }

    private fun getPaymentMethods() {
        subscription.add(
            newPaymentMainActivityViewModel.getNewPaymentMethods(payHerePaymentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { changePaymentLoading.visibility = View.VISIBLE }
                .doOnTerminate { changePaymentLoading.visibility = View.GONE }
                .doOnError { changePaymentLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        print(newPaymentMainActivityViewModel.newPaymentMethodsResponse)
                        val newPaymentMethodsResponse =
                            newPaymentMainActivityViewModel.newPaymentMethodsResponse;

                        print(newPaymentMethodsResponse)

                        val newMyPaymentMethodItemArrayList = ArrayList<NewMyCardItem>()


                        val myCardsJsonArray = newPaymentMethodsResponse.get("my_cards").asJsonArray


                        var savedMyCard: NewMyCardItem? = null;

                        if (prefManager.myPaymentCard != null && prefManager.myPaymentCard != "") {

                            savedMyCard = Gson().fromJson(
                                prefManager.myPaymentCard,
                                NewMyCardItem::class.java
                            );
                        }


                        for (i in 0 until myCardsJsonArray.size()) {
                            val jsonElement: JsonElement = myCardsJsonArray.get(i)
                            val newMyCardItem =
                                Gson().fromJson(jsonElement, NewMyCardItem::class.java);

                            if (savedMyCard != null && savedMyCard.id == newMyCardItem.id) {
                                newMyCardItem.isSelect = true;
                            }


                            newMyPaymentMethodItemArrayList.add(newMyCardItem)
                        }


                        newMyPaymentMethodAdapter =
                            NewMyPaymentMethodAdapter(
                                baseContext,
                                newMyPaymentMethodItemArrayList,
                                false,
                                false
                            )

                        try {
                            newMyPaymentMethodAdapter!!.onNewMyPaymentMethodItemClickListener =
                                this@NewChangePaymentActivity
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        recyclerViewForMyPaymentsMethod.adapter = newMyPaymentMethodAdapter


                        val paymentMethodTypes =
                            newPaymentMethodsResponse.get("methods").asJsonArray

                        print(paymentMethodTypes)

                        val newPaymentMethodTypeItemArrayList =
                            ArrayList<NewPaymentMethodTypeItem>()


                        for (i in 0 until paymentMethodTypes.size()) {
                            val jsonElement: JsonElement = paymentMethodTypes.get(i)
                            val newPaymentMethodTypeItem =
                                Gson().fromJson(
                                    jsonElement,
                                    NewPaymentMethodTypeItem::class.java
                                );

                            if (savedMyCard != null && savedMyCard.id == newPaymentMethodTypeItem.id) {
                                newPaymentMethodTypeItem.isSelect = true;
                            }

//                            if (newPaymentMethodTypeItem.enabled) {
                            newPaymentMethodTypeItemArrayList.add(newPaymentMethodTypeItem)
//                            }

                        }





                        newPaymentTypesAdapter =
                            NewPaymentTypesAdapter(
                                baseContext,
                                newPaymentMethodTypeItemArrayList,
                                false
                            )

                        try {
                            newPaymentTypesAdapter!!.onPaymentTypeItemClickListener =
                                this@NewChangePaymentActivity
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        recyclerViewForNewPaymentTypes.adapter = newPaymentTypesAdapter


                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                },
                    {
                        changePaymentLoading.visibility = View.GONE
                        showMessage(R.string.service_loading_fail)
                    }

                )
        );
    }


    private fun initiateUI() {

        initPreapprovalRequest = InitPreapprovalRequest();

        userId = prefManager.loginUser["uid"].toString();

        val fullName = prefManager.loginUser["name"].toString().split(" ");

        firstName = fullName[0];
        lastName = fullName[1];
        email = prefManager.loginUser["email"].toString();
        phone = prefManager.loginUser["mobile"].toString();


        imageViewForMainChangePaymentBackBtn.setOnClickListener {
            finish()
        }

//        cardViewForSaveNewCard.setOnClickListener {
//
//            createPayHereRequest("Preapproval")
//
//        }

        recyclerViewForMyPaymentsMethod.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        recyclerViewForNewPaymentTypes.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {

            val response: PHResponse<StatusResponse> =
                data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT) as (PHResponse<StatusResponse>);

            if (resultCode == RESULT_OK) {


                if (response != null)
                    if (response.isSuccess)
                        getPaymentMethods()
                    else
                        showMessage("Result:$response")
                else
                    showMessage("Result: no response")


            } else if (resultCode == RESULT_CANCELED) {

                if (response != null) {
                    if (response.status != -5) {
                        showMessage(response.toString());
                    } else {

                        if (response.data.message != null)
                            showMessage(response.data.message);
                    }
                } else
                    showMessage("User canceled the request");
            } else {
                showMessage("no payhere working")
            }
        }

    }

    override fun onMyPaymentMethodClick(newMyCardItem: NewMyCardItem) {
        newMyCardItem.isSelect = true;
        val newMyCardItemJsonObject: JsonObject = Gson().toJsonTree(newMyCardItem).asJsonObject;
        prefManager.saveMyPaymentCard(newMyCardItemJsonObject);
        finish()
    }


    private fun createPayHereRequest(requestType: String) {


        val orderId = System.currentTimeMillis()

        initPreapprovalRequest!!.merchantId = PAYHERE_MERCHANT_ID;
        initPreapprovalRequest!!.merchantSecret = PAYHERE_MERCHANT_SECRET;
        initPreapprovalRequest!!.orderId = orderId.toString()
        initPreapprovalRequest!!.currency = "LKR";
        initPreapprovalRequest!!.itemsDescription = "PayHere Pre-Approval";
        initPreapprovalRequest!!.custom1 = userId;
        initPreapprovalRequest!!.customer.firstName = firstName;
        initPreapprovalRequest!!.customer.lastName = lastName;
        initPreapprovalRequest!!.customer.email = email;
        initPreapprovalRequest!!.customer.phone = phone;
        initPreapprovalRequest!!.customer.address.address = "";
        initPreapprovalRequest!!.customer.address.city = "";
        initPreapprovalRequest!!.customer.address.country = "";
        initPreapprovalRequest!!.notifyUrl = PAYHERE_PREAPPROVAL_NOTIFY_URL

        val intent: Intent = Intent(this, PHMainActivity::class.java)
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, initPreapprovalRequest);
        PHConfigs.setBaseUrl(PAYHERE_BASE_URL);
        startActivityForResult(
            intent,
            PAYHERE_REQUEST
        );
    }

    override fun onClickChangeCard() {

    }

    override fun clickPaymentTypeItem(newPaymentMethodTypeItem: NewPaymentMethodTypeItem) {


        if (newPaymentMethodTypeItem.id == "savedcards") {


            openCreatePayHereRequestBottomSlider()


        } else {
            print(newPaymentMethodTypeItem)

            val newPaymentType = NewMyCardItem(
                newPaymentMethodTypeItem.id,
                "",
                "",
                "",
                "",
                newPaymentMethodTypeItem.title,
                newPaymentMethodTypeItem.description,
                "",
                newPaymentMethodTypeItem.icon_url,
                true
            )

            val newMyCardItemJsonObject: JsonObject =
                Gson().toJsonTree(newPaymentType).asJsonObject;
            prefManager.saveMyPaymentCard(newMyCardItemJsonObject);
            finish()
        }
    }


    private fun openCreatePayHereRequestBottomSlider() {


        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(this@NewChangePaymentActivity);
        bottomSheetDialog.setContentView(R.layout.om_delete_address_bottom_sheet);
        bottomSheetDialog.show();


        val title: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.address)
        val description: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.delete_address)

        val yesButton: Button? = bottomSheetDialog.findViewById<Button>(R.id.delete_btn)
        val cancelButton: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.not_now)



        if (Constants.type == Constants.Type.LIFEPLUS) {
            yesButton?.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            yesButton?.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

        title?.text = "Attention!";
        description?.text =
            "Rs.10 will be charged from the card when this is done & that Rs.10 will be instantly refunded back to the card."
        yesButton?.text = "Ok"

        yesButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
            createPayHereRequest("Preapproval")
        }

        cancelButton?.visibility = View.GONE


    }
}
package com.ayubo.life.ayubolife.lifeplus

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.insurances.CommonApiInterface
import com.ayubo.life.ayubolife.payment.EXTRA_FEEDBACK_DETAIL
import com.ayubo.life.ayubolife.reports.activity.ReportByIdResponse
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.feed_back_popup.*
import kotlinx.android.synthetic.main.hydration_tracker_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedBackPopupActivity : Activity(), FeedBackImprovementAdapter.OnToDoItemCardClickListener {
    var feedBackImprovementItemArrayList = ArrayList<FeedBackImprovementItem>();
    lateinit var improvementRecyclerView: RecyclerView;
    lateinit var questionnaireRadioGroup: RadioGroup;
    lateinit var dynamic_questionnaire_list: RecyclerView;
    lateinit var topic: TextView;
    lateinit var description: TextView;
    lateinit var rating_topic: TextView;
    lateinit var support_email: TextView;
    lateinit var support_phone_no: TextView;
    lateinit var textArea_information: EditText;
    lateinit var improvement_text_area_liner_layout: LinearLayout;
    lateinit var horizontal_line: View;
    lateinit var feed_back_rating_bar: RatingBar;
    lateinit var appToken: String;
    lateinit var pref: PrefManager;
    var rating: Int = 5;

    var reference: String = ""
    var referenceId: String = ""
    var feedbackType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_back_popup)
        rating = 5;
        pref = PrefManager(baseContext);
        appToken = pref.getUserToken();
        reference = ""
        referenceId = ""
        feedbackType = ""

        page_close_btn.setOnClickListener {
            finish();
        }

        feed_back_rating_bar = findViewById<RatingBar>(R.id.feed_back_rating_bar);
        improvement_text_area_liner_layout =
            findViewById<LinearLayout>(R.id.improvement_text_area_liner_layout);
        horizontal_line = findViewById<View>(R.id.horizontal_line);
        topic = findViewById<TextView>(R.id.topic);
        description = findViewById<TextView>(R.id.description);
        rating_topic = findViewById<TextView>(R.id.rating_topic);
        support_email = findViewById<TextView>(R.id.support_email);
        support_phone_no = findViewById<TextView>(R.id.support_phone_no);
        textArea_information = findViewById<EditText>(R.id.textArea_information);

        feed_back_rating_bar.setOnRatingBarChangeListener { ratingBar, ratingValue, b ->
            System.out.println(ratingValue)

            if (ratingValue.toInt() > 0) {
                submit_feedback_btn.setBackgroundResource(R.drawable.radius_background_orange)
                submit_feedback_btn.isEnabled = true;
                rating = ratingValue.toInt();
                new_feedback_loading.visibility = View.VISIBLE
                getFeedBackData(rating).execute();
            } else {
                feedBackImprovementItemArrayList = ArrayList<FeedBackImprovementItem>();
            }


        }

        questionnaireRadioGroup = findViewById<RadioGroup>(R.id.questionnaireRadioGroup);

        readExtras();


        setRecycleViewData();


        getFeedBackData(rating).execute();

        if (feedbackType == "rating") {
            improvement_text_area_liner_layout.visibility = View.VISIBLE
            improvementRecyclerView.visibility = View.VISIBLE
            feed_back_rating_bar.visibility = View.VISIBLE
            horizontal_line.visibility = View.VISIBLE
            questionnaireRadioGroup.visibility = View.GONE
            rating_topic.visibility = View.GONE
        } else {
            improvement_text_area_liner_layout.visibility = View.GONE
            questionnaireRadioGroup.visibility = View.VISIBLE
            rating_topic.visibility = View.VISIBLE
            improvementRecyclerView.visibility = View.GONE
            feed_back_rating_bar.visibility = View.GONE
            horizontal_line.visibility = View.GONE
            submit_feedback_btn.setBackgroundResource(R.drawable.radius_background_orange)
            submit_feedback_btn.isEnabled = true;
        }


        submit_feedback_btn.setOnClickListener {
            submitFeedBackDetail();
        }

        support_phone_no.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${support_phone_no.text}")
            startActivity(intent)
        }


        val firstWord: String = "Couldn’t find what you need ? ";
        val secondWord: String = "Contact our customer support";


        val spannable: Spannable = SpannableString(firstWord + secondWord);

        val face1: Typeface? = ResourcesCompat.getFont(baseContext, R.font.montserrat_medium);

        val face2: Typeface? = ResourcesCompat.getFont(baseContext, R.font.montserrat_semi_bold);



        spannable.setSpan(
            CustomTypefaceSpan("montserrat", face1, R.color.color_727272),
            0,
            firstWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannable.setSpan(
            CustomTypefaceSpan("montserrat", face2, R.color.color_3B3B3B),
            firstWord.length,
            firstWord.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        feed_back_contact_support_text.setText(spannable);


    }

    fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_FEEDBACK_DETAIL)) {
            val meta = bundle.getSerializable(EXTRA_FEEDBACK_DETAIL) as String
            val split = meta.split(":");
            reference = split[0];
            referenceId = split[1];
            feedbackType = split[2];
//            doctor:doctorID:rating

        }
    }


    private fun submitFeedBackDetail() {
        new_feedback_loading.visibility = View.VISIBLE;

        System.out.println(feedBackImprovementItemArrayList)

        val feedBackImprovementTypes: ArrayList<FeedBackObj> = ArrayList<FeedBackObj>();

        for (feedBackImprovementItem in feedBackImprovementItemArrayList) {

            if (feedBackImprovementItem.isSelected) {

                val item: FeedBackObj = FeedBackObj(
                    feedBackImprovementItem.id,
                    feedBackImprovementItem.app_id,
                    feedBackImprovementItem.user_id,
                    feedBackImprovementItem.category,
                    feedBackImprovementItem.status,
                    feedBackImprovementItem.priority,
                    feedBackImprovementItem.rating,
                    feedBackImprovementItem._rid,
                    feedBackImprovementItem._self,
                    feedBackImprovementItem._etag,
                    feedBackImprovementItem._attachments,
                    feedBackImprovementItem._ts
                );


                feedBackImprovementTypes.add(item);
            }
        }

        val description: String = textArea_information.text.toString()

        val submitFeedbackObj: SubmitFeedbackObj = SubmitFeedbackObj(
            rating,
            description,
            feedBackImprovementTypes,
            reference,
            referenceId
        );


        val commonApiInterface: CommonApiInterface =
            ApiClient.getClient().create(CommonApiInterface::class.java);

        val submitFeedBackRequestCall: Call<ReportByIdResponse> =
            commonApiInterface.createSubmitFeedBack(
                appToken,
                AppConfig.APP_BRANDING_ID,
                submitFeedbackObj
            );



        submitFeedBackRequestCall.enqueue(object : Callback<ReportByIdResponse> {
            override fun onResponse(
                call: Call<ReportByIdResponse>,
                response: Response<ReportByIdResponse>
            ) {
                new_feedback_loading.visibility = View.GONE
                if (response.isSuccessful()) {
                    val intent = Intent(baseContext, FeedbackFinalPageActivity::class.java)
                    startActivity(intent);
                    finish();


                }


            }

            override fun onFailure(call: Call<ReportByIdResponse>, t: Throwable) {
                new_feedback_loading.visibility = View.GONE
                finish();
            }


        });
    }

    private fun setRecycleViewData() {


        improvementRecyclerView = findViewById<RecyclerView>(R.id.improvement_recycler_view);
        improvementRecyclerView.removeAllViews();


        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
//        improvementRecyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayout.HORIZONTAL, false)

        improvementRecyclerView.layoutManager = gridLayoutManager


        new_feedback_loading.visibility = View.VISIBLE


    }

    override fun onItemClick(item: FeedBackImprovementItem) {
        System.out.println(item)
        System.out.println(feedBackImprovementItemArrayList)


    }

    internal inner class getFeedBackData(ratingNum: Int) : AsyncTask<Void, Void, Void>() {

        var ratingNum = ratingNum;

        override fun doInBackground(vararg params: Void?): Void? {
            val apiService: ApiInterface =
                ApiClient.getReportsApiClientNewForHemas().create(ApiInterface::class.java);

            val call: Call<FeedBackResponse> =
                apiService.getFeedbackData(
                    appToken,
                    AppConfig.APP_BRANDING_ID,
                    ratingNum,
                    feedbackType
                );

            call.enqueue(object : Callback<FeedBackResponse> {
                override fun onResponse(
                    call: Call<FeedBackResponse>,
                    response: Response<FeedBackResponse>
                ) {
                    if (response.isSuccessful) {
                        new_feedback_loading.visibility = View.GONE
                        System.out.println(response)

                        val mainResponse: FeedBackResponse? = response.body();

                        val mainResponseData =
                            Gson().toJsonTree(mainResponse?.getData()).asJsonObject;
                        topic.text = mainResponseData.get("title").asString;
                        description.text = mainResponseData.get("summary").asString;
                        rating_topic.text = mainResponseData.get("question").asString;
                        support_email.text = mainResponseData.get("support_email").asString;
                        support_phone_no.text = mainResponseData.get("support_number").asString;
                        val feedBackDataArrayList: JsonArray =
                            mainResponseData.get("categories").asJsonArray

                        feedBackImprovementItemArrayList = ArrayList<FeedBackImprovementItem>();
                        for (item in feedBackDataArrayList) {
                            val jsonElement: JsonElement = item;
                            val jsonObject: JsonObject = jsonElement.getAsJsonObject();

                            val feedBackImprovementItem = FeedBackImprovementItem(
                                jsonObject.get("id").asString,
                                jsonObject.get("app_id").asString,
                                jsonObject.get("user_id").asString,
                                jsonObject.get("category").asString,
                                jsonObject.get("status").asString,
                                jsonObject.get("priority").asInt,
                                if (jsonObject.get("rating") == null) "" else jsonObject.get("rating").asString,
                                jsonObject.get("_rid").asString,
                                jsonObject.get("_self").asString,
                                jsonObject.get("_etag").asString,
                                jsonObject.get("_attachments").asString,
                                jsonObject.get("_ts").asInt,
                                false

                            );

                            feedBackImprovementItemArrayList.add(feedBackImprovementItem);
                        }

                        if (feedbackType == "rating") {

                            val feedBackImprovementItemAdapter = FeedBackImprovementAdapter(
                                baseContext,
                                feedBackImprovementItemArrayList,
                                this@FeedBackPopupActivity
                            )
                            improvementRecyclerView.adapter = feedBackImprovementItemAdapter
                        } else {
                            questionnaireRadioGroup.removeAllViews();
                            if (feedBackImprovementItemArrayList.size > 0) {
                                feedBackImprovementItemArrayList.forEachIndexed { index, feedBackImprovementItem ->
                                    val radioButton = RadioButton(baseContext);
                                    val face: Typeface? =
                                        ResourcesCompat.getFont(
                                            baseContext,
                                            R.font.montserrat_medium
                                        );

                                    val radioParams: RadioGroup.LayoutParams =
                                        RadioGroup.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        );
                                    radioParams.setMargins(0, 0, 0, 10);
                                    radioButton.layoutParams = radioParams;
                                    radioButton.id = index
                                    radioButton.typeface = face;
                                    radioButton.text = feedBackImprovementItem.category;
                                    radioButton.textSize = 10F;
                                    radioButton.setTextColor(baseContext.resources.getColor(R.color.color_3B3B3B))

                                    var radioColor: String = "#ff860b";
                                    val radioDisableColor: String = "#C3C3C3";

                                    if (Constants.type == Constants.Type.LIFEPLUS) {
                                        radioColor = "#0AC350";
                                    }

                                    val colorStateList = ColorStateList(
                                        arrayOf(
                                            intArrayOf(-android.R.attr.state_checked), // unchecked
                                            intArrayOf(android.R.attr.state_checked) // checked
                                        ), intArrayOf(
                                            Color.parseColor(radioDisableColor), // unchecked color
                                            Color.parseColor(radioColor)  // checked color
                                        )
                                    )

                                    // finally, set the radio button's button tint list
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        radioButton.buttonTintList = colorStateList
                                    }

                                    // optionally set the button tint mode or tint blend mode
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        radioButton.buttonTintBlendMode = BlendMode.SRC_IN
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            radioButton.buttonTintMode = PorterDuff.Mode.SRC_IN
                                        }
                                    }



                                    radioButton.setOnClickListener {
                                        feedBackImprovementItem.isSelected = true

                                        feedBackImprovementItemArrayList.forEachIndexed { index, fItem ->

                                            if (fItem.id !== feedBackImprovementItem.id) {
                                                fItem.isSelected = false
                                            }
                                        }

                                        if (feedBackImprovementItem.category.toLowerCase() == "other") {
                                            improvement_text_area_liner_layout.visibility =
                                                View.VISIBLE
                                        } else {
                                            improvement_text_area_liner_layout.visibility =
                                                View.GONE
                                            textArea_information.setText("");
                                        }


                                    }

                                    if (index == 0) {

                                        if (feedBackImprovementItem.category.toLowerCase() == "other") {
                                            improvement_text_area_liner_layout.visibility =
                                                View.VISIBLE
                                        } else {
                                            improvement_text_area_liner_layout.visibility =
                                                View.GONE
                                            textArea_information.setText("");
                                        }


                                        radioButton.isChecked = true;
                                        feedBackImprovementItem.isSelected = true
                                    }

                                    questionnaireRadioGroup.addView(radioButton);
                                }

                            }


                        }


                    } else {
                        new_feedback_loading.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<FeedBackResponse>, t: Throwable) {
                    t.printStackTrace()
                    new_feedback_loading.visibility = View.GONE
                }

            });


            return null;
        }


    }


}
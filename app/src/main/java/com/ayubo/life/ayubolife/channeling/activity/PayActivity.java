package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.webrtc.App;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.config.Constants;
import com.ayubo.life.ayubolife.channeling.model.Booking;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.model.User;
import com.ayubo.life.ayubolife.channeling.model.VideoBooking;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.util.PhoneMask;
import com.ayubo.life.ayubolife.channeling.view.PinEntryDialogActionView;

public class PayActivity extends AppCompatActivity {

    //constants
    private static final int REQUEST_CODE_DIALOG = 101;
    public static final String EXTRA_BOOKING_OBJECT = "booking_object";
    public static final String EXTRA_VIDEO_BOOKING_OBJECT = "video_booking_object";

    //instances
    private RadioButton selectedButton;
    private Booking booking;
    private VideoBooking videoBooking;
    private MobileNumberView numberView;

    //primary data
    private String trId;
    private boolean isProcessing = false;
    private  boolean isDialogAddToBill=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

     //   Toolbar toolbar = findViewById(R.id.toolbar_pay);
      //  setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        readData(getIntent());
        initView();

        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void readData(Intent intent) {
        if (intent.getExtras() != null) {
            if (intent.getExtras().containsKey(EXTRA_BOOKING_OBJECT))
                booking = (Booking) intent.getExtras().getSerializable(EXTRA_BOOKING_OBJECT);
            else if (intent.getExtras().containsKey(EXTRA_VIDEO_BOOKING_OBJECT))
                videoBooking = (VideoBooking) intent.getExtras().getSerializable(EXTRA_VIDEO_BOOKING_OBJECT);
        }
    }

    private void initView() {
      //  View dialogPayView = findViewById(R.id.layout_dialog_payment);
        View cardPayView = findViewById(R.id.layout_card_payment);

        TextView txtPrice = findViewById(R.id.txt_price_proceed);
        if (booking != null)
            txtPrice.setText(String.format(Locale.getDefault(), "LKR : %,.2f", (booking.getPrice().getTotal() * 1.0)));
        else if(videoBooking != null)
            txtPrice.setText(String.format(Locale.getDefault(), "LKR : %,.2f", (videoBooking.getAmount())));

        isDialogAddToBill=false;

        numberView = new MobileNumberView();
        numberView.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        User user = new Gson().fromJson(sharedPreferences.getString(Constants.PREFERENCE_USER_OBJECT, ""), User.class);
        if (user != null && user.getPhone().startsWith("077")) {
            String number = String.format(Locale.getDefault(), "(%s) %s %s", user.getPhone().substring(0, 3),
                    user.getPhone().substring(3, 6), user.getPhone().substring(6));
            numberView.setText(number);
        }

        setRadioButton(cardPayView, ContextCompat.getDrawable(this, R.drawable.visa),
                getString(R.string.online_payment), false);

//        setRadioButton(dialogPayView, ContextCompat.getDrawable(this, R.drawable.dialog),
//                getString(R.string.add_to_dialog_bill), true);


        TextView btnPay = findViewById(R.id.txt_proceed_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isProcessing) {
                    isProcessing = true;
                    if (selectedButton != null) {

                        if (booking != null || videoBooking != null) {
                            String url;
                            if(isDialogAddToBill){
                                sendDialogPaymentRequest(numberView.getText());
                            }else{
                                if (booking != null)
                                    url = booking.getPayment().getHnb_payment_link();
                                else
                                    url = videoBooking.getPaymentLink();

                                if (url != null && !url.equals("")) {
                                    Log.d(PayActivity.class.getSimpleName(), "Payment Link - " + url);
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ((App) getApplication()).setBooking(null);
                                    startActivity(browserIntent);
                                    finish();
                                }
                            }

                        } else {
                            isProcessing = false;
                            Toast.makeText(PayActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        isProcessing = false;
                        Toast.makeText(PayActivity.this, R.string.error_no_payment_method, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void setRadioButton(View view, Drawable image, String title, final boolean showNumberView) {
        ImageView imgIcon = view.findViewById(R.id.img_payment);
        TextView txtMethodName = view.findViewById(R.id.txt_title_payment);
        final RadioButton radioButton = view.findViewById(R.id.radio_select_payment_row);

        imgIcon.setImageDrawable(image);
        txtMethodName.setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRadioButtonClicked(radioButton, showNumberView);

            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(radioButton, showNumberView);
            }
        });
    }

    private void onRadioButtonClicked(RadioButton radioButton, boolean showNumberView) {
        if (selectedButton != null)
            selectedButton.setChecked(false);
        radioButton.setChecked(true);
        selectedButton = radioButton;

        if(showNumberView){
            isDialogAddToBill=true;
        }
        numberView.setVisibility(showNumberView ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    class MobileNumberView {
        EditText edtNumber;
        TextView txtNumber;

        MobileNumberView() {
            edtNumber = findViewById(R.id.edt_proceed_number);
            txtNumber = findViewById(R.id.txt_proceed_mobile);

            edtNumber.addTextChangedListener(new PhoneMask());
        }

        void setText(String number) {
            edtNumber.setText(number);
        }

        void setVisibility(int visibility) {
            edtNumber.setVisibility(visibility);
            txtNumber.setVisibility(visibility);
        }

        String getText() {
            String value = edtNumber.getText().toString();
            value = value.replace(" ", "");
            value = value.replace("(", "");
            value = value.replace(")", "");

            return value;
        }
    }

    private void sendDialogPaymentRequest(String number) {
        String link = "";
        if (booking != null) {
            trId = String.valueOf(booking.getAppointment().getRef());
            link = AppConfig.METHOD_SOAP_DAILOG_PAY_CHANELLING;
        } else if (videoBooking != null) {
            trId = videoBooking.getPaymentLink();
            link = AppConfig.METHOD_SOAP_DAILOG_PAY_VIDEO;
        }
        number = "94" + number.substring(1);

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(link, new DialogPayParams(trId, number).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        readResponse(response);
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        isProcessing = false;
                        Toast.makeText(PayActivity.this, "Error - " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    private void readResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("result") == 0) {
                String correlator = jsonObject.getJSONObject("data").getString("clientcorrelator");
                Intent intent = new Intent(PayActivity.this, CustomDigitEntryDialogActivity.class);
                intent.putExtra("correlator", correlator);
                intent.putExtra("trId", trId);
               // intent.putExtra(CustomDigitEntryDialogActivity.EXTRA_ACTION_OBJECT, new PinEntryDialogActionView(PayActivity.this,correlator, trId));
                startActivityForResult(intent, REQUEST_CODE_DIALOG);

            } else {
                isProcessing = false;
                Toast.makeText(PayActivity.this, "Error - " + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            isProcessing = false;
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DIALOG)
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(PayActivity.this, ResultActivity.class);
                if (booking != null)
                    intent.putExtra(ResultActivity.EXTRA_BOOKING_OBJECT, booking);
                else if (videoBooking != null)
                    intent.putExtra(ResultActivity.EXTRA_VIDEO_BOOKING_OBJECT, videoBooking);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                ((App) getApplication()).setBooking(null);
                finish();
            } else if (resultCode == RESULT_FIRST_USER) {
                sendDialogPaymentRequest(numberView.getText());
            } else {
                isProcessing = false;
            }
    }

    class DialogPayParams extends SoapBasicParams {
        private String trId;
        private String dialogNo;



        DialogPayParams(String trId, String dialogNo) {

            this.trId = trId;
            this.dialogNo = dialogNo;
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();

            PrefManager pref = new PrefManager(PayActivity.this);
            user_id = pref.getLoginUser().get("uid");

            try {
                jsonObject.put("b", user_id);
                jsonObject.put("c", trId);
                jsonObject.put("d", dialogNo);
                jsonObject.put("a", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}

package com.ayubo.life.ayubolife.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.PrivacyActivity;
import com.ayubo.life.ayubolife.activity.TermsActivity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.lifeplus.ProfileNew;
import com.ayubo.life.ayubolife.login.model.RegisterMainResponse;
import com.ayubo.life.ayubolife.reports.activity.AutoCompleteRecordTypeAdapter;
import com.ayubo.life.ayubolife.reports.activity.ReportTypeObject;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.flavors.changes.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LetsGo extends AppCompatActivity implements AutoCompleteRecordTypeAdapter.OnItemClickListener {
    ApiInterface apiService;

    EditText txt_fname, txt_lname, txt_email;
    // ImageButton btnDate;
    TextView lbl_gender, lbl_date;

    AutoCompleteTextView auto_complete_text_view_gender;

    String snewGender, birthday_db;
    String sDate, sMonth, sYear;
    String s_fname, s_lname, email, country_code;

    boolean isDialogPopup = true;

    int selectYear, year, month, day;

    Calendar calendar;

    LinearLayout date_picker_layout;

    ProgressDialog prgDialog;

    boolean firsttime = false;
    // LazyDatePicker lazyDatePicker;
    boolean male = false;
    boolean female = false;
    String sttus, mobile, name, loginStatus;
    HashMap<String, String> user = null;
    private PrefManager prefManager;
    Button btn_LetsGo;
    ImageView brand_logo;
    TextView txt_privacypolicy, txt_termscondition;
    android.app.AlertDialog dialogView;

    ArrayList<ReportTypeObject> genderArrayList = new ArrayList<ReportTypeObject>();

    Boolean isChangedData = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#242424";
        int whiteInt = Color.parseColor(noticolor);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(whiteInt);
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();


            Drawable background = getResources().getDrawable(R.drawable.ayubo_registration_bg);
            if (Constants.type == Constants.Type.LIFEPLUS) {
                background = getResources().getDrawable(R.drawable.life_plus_registration);
            } else {
                background = getResources().getDrawable(R.drawable.ayubo_registration_bg);
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }


        setContentView(R.layout.activity_lets_go);
        genderArrayList = new ArrayList<ReportTypeObject>();
        genderArrayList.add(new ReportTypeObject("1", "Male"));
        genderArrayList.add(new ReportTypeObject("2", "Female"));
        isChangedData = false;
        localConstructor();


//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//
//            } else {
//                sttus= prefManager.getUserRegisterStatus();
//              //  mobile= prefManager.getMobileNumber();
//              //  name= prefManager.getPrefName();
//                user=prefManager.getLoginUser();
//
//                name=user.get("name");
//                mobile=user.get("mobile");
//
//                System.out.println("======================"+mobile+"   "+name);
//
//                if(sttus.equals("false")){
//
//                }else{
//                    Intent hhtpIntent = new Intent(LetsGo.this, EnterPromoCodeActivity.class);
//                    hhtpIntent.putExtra("fromClass", "no");
//                    startActivity(hhtpIntent);
//                    finish();
//                }
//            }
//        }


//        btn_Male.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                btn_Male.setBackground(getResources().getDrawable(R.drawable.transferent_boder_button_selected));
//                btn_FeMale.setBackground(getResources().getDrawable(R.drawable.transferent_boder_button));
//                gender.setText("Male");
//                btn_Male.setTextColor(Color.parseColor("#f87515"));
//                btn_FeMale.setTextColor(Color.parseColor("#ffffff"));
//                snewGender = "1";
//
//
//            }
//        });
//        btn_FeMale.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                btn_Male.setBackground(getResources().getDrawable(R.drawable.transferent_boder_button));
//                btn_FeMale.setBackground(getResources().getDrawable(R.drawable.transferent_boder_button_selected));
//                gender.setText("Female");
//                btn_Male.setTextColor(Color.parseColor("#ffffff"));
//                btn_FeMale.setTextColor(Color.parseColor("#f87515"));
//                snewGender = "2";
//            }
//        });

        if (Constants.type == Constants.Type.LIFEPLUS) {
            brand_logo.setImageResource(R.drawable.life_plus_logo);
            btn_LetsGo.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button);

        } else {
            brand_logo.setImageResource(R.drawable.correct_ayubo_logo);
            btn_LetsGo.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button);
        }


    }


    void localConstructor() {

        country_code = getIntent().getStringExtra("country_code");
        //  lazyDatePicker = findViewById(R.id.edt_dob_user);
        //   datePicker1 = findViewById(R.id.datePicker1);
        isDialogPopup = false;

        //  datePicker1.setVisibility(View.GONE);
        prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        apiService = null;

        date_picker_layout = (LinearLayout) findViewById(R.id.date_picker_layout);
        //  btnDate =(ImageButton) findViewById(R.id.btnDate);
//        btn_Male = (TextView) findViewById(R.id.btn_Male);
        lbl_gender = (TextView) findViewById(R.id.lbl_gender);

        txt_termscondition = (TextView) findViewById(R.id.txt_termscondition);
        txt_privacypolicy = (TextView) findViewById(R.id.txt_privacypolicy);
        btn_LetsGo = (Button) findViewById(R.id.btn_LetsGo);
        brand_logo = (ImageView) findViewById(R.id.brand_logo);
        auto_complete_text_view_gender = (AutoCompleteTextView) findViewById(R.id.auto_complete_text_view_gender);
        String birthYear = "";
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(birthYear);
            //  lazyDatePicker.setDate(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        txt_termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                Intent i = new Intent(LetsGo.this, TermsActivity.class);
                startActivity(i);

            }
        });
        txt_privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                Intent i = new Intent(LetsGo.this, PrivacyActivity.class);
                startActivity(i);


            }
        });
        btn_LetsGo.setImeOptions(EditorInfo.IME_ACTION_DONE);

        lbl_date = (TextView) findViewById(R.id.lbl_date);

        txt_fname = (EditText) findViewById(R.id.txt_fname);
        txt_lname = (EditText) findViewById(R.id.txt_lname);
        txt_email = (EditText) findViewById(R.id.txt_email);

        txt_fname.setText(name);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //   showDate(year, month + 1, day);
        birthday_db = "";
        email = "";
        snewGender = "";

        lbl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDialogPopup) {
                    isDialogPopup = true;
                    showAlert_Add(LetsGo.this);
                }

            }
        });
        date_picker_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDialogPopup) {
                    isDialogPopup = true;
                    showAlert_Add(LetsGo.this);
                }
            }
        });


        AutoCompleteRecordTypeAdapter adapter = new AutoCompleteRecordTypeAdapter(getBaseContext(), genderArrayList);
        auto_complete_text_view_gender.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        auto_complete_text_view_gender.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("dismiss");
            }
        });


        lbl_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lbl_gender.setVisibility(View.GONE);
                auto_complete_text_view_gender.setVisibility(View.VISIBLE);
                auto_complete_text_view_gender.showDropDown();
            }
        });


    }


    public void showAlert_Add(Context c) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_date_picker, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        dialogView = builder.create();
        dialogView.setCancelable(false);
        final DatePicker picker = (DatePicker) layoutView.findViewById(R.id.datePicker1);


        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDialogPopup = false;
                dialogView.cancel();

                //   finish();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDialogPopup = false;
                dialogView.cancel();
                System.out.println("Selected Date: " + picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
                String DateStr0 = picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear();
                String DateStr = picker.getYear() + "/" + (picker.getMonth() + 1) + "/" + picker.getDayOfMonth();
                birthday_db = DateStr;
                lbl_date.setText(DateStr);

            }
        });
        dialogView.show();
    }

    public void Login(View v) {

        s_fname = txt_fname.getText().toString();
        s_lname = txt_lname.getText().toString();
        email = txt_email.getText().toString();

        email = email.trim();
        s_fname = s_fname.trim();
        s_fname = s_fname.replaceAll(" ", "");

        s_lname = s_lname.trim();
        s_lname = s_lname.replaceAll(" ", "");
        snewGender = snewGender.trim();

        if (s_fname.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.toast_firstname_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if (!Utility.isAlpha(s_fname)) {
            Toast.makeText(getApplicationContext(), R.string.toast_firstname_onlycharacters, Toast.LENGTH_LONG).show();
            return;
        }
        if (s_lname.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.toast_lastname_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if (!Utility.isAlpha(s_lname)) {
            Toast.makeText(getApplicationContext(), R.string.toast_lastname_onlycharacters, Toast.LENGTH_LONG).show();
            return;
        }
//        if(email.equals("")){
//            Toast.makeText(getApplicationContext(), R.string.toast_email_empty, Toast.LENGTH_LONG).show();
//            return;
//        }

        if (birthday_db.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.toast_birthday_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if (snewGender.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.toast_gender_empty, Toast.LENGTH_LONG).show();
            return;
        } else {


            //=============SERVICE CALL=================================
            click_GoWithMobileNumber();
            //=============SERVICE CALL=================================
        }


    }


    public void click_GoWithMobileNumber() {

        if (Utility.isInternetAvailable(this)) {

            click_GoWithMobileNumberNew();
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
        }
    }


    public void click_GoWithMobileNumberNew() {

        final ProgressDialog progressDialog = new ProgressDialog(LetsGo.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        prefManager = new PrefManager(this);
        mobile = prefManager.getMobileNumber();


        if (SplashScreen.firebaseAnalytics != null) {
            //  FirebaseAnalytics Adding
            SplashScreen.firebaseAnalytics.setUserProperty("name", s_fname + " " + s_lname);
            SplashScreen.firebaseAnalytics.setUserProperty("phone", mobile);
            SplashScreen.firebaseAnalytics.setUserProperty("email", email);
            SplashScreen.firebaseAnalytics.setUserProperty("age", birthday_db);
            if (snewGender.equals("1")) {
                SplashScreen.firebaseAnalytics.setUserProperty("gender", "male");
            } else {
                SplashScreen.firebaseAnalytics.setUserProperty("gender", "female");
            }

        }

        String serviceName = "createAMobileUser";

        String jsonStr =
                "{" +
                        "\"mobile_number\": \"" + mobile + "\"," +
                        "\"first_name\": \"" + s_fname + "\"," +
                        "\"last_name\": \"" + s_lname + "\"," +
                        "\"date_of_birth\": \"" + birthday_db + "\", " +
                        "\"gender\": \"" + snewGender + "\", " +
                        "\"email\": \"" + email + "\", " +
                        "\"app_name\": \"" + AppConfig.APP_BRANDING_ID + "\", " +
                        "\"country_code\": \"" + country_code + "\", " +
                        "\"api_key\": \"" + "api-key" + "\"" +
                        "}";


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterMainResponse> call = apiService.createAMobileUserNew(AppConfig.APP_BRANDING_ID, AppConfig.SUGER_API_TOKEN, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<RegisterMainResponse>() {
            @Override
            public void onResponse(Call<RegisterMainResponse> call, Response<RegisterMainResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        RegisterMainResponse dataObj = response.body();
                        String userId = dataObj.getUserId();
                        String first_name = dataObj.getFirstName();
                        String last_name = dataObj.getLastName();
                        String email = dataObj.getEmail();
                        String profile_pic = dataObj.getProfilePic();
                        String hashToken = dataObj.getHashToken();
                        String dob = dataObj.getDateOfBirth();
                        String gender = dataObj.getGender();


                        prefManager.createUserProfile(
                                first_name + " " + last_name,
                                email,
                                dob,
                                gender,
                                "",
                                mobile,
                                country_code,
                                profile_pic,
                                userId,
                                mobile

                        );
                        prefManager.createLoginUser(userId, first_name + " " + last_name, email, mobile, hashToken, profile_pic, country_code);


                        prefManager.setUserToken("Bearer " + dataObj.getLumenToken());

                        Intent in = null;

                        if (Constants.type == Constants.Type.LIFEPLUS) {
                            in = new Intent(LetsGo.this, ProfileNew.class);
                        } else {
                            in = new Intent(LetsGo.this, EnterPromoCodeActivity.class);
                        }


                        in.putExtra("fromClass", "register");
                        startActivity(in);
                        finish();

                    }


                }

            }

            @Override
            public void onFailure(Call<RegisterMainResponse> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(LetsGo.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

            }
        });
    }


    public void selectDate(View view) {
        showAlert_Add(LetsGo.this);
        // datePicker1.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(ReportTypeObject reportTypeObject) {
        lbl_gender.setVisibility(View.VISIBLE);
        auto_complete_text_view_gender.setVisibility(View.GONE);
        lbl_gender.setText(reportTypeObject.getName());
        auto_complete_text_view_gender.dismissDropDown();

        ReportTypeObject selectedGenderObj = null;

        for (ReportTypeObject genderObj : genderArrayList) {
            if (genderObj.getName().equals(reportTypeObject.getName())) {
                selectedGenderObj = genderObj;
            }
        }

        assert selectedGenderObj != null;
        snewGender = selectedGenderObj.getId();
    }
}

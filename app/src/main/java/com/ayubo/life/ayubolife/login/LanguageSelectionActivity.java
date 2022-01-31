package com.ayubo.life.ayubolife.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.Mobile_Login_Activity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.login.model.GetDefaultLanguageMainData;
import com.ayubo.life.ayubolife.login.model.GetDefaultLanguageMainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.flavors.changes.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    private Integer gotoBtnItemIndex;
    com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo progressBar;
    //    private WheelPicker wheelCenter;
    String[] Languages = {"English", "සින්හල", "ぬふあて"};
    PrefManager pref;
    ArrayList<String> arrayList = null;
    Integer selectedPosition;
    TextView txt_caption;
    String langPosition;
    List<GetDefaultLanguageMainData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#c39e0f";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }
        if (Constants.type == Constants.Type.LIFEPLUS) {
            String noticolr = "#000000";
            int whiteInta = Color.parseColor(noticolr);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(whiteInta);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();

                //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                decor.setSystemUiVisibility(0);
                //   decor.setSystemUiVisibility(0);
                // }
            }
        }


        setContentView(R.layout.activity_language_selection2);
        pref = new PrefManager(this);
        arrayList = new ArrayList<String>();

        // arrayList.add("සිංහල");
        arrayList.add("English");
        // arrayList.add("ぬふあて");
//        wheelCenter = (WheelPicker) findViewById(R.id.main_wheel_center);
//
//
//        wheelCenter.setOnItemSelectedListener(this);
//        wheelCenter.setData(arrayList);
//        wheelCenter.setVisibleItemCount(2);
//        wheelCenter.setCurved(false);
//
//        wheelCenter.setCurtainColor(Color.parseColor("#519c3f"));

        progressBar = findViewById(R.id.progressBar);
        txt_caption = findViewById(R.id.txt_caption);
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDefaultLanguage();


    }

    public void getDefaultLanguage() {
        String appToken = pref.getUserToken();

        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClientBase().create(ApiInterface.class);
        Call<GetDefaultLanguageMainResponse> call = apiService.getDefaultLanguage(AppConfig.APP_BRANDING_ID, appToken, "");
        call.enqueue(new Callback<GetDefaultLanguageMainResponse>() {
            @Override
            public void onResponse(Call<GetDefaultLanguageMainResponse> call, Response<GetDefaultLanguageMainResponse> response) {
                progressBar.setVisibility(View.GONE);
                boolean status = response.isSuccessful();
                if (status) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(LanguageSelectionActivity.this, LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            list = response.body().getData();

                            arrayList.clear();

                            selectedPosition = 1;
                            for (int i = 0; i < list.size(); i++) {
                                arrayList.add(list.get(i).getName());
                                if (list.get(i).getDefault()) {
                                    selectedPosition = i;
                                    txt_caption.setText(list.get(i).getParagraphText());
                                }
                            }
//                            wheelCenter.setData(arrayList);
//                            wheelCenter.setVisibleItemCount(list.size());
//
//                            wheelCenter.setData(arrayList);
//                            wheelCenter.setSelectedItemPosition(selectedPosition);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<GetDefaultLanguageMainResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("========t======" + t);
            }
        });
    }

    public void sendDefaultLanguage() {
        String appToken = pref.getUserToken();
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClientBase().create(ApiInterface.class);
        Call<GetDefaultLanguageMainResponse> call = apiService.setDefaultLanguage(AppConfig.APP_BRANDING_ID, appToken, langPosition);
        call.enqueue(new Callback<GetDefaultLanguageMainResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetDefaultLanguageMainResponse> call, @NonNull Response<GetDefaultLanguageMainResponse> response) {
                progressBar.setVisibility(View.GONE);
                boolean status = response.isSuccessful();
                if (status) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {

                            Intent in = new Intent(LanguageSelectionActivity.this, LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            Toast.makeText(LanguageSelectionActivity.this, "Default language set", Toast.LENGTH_LONG).show();
                            finish();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDefaultLanguageMainResponse> call, Throwable t) {
                System.out.println("========t======" + t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public void goLogin(View v) {
        setLanguage();
        Intent in = new Intent(LanguageSelectionActivity.this, Mobile_Login_Activity.class);
        startActivity(in);
    }

    private void setLanguage() {

        //   String Text = spinner2.getSelectedItem().toString();
        String Text = "";

        String languageToLoad = null;
        if (Text.equals("English")) {
            languageToLoad = "en";
        } else if (Text.equals("සින්හල")) {
            languageToLoad = "si";
        } else {
            languageToLoad = "en";
        }
        // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void randomlySetGotoBtnIndex() {
//        gotoBtnItemIndex = (int) (Math.random() * wheelCenter.getData().size());
        //  gotoBtn.setText("Goto '" + wheelCenter.getData().get(gotoBtnItemIndex) + "'");
    }


//    @Override
//    public void onItemSelected(WheelPicker picker, Object data, int position) {
//        String text = "";
//        switch (picker.getId()) {
//
//            case R.id.main_wheel_center:
//                text = "Center:";
//                break;
//        }
//        String textStr= list.get(position).getParagraphText();
//        langPosition= list.get(position).getId().toString();
//        txt_caption.setText(textStr);
//
//        Log.d("====LS====",textStr);
//
//    }

//    @Override
//    public void onClick(View v) {
//        wheelCenter.setSelectedItemPosition(gotoBtnItemIndex);
//        randomlySetGotoBtnIndex();
//    }


    public void setDefaultLanguage(View view) {

        sendDefaultLanguage();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
package com.ayubo.life.ayubolife.post.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.model.NativeButtonObj;
import com.ayubo.life.ayubolife.post.adapter.NativePostTemplateAdapter;
import com.ayubo.life.ayubolife.post.model.ConciergeMainResponse;
import com.ayubo.life.ayubolife.post.model.Template;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NativePostActivity extends BaseActivity {

    //view
    private ProgressBar progress;
    private RecyclerView recyclerView;
    private Context context;
    PrefManager prefManager;

    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_post_activity);

        progress = findViewById(R.id.progress_post);
        recyclerView = findViewById(R.id.recycler_posts);
        context = NativePostActivity.this;
        prefManager = new PrefManager(NativePostActivity.this);
        status = prefManager.getMyGoalData().get("my_goal_status");
        String meta = getIntent().getStringExtra("meta");

        Log.d("getNativePost..", meta);


        AppEventsLogger loggerFB = AppEventsLogger.newLogger(this);
        loggerFB = AppEventsLogger.newLogger(this);

        Bundle params = new Bundle();
        params.putString(AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME, AppConfig.FACEBOOK_EVENT_ID_NATIVE_VIEW);
        params.putString(AppConfig.FACEBOOK_EVENT_ID_META, meta);
        loggerFB.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params);


        callViewGoals(meta);


    }


    private void conciergeCallBack(String moblieNumber) {
        progress.setVisibility(View.VISIBLE);
        ApiInterface apiService = new ApiClient().getClientWith_V6().create(ApiInterface.class);

        String jsonStr =
                "{" +
                        "\"mobile_number\": \"" + moblieNumber + "\"" +
                        "}";

        String token = "4c41334b31554e5369615055574666544271694961453769344852726767494947512b6a7365586d686b6b3d";
        String url = ApiClient.BASE_URL + "custom/service/v6/rest.php";
        Call<ConciergeMainResponse> modulesCall = apiService.getConciergeCallBack(url, AppConfig.APP_BRANDING_ID, token, "conciergeCallBackSMS", "JSON", "JSON", jsonStr);


        modulesCall.enqueue(new Callback<ConciergeMainResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConciergeMainResponse> call, @NonNull Response<ConciergeMainResponse> response) {
                if (response.body() != null) {

                    try {
                        if (response.isSuccessful()) {

                            String msg = response.body().getMessage();
                            showAlert_AddGoal(NativePostActivity.this, "", msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(NativePostActivity.this, response.message(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ConciergeMainResponse> call, @NonNull Throwable t) {
                Toast.makeText(NativePostActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }
        });
    }


    public void showAlert_AddGoal(Context c, String title, String msg) {


        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setVisibility(View.INVISIBLE);
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setText("OK");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.show();


    }

    public void callViewGoals(String meta) {
        progress.setVisibility(View.VISIBLE);
        String token = prefManager.getUserToken();
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<ResponseBody> modulesCall = apiService.getNativePost(AppConfig.APP_BRANDING_ID, token, meta);
        modulesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    System.out.println("=============raw============" + response.raw().toString());

                    try {
                        setAdapter(readResponse(response.body().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(NativePostActivity.this, response.message(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(NativePostActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }
        });
    }


    private void getNativePost(String meta) {
        progress.setVisibility(View.VISIBLE);
        ApiInterface apiService = new ApiClient().getClient3().create(ApiInterface.class);
        String token = prefManager.getUserToken();
        System.out.println("========token=======" + token);
        Call<ResponseBody> modulesCall = apiService.getNativePost(AppConfig.APP_BRANDING_ID, token, meta);
        modulesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    System.out.println("=============raw============" + response.raw().toString());

                    try {
                        setAdapter(readResponse(response.body().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(NativePostActivity.this, response.message(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(NativePostActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }
        });
    }

    private List<Template> readResponse(String response) {
        List<Template> templates = new ArrayList<>();
        try {
            System.out.println("============7============");
            System.out.println(response);
            System.out.println("============8============");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("template");

            for (int i = 0; i < jsonArray.length(); i++) {
                Template template = new Template();
                JSONObject obj = (JSONObject) jsonArray.get(i);
                switch (obj.getString("type")) {

                    case "heading":
                    case "sub-heading":
                    case "paragraph":
                        template = new Gson().fromJson(obj.toString(), Template.TextTemplate.class);
                        String ttt = template.getType();
                        break;
                    case "bulleted-list":
                    case "numbered-list":
                        template = new Gson().fromJson(obj.toString(), Template.ListTemplate.class);
                        break;
                    case "price":
                        template = new Gson().fromJson(obj.toString(), Template.PriceTemplate.class);
                        break;
                    case "image-sub":
                    case "video-sub":
                    case "image-full":
                    case "video-full":
                    case "gif":
                        template = new Gson().fromJson(obj.toString(), Template.MediaTemplate.class);
                        break;
                    case "table":
                        template = new Gson().fromJson(obj.toString(), Template.TableTemplate.class);
                        break;
                    case "terms":
                        template = new Gson().fromJson(obj.toString(), Template.TermTemplate.class);
                        break;
                    case "termsnconditions":
                        template = new Gson().fromJson(obj.toString(), Template.TermAndConditionTemplate.class);
                        break;
                    case "button":
                        template = new Gson().fromJson(obj.toString(), Template.ButtonTemplate.class);
                        break;
                    case "authors":
                        template = new Gson().fromJson(obj.toString(), Template.AuthorTemplate.class);
                        break;

                    case "listed_tiles":
                        template = new Gson().fromJson(obj.toString(), Template.ListItemData.class);
                        break;

                }
                templates.add(template);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return templates;
    }

    private void setAdapter(List<Template> templates) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        int width = deviceScreenDimension.getDisplayWidth();

        //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        //  recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NativePostTemplateAdapter adapter = new NativePostTemplateAdapter(NativePostActivity.this, templates, width);


        recyclerView.setAdapter(adapter);

        adapter.OnProcessAction(new NativePostTemplateAdapter.OnProcessActionInterface() {
            @Override
            public void onProcessActionButton(String action, String meta) {
                processAction(action, meta);
            }


        });

        adapter.OnACBBack(new NativePostTemplateAdapter.OnActionButtonInterface() {
            @Override
            public void onActionButton(NativeButtonObj data) {


                if (data.getAction().equals("conciergeCallBack")) {
                    PrefManager prefManager = new PrefManager(NativePostActivity.this);
                    String moblieNumber = prefManager.getLoginUser().get("mobile");
                    conciergeCallBack(moblieNumber);
                } else {
                    processAction(data.getAction(), data.getMeta());
                }


            }
        });

    }


}

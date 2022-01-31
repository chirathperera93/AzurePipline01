package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.ask.AskActivity;
import com.ayubo.life.ayubolife.book_videocall.BookVideoCallActivity;
import com.ayubo.life.ayubolife.channeling.adapter.DoctorsAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.model.Doctor;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.view.NewSelectDoctorForFavourite;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ImageButton btn_backImgBtn;
    //views
    private ShimmerRecyclerView recycler_doctors;
    private View errorView;
    ImageView img_logo_hemas;
    //    private TextView txtErrorMessage;
    Context con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


    }


    @Override
    protected void onResume() {
        super.onResume();

        Ram.setMycontext(DashboardActivity.this);

        setView();

        if (Constants.type == Constants.Type.AYUBO) {
            setupAyuboMenu();
            // setupLifePlusMenu();
        }
        if (Constants.type == Constants.Type.HEMAS) {
            setupHemasMenu();
        }
        if (Constants.type == Constants.Type.SHESHELLS) {
            setupAyuboMenu();
        }
        if (Constants.type == Constants.Type.LIFEPLUS) {
            setupLifePlusMenu();
        } else {
            setupAyuboMenu();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    private void setupLifePlusMenu() {

        View view_video = findViewById(R.id.layout_video_call_button);
        setButton(view_video, getString(R.string.video_call_title), ContextCompat.getDrawable(this, R.drawable.lf_audio_call),
                getString(R.string.video_call_message));
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, BookVideoCallActivity.class));
            }
        });

        View report_review = findViewById(R.id.layout_report_review);
        setButton(report_review, getString(R.string.review_a_report), ContextCompat.getDrawable(this, R.drawable.lf_report_review),
                getString(R.string.revier_message));
        report_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, GetAReviewActivity.class);
                startActivity(intent);

            }
        });


        View ask_button = findViewById(R.id.layout_ask_button);
        setButton(ask_button, getString(R.string.ask_title), ContextCompat.getDrawable(this, R.drawable.lf_ask_question),
                getString(R.string.ask_message));
        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, AskActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setupAyuboMenu() {

        View view_channel = findViewById(R.id.layout_channel_doctor_button);
        setButton(view_channel, getString(R.string.channel_doctor_title), ContextCompat.getDrawable(this, R.drawable.channel_selected),
                getString(R.string.channelling_message));
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, VisitDoctorActivity.class));
            }
        });

        View view_video = findViewById(R.id.layout_video_call_button);
        setButton(view_video, getString(R.string.video_call_title), ContextCompat.getDrawable(this, R.drawable.video_call_selected),
                getString(R.string.video_call_message));
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, BookVideoCallActivity.class));
            }
        });

        View report_review = findViewById(R.id.layout_ask_button);
        setButton(report_review, getString(R.string.ask_title), ContextCompat.getDrawable(this, R.drawable.ask_selected),
                getString(R.string.ask_message));
        report_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, AskActivity.class);
                startActivity(intent);

            }
        });


        View ask_button = findViewById(R.id.layout_report_review);
        setButton(ask_button, getString(R.string.review_a_report), ContextCompat.getDrawable(this, R.drawable.report_review),
                getString(R.string.revier_message));
        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, GetAReviewActivity.class);

                startActivity(intent);
            }
        });


//        View view_booking = findViewById(R.id.layout_report_button);
//        setButton(view_booking, getString(R.string.mybooking_title), ContextCompat.getDrawable(this, R.drawable.review_selected),
//                getString(R.string.booking_message));
//        view_booking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
//
//            }
//        });


    }

    private void setupHemasMenu() {

//        View view_channel_doctor = findViewById(R.id.layout_video_call_button);
//        setButton(view_channel_doctor, getString(R.string.channel_doctor_title), ContextCompat.getDrawable(this, R.drawable.channel_selected),
//                getString(R.string.channelling_message));
//        view_channel_doctor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DashboardActivity.this, VisitDoctorActivity.class));
//            }
//        });
//
//
//        View view_booking = findViewById(R.id.layout_channel_doctor_button);
//        setButton(view_booking, getString(R.string.mybooking_title), ContextCompat.getDrawable(this, R.drawable.review_selected),
//                getString(R.string.booking_message));
//        view_booking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
//
//            }
//        });
//
//
//        View view_video = findViewById(R.id.layout_ask_button);
//        setButton(view_video, getString(R.string.video_call_title), ContextCompat.getDrawable(this, R.drawable.video_call_selected),
//                getString(R.string.video_call_message));
//        view_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DashboardActivity.this, MyDoctor_Activity.class);
//                Ram.setMapSreenshot(null);
//                intent.putExtra("activityName", "myExperts");
//                startActivity(intent);
//
//            }
//        });
//
//
//        View view_ask = findViewById(R.id.layout_report_button);
//        setButton(view_ask, getString(R.string.ask_title), ContextCompat.getDrawable(this, R.drawable.ask_selected),
//                getString(R.string.ask_message));
//        view_ask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            //   Intent intent = new Intent(DashboardActivity.this, VisitDoctorActivity.class);
//                Intent intent = new Intent(DashboardActivity.this, AskQuestion_Activity.class);
//                startActivity(intent);
//
//            }
//        });
//


    }


    private void setView() {

        ImageButton btn_backImgBtn = findViewById(R.id.btn_backImgBtn);
        ImageView img_hemas_logo = findViewById(R.id.img_hemas_logo);

        if (Constants.type == Constants.Type.AYUBO) {
            img_hemas_logo.setVisibility(View.GONE);
        }
        if (Constants.type == Constants.Type.HEMAS) {
            img_hemas_logo.setVisibility(View.VISIBLE);
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            img_hemas_logo.setVisibility(View.GONE);
        }


        LinearLayout btn_backImgBtn_layout = findViewById(R.id.btn_backImgBtn_layout);

        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        View view_ask = findViewById(R.id.layout_search_button);
        EditText editText = view_ask.findViewById(R.id.edt_search_value);
        TextView txt_activity_heading = view_ask.findViewById(R.id.txt_activity_heading);
        txt_activity_heading.setText("Favourite");
        editText.setHint("Search any doctor");

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);

                DocSearchParameters params;
                params = new DocSearchParameters();
                PrefManager pref = new PrefManager(DashboardActivity.this);
                String user_id = pref.getLoginUser().get("uid");
                params.setUser_id(user_id);

                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new NewSelectDoctorForFavourite(params));
                startActivity(intent);
            }
        });


        recycler_doctors = findViewById(R.id.recycler_doctors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_doctors.setLayoutManager(linearLayoutManager);
        recycler_doctors.showShimmerAdapter();


        errorView = findViewById(R.id.layout_error);
//        txtErrorMessage = errorView.findViewById(R.id.txt_message_error);
        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavorites();
            }
        });

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        getFavorites();
    }

    void setTopView() {

        View layout_back_to_home = findViewById(R.id.layout_back_to_home);
        ImageButton btn_backImgBtn = layout_back_to_home.findViewById(R.id.btn_backImgBtn);
        LinearLayout btn_backImgBtn_layout = layout_back_to_home.findViewById(R.id.btn_backImgBtn_layout);

        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        View view_ask = findViewById(R.id.layout_search_button);
        EditText editText = view_ask.findViewById(R.id.edt_search_value);
        TextView txt_activity_heading = view_ask.findViewById(R.id.txt_activity_heading);
        txt_activity_heading.setText("Favourite");
        editText.setHint("Search doctor");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);

                DocSearchParameters params;
                params = new DocSearchParameters();
                PrefManager pref = new PrefManager(DashboardActivity.this);
                String user_id = pref.getLoginUser().get("uid");
                params.setUser_id(user_id);

                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new NewSelectDoctorForFavourite(params));
                startActivity(intent);
            }
        });


    }


    private void getFavorites() {

        errorView.setVisibility(View.GONE);
        recycler_doctors.showShimmerAdapter();

        setOfflineData();

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_FAVORITES, new SoapBasicParams().getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        recycler_doctors.hideShimmerAdapter();
                        setDoctorsAdapter(readFavoriteDoctors(response));
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        recycler_doctors.hideShimmerAdapter();
                    }
                }).execute();
    }

    private void setOfflineData() {
        Object object = AppHandler.readOfflineData(this, AppConfig.OFFLINE_FILE_FAVOURITE);
        if (object != null) {
            List<Doctor> doctors = (List<Doctor>) object;
            recycler_doctors.hideShimmerAdapter();
            setDoctorsAdapter(doctors);
        }
    }

    private List<Doctor> readFavoriteDoctors(String response) {
        List<Doctor> dataArray = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            Type messageType = new TypeToken<List<Doctor>>() {
            }.getType();

            dataArray = new Gson().fromJson(jsonObject.getJSONArray("data")
                    .toString(), messageType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (dataArray == null)
            dataArray = new ArrayList<>();
        dataArray.add(new Doctor());

        AppHandler.writeOfflineData(this, dataArray, AppConfig.OFFLINE_FILE_FAVOURITE);

        return dataArray;
    }

    private void setDoctorsAdapter(List<Doctor> doctors) {
        DoctorsAdapter adapter = new DoctorsAdapter(this, null, new ArrayList<Object>(doctors));
        adapter.setOnDoctorClickListener(new DoctorsAdapter.OnDoctorClickListener() {
            @Override
            public void onDoctorClick(Object object) {
                Doctor doctor = (Doctor) object;

                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);

                DocSearchParameters params;
                params = new DocSearchParameters();
                PrefManager pref = new PrefManager(DashboardActivity.this);
                String user_id = pref.getLoginUser().get("uid");
                params.setUser_id(user_id);

                if (doctor.getId() != 0) {
                    params.setDate("");
                    params.setSpecializationId(doctor.getSpecialization_id());
                    params.setDoctorId(String.valueOf(doctor.getId()));
                    params.setLocationId(doctor.getHospital_id());
                    intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(params));
                } else {
                    intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new NewSelectDoctorForFavourite(params));
                }

                startActivity(intent);
            }
        });
        recycler_doctors.setAdapter(adapter);
    }

    private void setButton(View view, String name, Drawable drawable, String message) {
        ((ImageView) view.findViewById(R.id.img_icon_button)).setImageDrawable(drawable);
        ((TextView) view.findViewById(R.id.txt_name_button)).setText(name);
        ((TextView) view.findViewById(R.id.txt_desc_button)).setText(message);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = AppHandler.getDeviceHeight(this) / 9;
        view.setLayoutParams(params);
    }
}

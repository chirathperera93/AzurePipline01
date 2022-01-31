package com.ayubo.life.ayubolife.map_challenges.activity;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.map_challenges.adapter.LeaderBoardAdapter;
import com.ayubo.life.ayubolife.map_challenges.model.NewLeaderboard;
import com.ayubo.life.ayubolife.map_challenges.model.LeaderboardMainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderBoardActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_SEARCH_OBJECT = "search_object";


    //instances
    private List<Object> source;
    Gson gson;
    PrefManager pref;


    //views
    private ProgressDialog mProgressDialog;
    private View errorView;
    private TextView txt_last_updated_date, txtNoResult, txtErrorMessage, txt_team_name;
    RecyclerView recyclerView_Leaderboard;
    String type;
    String relatedID;
    String challangeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        //initUiView();

        initData();

        readExtras();

    }


    public void getLeaderboard() {

        String jsonStr =
                "{" +
                        "\"challenge_id\": \"" + challangeID + "\"," +
                        "\"type\": \"" + type + "\"" +
                        "\"related_id\": \"" + relatedID + "\"" +
                        "}";

        mProgressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LeaderboardMainResponse> call = apiService.goalLeaderboard(AppConfig.APP_BRANDING_ID, "challenge_native_leaderboard", "JSON", "JSON", jsonStr);

        call.enqueue(new Callback<LeaderboardMainResponse>() {
            @Override
            public void onResponse(Call<LeaderboardMainResponse> call, Response<LeaderboardMainResponse> response) {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }

                if (response.isSuccessful()) {


                    Gson gson1 = new Gson();

                    LeaderboardMainResponse dataObj = (LeaderboardMainResponse) response.body();
                    List<NewLeaderboard> dataList = dataObj.getLeaderboard();

                    txt_last_updated_date.setText(dataObj.getLastUpdated());

                    if ((dataObj.getTitle() != null) && (dataObj.getTitle().length() > 0)) {
                        txt_team_name.setText(dataObj.getTitle());
                        txt_team_name.setVisibility(View.VISIBLE);
                    } else {
                        txt_team_name.setVisibility(View.GONE);
                    }


                    loadView(dataList);


                }
            }

            @Override
            public void onFailure(Call<LeaderboardMainResponse> call, Throwable t) {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }
                //  Toast.makeText(ReportDetailsActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }


    private void readExtras() {
        Bundle extras = getIntent().getExtras();
        type = getIntent().getStringExtra("type");
        relatedID = getIntent().getStringExtra("relatedID");
        challangeID = getIntent().getStringExtra("challangeID");

        getLeaderboard();
    }

    void loadView(List<NewLeaderboard> dataList) {

        LeaderBoardAdapter adapter = new LeaderBoardAdapter(this, dataList);
        recyclerView_Leaderboard.setAdapter(adapter);

    }

//    void initUiView() {
//
//        recyclerView_Leaderboard= (RecyclerView) findViewById(R.id.recyclerView_Leaderboard);
//        txt_last_updated_date=  findViewById(R.id.txt_last_updated_date);
//        txt_team_name=  findViewById(R.id.txt_team_name);
//
//        recyclerView_Leaderboard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        txtNoResult=findViewById(R.id.txtNoResult);
//        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
//        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
//        btn_back_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }


    void initData() {

        pref = new PrefManager(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        gson = new Gson();

    }

}

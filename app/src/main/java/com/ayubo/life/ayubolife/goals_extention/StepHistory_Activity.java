package com.ayubo.life.ayubolife.goals_extention;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.goals_extention.models.Data_StepHistory;
import com.ayubo.life.ayubolife.goals_extention.models.MainResponse_History;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse;
import com.ayubo.life.ayubolife.pojo.reports.Data;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepHistory_Activity extends AppCompatActivity {
    ListView list;
    List<Data> data;
    CustomAdapter adapter;
    ImageButton btn_backImgBtn;
    boolean isNeedCallService = false;
    String userid_ExistingUser;
    PrefManager pref;
    Gson gson = null;
    String products_list = null;
    private static final String TAG = StepHistory_Activity.class.getSimpleName();
    SimpleDateFormat formatWith_yyyy_MM_dd;

    LinearLayout lay_btnBack;

    String appToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_history);

        formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

        localConstructor();

        initView();

        initEvent();

        pref = new PrefManager(StepHistory_Activity.this);
        appToken = pref.getUserToken();

        callWellnessDashboard(appToken);

        // updateView(loadDataForView("step_history_data"));


        if (isNeedCallService) {
            //   serviceCallFor_GettingLatestData("viewGoals");
        }

        //  initData();

    }


    private void initEvent() {
        lay_btnBack.setOnClickListener(new View.OnClickListener() {
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


    }

    private void initView() {
        lay_btnBack = (LinearLayout) findViewById(R.id.lay_btnBack);
        list = (ListView) findViewById(R.id.list_steps_history);
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // don't send events once the activity is destroyed
        //  disposable.dispose();
    }


    private void localConstructor() {
        data = new ArrayList<Data>();
        pref = new PrefManager(StepHistory_Activity.this);
        gson = new Gson();
        data.add(new Data("id", "2018/08/22", "3500", 3, "link", "icon", true));
    }

    private ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> loadDataForView(String viewName) {
        ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> data = null;

        DBString dataObj = DBRequest.getCashDataByType(StepHistory_Activity.this, viewName);

        if (dataObj == null) {
            isNeedCallService = true;
        } else {
            long minits = Util.getTimestampAsMinits(dataObj.getName());
            if (minits > 5) {
                isNeedCallService = true;
            }

            products_list = dataObj.getId();
            if ((products_list != null) && (products_list.length() > 5)) {

                Type type = new TypeToken<MainResponse>() {
                }.getType();

                com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse resp = gson.fromJson(products_list, type);

                // updateView();
            }
        }

        return data;
    }

    void updateView(List<Data_StepHistory> dataObj) {

        adapter = new CustomAdapter(dataObj, StepHistory_Activity.this);
        list.setAdapter(adapter);

    }

    private void callWellnessDashboard(String token) {

        ApiInterface apiService = ApiClient.getGoalExApiClientNew().create(ApiInterface.class);

        Call<MainResponse_History> call = apiService.getDashboardStepHistory(AppConfig.APP_BRANDING_ID, token);
        call.enqueue(new Callback<MainResponse_History>() {
            @Override
            public void onResponse(Call<MainResponse_History> call, Response<MainResponse_History> response) {

                if (response.isSuccessful()) {
                    if (response.body().getResult() == 0) {
                        List<Data_StepHistory> dataObj = response.body().getData();
                        updateView(dataObj);
                        //  System.out.println("===========response===Success============"+name);
                    } else {
                        System.out.println("==============Fail============");
                    }


                } else {
                    System.out.println("===========response===Fail============");
                }
            }

            @Override
            public void onFailure(Call<MainResponse_History> call, Throwable t) {
                System.out.println("============Fail============" + t);
                //    Toast.makeText(DashBoard_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

            }
        });
    }


    public void serviceCallFor_GettingLatestData(String serviceName) {

        userid_ExistingUser = pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResponse> call = apiService.viewGoals(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> call, Response<MainResponse> response) {
                //  MainResponse_History

                if (response.isSuccessful()) {

                    Gson gson1 = new Gson();
                    String jsonCars = gson1.toJson(response.body());

                    DBRequest.updateDoctorData(StepHistory_Activity.this, jsonCars, "step_history_data");

                    if ((data != null) && (data.size() > 0)) {
                        data.clear();
                    }
                    //  data = (ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum>) response.body().getData();
                    // com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum ddd=new com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum();

                    //  updateView(data);

                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> call, Throwable t) {

                Toast.makeText(StepHistory_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }


    public class CustomAdapter extends ArrayAdapter<Data_StepHistory> {

        private List<Data_StepHistory> dataSet;
        Context mContext;

        private class ViewHolder {
            LinearLayout bottom_line_seperator;
            TextView txt_date, txt_steps, txt_mets, txt_distance, txt_cals;

        }

        public CustomAdapter(List<Data_StepHistory> moviesList, Context context) {
            super(context, R.layout.stephistory_listview_raw, moviesList);
            this.dataSet = moviesList;
            this.mContext = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            Data_StepHistory obj = dataSet.get(position);
            if (rowView == null) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                rowView = inflater.inflate(R.layout.stephistory_listview_raw, null);

                // configure view holder
                CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder();

                viewHolder.txt_date = (TextView) rowView.findViewById(R.id.txt_date);
                viewHolder.txt_steps = (TextView) rowView.findViewById(R.id.txt_steps);
                viewHolder.txt_mets = (TextView) rowView.findViewById(R.id.txt_mets);
                viewHolder.txt_distance = (TextView) rowView.findViewById(R.id.txt_distance);
                viewHolder.txt_cals = (TextView) rowView.findViewById(R.id.txt_cals);

                rowView.setTag(viewHolder);
            }
            // fill data
            CustomAdapter.ViewHolder holder = (CustomAdapter.ViewHolder) rowView.getTag();


            if (obj != null) {

                Date date1 = null;
                String strDate = null;
                try {
                    strDate = obj.getDate();
                    //    strDate=  strDate.substring(0, strDate.length() - 8);
                    // strDate = formatWith_yyyy_MM_dd.format(strDate);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.txt_date.setText(strDate);

                String steps_with_comma = NumberFormat.getIntegerInstance().format(Integer.parseInt(obj.getSteps()));

                holder.txt_steps.setText(steps_with_comma);
                holder.txt_mets.setText(obj.getMets());
                holder.txt_distance.setText(obj.getDistance() + " Km");
                holder.txt_cals.setText(obj.getCalories());
            }
            return rowView;
        }

    }


}

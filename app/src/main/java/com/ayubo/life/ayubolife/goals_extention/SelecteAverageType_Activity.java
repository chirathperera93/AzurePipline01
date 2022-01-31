package com.ayubo.life.ayubolife.goals_extention;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals_extention.models.dash_category.CategoryListMainResponse;
import com.ayubo.life.ayubolife.goals_extention.models.dash_category.Datum;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class SelecteAverageType_Activity extends AppCompatActivity {
    ArrayList<Datum>  dataListObj;
    ListView list;
    List<Datum> data;
    CustomListAdapterLite adapter;
    ImageButton btn_backImgBtn;
    boolean isNeedCallService = false;
    String userid_ExistingUser;
    PrefManager pref;
    Gson gson = null;
    String products_list = null;

    private static final String TAG = SelecteAverageType_Activity.class.getSimpleName();
    String from,meta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecte_average_type_);


        meta = getIntent().getStringExtra("meta");
        from = getIntent().getStringExtra("from");

        localConstructor();

        initView();

        initEvent();




        callWellnessDashboard();

        if (isNeedCallService) {
            //   serviceCallFor_GettingLatestData("viewGoals");
        }
    }

    public void callWellnessDashboard() {

       // MyProgressLoading.showLoading(getApplicationContext(),"Please wait...");

        pref=new PrefManager(SelecteAverageType_Activity.this);
        String token= pref.getUserToken();

        userid_ExistingUser=pref.getLoginUser().get("uid");

        ApiInterface apiService = ApiClient.getGoalExApiClient().create(ApiInterface.class);

        Call<CategoryListMainResponse> call = apiService.getDashboardCategorys(AppConfig.APP_BRANDING_ID,token);
        call.enqueue(new Callback<CategoryListMainResponse>() {
            @Override
            public void onResponse(Call<CategoryListMainResponse> call, Response<CategoryListMainResponse> response) {
             //   MyProgressLoading.dismissDialog();
                if(response.isSuccessful()) {
                    if(response.body().getResult()==0){

                        dataListObj= (ArrayList<Datum>) response.body().getData();
                        if(dataListObj!=null){
                            updateView(dataListObj);
                        }

                    }else{
                        Toast.makeText(SelecteAverageType_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

                    }




                }else{
                    Toast.makeText(SelecteAverageType_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<CategoryListMainResponse> call, Throwable t) {
              //  MyProgressLoading.dismissDialog();
                Toast.makeText(SelecteAverageType_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

            }
        });
    }


    private void localConstructor() {
        data = new ArrayList<Datum>();
        pref = new PrefManager(SelecteAverageType_Activity.this);
        gson = new Gson();

    }

    private void initEvent() {

        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SelecteAverageType_Activity.this, DashBoard_Activity.class);
                startActivity(i);
                finish();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

               Datum obj=   dataListObj.get(position);
               String catogoryId= obj.getId();

                pref.setGoalCategory(catogoryId);
                pref.setGoalCategoryName(obj.getName());

               if(from.equals("details_view")){
                   Intent intent = new Intent(SelecteAverageType_Activity.this, DashboardViewActivity.class);
                   startActivity(intent);
                   finish();
               }
               else if(from.equals("summery_view")){
                    Intent intent = new Intent(SelecteAverageType_Activity.this, DashBoard_Activity.class);
                    intent.putExtra("meta", meta);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initView() {

        list = (ListView) findViewById(R.id.list_steps_history);
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);

    }

    void updateView(ArrayList<Datum> dat) {

        adapter=new CustomListAdapterLite(SelecteAverageType_Activity.this,dat);
        list.setAdapter(adapter);
    }

    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<Datum> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<Datum> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.select_stepavg_type_listview_raw, null);

            TextView title = (TextView)vi.findViewById(R.id.txt_name); // title
            ImageView ssshare_com_list_image = (ImageView)vi.findViewById(R.id.ssshare_com_list_image); // title

            Datum obj = data.get(position);
            System.out.println("=========obj.getName()========"+obj.getName());
            System.out.println("==========="+obj.getImageUrl());

            if(obj.getImageUrl()!=null){
                String imageURL=obj.getImageUrl();
                imageURL=imageURL.replace("zoom_level","xxxhdpi");

                RequestOptions requestOptions1 = new RequestOptions()
                        .transform(new CircleTransform(SelecteAverageType_Activity.this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(SelecteAverageType_Activity.this).load(imageURL)
                        .transition(withCrossFade())
                        .apply(requestOptions1)
                        .into(ssshare_com_list_image);
            }


            title.setText(obj.getName());




            return vi;
        }
    }



}
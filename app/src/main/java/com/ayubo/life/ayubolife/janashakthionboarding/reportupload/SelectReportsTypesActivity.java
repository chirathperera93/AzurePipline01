package com.ayubo.life.ayubolife.janashakthionboarding.reportupload;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.DoctorObj;
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Report;
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.ReportTypesMainResponse;
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse;
import com.ayubo.life.ayubolife.quick_links.experts.ExpertAdapter;
import com.ayubo.life.ayubolife.quick_links.experts.Quick_Utility;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.reports.model.FamilyMemberData;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Ram;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SelectReportsTypesActivity extends AppCompatActivity {

    EditText editText;
    ArrayList<Report> songsList;
    String activityName;
    String serviceName,activityHeading;
    View view;
    private RecyclerView recyclerView;
    ReportTypeAdapter adapter;


    PrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reports_types);

        SelectReportsTypesActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        pref = new PrefManager(this);
        songsList = new ArrayList<>();
        ArrayList<Object> songsLis= Ram.getDoctorDataList();

        recyclerView=findViewById(R.id.list_expert_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        editText= findViewById(R.id.edt_search_value_new);
        recyclerView.setLayoutManager(linearLayoutManager);


        editText.setHint("Search here");

        songsList=new ArrayList<Report>();

//        if((Ram.getMedicalReportList()!=null && Ram.getMedicalReportList().size()>0)){
//            adapter = new ReportTypeAdapter(SelectReportsTypesActivity.this, songsList);
//            Ram.setMedicalReportList(songsList);
//            recyclerView.setAdapter(adapter);
//        }else{
//            callViewGoals();
//        }

        callViewGoals();

    }

    public void callViewGoals() {
       String userid_ExistingUser=pref.getLoginUser().get("uid");
        String jsonStr = "{" +
                "\"userid\": \"" + userid_ExistingUser + "\"" +
                "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReportTypesMainResponse> call = apiService.getReportMaster(AppConfig.APP_BRANDING_ID,"get_report_master", "JSON","JSON",jsonStr);
        call.enqueue(new Callback<ReportTypesMainResponse>() {
            @Override
            public void onResponse(Call<ReportTypesMainResponse> call, Response<ReportTypesMainResponse> response) {

                if(response.isSuccessful()) {

                    songsList= (ArrayList<Report>) response.body().getReports();

                    adapter = new ReportTypeAdapter(SelectReportsTypesActivity.this, songsList);
                    Ram.setMedicalReportList(songsList);
                    recyclerView.setAdapter(adapter);
                    System.out.println("=============="+response.toString());
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            // filterData(charSequence.toString());

                            adapter.getFilter().filter(charSequence.toString());

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ReportTypesMainResponse> call, Throwable t) {


                System.out.println("========t======"+t);
            }
        });
    }

    public class ReportTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        //instances
        // Declare Variables
        private ArrayList<Report> originalDatalist = null;
        private ArrayList<Report> contactListFiltered = null;
        private Activity activity;
        private FamilyMemberAdapter.OnItemClickListener listener;
        private PrefManager pref;


        private FamilyMemberAdapter.OnClickAssignUserImage listener_delete;



        public void setOnSelectAssignUserListener(FamilyMemberAdapter.OnClickAssignUserImage listener) {
            this.listener_delete = listener;
        }
        //    Interface For Events Handing ...........



//        interface OnClickAssignUserImage {
//            void onSelectAssignUser(FamilyMemberData obj, int pos);
//        }

        public ReportTypeAdapter(Activity activity, ArrayList<Report> worldpopulationlist) {
            this.originalDatalist = worldpopulationlist;
            this.activity = activity;
            pref=new PrefManager(activity);

            this.contactListFiltered = originalDatalist;
        }

        class TitleViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle;

            TitleViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.txt_history_title_row_heading);
            }
        }
        class ExpertViewHolder extends RecyclerView.ViewHolder {


            TextView txtName;



            ExpertViewHolder(View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txt_reportName);

            }
        }

        public void setOnItemClickListener(FamilyMemberAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

                    View viewItem = inflater.inflate(R.layout.raw_report_type, parent, false);
                    viewHolder = new ReportTypeAdapter.ExpertViewHolder(viewItem);

            return viewHolder;



        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
            if (holder instanceof ReportTypeAdapter.ExpertViewHolder) {
                ReportTypeAdapter.ExpertViewHolder imgViewHolder = (ReportTypeAdapter.ExpertViewHolder) holder;
                Report expert = (Report) contactListFiltered.get(position);

                holder.itemView.setTag(expert);
                imgViewHolder.txtName.setText(expert.getName());
              //  imgViewHolder.txt_category.setText(expert.getSpec());

            }


            final int pos = holder.getAdapterPosition();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Report expert = (Report) view.getTag();
                 //   expert.getId();
                    Ram.setMedicalReportObject(expert);
                    finish();
                }
            });

        }
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = originalDatalist;
                    } else {
                        ArrayList<Report> filteredList = new ArrayList<>();
                        for (Report row : originalDatalist) {
                            if(row!=null) {

                                if (row.getName()!=null) {
                                    String objectText;
                                    objectText = row.getName().toLowerCase();

                                    if (objectText.contains(charString.toLowerCase())) {
                                        filteredList.add(row);
                                    }
                                }
                            }
                        }

                        contactListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    ArrayList<Report> filteredDataList = (ArrayList<Report>) filterResults.values;
                   // filteredDataList=filterResults;
                    contactListFiltered= (ArrayList<Report>) filteredDataList;

                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getItemCount() {
            if (contactListFiltered == null)
                return 0;

            else
                return contactListFiltered.size();
        }
        @Override
        public int getItemViewType(int position) {


            return super.getItemViewType(position);
        }

        //    @Override
//    public int getItemViewType(int position) {
//        if (contactListFiltered == null)
//            return VIEW_TYPE_FAMILY_MEMBER;
//        else
//            return VIEW_TYPE_FAMILY_MEMBER;
//    }
//        public interface OnItemClickListener {
//            void onItemClick(Object object);
//        }
//
//
//
//        public interface OnDoctorClickListener {
//            void onDoctorClick(Object object);
//
////        void onNewExpertFocused(Expert expert);
//        }


    }

}

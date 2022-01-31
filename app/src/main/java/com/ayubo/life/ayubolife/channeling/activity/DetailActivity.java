package com.ayubo.life.ayubolife.channeling.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.adapter.DetailsViewAdapter;
import com.ayubo.life.ayubolife.channeling.adapter.SearchAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.DetailRow;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL_ACTION_OBJECT = "detail_action_object";

    //instances
    private DetailAction detailAction;
    private List<Object> objects;
    private DetailsViewAdapter adapter;
    private boolean isResponseSuccess;

    //views
    private TextView txtPrice;
    private ProgressBar progressBar;
    TextView txtConfirm;
    TextView txtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        readExtras();
        initView();
        getData();



    }

    private void readExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(EXTRA_DETAIL_ACTION_OBJECT)) {
            detailAction = (DetailAction) bundle.getSerializable(EXTRA_DETAIL_ACTION_OBJECT);
        }
    }

    private void initView() {

        isResponseSuccess=false;
        //  ImageView img_doc_doctor_info = findViewById(R.id.img_doc_doctor_info);

        //  TextView txtDocName = findViewById(R.id.txt_doctor_name_doctor_info);
        // TextView txtSpecialty = findViewById(R.id.txt_specialty_doctor_info);
        txtNote = findViewById(R.id.txt_detail_note);
        txtPrice = findViewById(R.id.txt_price_details);
        txtConfirm = findViewById(R.id.txt_confirm_details);
        //  findViewById(R.id.txt_note_doctor_info).setVisibility(View.GONE);
        progressBar = findViewById(R.id.prg_details);



        if (detailAction != null) {

//            Glide.with(this).load(Ram.getDoctorImage()).into(img_doc_doctor_info);
//            txtDocName.setText(detailAction.getDocName());
//            txtSpecialty.setText(detailAction.getSpecialisation());

            String sNote=detailAction.getDoctorNote();

            if((sNote!=null) && (sNote.length()>3)){
                sNote=sNote.toLowerCase();
                sNote = sNote.substring(0, 1).toUpperCase() + sNote.substring(1);
            }else{
                sNote="";
            }


            // txtNote.setText(sNote);


            txtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isResponseSuccess) {
                        detailAction.onFinish(DetailActivity.this);
                    }
                }
            });

            RecyclerView recycler_search = findViewById(R.id.recycler_session_details);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_search.setLayoutManager(linearLayoutManager);

            DBString data=new DBString(detailAction.getDocName(),detailAction.getSpecialisation());
            adapter = new DetailsViewAdapter(this, getDetailList(), new DetailsSearchActions(),data);
            adapter.setOnItemClickListener(new DetailsViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Object object) {
                }
            });

            adapter.setOnClickFamilyMemberListener(new DetailsViewAdapter.OnClickBack() {
                @Override
                public void onBackClick() {

                    finish();
                }
            });
            recycler_search.setAdapter(adapter);
        }
    }

    private List<Object> getDetailList() {

        if (detailAction != null)
            objects =  detailAction.getPreList();
        else
            objects =  new ArrayList<>();

        return objects;
    }

    private void getData() {
        if (detailAction != null && detailAction.getAddDownloadBuilder() != null) {
            if (detailAction.hasData(this)) {
                removeBooking();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            new DownloadData(this, detailAction.getAddDownloadBuilder()).
                    setOnDownloadListener(SearchActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                        @Override
                        public void onDownloadSuccess(final String response, int what, int code) {
                            progressBar.setVisibility(View.GONE); String message=null;

                            if (detailAction.readData(DetailActivity.this, response)) {
                                isResponseSuccess=true;
                                List<Object> list = detailAction.getPostList();

                                objects.addAll(list);

                                adapter.notifyDataSetChanged();

                                Double totalPrice = detailAction.getTotalPrice();

                                txtPrice.setText(String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, totalPrice));
                            } else {
                                isResponseSuccess=false;
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    message = jsonObj.optString("error").toString();
//                                        JSONObject jsonObj2 = new JSONObject(message);
//                                        String error = jsonObj2.optString("name").toString();
//                                        error = error.substring(1);
//                                        error = error.substring(0, error.length() - 1);
//                                        error = error.replace("\"", "");
                                    txtConfirm.setBackgroundResource(R.color.text_color_contact_sub);
                                    txtPrice.setTextColor(Color.RED);
                                    txtPrice.setText(message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onDownloadFailed(String errorMessage, int what, int code) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                            progressBar.setVisibility(View.GONE);
//                            errorView.setVisibility(View.VISIBLE);
//                            errorView.startAnimation(AnimationUtils.loadAnimation(SearchActivity.this, R.anim.anim_shake));
                        }
                    }).execute();
        }
    }

    private void removeBooking() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, detailAction.getRemoveDownloadBuilder(this)).
                setOnDownloadListener(SearchActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        ((App) getApplication()).setBooking(null);
                        getData();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    class DetailsSearchActions implements SearchActivity.SearchActions {

        @Override
        public String getTitle(Context context) {
            return null;
        }

        @Override
        public boolean isValueConsists(Object object, String value) {
            return true;
        }

        @Override
        public boolean onFinish(Activity activity, Object object) {
            return false;
        }

        @Override
        public List<Object> readObject(JSONObject jsonObject) {
            return null;
        }

        @Override
        public String getName(Object object) {
            return ((DetailRow) object).getTitle();
        }

        @Override
        public String getValue(Object object) {
            return ((DetailRow) object).getValue();
        }

        @Override
        public String getImageUrl(Object object) {
            return String.valueOf(((DetailRow) object).getIcon());
        }

        @Override
        public DownloadDataBuilder getDownloadBuilder() {
            return null;
        }

        @Override
        public int getViewType() {
            return SearchAdapter.SUMMARY_TYPE;
        }

        @Override
        public String getOfflineFileName() {
            return "";
        }
    }

    public interface DetailAction {

        String getDocName();

        String getSpecialisation();

        Double getTotalPrice();

        DownloadDataBuilder getAddDownloadBuilder();

        DownloadDataBuilder getRemoveDownloadBuilder(Activity activity);

        boolean readData(Activity activity, String response);

        void onFinish(Activity activity);

        String getDoctorNote();

//        String getActivity(Activity activity);

        boolean hasData(Activity activity);

        List<Object> getPreList();

        List<Object> getPostList();
    }
}

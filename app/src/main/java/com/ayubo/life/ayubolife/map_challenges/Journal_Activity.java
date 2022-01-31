package com.ayubo.life.ayubolife.map_challenges;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.TemplateViewActivity;
import com.ayubo.life.ayubolife.body.ShareEntity;
import com.ayubo.life.ayubolife.home.ExpandableListAdapter;
import com.ayubo.life.ayubolife.model.ImageListObj;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Journal_Activity extends AppCompatActivity {
    RadioButton btn_appointment_left, btn_appointment_right;
    CustomListAdapter adapter;
    ExpandableListAdapter listAdapter;
    ListView list;
    List<ImageListObj> listData;
    HashMap<String, List<ImageListObj>> listDataChild;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    String statusFromServiceAPI_db;
    ArrayList<ShareEntity> data;
    int sectionCount = 0;
    ArrayList<RoadLocationObj> myCityList;
    ArrayList<RoadLocationObj> localmyTagList;
    boolean gyroExists;
    ProgressDialog prgDialog;
    String userid_ExistingUser, setDeviceID_Success, today, serviceDataStatus, challenge_id, steps, tableTopic;
    int total_steps;
    ArrayList<String> activeArray = null;


    String service_checkpoints, enabled_checkpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_);


        listData = new ArrayList<ImageListObj>();
        listDataChild = new HashMap<String, List<ImageListObj>>();
        list = (ListView) findViewById(R.id.jurnal_lv);
        myCityList = new ArrayList<RoadLocationObj>();
        localmyTagList = new ArrayList<RoadLocationObj>();
        activeArray = new ArrayList<String>();

        prgDialog = new ProgressDialog(Journal_Activity.this);

        pref = new PrefManager(this);

        PackageManager packageManager = getPackageManager();
        gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

        try {

            userid_ExistingUser = pref.getLoginUser().get("uid");
            challenge_id = getIntent().getStringExtra("challenge_id");
            steps = getIntent().getStringExtra("total_steps");

            service_checkpoints = getIntent().getStringExtra("service_checkpoints");


            if (service_checkpoints.equals("true")) {
                enabled_checkpoints = getIntent().getStringExtra("enabled_checkpoints");
                activeArray = Utility.getActiveArray(enabled_checkpoints);
                System.out.println("=========activeArray===============" + activeArray.toString());
            }

            total_steps = Integer.parseInt(steps);

            Intent intent = getIntent();
            MapChallengeActivity.Book book = intent.getParcelableExtra("Book");
            localmyTagList = book.myTagList;

        } catch (Exception e) {
            e.printStackTrace();

        }


        try {
            for (int i = 0; i < localmyTagList.size(); i++) {

                RoadLocationObj jObj = localmyTagList.get(i);
                String wc = jObj.getWc();
                if (wc.length() > 10) {
                    tableTopic = wc;
                }
                myCityList.add(localmyTagList.get(i));
            }

            if (myCityList.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Journal_Activity.this);
                LayoutInflater inflat = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflat.inflate(R.layout.alert_no_journaldata_layout, null, false);
                builder.setView(layoutView);

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {

                adapter = new CustomListAdapter(Journal_Activity.this, myCityList);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {

                            RoadLocationObj obj = localmyTagList.get(position);

                            String st = obj.getSteps().toString();

                            int istp = Integer.parseInt(st);
                            String URLs = obj.getMeta().toString();

                            if (isContain(activeArray, Integer.toString(position))) {
                                if (URLs.contains("panaroma")) {
                                    if (gyroExists) {
                                        Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                        intent.putExtra("URL", URLs);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                        intent.putExtra("URL", URLs);
                                        startActivity(intent);
                                    }
                                } else if (URLs.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URLs);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URLs);
                                    startActivity(intent);
                                }
                            }


                            if (position == 1) {
                                String URL = obj.getMeta().toString();
                                System.out.println("====Reached===========" + URL);
                                if (URL.length() > 5) {
                                    if (URL.contains("panaroma")) {
                                        if (gyroExists) {
                                            Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                            intent.putExtra("URL", URL);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                            intent.putExtra("URL", URL);
                                            startActivity(intent);
                                        }
                                    } else if (URL.contains("nativeview")) {
                                        Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }

                                }
                            } else if (service_checkpoints.equals("true")) {

                                if (isContain(activeArray, Integer.toString(position))) {
                                    String URL = obj.getMeta().toString();
                                    if (URL.contains("panaroma")) {
                                        if (gyroExists) {
                                            Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                            intent.putExtra("URL", URL);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                            intent.putExtra("URL", URL);
                                            startActivity(intent);
                                        }
                                    } else if (URL.contains("nativeview")) {
                                        Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                }
                            } else if (total_steps > istp) {
                                String URL = obj.getMeta().toString();
                                System.out.println("====Reached===========" + URL);
                                if (URL.length() > 5) {
                                    if (URL.contains("panaroma")) {
                                        if (gyroExists) {
                                            Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                            intent.putExtra("URL", URL);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                            intent.putExtra("URL", URL);
                                            startActivity(intent);
                                        }
                                    } else if (URL.contains("nativeview")) {
                                        Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                System.out.println("====Not Reached===========");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }


                });

            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        btn_appointment_left = findViewById(R.id.btn_appointment_left);
        btn_appointment_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    class CustomListAdapter extends BaseAdapter {
        String myStrings[];
        ImageLoader imageLoader;
        private Context activity;
        private ArrayList<RoadLocationObj> data;
        private LayoutInflater inflater = null;
        // public ImageLoader imageLoader;

        public CustomListAdapter(Context a, ArrayList<RoadLocationObj> d) {
            activity = a;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
            progressBar.setVisibility(View.VISIBLE);
            if (imageUrl != null) {
                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageBitmap(response.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	Log.e("onErrorResponse ", error.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;


            RoadLocationObj obj = null;
            try {

                obj = data.get(position);

                if (convertView == null)
                    vi = inflater.inflate(R.layout.raw_challenge_journal, null);

                LayoutInflater inflater = LayoutInflater.from(Journal_Activity.this);
                // convertView = inflater.inflate(R.layout.raw_challenge_journal, parent, false);

//                TextView text_description = (TextView) vi.findViewById(R.id.text_description);
                TextView child_location_desc = (TextView) vi.findViewById(R.id.child_location_desc);
//                TextView header_title = (TextView) vi.findViewById(R.id.header_title);
//                ImageView header_icon = (ImageView) vi.findViewById(R.id.header_icon);
                ImageView img_cild_image = (ImageView) vi.findViewById(R.id.child_location_img);
                // LinearLayout cel_bg = (LinearLayout) vi.findViewById(R.id.lay_shadow);
                ProgressBar progressNewsList = (ProgressBar) vi.findViewById(R.id.progressNewsList);


                //========================================
                //========================================
                //=============== NEW VIEW=======================
                if (service_checkpoints.equals("true")) {
                    if (position == 0) {

//                        header_title.setText("Where it all begins!!");
//                        header_title.setTag(Integer.toString(position));
//                        header_title.setVisibility(View.VISIBLE);
                    }
//                    if (position == 0) {
//                        text_description.setVisibility(View.VISIBLE);
//                        img_cild_image.setVisibility(View.GONE);
//                        cel_bg.setVisibility(View.GONE);
//                        progressNewsList.setVisibility(View.GONE);
//
//                        Bitmap bitm = BitmapFactory.decodeResource(getResources(), R.drawable.ayubo);
//                        header_icon.setImageBitmap(bitm);
//                        text_description.setText(tableTopic);
//                        text_description.setMaxLines(3);
//
//                        header_title.setText("Where it all begins!!");
//                        header_title.setTag(Integer.toString(position));
//                    } else {

//                        text_description.setVisibility(View.GONE);

                    //      img_cild_image.setVisibility(View.VISIBLE);
                    //   cel_bg.setVisibility(View.VISIBLE);
                    progressNewsList.setVisibility(View.VISIBLE);

//                        header_title.setText(obj.getCity());
//                        header_title.setTag(Integer.toString(position));
                    String disableImageURl = null;

                    // child_location_desc.setOnClickListener(this);
                    if (imageLoader == null)
                        imageLoader = App.getInstance().getImageLoader();

                    String imageURl = obj.getCityimg().toString();
                    System.out.println("============imageURl======1========" + imageURl);

                    int currentSteps = Integer.parseInt(obj.getSteps());

                    System.out.println("=====position============" + position);
                    System.out.println("=================" + activeArray.toString());

                    if (isContain(activeArray, Integer.toString(position))) {
                        System.out.println("====Contains============");
                        try {
//                                img_cild_image.setImageUrl(imageURl, imageLoader);
//                                loadImage(imageURl, img_cild_image, progressNewsList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        child_location_desc.setText(obj.getCitymsg().toString());
                        child_location_desc.setMaxLines(4);

                        Bitmap bitm = BitmapFactory.decodeResource(getResources(), R.drawable.landscape_blue_icon);
//                            header_icon.setImageBitmap(bitm);
                    } else {
                        if (imageURl.equals("0")) {
                        } else if (imageURl.length() < 10) {
                        } else {
                            int fulllen = imageURl.length();
                            fulllen = fulllen - 10;
                            String first = imageURl.substring(0, fulllen);
                            disableImageURl = first + "banner_disable.jpg";
                            System.out.println(first);
                        }
                        System.out.println("============imageURl====0==========" + imageURl);
                        try {
//                                img_cild_image.setImageUrl(disableImageURl, imageLoader);
//                                loadImage(disableImageURl, img_cild_image, progressNewsList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        child_location_desc.setText("");
                        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.landscape_grey_icon);
//                            header_icon.setImageBitmap(bitmap5);

                    }

                    child_location_desc.setTag(Integer.toString(position));
                    //  img_cild_image.setTag(Integer.toString(position));

                    //=================================
                    //======END OF NEW VIEW===========================
                    // }
                } else {
                    //=============== 0LD VIEW=======================
//                if (position == 0) {
//                    text_description.setVisibility(View.VISIBLE);
//
//                    img_cild_image.setVisibility(View.GONE);
//                    cel_bg.setVisibility(View.GONE);
//                    progressNewsList.setVisibility(View.GONE);
//
//                    Bitmap bitm = BitmapFactory.decodeResource(getResources(), R.drawable.ayubo);
//                    header_icon.setImageBitmap(bitm);
//                    text_description.setText(tableTopic);
//                    text_description.setMaxLines(3);
//
//                    header_title.setText("Where it all begins!!");
//                    header_title.setTag(Integer.toString(position));
//                } else {
                    if (position == 0) {

//                        header_title.setText("Where it all begins!!");
//                        header_title.setTag(Integer.toString(position));
//                        header_title.setVisibility(View.VISIBLE);
                    }
//                    text_description.setVisibility(View.VISIBLE);

//                    img_cild_image.setVisibility(View.VISIBLE);
//                 //   cel_bg.setVisibility(View.VISIBLE);
                    progressNewsList.setVisibility(View.VISIBLE);

//                    header_title.setText(obj.getCity());
//                    header_title.setTag(Integer.toString(position));
                    String disableImageURl = null;

                    // child_location_desc.setOnClickListener(this);
                    if (imageLoader == null)
                        imageLoader = App.getInstance().getImageLoader();

                    String imageURl = obj.getCityimg().toString();
                    System.out.println("============imageURl======1========" + imageURl);

                    int currentSteps = Integer.parseInt(obj.getSteps());
                    if (total_steps >= currentSteps) {

                        try {
//                            img_cild_image.setImageUrl(imageURl, imageLoader);
//                            loadImage(imageURl, img_cild_image, progressNewsList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        child_location_desc.setText(obj.getCitymsg().toString());
                        child_location_desc.setMaxLines(4);

                        Bitmap bitm = BitmapFactory.decodeResource(getResources(), R.drawable.landscape_blue_icon);
//                        header_icon.setImageBitmap(bitm);
                    } else {
                        if (imageURl.equals("0")) {
                        } else if (imageURl.length() < 10) {
                        } else {
                            int fulllen = imageURl.length();
                            fulllen = fulllen - 10;
                            String first = imageURl.substring(0, fulllen);
                            disableImageURl = first + "banner_disable.jpg";
                            System.out.println(first);
                        }
                        System.out.println("============imageURl====0==========" + imageURl);
                        try {
//                            img_cild_image.setImageUrl(disableImageURl, imageLoader);
//                            loadImage(disableImageURl, img_cild_image, progressNewsList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        child_location_desc.setText("");
                        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.landscape_grey_icon);
//                        header_icon.setImageBitmap(bitmap5);

                    }

                    child_location_desc.setTag(Integer.toString(position));
                    //   img_cild_image.setTag(Integer.toString(position));

//=================================
                    //=================================
                    //  }
                }
                img_cild_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // fire user Like

                        String puio = v.getTag().toString();
                        int pos = Integer.parseInt(puio);
                        RoadLocationObj obj = myCityList.get(pos);

                        String st = obj.getSteps().toString();
                        int istp = Integer.parseInt(st);
                        if (pos == 1) {
                            String URL = obj.getMeta().toString();
                            System.out.println("====Reached===========" + URL);
                            if (URL.length() > 5) {
                                if (URL.contains("panaroma")) {
                                    if (gyroExists) {
                                        Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                } else if (URL.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                }

                            }
                        } else if (service_checkpoints.equals("true")) {

                            if (isContain(activeArray, Integer.toString(pos))) {
                                String URL = obj.getMeta().toString();
                                if (URL.contains("panaroma")) {
                                    if (gyroExists) {
                                        Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                } else if (URL.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                }
                            }
                        } else if (total_steps > istp) {
                            String URL = obj.getMeta().toString();
                            System.out.println("====Reached===========" + URL);
                            if (URL.length() > 5) {
                                if (URL.contains("panaroma")) {
                                    if (gyroExists) {
                                        Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                } else if (URL.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            System.out.println("====Not Reached===========");
                        }
                    }
                });

                child_location_desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // fire user Like

                        String puio = v.getTag().toString();
                        int pos = Integer.parseInt(puio);
                        RoadLocationObj obj = myCityList.get(pos);

                        String st = obj.getSteps().toString();
                        int istp = Integer.parseInt(st);
                        if (pos == 1) {
                            String URL = obj.getMeta().toString();
                            System.out.println("====Reached===========" + URL);
                            if (URL.length() > 5) {
                                if (URL.contains("panaroma")) {
                                    if (gyroExists) {
                                        Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                } else if (URL.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                }

                            }
                        } else if (service_checkpoints.equals("true")) {

                            if (isContain(activeArray, Integer.toString(pos))) {
                                String URL = obj.getMeta().toString();
                                if (URL.contains("panaroma")) {
                                    if (gyroExists) {
                                        Intent intent = new Intent(Journal_Activity.this, PanaramaView_NewActivity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Journal_Activity.this, PanoramaView_LawDevices_Activity.class);
                                        intent.putExtra("URL", URL);
                                        startActivity(intent);
                                    }
                                } else if (URL.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                }
                            }
                        } else if (total_steps > istp) {
                            String URL = obj.getMeta().toString();
                            System.out.println("====Reached===========" + URL);
                            if (URL.length() > 5) {
                                if (URL.contains("nativeview")) {
                                    Intent intent = new Intent(Journal_Activity.this, TemplateViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Journal_Activity.this, CommonWebViewActivity.class);
                                    intent.putExtra("URL", URL);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            System.out.println("====Not Reached===========");
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();

            }

            return vi;


        }


    }

//hddhgcghfghghg

    private boolean isContain(ArrayList<String> arr, String targetValue) {
        for (int i = 0; i < arr.size(); i++) {
            String currentval = arr.get(i);
            if (currentval.equals(targetValue))
                return true;
        }
        return false;
    }
}

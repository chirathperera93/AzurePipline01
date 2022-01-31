package com.ayubo.life.ayubolife.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.BodyViewActivity;
import com.ayubo.life.ayubolife.activity.CommonView_Second_WebviewActivity;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.FoodViewActivity;
import com.ayubo.life.ayubolife.activity.HealthViewActivity;
import com.ayubo.life.ayubolife.activity.MindViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.gallery.Image;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;


public class MedicalReports_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Image> images;
    private ImageView viewPager;
    String hasToken, loginType;
    SwipeRefreshLayout mySwipeRefreshLayout;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    ImageButton btn_signout;
    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;
    DatabaseHandler db;
    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private PrefManager pref;
    //private ProgressDialog progressBar;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url=null;ImageButton  boBack;
    private OnFragmentInteractionListener mListener;
 //   ProgressDialog prgDialog;
    public MedicalReports_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Prescription3_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicalReports_Fragment newInstance(String param1, String param2) {
        MedicalReports_Fragment fragment = new MedicalReports_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    PrefManager prefNew;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_prescription3_, container, false);

        View view = inflater.inflate(R.layout.fragment_medical_reports_, container, false);
        System.out.println(userid_ExistingUser + "============MedicalReports_Fragment=======onCreateView");
        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        db = DatabaseHandler.getInstance(getContext());

        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);
        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        prefNew=new PrefManager(getContext());

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);


        pref = new PrefManager(getContext());
        hasToken=pref.getLoginUser().get("hashkey");

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);


        url = ApiClient.BASE_URL + "index.php?entryPoint=mobileMedicalHistory";
        String newUrl=Utility.appendAyuboLoginInfo(prefNew.getLoginUser().get("hashkey"),url);



        if (Utility.isInternetAvailable(getContext())) {

            webView = (WebView) view.findViewById(R.id.webView_medicalreports);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);

            webView.setWebViewClient(new Callback());

            webView.requestFocus(View.FOCUS_DOWN);
            webView.loadUrl(newUrl);
            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            mySwipeRefreshLayout.setRefreshing(false);

                        }
                    }
            );

        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

public class Callback extends WebViewClient{

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        boolean val=false;
        System.out.println("========Medical Reports======Redireted URL===================="+url);
        if(url.contains("index.php?entryPoint=mobileMedicalHistory")){
            val=false;
            return val;
        }
        else if(url.contains("index.php?module=PC_Hospitals&action=connect")){
            val=true;
            Intent intent = new Intent(getContext(), CommonView_Second_WebviewActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);

            return val;
        }
       else if(url.contains(MAIN_URL_APPS)){
            val=true;
            System.out.println("==============Redireted URL===================="+url);
            //  url="https://www.w3schools.com/";
            webView.loadUrl(url);
            return val;
        }
        else if(url.contains("disable_back")){
            val=true;
            return val;
        }
        else if(url.contains("index.php?entryPoint=mobile_mind_view")){
            val=true;
            Intent in = new Intent(getContext(), MindViewActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("index.php?entryPoint=mobile_body_view")){
            val=true;
            Intent in = new Intent(getContext(), BodyViewActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("index.php?entryPoint=mobile_food_view")){
            val=true;
            Intent in = new Intent(getContext(), FoodViewActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("index.php?entryPoint=mobile_health_view")){
            val=true;
            Intent in = new Intent(getContext(), HealthViewActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
            val=false;
            return val;
        }
        else if(url.contains("initChatMobile")){
            val=true;
            Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
            return val;
        }
        else if(url.contains(MAIN_URL_APPS)){
            val=true;
            webView.loadUrl(url);
            return val;
        }
        else if(url.contains("water_intake_window_call")){
            val=true;
            Intent in = new Intent(getContext(), WaterActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("openInBrowser=true")){
            val=true;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            return val;
        }
        else if(url.contains(ApiClient.BASE_URL)){
            val=true;
            Intent intent = new Intent(getContext(), CommonView_Second_WebviewActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
            return val;
        }
        else{
            val=true;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            return val;
        }

    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
        String htmlFilename = "error.html";
        AssetManager mgr =getActivity().getBaseContext().getAssets();
        try {
            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = Utility.StreamToString(in);
            in.close();
            webView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        if(webProgress_timeline!=null){
//            webProgress_timeline.dismiss();
//        }


    }
}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

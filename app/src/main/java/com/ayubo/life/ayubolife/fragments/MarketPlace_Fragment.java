package com.ayubo.life.ayubolife.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.gallery.Image;
import com.ayubo.life.ayubolife.model.User;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MarketPlace_Fragment extends Fragment {
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
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url=null;ImageButton  boBack;
    private OnFragmentInteractionListener mListener;
    ProgressDialog prgDialog;
    public MarketPlace_Fragment() {
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
    public static MarketPlace_Fragment newInstance(String param1, String param2) {
        MarketPlace_Fragment fragment = new MarketPlace_Fragment();
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
    }PrefManager pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_prescription3_, container, false);

        View view = inflater.inflate(R.layout.fragment_medical_reports_, container, false);

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");

        db = DatabaseHandler.getInstance(getContext());

        pref = new PrefManager(getContext());
        User u = db.getUserProfileDetailsForUserId();
        if (u == null) {
            System.out.println(userid_ExistingUser + "=============Empty User==========");
        } else {
            userid_ExistingUser = u.getUsernameid();
            hasToken = u.getPassword();
            loginType = u.getDatemodified();
            System.out.println(userid_ExistingUser+"======================="+hasToken+"      "+loginType);
        }
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        prgDialog = new ProgressDialog(getContext());
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);


        url = ApiClient.BASE_URL_entypoint+ "ayuboLifeTimelineLogin&ref=";
        url = ApiClient.BASE_URL + "index.php?entryPoint=myProfilePage";
        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);

        if (Utility.isInternetAvailable(getContext())) {

            webView = (WebView) view.findViewById(R.id.webView1);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.getSettings().setDomStorageEnabled(true);

            System.out.println("=====url========="+url);
            System.out.println("=====encodedHashToken========="+encodedHashToken);

            try {
                webView.loadUrl(url + encodedHashToken);
                webView.setWebViewClient(new WebViewController());
            }catch(Exception e){
                System.out.println("=====Web Loading Error========="+e);
            }

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



    private void userLogoutServiceCall() {
        if (Utility.isInternetAvailable(getContext())) {
            prgDialog.show();
            prgDialog.setMessage("Loading...");
            Service_userLogout task = new Service_userLogout();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }
    }
    private class Service_userLogout extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            makePostRequest5();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.cancel();
            if (!(statusFromServiceAPI_db == null)) {
                //  userIdFromServiceAPI
                if (statusFromServiceAPI_db.equals("99")) {
                    textt.setText("Email or Password invalid");
                    toastt.setView(layoutt);
                    toastt.show();

                    return;
                }
                if (statusFromServiceAPI_db.equals("11")) {

                    textt.setText("Email or Password invalid");
                    toastt.setView(layoutt);
                    toastt.show();

                    return;
                }
                if (statusFromServiceAPI_db.equals("0")) {

                    //Successfull Logout ================================

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("uid", "0");
                    editor.putBoolean("loginStatus", false);
                    editor.commit();

                //    startActivity(new Intent(getContext(), MainActivity.class));

                } else {
                    //  Toast.makeText(getApplicationContext(), "Login Failed !",
                    //        Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
    private void makePostRequest5() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        //  email = lusername;
        //  password = lpassword;

        String jsonStr =
                "{" +

                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "session_logout"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("..........response..........." + response);

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }
                        JSONObject jsonObj = new JSONObject(responseString);


                        String number = jsonObj.optString("number").toString();

                        if(number.isEmpty()){

                            String res = jsonObj.optString("result").toString();

                            result = Integer.parseInt(res);
                            statusFromServiceAPI_db=Integer.toString(result);
                            System.out.println("==========================Result===============>" + result);
                            if (result == 11) {
                                System.out.println("...........................................result " + result);

                            }
                            if (result == 0) {

                                statusFromServiceAPI_db="0";

                            }

                        }else{

                            statusFromServiceAPI_db = "99";
                            System.out.println("=========================LOGIN FAIL=========>" + 3);
                        }


//                        if (number.equals("10")) {
//                            statusFromServiceAPI_db = "11";
//                            System.out.println("=========================LOGIN FAIL=========>" + number);
//                        } else {


                    }
              catch (Exception e) {
            // Log exception
            e.printStackTrace();
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
    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

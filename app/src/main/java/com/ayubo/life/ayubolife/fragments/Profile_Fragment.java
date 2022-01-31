package com.ayubo.life.ayubolife.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
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
import com.ayubo.life.ayubolife.gallery.Image;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;


import java.util.ArrayList;


public class Profile_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Image> images;
    private ImageView viewPager;
    String hasToken;
   // SwipeRefreshLayout mySwipeRefreshLayout;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    ImageButton btn_signout;
    TextView btn_Appointment;

    SharedPreferences prefs;
    String userid_ExistingUser;

    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String url=null;String url_logout=null;
    WebView webView;
   // ImageButton  boBack;
    private OnFragmentInteractionListener mListener;
    ProgressDialog prgDialog_pro;
    String regId;


    View view;
    public Profile_Fragment() {
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
    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
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
    PrefManager pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_prescription3_, container, false);

        view = inflater.inflate(R.layout.fragment_image_slider, container, false);

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
        hasToken= prefs.getString("hashToken", "0");

        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        pref = new PrefManager(getContext());


        url = ApiClient.BASE_URL + "index.php?entryPoint=myProfilePage";
        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);


        webView = (WebView) view.findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Utility.isInternetAvailable(getContext())) {

            System.out.println(userid_ExistingUser+"======================="+encodedHashToken);
            prgDialog_pro = new ProgressDialog(getContext());
            prgDialog_pro.setCancelable(false);
            prgDialog_pro.show();
            prgDialog_pro.setMessage("Loading Profile...");

            webView.loadUrl(newUrl);
            webView.setWebViewClient(new WebViewController());

            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    prgDialog_pro.dismiss();
                    System.out.println("=======LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL=======Profile====");
                }
            });

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

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

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

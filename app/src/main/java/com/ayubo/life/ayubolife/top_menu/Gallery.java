package com.ayubo.life.ayubolife.top_menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.gallery.Image;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Gallery extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Image> images;
    private ImageView viewPager;
    String hashToken, loginType;
    SwipeRefreshLayout mySwipeRefreshLayout;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    Button valid;

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
    private com.ayubo.life.ayubolife.fragments.VedioGallery_Fragment.OnFragmentInteractionListener mListener;
    ProgressDialog prgDialog,web_V_prgDialog;
    public Gallery() {
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
    public static Gallery newInstance(String param1, String param2) {
        Gallery fragment = new Gallery();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Whatever is there to save
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }
    PrefManager pref=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_prescription3_, container, false);

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
        hashToken= prefs.getString("hashToken", "0");
        setRetainInstance(true);

        pref=new PrefManager(getContext());
        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
        Ram.setIsOne_Medicine(true);

        try {
            encodedHashToken = URLEncoder.encode(hashToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        url = ApiClient.BASE_URL + "index.php?module=HAL_Timeline&action=gallery";
        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);

        if (Utility.isInternetAvailable(getContext())) {

            webView = (WebView) view.findViewById(R.id.webView_videogallery);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.getSettings().setJavaScriptEnabled(true);
            WebSettings ws = webView.getSettings();
            ws.setJavaScriptEnabled(true);
            ws.setDomStorageEnabled(true);

            web_V_prgDialog = new ProgressDialog(getContext());
            web_V_prgDialog.show();
            web_V_prgDialog.setCancelable(false);
            web_V_prgDialog.setMessage("Loading..");
            webView.getSettings().setDomStorageEnabled(true);


            try {
                webView.loadUrl(newUrl);
                //  url="https://www.w3schools.com/";
                //   webView.loadUrl(url);
                webView.setWebViewClient(new WebViewController());
            }catch(Exception e){
                System.out.println("=====Web Loading Error========="+e);
            }
            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    if(web_V_prgDialog!=null){
                        web_V_prgDialog.dismiss();
                    }

                    System.out.println("=======LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL===Pre Gallery=======");
                }
            });


        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();

        }
//        webView.setWebViewClient(new WebViewClient() {
//
//            public void onPageFinished(WebView view, String url) {
//                prgDialog.cancel();
//                System.out.println("=======LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL===========");
//            }
//        });

        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Gallery.OnFragmentInteractionListener) {
            mListener = (com.ayubo.life.ayubolife.fragments.VedioGallery_Fragment.OnFragmentInteractionListener) context;
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
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(web_V_prgDialog!=null){
            web_V_prgDialog.dismiss();
            web_V_prgDialog=null;
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

class MyWebViewClient2 extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).getHost().equals("www.google.com")) {
            // This is my web site, so do not override; let my WebView load the page
            // Toast.makeText(getContext(), "www.google.com", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        System.out.println("**********************************************************2");
        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        // startActivity(intent);
        return true;
    }
}


class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();

        System.out.println("**********************************************************");

    }
}
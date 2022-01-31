package com.ayubo.life.ayubolife.top_menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.fragments.Profile_Fragment;
import com.ayubo.life.ayubolife.fragments.TimeLineFragment;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
 View view;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;  String url;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog prgDialog_pro;
    HashMap<String, String> user=null;

    SharedPreferences prefs;
    //String userid_ExistingUser;
    private PrefManager prefManager;
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    SwipeRefreshLayout mySwipeRefreshLayout;
    WebView webView;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home2, container, false);


        prefManager = new PrefManager(getContext());
        user=prefManager.getLoginUser();
        hasToken=user.get("hashkey");

        System.out.println("===@ Home=====hasToken=============="+hasToken);

        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        if(hasToken==null){

        }else {

            try {
                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

            url = ApiClient.BASE_URL_entypoint + "ayuboLifeTimelineLogin&ref=";

            if (Utility.isInternetAvailable(getContext())) {

                web_prgDialog = new ProgressDialog(getContext());
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading Timeline...");

                webView = (WebView) view.findViewById(R.id.webView_newhome);
                webView.getSettings().setJavaScriptEnabled(true);


                webView.clearCache(true);

                webView.setWebViewClient(new Callback());
                webView.loadUrl(url + encodedHashToken);
                System.out.println("==========TimeLine===url=======" + url);

                System.out.println("==========TimeLine===onCreateView=======");
                System.out.println("==========TimeLine===onCreateView=======");
                System.out.println("==========TimeLine===onCreateView=======");
                System.out.println("==========TimeLine===onCreateView=======");
                System.out.println("==========TimeLine===onCreateView=======");
                System.out.println("==========TimeLine===onCreateView=======");

                System.out.println("=============TimeLine =======" + encodedHashToken);
             //   webView.setWebViewClient(new WebViewController());

                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {
                        web_prgDialog.cancel();
                        System.out.println("=======LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL===TimeLine=======");
                    }
                });

                mySwipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                if (Utility.isInternetAvailable(getContext())) {
                                    webView.reload();
                                } else {
                                    mySwipeRefreshLayout.setRefreshing(false);
                                    textt.setText("Unable to detect an active internet connection");
                                    toastt.setView(layoutt);
                                    toastt.show();
                                }

                                webView.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onPageFinished(WebView web, String url) {
                                        if (mySwipeRefreshLayout.isRefreshing()) {
                                            mySwipeRefreshLayout.setRefreshing(false);
                                        }
                                    }
                                });
                            }
                        }
                );

            } else {
                textt.setText("Unable to detect an active internet connection");
                toastt.setView(layoutt);
                toastt.show();

            }
        }
        return view;

    }
    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false; String cc=null;
            cc="https://devo.ayubo.life/index.php?entryPoint=ayuboLifeTimeline";
            if(url.startsWith(cc)){
                val=false;
            }
            cc=MAIN_URL_LIVE_HAPPY + "water_intake_window_call";
            if(url.startsWith(cc)){
                if(cc.equals(url)){

                    val=true;
                    Intent in = new Intent(getContext(), WaterActivity.class);
                    startActivity(in);
                }
            }else{
                val=true;
                System.out.println("================");
                System.out.println("======Out Side App==========="+url);
                System.out.println("=================");

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
            return val;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            webView.reload();
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
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
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;
            String cc=MAIN_URL_LIVE_HAPPY + "water_intake_window_call";
            if(url.startsWith("https://livehappy.ayubo.life")){
                if(cc.equals(url)){
                    System.out.println("================");
                    System.out.println("===WebViewController===In Side App=====WebViewClient======"+url);
                    System.out.println("=================");
                    val=true;
                    //   Intent in = new Intent(getContext(), WaterActivity.class);
                    //  startActivity(in);
                }
            }else{
                val=true;
                System.out.println("================");
                System.out.println("======Out Side App==========="+url);
                System.out.println("=================");

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

            return val;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if(webProgress_timeline!=null){
//                webProgress_timeline.dismiss();
//            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webView.reload();
        }

    }
}

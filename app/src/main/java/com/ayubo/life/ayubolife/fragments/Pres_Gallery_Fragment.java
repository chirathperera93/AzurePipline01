package com.ayubo.life.ayubolife.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.gallery.Image;
import com.ayubo.life.ayubolife.model.User;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Pres_Gallery_Fragment extends Fragment {
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
    TextView header_menu;  ImageButton btn_GoBackToDoctorsSearch;
    ImageButton btn_signout;
    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;
    DatabaseHandler db;
    String encodedHashToken,statusFromServiceAPI_db;
    int result;protected View mView;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url=null;ImageButton  boBack;
    private OnFragmentInteractionListener mListener;
    ProgressDialog prgDialog,web_prgDialog;
    public Pres_Gallery_Fragment() {
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
    public static Pres_Gallery_Fragment newInstance(String param1, String param2) {
        Pres_Gallery_Fragment fragment = new Pres_Gallery_Fragment();
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

        mView = inflater.inflate(R.layout.fragment_pres__gallery_, container, false);

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
//        prgDialog = new ProgressDialog(getContext());
//        prgDialog.setCancelable(false);
//        prgDialog.show();
//        prgDialog.setMessage("Loading...");
        Ram.setIsOne_Medicine(true);
        db = DatabaseHandler.getInstance(getContext());
        User u = db.getUserProfileDetailsForUserId();
        pref=new PrefManager(getContext());
        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) mView.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
        hasToken= prefs.getString("hashToken", "0");

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        header_menu = (TextView) mView.findViewById(R.id.goto_MyOrders);
        header_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Ram.setIsOne_DoctorSearch(true);
                Ram.setIsTwo_DoctorSearch(false);
//                Ram.setIsThree_DoctorSearch(false);
//                Ram.setIsFour_DoctorSearch(false);
//                Ram.setIsFive_DoctorSearch(false);
//                Ram.setIsSix_DoctorSearch(false);
                Ram.setIsMyHistory(false);

                Ram.setIsOne_Medicine(true);
                Ram.setIsTwo_Medicine(false);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //  ft.remove(fm.findFragmentByTag("f" + Integer.toString(navigation - 2)));
                Fragment fragOne = new Medicine_Holder2_Fragment();
                Bundle arguments = new Bundle();
                arguments.putBoolean("shouldYouCreateAChildFragment", true);
                fragOne.setArguments(arguments);
                ft.replace(R.id.fr_medicine__holder_layout, fragOne);
                ft.commit();

            }
        });
        btn_GoBackToDoctorsSearch = (ImageButton) mView.findViewById(R.id.btn_GoBackToDoctorsSearch);
        btn_GoBackToDoctorsSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Ram.setIsOne_DoctorSearch(true);
                Ram.setIsTwo_DoctorSearch(false);
//                Ram.setIsThree_DoctorSearch(false);
//                Ram.setIsFour_DoctorSearch(false);
//                Ram.setIsFive_DoctorSearch(false);
//                Ram.setIsSix_DoctorSearch(false);


                Ram.setIsOne_Medicine(true);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //  ft.remove(fm.findFragmentByTag("f" + Integer.toString(navigation - 2)));
                Fragment fragOne = new Medicine_Holder2_Fragment();
                Bundle arguments = new Bundle();
                arguments.putBoolean("shouldYouCreateAChildFragment", true);
                fragOne.setArguments(arguments);
                ft.replace(R.id.fr_medicine__holder_layout, fragOne);

                ft.commit();

            }
        });


        mySwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeContainer);

        url = ApiClient.BASE_URL + "index.php?module=HAL_Timeline&action=gallery";
        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);



        if (Utility.isInternetAvailable(getContext())) {
            web_prgDialog = new ProgressDialog(getContext());
            web_prgDialog.setCancelable(false);
            web_prgDialog.show();
            web_prgDialog.setMessage("Loading...");

            webView = (WebView) mView.findViewById(R.id.webView_pre_gallery);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.getSettings().setDomStorageEnabled(true);

            System.out.println("=====url========="+url);
            System.out.println("===in Gallery==encodedHashToken========="+encodedHashToken);
            System.out.println("==================");
            System.out.println("==================");
            System.out.println("==================");
            System.out.println("==================");
            try {
                webView.loadUrl(newUrl);
                webView.setWebViewClient(new WebViewController());
            }catch(Exception e){
                System.out.println("=====Web Loading Error========="+e);
            }
            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    web_prgDialog.cancel();
                    System.out.println("=======LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL===Pre Image Gallery=======");
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
        System.out.println("==================mView==========="+mView);
        return mView;


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
            view.loadUrl(url);
            return true;
        }
    }
}

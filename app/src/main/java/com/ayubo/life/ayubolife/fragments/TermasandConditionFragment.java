package com.ayubo.life.ayubolife.fragments;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TermasandConditionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TermasandConditionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermasandConditionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters

    String url=null;ImageButton  boBack;

    private ProgressDialog prgDialog_ch,progressBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TermasandConditionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TermasandConditionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TermasandConditionFragment newInstance(String param1, String param2) {
        TermasandConditionFragment fragment = new TermasandConditionFragment();
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

        View view = inflater.inflate(R.layout.fragment_termasand_condition, container, false);
        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
        hasToken= prefs.getString("hashToken", "0");

        userid_ExistingUser= prefs.getString("uid", "0");



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


        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);


        prgDialog_ch = new ProgressDialog(getContext());
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

//        ImageButton  boBack=(ImageButton)view.findViewById(R.id.boBack);
//        boBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                userLogoutServiceCall();
//            }
//        });
        url = "https://www.hemashospitals.com/legal/privacy.php";


        if (Utility.isInternetAvailable(getContext())) {
            progressBar = new ProgressDialog(getContext());
            progressBar.show();
            progressBar.setMessage("Loading data...");
            webView = (WebView) view.findViewById(R.id.webView_challenges);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.getSettings().setDomStorageEnabled(true);

            System.out.println("=====url========="+url);
            System.out.println("=====encodedHashToken========="+encodedHashToken);

            try {
                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewController());

                System.out.println("=====userid_ExistingUser========="+userid_ExistingUser);
                System.out.println("=====hasToken========="+hasToken);

            }catch(Exception e){
                System.out.println("=====Web Loading Error========="+e);
            }
            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.dismiss();
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
        if (context instanceof Challenges_Fragment.OnFragmentInteractionListener) {
            mListener = (TermasandConditionFragment.OnFragmentInteractionListener) context;
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

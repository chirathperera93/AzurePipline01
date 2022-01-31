package com.ayubo.life.ayubolife.new_sidemenu_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CorporateNewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CorporateNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CorporateNewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PrefManager prefManager;
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    SwipeRefreshLayout mySwipeRefreshLayout;
    WebView webView;
    HashMap<String, String> user=null;
    String url;

    private OnFragmentInteractionListener mListener;

    public CorporateNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CorporateNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CorporateNewFragment newInstance(String param1, String param2) {
        CorporateNewFragment fragment = new CorporateNewFragment();
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
    private void localConstructor(){
        prefManager = new PrefManager(getContext());
        user=prefManager.getLoginUser();
        hasToken=user.get("hashkey");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_corporate_new,
                container, false);

        localConstructor();

        if(hasToken==null){}else {
            try {
                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            url = ApiClient.BASE_URL_entypoint + "ayuboLifeTimelineLogin&type=findCorporate&ref=";
            if (Utility.isInternetAvailable(getContext())) {

                web_prgDialog = new ProgressDialog(getContext());
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                webView = (WebView) view.findViewById(R.id.webView_coporate);
                webView.getSettings().setJavaScriptEnabled(true);

                webView.clearCache(true);
                webView.loadUrl(url + encodedHashToken);
                webView.setWebViewClient(new WebViewController());
                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {
                        web_prgDialog.cancel();

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

                                    Toast.makeText(getActivity(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();

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

                Toast.makeText(getActivity(),R.string.toast_no_internet, Toast.LENGTH_LONG).show();

            }
        }
        return view;

    }


    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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
}

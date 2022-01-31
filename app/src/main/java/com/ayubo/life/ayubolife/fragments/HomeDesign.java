package com.ayubo.life.ayubolife.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.model.NewHomeMenuOneObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeDesign.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeDesign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeDesign extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<NewHomeMenuOneObj> dataList = null;
    ArrayList<NewHomeMenuOneObj> serviceList = null;
    LinearLayout layout_main_menu_horizontal,layout_main_menu_horizontal_footer;
    LayoutInflater inflater; ImageLoader imageLoader;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeDesign() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeDesign.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeDesign newInstance(String param1, String param2) {
        HomeDesign fragment = new HomeDesign();
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

        View view= inflater.inflate(R.layout.fragment_home_design, container, false);

        callOnLoadService();

        layout_main_menu_horizontal=(LinearLayout)view.findViewById(R.id.layout_main_menu_horizontal);
        layout_main_menu_horizontal_footer=(LinearLayout)view.findViewById(R.id.layout_main_menu_horizontal_footer);

        return view;

    }
    private void callOnLoadService() {

        if (Utility.isInternetAvailable(getContext())) {
            Service_checkAppVersion task = new Service_checkAppVersion();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {

        }
    }

    private class Service_checkAppVersion extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... urls) {
            checkAppVersion();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(dataList.toString());

            for (int i = 0; i < dataList.size(); i++) {
                NewHomeMenuOneObj obj = dataList.get(i);
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.newdisign_menu_item, null);
                ProgressBar progressNewsList = (ProgressBar) myView.findViewById(R.id.progressNewsList);
                TextView txt_desc = (TextView) myView.findViewById(R.id.txt_desc);
                Button btn_action = (Button) myView.findViewById(R.id.btn_action);
                btn_action.setTag(obj.getLink());
                btn_action.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click

                        String url=v.getTag().toString();
                        System.out.println(url);
                        Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                        intent.putExtra("URL",url );
                        startActivity(intent);
                    }
                });
                NetworkImageView remote_image_icon=(NetworkImageView)myView.findViewById(R.id.remote_image_icon);
                String img=  obj.getImage();

                if (imageLoader == null)
                    imageLoader= App.getInstance().getImageLoader();

                remote_image_icon.setImageUrl(img, imageLoader);
                loadImage(img,remote_image_icon,progressNewsList);
                txt_desc.setText(obj.getTitle());
                btn_action.setText(obj.getButton_text());
                layout_main_menu_horizontal.addView(myView);
            }
//=================================================
            //================================

            for (int i = 0; i < serviceList.size(); i++) {
                NewHomeMenuOneObj obj = serviceList.get(i);
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.newdisign_menu_item_footer, null);
                ProgressBar progressNewsList = (ProgressBar) myView.findViewById(R.id.progressNewsList);
                TextView txt_service_name = (TextView) myView.findViewById(R.id.txt_service_name);
                NetworkImageView remote_image_icon=(NetworkImageView)myView.findViewById(R.id.footer_remote_image_icon);

                remote_image_icon.setTag(obj.getLink());
                remote_image_icon.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click

                        String url=v.getTag().toString();
                        System.out.println(url);
                        Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                        intent.putExtra("URL",url );
                        startActivity(intent);
                    }
                });

                String img=  obj.getImage();

                if (imageLoader == null)
                    imageLoader= App.getInstance().getImageLoader();

                remote_image_icon.setImageUrl(img, imageLoader);
                loadImage(img,remote_image_icon,progressNewsList);
                txt_service_name.setText(obj.getTitle());
                layout_main_menu_horizontal_footer.addView(myView);
            }

            //===========================================
            //========================================



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

        private void checkAppVersion() {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL+"custom/include/javascript/home_params.json");
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String jsonStree =
                    "{" +
                            "\"userid\": \"" + "" + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "app_on_load"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStree));
            System.out.println(".............getUserProfilePicture................" + jsonStree);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }
            //making POST request.
            try {
                int r = 0;
                HttpResponse response = null;
                try {
                    response = httpClient.execute(httpPost);
                    System.out.println("......getUserProfilePicture..response..........." + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response != null) {
                    r = response.getStatusLine().getStatusCode();
                    if (r == 200) {


                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                            StringBuilder builder = new StringBuilder();

                            if (!(builder == null && builder.toString().equals(""))) {

                                for (String line = null; (line = reader.readLine()) != null; ) {

                                    dataList = new ArrayList<NewHomeMenuOneObj>();
                                    serviceList = new ArrayList<NewHomeMenuOneObj>();

                                    builder.append(line).append("\n");
                                    System.out.println("..............=........................." + line);
                                    JSONObject jsonObj = new JSONObject(line);
                                    String res = jsonObj.optString("tip").toString();
                                    String main = jsonObj.optString("main").toString();
                                    String service = jsonObj.optString("service").toString();
                                    String bg = jsonObj.optString("bg").toString();

                                    JSONArray myListsAll = null;
                                    try {
                                        myListsAll = new JSONArray(main);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    for (int i = 0; i < myListsAll.length(); i++) {

                                        JSONObject jsonMainNode3 = null;
                                        try {
                                            jsonMainNode3 = (JSONObject) myListsAll.get(i);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        String title = jsonMainNode3.optString("title");
                                        String image = jsonMainNode3.optString("image");
                                        String button = jsonMainNode3.optString("button");
                                        String button_text = jsonMainNode3.optString("button_text");
                                        String link = jsonMainNode3.optString("link");
                                        dataList.add(new NewHomeMenuOneObj(title, image, button, button_text, link));
                                    }

                                    //=======================================================
                                    //==========================================
                                    JSONArray myServiceLists = null;
                                    try {
                                        myServiceLists = new JSONArray(service);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    for (int i = 0; i < myServiceLists.length(); i++) {

                                        JSONObject jsonMainNode3 = null;
                                        try {
                                            jsonMainNode3 = (JSONObject) myServiceLists.get(i);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        String title = jsonMainNode3.optString("title");
                                        String image = jsonMainNode3.optString("image");
                                        String link = jsonMainNode3.optString("link");
                                        serviceList.add(new NewHomeMenuOneObj(title, image, "", "", link));
                                    }





                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }

                }

            } catch (ClientProtocolException e) {
                System.out.println(".........Slow Network Connection..error......." + e);
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }

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

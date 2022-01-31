package com.ayubo.life.ayubolife.health;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.rest.ApiClient;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.R.id.imageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Health_TracksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Health_TracksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Health_TracksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CustomListAdapterLite adapter;   ListView list;
    private ArrayList<HashMap<String, String>> data;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Health_TracksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Health_TracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Health_TracksFragment newInstance(String param1, String param2) {
        Health_TracksFragment fragment = new Health_TracksFragment();
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

        View view = inflater.inflate(R.layout.fragment_health__tracks, container, false);

        list=(ListView)view.findViewById(R.id.health_track_list);
        HashMap<String, String> song = new HashMap<String, String>();

        ArrayList<SImpleListString> data=new ArrayList<SImpleListString>();

        Bitmap bitmap1= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.water_icns);
        data.add(new SImpleListString(bitmap1,"WATER INTAKE"));

        Bitmap bitmap2= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.temp_icns);
        data.add(new SImpleListString(bitmap2,"TEMPERATURE"));

        Bitmap bitmap3= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sugar_icns);
        data.add(new SImpleListString(bitmap3,"FASTING BLOOD SUGAR"));

        Bitmap bitmap4= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sugar_icns);
        data.add(new SImpleListString(bitmap4,"RANDOM BLOOD SUGAR"));

        Bitmap bitmap5= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sugar_icns);
        data.add(new SImpleListString(bitmap5,"HBA1C"));

        Bitmap bitmap6= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.presure_icns);
        data.add(new SImpleListString(bitmap6,"BLOOD PERSSURE"));

        Bitmap bitmap7= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cholesterols);
        data.add(new SImpleListString(bitmap7,"CHOLESTEROL"));

        Bitmap bitmap8= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.vital_icns);
        data.add(new SImpleListString(bitmap8,"VITALS"));

        adapter=new CustomListAdapterLite(getContext(), data);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                if(position==0){
                    Intent in = new Intent(getContext(), WaterActivity.class);
                    startActivity(in);
                }
                if(position==1){
                  String url= ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=tempereture&form_name=TEMPERETURE#addData";
                  Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                  intent.putExtra("URL", url);
                  startActivity(intent);
                }
                if(position==2){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=blood_sugar&form_name=FASTING%20BLOOD%20SUGAR";
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==3){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=random_blood_sugar&form_name=RANDOM%20BLOOD%20SUGAR";
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==4){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=hba1c&form_name=HBA1C";
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==5){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=blood_pressure&form_name=BLOOD%20PRESSURE";
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==6){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=lipid_profile&form_name=CHOLESTEROL";
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==7){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=vitals&form_name=VITALS";
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
            }
        });

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

    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<SImpleListString> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<SImpleListString> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.health_custome_list_raw, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // title
            ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> song = new HashMap<String, String>();

            SImpleListString obj = data.get(position);

            title.setText(obj.getName());
            thumb_image.setImageBitmap(obj.getBitmap());

            return vi;
        }
    }
}

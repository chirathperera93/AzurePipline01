package com.ayubo.life.ayubolife.body;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.ayubo.life.ayubolife.activity.ActivityTrackerActivity;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.health.Health_TracksFragment;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.utility.Ram;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Body_TracksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Body_TracksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Body_TracksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
CustomListAdapterLite adapter;   ListView list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String activityType,imageName,metValue;



    private OnFragmentInteractionListener mListener;

    public Body_TracksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Body_TracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Body_TracksFragment newInstance(String param1, String param2) {
        Body_TracksFragment fragment = new Body_TracksFragment();
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

    private void   goToStartActivityScreen(){
       Intent intent = new Intent(getContext(), StartWorkoutActivity.class);


        if(activityType.equals("Boxing")){
            imageName="boxing";
            metValue="12";
        }
        else if(activityType.equals("Climbing")){
            imageName="climbing";
            metValue="11";
        }
        else if(activityType.equals("Jumping")){
            imageName="jumping";
            metValue="10";
        }
        else if(activityType.equals("Bicycling")){
            imageName="cycling";
            metValue="8";
        }
        else if(activityType.equals("Football")){
            imageName="football";
            metValue="8";
        }
        else if(activityType.equals("Tennis")){
            imageName="tennis";
            metValue="7";
        }
        else if(activityType.equals("Run")){
            imageName="run";
            metValue="7";
        }
        else if(activityType.equals("Aerobic")){
            imageName="aerobic";
            metValue="7";
        }
        else if(activityType.equals("Hiking")){
            imageName="hiking";
            metValue="6";
        }
        else if(activityType.equals("Swimming")){
            imageName="swim";
            metValue="6";
        }
        else if(activityType.equals("Basketball")){
            imageName="basketball";
            metValue="6";
        }
        else if(activityType.equals("Zumba")){
            imageName="zumba";
            metValue="6";
        }
        else if(activityType.equals("Exercise")){
            imageName="exercise";
            metValue="5.5";
        }
        else if(activityType.equals("Badminton")){
            imageName="badminton";
            metValue="4.5";
        }
        else if(activityType.equals("Walk")){
            imageName="walk";
            metValue="3.5";
        }
        else if(activityType.equals("Football")){
            imageName="football";
            metValue="8";
        }
        else if(activityType.equals("Exercise")){
            imageName="exercise";
            metValue="5.5";
        }
        else if(activityType.equals("Badminton")){
            imageName="badminton";
            metValue="4.5";
        }
        else if(activityType.equals("Golf")){
            imageName="golf";
            metValue="4.5";
        }
        else if(activityType.equals("Yoga")){
            imageName="yoga";
            metValue="2.5";
        }

        else{

        }

        intent.putExtra("fName", activityType);
        intent.putExtra("fImage", imageName);
        intent.putExtra("fMet", metValue);
        startActivity(intent);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_body__tracks, container, false);
        View view = inflater.inflate(R.layout.fragment_body__tracks, container, false);

        list=(ListView)view.findViewById(R.id.health_track_list);
        HashMap<String, String> song = new HashMap<String, String>();

        ArrayList<SImpleListString> data=new ArrayList<SImpleListString>();





        Bitmap bitmap1= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.runnings);
        data.add(new SImpleListString(bitmap1,"RUNNING"));

        Bitmap bitmap2= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.climbings);
        data.add(new SImpleListString(bitmap2,"HIKING"));

        Bitmap bitmap3= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.badmintons);
        data.add(new SImpleListString(bitmap3,"BADMINTON"));

        Bitmap bitmap4= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cyclings);
        data.add(new SImpleListString(bitmap4,"CYCLING"));

        Bitmap bitmap5= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.basketballs);
        data.add(new SImpleListString(bitmap5,"BASKETBALL"));

        Bitmap bitmap6= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.swimmings);
        data.add(new SImpleListString(bitmap6,"SWIMMING"));



        adapter=new CustomListAdapterLite(getContext(), data);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                if(position==0){

                    String activityId = "1d29596b-9771-3c41-724e-5937c8878a2b";
                    String activityName = "RUNNING";
                    String mets="7";
                    Intent intent = new Intent(getContext(), StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);

                }
                if(position==1){
                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
                    String activityName = "HIKING";
                    String mets="6";

                    Intent intent = new Intent(getContext(), StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);

                    startActivity(intent);
                }
                if(position==2){
                    String activityId = "b5504c46-4109-9724-d368-5948ad558767";
                    String activityName = "BADMINTON";
                    System.out.println("Selected Activity  : "+activityName);
                    Intent intent = new Intent(getContext(), ActivityTrackerActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    startActivity(intent);

//                    String activityId = "b5504c46-4109-9724-d368-5948ad558767";
//                    String activityName = "BADMINTON";
//                    String mets="4";
//                    Intent intent = new Intent(getContext(), StartWorkoutActivity.class);
//                    intent.putExtra("activityName", activityName);
//                    intent.putExtra("activityId", activityId);
//                    intent.putExtra("mets", mets);
//                    Ram.setActivityName(activityName);
//                    Ram.setActivityId(activityId);
//                    Ram.setMets(mets);
//                    startActivity(intent);
                }
                if(position==3){
                    String activityId = "8ceb0f82-9b73-a12a-225f-59478c7063c1";
                    String activityName = "CYCLING";
                    String mets="8";

                    Intent intent = new Intent(getContext(), StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);

                    startActivity(intent);

                }
                if(position==4){
                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
                    String activityName = "BASKETBALL";
                    System.out.println("Selected Activity  : "+activityName);
                    Intent intent = new Intent(getContext(), ActivityTrackerActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    startActivity(intent);
//                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
//                    String activityName = "BASKETBALL";
//                    String mets="6";
//                    Intent intent = new Intent(getContext(), StartWorkoutActivity.class);
//                    intent.putExtra("activityName", activityName);
//                    intent.putExtra("activityId", activityId);
//                    intent.putExtra("mets", mets);
//                    Ram.setActivityName(activityName);
//                    Ram.setActivityId(activityId);
//                    Ram.setMets(mets);
//                    startActivity(intent);
                }
                if(position==5){
                    String activityId = "e9ec0886-bd3b-786c-0a89-5948adf1c19a";
                    String activityName = "SWIMMING";
                    System.out.println("Selected Activity  : "+activityName);
                    Intent intent = new Intent(getContext(), ActivityTrackerActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    startActivity(intent);
//                    String activityId = "e9ec0886-bd3b-786c-0a89-5948adf1c19a";
//                    String activityName = "SWIMMING";
//                    String mets="6";
//
//                    Intent intent = new Intent(getContext(), StartWorkoutActivity.class);
//                    intent.putExtra("activityName", activityName);
//                    intent.putExtra("activityId", activityId);
//                    intent.putExtra("mets", mets);
//
//                    Ram.setActivityName(activityName);
//                    Ram.setActivityId(activityId);
//                    Ram.setMets(mets);
//
//                    startActivity(intent);
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

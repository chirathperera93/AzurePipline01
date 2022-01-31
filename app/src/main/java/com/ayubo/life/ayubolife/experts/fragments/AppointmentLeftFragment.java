package com.ayubo.life.ayubolife.experts.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.home_popup_menu.DoctorObj;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.quick_links.experts.ExpertAdapter;
import com.ayubo.life.ayubolife.quick_links.experts.Quick_Utility;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.utility.Ram;

import java.util.ArrayList;

;


public class AppointmentLeftFragment extends Fragment {
    EditText editText;
    ArrayList<Object> songsList;
    String activityName;
    String serviceName, activityHeading;
    View view;
    private RecyclerView recyclerView;
    ExpertAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appointment_left, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        activityName = "my_doctor";
        if (activityName.equals("my_doctor")) {
            activityHeading = "Medical Doctor";
            serviceName = "getMedicalExperts";
        } else if (activityName.equals("mydiatition")) {
            serviceName = "getLifeStyleExperts";
            activityHeading = "Dietitian";
        } else if (activityName.equals("mytrainer")) {
            serviceName = "getTrainerExperts";
            activityHeading = "Trainer";
        }

        songsList = new ArrayList<>();
        ArrayList<Object> songsLis = Ram.getDoctorDataList();

        recyclerView = view.findViewById(R.id.list_expert_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        if (songsLis != null) {
            songsList = (ArrayList<Object>) Quick_Utility.getSortedDoctorDataList(songsLis);
        }


        adapter = new ExpertAdapter(getActivity(), songsList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.my_recycle_divider));
        recyclerView.addItemDecoration(divider);


        View view_ask = view.findViewById(R.id.layout_search_button);
        editText = view_ask.findViewById(R.id.edt_search_value);
        TextView txt_activity_heading = view_ask.findViewById(R.id.txt_activity_heading);

        txt_activity_heading.setText("Talk to an Expert");
        editText.setHint("Search Expert/Speciality");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filterData(charSequence.toString());

                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setOnItemClickListener(new FamilyMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {

                DoctorObj dataModel = (DoctorObj) object;
                if (dataModel != null) {
                    String doctor_id = dataModel.getId().toString();
                    Intent intent = new Intent(getActivity(), MyDoctorLocations_Activity.class);
                    intent.putExtra("doctor_id", doctor_id);
                    intent.putExtra("activityName", activityName);
                    startActivity(intent);
                }


            }
        });

        return view;
    }


}

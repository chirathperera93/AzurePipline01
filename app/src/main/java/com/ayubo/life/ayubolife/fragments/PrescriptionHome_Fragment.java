package com.ayubo.life.ayubolife.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.utility.Ram;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrescriptionHome_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrescriptionHome_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrescriptionHome_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean isPrescription1_Fragment=true;
    boolean isPrescription2_Fragment=false;
    boolean isPrescription3_Fragment=false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PrescriptionHome_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrescriptionHome_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrescriptionHome_Fragment newInstance(String param1, String param2) {
        PrescriptionHome_Fragment fragment = new PrescriptionHome_Fragment();
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
     //   return inflater.inflate(R.layout.fragment_prescription_home_, container, false);


        View view = inflater.inflate(R.layout.fragment_prescription_home_,
                container, false);



        if(isPrescription1_Fragment){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragOne = new Prescription1_Fragment();
            Bundle arguments = new Bundle();
            arguments.putBoolean("shouldYouCreateAChildFragment9", true);
            fragOne.setArguments(arguments);
            ft.replace(R.id.fragment_container_9999, fragOne);
            ft.commit();
        }

        else if(isPrescription2_Fragment){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragOne = new Prescription2_Fragment();
            Bundle arguments = new Bundle();
            arguments.putBoolean("shouldYouCreateAChildFragment9", true);
            fragOne.setArguments(arguments);
            ft.replace(R.id.fragment_container_9999, fragOne);
            ft.commit();
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

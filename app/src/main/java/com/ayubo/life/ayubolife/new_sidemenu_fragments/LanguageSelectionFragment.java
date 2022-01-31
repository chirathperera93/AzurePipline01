package com.ayubo.life.ayubolife.new_sidemenu_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LanguageSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LanguageSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguageSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   Button btnNext;TextView succsess_msg;
    private PrefManager pref;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Spinner spinner2;
    public LanguageSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LanguageSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LanguageSelectionFragment newInstance(String param1, String param2) {
        LanguageSelectionFragment fragment = new LanguageSelectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_language_selection,
                container, false);
        spinner2= (Spinner) view.findViewById(R.id.language_selection);
        pref = new PrefManager(getContext());

        addItemsOnSpinner2();

        succsess_msg= (TextView) view.findViewById(R.id.succsess_msg);

        btnNext= (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setLanguage();
                succsess_msg.setText("Ayoubo.life app to be restarted for the changes to take effect.");
                btnNext.setVisibility(View.GONE);
            }
        });
        return view;
    }


    private void setLanguage() {
        String Text = spinner2.getSelectedItem().toString();

        String languageToLoad =null;
        if(Text.equals("English")){
            languageToLoad = "en";
        }
        else if(Text.equals("සින්හල")){
            languageToLoad = "si";
        }
        else{
            languageToLoad = "en";
        }
       pref.setLanguage(languageToLoad);

        // your language


//         Intent in = new Intent(getActivity(), WelcomeHome.class);
//        getActivity().finish();
//        startActivity(in);


    }
    public void addItemsOnSpinner2() {

       // spinner2 = (Spinner) view.findViewById(R.id.language_selection);
        List<String> list = new ArrayList<String>();
        list.add("English");
        list.add("සින්හල");
        list.add("ぬふあて");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
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

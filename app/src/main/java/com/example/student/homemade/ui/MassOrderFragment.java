package com.example.student.homemade.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.MainActivity;
import com.example.student.homemade.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;


public class MassOrderFragment extends Fragment {

    View v;
    //MAKING VARIBALES FOR TEXT VIEWS
    TextView submitTextView,headingText;
    EditText dateText,timeText,addressText;
    Button submitButton;
    Spinner spinnerItems,spinnerSeller;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference providerIds  = db.collection("Provider");




    public int checkForValidInput() {

        //INPUT FROM USER
        String date1 = dateText.getText().toString();
        String time1 = timeText.getText().toString();
        String address1 = addressText.getText().toString();
        if(date1.length() < 10) return 1;
        int temp1 = Integer.parseInt(date1.substring(0,2));
        int temp2 = Integer.parseInt(date1.substring(3,5));
        int temp3 = Integer.parseInt(date1.substring(6,10));
        //CHECKING FOR CORRECT DATE
        Toast.makeText(getActivity(),date1.substring(0,2), Toast.LENGTH_SHORT).show();
        return 0;

    }

    //SUBMIT BUTTON IN ACTION
    public int submitToUserButton(View view)
    {

        Toast.makeText(getActivity(), "ORDER SUBMITTED TO USER", Toast.LENGTH_SHORT).show();
        //INITIALISING ALL THE TEXTS
        headingText = (TextView) v.findViewById(R.id.headingText);
        dateText = (EditText) v.findViewById(R.id.dateText);
        addressText = (EditText) v.findViewById(R.id.addressText);
        timeText = (EditText) v.findViewById(R.id.timeText);
        submitTextView = (TextView) v.findViewById(R.id.submitTextView);
        submitButton = (Button) v.findViewById(R.id.submitToSellerButton);
        //INITIALISING DONE

        //NOW MAKING EVERYTHING INVISIBLE
        headingText.setVisibility(view.GONE);
        dateText.setVisibility(view.GONE);
        addressText.setVisibility(view.GONE);
        timeText.setVisibility(view.GONE);
        submitTextView.setVisibility(view.GONE);
        submitButton.setVisibility(view.GONE);
        spinnerItems.setVisibility(view.GONE);
        spinnerSeller.setVisibility(view.GONE);
        //EVERYTHING INVISIBLE ON BUTTON CLICK

        submitTextView.setVisibility(View.VISIBLE);
        return 1;

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MassOrderFragment() {
        // Required empty public constructor
    }

    public static MassOrderFragment newInstance() {
        MassOrderFragment fragment = new MassOrderFragment();
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

        v=  inflater.inflate(R.layout.fragment_mass_order, container, false);



        dateText = (EditText) v.findViewById(R.id.dateText);
        timeText = (EditText) v.findViewById(R.id.timeText);
        addressText = (EditText) v.findViewById(R.id.addressText);

        submitButton = (Button) v.findViewById(R.id.submitToSellerButton);

        //CREATING A SPINNER AND ITEMS STRING ARRAY IS IN res/values/strings.xml NAMED "items"
        spinnerItems = (Spinner) v.findViewById(R.id.spinnerItems);
        ArrayAdapter<String> arrayAdapterItems = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.items));
        arrayAdapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItems.setAdapter(arrayAdapterItems);

        //SPINNER FOR NUMBER OF ITEMS
        spinnerItems = (Spinner) v.findViewById(R.id.numebrOfItems);
        ArrayAdapter<String> arrayAdapterNumber = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.numbers));
        arrayAdapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItems.setAdapter(arrayAdapterNumber);



        //CREATING 2ND SPINNER FOR SELLER AND SELLER ARRAY IS IN res/vales/strings.xml NAMED "seller"
        //MAKE A STRING ARRAY AND REPLACE IT IN R.array.-------
        spinnerSeller = (Spinner) v.findViewById(R.id.spinnerSeller);
        loadSellerSpinner();



        //CREATING 2ND SPINNER FOR SELLER AND SELLER ARRAY IS IN res/vales/strings.xml NAMED "seller"


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date1 = "1234567890";
                date1 = dateText.getText().toString();
                String time1 = timeText.getText().toString();
                String address1 = addressText.getText().toString();
                Pattern DATE_PATTERN = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
                Pattern TIME_PATTERN = Pattern.compile("(1[012]|[1-9]):[0-5][0-9](\\\\s)?(?i)(am|pm)");


                if( !DATE_PATTERN.matcher(date1).matches() ) {
                    Toast.makeText(getActivity(), "DATE FORMAT NOT CORRECT", Toast.LENGTH_SHORT).show();
                }
                else if( !TIME_PATTERN.matcher(time1).matches() )
                {
                    Toast.makeText(getActivity(), "TIME FORMAT NOT CORRECT", Toast.LENGTH_SHORT).show();
                }
                else if(address1.length() == 0)
                {
                    Toast.makeText(getActivity(), "ADDRESS BAR EMPTY", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitToUserButton(v);
                }

            }
        });
        return v;
    }

    void loadSellerSpinner(){
        final ArrayList<String>  sellerList = new ArrayList<String>();
        sellerList.add("something");
//        providerIds.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                            Map<String,Object> map = documentSnapshot.getData();
//                        }
//                    }
//                });
        ArrayAdapter<String> arrayAdapterSeller = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,sellerList);
        arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeller.setAdapter(arrayAdapterSeller);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
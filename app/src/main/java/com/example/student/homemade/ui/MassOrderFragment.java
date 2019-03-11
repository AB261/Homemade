package com.example.student.homemade.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.MainActivity;
import com.example.student.homemade.MassOrderItems;
import com.example.student.homemade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import static android.R.layout.simple_spinner_item;


public class MassOrderFragment extends Fragment {

    View v;
    //MAKING VARIBALES FOR TEXT VIEWS
    TextView submitTextView, headingText;
    EditText dateText, timeText, addressText;
    Button submitButton;
    Spinner spinnerItems, spinnerSeller,spinnerNoOfItems;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference providerIds = db.collection("Provider");
    ArrayList<String> sellerList;
    ImageView addItems;
    int noOfItems = 0;
    ArrayList<String> arrayOfItems = new ArrayList<String>();
    ArrayList<Integer> arrayOfRespectiveAmountOfItemsChoosen = new ArrayList<Integer>();



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

        v = inflater.inflate(R.layout.fragment_mass_order, container, false);


        dateText =  v.findViewById(R.id.dateText);
        timeText = v.findViewById(R.id.timeText);
        addressText = v.findViewById(R.id.addressText);
        submitButton = v.findViewById(R.id.submitToSellerButton);
        spinnerSeller = v.findViewById(R.id.spinnerSeller);
        ListView listView = v.findViewById(R.id.lvItemsAdded);
        addItems = v.findViewById(R.id.btnAddItems);


        MassOrderFragment.CustomAdapter customAdapter = new MassOrderFragment.CustomAdapter();
        listView.setAdapter(customAdapter);


        //CREATING 2ND SPINNER FOR SELLER AND SELLER ARRAY IS IN res/vales/strings.xml NAMED "seller"
        loadSellerSpinner();
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MassOrderItems.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForValidInput();
            }
        });

        return v;
    }


    ///////////////THIS IS ADD PROVIDER NAMES TO SPINNER
    void loadSellerSpinner() {
        sellerList = new ArrayList<String>();
        providerIds.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                Map map = queryDocumentSnapshot.getData();
                                if (map.get("restaurantname") != null) {
                                    sellerList.add((String) map.get("restaurantname"));
                                }

                            }
                        }
                        ArrayAdapter<String> arrayAdapterSeller = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, sellerList);
                        arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSeller.setAdapter(arrayAdapterSeller);
                    }

                });
    }
    ///////////////THIS IS ADD PROVIDER NAMES TO SPINNER


    ////////////////CHECKS IF ENTERED DETAILS ARE RIGHT OR NOT
    public void checkForValidInput() {

        String date1;
        date1 = dateText.getText().toString();
        String time1 = timeText.getText().toString();
        String address1 = addressText.getText().toString();
        Pattern DATE_PATTERN = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
        Pattern TIME_PATTERN = Pattern.compile("(1[012]|[1-9]):[0-5][0-9](\\\\s)?(?i)(am|pm)");


        if (!DATE_PATTERN.matcher(date1).matches()) {
            Toast.makeText(getActivity(), "DATE FORMAT NOT CORRECT", Toast.LENGTH_SHORT).show();
        } else if (!TIME_PATTERN.matcher(time1).matches()) {
            Toast.makeText(getActivity(), "TIME FORMAT NOT CORRECT", Toast.LENGTH_SHORT).show();
        } else if (address1.length() == 0) {
            Toast.makeText(getActivity(), "ADDRESS BAR EMPTY", Toast.LENGTH_SHORT).show();
        } else {
            submitToUserButton(v);
        }


    }
    ////////////////CHECKS IF ENTERED DETAILS ARE RIGHT OR NOT


    //SUBMIT BUTTON IN ACTION
    public int submitToUserButton(View view) {

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
    //SUBMIT BUTTON IN ACTION


    ////////CUSTOM ADAPTER FOR LIST OF ITEMS ADDED
//    class CustomAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return IMAGES.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup parent) {
//            view = getLayoutInflater().inflate(R.layout.trending_items_custom_view,null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);
//            TextView textView_name = (TextView) view.findViewById(R.id.textView_names);
//            TextView textView_description = (TextView) view.findViewById(R.id.textView_description);
//
//            imageView.setImageResource(IMAGES[i]);
//            textView_name.setText(NAMES[i]);
//            textView_description.setText(DESCRIPTION[i]);
//            return view;
//        }
//    }
    ////////CUSTOM ADAPTER FOR LIST OF ITEMS ADDED

    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return noOfItems;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.custom_layout_for_massorder_items,null);
            TextView nameOfItem = view.findViewById(R.id.foodItemsName);
            TextView numberOfItem = view.findViewById(R.id.foodItemsAmount);
            nameOfItem.setText(arrayOfItems.toArray()[i].toString());
            numberOfItem.setText(arrayOfRespectiveAmountOfItemsChoosen.toArray()[i].toString());
            return view;
        }
    }
    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS




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
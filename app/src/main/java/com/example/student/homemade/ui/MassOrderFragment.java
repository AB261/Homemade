package com.example.student.homemade.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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

import com.example.student.homemade.ListOfMassOrderItems;
import com.example.student.homemade.MainActivity;
import com.example.student.homemade.MassOrderItems;
import com.example.student.homemade.R;
import com.example.student.homemade.ViewExistingMassOrders;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.R.layout.simple_spinner_item;


public class MassOrderFragment extends Fragment {

    View v;
    //MAKING VARIBALES FOR TEXT VIEWS
    TextView submitTextView, headingText;
    EditText dateText, timeText, addressText;
    Button submitButton,existingMassOrdersButton;
    Spinner  spinnerSeller;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference providerIds = db.collection("Provider");
    ArrayList<String> sellerList  = new ArrayList<String>();
    String address1,time1,date1;
    Map<String,String> sellerMapContainsNameAndID;
    int totalprice = 0;
    ArrayAdapter<String> arrayAdapterSeller;



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
        submitButton = v.findViewById(R.id.btnNextPage);
        spinnerSeller = v.findViewById(R.id.spinnerSeller);
        existingMassOrdersButton = v.findViewById(R.id.btnToExistingMassOrders);
        sellerMapContainsNameAndID = new HashMap<>();




        //CREATING 2ND SPINNER FOR SELLER AND SELLER ARRAY IS IN res/vales/strings.xml NAMED "seller"
        loadSellerSpinner();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForValidInput();
            }
        });

        existingMassOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewExistingMassOrders.class));
            }
        });

        return v;
    }


    ////////// WHEN CONSUMER CLICKS ON NEXT BUTTON
    void  goToNextPage(){
        Intent intent = new Intent(getActivity(), ListOfMassOrderItems.class);
        String resturant;

        try {
            resturant = spinnerSeller.getSelectedItem().toString();
            intent.putExtra("nameOfResturant", resturant);
            intent.putExtra("date",date1);
            intent.putExtra("time", time1);
            intent.putExtra("address",address1);
            String providerID = sellerMapContainsNameAndID.get(resturant);
            intent.putExtra("providerID",providerID);
            intent.putExtra("totalPrice","0");

        }catch(Exception e){
            Toast.makeText(getActivity(), "NO RESTAURANT SELECTED", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> arrayOfItems = new ArrayList<String>();
        ArrayList<Integer> arrayOfRespectiveAmountOfItemsChoosen = new ArrayList<Integer>();///ARRAY LIST MADE

        intent.putStringArrayListExtra("items",arrayOfItems);
        intent.putIntegerArrayListExtra("amount",arrayOfRespectiveAmountOfItemsChoosen);        //ARRAY LIST PASSED
        startActivity(intent);

    }
    ////////// WHEN CONSUMER CLICKS ON NEXT BUTTON



    ///////////////THIS IS ADD PROVIDER NAMES TO SPINNER
    void loadSellerSpinner() {

        providerIds.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                Map map = queryDocumentSnapshot.getData();

                                if (map.get("restaurantName") != null && map.get("id") != null) {
                                    sellerList.add((String) map.get("restaurantName"));
                                    sellerMapContainsNameAndID.put(map.get("restaurantName").toString(),map.get("id").toString());
                                }



                            }
                            if(getActivity() != null) {
                                arrayAdapterSeller = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1, sellerList);
                                arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSeller.setAdapter(arrayAdapterSeller);
                            }

                        }

                    }

                });
    }
    ///////////////THIS IS ADD PROVIDER NAMES TO SPINNER


    ////////////////CHECKS IF ENTERED DETAILS ARE RIGHT OR NOT
    public void checkForValidInput() {


        date1 = dateText.getText().toString();
        time1 = timeText.getText().toString();
        address1 = addressText.getText().toString();
        Pattern DATE_PATTERN = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
        Pattern TIME_PATTERN = Pattern.compile("(1[012]|[1-9]):[0-5][0-9](\\\\s)?(?i)(\\s)(am|pm)");


        if (!DATE_PATTERN.matcher(date1).matches()) {
            Toast.makeText(getActivity(), "DATE FORMAT NOT CORRECT", Toast.LENGTH_SHORT).show();
        } else if (!TIME_PATTERN.matcher(time1).matches()) {
            Toast.makeText(getActivity(), "TIME FORMAT NOT CORRECT", Toast.LENGTH_SHORT).show();
        } else if (address1.length() == 0) {
            Toast.makeText(getActivity(), "ADDRESS BAR EMPTY", Toast.LENGTH_SHORT).show();
        } else {
            goToNextPage();
        }


    }
    ////////////////CHECKS IF ENTERED DETAILS ARE RIGHT OR NOT




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
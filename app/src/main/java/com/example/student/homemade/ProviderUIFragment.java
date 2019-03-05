package com.example.student.homemade;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProviderUIFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProviderUIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderUIFragment extends Fragment {
    FloatingActionButton add_menu;
    CardView current_orders;
    CardView orders_history;
    CardView reviews;
    private FirebaseFirestore firebaseFirestore;

    private HashMap<String, String> itemPictures = new HashMap<>();

    private FirebaseAuth mAuth;
    public String sellerID = FirebaseAuth.getInstance().getUid();

    TextView Sellername;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProviderUIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderUIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderUIFragment newInstance(String param1, String param2) {
        ProviderUIFragment fragment = new ProviderUIFragment();
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
        firebaseFirestore = FirebaseFirestore.getInstance();

        Log.d("sellerID",sellerID);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sellers_dashboard, container, false);
        Sellername = view.findViewById(R.id.restaurant_name);

//        String S = "L9DYxJQza3OVeWIxlZiE";
        firebaseFirestore.collection("Provider").document(sellerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DOCSNAP", "DocumentSnapshot data: " + document.get("username"));
                        itemPictures = (HashMap<String, String>) document.get("itemPictures");
                        Log.d("SIZE",itemPictures.size()+"");
                        Sellername.setText("Hello " + document.get("username").toString());
                    } else {
                        Log.d("NOOE", "No such document");
                    }
                } else {
                    Log.d("FAIL", "get failed with ", task.getException());
                }
            }
        });


//        Sellername.setText("Hello" + sellername);

        add_menu = view.findViewById(R.id.add_menu);

        add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChooseActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                startActivity(intent);
            }
        });

        current_orders = view.findViewById(R.id.current_orders);

        current_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CurrentOrdersActivity.class);
                startActivity(intent);
            }
        });

        orders_history = view.findViewById(R.id.orders_history);

        orders_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OrdersHistoryActivity.class);
                startActivity(intent);
            }
        });

        reviews = view.findViewById(R.id.reviews);

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ReviewDisplayActivity.class);
                startActivity(intent);
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
}

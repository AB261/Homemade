package com.example.student.homemade.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.R;
import com.example.student.homemade.RatingandReviewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoricalOrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoricalOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricalOrdersFragment extends Fragment {

    View v;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> nameArrayList,dateArrayList,timeArrayList,providerIDArrayList;
    ArrayList<String> nameArrayListFinal,dateArrayListFinal,timeArrayListFinal,providerIDArrayListFinal;

    ArrayList<Double> priceArrayList;
    ArrayList<Double> priceArrayListFinal;
    ListView historyOfOrdersListView;
    Button searchButton;
    EditText nameOfItem;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference ordersRef  = db.collection("Orders");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HistoricalOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoricalOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricalOrdersFragment newInstance(String param1, String param2) {
        HistoricalOrdersFragment fragment = new HistoricalOrdersFragment();
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
        v = inflater.inflate(R.layout.fragment_historical_orders, container, false);

        searchButton = v.findViewById(R.id.btnSearchForHistoryOfOrders);
        nameOfItem = v.findViewById(R.id.etNameOfHistoryOfOrder);
        historyOfOrdersListView = (ListView) v.findViewById(R.id.lvHistory);
        loadHistoryOfOrders();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repopulateTheListView();

            }
        });

        return v;
    }




    void loadHistoryOfOrders(){

        dateArrayList = new ArrayList<>();
        timeArrayList = new ArrayList<>();
        priceArrayList = new ArrayList<>();
        nameArrayList = new ArrayList<>();
        providerIDArrayList = new ArrayList<>();
       // providersNameArrayList = new ArrayList<>();

        ///final array
        dateArrayListFinal = new ArrayList<>();
        timeArrayListFinal = new ArrayList<>();
        priceArrayListFinal = new ArrayList<>();
        nameArrayListFinal = new ArrayList<>();
        providerIDArrayListFinal = new ArrayList<>();
      //  providersNameArrayListFinal = new ArrayList<>();


        ordersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot currentOrderDetails : task.getResult()){

                        Map map =  currentOrderDetails.getData();
                        if( map.get("consumer") != null && map.get("consumer").equals(FirebaseAuth.getInstance().getUid()) ){           ///checking for particular consumer


                            String currDate= "No Date",currTime = "No Time";                                                    //INCASE DATE AND TIME ARE NULL
                            if(  !map.get("orderDate").equals(""))   currDate = map.get("orderDate").toString();
                            if(  !map.get("orderTime").equals(""))    currTime = map.get("orderTime").toString();


                            if((boolean)map.get("delivered") == false){         ///not delivered
                                ArrayList<HashMap> orders = (ArrayList) map.get("itemsOrdered");
                                //ITEM ARE STORED IN A MAP INSIDE A ARRAYLIST SO I'M RETRIEVING THEM USING HASHMAP
                                for(int i=0 ;i<orders.size() ; i++) {
                                    nameArrayList.add(orders.get(i).get("itemName").toString());
                                    dateArrayList.add(currDate);
                                    timeArrayList.add(currTime);
                                    priceArrayList.add( Double.valueOf(String.valueOf(orders.get(i).get("itemCost"))));
                                    if(map.get("provider")!= null){
                                        providerIDArrayList.add(map.get("provider").toString());
                                        }
                                    else {
                                        providerIDArrayList.add("");
                                       // providersNameArrayList.add("The Yellow Chilli");

                                    }

//
                                }
                                /////NOW EVERYORDER RELATED TO THE PARTICULAR LOGINED USER IS STORED IN LIST
                                /////NEXT TASK IS TO PUT EVERYTHING IN LISTVIEW
                            }
                        }
                    }
                    Log.i("something",Integer.toString(providerIDArrayList.size()));
                    //findProvidersName();
                    fillin();



                }
                else{
                    Toast.makeText(getActivity(), "CANNOT DISPLAY ITEMS!", Toast.LENGTH_SHORT).show();
                    nameArrayList.add("NO ITEMS TO DISPLAY BECAUSE OF ERROR IN LOADING");

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        nameArrayList.add("NO ITEMS TO DISPLAY BECAUSE OF ERROR IN LOADING");
                        Toast.makeText(getActivity(), "CANNOT LOAD CURRENT ORDERS!", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    ////////////////////////////////////////TO FIND THE PROVIDERS NAME BY QUERYING AS WE COULDN'T MAKE DATABASE CHANEGS AT THE LAST MOMENT
//   void findProvidersName(){
//
//       final Integer[] count = {0};
//
//        CollectionReference providersCollection = db.collection("Provider");
//
//      // Log.i("FUCK",Integer.toString(providerIDArrayList.size()));
//       //ProviderIDArrayList is ok
//        for(String providerID : providerIDArrayList) {
//            //THIS FOR LOOP IS WORKING FINE
//
//            providersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        Boolean flag = false;
//
//                        for (QueryDocumentSnapshot allProviders : task.getResult()) {
//                            Map m = allProviders.getData();
//
//                            //Toast.makeText(getActivity(),m.get("restaurantName").toString() , Toast.LENGTH_SHORT).show();
//                            if (m.get("id") != null) {
//                                if (m.get("id").toString().equals(providerID)) {
//                                    //Toast.makeText(getActivity(), providerID + "\n" + m.get("id").toString() , Toast.LENGTH_SHORT).show();
//                                    if (m.get("restaurantName") != null) {
//
//                                        Toast.makeText(getActivity(), m.get("restaurantName").toString(), Toast.LENGTH_SHORT).show();
//                                        String temps =  m.get("restaurantName").toString();
//
//                                        providersNameArrayList.add(temps);
//                                        count[0]++;
//
//                                        flag = true;
//                                        break;
//                                    }
//                                }
//
//                            }
//
//                            Log.d("Hey","ID"+String.valueOf(providerIDArrayList.size()) + "count is"  + Integer.toString(count[0]));
//                            Log.d("Hey","Name"+String.valueOf(providersNameArrayList.size()));
//
//
//                        }
//                        if (flag) {
//                            Toast.makeText(getActivity(), "firstFirst", Toast.LENGTH_SHORT).show();
//                            providersNameArrayList.add("The Yellow Chilli");
//                            count[0]++;
//
//                        }
//                    }
//
//                }
//
//
//
//            });
//
//        }
//       if(count[0] >=providerIDArrayList.size())
//       {
//           Log.d("FUCK","ITS FILLED"+"Name"+String.valueOf(providersNameArrayList.size()));
//           fillin();
//       }
//
//
//
//
//   }


    void fillin(){
        /////I'm not changing the originals just the finals to make the loading faster
        for(int i=0;i<nameArrayList.size() ; i++){
            Log.i("seeocn","some");
            nameArrayListFinal.add(nameArrayList.get(i));
            timeArrayListFinal.add(timeArrayList.get(i));
            dateArrayListFinal.add(dateArrayList.get(i));
            priceArrayListFinal.add(priceArrayList.get(i));
            providerIDArrayListFinal.add(providerIDArrayList.get(i));
           // providersNameArrayListFinal.add(providersNameArrayList.get(i));

        }
        OrderAdapter customAdapter = new OrderAdapter();
        historyOfOrdersListView.setAdapter(customAdapter);
    }

    /////////////////////////////////////THIS IS FOR SEARCH BAR
    //////////////I WANTED IT TO LOAD QUICKLY SO I DIDN'T DO QUERY AGAIN AND AGAIN BUT JUST ONCE
    void repopulateTheListView(){
        dateArrayListFinal = new ArrayList<>();
        timeArrayListFinal = new ArrayList<>();
        priceArrayListFinal = new ArrayList<>();
        nameArrayListFinal = new ArrayList<>();
        providerIDArrayListFinal = new ArrayList<>();
       // providersNameArrayListFinal = new ArrayList<>();

        if(nameOfItem.getText().toString().equals(""))
        {

            for(int i=0;i<nameArrayList.size() ; i++){
                nameArrayListFinal.add(nameArrayList.get(i));
                timeArrayListFinal.add(timeArrayList.get(i));
                dateArrayListFinal.add(dateArrayList.get(i));
                priceArrayListFinal.add(priceArrayList.get(i));
                providerIDArrayListFinal.add(providerIDArrayList.get(i));
              //  providersNameArrayListFinal.add(providersNameArrayList.get(i));

            }

            Toast.makeText(getActivity(), "Enter Something", Toast.LENGTH_SHORT).show();
            return;
        }


        String searchedValue = nameOfItem.getText().toString();

        boolean flag = false;
        for(int i=0;i<nameArrayList.size() ; i++){
            if(nameArrayList.get(i).equals(searchedValue)){
                flag = true;
                nameArrayListFinal.add(nameArrayList.get(i));
                timeArrayListFinal.add(timeArrayList.get(i));
                dateArrayListFinal.add(dateArrayList.get(i));
                priceArrayListFinal.add(priceArrayList.get(i));
                providerIDArrayListFinal.add(providerIDArrayList.get(i));
           //     providersNameArrayListFinal.add(providersNameArrayList.get(i));
            }
        }

        if(flag) {
            HistoricalOrdersFragment.OrderAdapter customAdapter = new HistoricalOrdersFragment.OrderAdapter();
            historyOfOrdersListView.setAdapter(customAdapter);
        }
        else{
            Toast.makeText(getActivity(), "Cannot Find the Item", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS

    class OrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return providerIDArrayListFinal.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_view_for_history_of_orders,null);


            TextView name =  view.findViewById(R.id.tvNameOfFood);
            TextView price = view.findViewById(R.id.tvPriceOfFood);
            TextView date = view.findViewById(R.id.tvDateOfFood);
            TextView time = view.findViewById(R.id.tvTimeOfFood);
           // TextView nameOfProvider = view.findViewById(R.id.tvNameOfResturant);
            Button reviewButton = view.findViewById(R.id.btnToReviewsPage);

            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(getActivity(), RatingandReviewActivity.class);
                        intent.putExtra("providerID", providerIDArrayListFinal.get(i));
                        startActivity(intent);
                    }
                    catch (Exception e){

                        Log.i("sending Intent",e.getStackTrace().toString());
                    }
                }
            });
            name.setText(nameArrayListFinal.get(i));
            price.setText(priceArrayListFinal.get(i).toString());
            date.setText(dateArrayListFinal.get(i));
            time.setText(timeArrayListFinal.get(i));
          //  nameOfProvider.setText("Yellow");


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

///some comment to make changes
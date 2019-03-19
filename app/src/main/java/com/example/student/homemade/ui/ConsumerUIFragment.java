package com.example.student.homemade.ui;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ConsumerUIFragment extends Fragment {
    View v;

    Button reviewButton;
    ListView orderList;
    ArrayList<String> nameArrayList,dateArrayList,timeArrayList;
    ArrayList<Double> priceArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserUID = firebaseAuth.getUid();
    CollectionReference ordersRef  = db.collection("Orders");

    public ConsumerUIFragment() {
        // Required empty public constructor
    }


    public static ConsumerUIFragment newInstance() {
        return new ConsumerUIFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FUNCTIONS TO CHANGE LAYOUT ON BUTTON PRESS

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_consumer_ui, container, false);
        orderList =  v.findViewById(R.id.currentOrders);
        reviewButton = v.findViewById(R.id.reviewButton);
        loadCurrentOrders();




//
//        ConsumerUIFragment.CustomAdapter customAdapter = new ConsumerUIFragment();
//        listView.setAdapter(customAdapter);


        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , RatingandReviewActivity.class);
                startActivity(intent);
            }
        });




        return v;
    }

    void loadCurrentOrders(){

        dateArrayList = new ArrayList<>();
        timeArrayList = new ArrayList<>();
        priceArrayList = new ArrayList<>();
        nameArrayList = new ArrayList<>();

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
//                                    Log.i("items", nameArrayList.get(i) + "\t" + dateArrayList.get(i) + "\t" + timeArrayList.get(i) +"\t"
//                                                        + statusArrayList.get(i) + "\t" + Double.toString(priceArrayList.get(i)));
                                }
                                /////NOW EVERYORDER RELATED TO THE PARTICULAR LOGINED USER IS STORED IN LIST
                                /////NEXT TASK IS TO PUT EVERYTHING IN LISTVIEW
                            }
                        }
                    }
                    ConsumerUIFragment.OrderAdapter customAdapter = new ConsumerUIFragment.OrderAdapter();
                    orderList.setAdapter(customAdapter);
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

    class OrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nameArrayList.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_list_view_for_orders,null);


            TextView name =  view.findViewById(R.id.tvNameOfFood);
            TextView price = view.findViewById(R.id.tvPriceOfFood);
            TextView date = view.findViewById(R.id.tvDateOfFood);
            TextView time = view.findViewById(R.id.tvTimeOfFood);


            name.setText(nameArrayList.get(i));
            price.setText(priceArrayList.get(i).toString());
            date.setText(dateArrayList.get(i));
            time.setText(timeArrayList.get(i));

            return view;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}

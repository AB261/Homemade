package com.example.student.homemade.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



//LOGIC IS TO SHOW EVERYTHING HAVING FREQ > 2
//FIRST PUT ALL THE ITEMS EVER ORDERED IN THE NAME AND PRICE ARRAY LIST
//THEN PUT ALL OF THEM IN MAP<STRING,(PRICE,FREQ)> TO CALCULATE FREQ
//AGAIN PUT EVERYTHING IN NAME AND PRICE ARRAYLIST
//DISPLAY IT USING CUSTOM ADAPTER

public class TrendingItemsFragment extends Fragment {
    View v;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> nameArrayList,dateArrayList,timeArrayList;
    ArrayList<Double> priceArrayList;
    ListView historyOfOrdersListView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference ordersRef  = db.collection("Orders");




    public TrendingItemsFragment() {
        // Required empty public constructor
    }

    public static TrendingItemsFragment newInstance() {

        return new TrendingItemsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_trending_items, container, false);

         historyOfOrdersListView = v.findViewById(R.id.lvTrending);
        loadHistoryOfOrders();
        return v;

    }

    void loadHistoryOfOrders(){

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


                            String currDate= "",currTime = "";                                                    //INCASE DATE AND TIME ARE NULL


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

                    removeSameOccurancesOfItemsToCountFreq();

                    TrendingItemsFragment.OrderAdapter customAdapter = new TrendingItemsFragment.OrderAdapter();
                    historyOfOrdersListView.setAdapter(customAdapter);
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

    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS



    /////////////////////REMOVES SAME OCCURANCES OF ITEMS
    void removeSameOccurancesOfItemsToCountFreq() {

        //Toast.makeText(this, Integer.toString(arrayOfItems.size()), Toast.LENGTH_SHORT).show();
        Map<String,priceAndFreq> mapFreq = new HashMap<>();  //object will have freq and price
        for(int i=0; i<nameArrayList.size();i++){            ///THE ITEMS WHICH CAME FROM THE ADD PAGE ARE
            priceAndFreq j = null;
            try {
                j = mapFreq.get(nameArrayList.get(i));
            }catch (Exception e){}

            if( j == null){
                j = new priceAndFreq(priceArrayList.get(i) , 1);
            }
            else{
                j.setFreq(j.getFreq() + 1);
            }


            mapFreq.put(nameArrayList.get(i), j );             ///IF ITEM EXIST ADD NUMBER OF ITEMS + 1 else NUMBER OF ITEMS
            ///////IF ITEM ALREADY EXIST INCREASE IT'S FREQUENCY

        }



        ///////COPIED EVERYTHING TO MAP

        priceArrayList = new ArrayList<Double>();
        nameArrayList = new ArrayList<String>();
        /////MAKE NEW ARRAY LIST AND COPY CONTENTS OF MAP INTO IT

        for (Map.Entry<String,priceAndFreq> entry : mapFreq.entrySet()){
            if(entry.getValue().getFreq() > 2)
            nameArrayList.add(entry.getKey());
            priceArrayList.add(entry.getValue().getPrice());

        }

    }
    /////////////////////REMOVES SAME OCCURANCES OF ITEMS

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
            ImageView calanderImage = view.findViewById(R.id.ivDate);
            ImageView clockImage = view.findViewById(R.id.ivTime);
            calanderImage.setVisibility(View.GONE);
            clockImage.setVisibility(View.GONE);

            name.setText(nameArrayList.get(i));
            price.setText(priceArrayList.get(i).toString());
            date.setText(dateArrayList.get(i));
            time.setText(timeArrayList.get(i));

            return view;
        }
    }

    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS



    ///THIS IS TO HANDLE FREQ OF EACH ITEM AND ITS PRICE
    class priceAndFreq{
        Double price;
        Integer freq;

        public priceAndFreq(Double price, Integer freq) {
            this.price = price;
            this.freq = freq;
        }

        public priceAndFreq() {
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getFreq() {
            return freq;
        }

        public void setFreq(Integer freq) {
            this.freq = freq;
        }
    }
    ///THIS IS TO HANDLE FREQ OF EACH ITEM AND ITS PRICE

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

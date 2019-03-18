package com.example.student.homemade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.ui.ConsumerDetailsFragment;
import com.example.student.homemade.ui.ConsumerUIFragment;
import com.example.student.homemade.ui.MassOrderFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//////intent IS NAME OF INTENT GETTING VALUES VIA INTENT
////THE VALUES ARE  1.ARRAY OF ITEMS ,2.ARRAY OF PRICES , 3.RESTAURANT NAME ,4.DATE TIME ADDRESS AND PROVIDER ID

public class ListOfMassOrderItems extends AppCompatActivity implements Serializable {


    ArrayList<Integer> arrayOfRespectiveAmountOfItemsChoosen = new ArrayList<Integer>();
    ArrayList<String> arrayOfItems = new ArrayList<String>();
    int noOfItems = 0;
    ImageView addItems;
    ListView listView;
    String resturantName,date,time,address,providerID;
    Intent intent;
    String item1,item2,item3,item4;
    int amount1,amount2,amount3,amount4;
    CustomAdapter customAdapter;
    Button submitButton;
    CollectionReference massOrderRef;
    DocumentReference providerRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_mass_order_items);

        listView = findViewById(R.id.lvItemsAdded);
        addItems = findViewById(R.id.btnAddItems);
        submitButton = findViewById(R.id.btnSubmitToSeller);

        intent = getIntent();
        resturantName = intent.getStringExtra("nameOfResturant");           //GETTING ARRAY LIST FROM INTENT
        arrayOfItems = intent.getStringArrayListExtra("items");
        arrayOfRespectiveAmountOfItemsChoosen = intent.getIntegerArrayListExtra("amount");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        address = intent.getStringExtra("address");
        providerID = intent.getStringExtra("providerID");
        massOrderRef = FirebaseFirestore.getInstance().collection("Mass Orders");
        providerRef = massOrderRef.document(providerID);

        getAllDetailsAndPutInListView();


        ////////////GOING TO ADD ITEMS PAGE
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSend = new Intent(ListOfMassOrderItems.this, MassOrderItems.class);
                intentSend.putExtra("nameOfResturant", resturantName);
                intentSend.putStringArrayListExtra("items",arrayOfItems);
                intentSend.putIntegerArrayListExtra("amount",arrayOfRespectiveAmountOfItemsChoosen);              //PUTTING ARRAY LIST IN INTENT
                intentSend.putExtra("date", date);
                intentSend.putExtra("address", address);
                intentSend.putExtra("time", time);
                intentSend.putExtra("providerID", providerID);

                startActivity(intentSend);
            }
        });
        ////////////GOING TO ADD ITEMS PAGE

        ////WHEN FINAL SUBMIT BUTTON IS PRESSED
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> massOrder = new HashMap<>();
                massOrder.put("orderDate",date);
                massOrder.put("orderTime",time);
                massOrder.put("address",address);
                massOrder.put("resturantName",resturantName);
                massOrder.put("Consumer", FirebaseAuth.getInstance().getUid());
                massOrder.put("provider",providerID);
                massOrder.put("delivered",false);
                massOrder.put("paid",false);
                Map<String,Integer> orderItemsMap = new HashMap<>();
                for(int i=0 ;i < arrayOfItems.size() ; i++){
                    orderItemsMap.put(arrayOfItems.get(i) , arrayOfRespectiveAmountOfItemsChoosen.get(i));
                }
                massOrder.put("orderItems",orderItemsMap);
                providerRef.set(massOrder)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                try{
                                    ConsumerUIFragment fragment = new ConsumerUIFragment();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment)
                                            .commit();
                                }
                                catch (Exception e){
                                    Log.i("jump",e.toString());
                                }
                                Toast.makeText(ListOfMassOrderItems.this, "SUCCESSFULLY ORDER PLACED", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListOfMassOrderItems.this, "SORRY ORDER COULD NOT BE PLACED", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



    }


    void getAllDetailsAndPutInListView(){

        removeSameOccurancesOfItems();
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

    }


    /////////////////////REMOVES SAME OCCURANCES OF ITEMS
    void removeSameOccurancesOfItems() {

        //Toast.makeText(this, Integer.toString(arrayOfItems.size()), Toast.LENGTH_SHORT).show();
        Map<String,Integer> map = new HashMap<String, Integer>();
        for(int i=0; i<arrayOfItems.size();i++){            ///THE ITEMS WHICH CAME FROM THE ADD PAGE ARE
            Integer j = map.get(arrayOfItems.get(i));
            map.put(arrayOfItems.get(i), (j == null) ? arrayOfRespectiveAmountOfItemsChoosen.get(i) : j + arrayOfRespectiveAmountOfItemsChoosen.get(i));
            ///////IF ITEM ALREADY EXIST INCREASE IT'S FREQUENCY

        }

        ///////COPIED EVERYTHING TO MAP

        arrayOfRespectiveAmountOfItemsChoosen = new ArrayList<Integer>();
        arrayOfItems = new ArrayList<String>();
        /////MAKE NEW ARRAY LIST AND COPY CONTENTS OF MAP INTO IT
        for (Map.Entry<String,Integer> entry : map.entrySet()){
            arrayOfItems.add(entry.getKey());
            arrayOfRespectiveAmountOfItemsChoosen.add(entry.getValue());

        }

    }
    /////////////////////REMOVES SAME OCCURANCES OF ITEMS


    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS

    class CustomAdapter extends BaseAdapter {

        public CustomAdapter(){}

        @Override
        public int getCount() {
            return arrayOfItems.size();
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
            TextView nameOfFoodItems = view.findViewById(R.id.foodItemsName);
            TextView amountOfFoodItems = view.findViewById(R.id.foodItemsAmount);


            nameOfFoodItems.setText(arrayOfItems.get(i));
            amountOfFoodItems.setText(arrayOfRespectiveAmountOfItemsChoosen.get(i).toString());
            return view;
        }
    }
    /////////////////CUSTOM ADADPTER FOR LIST OF CURRENT ITEMS


}

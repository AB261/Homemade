package com.example.student.homemade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.student.homemade.ui.MassOrderFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MassOrderItems extends AppCompatActivity implements Serializable {

    Spinner breakfastSpinner, lunchSpinner, snacksSpinner, dinnerSpinner;
    EditText amountBreakfast, amountLunch, amountSnacks, amountDinner;
    ArrayList<String> breakfastList, lunchList, snacksList, dinnerList;
    DocumentReference breakfastCollection, lunchCollection, snacksCollection, dinnerCollection;
    ArrayList<Integer> arrayOfRespectiveAmountOfItemsChoosen;
    ArrayList<String> arrayOfItems;             ///////ARRAY LIST DECLARED
    Button saveItems;
    String nameOfResturant,date,time,address,providerID;  ///GETTING THESE THINGS VIA INTENT
    String item1,item2,item3,item4;
    int amount1,amount2,amount3,amount4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_order_items);



//        amountBreakfast = findViewById(R.id.etBreakfast);
//        amountLunch = findViewById(R.id.etLunch);
//        amountSnacks = findViewById(R.id.etSnacks);
//        amountDinner = findViewById(R.id.etDinner);
//        breakfastSpinner = findViewById(R.id.spinnerBreakfast);
//        lunchSpinner = findViewById(R.id.spinnerLunch);
//        snacksSpinner = findViewById(R.id.spinnerSnacks);
//        dinnerSpinner = findViewById(R.id.spinnerDinner);
        saveItems= findViewById(R.id.btnAddItems);
        arrayOfRespectiveAmountOfItemsChoosen = new ArrayList<Integer>();
        arrayOfItems = new ArrayList<String>();

        //setTitle("Selection Page");
        Intent intentGet = getIntent();
        nameOfResturant = intentGet.getStringExtra("nameOfResturant");
        arrayOfItems = intentGet.getStringArrayListExtra("items");             ///ARRAY LIST INITIALIZED
        arrayOfRespectiveAmountOfItemsChoosen = intentGet.getIntegerArrayListExtra("amount");
        date = intentGet.getStringExtra("date");
        time = intentGet.getStringExtra("time");
        address = intentGet.getStringExtra("address");
        providerID = intentGet.getStringExtra("providerID");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference providerIds = db.collection("Provider").document(providerID).collection("menu");
        breakfastCollection = providerIds.document("Breakfast");
        lunchCollection = providerIds.document("Lunch");
        snacksCollection = providerIds.document("Snacks");
        dinnerCollection = providerIds.document("Dinner");

        populateBreakfast();
        populateLunch();
        populateSnacks();
        populateDinner();

        saveItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    item1 = breakfastSpinner.getSelectedItem().toString();
                }catch(Exception e){}

                try {
                    item2 = lunchSpinner.getSelectedItem().toString();
                }catch (Exception e){}

                try {
                    item3 = snacksSpinner.getSelectedItem().toString();
                }catch (Exception e){}

                try {
                    item4 = dinnerSpinner.getSelectedItem().toString();
                }catch (Exception e){}

                sendItemDetailsToMassOrderFragment();

            }
        });

    }


    void sendItemDetailsToMassOrderFragment(){

        if(checkforallfields() == false){
            Toast.makeText(this, "SOME FIELDS ARE EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }

        try {


            amount1 = Integer.parseInt(amountBreakfast.getText().toString());
            amount2 = Integer.parseInt(amountLunch.getText().toString());
            amount3 = Integer.parseInt(amountSnacks.getText().toString());
            amount4 = Integer.parseInt(amountDinner.getText().toString());

//            intent.putExtra("breakfast", item1);
//            intent.putExtra("lunch", item2);
//            intent.putExtra("snacks", item3);
//            intent.putExtra("dinner", item4);
//            intent.putExtra("amount1", amount1);
//            intent.putExtra("amount2", amount2);
//            intent.putExtra("amount3", amount3);
//            intent.putExtra("amount4", amount4);
            if(amount1 != 0 && item1 != null) {
                arrayOfItems.add(item1);
                arrayOfRespectiveAmountOfItemsChoosen.add(amount1);
            }

            if(amount2 != 0 && item2 != null) {
                arrayOfItems.add(item2);
                arrayOfRespectiveAmountOfItemsChoosen.add(amount2);
            }

            if(amount3 != 0 && item3 != null) {
                arrayOfItems.add(item3);
                arrayOfRespectiveAmountOfItemsChoosen.add(amount3);
            }

            if(amount4 != 0 && item4 != null) {
                arrayOfItems.add(item4);
                arrayOfRespectiveAmountOfItemsChoosen.add(amount4);        ////ALL ITEMS ADDED TO ARRAY AND NOW ARRAY WILL BE PASSED AS INTENT
            }


        }catch(Exception e){

            Log.e("fucked_up", "I got an error", e);
            Toast.makeText(MassOrderItems.this, "NO ITEM SELECTED IN SOME FIELDS", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentSend = new Intent(MassOrderItems.this, ListOfMassOrderItems.class);
        intentSend.putExtra("nameOfResturant",nameOfResturant);
        intentSend.putStringArrayListExtra("items",arrayOfItems);
        intentSend.putIntegerArrayListExtra("amount",arrayOfRespectiveAmountOfItemsChoosen);
        intentSend.putExtra("date", date);
        intentSend.putExtra("address", address);
        intentSend.putExtra("time", time);
        intentSend.putExtra("providerID", providerID);

        startActivity(intentSend);
        finish();

    }


    ///////////////CHECKING IF ALL FIELDS ARE FILLED OR NOT
    boolean checkforallfields(){
        if(amountBreakfast.getText().toString().matches("")) return false;
        if(amountLunch.getText().toString().matches("")) return false;
        if(amountSnacks.getText().toString().matches("")) return false;
        if(amountDinner.getText().toString().matches("")) return false;
        return true;

    }
    ////////////////////////TO POPULATE THE SPINNER
    void populateBreakfast() {
        breakfastList = new ArrayList<String>();
        breakfastCollection.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentM = task.getResult();
                    if (documentM.exists()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) documentM.getData().get("items");
                        for (String key : map.keySet()) {
                            breakfastList.add(key);
                        }

                        ArrayAdapter<String> arrayAdapterSeller = new ArrayAdapter<String>(MassOrderItems.this, android.R.layout.simple_list_item_1, breakfastList);
                        arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        breakfastSpinner.setAdapter(arrayAdapterSeller);


                    } else {
                        Log.i("dammit", "document does not exist");
                    }



                }else{
                    Toast.makeText(MassOrderItems.this, "TASK NOT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MassOrderItems.this, "BREAKFAST MENU DOES NOT EXIST", Toast.LENGTH_SHORT).show();
                breakfastSpinner.setVisibility(View.GONE);
                amountBreakfast.setVisibility(View.GONE);
            }
        });

    }
    ////////////////////////TO POPULATE THE SPINNER



    ////////////////////////TO POPULATE THE SPINNER
    void populateLunch() {

        lunchList = new ArrayList<String>();
        lunchCollection.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentM = task.getResult();
                    if (documentM.exists()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) documentM.getData().get("items"); //GETS THE HASHMAP CONTAINING ITEMS
                        for (String key : map.keySet()) {
                            lunchList.add(key);
                        }
                        ArrayAdapter<String> arrayAdapterSeller = new ArrayAdapter<String>(MassOrderItems.this, android.R.layout.simple_list_item_1, lunchList);
                        arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        lunchSpinner.setAdapter(arrayAdapterSeller);
                    } else {
                        Log.i("dammit", "document does not exist1");
                    }



                }else{
                    Toast.makeText(MassOrderItems.this, "TASK NOT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MassOrderItems.this, "LUNCH MENU DOES NOT EXIST", Toast.LENGTH_SHORT).show();
                lunchSpinner.setVisibility(View.GONE);
                amountLunch.setVisibility(View.GONE);
            }
        });

    }
    ////////////////////////TO POPULATE THE SPINNER




    ////////////////////////TO POPULATE THE SPINNER
    void populateSnacks() {

        snacksList = new ArrayList<String>();
        snacksCollection.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentM = task.getResult();
                    if (documentM.exists()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) documentM.getData().get("items");
                        for (String key : map.keySet()) {
                            snacksList.add(key);
                        }
                        ArrayAdapter<String> arrayAdapterSeller = new ArrayAdapter<String>(MassOrderItems.this, android.R.layout.simple_list_item_1, snacksList);
                        arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        snacksSpinner.setAdapter(arrayAdapterSeller);
                    } else {
                        Log.i("dammit", "document does not exist2");
                    }



                }else{
                    Toast.makeText(MassOrderItems.this, "TASK NOT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    snacksSpinner.setVisibility(View.GONE);
                    amountSnacks.setVisibility(View.GONE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MassOrderItems.this, "SNACKS MENU DOES NOT EXIST", Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////TO POPULATE THE SPINNER



    ////////////////////////TO POPULATE THE SPINNER
    void populateDinner() {

        dinnerList = new ArrayList<String>();
        dinnerCollection.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentM = task.getResult();
                    if (documentM.exists()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) documentM.getData().get("items");
                        for (String key : map.keySet()) {
                            dinnerList.add(key);
                        }
                        ArrayAdapter<String> arrayAdapterSeller = new ArrayAdapter<String>(MassOrderItems.this, android.R.layout.simple_list_item_1, dinnerList);
                        arrayAdapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dinnerSpinner.setAdapter(arrayAdapterSeller);
                    } else {
                        Log.i("dammit", "document does not exist3");
                    }



                }else{
                    Toast.makeText(MassOrderItems.this, "TASK NOT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MassOrderItems.this, "DINNER MENU DOES NOT EXIST", Toast.LENGTH_SHORT).show();
                dinnerSpinner.setVisibility(View.GONE);
                amountDinner.setVisibility(View.GONE);
            }
        });

    }
    ////////////////////////TO POPULATE THE SPINNER


}
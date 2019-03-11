package com.example.student.homemade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CustomerConfirmActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    final String consumerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//////////////////////////////////////////////////////////////////////////////////////////////////////
//    double orderTotal = 456.05;
//    final String providerID = "vMR09oO90SbUtCapURrudg5QMlw2";
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////


    private static final String TAG = "CUSTOMERCONFIRMACTIVITY";
//    final boolean isMassOrder = true;
//    final double discountMassOrder = 23.4;
//    double discountLongTerm = 46.7;
    final double deliveryCharges = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_confirm);
        FirebaseApp.initializeApp(this);

        Intent intent = getIntent();
        final String doc = intent.getStringExtra("docRef");
        Log.d("docRef",doc);
        final DocumentReference docRef = db.collection("Orders").document(doc);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.d("newnew","In here");
                Log.d("newnew",documentSnapshot.getData().toString());
                final Map<String, Object> order = documentSnapshot.getData();
                final String providerID = order.get("provider").toString();
                final DocumentReference provider = db.collection("Provider").document(providerID);
                final String consumerID = order.get("consumer").toString();
                Log.d("consumerID",consumerID);
                final double orderTotal1 = Double.parseDouble(order.get("orderTotal").toString());
                final boolean isMassOrder = Boolean.parseBoolean(order.get("isMassOrder").toString());

                provider.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("newnew","In provider");
                        Map<String,Object> data = documentSnapshot.getData();
                        final Double discountLongTerm = Double.parseDouble(data.get("longTermSubscriptionDiscount").toString());
                        final Double discountMassOrder = Double.parseDouble(data.get("massOrderDiscount").toString());

                        db.collection("Consumer").document(consumerID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Button lastButton = findViewById(R.id.btnlast);
                                TextView walletCost = findViewById(R.id.walletCost);
                                TextView totalCost = findViewById(R.id.totalCost);
                                TextView statusInfo = findViewById(R.id.statusInfo);

                                Log.d("newnew","In consumer");
                                final HashMap<String, Object> map = (HashMap<String, Object>)documentSnapshot.getData();
                                final boolean isSubscriber = false;
                                walletCost.setText(map.get("wallet").toString());
                                final Double orderTotal = orderTotal1 - (isSubscriber?discountLongTerm*orderTotal1*0.01:0) - (isMassOrder?orderTotal1*0.01*discountMassOrder:0) + deliveryCharges ;
                                totalCost.setText(orderTotal + "");
                                statusInfo.setText("Please proceed to make the payment");
                                if(Double.parseDouble(map.get("wallet").toString()) > orderTotal) {
                                    lastButton.setText("Pay");
                                    lastButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d(TAG,"working till here for more case");
                                            docRef.update("paid",true);
                                            Date currentTime = Calendar.getInstance().getTime();
                                            docRef.update("orderTime",currentTime.toString());
                                            db.collection("Consumer").document(consumerID).update("wallet", Double.parseDouble(map.get("wallet").toString()) - orderTotal);
                                            db.collection("Provider").document(providerID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    db.collection("Provider").document(providerID).update("wallet",Double.parseDouble(documentSnapshot.get("wallet").toString()) + orderTotal);
                                                    Log.d(TAG,"provider updated successfully");
                                                }
                                            });

                                        }
                                    });
                                }else{
                                    statusInfo.setText("OOPS!! You don't have enough balance to make the payment");
                                    lastButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d(TAG, "Working properly for the less amount case add the code later");
                                        }
                                    });
                                }

                            }
                        });

//                        db.collection("Consumer").whereEqualTo("id", consumerID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                Button lastButton = findViewById(R.id.btnlast);
//                                TextView walletCost = findViewById(R.id.walletCost);
//                                TextView totalCost = findViewById(R.id.totalCost);
//                                TextView statusInfo = findViewById(R.id.statusInfo);
//
//
//                                if (task.isSuccessful()) {
//                                    for (final QueryDocumentSnapshot document : task.getResult()) {
//                                        Log.d("newnew","In consumer");
//                                        final HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
//                                        final String docID = document.getId();
//                                        final boolean isSubscriber = (Boolean)map.get("isSubscriber");
//                                        walletCost.setText(map.get("wallet").toString());
//                                        final Double orderTotal = orderTotal1 - (isSubscriber?discountLongTerm*orderTotal1*0.01:0) - (isMassOrder?orderTotal1*0.01*discountMassOrder:0) + deliveryCharges ;
//                                        totalCost.setText(orderTotal + "");
//                                        statusInfo.setText("Please proceed to make the payment");
//                                        if(Double.parseDouble(map.get("wallet").toString()) > orderTotal){
//                                            lastButton.setText("Pay");
//                                            lastButton.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    Log.d(TAG,"working till here for more case");
////                                    db.collection("Orders").add(new );
//                                                    db.collection("Consumer").document(docID).update("wallet", Double.parseDouble(map.get("wallet").toString()) - orderTotal);
//                                                    db.collection("Provider").whereEqualTo("id", providerID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                            for(QueryDocumentSnapshot documentP : task.getResult()){
//                                                                db.collection("Provider").document(documentP.getId()).update("wallet",Double.parseDouble(documentP.getData().get("wallet").toString()) + orderTotal);
//                                                                Log.d(TAG,"provider updated successfully");
//                                                            }
//                                                        }
//                                                    });
//
//                                                }
//                                            });
//                                        }else{
//                                            statusInfo.setText("OOPS!! You don't have enough balance to make the payment");
//                                            lastButton.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    Log.d(TAG, "Working properly for the less amount case add the code later");
//                                                }
//                                            });
//                                        }
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                           }
//                        });
                    }
                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("newnew","FAILURE");
                    }
                });


    }
}
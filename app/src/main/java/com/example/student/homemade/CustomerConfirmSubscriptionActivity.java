package com.example.student.homemade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomerConfirmSubscriptionActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "SUBSCRIPTIONACTIVITY";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_confirm_subscription);
        FirebaseApp.initializeApp(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confirm Subscription");
        Intent intent = getIntent();

        final String time = intent.getStringExtra("subscriptionTime");
        final String consumerID = intent.getStringExtra("consumerID");
        final String providerID = intent.getStringExtra("providerID");
        final double costPerMonth =  150;

//        Log.d("consumerID",consumerID);
//        Log.d("providerID",providerID);
//        Log.d("subscriptionTime",time);

        TextView totalCostS = findViewById(R.id.totalCostS);
        final double costSub = (Integer.parseInt(time) * costPerMonth);
        Log.d("costSub",costSub + "");
        if(totalCostS == null){
            Log.d("ido","null");
        }
        Log.d("ido",totalCostS.toString());
        totalCostS.setText( costSub + "");

        db.collection("Consumer").document(consumerID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object>  map = documentSnapshot.getData();
                final double wallet = Double.parseDouble(map.get("wallet").toString());
                TextView walletCostS = findViewById(R.id.walletCostS);
                walletCostS.setText(Math.round(wallet) + "");

                if(wallet < costSub){
                    TextView status = findViewById(R.id.statusInfoS);
                    status.setText("Sorry not enough funds in your wallet");

                    Button lastBtnS = findViewById(R.id.btnlastS);
                    lastBtnS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(CustomerConfirmSubscriptionActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    TextView status = findViewById(R.id.statusInfoS);
                    status.setText("Please proceed to pay");



                    Button lastBtnS = findViewById(R.id.btnlastS);
                    lastBtnS.setText("Pay");

                    lastBtnS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.collection("Provider").document(providerID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    double currMoney = Double.parseDouble(documentSnapshot.get("wallet").toString());
                                    Log.d(TAG,currMoney + "");
                                    db.collection("Provider").document(providerID).update("wallet",currMoney + costSub);
                                }
                            });

                            db.collection("Consumer").document(consumerID).update("wallet",wallet + costSub);

                            Toast.makeText(getApplicationContext(),"Ordered placed successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CustomerConfirmSubscriptionActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });

                }

            }
        });

    }

}

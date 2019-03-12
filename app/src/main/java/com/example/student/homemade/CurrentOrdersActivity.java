package com.example.student.homemade;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentOrdersActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static final String TAG = "CURRENTORDERSACTIVITY";
    private CurrentOrdersRecyclerViewAdapter recyclerViewAdapter;
    //vars
    private ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        Log.d(TAG, "onCreate: started.");
        mAuth = FirebaseAuth.getInstance();
        //PLEASE PASS provider ID to this activity from login page
//        int myproviderID = 13;
        //PLEASE PASS provider ID to this activity from login page
        String myProviderId = mAuth.getUid();



        final RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new CurrentOrdersRecyclerViewAdapter(this, orderInfos);
        recyclerView.setAdapter(recyclerViewAdapter);

//        DocumentReference docRef = db.collection("Orders").document(myproviderIDString);
//        Query docRef = db.collection("Orders").whereEqualTo("Provider",myproviderID);
        db.collection("Orders").whereEqualTo("provider", myProviderId).whereEqualTo("delivered", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                OrderInfo orderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d(TAG, "client id :" + map.get("provider"));
                        Log.d(TAG, "client id :" + map.get("consumer"));
                        Log.d(TAG, "client id :" + map.get("completed"));
                        Log.d(TAG, "client id :" + map.get("delivered"));
                        Log.d(TAG, "client id :" + map.get("deliveryPerson"));
                        Log.d(TAG, "client id :" + map.get("isMassOrder"));
                        Log.d(TAG, "client id :" + map.get("paid"));
                        Log.d(TAG, "client id :" + map.get("orderTotal"));
                        Log.d(TAG, "client id :" + map.get("itemsOrdered"));
                        Log.d(TAG, "client id :" + map.get("orderDate"));
                        Log.d(TAG, "client id :" + map.get("orderTime"));
                        orderInfo = new OrderInfo(map.get("provider").toString(), map.get("consumer").toString(), (Boolean)map.get("completed"), (Boolean)map.get("delivered"), map.get("deliveryPerson").toString(),(Boolean)map.get("isMassOrder"), (Boolean)map.get("paid"), (ArrayList<HashMap<String,Object>>) map.get("itemsOrdered"), (double)map.get("orderTotal"),map.get("orderTime").toString(), map.get("orderDate").toString());
//                        orderInfo = document.toObject(OrderInfo.class);
//                        Log.d("ORDERINFO",orderInfo.toString());

                        recyclerViewAdapter.added(orderInfo);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }
}

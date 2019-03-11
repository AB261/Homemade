package com.example.student.homemade;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdersHistoryActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ORDERSHISTORYACTIVITY";
    private OrdersHistoryRecyclerViewAdapter ordersHistoryRecyclerViewAdapter;
    //vars
    private ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);
        Log.d(TAG, "onCreate: started.");

        //PLEASE PASS provider ID to this activity from login page
        int myproviderID = 13;
        //PLEASE PASS provider ID to this activity from login page

        final RecyclerView recyclerView = findViewById(R.id.orders_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersHistoryRecyclerViewAdapter = new OrdersHistoryRecyclerViewAdapter(this, orderInfos);
        recyclerView.setAdapter(ordersHistoryRecyclerViewAdapter);

        db.collection("Orders").whereEqualTo("Provider",myproviderID).whereEqualTo("delivered", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                OrderInfo orderInfo;
                Log.d(TAG,"Entered");
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d(TAG, "client id :" + map.get("client"));
                        orderInfo = new OrderInfo(Integer.parseInt(map.get("Provider").toString()), Integer.parseInt(map.get("client").toString()), (Boolean) map.get("completed"), (Boolean) map.get("delivered"), Integer.parseInt(map.get("delivery_person").toString()), Integer.parseInt(map.get("orderID").toString()), (Boolean) map.get("paid"), (ArrayList) map.get("things_ordered"), (String) map.get("time_and_date"), Integer.parseInt(map.get("timeBeforeCancel").toString()), Float.parseFloat(map.get("total_cost").toString()));
                        ordersHistoryRecyclerViewAdapter.added(orderInfo);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}


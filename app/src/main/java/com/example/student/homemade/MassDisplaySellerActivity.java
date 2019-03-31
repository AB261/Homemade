
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

public class MassDisplaySellerActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static final String TAG = "MASSORDERSDISPLAY";
    private MassOrdersDisplaySellerRecyclerViewAdapter recyclerViewAdapter;
    //vars
    private ArrayList<MassOrderInfo> massOrderInfos = new ArrayList<MassOrderInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_display_seller);
        Log.d(TAG, "onCreate: started.");
        mAuth = FirebaseAuth.getInstance();
        String myProviderId = mAuth.getUid();



        final RecyclerView recyclerView = findViewById(R.id.mass_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new MassOrdersDisplaySellerRecyclerViewAdapter(this, massOrderInfos);
        recyclerView.setAdapter(recyclerViewAdapter);

//        DocumentReference docRef = db.collection("Orders").document(myproviderIDString);
//        Query docRef = db.collection("Orders").whereEqualTo("Provider",myproviderID);
        db.collection("Mass Orders").whereEqualTo("provider", myProviderId).whereEqualTo("delivered", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                MassOrderInfo massOrderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d(TAG, "client id :" + map.get("address"));
                        Log.d(TAG, "client id :" + map.get("Consumer"));
                        Log.d(TAG, "client id :" + map.get("delivered"));
                        Log.d(TAG, "client id :" + map.get("paid"));
                        Log.d(TAG, "client id :" + map.get("resturantName"));
                        Log.d(TAG, "client id :" + map.get("orderItems"));
                        Log.d(TAG, "client id :" + map.get("orderDate"));
                        Log.d(TAG, "client id :" + map.get("orderTime"));
                        Log.d(TAG, "client id :" + map.get("provider"));

                        massOrderInfo = new MassOrderInfo(map.get("Consumer").toString(), map.get("address").toString(), (Boolean)map.get("delivered"), map.get("orderDate").toString(),map.get("orderTime").toString(), (HashMap<String,Object>)map.get("orderItems"), (Boolean) map.get("paid"),map.get("provider").toString(), map.get("resturantName").toString());
//                        orderInfo = document.toObject(OrderInfo.class);
//                        Log.d("ORDERINFO",orderInfo.toString());

                        recyclerViewAdapter.added(massOrderInfo);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }
}

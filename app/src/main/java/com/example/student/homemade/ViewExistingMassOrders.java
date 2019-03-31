package com.example.student.homemade;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.view.View.GONE;

public class ViewExistingMassOrders extends AppCompatActivity {
    ListView massOrderHistoryListView;
    ArrayList<String> nameOfProviderArrayList,foodItemsArrayList,deliveryStatusArrayList;
    Integer sizeOfArrayList = 0;
    TextView noItemsToDisplayText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference massOrdersRef  = db.collection("Mass Orders");
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_existing_mass_orders);
        massOrderHistoryListView = findViewById(R.id.lvMassOrderItemsOrdered);
        loadMassOrders();

    }

    void loadMassOrders(){
        nameOfProviderArrayList = new ArrayList<>();
        foodItemsArrayList = new ArrayList<>();
        deliveryStatusArrayList = new ArrayList<>();
        noItemsToDisplayText = findViewById(R.id.tvNoItemsDisplayText);
        massOrdersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot massOrdersList : task.getResult()){

                        Map m = massOrdersList.getData();

                        if(m.get("Consumer").toString().equals(FirebaseAuth.getInstance().getUid())){

                           Map foodItems = (HashMap)m.get("orderItems");
                           for(Object names : foodItems.keySet()){
                                flag = true;
                               foodItemsArrayList.add(names.toString());
                               nameOfProviderArrayList.add(m.get("resturantName").toString());
                               if((boolean)m.get("delivered") == false) deliveryStatusArrayList.add("0");
                               else if((boolean)m.get("delivered") == true) deliveryStatusArrayList.add("1");


                           }

                        }
                    }

                    sizeOfArrayList = nameOfProviderArrayList.size();
                    if(flag){
                    ViewExistingMassOrders.MassOrderAdapter adapter = new ViewExistingMassOrders.MassOrderAdapter();
                    massOrderHistoryListView.setAdapter(adapter);
                    }
                    else{
                        noItemsToDisplayText.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(ViewExistingMassOrders.this, "CANNOT DISPLAY ITEMS!", Toast.LENGTH_SHORT).show();
                    sizeOfArrayList = nameOfProviderArrayList.size();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewExistingMassOrders.this, "CANNOT LOAD CURRENT ORDERS!", Toast.LENGTH_SHORT).show();
                sizeOfArrayList = nameOfProviderArrayList.size();
            }
        });



    }

    class MassOrderAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nameOfProviderArrayList.size();
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
        public View getView(int i, View v, ViewGroup parent) {
            v= getLayoutInflater().inflate(R.layout.custom_layout_for_mass_orders,null);
            TextView providerName = v.findViewById(R.id.tvNameOfProviderForMassOrders);
            TextView foodName = v.findViewById(R.id.tvNameOfFoodForMassOrderItems);
            ImageView deliveryImage = v.findViewById(R.id.ivImageForMassOrderStatus);

            foodName.setText(foodItemsArrayList.get(i));
            providerName.setText(nameOfProviderArrayList.get(i));
            if(deliveryStatusArrayList.get(i).equals("0"))
                deliveryImage.setImageResource(R.drawable.cross_red_logo);



            return v;
        }
    }

}

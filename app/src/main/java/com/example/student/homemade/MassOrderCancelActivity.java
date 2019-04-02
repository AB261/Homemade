package com.example.student.homemade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.student.homemade.ui.HistoricalOrdersFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class MassOrder2 {
    String provider;
    String Consumer;
    boolean delivered;
    String address;
    boolean paid;
    String orderTime;
    double orderTotal;
    String restaurantName;
    HashMap<String, Integer> orderItems;
    String orderDate;

    public MassOrder2(){

    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setConsumer(String consumer) {
        Consumer = consumer;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setorderItems(HashMap<String, Integer> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProvider() {
        return provider;
    }

    public String getConsumer() {
        return Consumer;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public String getAddress() {
        return address;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public MassOrder2(String provider, String consumer, boolean delivered, String address, boolean paid, String orderTime, double orderTotal, String restaurantName, HashMap<String, Integer> orderItems, String orderDate) {
        this.provider = provider;
        Consumer = consumer;
        this.delivered = delivered;
        this.address = address;
        this.paid = paid;
        this.orderTime = orderTime;
        this.orderTotal = orderTotal;
        this.restaurantName = restaurantName;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
    }

    public String getOrderItems() {
        String result = "";
        if(orderItems.size() == 0){
            return result;
        }

        for(Map.Entry item:orderItems.entrySet()){
            result = result + item.getKey() + " ( " + item.getValue() + " )  ";
        }


        return result;
    }

    public boolean equals(MassOrder2 order2){
        if(this.provider.equals(order2.provider) && this.Consumer.equals(order2.Consumer) && this.delivered == order2.delivered && this.orderTime.equals(order2.orderTime) &&
                this.orderTotal == order2.orderTotal){
            return true;
        }
        return false;
    }
}


public class MassOrderCancelActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private ArrayList<OrderInfo> mExampleList;
    private ArrayList<MassOrder2> massOrderInfos = new ArrayList<MassOrder2>();
    private RecyclerView mRecyclerview;
    private static final String TAG = "MASSCANCELORDERFRAGMENT";
    private MassCancelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    View v;
    String myconsumerID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_order_cancel);
        myconsumerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("=======", "consumer id at mass order is " + myconsumerID);
        Button btnGoBackFromMassCancelOrder = findViewById(R.id.btnGoBackFromMassCancelOrder);
        btnGoBackFromMassCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buildRecyclerView();
    }
    public void removeItem(int position){


        final MassOrder2 currOrder = massOrderInfos.get(position);

        final int position1 = position;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this);//, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this mass order?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        //---------------------------
                        Log.d("===========", "here");

                        db.collection("Mass Orders").whereEqualTo("Consumer", myconsumerID).whereEqualTo("delivered", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                OrderInfo orderInfo;
                                if (task.isSuccessful()) {
                                    Log.d("=======", "searching");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("=========", "searching2");

//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                                        MassOrder2 massOrder2 = document.toObject(MassOrder2.class);
                                        String documentID;
                                        Log.d("===========", massOrder2.getOrderTime() + " currorder " + currOrder.getOrderTime());
                                        if(currOrder.equals(massOrder2)) {
                                            documentID = document.getId();

                                            Log.d("============", "found order");
                                            db.collection("Mass Orders").document(documentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("=======", "DocumentSnapshot successfully deleted!");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("========", "Error deleting document", e);
                                                        }
                                                    });
                                            break;
                                        }
                                        else{
                                            Log.d("=========", "next loop");
                                            continue;
                                        }


                                    }
                                } else {
                                    Log.d("================", "Error getting documents: ", task.getException());
                                }
                            }
                        });
//-----------------------------------------
                        Log.d("===========", "here2");

                        Log.d("==========",massOrderInfos.get(position1).toString());
                        Log.d("=============",currOrder.provider + " " + currOrder.orderTotal + " " + currOrder.Consumer);

                        db.collection("Provider").document(currOrder.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                double cost = Double.parseDouble(documentSnapshot.getData().get("wallet").toString()) - currOrder.orderTotal;
                                db.collection("Provider").document(currOrder.provider).update("wallet",cost);
                            }
                        });

                        db.collection("Consumer").document(currOrder.Consumer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                double cost = Double.parseDouble(documentSnapshot.getData().get("wallet").toString()) + currOrder.orderTotal;
                                db.collection("Provider").document(currOrder.Consumer).update("wallet",cost);
                            }
                        });
                        massOrderInfos.remove(position1);
                        mAdapter.notifyItemRemoved(position1);
                        Log.d("============","Successfully cancelled order");
                        inform("Your money has been refunded to your wallet!");

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    public void buildRecyclerView(){
        Log.d("===========", "building recycler view");
        mRecyclerview=findViewById(R.id.massrecyclerview);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mAdapter=new MassCancelAdapter(massOrderInfos);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListner(new MassCancelAdapter.OnItemClickListner() {
            @Override
            //public void onItemClick(int position) {
            //    changeItem(position," ");
            //}


            public void onDeleteClick(int position) {
                Log.d("=========", "position is " + position);
                final int position2 = position;

                MassOrder2 orderToCancel = massOrderInfos.get(position);

                final String time_and_date = orderToCancel.orderTime;
                final String orderDate = orderToCancel.orderDate;
                int timeBeforeCancel;

                db.collection("Provider").document(orderToCancel.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();


                        //////////-----------------------//////////////////////////////change here////////////--------------------------------

                        final int timeBeforeCancel = 24*60;

                        int hour, mins, day, year, month;

                        if(time_and_date.equals("")){
                            hour = 13;
                            mins = 30;
                        }
                        else {
                            hour = Integer.parseInt(time_and_date.substring(0, time_and_date.indexOf(':')));
                            mins = Integer.parseInt(time_and_date.substring(time_and_date.indexOf(':') + 1, time_and_date.indexOf(' ')));
                            if(time_and_date.substring(time_and_date.indexOf(' ') + 1).equals("pm")){
                                hour += 12;
                            }
                            else if(time_and_date.substring(time_and_date.indexOf(' ') + 1).equals("am") && hour == 12){
                                hour = 0;
                            }
                        }

                        if(orderDate.equals("")){
                            day = 12;
                            month = 3;
                            year = 2019;
                        }
                        else{
                            int index1 = 0, index2 = 0;
                            for(int i=0; i<orderDate.length(); i++){
                                if(orderDate.charAt(i) == '-'){
                                    index1 = i;
                                    break;
                                }
                            }
                            year = Integer.parseInt(orderDate.substring(0, index1));

                            for(int i=index1 + 1; i < orderDate.length(); i++){
                                if(orderDate.charAt(i) == '-'){
                                    index2 = i;
                                    break;
                                }
                            }
                            month = Integer.parseInt(orderDate.substring(index1+1, index2));
                            day = Integer.parseInt(orderDate.substring(index2+1, orderDate.length()));
                        }
                        Calendar calendar = Calendar.getInstance();
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);
                        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int currentMonth = calendar.get(Calendar.MONTH) + 1;
                        int currentYear = calendar.get(Calendar.YEAR);

                        Log.d("=========", "hour " + hour + " mins " + mins + " currentHour " + currentHour + " currentMinute " + currentMinute + " timeBeforeCancel " + timeBeforeCancel);
                        Log.d("=====" ,"curr day " + currentDay + "  order day " + day + " curr month " + currentMonth + " order month " + month);
                        int lhs = (currentDay-1)*1440 + (currentMonth)*43800 + /*(currentYear-1)*525600 + */ currentHour * 60 + currentMinute;
                        int rhs = (day-1)*1440 + (month)*43800 + /*+ (year-1)*525600 + */hour * 60 + mins - timeBeforeCancel;


                        if(((currentDay-1)*1440 + (currentMonth)*43800 + /*(currentYear-1)*525600 + */ currentHour * 60 + currentMinute <
                                (day-1)*1440 + (month)*43800 + /*+ (year-1)*525600 + */hour * 60 + mins - timeBeforeCancel)
                        ) {
                            Log.d("=====", "lhs " + lhs);
                            Log.d("=====", "rhs " + rhs);
                            removeItem(position2);
                        }
                        else{
                            inform("Sorry, too late to cancel this order.");
                        }
                    }
                });


            }
        });

        db.collection("Mass Orders").whereEqualTo("Consumer", myconsumerID).whereEqualTo("delivered", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                        MassOrder2 massOrder2 = document.toObject(MassOrder2.class);
                        Log.d("=========", document.getId());
                        Log.d("========", "consumer "+ document.getData());

//                        orderInfo = new OrderInfo(Integer.parseInt(map.get("Provider").toString()), Integer.parseInt(map.get("client").toString()), (Boolean) map.get("completed"), (Boolean) map.get("delivered"), Integer.parseInt(map.get("delivery_person").toString()), Integer.parseInt(map.get("orderID").toString()), (Boolean) map.get("paid"), (ArrayList) map.get("things_ordered"), (String) map.get("time_and_date"), Integer.parseInt(map.get("timeBeforeCancel").toString()), Float.parseFloat(map.get("total_cost").toString()));
                        Log.d("==========", "object items are " + massOrder2.getOrderItems());
                        mAdapter.added(massOrder2);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void inform(String string){
        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_INDEFINITE).setDuration(8000);

        snack.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        }).setActionTextColor(findViewById(android.R.id.content).getResources().getColor(R.color.white));

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(findViewById(android.R.id.content).getResources().getColor(R.color.white));

        view.setBackgroundColor(findViewById(android.R.id.content).getResources().getColor(R.color.signupColor));
        snack.show();
    }
}

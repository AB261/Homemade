// package com.example.student.homemade;

// import android.content.Context;
// import android.content.DialogInterface;
// import android.content.Intent;
// import android.os.Build;
// import android.support.annotation.NonNull;
// import android.support.design.widget.Snackbar;
// import android.support.v4.app.Fragment;
// import android.os.Bundle;
// import android.support.v7.app.AlertDialog;
// import android.support.v7.widget.LinearLayoutManager;
// import android.support.v7.widget.RecyclerView;
// import android.util.Log;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;
// import android.widget.ListView;
// import android.widget.TextView;

// import com.example.student.homemade.ui.HistoricalOrdersFragment;
// import com.facebook.login.LoginManager;
// import com.google.android.gms.auth.api.signin.GoogleSignIn;
// import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
// import com.google.android.gms.tasks.OnCompleteListener;
// import com.google.android.gms.tasks.OnFailureListener;
// import com.google.android.gms.tasks.OnSuccessListener;
// import com.google.android.gms.tasks.Task;
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.database.DataSnapshot;
// import com.google.firebase.database.DatabaseError;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.database.Query;
// import com.google.firebase.database.ValueEventListener;
// import com.google.firebase.firestore.DocumentSnapshot;
// import com.google.firebase.firestore.FirebaseFirestore;
// import com.google.firebase.firestore.QueryDocumentSnapshot;
// import com.google.firebase.firestore.QuerySnapshot;

// import java.util.ArrayList;
// import java.util.Calendar;
// import java.util.HashMap;

// class Order2 {
//     String provider;
//     String consumer;
//     boolean completed;
//     boolean delivered;
//     boolean paid;
//     String orderTime;
//     double orderTotal;
//     ArrayList<FoodItem2> itemsOrdered;
//     String deliveryPerson;
//     boolean isMassOrder;
//     String orderDate;

//     public String getOrderDate() {
//         return orderDate;
//     }

//     public void setOrderDate(String orderDate) {
//         this.orderDate = orderDate;
//     }

//     public void setMassOrder(boolean massOrder) {
//         isMassOrder = massOrder;
//     }

//     public boolean isMassOrder() {
//         return isMassOrder;
//     }

//     public void setProvider(String provider) {
//         this.provider = provider;
//     }

//     public void setConsumer(String consumer) {
//         this.consumer = consumer;
//     }

//     public void setCompleted(boolean completed) {
//         this.completed = completed;
//     }

//     public void setDelivered(boolean delivered) {
//         this.delivered = delivered;
//     }

//     public void setPaid(boolean paid) {
//         this.paid = paid;
//     }

//     public void setOrderTime(String orderTime) {
//         this.orderTime = orderTime;
//     }

//     public void setOrderTotal(double orderTotal) {
//         this.orderTotal = orderTotal;
//     }

//     public void setItemsOrdered(ArrayList<FoodItem2> itemsOrdered) {
//         this.itemsOrdered = itemsOrdered;
//     }

//     public void setDeliveryPerson(String deliveryPerson) {
//         this.deliveryPerson = deliveryPerson;
//     }

//     public String getProvider() {
//         return provider;
//     }

//     public String getConsumer() {
//         return consumer;
//     }

//     public boolean isCompleted() {
//         return completed;
//     }

//     public boolean isDelivered() {
//         return delivered;
//     }

//     public boolean isPaid() {
//         return paid;
//     }

//     public String getOrderTime() {
//         return orderTime;
//     }

//     public double getOrderTotal() {
//         return orderTotal;
//     }

//     public String getDeliveryPerson() {
//         return deliveryPerson;
//     }


//     public Order2(){

//     }

//     public Order2(String provider, String consumer, boolean completed, boolean delivered, boolean paid, String orderTime, double orderTotal, ArrayList<FoodItem2> itemsOrdered, String deliveryPerson) {
//         this.provider = provider;
//         this.consumer = consumer;
//         this.completed = completed;
//         this.delivered = delivered;
//         this.paid = paid;
//         this.orderTime = orderTime;
//         this.orderTotal = orderTotal;
//         this.itemsOrdered = itemsOrdered;
//         this.deliveryPerson = deliveryPerson;
//     }

//     public String getItemsOrdered() {
//         String result = "";
//         if(itemsOrdered.size() == 0){
//             return result;
//         }
//         for(int i=0; i<itemsOrdered.size(); i++){
//             if(itemsOrdered.get(i).itemNumber != 0) {
//                 result = result + itemsOrdered.get(i).itemName + " - " + itemsOrdered.get(i).itemNumber + "  ";
//             }
//         }
// //        if(itemsOrdered.get(itemsOrdered.size() - 1).itemNumber != 0)
// //            result = result + itemsOrdered.get(itemsOrdered.size() - 1).itemName + ":" + itemsOrdered.get(itemsOrdered.size()-1).itemNumber;


//         return result;
//     }

//     public boolean equals(Order2 order2){
//         if(this.provider.equals(order2.provider) && this.consumer.equals(order2.consumer) && this.completed == order2.completed && this.delivered == order2.delivered && this.orderTime.equals(order2.orderTime) &&
//         this.orderTotal == order2.orderTotal && this.deliveryPerson.equals(order2.deliveryPerson) && this.itemsOrdered.equals(order2.itemsOrdered)){
//             return true;
//         }
//         return false;
//     }
// }

// class FoodItem2 {

//     String itemName;
//     float itemCost;
//     int itemNumber;

//     public FoodItem2(){

//     }

//     public FoodItem2(String itemName, float itemCost, int itemNumber) {
//         this.itemName = itemName;
//         this.itemCost = itemCost;
//         this.itemNumber = itemNumber;
//     }

//     public boolean equals(FoodItem2 item){
//         if(this.itemName.equals(item.itemName) && this.itemCost == item.itemCost && this.itemNumber == item.itemNumber){
//             return true;
//         }
//         return false;
//     }

//     public void setItemName(String itemName) {
//         this.itemName = itemName;
//     }

//     public void setItemCost(float itemCost) {
//         this.itemCost = itemCost;
//     }

//     public void setItemNumber(int itemNumber) {
//         this.itemNumber = itemNumber;
//     }

//     public String getItemName() {
//         return itemName;
//     }

//     public float getItemCost() {
//         return itemCost;
//     }

//     public int getItemNumber() {
//         return itemNumber;
//     }
// }

// public class CancelOrderFragment extends Fragment {
//     FirebaseFirestore db = FirebaseFirestore.getInstance();
//     //private ArrayList<OrderInfo> mExampleList;
//     private ArrayList<Order2> orderInfos = new ArrayList<Order2>();
//     private RecyclerView mRecyclerview;
//     private static final String TAG = "CANCELORDERFRAGMENT";
//     private CancelAdapter mAdapter;
//     private RecyclerView.LayoutManager mLayoutManager;
//     View v;
//     String myconsumerID;

//     public CancelOrderFragment(){

//     }
//     public static CancelOrderFragment newInstance() {
//         CancelOrderFragment fragment = new CancelOrderFragment();
//         return fragment;
//     }
//     //    @Override
// //    protected void onCreate(Bundle savedInstanceState) {
// //        super.onCreate(savedInstanceState);
// //        setContentView(R.layout.activity_cancel);
// //        createExampleList();
// //        buildRecyclerView();
// //    }
//     @Override
//     public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                              Bundle savedInstanceState) {
//         // Inflate the layout for this fragment
//         myconsumerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//         Log.d("=======", "consumer id is " + myconsumerID);
//         v = inflater.inflate(R.layout.activity_cancel, container, false);
//         //createExampleList();
//         buildRecyclerView();
//         return v;
//     }
//     public void removeItem(int position){


//         final Order2 currOrder = orderInfos.get(position);
// //        int orderID = currOrder.or();

// //        Log.v("========", "order id is " + orderID);

//         db.collection("Orders").whereEqualTo("consumer", myconsumerID).whereEqualTo("delivered", false).whereEqualTo("paid", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//             @Override
//             public void onComplete(@NonNull Task<QuerySnapshot> task) {
// //                OrderInfo orderInfo;
//                 if (task.isSuccessful()) {
//                     for (QueryDocumentSnapshot document : task.getResult()) {

// //                        Log.d(TAG, document.getId() + " => " + document.getData());
// //                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

//                         Order2 order2 = document.toObject(Order2.class);
//                         String documentID;
//                         if(currOrder.equals(order2)) {
//                             documentID = document.getId();


//                             db.collection("Orders").document(documentID)
//                                     .delete()
//                                     .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                         @Override
//                                         public void onSuccess(Void aVoid) {
//                                             Log.v("=======", "DocumentSnapshot successfully deleted!");
//                                         }
//                                     })
//                                     .addOnFailureListener(new OnFailureListener() {
//                                         @Override
//                                         public void onFailure(@NonNull Exception e) {
//                                             Log.v("========", "Error deleting document", e);
//                                         }
//                                     });
//                         }

//                     }
//                 } else {
//                     Log.d(TAG, "Error getting documents: ", task.getException());
//                 }
//             }
//         });

// //        db.collection("Orders").document("13")
// //                .delete()
// //                .addOnSuccessListener(new OnSuccessListener<Void>() {
// //                    @Override
// //                    public void onSuccess(Void aVoid) {
// //                        Log.v("=======", "DocumentSnapshot successfully deleted!");
// //                    }
// //                })
// //                .addOnFailureListener(new OnFailureListener() {
// //                    @Override
// //                    public void onFailure(@NonNull Exception e) {
// //                        Log.v("========", "Error deleting document", e);
// //                    }
// //                });

//         final int position1 = position;

//         AlertDialog.Builder builder;
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//             builder = new AlertDialog.Builder(getContext());//, android.R.style.Theme_Material_Dialog_Alert);
//         } else {
//             builder = new AlertDialog.Builder(getContext());
//         }
//         builder//.setTitle("Logout")
//                 .setMessage("Are you sure you want to cancel this order?")
//                 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                     public void onClick(DialogInterface dialog, int which) {
//                         // continue with delete

// //                        Log.d("ayyyyyyyyyyyyyyyyyy",orderInfos.get(position1).toString());
//                         Log.d("ayyyyyyyyyyyyyyyyyyy",currOrder.provider + " " + currOrder.orderTotal + " " + currOrder.consumer);

//                         db.collection("Provider").document(currOrder.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                             @Override
//                             public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                 double cost = Double.parseDouble(documentSnapshot.getData().get("wallet").toString()) - currOrder.orderTotal;
//                                 db.collection("Provider").document(currOrder.provider).update("wallet",cost);
//                             }
//                         });

//                         db.collection("Consumer").document(currOrder.consumer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                             @Override
//                             public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                 double cost = Double.parseDouble(documentSnapshot.getData().get("wallet").toString()) + currOrder.orderTotal;
//                                 db.collection("Provider").document(currOrder.consumer).update("wallet",cost);
//                             }
//                         });

//                         orderInfos.remove(position1);
//                         mAdapter.notifyItemRemoved(position1);
//                         inform("Successfully cancelled order");

//                     }
//                 })
//                 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                     public void onClick(DialogInterface dialog, int which) {
//                         // do nothing
//                     }
//                 })
//                 .setIcon(android.R.drawable.ic_dialog_alert)
//                 .show();


//     }


//     //    public void changeItem(int position,String text){
// //        mExampleList.get(position).changeText1(text);
// //        mAdapter.notifyItemChanged(position);
// //    }
// //    public void createExampleList(){
// //        mExampleList=new ArrayList<>();
// //        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
// //        mExampleList.add(new CancelItem("Line12","Line2","Line3"));
// //        mExampleList.add(new CancelItem("onDeleteClickLine1","Line2","Line3"));
// //        mExampleList.add(new CancelItem("Line25","Line2","Line3"));
// //        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
// //        mExampleList.add(new CancelItem("Line12","Line2","Line3"));
// //        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
// //        mExampleList.add(new CancelItem("Line25","Line2","Line3"));
// //(String) map.get("timeOrderPlaced")
// //
// //    }
//     public void buildRecyclerView(){
//         Log.d("===========", "building recycler view");
//         mRecyclerview=v.findViewById(R.id.recyclerview);
//         mRecyclerview.setHasFixedSize(true);
//         mLayoutManager=new LinearLayoutManager(getActivity());
//         mAdapter=new CancelAdapter(orderInfos);
//         mRecyclerview.setLayoutManager(mLayoutManager);
//         mRecyclerview.setAdapter(mAdapter);
//         mAdapter.setOnItemClickListner(new CancelAdapter.OnItemClickListner() {
//             @Override
//             //public void onItemClick(int position) {
//             //    changeItem(position," ");
//             //}


//             public void onDeleteClick(int position) {
//                 Log.d("=========", "position is " + position);
//                 final int position2 = position;

//                 Order2 orderToCancel = orderInfos.get(position);

//                 final String time_and_date = orderToCancel.orderTime;
//                 final String orderDate = orderToCancel.orderDate;
//                 int timeBeforeCancel;

//                 db.collection("Provider").document(orderToCancel.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                     @Override
//                     public void onSuccess(DocumentSnapshot documentSnapshot) {
//                         HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
//                         final int timeBeforeCancel = Integer.parseInt(map.get("timeBeforeCancel").toString());

//                         int hour, mins, day, year, month;

//                         if(time_and_date.equals("")){
//                             hour = 13;
//                             mins = 30;
//                         }
//                         else {
//                             hour = Integer.parseInt(time_and_date.substring(0, time_and_date.indexOf(':')));
//                             mins = Integer.parseInt(time_and_date.substring(time_and_date.indexOf(':') + 1));
//                         }

//                         if(orderDate.equals("")){
//                             day = 12;
//                             month = 3;
//                             year = 2019;
//                         }
//                         else{
//                             int index1 = 0, index2 = 0;
//                             for(int i=0; i<orderDate.length(); i++){
//                                 if(orderDate.charAt(i) == '/'){
//                                     index1 = i;
//                                     break;
//                                 }
//                             }
//                             day = Integer.parseInt(orderDate.substring(0, index1));

//                             for(int i=index1 + 1; i < orderDate.length(); i++){
//                                 if(orderDate.charAt(i) == '/'){
//                                     index2 = i;
//                                     break;
//                                 }
//                             }
//                             month = Integer.parseInt(orderDate.substring(index1+1, index2));
//                             year = Integer.parseInt(orderDate.substring(index2+1, orderDate.length()));
//                         }
//                         Calendar calendar = Calendar.getInstance();
//                         int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
//                         int currentMinute = calendar.get(Calendar.MINUTE);
//                         int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//                         int currentMonth = calendar.get(Calendar.MONTH);
//                         int currentYear = calendar.get(Calendar.YEAR);

//                         Log.d("=========", "hour " + hour + " mins " + mins + " currentHour " + currentHour + " currentMinute " + currentMinute + " timeBeforeCancel " + timeBeforeCancel);

//                         if(((currentDay-1)*1440 + (currentMonth-1)*43800 /*+ (currentYear-1)*525600*/ + currentHour * 60 + currentMinute <
//                                 (day-1)*1440 + (month-1)*43800 + /*(year-1)*525600*/ + hour * 60 + mins + timeBeforeCancel) && (currentHour * 60 + currentMinute > hour * 60 + mins)
//                                 ) {

//                                 removeItem(position2);
//                         }
//                         else{
//                             inform("Sorry, too late to cancel this order.");
//                         }
//                     }
//                 });

// //                int hour = Integer.parseInt(time_and_date.substring(0, time_and_date.indexOf(':')));
// //                int mins = Integer.parseInt(time_and_date.substring(time_and_date.indexOf(':') + 1));
// //
// //                Calendar calendar = Calendar.getInstance();
// //                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
// //                int currentMinute = calendar.get(Calendar.MINUTE);
// //
// //                Log.d("=========", "hour " + hour + " mins " + mins + " currentHour " + currentHour + " currentMinute " + currentMinute + " timeBeforeCancel " + timeBeforeCancel);
// //
// //                if(currentHour * 60 + currentMinute < hour * 60 + mins + timeBeforeCancel) {
// //                    removeItem(position);
// //                }
// //                else{
// //                    inform("Sorry, too late to cancel this order.");
// //                }
//             }
//         });

//         db.collection("Orders").whereEqualTo("consumer", myconsumerID).whereEqualTo("delivered", false).whereEqualTo("paid", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//             @Override
//             public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                 OrderInfo orderInfo;
//                 if (task.isSuccessful()) {
//                     for (QueryDocumentSnapshot document : task.getResult()) {

// //                        Log.d(TAG, document.getId() + " => " + document.getData());
// //                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

//                         Order2 order2 = document.toObject(Order2.class);

// //                        orderInfo = new OrderInfo(Integer.parseInt(map.get("Provider").toString()), Integer.parseInt(map.get("client").toString()), (Boolean) map.get("completed"), (Boolean) map.get("delivered"), Integer.parseInt(map.get("delivery_person").toString()), Integer.parseInt(map.get("orderID").toString()), (Boolean) map.get("paid"), (ArrayList) map.get("things_ordered"), (String) map.get("time_and_date"), Integer.parseInt(map.get("timeBeforeCancel").toString()), Float.parseFloat(map.get("total_cost").toString()));
//                         Log.d("==========", "object items are " + order2.getItemsOrdered());
//                         mAdapter.added(order2);
//                     }
//                 } else {
//                     Log.d(TAG, "Error getting documents: ", task.getException());
//                 }
//             }
//         });
//     }

//     public void inform(String string){
//         final Snackbar snack = Snackbar.make(getView()/*.findViewById(android.R.id.content)*/, string, Snackbar.LENGTH_INDEFINITE).setDuration(8000);

//         snack.setAction("Dismiss", new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 snack.dismiss();
//             }
//         }).setActionTextColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.white));

//         View view = snack.getView();
//         TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//         tv.setTextColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.white));

//         view.setBackgroundColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.signupColor));
//         snack.show();
//     }
// }





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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

class Order2 {
    String provider;
    String consumer;
    boolean completed;
    boolean delivered;
    boolean paid;
    String orderTime;
    double orderTotal;
    ArrayList<FoodItem2> itemsOrdered;
    String deliveryPerson;
    boolean isMassOrder;
    String orderDate;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setMassOrder(boolean massOrder) {
        isMassOrder = massOrder;
    }

    public boolean isMassOrder() {
        return isMassOrder;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
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

    public void setItemsOrdered(ArrayList<FoodItem2> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public String getProvider() {
        return provider;
    }

    public String getConsumer() {
        return consumer;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isDelivered() {
        return delivered;
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

    public String getDeliveryPerson() {
        return deliveryPerson;
    }


    public Order2(){

    }

    public Order2(String provider, String consumer, boolean completed, boolean delivered, boolean paid, String orderTime, double orderTotal, ArrayList<FoodItem2> itemsOrdered, String deliveryPerson) {
        this.provider = provider;
        this.consumer = consumer;
        this.completed = completed;
        this.delivered = delivered;
        this.paid = paid;
        this.orderTime = orderTime;
        this.orderTotal = orderTotal;
        this.itemsOrdered = itemsOrdered;
        this.deliveryPerson = deliveryPerson;
    }

    public String getItemsOrdered() {
        String result = "";
        if(itemsOrdered.size() == 0){
            return result;
        }
        for(int i=0; i<itemsOrdered.size(); i++){
            if(itemsOrdered.get(i).itemNumber != 0) {
                result = result + itemsOrdered.get(i).itemName + " - " + itemsOrdered.get(i).itemNumber + "  ";
            }
        }
//        if(itemsOrdered.get(itemsOrdered.size() - 1).itemNumber != 0)
//            result = result + itemsOrdered.get(itemsOrdered.size() - 1).itemName + ":" + itemsOrdered.get(itemsOrdered.size()-1).itemNumber;


        return result;
    }

    public boolean equals(Order2 order2){
        if(this.provider.equals(order2.provider) && this.consumer.equals(order2.consumer) && this.completed == order2.completed && this.delivered == order2.delivered && this.orderTime.equals(order2.orderTime) &&
        this.orderTotal == order2.orderTotal && this.deliveryPerson.equals(order2.deliveryPerson) && this.itemsOrdered.equals(order2.itemsOrdered)){
            return true;
        }
        return false;
    }
}

class FoodItem2 {

    String itemName;
    float itemCost;
    int itemNumber;

    public FoodItem2(){

    }

    public FoodItem2(String itemName, float itemCost, int itemNumber) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemNumber = itemNumber;
    }

    public boolean equals(FoodItem2 item){
        if(this.itemName.equals(item.itemName) && this.itemCost == item.itemCost && this.itemNumber == item.itemNumber){
            return true;
        }
        return false;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCost(float itemCost) {
        this.itemCost = itemCost;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public float getItemCost() {
        return itemCost;
    }

    public int getItemNumber() {
        return itemNumber;
    }
}

public class CancelOrderFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private ArrayList<OrderInfo> mExampleList;
    private ArrayList<Order2> orderInfos = new ArrayList<Order2>();
    private RecyclerView mRecyclerview;
    private static final String TAG = "CANCELORDERFRAGMENT";
    private CancelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    View v;
    String myconsumerID;

    public CancelOrderFragment(){

    }
    public static CancelOrderFragment newInstance() {
        CancelOrderFragment fragment = new CancelOrderFragment();
        return fragment;
    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cancel);
//        createExampleList();
//        buildRecyclerView();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myconsumerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("=======", "consumer id is " + myconsumerID);
        v = inflater.inflate(R.layout.activity_cancel, container, false);
        //createExampleList();
        buildRecyclerView();
        return v;
    }
    public void removeItem(int position){


        final Order2 currOrder = orderInfos.get(position);
//        int orderID = currOrder.or();

//        Log.v("========", "order id is " + orderID);

        db.collection("Orders").whereEqualTo("consumer", myconsumerID).whereEqualTo("delivered", false).whereEqualTo("paid", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                OrderInfo orderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                        Order2 order2 = document.toObject(Order2.class);
                        String documentID;
                        if(currOrder.equals(order2)) {
                            documentID = document.getId();


                            db.collection("Orders").document(documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.v("=======", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.v("========", "Error deleting document", e);
                                        }
                                    });
                        }

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

//        db.collection("Orders").document("13")
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.v("=======", "DocumentSnapshot successfully deleted!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.v("========", "Error deleting document", e);
//                    }
//                });

        final int position1 = position;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext());//, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder//.setTitle("Logout")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

//                        Log.d("ayyyyyyyyyyyyyyyyyy",orderInfos.get(position1).toString());
                        Log.d("ayyyyyyyyyyyyyyyyyyy",currOrder.provider + " " + currOrder.orderTotal + " " + currOrder.consumer);

                        db.collection("Provider").document(currOrder.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                double cost = Double.parseDouble(documentSnapshot.getData().get("wallet").toString()) - currOrder.orderTotal;
                                db.collection("Provider").document(currOrder.provider).update("wallet",cost);
                            }
                        });

                        db.collection("Consumer").document(currOrder.consumer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                double cost = Double.parseDouble(documentSnapshot.getData().get("wallet").toString()) + currOrder.orderTotal;
                                db.collection("Provider").document(currOrder.consumer).update("wallet",cost);
                            }
                        });

                        orderInfos.remove(position1);
                        mAdapter.notifyItemRemoved(position1);
                        inform("Successfully cancelled order");

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


    //    public void changeItem(int position,String text){
//        mExampleList.get(position).changeText1(text);
//        mAdapter.notifyItemChanged(position);
//    }
//    public void createExampleList(){
//        mExampleList=new ArrayList<>();
//        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
//        mExampleList.add(new CancelItem("Line12","Line2","Line3"));
//        mExampleList.add(new CancelItem("onDeleteClickLine1","Line2","Line3"));
//        mExampleList.add(new CancelItem("Line25","Line2","Line3"));
//        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
//        mExampleList.add(new CancelItem("Line12","Line2","Line3"));
//        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
//        mExampleList.add(new CancelItem("Line25","Line2","Line3"));
//(String) map.get("timeOrderPlaced")
//
//    }
    public void buildRecyclerView(){
        Log.d("===========", "building recycler view");
        mRecyclerview=v.findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new CancelAdapter(orderInfos);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListner(new CancelAdapter.OnItemClickListner() {
            @Override
            //public void onItemClick(int position) {
            //    changeItem(position," ");
            //}


            public void onDeleteClick(int position) {
                Log.d("=========", "position is " + position);
                final int position2 = position;

                Order2 orderToCancel = orderInfos.get(position);

                final String time_and_date = orderToCancel.orderTime;
                final String orderDate = orderToCancel.orderDate;
                int timeBeforeCancel;

                db.collection("Provider").document(orderToCancel.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                        final int timeBeforeCancel = Integer.parseInt(map.get("timeBeforeCancel").toString());

                        int hour, mins, day, year, month;

                        if(time_and_date.equals("")){
                            hour = 13;
                            mins = 30;
                        }
                        else {
                            hour = Integer.parseInt(time_and_date.substring(0, time_and_date.indexOf(':')));
                            mins = Integer.parseInt(time_and_date.substring(time_and_date.indexOf(':') + 1));
                        }

                        if(orderDate.equals("")){
                            day = 12;
                            month = 3;
                            year = 2019;
                        }
                        else{
                            int index1 = 0, index2 = 0;
                            for(int i=0; i<orderDate.length(); i++){
                                if(orderDate.charAt(i) == '/'){
                                    index1 = i;
                                    break;
                                }
                            }
                            day = Integer.parseInt(orderDate.substring(0, index1));

                            for(int i=index1 + 1; i < orderDate.length(); i++){
                                if(orderDate.charAt(i) == '/'){
                                    index2 = i;
                                    break;
                                }
                            }
                            month = Integer.parseInt(orderDate.substring(index1+1, index2));
                            year = Integer.parseInt(orderDate.substring(index2+1, orderDate.length()));
                        }
                        Calendar calendar = Calendar.getInstance();
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);
                        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int currentMonth = calendar.get(Calendar.MONTH);
                        int currentYear = calendar.get(Calendar.YEAR);

                        Log.d("=========", "hour " + hour + " mins " + mins + " currentHour " + currentHour + " currentMinute " + currentMinute + " timeBeforeCancel " + timeBeforeCancel);

                        if(((currentDay-1)*1440 + (currentMonth-1)*43800 /*+ (currentYear-1)*525600*/ + currentHour * 60 + currentMinute <
                                (day-1)*1440 + (month-1)*43800 + /*(year-1)*525600*/ + hour * 60 + mins + timeBeforeCancel) && (currentHour * 60 + currentMinute > hour * 60 + mins)
                                ) {

                                removeItem(position2);
                        }
                        else{
                            inform("Sorry, too late to cancel this order.");
                        }
                    }
                });

//                int hour = Integer.parseInt(time_and_date.substring(0, time_and_date.indexOf(':')));
//                int mins = Integer.parseInt(time_and_date.substring(time_and_date.indexOf(':') + 1));
//
//                Calendar calendar = Calendar.getInstance();
//                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
//                int currentMinute = calendar.get(Calendar.MINUTE);
//
//                Log.d("=========", "hour " + hour + " mins " + mins + " currentHour " + currentHour + " currentMinute " + currentMinute + " timeBeforeCancel " + timeBeforeCancel);
//
//                if(currentHour * 60 + currentMinute < hour * 60 + mins + timeBeforeCancel) {
//                    removeItem(position);
//                }
//                else{
//                    inform("Sorry, too late to cancel this order.");
//                }
            }
        });

        db.collection("Orders").whereEqualTo("consumer", myconsumerID).whereEqualTo("delivered", false).whereEqualTo("paid", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                OrderInfo orderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                        Order2 order2 = document.toObject(Order2.class);

//                        orderInfo = new OrderInfo(Integer.parseInt(map.get("Provider").toString()), Integer.parseInt(map.get("client").toString()), (Boolean) map.get("completed"), (Boolean) map.get("delivered"), Integer.parseInt(map.get("delivery_person").toString()), Integer.parseInt(map.get("orderID").toString()), (Boolean) map.get("paid"), (ArrayList) map.get("things_ordered"), (String) map.get("time_and_date"), Integer.parseInt(map.get("timeBeforeCancel").toString()), Float.parseFloat(map.get("total_cost").toString()));
                        Log.d("==========", "object items are " + order2.getItemsOrdered());
                        mAdapter.added(order2);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void inform(String string){
        final Snackbar snack = Snackbar.make(getView()/*.findViewById(android.R.id.content)*/, string, Snackbar.LENGTH_INDEFINITE).setDuration(8000);

        snack.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        }).setActionTextColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.white));

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.white));

        view.setBackgroundColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.signupColor));
        snack.show();
    }
}

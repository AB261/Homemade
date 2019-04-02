// package com.example.student.homemade;

// import android.content.Intent;
// import android.os.Parcelable;
// import android.support.annotation.NonNull;
// import android.support.v7.app.AppCompatActivity;
// import android.os.Bundle;
// import android.support.v7.widget.Toolbar;

// import android.util.Log;
// import android.view.View;
// import android.widget.Button;
// import android.widget.ListAdapter;
// import android.widget.ListView;
// import android.widget.TextView;

// import com.google.android.gms.tasks.OnCompleteListener;
// import com.google.android.gms.tasks.OnFailureListener;
// import com.google.android.gms.tasks.OnSuccessListener;
// import com.google.android.gms.tasks.Task;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.firestore.DocumentReference;
// import com.google.firebase.firestore.DocumentSnapshot;
// import com.google.firebase.firestore.FirebaseFirestore;
// import com.google.firebase.firestore.QueryDocumentSnapshot;
// import com.google.firebase.firestore.QuerySnapshot;

// import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.Calendar;

// import java.util.HashMap;
// import java.util.List;

// class Order {
//     String provider;
//     String consumer;
//     boolean completed;
//     boolean delivered;
//     boolean paid;
//     String orderTime;
//     double orderTotal;
//     ArrayList<FoodItem> itemsOrdered;
//     String deliveryPerson;
//     boolean isMassOrder;
//     String orderDate;

//     public Order(String provider, String consumer, boolean completed, boolean delivered, boolean paid, String orderTime, double orderTotal, ArrayList<FoodItem> itemsOrdered, String deliveryPerson, boolean isMassOrder, String orderDate) {

//         this.provider = provider;
//         this.consumer = consumer;
//         this.completed = completed;
//         this.delivered = delivered;
//         this.paid = paid;
//         this.orderTime = orderTime;
//         this.orderTotal = orderTotal;
//         this.itemsOrdered = itemsOrdered;
//         this.deliveryPerson = deliveryPerson;
//         this.isMassOrder = isMassOrder;
//         this.orderDate = orderDate;
//     }
// }

// class FoodItem {

//     String itemName;
//     float itemCost;
//     int itemNumber;

//     public FoodItem(String itemName, float itemCost, int itemNumber) {
//         this.itemName = itemName;
//         this.itemCost = itemCost;
//         this.itemNumber = itemNumber;
//     }

// }

// class Provider {
//     String userID;
//     String menu;

//     public String getUserID() {
//         return userID;
//     }

//     public void setUserID(String userID) {
//         this.userID = userID;
//     }

//     public String getMenu() {
//         return menu;
//     }

//     public void setMenu(String menu) {
//         this.menu = menu;
//     }
// }

// //class Order {
// //
// //    FoodItem[] orderedItems;
// //    float totalCost;
// //
// //    public Order(FoodItem[] orderedItems, float totalCost) {
// //        this.orderedItems = orderedItems;
// //        this.totalCost = totalCost;
// //    }
// //}

// class Item {
//     List<String> content;
//     String menu_type;

//     public List<String> getContent() {
//         return content;
//     }

//     public void setContent(List<String> content) {
//         this.content = content;
//     }

//     public String getMenu_type() {
//         return menu_type;
//     }

//     public void setMenu_type(String menu_type) {
//         this.menu_type = menu_type;
//     }
// }



// public class OrderPageActivity extends AppCompatActivity {

//     private static final String TAG = OrderPageActivity.class.getName();
//     ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
//     ListAdapter itemAdapter;
//     ListView itemListView;

//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_order_page);

//         Button orderButton = findViewById(R.id.orderButton);

//         Toolbar toolbar = findViewById(R.id.toolbar);
//         toolbar.setTitle("Menu");

//         itemListView = (ListView) findViewById(R.id.itemList);
// //        FirebaseApp.initializeApp(this);
//         final String providerID = "vMR09oO90SbUtCapURrudg5QMlw2";
//         final String type = "Breakfast";
//         final String consumerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//         final FirebaseFirestore db = FirebaseFirestore.getInstance();
//         Log.d("consumerid",consumerID);
//         db.collection("Provider")
//                 .whereEqualTo("id", providerID)
//                 .get()
//                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                     @Override
//                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                         if (task.isSuccessful()) {
//                             for(QueryDocumentSnapshot document : task.getResult()){
//                                 Log.d("newone","entered the arena");
//                                 db.collection("Provider").document(document.getId())
//                                         .collection("menu").document(type).get()
//                                         .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                     @Override
//                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                         if(task.isSuccessful()){
//                                             DocumentSnapshot documentM = task.getResult();
//                                             if(documentM.exists()) {
//                                                 HashMap<String, Object> map = (HashMap<String, Object>) documentM.getData().get("items");
//                                                 for(String key : map.keySet()){
//                                                     foodItems.add(new FoodItem(key, Float.parseFloat(map.get(key).toString()), 0));
//                                                     itemAdapter =
//                                                             new CustomAdapter(getApplicationContext(),
//                                                                     foodItems, OrderPageActivity.this);
//                                                     itemListView.setAdapter(itemAdapter);
//                                                 }
//                                             }else{
//                                                 Log.d("fuck","the documentM doesn't exist");
//                                             }
//                                         }
//                                     }
//                                 });
//                             }



//                         } else {
//                             Log.d(TAG, "Error getting documents: ", task.getException());
//                         }
//                     }
//                 });

//         orderButton.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 TextView totalCost = findViewById(R.id.totalCost);
//                 double orderTotal = Double.parseDouble(totalCost.getText().toString());
//                 String orderDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/"+Calendar.getInstance().get(Calendar.MONTH)+"/"+Calendar.getInstance().get(Calendar.YEAR);
//                 Order order = new Order(providerID,consumerID,false,false,false,"",orderTotal,foodItems,"",false,orderDate);

//                 DocumentReference docRef = db.collection("Orders").document();
//                 docRef.set(order);
//                 String doc = docRef.getId();
// //                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
// //                            @Override
// //                            public void onSuccess(DocumentReference documentReference) {
// //                                Log.d("newone","SuCCESS");
// //                            }
// //                        })
// //                        .addOnFailureListener(new OnFailureListener() {
// //                            @Override
// //                            public void onFailure(@NonNull Exception e) {
// //                                Log.d("newone","Failure");
// //                            }
// //                        });
//                 //                HashMap<String,Object> intentHash = new HashMap<String,Object>();
// ////                intentHash.put("foodItems",foodItems);
// //                Log.d("newone",foodItems.toString());
// //                Log.d("newone",foodItems.get(0).toString());
// //                intentHash.put("providerID",providerID);
// //                intentHash.put("type",type);
// //                intentHash.put("consumerID",consumerID);
// //                intentHash.put("orderTotal",orderTotal);
// //                Log.d("newone",foodItems.get(0).itemName + " " +foodItems.get(0).itemNumber + " " + foodItems.get(0).itemCost);
// ////                Log.d("newone",intentHash.get("foodItems").toString());
//                 Intent intentNew = new Intent(OrderPageActivity.this,CustomerConfirmActivity.class);


//                 intentNew.putExtra("docRef",doc);
//                 //                intentNew.putExtra("intentHash",intentHash);
// //                Bundle args = new Bundle();
// //                args.putSerializable("ARRAYLIST",(Serializable)foodItems);
// //                intentNew.putExtra("BUNDLE",args);
// ////                intentNew.putStringArrayListExtra("foodItems",foodItems);
//                  startActivity(intentNew);
//             }
//         });
//     }
// }

package com.example.student.homemade;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;

class Order {
    String provider;
    String consumer;
    boolean completed;
    boolean delivered;
    boolean paid;
    String orderTime;
    double orderTotal;
    ArrayList<FoodItem> itemsOrdered;
    String deliveryPerson;
    boolean isMassOrder;
    String orderDate;

    public Order(String provider, String consumer, boolean completed, boolean delivered, boolean paid, String orderTime, double orderTotal, ArrayList<FoodItem> itemsOrdered, String deliveryPerson, boolean isMassOrder, String orderDate) {

        this.provider = provider;
        this.consumer = consumer;
        this.completed = completed;
        this.delivered = delivered;
        this.paid = paid;
        this.orderTime = orderTime;
        this.orderTotal = orderTotal;
        this.itemsOrdered = itemsOrdered;
        this.deliveryPerson = deliveryPerson;
        this.isMassOrder = isMassOrder;
        this.orderDate = orderDate;
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

    public void setItemsOrdered(ArrayList<FoodItem> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public void setMassOrder(boolean massOrder) {
        isMassOrder = massOrder;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public ArrayList<FoodItem> getItemsOrdered() {
        return itemsOrdered;
    }

    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    public boolean isMassOrder() {
        return isMassOrder;
    }

    public String getOrderDate() {
        return orderDate;
    }
}

class FoodItem {

    String itemName;
    float itemCost;
    int itemNumber;

    public FoodItem(String itemName, float itemCost, int itemNumber) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemNumber = itemNumber;
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

class Provider {
    String userID;
    String menu;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}

//class Order {
//
//    FoodItem[] orderedItems;
//    float totalCost;
//
//    public Order(FoodItem[] orderedItems, float totalCost) {
//        this.orderedItems = orderedItems;
//        this.totalCost = totalCost;
//    }
//}

class Item {
    List<String> content;
    String menu_type;

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }
}



public class OrderPageActivity extends AppCompatActivity {

    private static final String TAG = OrderPageActivity.class.getName();
    ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
    ListAdapter itemAdapter;
    ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        Button orderButton = findViewById(R.id.orderButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");

        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("restaurantName");

        itemListView = (ListView) findViewById(R.id.itemList);
//        FirebaseApp.initializeApp(this);

//        final String providerID = "vMR09oO90SbUtCapURrudg5QMlw2";
//        final String type = "Breakfast";

        final String consumerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("consumerid",consumerID);

        db.collection("Provider")
                .whereEqualTo("restaurantName",restaurantName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String p="";
                            for(QueryDocumentSnapshot document : task.getResult()){
                                p = document.getId();
                            }
                            final String providerID = p;
                            Log.d("newone",providerID);

                            if(providerID==""){
                                Log.d("newone","ERROR");
                            }
                            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            String t;
                            if(hour >= 6 && hour < 12){
                                t = "Breakfast";
                            }else if(hour>=12 && hour < 16){
                                t = "Lunch";
                            }else if(hour>=16 && hour < 20){
                                t = "Snacks";
                            }else{
                                t = "Dinner";
                            }
                            final String type = t;
                            Log.d("newone",type);
                            db.collection("Provider")
                                    .whereEqualTo("id", providerID)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for(QueryDocumentSnapshot document : task.getResult()){
                                                    Log.d("newone","entered the arena");
                                                    db.collection("Provider").document(document.getId())
                                                            .collection("menu").document(type).get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.isSuccessful()){
                                                                        DocumentSnapshot documentM = task.getResult();
                                                                        if(documentM.exists()) {
                                                                            HashMap<String, Object> map = (HashMap<String, Object>) documentM.getData().get("items");
                                                                            for(String key : map.keySet()){
                                                                                foodItems.add(new FoodItem(key, Float.parseFloat(map.get(key).toString()), 0));
                                                                                itemAdapter =
                                                                                        new CustomAdapter(getApplicationContext(),
                                                                                                foodItems, OrderPageActivity.this);
                                                                                itemListView.setAdapter(itemAdapter);
                                                                            }
                                                                        }else{
                                                                            Log.d("fuck","the documentM doesn't exist");
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }



                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                            orderButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TextView totalCost = findViewById(R.id.totalCost);
                                    double orderTotal = Double.parseDouble(totalCost.getText().toString());
                                    int currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                                    String orderDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/"+currMonth+"/"+Calendar.getInstance().get(Calendar.YEAR);
                                    Order order = new Order(providerID,consumerID,false,false,false,"",orderTotal,foodItems,"",false,orderDate);

                                    DocumentReference docRef = db.collection("Orders").document();
                                    docRef.set(order);
                                    String doc = docRef.getId();
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d("newone","SuCCESS");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("newone","Failure");
//                            }
//                        });
                                    //                HashMap<String,Object> intentHash = new HashMap<String,Object>();
////                intentHash.put("foodItems",foodItems);
//                Log.d("newone",foodItems.toString());
//                Log.d("newone",foodItems.get(0).toString());
//                intentHash.put("providerID",providerID);
//                intentHash.put("type",type);
//                intentHash.put("consumerID",consumerID);
//                intentHash.put("orderTotal",orderTotal);
//                Log.d("newone",foodItems.get(0).itemName + " " +foodItems.get(0).itemNumber + " " + foodItems.get(0).itemCost);
////                Log.d("newone",intentHash.get("foodItems").toString());
//
//
//
                                    Intent intentNew = new Intent(OrderPageActivity.this,CustomerConfirmActivity.class);


                                    intentNew.putExtra("docRef",doc);
                                    //                intentNew.putExtra("intentHash",intentHash);
//                Bundle args = new Bundle();
//                args.putSerializable("ARRAYLIST",(Serializable)foodItems);
//                intentNew.putExtra("BUNDLE",args);
////                intentNew.putStringArrayListExtra("foodItems",foodItems);
                                    startActivity(intentNew);



                                    ///////////////
//                                    Intent intent = new Intent(OrderPageActivity.this,CustomerConfirmSubscriptionActivity.class);
//                                    intent.putExtra("subscriptionTime","4");
//                                    intent.putExtra("customerID","crlWbcGPeSPpIUD4ZhGSkuGbkQG2");
//                                    intent.putExtra("providerID","vMR09oO90SbUtCapURrudg5QMlw2");
//                                    startActivity(intent);
                                    ///////////////////
                                }
                            });

                        }
                    }
                });


    }
}

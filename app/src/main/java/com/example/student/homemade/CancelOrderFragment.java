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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CancelOrderFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private ArrayList<OrderInfo> mExampleList;
    private ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
    private RecyclerView mRecyclerview;
    private static final String TAG = "CANCELORDERFRAGMENT";
    private CancelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int myconsumerID;
    View v;

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
        v = inflater.inflate(R.layout.activity_cancel, container, false);
        myconsumerID = 113;
        //createExampleList();
        buildRecyclerView();
        return v;
    }
    public void removeItem(int position){

        OrderInfo currOrder = orderInfos.get(position);
        int orderID = currOrder.getOrderID();

        Log.v("========", "order id is " + orderID);

        db.collection("Orders").document("13")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("=======", "DocumentSnapshot successfully deleted!");
                        inform("Successfully cancelled order");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("========", "Error deleting document", e);
                    }
                });

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
                        orderInfos.remove(position1);
                        mAdapter.notifyItemRemoved(position1);

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

                OrderInfo orderToCancel = orderInfos.get(position);

                String time_and_date = orderToCancel.time_and_date;
                int timeBeforeCancel = orderToCancel.timeBeforeCancel;

                int hour = Integer.parseInt(time_and_date.substring(0, time_and_date.indexOf(':')));
                int mins = Integer.parseInt(time_and_date.substring(time_and_date.indexOf(':') + 1));

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                Log.d("=========", "hour " + hour + " mins " + mins + " currentHour " + currentHour + " currentMinute " + currentMinute + " timeBeforeCancel " + timeBeforeCancel);

                if(currentHour * 60 + currentMinute < hour * 60 + mins + timeBeforeCancel) {
                    removeItem(position);
                }
                else{
                    inform("Sorry, too late to cancel this order.");
                }
            }
        });

        db.collection("Orders").whereEqualTo("client", myconsumerID).whereEqualTo("delivered", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                OrderInfo orderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d(TAG, "client id :" + map.get("client"));
                        orderInfo = new OrderInfo(Integer.parseInt(map.get("Provider").toString()), Integer.parseInt(map.get("client").toString()), (Boolean) map.get("completed"), (Boolean) map.get("delivered"), Integer.parseInt(map.get("delivery_person").toString()), Integer.parseInt(map.get("orderID").toString()), (Boolean) map.get("paid"), (ArrayList) map.get("things_ordered"), (String) map.get("time_and_date"), Integer.parseInt(map.get("timeBeforeCancel").toString()), Float.parseFloat(map.get("total_cost").toString()));
                        mAdapter.added(orderInfo);
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
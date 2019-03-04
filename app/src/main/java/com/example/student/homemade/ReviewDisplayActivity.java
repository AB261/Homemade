package com.example.student.homemade;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewDisplayAdapter reviewDisplayAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    public String myproviderID = "13";//        FirebaseAuth.getInstance().getUid()
    public String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);

        recyclerView = findViewById(R.id.reviewrv);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reviewDisplayAdapter = new ReviewDisplayAdapter(this, new ArrayList<ReviewInfo>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(reviewDisplayAdapter);

        Log.d("user", mAuth.getUid() + "!");


        fetch();


    }


    public void fetch() {
        firebaseFirestore.collection("Reviews and Ratings").whereEqualTo("reviewee", myproviderID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ReviewInfo reviewInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d("USER : ", document.get("reviewer").toString());
                        username = findusername(document,map);
//                        Log.d("Username",username);
//                        reviewInfo = new ReviewInfo(Integer.parseInt(map.get("ratings").toString()), map.get("review").toString(), map.get("reviewID").toString(), map.get("reviewee").toString(), username, map.get("date").toString());
//                        reviewDisplayAdapter.added(reviewInfo);
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    public String findusername(QueryDocumentSnapshot document, final HashMap<String, Object> map){

        final String[] usernamearray = new String[1];
        firebaseFirestore.collection("Consumer").whereEqualTo("id", document.get("reviewer")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                ReviewInfo reviewInfo;
                if (task2.isSuccessful()) {
                    for (QueryDocumentSnapshot document2 : task2.getResult()) {
                        Log.d("USERINFO ",document2.get("username").toString());
                         username = document2.get("username").toString();
                         Log.d("HERE",username);
                        reviewInfo = new ReviewInfo(Integer.parseInt(map.get("ratings").toString()), map.get("review").toString(), map.get("reviewID").toString(), map.get("reviewee").toString(), username, map.get("date").toString());
                        reviewDisplayAdapter.added(reviewInfo);
//                         username[0] = document2.get("username").toString();

//                                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                    }
                } else {
                    Log.d("INSIDE ERROR", "Error getting documents: ", task2.getException());
                }
            }
        });
//        Log.d("BROOOROROOROR",username);
        return null;
    }
}

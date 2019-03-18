package com.example.student.homemade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.ui.ConsumerUIFragment;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RatingandReviewActivity extends AppCompatActivity {
    RatingBar ratebar;
    TextView tvrate;
    Button btn;
    EditText et;
    double rating;
    String review;
    String date;
    int reviewID = 1234;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("RatingandReviewActivity");
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        et = findViewById(R.id.editText3);
        btn = findViewById(R.id.btnsubmit);
        ratebar = findViewById(R.id.ratebar);
        tvrate = findViewById(R.id.tvrate);
        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvrate.setText("Rated : " + rating);
            }
        });
        rating = ratebar.getNumStars();
        Log.d("Rating",rating + "");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review=et.getText().toString();
                validate(review);
            }
        });

        Log.d("Review" ,review);
    }
    private void validate(String rev){

        if(rev.isEmpty()){
            Toast.makeText(this,"Please Enter all details", Toast.LENGTH_SHORT).show();
        }
        else {


        review = et.getText().toString();
        rating = ratebar.getRating();
        reviewID = reviewID+1;
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        date=timeStamp;
        Map<String, Object> user = new HashMap<>();
        user.put("date ",date);
        user.put("ratings", rating);
        user.put("review", review);
        user.put("reviewID", reviewID);
        user.put("reviewee", 1815);
        user.put("reviewer",mAuth.getUid() );
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

// Add a new document with a generated ID
        db.collection("Reviews and Ratings")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("hjughjig", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("qwerty", "Error adding document", e);
                    }
                });
            Intent intent = new Intent(RatingandReviewActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}

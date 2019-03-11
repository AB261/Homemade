package com.example.student.homemade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RatingandReviewActivity extends AppCompatActivity {
    RatingBar ratebar;
    TextView tvrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("RatingandReviewActivity");
        setSupportActionBar(toolbar);

        ratebar = findViewById(R.id.ratebar);
        tvrate = findViewById(R.id.tvrate);
        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvrate.setText("Rated : " + rating);

            }
        });

        String userID = FirebaseAuth.getInstance().getUid();
    }
}
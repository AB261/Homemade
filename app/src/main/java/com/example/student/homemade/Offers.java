package com.example.student.homemade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.student.homemade.ui.ConsumerUIFragment;

public class Offers extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        btn=(Button)findViewById(R.id.back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent = new Intent(Offers.this, MainActivity.class);
                // myIntent.putExtra("key", value); //Optional parameters
                  startActivity(mintent);
            }
        });
    }
}

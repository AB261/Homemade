package com.example.student.homemade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.student.homemade.ui.ConsumerUIFragment;

public class Success_sub extends AppCompatActivity {
    Button btn,b_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_sub);

        btn = (Button) findViewById(R.id.btn_offer);
        b_btn=(Button)findViewById(R.id.b_btn) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Success_sub.this, Offers.class);
                // myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });
        b_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent = new Intent(Success_sub.this, MainActivity.class);
                // myIntent.putExtra("key", value); //Optional parameters
                startActivity(mintent);
            }
        });
    }
}

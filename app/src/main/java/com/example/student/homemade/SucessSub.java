package com.example.student.homemade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.student.homemade.ui.ConsumerUIFragment;

public class SucessSub extends AppCompatActivity {
    Button btn,b_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess_sub);
        btn = (Button) findViewById(R.id.btn_offer);
        b_btn=(Button)findViewById(R.id.btn_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SucessSub.this, OffersActivity.class);
                // myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });
        b_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SucessSub.this, ConsumerUIFragment.class);
                startActivity(intent);
            }
        });
    }
}

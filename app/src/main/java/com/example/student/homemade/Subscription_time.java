package com.example.student.homemade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Subscription_time extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    private FirebaseAuth mAuth;
    Intent intent;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_subscription);
        mAuth = FirebaseAuth.getInstance();
        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);
        intent =getIntent();
        String restaurantName = intent.getStringExtra("restaurantName");

        Button buttonApply = findViewById(R.id.button_apply);
        Log.d("Hey","JHere");

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(radioId);
                String choice = (String) radioButton.getText();
                textView.setText("Your choice: " + choice);
                Intent intent = new Intent(getBaseContext(), OrderPageActivity.class);
                //intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("subscriptionTime", choice);
                intent.putExtra("consumerID", mAuth.getUid());
                intent.putExtra("providerID",restaurantName);
                startActivity(intent);

            }
        });
        Log.d("Hey","HereAlso");
        db.collection("Provider").whereEqualTo("restaurantName",restaurantName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                OrderInfo orderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HashMap<String,Object> map = (HashMap<String,Object>)document.getData();
                        HashMap<String,String> subs = (HashMap<String,String>)map.get("Subscriptions");
//                        subs.put("restaurantName","123456");
                        db.collection("Consumer").document(mAuth.getUid()).update("Subscriptions",subs);
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                        HashMap<String, Object> m = (HashMap<String, Object>) document.getData();
//                            String providerID=m.get("Provider").toString();
                    }
                }
            }

        });
        Log.d("Hey","AlsoHere");
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }



}

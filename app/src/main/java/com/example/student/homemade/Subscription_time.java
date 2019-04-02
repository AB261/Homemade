package com.example.student.homemade;

;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Subscription_time extends AppCompatActivity {

    RadioGroup radioGroup;
    private FirebaseFirestore firebaseFirestore;
    RadioButton radioButton;
    TextView textView;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_food_subscription);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String providerID = intent.getStringExtra("providerID");
        String restaurantName = intent.getStringExtra("restaurantName");
        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);
//        HashMap<String, HashMap> Subscriptions = new HashMap<>();
//        HashMap<String, String> m = new HashMap<>();
//        m.put(providerID, String.valueOf(System.currentTimeMillis()));
//        Subscriptions.put("Subscription", m);
//        firebaseFirestore.collection("Provider").document(FirebaseAuth.getInstance().getUid())
//                .set(Subscriptions)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("SUCCESS", "SUCCESS");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("MACHSAAAADASAS", e.toString());
//                    }
//                });
        Button buttonApply = findViewById(R.id.button_apply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(radioId);
                String[] choices = radioButton.getText().toString().split(" ");
                textView.setText("Your choice: " + choices[0]);

                Intent intent = new Intent(getBaseContext(), CustomerConfirmSubscriptionActivity.class);
//              //intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("subscriptionTime", choices[0]);

                Map<String, Object> sub = new HashMap<>();
                sub.put(providerID, choices[0]);
                HashMap<String,Object> m = new HashMap<>();
                m.put("Subscription",sub);

// Add a new document with a generated ID
                db.collection("Consumer").document(mAuth.getUid()).set(m, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success","s");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("FSA",e.toString());
                            }
                        });


                intent.putExtra("consumerID", mAuth.getUid());
                intent.putExtra("providerID", providerID);
                startActivity(intent);
            }
        });

    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        Toast.makeText(this, "You have selected : " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
}


///////////////////////////////////////////


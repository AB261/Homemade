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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Subscription_time extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_subscription);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("restaurantName");
        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);
        Button buttonApply = (Button) findViewById(R.id.button_apply);
        db.collection("Provider").whereEqualTo("restaurantName", restaurantName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                OrderInfo orderInfo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                       final String providerID=map.get("restaurantName").toString();

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
                                intent.putExtra("providerID",providerID);
                                startActivity(intent);

                            }
                        });



                    }
                }
            }
        });



//        buttonApply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int radioId = radioGroup.getCheckedRadioButtonId();
//                radioButton = findViewById(radioId);
//                String choice = (String) radioButton.getText();
//                textView.setText("Your choice: " + choice);
//                Intent intent = new Intent(getBaseContext(), OrderPageActivity.class);
//                //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//                intent.putExtra("subscriptionTime", choice);
//                intent.putExtra("consumerID", mAuth.getUid());
//                intent.putExtra("providerID",providerID);
//                startActivity(intent);
//
//            }
//        });


    checkButton();
        Map<String, Object> user = new HashMap<>();
        user.put("Subscriptions", "aafgd");


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("qwerty", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hey", "Error adding document", e);
                    }
                });
    }


    public void checkButton() {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
}

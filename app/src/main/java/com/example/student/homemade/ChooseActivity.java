package com.example.student.homemade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ChooseActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CardView breakfast;
    Button update;
    CardView lunch;
    CardView snacks;
    CardView dinner;
    private HashMap<String, String> itemPictures = new HashMap<>();
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("guh", "ss");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

itemPictures = (HashMap<String,String>)getIntent().getExtras().get("itemPictures");
        breakfast = findViewById(R.id.button_breakfast);

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseActivity.this, MenuActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                intent.putExtra("type", "Breakfast");
                startActivity(intent);
            }
        });


        lunch = findViewById(R.id.button_lunch);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, MenuActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                intent.putExtra("type", "Lunch");
                startActivity(intent);
            }
        });

        dinner = findViewById(R.id.button_dinner);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, MenuActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                intent.putExtra("type", "Dinner");
                startActivity(intent);
            }
        });

        snacks = findViewById(R.id.button_snacks);
        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, MenuActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                intent.putExtra("type", "Snacks");
                startActivity(intent);
            }
        });

        update = findViewById(R.id.button_update);
        update.setVisibility(View.GONE);
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChooseActivity.this,UploadActivity.class);
////                intent.putExtra(type);
//                startActivity(intent);
//            }
//        });

        //
//                Intent val = getIntent();
//                val.getStringExtra("type");
//                //Get menu type and
//                db.collection("Menu").orderBy("menuID", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("THIS IS  ", document.getId() + " => " + document.getData());
//                                HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
//                                Log.d("MENU TYPE =  ", "" + map.get("menu_type"));
//                                String menu_type = map.get("menu_type").toString();
//                                if(menu_type.equals("Breakfast")) {
//                                    Intent intent = new Intent(ChooseActivity.this, BreakfastActivity.class);
//                                    intent.putExtra("type", "breakfast");
//                                    startActivity(intent);
//                                }
//                                else if(menu_type.equals("Lunch")) {
//                                    Intent intent = new Intent(ChooseActivity.this, LunchActivity.class);
//                                    intent.putExtra("type", "lunch");
//                                    startActivity(intent);
//                                }
//                                else if(menu_type.equals("Dinner")) {
//                                    Intent intent = new Intent(ChooseActivity.this, DinnerActivity.class);
//                                    intent.putExtra("type", "dinner");
//                                    startActivity(intent);
//                                }
//                                else if(menu_type.equals("Snacks")) {
//                                    Intent intent = new Intent(ChooseActivity.this, SnacksActivity.class);
//                                    intent.putExtra("type", "snacks");
//                                    startActivity(intent);
//                                }
//
//                            }
//                        } else {
//                            Log.d("R", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//            }
//        });

    }
}

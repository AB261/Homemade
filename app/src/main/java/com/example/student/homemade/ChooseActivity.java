package com.example.student.homemade;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CardView breakfast;
    Button add_custom_items;
    CardView lunch;
    CardView snacks;
    CardView dinner;
    FirebaseFirestore firebaseFirestore;
    private Seller seller;
    private HashMap<String, String> itemPictures = new HashMap<>();
    private ArrayList<String> customItems = new ArrayList<>();
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
        seller = (Seller)getIntent().getExtras().getParcelable("seller");
        firebaseFirestore = FirebaseFirestore.getInstance();
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseActivity.this, MenuActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                intent.putExtra("customItems",customItems);
                intent.putExtra("seller",seller);
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
                intent.putExtra("customItems",customItems);
                intent.putExtra("seller",seller);
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
                intent.putExtra("customItems",customItems);
                intent.putExtra("seller",seller);
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
                intent.putExtra("customItems",customItems);
                intent.putExtra("seller",seller);
                intent.putExtra("type", "Snacks");
                startActivity(intent);
            }
        });

        add_custom_items = findViewById(R.id.add_custom_items);
        add_custom_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.myDialog);

                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View dialogView = inflater.inflate(R.layout.dialog_new_item, null);
                builder.setView(dialogView);

                builder.setTitle("Add new item");
                /*AlertDialog alertDialog = builder.create();
                alertDialog.show();*/

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText itemName = dialogView.findViewById(R.id.wallet_et_email);
                        //EditText itemPrice = dialogView.findViewById(R.id.wallet_et_password);

                        firebaseFirestore.collection("Provider").document(FirebaseAuth.getInstance().getUid())
                                .update("customItems",FieldValue.arrayUnion(itemName.getText().toString()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        customItems.add(itemName.getText().toString());
                                        Log.d("SUCCESS", "SUCCESS");
                                        seller.getCustomItems().add(itemName.getText().toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("MACHSAAAADASAS", e.toString());
                                    }
                                });

                    }

                });
                builder.setView(dialogView);
                builder.show();


            }
        });
//        add_custom.setOnClickListener(new View.OnClickListener() {
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

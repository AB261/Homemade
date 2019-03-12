package com.example.student.homemade;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private MenuItemAdapter menuAdapter;
    private Button submit;
    private Button update;
    private String type;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private TextView title;
    private DatabaseReference mDatabaseRef;
    private ArrayList<MenuItem> mUploads;
    private HashMap<String, String> itemPictures = new HashMap<>();
    private ArrayList<MenuItem> menuItems;
    private Seller seller;
    private ArrayList<String> present = new ArrayList<>();
    private ArrayList<String> absent = new ArrayList<>();
    private String[] stringA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        itemPictures = (HashMap<String,String>)getIntent().getExtras().get("itemPictures");
        type = (String) getIntent().getExtras().get("type");
        recyclerView = findViewById(R.id.rv);
        seller = (Seller)getIntent().getExtras().getSerializable("seller");
        fab = findViewById(R.id.add_item);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        menuItems = (ArrayList<MenuItem>)getIntent().getExtras().get("menuItems");
        if(menuItems == null) {
            menuAdapter = new MenuItemAdapter(this, new ArrayList<MenuItem>(), itemPictures, type);
            fetch();
        }
        else{
            menuAdapter = new MenuItemAdapter(this, menuItems, itemPictures, type);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(menuAdapter);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        Log.d("MENUADAPATER HERE",menuAdapter.getItemNames().toString());
        title.setText(type);

        Log.d("user", mAuth.getUid() + "!");

        Log.d("SELLER IS",seller.toString());
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        Log.d("DBREF",mDatabaseRef.toString());


//        ArrayList<String> dummy =


//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("SDLKJFKSDJFLKSJDFKLJSDF","asdkfhask");
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    MenuItem upload = postSnapshot.getValue(MenuItem.class);
//                    mUploads.add(upload);
//                }
//
//                menuAdapter = new MenuItemAdapter(MenuActivity.this, mUploads);
//
//                recyclerView.setAdapter(menuAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("HERERERERRE","HETRERER");
//                Toast.makeText(MenuActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


//        update = findViewById(R.id.upload_image);
////        update.setVisibility(View.GONE);
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MenuActivity.this,UploadActivity.class);
//                intent.putExtra("type",type);
//                startActivity(intent);
//            }
//        });

//        mUploads = new ArrayList<>();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
//        Log.d("DBREF",mDatabaseRef.toString());
//
//
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("SDLKJFKSDJFLKSJDFKLJSDF","asdkfhask");
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    MenuItem upload = postSnapshot.getValue(MenuItem.class);
//                    mUploads.add(upload);
//                }
//
//                menuAdapter = new MenuItemAdapter(MenuActivity.this, mUploads);
//
//                recyclerView.setAdapter(menuAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("HERERERERRE","HETRERER");
//                Toast.makeText(MenuActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

    Log.d("PRESENT ARRAY IN MENU",present.toString());
        update = findViewById(R.id.upload_image);
//        update.setVisibility(View.GONE);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] list = getResources().getStringArray(R.array.food_array);
                List<String> l = Arrays.<String>asList(list);
                Log.d("THIS IS l ARRAY",l.toString());
                ArrayList<String> al = new ArrayList<String>(l);
                Log.d("THIS IS al ARRAY before",al.toString());
                al.addAll(seller.getCustomItems());

                Log.d("THIS IS al ARRAY",al.toString());
                Log.d("menuadapter",menuAdapter.getItemNames().toString());
                for(String s:al){

                    if(menuAdapter.getItemNames().contains(s) && !present.contains(s))
                    {
                        present.add(s);
                    }
                    else if(!absent.contains(s) && !present.contains(s)){
                        absent.add(s);
                    }
                }
                Log.d("THIS IS present ARRAY",present.toString());
                Log.d("THIS IS absent ARRAY",absent.toString());
                stringA = new String[absent.size()];
                absent.toArray(stringA);
                for(int i=0;i<absent.size();i++)
                    Log.d("stringA",stringA[i]);

                Intent intent = new Intent(MenuActivity.this,UploadActivity.class);
                intent.putExtra("itemPictures",itemPictures);
                intent.putExtra("seller", seller);
                intent.putExtra("menuItems",menuAdapter.getItems());
                intent.putExtra("present",present);
                intent.putExtra("absent",absent);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItems(v);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Long> map = new HashMap<>();

                int size = menuAdapter.getItemCount();
                Log.d("BRO", "" + size);
                if (size != 0) {
                    Boolean update = true;
                    for (MenuItem menuItem : menuAdapter.getItems()) {

                        if (menuItem.getPrice() == 0L) {
                            Log.d("tag", menuItem.getName());
                            update = false;

                            menuAdapter.notifyItemChanged(menuAdapter.getItems().indexOf(menuItem));
                        }
                        map.put(menuItem.getName(), menuItem.getPrice());
                        Log.d("UPDDATE", update + " ");
                    }
                    if (update) {

                        Log.d("INSIDE", update + " ");
                        HashMap<String, HashMap> m = new HashMap<>();
                        m.put("items", map);
                        Log.d(" ID ", FirebaseAuth.getInstance().getUid());
                        firebaseFirestore.collection("Provider").document(FirebaseAuth.getInstance().getUid())
                                .collection("menu").document(type)
                                .set(m)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("SUCCESS", "SUCCESS");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("MACHSAAAADASAS", e.toString());
                                    }
                                });
                        Toast.makeText(MenuActivity.this,"Menu uploaded",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                }
                else{
                    Toast.makeText(MenuActivity.this,"Enter Price",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void selectItems(View view) {
        String[] list = getResources().getStringArray(R.array.food_array);
        List<String> l = Arrays.<String>asList(list);
        Log.d("THIS IS l ARRAY",l.toString());
        ArrayList<String> al = new ArrayList<String>(l);
        Log.d("THIS IS al ARRAY before",al.toString());
        al.addAll(seller.getCustomItems());

        Log.d("THIS IS al ARRAY",al.toString());
        Log.d("menuadapter",menuAdapter.getItemNames().toString());
        for(String s:al){

            if(menuAdapter.getItemNames().contains(s) && !present.contains(s))
            {
                present.add(s);
            }
            else if(!absent.contains(s) && !present.contains(s)){
                absent.add(s);
            }
        }
        Log.d("THIS IS present ARRAY",present.toString());
        Log.d("THIS IS absent ARRAY",absent.toString());
        stringA = new String[absent.size()];
        absent.toArray(stringA);
        for(int i=0;i<absent.size();i++)
            Log.d("stringA",stringA[i]);

        final ArrayList<Integer> selectedList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.myDialog);
        builder.setTitle("Select Items");

        builder.setMultiChoiceItems(stringA, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            selectedList.add(which);
                            Log.d("ischecked", selectedList.size() + "!");
                        } else if (selectedList.contains(which)) {
                            Log.d("not checked", selectedList.size() + "!");
                            selectedList.remove(selectedList.indexOf(which));
                        }

                    }
                });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<MenuItem> menuItems = new ArrayList<>();

                for (Integer i : selectedList) {

                    MenuItem menuItem = new MenuItem();
                    menuItem.setName(stringA[i]);
                    menuItem.setPrice(0L);
                    menuItems.add(menuItem);
                    menuAdapter.added(menuItem);

                }

            }
        });
        builder.show();

    }

    public void fetch() {

        Log.d("TYPEBRO",type);
        firebaseFirestore.collection("Provider").document(FirebaseAuth.getInstance().getUid())
                .collection("menu").document(type)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.getData() != null) {
                            //HashMap<String, >
//                            Log.d("tag", documentSnapshot.get("items").toString());
                            HashMap<String, Long> map = (HashMap<String, Long>) documentSnapshot.get("items");

                            for (Map.Entry<String, Long> entry : map.entrySet()) {

                                MenuItem menuItem = new MenuItem();
                                menuItem.setPrice(entry.getValue());
                                menuItem.setName(entry.getKey());
                                menuAdapter.added(menuItem);
                            }
                            menuAdapter.setMap(map);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}

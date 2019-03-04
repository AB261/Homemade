package com.example.student.homemade;

import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private MenuItemAdapter menuAdapter;
    private Button submit;
    private String type;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        type = (String) getIntent().getExtras().get("type");
        recyclerView = findViewById(R.id.rv);
        fab = findViewById(R.id.add_item);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        menuAdapter = new MenuItemAdapter(this, new ArrayList<MenuItem>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(menuAdapter);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);

        title.setText(type);
        Log.d("user", mAuth.getUid() + "!");

        fetch();

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
                    }

                }
            }
        });


    }

    public void selectItems(View view) {

        final String[] list = getResources().getStringArray(R.array.food_array);
        final ArrayList<Integer> selectedList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Select Items");

        builder.setMultiChoiceItems(list, null,
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
                    menuItem.setName(list[i]);
                    menuItem.setPrice(0L);
                    menuItems.add(menuItem);
                    menuAdapter.added(menuItem);

                }

            }
        });
        builder.show();

    }

    public void fetch() {

        firebaseFirestore.collection("Provider").document(FirebaseAuth.getInstance().getUid())
                .collection("menu").document(type)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.getData() != null) {
                            //HashMap<String, >
                            //Log.d("tag", documentSnapshot.get("items").toString());
                            HashMap<String, Long> map = (HashMap<String, Long>) documentSnapshot.get("items");

                            for (Map.Entry<String, Long> entry : map.entrySet()) {

                                MenuItem menuItem = new MenuItem();
                                menuItem.setPrice(entry.getValue());
                                menuItem.setName(entry.getKey());
                                menuAdapter.added(menuItem);

                            }

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

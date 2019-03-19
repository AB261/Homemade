package com.example.student.homemade.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.homemade.ConsumerDetailsClass;
import com.example.student.homemade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    Map m;
    Button savePassword;
    EditText newPassword, oldPassword;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference consumerRef = db.collection("Consumer").document(FirebaseAuth.getInstance().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        newPassword = findViewById(R.id.etNewPassword);
        savePassword = findViewById(R.id.btnSavePassword);
        oldPassword = findViewById(R.id.etOldPassword);
        progressDialog = new ProgressDialog(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    void changePassword() {
        String newPass = newPassword.getText().toString();
        if (newPass.equals("")) {
            Toast.makeText(this, "ENTER NEW PASSWORD!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldPassword.getText().toString().equals("")) {
            Toast.makeText(this, "ENTER OLD PASSWORD!!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email = firebaseUser.getEmail();
        String oldpass = oldPassword.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldpass);
        progressDialog.show();
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    progressDialog.setMessage("CHANGING PASSWORD ,HOLD ON!!");
                    firebaseUser.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                makechangesinfirestore(newPassword.getText().toString());
                                progressDialog.dismiss();
                                Toast.makeText(ChangePassword.this, "PASSWORD UPDATED", Toast.LENGTH_SHORT).show();


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(ChangePassword.this, "Update Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                } else {
                    Toast.makeText(ChangePassword.this, "WRONG PASSWORD!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void makechangesinfirestore(String newpass) {
        Log.d("FUCK", newpass);
        consumerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                m = documentSnapshot.getData();
                if (m.get("password") != null) {
                    m.put("password", newpass);
                    consumerRef.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChangePassword.this, "SUCCESSULLY UPDATED DATABASE", Toast.LENGTH_SHORT).show();
                            changeFragment();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePassword.this, "CANNOT PUT NEW PASSWORD IN THE DATABASE", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangePassword.this, "CANNOT GET OLD PASSWORD FROM THE DATABASE!", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    void changeFragment() {
        finish();
    }

}

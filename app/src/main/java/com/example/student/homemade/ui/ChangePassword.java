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

import com.example.student.homemade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePassword extends AppCompatActivity {

    Button savePassword;
    EditText newPassword,oldPassword;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

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

    void changePassword(){
        String newPass = newPassword.getText().toString();
        if(newPass.equals("")) {
            Toast.makeText(this, "ENTER NEW PASSWORD!!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email = firebaseUser.getEmail();
        String oldpass = oldPassword.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);

        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    progressDialog.setMessage("CHANGING PASSWORD ,HOLD ON!!");
                    firebaseUser.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ChangePassword.this, "PASSWORD UPDATED", Toast.LENGTH_SHORT).show();
                                try{
                                    ConsumerUIFragment fragment = new ConsumerUIFragment();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment)
                                            .commit();
                                }
                                catch (Exception e){
                                    Log.i("jump",e.toString());
                                }
                            }
                            else {
                                progressDialog.dismiss();

                                Toast.makeText(ChangePassword.this, "Update Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
                else{
                    Toast.makeText(ChangePassword.this, "REAUTHENTICATION FAILURE", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}

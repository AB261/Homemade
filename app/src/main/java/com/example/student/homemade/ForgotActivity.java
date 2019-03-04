package com.example.student.homemade;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Arrays;

public class ForgotActivity extends AppCompatActivity {
    private Button btnReset;
    private FirebaseAuth mAuth;
    private EditText enEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        mAuth = FirebaseAuth.getInstance();
        enEmail = findViewById(R.id.enEmail);
        btnReset = findViewById(R.id.btnReset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email;
                email = enEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    //Toast.makeText(getApplication(), "Enter your registered email and press \"Forgot Password\"", Toast.LENGTH_LONG).show();
                    inform("Empty password.");

                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);

                mAuth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(LoginActivity.this, "We have sent you instructions at your email to reset your password!", Toast.LENGTH_LONG).show();
                                    informModified("Email successfully sent!");
                                    //finish();

                                } else {
                                    //Toast.makeText(LoginActivity.this, "This email is not registered in this app", Toast.LENGTH_LONG).show();
                                    inform("This is an unregistered email. Please ensure that you enter a valid email.");
                                }

                                //progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private void inform(String string){
        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_INDEFINITE).setDuration(8000);

        snack.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        }).setActionTextColor(findViewById(android.R.id.content).getResources().getColor(R.color.white));

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(findViewById(android.R.id.content).getResources().getColor(R.color.white));

        view.setBackgroundColor(findViewById(android.R.id.content).getResources().getColor(R.color.signupColor));
        snack.show();
    }

    private void informModified(String string){
        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_INDEFINITE).setDuration(8000);

        snack.setAction("Go back to Login Page", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setActionTextColor(findViewById(android.R.id.content).getResources().getColor(R.color.white));

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(findViewById(android.R.id.content).getResources().getColor(R.color.white));

        view.setBackgroundColor(findViewById(android.R.id.content).getResources().getColor(R.color.signupColor));
        snack.show();
    }
}
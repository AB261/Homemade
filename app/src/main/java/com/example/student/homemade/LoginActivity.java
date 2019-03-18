package com.example.student.homemade;

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
import android.widget.ProgressBar;
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


public class LoginActivity extends AppCompatActivity {
    private static int RC_SIGN_IN = 100;
    private int flag = 0;


    private Button login, register, btnForgot, signInButton;
    private ProgressBar loginProgressBar;
    private EditText etEmail, etPass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    Button loginButton;
    private LoginManager loginManager;
    CallbackManager mCallbackManager;
    String TAG ="Hey";
    private Activity activity;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        FirebaseApp.initializeApp(getApplicationContext());
        loginManager = LoginManager.getInstance();
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnReg);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnForgot = findViewById(R.id.btnForgot);
        signInButton = findViewById(R.id.sign_in_button);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        loginProgressBar.setVisibility(View.INVISIBLE);

        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        //--------Passing Intent to Start Page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openStartPageActivity();
                openSplashPageActivity();
            }
        });




        //----------Forgot password below

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String email;
//                email = etEmail.getText().toString().trim();
//
//                if (TextUtils.isEmpty(email)) {
//                    //Toast.makeText(getApplication(), "Enter your registered email and press \"Forgot Password\"", Toast.LENGTH_LONG).show();
//                    inform("Enter your Registered Email and press \"Forgot Password\".");
//
//                    return;
//                }
//
//                //progressBar.setVisibility(View.VISIBLE);
//
//                mAuth.sendPasswordResetEmail(email)
//
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    //Toast.makeText(LoginActivity.this, "We have sent you instructions at your email to reset your password!", Toast.LENGTH_LONG).show();
//                                    inform("We have sent you instructions at your Email to reset your Password!");
//
//                                } else {
//                                    //Toast.makeText(LoginActivity.this, "This email is not registered in this app", Toast.LENGTH_LONG).show();
//                                    inform("This Email is not registered in this App.");
//                                }
//
//                                //progressBar.setVisibility(View.GONE);
//                            }
//                        });

                //----------changes above
                Intent forgotIntent = new Intent(activity, ForgotActivity.class);
                startActivity(forgotIntent);
            }
        });

        //----------Google below

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("853704140115-a7gjo5s8uvvsfig7ij7s1t6cg9ejh1ld.apps.googleusercontent.com")
                .requestEmail()
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });

        //----------Facebook below

        loginButton = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            postLogin();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //----------when clicked on login make the button invisible and make the progress bar visible
                login.setVisibility(View.INVISIBLE);
                loginProgressBar.setVisibility(View.VISIBLE);

                //----------login button prompt

                if(etEmail.getText().toString().isEmpty()){
                    //Toast.makeText(getApplicationContext(), "Empty Username", Toast.LENGTH_LONG).show();
                    inform("Empty Username");
                }
                else if(etPass.getText().toString().isEmpty()){
                    //Toast.makeText(getApplicationContext(), "Empty Password", Toast.LENGTH_LONG).show();
                    inform("Empty Password");
                }

                else{

                    //--------------firebase email password auth below


                    mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    postLoginWithIntent(etEmail.getText().toString());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_LONG).show();
                                    inform("Incorrect Username / Password");

                                    //------------If login fails make the login button visible again and the progress bar invisible
                                    login.setVisibility(View.VISIBLE);
                                    loginProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        });

        //-------------Facebook part
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logInWithReadPermissions(activity,
                        Arrays.asList("email"));
            }
        });
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i(TAG,"Hello"+loginResult.getAccessToken().getToken());
                //  Toast.makeText(MainActivity.this, "Token:"+loginResult.getAccessToken(), Toast.LENGTH_LONG).show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //---------------The below code may be needed later


//        mAuthListener = new FirebaseAuth.AuthStateListener(){
//
//
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user!=null){
//                    name = user.getDisplayName();
//                    //Toast.makeText(LoginActivity.this,""+user.getDisplayName(),Toast.LENGTH_LONG).show();
//                    //Snackbar.make(findViewById(android.R.id.content), "This email is not registered in this app", Snackbar.LENGTH_LONG).show();
//                }else {
//                    //Toast.makeText(LoginActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
//                    //Snackbar.make(findViewById(android.R.id.content), "This email is not registered in this app", Snackbar.LENGTH_LONG).show();
//                    inform("This Email is not registered in this App.");
//                }
//
//
//            }
//        };

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Toast.makeText(LoginActivity.this,"Google sign in failed",Toast.LENGTH_LONG).show();
                inform("Google Sign-In Failed");
                Log.w(TAG, "---------Google sign in failed", e);
                // ...
            }
        }

        //----------Facebook part

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            postLogin();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            postLoginWithIntent(user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("Google Error", task.getException().toString());
                            //Toast.makeText(LoginActivity.this, "Authentication error", Toast.LENGTH_LONG).show();
                            inform("Google Authentication Error");
                        }

                        // ...
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(LoginActivity.this, "Success",
//                                    Toast.LENGTH_LONG).show();
                            postLogin();

                        }else{
                            Log.e("------Error------", task.getException().toString());
                            //Toast.makeText(LoginActivity.this, "Authentication error. Consider updating the Facebook app if you have one.", Toast.LENGTH_LONG).show();
                            inform("Facebook Authentication Error. Consider updating the Facebook App");

                        }


                    }
                });
    }

    //---------------------------------------------------------
    private void postLogin(){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    //---------------------------------------------------------

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

    private void postLoginWithIntent(final String email){

        Log.d("LoginActivity", "string is "+email);
        SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email",email);
        editor.apply();



        db.collection("user").whereEqualTo("email", email).whereEqualTo("typeOfUser", "Provider").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot document = task.getResult();
                    Log.d("LoginActivity", "---printing"+document.getDocuments().toString());
                    if(document.getDocuments().size() != 0){
                        SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("ProviderOrConsumerFlag",1);
                        editor.commit();

                        Log.d("LoginActivity", "-----------Successfully found user in Provider");
                        //---------------------------Send intent to provider UI
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        //mainIntent.putExtra("ProviderOrConsumer", 1);
                        startActivity(mainIntent);
                        finish();
                    }
                    else{
                        SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("ProviderOrConsumerFlag",2);
                        editor.commit();
                        Log.d("LoginActivity", "-----------Successfully found user in Consumer");
                        //---------------------------Send intent to consumer UI
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        //mainIntent.putExtra("ProviderOrConsumer", 2);
                        startActivity(mainIntent);
                        finish();

                    }

//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            if (((String) document.getData().get("email")).equals(email)) {
//                                flag = 1;
//                                SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
//                                SharedPreferences.Editor editor = settings.edit();
//                                editor.putInt("ProviderOrConsumerFlag",1);
//                                editor.commit();
//
//                                Log.d("LoginActivity", "-----------Successfully found user in Provider");
//                                //---------------------------Send intent to provider UI
//                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                                //mainIntent.putExtra("ProviderOrConsumer", 1);
//                                startActivity(mainIntent);
//                                finish();
//                            } else {
//                                Log.d("LoginActivity", "-----------Not found in Provider");
//                            }
//
//                            break;
//                        }

                }
            }
        });




//            db.collection("Consumer").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            if (((String) document.getData().get("email")).equals(email)) {
//                                flag = 2;
//                                SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
//                                SharedPreferences.Editor editor = settings.edit();
//                                editor.putInt("ProviderOrConsumerFlag",2);
//                                editor.commit();
//                                Log.d("LoginActivity", "-----------Successfully found user in Consumer");
//                                //---------------------------Send intent to consumer UI
//                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                                //mainIntent.putExtra("ProviderOrConsumer", 2);
//                                startActivity(mainIntent);
//                                finish();
//                            } else {
//                                Log.d("LoginActivity", "-----------Not found in Provider");
//                            }
//
//                            break;
//                        }
//                    }
//                }
//            });




        //if user is neither producer or consumer. This is a fail safe, and no such users must exist
//        if(flag == 0) {
//            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(mainIntent);
//            finish();
//        }

    }

    //----------------Opens the start activity page i.e Signup pages and intro's
    public void openStartPageActivity() {
        Intent startIntent = new Intent(this, StartPage.class);
        startActivity(startIntent);
    }
    
    //----------------Opens the splash activity page which goes to slider and then to start page.
    public void openSplashPageActivity() {
        Intent startIntent = new Intent(this, SplashActivity.class);
        startActivity(startIntent);
    }
}

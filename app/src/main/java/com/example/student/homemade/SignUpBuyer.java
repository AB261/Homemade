package com.example.student.homemade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpBuyer extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            //"(?=.*[0-9])" +               //at least 1 digit
            "(?=.*[a-z])" +               //at least 1 lower case letter
            "(?=.*[A-Z])" +               //at least 1 upper case letter
            //"(?=.*[a-zA-Z])" +            //any letter
            "(?=.*[@#$%^&+=])" +          //at least 1 special character
            //"(?=\\S+$)" +                 //no white space
            //".{4,}" +                     //at least 4 characters
            "$");

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public EditText textInputEmail;
    public EditText textInputUsername;
    public EditText textInputPassword;
    public EditText textInputConfirmPassword;
    public EditText textInputPhoneNumber;
    public EditText textInputAddress;
    public ImageView imageUserPhoto;
    public static int PReqCode = 1;
    public static int REQUESCODE = 1;
    public Uri pickedImgUri;
    private Typeface myFont;
    private TextView headText;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorage;
    private ProgressBar loadingProgress;
    private Button signUpBuyerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_buyer);


        myFont = Typeface.createFromAsset(this.getAssets(), "fonts/PlayfairDisplay_Black.ttf");
        headText = findViewById(R.id.head_text);
        headText.setTypeface(myFont);

        textInputUsername = findViewById(R.id.text_input_username_buyer);
        textInputEmail = findViewById(R.id.text_input_email_buyer);
        textInputPassword = findViewById(R.id.text_input_password_buyer);
        textInputConfirmPassword = findViewById(R.id.text_input_confirm_password_buyer);
        textInputPhoneNumber = findViewById(R.id.text_input_phone_number_buyer);
        textInputAddress = findViewById(R.id.text_input_address_buyer);
        imageUserPhoto = findViewById(R.id.profile_picture_buyer);
        signUpBuyerBtn = findViewById(R.id.signup_buyer_button);
        loadingProgress = findViewById(R.id.progress_bar_signup_buyer);

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signUpBuyerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBuyerBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                confirmInput();
            }
        });

        imageUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= 28) {
                    checkAndRequestforPermission();
                }

                else {
                    openGallery();
                }

            }
        });
    }


    private void openGallery() {
        //open gallery intent and wait for user to pick an image!

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);

    }




    private void checkAndRequestforPermission() {

        if(ContextCompat.checkSelfPermission(SignUpBuyer.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(SignUpBuyer.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(SignUpBuyer.this, "Please accept required permission", Toast.LENGTH_SHORT).show();
            }

            else {
                ActivityCompat.requestPermissions(SignUpBuyer.this,
                                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    PReqCode);
            }
        }

        else
            openGallery();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //the user has successfully picked an image
            // we need to save the reference to a uri variable

            pickedImgUri = data.getData();
            imageUserPhoto.setImageURI(pickedImgUri);
        }

    }



    private boolean validateEmail() {
        String emailInput = textInputEmail.getText().toString().trim();

        if(emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        }

        //Given below is a predefined pattern that validates email address; emailInput string should be passed into matcher; returns true if they match
        //Keep the cursor on EMAIL_ADDRESS below and click on "ctrl + b" to get declaration.
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Invalid email address. Pls enter a valid one.");
            return false;
        }

        else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getText().toString().trim();

        if(usernameInput.isEmpty()) {
            textInputUsername.setError("Field cannot be empty");
            return false;
        }

        if(usernameInput.length() > 15) {
            textInputUsername.setError("Field cannot have greater than 15 characters");
            return false;
        }

        else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getText().toString().trim();

        if(passwordInput.isEmpty()) {
            textInputPassword.setError("Field cannot be empty");
            return false;
        }
        else if(passwordInput.length() < 8) {
            textInputPassword.setError("Field cannot have less than 8 characters");
            return false;
        }
//        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            textInputPassword.setError("Password too weak. Must have atleast one small, one capital letter and one special character.");
//            return false;
//        }
        else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        String addressInput = textInputAddress.getText().toString().trim();

        if(addressInput.isEmpty()) {
            textInputAddress.setError("Field cannot be empty");
            return false;
        }

        else {
            textInputAddress.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String phoneInput = textInputPhoneNumber.getText().toString().trim();

        if(phoneInput.isEmpty()) {
            textInputPhoneNumber.setError("Field cannot be empty");
            return false;
        }

        else {
            textInputPhoneNumber.setError(null);
            return true;
        }
    }



    public void confirmInput() {
        // validate password has been removed because of regex problems
        if(!validateEmail() || !validateUsername() || !validatePassword() || !textInputPassword.getText().toString().equals(textInputConfirmPassword.getText().toString()) || !validateAddress()) {
            loadingProgress.setVisibility(View.INVISIBLE);
            signUpBuyerBtn.setVisibility(View.VISIBLE);
            Log.d("Sign UP", "validation error");
            return;
        }

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Study Firebase Storage Properly before and implement this thing properly



        String input = "Email: " + textInputEmail.getText().toString();
        input += "\n";
        input += "Username: " + textInputUsername.getText().toString();
        input += "\n";
        input += "Password: " + textInputPassword.getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();



        mAuth.createUserWithEmailAndPassword(textInputEmail.getText().toString(), textInputPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()) {

                    mAuth.signInWithEmailAndPassword(textInputEmail.getText().toString(), textInputPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String input = "Email: " + textInputEmail.getText().toString();
                            input += "\n";
                            input += "Username: " + textInputUsername.getText().toString();
                            input += "\n";
                            input += "Password: " + textInputPassword.getText().toString();

                            Map<String, Object> userdata = new HashMap<>();
                            userdata.put("username", textInputUsername.getText().toString());
                            userdata.put("email", textInputEmail.getText().toString());
                            userdata.put("password", textInputPassword.getText().toString());
                            userdata.put("contactNumber", textInputPhoneNumber.getText().toString());
                            userdata.put("address", textInputAddress.getText().toString());
                            userdata.put("wallet", "1000");
//                            userdata.put("profile_picture", pickedImgUri);
                            userdata.put("typeOfUser", "Consumer");

                            db.collection("user").document(mAuth.getCurrentUser().getUid()).set(userdata)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("SignUP", "Signup of " + textInputEmail.getText().toString() + " successful.");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("SignUP", "Signup of " + textInputEmail.getText().toString() + " failure.");
                                        }
                                    });

                            db.collection("Consumer").document(mAuth.getCurrentUser().getUid()).set(userdata)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("SignUP", "Signup of " + textInputEmail.getText().toString() + " successful.");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("SignUP", "Signup of " + textInputEmail.getText().toString() + " failure.");
                                        }
                                    });

                            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("consumers_photos");
                            final StorageReference imageFilePath = mStorage.child(mAuth.getCurrentUser().getUid());
                            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("Storage successful.", "Storage of " + textInputEmail.getText().toString() + " Successful");
                                            loadingProgress.setVisibility(View.INVISIBLE);
                                            signUpBuyerBtn.setVisibility(View.VISIBLE);

                                            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(mainActivity);
                                            finish();
                                        }
                                    });
                                }
                            });

//                            Toast.makeText(getApplicationContext(), "Successfull Sign Up. Go to Login and Start Over", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }


}


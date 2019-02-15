package com.example.student.homemade;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpSeller extends AppCompatActivity {

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
    public EditText textInputRestaurantName;
    public EditText textInputRestaurantDetails;
    public EditText textInputUsername;
    public EditText textInputPassword;
    public EditText textInputPhoneNumber;
    public EditText textInputProfilePicture;
    public EditText textInputImageResourceId;
    private Typeface myFont;
    private TextView headText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_seller);

        myFont = Typeface.createFromAsset(this.getAssets(), "fonts/PlayfairDisplay_Black.ttf");
        headText = findViewById(R.id.head_text);
        headText.setTypeface(myFont);

        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputRestaurantName = findViewById(R.id.text_input_restaurant_name);
        textInputRestaurantDetails = findViewById(R.id.text_input_restaurant_details);
        textInputPhoneNumber = findViewById(R.id.text_input_phone_number);
        textInputImageResourceId = findViewById(R.id.text_input_image_resource_id);
        textInputProfilePicture = findViewById(R.id.text_input_profile_picture_id);
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

        else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validateRestaurantName() {
        String restaurantInput = textInputRestaurantName.getText().toString().trim();

        if(restaurantInput.isEmpty()) {
            textInputRestaurantName.setError("Field cannot be empty");
            return false;
        }

        else {
            textInputRestaurantName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getText().toString().trim();

        if(passwordInput.isEmpty()) {
            textInputPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak. Must have atleast one small, one capital letter and one special character.");
            return false;
        }
        else {
            textInputPassword.setError(null);
            return true;
        }
    }


    public void confirmInput(View v) {
        // validate password has been removed because of regex problems
        if(!validateEmail() | !validateUsername() | !validateRestaurantName()) {
            return;
        }

        String input = "Email: " + textInputEmail.getText().toString();
        input += "\n";
        input += "Username: " + textInputUsername.getText().toString();
        input += "\n";
        input += "RestaurantName: " + textInputRestaurantName.getText().toString();
        input += "\n";
        input += "RestaurantDetails: " + textInputRestaurantDetails.getText().toString();
        input += "\n";
        input += "Password: " + textInputPassword.getText().toString();



        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(textInputEmail.getText().toString(), textInputPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()) {
                    String input = "Email: " + textInputEmail.getText().toString();
                    input += "\n";
                    input += "Username: " + textInputUsername.getText().toString();
                    input += "\n";
                    input += "Password: " + textInputPassword.getText().toString();
                    input += "\n";
                    input += "Restaurant Name: " + textInputRestaurantName.getText().toString();
                    input += "\n";
                    input += "Restaurant Details: " + textInputRestaurantDetails.getText().toString();
                    input += "\n";

                    Map<String, Object> user = new HashMap<>();
                    user.put("active",true);
                    user.put("address",null);
                    user.put("description", textInputRestaurantDetails.getText().toString());
                    user.put("email", textInputEmail.getText().toString());
                    user.put("imageResourceId", textInputImageResourceId.getText().toString());
                    //user.menu("menu",)
                    user.put("phone",textInputPhoneNumber.getText().toString());
                    user.put("profilepictures",textInputProfilePicture.getText().toString());
                    user.put("restaurantname", textInputRestaurantName.getText().toString());
                    //user.put("userid", )
                    user.put("username", textInputUsername.getText().toString());
                    user.put("wallet", 100);
                    user.put("password", textInputPassword.getText().toString());
                    user.put("typeOfUser", "Provider");
//                    user.put("username", textInputUsername.getText().toString());
//                    user.put("email", textInputEmail.getText().toString());
//                    user.put("password", textInputPassword.getText().toString());
//                    user.put("restaurant name", textInputRestaurantName.getText().toString());
//                    user.put("restaurant details", textInputRestaurantDetails.getText().toString());
//                    user.put("restaurant phone number", textInputPhoneNumber.getText().toString());
//                    user.put("restaurant image resource id", textInputImageResourceId.getText().toString());
//                    user.put("restaurant person profile image id", textInputProfilePicture.getText().toString());
//                    user.put("typeOfUser", "Provider");

                    db.collection("user").document(textInputEmail.getText().toString()).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("user SignUP", "Signup of " + textInputEmail.getText().toString() + " to user document successful.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("user SignUP", "Signup of " + textInputEmail.getText().toString() + " to user document failure.");
                                }
                            });


                    db.collection("Provider").document(textInputRestaurantName.getText().toString()).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Provider SignUP", "Signup of " + textInputEmail.getText().toString() + " to Provider document successful.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Provider SignUP", "Signup of " + textInputEmail.getText().toString() + " to Provider document failure.");
                                }
                            });

                    Toast.makeText(getApplicationContext(), "Successfull Sign Up. Please go back and click Login to go to your Dashboard.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpSeller.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}
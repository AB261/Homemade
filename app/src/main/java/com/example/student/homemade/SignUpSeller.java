package com.example.student.homemade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.GeoPoint;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public EditText textInputEmail;
    public EditText textInputRestaurantName;
    public EditText textInputRestaurantDetails;
    public EditText textInputUsername;
    public EditText textInputPassword;
    public EditText textInputPhoneNumber;
    public ImageView imageUserPhoto;
    public ImageView imageRestaurantPhoto;
    public static int PReqCode = 1;
    public static int REQUESCODE = 1;
    public Uri pickedImgUserUri;
    public Uri pickedImgRestaurantUri;
    private Typeface myFont;
    private TextView headText;
    private StorageReference mStorage;
    private ProgressBar loadingProgress;
    private Button signUpSellerBtn;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String sellerLocation;
    private GeoPoint geoPoint;


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_seller);

        myFont = Typeface.createFromAsset(this.getAssets(), "fonts/PlayfairDisplay_Black.ttf");
        headText = findViewById(R.id.head_text);
        headText.setTypeface(myFont);

        textInputEmail = findViewById(R.id.text_input_email_seller);
        textInputUsername = findViewById(R.id.text_input_username_seller);
        textInputPassword = findViewById(R.id.text_input_password_seller);
        textInputRestaurantName = findViewById(R.id.text_input_restaurant_name_seller);
        textInputRestaurantDetails = findViewById(R.id.text_input_restaurant_details_seller);
        textInputPhoneNumber = findViewById(R.id.text_input_phone_number_seller);
        signUpSellerBtn = findViewById(R.id.signup_seller_button);
        loadingProgress = findViewById(R.id.progress_bar_signup_seller);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signUpSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpSellerBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                confirmInput();
            }
        });

        //User Image
        imageUserPhoto = findViewById(R.id.profile_picture_seller);

        imageUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 28) {
                    checkAndRequestforPermission();
                } else {
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

        if (ContextCompat.checkSelfPermission(SignUpSeller.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpSeller.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(SignUpSeller.this, "Please accept required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SignUpSeller.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //the user has successfully picked an image
            // we need to save the reference to a uri variable

            pickedImgUserUri = data.getData();
            imageUserPhoto.setImageURI(pickedImgUserUri);
        }

    }


    private boolean validateEmail() {
        String emailInput = textInputEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        }

        //Given below is a predefined pattern that validates email address; emailInput string should be passed into matcher; returns true if they match
        //Keep the cursor on EMAIL_ADDRESS below and click on "ctrl + b" to get declaration.
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Invalid email address. Pls enter a valid one.");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field cannot be empty");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validateRestaurantName() {
        String restaurantInput = textInputRestaurantName.getText().toString().trim();

        if (restaurantInput.isEmpty()) {
            textInputRestaurantName.setError("Field cannot be empty");
            return false;
        } else {
            textInputRestaurantName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field cannot be empty");
            return false;
        } else if (passwordInput.length() < 8) {
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



    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(SignUpSeller.this, new String[] {ACCESS_FINE_LOCATION}, 1);
    }

    public void getSellerLocation() {

        requestLocationPermission();

        if (ActivityCompat.checkSelfPermission(SignUpSeller.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(SignUpSeller.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    sellerLocation = location.toString();
                    double lat = (double) (location.getLatitude());
                    double lng = (double) (location.getLongitude());

                    geoPoint = new GeoPoint(lat, lng);
                    Toast.makeText(getApplicationContext(), geoPoint.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void confirmInput() {
        // validate password has been removed because of regex problems
        if(!validateEmail() || !validateUsername() || !validateRestaurantName() || !validatePassword()) {
            loadingProgress.setVisibility(View.INVISIBLE);
            signUpSellerBtn.setVisibility(View.VISIBLE);
            return;
        }


        getSellerLocation();


        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
                            input += "\n";
                            input += "Restaurant Name: " + textInputRestaurantName.getText().toString();
                            input += "\n";
                            input += "Restaurant Details: " + textInputRestaurantDetails.getText().toString();
                            input += "\n";

                            /////////////////////////////////////////////////////////////////////////////////////////////////

                            Map<String, Object> user = new HashMap<>();
                            user.put("availability",true);
                            user.put("address", geoPoint);
                            user.put("description", textInputRestaurantDetails.getText().toString());
                            user.put("email", textInputEmail.getText().toString());
                            //user.put("imageResourceId", textInputImageResourceId.getText().toString());
                            //user.menu("menu",)

                            user.put("phone",textInputPhoneNumber.getText().toString());
                            //user.put("profilepictures",textInputProfilePicture.getText().toString());
                            user.put("restaurantName", textInputRestaurantName.getText().toString());
                            user.put("id", mAuth.getCurrentUser().getUid().toString());
                            user.put("username", textInputUsername.getText().toString());
                            user.put("wallet", 100);
                            user.put("password", textInputPassword.getText().toString());
                            user.put("typeOfUser", "Provider");

                            db.collection("user").document(mAuth.getCurrentUser().getUid()).set(user)
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


                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                            ArrayList<String> customItems = new ArrayList<>();
                            HashMap<String, String> itemPictures = new HashMap<>();
                            Map<String, Object> provider = new HashMap<>();

                            provider.put("availability",true);
                            provider.put("address", geoPoint);
                            provider.put("description", textInputRestaurantDetails.getText().toString());
                            provider.put("email", textInputEmail.getText().toString());
                            provider.put("itemPictures", itemPictures);
                            provider.put("customItems", customItems);
                            provider.put("longTermSubscriptionDiscount", 0);
                            provider.put("massOrderDiscount", 0);
                            provider.put("noOfMassOrders", 0);
                            provider.put("timeBeforeCancel", 0);
                            //user.put("imageResourceId", textInputImageResourceId.getText().toString());
                            //user.menu("menu",)
                            provider.put("phone",textInputPhoneNumber.getText().toString());
                            //user.put("profilepictures",textInputProfilePicture.getText().toString());
                            provider.put("restaurantName", textInputRestaurantName.getText().toString());
                            provider.put("id", mAuth.getCurrentUser().getUid().toString());
                            provider.put("username", textInputUsername.getText().toString());
                            provider.put("wallet", 100);
                            provider.put("password", textInputPassword.getText().toString());
                            provider.put("typeOfUser", "Provider");

                            db.collection("Provider").document(mAuth.getCurrentUser().getUid()).set(provider)
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


                            ///////////////////////////////////////////////////////////////////////////////////////////////////////


                            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("providers_photos");
                            final StorageReference imageFilePath1 = mStorage.child("profile_pictures").child(mAuth.getCurrentUser().getUid());

                            imageFilePath1.putFile(pickedImgUserUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageFilePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("Storage successful.", "Storage of " + textInputEmail.getText().toString() + " Successful");
                                            loadingProgress.setVisibility(View.INVISIBLE);
                                            signUpSellerBtn.setVisibility(View.VISIBLE);

                                            Toast.makeText(getApplicationContext(), "Successfull Sign Up. Now add Restaurant Images.", Toast.LENGTH_LONG).show();

                                            Intent part2Activity = new Intent(getApplicationContext(), SignUpSeller2.class);
                                            startActivity(part2Activity);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }


}
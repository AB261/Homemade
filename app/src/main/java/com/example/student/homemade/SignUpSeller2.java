package com.example.student.homemade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpSeller2 extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public ImageView imageRestaurantPhoto;
    public static int PReqCode = 1;
    public static int REQUESCODE = 1;
    public Uri pickedImgRestaurantUri;
    private Typeface myFont;
    private TextView headText;
    private StorageReference mStorage;
    private ProgressBar loadingProgress;
    private Button signUpSellerBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_seller2);

        myFont = Typeface.createFromAsset(this.getAssets(), "fonts/PlayfairDisplay_Black.ttf");
        headText = findViewById(R.id.head_text);
        headText.setTypeface(myFont);

        signUpSellerBtn2 = findViewById(R.id.signup_seller_button2);
        loadingProgress = findViewById(R.id.progress_bar_signup_seller2);

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        imageRestaurantPhoto = findViewById(R.id.restaurant_picture_seller);

        signUpSellerBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pickedImgRestaurantUri!=null){

                    signUpSellerBtn2.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.VISIBLE);
                    confirmInput();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Pick an image please",Toast.LENGTH_LONG).show();
                }
            }
        });

        imageRestaurantPhoto.setOnClickListener(new View.OnClickListener() {
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

        if(ContextCompat.checkSelfPermission(SignUpSeller2.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(SignUpSeller2.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(SignUpSeller2.this, "Please accept required permission", Toast.LENGTH_SHORT).show();
            }

            else {
                ActivityCompat.requestPermissions(SignUpSeller2.this,
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

            pickedImgRestaurantUri = data.getData();
            imageRestaurantPhoto.setImageURI(pickedImgRestaurantUri);
        }

    }

    private void confirmInput() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("providers_photos");
        final StorageReference imageFilePath2 = mStorage.child("restaurant_pictures").child(mAuth.getCurrentUser().getUid());
        if(pickedImgRestaurantUri==null){
            Toast.makeText(getApplicationContext(), "Please add image before submitting", Toast.LENGTH_LONG).show();

        }
        if(pickedImgRestaurantUri!=null){
            imageFilePath2.putFile(pickedImgRestaurantUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("Storage successful.", "Storage of Restaurant Image of " + user.getUid().toString() + " Successful");
                            loadingProgress.setVisibility(View.INVISIBLE);
                            signUpSellerBtn2.setVisibility(View.VISIBLE);


                            Toast.makeText(getApplicationContext(), "Restaurant Registered Successfully, Welcome to our Platform!", Toast.LENGTH_LONG).show();

                            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivity);
                            finish();
                        }
                    });
                }
            });
        }


    }
}

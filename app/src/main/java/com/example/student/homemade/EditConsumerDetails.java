package com.example.student.homemade;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.homemade.ui.ConsumerDetailsFragment;
import com.example.student.homemade.ui.ConsumerUIFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EditConsumerDetails extends AppCompatActivity {

    EditText editName,editAddress,editContact;
    ImageView editPhoto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("consumers_photos");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUID = firebaseAuth.getUid();
    Button editDetailsbtn;
    boolean flagImage = false;
    Map detailsOld;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_consumer_details);

        showOldPic();
        showOldDetails();

        editName = findViewById(R.id.tvEditName);
        editAddress = findViewById(R.id.tvEditAddress);
        editContact = findViewById(R.id.tvEditContact);
        editPhoto = findViewById(R.id.ivEditPic);
        editDetailsbtn = findViewById(R.id.saveDetails);
        showOldPic();



        ////////////LOADINGIIMAGE2 LOADING IMAGE FROM GALARY
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");//image IS FOR IMAGE, WE CAN USE APPLICATION/*,AUDIO/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE);     //calls onActivityResult
            }
        });
        //LOADING IMAGE DONE FROM GALARY DONE

        /////////////////update button pressed
        editDetailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDetails();
                if(flagImage == true) setimage();
                flagImage = false;
                finish();
            }
        });




    }

    void setDetails(){
        if(!validate()) return;

        DocumentReference noteRef = db.collection("Consumer").document(currentUID);

        String name,address,contact;
        name = editName.getText().toString();
        address = editAddress.getText().toString();
        contact = editContact.getText().toString();

        Map  details = detailsOld;
        details.put("username",name);
        details.put("address",address);
        details.put("contactNumber",contact);

        noteRef.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditConsumerDetails.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditConsumerDetails.this, "Details cannot be saved", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    void setimage(){
        ////////////////////////////////////////for image uplaod  #partToSetImage
        StorageReference imageReference = FirebaseStorage.getInstance().getReference().child("consumers_photos").child(currentUID);
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditConsumerDetails.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditConsumerDetails.this, "Upoad successful", Toast.LENGTH_SHORT).show();


            }
        });
        ///////////////////////////////////// image uploading part ends

    }



    Boolean validate(){
        String name,address,contact;
        name = editName.getText().toString();
        address = editAddress.getText().toString();
        contact = editContact.getText().toString();
        if( name.equals("") || address.equals("")  || contact.equals(""))   return false;

        return true;


    }


    void showOldPic(){

        /////////LOADING IMAGE FROM FIREBASE AND DISPLAYING DONE
        storageReference = FirebaseStorage.getInstance().getReference();

        storageReference.child("consumers_photos").child(currentUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {          /////do change here

            @Override
            public void onSuccess(Uri uri) {
                // profilePic.setImageURI(uri);             THIS WON'T WORK AS IT'S RETURNING A URL RATHER THAN A IMAGE
                Picasso.get().load(uri).fit().centerCrop().into(editPhoto);//GET THIS FROM SQUARE PICASSO ,DON'T FORGET ITS DEPENDENCY


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditConsumerDetails.this, "CANNOT LOAD IMAGE", Toast.LENGTH_SHORT).show();
            }
        });

        /////////LOADING IMAGE FROM FIREBASE AND DISPLAYING DONE
    }

    void showOldDetails(){

        DocumentReference myref =  db.collection("Consumer").document(currentUID);


        myref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                  detailsOld = documentSnapshot.getData();
                editName.setText( detailsOld.get("username").toString()  );
                editAddress.setText(   detailsOld.get("address").toString()  );
                editContact.setText(detailsOld.get("contactNumber").toString());

            }
        });
    }

    ////////LOADING IMAGE FROM GALARY
    static int PICK_IMAGE = 123;

    Uri imagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                flagImage = true;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                editPhoto.setImageBitmap(bitmap);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //AFTER THIS IMAGE IS UPLOADED TO FIREBASE USING  #partToSetImage
    ///LOADING IMAGE FROM GALARY CONTINUED AT LOADINGIMAGE PART2
}

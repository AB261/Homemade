
package com.example.student.homemade.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.ConsumerDetailsClass;
import com.example.student.homemade.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ConsumerDetailsFragment extends Fragment {

    ////VARIABLE INITIALIZATION
    TextView userName,userAddress,userContact,userWallet,userEmail;
    ImageView userProfilePic;
    Button editUserDetails,changeUserPassword;
    View v;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserUID = firebaseAuth.getUid();
    DocumentReference notebookRef  = db.collection("Consumer").document("saharsh1999@nitk.ac.in");







    public ConsumerDetailsFragment(){
        //Required empty constructor
    }

    public static ConsumerDetailsFragment newInstance() {
        ConsumerDetailsFragment fragment = new ConsumerDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.activity_consumer_details_layout, container, false);
        context=getActivity();
        userName = v.findViewById(R.id.tvProfileName);
        userAddress = v.findViewById(R.id.tvProfileAddress);
        userContact = v.findViewById(R.id.tvProfileContact);
        userWallet = v.findViewById(R.id.tvProfileWallet);
        userEmail = v.findViewById(R.id.tvProfileEmail);
        changeUserPassword = v.findViewById(R.id.btnChangePassword);
        editUserDetails = v.findViewById(R.id.btnEditDetails);
        setDetails();
        setProfilePic();


        return v;
    }

    ////CODE TO FETCH AND SHOW DETAILS USING CLASS ConsumerDetailsClass
    public void setDetails(){

        notebookRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ConsumerDetailsClass details = documentSnapshot.toObject(ConsumerDetailsClass.class);

                userName.setText(details.getUsername());
                userAddress.setText(details.getAddress());
                userContact.setText(details.getContactNo());
                userEmail.setText(details.getEmail());
                userWallet.setText(details.getWallet());

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Sorry that didn't work", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void setProfilePic(){

//            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    // profilePic.setImageURI(uri);             THIS WON'T WORK AS IT'S RETURNING A URL RATHER THAN A IMAGE
//                    try {
//                        Picasso.get().load(uri).fit().centerCrop().into(userProfilePic);//GET THIS FROM SQUARE PICASSO ,DON'T FORGET ITS DEPENDENCY
//                    }catch (Exception e){
//                        Log.i("fuck",e.toString());
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getActivity(), "CANNOT LOAD IMAGE", Toast.LENGTH_SHORT).show();
//
//                }
//            });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

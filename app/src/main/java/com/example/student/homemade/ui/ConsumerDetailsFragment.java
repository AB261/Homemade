
package com.example.student.homemade.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
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
import com.example.student.homemade.EditConsumerDetails;
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
import com.squareup.picasso.Transformation;

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
    DocumentReference notebookRef  = db.collection("Consumer").document("nigga@99.com");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("consumers_photos").child("somerandompic");
    ProgressDialog progressDialog;




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
        userProfilePic = v.findViewById(R.id.ivProfilePic);
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setMessage("LOADING!! PLEASE WAIT...");
        progressDialog.show();
        setDetails();
        setProfilePic();

        editUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditConsumerDetails.class));
            }
        });


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

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // profilePic.setImageURI(uri);             THIS WON'T WORK AS IT'S RETURNING A URL RATHER THAN A IMAGE
//                    Picasso.get()
//                            .load(uri)
//                            .error(R.drawable.ic_phone_android_black_24dp)
//                            .placeholder(R.drawable.com_facebook_button_icon_white)
//                            .resize(200, 200)
//                            .transform(new ImageTrans_CircleTransform())
//                            .into(userProfilePic);

                Picasso.get().load(uri).fit().centerCrop().into(userProfilePic);//GET THIS FROM SQUARE PICASSO ,DON'T FORGET ITS DEPENDENCY
                progressDialog.dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "CANNOT LOAD IMAGE", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    //something for round image
    public class ImageTrans_CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            if (source == null || source.isRecycled()) {
                return null;
            }
            int borderwidth = userProfilePic.getWidth();
            int bordercolor = userProfilePic.getSolidColor();

            final int width = source.getWidth() + borderwidth;
            final int height = source.getHeight() + borderwidth;

            Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            canvas.drawCircle(width / 2, height / 2, radius, paint);

            //border code
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(bordercolor);
            paint.setStrokeWidth(borderwidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderwidth / 2, paint);
            //--------------------------------------

            if (canvasBitmap != source) {
                source.recycle();
            }

            return canvasBitmap;
        }
        @Override
        public String key() {
            return "circle";
        }
    }

    //something for round image
}
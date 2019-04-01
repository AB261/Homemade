package com.example.student.homemade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.student.homemade.ui.RestaurantDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;


//Adapter that displays restaurant cards
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    private Context mContext;
    private List<RestaurantModel> myList;
    private String TAG = "RestaurantAdapter";


    public RestaurantAdapter(@NonNull Context context, List<RestaurantModel> list) {
        this.mContext = context;
        this.myList = list;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final RestaurantModel restaurantModel = myList.get(i);

        Log.d(TAG,"Now it contains"+restaurantModel.getReview().get(0));
        String dis = mContext.getString(R.string.dist_from_curr_loc) +": " + restaurantModel.getDistance();

        myViewHolder.distanceFromCurrLoc.setText(dis + " km");
        // myViewHolder.rating.setText("Rating:" + String.valueOf(5));
        myViewHolder.restaurantName.setText(restaurantModel.getRestaurantName());
        myViewHolder.rating.setRating((float) restaurantModel.getRating());
        myViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.btn.setText("Subscribed");
                myViewHolder.btn.setBackgroundColor(Color.DKGRAY);
                Intent intent = new Intent(mContext.getApplicationContext(), Subscription_time.class);
                intent.putExtra("restaurantName", restaurantModel.getRestaurantName());
                intent.putExtra("providerID",restaurantModel.getUserID());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);


            }
        });

//Glide is a library used to put images in image view
        StringBuilder url = new StringBuilder("providers_photos/restaurant_pictures/");
        url.append(restaurantModel.getUserID());
        Log.d(TAG,url+restaurantModel.getUserID());
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(url.toString().trim());

        if (restaurantModel.getRestaurantImage() == null) {

            final long ONE_MEGABYTE = 1024 * 1024;
            mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//                DisplayMetrics dm = new DisplayMetrics();
//                mContext.getResources().getWindowManager().getDefaultDisplay().getMetrics(dm);
                    Log.v(TAG, "Got image");
                    myViewHolder.thumbnail.setMinimumHeight(dm.heightPixels);
                    myViewHolder.thumbnail.setMinimumWidth(dm.widthPixels);
                    restaurantModel.setRestaurantImage(bm);
                    myViewHolder.thumbnail.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide.with(mContext).load(R.drawable.default_rest).into(myViewHolder.thumbnail);

                }
            });
        } else {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//                DisplayMetrics dm = new DisplayMetrics();
//                mContext.getResources().getWindowManager().getDefaultDisplay().getMetrics(dm);
            myViewHolder.thumbnail.setMinimumHeight(dm.heightPixels);
            myViewHolder.thumbnail.setMinimumWidth(dm.widthPixels);
            myViewHolder.thumbnail.setImageBitmap(restaurantModel.getRestaurantImage());
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantDialogFragment dialogFragment = new RestaurantDialogFragment();
                Bundle args = new Bundle();
                args.putString("stars", String.valueOf(restaurantModel.getRating()));
                args.putString("title", restaurantModel.getRestaurantName());
                args.putStringArrayList("reviews", restaurantModel.getReview());
                args.putString("description", restaurantModel.getDescription());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (restaurantModel.getRestaurantImage() != null) {
                    restaurantModel.getRestaurantImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    args.putByteArray("image", byteArray);
                }


                Log.d(TAG, String.valueOf(restaurantModel.getRating()));
                Log.d(TAG, restaurantModel.getRestaurantName());
                Log.d(TAG, restaurantModel.getReview().toString());

                dialogFragment.setArguments(args);
                dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Simple Dialog");
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_items, viewGroup, false);


        return new MyViewHolder(itemView);

    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName, distanceFromCurrLoc;
        RatingBar rating;
        ImageView thumbnail;
        Button btn;

        MyViewHolder(View view) {
            super(view);

            restaurantName = view.findViewById(R.id.restaurantname);
            distanceFromCurrLoc = view.findViewById(R.id.distance);
            thumbnail = view.findViewById(R.id.coverImageView);
            rating = view.findViewById(R.id.rating_bar);
            btn = view.findViewById(R.id.btn_sub);

        }

    }
}
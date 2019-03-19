package com.example.student.homemade.ui;

import android.content.Context;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.student.homemade.R;
import com.example.student.homemade.RestaurantModel;
import com.example.student.homemade.ReviewInfo;
import com.google.maps.model.PlaceDetails;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{
    private Context mContext;
    private String TAG = "ReviewAdapter";
    private ArrayList<String> userName;
    public ArrayList<String> review;
    public ArrayList<Integer> rating;
    public ReviewAdapter(Context context, ArrayList<String> userName, ArrayList<String> review, ArrayList<Integer> rating){
        mContext=context;
        this.userName=userName;
        this.review=review;
        this.rating=rating;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.review.setText(review.get(i));
        myViewHolder.rating.setRating(rating.get(i));
        myViewHolder.user.setText(userName.get(i));


    }


    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView review;
        TextView user;
        RatingBar rating;

        MyViewHolder(View view) {
            super(view);
            review=view.findViewById(R.id.userReview);
            user = view.findViewById(R.id.userName);
            rating=view.findViewById(R.id.userRating);

        }

    }
}

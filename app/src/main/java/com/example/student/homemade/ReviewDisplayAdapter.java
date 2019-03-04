package com.example.student.homemade;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class  ReviewDisplayAdapter extends RecyclerView.Adapter<ReviewDisplayAdapter.MyViewHolder> {
    private ArrayList<ReviewInfo> info;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RatingBar ratings;
        TextView review;
        public CircleImageView photo;
        TextView reviewID;
        TextView reviewee;
        TextView reviewer;
        TextView timeAndDate;

        RelativeLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ratings = itemView.findViewById(R.id.ratings);
            review = itemView.findViewById(R.id.reviews);
            reviewer = itemView.findViewById(R.id.username);
            timeAndDate = itemView.findViewById(R.id.date);
            photo = itemView.findViewById(R.id.user_image);
            layout = itemView.findViewById(R.id.orders_layout_rl);


        }
    }

    public ReviewDisplayAdapter(Context context, ArrayList<ReviewInfo> info) {
        this.info = info;
        this.context = context;
    }
    @NonNull
    @Override
    public ReviewDisplayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.review_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ReviewDisplayAdapter.MyViewHolder holder, final int position) {

        final ReviewInfo item = info.get(position);
        holder.ratings.setRating(item.getRatings());
//        holder.ratings.setNumStars(item.getRatings());
        holder.review.setText(item.getReview());
        holder.timeAndDate.setText(item.getTimeAndDate().toString());
        holder.reviewer.setText(item.getReviewer());


    }

    @Override
    public int getItemCount() {

        return info.size();

    }

    public void added(ReviewInfo c) {
        Log.d("added @ adapter", info.size() + "s");
        info.add(c);
        notifyItemInserted(info.indexOf(c));
    }

    public void remove(ReviewInfo c) {

        int pos = info.indexOf(c);
        info.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, info.size());
    }

    public ArrayList<ReviewInfo> getItems(){
        return info;
    }

}
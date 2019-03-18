package com.example.student.homemade;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class CancelAdapter extends RecyclerView.Adapter<CancelAdapter.ViewHolder> {
    private ArrayList<Order2> info;
    private OnItemClickListner mListner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public interface OnItemClickListner{
        void onDeleteClick(int position);
    }
    public void setOnItemClickListner(OnItemClickListner listener){
        mListner=listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView line1;
        TextView line2;
        TextView line3;
        TextView line4;
        TextView line5;
        ImageView mDeleteImage;

        public ViewHolder(@NonNull View itemView, final OnItemClickListner listener) {
            super(itemView);
            Log.d("=========", "at view holder");
            line1=itemView.findViewById(R.id.orderitem1);
            line2=itemView.findViewById(R.id.orderitem2);
            line3=itemView.findViewById(R.id.orderitem3);
            line4=itemView.findViewById(R.id.orderitem4);
            line5=itemView.findViewById(R.id.orderitem5);

            mDeleteImage=itemView.findViewById(R.id.image_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){

                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }

                }
            });
        }
    }
    public CancelAdapter(ArrayList<Order2>info){
        this.info=info;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_item,parent,false);
        ViewHolder evh=new ViewHolder(v,mListner);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Order2 orderInfo=info.get(position);
//        String restaurantName;
//        int timeBeforeCancel;
        final ViewHolder viewHolder1 = viewHolder;
        db.collection("Provider").document(orderInfo.provider).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                String restaurantName = map.get("restaurantName").toString();
                int timeBeforeCancel = Integer.parseInt(map.get("timeBeforeCancel").toString());
                viewHolder1.line1.setText("" + restaurantName);
                viewHolder1.line2.setText("Time when order was placed: " + orderInfo.orderTime );
                viewHolder1.line3.setText("Order can be cancelled before: " + timeBeforeCancel + " min");
                viewHolder1.line4.setText("Items: " + orderInfo.getItemsOrdered());
                viewHolder1.line5.setText("Total Cost: " + orderInfo.getOrderTotal());
            }
        });

//        viewHolder.line1.append("Order ID: " + orderInfo.getOrderID() + " --- Time: " + orderInfo.getTime_and_date());
//        viewHolder.line2.append("Time before cancel: " + orderInfo.timeBeforeCancel + " min");
//        viewHolder.line3.append(/*"Total Cost: " + orderInfo.getTotal_cost() + */"Items ordered: " + orderInfo.getThings_ordered());


    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public void added(Order2 order2){
        info.add(order2);
        notifyItemInserted(info.indexOf(order2));

    }
}
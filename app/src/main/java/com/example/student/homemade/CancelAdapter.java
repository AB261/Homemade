package com.example.student.homemade;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CancelAdapter extends RecyclerView.Adapter<CancelAdapter.ViewHolder> {
    private ArrayList<OrderInfo> info;
    private OnItemClickListner mListner;
    public interface OnItemClickListner{
        void onDeleteClick(int position);
    }
    public void setOnItemClickListner(OnItemClickListner listener){
        mListner=listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time_and_date;
        TextView no_order_items;
        TextView order_id;
        TextView things_ordered;
        ImageView mDeleteImage;

        public ViewHolder(@NonNull View itemView, final OnItemClickListner listener) {
            super(itemView);
            time_and_date=itemView.findViewById(R.id.orderitem1);
            no_order_items=itemView.findViewById(R.id.orderitem2);
            things_ordered=itemView.findViewById(R.id.orderitem3);
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
    public CancelAdapter(ArrayList<OrderInfo>info){
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
        OrderInfo orderInfo=info.get(position);
        viewHolder.time_and_date.append(orderInfo.getTime_and_date());
        viewHolder.no_order_items.append("" + orderInfo.getNoOrders());
        //viewHolder.order_id.append("" + orderInfo.getOrderID());
        viewHolder.things_ordered.append("" + orderInfo.getThings_ordered());


//        viewHolder.time_and_date.setText(orderInfo.getTime_and_date());
//        viewHolder.no_order_items.setText(orderInfo.getNoOrders());
//        viewHolder.things_ordered.setText(orderInfo.getThings_ordered());
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public void added(OrderInfo orderInfo){
        info.add(orderInfo);
        notifyItemInserted(info.indexOf(orderInfo));

    }
}
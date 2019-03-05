package com.example.student.homemade;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {
    MenuItemAdapter(Context context) {
        context = context;
    }

    private Context context;
    private ArrayList<MenuItem> items;
    private HashMap<String,String> itemPictures;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircleImageView photo;
        public EditText price;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            photo = view.findViewById(R.id.item_image);
            price = view.findViewById(R.id.item_price);

        }
    }


    public MenuItemAdapter(Context context, ArrayList<MenuItem> items, HashMap<String,String> itemPictures) {
        this.context = context;
        this.items = items;
        this.itemPictures = itemPictures;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.menu_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MenuItem item = items.get(position);
        holder.name.setText(item.getName());

        holder.price.setText(item.getPrice().toString());
        if (item.getPrice() == 0) {
            Log.d("bind", item.getName());
            holder.price.setError("enter");
        }

        holder.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Log.d("before",s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("ontextchanged",s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    item.setPrice(Long.valueOf(s.toString()));
                } else if (s.length() == 0) {
                    item.setPrice(0L);
                }
                // Log.d("after",s.toString());

            }
        });
        if(itemPictures.containsKey(item.getName()))
            Glide.with(context).load(itemPictures.get(item.getName())).into(holder.photo);
        else
            Glide.with(context).load(android.R.drawable.ic_menu_report_image).into(holder.photo);


    }

    @Override
    public int getItemCount() {

        return items.size();

    }

    public void added(MenuItem c) {
        Log.d("added @ adapter", items.size() + "s");
        items.add(c);
        notifyItemInserted(items.indexOf(c));
    }

    public void remove(MenuItem c) {

        int pos = items.indexOf(c);
        items.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, items.size());
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

}
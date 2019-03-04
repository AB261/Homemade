package com.example.student.homemade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.student.homemade.ui.HistoricalOrdersFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CancelOrderFragment extends Fragment {
    private ArrayList<CancelItem> mExampleList;
    private RecyclerView mRecyclerview;
    private CancelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    View v;

    public CancelOrderFragment(){

    }
    public static CancelOrderFragment newInstance() {
        CancelOrderFragment fragment = new CancelOrderFragment();
        return fragment;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cancel);
//        createExampleList();
//        buildRecyclerView();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_cancel, container, false);
        createExampleList();
        buildRecyclerView();
        return v;
    }
    public void removeItem(int position){

        final int position1 = position;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext());//, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder//.setTitle("Logout")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        mExampleList.remove(position1);
                        mAdapter.notifyItemRemoved(position1);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    public void changeItem(int position,String text){
        mExampleList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }
    public void createExampleList(){
        mExampleList=new ArrayList<>();
        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
        mExampleList.add(new CancelItem("Line12","Line2","Line3"));
        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
        mExampleList.add(new CancelItem("Line25","Line2","Line3"));
        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
        mExampleList.add(new CancelItem("Line12","Line2","Line3"));
        mExampleList.add(new CancelItem("Line1","Line2","Line3"));
        mExampleList.add(new CancelItem("Line25","Line2","Line3"));


    }
    public void buildRecyclerView(){
        mRecyclerview=v.findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new CancelAdapter(mExampleList);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListner(new CancelAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                changeItem(position," ");
            }


            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }
}

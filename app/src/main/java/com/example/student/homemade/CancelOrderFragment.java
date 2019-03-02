package com.example.student.homemade;

import android.support.v4.app.Fragment;
import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.student.homemade.ui.HistoricalOrdersFragment;

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
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
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

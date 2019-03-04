package com.example.student.homemade.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.student.homemade.R;
import com.example.student.homemade.RatingandReviewActivity;

import java.util.ArrayList;


public class ConsumerUIFragment extends Fragment {
    View v;

    // DECLARING BUTTONS TO CHANGE LAYOUT
    Context context;

    Button orderingButton; //FOR ORDERING PAGE
    Button reviewButton;


    //declaring list views for the two lists
    ListView orderList;
    //ListView historyOfOrder;





    public ConsumerUIFragment() {
        // Required empty public constructor
    }


    public static ConsumerUIFragment newInstance() {
        return new ConsumerUIFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FUNCTIONS TO CHANGE LAYOUT ON BUTTON PRESS

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_consumer_ui, container, false);
        context=getActivity();

        orderingButton = (Button) v.findViewById(R.id.orderButton);
        reviewButton = (Button) v.findViewById(R.id.review_button);

        orderingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

//                Toast.makeText(ConsumerDashboard.this, "BUTTON PRESSED", Toast.LENGTH_SHORT).show();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , RatingandReviewActivity.class);
                startActivity(intent);
            }
        });


        //connecting them by id
        orderList = (ListView) v.findViewById(R.id.currentOrders);
        //historyOfOrder = (ListView) v.findViewById(R.id.historyOfOrders);

        //random initialization for the moment start
        ArrayList<String> order = new ArrayList<String>();
        order.add("BIRIYANI");
        order.add("DOSA");
        order.add("UPMA");
        order.add("ALOO PARATHA");
        order.add("SEEK KABAB");

//        ArrayList<String> history = new ArrayList<String>();
//        history.add("MASALA IDLI");
//        history.add("FIRED RICE");
//        history.add("SHEZWAN CHOWMIN");
//        history.add("OMLET");
//        history.add("CHICKEN KABAB");
//        history.add("SOYABEAN SUKKA");
//        //random initialization for the moment end

        //MAKING ARRAY ADAPTER TO DISPLAY
        ArrayAdapter<String> arrayOrder = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,order);
        // ArrayAdapter<String> arrayHistory = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,history);
        orderList.setAdapter(arrayOrder);
        //historyOfOrder.setAdapter(arrayHistory);
        //ARRAY ADAPTER AND DISPLAY IN LIST DONE


        //DEFINING SOME FUNCTION TO EXECUTE WHEN SOME ELEMENT IN LIST VIEW IS TAPPED

        //FOR ORDERLIST LIST
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        //FOR HISTORY OF ORDERLIST LIST
//        historyOfOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//            }
//        });

        return v;

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}

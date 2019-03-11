package com.example.student.homemade;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;



public class CustomAdapter extends ArrayAdapter<FoodItem> {

    Activity activity;


    List<FoodItem> foodItems;

    public CustomAdapter(Context context, List<FoodItem> foodItems, Activity mainActivity) {
        super(context, R.layout.my_list_item, foodItems);
        this.foodItems = foodItems;
        //
        this.activity= mainActivity;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent){



        LayoutInflater itemInflater = LayoutInflater.from(getContext());
        final View itemView = itemInflater.inflate(R.layout.my_list_item, parent, false);

        final FoodItem singleFoodItem = getItem(position);
        //ImageView itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
        EditText itemNumber = (EditText) itemView.findViewById(R.id.itemNumber);
        TextView itemName = (TextView) itemView.findViewById(R.id.itemName);
        TextView itemCost = (TextView) itemView.findViewById(R.id.itemCost);

        itemName.setText(singleFoodItem.itemName);
        itemCost.setText(Float.toString(singleFoodItem.itemCost));
        itemNumber.setText(String.valueOf(singleFoodItem.itemNumber));

        Button orderButton = (Button) itemView.findViewById(R.id.orderButton);
        Button increase = (Button) itemView.findViewById(R.id.increase);
        Button decrease = (Button) itemView.findViewById(R.id.decrease);

        increase.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText itemNumber = itemView.findViewById(R.id.itemNumber);
                // TextView totalCost = convertView.findViewById(R.id.totalCost);

                singleFoodItem.itemNumber+=1;
                itemNumber.setText(String.valueOf(singleFoodItem.itemNumber));

                float cost = 0;

                for(FoodItem item: foodItems) {
                    cost += item.itemCost * item.itemNumber;
                }
                TextView textView = activity.findViewById(R
                        .id.totalCost);
                textView.setText(String.valueOf(cost));
//                totalCost.setText(Float.toString(cost));
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                EditText itemNumber = itemView.findViewById(R.id.itemNumber);
                TextView totalCost = activity.findViewById(R.id.totalCost);


                if(singleFoodItem.itemNumber - 1 >= 0) {
                    singleFoodItem.itemNumber -= 1;
                    itemNumber.setText(String.valueOf(singleFoodItem.itemNumber));
                }

                float cost = 0;

                for(FoodItem item: foodItems) {
                    cost += item.itemCost * item.itemNumber;
                }

                totalCost.setText(Float.toString(cost));


//                itemNumber.setText(Math.max(Integer.parseInt(itemNumber.getText().toString()) - 1,0));
            }


        });



        return itemView;
    }
}
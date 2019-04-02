
package com.example.student.homemade;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.Map;

public class MassOrdersDisplaySellerRecyclerViewAdapter extends RecyclerView.Adapter<MassOrdersDisplaySellerRecyclerViewAdapter.ViewHolder> {
    private ArrayList<MassOrderInfo> info;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String consumer;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView time_and_date;
        TextView no_order_items;
        TextView order_id;
        TextView things_ordered;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time_and_date = itemView.findViewById(R.id.time_and_date);
            no_order_items = itemView.findViewById(R.id.no_order_items);
            order_id = itemView.findViewById(R.id.order_id);
            things_ordered = itemView.findViewById(R.id.things_ordered);
            layout = itemView.findViewById(R.id.orders_layout_rl);


        }
    }

    public MassOrdersDisplaySellerRecyclerViewAdapter(Context context, ArrayList<MassOrderInfo> info) {
        this.info = info;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_recycler_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final MassOrderInfo massOrderInfo = info.get(i);
        String consumerId = massOrderInfo.getConsumer();
        firebaseFirestore.collection("user").document(consumerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("USERINFO ",document.get("username").toString());
                    consumer = document.get("username").toString();
                    Log.d("HERE", consumer);
                    viewHolder.order_id.append(consumer);
                }
                else {
                    Log.d("INSIDE ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
        viewHolder.time_and_date.append(massOrderInfo.getOrderTime() + " " + massOrderInfo.getOrderDate());
        viewHolder.no_order_items.append("" + massOrderInfo.getNoOrders());
        int total_number_of_orders=0;
        Iterator hmIterator = massOrderInfo.getOrderItems().entrySet().iterator();
        String item = "";
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            Long value = Long.parseLong(mapElement.getValue().toString());
            Log.d("bro",mapElement.getKey() + " : " + value);
            if(value!=0)
            {
                total_number_of_orders+=value;
                item += mapElement.getKey() + "(" + value + ") ,";
            }

        }


//        for(Object value : massOrderInfo.getOrderItems().values()){
//            if(Integer.parseInt(value.toString())!=0){
//                total_number_of_orders += Integer.parseInt(value.toString());
//                item += massOrderInfo.getOrderItems().get(j).get("itemName").toString() + "(" + massOrderInfo.getItemsOrdered().get(j).get("itemNumber").toString() + ") ,";
//            }
//        }
//
//
//        for(int j=0;j<massOrderInfo.getNoOrders();j++) {
//            if (!massOrderInfo.getOrderItems().get("itemNumber").toString().equals("0")) {
//                total_number_of_orders += Integer.parseInt(massOrderInfo.getOrderItems().get(j).get("itemNumber").toString());
//                item += massOrderInfo.getOrderItems().get(j).get("itemName").toString() + "(" + massOrderInfo.getItemsOrdered().get(j).get("itemNumber").toString() + ") ,";
//                Log.d("THIS", massOrderInfo.getOrderItems().get(j).get("itemNumber").toString() + " " + massOrderInfo.getItemsOrdered().get(j).get("itemName").toString());
//            }
//        }

            viewHolder.things_ordered.append("" + item);
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public void added(MassOrderInfo orderInfo) {
        info.add(orderInfo);
        notifyItemInserted(info.indexOf(orderInfo));

    }


}
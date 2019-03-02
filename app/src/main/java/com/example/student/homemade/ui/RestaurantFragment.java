package com.example.student.homemade.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.homemade.R;
import com.example.student.homemade.RestaurantAdapter;
import com.example.student.homemade.RestaurantModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


//Fragment displays restaurant list
public class RestaurantFragment extends Fragment {


    final double latitude = 12.9807;
    final double longitude = 74.8031;
    View v;
    SwipeRefreshLayout swipeRefreshLayout;
    String TAG = "RestaurantFragment";
    ArrayList<RestaurantModel> restaurantList, dupRestaurantList;
    RecyclerView mRecyclerView;
    RestaurantAdapter myAdapter;
    ProgressBar progressBar;
    EditText editText, minratingEditText;
    TextView minRatingText;
    Spinner filterSpinner;


    //Sets up the database


    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.restaurant_card, container, false);
        mRecyclerView = v.findViewById(R.id.cardView);
        swipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        editText = v.findViewById(R.id.inputSearch);
        minratingEditText = v.findViewById(R.id.min_rating_edit_text);
        minRatingText = v.findViewById(R.id.min_rating_text);
        getActivity().setTitle("Restaurants Available");
        progressBar = v.findViewById(R.id.progress_circular);
        filterSpinner = v.findViewById(R.id.filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        filterSpinner.setAdapter(adapter);
        setinitVis();
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                switch (selectedFilter) {
                    case "Alphabetically":
                        Collections.sort(restaurantList, new Comparator<RestaurantModel>() {
                            @Override
                            public int compare(RestaurantModel o1, RestaurantModel o2) {
                                return o1.getRestaurantName().compareToIgnoreCase(o2.getRestaurantName());
                            }
                        });
                        break;
                    case "Distance: Near to Far":
                        Collections.sort(restaurantList, new Comparator<RestaurantModel>() {
                            @Override
                            public int compare(RestaurantModel o1, RestaurantModel o2) {
                                if (o1.getDistance() > o2.getDistance()) return 1;
                                else if (o1.getDistance() < o2.getDistance()) return -1;
                                else return 0;
                            }
                        });
                        break;
                    case "Rating: High to Low":
                        Collections.sort(restaurantList, new Comparator<RestaurantModel>() {
                            @Override
                            public int compare(RestaurantModel o1, RestaurantModel o2) {
                                if (o1.getRating() > o2.getRating()) return -1;
                                else if (o1.getRating() < o2.getRating()) return 1;
                                else return 0;
                            }
                        });
                        break;
                    default:
                        restaurantList.clear();
                        restaurantList.addAll(dupRestaurantList);
                        break;
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        minratingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                restaurantList.clear();
                restaurantList.addAll(dupRestaurantList);
                boolean b=s instanceof String;
                Log.d(TAG,s.toString()+b);

                if (s.length()>0) {
                    double rating = Double.parseDouble(s.toString());
                    if (rating > 5.0 || rating < 0) {
                        Toast.makeText(getContext(), "Please input a rating between 0 and 5", Toast.LENGTH_LONG).show();
                        restaurantList.clear();
                        myAdapter.notifyDataSetChanged();
                    } else {
                        restaurantList.clear();
                        filterbyrating(Double.parseDouble(s.toString()));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
//                restaurantList.clear();
//                restaurantList.addAll(dupRestaurantList);
//                if (!(s.toString().isEmpty())) {
//                    double rating = Double.parseDouble(s.toString());
//                    if (rating > 5.0 || rating < 0) {
//                        Toast.makeText(getContext(), "Please input a rating between 0 and 5", Toast.LENGTH_LONG).show();
//                    } else {
//                        filterbyrating(Double.parseDouble(s.toString()));
//                    }
//                }

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==0) {
                    restaurantList.clear();
                    restaurantList.addAll(dupRestaurantList);
                    myAdapter.notifyDataSetChanged();
                }
                else{
                    restaurantList.clear();
                    filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length()==0) {
                    restaurantList.clear();
                    restaurantList.addAll(dupRestaurantList);
                    myAdapter.notifyDataSetChanged();
                }
                else{
                    restaurantList.clear();
                    filter(s.toString());
                }

            }
        });
        //SetVisibility


        return v;

    }



    public void setinitVis() {
        progressBar.setVisibility(View.VISIBLE);
        filterSpinner.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        minRatingText.setVisibility(View.GONE);
        minratingEditText.setVisibility(View.GONE);
    }

    public void setfinVis() {
        progressBar.setVisibility(View.GONE);
        filterSpinner.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        minRatingText.setVisibility(View.VISIBLE);
        minratingEditText.setVisibility(View.VISIBLE);
    }

    //Attaches adapter and makes a list when activity is created
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.current_location = "In Nitk";


        mRecyclerView.setHasFixedSize(true);
        restaurantList = new ArrayList<>();
        dupRestaurantList = new ArrayList<>();

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(MyLayoutManager);
        myAdapter = new RestaurantAdapter(getContext(), restaurantList);
        mRecyclerView.setAdapter(myAdapter);
        initializeList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restaurantList.clear();
                dupRestaurantList.clear();
                myAdapter.notifyDataSetChanged();
                setinitVis();
                initializeList();
                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    //initializes a list of dummy data
    public void initializeList() {
        final ArrayList<String> restaurantName = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> review = new ArrayList<>();
        ArrayList<Float> distance = new ArrayList<>();
        ArrayList<String> imageResourceId = new ArrayList<>();
        ArrayList<Float> rating = new ArrayList<>();

        final Object[] restaurantModels = {restaurantName, description, review, distance, imageResourceId, rating};

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference provider = db.collection("Provider");
        provider.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = 0;
                        final int[] counter = {0};
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                final String[] userID = {"1234"};


                                Map map = document.getData();
                                userID[0] = (String) map.get("userid");


                                Log.d(TAG, document.getId() + " => " + map);
                                ArrayList<String> restaurantNames = (ArrayList<String>) restaurantModels[0];
                                ArrayList<String> imageResourceIds = (ArrayList<String>) restaurantModels[4];
                                ArrayList<String> descriptions = (ArrayList<String>) restaurantModels[1];
                                ArrayList<Float> distances = (ArrayList<Float>) restaurantModels[3];

                                if (map.get("restaurantname") == null) {
                                    restaurantNames.add("NITK NC");
                                } else {
                                    restaurantNames.add(map.get("restaurantname").toString());
                                }
                                if (map.get("imageResourceId") == null) {
                                    imageResourceIds.add("null");
                                } else {
                                    imageResourceIds.add((String) map.get("imageResourceId"));
                                }

                                if (map.get("description") == null) {
                                    String str = "We strive to deliver to you the best food possible";
                                    descriptions.add(str);
                                } else {
                                    descriptions.add(map.get("description").toString());
                                }

                                if (map.get("address") == null) {
                                    Location crntLocation = new Location(restaurantNames.get(i));
                                    crntLocation.setLatitude(latitude);
                                    crntLocation.setLongitude(longitude);
                                    Location newLocation = new Location("Current Location");
                                    newLocation.setLatitude(74.7943);
                                    newLocation.setLongitude(13.0108);
                                    distances.add(crntLocation.distanceTo(newLocation) / 1000);
                                } else {
                                    GeoPoint restaurantLocation = (GeoPoint) map.get("address");
                                    Location crntLocation = new Location(restaurantNames.get(i));
                                    crntLocation.setLatitude(latitude);
                                    crntLocation.setLongitude(longitude);
                                    Location newLocation = new Location("Current Location");
                                    newLocation.setLatitude(restaurantLocation.getLatitude());
                                    newLocation.setLongitude(restaurantLocation.getLongitude());
                                    double d = crntLocation.distanceTo(newLocation) / 1000;
                                    d = round(d, 2);
                                    distances.add((float) d);
                                }


//                                Log.d(TAG, restaurantNames.get(0)+ imageResourceIds.get(0) + descriptions.get(0));


//                                Log.d(TAG, userID[0]);

                                final CollectionReference ratingsAndReviews = db.collection("Reviews and Ratings");
                                OnCompleteListener<QuerySnapshot> completeListener;
                                ratingsAndReviews.whereEqualTo("reviewee", userID[0])
                                        .get()
                                        .addOnCompleteListener(completeListener = new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                int i = 1;
                                                int counter = 0;
                                                Double totalRating = new Double(0);
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        Map map = document.getData();
                                                        Log.d(TAG, document.getId() + " => " + map);

                                                        ArrayList<String> reviews = (ArrayList<String>) restaurantModels[2];
                                                        reviews.add((String) map.get("review"));
//                                                        Log.d(TAG, reviews.get(i));
                                                        ArrayList<Double> ratings = (ArrayList<Double>) restaurantModels[5];
                                                        Long ratingLong = new Long((Long) map.get("ratings"));
                                                        double ratingdouble = ratingLong.doubleValue();
                                                        totalRating += ratingdouble;
                                                        ratings.add(0, totalRating / i);

                                                        Log.d(TAG, ratings.get(0).toString() + reviews.get(i - 1));
                                                        i++;


                                                        Log.d(TAG, "MAP SIZE" + map.size());


                                                    }
                                                }

                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                ArrayList<String> restaurantNames = (ArrayList<String>) restaurantModels[0];
                                                ArrayList<String> imageResourceIds = (ArrayList<String>) restaurantModels[4];
                                                ArrayList<String> descriptions = (ArrayList<String>) restaurantModels[1];
                                                ArrayList<Float> distances = (ArrayList<Float>) restaurantModels[3];
                                                ArrayList<Double> ratings = (ArrayList<Double>) restaurantModels[5];
                                                ArrayList<String> reviews = (ArrayList<String>) restaurantModels[2];
                                                ArrayList<String> reviewsToBeCopied = new ArrayList<>(reviews.size());
                                                for (String x : reviews) {
                                                    reviewsToBeCopied.add(x);
                                                }

//                                                Log.d(TAG,String.valueOf(restaurantNames.size()) );
//                                                Log.d(TAG,String.valueOf(imageResourceIds.size()) );
//                                                Log.d(TAG,String.valueOf(descriptions.size()) );
//                                                Log.d(TAG,String.valueOf(descriptions.get(counter[0])));
//                                                Log.d(TAG,String.valueOf(ratings.size()) );
//                                                Log.d(TAG,String.valueOf(reviews.size()) );

                                                Log.d(TAG, String.valueOf(counter[0]));
                                                if (reviews.size() == 0) {
                                                    reviews.add("None");
                                                }
                                                if (ratings.size() == 0) {
                                                    ratings.add(4.0);
                                                }


//
                                                setfinVis();

                                                RestaurantModel restaurantModel = new RestaurantModel(restaurantNames.get(counter[0]), descriptions.get(counter[0]), reviewsToBeCopied, distances.get(counter[0]), imageResourceIds.get(counter[0]), ratings.get(0));
                                                counter[0] = counter[0] + 1;

//                                                Log.d(TAG,String.valueOf(counter[0]));
                                                restaurantList.add(restaurantModel);
                                                dupRestaurantList.add(restaurantModel);

                                                myAdapter.notifyDataSetChanged();

                                                reviews.clear();
                                                ratings.clear();
                                                Log.d(TAG, restaurantList.get(0).getReview().toString());


                                            }
                                        });


//                                restaurantNames.clear();
//                                imageResourceIds.clear();
//                                descriptions.clear();
//                                distances.clear();
//                                ArrayList<Double> ratings = (ArrayList<Double>) restaurantModels[5];
//                                ratings.clear();
//                                ArrayList<String> reviews = (ArrayList<String>) restaurantModels[2];
//                                reviews.clear();


//
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

//            Log.d(TAG,restaurantList.get(0).getReview().toString());


    }

    public void updateList(ArrayList<RestaurantModel> list) {
        restaurantList.clear();
        restaurantList.addAll(list);
        myAdapter.notifyDataSetChanged();
    }
    public void filterbyrating(double rating) {
        ArrayList<RestaurantModel> temp = new ArrayList<>();
        for (RestaurantModel restaurantModel : dupRestaurantList) {
            if (restaurantModel.getRating() >= rating) {
                temp.add(restaurantModel);
            }
        }
        updateList(temp);
    }

    void filter(String text) {
        ArrayList<RestaurantModel> temp = new ArrayList();
        if (text.isEmpty()) {
            temp.clear();
            temp.addAll(dupRestaurantList);
        }
        for (RestaurantModel restaurantModel : dupRestaurantList) {
            if (restaurantModel.getRestaurantName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(restaurantModel);
            }

        }

        updateList(temp);
    }


}
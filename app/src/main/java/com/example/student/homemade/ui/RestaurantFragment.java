package com.example.student.homemade.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.example.student.homemade.GpsUtils;
import com.example.student.homemade.R;
import com.example.student.homemade.RestaurantAdapter;
import com.example.student.homemade.RestaurantModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import java.util.Locale;
import java.util.Map;


//Fragment displays restaurant list
public class RestaurantFragment extends Fragment {


    double latitude;
    double longitude;
    Context context;
    View v;
    SwipeRefreshLayout swipeRefreshLayout;
    String TAG = "RestaurantFragment";
    ArrayList<RestaurantModel> restaurantList, dupRestaurantList;
    RecyclerView mRecyclerView;
    RestaurantAdapter myAdapter;
    ProgressBar progressBar;
    EditText editText, minratingEditText;
    TextView minRatingText, emptyTextView;
    Spinner filterSpinner;
    private int locationRequestCode = 1000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isContinue = false;
    private boolean isGPS = false;
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;


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

        context = getActivity();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
        new GpsUtils(context).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
//                        Log.d(TAG,"Latitude1:"+latitude+"Longitude1"+longitude);
//                        if (!isContinue) {
//                            txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
//                        } else {
//                            stringBuilder.append(wayLatitude);
//                            stringBuilder.append("-");
//                            stringBuilder.append(wayLongitude);
//                            stringBuilder.append("\n\n");
//                            txtContinueLocation.setText(stringBuilder.toString());

//                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                        getLocation();
                    }
                }
            }
        };


        v = inflater.inflate(R.layout.restaurant_card, container, false);
        mRecyclerView = v.findViewById(R.id.cardView);
        swipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        editText = v.findViewById(R.id.inputSearch);
        minratingEditText = v.findViewById(R.id.min_rating_edit_text);
        emptyTextView = v.findViewById(R.id.Empty_text_view);
        minRatingText = v.findViewById(R.id.min_rating_text);
        getActivity().setTitle("Restaurants Available");
        progressBar = v.findViewById(R.id.progress_circular);
        Sprite cubeGrid = new CubeGrid();
        progressBar.setIndeterminateDrawable(cubeGrid);
        filterSpinner = v.findViewById(R.id.filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        filterSpinner.setAdapter(adapter);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            setinitVis();
            Log.d(TAG,"Stop2");

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
                    boolean b = s instanceof String;
//                    Log.d(TAG, s.toString() + b);

                    if (s.length() > 0) {
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
                    if (s.toString().length() == 0) {
                        restaurantList.clear();
                        restaurantList.addAll(dupRestaurantList);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        restaurantList.clear();
                        filter(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.toString().length() == 0) {
                        restaurantList.clear();
                        restaurantList.addAll(dupRestaurantList);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        restaurantList.clear();
                        filter(s.toString());
                    }

                }
            });
            //SetVisibility


        return v;

    }

    private void getLocation() {
        Log.d(TAG,"Stop3");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity)context, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        initializeList();

                        Log.d(TAG,"Latitude3:"+latitude+"Longitude3"+longitude);
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        Log.d(TAG,"Stop4");
                        initializeList();
                    } else {
                        Log.d(TAG,"Stop5");
                        mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity)context, location -> {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                initializeList();
                                Log.d(TAG,"Stop6");
                                Log.d(TAG,"Latitude:"+latitude+"Longitude"+longitude);
                            } else {
                                Log.d(TAG,"Stop7");
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                initializeList();

                            }
                        });
                    }
                } else {
                    Log.d(TAG,"Stop8");
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"Stop9");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true;
            }
        }
    }

    public void setinitVis() {
        progressBar.setVisibility(View.VISIBLE);
        filterSpinner.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        minRatingText.setVisibility(View.GONE);
        minratingEditText.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);
    }

    public void setfinVis() {
        progressBar.setVisibility(View.GONE);
        filterSpinner.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        minRatingText.setVisibility(View.VISIBLE);
        minratingEditText.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

    public void setVisIfNoResult() {
        progressBar.setVisibility(View.GONE);
        filterSpinner.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        minRatingText.setVisibility(View.VISIBLE);
        minratingEditText.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
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
        Log.d(TAG,"Stop10");
        getLocation();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restaurantList.clear();
                dupRestaurantList.clear();
                myAdapter.notifyDataSetChanged();
                setinitVis();
                getLocation();
                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    //initializes a list of dummy data
    public void initializeList() {
        dupRestaurantList.clear();
        restaurantList.clear();
        final ArrayList<String> restaurantName = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> review = new ArrayList<>();
        ArrayList<Float> distance = new ArrayList<>();
        ArrayList<String> imageResourceId = new ArrayList<>();
        ArrayList<Float> rating = new ArrayList<>();
//        Log.d(TAG,"Latitudeis:"+latitude+"Longitudeis"+longitude);
        Log.d(TAG,"Stop11");

        final Object[] restaurantModels = {restaurantName, description, review, distance, imageResourceId, rating};

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference provider = db.collection("Provider");
        provider.whereEqualTo("active",true).get()
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


//                                Log.d(TAG, document.getId() + " => " + map);
                                ArrayList<String> restaurantNames = (ArrayList<String>) restaurantModels[0];
                                ArrayList<String> imageResourceIds = (ArrayList<String>) restaurantModels[4];
                                ArrayList<String> descriptions = (ArrayList<String>) restaurantModels[1];
                                ArrayList<Float> distances = (ArrayList<Float>) restaurantModels[3];

                                if (map.get("restaurantname") == null) {
                                    restaurantNames.add("NITK NC");
                                    userID[0]="NITK NC";

                                } else {
                                    restaurantNames.add(map.get("restaurantname").toString());
                                    userID[0]=map.get("restaurantname").toString();
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

//                                Log.d(TAG,"Location calc correc :Latitudeis:"+latitude+"Longitudeis"+longitude);

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


                                Log.d(TAG, userID[0]);

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
//                                                        Log.d(TAG, document.getId() + " => " + map);

                                                        ArrayList<String> reviews = (ArrayList<String>) restaurantModels[2];
                                                        reviews.add((String) map.get("review"));
//                                                        Log.d(TAG, reviews.get(i));
                                                        ArrayList<Double> ratings = (ArrayList<Double>) restaurantModels[5];
                                                        Long ratingLong = new Long((Long) map.get("ratings"));
                                                        double ratingdouble = ratingLong.doubleValue();
                                                        totalRating += ratingdouble;
                                                        ratings.add(0, totalRating / i);

//                                                        Log.d(TAG, ratings.get(0).toString() + reviews.get(i - 1));
                                                        i++;


//                                                        Log.d(TAG, "MAP SIZE" + map.size());


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

//                                                Log.d(TAG,restaurantNames.get(0) );
//                                                Log.d(TAG,imageResourceId.get(0) );
//                                                Log.d(TAG,descriptions.get(0) );
//                                                Log.d(TAG,String.valueOf(distances.get(0)));
//                                                Log.d(TAG,String.valueOf(ratings );
//                                                Log.d(TAG,reviews.toString());

//                                                Log.d(TAG, String.valueOf(counter[0]));
                                                if (reviews.size() == 0) {
                                                    reviewsToBeCopied.add("None");
                                                }
                                                if (ratings.size() == 0) {
                                                    ratings.add(4.0);
                                                }


//


                                                    RestaurantModel restaurantModel = new RestaurantModel(restaurantNames.get(counter[0]), descriptions.get(counter[0]), reviewsToBeCopied, distances.get(counter[0]), imageResourceIds.get(counter[0]), ratings.get(0));
                                                    restaurantList.add(restaurantModel);
                                                    dupRestaurantList.add(restaurantModel);

                                                counter[0] = counter[0] + 1;

//                                                Log.d(TAG,String.valueOf(counter[0]));


                                                myAdapter.notifyDataSetChanged();
                                                if(restaurantList.isEmpty()){
                                                    setVisIfNoResult();
                                                } else{
                                                    setfinVis();
                                                }

                                                reviews.clear();
                                                ratings.clear();
//                                                Log.d(TAG, restaurantList.get(0).getReview().toString());


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
        if(list.isEmpty()){
            setVisIfNoResult();
        }
        else{
            setfinVis();
        }
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
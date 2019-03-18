package com.example.student.homemade;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnPolylineClickListener {

    private static final String TAG = "MapsActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public static final float DEFAULT_ZOOM = 15f;
    Location userLocationCurrent;

    //widgets
    private EditText mSearchText;
    private ImageView mGps;
    private ProgressBar mSearchProgressBar;

    //vars
    private Boolean mLocationPermissionsGranted;
    private FusedLocationProviderClient mFusedLoactionProviderClient;
    private FirebaseFirestore mDb;
    private FirebaseStorage storage;

    private GoogleMap mMap;
    private GeoApiContext mGeoApiContext = null;
    private ArrayList<MapsPolylineData> mPolylinesData = new ArrayList<>();
    private Marker mSelectedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mDb = FirebaseFirestore.getInstance();

        mSearchText = findViewById(R.id.input_search_maps);
        mGps = findViewById(R.id.ic_gps);
        mSearchProgressBar = findViewById(R.id.mapsSearchProgressBar);

        mSearchProgressBar.setVisibility(View.INVISIBLE);

        if (isServicesOK()) {
            getLocationPermission();
            if (mLocationPermissionsGranted == true) {
                init();
                initMap();
            }
        }
    }

    private void init() {
        Log.e(TAG, "init: initializing..");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for search
                    geoLocate();
                    hideSoftKeyBoard();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGps.setVisibility(View.INVISIBLE);
                mSearchProgressBar.setVisibility(View.VISIBLE);
                Log.e(TAG, "Find text search button clicked.");
                //getDeviceLocation();
                findLocationFromText();
                mSearchText.getText().clear();
            }
        });

        setAllRestaurantsLocations();

        hideSoftKeyBoard();
    }

    private void setAllRestaurantsLocations() {
        mDb.collection("Provider")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GeoPoint geoPoint;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                                geoPoint = (GeoPoint) map.get("address");
                                Log.d(TAG, "findLocationFromText: Found " + (GeoPoint)map.get("address"));
                                if((String) map.get("restaurantName") != null)
                                    moveCameraWithImage(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), DEFAULT_ZOOM, (String) map.get("restaurantName"), (String) map.get("description"));
                                getDeviceLocation();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void findLocationFromText() {

        final String searchString = mSearchText.getText().toString().trim();


        mDb.collection("Provider")
                .whereEqualTo("restaurantName", searchString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "findLocationFromText: found a restaurant");
                                GeoPoint geoPoint;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                                geoPoint = (GeoPoint) map.get("address");
                                Log.d(TAG, "findLocationFromText: Found " + (GeoPoint)map.get("address"));
                                moveCamera(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), DEFAULT_ZOOM, searchString);
                                mGps.setVisibility(View.VISIBLE);
                                mSearchProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        else {
                            Log.e(TAG, "findLocationFromText: unable to find restaurant");
                            Toast.makeText(MapsActivity.this, "Unable to find "+ searchString +" Does not exist on Database" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void geoLocate() {
        Log.e(TAG, "In Geolocate:");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }

        if(list.size() > 0) {
            Address address = list.get(0);

            Log.e(TAG, "geoLocate: found a location, list size is greater than zero.");
            Toast.makeText(this, address.toString() , Toast.LENGTH_LONG).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.setOnPolylineClickListener(MapsActivity.this);
        mMap.setOnInfoWindowClickListener(MapsActivity.this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the current device location");
        mFusedLoactionProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted) {
                //mFusedLoactionProviderClient.getLastLocation();
                mFusedLoactionProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location.");
                            Location currentLocation = (Location) task.getResult();
                            userLocationCurrent = currentLocation;
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");

                        }else {
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        catch(SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if(title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title);
            mMap.addMarker(options);
        }

        else {
            MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyBoard();
    }

    private void moveCameraWithImage(LatLng latlng, float zoom, String title, String description) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title)
                    .snippet(description)
                    .icon(bitMapDescriptorFromVector(getApplicationContext(), R.mipmap.ic_maps_res_disp_icon_round));
        mMap.addMarker(options);

        hideSoftKeyBoard();
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            }
            else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE );
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                for(int i=0;i<grantResults.length;++i) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        mLocationPermissionsGranted = false;
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
            }
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);

        if(available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicecsOK: Google Play Services is working.");
            Toast.makeText(this, "Google Play Services is working fine.", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occured but we can fix it.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
            return false;
        }

        else {
            Toast.makeText(this, "You can't make map requests. Device outdated.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private BitmapDescriptor bitMapDescriptorFromVector(Context context, int vectorResId) {
        final int markerWidth = (int) context.getResources().getDimension(R.dimen.custom_marker_image_maps);
        final int markerHeight = (int) context.getResources().getDimension(R.dimen.custom_marker_image_maps);
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void hideSoftKeyBoard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void calculateDirections(Marker marker){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        userLocationCurrent.getLatitude(),
                        userLocationCurrent.getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);
                if(mPolylinesData.size() > 0) {
                    for(MapsPolylineData polylineData : mPolylinesData) {
                        polylineData.getPolyline().remove();
                    }
                    mPolylinesData.clear();
                    mPolylinesData = new ArrayList<>();
                }

                double duration = 99999999;

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(MapsActivity.this, R.color.grey));
                    polyline.setClickable(true);
                    mPolylinesData.add(new MapsPolylineData(polyline, route.legs[0]));
                    

                    double tempDuration = route.legs[0].duration.inSeconds;
                    if(tempDuration < duration) {
                        duration = tempDuration;
                        onPolylineClick(polyline);
                    }

                    mSelectedMarker.setVisible(false);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_options, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.normal_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                return true;
//            case R.id.hybrid_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                return true;
//            case R.id.satellite_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                return true;
//            case R.id.terrain_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                return true;
//            default:
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("My Location")) {
            marker.hideInfoWindow();
        }

        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setMessage("Determine route to this restaurant?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unsused")final DialogInterface dialog, @SuppressWarnings("unsused") final int id) {
                            mSelectedMarker = marker;
                            calculateDirections(marker);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unsused") final int id) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        int index = 0;
        for(MapsPolylineData polylineData: mPolylinesData){
            index++;
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(MapsActivity.this, R.color.bluedark));
                polylineData.getPolyline().setZIndex(1);

                LatLng endLocation = new LatLng(
                        polylineData.getLeg().endLocation.lat,
                        polylineData.getLeg().endLocation.lng
                );

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(endLocation)
                        .title("Trip: #" + index)
                        .snippet("Duration: " + polylineData.getLeg().duration)
                );

                marker.showInfoWindow();
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(MapsActivity.this, R.color.grey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

}

package com.example.student.homemade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.student.homemade.ui.ConsumerDetailsFragment;
import com.example.student.homemade.ui.ConsumerUIFragment;
import com.example.student.homemade.ui.DeliveryAndTrackingFragment;
import com.example.student.homemade.ui.HistoricalOrdersFragment;
import com.example.student.homemade.ui.MassOrderFragment;
import com.example.student.homemade.ui.RestaurantFragment;
import com.example.student.homemade.ui.TrendingItemsFragment;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements ProviderUIFragment.OnFragmentInteractionListener, ConsumerUIFragment.OnFragmentInteractionListener{
    private static int c;
    Button logout;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private Context context;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    String mActivityTitle;
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Dashboard");
        //logout = findViewById(R.id.main_btn_logout);
        mDrawer = findViewById(R.id.drawer_view);

        //-----------------added here

        SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
        Log.v(TAG,settings.getString("email","homemade"));
        c = settings.getInt("ProviderOrConsumerFlag", 0);
        Log.d("BRO","" + c);
        navigationView = findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();
        menu.add(0, 0, 0, "Dashboard");




        if(c==1){
            //Provider



        }
        else{
            //Consumer
            menu.add(0, 4, 0, "Restaurant List");
            menu.add(0, 5, 0, "Order History");
            menu.add(0, 6, 0, "Mass Orders");
            menu.add(0, 7, 0, "Trending Items");
            menu.add(0, 8, 0, "Cancel Order");
            menu.add(0, 9, 0, "Delivery and Tracking");
        }

        menu.add(0, 1, 0, "User Details");
        menu.add(0, 2, 0, "Send Feedback");
        menu.add(0, 3, 0, "Logout");

        //----------------ended here
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setupDrawerContent(navigationView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mDrawerToggle);
        setupDrawerContent(navigationView);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header);
        final ImageView imageView = hView.findViewById(R.id.header_imageView);
        imageView.setVisibility(View.INVISIBLE);
        final ProgressBar progressBar = hView.findViewById(R.id.progress_bar_header);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        Log.v(TAG+"c:",String.valueOf(c));
        StringBuilder url = (c!=2) ? new StringBuilder("providers_photos/profile_pictures/") : new StringBuilder("consumers_photos/");
        url.append(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.v(TAG+"url:",url.toString());
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(url.toString().trim());
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                Log.v(TAG,"Got image");
                imageView.setMinimumHeight(dm.heightPixels);
                imageView.setMinimumWidth(dm.widthPixels);
                imageView.setImageBitmap(bm);
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.userphoto);
                imageView.setVisibility(View.VISIBLE);
            }
        });
        TextView headertextView = hView.findViewById(R.id.username_header);
        headertextView.setText(settings.getString("email","homemade"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
        c = settings.getInt("ProviderOrConsumerFlag", 0);
        Class fragmentClass;
        Fragment fragment = null;
        //Intent loginIntent = getIntent();
        //c = loginIntent.getIntExtra("ProviderOrConsumer", 0);
        if(c == 1) {
            fragmentClass = ProviderUIFragment.class;
            FragmentManager fragmentManager = getSupportFragmentManager();
            try {

                 fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {

                e.printStackTrace();

            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if(c == 2) {
            fragmentClass = ConsumerUIFragment.class;
            FragmentManager fragmentManager = getSupportFragmentManager();
            try {

                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {

                e.printStackTrace();

            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else
            fragmentClass = null;




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null) {
            Log.d("Main", user.getUid());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {


        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override

                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight

                        selectDrawerItem(menuItem);

                        return true;

                    }

                });


    }

    public void selectDrawerItem(MenuItem menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked

        Fragment fragment = null;

        Class fragmentClass=null;

        switch (menuItem.getItemId()) {
            case 0:
                if(c != 2)
                    fragmentClass = ProviderUIFragment.class;
                else if(c == 2)
                    fragmentClass = ConsumerUIFragment.class;
//                else
//                    fragmentClass = null;
                break;
            case 4:
                fragmentClass = RestaurantFragment.class;
                //toolbar.setTitle("Resturant List");
                break;
            case 5:
                fragmentClass = HistoricalOrdersFragment.class;
                // toolbar.setTitle("Resturant List");
                break;
            case 6:
                fragmentClass = MassOrderFragment.class;
                // toolbar.setTitle("Mass Order");
                break;
            case 7:
                fragmentClass = TrendingItemsFragment.class;
                // toolbar.setTitle("Trending Items");
                break;
            case 9:
                fragmentClass = DeliveryAndTrackingFragment.class;
                Intent intentMap = new Intent(context, MapsActivity.class);
                startActivity(intentMap);
                // toolbar.setTitle("Delivery and Tracking");
                break;
            case 1:
//                Intent intent1 = new Intent(context, ConsumerDetailsFragment.class);
//                startActivity(intent1);
                fragmentClass = ConsumerDetailsFragment.class;
                break;
            case 8:
                fragmentClass = CancelOrderFragment.class;
                break;
            case 2:
                fragmentClass = FeedbackFragment.class;
                break;
            case 3:

                //Logic for logout


                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context);//, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder//.setTitle("Logout")
                        .setMessage("Are you sure you want to logout from your account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken("853704140115-a7gjo5s8uvvsfig7ij7s1t6cg9ejh1ld.apps.googleusercontent.com")
                                        .requestEmail()
                                        .build();
                                FirebaseAuth.getInstance().signOut();
                                GoogleSignIn.getClient(getApplicationContext(), gso).signOut();
                                LoginManager.getInstance().logOut();

                                //intent to login page
                                Intent newLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(newLoginIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                if(c == 1)
                    fragmentClass = ProviderUIFragment.class;
                else
                    fragmentClass = ConsumerUIFragment.class;


//                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken("853704140115-a7gjo5s8uvvsfig7ij7s1t6cg9ejh1ld.apps.googleusercontent.com")
//                        .requestEmail()
//                        .build();
//                FirebaseAuth.getInstance().signOut();
//                GoogleSignIn.getClient(getApplicationContext(), gso).signOut();
//                LoginManager.getInstance().logOut();
//
//                //intent to login page
//                Intent newLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(newLoginIntent);
//                finish();


                break;



            default:
                fragmentClass = ProviderUIFragment.class;

        }


        try {

            fragment = (Fragment) fragmentClass.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        } catch (Exception e) {

            e.printStackTrace();

        }


        // Insert the fragment by replacing any existing fragment



        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);

        // Set action bar title

        setTitle(menuItem.getTitle());

        // Close the navigation drawer

        mDrawer.closeDrawers();

    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

}


package com.example.student.homemade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.student.homemade.ui.ConsumerDetailsFragment;
import com.example.student.homemade.ui.ConsumerUIFragment;
import com.example.student.homemade.ui.DeliveryAndTrackingFragment;
import com.example.student.homemade.ui.HistoricalOrdersFragment;
import com.example.student.homemade.ui.MassOrderFragment;
import com.example.student.homemade.ui.RestaurantFragment;
import com.example.student.homemade.ui.TrendingItemsFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements ProviderUIFragment.OnFragmentInteractionListener, ConsumerUIFragment.OnFragmentInteractionListener{
    private static int c;
    Button logout;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private Context context;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Dashboard");
        //logout = findViewById(R.id.main_btn_logout);
        mDrawer = findViewById(R.id.drawer_view);
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
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mDrawerToggle);
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        SharedPreferences settings = getSharedPreferences("ProviderOrConsumerPreference", 0);
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

    private void setupDrawer() {

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

        Class fragmentClass;

        switch (menuItem.getItemId()) {
            case R.id.dashboard:
                if(c == 1)
                    fragmentClass = ProviderUIFragment.class;
                else if(c == 2)
                    fragmentClass = ConsumerUIFragment.class;
                else
                    fragmentClass = null;
                break;
            case R.id.rest_list:
                fragmentClass = RestaurantFragment.class;
                //toolbar.setTitle("Resturant List");
                break;
            case R.id.historical_orders:
                fragmentClass = HistoricalOrdersFragment.class;
                // toolbar.setTitle("Resturant List");
                break;
            case R.id.mass_orders:
                fragmentClass = MassOrderFragment.class;
                // toolbar.setTitle("Mass Order");
                break;
            case R.id.trending_items:
                fragmentClass = TrendingItemsFragment.class;
                // toolbar.setTitle("Trending Items");
                break;
            case R.id.delivery_and_tracking:
                fragmentClass = DeliveryAndTrackingFragment.class;
                Intent intentMap = new Intent(context, MapsActivity.class);
                startActivity(intentMap);
                // toolbar.setTitle("Delivery and Tracking");
                break;
            case R.id.user_button:
//                Intent intent1 = new Intent(context, ConsumerDetailsFragment.class);
//                startActivity(intent1);
                fragmentClass = ConsumerDetailsFragment.class;
                break;
            case R.id.cancel_order:
                fragmentClass = CancelOrderFragment.class;
                break;
            case R.id.main_btn_logout:

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

                fragmentClass = MassOrderFragment.class;


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

        } catch (Exception e) {

            e.printStackTrace();

        }


        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();


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


package com.goutermout.beerratingapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private DrawerLayout drawerLayout;
    private static final String MENU_ADD_BEER = "Add Beer";
    private static final String MENU_SEARCH = "Search";
    private static final String MENU_ABOUT = "About";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddBeerFragment fragment = new AddBeerFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        Log.i("Menu Title", String.valueOf(menuItem.getTitle()));

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if(menuItem.getTitle().equals(MENU_ADD_BEER)) {

                            swapToAddBeerFragment();
                        } else if (menuItem.getTitle().equals(MENU_SEARCH)){
                            swapSearchFragment("","");
                        } else if(menuItem.getTitle().equals(MENU_ABOUT)){
                            swapToAboutFragment();
                        }
                        return true;
                    }
                });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void swapToAddBeerFragment(){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddBeerFragment fragment = new AddBeerFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
 //       fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void swapToAboutFragment(){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AboutFragment fragment = new AboutFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        //       fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void swapSearchFragment(String beerName, String beerBrew){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("beerName", beerName);
        bundle.putString("beerBrew", beerBrew);

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}



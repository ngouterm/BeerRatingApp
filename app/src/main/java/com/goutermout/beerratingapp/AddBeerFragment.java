package com.goutermout.beerratingapp;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBeerFragment extends Fragment {
    private SQLiteDatabase db;
    private String beerName;
    private String beerBrew;
    private float tasteRating;
    private float lookRating;
    private float smellRating;
    private float feelRating;


    private TextView beerName_tv;
    private TextView beerBrew_tv;
    private RatingBar tasteRating_rb;
    private RatingBar lookRating_rb;
    private RatingBar smellRating_rb;
    private RatingBar feelRating_rb;
    private Button addBeer_btn;

    public AddBeerFragment()  {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_beer, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        beerName_tv = view.findViewById(R.id.beer_name_txt);
        beerBrew_tv = view.findViewById(R.id.beer_brew_txt);
        tasteRating_rb = view.findViewById(R.id.tasteRating);
        lookRating_rb = view.findViewById(R.id.lookRating);
        smellRating_rb = view.findViewById(R.id.smellRating);
        feelRating_rb = view.findViewById(R.id.feelRating);



        addBeer_btn = view.findViewById(R.id.add_beer_btn);
        addBeer_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                beerName = beerName_tv.getText().toString();
                beerBrew = beerBrew_tv.getText().toString();

                tasteRating = tasteRating_rb.getRating();
                lookRating = lookRating_rb.getRating();
                smellRating = smellRating_rb.getRating();
                feelRating = feelRating_rb.getRating();

                if(beerDuplicateChecker(beerName)){
                    return;
                }

                SQLiteOpenHelper beerDatabaseHelper = new BeerDatabaseHelper(getActivity());
                try {
                    db = beerDatabaseHelper.getWritableDatabase();
                } catch(SQLiteException e) {
                    Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                }

                try {
                    BeerDatabaseHelper.insertBeer(db,beerName,beerBrew,tasteRating,lookRating,smellRating,feelRating);
                    clearScreen();
                    Toast toast = Toast.makeText(getActivity(), "Beer Added", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (SQLiteException e){
                    Toast toast = Toast.makeText(getActivity(), "Beer Add Failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
    private boolean beerDuplicateChecker(String name){
        ArrayList<HashMap<String,String>> beers = new ArrayList<>();

        SQLiteOpenHelper beerDatabaseHelper = new BeerDatabaseHelper(getActivity());
        try {
            db = beerDatabaseHelper.getReadableDatabase();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        try {
            beers = BeerDatabaseHelper.getBeers(db);
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(getActivity(), "Error getting beer list", Toast.LENGTH_SHORT);
            toast.show();
        }

        if(beers != null) {
            ArrayList<HashMap<String, String>> filterdBeers = new ArrayList<>();
            for (HashMap<String, String> resultsHash : beers) {
                HashMap<String, String> beerFound = new HashMap<String, String>();
                Log.i("results string", String.valueOf(resultsHash.get("NAME")));
                if (resultsHash.get("NAME").contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void clearScreen(){
        beerName_tv.setText("");
        beerBrew_tv.setText("");

        tasteRating_rb.setRating(0F);
        lookRating_rb.setRating(0F);
        smellRating_rb.setRating(0F);
        feelRating_rb.setRating(0F);
    }



}

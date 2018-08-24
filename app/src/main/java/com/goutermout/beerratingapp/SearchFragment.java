package com.goutermout.beerratingapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class SearchFragment extends Fragment {

    private View v;
    private SQLiteDatabase db;

    private TextView beerName_tv;
    private TextView beerBrew_tv;
    private ScrollView sv;
    private LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false);

        String beerName = getArguments().getString("beerName");
        String beerBrew = getArguments().getString("beerBrew");


        beerName_tv = v.findViewById(R.id.beer_name_txt);
        beerBrew_tv = v.findViewById(R.id.beer_brew_txt);

        beerName_tv.setText(beerName);
        beerBrew_tv.setText(beerBrew);

        ll = v.findViewById(R.id.database_results);


        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        beerViewUpdater(beerName_tv.getText().toString(),"name");
        beerName_tv.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                beerViewUpdater(s.toString(),"name");
            }
        });
        beerBrew_tv.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                beerViewUpdater(s.toString(),"brewer");
            }
        });
    }

    private void beerViewUpdater(String filterText, String filter){
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

        if(beers != null){
            beerLister(beerFilter(beers,filterText,filter));
        }
    }



    private void beerLister(ArrayList<HashMap<String,String>> filterdBeers){
        // Create a LinearLayout element


        Log.i("Filtered Beers", String.valueOf(Objects.toString(filterdBeers)));
        ll.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(HashMap<String,String> beer : filterdBeers) {
            if (!(beer.isEmpty())) {
                RelativeLayout rl = new RelativeLayout(getActivity());
                //           llinner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
                //           llinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                //           llinner.setGravity(RelativeLayout.CENTER_HORIZONTAL);
                // Add text
                TextView tvName = new TextView(getActivity());
                tvName.setGravity(Gravity.LEFT);
                tvName.setText(beer.get("NAME"));
                tvName.setLayoutParams(layoutParams);
                Log.i("Name Value", String.valueOf(tvName.getText()));
//            tvName.setWidth(LayoutParams.WRAP_CONTENT);
                rl.addView(tvName);

                TextView tvBrew = new TextView(getContext());
                tvBrew.setGravity(Gravity.CENTER_HORIZONTAL);
                tvBrew.setText(beer.get("BREWER"));
                tvBrew.setLayoutParams(layoutParams);
                Log.i("Brewer Value", String.valueOf(tvBrew.getText()));
//            tvBrew.setWidth(LayoutParams.WRAP_CONTENT);
                rl.addView(tvBrew);

                TextView tvScore = new TextView(getActivity());
                tvScore.setGravity(Gravity.RIGHT);
                tvScore.setText(beer.get("OVERALL"));
                tvScore.setLayoutParams(layoutParams);
                Log.i("Score Value", String.valueOf(tvScore.getText()));
//            tvScore.setWidth(LayoutParams.WRAP_CONTENT);
                rl.addView(tvScore);
                Log.i("Cursor Value", String.valueOf(Objects.toString(beer)));


                ll.addView(rl);
            }

        }

    }

    private ArrayList<HashMap<String, String>> beerFilter(ArrayList<HashMap<String,String>> beers, String filterText, String filter){
        ArrayList<HashMap<String,String>> filterdBeers = new ArrayList<>();
        for(HashMap<String,String> resultsHash : beers){
            HashMap<String,String> beerFound = new  HashMap<String,String>();
            if (resultsHash.get("NAME").contains(filterText) && filter.equals("name")){
                beerFound.put("NAME", resultsHash.get("NAME"));
                beerFound.put("BREWER", resultsHash.get("BREWER"));
                beerFound.put("OVERALL", resultsHash.get("OVERALL"));
            }else if(resultsHash.get("BREWER").contains(filterText) && filter.equals("brewer")){
                beerFound.put("NAME", resultsHash.get("NAME"));
                beerFound.put("BREWER", resultsHash.get("BREWER"));
                beerFound.put("OVERALL", resultsHash.get("OVERALL"));
            }
            filterdBeers.add(beerFound);
        }
       return  filterdBeers;
    }
}

package com.goutermout.beerratingapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.floor;


public class BeerDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "beerdatabase"; //name of our database
    private static final int DB_VERSION = 1; //the version of our database
    private static Context context;

    BeerDatabaseHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db,0,DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db,oldVersion,newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE BEER (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "BREWER TEXT, "
                    + "OVERALL FLOAT, "
                    + "TASTE FLOAT, "
                    + "LOOK FLOAT, "
                    + "SMELL FLOAT, "
                    + "FEEL FLOAT);");
        }

    }

    public static void insertBeer(SQLiteDatabase db, String name,
                                   String description, float taste, float look, float smell, float feel){
        ContentValues beerValues = new ContentValues();
        beerValues.put("NAME", name);
        beerValues.put("BREWER", description);
        beerValues.put("OVERALL", round(((taste + look + smell + feel)/4),2));
        beerValues.put("TASTE", taste);
        beerValues.put("LOOK", look);
        beerValues.put("SMELL", smell);
        beerValues.put("FEEL", feel);
//      beerValues.put("IMAGE_RESOURCE_ID", resourceId);

        db.insert("BEER",null, beerValues);
        Log.i("results string", String.valueOf(beerValues));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList<HashMap<String,String>> getBeers(SQLiteDatabase db) {

        final String TABLE_NAME = "BEER";
        ArrayList<HashMap<String,String>> data = new ArrayList<>();
        HashMap<String,String> results;
        try{
            Cursor cursor      = db.query(TABLE_NAME,
                    new String[]{"_id","NAME","BREWER","OVERALL","TASTE","LOOK","SMELL","FEEL"},
                    null,null,null,
                    null,null,null);


            if (cursor.moveToFirst()) {
                do {
                    results = new HashMap<>();
                    for(int column=0; column <= cursor.getColumnCount(); column++) {

                        switch (column) {
                            case 0:
                                Log.i("Cursor Value", String.valueOf(Objects.toString(cursor.getLong(column))));
                                results.put(cursor.getColumnName(column), Objects.toString(cursor.getLong(column)));
                                break;
                            case 1:
                            case 2:
                                Log.i("Cursor Value", String.valueOf(Objects.toString(cursor.getString(column))));
                                results.put(cursor.getColumnName(column), cursor.getString(column));
                                break;
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                Log.i("Cursor Value", String.valueOf(Objects.toString(cursor.getFloat(column))));
                                results.put(cursor.getColumnName(column), Objects.toString(cursor.getFloat(column)));
                                break;
                        }
                    }
                    data.add(results);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(context,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        Log.i("The Hash", String.valueOf(data));
        return data;
    }

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
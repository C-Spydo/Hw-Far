package com.spydotechcorps.hwfar.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spydotechcorps.hwfar.provider.MyContentProvider;
/**
 * Created by INGENIO on 3/8/2015.
 */

public class Dbhandler extends SQLiteOpenHelper {


    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "distanceDB.db";
    public static final String TABLE_DISTANCES = "distances";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DISTANCE = "distance";

    public Dbhandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myCR = context.getContentResolver();    // obtaining reference to content resolver in content provider
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_DISTANCES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DESCRIPTION
                + " TEXT," + COLUMN_DISTANCE + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTANCES);
        onCreate(db);

    }


    public void addDistances(String a,String b) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, a);
        values.put(COLUMN_DISTANCE, b);

        myCR.insert(MyContentProvider.CONTENT_URI, values);
    }

    public saveDistance findDistances(String Distancesname) {
        String[] projection = {COLUMN_ID,
                COLUMN_DESCRIPTION, COLUMN_DISTANCE };

        String selection = "Distancesname = \"" + Distancesname + "\"";

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,
                projection, selection, null,
                null);

        saveDistance Distances = new saveDistance();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            Distances.setID(Integer.parseInt(cursor.getString(0)));
            Distances.setdesc(cursor.getString(1));
            Distances.setdistance(cursor.getString(2));
            cursor.close();
        } else {
            Distances = null;
        }
        return Distances;
    }

    public boolean deleteDistances(String Distancesname) {

        boolean result = false;

        String selection = "Distancesname = \"" + Distancesname + "\"";

        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI,
                selection, null);

        if (rowsDeleted > 0)
            result = true;

        return result;
    }



}





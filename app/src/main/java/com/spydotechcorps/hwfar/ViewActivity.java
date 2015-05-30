package com.spydotechcorps.hwfar;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.spydotechcorps.hwfar.database.Dbhandler;
import com.spydotechcorps.hwfar.provider.MyContentProvider;

import static android.support.v4.app.LoaderManager.LoaderCallbacks;

public class ViewActivity extends FragmentActivity {
    // ... existing code ...
    public SimpleCursorAdapter myCursorAdapter;
    public static final int DISTANCES_LOADER_ID = 212;
    private static String TAG="CursorLoader";
    private ContentResolver contentResolver;
    private  ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ...
        setContentView(R.layout.fragment_view);
       listView = (ListView) findViewById(R.id.listview_records);
        setupCursorAdapter();


        //for now abeg
       // getSupportLoaderManager().initLoader(DISTANCES_LOADER_ID,
                //new Bundle(), distancesLoader);

           // defining listview and linking listview to the cursor adapter
        //note: cursor adapter is being used here instead of array adapter because the data to be displayed
        //in the list is from our content provider instead of from an array.

    }
//------------------------------------------------------------------------------------------------
    // Create simple cursor adapter to connect the cursor dataset we load with a ListView
    private void setupCursorAdapter() {
        // Column data from cursor to bind views from
        String[] uiBindFrom = { Dbhandler.COLUMN_DESCRIPTION, Dbhandler.COLUMN_DISTANCE
                 };
        // View IDs which will have the respective column data inserted
       // int[] uiBindTo = {android.R.id.text1, android.R.id.text2  };
        //use a custom defined textviews instead
        int[] uiBindTo = {R.id.description, R.id.location };
        // Create the simple cursor adapter to use for our list
        // specifying the template to inflate (item_contact),

        //modifying template to reflect my initial code here
       /* myCursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_2,
                null, uiBindFrom, uiBindTo,
                0);*/
        //use the below instead
        String[] projection = { Dbhandler.COLUMN_ID,Dbhandler.COLUMN_DESCRIPTION, Dbhandler.COLUMN_DISTANCE };
        contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MyContentProvider.CONTENT_URI,projection,null,null,null);
        if(cursor == null){
            Log.d("Gush", "Cursor returned null");

        }else
        if(cursor.getCount() < 1){
            Toast.makeText(getApplicationContext(),"Data retrieve not successful", Toast.LENGTH_LONG).show();

        }else
         {
            myCursorAdapter = new SimpleCursorAdapter(
                    this, R.layout.list_item,
                    cursor, uiBindFrom, uiBindTo, 0);
            try {
                listView.setAdapter(myCursorAdapter);
            } catch (Exception e) {
               Log.d("Error","Ouch, could not populate list view");
            }
        }


      //not useful
      /* if (myCursorAdapter!=null) {
           try {
               listView.setAdapter(myCursorAdapter);
           } catch (Exception lanre) {
               //Toast.makeText(getApplicationContext(), "Testing Microphone", Toast.LENGTH_SHORT).show();
           }
       }
        else{
                Toast.makeText(getApplicationContext(), "There is nothing to View", Toast.LENGTH_SHORT).show();
       }*/

    }
    //---------------------------------------------------------------------------------------------------

    // Defines the asynchronous callback for the contacts data loader
    public LoaderCallbacks<Cursor> distancesLoader =
            new LoaderCallbacks<Cursor>() {
                // Create and return the actual cursor loader for the contacts data
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // Define the columns to retrieve

                    // Construct the loader
                    String[] projection = { Dbhandler.COLUMN_DESCRIPTION, Dbhandler.COLUMN_DISTANCE };
                    CursorLoader cursorLoader = new CursorLoader(ViewActivity.this,
                            MyContentProvider.CONTENT_URI, // URI
                            projection,  // projection fields
                            null, // the selection criteria
                            null, // the selection args
                            null // the sort order
                    );

                    // CursorLoader cursorLoader =new CursorLoader(this, uri,projection,selection,selectionArguments,sortOrder);
                    // Return the loader for use
                    return cursorLoader;
                }

                // When the system finishes retrieving the Cursor through the CursorLoader,
                // a call to the onLoadFinished() method takes place.
                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    // The swapCursor() method assigns the new Cursor to the adapter
                    try {
                        if(myCursorAdapter!=null && cursor!=null)
                            myCursorAdapter.swapCursor(cursor); //swap the new cursor in.
                        else
                            Log.v(TAG,"OnLoadFinished: myCursorAdapter is null");
                        }
                    catch(Exception samson){
                        //Toast.makeText(getApplicationContext(),samson.toString(), Toast.LENGTH_SHORT).show();
                    }

                   /*
                        myCursorAdapter.swapCursor(cursor);

                    catch(Exception samson){
                        //Toast.makeText(getApplicationContext(),samson.toString(), Toast.LENGTH_SHORT).show();
                    }*/
                }





                // This method is triggered when the loader is being reset
                // and the loader data is no longer available. Called if the data
                // in the provider changes and the Cursor becomes stale.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    if(myCursorAdapter!=null)
                        myCursorAdapter.swapCursor(null);
                    else
                        Log.v(TAG,"OnLoadFinished: myCursorAdapter is null");



                    //myCursorAdapter.swapCursor(null);
                }
            };

}
package com.spydotechcorps.hwfar;

//import android.app.LoaderManager;
//import android.content.Loader;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.CursorAdapter;
import android.support.v4.widget.CursorAdapter;
//import android.content.CursorLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.widget.Toast;

import com.spydotechcorps.hwfar.provider.MyContentProvider;


public  class ViewActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int Records_LOADER = 0;
    private CursorAdapter myCursorAdapter;
    private static final String TAGG = "FragmentActivity";
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
       getSupportLoaderManager().initLoader(Records_LOADER,null,this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public CursorLoader onCreateLoader(int records_LOADER, Bundle additionalArguments){

        if (getSupportLoaderManager().hasRunningLoaders()){
            Log.i(TAGG, "Using existing Loader");
        }
        else{
            Log.i(TAGG,"Creating a new Loader");
        }
        Uri uri=MyContentProvider.CONTENT_URI;
        String[] projection=null;
        String selection=null;
        String[]selectionArguments=null;
        String sortOrder=null;
        Log.i(TAGG, "Querying content resolver on background thread.");
        CursorLoader cursorLoader =new CursorLoader(this, uri,projection,selection,selectionArguments,sortOrder);
        Log.i(TAGG, "Returning the Cursor");
        return cursorLoader;

    }

    //Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
    if (cursor!=null) {
        Log.i(TAGG, "Query finished. Number records" + "found in the Cursor: " + cursor.getCount());
        Log.i(TAGG, "Cursor passed to adapter. ");
        myCursorAdapter.swapCursor(cursor);
    }
    else{
        Toast.makeText(this, "No Records to show",
                Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        try  {
            Log.i(TAGG, "Loader reset. Cursor about to be closed. " + "We can't use it anymore");

            myCursorAdapter.swapCursor(null);
        }
        catch(Exception e){
            //String e=parse.toString(e);
        }
    }


    // @Override
    public void onLoaderReset(CursorLoader cursorLoader){
        if (cursorLoader!=null) {
            Log.i(TAGG, "Loader reset. Cursor about to be closed. " + "We can't use it anymore");

            myCursorAdapter.swapCursor(null);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
   public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view, container, false);
            return rootView;
        }
    }
}


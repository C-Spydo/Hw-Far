package com.spydotechcorps.hwfar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//extends service implements LocationListener
public class MainActivity extends ActionBarActivity {
    //private ShareActionProvider mShareActionProvider;
    private boolean mTwoPane;

    private String newObject;
    private String txt1;
    private String txt2;
    private String txt3;
    private static boolean isDataLoaded;


    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //newObject=new MyDataObject();

        if (findViewById(R.id.merge_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the app will switch to the tablet layout
            //which contains both the Location Fragment and tips fragment.
            mTwoPane = true;
            // In two-pane mode, show the tablet view in this activity by
            // inflating the fragment_main sw600dp xml instead of the normal fragment_main
             if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.merge_container, new LocationFragment())  // i still need to heavily edit this, don't really know what is happeing here.
                        .commit();
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LocationFragment())
                        .commit();
            }
        }


        Toast maintoast=Toast.makeText(getApplicationContext(),"Ensure GPS and Location Services are turned On", Toast.LENGTH_SHORT);
        maintoast.setGravity(Gravity.CENTER,0,0);
        maintoast.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button,
        // Parent Activity is specified in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        if (id == R.id.action_tips) {
            startActivity(new Intent(this, TipsActivity.class));
            return true;
        }
        if (id == R.id.action_snap) {
            hascamera();
            return true;
        }
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        if (id == R.id.action_viewRecords) {
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
           startActivity(intent);
            return true;
        }
       /* if (id == R.id.menu_share) {
            Intent sendIntent=new Intent();
            setShareIntent(sendIntent);
            Toast.makeText(getApplicationContext(), "Just Fooling Around",
                    Toast.LENGTH_LONG).show();
            return true;
        }*/

        //return super.onOptionsItemSelected(item);
        return true;
    }


    // checking if the device has a camera (either front or back or both)
    private boolean hascamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA
        )) {
            snapphoto(0);
            return true;
        } else {
            return false;
        }
    }


    //Intent to take picture, device will use any app capable of this actionand
   // the user is provided with a choice if there are more than one app capable of the action specified.
    private void snapphoto(int TAKE_PICTURE) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, TAKE_PICTURE);
        }
    }


    // code block to obtain current location from Google Maps
    // When this function is executed, control is handed over to the Google Map Application.
    private void openPreferredLocationInMap() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPrefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));



        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
        }
    }
}




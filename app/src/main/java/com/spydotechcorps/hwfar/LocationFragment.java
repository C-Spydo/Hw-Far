package com.spydotechcorps.hwfar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.spydotechcorps.hwfar.database.Dbhandler;


public class LocationFragment extends Fragment {
    protected LocationManager locationManager;
    //private int someStateValue;
    TextView result2;
    private String data1;
   // protected Button point1;
   // protected Button point2;
    Location location1;
    Location location2;
    private String m_Text = "";

    public LocationFragment() {
    }
    View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //retain this fragment
        setRetainInstance(true);
    }

    /*public void setData(String data){
        this.data1=data1;
    }
    public String getData(){

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

                //setActionBar(null);
            if (rootView!=null){
                ((ViewGroup)rootView.getParent()).removeView (rootView);
            }



        // INSERT SAVE INSTANCE GLOBAL VARIBALE HERE


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        Button point1 = (Button) rootView.findViewById(R.id.point1);
        Button point2 = (Button) rootView.findViewById(R.id.point2);
        Button Go= (Button) rootView.findViewById(R.id.gobutton);
        Button btnSave= (Button) rootView.findViewById(R.id.btnSave);
        final TextView result =(TextView) rootView.findViewById(R.id.txtResult);
        final TextView coord1 =(TextView) rootView.findViewById(R.id.coord1);
        final TextView coord2 =(TextView) rootView.findViewById(R.id.coord2);
        final TextView tv2 =(TextView) rootView.findViewById(R.id.textView2);
        final TextView tv3 =(TextView) rootView.findViewById(R.id.textView3);

        point1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rootView) {
                location1= scl();
                if (location1!=null) {
                        String message1 = String.format(
                        "Lng: %1$s \n Lat: %2$s",
                        location1.getLongitude(), location1.getLatitude());
                        coord1.setText(message1);
                        tv2.setText("Move to Point 2 and Press Button Point 2 below");
                }
                else{
                }
            }
        });
        point2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rootView) {

                location2 = scl();
                if (location2 != null) {
                    String message2 = String.format(
                            "Lng: %1$s \n Lat: %2$s",
                            location2.getLongitude(), location2.getLatitude());
                            coord2.setText(message2);
                }
            }
        });



        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rootView) {
                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());
                //sharedPrefs.registerOnSharedPreferenceChangeListener(this);
                String unit = sharedPrefs.getString(
                        getString(R.string.pref_units_key),
                        getString(R.string.pref_units_default));

               /* Preference.OnPreferenceChangeListener listener=new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        //unit=newValue;
                        return false;
                    }
                };*/

                //Toast.makeText(getActivity(),unit,Toast.LENGTH_SHORT).show();

                if (location1!=null){
                    if (location2!=null) {
                        String unitlabel;
                        double distance = location1.distanceTo(location2);
                        if (unit.equals("m")) {
                            distance=distance;
                            unitlabel="  Metres";
                        }
                        else if (unit.equals("km")){
                            distance=(distance/1000);
                            unitlabel="  Kilometres";
                        }
                        else if (unit.equals("Centimetres")){
                            distance=(distance*100);
                            unitlabel="  Centimetres";
                        }
                        else if (unit.equals("yards")){
                            distance=(distance*1.0936133);
                            unitlabel="  Yards";
                        }

                        else if (unit.equals("miles")){
                            distance=(distance*0.000621371192);
                            unitlabel="  Miles";
                        }

                        else if (unit.equals("in")){
                            distance=(distance*39.3700787);
                            unitlabel="  Inches";
                        }
                        else{
                            unitlabel="  Metres";
                        }
                        distance=(double)Math.round(distance * 10000) / 10000;
                        String dist = String.valueOf(distance);
                        tv3.setText("Distance Between Points 1 & 2 is ");
                        result.setText(dist+ unitlabel);
                        //result.setText("We gat you");
                        //Toast.makeText(LocationFragment.this, dist, Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rootView) {
                newsave(rootView);
            }






        });

         result2 =(TextView) rootView.findViewById(R.id.txtResult);

        return rootView;
    }


   // @Override
    public void newsave (View rootView) {
        final Dbhandler dbHandler = new Dbhandler(this.getActivity(), null, null, 1);

                if (result2.getText().toString()=="") {
                    Toast.makeText(getActivity(), "There is nothing to Save", Toast.LENGTH_SHORT).show();
                }
        else {
                        final String distance = (result2.getText().toString());

                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                        builder.setTitle("Description");

                        // Set up the input
                        final EditText input = new EditText(this.getActivity());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();

                                String ddesc = m_Text;
                                //saveDistance distances = new saveDistance(ddesc,distance);
                                try {
                                    dbHandler.addDistances(ddesc, distance);
                                } catch (Exception e) {
                                    //Toast.makeText()
                                }
                                //Toast.makeText(LocationFragment.this, "Save Successfull", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }




     }


    public Location scl() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        MyCurrentLocationListener locationListener = new MyCurrentLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                locationListener
        );


        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       // location.distanceTo(location2);
        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"Error!, Can't obtain Location Wait a While",Toast.LENGTH_SHORT).show();
        }

        return location;
    }


    class MyCurrentLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            /*String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(LocationFragment.this, message, Toast.LENGTH_LONG).show();*/
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(getActivity(), "Provider status changed",
                          Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getActivity(), "Provider disabled by the user. GPS turned off, Turn on & Wait a While", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String s) {
           Toast.makeText(getActivity(),"Provider enabled by the user. GPS turned on, Wait a While", Toast.LENGTH_SHORT).show();
        }
    }
}







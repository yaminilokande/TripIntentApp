package com.example.android.tripIntent.map;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.tripIntent.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;


/**
 * Created by BT on 11/25/16.
 */


public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private String addesses = null;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_section);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        new JSONTask().execute("http://54.186.205.2:8888/api/auth/login?email=gudururamya@gmail.com&password=Secret@123");

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



    }

    class JSONTask extends AsyncTask<String,String,String> implements OnMapReadyCallback{

        @Override
        protected String doInBackground(String... params) {
            URLConnection conn;
            BufferedReader reader = null;
            URL url = null;
            try {
                url = new URL(params[0]);
                conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(true);
                conn.connect();
                //Internal Redirect to Location API
                String newUrl = "http://54.186.205.2:8888/api/location";
                String cookies = conn.getHeaderField("Set-Cookie");
                // open the new connnection again
                conn = new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");

                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //textIn1.setText(result);
            addesses = result;
            if(addesses !=null) {
                JSONArray ary1 = null;
                try {
                    ary1 = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int n = ary1.length();
                String[] location = new String[n];
                try {

                    for (int i = 0; i < n; ++i) {
                        final JSONObject person = ary1.getJSONObject(i);
                        location[i] = person.getString("location");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("MAP","onPostExecute");
                    System.out.println("Printning Array inside finally");
                    System.out.println(Arrays.toString(location));
                    System.out.println("Inside Finally completed");

            }

            mapFragment.getMapAsync(this);


        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d("MAP","Inside onMapReady");
            mMap = googleMap;

            String ret = addesses;
            String[] addressList;
            if(addesses !=null) {
                JSONArray ary1 = null;
                try {
                    ary1 = new JSONArray(ret);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int n = ary1.length();
                addressList = new String[n];
                try {
                    for (int i = 0; i < n; ++i) {
                        final JSONObject person = ary1.getJSONObject(i);
                        addressList[i] = person.getString("location");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Printning Array inside finally");
                    System.out.println(Arrays.toString(addressList));
                    System.out.println("Inside Finally completed");
                }
            }else {
                addressList = new String[]{"Hong Kong", "Paris", "France","Iran" , "India"};
            }
            Geocoder geoCoder = new Geocoder(getApplicationContext());
            for(int i = 0; i<addressList.length ; i++) {
                try {
                    List<Address> locations = geoCoder.getFromLocationName(addressList[i], 1);
                    for (Address a : locations) {
                        double latti = a.getLatitude();
                        double longi = a.getLongitude();
                        LatLng placeCordinates = new LatLng(latti, longi);
                        //Marker object for showing point on map.
                        MarkerOptions marker1 = new MarkerOptions().position(placeCordinates).title(addressList[i]);
                        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue2));
                        //Adding marker on map
                        mMap.addMarker(marker1);
                      //  mMap.setOnMarkerClickListener(this);
                        //Camera will show last place added into Database
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeCordinates));
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this,marker.getTitle() + " has been clicked ", Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


        // Retrieve the data from the marker.
        // Return false to indicate that we have not consumed the event and that we wish

        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


}

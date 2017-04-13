package com.example.android.tripIntent.sos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tripIntent.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by BT on 10/18/16.
 */

// Messages are sent from this activity only when the user clicks the button 2 i,e the panic button.

public class ThirdActivity extends Activity {
    String MySms = null;
    private EditText textSMS;
    private TextView t;

    String log;
    int count=0;
    Button buttonSend;
    /*GPS*/
    private LocationManager locationManager =null;
    private LocationListener locationListener=null;

    private Button btnGetLocation = null;
    private EditText editLocation = null;



    private static final String TAG = "Debug";
    private Boolean flag = false;





    String name, phone, name1,phone1;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.third_sos);
        t=new TextView(this);


        textSMS = (EditText) findViewById(R.id.editTextSMS);
        textSMS.setKeyListener(null);

        t=(TextView)findViewById(R.id.textView1);
        t.setTextColor(Color.parseColor("#000000"));



        //GPS start
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        flag = displayGpsStatus();
        if (flag)
        {

            Log.v(TAG, "onClick");

            textSMS.setText("Please!! move your device to"+
                    " see the changes in coordinates."+"\nWait..");

            //pb.setVisibility(View.VISIBLE);
            locationListener = new MyLocationListener();

            locationManager.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);

        } else {
            alertbox("Gps Status:", "Your GPS is: OFF");
        }



        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber()+"Count: "+count;
            String ll = ""+count;
            Log.d("Name: ", log);

            count++;
        }






        addListenerOnButton();
    }

    public void addListenerOnButton() {

		/* // Use this if you want to send sms only once.
		  buttonSend = (Button) findViewById(R.id.button1);

		buttonSend.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

					flag = displayGpsStatus();
					if (flag)
					{

						Log.v(TAG, "onClick");

						textSMS.setText("Please!! move your device to"+
								" see the changes in coordinates."+"\nWait..");

						//pb.setVisibility(View.VISIBLE);
						locationListener = new MyLocationListener();

						locationListener = new MyLocationListener();

						locationManager.requestLocationUpdates(LocationManager
								.GPS_PROVIDER, 5000, 10,locationListener);

					} else {
						alertbox("Gps Status!!", "Your GPS is: OFF");
					}

					try {
						Log.d("Reading: ", "Reading all contacts..");
						List<Contact> contacts = db.getAllContacts();
						for (Contact cn : contacts) {
							System.out.println("PhNo: "+cn.getPhoneNumber()+" "+"Message: "+MySms);

						//	SmsManager smsManager = SmsManager.getDefault();
						//    smsManager.sendTextMessage(cn.getPhoneNumber(), null, MySms, null, null);

						//Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();

						}

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"SMS faild, please try again later!",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

			}

		});

		 */
        //GPS Button

        btnGetLocation = (Button) findViewById(R.id.button2);

        btnGetLocation.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                flag = displayGpsStatus();
                if (flag)
                {

                    Log.v(TAG, "onClick");

                    textSMS.setText("Please!! move your device to"+
                            " see the changes in coordinates."+"\nWait..");

                    //pb.setVisibility(View.VISIBLE);
                    locationListener = new MyLocationListener();

                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10,locationListener);

                } else {
                    alertbox("Gps Status!!", "Your GPS is: OFF");
                }

            }
        });

    }


    //Private class MylocationListener starts here
    private class MyLocationListener implements LocationListener {
     // Use this if you want to send sms on every location change.
        @Override
        public void onLocationChanged(Location loc) {
            String ad1 = null, ad2 = null;
            String s=null;
            textSMS.setText("");
            //pb.setVisibility(View.INVISIBLE);
            //Toast.makeText(getBaseContext(),"Location changed : Lat: " + loc.getLatitude()+ " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " +loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " +loc.getLatitude();
            Log.v(TAG, latitude);

			/*----------to get City-Name from coordinates ------------- */
            String cityName=null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address>  addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0)
                    //System.out.println(addresses.get(0).getLocality());
                    cityName=addresses.get(0).getLocality();
                //ad1=addresses.get(0).getFeatureName();
                ad1=addresses.get(0).getCountryName();
                ad2=addresses.get(0).getAddressLine(0);

                //cityName=addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(ad1 == null)
                s = "Help me.. I am in trouble";
            else
                s = "Help me.. I am here: "+ad2+" "+cityName+" "+ad1;
            textSMS.setText(s);
            MySms = s+"";
            System.out.println(s);



            try {
                Log.d("Sending: ", "Sending message..");
                List<Contact> contacts = db.getAllContacts();



                for (Contact cn : contacts) {
                    System.out.println("PhNo: "+cn.getPhoneNumber()+" "+"Message: "+MySms);
                    t.setText("Message send");

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(cn.getPhoneNumber(), null, MySms, null, null);

                    Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


}


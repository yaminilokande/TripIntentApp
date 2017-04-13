package com.example.android.tripIntent.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.tripIntent.R;
import com.example.android.tripIntent.map.MapsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


import static com.example.android.tripIntent.chat.ChatActivity.ANONYMOUS;


public class TabbedActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
  //  private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "TabbedActivity";
    private String mUsername;
    private String mPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
      //  mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
     //   mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.invite_menu:
                //sendInvitation();
                return true;
            case R.id.crash_menu:
                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
                causeCrash();
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                mUsername = ANONYMOUS;
                mPhotoUrl = null;
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            case R.id.fresh_config_menu:
                //fetchConfig();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);

    private void causeCrash() {
        throw new NullPointerException("Fake null pointer exception");
    }

//    private void sendInvitation() {
//        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//                .setMessage(getString(R.string.invitation_message))
//                .setCallToActionText(getString(R.string.invitation_cta))
//                .build();
//        startActivityForResult(intent, REQUEST_INVITE);
//    }

    // Fetch the config to determine the allowed length of messages.
//    public void fetchConfig() {
//        long cacheExpiration = 3600; // 1 hour in seconds
//        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
//        // server. This should not be used in release builds.
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//        }
//        mFirebaseRemoteConfig.fetch(cacheExpiration)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // Make the fetched config available via FirebaseRemoteConfig get<type> calls.
//                        mFirebaseRemoteConfig.activateFetched();
//                        applyRetrievedLengthLimit();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // There has been an error fetching the config
//                        Log.w(TAG, "Error fetching config: " + e.getMessage());
//                        applyRetrievedLengthLimit();
//                    }
//                });
//    }



    //Place Holder View deleted

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position){
//                case 0:
//                    ChatActivity tab1 = new ChatActivity();
//                    return tab1;
//                case 1:
//                    CameraActivity tab2 = new CameraActivity();
//                    return tab2;
//                case 2:
//                    SosActivity tab3 = new SosActivity();
//                    return tab3;
//                case 3:
//                    MapsActivity tab4 = new MapsActivity();
//                    return tab4;
//                default:
//                    return null;
//            }
//
//        }
//
//        @Override
//        public int getCount() {
//            // Show 4 total pages.
//            return 4;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "CHAT";
//                case 1:
//                    return "PHOTOS";
//                case 2:
//                    return "MAPS";
//                case 3:
//                    return "SOS";
//            }
//            return null;
//        }
//    }
}

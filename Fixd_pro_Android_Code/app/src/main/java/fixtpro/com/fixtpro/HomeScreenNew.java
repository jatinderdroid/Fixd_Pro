package fixtpro.com.fixtpro;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import fixtpro.com.fixtpro.fragment.AddServiceFragment;
import fixtpro.com.fixtpro.fragment.HasPowerSourceFragment;
import fixtpro.com.fixtpro.fragment.HomeFragment;
import fixtpro.com.fixtpro.fragment.InstallorRepairFragment;
import fixtpro.com.fixtpro.fragment.PartsFragment;
import fixtpro.com.fixtpro.fragment.RepairFragment;
import fixtpro.com.fixtpro.fragment.RepairInfoFragment;
import fixtpro.com.fixtpro.fragment.ScheduledListDetailsFragment;
import fixtpro.com.fixtpro.fragment.SignatureFragment;
import fixtpro.com.fixtpro.fragment.StartJobFragment;
import fixtpro.com.fixtpro.fragment.TellUsWhatsWrongFragment;
import fixtpro.com.fixtpro.fragment.WhatTypeOfServiceFragment;
import fixtpro.com.fixtpro.fragment.WhatsWrongFragment;
import fixtpro.com.fixtpro.fragment.WhichApplianceAddServiceFragment;
import fixtpro.com.fixtpro.fragment.WorkOrderFragment;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//http://stackoverflow.com/questions/13895149/sliding-menu-locks-touch-event-on-upper-view
public class HomeScreenNew extends BaseActivity implements ScheduledListDetailsFragment.OnFragmentInteractionListener,FragmentManager.OnBackStackChangedListener, StartJobFragment.OnFragmentInteractionListener,ConnectionCallbacks, OnConnectionFailedListener, LocationListener , InstallorRepairFragment.OnFragmentInteractionListener, AddServiceFragment.OnFragmentInteractionListener, WhatTypeOfServiceFragment.OnFragmentInteractionListener, WhichApplianceAddServiceFragment.OnFragmentInteractionListener, HasPowerSourceFragment.OnFragmentInteractionListener, WhatsWrongFragment.OnFragmentInteractionListener, TellUsWhatsWrongFragment.OnFragmentInteractionListener, RepairFragment.OnFragmentInteractionListener, PartsFragment.OnFragmentInteractionListener, WorkOrderFragment.OnFragmentInteractionListener, RepairInfoFragment.OnFragmentInteractionListener, SignatureFragment.OnFragmentInteractionListener,
        ResultCallback<LocationSettingsResult> {
    public String currentFragmentTag = "";
    int CONTACTUS_REQUESTCODE = 1;
    private ImageView img_Toggle, img_Right;
    private TextView titletext, txtDone, txtBack;
    FragmentManager fragmentManager ;
    SlidingMenu slidingMenu = null ;

    //    Location Service Variable Declaration
    protected static final String TAG = "location-updates-sample";

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;


    protected LocationSettingsRequest mLocationSettingsRequest;

    private LocationResponseListener locationResponseListener = null ;

    public HomeScreenNew() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slidingMenu = getSlidingMenu();
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        setContentView(R.layout.activity_home_screen_new);

        fragmentManager = getSupportFragmentManager();
        setWidgets();
        setListeners();
        initLayout();
//        / Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();


    }
    private void initLayout(){
        Bundle b = new Bundle();
        b.putString("title", getString(R.string.app_name));
        titletext.setText(b.getString("title", ""));
        HomeFragment fragment = new HomeFragment();
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment,Constants.HOME_FRAGMENT);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }
    public void hideRight(){
        img_Right.setVisibility(View.INVISIBLE);
        txtDone.setVisibility(View.INVISIBLE);
    }

    private void setListeners() {
        img_Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleLeftClick();
            }
        });
        img_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRightClick();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popStack();
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRightClick();
            }
        });

    }
    public void popStack(){
        fragmentManager.popBackStack();
    }
    private void handleLeftClick(){
        if (currentFragmentTag.equals(Constants.HOME_FRAGMENT) || currentFragmentTag.equals(Constants.MYJOB_FRAGMENT)
                || currentFragmentTag.equals(Constants.PAYMENT_FRAGMENT) || currentFragmentTag.equals(Constants.RATING_FRAGMENT)
                || currentFragmentTag.equals(Constants.SETTING_FRAGMENT) || currentFragmentTag.equals(Constants.START_JOB_FRAGMENT )
                || currentFragmentTag.equals(Constants.INSTALL_OR_REPAIR_FRAGMENT )){
            toggle();
        }else if (currentFragmentTag.equals(Constants.SCHEDULED_LIST_DETAILS_FRAGMENT)){
            popStack();
        } else if (currentFragmentTag.equals(Constants.PARTS_FRAGMENT)){
            ((PartsFragment)fragmentManager.findFragmentByTag(Constants.PARTS_FRAGMENT)).clearList();
        }
    }
    private void handleRightClick(){
        if (currentFragmentTag.equals(Constants.HOME_FRAGMENT)){
            Log.e("","-----"+((HomeFragment)fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT)));
            ((HomeFragment)fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT)).refresh();
        }else if(currentFragmentTag.equals(Constants.START_JOB_FRAGMENT)){
            //Show start job dialog
            ((StartJobFragment)fragmentManager.findFragmentByTag(Constants.START_JOB_FRAGMENT)).showStartJobDialog();
        }else if (currentFragmentTag.equals(Constants.TELL_US_WHATS_WRONG_FRAGMENT)){
            ((TellUsWhatsWrongFragment)fragmentManager.findFragmentByTag(Constants.TELL_US_WHATS_WRONG_FRAGMENT)).submitPost();
        }
        else if (currentFragmentTag.equals(Constants.PARTS_FRAGMENT)){
            ((PartsFragment)fragmentManager.findFragmentByTag(Constants.PARTS_FRAGMENT)).submitPost();
        }else if (currentFragmentTag.equals(Constants.WORK_ORDER_FRAGMENT)){
            ((WorkOrderFragment)fragmentManager.findFragmentByTag(Constants.WORK_ORDER_FRAGMENT)).submitPost();
        }else if (currentFragmentTag.equals(Constants.REPAIR_INFO_FRAGMENT)){
            ((RepairInfoFragment)fragmentManager.findFragmentByTag(Constants.REPAIR_INFO_FRAGMENT)).submitPost();
        }else if (currentFragmentTag.equals(Constants.WHATS_WRONG_FRAGMENT)){
            ((WhatsWrongFragment)fragmentManager.findFragmentByTag(Constants.WHATS_WRONG_FRAGMENT)).submitPost();
        }
    }
    private void setWidgets() {
        img_Toggle = (ImageView) findViewById(R.id.img_Toggle);
        img_Right = (ImageView) findViewById(R.id.img_Right);
        titletext = (TextView)findViewById(R.id.titletext);
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);
    }

    public void switchFragment(final Fragment fragment, final String Tag , final boolean addToStack,Bundle bundle) {
        if (slidingMenu.isMenuShowing())
        toggle();
        if (Tag.equals(currentFragmentTag))
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment,Tag);
                    if (addToStack)
                        fragmentTransaction.addToBackStack(Tag);
                    fragmentTransaction.commit();
                    fragmentManager.executePendingTransactions();
                }
            }
        },500);

    }
    public void logOut(){
        toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utilities.getSharedPreferences(HomeScreenNew.this).edit().clear().commit();
                Intent j = new Intent(HomeScreenNew.this, Login_Register_Activity.class);
                startActivity(j);
                finish();
            }
        }, 500);

    }
    public void contactUs(){
        toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(HomeScreenNew.this, ContactUsActivity.class);
                startActivityForResult(i, CONTACTUS_REQUESTCODE);
            }
        }, 500);
    }
    public void setCurrentFragmentTag(String currentFragmentTag){
        this.currentFragmentTag = currentFragmentTag ;
//        setToolBar(currentFragmentTag);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void setToolBar(String Tag){
        if (Tag.equals(Constants.HOME_FRAGMENT)){
            txtBack.setVisibility(View.GONE);
            txtDone.setVisibility(View.GONE);
            img_Right.setVisibility(View.VISIBLE);
            img_Toggle.setVisibility(View.VISIBLE);
            img_Toggle.setImageResource(R.drawable.menu_icon);
            img_Right.setImageResource(R.drawable.refresh);

        }else if (Tag.equals(Constants.MYJOB_FRAGMENT)
                || Tag.equals(Constants.PAYMENT_FRAGMENT) || Tag.equals(Constants.RATING_FRAGMENT)
                || Tag.equals(Constants.SETTING_FRAGMENT)){
            txtBack.setVisibility(View.GONE);
            txtDone.setVisibility(View.GONE);
            img_Right.setVisibility(View.GONE);
            img_Toggle.setVisibility(View.VISIBLE);
            img_Toggle.setImageResource(R.drawable.menu_icon);
        }else if (Tag.equals(Constants.SCHEDULED_LIST_DETAILS_FRAGMENT)){
            img_Toggle.setImageResource(R.drawable.screen_cross);
        }
    }

    @Override
    public void onBackStackChanged() {
//        setToolBar(currentFragmentTag);
    }
    public void setTitletext(String Text){
        titletext.setText(Text);
    }
    public void setLeftToolBarText(String Text){

        img_Toggle.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtBack.setText(Text);
    }
    public void setRightToolBarText(String Text){
        img_Right.setVisibility(View.GONE);
        txtDone.setVisibility(View.VISIBLE);
        txtDone.setText(Text);
    }
    public void setLeftToolBarImage(int resId){
        txtBack.setVisibility(View.GONE);
        img_Toggle.setVisibility(View.VISIBLE);
        img_Toggle.setImageResource(resId);
    }
    public void setRightToolBarImage(int resId){
        txtDone.setVisibility(View.GONE);
        img_Right.setVisibility(View.VISIBLE);
        img_Right.setImageResource(resId);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
//        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
            startLocationUpdates();

    }
    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (locationResponseListener != null){
            locationResponseListener.handleLocationResponse(location);
            locationResponseListener = null ;
        }


    }
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    /**
     * Stores activity data in the Bundle.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
//            savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
//            savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
//            super.onSaveInstanceState(savedInstanceState);
//        }
//    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest).setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }
    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }
    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(HomeScreenNew.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }
    public void getLocation(LocationResponseListener locationResponseListener){
        this. locationResponseListener = locationResponseListener ;
        checkLocationSettings();
    }
    public  void popInclusiveFragment(String TAG){
        fragmentManager.popBackStack(TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
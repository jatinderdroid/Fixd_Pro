package com.fixtconsumer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.HasPowerSourceBean;
import com.fixtconsumer.fragments.AlreadyPurchasedPlanFragment;
import com.fixtconsumer.fragments.ChangePasswordFragment;
import com.fixtconsumer.fragments.ChatUserFragment;
import com.fixtconsumer.fragments.CompletePurchasePlan;
import com.fixtconsumer.fragments.EditAddressFragment;
import com.fixtconsumer.fragments.FileClaimFragment;
import com.fixtconsumer.fragments.HasPowerSourceFragment;
import com.fixtconsumer.fragments.HomeFragment;
import com.fixtconsumer.fragments.HomeServiceFragment;
import com.fixtconsumer.fragments.HowManyLocksFragment;
import com.fixtconsumer.fragments.JobSummaryFragment;
import com.fixtconsumer.fragments.MyAccountFragment;
import com.fixtconsumer.fragments.MyJobsFragment;
import com.fixtconsumer.fragments.PlansFragment;
import com.fixtconsumer.fragments.RewardFragment;
import com.fixtconsumer.fragments.SettingAddressFragment;
import com.fixtconsumer.fragments.SettingsAddCardFragment;
import com.fixtconsumer.fragments.SettingsFragment;
import com.fixtconsumer.fragments.SettingsSavingCardFragment;
import com.fixtconsumer.fragments.TellUsMoreFragment;
import com.fixtconsumer.fragments.WhatTypeOfProjectFragment;
import com.fixtconsumer.fragments.WhatsTheProblemFragment;
import com.fixtconsumer.fragments.WhenDoYouWantCalanderFragment;
import com.fixtconsumer.fragments.WhenDoYouWantCalanderRekey;
import com.fixtconsumer.fragments.WhichApplianceFragment;
import com.fixtconsumer.gcm_components.MessageReceivingService;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.JSONParser;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends BaseActivity implements HomeFragment.OnFragmentInteractionListener, MyJobsFragment.OnFragmentInteractionListener,
        PlansFragment.OnFragmentInteractionListener, ChatUserFragment.OnFragmentInteractionListener,
        RewardFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,
        HomeServiceFragment.OnFragmentInteractionListener, WhatTypeOfProjectFragment.OnFragmentInteractionListener,
        WhichApplianceFragment.OnFragmentInteractionListener, HasPowerSourceFragment.OnFragmentInteractionListener,
        WhatsTheProblemFragment.OnFragmentInteractionListener,TellUsMoreFragment.OnFragmentInteractionListener,
        WhenDoYouWantCalanderFragment.OnFragmentInteractionListener,JobSummaryFragment.OnFragmentInteractionListener,
        CompletePurchasePlan.OnFragmentInteractionListener, HowManyLocksFragment.OnFragmentInteractionListener ,
        WhenDoYouWantCalanderRekey.OnFragmentInteractionListener, SettingAddressFragment.OnFragmentInteractionListener,
        FileClaimFragment.OnFragmentInteractionListener, MyAccountFragment.OnFragmentInteractionListener,
        ChangePasswordFragment.OnFragmentInteractionListener, SettingsAddCardFragment.OnFragmentInteractionListener,
        SettingsSavingCardFragment.OnFragmentInteractionListener,EditAddressFragment.OnFragmentInteractionListener,
        AlreadyPurchasedPlanFragment.OnFragmentInteractionListener {

    SlidingMenu slidingMenu = null ;
    FragmentManager fragmentManager ;
    private ImageView img_Toggle, img_Right;
    private TextView titletext, txtDone, txtBack;
    int CONTACTUS_REQUESTCODE = 1;
    public String currentFragmentTag = "";
    private SharedPreferences _prefs =  null ;
    String token = "";
    public HomeActivity() {
        super();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slidingMenu = getSlidingMenu();
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidiing_menu_width);
        setContentView(R.layout.activity_home);
        _prefs = Utility.getSharedPreferences(this);
        fragmentManager = getSupportFragmentManager();
        setWidgets();
        setListeners();
        HomeFragment fragment = new HomeFragment();
        if (getIntent().getExtras() != null){
            if ( getIntent().hasCategory("fragmentTag")){
                String fragmenttag = getIntent().getStringExtra("fragmentTag");
                if (fragmenttag.equals(Constants.MYJOB_FRAGMENT)){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("openScheduleTab",getIntent().getBooleanExtra("openScheduleTab",false));
                    switchFragment(new MyJobsFragment(), Constants.MYJOB_FRAGMENT, false, bundle);
                }
            }else
                switchFragment(fragment, Constants.HOME_FRAGMENT, false, null);
        }else
        switchFragment(fragment, Constants.HOME_FRAGMENT, false, null);
//        setTitletext("Home");
    }
    private void setWidgets() {
        img_Toggle = (ImageView) findViewById(R.id.img_Toggle);
        img_Right = (ImageView) findViewById(R.id.img_Right);
        titletext = (TextView)findViewById(R.id.titletext);
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);
    }
    private void setListeners(){
        img_Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
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
        img_Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLeftClick();
            }
        });
    }
    private void handleRightClick(){
        if (currentFragmentTag.equals(Constants.TELL_US_MORE_FRAGMEMT))
        ((TellUsMoreFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.WHICH_APPLIANCE_FRAGMENT)){
            ((WhichApplianceFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        }else if (currentFragmentTag.equals(Constants.HAS_POWER_SOURCE_FRAGMENT))
            ((HasPowerSourceFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
         else if (currentFragmentTag.equals(Constants.WHEN_DO_YOU_WANT_FRAGMENT))
            ((WhenDoYouWantCalanderFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.WHEN_DO_YOU_WANT_REKEY_FRAGMENT))
            ((WhenDoYouWantCalanderRekey)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.My_ACCOUNT_FRAGMENT))
            ((MyAccountFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.CHANGE_PASSWORD_FRAGMENT))
            ((ChangePasswordFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.SETTING_SAVING_CARD_FRAGMENT))
            ((SettingsSavingCardFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.SETTING_EDIT_ADDRESS_FRAGMENT))
            ((EditAddressFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
        else if (currentFragmentTag.equals(Constants.SETTING_ADDRESS_FRAGMENT))
            ((SettingAddressFragment)fragmentManager.findFragmentByTag(currentFragmentTag)).submit();
    }
    public void setTitletext(String Text){
        titletext.setText(Text);
        titletext.setBackgroundColor(Color.TRANSPARENT);
    }
    public void setTitleBack(int Text){
        titletext.setText("");
        titletext.setBackgroundResource(Text);
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
    public void hideRight(){
        img_Right.setVisibility(View.INVISIBLE);
        txtDone.setVisibility(View.INVISIBLE);
    }
    public void contactUs(){
        toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivityForResult(i, CONTACTUS_REQUESTCODE);
            }
        }, 500);
    }
    public void referFriend(){
        toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(HomeActivity.this, InviteFriendActivity.class);
                startActivityForResult(i, CONTACTUS_REQUESTCODE);
            }
        }, 500);
    }
    public void popStack(){
        fragmentManager.popBackStack();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void setCurrentFragmentTag(String currentFragmentTag){
        this.currentFragmentTag = currentFragmentTag ;
//        setToolBar(currentFragmentTag);
    }
    public void logOut(){
        toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.getSharedPreferences(HomeActivity.this).edit().clear().commit();
                Intent j = new Intent(HomeActivity.this, LoginSignUpActivity.class);
                startActivity(j);
                finish();
            }
        }, 500);
    }
    public void switchFragment(final Fragment fragment, final String Tag , final boolean addToStack, final Bundle bundle) {
        if (slidingMenu.isMenuShowing())
            toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragment != null) {
                    if (bundle != null)
                        fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    fragmentTransaction.replace(R.id.container_body, fragment, Tag);
                    if (addToStack)
                        fragmentTransaction.addToBackStack(Tag);
                    fragmentTransaction.commit();
                    fragmentManager.executePendingTransactions();
                }
            }
        },500);
    }
    public void switchFragmentAdd1(final Fragment fragment, final String Tag , final boolean addToStack,Bundle bundle) {
        if (slidingMenu.isMenuShowing())
            toggle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    fragmentTransaction.add(R.id.container_body, fragment, Tag);
                    if (addToStack)
                        fragmentTransaction.addToBackStack(Tag);
                    fragmentTransaction.commit();
                    fragmentManager.executePendingTransactions();
                }
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        handleLeftClick();
    }
    private void handleLeftClick(){
        if (currentFragmentTag.equals(Constants.HOME_FRAGMENT) || currentFragmentTag.equals(Constants.MYJOB_FRAGMENT)
                || currentFragmentTag.equals(Constants.PLANS_FRAGMENT)  || currentFragmentTag.equals(Constants.PURCHASED_PLANS_FRAGMENT) || currentFragmentTag.equals(Constants.CHATUSER_FRAGMENT)
                || currentFragmentTag.equals(Constants.SETTING_FRAGMENT) || currentFragmentTag.equals(Constants.REWARD_FRAGMENT )
                ){
            toggle();
        }else {
            popStack();
        }
    }
    public int getbackStackCounts(){
        return  getSupportFragmentManager().getBackStackEntryCount();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onResume() {

        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (Utility.getSharedPreferences(this).getString(Preferences.GCM_TOKEN,"").equals(""))
            startService(new Intent(this, MessageReceivingService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("gcm_token_receiver"));
//        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            if (intent != null){
                token = intent.getStringExtra("token");
                updateToken();
            }
        }
    };

    private void updateToken(){
        new AsyncTask<Void, Void, Void>() {
            JSONObject jsonObject = null;

            @Override
            protected Void doInBackground(Void... params) {
                JSONParser jsonParser = new JSONParser();
                jsonObject = jsonParser.makeHttpRequest(Constants.BASE_URL_SINGLE, "POST", getTokenUpdateParameters(), new IHttpExceptionListener() {
                    @Override
                    public void handleException(String exception) {
                        Log.e("", "exception" + exception);
                    }
                });
                if (jsonObject != null) {
                    try {
                        String STATUS = jsonObject.getString("STATUS");
                        if (STATUS.equals("SUCCESS")) {
                            if (token != null){
                                if (!token.equals("null") && !token.equals("")){
                                    Utility.getSharedPreferences(HomeActivity.this).edit().putString(Preferences.GCM_TOKEN,token).commit();
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return null;
            }
        }.execute();
    }
    private HashMap<String, String> getTokenUpdateParameters() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api","update");
        hashMap.put("object", "customers");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[customers][gcm_token]", token);
        return hashMap;
    }
}

package com.fixtconsumer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fixtconsumer.R;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;
    SharedPreferences _prefs = null;
    Context _context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        _prefs = Utility.getSharedPreferences(_context);
        showSplash();
    }
    private void showSplash(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (_prefs.getString(Preferences.AUTH_TOKEN,"").length() == 0){
                    Intent i = new Intent(Splash.this, LoginSignUpActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }else {
                    Intent i = new Intent(Splash.this, HomeActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}

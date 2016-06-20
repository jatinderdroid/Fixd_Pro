package com.fixtconsumer.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.adapters.ImageViewPagerAdapter;
import com.fixtconsumer.views.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoginSignUpActivity extends AppCompatActivity {
    TextView lblPagerScreenText ;
    Button btnLogin, btnSignUp;
    Typeface fontfamily;
    Timer timer;
    int page = 0;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    ArrayList<Integer> pagerAry = new ArrayList<Integer>();
    ArrayList<String> pagerAryText = new ArrayList<String>();
    ImageViewPagerAdapter pagerAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        setWidgets();
        setListeners();
        setTypeface();
    }
    private void initPagerArray(){
        // adding pager drawables
        pagerAry.clear();
        pagerAryText.clear();

        pagerAry.add(R.drawable.feature_bg_one);
        pagerAry.add(R.drawable.feature_bg_two);
        pagerAry.add(R.drawable.feature_bg_three);
        pagerAry.add(R.drawable.feature_bg_four);

        // adding pager text
        pagerAryText.add(getResources().getString(R.string.features_text1));
        pagerAryText.add(getResources().getString(R.string.features_text2));
        pagerAryText.add(getResources().getString(R.string.features_text3));
        pagerAryText.add(getResources().getString(R.string.features_text4));

        pagerAdapter = new ImageViewPagerAdapter(this,pagerAry);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
        try{
            pageSwitcher(5);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private  void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        btnLogin.setTypeface(fontfamily);
        btnSignUp.setTypeface(fontfamily);


    }
    private void setWidgets(){
        lblPagerScreenText = (TextView)findViewById(R.id.lblPagerScreenText);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        indicator =  (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setRadius(8);
    }
    private void setListeners(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignUpActivity.this,Login.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignUpActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 2500); // delay
        // in
        // milliseconds
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                initPagerArray();
//            }
//        },2000);

    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            LoginSignUpActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    if (page > pagerAry.size() - 1) { // In my case the number of pages are 5
                        page = 0;
                    } else {
                        lblPagerScreenText.setText(pagerAryText.get(page));
                        viewPager.setCurrentItem(page++);

                    }
                }
            });

        }
    }
}

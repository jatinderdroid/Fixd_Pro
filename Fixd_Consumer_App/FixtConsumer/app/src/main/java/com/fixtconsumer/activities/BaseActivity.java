package com.fixtconsumer.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fixtconsumer.R;
import com.fixtconsumer.fragments.FragmentDrawer1;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by sahil on 09-05-2016.
 */
public class BaseActivity extends SlidingFragmentActivity {
    private FragmentDrawer1 drawerFragment;
    public BaseActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_base);
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            drawerFragment = new FragmentDrawer1();
            t.replace(R.id.menu_frame, drawerFragment);
            t.commit();
        } else {
            drawerFragment = (FragmentDrawer1)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindWidth(428);
        //        sm.setShadowWidthRes(R.dimen.shadow_width);
        //        sm.setShadowDrawable(R.drawable.shadow);
        //        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);


    }


}


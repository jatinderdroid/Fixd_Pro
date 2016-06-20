package com.fixtconsumer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.activities.InviteFriendActivity;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

/**
 * Created by sahil on 09-05-2016.
 */
public class FragmentDrawer1 extends Fragment implements View.OnClickListener {

    private static String TAG = FragmentDrawer1.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    LinearLayout home, myjobs, plans,message, myratings, settings, refer_friend, contactus, logout;
    TextView home_title, myjobs_title, plans_title, myratings_title, referfriend_title, settings_title,contactus_title, logout_title,message_title;
    private FragmentDrawerListener drawerListener;
    View containerView;
    Typeface fontfamily;
    SharedPreferences _prefs = null;
    Context _context = null ;
    String  typePlan = "purchase";
    public FragmentDrawer1() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getActivity();
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer1, container, false);

        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");

        home = (LinearLayout) layout.findViewById(R.id.home);
        myjobs = (LinearLayout) layout.findViewById(R.id.myjobs);
        plans = (LinearLayout) layout.findViewById(R.id.plans);
        message = (LinearLayout) layout.findViewById(R.id.message);
        myratings = (LinearLayout) layout.findViewById(R.id.myratings);
        settings = (LinearLayout) layout.findViewById(R.id.settings);
        refer_friend = (LinearLayout) layout.findViewById(R.id.refer_friend);
        contactus = (LinearLayout) layout.findViewById(R.id.contactus);
        logout = (LinearLayout) layout.findViewById(R.id.logout);
        home_title = (TextView) layout.findViewById(R.id.home_title);
        myjobs_title = (TextView) layout.findViewById(R.id.myjobs_title);
        plans_title = (TextView) layout.findViewById(R.id.plans_title);
        myratings_title = (TextView) layout.findViewById(R.id.myratings_title);
        settings_title = (TextView) layout.findViewById(R.id.settings_title);
        referfriend_title = (TextView) layout.findViewById(R.id.referfriend_title);
        contactus_title = (TextView) layout.findViewById(R.id.contactus_title);
        logout_title = (TextView) layout.findViewById(R.id.logout_title);
        message_title = (TextView) layout.findViewById(R.id.message_title);

//        Setting TypeFace
        home_title.setTypeface(fontfamily);
        myjobs_title.setTypeface(fontfamily);
        plans_title.setTypeface(fontfamily);
        myratings_title.setTypeface(fontfamily);
        settings_title.setTypeface(fontfamily);
        message_title.setTypeface(fontfamily);
        referfriend_title.setTypeface(fontfamily);
        contactus_title.setTypeface(fontfamily);
        logout_title.setTypeface(fontfamily);

        home.setOnClickListener(this);
        myjobs.setOnClickListener(this);
        plans.setOnClickListener(this);
        myratings.setOnClickListener(this);
        settings.setOnClickListener(this);
        message.setOnClickListener(this);
        contactus.setOnClickListener(this);
        refer_friend.setOnClickListener(this);
        logout.setOnClickListener(this);

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle b = new Bundle();
        switch (v.getId())
        {
            case R.id.home:
                fragment = new HomeFragment();
                b.putString("title", getString(R.string.welcomedroid));
                ((HomeActivity)getActivity()).switchFragment(fragment, Constants.HOME_FRAGMENT,false,b);
                break;
            case R.id.myjobs:
                fragment = new MyJobsFragment();
                b.putString("title", getString(R.string.nav_item_myjobs));
                ((HomeActivity)getActivity()).switchFragment(fragment, Constants.MYJOB_FRAGMENT,false,b);
                break;
            case R.id.plans:
                if (getTypePlan().equals("purchase"))
                    fragment = new PlansFragment();
                else
                    fragment = new AlreadyPurchasedPlanFragment();
                b.putString("title", getString(R.string.nav_item_plans));
                if (!getTypePlan().equals("purchase"))
                    ((HomeActivity)getActivity()).switchFragment(fragment, Constants.PLANS_FRAGMENT,false,b);
                else
                    ((HomeActivity)getActivity()).switchFragment(fragment, Constants.PURCHASED_PLANS_FRAGMENT,false,b);
                break;
            case R.id.message:
                fragment = new ChatUserFragment();
                b.putString("title",  getString(R.string.nav_item_plans));
                ((HomeActivity)getActivity()).switchFragment(fragment, Constants.CHATUSER_FRAGMENT,false,b);
                break;
            case R.id.myratings:
                fragment = new RewardFragment();
                b.putString("title", getString(R.string.nav_item_revards));
                ((HomeActivity)getActivity()).switchFragment(fragment, Constants.REWARD_FRAGMENT,false,b);
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                b.putString("title", getString(R.string.nav_item_settings));
                ((HomeActivity)getActivity()).switchFragment(fragment, Constants.SETTING_FRAGMENT,false,b);
                break;
            case R.id.refer_friend:
//                Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
//                startActivity(intent);
                ((HomeActivity)getActivity()).referFriend();
                break;
            case R.id.contactus:
                ((HomeActivity)getActivity()).contactUs();
                break;
            case R.id.logout:
                ((HomeActivity)getActivity()).logOut();
                break;
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
    private String getTypePlan(){
        String rekey = _prefs.getString(Preferences.FREE_REY_KEY,"");
        String warrenty_paln = _prefs.getString(Preferences.TOTAL_WARRENTY_PLAN,"");
        if (rekey.equals("1")){
            typePlan = "rekey";
        }else if (rekey.equals("0") && !warrenty_paln.equals("0")){
            typePlan = "claim" ;
        }else {
            typePlan = "purchase";
        }
        return typePlan;
    }
}

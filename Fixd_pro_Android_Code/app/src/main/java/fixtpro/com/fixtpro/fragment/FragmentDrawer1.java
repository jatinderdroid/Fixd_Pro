package fixtpro.com.fixtpro.fragment;

/**
 * Created by sony on 15/02/16.
 */

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

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.utilites.Constants;


public class FragmentDrawer1 extends Fragment implements View.OnClickListener {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    LinearLayout home, myjobs, payments, myratings, settings, contactus, logout;
    TextView home_title, myjobs_title, payments_title, myratings_title, settings_title, contactus_title, logout_title;
    private FragmentDrawerListener drawerListener;
    View containerView;
    Typeface fontfamily;

    public FragmentDrawer1() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer1, container, false);

        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");

        home = (LinearLayout) layout.findViewById(R.id.home);
        myjobs = (LinearLayout) layout.findViewById(R.id.myjobs);
        payments = (LinearLayout) layout.findViewById(R.id.payments);
        myratings = (LinearLayout) layout.findViewById(R.id.myratings);
        settings = (LinearLayout) layout.findViewById(R.id.settings);
        contactus = (LinearLayout) layout.findViewById(R.id.contactus);
        logout = (LinearLayout) layout.findViewById(R.id.logout);
        home_title = (TextView) layout.findViewById(R.id.home_title);
        myjobs_title = (TextView) layout.findViewById(R.id.myjobs_title);
        payments_title = (TextView) layout.findViewById(R.id.payments_title);
        myratings_title = (TextView) layout.findViewById(R.id.myratings_title);
        settings_title = (TextView) layout.findViewById(R.id.settings_title);
        contactus_title = (TextView) layout.findViewById(R.id.contactus_title);
        logout_title = (TextView) layout.findViewById(R.id.logout_title);

//        Setting TypeFace
        home_title.setTypeface(fontfamily);
        myjobs_title.setTypeface(fontfamily);
        payments_title.setTypeface(fontfamily);
        myratings_title.setTypeface(fontfamily);
        settings_title.setTypeface(fontfamily);
        contactus_title.setTypeface(fontfamily);
        logout_title.setTypeface(fontfamily);

        home.setOnClickListener(this);
        myjobs.setOnClickListener(this);
        payments.setOnClickListener(this);
        myratings.setOnClickListener(this);
        settings.setOnClickListener(this);
        contactus.setOnClickListener(this);
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
                ((HomeScreenNew)getActivity()).switchFragment(fragment, Constants.HOME_FRAGMENT,false,b);
                break;
            case R.id.myjobs:
                fragment = new MyJobsFragment();
                b.putString("title", getString(R.string.nav_item_myjobs));
                ((HomeScreenNew)getActivity()).switchFragment(fragment, Constants.MYJOB_FRAGMENT,false,b);
                break;
            case R.id.payments:
                fragment = new PaymentsFragment();
                b.putString("title",  getString(R.string.nav_item_payments));
                ((HomeScreenNew)getActivity()).switchFragment(fragment, Constants.PAYMENT_FRAGMENT,false,b);
                break;
            case R.id.myratings:
                fragment = new RatingFragment();
                b.putString("title", getString(R.string.nav_item_myratings));
                ((HomeScreenNew)getActivity()).switchFragment(fragment, Constants.RATING_FRAGMENT,false,b);
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                b.putString("title", getString(R.string.nav_item_settings));
                ((HomeScreenNew)getActivity()).switchFragment(fragment, Constants.SETTING_FRAGMENT,false,b);
                break;
            case R.id.contactus:
                ((HomeScreenNew)getActivity()).contactUs();
                break;
            case R.id.logout:
                ((HomeScreenNew)getActivity()).logOut();
                break;
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
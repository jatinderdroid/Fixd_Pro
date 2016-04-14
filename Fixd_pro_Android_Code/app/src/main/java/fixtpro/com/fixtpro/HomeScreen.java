package fixtpro.com.fixtpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fixtpro.com.fixtpro.fragment.FragmentDrawer1;
import fixtpro.com.fixtpro.fragment.HomeFragment;
import fixtpro.com.fixtpro.fragment.MyJobsFragment;
import fixtpro.com.fixtpro.fragment.PaymentsFragment;
import fixtpro.com.fixtpro.fragment.RatingFragment;
import fixtpro.com.fixtpro.fragment.SettingsFragment;
import fixtpro.com.fixtpro.utilites.Utilities;

public class HomeScreen extends AppCompatActivity implements FragmentDrawer1.FragmentDrawerListener{
    private Toolbar mToolbar;
    private FragmentDrawer1 drawerFragment;
    int CONTACTUS_REQUESTCODE = 1;
    boolean addToBackStack = false ;
    String Tag = "Home Fragment" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer1)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.welcomedroid);
                addToBackStack = false ;
                Tag =  "Home Fragment" ;
                break;
            case 1:
                fragment = new MyJobsFragment();
                title = getString(R.string.nav_item_myjobs);
                addToBackStack = false ;
                Tag =  "MyJobs Fragment" ;
                break;
            case 2:
                fragment = new PaymentsFragment();
                title = getString(R.string.nav_item_payments);
                addToBackStack = false ;
                Tag =  "Payment Fragment" ;
                break;
            case 3:
                fragment = new RatingFragment();
                title = getString(R.string.nav_item_myratings);
                addToBackStack = false ;
                Tag =  "Rating Fragment" ;
                break;
            case 4:
                fragment = new SettingsFragment();
                title = getString(R.string.nav_item_settings);
                addToBackStack = false ;
                Tag =  "Settings Fragment" ;
                break;
            case 5:
                Intent i = new Intent(this, ContactUsActivity.class);
                startActivityForResult(i, CONTACTUS_REQUESTCODE);
                break;
            case 6:
                Utilities.getSharedPreferences(this).edit().clear().commit();
                Intent j = new Intent(this, Login_Register_Activity.class);
                startActivity(j);
                finish();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            if (addToBackStack){
                fragmentTransaction.addToBackStack(Tag);
                mToolbar.setVisibility(View.GONE);
            }

            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACTUS_REQUESTCODE)
        {
            Fragment fragment = new HomeFragment();
            String title = getString(R.string.welcomedroid);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}

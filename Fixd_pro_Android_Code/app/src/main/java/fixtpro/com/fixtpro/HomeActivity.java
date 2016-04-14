package fixtpro.com.fixtpro;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fixtpro.com.fixtpro.fragment.FragmentDrawer;

public class HomeActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
//        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
//        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
//        drawerFragment.setDrawerListener(this);
//
//        // display the first navigation drawer view on app launch
//        displayView(0, "", "")
    }
    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                customback();
            }
        });
        setSupportActionBar(mToolbar);

    }
}

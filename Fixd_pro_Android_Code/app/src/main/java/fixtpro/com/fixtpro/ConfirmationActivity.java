package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context = ConfirmationActivity.this;
    private String TAG = "ConfirmationActivity";
    TextView done, date, time_interval, contact_name, address;
    ImageView action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
        if(Utilities.getSharedPreferences(this).getString(Preferences.ROLE, null).equals("pro")){
            action.setImageResource(R.drawable.assign_technician);
            done.setVisibility(View.GONE);
        }else if(Utilities.getSharedPreferences(this).getString(Preferences.ROLE, null).equals("technician")){
            action.setImageResource(R.drawable.view_in_calendar);
            done.setVisibility(View.VISIBLE);
        }
    }

    public void setWidgets(){
        done = (TextView) findViewById(R.id.done);
        date = (TextView) findViewById(R.id.date);
        time_interval = (TextView) findViewById(R.id.time_interval);
        contact_name = (TextView) findViewById(R.id.contact_name);
        address = (TextView) findViewById(R.id.address);
        action = (ImageView) findViewById(R.id.action);
    }

    public void setListeners(){
        done.setOnClickListener(this);
        action.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.done:
                break;
            case R.id.action:
                Intent i = new Intent(context,AssignTechnicianActivity.class);
                startActivity(i);
                break;
        }
    }
}

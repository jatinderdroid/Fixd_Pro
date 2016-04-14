package fixtpro.com.fixtpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fixtpro.com.fixtpro.beans.AvailableJobModal;

public class ScheduledJobListClickActivity extends AppCompatActivity implements View.OnClickListener{
    AvailableJobModal model;
    private static GoogleMap mMap;
    TextView contactName, address, date, timeinterval, appliance_types_name_and_service_type;
    ImageView cancel, en_routeimg, cancel_jobimg, appliance_type_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_job_list_click);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
        model = (AvailableJobModal) getIntent().getSerializableExtra("JOB_DETAIL");
        contactName.setText(model.getContact_name());
        address.setText(model.getJob_customer_addresses_address() + " - " + model.getJob_customer_addresses_city() + "," + model.getJob_customer_addresses_state());
        date.setText(model.getRequest_date());
        timeinterval.setText(model.getTimeslot_start()+" - "+model.getTimeslot_end());
        String STR_appliance_types_name_and_service_type = "";
        for(int j = 0; j < model.getJob_appliances_arrlist().size(); j++)
        {
            STR_appliance_types_name_and_service_type = STR_appliance_types_name_and_service_type + model.getJob_appliances_arrlist().get(j).getAppliance_type_name()+" "+model.getService_type()+" ";
        }
        appliance_types_name_and_service_type.setText(STR_appliance_types_name_and_service_type);
        STR_appliance_types_name_and_service_type = "";
        setUpMap();
    }

    public void setWidgets(){
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map)).getMap();
        cancel = (ImageView) findViewById(R.id.cancel);
        en_routeimg = (ImageView) findViewById(R.id.enroute);
        cancel_jobimg = (ImageView) findViewById(R.id.canceljob);
        contactName = (TextView) findViewById(R.id.contactname);
        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        timeinterval = (TextView) findViewById(R.id.timeinterval);
        appliance_types_name_and_service_type = (TextView) findViewById(R.id.appliance_types_name_and_service_type);
        appliance_type_img = (ImageView) findViewById(R.id.appliance_type_img);
    }

    public void setListeners(){
        cancel.setOnClickListener(this);
        en_routeimg.setOnClickListener(this);
        cancel_jobimg.setOnClickListener(this);
    }

    private void setUpMap() {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(model.getLatitude(), model.getLongitude())).title("My Home").snippet("Home Address"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(model.getLatitude(),
                model.getLongitude()), 12.0f));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.enroute:
                break;
            case R.id.canceljob:
                Intent i = new Intent(ScheduledJobListClickActivity.this, DeclineJobActivity.class);
                i.putExtra("JobType","Scheduled");
                i.putExtra("JobId",model.getId());
                startActivity(i);
                finish();
                break;
        }
    }
}

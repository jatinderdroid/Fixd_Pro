package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.paging.listview.PagingListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import fixtpro.com.fixtpro.adapters.AvailableJobsPagingAdaper;
import fixtpro.com.fixtpro.adapters.HorizontalScrollApplianceAdapter;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class AvailableJobListClickActivity extends AppCompatActivity implements View.OnClickListener{
    TextView contactName, address, date, timeinterval;
    ImageView cancel, pickupimg, declineimg, appliance_type_img;
    AvailableJobModal model;
    private static GoogleMap mMap;
    HorizontalScrollView horizontal_Scroll_view ;
    HorizontalScrollApplianceAdapter horizontalScrollApplianceAdapter = null;
    LinearLayout appliance_layout ;
    View divider_scrollview;
    LayoutInflater inflater ;
    ImageLoader imageLoader ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_job_list_click);
        getSupportActionBar().hide();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        setWidgets();
        setListeners();
        model = (AvailableJobModal) getIntent().getSerializableExtra("JOB_DETAIL");
        Log.e("Avail CONTACT NAME PICK", model.getContact_name());

        contactName.setText(model.getContact_name());
        address.setText(model.getJob_customer_addresses_address() + " - " + model.getJob_customer_addresses_city() + "," + model.getJob_customer_addresses_state());
        date.setText(model.getRequest_date());
        timeinterval.setText(model.getTimeslot_start() + " - " + model.getTimeslot_end());

        setUpMap();
        setupAppliancesImages();
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

    public void setWidgets(){
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map)).getMap();
        cancel = (ImageView) findViewById(R.id.cancel);
        pickupimg = (ImageView) findViewById(R.id.pickupimg);
        declineimg = (ImageView) findViewById(R.id.declineimg);
        contactName = (TextView) findViewById(R.id.contactname);
        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        timeinterval = (TextView) findViewById(R.id.timeinterval);

        appliance_type_img = (ImageView) findViewById(R.id.appliance_type_img);
        horizontal_Scroll_view = (HorizontalScrollView)findViewById(R.id.horizontal_Scroll_view);
        appliance_layout = (LinearLayout)findViewById(R.id.appliance_layout);
        divider_scrollview =(View)findViewById(R.id.divider_scrollview);
    }

    public void setListeners(){
        cancel.setOnClickListener(this);
        pickupimg.setOnClickListener(this);
        declineimg.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_available_job_list_click, menu);
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



    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.pickupimg:
                if(Utilities.getSharedPreferences(this).getString(Preferences.ROLE, null).equals("pro"))
                {
                    GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerProPickup, this, "Loading");
                    responseAsync.execute(getRequestParamsPro());
                }else if(Utilities.getSharedPreferences(this).getString(Preferences.ROLE, null).equals("technician")){
                    GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerTechnicianPickup, this, "Loading");
                    responseAsync.execute(getRequestParamsTechnician());
                }
                break;
            case R.id.declineimg:
                Intent i = new Intent(AvailableJobListClickActivity.this, DeclineJobActivity.class);
                i.putExtra("JobType","Available");
                i.putExtra("JobId",model.getId());
                startActivity(i);
                finish();
                break;
        }
    }

    ResponseListener responseListenerTechnicianPickup = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    handler.sendEmptyMessage(0);
                }else {
                    handler.sendEmptyMessage(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    ResponseListener responseListenerProPickup = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    handler.sendEmptyMessage(0);
                }else {
                    handler.sendEmptyMessage(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    Intent j = new Intent(AvailableJobListClickActivity.this, ConfirmationActivity.class);
                    startActivity(j);
                    break;
                }
                case 1:{

                    break;
                }
            }
        }
    };

    private HashMap<String,String> getRequestParamsPro(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","jobs");
        hashMap.put("object","lock");
        hashMap.put("job_id", model.getId());
        hashMap.put("token", Utilities.getSharedPreferences(this).getString(Preferences.AUTH_TOKEN, null));
        return hashMap;
    }

    private HashMap<String,String> getRequestParamsTechnician(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","pickup_job");
        hashMap.put("object","jobs");
        hashMap.put("data[id] ", model.getId());
        hashMap.put("token", Utilities.getSharedPreferences(this).getString(Preferences.AUTH_TOKEN, null));

        return hashMap;
    }
    private void setupAppliancesImages(){
        ArrayList<JobAppliancesModal> arrayList = model.getJob_appliances_arrlist();
        if (model.getJob_appliances_arrlist().size() > 0){
            inflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0 ; i < model.getJob_appliances_arrlist().size() ; i++){
                View child = getLayoutInflater().inflate(R.layout.jobs_image_title_item, null);
                TextView txtTitle = (TextView)child.findViewById(R.id.txtTypeTitle);
                final ImageView imgType = (ImageView)child.findViewById(R.id.imgType);
                txtTitle.setText(model.getJob_appliances_arrlist().get(i).getAppliance_type_name() + " Install");
                horizontal_Scroll_view.addView(child);
                Log.e("", "Image Url" + model.getJob_appliances_arrlist().get(i).getImg_original());
                imageLoader.loadImage(model.getJob_appliances_arrlist().get(i).getImg_original(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        imgType.setImageBitmap(loadedImage);
                    }
                });
            }
        }else {
            appliance_layout.setVisibility(View.GONE);
            divider_scrollview.setVisibility(View.GONE);
        }
    }
}

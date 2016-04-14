package fixtpro.com.fixtpro;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fixtpro.com.fixtpro.adapters.HorizontalScrollApplianceAdapter;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.Utilities;
import fixtpro.com.fixtpro.views.HorizontalListView;
import fixtpro.com.fixtpro.views.RatingBarView;


public class JobCompletedActivity extends AppCompatActivity {

    private Context context = JobCompletedActivity.this;
    private String TAG = "JobCompletedActivity";
    private Toolbar toolbar;
    private ImageView cancel;
    private Typeface fontfamily;
    private TextView txtToolbar,txtName,txtAddress,txtDateTime,txtArrivaltime,txtCompletedTime,txtTechName,txtTotalJobScheduled,txt8,txtTotal,txtServceTicket;
    private de.hdodenhof.circleimageview.CircleImageView circleImage;
    private RatingBarView custom_ratingbar_tech;
    private AvailableJobModal availableJobModal = null ;
    ArrayList<JobAppliancesModal> jobapplianceslist = null ;
    private HorizontalListView mHlvCustomList;
    HorizontalScrollApplianceAdapter horizontalScrollApplianceAdapter = null;
    LinearLayout appliance_layout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_completed);
        setToolBar();
        setWidgets();
        setTypeface();
        setClickListner();
        if (getIntent() != null){
            availableJobModal = (AvailableJobModal)getIntent().getSerializableExtra("CompletedJobObject");
            jobapplianceslist = availableJobModal.getJob_appliances_arrlist();
            horizontalScrollApplianceAdapter = new HorizontalScrollApplianceAdapter(this,jobapplianceslist);
            mHlvCustomList.setAdapter(horizontalScrollApplianceAdapter);
            setValues();
        }
    }
    private void setValues(){
        appliance_layout = (LinearLayout)findViewById(R.id.appliance_layout);
        txtName.setText(availableJobModal.getContact_name());
        txtAddress.setText(availableJobModal.getJob_customer_addresses_address()+" - "+availableJobModal.getJob_customer_addresses_city()+","+availableJobModal.getJob_customer_addresses_state());
        txtDateTime.setText(Utilities.convertDate(availableJobModal.getRequest_date()));
        txtArrivaltime.setText("Arrival Time: "+Utilities.Am_PMFormat(availableJobModal.getTimeslot_start()));
        txtCompletedTime.setText("Completed Time: "+Utilities.Am_PMFormat(availableJobModal.getTimeslot_end()));
        txtTechName.setText("(null)");
        txtTotalJobScheduled.setText("(null)");
        txtTotal.setText("Total................$"+availableJobModal.getCost_details_pro_earned());
    }
    private void setClickListner() {
        txtServceTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ServiceTicketActivity.class);
                i.putExtra("jobId",availableJobModal.getId());
                startActivity(i);
            }
        });
    }
    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");

        txtToolbar.setTypeface(fontfamily);
        txtName.setTypeface(fontfamily);
        txtAddress.setTypeface(fontfamily);
        txtDateTime.setTypeface(fontfamily);
        txtArrivaltime.setTypeface(fontfamily);
        txtCompletedTime.setTypeface(fontfamily);
        txtTechName.setTypeface(fontfamily);
        txtTotalJobScheduled.setTypeface(fontfamily);
        txt8.setTypeface(fontfamily);
        txtTotal.setTypeface(fontfamily);

        txtServceTicket.setTypeface(fontfamily);

    }

    private void setWidgets() {

        txtName = (TextView)findViewById(R.id.txtName);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtDateTime = (TextView)findViewById(R.id.txtDateTime);
        txtArrivaltime = (TextView)findViewById(R.id.txtArrivaltime);
        txtCompletedTime = (TextView)findViewById(R.id.txtCompletedTime);
        txtTechName = (TextView)findViewById(R.id.txtTechName);
        txtTotalJobScheduled = (TextView)findViewById(R.id.txtTotalJobScheduled);
        txt8 = (TextView)findViewById(R.id.txt8);
        txtTotal = (TextView)findViewById(R.id.txtTotal);

        txtServceTicket = (TextView)findViewById(R.id.txtServceTicket);

        custom_ratingbar_tech = (RatingBarView)findViewById(R.id.custom_ratingbar_tech);
        custom_ratingbar_tech.setClickable(false);

        mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvImageList);
    }

    private void setToolBar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cancel = (ImageView)toolbar.findViewById(R.id.cancel);
        txtToolbar = (TextView)toolbar.findViewById(R.id.txtToolbar);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

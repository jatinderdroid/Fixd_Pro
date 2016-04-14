package fixtpro.com.fixtpro;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fixtpro.com.fixtpro.beans.RatingListModal;
import fixtpro.com.fixtpro.utilites.Utilities;
import fixtpro.com.fixtpro.views.RatingBarView;

public class RatingDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView cancel;
    RatingListModal ratingListModal = null ;
    RatingBarView custom_ratingbar,customrating_tech ;
    TextView txtToolbar,txtJobId,txtUserName,txtAddress,txtDateTime,txtArrivalTime,txtCompleteTime,txtTechName,txtDetailss;
    Typeface fontfamily ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        setToolbar();
        setWidgets();
        setTypeFace();
        if (getIntent() != null){
            ratingListModal = (RatingListModal)getIntent().getSerializableExtra("RatingObject");
            setValues();
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cancel = (ImageView)toolbar.findViewById(R.id.cancel);
        txtToolbar = (TextView)toolbar.findViewById(R.id.txtToolbar);
        txtToolbar.setTypeface(fontfamily);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void setWidgets(){
        custom_ratingbar = (RatingBarView)findViewById(R.id.custom_ratingbar);
        custom_ratingbar.setClickable(false);
        customrating_tech = (RatingBarView)findViewById(R.id.customrating_tech);
        customrating_tech.setClickable(false);

        txtJobId = (TextView)findViewById(R.id.txtJobId);
        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtDateTime = (TextView)findViewById(R.id.txtDateTime);
        txtArrivalTime = (TextView)findViewById(R.id.txtArrivalTime);
        txtCompleteTime = (TextView)findViewById(R.id.txtCompleteTime);
        txtTechName = (TextView)findViewById(R.id.txtTechName);
        txtDetailss = (TextView)findViewById(R.id.txtDetailss);
    }
    private void setTypeFace(){
        txtJobId.setTypeface(fontfamily);
        txtUserName.setTypeface(fontfamily);
        txtAddress.setTypeface(fontfamily);
        txtDateTime.setTypeface(fontfamily);
        txtArrivalTime.setTypeface(fontfamily);
        txtCompleteTime.setTypeface(fontfamily);
        txtTechName.setTypeface(fontfamily);
        txtDetailss.setTypeface(fontfamily);
    }
    private void setValues(){
        txtJobId.setText("Job # "+ ratingListModal.getJob_id());
        txtUserName.setText(ratingListModal.getCustomers_first_name() +" " +ratingListModal.getCustomers_last_name());
        txtAddress.setText(ratingListModal.getJobs_job_customers_addresses_address()+" - "+ratingListModal.getJobs_job_customers_addresses_city()+","+ratingListModal.getJobs_job_customers_addresses_state());
        txtDateTime.setText(Utilities.convertDate(ratingListModal.getJobs_request_date()));
        if (!ratingListModal.getJobs_started_at().equals("0000-00-00 00:00:00")){
            txtArrivalTime.setText(ratingListModal.getJobs_started_at());
        }else {
            txtArrivalTime.setText("(null)");
        }
        if (!ratingListModal.getJobs_finished_at().equals("0000-00-00 00:00:00")){
            txtCompleteTime.setText(ratingListModal.getJobs_finished_at());
        }else {
            txtCompleteTime.setText("(null)");
        }


        txtTechName.setText(ratingListModal.getJobs_technilcians_first_name() +" "+ ratingListModal.getJobs_technilcians_last_name());
        txtDetailss.setText(ratingListModal.getComments());

        custom_ratingbar.setStar((int) (Float.parseFloat(ratingListModal.getRatings())), true);
        customrating_tech.setStar((int) (Float.parseFloat(ratingListModal.getJobs_technilcians_avg_rating())), true);
    }
}

package fixtpro.com.fixtpro;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.utilites.Utilities;

public class PaymentDetailsActivity extends AppCompatActivity {
    private Context context = PaymentDetailsActivity.this;
    private String TAG  = "PaymentDetailsActivity";
    private Typeface fontfamily;
    ImageView img_Cancel;
    TextView titletext,txtPayment,txtJobID,txtName,txtAddress,txtDateTime,txtArrivaltxt,txtArrivalTime,
            txtCompletedtxt,txtCompletedTime,txtSummary,txtTripCharges,txtTripChargesDoller,txtRepairType,
    txtRepairCost ,txtSubTotal,txtSubTotaldDoller,txtFixdFee,txtFixdFeeDoller,txtTotalEared,txtTotalEaredDoller;
    AvailableJobModal availableJobModal = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        setToolbar();
        setWidgets();
        setTypeface();
        if (getIntent() != null){
            availableJobModal = (AvailableJobModal)getIntent().getSerializableExtra("PaymentObject");
            setValues();
        }
        
    }
    private  void setValues(){
        txtPayment.setText("$"+String.format("%.2f", Float.parseFloat(availableJobModal.getCost_details_pro_earned())));
        txtJobID.setText("Job # "+availableJobModal.getId());
        txtName.setText(availableJobModal.getContact_name());
        txtAddress.setText(availableJobModal.getJob_customer_addresses_address()+" - "+availableJobModal.getJob_customer_addresses_city()+","+availableJobModal.getJob_customer_addresses_state());
        txtDateTime.setText(Utilities.convertDate(availableJobModal.getRequest_date()));
        txtArrivalTime.setText(Utilities.Am_PMFormat(availableJobModal.getTimeslot_start()));
        txtCompletedTime.setText(Utilities.Am_PMFormat(availableJobModal.getTimeslot_end()));
        txtTripChargesDoller.setText("$"+String.format("%.2f", Float.parseFloat(availableJobModal.getCost_details_tripcharges())));
        if (availableJobModal.getCost_details_repair_type().length() == 0){
            txtRepairType.setVisibility(View.GONE);
            txtRepairCost.setVisibility(View.GONE);
        }
        txtRepairType.setText(availableJobModal.getCost_details_repair_type());
        txtRepairCost.setText("$"+String.format("%.2f", Float.parseFloat(availableJobModal.getCost_details_repair_value())));
        txtSubTotaldDoller.setText("$" + ( String.format("%.2f", Float.parseFloat(availableJobModal.getCost_details_customer_payment()))));
        txtFixdFeeDoller.setText("$" + String.format("%.2f", Float.parseFloat(availableJobModal.getCost_details_fixd_fee())));
        txtFixdFee.setText("Fixd Fee("+ availableJobModal.getCost_details_fixd_fee_percentage()+"%)");
        txtTotalEaredDoller.setText("$"+String.format("%.2f",Float.parseFloat(availableJobModal.getCost_details_pro_earned())));
    }
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img_Cancel = (ImageView)toolbar.findViewById(R.id.img_Cancel);
        img_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titletext = (TextView)toolbar.findViewById(R.id.titletext);
        titletext.setTypeface(fontfamily);

    }
    private void setWidgets(){
        txtPayment = (TextView)findViewById(R.id.txtPayment);
        txtJobID = (TextView)findViewById(R.id.txtJobID);
        txtName= (TextView)findViewById(R.id.txtName);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtDateTime= (TextView)findViewById(R.id.txtDateTime);
        txtArrivaltxt= (TextView)findViewById(R.id.txtArrivaltxt);
        txtArrivalTime= (TextView)findViewById(R.id.txtArrivalTime);
        txtCompletedtxt= (TextView)findViewById(R.id.txtCompletedtxt);
        txtCompletedTime = (TextView)findViewById(R.id.txtCompletedTime);
        txtSummary = (TextView)findViewById(R.id.txtSummary);
        txtTripCharges= (TextView)findViewById(R.id.txtTripCharges);
        txtTripChargesDoller = (TextView)findViewById(R.id.txtTripChargesDoller);
        txtRepairType= (TextView)findViewById(R.id.txtRepairType);
        txtRepairCost= (TextView)findViewById(R.id.txtRepairCost);
        txtSubTotal= (TextView)findViewById(R.id.txtSubTotal);
        txtSubTotaldDoller= (TextView)findViewById(R.id.txtSubTotaldDoller);
        txtFixdFee= (TextView)findViewById(R.id.txtFixdFee);
        txtFixdFeeDoller = (TextView)findViewById(R.id.txtFixdFeeDoller);
        txtTotalEared= (TextView)findViewById(R.id.txtTotalEared);
        txtTotalEaredDoller= (TextView)findViewById(R.id.txtTotalEaredDoller);
    }
    private void setTypeface(){
        txtPayment.setTypeface(fontfamily);
        txtJobID.setTypeface(fontfamily);
        txtName.setTypeface(fontfamily);
        txtAddress.setTypeface(fontfamily);
        txtDateTime.setTypeface(fontfamily);
        txtArrivaltxt.setTypeface(fontfamily);
        txtArrivalTime.setTypeface(fontfamily);
        txtCompletedtxt.setTypeface(fontfamily);
        txtCompletedTime.setTypeface(fontfamily);
        txtSummary.setTypeface(fontfamily);
        txtTripCharges.setTypeface(fontfamily);
        txtTripChargesDoller.setTypeface(fontfamily);
        txtRepairType.setTypeface(fontfamily);
        txtRepairCost.setTypeface(fontfamily);
        txtSubTotal.setTypeface(fontfamily);
        txtSubTotaldDoller.setTypeface(fontfamily);
        txtFixdFee.setTypeface(fontfamily);
        txtFixdFeeDoller .setTypeface(fontfamily);
        txtTotalEared.setTypeface(fontfamily);
        txtTotalEaredDoller.setTypeface(fontfamily);
    }

}

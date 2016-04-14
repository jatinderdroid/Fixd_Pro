package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.adapters.AvailableJobsPagingAdaper;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.beans.JobPartsUsedModal;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class ServiceTicketActivity extends AppCompatActivity {
    private Context context = ServiceTicketActivity.this;
    private String TAG = "ServiceTicketActivity";
    private Toolbar toolbar;
    private ImageView cancel;
    private Typeface fontfamily,italicfontfamily;
    ArrayList<AvailableJobModal> completedjoblist  = new ArrayList<AvailableJobModal>();
    String jobId = "";
    TextView txtToolbar,txttop,txtCustomer,txtCustomerSet,txtJobName,txtJobNameSet,txtType,txtTypeSet,
            txtUnit1,txtUnit1Set,txtcustomerCompaint,txtcustomerCompaintSet,txtGarInstall,txtGarDoller,txtIceInstall,txtTax,
            txtTexDoller,txtTotalDoller,txtContractor,txtContractoeDoller,txtIceDoller;
    String nextComplete = "null";
    String error_message =  "";
    Context _context = this;
    LinearLayout layoutAppliances,layoutPartsUsed ;
    LayoutInflater inflater = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_ticket);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setToolBar();

        setWidgets();

        setTypeface();
        if (getIntent() != null){
            jobId = getIntent().getStringExtra("jobId");
            GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST", responseListenerCompleted, this, "Loading");
            responseAsyncCompleted.execute(getRequestParams(jobId));
        }

    }


    ResponseListener responseListenerCompleted = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","Response"+Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    nextComplete = pagination.getString("next");
                    for(int i = 0; i < results.length(); i++){
                        JSONObject obj = results.getJSONObject(i);
                        AvailableJobModal model = new AvailableJobModal();
                        model.setContact_name(obj.getString("contact_name"));
                        model.setCreated_at(obj.getString("created_at"));
                        model.setCustomer_id(obj.getString("customer_id"));
                        model.setCustomer_notes(obj.getString("customer_notes"));
                        model.setFinished_at(obj.getString("finished_at"));
                        model.setId(obj.getString("id"));
                        model.setJob_id(obj.getString("job_id"));
                        model.setLatitude(obj.getDouble("latitude"));
                        model.setLocked_by(obj.getString("locked_by"));
                        model.setLocked_on(obj.getString("locked_on"));
                        model.setLongitude(obj.getDouble("longitude"));
                        model.setPhone(obj.getString("phone"));
                        model.setPro_id(obj.getString("pro_id"));
                        model.setRequest_date(obj.getString("request_date"));
                        model.setService_id(obj.getString("service_id"));
                        model.setService_type(obj.getString("service_type"));
                        model.setStarted_at(obj.getString("started_at"));
                        model.setStatus(obj.getString("status"));
                        model.setTechnician_id(obj.getString("technician_id"));
                        model.setTime_slot_id(obj.getString("time_slot_id"));
                        model.setTitle(obj.getString("title"));
                        model.setTotal_cost(obj.getString("total_cost"));
                        model.setUpdated_at(obj.getString("updated_at"));
                        model.setWarranty(obj.getString("warranty"));
//                        if(Utilities.getSharedPreferences(getContext()).getString(Preferences.ROLE, null).equals("pro")) {
                        JSONArray job_part_userd = obj.getJSONArray("job_parts_used");
                        ArrayList<JobPartsUsedModal> job_parts_used_list = new ArrayList<JobPartsUsedModal>();
                        if (job_part_userd != null){
                            for (int j = 0; j < job_part_userd.length(); j++) {
                                JobPartsUsedModal jobPartsUsedModal = new JobPartsUsedModal();
                                JSONObject jsonObject = job_part_userd.getJSONObject(j);
                                jobPartsUsedModal.setId(jsonObject.getString("id"));
                                jobPartsUsedModal.setPart_num(jsonObject.getString("part_num"));
                                jobPartsUsedModal.setPart_desc(jsonObject.getString("part_desc"));
                                jobPartsUsedModal.setQty(jsonObject.getString("qty"));
                                jobPartsUsedModal.setPart_cost(jsonObject.getString("part_cost"));
                                job_parts_used_list.add(jobPartsUsedModal);
                            }
                        }
                        model.setJob_parts_used_list(job_parts_used_list);
                        JSONArray jobAppliances = obj.getJSONArray("job_appliances");
                        ArrayList<JobAppliancesModal> jobapplianceslist = new ArrayList<JobAppliancesModal>();
                        if(jobAppliances != null){
                            for (int j = 0; j < jobAppliances.length(); j++) {
                                JSONObject jsonObject = jobAppliances.getJSONObject(j);
                                JobAppliancesModal mod = new JobAppliancesModal();
                                mod.setJob_appliances_job_id(jsonObject.getString("job_id"));
                                mod.setJob_appliances_appliance_id(jsonObject.getString("appliance_id"));

                                JSONObject appliance_type_obj = jsonObject.getJSONObject("appliance_types");
                                mod.setAppliance_type_id(appliance_type_obj.getString("id"));
                                mod.setAppliance_type_has_power_source(appliance_type_obj.getString("has_power_source"));
                                mod.setAppliance_type_service_id(appliance_type_obj.getString("service_id"));
                                mod.setAppliance_type_name(appliance_type_obj.getString("name"));
                                mod.setAppliance_type_soft_deleted(appliance_type_obj.getString("_soft_deleted"));
                                if (jsonObject.isNull("image")){
                                    JSONObject image_obj = appliance_type_obj.getJSONObject("image");
                                    if (!image_obj.isNull("original")){
                                        mod.setImg_original(image_obj.getString("original"));
                                        mod.setImg_160x170(image_obj.getString("160x170"));
                                        mod.setImg_150x150(image_obj.getString("150x150"));
                                        mod.setImg_75x75(image_obj.getString("75x75"));
                                        mod.setImg_30x30(image_obj.getString("30x30"));
                                    }
                                }


//                                JSONObject services_obj = jsonObject.getJSONObject("services");
//                                mod.setService_id(services_obj.getString("id"));
//                                mod.setService_name(services_obj.getString("name"));
//                                mod.setService_created_at(services_obj.getString("created_at"));
//                                mod.setService_updated_at(services_obj.getString("updated_at"));
                                jobapplianceslist.add(mod);
                            }
//                            }
                            model.setJob_appliances_arrlist(jobapplianceslist);
                        }
                        JSONObject time_slot_obj = obj.getJSONObject("time_slots");
                        model.setTime_slot_id(time_slot_obj.getString("id"));
                        model.setTimeslot_start(time_slot_obj.getString("start"));
                        model.setTimeslot_end(time_slot_obj.getString("end"));
                        model.setTimeslot_soft_deleted(time_slot_obj.getString("_soft_deleted"));

                        if (!obj.isNull("job_repair")){
                            JSONObject job_repair_obj = obj.getJSONObject("job_repair");
                            if (!job_repair_obj.isNull("repair_types")){
                                JSONObject job_repair_repair_type_obj  = job_repair_obj.getJSONObject("repair_types");
                                model.setJob_repair_job_types_cost(job_repair_repair_type_obj.getString("cost"));
                                model.setJob_repair_job_types_name(job_repair_repair_type_obj.getString("name"));
                            }

                        }

                        JSONObject cost_details_obj = obj.getJSONObject("cost_details");
                        if (!cost_details_obj.isNull("repair")){
                            JSONObject cost_details_repair  = cost_details_obj.getJSONObject("repair");
                            Iterator<?> keys = cost_details_repair.keys();
                            if (keys.hasNext()){
                                String key = (String)keys.next();
                                String value = cost_details_repair.getString(key);
                                model.setCost_details_repair_type(key);
                                model.setCost_details_repair_value(value);
                            }
                        }

                        model.setCost_details_tripcharges(cost_details_obj.getString("trip_charges"));
                        model.setCost_details_tax(cost_details_obj.getString("tax"));

                        model.setCost_details_fixd_fee_percentage(cost_details_obj.getString("fixd_fee_percentage"));
                        model.setCost_details_fixd_fee(cost_details_obj.getString("fixd_fee"));
                        model.setCost_details_pro_earned(cost_details_obj.getString("pro_earned"));
                        model.setCost_details_customer_payment(cost_details_obj.getString("customer_payment"));
                        if (!obj.isNull("job_customer_addresses")){
                            JSONObject job_customer_addresses_obj = obj.getJSONObject("job_customer_addresses");
                            model.setJob_customer_addresses_id(job_customer_addresses_obj.getString("id"));
                            model.setJob_customer_addresses_zip(job_customer_addresses_obj.getString("zip"));
                            model.setJob_customer_addresses_city(job_customer_addresses_obj.getString("city"));
                            model.setJob_customer_addresses_state(job_customer_addresses_obj.getString("state"));
                            model.setJob_customer_addresses_address(job_customer_addresses_obj.getString("address"));
                            model.setJob_customer_addresses_address_2(job_customer_addresses_obj.getString("address_2"));
                            model.setJob_customer_addresses_updated_at(job_customer_addresses_obj.getString("updated_at"));
                            model.setJob_customer_addresses_created_at(job_customer_addresses_obj.getString("created_at"));
                            model.setJob_customer_addresses_job_id(job_customer_addresses_obj.getString("job_id"));
                        }
                        completedjoblist.add(model);
                    }

                    handler.sendEmptyMessage(4);
                }else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

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
    private void setWidgets() {
        txttop = (TextView)findViewById(R.id.txttop);
        txtCustomer = (TextView)findViewById(R.id.txtCustomer);
        txtCustomerSet = (TextView)findViewById(R.id.txtCustomerSet);
        txtJobName = (TextView)findViewById(R.id.txtJobName);
        txtJobNameSet = (TextView)findViewById(R.id.txtJobNameSet);
        txtType = (TextView)findViewById(R.id.txtType);
        txtTypeSet = (TextView)findViewById(R.id.txtTypeSet);
        txtUnit1 = (TextView)findViewById(R.id.txtUnit1);
        txtUnit1Set = (TextView)findViewById(R.id.txtUnit1Set);
        txtcustomerCompaint = (TextView)findViewById(R.id.txtcustomerCompaint);
        txtcustomerCompaintSet = (TextView)findViewById(R.id.txtcustomerCompaintSet);

        txtGarInstall = (TextView)findViewById(R.id.txtGarInstall);
        txtGarDoller = (TextView)findViewById(R.id.txtGarDoller);
        txtTax = (TextView)findViewById(R.id.txtTax);
        txtTexDoller = (TextView)findViewById(R.id.txtTexDoller);
        txtTotalDoller = (TextView)findViewById(R.id.txtTotalDoller);
        txtContractor = (TextView)findViewById(R.id.txtContractor);
        txtContractoeDoller = (TextView)findViewById(R.id.txtContractoeDoller);
        layoutAppliances = (LinearLayout)findViewById(R.id.layout4);
        layoutPartsUsed = (LinearLayout)findViewById(R.id.layout7);
    }
    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        italicfontfamily = Typeface.createFromAsset(getAssets(),"HelveticaNeueLTStd-It.otf");

        txtToolbar.setTypeface(fontfamily);
        txttop.setTypeface(fontfamily);
        txtCustomer.setTypeface(fontfamily);
        txtCustomerSet.setTypeface(fontfamily);
        txtJobName.setTypeface(fontfamily);
        txtJobNameSet.setTypeface(fontfamily);
        txtType.setTypeface(fontfamily);
        txtTypeSet.setTypeface(fontfamily);


        txtcustomerCompaint.setTypeface(fontfamily);
        txtcustomerCompaintSet.setTypeface(fontfamily);

        txtGarInstall.setTypeface(fontfamily);
        txtGarDoller.setTypeface(fontfamily);

        txtTax.setTypeface(fontfamily);
        txtTexDoller.setTypeface(fontfamily);
        txtTotalDoller.setTypeface(fontfamily);

        txtContractor.setTypeface(italicfontfamily);
        txtContractoeDoller.setTypeface(italicfontfamily);
    }

    private HashMap<String,String> getRequestParams(String Jobid){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api", "read");
        hashMap.put("object","jobs");
        if (Utilities.getSharedPreferences(this).getString(Preferences.ROLE, null).equals("pro"))
        hashMap.put("select", "^*,job_parts_used.^*,job_images.^*,job_repair.^*,job_repair.repair_types.^*,technicians.^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        else
        hashMap.put("select", "^*,job_parts_used.^*,job_images.^*,job_repair.^*,job_repair.repair_types.^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        hashMap.put("where[id]", Jobid);
        hashMap.put("token", Utilities.getSharedPreferences(this).getString(Preferences.AUTH_TOKEN, null));
        hashMap.put("page", 1+"");
        hashMap.put("per_page", "15");
        return hashMap;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 1:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }

                case 4:{
                    setValues();
                    break;
                }

            }
        }
    };
    private  void setValues(){

        if (completedjoblist.size() > 0){
            for (int i = 0 ; i < completedjoblist.get(0).getJob_appliances_arrlist().size() ; i++ ){
                View view = inflater.inflate(R.layout.service_ticket_unit_item, null);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params1.setMargins(0, 16, 0, 0);
                params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                ((LinearLayout)view).setLayoutParams(params1);

                TextView txtUnit1 = (TextView)view.findViewById(R.id.txtUnit1);
                TextView txtUnit1Set = (TextView)view.findViewById(R.id.txtUnit1Set);
                ViewGroup.LayoutParams params = txtUnit1Set.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;

                txtUnit1Set.setLayoutParams(params);
                txtUnit1.setTypeface(fontfamily);
                txtUnit1Set.setTypeface(fontfamily);
                txtUnit1.setText("Unit " +(i+1) +":");
                txtUnit1Set.setText(completedjoblist.get(0).getJob_appliances_arrlist().get(i).getAppliance_type_name());
                layoutAppliances.addView(view);
            }
            for ( int i = 0 ; i < completedjoblist.get(0).getJob_parts_used_list().size() ; i++){
                View view = inflater.inflate(R.layout.service_ticket_parts_used_item, null);
                TextView txtpart = (TextView)view.findViewById(R.id.txtpart);
                TextView txtpartNum = (TextView)view.findViewById(R.id.txtpartNum);
                TextView txtpartNumD = (TextView)view.findViewById(R.id.txtpartNumD);
                TextView txtpartNumDSet = (TextView)view.findViewById(R.id.txtpartNumDSet);
                txtpart.setTypeface(fontfamily);
                txtpartNum.setTypeface(fontfamily);
                txtpartNumD.setTypeface(fontfamily);
                txtpartNumDSet.setTypeface(fontfamily);
                txtpart.setText("Part "+ i + ":");
                txtpartNum.setText(completedjoblist.get(0).getJob_parts_used_list().get(i).getQty());
                txtpartNumD.setText(completedjoblist.get(0).getJob_parts_used_list().get(i).getPart_desc());
                txtpartNumDSet.setText(completedjoblist.get(0).getJob_parts_used_list().get(i).getPart_num());
                layoutPartsUsed.addView(view);
            }
        }


    }
    private void showAlertDialog(String Title,String Message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                _context);

        // set title
        alertDialogBuilder.setTitle(Title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}

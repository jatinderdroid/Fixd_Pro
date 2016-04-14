package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class MyJobsSearchActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout comp_schedule_layout;
    TextView completedtext, scheduledtext, cancel;
    EditText txtSearch;
    PagingListView schedeledJob_list_view,completedJob_list_view = null ;
    private boolean  isComplete = true;
    ArrayList<AvailableJobModal> completedjoblist = new ArrayList<AvailableJobModal>();
    ArrayList<AvailableJobModal> schedulejoblist = new ArrayList<AvailableJobModal>();
    static  int completed_page  = 1 ;
    static  int scheduled_page = 1 ;
    String nextComplete = "null", nextScheduled = "null";
    String error_message = "",searchTerm = "";
    Context _context = MyJobsSearchActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs_search);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
    }

    public void setWidgets(){
        comp_schedule_layout = (LinearLayout)findViewById(R.id.comp_schedule_layout);
        completedtext = (TextView)findViewById(R.id.completedtext);
        scheduledtext = (TextView)findViewById(R.id.scheduledtext);
        cancel = (TextView)findViewById(R.id.cancel);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        schedeledJob_list_view = (PagingListView)findViewById(R.id.schedeledJob_list_view);
        completedJob_list_view = (PagingListView)findViewById(R.id.completedJob_list_view);
    }

    public void setListeners(){
        completedtext.setOnClickListener(MyJobsSearchActivity.this);
        scheduledtext.setOnClickListener(MyJobsSearchActivity.this);
        cancel.setOnClickListener(MyJobsSearchActivity.this);
        schedeledJob_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyJobsSearchActivity.this, ScheduledJobListClickActivity.class);
                i.putExtra("JOB_DETAIL", schedulejoblist.get(position));
                startActivity(i);
            }
        });
        completedJob_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MyJobsSearchActivity.this,JobCompletedActivity.class);
                i.putExtra("CompletedJobObject",completedjoblist.get(position));
                startActivity(i);
            }
        });
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchTerm = txtSearch.getText().toString().trim() ;

                    if (isComplete){
                        GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST",responseListenerCompleted, MyJobsSearchActivity.this, "Loading");
                        responseAsyncCompleted.execute(getRequestParams("Complete"));
                    }else{
                        GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST",  responseListenerScheduled, MyJobsSearchActivity.this, "Loading");
                        responseAsyncCompleted.execute(getRequestParams("Scheduled"));
                    }

                }
                return false;
            }
        });
    }
    ResponseListener responseListenerScheduled = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {

                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    nextScheduled = pagination.getString("next");
                    if (results.length() == 0 ){
                        showAlertDialog("Fixd_Pro","No Job Found");
                        return;
                    }else{
                        schedulejoblist.clear();
                    }
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
                        JSONArray jobAppliances = obj.getJSONArray("job_appliances");
                        ArrayList<JobAppliancesModal>  jobapplianceslist = new ArrayList<JobAppliancesModal>();
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

//                                JSONObject image_obj = jsonObject.getJSONObject("image");
//                                mod.setImg_original(image_obj.getString("original"));
//                                mod.setImg_160x170(image_obj.getString("160x170"));
//                                mod.setImg_150x150(image_obj.getString("150x150"));
//                                mod.setImg_75x75(image_obj.getString("75x75"));
//                                mod.setImg_30x30(image_obj.getString("30x30"));

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
                        if (!obj.isNull("cost_details")){
                            JSONObject cost_details_obj = obj.getJSONObject("cost_details");
                            model.setCost_details_tripcharges(cost_details_obj.getString("trip_charges"));
                            model.setCost_details_tax(cost_details_obj.getString("tax"));
                            model.setCost_details_tripcharges(cost_details_obj.getString("trip_charges"));
                            model.setCost_details_fixd_fee_percentage(cost_details_obj.getString("fixd_fee_percentage"));
                            model.setCost_details_fixd_fee(cost_details_obj.getString("fixd_fee"));
                            model.setCost_details_pro_earned(cost_details_obj.getString("pro_earned"));
                            model.setCost_details_customer_payment(cost_details_obj.getString("customer_payment"));
                        }

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
                        schedulejoblist.add(model);
                    }

                    handler.sendEmptyMessage(2);
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
    ResponseListener  responseListenerCompleted= new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    nextComplete = pagination.getString("next");
                    if (results.length() == 0 ){
                        showAlertDialog("Fixd_Pro","No Job Found");
                        return;
                    }else{
                        completedjoblist.clear();
                    }
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
                        JSONArray jobAppliances = obj.getJSONArray("job_appliances");
                        ArrayList<JobAppliancesModal>  jobapplianceslist = new ArrayList<JobAppliancesModal>();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; MyJobsSearchActivity.this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_jobs_search, menu);
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
            case R.id.completedtext:
                comp_schedule_layout.setBackgroundResource(R.drawable.search_completed);
                completedJob_list_view.setVisibility(View.VISIBLE);
                schedeledJob_list_view.setVisibility(View.GONE);
                isComplete = true;
                break;
            case R.id.scheduledtext:
                comp_schedule_layout.setBackgroundResource(R.drawable.search_scheduled);
                schedeledJob_list_view.setVisibility(View.VISIBLE);
                completedJob_list_view.setVisibility(View.GONE);
                isComplete = false;
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
    private HashMap<String,String> getRequestParams(String Status){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","jobs");
        hashMap.put("select", "^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        hashMap.put("where[status]", Status);
        hashMap.put("token", Utilities.getSharedPreferences(MyJobsSearchActivity.this).getString(Preferences.AUTH_TOKEN, null));
        if (isComplete)
            hashMap.put("page", completed_page+"");
        else
            hashMap.put("page", scheduled_page+"");
        hashMap.put("per_page", "20");
        hashMap.put("search", searchTerm);
        return hashMap;
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
                        // if MyJobsSearchActivity.this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{

                    break;
                }
                case 1:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
                case 2:{
                    AvailableJobsPagingAdaper adapter = new AvailableJobsPagingAdaper(MyJobsSearchActivity.this,schedulejoblist,getResources());
                    schedeledJob_list_view.setAdapter(adapter);


                    schedeledJob_list_view.onFinishLoading(true, schedulejoblist);
                    if (!nextScheduled.equals("null")) {
                        schedeledJob_list_view.setHasMoreItems(true);
                    }else {
                        schedeledJob_list_view.setHasMoreItems(false);
                    }

                    schedeledJob_list_view.setPagingableListener(new PagingListView.Pagingable() {
                        @Override
                        public void onLoadMoreItems() {
                            if (!nextScheduled.equals("null")) {
                                scheduled_page = Integer.parseInt(nextScheduled);
                                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerScheduled, MyJobsSearchActivity.this, "Loading");
                                responseAsync.execute(getRequestParams("Scheduled"));
                            } else {
                                schedeledJob_list_view.onFinishLoading(false, null);
                            }
                        }
                    });
                    break;
                }
                case 3:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
                case 4:{
                    AvailableJobsPagingAdaper adapter = new AvailableJobsPagingAdaper(MyJobsSearchActivity.this,completedjoblist,getResources());
                    completedJob_list_view.setAdapter(adapter);


                    completedJob_list_view.onFinishLoading(true, completedjoblist);
                    if (!nextComplete.equals("null")) {
                        completedJob_list_view.setHasMoreItems(true);
                    }else {
                        completedJob_list_view.setHasMoreItems(false);
                    }

                    completedJob_list_view.setPagingableListener(new PagingListView.Pagingable() {
                        @Override
                        public void onLoadMoreItems() {
                            if (!nextComplete.equals("null")) {
                                completed_page = Integer.parseInt(nextComplete);
                                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerCompleted, MyJobsSearchActivity.this, "Loading");
                                responseAsync.execute(getRequestParams("Complete"));
                            } else {
                                completedJob_list_view.onFinishLoading(false, null);
                            }
                        }
                    });
                    break;
                }
                case 5:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
            }
        }
    };

}

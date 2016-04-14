package fixtpro.com.fixtpro.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.AvailableJobListClickActivity;
import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.JobCompletedActivity;
import fixtpro.com.fixtpro.MyJobsSearchActivity;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.ResponseListener;
import fixtpro.com.fixtpro.ScheduledJobListClickActivity;
import fixtpro.com.fixtpro.adapters.AvailableJobsPagingAdaper;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Singleton;
import fixtpro.com.fixtpro.utilites.Utilities;


public class MyJobsFragment extends Fragment implements View.OnClickListener{
    TextView completedtext, availabletext, scheduletext;
    PagingListView completed_listview, availableJob_listView, scheduleJob_listView;
    LinearLayout comp_avail_schedule_layout;
    public static int pageAvaileble = 1 ;
    public static int pageSheduled = 1 ;
    public  static int pagecomplted = 1 ;
    ArrayList<AvailableJobModal> availablejoblist = null;
    ArrayList<AvailableJobModal> schedulejoblist  = null;
    ArrayList<AvailableJobModal> completedjoblist  = null;
    String nextAvailable = "null";
    String nextScheduled = "null";
    String nextComplete = "null";
    String error_message =  "";
    String MODE = "Complete" ;
    Context _context ;
    Singleton singleton = null ;
    Fragment fragment = null ;
    String role = "pro";
    SharedPreferences _prefs = null;
    public MyJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        _context = getActivity();
        singleton = Singleton.getInstance();
        this.nextScheduled =  singleton.nextSchduled;
        this.nextAvailable =  singleton.nextAvailable;
        this.nextComplete  =  singleton.nextCompleted;
        this.pageAvaileble = singleton.pageAvaileble;
        this.pageAvaileble = singleton.pageSheduled;
        this.pagecomplted = singleton.compltedpage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_my_jobs, container, false);

        availablejoblist = singleton.getAvailablejoblist();
        schedulejoblist = singleton.getSchedulejoblist();
        completedjoblist = singleton.getCompletedjoblist();
        _prefs = Utilities.getSharedPreferences(_context);
        role = _prefs.getString(Preferences.ROLE, "pro");
        setWidgets(view);

        setListener();
        if (completedjoblist.size() > 0){
            handler.sendEmptyMessage(4);
        }else{
            GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST", responseListenerCompleted, getActivity(), "Loading");
            responseAsyncCompleted.execute(getRequestParams("Complete"));
        }

        return view;
    }

    public void setWidgets(View v){
        completedtext = (TextView) v.findViewById(R.id.completed_text);
        availabletext = (TextView) v.findViewById(R.id.available_text);
        scheduletext = (TextView) v.findViewById(R.id.scheduled_text);
        comp_avail_schedule_layout = (LinearLayout) v.findViewById(R.id.comp_avail_schedule_layout);
        completed_listview = (PagingListView) v.findViewById(R.id.completed_listview);
        availableJob_listView = (PagingListView) v.findViewById(R.id.available_listview);
        scheduleJob_listView = (PagingListView) v.findViewById(R.id.scheduled_listview);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.MYJOB_FRAGMENT);
        setupToolBar();
    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).hideRight();
        ((HomeScreenNew)getActivity()).setTitletext("My Jobs");
        ((HomeScreenNew)getActivity()).setLeftToolBarImage(R.drawable.menu_icon);
    }
    public void setListener(){
        completedtext.setOnClickListener(this);
        availabletext.setOnClickListener(this);
        scheduletext.setOnClickListener(this);
        completed_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(_context,JobCompletedActivity.class);
                i.putExtra("CompletedJobObject",completedjoblist.get(position));
                startActivity(i);
            }
        });
        availableJob_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // send where details is object
                AvailableJobModal job_detail = new AvailableJobModal();
                job_detail = availablejoblist.get(position);
                Intent i = new Intent(getActivity(), AvailableJobListClickActivity.class);
                i.putExtra("JOB_DETAIL", job_detail);
                startActivity(i);
            }
        });

        scheduleJob_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableJobModal job_detail = new AvailableJobModal();
                job_detail = schedulejoblist.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("SCHEDULED_JOB_DETAIL",job_detail);
                fragment = new ScheduledListDetailsFragment();
//                fragment.setArguments(bundle);
                CurrentScheduledJobSingleTon.getInstance().setCurrentJonModal(job_detail);
                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.SCHEDULED_LIST_DETAILS_FRAGMENT, true, null);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_myjobs_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent i = new Intent(getActivity(), MyJobsSearchActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.completed_text:
                comp_avail_schedule_layout.setBackgroundResource(R.drawable.completedjobs);
                completed_listview.setVisibility(View.VISIBLE);
                availableJob_listView.setVisibility(View.GONE);
                scheduleJob_listView.setVisibility(View.GONE);
                MODE = "Complete" ;
                if (completedjoblist.size() == 0) {
                    GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST", responseListenerCompleted, getActivity(), "Loading");
                    responseAsyncCompleted.execute(getRequestParams("Complete"));
                }
                break;
            case R.id.available_text:
                comp_avail_schedule_layout.setBackgroundResource(R.drawable.availablejobs);
                completed_listview.setVisibility(View.GONE);
                availableJob_listView.setVisibility(View.VISIBLE);
                scheduleJob_listView.setVisibility(View.GONE);
                MODE = "Open" ;
                if (availablejoblist.size() == 0){
                    GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerAvailable, getActivity(), "Loading");
                    responseAsync.execute(getRequestParams("Open"));
                }else{
                    handler.sendEmptyMessage(0);
                }


                break;
            case R.id.scheduled_text:
                comp_avail_schedule_layout.setBackgroundResource(R.drawable.scheduledjobs);
                completed_listview.setVisibility(View.GONE);
                availableJob_listView.setVisibility(View.GONE);
                scheduleJob_listView.setVisibility(View.VISIBLE);
                MODE = "Scheduled" ;
                if (schedulejoblist.size() == 0){
                    GetApiResponseAsync responseAsyncScheduled = new GetApiResponseAsync("POST", responseListenerScheduled, getActivity(), "Loading");
                    responseAsyncScheduled.execute(getRequestParams("Scheduled"));
                }else {
                    handler.sendEmptyMessage(2);
                }

                break;
        }
    }


    private HashMap<String,String> getRequestParams(String Status){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","jobs");
        if (!role.equals("pro"))
            hashMap.put("select", "^*,job_appliances.^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        else
            hashMap.put("select", "^*,job_appliances.^*,technicians.^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        if (!Status.equals("Scheduled"))
            hashMap.put("where[status]", Status);
        else
            hashMap.put("where[status@NOT_IN]", "Complete,Open");
        hashMap.put("token", Utilities.getSharedPreferences(getActivity()).getString(Preferences.AUTH_TOKEN, null));
        if (MODE.equals("Complete"))
            hashMap.put("page", pagecomplted+"");
        else  if (MODE.equals("Scheduled"))
            hashMap.put("page", pageSheduled+"");
        else
            hashMap.put("page", pageAvaileble+"");
        hashMap.put("per_page", "20");
        return hashMap;
    }

    ResponseListener responseListenerAvailable = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    nextAvailable = pagination.getString("next");
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
                        ArrayList<JobAppliancesModal> jobapplianceslist = new ArrayList<JobAppliancesModal>();
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
                            if (!jsonObject.isNull("image")){
                                JSONObject image_obj = jsonObject.getJSONObject("image");
                                mod.setImg_original(image_obj.getString("original"));
                                mod.setImg_160x170(image_obj.getString("160x170"));
                                mod.setImg_150x150(image_obj.getString("150x150"));
                                mod.setImg_75x75(image_obj.getString("75x75"));
                                mod.setImg_30x30(image_obj.getString("30x30"));
                            }


//                                JSONObject services_obj = jsonObject.getJSONObject("services");
//                                mod.setService_id(services_obj.getString("id"));
//                                mod.setService_name(services_obj.getString("name"));
//                                mod.setService_created_at(services_obj.getString("created_at"));
//                                mod.setService_updated_at(services_obj.getString("updated_at"));

                            jobapplianceslist.add(mod);
//                            }
                            model.setJob_appliances_arrlist(jobapplianceslist);
                        }
                        JSONObject time_slot_obj = obj.getJSONObject("time_slots");
                        model.setTime_slot_id(time_slot_obj.getString("id"));
                        model.setTimeslot_start(time_slot_obj.getString("start"));
                        model.setTimeslot_end(time_slot_obj.getString("end"));
                        model.setTimeslot_soft_deleted(time_slot_obj.getString("_soft_deleted"));

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
                        availablejoblist.add(model);
                    }
                    handler.sendEmptyMessage(0);
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    AvailableJobsPagingAdaper adapter = new AvailableJobsPagingAdaper(getActivity(),availablejoblist,getResources());
                    availableJob_listView.setAdapter(adapter);


                    availableJob_listView.onFinishLoading(true, availablejoblist);
                    if (!nextAvailable.equals("null")) {
                        availableJob_listView.setHasMoreItems(true);
                    }else {
                        availableJob_listView.setHasMoreItems(false);
                    }

                    availableJob_listView.setPagingableListener(new PagingListView.Pagingable() {
                        @Override
                        public void onLoadMoreItems() {
                            if (!nextAvailable.equals("null")) {
                                pageAvaileble = Integer.parseInt(nextAvailable);

                                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerAvailable, getActivity(), "Loading");
                                responseAsync.execute(getRequestParams("Open"));
                            } else {
                                availableJob_listView.onFinishLoading(false, null);
                            }
                        }
                    });

                    break;
                }
                case 1:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
                case 2:{
                    AvailableJobsPagingAdaper adapter = new AvailableJobsPagingAdaper(getActivity(),schedulejoblist,getResources());
                    scheduleJob_listView.setAdapter(adapter);


                    scheduleJob_listView.onFinishLoading(true, schedulejoblist);
                    if (!nextScheduled.equals("null")) {
                        scheduleJob_listView.setHasMoreItems(true);
                    }else {
                        scheduleJob_listView.setHasMoreItems(false);
                    }

                    scheduleJob_listView.setPagingableListener(new PagingListView.Pagingable() {
                        @Override
                        public void onLoadMoreItems() {
                            if (!nextScheduled.equals("null")) {
                                pageSheduled = Integer.parseInt(nextScheduled);
                                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerScheduled, getActivity(), "Loading");
                                responseAsync.execute(getRequestParams("Scheduled"));
                            } else {
                                scheduleJob_listView.onFinishLoading(false, null);
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
                    AvailableJobsPagingAdaper adapter = new AvailableJobsPagingAdaper(getActivity(),completedjoblist,getResources());
                    completed_listview.setAdapter(adapter);


                    completed_listview.onFinishLoading(true, completedjoblist);
                    if (!nextComplete.equals("null")) {
                        completed_listview.setHasMoreItems(true);
                    }else {
                        completed_listview.setHasMoreItems(false);
                    }

                    completed_listview.setPagingableListener(new PagingListView.Pagingable() {
                        @Override
                        public void onLoadMoreItems() {
                            if (!nextComplete.equals("null")) {
                                pagecomplted = Integer.parseInt(nextComplete);
                                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerCompleted, getActivity(), "Loading");
                                responseAsync.execute(getRequestParams("Complete"));
                            } else {
                                completed_listview.onFinishLoading(false, null);
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
                                mod.setJob_appliances_id(jsonObject.getString("id"));
                                mod.setJob_appliances_job_id(jsonObject.getString("job_id"));
                                mod.setJob_appliances_appliance_id(jsonObject.getString("appliance_id"));
                                if (!jsonObject.isNull("description")){
                                    mod.setJob_appliances_appliance_description(jsonObject.getString("description"));
                                }
                                if (!jsonObject.isNull("service_type")){
                                    mod.setJob_appliances_service_type(jsonObject.getString("service_type"));
                                }
                                if (!jsonObject.isNull("customer_complaint")) {
                                    mod.setJob_appliances_customer_compalint(jsonObject.getString("customer_complaint"));
                                }
                                JSONObject appliance_type_obj = jsonObject.getJSONObject("appliance_types");
                                mod.setAppliance_type_id(appliance_type_obj.getString("id"));
                                mod.setAppliance_type_has_power_source(appliance_type_obj.getString("has_power_source"));
                                mod.setAppliance_type_service_id(appliance_type_obj.getString("service_id"));
                                mod.setAppliance_type_name(appliance_type_obj.getString("name"));
                                mod.setAppliance_type_soft_deleted(appliance_type_obj.getString("_soft_deleted"));
                                if (!appliance_type_obj.isNull("image")){
                                JSONObject image_obj = appliance_type_obj.getJSONObject("image");
                                Log.e("", "-----" + image_obj.toString());
                                    if (!image_obj.isNull("original")) {
                                        mod.setImg_original(image_obj.getString("original"));
                                        mod.setImg_160x170(image_obj.getString("160x170"));
                                        mod.setImg_150x150(image_obj.getString("150x150"));
                                        mod.setImg_75x75(image_obj.getString("75x75"));
                                        mod.setImg_30x30(image_obj.getString("30x30"));
                                    }
                                }
//

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
                        if (!obj.isNull("technicians")){
                            JSONObject technician_object  =  obj.getJSONObject("technicians");
                            model.setTechnician_id(technician_object.getString("id"));
                            model.setTechnician_fname(technician_object.getString("first_name"));
                            model.setTechnician_lname(technician_object.getString("last_name"));
                            model.setTechnician_pickup_jobs(technician_object.getString("pickup_jobs"));
                            model.setTechnician_avg_rating(technician_object.getString("avg_rating"));
                            model.setTechnician_scheduled_job_count(technician_object.getString("scheduled_jobs_count"));
                            model.setTechnician_completed_job_count(technician_object.getString("completed_jobs_count"));
                            if (!technician_object.isNull("profile_image")){
                                JSONObject object_profile_image  = technician_object.getJSONObject("profile_image");
                                if (!object_profile_image.isNull("original"))
                                model.setTechnician_profile_image(object_profile_image.getString("original"));
                            }

                        }
                        JSONObject time_slot_obj = obj.getJSONObject("time_slots");
                        model.setTime_slot_id(time_slot_obj.getString("id"));
                        model.setTimeslot_start(time_slot_obj.getString("start"));
                        model.setTimeslot_end(time_slot_obj.getString("end"));
                        model.setTimeslot_soft_deleted(time_slot_obj.getString("_soft_deleted"));
                        if (!obj.isNull("cost_details")){
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

    ResponseListener responseListenerCompleted = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
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

                        if (!obj.isNull("cost_details")){
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

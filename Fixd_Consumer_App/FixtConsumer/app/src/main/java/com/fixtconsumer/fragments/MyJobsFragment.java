package com.fixtconsumer.fragments;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.CancelSchduleJob;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.ExpandListAdapter;
import com.fixtconsumer.adapters.MyJobAdapter;
import com.fixtconsumer.beans.Child;
import com.fixtconsumer.beans.Group;
import com.fixtconsumer.beans.JobAppliancesModal;
import com.fixtconsumer.beans.JobModal;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.MyJobsSingleton;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;
import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link MyJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyJobsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /*Fragment Used Widgets*/
    TextView img_Completed, img_schedule;
    PagingListView listview_Completed, listview_Schedule;
    LinearLayout layout_tabs;

    String error_message = "";
    CheckAlertDialog checkALert;
    SharedPreferences _prefs = null ;
    Context _context = null ;


    public static int pageSheduled = 1 ;
    public  static int pagecomplted = 1 ;

    ArrayList<JobModal> schedulejoblist  = null;
    ArrayList<JobModal> completedjoblist  = null;

    String nextScheduled = "null";
    String nextComplete = "null";
    MyJobsSingleton singleton = null ;

    MyJobAdapter adapterSchduled = null ;
    MyJobAdapter adapterComplted= null ;
    boolean openScheduleTab = false ;

    public MyJobsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyJobsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyJobsFragment newInstance(String param1, String param2) {
        MyJobsFragment fragment = new MyJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
            openScheduleTab = getArguments().getBoolean("openScheduleTab");
        }
        _context = getActivity();
        _prefs = Utility.getSharedPreferences(_context);
        singleton = MyJobsSingleton.getInstance();
        checkALert = new CheckAlertDialog();
        this.nextScheduled =  singleton.nextSchduled;
        this.nextComplete  =  singleton.nextCompleted;
        this.pageSheduled = singleton.pageSheduled;
        this.pagecomplted = singleton.compltedpage;
        schedulejoblist = singleton.getSchedulejoblist();
        completedjoblist = singleton.getCompletedjoblist();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView = inflater.inflate(R.layout.fragment_my_jobs, container, false);

        setWidgetIds(rooView);

        layout_tabs.setBackgroundResource(R.drawable.schedule);

        setTabSelectListner();
        setListListeners();
        if (openScheduleTab){
            listview_Completed.setVisibility(View.GONE);
            listview_Schedule.setVisibility(View.VISIBLE);
            layout_tabs.setBackgroundResource(R.drawable.completed);
            if (schedulejoblist.size() > 0){
                handler.sendEmptyMessage(0);
            }else {
                GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",schduledJobsResponseListener,schduledJobsExceptionListener,getActivity(),"Loading");
                apiResponseAsync.execute(getSchduledJobParams());
            }

        }else {
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",completedJobsResponseListener,completedJobsExceptionListener,getActivity(),"Loading");
            apiResponseAsync.execute(getCompleteJobParams());
        }
        return rooView;
    }

    private void setWidgetIds(View view) {
        layout_tabs = (LinearLayout) view.findViewById(R.id.layout_tabs);
        img_Completed = (TextView) view.findViewById(R.id.img_Completed);
        img_schedule = (TextView) view.findViewById(R.id.img_schedule);
        listview_Completed = (PagingListView) view.findViewById(R.id.listview_Completed);
        listview_Schedule = (PagingListView) view.findViewById(R.id.listview_Schedule);

    }
    private void setListListeners(){
        listview_Completed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listview_Schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CancelSchduleJob.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
    private void setTabSelectListner() {
        img_Completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview_Completed.setVisibility(View.VISIBLE);
                listview_Schedule.setVisibility(View.GONE);
                layout_tabs.setBackgroundResource(R.drawable.schedule);
                if (completedjoblist.size() > 0){
                    handler.sendEmptyMessage(2);
                }else {
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",completedJobsResponseListener,completedJobsExceptionListener,getActivity(),"Loading");
                    apiResponseAsync.execute(getCompleteJobParams());
                }

            }
        });
        img_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview_Completed.setVisibility(View.GONE);
                listview_Schedule.setVisibility(View.VISIBLE);
                layout_tabs.setBackgroundResource(R.drawable.completed);
                if (schedulejoblist.size() > 0){
                    handler.sendEmptyMessage(0);
                }else {
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",schduledJobsResponseListener,schduledJobsExceptionListener,getActivity(),"Loading");
                    apiResponseAsync.execute(getSchduledJobParams());
                }
            }
        });
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.MYJOB_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).hideRight();
        ((HomeActivity)getActivity()).setTitletext("My Jobs");
        ((HomeActivity)getActivity()).setLeftToolBarImage(R.drawable.ico_menu);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private HashMap<String,String> getCompleteJobParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","jobs");
        hashMap.put("select","^*,technicians.^*,job_appliances.^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("where[status]","Complete");
        hashMap.put("per_page","25");
        hashMap.put("page","1");
        return hashMap;
    }
    IHttpResponseListener completedJobsResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","response"+Response);
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    nextScheduled = pagination.getString("next");
                    for(int i = 0; i < results.length(); i++){
                        JSONObject obj = results.getJSONObject(i);
                        JobModal model = new JobModal();
                        model.setContact_name(obj.getString("contact_name"));
                        model.setCreated_at(obj.getString("created_at"));
                        model.setCustomer_id(obj.getString("customer_id"));
                        model.setCustomer_notes(obj.getString("customer_notes"));
                        model.setFinished_at(obj.getString("finished_at"));
                        model.setId(obj.getString("id"));
                        model.setLocked_by(obj.getString("locked_by"));
                        model.setLocked_on(obj.getString("locked_on"));
                        model.setPhone(obj.getString("phone"));
                        model.setPro_id(obj.getString("pro_id"));
                        model.setRequest_date(obj.getString("request_date"));
                        model.setStarted_at(obj.getString("started_at"));
                        model.setStatus(obj.getString("status"));
                        model.setTitle(obj.getString("title"));
                        model.setTotal_cost(obj.getString("total_cost"));
                        model.setUpdated_at(obj.getString("updated_at"));
                        model.setWarranty(obj.getString("warranty"));
                        model.setIs_claim(obj.getString("is_claim"));
                        model.setCustomer_warranty_plan_id(obj.getString("customer_warranty_plan_id"));
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
                                if (!jsonObject.isNull("appliance_types")) {
                                    JSONObject appliance_type_obj = jsonObject.getJSONObject("appliance_types");
                                    mod.setAppliance_type_id(appliance_type_obj.getString("id"));
                                    mod.setAppliance_type_has_power_source(appliance_type_obj.getString("has_power_source"));
                                    mod.setAppliance_type_service_id(appliance_type_obj.getString("service_id"));
                                    mod.setAppliance_type_name(appliance_type_obj.getString("name"));
                                    mod.setAppliance_type_soft_deleted(appliance_type_obj.getString("_soft_deleted"));
                                    if (!appliance_type_obj.isNull("image")) {
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
                                    JSONObject services_obj = appliance_type_obj.getJSONObject("services");
                                    mod.setService_id(services_obj.getString("id"));
                                    mod.setService_name(services_obj.getString("name"));
                                    mod.setService_created_at(services_obj.getString("created_at"));
                                    mod.setService_updated_at(services_obj.getString("updated_at"));
                                    jobapplianceslist.add(mod);
                                }
                            }
                            model.setJob_appliances_arrlist(jobapplianceslist);
                        }
                        if (!obj.isNull("technicians")){
                            JSONObject technician_object  =  obj.getJSONObject("technicians");
                            model.setTechnician_technicians_id(technician_object.getString("id"));
                            model.setTechnician_user_id(technician_object.getString("user_id"));
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
                            model.setJob_customer_addresses_latitude(job_customer_addresses_obj.getString("latitude"));
                            model.setJob_customer_addresses_longitude(job_customer_addresses_obj.getString("longitude"));
                        }
                        completedjoblist.add(model);
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
    IHttpExceptionListener completedJobsExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception ;
            handler.sendEmptyMessage(1);
        }
    };
    private HashMap<String,String> getSchduledJobParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","jobs");
        hashMap.put("select","^*,pros.company_name,technicians.^*,job_appliances.^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("where[status@NOT_IN]","Complete");
        hashMap.put("per_page","25");
        hashMap.put("page","1");
        return hashMap;
    }

    IHttpResponseListener schduledJobsResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","response"+Response);
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    nextScheduled = pagination.getString("next");
                    for(int i = 0; i < results.length(); i++){
                        JSONObject obj = results.getJSONObject(i);
                        JobModal model = new JobModal();
                        model.setContact_name(obj.getString("contact_name"));
                        model.setCreated_at(obj.getString("created_at"));
                        model.setCustomer_id(obj.getString("customer_id"));
                        model.setCustomer_notes(obj.getString("customer_notes"));
                        model.setFinished_at(obj.getString("finished_at"));
                        model.setId(obj.getString("id"));
                        model.setLocked_by(obj.getString("locked_by"));
                        model.setLocked_on(obj.getString("locked_on"));
                        model.setPhone(obj.getString("phone"));
                        model.setPro_id(obj.getString("pro_id"));
                        model.setRequest_date(obj.getString("request_date"));
                        model.setStarted_at(obj.getString("started_at"));
                        model.setStatus(obj.getString("status"));
                        model.setTitle(obj.getString("title"));
                        model.setTotal_cost(obj.getString("total_cost"));
                        model.setUpdated_at(obj.getString("updated_at"));
                        model.setWarranty(obj.getString("warranty"));
                        model.setIs_claim(obj.getString("is_claim"));
                        model.setCustomer_warranty_plan_id(obj.getString("customer_warranty_plan_id"));
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
                                if (!jsonObject.isNull("appliance_types")) {
                                    JSONObject appliance_type_obj = jsonObject.getJSONObject("appliance_types");
                                    mod.setAppliance_type_id(appliance_type_obj.getString("id"));
                                    mod.setAppliance_type_has_power_source(appliance_type_obj.getString("has_power_source"));
                                    mod.setAppliance_type_service_id(appliance_type_obj.getString("service_id"));
                                    mod.setAppliance_type_name(appliance_type_obj.getString("name"));
                                    mod.setAppliance_type_soft_deleted(appliance_type_obj.getString("_soft_deleted"));
                                    if (!appliance_type_obj.isNull("image")) {
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
                                    JSONObject services_obj = appliance_type_obj.getJSONObject("services");
                                    mod.setService_id(services_obj.getString("id"));
                                    mod.setService_name(services_obj.getString("name"));
                                    mod.setService_created_at(services_obj.getString("created_at"));
                                    mod.setService_updated_at(services_obj.getString("updated_at"));
                                    jobapplianceslist.add(mod);
                                }
                            }
                            model.setJob_appliances_arrlist(jobapplianceslist);
                        }
                        if (!obj.isNull("technicians")){
                            JSONObject technician_object  =  obj.getJSONObject("technicians");
                            model.setTechnician_technicians_id(technician_object.getString("id"));
                            model.setTechnician_user_id(technician_object.getString("user_id"));
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
                            model.setJob_customer_addresses_latitude(job_customer_addresses_obj.getString("latitude"));
                            model.setJob_customer_addresses_longitude(job_customer_addresses_obj.getString("longitude"));
                        }
                        schedulejoblist.add(model);
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

    IHttpExceptionListener schduledJobsExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("","response"+exception);
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    adapterSchduled = new MyJobAdapter(getActivity(),schedulejoblist,getResources());
                    listview_Schedule.setAdapter(adapterSchduled);
                    listview_Schedule.setHasMoreItems(false);
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
                case 2:{
                    adapterComplted = new MyJobAdapter(getActivity(),completedjoblist,getResources());
                    listview_Completed.setAdapter(adapterComplted);
                    listview_Completed.setHasMoreItems(false);
                    break;
                }
            }
        }
    };
}

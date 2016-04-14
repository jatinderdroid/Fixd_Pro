package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.PaymentDetailsActivity;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.ResponseListener;
import fixtpro.com.fixtpro.adapters.PaymentsJobsPagingAdaper;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Singleton;
import fixtpro.com.fixtpro.utilites.Utilities;

public class PaymentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    PagingListView completed_listview ;
    public static int pagePayment = 1 ;
    public  static int compltedpage = 1 ;
    Context _context ;
    private Typeface fontfamily;

    ArrayList<AvailableJobModal> completedjoblist  = null;
    String next = "null";
    Singleton singleton = null ;
    String error_message =  "";
    String total_earnings = "",jobs_accepted="",hours_worked ="" ;
    public PaymentsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        _context = getActivity();
        singleton = Singleton.getInstance();
        completedjoblist = singleton.getCompletedjoblist();
        next = singleton.nextCompleted ;
        compltedpage = singleton.compltedpage ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View roootView = inflater.inflate(R.layout.fragment_payments, container, false);

        setWidgets(roootView);

        setTypeface();

        setClickListner();

        GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST", responseListenerPayments, getActivity(), "Loading");
        responseAsyncCompleted.execute(getRequestParams());
        // Inflate the layout for this fragment
        return roootView;
    }
    private void setTypeface(){
        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");

    }
    private void setWidgets(View v){

        completed_listview  = (com.paging.listview.PagingListView)v.findViewById(R.id.completed_listview);

    }
    private void setClickListner(){
        completed_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    return;
                }
                Intent i = new Intent(getActivity(), PaymentDetailsActivity.class);
                i.putExtra("PaymentObject",completedjoblist.get(position - 1));
                startActivity(i);
            }
        });
    }
    ResponseListener responseListenerPayments = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response.toString()" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS")){
                    total_earnings  = Response.getJSONObject("RESPONSE").getString("total_earnings");
                    jobs_accepted  = Response.getJSONObject("RESPONSE").getString("jobs_accepted");
                    hours_worked  = Response.getJSONObject("RESPONSE").getString("hours_worked");
                    handler.sendEmptyMessage(1);
                }else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(3);
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
                case 1:{

                    if (completedjoblist.size() > 0){
                        handler.sendEmptyMessage(2);
                    }else{
                        GetApiResponseAsync responseAsyncCompleted = new GetApiResponseAsync("POST", responseListenerCompleted, getActivity(), "Loading");
                        responseAsyncCompleted.execute(getRequestParamsJobs("Complete"));
                    }
                    break;
                }
                case 2:{
                    PaymentsJobsPagingAdaper adapter = new PaymentsJobsPagingAdaper(getActivity(),completedjoblist,getResources());
                    completed_listview.addHeaderView(addHeader());;
                    completed_listview.setAdapter(adapter);


                    completed_listview.onFinishLoading(true, completedjoblist);
                    if (!next.equals("null")) {
                        completed_listview.setHasMoreItems(true);
                    }else {
                        completed_listview.setHasMoreItems(false);
                    }

                    completed_listview.setPagingableListener(new PagingListView.Pagingable() {
                        @Override
                        public void onLoadMoreItems() {
                            if (!next.equals("null")) {
                                compltedpage = Integer.parseInt(next);
                                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerCompleted, getActivity(), "Loading");
                                responseAsync.execute(getRequestParamsJobs("Complete"));
                            } else {
                                completed_listview.onFinishLoading(false, null);
                            }
                        }
                    });

                    break;
                }case 3:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
                default:{

                    break;
                }
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

    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","completed_jobs_stats");
        hashMap.put("object","jobs");
        hashMap.put("token", Utilities.getSharedPreferences(getActivity()).getString(Preferences.AUTH_TOKEN, null));
        return hashMap;
    }
    ResponseListener responseListenerCompleted = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    next = pagination.getString("next");
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

                    handler.sendEmptyMessage(2);
                }else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(3);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private HashMap<String,String> getRequestParamsJobs(String Status){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","jobs");
        hashMap.put("select", "^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        hashMap.put("where[status]", Status);
        hashMap.put("token", Utilities.getSharedPreferences(getActivity()).getString(Preferences.AUTH_TOKEN, null));
        hashMap.put("page", compltedpage+"");
        hashMap.put("per_page", "20");
        return hashMap;
    }
    private ViewGroup addHeader(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.payment_list_header, completed_listview,
                false);
        TextView txtTotalEarningNum,txtTotalEarning,txtnumtotaljobAccepted,txtjobstxt,txtHourtxt,txtnumhours;
        txtTotalEarningNum = (TextView)header.findViewById(R.id.txtTotalEarningNum);
        txtTotalEarning = (TextView)header.findViewById(R.id.txtTotalEarning);
        txtnumtotaljobAccepted = (TextView)header.findViewById(R.id.txtnumtotaljobAccepted);
        txtjobstxt = (TextView)header.findViewById(R.id.txtjobstxt);
        txtHourtxt = (TextView)header.findViewById(R.id.txtHourtxt);
        txtnumhours = (TextView)header.findViewById(R.id.txtnumhours);
        txtTotalEarningNum.setTypeface(fontfamily);
        txtTotalEarning.setTypeface(fontfamily);
        txtnumtotaljobAccepted.setTypeface(fontfamily);
        txtjobstxt.setTypeface(fontfamily);
        txtHourtxt.setTypeface(fontfamily);
        txtnumhours.setTypeface(fontfamily);
        txtnumtotaljobAccepted.setText(jobs_accepted);
        txtnumhours.setText(hours_worked);
        txtTotalEarningNum.setText("$" + total_earnings + ".00");
        return header;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.PAYMENT_FRAGMENT);
        setupToolBar();
    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).hideRight();
        ((HomeScreenNew)getActivity()).setTitletext("Payments");
        ((HomeScreenNew)getActivity()).setLeftToolBarImage(R.drawable.menu_icon);
    }
}

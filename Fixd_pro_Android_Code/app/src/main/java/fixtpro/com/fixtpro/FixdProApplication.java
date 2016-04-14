package fixtpro.com.fixtpro;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.beans.SkillTrade;
import fixtpro.com.fixtpro.singleton.TradeSkillSingleTon;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.JSONParser;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Singleton;
import fixtpro.com.fixtpro.utilites.Utilities;

/**
 * Created by sony on 09-02-2016.
 */
public class FixdProApplication  extends Application{
    ArrayList<SkillTrade> arrayList = new ArrayList<SkillTrade>();
    ArrayList<AvailableJobModal> completedjoblist  = null;
    private  int compltedpage = 1 ;
    String next = "null";
    Singleton singleton = null ;
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = Singleton.getInstance();
        completedjoblist = singleton.getCompletedjoblist();
        next = singleton.nextCompleted ;
//         Getting Trade Skills on App Start
        new AsyncTask<Void,Void,Void>(){
            JSONObject jsonObject =null;
            @Override
            protected Void doInBackground(Void... params) {
                JSONParser jsonParser = new JSONParser();
                jsonObject = jsonParser.makeHttpRequest(Constants.BASE_URL,"POST",getTradeSkillRequestParams());
                if (jsonObject != null){
                    try {
                        String STATUS = jsonObject.getString("STATUS");
                        if (STATUS.equals("SUCCESS")){
                            JSONObject RESPONSE = jsonObject.getJSONObject("RESPONSE");
                            JSONArray results = RESPONSE.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                JSONObject object = results.getJSONObject(i);
                                SkillTrade skillTrade = new SkillTrade();
                                skillTrade.setId(object.getInt("id"));
                                skillTrade.setTitle(object.getString("name"));
                                arrayList.add(skillTrade);
                            }
                            TradeSkillSingleTon.getInstance().setList(arrayList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return null;
            }
        }.execute();
    if(Utilities.getSharedPreferences(getApplicationContext()).getString(Preferences.AUTH_TOKEN, null) != null){
        getJobsCompleted();
    }

    }
    private void  getJobsCompleted(){
        new AsyncTask<Void,Void,Void>(){
            JSONObject jsonObject =null;
            @Override
            protected Void doInBackground(Void... params) {
                JSONParser jsonParser = new JSONParser();
                jsonObject = jsonParser.makeHttpRequest(Constants.BASE_URL,"POST",getRequestParams());
                if (jsonObject != null){
                    responseListenerCompleted.handleResponse(jsonObject);
                }
                return null;
            }
        }.execute();
    }
    private HashMap<String,String> getTradeSkillRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","services");
        hashMap.put("select","^*");
        hashMap.put("per_page", "999");
        hashMap.put("page", "1");
        return hashMap;
    }
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","jobs");
        hashMap.put("select", "^*,job_appliances.appliance_types.services.^*,job_appliances.appliance_types.^*,time_slots.^*,job_customer_addresses.^*");
        hashMap.put("where[status]", "Complete");
        hashMap.put("token", Utilities.getSharedPreferences(getApplicationContext()).getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("page", compltedpage+"");
        hashMap.put("per_page", "15");
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
                    Singleton.getInstance().setNextCompleted(next);
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

                    Singleton.getInstance().setCompletedjoblist(completedjoblist);
                    if (!next.equals("null")) {
                        Singleton.getInstance().setNextSchduled(Integer.parseInt(next)+"");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}

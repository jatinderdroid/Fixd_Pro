package com.fixtconsumer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.ApplianceTypeBeans;
import com.fixtconsumer.beans.SimplePlansBean;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.WarrentyPlanSingleton;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlansActivity extends AppCompatActivity {

    SharedPreferences _prefs = null ;
    Context _context = this;
    String error_message = "";
    CheckAlertDialog checkALert;
    ArrayList <WarrentyPlanBean> warrentyPlanBeanArrayList = WarrentyPlanSingleton.getInstance().arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        _prefs = Utility.getSharedPreferences(_context);
        checkALert = new CheckAlertDialog();
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",planIHttpResponseListener,planIHttpExceptionListener,PlansActivity.this,"Signing In");
        apiResponseAsync.execute(getPlansRequestParams());
    }
    IHttpResponseListener planIHttpResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
                    if (results.length() == 0){
                        error_message = "No Plans found" ;
                        handler.sendEmptyMessage(1);
                    }else {
                        for (int i = 0 ; i < results.length() ; i++){
                            JSONObject jsonObject = results.getJSONObject(i);
                            WarrentyPlanBean warrentyPlanBean = new WarrentyPlanBean();
                            warrentyPlanBean.setId(jsonObject.getString("id"));
                            warrentyPlanBean.setName(jsonObject.getString("name"));
                            warrentyPlanBean.setPrice(jsonObject.getString("price"));
                            warrentyPlanBean.setPrice_inclusive_tax(jsonObject.getString("price_inclusive_tax"));
                            warrentyPlanBean.setTax(jsonObject.getString("tax"));
                            warrentyPlanBean.setType(jsonObject.getString("type"));
                            warrentyPlanBean.set_order(jsonObject.getString("_order"));
                            JSONArray appliance_types = jsonObject.getJSONArray("appliance_types");
                            ArrayList<ApplianceTypeBeans> applianceTypeBeansesArrayList = new ArrayList<ApplianceTypeBeans>();

                            for (int j = 0 ; j < appliance_types.length() ; j++){
                                ApplianceTypeBeans applianceTypeBeans = new ApplianceTypeBeans();
                                JSONObject jsonObjectAppliance = appliance_types.getJSONObject(j);
                                applianceTypeBeans.setName(jsonObjectAppliance.getString("name"));
                                applianceTypeBeans.setWarranty_plans_id("warranty_plans_id");
                                applianceTypeBeansesArrayList.add(applianceTypeBeans);
                            }
                            warrentyPlanBean.setApplianceTypeBeansArrayList(applianceTypeBeansesArrayList);
                            ArrayList<SimplePlansBean> apSimplePlansBeanArrayList = new ArrayList<SimplePlansBean>();
                            JSONArray simple_plans = jsonObject.getJSONArray("simple_plans");
                            for (int j = 0 ; j < simple_plans.length() ; j++){
                                SimplePlansBean simplePlansBean = new SimplePlansBean();
                                JSONObject jsonObjectAppliance = appliance_types.getJSONObject(j);
                                simplePlansBean.setId(jsonObjectAppliance.getString("id"));
                                simplePlansBean.setName(jsonObjectAppliance.getString("name"));
                                simplePlansBean.setPrice(jsonObjectAppliance.getString("price"));
                                simplePlansBean.setType(jsonObjectAppliance.getString("type"));
                                simplePlansBean.set_order(jsonObjectAppliance.getString("_order"));
                                simplePlansBean.setCombined_plan_id(jsonObjectAppliance.getString("combined_plan_id"));
                                simplePlansBean.setSimple_plan_id(jsonObjectAppliance.getString("simple_plan_id"));
                                simplePlansBean.setWarranty_plans_id("warranty_plans_id");
                                apSimplePlansBeanArrayList.add(simplePlansBean);
                            }
                            warrentyPlanBean.setSimplePlansBeanArrayList(apSimplePlansBeanArrayList);
                            warrentyPlanBeanArrayList.add(warrentyPlanBean);
                        }
                        handler.sendEmptyMessage(0);
                    }

                }
                else{
                    JSONObject errors = response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
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
//                    set Adapter
                    Log.e("","Size"+warrentyPlanBeanArrayList.size());
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(PlansActivity.this,_context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
            }
        }
    };
    IHttpExceptionListener planIHttpExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("","exception"+exception);
        }
    };
    private HashMap<String,String> getPlansRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object", "warranty_plans");
        hashMap.put("select", "^*,appliance_types.name,simple_plans.^*,simple_plans.appliance_types.name");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }

}

package com.fixtconsumer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.JobModal;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.MyJobsSingleton;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class ConfirmCancelSchduleJob extends AppCompatActivity {
    int position = 0 ;
    TextView txtCancelJob;
    EditText editText_TellUsWhy ;
    MyJobsSingleton singleton = MyJobsSingleton.getInstance();
    JobModal modal = null ;
    String reason = "";
    CheckAlertDialog checkALert;
    SharedPreferences _prefs = null ;
    String error_message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_cancel_schdule_job);
        checkALert = new CheckAlertDialog();
        _prefs = Utility.getSharedPreferences(this);
        setWidgets();
        settListener();
        if (getIntent() != null){
            position = getIntent().getIntExtra("position",0);
            modal = singleton.getSchedulejoblist().get(position);
//            initLayout();
        }
    }
    private void setWidgets(){
        txtCancelJob = (TextView)findViewById(R.id.txtCancelJob);
        editText_TellUsWhy = (EditText)findViewById(R.id.editText_TellUsWhy);
    }
    private void settListener(){
        txtCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = editText_TellUsWhy.getText().toString().trim();
                if (reason.length() == 0 ){
                    checkALert.showcheckAlert(ConfirmCancelSchduleJob.this, ConfirmCancelSchduleJob.this.getResources().getString(R.string.alert_title), "Tell us why you are cancelling.");
                }else {
//                    cancel Job
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",cancelJobResponseListener,cancelJobExceptionListener,ConfirmCancelSchduleJob.this,"Cancelling.");
                    apiResponseAsync.execute(getCancelJobParams());
                }
            }
        });
    }
    private HashMap<String,String> getCancelJobParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","cancel");
        hashMap.put("object","jobs");
        hashMap.put("data[job_id]",modal.getId());
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("data[reason]",reason);
        return hashMap;
    }
    IHttpResponseListener cancelJobResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("",""+Response);
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {  handler.sendEmptyMessage(0);
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
    IHttpExceptionListener cancelJobExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
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
//                    singleton.getSchedulejoblist().remove(position);
                    Intent intent = new Intent(ConfirmCancelSchduleJob.this,HomeActivity.class);
                    intent.putExtra("fragmentTag",Constants.MYJOB_FRAGMENT);
                    intent.putExtra("openScheduleTab",true);
                    startActivity(intent);
                    break;
                }
                case 1:{
                    break;
                }
            }
        }
    };
}

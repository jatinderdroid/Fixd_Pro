package com.fixtconsumer.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Utility;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
    ImageView cancel;
    TextView txtReset;
    EditText txtEmailPhone;
    String email_phone ="";
    CheckAlertDialog checkALert;
    String error_message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        checkALert = new CheckAlertDialog();
        setWidgets();
        setListeners();
    }
    private void setWidgets(){
        cancel = (ImageView)findViewById(R.id.cancel);
        txtReset = (TextView)findViewById(R.id.txtReset);
        txtEmailPhone = (EditText)findViewById(R.id.txtEmailPhone);
    }
    private void setListeners(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_phone = txtEmailPhone.getText().toString().trim() ;
                if (email_phone.length() == 0)
                checkALert.showcheckAlert(ForgotPassword.this,getResources().getString(R.string.alert_title),"Please enter phone/email");
                else{
//               call     api
                    Utility.hideKeyBoad(ForgotPassword.this, v);
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",resetResponseListener,resetExceptionListener,ForgotPassword.this,"Processing In");
                    apiResponseAsync.execute(getResetPasswordRequestParams());
                }
//
            }
        });

    }
    private HashMap<String,String> getResetPasswordRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","send_token");
        hashMap.put("data[phone_email] ",email_phone);
        hashMap.put("object","password");
        return hashMap;
    }
    IHttpResponseListener resetResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {

        }
    };
    IHttpExceptionListener resetExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("", "response" + exception);
            error_message = exception ;
            handler.sendEmptyMessage(1);
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    finish();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(ForgotPassword.this,getResources().getString(R.string.alert_title),error_message);
                    break;
                }
                case 2:{
                }
            }
        }
    };
}

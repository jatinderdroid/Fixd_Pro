package com.fixtconsumer.activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;

public class InviteByEmailActivity extends AppCompatActivity {
    TextView txtCancel, txtInvite,txtDone;
    EditText txtInput;
    String input = "";
    CheckAlertDialog checkALert;
    String error_message = "";
    SharedPreferences _pref = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_invite_email);
        _pref = Utility.getSharedPreferences(this);
        checkALert = new CheckAlertDialog();
        setWidgets();
        setListeners();
    }
    private void setListeners(){
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyBoad(InviteByEmailActivity.this,txtInput);
                input = txtInput.getText().toString().trim();
                if (input.length() == 0) {
                    checkALert.showcheckAlert(InviteByEmailActivity.this, InviteByEmailActivity.this.getResources().getString(R.string.alert_title), "Please enter mobile number.");
                } else {
//                    call api
                    sendInviteRequest();
                }
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setText("");

            }
        });
    }
    private void setWidgets(){
        txtCancel = (TextView)findViewById(R.id.txtCancel);
        txtInvite = (TextView)findViewById(R.id.txtInvite);
        txtDone = (TextView)findViewById(R.id.txtDone);
        txtInput = (EditText)findViewById(R.id.txtInput);

    }
    private HashMap<String,String> getInviteParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","invite_email");
        hashMap.put("object","referral");
        hashMap.put("data[email]",input);
        hashMap.put("token",_pref.getString(Preferences.AUTH_TOKEN,""));

        return hashMap;
    }
    private void sendInviteRequest(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",getInviteResponse,getInviteException,this,"Inviting.");
        apiResponseAsync.execute(getInviteParams());
    }
    IHttpResponseListener getInviteResponse = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "" + response);
            try {
                Log.e("", "response" + response);
                if (response.getString("STATUS").equals("SUCCESS")) {

                    handler.sendEmptyMessage(0);
                }else{
                    JSONObject errors = response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(1);
                }
            }catch (JSONException e){
            }
        }
    };
    IHttpExceptionListener getInviteException = new IHttpExceptionListener() {
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
                    txtInput.setText("");
                    showSuccessDialog();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(InviteByEmailActivity.this, InviteByEmailActivity.this.getResources().getString(R.string.alert_title), error_message);
                    break;
                }
            }

        }
    };
    private void showSuccessDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_invite_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}

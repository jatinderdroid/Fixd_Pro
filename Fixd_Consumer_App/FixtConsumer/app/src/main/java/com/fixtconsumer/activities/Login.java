package com.fixtconsumer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.GetApiResponseAsyncMutipart;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.net.MultipartUtility;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class Login extends AppCompatActivity {
    private Context context = Login.this;
    private String TAG = "Login";
    Typeface fontfamily;
    CheckAlertDialog checkALert;

    private ImageView imgClose;
    private EditText txtEmail,txtPassword;
    private Button btnLogin;
    private TextView txtForgetPassword;
    MultipartUtility multipart = null;
    public String Email = "",Password = "",error_message = "";;
    SharedPreferences _prefs = null ;
    Context _context = this ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkALert = new CheckAlertDialog();
        _prefs = Utility.getSharedPreferences(_context);
        /*Set Widgets Ids*/
        setWidgetIDs();
        /*Set Typeface*/
        setTypeface();
        /*SetClick Listner*/
        setClickListner();

    }

    private void setWidgetIDs() {
        imgClose = (ImageView)findViewById(R.id.imgClose);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtForgetPassword = (TextView)findViewById(R.id.txtForgetPassword);

    }
    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        txtEmail.setTypeface(fontfamily);
        txtPassword.setTypeface(fontfamily);
        btnLogin.setTypeface(fontfamily);
        txtForgetPassword.setTypeface(fontfamily);
    }
    private void setClickListner() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = txtEmail.getText().toString();
                Password = txtPassword.getText().toString();
                if (Email.length() == 0){
                    checkALert.showcheckAlert(Login.this, context.getResources().getString(R.string.alert_title), "Please enter email address.");
                }else if (Password.length() == 0){
                    checkALert.showcheckAlert(Login.this,context.getResources().getString(R.string.alert_title), "Please enter password.");
                }else if (Password.length() < 6){
                    checkALert.showcheckAlert(Login.this,context.getResources().getString(R.string.alert_title), "Your password should be 6 or more characters long, Please try again.");
                } else{
                    Utility.hideKeyBoad(_context,v);
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",loginResponseListener,loginExceptionListener,Login.this,"Signing In");
                    apiResponseAsync.execute(getLoginRequestParams());

                }
            }
        });
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
    IHttpResponseListener loginResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("","response"+response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONObject RESPONSE = response.getJSONObject("RESPONSE");
                    SharedPreferences.Editor editor = _prefs.edit();
                    String token  = RESPONSE.getString("token");
                    editor.putString(Preferences.AUTH_TOKEN,token);

                    JSONObject users = RESPONSE.getJSONObject("users");
                    editor.putString(Preferences.ID,users.getString("id"));
                    editor.putString(Preferences.EMAIL,users.getString("email"));
                    editor.putString(Preferences.ROLE,users.getString("role"));
                    editor.putString(Preferences.PHONE,users.getString("phone"));

                    JSONObject customer = users.getJSONObject("customers");
                    editor.putString(Preferences.FIRSTNAME,customer.getString("first_name"));
                    editor.putString(Preferences.LASTNAME,customer.getString("last_name"));
                    editor.putString(Preferences.BRAINTREE_CUSTOMER_ID,customer.getString("braintree_customer_id"));
                    editor.putString(Preferences.FREE_REY_KEY,customer.getString("free_re_key"));

                    JSONObject customer_settings = users.getJSONObject("customer_settings");
                    editor.putString(Preferences.SETTINGS_ID,customer_settings.getString("id"));
                    editor.putString(Preferences.SETTINGS_LOCATION_SERVICE,customer_settings.getString("location_services"));
                    editor.putString(Preferences.SETTINGS_TEXT_MESSAGING,customer_settings.getString("text_messaging"));
                    editor.putString(Preferences.SETTINGS_NOTIFICATION, customer_settings.getString("notifications"));

                    JSONObject quickblox_accounts = users.getJSONObject("quickblox_accounts");
                    editor.putString(Preferences.QB_ACCOUNT_ID,quickblox_accounts.getString("account_id"));
                    editor.putString(Preferences.QB_LOGIN,quickblox_accounts.getString("login"));
                    editor.putString(Preferences.QB_PASSWORD,quickblox_accounts.getString("qb_password"));

                    String warrenty_plan  = users.getString("total_warranty_plans_subscriptions");
                    String has_card  = users.getString("has_card");

                    editor.putString(Preferences.TOTAL_WARRENTY_PLAN,warrenty_plan);
                    editor.putString(Preferences.HAS_CARD,has_card);
                    editor.commit();
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    IHttpExceptionListener loginExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("","response"+exception);
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
                    Intent intent = new Intent(Login.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(Login.this,context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
                case 2:{

                }
            }
        }
    };
    private HashMap<String,String> getLoginRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","login");
        hashMap.put("data[phone_email]",Email);
        hashMap.put("object","users");
        hashMap.put("data[password]",Password);
        hashMap.put("data[role]","customer");
        return hashMap;
    }

}


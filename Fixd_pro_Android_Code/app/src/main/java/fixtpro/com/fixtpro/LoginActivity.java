package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class LoginActivity extends AppCompatActivity {
    TextView txtBack,txtDone;
    EditText txtPhone,txtPassword;
    String Phone,Password;
    Context _context =  this;
    Typeface fontfamily;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setWidgets();
        setListeners();
        setTypeface();
        editor = Utilities.getSharedPreferences(_context).edit();
    }
    private  void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
        txtPhone.setTypeface(fontfamily);
        txtPassword.setTypeface(fontfamily);

    }
    private void setListeners(){
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone = txtPhone.getText().toString().trim();
                Password = txtPassword.getText().toString().trim();

                if (Phone.length() == 0){
                    showAlertDialog("Fixd-Pro","Please enter the phone number.");
                    return;
                }else if (Phone.length() < 10){
                    showAlertDialog("Fixd-Pro","Your phone number seems to invalid, Please try again.");
                    return;
                }
                else if (Password.length() == 0){
                    showAlertDialog("Fixd-Pro","Please enter the password.");
                    return;
                }
                else if (Password.length() < 6) {
//                    Show Dialog
                    showAlertDialog("Fixd-Pro","Your password should be 6 or more characters long, Please try again.");
                    return;
                }else {
                    Utilities.hideKeyBoad(_context, LoginActivity.this.getCurrentFocus());
//                    do it
                    GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",loginResponseListener,LoginActivity.this,"Signing In.");
                    getApiResponseAsync.execute(getLoginRequestParams());
                }
            }
        });
    }
    ResponseListener loginResponseListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            try {
                String Status = Response.getString("STATUS");
                Log.e("LOGIN ACTIVITY RESPONSE", Response + "");


                if (Status.equals("SUCCESS")){
                    String Token = Response.getJSONObject("RESPONSE").getString("token");
                    String role = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("role");
                    String email = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("email");
                    String phone = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("phone");
                    String id = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("id");
                    Log.e("AUTH TOKEN", Token);
                    Log.e("ROLE", role);
                    JSONObject pro_settings;
                    editor.putString(Preferences.ROLE, role);
                    editor.putString(Preferences.AUTH_TOKEN, Token);
                    editor.putString(Preferences.EMAIL, email);
                    editor.putString(Preferences.PHONE, phone);
                    editor.putString(Preferences.ID, id);
                    if(role.equals("pro")){
                        pro_settings = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("pro_settings");
                        editor.putString(Preferences.SETTING_ID, pro_settings.getString("id"));
                        editor.putString(Preferences.LOCATION_SERVICE, pro_settings.getString("location_services"));
                        editor.putString(Preferences.TEXT_MESSAGING, pro_settings.getString("text_messaging"));
                        editor.putString(Preferences.AVAILABLE_JOBS_NOTIFICATION, pro_settings.getString("available_jobs_notification"));
                        editor.putString(Preferences.JOB_WON_NOTIFICATION, pro_settings.getString("job_won_notification"));
                        editor.putString(Preferences.JOB_LOST_NOTIFICATION, pro_settings.getString("job_lost_notification") );
                        editor.putString(Preferences.JOB_RESECHDULED, pro_settings.getString("job_rescheduled"));
                        editor.putString(Preferences.JOB_CANCELLED, pro_settings.getString("job_canceled"));

                        JSONObject profile_image = null ;
                        profile_image = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("technicians").getJSONObject("profile_image");
                        if (!profile_image.isNull("original")){
                            String image_original =  profile_image.getString("original");
                            editor.putString(Preferences.PROFILE_IMAGE, image_original);
                        }

                        JSONObject profile_image_diver_licence = null ;
                        profile_image_diver_licence = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("technicians").getJSONObject("driver_license_image");
                        if (!profile_image_diver_licence.isNull("original")){
                            String image_original =  profile_image_diver_licence.getString("original");
                            editor.putString(Preferences.DRIVER_LICENSE_IMAGE, image_original);
                        }


                        editor.putString(Preferences.LOGIN_JSON_DATA, Response.toString());

                        JSONObject pros =  null ;
                        pros = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("pros");
                        String city  = pros.getString("city");
                        String state = pros.getString("state");
                        String zip = pros.getString("zip");
                        String address = pros.getString("address");
                        String hourly_rate = pros.getString("hourly_rate");
                        String company_name = pros.getString("company_name");
                        String ein_number  = pros.getString("ein_number");
                        String bank_name  = pros.getString("bank_name");
                        String bank_routing_number  = pros.getString("bank_routing_number");
                        String bank_account_number  = pros.getString("bank_account_number");
                        String bank_account_type  = pros.getString("bank_account_type");
                        String insurance = pros.getString("insurance");
                        String insurance_policy = pros.getString("insurance_policy");
                        editor.putString(Preferences.CITY, city);
                        editor.putString(Preferences.STATE, state);
                        editor.putString(Preferences.ZIP, zip);
                        editor.putString(Preferences.ADDRESS, address);
                        editor.putString(Preferences.HOURLY_RATE, hourly_rate);
                        editor.putString(Preferences.COMPANY_NAME, company_name);
                        editor.putString(Preferences.EIN_NUMEBR, ein_number);
                        editor.putString(Preferences.BANK_NAME, bank_name);
                        editor.putString(Preferences.BANK_ROUTING_NUMBER, bank_routing_number);
                        editor.putString(Preferences.BANK_ACCOUNT_NUMBER, bank_account_number);
                        editor.putString(Preferences.BANK_ACCOUNT_TYPE, bank_account_type);
                        editor.putString(Preferences.INSURANCE, insurance);
                        editor.putString(Preferences.INSURANCE_POLICY, insurance_policy);
                    }
                    JSONArray services = null;
                    services = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONArray("services");
                    editor.putString(Preferences.SERVICES_JSON_ARRAY,services.toString());
                    JSONObject technicians = null;
                    technicians = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("technicians");
                    String first_name = technicians.getString("first_name");
                    String last_name = technicians.getString("last_name");
                    String social_security_number = technicians.getString("social_security_number");
                    String years_in_business = technicians.getString("years_in_business");
                    String trade_license_number = technicians.getString("trade_license_number");
                    editor.putString(Preferences.FIRST_NAME, first_name);
                    editor.putString(Preferences.LAST_NAME, last_name);
                    editor.putString(Preferences.SOCIAL_SECURITY_NUMBER, social_security_number);
                    editor.putString(Preferences.YEARS_IN_BUSINESS, years_in_business);
                    editor.putString(Preferences.TRADE_LICENSE_NUMBER, trade_license_number);
                    editor.putBoolean(Preferences.ISLOGIN, true);
                    editor.commit();

                    Log.e("LOGIN ACTIVITY RESPONSE from prefs", Utilities.getSharedPreferences(_context).getString(Preferences.LOGIN_JSON_DATA, "0"));

                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(0);
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
                    showAlertDialog("Fixd-Pro","invalid login credentials.");
                    break;
                }case 1:{
//                    showAlertDialog("Fixd-Pro","Success");
                    Intent i = new Intent(LoginActivity.this, HomeScreenNew.class);
                    startActivity(i);
                    finish();
                    break;
                }
                default:{

                }
            }
        }
    };
    private void setWidgets(){
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);

        txtPhone = (EditText)findViewById(R.id.txtPhone);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
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
    private HashMap<String,String> getLoginRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api", "login");
        hashMap.put("phone",Phone);
        hashMap.put("password", Password);
        return hashMap;
    }
}

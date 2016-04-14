package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.beans.SkillTrade;
import fixtpro.com.fixtpro.singleton.TradeSkillSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;


public class ChangePassword extends AppCompatActivity {
    TextView txtBack, txtDone, lblPhone;
    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    SharedPreferences _prefs = null ;
    Context _context  = this ;
    String Phone  = "";
    String authToken  =  "";
    String current_password = "",new_password = "",confirm_password  = "";
    String message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setWidgets();
        _prefs = Utilities.getSharedPreferences(_context);
        authToken  = _prefs.getString(Preferences.AUTH_TOKEN, null) ;
        Phone  = _prefs.getString(Preferences.PHONE,"");
        lblPhone.setText(Phone);

        setListeners();

    }
    private  void setWidgets(){
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);
        lblPhone = (TextView)findViewById(R.id.lblPhone);

        txtCurrentPassword = (EditText)findViewById(R.id.txtCurrentPassword);
        txtNewPassword = (EditText)findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);


    }
    private void setListeners(){
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_password =  txtCurrentPassword.getText().toString().trim();
                new_password =  txtNewPassword.getText().toString().trim();
                confirm_password =  txtConfirmPassword.getText().toString().trim();
                if (current_password.length() == 0){
                    showAlertDialog("Fixd-Pro", "Please enter the current password.",false);
                    return;
                }else if (current_password.length() < 6){
                    showAlertDialog("Fixd-Pro", "Your current password should be 6 or more characters long, Please try again.",false);
                    return;
                }
                else if (new_password.length() == 0){
                    showAlertDialog("Fixd-Pro", "Please enter the new password.",false);
                    return;
                }
                else if (new_password.length() < 6){
                    showAlertDialog("Fixd-Pro", "Your new password should be 6 or more characters long, Please try again.",false);
                    return;
                }
                else if (confirm_password.length() == 0){
                    showAlertDialog("Fixd-Pro", "Please enter the confirm password.",false);
                    return;
                }
                else if (confirm_password.length() < 6){
                    showAlertDialog("Fixd-Pro", "Your confirm password should be 6 or more characters long, Please try again.",false);
                    return;
                }
                else if (!confirm_password.equals(new_password)){
                    showAlertDialog("Fixd-Pro", "password not matched",false);
                    return;
                }else {
//                    do it
                    Utilities.hideKeyBoad(_context, ChangePassword.this.getCurrentFocus());
                    GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST",changePasswordListener,ChangePassword.this,"Loading");
                    responseAsync.execute(getChangePasswordParametrs());
                }


            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    ResponseListener changePasswordListener  = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject jsonObject) {
            Log.e("","Response"+jsonObject.toString());
            try {
                String STATUS = jsonObject.getString("STATUS");
                if (STATUS.equals("SUCCESS")){
                    message = "Password changed successfully";
                    handler.sendEmptyMessage(0);
                }else {
                    JSONObject errors = jsonObject.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        message = errors.getString(key);
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
                    showAlertDialog("Fixed-Pro",message,true);
                    break;
                }
                case 1:{
                    showAlertDialog("Fixed-Pro",message,false);
                    break;
                }
                default:{
                    showAlertDialog("Fixed-Pro","Error! Try later",false);
                }
            }
        }
    };
    private void showAlertDialog(String Title,String Message, final boolean dofinish){
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
                        if (dofinish){
                            finish();
                        }
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    private HashMap<String,String> getChangePasswordParametrs(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api", "change_password");
        hashMap.put("object", "account");
        hashMap.put("new_password",new_password);
        hashMap.put("password", current_password);
        hashMap.put("token", authToken);
        return hashMap;
    }

}

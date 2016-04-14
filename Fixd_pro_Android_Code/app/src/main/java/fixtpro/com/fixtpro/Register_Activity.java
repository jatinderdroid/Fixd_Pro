package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class Register_Activity extends AppCompatActivity {
    TextView txtBack,txtDone;
    EditText txtPhone,txtPassword,txtConfirmPassword;
    String Phone = "",Password = "",ConfirmPassword= "";
    Context _context =  this;
    Typeface fontfamily;
    HashMap<String,String> finalRequestParams = new HashMap<String,String>();
    String error_message = "";
    SharedPreferences _prefs = null;
    boolean isPro = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        _prefs = Utilities.getSharedPreferences(_context);
        setWidgets();
        setListeners();
        setTypeface();
    }
    private  void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
        txtPhone.setTypeface(fontfamily);
        txtPassword.setTypeface(fontfamily);
        txtConfirmPassword.setTypeface(fontfamily);
        txtPhone.setText(_prefs.getString(Preferences.PHONE,""));
        if (_prefs.getString(Preferences.PHONE,"").length() > 0){
            txtPhone.setEnabled(false);
            isPro = false ;
        }
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
                ConfirmPassword = txtConfirmPassword.getText().toString().trim();
                if (Phone.length() == 0) {
                    showAlertDialog("Fixd-Pro", "Please enter the phone number.");
                    return;
                } else if (Phone.length() < 8) {
                    showAlertDialog("Fixd-Pro", "Your phone number seems to invalid, Please try again.");
                    return;
                } else if (Password.length() == 0) {
                    showAlertDialog("Fixd-Pro", "Please enter the password.");
                    return;
                } else if (Password.length() < 6) {
//                    Show Dialog
                    showAlertDialog("Fixd-Pro", "Your password should be 6 or more characters long, Please try again.");
                    return;
                } else if (ConfirmPassword.length() == 0) {
//                    show dialog
                    showAlertDialog("Fixd-Pro", "Please re-enter the password to confirm.");
                    return;
                } else if (!Password.equals(ConfirmPassword)) {
//                    show dialog
                    showAlertDialog("Fixd-Pro", "Password did not match,Please try again.");
                    return;
                } else {
//                    do it
                    if (isPro){
                        Utilities.hideKeyBoad(_context, Register_Activity.this.getCurrentFocus());
                        GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST",checkIfPhoneExistsListener,Register_Activity.this,"Loading");
                        responseAsync.execute(getCheckPhoneRequestParams());
                    }else{
                        handler.sendEmptyMessage(0);
                    }

//                    Intent intent = new Intent(_context,UserProfileScreen.class);
//                    startActivity(intent);

                }
            }
        });
    }
    ResponseListener checkIfPhoneExistsListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "Response" + response.toString());
            try {
                String status = response.getString("STATUS");
                if (status.equals("SUCCESS")){
                    JSONArray Data = response.getJSONArray("RESPONSE");
//                    Check if Phone Already Exist
                    if (Data.length() == 0){
                        handler.sendEmptyMessage(0);
                    }else{
                        handler.sendEmptyMessage(1);
                    }
                }else{
//              Check for Errors
                    JSONObject errors = response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(5);
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
                    Intent intent = new Intent(_context,UserProfileScreen.class);
                    if (!isPro){
                        finalRequestParams.put("object","update_by_code");
                        finalRequestParams.put("api","technician");
                        finalRequestParams.put("invite_code",_prefs.getString(Preferences.TECH_INVITE_CODE,""));
                        finalRequestParams.put("password",Password);
                    }else {

                        finalRequestParams.put("api","signup");
                        finalRequestParams.put("with_token","1");
                        finalRequestParams.put("data[phone]",Phone);
                        finalRequestParams.put("data[password]",Password);
                        finalRequestParams.put("object","pros");
                    }

                    intent.putExtra("finalRequestParams",finalRequestParams);
                    startActivity(intent);
                    break;
                }
                case 1:{
                    showAlertDialog("Fixd-Pro","Phone number already exist.");
                    break;
                }
                default:{
                    showAlertDialog("Fixd-pro",error_message);
                    break;
                }
            }
        }
    };
    private void setWidgets(){
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);

        txtPhone = (EditText)findViewById(R.id.txtPhone);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);

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
    private HashMap<String,String> getCheckPhoneRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","users");
        hashMap.put("select","id");
        hashMap.put("where[phone]",Phone);
        return hashMap;
    }
}

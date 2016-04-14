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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsyncBatch;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class Login_Register_Activity extends AppCompatActivity implements View.OnTouchListener {

    TextView txtVarification;
    Button Register,btnSignIn;
    Context _context = this;
    EditText txtbox1,txtbox2,txtbox3,txtbox4,txtbox5,txtbox6;
    Typeface fontfamily;
    String error_message = "";
    String varification_code = "";
    SharedPreferences _prefs = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__register_);
        _prefs = Utilities.getSharedPreferences(this);
        setWidgets();
        setListeners();
        setTypeface();
        txtVarification.setText(Html.fromHtml(this.getResources().getString(R.string.varification_text)));
        if(Utilities.getSharedPreferences(_context).getString(Preferences.AUTH_TOKEN, null) != null)
        {
            Intent i = new Intent(Login_Register_Activity.this, HomeScreenNew.class);
            startActivity(i);
            finish();
        }
    }
    private  void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        Register.setTypeface(fontfamily);
        btnSignIn.setTypeface(fontfamily);
//        txtVarification.setTypeface(fontfamily);
    }
    
    private void setListeners(){
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, Register_Activity.class);
                _prefs.edit().clear().commit();
                //Intent intent = new Intent(_context, Add_TechScreen.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, LoginActivity.class);
                startActivity(intent);
            }
        });

        txtbox1.setOnTouchListener(this);
        txtbox2.setOnTouchListener(this);
        txtbox3.setOnTouchListener(this);
        txtbox4.setOnTouchListener(this);
        txtbox5.setOnTouchListener(this);
        txtbox6.setOnTouchListener(this);


        txtbox6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //do something
                    if (txtbox1.getText().toString().length() == 0 || txtbox2.getText().toString().length() == 0 || txtbox3.getText().toString().length() == 0 || txtbox4.getText().toString().length() == 0 || txtbox5.getText().toString().length() == 0 || txtbox6.getText().toString().length() == 0){
                        showAlertDialog("Fixd-Pro","Please enter varification code");
                    }else{
                        varification_code = txtbox1.getText().toString() + txtbox2.getText().toString() +txtbox3.getText().toString() + txtbox4.getText().toString() +txtbox5.getText().toString() + txtbox6.getText().toString();
                        GetApiResponseAsync responseAsyncOverview= new GetApiResponseAsync("POST", responseListenerAddTechInvite, Login_Register_Activity.this, "Loading");
                        responseAsyncOverview.execute(getRequestParams());
                    }
                }
                return false;
            }
        });


        txtbox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtbox2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtbox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtbox3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtbox3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtbox4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtbox4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtbox5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtbox5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtbox6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtbox6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void setWidgets(){
        txtVarification = (TextView)findViewById(R.id.txtVarification);
        Register = (Button)findViewById(R.id.Register);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        txtbox1 = (EditText)findViewById(R.id.txtbox1);
        txtbox2 = (EditText)findViewById(R.id.txtbox2);
        txtbox3 = (EditText)findViewById(R.id.txtbox3);
        txtbox4 = (EditText)findViewById(R.id.txtbox4);
        txtbox5 = (EditText)findViewById(R.id.txtbox5);
        txtbox6 = (EditText)findViewById(R.id.txtbox6);
    }
    private void clearAddTextBox(){
        txtbox1.setText("");
        txtbox2.setText("");
        txtbox3.setText("");
        txtbox4.setText("");
        txtbox5.setText("");
        txtbox6.setText("");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                clearAddTextBox();
//                Utilities.hideKeyBoad(_context, Login_Register_Activity.this.getCurrentFocus());
////              txtbox1.requestFocus();
//            }
//        });
        clearAddTextBox();
        Utilities.hideKeyBoad(_context, Login_Register_Activity.this.getCurrentFocus());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtbox1.requestFocus();
            }
        }, 500);
        return false;
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

    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","technicians");
        hashMap.put("select", "^*,users.^*");
        hashMap.put("where[invite_code]", varification_code);
        hashMap.put("where[is_used]", "0");

        return hashMap;
    }
    ResponseListener responseListenerAddTechInvite = new ResponseListener(){

        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","Response"+Response);
            try {
                if (Response.getString("STATUS").equals("SUCCESS")){
                    JSONObject reponseObject = Response.getJSONObject("RESPONSE");
                    JSONArray resultArray = reponseObject.getJSONArray("results");
                    if (resultArray.length() > 0){
                        JSONObject techObject = resultArray.getJSONObject(0);
                        String user_id = techObject.getString("user_id");
                        String first_name = techObject.getString("first_name");
                        String last_name = techObject.getString("last_name");

                        if (!techObject.isNull("users")){
                            JSONObject userObject = techObject.getJSONObject("users");
                            String role = userObject.getString("role");
                            String email = userObject.getString("email");
                            String phone = userObject.getString("phone");
                            SharedPreferences.Editor editor = _prefs.edit();
                            editor.putString(Preferences.ID,user_id);
                            editor.putString(Preferences.FIRST_NAME,first_name);
                            editor.putString(Preferences.LAST_NAME,last_name);
                            editor.putString(Preferences.EMAIL,email);
                            editor.putString(Preferences.PHONE,phone);
                            editor.putString(Preferences.ROLE,role);
                            editor.putString(Preferences.TECH_INVITE_CODE,varification_code);
                            editor.commit();
                            handler.sendEmptyMessage(1);

                        }
                    }else{
                        handler.sendEmptyMessage(2);
                    }

                }else  if(Response.getString("STATUS").equals("FAILED")){
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
                case 0:{
                    break;
                }
                case 1:{
                    Intent intent = new Intent(Login_Register_Activity.this,Register_Activity.class);
                    startActivity(intent);
                    break;
                }
                case 2:{
                     showAlertDialog("Fixd-pro","Already used code");
                    break;
                }
                default:{
                    showAlertDialog("Fixd-pro",error_message);
                }
            }
        }
    };
}

package fixtpro.com.fixtpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;
import fixtpro.com.fixtpro.views.WheelView;

public class AddBankAccountActivity extends AppCompatActivity {
    Context _context =  this;
    TextView txtBack,txtDone,lblAddBankAccount,txtAccountType;
    EditText txtBankName,txtRoutingNumber,txtAccountNumber;
    String bank_name,routing_number,account_type = "",account_number;
    Typeface fontfamily ;
    HashMap<String,String> finalRequestParams = null;
    SharedPreferences _prefs = null ;
    private boolean isEditMode = false ;
    String error_message = "";
    String image_profile = null,image_driver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);
        _prefs = Utilities.getSharedPreferences(_context);
        setWidgets();
        if (_prefs.getBoolean(Preferences.ISLOGIN,false)){
            isEditMode = true;
            intLayout();
        }else {
            if (getIntent() != null) {
                finalRequestParams = (HashMap<String, String>) getIntent().getSerializableExtra("finalRequestParams");
                image_profile = getIntent().getStringExtra("image_profile");
                image_driver = getIntent().getStringExtra("image_driver");
            }
        }

        setTypeface();
        setListeners();
    }
    private  void intLayout(){
        txtBankName.setText(_prefs.getString(Preferences.BANK_NAME,""));
        txtRoutingNumber.setText(_prefs.getString(Preferences.BANK_ROUTING_NUMBER,""));
        txtAccountNumber.setText(_prefs.getString(Preferences.BANK_ACCOUNT_NUMBER,""));
        txtAccountType.setText(_prefs.getString(Preferences.BANK_ACCOUNT_TYPE,""));

    }
    /*helveticaNeue LightItalic*/
    private void setWidgets(){
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);
        txtBankName = (EditText)findViewById(R.id.txtBankName);
        txtRoutingNumber = (EditText)findViewById(R.id.txtRoutingNumber);
        txtAccountNumber = (EditText)findViewById(R.id.txtAccountNumber);
        txtAccountType = (TextView)findViewById(R.id.txtAccountType);
        lblAddBankAccount = (TextView)findViewById(R.id.lblAddBankAccount);
    }
    private void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
        txtBankName.setTypeface(fontfamily);
        txtRoutingNumber.setTypeface(fontfamily);
        txtAccountNumber.setTypeface(fontfamily);
        txtAccountType.setTypeface(fontfamily);
        lblAddBankAccount.setTypeface(fontfamily);
    }
    private void setListeners(){
        txtAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountTypeDialog();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_name = txtBankName.getText().toString().trim();
                routing_number = txtRoutingNumber.getText().toString().trim();
                account_number = txtAccountNumber.getText().toString().trim();
                account_type = txtAccountType.getText().toString().trim();
                if (bank_name.length() == 0){
                    showAlertDialog("Fixd-Pro","Please enter the bank name.");
                    return;
                }else if (routing_number.length() == 0){
                    showAlertDialog("Fixd-Pro","Please enter the routing number.");
                    return;
                }else if (account_number.length() == 0){
                    showAlertDialog("Fixd-Pro","Please enter the account number.");
                    return;
                }else if (account_type.length() == 0){
                    showAlertDialog("Fixd-Pro","Please enter the account type.");
                    return;
                }else {
                    if (!isEditMode){
                        Intent intent = new Intent(_context,BackgroundSaftyCheckScreen.class);
                        finalRequestParams.put("data[pros][bank_name]",bank_name);
                        finalRequestParams.put("data[pros][bank_routing_number]",routing_number);
                        finalRequestParams.put("data[pros][bank_account_number]",account_number);
                        finalRequestParams.put("data[pros][bank_account_type]",account_type);
                        intent.putExtra("finalRequestParams", finalRequestParams);
                        intent.putExtra("image_driver",image_driver);
                        intent.putExtra("image_profile",image_profile);
                        intent.putExtra("ispro",true);
                        startActivity(intent);
                    }else {
                        GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", bankAccountUpdateListener, AddBankAccountActivity.this, "Loading");
                        responseAsync.execute(getBankAccountUpdateParameters());
                    }
//                    do it

                }
            }
        });
    }
    ResponseListener bankAccountUpdateListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
         Log.e("", "Response" + Response.toString());
            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    SharedPreferences.Editor editor = _prefs.edit();
                    editor.putString(Preferences.BANK_NAME,bank_name);
                    editor.putString(Preferences.BANK_ROUTING_NUMBER,routing_number);
                    editor.putString(Preferences.BANK_ACCOUNT_NUMBER,account_number);
                    editor.putString(Preferences.BANK_ACCOUNT_TYPE,account_type);
                    editor.commit();
                    finish();
                }else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                        handler.sendEmptyMessage(0);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showAlertDialog("Fixd-Pro",error_message);

        }
    };
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
    private  void showAccountTypeDialog(){
        final String[] TYPES = new String[]{"Saving", "Checking"};
        final Dialog dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wheelviewdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        WheelView wheelView = (WheelView) dialog.findViewById(R.id.wheelView);
        ImageView imgok = (ImageView) dialog.findViewById(R.id.img_ok);
        wheelView.setOffset(1);
        wheelView.setItems(Arrays.asList(TYPES));
        wheelView.setSeletion(2);
        if (account_type.length() == 0) {
            account_type = TYPES[1].toString();
            txtAccountType.setText(account_type);
        }
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                account_type = TYPES[selectedIndex - 1].toString();
                txtAccountType.setText(account_type);
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private HashMap<String,String> getBankAccountUpdateParameters(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","update");
        hashMap.put("object","pros");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("data[pros][bank_account_type]", account_type);
        hashMap.put("data[pros][bank_name]", bank_name);
        hashMap.put("data[pros][bank_account_number]", account_number);
        hashMap.put("data[pros][bank_routing_number]", routing_number);

        return hashMap;
    }
}

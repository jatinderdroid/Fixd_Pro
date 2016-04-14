package fixtpro.com.fixtpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.ExceptionListener;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsyncMutipart;
import fixtpro.com.fixtpro.utilites.MultipartUtility;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class BackgroundSaftyCheckScreen extends AppCompatActivity {
    private Context _context = this;
    private String TAG = "BackgroundSaftyCheckScreen";
    private TextView txtBack, txtDone;
    private Typeface fontfamily, italic_fontfamily;
    EditText text_securityno;
    TextView text_backgroundsafety, text_description, text_thisno, text_enduserDetail, check_text;
    String security_number = "";
    private boolean isChecked = false;
    CheckBox check_id;
    HashMap<String, String> finalRequestParams = null;
    SharedPreferences _prefs = null;
    SharedPreferences.Editor editor;
    String error_message = "";
    String image_profile = null,image_driver = null,years_in_business ="" ;
    private Dialog dialog;
    MultipartUtility multipart = null;
    private boolean isPro = true ;
    private static final int MY_SCAN_REQUEST_CODE = 200 ;
    String card_number = "",month = "",year = "",cvv = "",zip_code = "",first_name = "" ,last_name = "";
    EditText editCardNo,editYear,editMonth,editCw,editZipCode,editFirstName,editLastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_safty_check_screen);


        _prefs = Utilities.getSharedPreferences(_context);
        editor = Utilities.getSharedPreferences(_context).edit();
        if (getIntent() != null) {
            finalRequestParams = (HashMap<String, String>) getIntent().getSerializableExtra("finalRequestParams");
            image_profile = getIntent().getStringExtra("image_profile");
            image_driver = getIntent().getStringExtra("image_driver");
            years_in_business = getIntent().getStringExtra("years_in_business");
            isPro = getIntent().getBooleanExtra("ispro",true);
            Log.e("SIZE", "finalRequestParams" + finalRequestParams.size());
        }
        setWidgetIDs();
        setTypeface();

        setClickListner();
//        if (!_prefs.getString(Preferences.ROLE,"pro").equals("pro")){
//            isPro = false ;
//        }
    }

    private void setClickListner() {
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
        check_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
                isChecked = isCheck;
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                security_number = text_securityno.getText().toString();
                if (security_number.length() == 0) {
                    showAlertDialog("Fixd-Pro", "Please enter the security number.");
                    return;
                } else if (security_number.length() != 9) {
                    showAlertDialog("Fixd-Pro", "Security number seems to be invalid.");
                    return;
                } else if (!isChecked) {
                    showAlertDialog("Fixd-Pro", "please read the terms below and accept those before proceeding.");
                    return;
                } else {
                    if (isPro){
                        finalRequestParams.put("data[technicians][social_security_number]", security_number);
                        finalRequestParams.put("data[technicians][years_in_business]", "10");
                    }

                    else{
                        finalRequestParams.put("social_security_number", security_number);
                        finalRequestParams.put("years_in_business", "10");
                    }
                    if (isPro)
                        showCustomDialog();
                    else
                        sendRequest();



//                    GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST", responseListener, BackgroundSaftyCheckScreen.this, "Registering");
//                    getApiResponseAsync.execute(finalRequestParams);
//                    Intent intent = new Intent(_context, Add_TechScreen.class);
//                    startActivity(intent);
                }

            }
        });
    }
    private void  sendRequest(){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                createMultiPartRequest();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, responseListener,exceptionListener, BackgroundSaftyCheckScreen.this, "Registering");
                getApiResponseAsync.execute();
            }
        }.execute();
    }

    private  MultipartUtility createMultiPartRequest(){

            try {
                multipart = new MultipartUtility(Constants.BASE_URL, Constants.CHARSET);
                for ( String key : finalRequestParams.keySet() ) {
                    multipart.addFormField(key, finalRequestParams.get(key));
                    Log.e(""+key,"="+finalRequestParams.get(key));
                }
                if (isPro){
                    if (image_driver != null){
                        multipart.addFilePart("data[technicians][driver_license_image]",new File(image_driver));
                    }
                    if (image_profile != null){
                        multipart.addFilePart("data[technicians][profile_image]",new File(image_profile));
                    }
                }else {
                    if (image_driver != null){
                        multipart.addFilePart("driver_license_image",new File(image_driver));
                    }
                    if (image_profile != null){
                        multipart.addFilePart("profile_image",new File(image_profile));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return multipart;
    }
    ExceptionListener exceptionListener = new ExceptionListener(){

        @Override
        public void handleException(int exceptionStatus) {
            handler.sendEmptyMessage(exceptionStatus);
        }
    };
    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                String STATUS = Response.getString("STATUS");
                if (STATUS.equals("SUCCESS")) {
                    JSONObject RESPONSE = Response.getJSONObject("RESPONSE");

                    Utilities.getSharedPreferences(_context).edit().putString(Preferences.REGISTER_JSON_DATA, RESPONSE.toString()).commit();

                    String Token = Response.getJSONObject("RESPONSE").getString("token");
                    String role = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("role");
                    String email = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("email");
                    String phone = Response.getJSONObject("RESPONSE").getJSONObject("users").getString("phone");
                    Log.e("AUTH TOKEN", Token);
                    Log.e("ROLE", role);
                    JSONObject pro_settings;
                    editor.putString(Preferences.ROLE, role);
                    editor.putString(Preferences.AUTH_TOKEN, Token);
                    editor.putString(Preferences.EMAIL, email);
                    editor.putString(Preferences.PHONE, phone);
                    if (role.equals("pro")) {
                        pro_settings = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("pro_settings");
                        editor.putString(Preferences.SETTING_ID, pro_settings.getString("id"));
                        editor.putString(Preferences.LOCATION_SERVICE, pro_settings.getString("location_services"));
                        editor.putString(Preferences.TEXT_MESSAGING, pro_settings.getString("text_messaging"));
                        editor.putString(Preferences.AVAILABLE_JOBS_NOTIFICATION, pro_settings.getString("available_jobs_notification"));
                        editor.putString(Preferences.JOB_WON_NOTIFICATION, pro_settings.getString("job_won_notification"));
                        editor.putString(Preferences.JOB_LOST_NOTIFICATION, pro_settings.getString("job_lost_notification"));
                        editor.putString(Preferences.JOB_RESECHDULED, pro_settings.getString("job_rescheduled"));
                        editor.putString(Preferences.JOB_CANCELLED, pro_settings.getString("job_canceled"));

                        JSONObject profile_image = null;
                        profile_image = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("technicians").getJSONObject("profile_image");
                        if (profile_image.has("original")) {
                            String image_original = profile_image.getString("original");
                            editor.putString(Preferences.PROFILE_IMAGE, image_original);
                        }

                        editor.putString(Preferences.LOGIN_JSON_DATA, Response.toString());

                        JSONObject pros = null;
                        pros = Response.getJSONObject("RESPONSE").getJSONObject("users").getJSONObject("pros");
                        String city = pros.getString("city");
                        String state = pros.getString("state");
                        String zip = pros.getString("zip");
                        String address = pros.getString("address");
                        String hourly_rate = pros.getString("hourly_rate");
                        String company_name = pros.getString("company_name");
                        String ein_number = pros.getString("ein_number");
                        String bank_name = pros.getString("bank_name");
                        String bank_routing_number = pros.getString("bank_routing_number");
                        String bank_account_number = pros.getString("bank_account_number");
                        String bank_account_type = pros.getString("bank_account_type");
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
                    editor.putString(Preferences.SERVICES_JSON_ARRAY, services.toString());
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
//                    Get Json Data from prefs
//                    String strJson =  Utilities.getSharedPreferences(_context).getString("jsondata", "0");
//                    if(strJson != null){
//                        JSONObject jsonData = new JSONObject(strJson);}

                    String token = RESPONSE.getString("token");
                    if (_prefs.edit().putString(Preferences.AUTH_TOKEN, token).commit()) {
                        if (_prefs.getString(Preferences.ROLE,"").equals("pro")){
                            Intent intent = new Intent(BackgroundSaftyCheckScreen.this, Add_TechScreen.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(BackgroundSaftyCheckScreen.this, SetupCompleteScreen.class);
                            startActivity(intent);
                        }

                    }
                } else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    showAlertDialog("Fixd-pro", error_message);
                    break;
                }
                case 500:{
                    showAlertDialog("Fixd-pro", "Server Error 500");
                    break;

                }
                default:{

                }
            }
        }
    };

    private void setWidgetIDs() {
        text_securityno = (EditText) findViewById(R.id.text_securityno);

        txtBack = (TextView) findViewById(R.id.txtBack);
        txtDone = (TextView) findViewById(R.id.txtDone);
        text_backgroundsafety = (TextView) findViewById(R.id.text_backgroundsafety);
        text_description = (TextView) findViewById(R.id.text_description);
        text_thisno = (TextView) findViewById(R.id.text_thisno);
        text_enduserDetail = (TextView) findViewById(R.id.text_enduserDetail);
        check_text = (TextView) findViewById(R.id.check_text);
        check_id = (CheckBox) findViewById(R.id.check_id);

    }

    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
//        italic_fontfamily = Typeface.createFromAsset(getAssets(),"helvetica-neue-light-italic.ttf");
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
        text_securityno.setTypeface(fontfamily);
//        text_backgroundsafety.setTypeface(fontfamily);
        text_description.setTypeface(fontfamily);
        text_thisno.setTypeface(italic_fontfamily);
        text_enduserDetail.setTypeface(fontfamily);
        check_text.setTypeface(fontfamily);

    }

    private void showAlertDialog(String Title, String Message) {
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

    private void showCustomDialog() {
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.backgroundsafty_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtAlmostDone = (TextView) dialog.findViewById(R.id.txtAlmostDone);
        TextView txtBecauseOur = (TextView) dialog.findViewById(R.id.txtBecauseOur);
        TextView txtRateDoller = (TextView) dialog.findViewById(R.id.txtRateDoller);
        editCardNo = (EditText) dialog.findViewById(R.id.editCardNo);
        ImageView img_keypad = (ImageView) dialog.findViewById(R.id.img_keypad);
        editYear = (EditText) dialog.findViewById(R.id.editYear);
        editMonth = (EditText) dialog.findViewById(R.id.editMonth);
        editCw = (EditText) dialog.findViewById(R.id.editCw);
        editZipCode = (EditText) dialog.findViewById(R.id.editZipCode);
        editFirstName = (EditText) dialog.findViewById(R.id.editFirstName);
        editLastName = (EditText) dialog.findViewById(R.id.editLastName);
        ImageView img_camra = (ImageView) dialog.findViewById(R.id.img_camra);
        TextView txtScanCard = (TextView) dialog.findViewById(R.id.txtScanCard);
        ImageView img_finish = (ImageView) dialog.findViewById(R.id.img_finish);
        LinearLayout btnScanCard = (LinearLayout)dialog.findViewById(R.id.layout3);

        txtAlmostDone.setTypeface(fontfamily);
        txtBecauseOur.setTypeface(fontfamily);

        txtRateDoller.setTypeface(fontfamily);
        editCardNo.setTypeface(fontfamily);
        editYear.setTypeface(fontfamily);
        editMonth.setTypeface(fontfamily);
        editCw.setTypeface(fontfamily);
        editFirstName.setTypeface(fontfamily);
        editLastName.setTypeface(fontfamily);
        editZipCode.setTypeface(fontfamily);
        txtScanCard.setTypeface(fontfamily);

        txtScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress(v);
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        img_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_number = editCardNo.getText().toString().trim();
                month = editMonth.getText().toString().trim();
                year = editYear.getText().toString().trim();;
                cvv = editCw.getText().toString().trim();
                zip_code = editZipCode.getText().toString().trim();;
                first_name = editFirstName.getText().toString().trim();
                last_name = editLastName.getText().toString().trim();
                if (card_number.length() == 0){
                    showAlertDialog("Fixd-pro","please enter card number");
                }else if (card_number.length() != 16){
                    showAlertDialog("Fixd-pro","please enter a valid card number");
                }
                else if (month.length() == 0){
                    showAlertDialog("Fixd-pro","please enter month");
                }
                else if (year.length() == 16){
                    showAlertDialog("Fixd-pro","please enter a year");
                }
                else if (year.length() < 4){
                    showAlertDialog("Fixd-pro","please enter a valid a valid year");
                }else if (cvv.length() == 0){
                    showAlertDialog("Fixd-pro","please enter CVV");
                }else if (cvv.length() < 3){
                    showAlertDialog("Fixd-pro","please enter a valid a CVV number");
                }else if (zip_code.length() ==0){
                    showAlertDialog("Fixd-pro","please enter Zip");
                }else if (first_name.length() ==0){
                    showAlertDialog("Fixd-pro","please enter first name");
                }else if (last_name.length() ==0){
                    showAlertDialog("Fixd-pro","please enter last name");
                }else {
                    finalRequestParams.put("data[credit_card][card_number]",card_number);
                    finalRequestParams.put("data[credit_card][cvv]",cvv);
                    finalRequestParams.put("data[credit_card][first_name]",first_name);
                    finalRequestParams.put("data[credit_card][last_name]",last_name);
                    finalRequestParams.put("data[credit_card][month]",month);
                    finalRequestParams.put("data[credit_card][year]",year);
                    finalRequestParams.put("data[credit_card][zip]",zip_code);
                    sendRequest();
                }

            }
        });

        dialog.show();


    }

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true); // default: false
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                ;

                editCardNo.setText(scanResult.getRedactedCardNumber().replace(" ",""));
                card_number = scanResult.cardNumber;

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    editYear.setText(scanResult.expiryYear+"");
                    editMonth.setText(scanResult.expiryMonth+"");
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                    editCw.setText(scanResult.cvv);
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                    editZipCode.setText(scanResult.postalCode);
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
}

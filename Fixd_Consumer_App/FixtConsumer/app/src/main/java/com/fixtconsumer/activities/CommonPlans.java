package com.fixtconsumer.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.adapters.CompletePlanAdapter;
import com.fixtconsumer.adapters.OtherPlansAdapter;
import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.singletons.WarrentyPlanSingleton;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CommonPlans extends AppCompatActivity {
    TextView lblBack,lblPlanName;
    ListView lstCompletePlan ;
    WarrentyPlanBean warrentyPlanBean ;
    SharedPreferences _prefs;
    boolean isnew = false ;
    String error_message = "";
    CheckAlertDialog checkALert;
    private Dialog dialog;
    Context _context = this ;
    EditText editCardNo, editYear, editMonth, editCw, editZipCode, editFirstName, editLastName;
    String card_number = "", month = "", year = "", cvv = "", zip_code = "", first_name = "", last_name = "";
    private Typeface fontfamily, italic_fontfamily;
    private static final int MY_SCAN_REQUEST_CODE = 200;
    RelativeLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_plans);
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        if (getIntent() != null)
            isnew = getIntent().getBooleanExtra("isnew",false);
        _prefs = Utility.getSharedPreferences(this);
        checkALert = new CheckAlertDialog();
        warrentyPlanBean = WarrentyPlanSingleton.getInstance().getWarrentyPlanBean();
        setWidgets();
        setListeners();
        lblPlanName.setText(warrentyPlanBean.getName() + " Plan");
        mainLayout.setBackgroundResource(Utility.getSelectedPlanPhoto(warrentyPlanBean.getName()));
        lstCompletePlan = (ListView)findViewById(R.id.lstCompletePlan);
        OtherPlansAdapter adapter = new OtherPlansAdapter(this,warrentyPlanBean.getApplianceTypeBeansArrayList(),getResources());
        lstCompletePlan.setAdapter(adapter);
        setHeader();
        setFooter();
//        setWidgets();
//        setListeners();
    }
    private void setHeader(){
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.other_plan_header_item, lstCompletePlan, false);
        TextView txtPlanPrice = (TextView)header.findViewById(R.id.txtPlanPrice);
        txtPlanPrice.setText("$"+warrentyPlanBean.getPrice_inclusive_tax()+" /mo.");
        lstCompletePlan.addHeaderView(header, null, false);
    }
    private void setFooter(){
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.item_purchase_footer, lstCompletePlan, false);
        Button btnPurchase = (Button)header.findViewById(R.id.btnPurchase);
        if (!isnew)
            btnPurchase.setText("Cancel");
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnew){
                    if (_prefs.getString(Preferences.HAS_CARD,"0").equals("0")){
//                    do purchase
                        subscripbePlan();
                    }else{
//                    add card and do purchasea
                        showAddCardDialog(false);
                    }
                }else {
//                    Cancel Plan
                    showCancelPlanDialog();
                }
            }
        });
        lstCompletePlan.addFooterView(header, null, false);
    }
    private void setWidgets(){
        lblBack = (TextView)findViewById(R.id.cancel);
        lblPlanName = (TextView)findViewById(R.id.lblPlanName);
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
    }
    private void setListeners(){
        lblBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void purchasePlan(){

    }
    IHttpResponseListener getCancelPlanResponse = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("",""+response);
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
    IHttpResponseListener getSubscribePlanResponse = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("",""+response);
            try {
                Log.e("","response"+response);
                AddressesSingleTonClasses.getInstance().arrayListFileAClain.clear();
                if (response.getString("STATUS").equals("SUCCESS")) {

                    handler.sendEmptyMessage(0);
                }else{
                    JSONObject errors = response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
                        if (key.equals("501")){
                            if (dialog != null && dialog.isShowing())
                                dialog.dismiss();
                            showAddCardDialog(true);

                            return;
                        }
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(1);
                }
            }catch (JSONException e){
            }
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    Intent intent = new Intent(CommonPlans.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(CommonPlans.this, getResources().getString(R.string.alert_title), error_message);
                    break;
                }
                case 2:{
                    subscripbePlan();
                    break ;
                }
             }
        }
    };
    IHttpExceptionListener getCancelPlanException = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {

        }
    };
    IHttpExceptionListener getSubscribePlanException = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {

        }
    };
    private void cancelPlan(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",getCancelPlanResponse,getCancelPlanException,this,"Canceling");
        apiResponseAsync.execute(getCancelPlan());
    }
    private void subscripbePlan(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",getSubscribePlanResponse,getSubscribePlanException,this,"Subscribing");
        apiResponseAsync.execute(getSubscribePlan());
    }
    private HashMap<String,String> getCancelPlan(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","cancel");
        hashMap.put("object","warranty_plans");
        hashMap.put("data[plan_id]",warrentyPlanBean.getId());
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        return hashMap;
    }
    private HashMap<String,String> getSubscribePlan(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","subscribe");
        hashMap.put("object","warranty_plans");
        hashMap.put("data[plan_id]",warrentyPlanBean.getId());
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[customer_address_id]", WarrentyPlanSingleton.getInstance().AddressId);
        return hashMap;
    }
    private void showCancelPlanDialog(){
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel_plan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtSave = (TextView) dialog.findViewById(R.id.txtSave);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelPlan();
            }
        });
        dialog.show();
    }
    private void showAddCardDialog(boolean  isFailed) {
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!isFailed)
            dialog.setContentView(R.layout.card_info_dialog);
        else
            dialog.setContentView(R.layout.dialog_payment_failed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtAlmostDone = (TextView) dialog.findViewById(R.id.txtAlmostDone);
        TextView txtSave = (TextView) dialog.findViewById(R.id.txtSave);
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

        LinearLayout btnScanCard = (LinearLayout) dialog.findViewById(R.id.layout3);

        txtAlmostDone.setTypeface(fontfamily);
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

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_number = editCardNo.getText().toString().trim();
                month = editMonth.getText().toString().trim();
                year = editYear.getText().toString().trim();
                ;
                cvv = editCw.getText().toString().trim();
                zip_code = editZipCode.getText().toString().trim();
                ;
                first_name = editFirstName.getText().toString().trim();
                last_name = editLastName.getText().toString().trim();
                if (card_number.length() == 0) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter card number");
                } else if (card_number.length() != 16) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter a valid card number");
                } else if (month.length() == 0) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter month");
                } else if (year.length() == 16) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter a year");
                } else if (year.length() < 4) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter a valid a valid year");
                } else if (cvv.length() == 0) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter CVV");
                } else if (cvv.length() < 3) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter a valid a CVV number");
                }
//                else if (zip_code.length() == 0) {
//                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter Zip");
//                }
                else if (first_name.length() == 0) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter first name");
                } else if (last_name.length() == 0) {
                    checkALert.showcheckAlert(CommonPlans.this, getString(R.string.alert_title), "please enter last name");
                } else {
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", addCardResponseListener, addCardExceptionListener, CommonPlans.this, "Scheduling");
                    apiResponseAsync.execute(getAddCardPareams());
                }

            }
        });

        dialog.show();
    }
    private HashMap<String, String> getAddCardPareams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "create");
        hashMap.put("object", "cards");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[card_number]", card_number);
        hashMap.put("data[month]", month);
        hashMap.put("data[firstname]", first_name);
        hashMap.put("data[lastname]", last_name);
        hashMap.put("data[year]", year);
        hashMap.put("data[cvv]", cvv);
//        hashMap.put("data[zip_code]", zip_code);
        hashMap.put("data[primary]", "1");
        hashMap.put("id", _prefs.getString(Preferences.ID,""));

        return hashMap;
    }
    IHttpResponseListener addCardResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
//                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
//                    JSONArray results = response.getJSONArray("RESPONSE");
//                    for (int i = 0; i < results.length(); i++) {
//                        JSONObject object = results.getJSONObject(i);
//                        HomeServiceBeans homeService = new HomeServiceBeans();
//                        homeService.setId(object.getInt("id"));
//                        homeService.setTitle(object.getString("name"));
//                        homeService.setStatus(object.getString("status"));
//                        homeService.setDisplay_order(object.getString("display_order"));
//                        homeService.setImage(object.getString("name"));
//                        arrayList.add(homeService);
//                    }
//                    Collections.sort(arrayList, new Comparator<HomeServiceBeans>() {
//
//                        public int compare(HomeServiceBeans p1, HomeServiceBeans p2) {
//                            return Integer.parseInt(p1.getDisplay_order()) - Integer.parseInt(p2.getDisplay_order());
//                        }
//                    });
//                    HomeServiceSingleTon.getInstance().setList(arrayList);
                    _prefs.edit().putString(Preferences.HAS_CARD, "1").commit();
                    handler.sendEmptyMessage(2);

                } else {
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
    IHttpExceptionListener addCardExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    };
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(CommonPlans.this, CardIOActivity.class);
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true); // default: false
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                ;

                editCardNo.setText(scanResult.getRedactedCardNumber().replace(" ", ""));
                card_number = scanResult.cardNumber;

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    editYear.setText(scanResult.expiryYear + "");
                    editMonth.setText(scanResult.expiryMonth + "");
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
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }

}

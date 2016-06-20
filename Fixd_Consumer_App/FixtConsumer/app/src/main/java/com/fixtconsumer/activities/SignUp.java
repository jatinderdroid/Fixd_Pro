package com.fixtconsumer.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.GetApiResponseAsyncMutipart;
import com.fixtconsumer.net.GetApiResponseAsyncNoProgress;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.net.MultipartUtility;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.ImageHelper2;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity implements TextView.OnEditorActionListener {
    public final String TAG = "SignUp";
    public Context context  = SignUp.this;
    CheckAlertDialog checkALert;
    TextView txtIagree_text;
    CircleImageView profile_pic;
    EditText txtFirstName, txtLastName, txtEmail, txtPassword, txtConfirmPassword, txtMobile, txtAddress, txtZip, txtAddress2;
    Typeface fontfamily;
    Button btnSignUp;
    ImageView imgClose;
    TextView txtCity, txtState, lblAddPhoto;;
    public String FirstName = "",LastName = "",EmailAddress = "",Password = "",ConfirmPassword = "",MobileNumber = "",
    Address = "",ZipCode = "",City = "",State = "",error_message = "", Address2 = "" ,StateAbre = "";
    SharedPreferences _prefs = null ;
    Context _context = this ;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    public String selectedImagePath=null;
    public String Path, path;
    public Uri selectedImageUri;
    Dialog dialog = null ;
    MultipartUtility multipart = null;

//    https://android-arsenal.com/details/1/2046
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkALert = new CheckAlertDialog();
        _prefs = Utility.getSharedPreferences(_context);
        setWidgets();
        setTypeface();
        setListeners();
        txtIagree_text.setText(Html.fromHtml(this.getResources().getString(R.string.i_agrre_text)));

    }

    private void setWidgets(){
        lblAddPhoto = (TextView) findViewById(R.id.lblChangePhoto);
        txtIagree_text = (TextView)findViewById(R.id.txtIagree_text);
        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);
        txtMobile = (EditText)findViewById(R.id.txtMobile);
        txtAddress = (EditText)findViewById(R.id.txtAddress);
        txtAddress2 = (EditText)findViewById(R.id.txtAddress2);
        txtZip = (EditText)findViewById(R.id.txtZip);
        txtCity = (TextView)findViewById(R.id.txtCity);
        txtState = (TextView)findViewById(R.id.txtState);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        imgClose = (ImageView)findViewById(R.id.imgClose);
        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
    }
    private  void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
//        txtFirstName.setTypeface(fontfamily);
//        txtLastName.setTypeface(fontfamily);
//        txtEmail.setTypeface(fontfamily);
//        txtPassword.setTypeface(fontfamily);
//        txtConfirmPassword.setTypeface(fontfamily);
//        txtMobile.setTypeface(fontfamily);
//        txtAddress.setTypeface(fontfamily);
//        txtAddress2.setTypeface(fontfamily);
//        txtZip.setTypeface(fontfamily);
//        txtCity.setTypeface(fontfamily);
//        txtState.setTypeface(fontfamily);
//        btnSignUp.setTypeface(fontfamily);
//        txtIagree_text.setTypeface(fontfamily);
        txtZip.setOnEditorActionListener(this);

    }

    private void setListeners(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirstName = txtFirstName.getText().toString().trim();
                LastName = txtLastName.getText().toString().trim();
                EmailAddress = txtEmail.getText().toString().trim();
                Password = txtPassword.getText().toString().trim();
                ConfirmPassword = txtConfirmPassword.getText().toString().trim();
                MobileNumber = txtMobile.getText().toString().trim();
                Address = txtAddress.getText().toString().trim();
                Address2 = txtAddress2.getText().toString().trim();
                ZipCode = txtZip.getText().toString().trim();
                City = txtCity.getText().toString().trim();
                State = txtState.getText().toString().trim();
                if (FirstName.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter first name.");
                }else if (LastName.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter last name.");
                }else if (EmailAddress.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter email address.");
                }else if (!Utility.isValidEmail(EmailAddress)){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter valid email address.");
                }else if (Password.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter password.");
                }else if (Password.length() < 6){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Your password should be 6 or more characters long, Please try again.");
                }else if (ConfirmPassword.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter confirm password.");
                }else if (!ConfirmPassword.equals(Password)){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Password did not match,Please try again.");
                }else if (MobileNumber.equals("") ){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter mobile number.");
                }else if (MobileNumber.length() < 10){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Your phone number seems to invalid, Please try again.");
                }else if (Address.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter address.");
                }else if (ZipCode.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter zip code.");
                }
                else if (City.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter city.");
                }else if (State.equals("")){
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),"Please enter state.");
                }
                else{
                    Utility.hideKeyBoad(_context,v);
                    new AsyncTask<Void, Void, String>() {

                        @Override
                        protected String doInBackground(Void... params) {
                            createMultiPartRequest();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                    GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, registerResponseListener, registerExceptionListener, SignUp.this, "Registering");
                    getApiResponseAsync.execute();
                }
            }.execute();
//                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",registerResponseListener,registerExceptionListener,SignUp.this,"Registering");
//                    apiResponseAsync.execute(getRegisterRequestParams());
                }
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
        lblAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtZip.getText().toString().trim().equals("")) {
                    checkALert.showcheckAlert(SignUp.this, context.getResources().getString(R.string.alert_title), "Please enter zip code.");
                } else {
                    Utility.hideKeyBoad(_context, v);
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", zipResponseListener, zipExceptionListener, SignUp.this, "Getting");
                    apiResponseAsync.execute(getZipRequestParams());
                }
            }
        });

        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtZip.getText().toString().trim().equals("")) {
                    checkALert.showcheckAlert(SignUp.this, context.getResources().getString(R.string.alert_title), "Please enter zip code.");
                } else {
                    Utility.hideKeyBoad(_context, v);
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", zipResponseListener, zipExceptionListener, SignUp.this, "Getting");
                    apiResponseAsync.execute(getZipRequestParams());
                }
            }
        });

    }
    /*Create Camra Gallery PopUP*/
    private void showCamraGalleryPopUp() {
        dialog = new Dialog(_context);
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dialog_camra_gallery);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtTakePicture = (TextView) dialog.findViewById(R.id.txtTakePicture);
        TextView txtCamera = (TextView) dialog.findViewById(R.id.txtCamera);
        TextView txtGallery = (TextView) dialog.findViewById(R.id.txtGallery);
        // set the typeface...
        txtCamera.setTypeface(fontfamily);
        txtGallery.setTypeface(fontfamily);
        txtTakePicture.setTypeface(fontfamily);
        // set the click listner...
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera();
            }
        });
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        dialog.show();
    }
    private  void openCamera(){
        Intent camraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camraIntent, CAMERA_REQUEST);
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CAMERA_REQUEST) {
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri uri = data.getData();
                    Picasso.with(this).load(uri)
                            .resize(172, 172).centerCrop().into(profile_pic);
                    selectedImagePath = ImageHelper2.getRealPathFromURI(uri.toString(), _context);

//                selectedImageUri = getImageUri(this, photo);
//                if (selectedImageUri == null)
//                    return;
//                Path = ImageHelper2.compressImage(selectedImageUri, this);
//                profile_pic.setImageBitmap(ImageHelper2.decodeSampledBitmapFromFile(Path, 400, 400));
//                    profile_pic.setImageBitmap(photo);
                    lblAddPhoto.setText("Change Photo");

//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageHelper2.decodeSampledBitmapFromFile(Path, 400, 200).compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] b = baos.toByteArray();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Picasso.with(this).load(selectedImageUri)
                        .resize(150, 150).centerCrop().into(profile_pic);
                Log.e("UserProfileScreen", "Image Path : " + selectedImagePath);
//                    profile_pic.setImageURI(selectedImageUri);
                lblAddPhoto.setText("Change Photo");
            }
        }
    }

    // get path from the gallery...
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // get the path/....
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("", "path............" + path);
        return Uri.parse(path);
    }
    IHttpResponseListener zipResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("","response"+response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
                    if (results.length() == 0){
                        error_message = "Please enter valid zip code" ;
                        handler.sendEmptyMessage(4);
                    }else {
                        JSONObject jsonObject = results.getJSONObject(0);
                        City = jsonObject.getString("cityname");
                        State = jsonObject.getString("statename");
                        StateAbre = jsonObject.getString("stateabbr");
                        handler.sendEmptyMessage(2);
                    }

                }
                else{
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
    IHttpExceptionListener zipExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("","response"+exception);
            error_message = exception ;
            handler.sendEmptyMessage(1);
        }
    };
    IHttpResponseListener registerResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    SharedPreferences.Editor editor = _prefs.edit();

                    JSONObject RESPONSE = response.getJSONObject("RESPONSE");
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

                    JSONObject customer_settings = users.getJSONObject("customer_settings");
                    editor.putString(Preferences.SETTINGS_ID,customer_settings.getString("id"));
                    editor.putString(Preferences.SETTINGS_LOCATION_SERVICE,customer_settings.getString("location_services"));
                    editor.putString(Preferences.SETTINGS_TEXT_MESSAGING,customer_settings.getString("text_messaging"));
                    editor.putString(Preferences.SETTINGS_NOTIFICATION,customer_settings.getString("notifications"));

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
    IHttpExceptionListener registerExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("","response"+exception );
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
                    Intent intent = new Intent(SignUp.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
                case 2:{
                    txtState.setText(State);
                    txtCity.setText(City);
                    break;
                }case 3:{
                    Intent intent = new Intent(SignUp.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                }case 4:{
                    checkALert.showcheckAlert(SignUp.this,context.getResources().getString(R.string.alert_title),error_message);
                    txtState.setText("");
                    txtCity.setText("");
                    break;
                }
            }
        }
    };
    IHttpResponseListener loginResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
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

                    JSONObject customer_settings = users.getJSONObject("customer_settings");
                    editor.putString(Preferences.SETTINGS_ID,customer_settings.getString("id"));
                    editor.putString(Preferences.SETTINGS_LOCATION_SERVICE,customer_settings.getString("location_services"));
                    editor.putString(Preferences.SETTINGS_TEXT_MESSAGING,customer_settings.getString("text_messaging"));
                    editor.putString(Preferences.SETTINGS_NOTIFICATION,customer_settings.getString("notifications"));

                    editor.commit();
                    handler.sendEmptyMessage(3);
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
    private void createMultiPartRequest(){
        try {
            multipart = new MultipartUtility(Constants.BASE_URL_SINGLE, Constants.CHARSET);
            multipart.addFormField("api","signup");
            multipart.addFormField("object","customers");
            multipart.addFormField("data[phone]",MobileNumber);
            multipart.addFormField("data[state]",StateAbre);
            multipart.addFormField("data[city]",City);
            multipart.addFormField("data[zip]",ZipCode);
            multipart.addFormField("data[email]",EmailAddress);
            multipart.addFormField("data[last_name]",LastName);
            multipart.addFormField("data[first_name]",FirstName);
            multipart.addFormField("data[password]",Password);
            multipart.addFormField("data[address_2]",Address2);
            multipart.addFormField("data[address]",Address);
            if (selectedImagePath != null)
                multipart.addFilePart("data[profile_image]",new File(selectedImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private HashMap<String,String> getRegisterRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","register");
        hashMap.put("object", "customer");
        hashMap.put("phone", MobileNumber);
        hashMap.put("address", Address);
        hashMap.put("address_2", Address2);
        hashMap.put("state", State);
        hashMap.put("city", City);
        hashMap.put("zip", ZipCode);
        hashMap.put("email", EmailAddress);
        hashMap.put("first_name", FirstName);
        hashMap.put("last_name", LastName);
        hashMap.put("password", Password);
        return hashMap;
    }

    private HashMap<String,String> getZipRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object", "zipcodes");
        hashMap.put("per_page", 20+"");
        hashMap.put("page", 1+"");
        hashMap.put("where[zipcode]", txtZip.getText().toString());
        return hashMap;
    }
    private HashMap<String,String> getLoginRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","login");
        hashMap.put("email",EmailAddress);
        hashMap.put("password",Password);
        return hashMap;
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.txtZip && actionId == EditorInfo.IME_ACTION_DONE ){
            Utility.hideKeyBoad(_context,v);
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",zipResponseListener,zipExceptionListener,SignUp.this,"Getting");
            apiResponseAsync.execute(getZipRequestParams());
        }
        return true;
    }
}

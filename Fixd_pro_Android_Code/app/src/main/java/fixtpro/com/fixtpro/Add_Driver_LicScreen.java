  package fixtpro.com.fixtpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.beans.SkillTrade;
import fixtpro.com.fixtpro.imageupload.ImageHelper2;
import fixtpro.com.fixtpro.singleton.TradeSkillSingleTon;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.ExceptionListener;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsyncMutipart;
import fixtpro.com.fixtpro.utilites.MultipartUtility;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;
import fixtpro.com.fixtpro.views.WheelView;

public class Add_Driver_LicScreen extends AppCompatActivity {
    private Context _context = this;
    private String TAG = "Add_Driver_LicScreen";
    Typeface fontfamily;
    TextView txtBack,txtDone,txtTradeskill,txtHourlyrate,lblAddDriverLicence;
    EditText txtEinnumber,txtInsurance,txtPolicyNumber,txtYearsBusi,txtTradeLiNo;
    HashMap<String,String> finalRequestParams = null;
    ImageView imgDriver;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int TRADE_LICENCE_NUMBER = 3;
    public String Path,path;
    public Uri selectedImageUri;
    public String ein_number = "",insurance_carrier = "",policy_number = "",years_in_business = "",trade_skill  = "",hourly_rate = "",trade_license_number = "";
    int finalHeight, finalWidth;
    File driver_license_image_file = null ;
    SharedPreferences _prefs  = null;
    private boolean isEditMode = false ;
    String error_message = "";
    Dialog dialog = null ;
    String image_profile = null ;
    MultipartUtility multipart = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__driver__lic_screen);
        setWidgetIDs();
        _prefs = Utilities.getSharedPreferences(_context);
        if (_prefs.getBoolean(Preferences.ISLOGIN,false)){
            isEditMode = true;

        }else {
            if (getIntent() != null) {
                finalRequestParams = (HashMap<String, String>) getIntent().getSerializableExtra("finalRequestParams");
                image_profile = getIntent().getStringExtra("image_profile");
            }
        }

        setTypeface();
        setClickListner();
//        setTradeSkills();
        ViewTreeObserver vto = imgDriver.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imgDriver.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imgDriver.getMeasuredHeight();
                finalWidth = imgDriver.getMeasuredWidth();

                return true;
            }
        });
        intLayout();
    }
    private void intLayout(){
        if (!_prefs.getString(Preferences.ROLE,"pro").equals("pro") && isEditMode){
            txtEinnumber.setVisibility(View.GONE);
            txtInsurance.setVisibility(View.GONE);
            txtPolicyNumber.setVisibility(View.GONE);
            txtHourlyrate.setVisibility(View.GONE);
        }
        if(!_prefs.getString(Preferences.ROLE, "pro").equals("pro") && !isEditMode){
            txtEinnumber.setVisibility(View.GONE);
            txtInsurance.setVisibility(View.GONE);
            txtPolicyNumber.setVisibility(View.GONE);
            txtHourlyrate.setVisibility(View.GONE);
        }
        txtTradeLiNo.setText(_prefs.getString(Preferences.TRADE_LICENSE_NUMBER, ""));
        txtYearsBusi.setText(_prefs.getString(Preferences.YEARS_IN_BUSINESS, ""));
        getTradeSkills();
        txtEinnumber.setText(_prefs.getString(Preferences.EIN_NUMEBR, ""));
        txtInsurance.setText(_prefs.getString(Preferences.INSURANCE, ""));
        txtPolicyNumber.setText(_prefs.getString(Preferences.INSURANCE_POLICY, ""));
        txtHourlyrate.setText(_prefs.getString(Preferences.HOURLY_RATE, ""));
        if (_prefs.getString(Preferences.DRIVER_LICENSE_IMAGE,null) != null){
            Target loadtarget = null;
            if (loadtarget == null) loadtarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // do something with the Bitmap
                    imgDriver.setAdjustViewBounds(true);
                    imgDriver.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgDriver.setImageBitmap(bitmap);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    imgDriver.setImageResource(R.drawable.driver_license);
                    Log.e("","Error");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }

            };

            Picasso.with(this).load(_prefs.getString(Preferences.DRIVER_LICENSE_IMAGE, null)).into(loadtarget);
        }

    }
    private void getTradeSkills(){
        String tradeSkills  = _prefs.getString(Preferences.SERVICES_JSON_ARRAY,"");
        try {
            JSONArray serviceArray = new JSONArray(tradeSkills);
            for (int i = 0 ; i < serviceArray.length() ; i++){
                Log.e("","");
                for (int k = 0 ; k < TradeSkillSingleTon.getInstance().getList().size(); k++){
                    if ((TradeSkillSingleTon.getInstance().getList().get(k).getId()+"").equals(serviceArray.getString(i))){
                        TradeSkillSingleTon.getInstance().getList().get(k).setIsChecked(true);
                    }
                }
            }
            setTradeSkills();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void setClickListner() {
        lblAddDriverLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
        txtTradeskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, SelectTradeLicence.class);
                startActivityForResult(intent, TRADE_LICENCE_NUMBER);

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

                    trade_license_number = txtTradeLiNo.getText().toString().trim();
                    years_in_business = txtYearsBusi.getText().toString().trim();
                    ein_number = txtEinnumber.getText().toString().trim();
                    insurance_carrier = txtInsurance.getText().toString().trim();
                    policy_number = txtPolicyNumber.getText().toString().trim();
                    hourly_rate = txtHourlyrate.getText().toString().trim();
                    if (trade_license_number.length() == 0) {
                        showAlertDialog("Fixd-Pro", "Please enter trade license number.");
                        return;
                    } else if (years_in_business.length() == 0) {
                        showAlertDialog("Fixd-Pro", "Please enter year in business.");
                        return;
                    } else if (trade_skill.length() == 0) {
                        showAlertDialog("Fixd-Pro", "Please enter skill.");
                        return;
                    } else if (ein_number.length() == 0 && (txtEinnumber.getVisibility() == View.VISIBLE)) {
                        showAlertDialog("Fixd-Pro", "Please enter ein number.");
                        return;
                    } else if (insurance_carrier.length() == 0 && (txtInsurance.getVisibility() == View.VISIBLE)) {
                        showAlertDialog("Fixd-Pro", "Please enter insurence carrier.");
                        return;
                    } else if (policy_number.length() == 0  && (txtPolicyNumber.getVisibility() == View.VISIBLE)) {
                        showAlertDialog("Fixd-Pro", "Please enter policy number.");
                        return;
                    } else if (hourly_rate.length() == 0  && (txtHourlyrate.getVisibility() == View.VISIBLE)) {
                        showAlertDialog("Fixd-Pro", "Please enter hourly rate.");
                        return;
                    } else {

                        if (_prefs.getString(Preferences.ROLE,"").equals("pro") || _prefs.getString(Preferences.ROLE,"").equals("technician") && isEditMode){
//                            GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", licenseUpdateListener, Add_Driver_LicScreen.this, "Loading");
//                            responseAsync.execute(getProfileUpdateParameters());
                            new AsyncTask<Void, Void, String>() {

                                @Override
                                protected String doInBackground(Void... params) {
                                    createMultiPartRequest(getProfileUpdateParameters());
                                    return null;
                                }
                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, licenseUpdateListener,exceptionListener, Add_Driver_LicScreen.this, "Updating");
                                    getApiResponseAsync.execute();
                                }
                            }.execute();
                        }
                        else if(!_prefs.getString(Preferences.ROLE, "pro").equals("pro") && !isEditMode){
                            Intent intent = new Intent(_context, BackgroundSaftyCheckScreen.class);
//                             finalRequestParams.put("years_in_business",Integer.parseInt(years_in_business)+"");
                            finalRequestParams.put("trade_license_number", trade_license_number);
                            Log.e("", "years_in_business" + years_in_business);

                            Log.e("","years_in_business"+years_in_business);
                            ArrayList<SkillTrade> skillTradesSelected  = TradeSkillSingleTon.getInstance().getListChecked();
                            for (int i = 0 ; i < skillTradesSelected.size() ; i++){
                                finalRequestParams.put("services[" + i + "]",skillTradesSelected.get(i).getId()+"");
                            }
//                             if (driver_license_image_file != null){
//                                 finalRequestParams.put("driver_license_image", new String(Utilities.convertFileToBytes(driver_license_image_file)));
//                             }
                            intent.putExtra("finalRequestParams",finalRequestParams);
                            intent.putExtra("image_driver",Path);
                            intent.putExtra("image_profile",image_profile);
                            intent.putExtra("years_in_business",years_in_business);
                            intent.putExtra("ispro",false);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(_context, WorkingRadiusActivity.class);
                            finalRequestParams.put("data[technicians][trade_license_number]",trade_license_number);
                            finalRequestParams.put("data[technicians][years_in_business]",years_in_business);
                            finalRequestParams.put("data[pros][ein_number]",ein_number);
                            finalRequestParams.put("data[pros][insurance]",insurance_carrier);
                            finalRequestParams.put("data[pros][insurance_policy]",policy_number);
                            finalRequestParams.put("data[pros][hourly_rate]", "75");
                            ArrayList<SkillTrade> skillTradesSelected  = TradeSkillSingleTon.getInstance().getListChecked();
                            for (int i = 0 ; i < skillTradesSelected.size() ; i++){
                                finalRequestParams.put("data[services][" + i + "]",skillTradesSelected.get(i).getId()+"");
                            }
//                            if (driver_license_image_file != null){
//                                finalRequestParams.put("data[technicians][driver_license_image]", new String(Utilities.convertFileToBytes(driver_license_image_file)));
//                            }
                            intent.putExtra("finalRequestParams",finalRequestParams);
                            intent.putExtra("image_driver",Path);
                            intent.putExtra("image_profile",image_profile);
                            intent.putExtra("years_in_business",years_in_business);
                            startActivity(intent);
                        }

//
//                        if (!isEditMode){
//
//                        Utilities.hideKeyBoad(_context, Add_Driver_LicScreen.this.getCurrentFocus());
//                         if (_prefs.getString(Preferences.ROLE,"").equals("pro")){
//                             Intent intent = new Intent(_context, WorkingRadiusActivity.class);
//                             finalRequestParams.put("data[technicians][trade_license_number]",trade_license_number);
//                             finalRequestParams.put("data[technicians][years_in_business]",years_in_business);
//                             finalRequestParams.put("data[pros][ein_number]",ein_number);
//                             finalRequestParams.put("data[pros][insurance]",insurance_carrier);
//                             finalRequestParams.put("data[pros][insurance_policy]",policy_number);
//                             finalRequestParams.put("data[pros][hourly_rate]", hourly_rate);
//                             ArrayLifst<SkillTrade> skillTradesSelected  = TradeSkillSingleTon.getInstance().getListChecked();
//                             for (int i = 0 ; i < skillTradesSelected.size() ; i++){
//                                 finalRequestParams.put("data[services][" + i + "]",skillTradesSelected.get(i).getId()+"");
//                             }
//                             if (driver_license_image_file != null){
//                                 finalRequestParams.put("data[technicians][driver_license_image]", new String(Utilities.convertFileToBytes(driver_license_image_file)));
//                             }
//                             intent.putExtra("finalRequestParams",finalRequestParams);
//                             intent.putExtra("image_driver",Path);
//                             intent.putExtra("image_profile",image_profile);
//                             intent.putExtra("years_in_business",years_in_business);
//                             startActivity(intent);
//                         }else {
//                             Intent intent = new Intent(_context, BackgroundSaftyCheckScreen.class);
////                             finalRequestParams.put("years_in_business",Integer.parseInt(years_in_business)+"");
//                             finalRequestParams.put("trade_license_number", trade_license_number);
//                             Log.e("", "years_in_business" + years_in_business);
//
//                             Log.e("","years_in_business"+years_in_business);
//                             ArrayList<SkillTrade> skillTradesSelected  = TradeSkillSingleTon.getInstance().getListChecked();
//                             for (int i = 0 ; i < skillTradesSelected.size() ; i++){
//                                 finalRequestParams.put("services[" + i + "]",skillTradesSelected.get(i).getId()+"");
//                             }
////                             if (driver_license_image_file != null){
////                                 finalRequestParams.put("driver_license_image", new String(Utilities.convertFileToBytes(driver_license_image_file)));
////                             }
//                             intent.putExtra("finalRequestParams",finalRequestParams);
//                             intent.putExtra("image_driver",Path);
//                             intent.putExtra("image_profile",image_profile);
//                             intent.putExtra("years_in_business",years_in_business);
//
//                             startActivity(intent);
//                         }
////                        Do it
//                        }else {
//                            GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", licenseUpdateListener, Add_Driver_LicScreen.this, "Loading");
//                            responseAsync.execute(getProfileUpdateParameters());
//                        }
                    }


                }
            });
        imgDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
        txtHourlyrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHourlyRateDialog();
            }
        });
        }
    ExceptionListener exceptionListener = new ExceptionListener(){

        @Override
        public void handleException(int exceptionStatus) {
            handler.sendEmptyMessage(exceptionStatus);
        }
    };
    ResponseListener licenseUpdateListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","Response.toString()"+Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS")){
                    SharedPreferences.Editor editor = _prefs.edit();
                    editor.putString(Preferences.TRADE_LICENSE_NUMBER,trade_license_number);
                    editor.putString(Preferences.YEARS_IN_BUSINESS,years_in_business);
                    editor.putString(Preferences.EIN_NUMEBR,ein_number);
                    editor.putString(Preferences.INSURANCE,insurance_carrier);
                    editor.putString(Preferences.INSURANCE_POLICY,policy_number);
                    editor.putString(Preferences.HOURLY_RATE, hourly_rate);
                    JSONObject RESPONSE = Response.getJSONObject("RESPONSE");
                    JSONArray services = RESPONSE.getJSONArray("services");
                    JSONArray local = new JSONArray();
                    for (int i = 0 ; i < services.length() ; i++){
                        if (_prefs.getString(Preferences.ROLE,"").equals("pro")){
                            JSONObject object = services.getJSONObject(i);
                            local.put(object.getString("id"));
                        }else {
                            local.put(services.getString(i));
                        }

                    }
                    JSONObject profile_image_diver_licence = null ;
                    profile_image_diver_licence = Response.getJSONObject("RESPONSE").getJSONObject("technicians").getJSONObject("driver_license_image");
                    if (!profile_image_diver_licence.isNull("original")){
                        String image_original =  profile_image_diver_licence.getString("original");
                        editor.putString(Preferences.DRIVER_LICENSE_IMAGE, image_original);
                    }
                    editor.putString(Preferences.SERVICES_JSON_ARRAY,local.toString());
                    editor.commit();
                    finish();
                }else{
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                        handler.sendEmptyMessage(2);
                    }
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
                case 500: {
                    showAlertDialog("Fixd-pro", "Server Error 500");
                    break;
                }
                default:{
                    showAlertDialog("Fixd-Pro",error_message);
                }
            }


        }
    };
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
    private void setWidgetIDs() {
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);

        txtEinnumber = (EditText)findViewById(R.id.txtEinnumber);
        txtInsurance = (EditText)findViewById(R.id.txtInsurance);
        txtPolicyNumber = (EditText)findViewById(R.id.txtPolicyNumber);
        txtYearsBusi = (EditText)findViewById(R.id.txtYearsBusi);
        txtTradeLiNo = (EditText)findViewById(R.id.txtTradeLiNo);
        txtTradeskill = (TextView)findViewById(R.id.txtTradeskill);
        txtHourlyrate = (TextView)findViewById(R.id.txtHourlyrate);

        imgDriver = (ImageView)findViewById(R.id.imgDriver);
        lblAddDriverLicence = (TextView)findViewById(R.id.lblAddDriverLicence);
    }

    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");

        txtDone.setTypeface(fontfamily);
        txtEinnumber.setTypeface(fontfamily);
        txtPolicyNumber.setTypeface(fontfamily);
        txtInsurance.setTypeface(fontfamily);
        txtYearsBusi.setTypeface(fontfamily);
        txtTradeLiNo.setTypeface(fontfamily);
        txtTradeskill.setTypeface(fontfamily);
        txtBack.setTypeface(fontfamily);
        txtHourlyrate.setTypeface(fontfamily);
        lblAddDriverLicence.setTypeface(fontfamily);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CAMERA_REQUEST) {
                try {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = data.getData();
//                selectedImageUri = getImageUri(this, photo);
//                if (selectedImageUri == null)
//                    return;
                    Path = ImageHelper2.compressImage(selectedImageUri, this);
//                imgDriver.setAdjustViewBounds(true);
                    imgDriver.getLayoutParams().height = finalHeight;
                    imgDriver.getLayoutParams().width = finalWidth;
//                    imgDriver.setImageBitmap(ImageHelper2.decodeSampledBitmapFromFile(Path, finalWidth, finalHeight));
                    lblAddDriverLicence.setVisibility(View.INVISIBLE);
//                    driver_license_image_file = new File(ImageHelper2.getRealPathFromURI(selectedImageUri.toString(), _context));
//                imgDriver.setImageBitmap(photo);

//                imgDriver.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(this).load(selectedImageUri)
                            .into(imgDriver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TRADE_LICENCE_NUMBER) {
                Log.e("", "TRADE_LICENCE_NUMBER" + TRADE_LICENCE_NUMBER);
                setTradeSkills();
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                Path = getPath(selectedImageUri);
                imgDriver.getLayoutParams().height = finalHeight;
                imgDriver.getLayoutParams().width = finalWidth;
//                imgDriver.setImageBitmap(ImageHelper2.decodeSampledBitmapFromFile(Path, finalWidth, finalHeight));
                Picasso.with(this).load(selectedImageUri)
                        .into(imgDriver);
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
    private void setTradeSkills(){
        ArrayList<SkillTrade> skillTrades  = TradeSkillSingleTon.getInstance().getListChecked();
        txtTradeskill.setText("");
        trade_skill = "";
        if (skillTrades.size() > 0){
//            txtTradeskill.setText(skillTrades.get(0).getTitle());
            for (int i = 0 ; i < skillTrades.size() - 1 ; i++){
                if (skillTrades.get(i).isChecked())
                trade_skill = trade_skill + skillTrades.get(i).getTitle() + ", ";
            }
            if (skillTrades.get(skillTrades.size() - 1).isChecked())
            trade_skill = trade_skill + skillTrades.get(skillTrades.size() - 1).getTitle();
            txtTradeskill.setText(trade_skill);
        }
    }
    private  void showHourlyRateDialog(){
        final String[] TYPES = new String[]{"$75", "$95","$100","$125","$150"};
        final String[] TYPES_NUMERIC = new String[]{"75", "95","100","125","150"};
        final Dialog dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wheelviewdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        ImageView imgok = (ImageView) dialog.findViewById(R.id.img_ok);
        WheelView wheelView = (WheelView) dialog.findViewById(R.id.wheelView);
        wheelView.setOffset(1);
        wheelView.setSeletion(2);
        wheelView.setItems(Arrays.asList(TYPES));
        if (hourly_rate.length() == 0){
            hourly_rate = TYPES_NUMERIC[1].toString();
            txtHourlyrate.setText(TYPES[1].toString());
        }

        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                hourly_rate = TYPES_NUMERIC[selectedIndex - 1].toString();
                txtHourlyrate.setText(TYPES[selectedIndex - 1].toString());
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
    private HashMap<String,String> getProfileUpdateParameters(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api", "update");
        if (_prefs.getString(Preferences.ROLE,"").equals("pro"))
            hashMap.put("object","pros");
        else
            hashMap.put("object","technician");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("data[technicians][trade_license_number]",trade_license_number);
        hashMap.put("data[technicians][years_in_business]", years_in_business);
        hashMap.put("data[pros][ein_number]", ein_number);
        hashMap.put("data[pros][insurance]", insurance_carrier);
        hashMap.put("data[pros][insurance_policy]", policy_number);
        hashMap.put("data[pros][hourly_rate]", hourly_rate);
        ArrayList<SkillTrade> skillTradesSelected  = TradeSkillSingleTon.getInstance().getListChecked();
        for (int i = 0 ; i < skillTradesSelected.size() ; i++){

        hashMap.put("data[user_services][" + i + "]",skillTradesSelected.get(i).getId()+"");

        Log.e("","hashMap"+hashMap.size());
        }
        return hashMap;
    }
    /*Create Camra Gallery PopUP*/
    private void showCamraGalleryPopUp() {
        dialog = new Dialog(_context);
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camra_gallery);
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

    private MultipartUtility createMultiPartRequest(HashMap<String,String> hashMap){

        try {
            multipart = new MultipartUtility(Constants.BASE_URL, Constants.CHARSET);
            for ( String key : hashMap.keySet() ) {
                multipart.addFormField(key, hashMap.get(key));
//                Log.e("key"+key,"finalRequestParams.get(key)"+finalRequestParams.get(key));
            }
            if (_prefs.getString(Preferences.ROLE, "pro").equals("pro") && isEditMode){
                if (Path != null){
                    multipart.addFilePart("data[technicians][driver_license_image]",new File(Path));
                }

            }else {
                if (Path != null){
                    multipart.addFilePart("driver_license_image",new File(Path));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipart;
    }
}

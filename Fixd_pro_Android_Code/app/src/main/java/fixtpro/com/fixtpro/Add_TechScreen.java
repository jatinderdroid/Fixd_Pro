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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fixtpro.com.fixtpro.adapters.TechnicianAdapter;
import fixtpro.com.fixtpro.beans.TechnicianModal;
import fixtpro.com.fixtpro.imageupload.ImageHelper2;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.ExceptionListener;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsyncMutipart;
import fixtpro.com.fixtpro.utilites.MultipartUtility;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class Add_TechScreen extends AppCompatActivity {
    private Context _context = this;
    private String TAG = "Add_Driver_LicScreen";
    Typeface fontfamily;
    TextView txtBack, txtDone, text_add_account;
    ImageView img_add;
    String token = "",phone = "",first_name = "",last_name = "",email = "",technician_id ="";
    boolean ispickUp_jobs = false;
    SharedPreferences _prefs = null ;
    File profile_image_file = null ;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private String selectedImagePath;
    public String Path,path;
    public Uri selectedImageUri;
    ImageView imgTech =  null ;
    ArrayList<TechnicianModal> technicianModalsList = new ArrayList<TechnicianModal>();
    ListView lstTechnicians;
    public int position = -1;
    public boolean toEdit = false;
    TechnicianAdapter adapter ;
    Dialog dialog;
    CircleImageView pro_pic;
    String Error = "";
    private  boolean isEditSetting = false ;
    MultipartUtility multipart = null;
//    dilogWidgets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__tech_screen);
        _prefs = Utilities.getSharedPreferences(_context);
        token = _prefs.getString(Preferences.AUTH_TOKEN,null);
        setWidgetIDs();
        setTypeface();
        setClickListner();
        if (getIntent() != null){
            isEditSetting = getIntent().getBooleanExtra("isEdit",false) ;
        }
    }

    private void setClickListner() {

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditSetting){
                    Intent i = new Intent(getApplicationContext(), SetupCompleteScreen.class);
                    startActivity(i);
                }else {
                    finish();
                }

            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = "";
                first_name = "";
                last_name = "";
                email = "";
                technician_id = "";
                showAddTechDialog(false);
            }
        });
        lstTechnicians.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TechnicianModal technicianModal = technicianModalsList.get(position);
                first_name = technicianModal.getFirstName();
                last_name = technicianModal.getLastName();
                phone = technicianModal.getPhone();
                email = technicianModal.getEmail();
                ispickUp_jobs = technicianModal.ispickjob();
                technician_id = technicianModal.getId();
                Add_TechScreen.this.position = position;
                showAddTechDialog(true);
            }
        });
    }


    private void setWidgetIDs() {
        txtBack = (TextView) findViewById(R.id.txtBack);
        txtDone = (TextView) findViewById(R.id.txtDone);
        text_add_account = (TextView) findViewById(R.id.text_add_account);
        img_add = (ImageView) findViewById(R.id.img_add);
        lstTechnicians = (ListView)findViewById(R.id.lstTechnicians);
    }

    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
        text_add_account.setTypeface(fontfamily);

    }
    private HashMap<String,String> getAddTechParam(){
        HashMap<String,String> paramHashMap = new HashMap<String,String>();
        paramHashMap.put("object", "technician");
        paramHashMap.put("api", "register");
        paramHashMap.put("token", token);
//        if (profile_image_file  != null)
//        paramHashMap.put("profile_image",new String(Utilities.convertFileToBytes(profile_image_file)));
        paramHashMap.put("first_name", first_name);
        paramHashMap.put("last_name", last_name);
        paramHashMap.put("email", email);
        paramHashMap.put("phone", phone);
        if (ispickUp_jobs)
            paramHashMap.put("pickup_jobs", "1");
        else
            paramHashMap.put("pickup_jobs", "0");
        return paramHashMap;
    }
    private HashMap<String,String> getUpdateTechParam(){
        HashMap<String,String> paramHashMap = new HashMap<String,String>();
        paramHashMap.put("object", "technician");
        paramHashMap.put("api", "update");
        paramHashMap.put("technician_id", technician_id);
        paramHashMap.put("token", token);
        if (profile_image_file  != null)
        paramHashMap.put("profile_image",new String(Utilities.convertFileToBytes(profile_image_file)));
        paramHashMap.put("first_name", first_name);
        paramHashMap.put("last_name", last_name);
        paramHashMap.put("email", email);
        paramHashMap.put("phone", phone);
        if (ispickUp_jobs)
            paramHashMap.put("pickup_jobs", "1");
        else
            paramHashMap.put("pickup_jobs", "0");
        return paramHashMap;
    }

    private  void openCamera(){
        Intent camraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camraIntent, CAMERA_REQUEST);
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLERY_REQUEST);
    }
    private void showAlertDialog(String Title,String Message){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
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
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Log.e("photo", photo + "");
                selectedImageUri = data.getData();
                Log.e("selectedImageUri", selectedImageUri + "");
//                String PhysicalPath = ImageHelper2.getRealPathFromURI(selectedImageUri.toString(),_context);
//                String PhysicalPath = getPath(selectedImageUri);
//                Log.e(TAG,"PhysicalPath"+PhysicalPath);
//                imgTech.setImageBitmap(photo);
//                pro_pic.setImageBitmap(photo);
//                profile_image_file = new File(PhysicalPath);
                Path = ImageHelper2.compressImage(selectedImageUri, this);
                Picasso.with(this).load(selectedImageUri).resize(128, 128).centerCrop()
                        .into(imgTech);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GALLERY_REQUEST) {
            Uri selectedImageUri = data.getData();
            Path = getPath(selectedImageUri);
            Log.e(TAG,"Image Path : " + selectedImagePath);
//            pro_pic.setImageURI(selectedImageUri);
//            profile_image_file = new File(selectedImagePath);
            Picasso.with(this).load(selectedImageUri).resize(128, 128).centerCrop()
                    .into(imgTech);
        }
    }
    public String getPath(Uri uri) {
        String[] projection= { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void showAddTechDialog(final boolean isEdit){
        toEdit = isEdit;
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_tech_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        pro_pic = (CircleImageView) dialog.findViewById(R.id.pro_pic);
        ImageView img_add_tech = (ImageView) dialog.findViewById(R.id.img_add_tech);
        imgTech = pro_pic ;
        CheckBox check_id = (CheckBox) dialog.findViewById(R.id.check_id);

        final EditText txtFirstName = (EditText) dialog.findViewById(R.id.txtFirstName);
        final EditText txtLastName = (EditText) dialog.findViewById(R.id.txtLastName);
        final EditText txtPhone = (EditText) dialog.findViewById(R.id.txtPhone);
        final EditText txtEmail = (EditText) dialog.findViewById(R.id.txtEmail);
        txtFirstName.setText(first_name);
        txtLastName.setText(last_name);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        check_id.setChecked(ispickUp_jobs);
        TextView check_text = (TextView) dialog.findViewById(R.id.check_text);

        txtFirstName.setTypeface(fontfamily);
        txtLastName.setTypeface(fontfamily);
        txtPhone.setTypeface(fontfamily);
        txtEmail.setTypeface(fontfamily);
        check_text.setTypeface(fontfamily);


        img_add_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                first_name = txtFirstName.getText().toString().trim();
                last_name = txtLastName.getText().toString().trim();
                phone = txtPhone.getText().toString().trim();
                email = txtEmail.getText().toString().trim();
                if (first_name.length() == 0) {
                    showAlertDialog("Fixd-Pro","Please enter first name.");
                } else if (last_name.length() == 0) {
                    showAlertDialog("Fixd-Pro","Please enter last name.");
                } else if (phone.length() == 0) {
                    showAlertDialog("Fixd-Pro","Please enter phone number.");
                } else if (phone.length() < 10) {
                    showAlertDialog("Fixd-Pro", "Your phone number seems to invalid, Please try again.");
                } else if (email.length() == 0) {
                    showAlertDialog("Fixd-Pro","Please enter email.");
                } else if (!Utilities.isValidEmail(email)) {
                    showAlertDialog("Fixd-Pro", "Your email address seems to invalid, Please try again.");
                } else {
                    Utilities.hideKeyBoad(_context, Add_TechScreen.this.getCurrentFocus());
                    if (!isEdit) {
                          addTech();
                    } else {
                          updateTech();
                    }
                }

            }
        });
        pro_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
                //openCamera();
            }
        });
        check_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ispickUp_jobs = isChecked;
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
          Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS")){
                    TechnicianModal technicianModal = new TechnicianModal();
                    JSONObject RESPONSE = Response.getJSONObject("RESPONSE");
                    String id = RESPONSE.getString("id");
                    String email = RESPONSE.getString("email");
                    String phone = RESPONSE.getString("phone");
                    JSONObject technicians = RESPONSE.getJSONObject("technicians");
                    String fname = technicians.getString("first_name");
                    String lname = technicians.getString("last_name");
                    String pick_up = technicians.getString("pickup_jobs");
                    technicianModal.setId(id);
                    technicianModal.setEmail(email);
                    technicianModal.setPhone(phone);
                    technicianModal.setFirstName(fname);
                    technicianModal.setLastName(lname);
                    if (pick_up.equals("1")){
                        technicianModal.setIspickjob(true);
                    }else{
                        technicianModal.setIspickjob(false);
                    }
                    if (toEdit){
                        technicianModalsList.remove(position);
                        technicianModalsList.add(position,technicianModal);
                        handler.sendEmptyMessage(1);
                    }else{
                        technicianModalsList.add(technicianModal);
                        handler.sendEmptyMessage(0);
                    }
                }else{
                    JSONObject ERRORS = Response.getJSONObject("ERRORS");
                    Iterator<String> keys2 = ERRORS.keys();
                    if (keys2.hasNext())
                    Error = ERRORS.getString(keys2.next());
                    handler.sendEmptyMessage(2);
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
//                    set ADAPTER
                    dialog.dismiss();
                    adapter = new TechnicianAdapter(Add_TechScreen.this,technicianModalsList,getResources());
                    lstTechnicians.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                }
                case 1:{
//                    noty
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    break;
                }
                case 2:{
                    dialog.dismiss();
                    showAlertDialog("Fixd-Pro", Error);
                    break;
                }
                case 500: {
                    dialog.dismiss();
                    showAlertDialog("Fixd-pro", "Server Error 500");
                    break;
                }
            }
        }
    };

    /*Create Camra Gallery PopUP*/
    private void showCamraGalleryPopUp(){
        dialog = new Dialog(_context);
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camra_gallery);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView)dialog.findViewById(R.id.img_close);
        TextView txtTakePicture = (TextView)dialog.findViewById(R.id.txtTakePicture);
        TextView txtCamera = (TextView)dialog.findViewById(R.id.txtCamera);
        TextView txtGallery = (TextView)dialog.findViewById(R.id.txtGallery);
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
                Log.e(""+key,""+hashMap.get(key));
            }
            if (Path != null){
                multipart.addFilePart("profile_image",new File(Path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipart;
    }
    private void updateTech(){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                createMultiPartRequest(getUpdateTechParam());
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, responseListener,exceptionListener, Add_TechScreen.this, "Updating");
                getApiResponseAsync.execute();
            }
        }.execute();
    }


    private void addTech(){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                createMultiPartRequest(getAddTechParam());
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, responseListener,exceptionListener, Add_TechScreen.this, "Adding");
                getApiResponseAsync.execute();
            }
        }.execute();
    }
    ExceptionListener exceptionListener = new ExceptionListener(){

        @Override
        public void handleException(int exceptionStatus) {
            handler.sendEmptyMessage(exceptionStatus);
        }
    };
}

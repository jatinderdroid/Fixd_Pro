package fixtpro.com.fixtpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import fixtpro.com.fixtpro.imageupload.ImageHelper2;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.ExceptionListener;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsyncMutipart;
import fixtpro.com.fixtpro.utilites.MultipartUtility;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class UserProfileScreen extends AppCompatActivity {
    TextView txtBack, txtDone, lblAddPhoto;
    CircleImageView profile_pic;
    EditText txtFirstName, txtLastName, txtPhone, txtEmail, txtCompany, txtAddress, txtCity, txtZip;
    String firstname = "", lastname = "", phone = "", email = "", company = "", address = "", city = "", zip = "";
    fixtpro.com.fixtpro.views.CircularImageView imgAddPhoto;
    Context _context = this;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    public String selectedImagePath=null;
    public String Path, path;
    public Uri selectedImageUri;
    private Typeface fontfamily;
    int finalHeight, finalWidth = 200;
    HashMap<String, String> finalRequestParams = null;
    File profileImageFile = null;
    private boolean isEditMode = false;
    SharedPreferences _prefs = null;
    String error_message = "";

    private Dialog dialog;
    private boolean isPro = true;
    MultipartUtility multipart = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_screen);
        _prefs = Utilities.getSharedPreferences(_context);
        setWidgets();
        if (_prefs.getBoolean(Preferences.ISLOGIN, false)) {
            isEditMode = true;

        } else {
            if (getIntent() != null) {
                finalRequestParams = (HashMap<String, String>) getIntent().getSerializableExtra("finalRequestParams");
            }
        }
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        setListeners();
        setTypeface();
        intLayout();

    }

    private void intLayout() {
        txtFirstName.setText(_prefs.getString(Preferences.FIRST_NAME, ""));
        txtLastName.setText(_prefs.getString(Preferences.LAST_NAME, ""));
        txtCompany.setText(_prefs.getString(Preferences.COMPANY_NAME, ""));
//        When technician Update Profile Screen
        if (!_prefs.getString(Preferences.ROLE, "pro").equals("pro") && isEditMode) {
            txtPhone.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
            txtCity.setVisibility(View.GONE);
            txtZip.setVisibility(View.GONE);
            txtCompany.setVisibility(View.GONE);
        }
//        When Technician Registers
        if(!_prefs.getString(Preferences.ROLE, "pro").equals("pro") && !isEditMode){
            txtAddress.setVisibility(View.GONE);
            txtCity.setVisibility(View.GONE);
            txtZip.setVisibility(View.GONE);
            txtCompany.setVisibility(View.GONE);
            txtPhone.setVisibility(View.GONE);
            txtEmail.setEnabled(false);
            if (_prefs.getString(Preferences.EMAIL,"").length() > 0){
                isPro = false ;
            }
        }

        txtPhone.setText(_prefs.getString(Preferences.PHONE, ""));
        txtEmail.setText(_prefs.getString(Preferences.EMAIL, ""));
        txtAddress.setText(_prefs.getString(Preferences.ADDRESS, ""));
        txtCity.setText(_prefs.getString(Preferences.CITY, ""));
        txtZip.setText(_prefs.getString(Preferences.ZIP, ""));
        Log.e("","profile_pic"+profile_pic);
        if (_prefs.getString(Preferences.PROFILE_IMAGE,null) != null){
            Target loadtarget = null;
            if (loadtarget == null) loadtarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // do something with the Bitmap
                    profile_pic.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    profile_pic.setImageResource(R.drawable.addphoto_img);
                    Log.e("","Error");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }

            };

            Picasso.with(this).load(_prefs.getString(Preferences.PROFILE_IMAGE,null)).into(loadtarget);
        }


    }

    private void setWidgets() {
        txtBack = (TextView) findViewById(R.id.txtBack);
        txtDone = (TextView) findViewById(R.id.txtDone);
        lblAddPhoto = (TextView) findViewById(R.id.lblChangePhoto);

        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtCompany = (EditText) findViewById(R.id.txtCompany);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtCity = (EditText) findViewById(R.id.txtCity);
        txtZip = (EditText) findViewById(R.id.txtZip);
        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
        txtFirstName.requestFocus();
//        imgAddPhoto = (fixtpro.com.fixtpro.views.CircularImageView)findViewById(R.id.imgAddPhoto);
//        imgAddPhoto.setImageDrawable(getResources().getDrawable(R.drawable.addphoto_img));
    }

    private void setTypeface() {
        lblAddPhoto.setTypeface(fontfamily);
        txtFirstName.setTypeface(fontfamily);
        txtPhone.setTypeface(fontfamily);
        txtEmail.setTypeface(fontfamily);
        txtCompany.setTypeface(fontfamily);
        txtAddress.setTypeface(fontfamily);
        txtCity.setTypeface(fontfamily);
        txtZip.setTypeface(fontfamily);
        txtLastName.setTypeface(fontfamily);
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
    }

    private void setListeners() {
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
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = txtFirstName.getText().toString().trim();
                lastname = txtLastName.getText().toString().trim();
                phone = txtPhone.getText().toString().trim();
                email = txtEmail.getText().toString().trim();
                company = txtCompany.getText().toString().trim();
                address = txtAddress.getText().toString().trim();
                city = txtCity.getText().toString().trim();
                zip = txtZip.getText().toString().trim();

                if (firstname.length() == 0) {
                    showAlertDialog("Fixd-Pro", "Please enter the first name.");
                    return;
                } else if (lastname.length() == 0) {
                    showAlertDialog("Fixd-Pro", "Please enter the last name.");
                    return;
                } else if (email.length() == 0) {
                    showAlertDialog("Fixd-Pro", "Please enter the email address.");
                    return;
                } else if (!Utilities.isValidEmail(email)) {
                    showAlertDialog("Fixd-Pro", "Your email address seems to invalid, Please try again.");
                    return;
                } else if (company.length() == 0 && (txtCompany.getVisibility() == View.VISIBLE)) {
                    showAlertDialog("Fixd-Pro", "Please enter the company name.");
                    return;
                } else if (address.length() == 0 && (txtAddress.getVisibility() == View.VISIBLE)) {
                    showAlertDialog("Fixd-Pro", "Please enter the address.");
                    return;
                } else if (city.length() == 0 && (txtCity.getVisibility() == View.VISIBLE)) {
                    showAlertDialog("Fixd-Pro", "Please enter the city.");
                    return;
                } else if (zip.length() == 0 && (txtZip.getVisibility() == View.VISIBLE)) {
                    showAlertDialog("Fixd-Pro", "Please enter the zip code.");
                    return;
                } else if (false && !isEditMode) {
                    showAlertDialog("Fixd-Pro", "Zip code length.");
                    return;
                } else {
//                  do it
                    Utilities.hideKeyBoad(_context, UserProfileScreen.this.getCurrentFocus());
                    if (!isEditMode) {
                        if (isPro) {
                            GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", checkIfEmailExistsListener, UserProfileScreen.this, "Loading");
                            responseAsync.execute(getCheckEmailRequestParams());
                        } else {
                            handler.sendEmptyMessage(0);
                        }

                    } else {
//                        is in edit mode
//                        GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", profileUpdateListener, UserProfileScreen.this, "Loading");
//                        responseAsync.execute(getProfileUpdateParameters());
                        new AsyncTask<Void, Void, String>() {

                            @Override
                            protected String doInBackground(Void... params) {
                                createMultiPartRequest(getProfileUpdateParameters());
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, profileUpdateListener,exceptionListener, UserProfileScreen.this, "Updating");
                                getApiResponseAsync.execute();
                            }
                        }.execute();
                    }

//                    Intent intent = new Intent(_context,Add_Driver_LicScreen.class);
//                    intent.putExtra("finalRequestParams",finalRequestParams);
//                    startActivity(intent);
                }


            }
        });


        txtZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_context, WorkingRadiusActivity.class);
                startActivity(i);
            }
        });

    }
    private MultipartUtility createMultiPartRequest(HashMap<String,String> hashMap){

        try {
            multipart = new MultipartUtility(Constants.BASE_URL, Constants.CHARSET);
            for ( String key : hashMap.keySet() ) {
                multipart.addFormField(key, hashMap.get(key));
//                Log.e("key"+key,"finalRequestParams.get(key)"+finalRequestParams.get(key));
            }

                if (selectedImagePath != null){
                    multipart.addFilePart("data[technicians][profile_image]",new File(selectedImagePath));
                }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipart;
    }
    ResponseListener profileUpdateListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    SharedPreferences.Editor editor = _prefs.edit();
                    editor.putString(Preferences.FIRST_NAME, firstname);
                    editor.putString(Preferences.LAST_NAME, lastname);
                    editor.putString(Preferences.EMAIL, email);
                    editor.putString(Preferences.PHONE, phone);
                    editor.putString(Preferences.CITY, city);
                    editor.putString(Preferences.ZIP, zip);
                    editor.putString(Preferences.ADDRESS, address);
                    editor.putString(Preferences.COMPANY_NAME, company);
                    JSONObject profile_image = null ;
                    profile_image = Response.getJSONObject("RESPONSE").getJSONObject("technicians").getJSONObject("profile_image");
                    if (!profile_image.isNull("original")){
                        String image_original =  profile_image.getString("original");
                        editor.putString(Preferences.PROFILE_IMAGE, image_original);
                    }
                    editor.commit();
                    finish();
                } else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
                        error_message = errors.getString(key);
                        handler.sendEmptyMessage(2);
                    }
                }

            } catch (JSONException e) {

            }
        }
    };
    ExceptionListener exceptionListener = new ExceptionListener(){

        @Override
        public void handleException(int exceptionStatus) {
            handler.sendEmptyMessage(exceptionStatus);
        }
    };
    ResponseListener checkIfEmailExistsListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "Response" + response.toString());
            try {
                String status = response.getString("STATUS");
                if (status.equals("SUCCESS")) {
                    JSONArray Data = response.getJSONArray("RESPONSE");
//                    Check if Phone Already Exist
                    if (Data.length() == 0) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
//              Check for Errors
                    JSONObject errors = response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
                        error_message = errors.getString(key);
                        handler.sendEmptyMessage(2);
                    }
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
                    if (!isEditMode) {
                        if (isPro){
                            Intent intent = new Intent(_context, Add_Driver_LicScreen.class);
                            finalRequestParams.put("data[technicians][first_name]", firstname);
                            finalRequestParams.put("data[technicians][last_name]", lastname);
                            finalRequestParams.put("data[email]", email);
                            finalRequestParams.put("data[pros][company_name]", company);
                            finalRequestParams.put("data[pros][address]", address);
                            finalRequestParams.put("data[pros][city]", city);
                            finalRequestParams.put("data[pros][zip]", zip);
                            intent.putExtra("finalRequestParams", finalRequestParams);
                            intent.putExtra("image_profile", selectedImagePath);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(_context, Add_Driver_LicScreen.class);
                            finalRequestParams.put("first_name", firstname);
                            finalRequestParams.put("last_name", lastname);
                            finalRequestParams.put("email", email);
                            intent.putExtra("finalRequestParams", finalRequestParams);
                            intent.putExtra("image_profile", selectedImagePath);
                            startActivity(intent);
                        }

                    } else {
                        finish();
                    }
                    break;
                }
                case 1: {
                    showAlertDialog("Fixd-Pro", "Email already exist.");
                    break;
                }
                case 500:{
                    showAlertDialog("Fixd-Pro", "Server Error 500.");
                }
                default: {
                    break;
                }
            }
        }
    };

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

    private void openCamera() {
        Intent camraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    private HashMap<String, String> getCheckEmailRequestParams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "read");
        hashMap.put("object", "users");
        hashMap.put("select", "id");
        hashMap.put("where[email]", email);
        return hashMap;
    }

    private HashMap<String, String> getProfileUpdateParameters() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "update");
        if (_prefs.getString(Preferences.ROLE, "").equals("pro"))
            hashMap.put("object", "pros");
        else
            hashMap.put("object", "technician");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[technicians][first_name]", firstname);
        hashMap.put("data[technicians][last_name]", lastname);
        hashMap.put("data[email]", email);
        hashMap.put("data[pros][company_name]", company);
        hashMap.put("data[pros][address]", address);
        hashMap.put("data[pros][city]", city);
        hashMap.put("data[pros][zip]", zip);
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

}

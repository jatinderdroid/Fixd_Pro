package fixtpro.com.fixtpro.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;


import fixtpro.com.fixtpro.AddBankAccountActivity;
import fixtpro.com.fixtpro.Add_Driver_LicScreen;
import fixtpro.com.fixtpro.Add_TechScreen;
import fixtpro.com.fixtpro.ChangePassword;
import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.ResponseListener;
import fixtpro.com.fixtpro.UserProfileScreen;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;
import fixtpro.com.fixtpro.views.SwitchButton;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }
    LinearLayout layout_change_password, layout_edit_profile, layout_edit_comp_info, layout_edit_bank_info, layout_edit_tech ;
    SwitchButton swhLocation, swhTextMessaging ;
    ImageView img_available_jobs_email, img_available_jobs_phone, img_job_won_email, img_job_won_phone,
              img_job_lost_email, img_job_lost_phone, img_job_reschduled_email, img_job_reschduled_phone,
              img_job_cancelled_email, img_job_cancelled_phone;

    View bank_account_divider, edit_tech_divider;

    SharedPreferences _prefs  = null ;

    SwitchButton sb_custom_miui;

    String userRole  = "";
    String authToken  = "";
    String notif_AvlJobs = "none", notif_JobWon = "none", notif_JobLost = "none",
           notif_JobResch = "none", notif_JobCancel = "none", loc_Settings = "0",
           text_Settings = "0";
    Context _context ;
    String error_message =  "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getActivity() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View roootView  = inflater.inflate(R.layout.fragment_settings, container, false);
        _prefs = Utilities.getSharedPreferences(_context);
        userRole  = _prefs.getString(Preferences.ROLE, null);
        authToken  = _prefs.getString(Preferences.AUTH_TOKEN, null);
        setWidgets(roootView);
        setListeners();
        initSettings();
        return roootView;
    }

    private  void  setWidgets(View view){
        layout_change_password  =  (LinearLayout) view.findViewById(R.id.layout_change_password);
        layout_edit_profile  =  (LinearLayout) view.findViewById(R.id.layout_edit_profile);
        layout_edit_comp_info  =  (LinearLayout) view.findViewById(R.id.layout_edit_comp_info);
        layout_edit_bank_info  =  (LinearLayout) view.findViewById(R.id.layout_edit_bank_info);
        layout_edit_tech =  (LinearLayout) view.findViewById(R.id.layout_edit_tech);
        bank_account_divider = (View)view.findViewById(R.id.bank_account_divider);
        edit_tech_divider = (View)view.findViewById(R.id.edit_tech_divider);
        if (!_prefs.getString(Preferences.ROLE,"").equals("pro")){
            layout_edit_bank_info.setVisibility(View.GONE);
            bank_account_divider.setVisibility(View.GONE);
            layout_edit_tech.setVisibility(View.GONE);
            edit_tech_divider.setVisibility(View.GONE);
        }
        swhLocation  = (SwitchButton)view.findViewById(R.id.swhLocation);
        swhTextMessaging  = (SwitchButton)view.findViewById(R.id.swhTextMessaging);

        img_available_jobs_email = (ImageView)view.findViewById(R.id.img_available_jobs_email);
        img_available_jobs_phone = (ImageView)view.findViewById(R.id.img_available_jobs_phone);
        img_job_won_email = (ImageView)view.findViewById(R.id.img_job_won_email);
        img_job_won_phone = (ImageView)view.findViewById(R.id.img_job_won_phone);
        img_job_lost_email = (ImageView)view.findViewById(R.id.img_job_lost_email);
        img_job_lost_phone = (ImageView)view.findViewById(R.id.img_job_lost_phone);
        img_job_reschduled_email = (ImageView)view.findViewById(R.id.img_job_reschduled_email);
        img_job_reschduled_phone = (ImageView)view.findViewById(R.id.img_job_reschduled_phone);
        img_job_cancelled_email = (ImageView)view.findViewById(R.id.img_job_cancelled_email);
        img_job_cancelled_phone = (ImageView)view.findViewById(R.id.img_job_cancelled_phone);
    }

    private  void initSettings(){
        notif_AvlJobs  = _prefs.getString(Preferences.AVAILABLE_JOBS_NOTIFICATION,"none");
        notif_JobWon  = _prefs.getString(Preferences.JOB_WON_NOTIFICATION,"none");
        notif_JobLost  = _prefs.getString(Preferences.JOB_LOST_NOTIFICATION,"none");
        notif_JobResch = _prefs.getString(Preferences.JOB_RESECHDULED,"none");
        notif_JobCancel = _prefs.getString(Preferences.JOB_CANCELLED,"none");
        loc_Settings = _prefs.getString(Preferences.LOCATION_SERVICE,"0");
        text_Settings = _prefs.getString(Preferences.TEXT_MESSAGING,"0");
        setLayout();
    }
    private  void setLayout(){
        if (notif_AvlJobs.equals("none")){
            img_available_jobs_email.setImageResource(R.drawable.white_radiobutton);
            img_available_jobs_phone.setImageResource(R.drawable.white_radiobutton);
        }else  if (notif_AvlJobs.equals("phone")){
            img_available_jobs_email.setImageResource(R.drawable.white_radiobutton);
            img_available_jobs_phone.setImageResource(R.drawable.radiobutton);
        }else  if (notif_AvlJobs.equals("email")){
            img_available_jobs_email.setImageResource(R.drawable.radiobutton);
            img_available_jobs_phone.setImageResource(R.drawable.white_radiobutton);
        }else{
            img_available_jobs_email.setImageResource(R.drawable.radiobutton);
            img_available_jobs_phone.setImageResource(R.drawable.radiobutton);
        }

        if (notif_JobWon.equals("none")){
            img_job_won_email.setImageResource(R.drawable.white_radiobutton);
            img_job_won_phone.setImageResource(R.drawable.white_radiobutton);
        }else  if (notif_JobWon.equals("phone")){
            img_job_won_email.setImageResource(R.drawable.white_radiobutton);
            img_job_won_phone.setImageResource(R.drawable.radiobutton);
        }else  if (notif_JobWon.equals("email")){
            img_job_won_email.setImageResource(R.drawable.radiobutton);
            img_job_won_phone.setImageResource(R.drawable.white_radiobutton);
        }else{
            img_job_won_email.setImageResource(R.drawable.radiobutton);
            img_job_won_phone.setImageResource(R.drawable.radiobutton);
        }

        if (notif_JobLost.equals("none")){
            img_job_lost_email.setImageResource(R.drawable.white_radiobutton);
            img_job_lost_phone.setImageResource(R.drawable.white_radiobutton);
        }else  if (notif_JobLost.equals("phone")){
            img_job_lost_email.setImageResource(R.drawable.white_radiobutton);
            img_job_lost_phone.setImageResource(R.drawable.radiobutton);
        }else  if (notif_JobLost.equals("email")){
            img_job_lost_email.setImageResource(R.drawable.radiobutton);
            img_job_lost_phone.setImageResource(R.drawable.white_radiobutton);
        }else{
            img_job_lost_email.setImageResource(R.drawable.radiobutton);
            img_job_lost_phone.setImageResource(R.drawable.radiobutton);
        }

        if (notif_JobResch.equals("none")){
            img_job_reschduled_email.setImageResource(R.drawable.white_radiobutton);
            img_job_reschduled_phone.setImageResource(R.drawable.white_radiobutton);
        }else  if (notif_JobResch.equals("phone")){
            img_job_reschduled_email.setImageResource(R.drawable.white_radiobutton);
            img_job_reschduled_phone.setImageResource(R.drawable.radiobutton);
        }else  if (notif_JobResch.equals("email")){
            img_job_reschduled_email.setImageResource(R.drawable.radiobutton);
            img_job_reschduled_phone.setImageResource(R.drawable.white_radiobutton);
        }else{
            img_job_reschduled_email.setImageResource(R.drawable.radiobutton);
            img_job_reschduled_phone.setImageResource(R.drawable.radiobutton);
        }

        if (notif_JobCancel.equals("none")){
            img_job_cancelled_email.setImageResource(R.drawable.white_radiobutton);
            img_job_cancelled_phone.setImageResource(R.drawable.white_radiobutton);
        }else  if (notif_JobCancel.equals("phone")){
            img_job_cancelled_email.setImageResource(R.drawable.white_radiobutton);
            img_job_cancelled_phone.setImageResource(R.drawable.radiobutton);
        }else  if (notif_JobCancel.equals("email")){
            img_job_cancelled_email.setImageResource(R.drawable.radiobutton);
            img_job_cancelled_phone.setImageResource(R.drawable.white_radiobutton);
        }else{
            img_job_cancelled_email.setImageResource(R.drawable.radiobutton);
            img_job_cancelled_phone.setImageResource(R.drawable.radiobutton);
        }
        if (loc_Settings.equals("0")){
            swhLocation.setChecked(false);
        }else {
            swhLocation.setChecked(true);
        }

        if (text_Settings.equals("0")){
            swhTextMessaging.setChecked(false);
        }else {
            swhTextMessaging.setChecked(true);
        }
    }
    private  void setListeners(){
        layout_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });
        layout_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(getActivity(), UserProfileScreen.class);
                startActivity(intent);
            }
        });
        layout_edit_comp_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), Add_Driver_LicScreen.class);
                startActivity(intent);

            }
        });
        layout_edit_bank_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), AddBankAccountActivity.class);
                startActivity(intent);

            }
        });

        swhLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    loc_Settings = "1";
                else
                    loc_Settings = "0";
                saveSettings();
            }
        });
        swhTextMessaging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    text_Settings = "1";
                else
                    text_Settings = "0";
                saveSettings();
            }
        });
        layout_edit_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Add_TechScreen.class);
                intent.putExtra("isEdit",true);
                startActivity(intent);
            }
        });
        img_available_jobs_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_AvlJobs.equals("none")){
                    notif_AvlJobs = "email" ;
                }else if (notif_AvlJobs.equals("both")){
                    notif_AvlJobs = "phone" ;
                }else if (notif_AvlJobs.equals("phone")){
                    notif_AvlJobs = "both" ;
                }else {
                    notif_AvlJobs = "none" ;
                }
                saveSettings();
            }
        });img_available_jobs_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_AvlJobs.equals("none")){
                    notif_AvlJobs = "phone" ;
                }else if (notif_AvlJobs.equals("both")){
                    notif_AvlJobs = "email" ;
                }else if (notif_AvlJobs.equals("email")){
                    notif_AvlJobs = "both" ;
                }else {
                    notif_AvlJobs = "none" ;
                }
                saveSettings();
            }
        });img_job_won_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobWon.equals("none")){
                    notif_JobWon = "email" ;
                }else if (notif_JobWon.equals("both")){
                    notif_JobWon = "phone" ;
                }else if (notif_JobWon.equals("phone")){
                    notif_JobWon = "both" ;
                }else {
                    notif_JobWon = "none" ;
                }
                saveSettings();
            }
        });img_job_won_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobWon.equals("none")){
                    notif_JobWon = "phone" ;
                }else if (notif_JobWon.equals("both")){
                    notif_JobWon = "email" ;
                }else if (notif_JobWon.equals("email")){
                    notif_JobWon = "both" ;
                }else {
                    notif_JobWon = "none" ;
                }
                saveSettings();
            }
        });img_job_lost_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobLost.equals("none")){
                    notif_JobLost = "email" ;
                }else if (notif_JobLost.equals("both")){
                    notif_JobLost = "phone" ;
                }else if (notif_JobLost.equals("phone")){
                    notif_JobLost = "both" ;
                }else {
                    notif_JobLost = "none" ;
                }
                saveSettings();
            }
        });img_job_lost_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobLost.equals("none")){
                    notif_JobLost = "phone" ;
                }else if (notif_JobLost.equals("both")){
                    notif_JobLost = "email" ;
                }else if (notif_JobLost.equals("email")){
                    notif_JobLost = "both" ;
                }else {
                    notif_JobLost = "none" ;
                }
                saveSettings();
            }
        });img_job_reschduled_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobResch.equals("none")){
                    notif_JobResch = "email" ;
                }else if (notif_JobResch.equals("both")){
                    notif_JobResch = "phone" ;
                }else if (notif_JobResch.equals("phone")){
                    notif_JobResch = "both" ;
                }else {
                    notif_JobResch = "none" ;
                }
                saveSettings();
            }
        });img_job_reschduled_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobResch.equals("none")){
                    notif_JobResch = "phone" ;
                }else if (notif_JobResch.equals("both")){
                    notif_JobResch = "email" ;
                }else if (notif_JobResch.equals("email")){
                    notif_JobResch = "both" ;
                }else {
                    notif_JobResch = "none" ;
                }
                saveSettings();
            }
        });img_job_cancelled_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobCancel.equals("none")){
                    notif_JobCancel = "email" ;
                }else if (notif_JobCancel.equals("both")){
                    notif_JobCancel = "phone" ;
                }else if (notif_JobCancel.equals("phone")){
                    notif_JobCancel = "both" ;
                }else {
                    notif_JobCancel = "none" ;
                }
                Log.e("going",notif_JobCancel);
                saveSettings();
            }
        });img_job_cancelled_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif_JobCancel.equals("none")){
                    notif_JobCancel = "phone" ;
                }else if (notif_JobCancel.equals("both")){
                    notif_JobCancel = "email" ;
                }else if (notif_JobCancel.equals("email")){
                    notif_JobCancel = "both" ;
                }else {
                    notif_JobCancel = "none" ;
                }
                Log.e("going",notif_JobCancel);
                saveSettings();
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private HashMap<String,String> getSettingParameters(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api", "update");
        hashMap.put("object", "pro_settings");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[text_messaging]", text_Settings);
        hashMap.put("data[location_services]", loc_Settings);
        hashMap.put("data[job_canceled]", notif_JobCancel);
        hashMap.put("data[job_rescheduled]", notif_JobResch);
        hashMap.put("data[job_lost_notification]", notif_JobLost);
        hashMap.put("data[job_won_notification]", notif_JobWon);
        hashMap.put("data[available_jobs_notification]", notif_AvlJobs);
        return hashMap;
    }
    private void saveSettings(){
        GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerSettings, getActivity(), "Loading");
        responseAsync.execute(getSettingParameters());
    }
    ResponseListener responseListenerSettings = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS")){
                    JSONObject pro_settings = Response.getJSONObject("RESPONSE");
                    SharedPreferences.Editor editor = _prefs.edit();
                    editor.putString(Preferences.SETTING_ID, pro_settings.getString("id"));
                    editor.putString(Preferences.LOCATION_SERVICE, pro_settings.getString("location_services"));
                    editor.putString(Preferences.TEXT_MESSAGING, pro_settings.getString("text_messaging"));
                    editor.putString(Preferences.AVAILABLE_JOBS_NOTIFICATION, pro_settings.getString("available_jobs_notification"));
                    editor.putString(Preferences.JOB_WON_NOTIFICATION, pro_settings.getString("job_won_notification"));
                    editor.putString(Preferences.JOB_LOST_NOTIFICATION, pro_settings.getString("job_lost_notification") );
                    editor.putString(Preferences.JOB_RESECHDULED, pro_settings.getString("job_rescheduled"));
                    editor.putString(Preferences.JOB_CANCELLED, pro_settings.getString("job_canceled"));

                    editor.commit();
                    handler.sendEmptyMessage(0);
                }else{
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
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
                    initSettings();
                    break;
                }
                case 1:{
//                    initSettings();
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
                default:{
                    break;
                }
            }
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
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.SETTING_FRAGMENT);
        setupToolBar();
    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).hideRight();
        ((HomeScreenNew)getActivity()).setTitletext("Settings");
        ((HomeScreenNew)getActivity()).setLeftToolBarImage(R.drawable.menu_icon);
    }
}

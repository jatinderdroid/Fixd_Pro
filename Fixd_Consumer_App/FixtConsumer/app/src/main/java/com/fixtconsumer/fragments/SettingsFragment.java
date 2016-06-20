package com.fixtconsumer.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.activities.LoginSignUpActivity;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;
import com.fixtconsumer.views.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView txtMyAccount,txtPassword,txtMyAddresses,txtPayment,txtLocationService,txtMessaging,txtNotification,txtSignOut;
    ImageView imgMyAccountNext,imgPasswordNext,imgMyAddressesNext,imgPaymentNext;
    RelativeLayout layout_MyAccount,layout_Password,layout_MyAddresses,layout_Payment,layout_LocationServices,layout_TextMessaging,layout_Notification;
    SwitchButton switchLocationService,switchMessaging,switchNotification;
    Typeface fontfamily;
    SharedPreferences _prefs =null ;
    Context _context = null ;
    String notifications = "0",location_services = "0",text_messaging = "0";
    String error_message = "";
    CheckAlertDialog checkALert;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);
        notifications = _prefs.getString(Preferences.SETTINGS_NOTIFICATION, "0");
        text_messaging = _prefs.getString(Preferences.SETTINGS_TEXT_MESSAGING,"0");
        location_services = _prefs.getString(Preferences.SETTINGS_LOCATION_SERVICE,"0");
        checkALert = new CheckAlertDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        setWidgetsIDs(rootView);
        setTypeface();
        intSettings();
        setListeners();
        return rootView;
    }
    private void intSettings(){
        if (notifications.equals("1"))
            switchNotification.setChecked(true);
        if (text_messaging.equals("1"))
            switchMessaging.setChecked(true);
        if (location_services.equals("1"))
            switchLocationService.setChecked(true);
    }
    private void setListeners(){
        switchLocationService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    location_services = "1";
                else
                    location_services = "0";
                sendSettingsRequest();
            }
        });
        switchMessaging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    text_messaging = "1";
                else
                    text_messaging = "0";
                sendSettingsRequest();
            }
        });
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("","");
                if (isChecked)
                    notifications = "1";
                else
                    notifications = "0";
                sendSettingsRequest();
            }
        });
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcheckAlert(getActivity(), "SIGN OUT", "Are you sure you want to Sign Out?");
            }
        });
        layout_MyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).switchFragment(new MyAccountFragment(), Constants.My_ACCOUNT_FRAGMENT, true, null);
            }
        });
        layout_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).switchFragment(new ChangePasswordFragment(),Constants.CHANGE_PASSWORD_FRAGMENT,true,null);
            }
        });
        layout_MyAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).switchFragment(new SettingAddressFragment(), Constants.SETTING_ADDRESS_FRAGMENT, true, null);
            }
        });
        layout_Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).switchFragment(new SettingsAddCardFragment(), Constants.SETTING_ADD_CARD_FRAGMENT, true, null);
            }
        });
    }
    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");

        txtMyAccount.setTypeface(fontfamily);
        txtPassword.setTypeface(fontfamily);
        txtMyAddresses.setTypeface(fontfamily);
        txtPayment.setTypeface(fontfamily);
        txtLocationService.setTypeface(fontfamily);
        txtMessaging.setTypeface(fontfamily);
        txtNotification.setTypeface(fontfamily);
    }

    private void setWidgetsIDs(View v) {
        //txtMyAccount,txtPassword,txtMyAddresses,txtPayment,txtLocationService,txtMessaging,txtNotification;
        txtMyAccount = (TextView)v.findViewById(R.id.txtMyAccount);
        txtPassword = (TextView)v.findViewById(R.id.txtPassword);
        txtMyAddresses = (TextView)v.findViewById(R.id.txtMyAddresses);
        txtPayment = (TextView)v.findViewById(R.id.txtPayment);
        txtLocationService = (TextView)v.findViewById(R.id.txtLocationService);
        txtMessaging = (TextView)v.findViewById(R.id.txtMessaging);
        txtNotification = (TextView)v.findViewById(R.id.txtNotification);
        txtSignOut = (TextView)v.findViewById(R.id.txtSignOut);
        layout_MyAccount = (RelativeLayout)v.findViewById(R.id.layout_MyAccount);
        layout_Password = (RelativeLayout)v.findViewById(R.id.layout_Password);
        layout_MyAddresses = (RelativeLayout)v.findViewById(R.id.layout_MyAddresses);
        layout_Payment = (RelativeLayout)v.findViewById(R.id.layout_Payment);
        layout_LocationServices = (RelativeLayout)v.findViewById(R.id.layout_LocationServices);
        layout_TextMessaging = (RelativeLayout)v.findViewById(R.id.layout_TextMessaging);
        layout_Notification = (RelativeLayout)v.findViewById(R.id.layout_Notification);
        switchLocationService = (SwitchButton)v.findViewById(R.id.switchLocationService);
        switchMessaging = (SwitchButton)v.findViewById(R.id.switchMessaging);
        switchNotification = (SwitchButton)v.findViewById(R.id.switchNotification);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private HashMap<String,String> getSettingsParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","update");
        hashMap.put("object", "customer_settings");
        hashMap.put("data[location_services]", location_services);
        hashMap.put("data[text_messaging]", text_messaging);
        hashMap.put("data[notifications]", notifications);
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
    private void sendSettingsRequest(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",settingsResponseListener,settingsExceptionListener,getActivity(),"Processing.");
        apiResponseAsync.execute(getSettingsParams());
    }
    IHttpResponseListener settingsResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("","response"+response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONObject RESPONSE = response.getJSONObject("RESPONSE");
                    notifications = RESPONSE.getString("notifications");
                    text_messaging = RESPONSE.getString("text_messaging");
                    location_services = RESPONSE.getString("location_services");
                    SharedPreferences.Editor editor = _prefs.edit();
                    editor.putString(Preferences.SETTINGS_LOCATION_SERVICE,location_services);
                    editor.putString(Preferences.SETTINGS_NOTIFICATION,notifications);
                    editor.putString(Preferences.SETTINGS_TEXT_MESSAGING,text_messaging);
                    editor.commit();
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
    IHttpExceptionListener settingsExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
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
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),error_message);
                    intSettings();
                    break;
                }
            }
        }
    };

    public void showcheckAlert(final Activity context,final String title, final String message) {
        // define alert...
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        //set title
        dialog.setTitle(title);
        // set message...
        dialog.setMessage(message);
        // set button status..onclick
        dialog.setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                if (_prefs.edit().clear().commit()){
                    Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                dialog.dismiss();

            }
        });
        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.SETTING_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).hideRight();
        ((HomeActivity)getActivity()).setTitletext("Settings");
        ((HomeActivity)getActivity()).setLeftToolBarImage(R.drawable.ico_menu);
    }
}

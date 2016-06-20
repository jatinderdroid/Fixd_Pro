package com.fixtconsumer.fragments;

import android.content.Context;
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
import android.widget.EditText;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {

    EditText txtFirstName,txtLastName,txtEmail,txtMobile;
    String fname = "",lname = "",email = "",mobile = "";
    Typeface fontfamily;
    Context _context = null ;
    String error_message = "";
    SharedPreferences _prefs = null ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    CheckAlertDialog checkALert;
    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
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
        checkALert = new CheckAlertDialog();
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        setWidgetsIDs(rootView);
        setTypeface();
        initAcount();
        return rootView;
    }
    private void initAcount(){
        fname = _prefs.getString(Preferences.FIRSTNAME,"");
        lname = _prefs.getString(Preferences.LASTNAME,"");
        email = _prefs.getString(Preferences.EMAIL,"");
        mobile = _prefs.getString(Preferences.PHONE,"");
        txtFirstName.setText(fname);
        txtLastName.setText(lname);
        txtEmail.setText(email);
        txtMobile.setText(mobile);
    }
    private void setTypeface() {
       fontfamily = Typeface.createFromAsset(getActivity().getAssets(),"HelveticaNeue-Thin.otf");
//        txtFirstName.setTypeface(fontfamily);
//        txtLastName.setTypeface(fontfamily);
//        txtEmail.setTypeface(fontfamily);
//        txtMobile.setTypeface(fontfamily);

    }

    private void setWidgetsIDs(View v) {
        txtFirstName = (EditText)v.findViewById(R.id.txtFirstName);
        txtLastName = (EditText)v.findViewById(R.id.txtLastName);
        txtEmail = (EditText)v.findViewById(R.id.txtEmail);
        txtMobile = (EditText)v.findViewById(R.id.txtMobile);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.My_ACCOUNT_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Done");
        ((HomeActivity)getActivity()).setTitletext("Account");
        ((HomeActivity)getActivity()).setLeftToolBarText("Back");
    }
    public void submit(){
        fname = txtFirstName.getText().toString().trim();
        lname = txtLastName.getText().toString().trim();
        email = txtEmail.getText().toString().trim();
        mobile = txtMobile.getText().toString().trim();
        if (fname.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter first name");
        }else  if (lname.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter last name");
        }
        else  if (email.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter email");
        }
        else  if (mobile.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter phone number");
        }else{
//            call api
            sendAccountRequest();
        }
    }
    private void sendAccountRequest(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",accountResponseListener,accountExceptionListener,getActivity(),"Processing.");
        apiResponseAsync.execute(getAccountParams());
    }
    IHttpResponseListener accountResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    SharedPreferences.Editor editor = _prefs.edit();
                    editor.putString(Preferences.FIRSTNAME,fname);
                    editor.putString(Preferences.LASTNAME,lname);
                    editor.putString(Preferences.EMAIL,email);
                    editor.putString(Preferences.PHONE,mobile);
                    editor.commit();
                    handler.sendEmptyMessage(0);
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
    IHttpExceptionListener accountExceptionListener = new IHttpExceptionListener() {
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
                    ((HomeActivity)getActivity()).popStack();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
            }
        }
    };
    private HashMap<String,String> getAccountParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","update");
        hashMap.put("object", "customers");
        hashMap.put("data[customers][first_name]", fname);
        hashMap.put("data[customers][last_name]", lname);
        hashMap.put("data[users][email]", email);
        hashMap.put("data[users][phone]", mobile);
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
}

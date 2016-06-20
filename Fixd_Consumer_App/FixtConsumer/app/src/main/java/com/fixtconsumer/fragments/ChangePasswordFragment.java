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
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String current_pass = "", new_pass = "", confirm_pass = "";
    CheckAlertDialog checkALert;
    Context _context = null ;
    String error_message = "";
    SharedPreferences _prefs = null ;
    private OnFragmentInteractionListener mListener;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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

    EditText txtCurrentPassword,txtNewPassword,txtConfirmPassword;
    Typeface fontfamily;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        setWidgetsIDs(rootView);
        setTypeface();
        return rootView;
    }

    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        txtCurrentPassword.setTypeface(fontfamily);
        txtNewPassword.setTypeface(fontfamily);
        txtConfirmPassword.setTypeface(fontfamily);
    }

    private void setWidgetsIDs(View v) {
        txtCurrentPassword = (EditText)v.findViewById(R.id.txtCurrentPassword);
        txtNewPassword = (EditText)v.findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText)v.findViewById(R.id.txtConfirmPassword);
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
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.CHANGE_PASSWORD_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Done");
        ((HomeActivity)getActivity()).setTitletext("Change Password");
        ((HomeActivity)getActivity()).setLeftToolBarText("Back");
    }
    public void submit(){
        current_pass = txtCurrentPassword.getText().toString().trim() ;
        new_pass = txtNewPassword.getText().toString().trim();
        confirm_pass = txtConfirmPassword.getText().toString().trim();
        if (current_pass.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter current password");
        }
        else  if (new_pass.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter new  password");
        }
        else  if (confirm_pass.length() == 0){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter confirm password");
        }
        else  if (current_pass.length() < 6){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"current password length must be greater than 6");
        }
        else  if (new_pass.length() < 6){
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"new password length must be greater than 6");
        }else  if (!new_pass.equals(confirm_pass)) {
            checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title), "password not matched");
        }else {
//            call api
            sendPasswordChangeRequest();
        }
    }

    private void sendPasswordChangeRequest(){
        Utility.hideKeyBoad(_context,getActivity().getWindow().getCurrentFocus());
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",accountResponseListener,accountExceptionListener,getActivity(),"Processing.");
        apiResponseAsync.execute(getAccountParams());
    }
    IHttpResponseListener accountResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
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
        hashMap.put("api","change_password");
        hashMap.put("object", "account");
        hashMap.put("password", current_pass);
        hashMap.put("new_password", new_pass );
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
}

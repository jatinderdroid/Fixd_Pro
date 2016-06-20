package com.fixtconsumer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAddressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtCity, txtState;;
    public String  Address = "",ZipCode = "",City = "",State = "",error_message = "", Address2 = "" ,StateAbre = "";
    SharedPreferences _prefs = null ;
    Context _context = null ;
    CheckAlertDialog checkALert;
    EditText txtAddress, txtZip, txtAddress2;
    int position = 0 ;
    boolean isnew = false ;
    String toolBarText = "Edit Address";
    private OnFragmentInteractionListener mListener;
    AddressesBean modal ;
    public  EditAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAddressFragment newInstance(String param1, String param2) {
        EditAddressFragment fragment = new EditAddressFragment();
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
           Bundle bundle = getArguments() ;
           isnew = bundle.getBoolean("isnew");
           if (isnew){
               toolBarText = "New Address";
           }
            else{
               toolBarText = "Edit Address";
               position = bundle.getInt("position");
               modal = AddressesSingleTonClasses.getInstance().arrayList.get(position);
               Address = modal.getAddress() ;
               Address2 = modal.getAddress_2();
               City = modal.getCity() ;
               ZipCode = modal.getZip();
               State = modal.getState();
           }

        }
        checkALert = new CheckAlertDialog();
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View roorView = inflater.inflate(R.layout.fragment_edit_address, container, false);
        setWidgets(roorView);
//        setTypeface();
        setListeners();
        initLayout();
        return roorView;
    }
    private void initLayout(){
        txtAddress.setText(Address);
        txtAddress2.setText(Address2);
        txtZip.setText(ZipCode);
        txtCity.setText(City);
        txtState.setText(State);
    }
    private void setWidgets(View roorView){
     
        txtAddress = (EditText)roorView.findViewById(R.id.txtAddress);
        txtAddress2 = (EditText)roorView.findViewById(R.id.txtAddress2);
        txtZip = (EditText)roorView.findViewById(R.id.txtZip);
        txtCity = (TextView)roorView.findViewById(R.id.txtCity);
        txtState = (TextView)roorView.findViewById(R.id.txtState);
    }

    @Override
    public void onResume() {
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.SETTING_EDIT_ADDRESS_FRAGMENT);
        setupToolBar();
        super.onResume();
    }

    private void setupToolBar() {
        ((HomeActivity) getActivity()).setRightToolBarText("Save");
        ((HomeActivity) getActivity()).setTitletext(toolBarText);
        ((HomeActivity) getActivity()).setLeftToolBarText("Back");
    }
    private void setListeners(){
        txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtZip.getText().toString().trim().equals("")) {
                    checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title), "Please enter zip code.");
                } else {
                    Utility.hideKeyBoad(_context, v);
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", zipResponseListener, zipExceptionListener, getActivity(), "Getting");
                    apiResponseAsync.execute(getZipRequestParams());
                }
            }
        });

        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtZip.getText().toString().trim().equals("")) {
                    checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title), "Please enter zip code.");
                } else {
                    Utility.hideKeyBoad(_context, v);
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", zipResponseListener, zipExceptionListener, getActivity(), "Getting");
                    apiResponseAsync.execute(getZipRequestParams());
                }
            }
        });
    }
    private HashMap<String,String> getZipRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object", "zipcodes");
        hashMap.put("per_page", 20+"");
        hashMap.put("page", 1 + "");
        hashMap.put("where[zipcode]", txtZip.getText().toString());
        return hashMap;
    }
    IHttpResponseListener zipResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
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
                    checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
                case 2:{
                    txtState.setText(State);
                    txtCity.setText(City);
                    break;
                }
            }
        }
    };
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
    public void submit(){
        Address = txtAddress.getText().toString().trim() ;
        Address2 = txtAddress2.getText().toString().trim() ;
        City = txtCity.getText().toString().trim() ;
        ZipCode = txtZip.getText().toString().trim() ;
        State = txtState.getText().toString().trim();
        if (Address.length() == 0)
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter address1");
//        else if (Address2.length() == 0)
//            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter address2");
        else if (ZipCode.length() == 0)
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter zipcode");
        else if(City.length() == 0)
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter city");
        else if (State.length() == 0)
            checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),"please enter state");
        else {
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", editaddresponseListener, editadddexceptionListener, getActivity(), "Processing");
            apiResponseAsync.execute(getEditAddParams());
        }
    }

    private HashMap<String,String> getEditAddParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        if (isnew){
            hashMap.put("api","create");
            hashMap.put("data[default]","0");
        }else {
            hashMap.put("api","update");
            hashMap.put("data[default]",modal.getDefault_address());
            hashMap.put("data[id]",modal.getId());
        }

        hashMap.put("object", "customer_addresses");
        hashMap.put("data[city]", City);
        hashMap.put("data[state]",StateAbre);
        hashMap.put("data[address]", Address);
        hashMap.put("data[address_2]", Address2);
        hashMap.put("data[zip]", ZipCode);
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
    IHttpExceptionListener editadddexceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception ;
            handler.sendEmptyMessage(1);
    }
    };
    IHttpResponseListener editaddresponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "");
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray jsonArray = response.getJSONArray("RESPONSE");
                    if (isnew){

                        AddressesBean addressesBean = new AddressesBean();
                        addressesBean.setId(jsonArray.getJSONObject(0).getString("id"));
                        addressesBean.setAddress_2(Address2);
                        addressesBean.setAddress(Address);
                        addressesBean.setZip(ZipCode);
                        addressesBean.setCity(City);
                        addressesBean.setState(State);
                        AddressesSingleTonClasses.getInstance().arrayList.add(0,addressesBean);
                    }else{
                        modal.setAddress_2(Address2);
                        modal.setAddress(Address);
                        modal.setZip(ZipCode);
                        modal.setCity(City);
                        modal.setState(State);
                    }
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
}

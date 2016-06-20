package com.fixtconsumer.fragments;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.HomeTypePlanAdapter;
import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.beans.HomePlansBean;
import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.GetApiResponseAsyncNoProgress;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.net.MultipartUtility;
import com.fixtconsumer.requests_collection.HomePlansRequestObject;
import com.fixtconsumer.requests_collection.RequestConstants;
import com.fixtconsumer.requests_collection.RequestMethods;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.singletons.HomeServiceSingleTon;
import com.fixtconsumer.singletons.RequestMultiPartSingleTon;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lstTypePlan ;
    String typePlan = "purchase";
    SharedPreferences _prefs = null ;
    Context _context = null ;
    ArrayList<HomePlansBean> arrayList = null  ;
    HomeTypePlanAdapter adapter = null ;
    private OnFragmentInteractionListener mListener;
    HomePlansBean homePlansBean = null ;
    LinearLayout home_plans;
    LayoutInflater inflater ;
    TextView txtName,txtServiceFor;
    String requestName = "";
    String address = "";
    ArrayList<AddressesBean> addressesBeanArrayList = new ArrayList<AddressesBean>();
    HomeRequestObjectSingleTon singleTon = HomeRequestObjectSingleTon.getInstance();
    String error_message = "";
    CheckAlertDialog checkALert;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        _context = getActivity();
        _prefs = Utility.getSharedPreferences(_context);
        inflater =  (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.HOME_FRAGMENT);
         setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarImage(R.drawable.calender);
        ((HomeActivity)getActivity()).setTitleBack(R.drawable.welcome);
        ((HomeActivity)getActivity()).setLeftToolBarImage(R.drawable.ico_menu);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(rootView);
        setListeners();
//         getting which plan screen to open
//        typePlan = getTypePlan();
        typePlan = "rekey";
//        getting items according to plan
        arrayList = Utility.getHomePlansList(typePlan);
//        adapter = new HomeTypePlanAdapter(getActivity(),arrayList,getResources());
//        lstTypePlan.setAdapter(adapter);
        setView();
        txtName.setText("Hello " + _prefs.getString(Preferences.FIRSTNAME, "") + "!");
        if (AddressesSingleTonClasses.getInstance().arrayList.size() == 0 )
        {
            GetApiResponseAsyncNoProgress apiResponseAsync = new GetApiResponseAsyncNoProgress(Constants.BASE_URL_SINGLE,"POST",addressResponseListener,addressExceptionListener,getActivity(),"Signing In");
            apiResponseAsync.execute(getAddressParams());
        }else {
            txtServiceFor.setText("Service for "+getDefaultAddress());
        }
        return rootView;
    }
    private String getDefaultAddress(){
        String Address = "";
        for (int i = 0 ; i < AddressesSingleTonClasses.getInstance().arrayList.size() ; i++){
            if (AddressesSingleTonClasses.getInstance().arrayList.get(i).getDefault_address().equals("1")){
                Address = AddressesSingleTonClasses.getInstance().arrayList.get(i).getAddress() +"-"+AddressesSingleTonClasses.getInstance().arrayList.get(i).getAddress_2() ;
                break;
            }
        }
        return Address;
    }
    IHttpResponseListener addressResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("","response"+response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject jsonObject = results.getJSONObject(i);
                        AddressesBean modal = new AddressesBean();
                        modal.setId(jsonObject.getString("id"));
                        modal.setZip(jsonObject.getString("zip"));
                        modal.setCity(jsonObject.getString("city"));
                        modal.setState(jsonObject.getString("state"));
                        modal.setAddress(jsonObject.getString("address"));
                        modal.setAddress_2(jsonObject.getString("address_2"));
                        modal.setDefault_address(jsonObject.getString("default"));
                        if (modal.getDefault_address().equals("1")){
                            modal.setIsChecked(true);
                        }
                        Log.e("", "response" + response);
                        AddressesSingleTonClasses.getInstance().arrayList.add(modal);
                        if (jsonObject.getString("default").equals("1")){
                            address = jsonObject.getString("address") +" - "+jsonObject.getString("address_2");
                        }

                    }
                    if (address.length() > 0){
                        _prefs.edit().putString(Preferences.FULL_ADDREESS_LIST_ARRAY,results.toString()).commit();
                        handler.sendEmptyMessage(0);
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
            handler.sendEmptyMessage(0);
        }
    };
    IHttpExceptionListener addressExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            handler.sendEmptyMessage(1);
        }
    };

    private  void setWidgets(View rootView){
        lstTypePlan = (ListView)rootView.findViewById(R.id.lstTypePlan);
        home_plans = (LinearLayout)rootView.findViewById(R.id.home_plans);
        txtName = (TextView)rootView.findViewById(R.id.txtName);
        txtServiceFor = (TextView)rootView.findViewById(R.id.txtServiceFor);
    }
    private void setListeners(){
        txtServiceFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).switchFragment(new SettingAddressFragment(), Constants.SETTING_ADDRESS_FRAGMENT, true, null);
            }
        });
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
    private String getTypePlan(){
        String rekey = _prefs.getString(Preferences.FREE_REY_KEY,"");
        String warrenty_paln = _prefs.getString(Preferences.TOTAL_WARRENTY_PLAN,"");
        if (rekey.equals("1")){
            typePlan = "rekey";
        }else if (rekey.equals("0") && !warrenty_paln.equals("0")){
            typePlan = "claim" ;
        }else {
            typePlan = "purchase";
        }
        return typePlan;
    }
    private void setView(){
        for (int i = 0 ; i < arrayList.size(); i++){
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            final View child = getActivity().getLayoutInflater().inflate(R.layout.item_home_type_plan, null);
            ImageView imgCat = (ImageView)child.findViewById(R.id.imgCat);
            TextView lblCat = (TextView)child.findViewById(R.id.lblCat);
            imgCat.setImageResource(arrayList.get(i).getPhoto());
            lblCat.setText(arrayList.get(i).getName());
            child.setTag(i+"");
            child.setLayoutParams(param);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String)child.getTag();
                    homePlansBean = arrayList.get(Integer.parseInt(tag));
                    if (homePlansBean.getName().equals(Constants.PURCHASE_PLAN)){
                        singleTon.setRequestName(RequestConstants.PURCHASE_PLAN_REQUEST);
                        ((HomeActivity) getActivity()).switchFragment(new PlansFragment(), Constants.PLANS_FRAGMENT, true, null);
                    }else if(homePlansBean.getName().equals(Constants.FREE_RE_KEY)){
                        singleTon.setRequestName(RequestConstants.FREE_RE_KEY_REQUEST);
                        singleTon.background_drawable = R.drawable.rekey_back ;
                        ((HomeActivity) getActivity()).switchFragment(new HowManyLocksFragment(), Constants.FREE_RE_KEY, true, null);
                    }else if(homePlansBean.getName().equals(Constants.FILE_CLAIM)){
//                        Call api to check for Addresses if more than one address then select Address first else Go Ahead
                        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",getAdressesFileClaimResponse,getAdressesFileClaimException,getActivity(),"Loading");
                        apiResponseAsync.execute(getFileClaimAddressParams());
//                        handler.sendEmptyMessage(1);
                    }else if(homePlansBean.getName().equals(Constants.HOME_SERVICE)){
                        singleTon.setRequestName(RequestConstants.HOME_SERVICES_REQUEST);
                        ((HomeActivity)getActivity()).switchFragment(new HomeServiceFragment(),Constants.HOME_SERVICE_FRAGMENT,true,null);
                    }
                }
            });
            home_plans.addView(child);
        }
    }
    IHttpResponseListener getAdressesFileClaimResponse = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            try {
                Log.e("","response"+response);
                AddressesSingleTonClasses.getInstance().arrayListFileAClain.clear();
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = response.getJSONArray("RESPONSE");
                    for (int i = 0 ; i < results.length() ; i++){
                        JSONObject jsonObject = results.getJSONObject(i);
                        AddressesBean modal = new AddressesBean();
                        modal.setId(jsonObject.getString("id"));
                        modal.setZip(jsonObject.getString("zip"));
                        modal.setCity(jsonObject.getString("city"));
                        modal.setState(jsonObject.getString("state"));
                        modal.setAddress(jsonObject.getString("address"));
                        modal.setAddress_2(jsonObject.getString("address_2"));
                        Log.e("", "response" + response);
                        AddressesSingleTonClasses.getInstance().arrayListFileAClain.add(modal);
                    }
//                    AddressesSingleTonClasses.getInstance().arrayListFileAClain = addressesBeanArrayList ;
                    handler.sendEmptyMessage(1);
                }else{
                    JSONObject errors = response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String key = (String) keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(2);
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
//                    _prefs.edit().putString(Preferences.ADDRESS,address).commit();
                    txtServiceFor.setText("Service for "+getDefaultAddress());

                    break;
                }
                case 1:{
                    if (AddressesSingleTonClasses.getInstance().arrayListFileAClain.size() > 1){
                        singleTon.setRequestName(RequestConstants.FILE_A_CLAIM_REQUEST);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isclaim", true);
                        bundle.putBoolean("purchase", false);
                        ((HomeActivity) getActivity()).switchFragment(new SettingAddressFragment(), Constants.SETTING_ADDRESS_FRAGMENT, true, bundle);
                    }else if (AddressesSingleTonClasses.getInstance().arrayListFileAClain.size() == 1){
                        singleTon.setRequestName(RequestConstants.FILE_A_CLAIM_REQUEST);
                        singleTon.address_file_a_claim_id =  AddressesSingleTonClasses.getInstance().arrayListFileAClain.get(0).getId();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isclaim", true);
                        ((HomeActivity) getActivity()).switchFragment(new HomeServiceFragment(), Constants.HOME_SERVICE_FRAGMENT, true, bundle);
                    }else if (AddressesSingleTonClasses.getInstance().arrayListFileAClain.size() == 0){
                        checkALert.showcheckAlert(getActivity(),getResources().getString(R.string.alert_title),"No Addresses with active plans found.");
                    }
                    break;
                }
                case 2:{
                    checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
            }
        }
    };
    IHttpExceptionListener getAdressesFileClaimException = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {

        }
    };
    private HashMap<String,String> getFileClaimAddressParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read_covered");
        hashMap.put("object","customer_addresses");
        hashMap.put("select","^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("page","1");
        hashMap.put("per_page","999");
        return hashMap;
    }
    private HashMap<String,String> getAddressParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","customer_addresses");
        hashMap.put("select","^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("page","1");
        hashMap.put("per_page","999");
        return hashMap;
    }
}

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
import android.widget.AdapterView;
import android.widget.ListView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.CommonPlans;
import com.fixtconsumer.activities.CompletePlans;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.WarrentyPlanAdapter;
import com.fixtconsumer.beans.ApplianceTypeBeans;
import com.fixtconsumer.beans.SimplePlansBean;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.singletons.WarrentyPlanSingleton;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlansFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences _prefs = null ;
    Context _context = null;
    String error_message = "";
    CheckAlertDialog checkALert;
    ArrayList<WarrentyPlanBean> warrentyPlanBeanArrayList = WarrentyPlanSingleton.getInstance().arrayList;
    ListView lstPlans ;
    private OnFragmentInteractionListener mListener;
    WarrentyPlanBean warrentyPlanBean = null ;
    Fragment fragment = null;
    public PlansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlansFragment newInstance(String param1, String param2) {
        PlansFragment fragment = new PlansFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_plans, container, false);
        _prefs = Utility.getSharedPreferences(_context);
        checkALert = new CheckAlertDialog();
        lstPlans = (ListView)rootView.findViewById(R.id.lstPlans);
        if (warrentyPlanBeanArrayList.size() > 0){
            handler.sendEmptyMessage(0);
        }else {
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",planIHttpResponseListener,planIHttpExceptionListener,getActivity(),"getting Plans");
            apiResponseAsync.execute(getPlansRequestParams());
        }
        lstPlans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                warrentyPlanBean = warrentyPlanBeanArrayList.get(position);
                if (AddressesSingleTonClasses.getInstance().arrayList.size() == 1){
                    WarrentyPlanSingleton.getInstance().AddressId = AddressesSingleTonClasses.getInstance().arrayList.get(0).getId();
                    if (warrentyPlanBean.getName().equals("Complete")){
                        WarrentyPlanSingleton.getInstance().setselectedPlan(warrentyPlanBean);
                        Intent intent = new Intent(getActivity(), CompletePlans.class);
                        intent.putExtra("plan",warrentyPlanBean);
                        intent.putExtra("isnew",true);
                        startActivity(intent);
//                    fragment = new CompletePurchasePlan();
//                    WarrentyPlanSingleton.getInstance().setselectedPlan(warrentyPlanBean);
//                    ((HomeActivity) getActivity()).switchFragment(fragment, Constants.COMPLETE_PURCHASE_FRAGMENT, true, null);

                    }else if (AddressesSingleTonClasses.getInstance().arrayList.size() == 0){
                        checkALert.showcheckAlert(getActivity(),getResources().getString(R.string.alert_title),"No Addresses with active plans found.");
                    }else {
                        WarrentyPlanSingleton.getInstance().setselectedPlan(warrentyPlanBean);
                        Intent intent = new Intent(getActivity(), CommonPlans.class);
                        intent.putExtra("plan",warrentyPlanBean);
                        intent.putExtra("isnew",true);
                        startActivity(intent);
//                    fragment = new CompletePurchasePlan();
//                    WarrentyPlanSingleton.getInstance().setselectedPlan(warrentyPlanBean);
//                    ((HomeActivity) getActivity()).switchFragment(fragment, Constants.COMPLETE_PURCHASE_FRAGMENT, true, null);

                    }
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isclaim", false);
                    bundle.putBoolean("purchase", true);
                    if (warrentyPlanBean.getName().equals("Complete"))
                        bundle.putBoolean("complete", true);
                    else
                        bundle.putBoolean("complete", false);
                    ((HomeActivity) getActivity()).switchFragment(new SettingAddressFragment(), Constants.SETTING_ADDRESS_FRAGMENT, true, bundle);
                }
            }
        });
        return rootView;
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
    private HashMap<String,String> getPlansRequestParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object", "warranty_plans");
        hashMap.put("select", "^*,appliance_types.name,simple_plans.^*,simple_plans.appliance_types.name");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
    IHttpResponseListener planIHttpResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
                    if (results.length() == 0){
                        error_message = "No Plans found" ;
                        handler.sendEmptyMessage(1);
                    }else {
                        for (int i = 0 ; i < results.length() ; i++){
                            JSONObject jsonObject = results.getJSONObject(i);
                            WarrentyPlanBean warrentyPlanBean = new WarrentyPlanBean();
                            warrentyPlanBean.setId(jsonObject.getString("id"));
                            warrentyPlanBean.setName(jsonObject.getString("name"));
                            warrentyPlanBean.setPrice(jsonObject.getString("price"));
                            warrentyPlanBean.setPrice_inclusive_tax(jsonObject.getString("price_inclusive_tax"));
                            warrentyPlanBean.setTax(jsonObject.getString("tax"));
                            warrentyPlanBean.setType(jsonObject.getString("type"));
                            warrentyPlanBean.set_order(jsonObject.getString("_order"));
                            JSONArray appliance_types = jsonObject.getJSONArray("appliance_types");
                            ArrayList<ApplianceTypeBeans> applianceTypeBeansesArrayList = new ArrayList<ApplianceTypeBeans>();

                            for (int j = 0 ; j < appliance_types.length() ; j++){
                                ApplianceTypeBeans applianceTypeBeans = new ApplianceTypeBeans();
                                JSONObject jsonObjectAppliance = appliance_types.getJSONObject(j);
                                applianceTypeBeans.setName(jsonObjectAppliance.getString("name"));
                                applianceTypeBeans.setWarranty_plans_id("warranty_plans_id");
                                applianceTypeBeansesArrayList.add(applianceTypeBeans);
                            }
                            warrentyPlanBean.setApplianceTypeBeansArrayList(applianceTypeBeansesArrayList);
                            ArrayList<SimplePlansBean> apSimplePlansBeanArrayList = new ArrayList<SimplePlansBean>();
                            JSONArray simple_plans = jsonObject.getJSONArray("simple_plans");
                            for (int j = 0 ; j < simple_plans.length() ; j++){
                                SimplePlansBean simplePlansBean = new SimplePlansBean();
                                JSONObject jsonObjectAppliance = simple_plans.getJSONObject(j);
                                simplePlansBean.setId(jsonObjectAppliance.getString("id"));
                                simplePlansBean.setName(jsonObjectAppliance.getString("name"));
                                simplePlansBean.setPrice(jsonObjectAppliance.getString("price"));
                                simplePlansBean.setType(jsonObjectAppliance.getString("type"));
                                simplePlansBean.set_order(jsonObjectAppliance.getString("_order"));
                                simplePlansBean.setCombined_plan_id(jsonObjectAppliance.getString("combined_plan_id"));
                                simplePlansBean.setSimple_plan_id(jsonObjectAppliance.getString("simple_plan_id"));
                                simplePlansBean.setWarranty_plans_id(jsonObjectAppliance.getString("warranty_plans_id"));
                                JSONArray jsonArraySimple = jsonObjectAppliance.getJSONArray("appliance_types");
                                ArrayList<ApplianceTypeBeans> arrayList = new ArrayList<ApplianceTypeBeans>();
                                for (int k = 0 ; k < jsonArraySimple.length() ; k++){
                                    ApplianceTypeBeans applianceTypeBeansSimple = new ApplianceTypeBeans();
                                    JSONObject jsonObjectApplianceSimple = jsonArraySimple.getJSONObject(k);
                                    applianceTypeBeansSimple.setName(jsonObjectApplianceSimple.getString("name"));
                                    applianceTypeBeansSimple.setWarranty_plans_id(jsonObjectApplianceSimple.getString("warranty_plans_id"));
                                    arrayList.add(applianceTypeBeansSimple);
                                }
                                simplePlansBean.setArrayList(arrayList);
                                apSimplePlansBeanArrayList.add(simplePlansBean);
                            }
                            warrentyPlanBean.setSimplePlansBeanArrayList(apSimplePlansBeanArrayList);
                            warrentyPlanBeanArrayList.add(warrentyPlanBean);
                            Collections.sort(warrentyPlanBeanArrayList, new Comparator<WarrentyPlanBean>() {

                                public int compare(WarrentyPlanBean p2, WarrentyPlanBean p1) {
                                    return (int) Float.parseFloat(p1.getPrice()) - (int) Float.parseFloat(p2.getPrice());
                                }
                            });

                        }
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
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
//                    set Adapter

                    WarrentyPlanAdapter adapter = new WarrentyPlanAdapter(getActivity(),warrentyPlanBeanArrayList,getResources());
                    lstPlans.setAdapter(adapter);
                    Log.e("","Size"+warrentyPlanBeanArrayList.size());
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(),_context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
            }
        }
    };
    IHttpExceptionListener planIHttpExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            Log.e("","exception"+exception);
            error_message = exception ;
            handler.sendEmptyMessage(1);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.PLANS_FRAGMENT);
        setupToolBar();

    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).hideRight();
        ((HomeActivity)getActivity()).setTitletext("Protection Plan");
        ((HomeActivity)getActivity()).setLeftToolBarImage(R.drawable.ico_menu);
    }
}

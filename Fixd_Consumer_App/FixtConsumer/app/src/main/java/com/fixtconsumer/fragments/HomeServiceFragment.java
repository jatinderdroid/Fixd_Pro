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
import android.widget.ListView;
import android.widget.Toast;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.HomeServicesAdapter;
import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.net.MultipartUtility;
import com.fixtconsumer.requests_collection.HomePlansRequestObject;
import com.fixtconsumer.requests_collection.RequestConstants;
import com.fixtconsumer.requests_collection.RequestMethods;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView lstHomeServices ;
    HomeServiceSingleTon singleTon = HomeServiceSingleTon.getInstance();
    HomeServicesAdapter adapter = null ;
    ArrayList<HomeServiceBeans> arrayList ;
    CheckAlertDialog checkALert = null ;
    String error_message = "";
    MultipartUtility multipartUtility = null;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    private boolean isclaim = false ;
    SharedPreferences _prefs = null ;
    Context _context = null ;
    public HomeServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeServiceFragment newInstance(String param1, String param2) {
        HomeServiceFragment fragment = new HomeServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.HOME_SERVICE_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).hideRight();
        ((HomeActivity)getActivity()).setLeftToolBarImage(R.drawable.white_cross_toolbar);
        if (!isclaim)
            ((HomeActivity)getActivity()).setTitletext("Choose a Service");
        else
            ((HomeActivity)getActivity()).setTitleBack(R.drawable.what_need_to_fixd);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           Bundle bundle = getArguments();
            isclaim = bundle.getBoolean("isclaim");
        }
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home2, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(rootView);
        setListeners();
        if (!isclaim)
            arrayList = singleTon.getList();
        else
            arrayList = singleTon.getListFileAClaim();
        if (arrayList.size() > 0){
            handler.sendEmptyMessage(0);
        }else {
//             make web call
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",homeServiceResponseListener,homeServiceExceptionListener,getActivity(),"Getting");
            apiResponseAsync.execute(getHomeServiceRequestParams());
        }
        return rootView;
    }
    private void setWidgets(View rootView){
        lstHomeServices = (ListView)rootView.findViewById(R.id.lstHomeServices);
    }
    private void setListeners(){
        lstHomeServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeServiceBeans homeServiceBeans = arrayList.get(position);
                if (homeServiceBeans.getStatus().equalsIgnoreCase("ACTIVE") && requestObjectSingleTon.getRequestName().equals(RequestConstants.HOME_SERVICES_REQUEST)){

                    FixdConsumerApplication.selectedAppliance = homeServiceBeans.getTitle() ;
                    HomePlansRequestObject homePlansRequestObject = new HomePlansRequestObject();
                    homePlansRequestObject.setChooseService(homeServiceBeans);
                    requestObjectSingleTon.setHomePlansRequestObject(homePlansRequestObject);
                    ((HomeActivity) getActivity()).switchFragment(new WhatTypeOfProjectFragment(), Constants.WHAT_TYPE_OF_PROJECT_FRAGMENT, true, null);
                }else  if (homeServiceBeans.getStatus().equalsIgnoreCase("ACTIVE") && requestObjectSingleTon.getRequestName().equals(RequestConstants.FILE_A_CLAIM_REQUEST)){
                    FixdConsumerApplication.selectedAppliance = homeServiceBeans.getTitle() ;
                    HomePlansRequestObject homePlansRequestObject = new HomePlansRequestObject();
                    homePlansRequestObject.setChooseService(homeServiceBeans);

                    FixdConsumerApplication.projectType = "Repair";
//                    HomePlansRequestObject homePlansRequestObject = requestObjectSingleTon.getHomePlansRequestObject();
                    homePlansRequestObject.setTypeofprojectName(FixdConsumerApplication.projectType);
                    requestObjectSingleTon.setHomePlansRequestObject(homePlansRequestObject);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isclaim", true);
                    ((HomeActivity) getActivity()).switchFragment(new WhichApplianceFragment(), Constants.WHICH_APPLIANCE_FRAGMENT, true, bundle);
                }

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
    private HashMap<String, String> getHomeServiceRequestParams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (isclaim){
            hashMap.put("api", "read_covered");
            hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        }else{
            hashMap.put("api", "read");
        }

        hashMap.put("object", "services");
        hashMap.put("select", "^*");
        hashMap.put("per_page", "999");
        hashMap.put("page", "1");
        return hashMap;
    }
    IHttpResponseListener homeServiceResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
//                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONArray results = response.getJSONArray("RESPONSE");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject object = results.getJSONObject(i);
                        HomeServiceBeans homeService = new HomeServiceBeans();
                        homeService.setId(object.getInt("id"));
                        homeService.setTitle(object.getString("name"));
                        homeService.setStatus(object.getString("status"));
                        homeService.setDisplay_order(object.getString("display_order"));
                        homeService.setImage(object.getString("name"));
                        arrayList.add(homeService);
                    }
                    Collections.sort(arrayList, new Comparator<HomeServiceBeans>() {

                        public int compare(HomeServiceBeans p1, HomeServiceBeans p2) {
                            return Integer.parseInt(p1.getDisplay_order()) - Integer.parseInt(p2.getDisplay_order());
                        }
                    });
//                    HomeServiceSingleTon.getInstance().setList(arrayList);
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
    IHttpExceptionListener homeServiceExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    adapter = new HomeServicesAdapter(getActivity(),arrayList,getResources());
                    lstHomeServices.setAdapter(adapter);
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(),getActivity().getResources().getString(R.string.alert_title),error_message);
                    break;
                }
            }
        }
    };
}

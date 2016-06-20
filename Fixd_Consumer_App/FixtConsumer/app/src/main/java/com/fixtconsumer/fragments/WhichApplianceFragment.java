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
import com.fixtconsumer.adapters.AppliancesAdapter;
import com.fixtconsumer.beans.AppliancesModal;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.requests_collection.HomePlansRequestObject;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhichApplianceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhichApplianceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhichApplianceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView lstWhichAppliance;
    Context _context;
    SharedPreferences _prefs = null ;
    String error_message = "";
    ArrayList<AppliancesModal> appliancesModalArrayList = new ArrayList<AppliancesModal>();
    Fragment fragment = null;
    CheckAlertDialog checkALert;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    private boolean isclaim = false;
    public WhichApplianceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhichApplianceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhichApplianceFragment newInstance(String param1, String param2) {
        WhichApplianceFragment fragment = new WhichApplianceFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            Bundle bundle = getArguments();
            isclaim = bundle.getBoolean("isclaim");
        }
        _context = getActivity();
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_which_appliance, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(view);
        lstWhichAppliance.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));
        setListeners();
        if (appliancesModalArrayList.size() > 0){
            handler.sendEmptyMessage(0);
        }else {
            getAppliances();
        }
        return view;
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.WHICH_APPLIANCE_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Next");
        ((HomeActivity)getActivity()).setTitletext("Appliances - " +requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName());
        ((HomeActivity)getActivity()).setLeftToolBarText("Cancel");
    }
    private void getAppliances(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",getAppliancesListener,getApplianceExceptionListener,getActivity(),"getting");
        apiResponseAsync.execute(getAppliancesParams());
    }
    private void setWidgets(View view){
        lstWhichAppliance = (ListView)view.findViewById(R.id.lstWhichAppliance);
    }
    private void setListeners(){
        lstWhichAppliance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if appliance has power source go to next screen to choose power source
                // else call web service to Add service
                if (requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName().equals("Repair")){
                AppliancesModal appliancesModal = appliancesModalArrayList.get(position);
                    clearArrayListQuantity();
                    appliancesModalArrayList.get(position).setQuantity("1");
                    requestObjectSingleTon.getHomePlansRequestObject().setWhichAppliancelist(appliancesModalArrayList);

                if (!hasPowerSource()){
                    fragment = new WhatsTheProblemFragment();
                    ((HomeActivity) getActivity()).switchFragment(fragment, Constants.WHATS_THE_PROBLEM_FRAGMEMT, true, null);
                }else {
                    // go to choose power source screen
                    fragment = new HasPowerSourceFragment();
                    ((HomeActivity) getActivity()).switchFragment(fragment, Constants.HAS_POWER_SOURCE_FRAGMENT, true, null);
                }
            }
            }
        });

    }
    private void clearArrayListQuantity(){
        for (int i = 0 ; i < appliancesModalArrayList.size() ; i++){
            appliancesModalArrayList.get(i).setQuantity("0");
        }
    }
    IHttpResponseListener getAppliancesListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response);
            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
//                    nextScheduled = pagination.getString("next");
                    for (int i = 0; i < results.length(); i++) {
                        AppliancesModal modal = new  AppliancesModal();
                        JSONObject jsonObject = results.getJSONObject(i);
                        modal.setId(jsonObject.getString("id"));
                        modal.setName(jsonObject.getString("name"));
                        modal.setService_id(jsonObject.getString("service_id"));
                        modal.setHas_power_source(jsonObject.getString("has_power_source"));
//                        if (!jsonObject.isNull("image")){
//                            JSONObject objectImage = jsonObject.getJSONObject("image");
//                            if (!objectImage.isNull("original"))
//                            modal.setImage_original(jsonObject.getString("original"));
//                        }
                        appliancesModalArrayList.add(modal);
                    }
                    handler.sendEmptyMessage(0);
                } else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    AppliancesAdapter adapter = new AppliancesAdapter(getActivity(),appliancesModalArrayList,getResources());
                    lstWhichAppliance.setAdapter(adapter);
                    break;
                }
                case 1: {
                        checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
            }
        }
    };
    IHttpExceptionListener getApplianceExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    };
    private HashMap<String,String> getAppliancesParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","appliance_types");
        if (isclaim)
        hashMap.put("covers_address",HomeRequestObjectSingleTon.getInstance().address_file_a_claim_id);
        hashMap.put("select","^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("where[service_id]", HomeRequestObjectSingleTon.getInstance().getHomePlansRequestObject().getChooseService().getId()+"");
//        hashMap.put("where[service_id]", "1");
        return hashMap;
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
    private boolean hasPowerSource(){
        boolean hasPower = false;
        for (int i = 0 ; i < appliancesModalArrayList.size() ; i++ ){
//            Toast.makeText(getActivity(),appliancesModalArrayList.get(i).getQuantity(),Toast.LENGTH_LONG).show();
            if (appliancesModalArrayList.get(i).getHas_power_source().equals("1") && Integer.parseInt(appliancesModalArrayList.get(i).getQuantity()) > 0) {
                hasPower = true ;
                break;
            }
        }
        return hasPower;
    }
    private boolean isQuantityChanged(){
        boolean isQuantityChanged = false ;
        for (int i = 0 ; i < appliancesModalArrayList.size() ; i++ ){
//            Toast.makeText(getActivity(),appliancesModalArrayList.get(i).getQuantity(),Toast.LENGTH_LONG).show();
            if (Integer.parseInt(appliancesModalArrayList.get(i).getQuantity()) > 0) {
                isQuantityChanged = true ;
                break;
            }
        }
        return isQuantityChanged ;
    }
    public void submit(){
//        Check if any quanity value selected
        if(!isQuantityChanged()){
           checkALert.showcheckAlert(getActivity(),getString(R.string.alert_title),"Please select appliance before continuing");
        }else{
            requestObjectSingleTon.getHomePlansRequestObject().setWhichAppliancelist(appliancesModalArrayList);
            if (!hasPowerSource()){
                fragment = new WhatsTheProblemFragment();
                ((HomeActivity) getActivity()).switchFragment(fragment, Constants.WHATS_THE_PROBLEM_FRAGMEMT, true, null);
            }else {
                // go to choose power source screen
                fragment = new HasPowerSourceFragment();
                ((HomeActivity) getActivity()).switchFragment(fragment, Constants.HAS_POWER_SOURCE_FRAGMENT, true, null);
            }
        }

    }
}

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

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.HasPowerSourceAdapter;
import com.fixtconsumer.adapters.WhatsTheProblemAdapter;
import com.fixtconsumer.beans.WhatsProblemBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
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
 * {@link WhatsTheProblemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhatsTheProblemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatsTheProblemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lstProblem ;
    Fragment fragment = null ;
    SharedPreferences _prefs = null;
    Context _context = null ;
    WhatsTheProblemAdapter adapter = null ;
    String error_message = "";
    ArrayList<WhatsProblemBean> arrayList = new ArrayList<WhatsProblemBean>();
    private OnFragmentInteractionListener mListener;
    CheckAlertDialog checkALert;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    public WhatsTheProblemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatsTheProblemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatsTheProblemFragment newInstance(String param1, String param2) {
        WhatsTheProblemFragment fragment = new WhatsTheProblemFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar = 0 ;
        View view = inflater.inflate(R.layout.fragment_whats_the_problem, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(view);
        lstProblem.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));
        setListeners();
        if (arrayList.size() > 0){
            handler.sendEmptyMessage(0);
        }else {
            getProblems();
        }
        return view;
    }
    private void setListeners(){
        lstProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestObjectSingleTon.getHomePlansRequestObject().setWhatsProblemBean(arrayList.get(position));
                ((HomeActivity) getActivity()).switchFragment(new TellUsMoreFragment(),Constants.TELL_US_MORE_FRAGMEMT,true,null);
            }
        });
    }
    private void setWidgets(View view){
        lstProblem = (ListView)view.findViewById(R.id.lstProblem);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.WHATS_THE_PROBLEM_FRAGMEMT);
        setupToolBar();
    }

    private void setupToolBar() {
        ((HomeActivity) getActivity()).setRightToolBarText("Next");
        ((HomeActivity) getActivity()).setTitletext("Appliances - "+requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName());
        ((HomeActivity) getActivity()).setLeftToolBarText("Cancel");
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void getProblems(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",getProblemListener,getProblemExceptionListener,getActivity(),"getting");
        apiResponseAsync.execute(getProblemParams());
    }
    IHttpResponseListener getProblemListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","response"+Response);
            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
//                    nextScheduled = pagination.getString("next");
                    for (int i = 0; i < results.length(); i++) {
                        WhatsProblemBean modal = new  WhatsProblemBean();
                        JSONObject jsonObject = results.getJSONObject(i);
                        modal.setId(jsonObject.getString("id"));
                        modal.setProblem(jsonObject.getString("problem"));
                        arrayList.add(modal);
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
    IHttpExceptionListener getProblemExceptionListener = new IHttpExceptionListener() {
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
                    adapter = new WhatsTheProblemAdapter(getActivity(), arrayList,getResources());
                    lstProblem.setAdapter(adapter);
                    break;
                }case 1:{
                    checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
            }
        }
    };
    private HashMap<String,String> getProblemParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","appliance_problems");
        hashMap.put("select","^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
//        hashMap.put("where[service_id]", CurrentServiceAddingSingleTon.getInstance().getSkillTrade().getId() + "");
        hashMap.put("where[appliance_id]", "3");
        hashMap.put("page", "1");
        hashMap.put("per_page", "999");
        return hashMap;
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
}

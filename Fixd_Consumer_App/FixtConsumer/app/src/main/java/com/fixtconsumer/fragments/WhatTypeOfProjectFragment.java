package com.fixtconsumer.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.WhatTypeOfProjectAdapter;
import com.fixtconsumer.beans.Brands;
import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.JSONParser;
import com.fixtconsumer.requests_collection.HomePlansRequestObject;
import com.fixtconsumer.singletons.BrandNamesSingleton;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.singletons.HomeServiceSingleTon;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhatTypeOfProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhatTypeOfProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatTypeOfProjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Brands> arrayListBrands = BrandNamesSingleton.getInstance().getBrands();

    private OnFragmentInteractionListener mListener;
    ListView lstTypeProject;
    TextView lblTitle;
    SharedPreferences _prefs = null ;
    Context _context = null ;
    ArrayList<String> arrayList = new ArrayList<String>();
    WhatTypeOfProjectAdapter adapter = null ;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    public WhatTypeOfProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatTypeOfProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatTypeOfProjectFragment newInstance(String param1, String param2) {
        WhatTypeOfProjectFragment fragment = new WhatTypeOfProjectFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_what_type_of_project, container, false);

        setWidgets(rootView);
        setListeners();
        arrayList.clear();
        if (!_prefs.getString(Preferences.TOTAL_WARRENTY_PLAN,"").equals("0")){
//            remove repair
            arrayList.add("Repair");
            arrayList.add("Install/Replace");
        }else {
            arrayList.add("Repair");
            arrayList.add("Install/Replace");
        }
        lstTypeProject.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));
        adapter = new WhatTypeOfProjectAdapter(getActivity(),arrayList,getResources());
        lstTypeProject.setAdapter(adapter);
        if (arrayListBrands.size() == 0)
        getBrands();
        return rootView;
    }
    private void setWidgets(View rootView){
        lblTitle = (TextView)rootView.findViewById(R.id.lblTitle);
        lstTypeProject = (ListView)rootView.findViewById(R.id.lstTypeProject);
    }
    private void setListeners(){
        lstTypeProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FixdConsumerApplication.projectType = arrayList.get(position);
                HomePlansRequestObject homePlansRequestObject = requestObjectSingleTon.getHomePlansRequestObject();
                homePlansRequestObject.setTypeofprojectName(FixdConsumerApplication.projectType);
                ((HomeActivity) getActivity()).switchFragment(new WhichApplianceFragment(), Constants.WHICH_APPLIANCE_FRAGMENT, true, null);
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
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.WHAT_TYPE_OF_PROJECT_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Next");
        ((HomeActivity)getActivity()).setTitletext("Appliances");
        ((HomeActivity)getActivity()).setLeftToolBarText("Back");
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

    private HashMap<String, String> getBrandsRequestParams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "read");
        hashMap.put("object", "brands");
        hashMap.put("select", "^*");
        hashMap.put("per_page", "999");
        hashMap.put("page", "1");
        hashMap.put("service", requestObjectSingleTon.getHomePlansRequestObject().getChooseService().getId()+"");
        hashMap.put("token", Utility.getSharedPreferences(getActivity()).getString(Preferences.AUTH_TOKEN, null));
        return hashMap;
    }
    private void getBrands(){
        new AsyncTask<Void, Void, Void>() {
            JSONObject jsonObject = null;

            @Override
            protected Void doInBackground(Void... params) {
                JSONParser jsonParser = new JSONParser();
                jsonObject = jsonParser.makeHttpRequest(Constants.BASE_URL_SINGLE, "POST", getBrandsRequestParams(), new IHttpExceptionListener() {
                    @Override
                    public void handleException(String exception) {
                        Log.e("", "exception" + exception);
                    }
                });
                if (jsonObject != null) {
                    try {
                        String STATUS = jsonObject.getString("STATUS");
                        if (STATUS.equals("SUCCESS")) {
//                            JSONObject RESPONSE = jsonObject.getJSONObject("RESPONSE");
                            JSONArray results = jsonObject.getJSONArray("RESPONSE");
                            for (int i = 0; i < results.length(); i++) {
                                Brands brands = new Brands();
                                if (!results.getJSONObject(i).getString("brand_name").equals(""))
                                {
                                    brands.setBrand_name(results.getJSONObject(i).getString("brand_name"));
                                    brands.setId(results.getJSONObject(i).getString("id"));
                                    arrayListBrands.add(brands);
                                }

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return null;
            }
        }.execute();
    }
}

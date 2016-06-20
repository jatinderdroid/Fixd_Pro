package com.fixtconsumer.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.HasPowerSourceAdapter;
import com.fixtconsumer.adapters.PowerSorceExpandListAdapter;
import com.fixtconsumer.beans.AppliancesModal;
import com.fixtconsumer.beans.HasPowerSourceBean;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HasPowerSourceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HasPowerSourceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HasPowerSourceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HasPowerSourceFragment() {
        // Required empty public constructor
    }
    ListView lstChoosePowerSource ;
    Fragment fragment = null ;
    SharedPreferences _prefs = null;
    Context _context = null ;

    HasPowerSourceAdapter adapter;
    PowerSorceExpandListAdapter  powerSorceExpandListAdapter = null  ;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    ArrayList<AppliancesModal> appliancesModalsTemp = new ArrayList<AppliancesModal>();
    ExpandableListView lstPowerSources;
    CheckAlertDialog checkALert;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HasPowerSourceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HasPowerSourceFragment newInstance(String param1, String param2) {
        HasPowerSourceFragment fragment = new HasPowerSourceFragment();
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
        View view = inflater.inflate(R.layout.fragment_has_power_source, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(view);
        setListeners();
        lstPowerSources.setBackgroundResource(Utility.getSelectedHomeServicePhoto(requestObjectSingleTon.getHomePlansRequestObject().getChooseService().getTitle()));
        lstChoosePowerSource.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));

        adapter = new HasPowerSourceAdapter(getActivity(), getPowerSources(),getResources());
        lstChoosePowerSource.setAdapter(adapter);
        if (appliancesModalsTemp.size() > 0){
            powerSorceExpandListAdapter = new PowerSorceExpandListAdapter(_context,appliancesModalsTemp);

            lstPowerSources.setAdapter(powerSorceExpandListAdapter);
        }else {
            for (int i = 0 ; i < requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().size() ; i++){
//            if (requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).getHas_power_source().equals("1")){
                requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).setItems(getPowerSources());
//            }
            }
            powerSorceExpandListAdapter = new PowerSorceExpandListAdapter(_context,getList());

            lstPowerSources.setAdapter(powerSorceExpandListAdapter);
        }

        return view;
    }

    private void setListeners(){
        lstChoosePowerSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragment = new WhatsTheProblemFragment();
                ((HomeActivity) getActivity()).switchFragment(fragment,Constants.WHATS_THE_PROBLEM_FRAGMEMT,true,null);
            }
        });
        lstPowerSources.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if ( appliancesModalsTemp.get(groupPosition).getItems().get(childPosition).isChecked()){
                    appliancesModalsTemp.get(groupPosition).getItems().get(childPosition).setChecked(false);
                }else {
                    for (int i = 0 ; i < appliancesModalsTemp.get(groupPosition).getItems().size(); i++){
                        appliancesModalsTemp.get(groupPosition).getItems().get(i).setChecked(false);
                    }
                    appliancesModalsTemp.get(groupPosition).getItems().get(childPosition).setChecked(true);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        powerSorceExpandListAdapter.notifyDataSetChanged();
                    }
                },1000);
                powerSorceExpandListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        lstPowerSources.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }
    private void setWidgets(View view){
        lstChoosePowerSource = (ListView)view.findViewById(R.id.lstChoosePowerSource);
        lstPowerSources = (ExpandableListView)view.findViewById(R.id.lstPowerSources);
        lstPowerSources.setGroupIndicator(null);

    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.HAS_POWER_SOURCE_FRAGMENT);
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
    public ArrayList<HasPowerSourceBean> getPowerSources(){
        ArrayList<HasPowerSourceBean> arrayList = new ArrayList<HasPowerSourceBean>();
        arrayList.add(new HasPowerSourceBean("Electric",false));
        arrayList.add(new HasPowerSourceBean("Gas",false));
        arrayList.add(new HasPowerSourceBean("Other",false));
        return arrayList;
    }
    private ArrayList<AppliancesModal> getList(){

        appliancesModalsTemp.clear();
        for (int i = 0 ; i < requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().size() ; i++){
            if (requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).getHas_power_source().equals("1") &&  Integer.parseInt(requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).getQuantity()) > 0){
                appliancesModalsTemp.add(requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i));
            }
        }
        return appliancesModalsTemp;
    }
    public void submit(){
        if (appliancesModalsTemp.size() == isAllSourcesSelected()){
            fragment = new WhatsTheProblemFragment();
            ((HomeActivity) getActivity()).switchFragment(fragment, Constants.WHATS_THE_PROBLEM_FRAGMEMT, true, null);
        }else {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "Please select appliance before continuing");
        }
    }
    private int isAllSourcesSelected(){
        int isSourceSelected = 0 ;
        for (int i = 0 ; i < appliancesModalsTemp.size() ;i++){
            for (int j = 0; j < appliancesModalsTemp.get(i).getItems().size() ;j++){
                if (appliancesModalsTemp.get(i).getItems().get(j).isChecked()){
                    isSourceSelected = isSourceSelected + 1;
                }
            }
        }
        return isSourceSelected;
    }
}

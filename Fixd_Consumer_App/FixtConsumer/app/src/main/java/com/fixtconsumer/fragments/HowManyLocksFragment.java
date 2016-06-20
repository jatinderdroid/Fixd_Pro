package com.fixtconsumer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.HowManyLocksAdapter;
import com.fixtconsumer.beans.AppliancesModal;
import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.beans.HowManyLocksBean;
import com.fixtconsumer.beans.TellUsMoreBean;
import com.fixtconsumer.beans.WhatsProblemBean;
import com.fixtconsumer.requests_collection.HomePlansRequestObject;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Utility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HowManyLocksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HowManyLocksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HowManyLocksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<HowManyLocksBean> arrayList ;
    HowManyLocksAdapter adapter = null ;
    ListView lstLocks ;
    private OnFragmentInteractionListener mListener;
    ArrayList<AppliancesModal> appliancesModalArrayList = new ArrayList<AppliancesModal>();
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    public HowManyLocksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HowManyLocksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HowManyLocksFragment newInstance(String param1, String param2) {
        HowManyLocksFragment fragment = new HowManyLocksFragment();
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
        arrayList = Utility.getLocksList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_how_many_locks, container, false);
        lstLocks = (ListView)rootView.findViewById(R.id.lstLocks);
        FixdConsumerApplication.selectedAppliance = "Re Key";
        lstLocks.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));
        adapter = new HowManyLocksAdapter(getActivity(),arrayList,getResources());
        lstLocks.setAdapter(adapter);
        lstLocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            AppliancesModal modal = new AppliancesModal();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    modal.setQuantity("3");
                }
                if (position == 1){
                    modal.setQuantity("4");
                }
                if (position == 2){
                    modal.setQuantity("5");
                }
                if (position == 3){
                    modal.setQuantity("10");
                }
                modal.setId("0");
                modal.setName("Re Key");
                modal.setBean(new TellUsMoreBean());
                modal.setBean(new TellUsMoreBean());
                appliancesModalArrayList.add(modal);
                HomePlansRequestObject homePlansRequestObject = new HomePlansRequestObject();
                homePlansRequestObject.setChooseService(new HomeServiceBeans());
                requestObjectSingleTon.setHomePlansRequestObject(homePlansRequestObject);
                requestObjectSingleTon.getHomePlansRequestObject().setWhichAppliancelist(appliancesModalArrayList);
                requestObjectSingleTon.getHomePlansRequestObject().setWhatsProblemBean(new WhatsProblemBean());
                requestObjectSingleTon.getHomePlansRequestObject().setWhatsProblemBean(new WhatsProblemBean());
                requestObjectSingleTon.getHomePlansRequestObject().setTypeofprojectName("Re Key");
                FixdConsumerApplication.selectedAppliance = "Re Key";
        ((HomeActivity) getActivity()).switchFragment(new WhenDoYouWantCalanderFragment(), Constants.WHEN_DO_YOU_WANT_FRAGMENT, true, null);
            }
        });
        return rootView;
    }
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.HOW_MANY_LOCKS_FARGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Next");
        ((HomeActivity)getActivity()).setTitle("Re Key");
        ((HomeActivity)getActivity()).setLeftToolBarText("Cancel");
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
}

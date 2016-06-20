package com.fixtconsumer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.CompletePlanAdapter;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.singletons.WarrentyPlanSingleton;
import com.fixtconsumer.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompletePurchasePlan.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompletePurchasePlan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletePurchasePlan extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lstCompletePlan ;
    private OnFragmentInteractionListener mListener;
    WarrentyPlanBean warrentyPlanBean ;
    public CompletePurchasePlan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletePurchasePlan.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletePurchasePlan newInstance(String param1, String param2) {
        CompletePurchasePlan fragment = new CompletePurchasePlan();
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
        warrentyPlanBean = WarrentyPlanSingleton.getInstance().getWarrentyPlanBean() ;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.COMPLETE_PURCHASE_FRAGMENT);
        setupToolBar();
    }

    private void setupToolBar() {
        ((HomeActivity) getActivity()).setRightToolBarText("Terms");
        ((HomeActivity) getActivity()).setTitletext("Complete Plan");
        ((HomeActivity) getActivity()).setLeftToolBarImage(R.drawable.cross);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  =inflater.inflate(R.layout.fragment_complete_purchase_plan, container, false);
        lstCompletePlan = (ListView)rootView.findViewById(R.id.lstCompletePlan);
        CompletePlanAdapter adapter = new CompletePlanAdapter(getActivity(),warrentyPlanBean.getSimplePlansBeanArrayList(),getResources());
        lstCompletePlan.setAdapter(adapter);
        setHeader();
        setFooter();
        return rootView;
    }
    private void setHeader(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.item_plan_header, lstCompletePlan, false);
        lstCompletePlan.addHeaderView(header, null, false);
    }
    private void setFooter(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.item_purchase_footer, lstCompletePlan, false);
        lstCompletePlan.addFooterView(header, null, false);
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

package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.adapters.WhatTypeOfServiceAdapter;
import fixtpro.com.fixtpro.singleton.CurrentServiceAddingSingleTon;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhatTypeOfServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhatTypeOfServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatTypeOfServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView lstServiceTyep ;
    Fragment fragment = null ;
    public WhatTypeOfServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatTypeOfServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatTypeOfServiceFragment newInstance(String param1, String param2) {
        WhatTypeOfServiceFragment fragment = new WhatTypeOfServiceFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_what_type_of, container, false);
        setWidgets(view);
        setListeners();
        WhatTypeOfServiceAdapter adapter = new WhatTypeOfServiceAdapter(getActivity(), CurrentServiceAddingSingleTon.getInstance().getServiceTypeList(),getResources());
        lstServiceTyep.setAdapter(adapter);
        return view;
    }
    private void setWidgets(View view){
        lstServiceTyep = (ListView)view.findViewById(R.id.lstServiceTyep);
    }
    private void setListeners(){
        lstServiceTyep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentServiceAddingSingleTon.getInstance().setSelectedServicetype(CurrentServiceAddingSingleTon.getInstance().getServiceTypeList().get(position));
                fragment = new WhichApplianceAddServiceFragment();
                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.WHICH_APPLIANCE_SERVICE_FGRAGMENT, true, null);
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
        super.onResume();
        ((HomeScreenNew) getActivity()).setCurrentFragmentTag(Constants.WHAT_TYPE_OF_SERVICE_FGRAGMENT);
        setupToolBar();
    }

    private void setupToolBar() {
        ((HomeScreenNew) getActivity()).hideRight();
        ((HomeScreenNew) getActivity()).setTitletext(CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getContact_name());
        ((HomeScreenNew) getActivity()).setLeftToolBarText("Back");
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

package com.fixtconsumer.fragments;

import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.AppliancesAdapter;
import com.fixtconsumer.adapters.CalendarAdapter;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.utils.CalendarCollection;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;
import com.fixtconsumer.views.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhenDoYouWantCalanderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhenDoYouWantCalanderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhenDoYouWantCalanderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    String selectedGridDate ;
    private TextView tv_month,tvYear;
    private OnFragmentInteractionListener mListener;
    String error_message = "";
    String[] TYPES ;
    String[] IDS ;
    CheckAlertDialog checkALert;
    WheelView wheelView;
    int SelectedIndex = 1 ;
    LinearLayout container_body ;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    public WhenDoYouWantCalanderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhenDoYouWantCalanderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhenDoYouWantCalanderFragment newInstance(String param1, String param2) {
        WhenDoYouWantCalanderFragment fragment = new WhenDoYouWantCalanderFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_when_do_you_want_calander, container, false);
        checkALert = new CheckAlertDialog();
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(getActivity(), cal_month);

        tv_month = (TextView) rootView.findViewById(R.id.tv_month);
        tvYear = (TextView) rootView.findViewById(R.id.tvYear);
        tv_month.setText(android.text.format.DateFormat.format("MMMM", cal_month));
        tvYear.setText(android.text.format.DateFormat.format("yyyy", cal_month));
        ImageButton previous = (ImageButton) rootView.findViewById(R.id.ib_prev);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();

            }
        });

        ImageButton next = (ImageButton) rootView.findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });


        GridView gridview = (GridView) rootView.findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
                selectedGridDate = CalendarAdapter.day_string
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                selectedGridDate = separatedTime[2] + "-" + separatedTime[1] + "-"+ separatedTime[0];
            }


        });

        wheelView = (WheelView) rootView.findViewById(R.id.wheelView);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                SelectedIndex = selectedIndex;
            }
        });
        container_body = (LinearLayout)rootView.findViewById(R.id.container_body);
        container_body.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));
        getTimeSlots();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.WHEN_DO_YOU_WANT_FRAGMENT);
        setupToolBar();

    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Next");
        if (FixdConsumerApplication.selectedAppliance.equals("Re Key"))
            ((HomeActivity)getActivity()).setTitletext("Re Key");
        else
            ((HomeActivity) getActivity()).setTitletext("Appliances - "+requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName());

        ((HomeActivity)getActivity()).setLeftToolBarText("Cancel");
    }
    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM", cal_month));
        tvYear.setText(android.text.format.DateFormat.format("yyyy", cal_month));

    }
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    private void getTimeSlots(){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",timeSlotResponseListener,timeSlotExceptionListener,getActivity(),"getting");
        apiResponseAsync.execute(getRequestParamsTimeSlot());
    }
    private HashMap<String,String> getRequestParamsTimeSlot(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","time_slots");
        hashMap.put("per_page","20");
        hashMap.put("page", "1");
        hashMap.put("token", Utility.getSharedPreferences(getActivity()).getString(Preferences.AUTH_TOKEN, null));

        return hashMap;
    }
    IHttpResponseListener timeSlotResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray result = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    IDS = new String[result.length()];
                    TYPES = new String[result.length()];
                    for (int i = 0 ; i < result.length() ; i++){
                        JSONObject jsonObject = result.getJSONObject(i);
                        IDS[i] = jsonObject.getString("id");
                        TYPES[i] = jsonObject.getString("start") + " - " + jsonObject.getString("end");
                    }
                    handler.sendEmptyMessage(0);
                }else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
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
                    wheelView.setItems(Arrays.asList(TYPES));
                    break;
                }
                case 1: {
                    checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
            }
        }
    };
    IHttpExceptionListener timeSlotExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    } ;
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
    public void submit(){
        if (selectedGridDate != null){
            requestObjectSingleTon.getHomePlansRequestObject().setTimeSlotId(IDS[SelectedIndex-1]);
            requestObjectSingleTon.getHomePlansRequestObject().setTimeSlotName(TYPES[SelectedIndex-1]);
            requestObjectSingleTon.getHomePlansRequestObject().setSchedulingDate(selectedGridDate);
            ((HomeActivity)getActivity()).switchFragment(new JobSummaryFragment(), Constants.JOB_SUMMARY_FRAGMENT, true, null);
        }else{
            checkALert.showcheckAlert(getActivity(),getString(R.string.alert_title),"Please select date");
        }
    }
}

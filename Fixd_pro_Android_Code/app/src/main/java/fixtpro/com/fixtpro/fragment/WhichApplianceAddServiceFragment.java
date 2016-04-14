package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.ResponseListener;
import fixtpro.com.fixtpro.adapters.AppliancesAdapter;
import fixtpro.com.fixtpro.adapters.InstallOrRepairJobAdapter;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.beans.install_repair_beans.AppliancesModal;
import fixtpro.com.fixtpro.singleton.CurrentServiceAddingSingleTon;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhichApplianceAddServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhichApplianceAddServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhichApplianceAddServiceFragment extends Fragment {
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
    public WhichApplianceAddServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhichApplianceAddServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhichApplianceAddServiceFragment newInstance(String param1, String param2) {
        WhichApplianceAddServiceFragment fragment = new WhichApplianceAddServiceFragment();
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
        _context = getActivity();
        _prefs = Utilities.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_which_appliance_add, container, false);
        setWidgets(view);
        setListeners();
        getAppliances();
        return view;
    }
    private void getAppliances(){
        GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",getAppliancesListener,getActivity(),"Getting.");
        getApiResponseAsync.execute(getAppliancesParams());
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
                AppliancesModal appliancesModal = appliancesModalArrayList.get(position);
                CurrentServiceAddingSingleTon.getInstance().setSelectedApplianceId(appliancesModal.getId());
                if (appliancesModal.getHas_power_source().equals("0")){
                    addServiceRequest();
                }else {
                    // go to choose power source screen
                     fragment = new HasPowerSourceFragment();

                     ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.HAS_POWERSOURCE_FRAGMENT, true, null);
                }
            }
        });
    }
    private HashMap<String,String> getAppliancesParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","appliance_types");
        hashMap.put("select","^*");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN,""));
        hashMap.put("where[service_id]", CurrentServiceAddingSingleTon.getInstance().getSkillTrade().getId()+"");
        return hashMap;
    }
    ResponseListener getAppliancesListener = new ResponseListener() {
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
                    showAlertDialog("Fixd-Pro", error_message);
                    break;
                }
                case 2:{
//                    pop fragment till repair or install
                    ((HomeScreenNew) getActivity()).popInclusiveFragment(Constants.ADD_SERVICE_FRAGMENT);
                    break;
                }
            }
        }
    };
    private void addServiceRequest(){
        GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",addServiceResponseListener,getActivity(),"Saving.");
        getApiResponseAsync.execute(getAddServiceParams());
    }
    private HashMap<String,String> getAddServiceParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","add_appliance");
        hashMap.put("object","jobs");
        hashMap.put("data[appliance_id]",CurrentServiceAddingSingleTon.getInstance().getSelectedApplianceId());
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[service_type]", CurrentServiceAddingSingleTon.getInstance().getSelectedServicetype() + "");
        hashMap.put("job_id", CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getId() + "");

        return hashMap;
    }
    ResponseListener addServiceResponseListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    error_message = Response.getString("RESPONSE");
                    handler.sendEmptyMessage(2);
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew) getActivity()).setCurrentFragmentTag(Constants.WHICH_APPLIANCE_SERVICE_FGRAGMENT);
         setupToolBar();
    }

    private void setupToolBar() {
        ((HomeScreenNew) getActivity()).hideRight();
        ((HomeScreenNew) getActivity()).setTitletext(CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getContact_name());
        ((HomeScreenNew) getActivity()).setLeftToolBarText("Back");
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
    private void showAlertDialog(String Title,String Message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                _context);

        // set title
        alertDialogBuilder.setTitle(Title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}

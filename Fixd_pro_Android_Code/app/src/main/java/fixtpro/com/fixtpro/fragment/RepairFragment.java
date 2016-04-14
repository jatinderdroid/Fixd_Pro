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
import fixtpro.com.fixtpro.adapters.RepairTypeAdapter;
import fixtpro.com.fixtpro.beans.install_repair_beans.RepairType;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepairFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepairFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepairFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SharedPreferences _prefs = null ;
    Context _context = null ;
    ListView listViewInstallType = null ;
    String error_message = "";
    ArrayList<RepairType> arrayList = new ArrayList<RepairType>();
    public RepairFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepairFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepairFragment newInstance(String param1, String param2) {
        RepairFragment fragment = new RepairFragment();
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
        View view = inflater.inflate(R.layout.fragment_repair, container, false);
        setWidgets(view);
        setListeners();
        GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",getRepairTypesListener,getActivity(),"Getting.");
        getApiResponseAsync.execute(getRepairOrInstallTypeRequestParams());
        return view;
    }
    private void setWidgets(View view){
        listViewInstallType = (ListView)view.findViewById(R.id.listViewInstallType);
    }
    private void setListeners(){
        listViewInstallType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentScheduledJobSingleTon.getInstance().getInstallOrRepairModal().getRepairType().setType(arrayList.get(position).getType());
                CurrentScheduledJobSingleTon.getInstance().getInstallOrRepairModal().getRepairType().setId(arrayList.get(position).getId());
                CurrentScheduledJobSingleTon.getInstance().getInstallOrRepairModal().getRepairType().setPrice(arrayList.get(position).getPrice());
                CurrentScheduledJobSingleTon.getInstance().getCurrentReapirInstallProcessModal().setIsCompleted(true);
                ((HomeScreenNew) getActivity()).popInclusiveFragment(Constants.REPAIR_TYPE_FRAGMENT);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.REPAIR_TYPE_FRAGMENT);
        setupToolBar();

    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).hideRight();
        ((HomeScreenNew)getActivity()).setTitletext("Repair Types");
        ((HomeScreenNew)getActivity()).setLeftToolBarText("Back");
    }
    private HashMap<String,String> getRepairOrInstallTypeRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","appliance_types");
        if (true)
            hashMap.put("select","install_types.name,install_types.cost,install_types.id");
        else
            hashMap.put("select","repair_types.name,repair_types.cost,repair_types.id");
        hashMap.put("token",_prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("where[id]", CurrentScheduledJobSingleTon.getInstance().getJobApplianceModal().getJob_appliances_appliance_id());
        return hashMap;
    }
    ResponseListener getRepairTypesListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    for (int i = 0 ; i < results.length() ; i++){
                        JSONObject jsonObject = results.getJSONObject(i);

                        JSONArray jsonArrayRepairType = null;
                        if (!jsonObject.isNull("repair_types")){
                            jsonArrayRepairType = jsonObject.getJSONArray("repair_types");
                        }else if (!jsonObject.isNull("install_types")){
                            jsonArrayRepairType = jsonObject.getJSONArray("install_types");
                        }
                        for (int j = 0 ; j < jsonArrayRepairType.length() ; j++){
                            JSONObject jsonObjectRepairType =  jsonArrayRepairType.getJSONObject(j);
                            RepairType repairType = new RepairType();
                            repairType.setId(jsonObjectRepairType.getString("id"));
                            repairType.setType(jsonObjectRepairType.getString("name"));
                            repairType.setPrice(jsonObjectRepairType.getString("cost"));
                            arrayList.add(repairType);
                        }
                    }
                    handler.sendEmptyMessage(0);
                } else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);

                    }
                    handler.sendEmptyMessage(1);
                }
            }catch (JSONException e){

            }
        }
    };

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    RepairTypeAdapter adapter = new RepairTypeAdapter(getActivity(),arrayList,getResources());
                    listViewInstallType.setAdapter(adapter);
                    break;
                }case 1:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
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

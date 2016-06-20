package com.fixtconsumer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.CommonPlans;
import com.fixtconsumer.activities.CompletePlans;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.SettingAddressesAdapter;
import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.listeners.AddressSettingsOperationListener;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.singletons.WarrentyPlanSingleton;
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
 * {@link SettingAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingAddressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SettingAddressesAdapter adapter = null;
    private OnFragmentInteractionListener mListener;
    ListView lstAddresses;
    SharedPreferences _prefs = null;
    Context _context = null;
    ArrayList<AddressesBean> arrayList = AddressesSingleTonClasses.getInstance().arrayList ;
    ArrayList<AddressesBean> arrayListFileAClaim = AddressesSingleTonClasses.getInstance().arrayListFileAClain ;
    CheckAlertDialog checkALert;
    String error_message = "";
    int operationalPostiona = 0 ;
    private boolean  isclaim = false;
    private boolean  ispurchase = false;
    private boolean  iscomplete = false;
    boolean mAlreadyLoaded = false ;
    public SettingAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingAddressFragment newInstance(String param1, String param2) {
        SettingAddressFragment fragment = new SettingAddressFragment();
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
            ispurchase = bundle.getBoolean("purchase");
            if (ispurchase)
                iscomplete = bundle.getBoolean("complete");
        }
        checkALert = new CheckAlertDialog();
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.SETTING_ADDRESS_FRAGMENT);
        setupToolBar();

    }

    private void setupToolBar() {
        if (!isclaim && !ispurchase)
            ((HomeActivity) getActivity()).setRightToolBarText("Save");
        else
            ((HomeActivity) getActivity()).setRightToolBarText("Next");
        ((HomeActivity) getActivity()).setTitletext("My Addresses");
        ((HomeActivity) getActivity()).setLeftToolBarText("Back");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting_address, container, false);
        lstAddresses = (ListView) rootView.findViewById(R.id.lstAddresses);
//        getList();
        if (isclaim)
            arrayList = AddressesSingleTonClasses.getInstance().arrayListFileAClain ;
        else
            arrayList = AddressesSingleTonClasses.getInstance().arrayList ;
            adapter = new SettingAddressesAdapter(getActivity(), arrayList, getResources(), listener);

        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            // Do this code only first time, not after rotation or reuse fragment from backstack
        }else {
            adapter.alreadyLoaded();
        }
            lstAddresses.setAdapter(adapter);
            setHeader();

            if (!isclaim && !ispurchase)
                setFooter();
        return rootView;
    }

    private void setHeader() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.item_aadress_header, lstAddresses, false);
        lstAddresses.addHeaderView(header, null, false);
    }

    private void setFooter() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.item_add_address_footer, lstAddresses, false);
        Button btnNewAddress = (Button) header.findViewById(R.id.btnNewAddress);
        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isnew",true);
                ((HomeActivity) getActivity()).switchFragment(new EditAddressFragment(),Constants.SETTING_EDIT_ADDRESS_FRAGMENT,true,bundle1);
            }
        });
        lstAddresses.addFooterView(header, null, false);
    }

    private void getList() {
        try {
            arrayList.clear();
            JSONArray jsonArray = new JSONArray(_prefs.getString(Preferences.FULL_ADDREESS_LIST_ARRAY, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AddressesBean modal = new AddressesBean();
                modal.setId(jsonObject.getString("id"));
                modal.setZip(jsonObject.getString("zip"));
                modal.setCity(jsonObject.getString("city"));
                modal.setState(jsonObject.getString("state"));
                modal.setAddress(jsonObject.getString("address"));
                modal.setAddress_2(jsonObject.getString("address_2"));
                modal.setDefault_address(jsonObject.getString("default"));
                arrayList.add(modal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    AddressSettingsOperationListener listener = new AddressSettingsOperationListener() {
        @Override
        public void handleOperation(int pos, String Opration) {

            Bundle bundle = new Bundle();
            bundle.putInt("pos", pos);
            bundle.putString("operation", Opration);
            Message msg = new Message();
            msg.setData(bundle);
            msg.what = 0;
            handler.sendMessage(msg);
        }
    };
    private void selecteDefultAddress(int pos) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setDefault_address("0");
        }
        arrayList.get(pos).setDefault_address("1");
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    Bundle bundle = msg.getData();
                    operationalPostiona =bundle.getInt("pos") ;
                    if (bundle.getString("operation").equals("select")) {
//                        uncheckAll Addreses
                        selecteAddress(bundle.getInt("pos"));
                    } else if (bundle.getString("operation").equals("delete")) {
                        // If file a Claim flow disable delete address functionality
                        if (isclaim)
                            return;
//                        delete Address
                        if (arrayList.get(bundle.getInt("pos")).getDefault_address().equals("1"))
                            checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title),"You cannot delete your selected address");
                        else{

                            showDeletePopUp(bundle.getInt("pos"));}
                    } else if (bundle.getString("operation").equals("edit")) {
                        // If file a Claim flow disable edit address functionality
                        if (isclaim)
                            return;
                        Bundle bundle1 = new Bundle();
                        bundle1.putBoolean("isnew",false);
                        bundle1.putInt("position", bundle.getInt("pos"));
                        ((HomeActivity) getActivity()).switchFragment(new EditAddressFragment(),Constants.SETTING_EDIT_ADDRESS_FRAGMENT,true,bundle1);
                    }
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
                case 2:{
                    arrayList.remove(operationalPostiona);
                    adapter.notifyDataSetChanged();
                    break;
                }
                case 3:{
                    ((HomeActivity)getActivity()).popStack();
                    break;
                }
            }

        }
    };



    private void showDeletePopUp(final int pos) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_delete_popup);
        dialog.setCancelable(false);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtTopInstruction = (TextView) dialog.findViewById(R.id.txtTopInstruction);
        TextView txtAddress = (TextView) dialog.findViewById(R.id.txtAddress);
        TextView txtPin = (TextView) dialog.findViewById(R.id.txtPin);
        TextView txtDelete = (TextView) dialog.findViewById(R.id.txtDelete);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });
        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSlectedAddress(pos);
               dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void selecteAddress(int pos) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setIsChecked(false);
        }
        arrayList.get(pos).setIsChecked(true);
        adapter.notifyDataSetChanged();
    }

    private void deleteSlectedAddress(int pos){
        GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",deleteresponseListener,deleteexceptionListener,getActivity(),"Deleting.");
        apiResponseAsync.execute(getDeleteParams(pos));
    }
    private  HashMap<String,String> getDeleteParams(int pos){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","delete");
        hashMap.put("object", "customer_addresses");
        hashMap.put("data[id]", arrayList.get(pos).getId());
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
    IHttpResponseListener deleteresponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    handler.sendEmptyMessage(2);
                }
                else{
                    JSONObject errors = response.getJSONObject("ERRORS");
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
    IHttpExceptionListener deleteexceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception ;
            handler.sendEmptyMessage(1);
        }
    };
    public void submit(){
//        set Default Address..
        if (isclaim){
            HomeRequestObjectSingleTon.getInstance().address_file_a_claim_id =  getDefaultAddressId();
            if (HomeRequestObjectSingleTon.getInstance().address_file_a_claim_id.length() == 0){
                checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title),"Please select an address before continuing");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("isclaim", true);
            ((HomeActivity) getActivity()).switchFragment(new HomeServiceFragment(), Constants.HOME_SERVICE_FRAGMENT, true, bundle);
        }else if(ispurchase){
            WarrentyPlanSingleton.getInstance().AddressId = getDefaultAddressId();
            if (iscomplete){
                Intent intent = new Intent(getActivity(), CompletePlans.class);
                intent.putExtra("isnew",true);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getActivity(), CommonPlans.class);
                intent.putExtra("isnew",true);
                startActivity(intent);
            }
        }else{
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",defaultAddressResponseListener,defaultAddressExceptioListener,getActivity(),"Saving.");
            apiResponseAsync.execute(getDefaultAddressParam());
        }
    }
    private  HashMap<String,String> getDefaultAddressParam(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","update");
        hashMap.put("object", "customer_addresses");
        hashMap.put("data[id]",getDefaultAddressId());
        hashMap.put("data[default]","1");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        return hashMap;
    }
    private String getDefaultAddressId(){
        String id = "";
        for (int i = 0 ; i < arrayList.size() ; i++){
            if (arrayList.get(i).isChecked() == true){
                id = arrayList.get(i).getId();
                break;
            }
        }
        return id;
    }
    IHttpResponseListener defaultAddressResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("",""+response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    selecteDefultAddress(operationalPostiona);
                    _prefs.edit().putString(Preferences.ADDRESS,arrayList.get(operationalPostiona).getAddress()).commit();
                    handler.sendEmptyMessage(3);
                }
                else{
                    JSONObject errors = response.getJSONObject("ERRORS");
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
    IHttpExceptionListener defaultAddressExceptioListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception ;
        }
    };
}

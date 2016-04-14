package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.content.DialogInterface;
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
import fixtpro.com.fixtpro.adapters.AddServiceAdapter;
import fixtpro.com.fixtpro.adapters.TradeSkillAdapter;
import fixtpro.com.fixtpro.beans.SkillTrade;
import fixtpro.com.fixtpro.singleton.CurrentServiceAddingSingleTon;
import fixtpro.com.fixtpro.singleton.TradeSkillSingleTon;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<SkillTrade> skillTrades ;
    AddServiceAdapter addServiceAdapter = null;
    ListView lstTradeSkill;
    ArrayList<SkillTrade> arrayList = new ArrayList<SkillTrade>();
    String error_message =  "";
    Context _context ;
    Fragment fragment = null ;
    public AddServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddServiceFragment newInstance(String param1, String param2) {
        AddServiceFragment fragment = new AddServiceFragment();
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
        }
        _context = getActivity();
        skillTrades = TradeSkillSingleTon.getInstance().getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        setWidgets(view);
        setListeners();
        if (skillTrades.size() > 0){
            handler.sendEmptyMessage(0);
        }else {
            GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",getTradeSkillListener,getActivity(),"Getting.");
            getApiResponseAsync.execute(getTradeSkillRequestParams());
        }

        return view;
    }
    private void setListAdapter(){

        addServiceAdapter = new AddServiceAdapter(getActivity(),skillTrades,getResources());
        lstTradeSkill.setAdapter(addServiceAdapter);

    }
    private void setWidgets(View view){
        lstTradeSkill = (ListView)view.findViewById(R.id.lstTradeSkill);
    }
    private void setListeners(){
        lstTradeSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragment = new WhatTypeOfServiceFragment();
                CurrentServiceAddingSingleTon.getInstance().setSkillTrade(TradeSkillSingleTon.getInstance().getList().get(position));
                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.WHAT_TYPE_OF_SERVICE_FGRAGMENT, true, null);
            }
        });
    }
    ResponseListener getTradeSkillListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject jsonObject) {
            Log.e("", "Response" + jsonObject);
            try {
                String STATUS = jsonObject.getString("STATUS");
                if (STATUS.equals("SUCCESS")){
                    JSONObject RESPONSE = jsonObject.getJSONObject("RESPONSE");
                    JSONArray results = RESPONSE.getJSONArray("results");
                    for (int i = 0 ; i < results.length() ; i++){
                        JSONObject object = results.getJSONObject(i);
                        SkillTrade skillTrade = new SkillTrade();
                        skillTrade.setId(object.getInt("id"));
                        skillTrade.setTitle(object.getString("name"));
                        arrayList.add(skillTrade);
                    }
                    TradeSkillSingleTon.getInstance().setList(arrayList);
                    handler.sendEmptyMessage(0);
                }else {
                    JSONObject errors = jsonObject.getJSONObject("ERRORS");
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    setListAdapter();
                    break;
                }case 1:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
                default:{
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
    public void onResume() {
        super.onResume();
        ((HomeScreenNew) getActivity()).setCurrentFragmentTag(Constants.ADD_SERVICE_FRAGMENT);
        setupToolBar();
    }

    private void setupToolBar() {
        ((HomeScreenNew) getActivity()).hideRight();
        ((HomeScreenNew) getActivity()).setTitletext(CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getContact_name());
        ((HomeScreenNew) getActivity()).setLeftToolBarText("Back");
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
    private HashMap<String,String> getTradeSkillRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","services");
        hashMap.put("select","^*");
        hashMap.put("per_page","999");
        hashMap.put("page","1");
        return hashMap;
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

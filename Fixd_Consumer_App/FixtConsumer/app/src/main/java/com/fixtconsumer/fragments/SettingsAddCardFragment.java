package com.fixtconsumer.fragments;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.CardsAdapter;
import com.fixtconsumer.beans.CreditCardBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.CreditCardsSingleton;
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
 * {@link SettingsAddCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsAddCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsAddCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lstCards;
    CheckAlertDialog checkALert;
    Context _context = null ;
    String error_message = "";
    SharedPreferences _prefs = null ;
    CreditCardsSingleton singleton = CreditCardsSingleton.getInstance();
    ArrayList<CreditCardBean> arrayList = singleton.arrayList ;
    private OnFragmentInteractionListener mListener;
    CardsAdapter adapter = null ;
    public SettingsAddCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsAddCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsAddCardFragment newInstance(String param1, String param2) {
        SettingsAddCardFragment fragment = new SettingsAddCardFragment();
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
        checkALert = new CheckAlertDialog();
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings_add_card, container, false);
        lstCards = (ListView)rootView.findViewById(R.id.lstCards);
        lstCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isnew", false);
                bundle.putInt("pos", position);
                ((HomeActivity) getActivity()).switchFragment(new SettingsSavingCardFragment(), Constants.SETTING_SAVING_CARD_FRAGMENT, true, bundle);
            }
        });
        if (arrayList.size() == 0){
            GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE,"POST",cardsResponseListener,cardsExceptionListener,getActivity(),"getting.");
            apiResponseAsync.execute(getSavedCardsParams());
        }else {
            handler.sendEmptyMessage(0);
        }
        return rootView;
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
    public void onResume() {
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.SETTING_ADD_CARD_FRAGMENT);
        setupToolBar();
        super.onResume();
    }

    private void setupToolBar() {
        ((HomeActivity) getActivity()).hideRight();
        ((HomeActivity) getActivity()).setTitletext("Payment");
        ((HomeActivity) getActivity()).setLeftToolBarText("Back");
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
    private HashMap<String,String> getSavedCardsParams() {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object", "cards");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN,""));
        return hashMap;
    }
    IHttpResponseListener cardsResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
//                    handler.sendEmptyMessage(0);
                    JSONArray RESPONSE = response.getJSONArray("RESPONSE");
                    arrayList.clear();
                    for (int i = 0 ; i < RESPONSE.length() ; i++){
                        JSONObject object = RESPONSE.getJSONObject(i);
                        CreditCardBean modal = new CreditCardBean();
                        modal.setId(object.getString("id"));
                        modal.setFirstname(object.getString("firstname"));
                        modal.setLastname(object.getString("lastname"));
                        modal.setCard_number(object.getString("card_number"));
                        modal.setCvv(object.getString("cvv"));
                        modal.setYear(object.getString("year"));
                        modal.setZipcode(object.getString("zip_code"));
                        modal.setPrimary(object.getString("primary"));
                        arrayList.add(modal);
                    }
                    handler.sendEmptyMessage(0);
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
    IHttpExceptionListener cardsExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception ;
            handler.sendEmptyMessage(1);
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    adapter = new CardsAdapter(getActivity(),arrayList,getResources());
                    lstCards.setAdapter(adapter);
                    setFooter();
                    break;
                }
                case 1:{
                    checkALert.showcheckAlert(getActivity(), _context.getResources().getString(R.string.alert_title),error_message);
                    break;
                }
            }
        }
    };
    private void setFooter() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.item_add_card_footer, lstCards, false);
        TextView btnNewAddress = (TextView) header.findViewById(R.id.btnAddCard);
        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isnew",true);
                ((HomeActivity) getActivity()).switchFragment(new SettingsSavingCardFragment(),Constants.SETTING_SAVING_CARD_FRAGMENT,true,bundle1);
            }
        });
        lstCards.addFooterView(header, null, false);
    }
}

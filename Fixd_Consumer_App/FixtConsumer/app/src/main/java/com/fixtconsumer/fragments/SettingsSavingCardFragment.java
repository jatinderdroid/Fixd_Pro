package com.fixtconsumer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.beans.CreditCardBean;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.singletons.CreditCardsSingleton;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsSavingCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsSavingCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsSavingCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editCardNo, editYear, editMonth, editCw, editZipCode, editFirstName, editLastName;
    TextView txtAlmostDone, txtScanCard;
    LinearLayout btnScanCard;
    ImageView img_camra, img_keypad;
    private Typeface fontfamily;
    private OnFragmentInteractionListener mListener;
    String card_number = "", month = "", year = "", cvv = "", zip_code = "", first_name = "", last_name = "";
    CheckAlertDialog checkALert;
    Context _context = null ;
    String error_message = "";
    SharedPreferences _prefs = null ;
    private static final int MY_SCAN_REQUEST_CODE = 200;
    int position = 0 ;
    boolean isnew = false ;
    String toolBarText = "Edit Address";
    CreditCardBean modal ;
    public SettingsSavingCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsSavingCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsSavingCardFragment newInstance(String param1, String param2) {
        SettingsSavingCardFragment fragment = new SettingsSavingCardFragment();
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
            Bundle bundle = getArguments() ;
            isnew = bundle.getBoolean("isnew");
            if (isnew){
                toolBarText = "Add Card";
            }
            else{
                toolBarText = "Edit card";
                position = bundle.getInt("position");
                modal = CreditCardsSingleton.getInstance().arrayList.get(position);
                card_number = modal.getCard_number();
                first_name = modal.getFirstname();
                last_name = modal.getLastname();
                cvv = modal.getCvv();
                zip_code = modal.getZipcode();
                month = modal.getMonth();
                year = modal.getYear();
            }

        }
        checkALert = new CheckAlertDialog();
        _context = getActivity() ;
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings_saving_card, container, false);
        setWidgets(rootView);
        setyTypeFace();
        setListeners();
        initLayout();
        return rootView;
    }
    private void initLayout(){
        editCardNo.setText(card_number);
        editFirstName.setText(first_name);
        editLastName.setText(last_name);
        editCw.setText(cvv);
        editZipCode.setText(zip_code);
        editMonth.setText(month);
        editYear.setText(year);
    }
    private void setListeners(){
        txtScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress(v);
            }
        });
    }
    private void setyTypeFace(){
        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        txtAlmostDone.setTypeface(fontfamily);
        editCardNo.setTypeface(fontfamily);
        editYear.setTypeface(fontfamily);
        editMonth.setTypeface(fontfamily);
        editCw.setTypeface(fontfamily);
        editFirstName.setTypeface(fontfamily);
        editLastName.setTypeface(fontfamily);
        editZipCode.setTypeface(fontfamily);
        txtScanCard.setTypeface(fontfamily);
    }
    private void setWidgets(View rootView){
        txtAlmostDone = (TextView) rootView.findViewById(R.id.txtAlmostDone);
        editCardNo = (EditText) rootView.findViewById(R.id.editCardNo);
        img_keypad = (ImageView) rootView.findViewById(R.id.img_keypad);
        editYear = (EditText) rootView.findViewById(R.id.editYear);
        editMonth = (EditText) rootView.findViewById(R.id.editMonth);
        editCw = (EditText) rootView.findViewById(R.id.editCw);
        editZipCode = (EditText) rootView.findViewById(R.id.editZipCode);
        editFirstName = (EditText) rootView.findViewById(R.id.editFirstName);
        editLastName = (EditText) rootView.findViewById(R.id.editLastName);
        img_camra = (ImageView) rootView.findViewById(R.id.img_camra);
        txtScanCard = (TextView) rootView.findViewById(R.id.txtScanCard);
        btnScanCard = (LinearLayout) rootView.findViewById(R.id.layout3);
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
    @Override
    public void onResume() {
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.SETTING_SAVING_CARD_FRAGMENT);
        setupToolBar();
        super.onResume();
    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Save");
        ((HomeActivity)getActivity()).setTitletext(toolBarText);
        ((HomeActivity)getActivity()).setLeftToolBarText("Back");
    }
    public void submit(){
        card_number = editCardNo.getText().toString().trim();
        month = editMonth.getText().toString().trim();
        year = editYear.getText().toString().trim();
        cvv = editCw.getText().toString().trim();
        zip_code = editZipCode.getText().toString().trim();
        first_name = editFirstName.getText().toString().trim();
        last_name = editLastName.getText().toString().trim();
        if (card_number.length() == 0) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter card number");
        } else if (card_number.length() != 16) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter a valid card number");
        } else if (month.length() == 0) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter month");
        } else if (year.length() == 16) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter a year");
        } else if (year.length() < 4) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter a valid a valid year");
        } else if (cvv.length() == 0) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter CVV");
        } else if (cvv.length() < 3) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter a valid a CVV number");
        } else if (zip_code.length() == 0) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter Zip");
        } else if (first_name.length() == 0) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter first name");
        } else if (last_name.length() == 0) {
            checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter last name");
        } else {
            if (isnew){
                GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", addCardResponseListener, addCardExceptionListener, getActivity(), "Saving");
                apiResponseAsync.execute(getAddCardPareams());
            }else {
                GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", addCardResponseListener, addCardExceptionListener, getActivity(), "Updating");
                apiResponseAsync.execute(getAddCardPareams());
            }
        }
    }
    private HashMap<String, String> getAddCardPareams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (isnew){
            hashMap.put("api", "create");
            if (CreditCardsSingleton.getInstance().arrayList.size() == 0)
                hashMap.put("data[primary]", "1");
            else
                hashMap.put("data[primary]", "0");
        }else{
            hashMap.put("api", "update");
            hashMap.put("data[id]", modal.getId());
            hashMap.put("data[primary]", modal.getPrimary());
        }

        hashMap.put("object", "cards");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[card_number]", card_number);
        hashMap.put("data[month]", month);
        hashMap.put("data[firstname]", first_name);
        hashMap.put("data[lastname]", last_name);
        hashMap.put("data[year]", year);
        hashMap.put("data[cvv]", cvv);
//        hashMap.put("data[zip_code]", zip_code);

        hashMap.put("id", _prefs.getString(Preferences.ID,""));

        return hashMap;
    }
    IHttpResponseListener addCardResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
                    _prefs.edit().putString(Preferences.HAS_CARD, "1").commit();
                    JSONObject object = response.getJSONObject("RESPONSE");
                    if (isnew){
                        CreditCardBean creditCardBean = new CreditCardBean();
                        creditCardBean.setId(object.getString("id"));
                        creditCardBean.setFirstname(object.getString("firstname"));
                        creditCardBean.setLastname(object.getString("lastname"));
                        creditCardBean.setCard_number(object.getString("card_number"));
                        creditCardBean.setCvv(object.getString("cvv"));
                        creditCardBean.setYear(object.getString("year"));
//                        creditCardBean.setZipcode(object.getString("zip_code"));
                        creditCardBean.setPrimary(object.getString("primary"));
                        CreditCardsSingleton.getInstance().arrayList.add(0,creditCardBean);
                    }else {

                        modal.setId(object.getString("id"));
                        modal.setFirstname(object.getString("firstname"));
                        modal.setLastname(object.getString("lastname"));
                        modal.setCard_number(object.getString("card_number"));
                        modal.setCvv(object.getString("cvv"));
                        modal.setYear(object.getString("year"));
//                        modal.setZipcode(object.getString("zip_code"));
                        modal.setPrimary(object.getString("primary"));
                    }
                    handler.sendEmptyMessage(0);

                } else {
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
    IHttpExceptionListener addCardExceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
//                    Now Card is Added its the call for Schedule
                    ((HomeActivity) getActivity()).popStack();
                    break;
                }
                case 1: {
                    checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
            }
        }
    };

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true); // default: false
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                ;

                editCardNo.setText(scanResult.getRedactedCardNumber().replace(" ", ""));
                card_number = scanResult.cardNumber;

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    editYear.setText(scanResult.expiryYear + "");
                    editMonth.setText(scanResult.expiryMonth + "");
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                    editCw.setText(scanResult.cvv);
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                    editZipCode.setText(scanResult.postalCode);
                }
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
}

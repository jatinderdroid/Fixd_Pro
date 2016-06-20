package com.fixtconsumer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.CoverFlowAdapter;
import com.fixtconsumer.adapters.HomeServicesAdapter;
import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.beans.AppliancesModal;
import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.net.GetApiResponseAsync;
import com.fixtconsumer.net.GetApiResponseAsyncMutipart;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.IHttpResponseListener;
import com.fixtconsumer.net.MultipartUtility;
import com.fixtconsumer.requests_collection.RequestConstants;
import com.fixtconsumer.singletons.AddressesSingleTonClasses;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.Preferences;
import com.fixtconsumer.utils.Utility;
import com.fixtconsumer.views.FeatureCoverFlowCustom;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobSummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobSummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //    FancyCoverFlow appliance_swipe_view;
    private OnFragmentInteractionListener mListener;
    FeatureCoverFlowCustom coverflow;
    private CoverFlowAdapter mAdapter;
    private Dialog dialog;
    private TextView txtName, txtAddress, txtTimeAndDate,txtTimeSlotName;
    EditText edtDesc;
    HomeRequestObjectSingleTon requestObjectSingleTon = HomeRequestObjectSingleTon.getInstance();
    private ArrayList<AppliancesModal> mData = requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist();
    RelativeLayout background;
    SharedPreferences _prefs = null;
    Context _context = null;
    Button btnSchedule;
    EditText editCardNo, editYear, editMonth, editCw, editZipCode, editFirstName, editLastName;
    String card_number = "", month = "", year = "", cvv = "", zip_code = "", first_name = "", last_name = "";
    private Typeface fontfamily, italic_fontfamily;
    CheckAlertDialog checkALert;
    private static final int MY_SCAN_REQUEST_CODE = 200;
    String error_message = "";
    MultipartUtility multipart = null;
    ArrayList<AppliancesModal> appliancesModalsTemp = new ArrayList<AppliancesModal>();
    public JobSummaryFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobSummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobSummaryFragment newInstance(String param1, String param2) {
        JobSummaryFragment fragment = new JobSummaryFragment();
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
        _context = getActivity();
        _prefs = Utility.getSharedPreferences(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_job_summary, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(rootView);
        setListeners();
        background.setBackgroundResource((Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance)));
        mAdapter = new CoverFlowAdapter(getActivity(), getList());
        txtName.setText(_prefs.getString(Preferences.FIRSTNAME, ""));
        txtAddress.setText(_prefs.getString(Preferences.ADDRESS, "") + " - " +_prefs.getString(Preferences.ADDRESS, ""));
        txtTimeAndDate.setText(requestObjectSingleTon.getHomePlansRequestObject().getSchedulingDate());
        txtTimeSlotName.setText(requestObjectSingleTon.getHomePlansRequestObject().getTimeSlotName());
        coverflow.setReflectionGap(60);
        coverflow.setAdapter(mAdapter);
        coverflow.setOnScrollPositionListener(new FeatureCoverFlowCustom.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //TODO CoverFlow stopped to position
                edtDesc.setText(getList().get(position).getName());
            }
            @Override
            public void onScrolling() {
                //TODO CoverFlow began scrolling
            }
        });
        return rootView;
    }

    private void setListeners() {
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_prefs.getString(Preferences.HAS_CARD, "0").equals("0")) {
//                    show Add Card Dialog
                    showAddCardDialog(false);

                } else {
//                    Just Schedule it
                    sendScheduleRequest();
                }
            }
        });
    }

    private void setWidgets(View rootView) {
        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        coverflow = (FeatureCoverFlowCustom) rootView.findViewById(R.id.coverflow);
        background = (RelativeLayout) rootView.findViewById(R.id.background);
        txtAddress = (TextView) rootView.findViewById(R.id.txtAddress);
        txtName = (TextView) rootView.findViewById(R.id.txtName);
        txtTimeAndDate = (TextView) rootView.findViewById(R.id.txtTimeAndDate);
        txtTimeSlotName = (TextView) rootView.findViewById(R.id.txtTimeSlotName);
        edtDesc = (EditText) rootView.findViewById(R.id.edtDesc);
        btnSchedule = (Button) rootView.findViewById(R.id.btnSchedule);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setCurrentFragmentTag(Constants.JOB_SUMMARY_FRAGMENT);
        setupToolBar();

    }

    private void setupToolBar() {
        ((HomeActivity) getActivity()).hideRight();
        ((HomeActivity) getActivity()).setTitletext("Appliances - "+requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName());
        ((HomeActivity) getActivity()).setLeftToolBarText("Back");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void schedulingDialog(){
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_schedulepop);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    private void showSuccessDialog(){
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_donepopup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtDone = (TextView)dialog.findViewById(R.id.txtDone);
        TextView txtDateTime = (TextView)dialog.findViewById(R.id.txtDateTime);
        TextView txtTimeSlotName = (TextView)dialog.findViewById(R.id.txtTimeSlotName);
        txtTimeSlotName.setText(requestObjectSingleTon.getHomePlansRequestObject().getTimeSlotName());
        txtDateTime.setText(requestObjectSingleTon.getHomePlansRequestObject().getSchedulingDate());
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();

            }
        });

        dialog.show();
    }
    private void showAddCardDialog(boolean  isFailed) {
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!isFailed)
            dialog.setContentView(R.layout.card_info_dialog);
        else
            dialog.setContentView(R.layout.dialog_payment_failed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtAlmostDone = (TextView) dialog.findViewById(R.id.txtAlmostDone);
        TextView txtSave = (TextView) dialog.findViewById(R.id.txtSave);
        editCardNo = (EditText) dialog.findViewById(R.id.editCardNo);
        ImageView img_keypad = (ImageView) dialog.findViewById(R.id.img_keypad);
        editYear = (EditText) dialog.findViewById(R.id.editYear);
        editMonth = (EditText) dialog.findViewById(R.id.editMonth);
        editCw = (EditText) dialog.findViewById(R.id.editCw);
        editZipCode = (EditText) dialog.findViewById(R.id.editZipCode);
        editFirstName = (EditText) dialog.findViewById(R.id.editFirstName);
        editLastName = (EditText) dialog.findViewById(R.id.editLastName);
        ImageView img_camra = (ImageView) dialog.findViewById(R.id.img_camra);
        TextView txtScanCard = (TextView) dialog.findViewById(R.id.txtScanCard);

        LinearLayout btnScanCard = (LinearLayout) dialog.findViewById(R.id.layout3);

        txtAlmostDone.setTypeface(fontfamily);
        editCardNo.setTypeface(fontfamily);
        editYear.setTypeface(fontfamily);
        editMonth.setTypeface(fontfamily);
        editCw.setTypeface(fontfamily);
        editFirstName.setTypeface(fontfamily);
        editLastName.setTypeface(fontfamily);
        editZipCode.setTypeface(fontfamily);
        txtScanCard.setTypeface(fontfamily);

        txtScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress(v);
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_number = editCardNo.getText().toString().trim();
                month = editMonth.getText().toString().trim();
                year = editYear.getText().toString().trim();
                ;
                cvv = editCw.getText().toString().trim();
                zip_code = editZipCode.getText().toString().trim();
                ;
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
                }
//                else if (zip_code.length() == 0) {
//                    checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter Zip");
//                }
                else if (first_name.length() == 0) {
                    checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter first name");
                } else if (last_name.length() == 0) {
                    checkALert.showcheckAlert(getActivity(), getString(R.string.alert_title), "please enter last name");
                } else {
                    GetApiResponseAsync apiResponseAsync = new GetApiResponseAsync(Constants.BASE_URL_SINGLE, "POST", addCardResponseListener, addCardExceptionListener, getActivity(), "Scheduling");
                    apiResponseAsync.execute(getAddCardPareams());
                }

            }
        });

        dialog.show();
    }

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

    private HashMap<String, String> getAddCardPareams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "create");
        hashMap.put("object", "cards");
        hashMap.put("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
        hashMap.put("data[card_number]", card_number);
        hashMap.put("data[month]", month);
        hashMap.put("data[firstname]", first_name);
        hashMap.put("data[lastname]", last_name);
        hashMap.put("data[year]", year);
        hashMap.put("data[cvv]", cvv);
//        hashMap.put("data[zip_code]", zip_code);
        hashMap.put("data[primary]", "1");
        hashMap.put("id", _prefs.getString(Preferences.ID,""));

        return hashMap;
    }

    IHttpResponseListener addCardResponseListener = new IHttpResponseListener() {
        @Override
        public void handleResponse(JSONObject response) {
            Log.e("", "response" + response);
            try {
                if (response.getString("STATUS").equals("SUCCESS")) {
//                    JSONArray results = response.getJSONObject("RESPONSE").getJSONArray("results");
//                    JSONArray results = response.getJSONArray("RESPONSE");
//                    for (int i = 0; i < results.length(); i++) {
//                        JSONObject object = results.getJSONObject(i);
//                        HomeServiceBeans homeService = new HomeServiceBeans();
//                        homeService.setId(object.getInt("id"));
//                        homeService.setTitle(object.getString("name"));
//                        homeService.setStatus(object.getString("status"));
//                        homeService.setDisplay_order(object.getString("display_order"));
//                        homeService.setImage(object.getString("name"));
//                        arrayList.add(homeService);
//                    }
//                    Collections.sort(arrayList, new Comparator<HomeServiceBeans>() {
//
//                        public int compare(HomeServiceBeans p1, HomeServiceBeans p2) {
//                            return Integer.parseInt(p1.getDisplay_order()) - Integer.parseInt(p2.getDisplay_order());
//                        }
//                    });
//                    HomeServiceSingleTon.getInstance().setList(arrayList);
                    _prefs.edit().putString(Preferences.HAS_CARD, "1").commit();
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
                    sendScheduleRequest();
                    break;
                }
                case 1: {
                    checkALert.showcheckAlert(getActivity(), getActivity().getResources().getString(R.string.alert_title), error_message);
                    break;
                }
                case 2:{
                    if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                    showSuccessDialog();
                }
            }
        }
    };

    private void sendScheduleRequest() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Show Schduling Dialog
//                schedulingDialog();
            }

            @Override
            protected String doInBackground(Void... params) {
                createMultiPartRequest();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                GetApiResponseAsyncMutipart getApiResponseAsync = new GetApiResponseAsyncMutipart(multipart, responseListener, exceptionListener, getActivity(), "Scheduling");
                getApiResponseAsync.execute();
            }

        }.execute();
    }

    IHttpResponseListener responseListener = new IHttpResponseListener() {
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
                        if (key.equals("501")){
                            if (dialog != null && dialog.isShowing())
                                dialog.dismiss();
                                showAddCardDialog(true);

                            return;
                        }
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    IHttpExceptionListener exceptionListener = new IHttpExceptionListener() {
        @Override
        public void handleException(String exception) {
            error_message = exception;
            handler.sendEmptyMessage(1);
        }
    };

    private MultipartUtility createMultiPartRequest() {

        try {
            multipart = new MultipartUtility(Constants.BASE_URL_SINGLE, Constants.CHARSET);
            multipart.addFormField("api", "create");
            multipart.addFormField("object", "jobs");
            multipart.addFormField("token", _prefs.getString(Preferences.AUTH_TOKEN, ""));
            multipart.addFormField("data[time_slot_id]", requestObjectSingleTon.getHomePlansRequestObject().getTimeSlotId());
            multipart.addFormField("data[request_date]", requestObjectSingleTon.getHomePlansRequestObject().getSchedulingDate());
            multipart.addFormField("data[phone]", _prefs.getString(Preferences.PHONE, ""));
            if (!requestObjectSingleTon.getRequestName().equals(RequestConstants.FILE_A_CLAIM_REQUEST))
                multipart.addFormField("data[is_claim]", "0");
            else{
                multipart.addFormField("data[is_claim]", "1");
                multipart.addFormField("data[address_id]", requestObjectSingleTon.address_file_a_claim_id);

            }
            multipart.addFormField("data[service_id]", requestObjectSingleTon.getHomePlansRequestObject().getChooseService().getId() + "");
            for (int i = 0; i < getList().size(); i++) {
                multipart.addFormField("data[job_appliances][" + i + "][service_type]", requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName());
                multipart.addFormField("data[job_appliances][" + i + "][brand_id]", appliancesModalsTemp.get(i).getBean().getImgPath());
                multipart.addFormField("data[job_appliances][" + i + "][description]", appliancesModalsTemp.get(i).getBean().getDescription());
                multipart.addFormField("data[job_appliances][" + i + "][customer_complaint]", requestObjectSingleTon.getHomePlansRequestObject().getWhatsProblemBean().getProblem());
                multipart.addFormField("data[job_appliances][" + i + "][appliance_id]", appliancesModalsTemp.get(i).getId());
                multipart.addFormField("data[job_appliances][" + i + "][power_source]", appliancesModalsTemp.get(i).getHas_power_source());
                if (appliancesModalsTemp.get(i).getBean().getImgPath().length() != 0)
                    multipart.addFilePart("image", new File(appliancesModalsTemp.get(i).getBean().getImgPath()));
                if (requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName().equals("Install") || requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName().equals("Maintain") ||requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName().equals("Re Key")){
                    multipart.addFormField("data[job_appliances][" + i + "][quantity]", appliancesModalsTemp.get(i).getQuantity().trim());
                }
            }
            multipart.addFormField("data[contact_name]", _prefs.getString(Preferences.FIRSTNAME, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipart;
    }

    private ArrayList<AppliancesModal> getList() {

        appliancesModalsTemp.clear();
        for (int i = 0; i < requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().size(); i++) {
            if (Integer.parseInt(requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).getQuantity()) > 0) {
                appliancesModalsTemp.add(requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i));
                Log.e("",""+requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).getBean());
            }
        }
        return appliancesModalsTemp;
    }

}
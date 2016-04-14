package fixtpro.com.fixtpro.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.ResponseListener;
import fixtpro.com.fixtpro.adapters.AvailableJobsPagingAdaper;
import fixtpro.com.fixtpro.adapters.InstallOrRepairJobAdapter;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Singleton;
import fixtpro.com.fixtpro.utilites.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InstallorRepairFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InstallorRepairFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstallorRepairFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView lstInstallRepair;
    ImageView img_finishjob;
    String role = "pro";
    Context _context;
    SharedPreferences _prefs = null;
    String error_message = "";
    Fragment fragment = null ;
    LinearLayout layout_finish_job ;
    Dialog dialog = null ;
    public InstallorRepairFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstallorRepairFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InstallorRepairFragment newInstance(String param1, String param2) {
        InstallorRepairFragment fragment = new InstallorRepairFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_installor_repair, container, false);
        _prefs = Utilities.getSharedPreferences(_context);
        role = _prefs.getString(Preferences.ROLE, "pro");
        setWidgets(view);
        setListeners();
        if (CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal() != null){
            handler.sendEmptyMessage(0);
        }else{
            GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerScheduled, getActivity(), "Loading");
            responseAsync.execute(getRequestParams());
        }
        return view;
    }

    private void setWidgets(View view) {
        lstInstallRepair = (ListView) view.findViewById(R.id.lstInstallRepair);
        img_finishjob = (ImageView) view.findViewById(R.id.img_finishjob);
        layout_finish_job = (LinearLayout)view.findViewById(R.id.layout_finish_job);
    }

    private void setListeners() {
        lstInstallRepair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragment = new WhatsWrongFragment();
                CurrentScheduledJobSingleTon.getInstance().setSelectedJobApplianceModal(CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getJob_appliances_arrlist().get(position));
                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.WHATS_WRONG_FRAGMENT, true, null);
            }
        });
        img_finishjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Fixd-Pro","Completed");
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
    public void onResume() {
        super.onResume();
        ((HomeScreenNew) getActivity()).setCurrentFragmentTag(Constants.INSTALL_OR_REPAIR_FRAGMENT);
        setupToolBar();
    }

    private void setupToolBar() {
        ((HomeScreenNew) getActivity()).hideRight();
        ((HomeScreenNew) getActivity()).setTitletext(CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getContact_name());
        ((HomeScreenNew) getActivity()).setLeftToolBarImage(R.drawable.menu_icon);
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

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "read");
        hashMap.put("object", "jobs");
        if (!role.equals("pro"))
            hashMap.put("select", "^*,job_appliances.^*,job_appliances.appliance_types.^*,job_appliances.job_parts_used.^*,job_appliances.job_appliance_install_info.^*,job_appliances.job_appliance_repair_info.^*,job_appliances.job_appliance_install_types.install_types.^*,job_customer_addresses.^*,technicians.^*,job_appliances.job_appliance_repair_whats_wrong.^*,job_appliances.job_appliance_repair_types.repair_types.^*");
        else
            hashMap.put("select", "^*,job_appliances.^*,job_appliances.appliance_types.^*,job_appliances.job_parts_used.^*,job_appliances.job_appliance_install_info.^*,job_appliances.job_appliance_repair_info.^*,job_appliances.job_appliance_install_types.install_types.^*,job_customer_addresses.^*,technicians.^*,job_appliances.job_appliance_repair_whats_wrong.^*,job_appliances.job_appliance_repair_types.repair_types.^*");

        hashMap.put("where[id]", CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getId() + "");
        hashMap.put("token", Utilities.getSharedPreferences(getActivity()).getString(Preferences.AUTH_TOKEN, null));
        hashMap.put("page", "1");
        hashMap.put("per_page", "20");
        return hashMap;
    }

    ResponseListener responseListenerScheduled = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());

            try {
                if (Response.getString("STATUS").equals("SUCCESS")) {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
//                    nextScheduled = pagination.getString("next");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        AvailableJobModal model = new AvailableJobModal();
                        model.setContact_name(obj.getString("contact_name"));
                        model.setCreated_at(obj.getString("created_at"));
                        model.setCustomer_id(obj.getString("customer_id"));
                        model.setCustomer_notes(obj.getString("customer_notes"));
                        model.setFinished_at(obj.getString("finished_at"));
                        model.setId(obj.getString("id"));
                        model.setJob_id(obj.getString("job_id"));
                        model.setLatitude(obj.getDouble("latitude"));
                        model.setLocked_by(obj.getString("locked_by"));
                        model.setLocked_on(obj.getString("locked_on"));
                        model.setLongitude(obj.getDouble("longitude"));
                        model.setPhone(obj.getString("phone"));
                        model.setPro_id(obj.getString("pro_id"));
                        model.setRequest_date(obj.getString("request_date"));
                        model.setService_id(obj.getString("service_id"));
                        model.setService_type(obj.getString("service_type"));
                        model.setStarted_at(obj.getString("started_at"));
                        model.setStatus(obj.getString("status"));
                        model.setTechnician_id(obj.getString("technician_id"));
                        model.setTime_slot_id(obj.getString("time_slot_id"));
                        model.setTitle(obj.getString("title"));
                        model.setTotal_cost(obj.getString("total_cost"));
                        model.setUpdated_at(obj.getString("updated_at"));
                        model.setWarranty(obj.getString("warranty"));
//                        if(Utilities.getSharedPreferences(getContext()).getString(Preferences.ROLE, null).equals("pro")) {
                        JSONArray jobAppliances = obj.getJSONArray("job_appliances");
                        ArrayList<JobAppliancesModal> jobapplianceslist = new ArrayList<JobAppliancesModal>();
                        if (jobAppliances != null) {
                            for (int j = 0; j < jobAppliances.length(); j++) {
                                JSONObject jsonObject = jobAppliances.getJSONObject(j);
                                JobAppliancesModal mod = new JobAppliancesModal();
                                mod.setJob_appliances_id(jsonObject.getString("id"));
                                mod.setJob_appliances_job_id(jsonObject.getString("job_id"));
                                mod.setJob_appliances_appliance_id(jsonObject.getString("appliance_id"));
                                if (!jsonObject.isNull("description")) {
                                    mod.setJob_appliances_appliance_description(jsonObject.getString("description"));
                                }
                                if (!jsonObject.isNull("service_type")) {
                                    mod.setJob_appliances_service_type(jsonObject.getString("service_type"));
                                }
                                if (!jsonObject.isNull("customer_complaint")) {
                                    mod.setJob_appliances_customer_compalint(jsonObject.getString("customer_complaint"));
                                }

                                JSONObject appliance_type_obj = jsonObject.getJSONObject("appliance_types");
                                mod.setAppliance_type_id(appliance_type_obj.getString("id"));
                                mod.setAppliance_type_has_power_source(appliance_type_obj.getString("has_power_source"));
                                mod.setAppliance_type_service_id(appliance_type_obj.getString("service_id"));
                                mod.setAppliance_type_name(appliance_type_obj.getString("name"));
                                mod.setAppliance_type_soft_deleted(appliance_type_obj.getString("_soft_deleted"));
                                if (!appliance_type_obj.isNull("image")) {
                                    JSONObject image_obj = appliance_type_obj.getJSONObject("image");
                                    Log.e("", "-----" + image_obj.toString());
                                    if (!image_obj.isNull("original")) {
                                        mod.setImg_original(image_obj.getString("original"));
                                        mod.setImg_160x170(image_obj.getString("160x170"));
                                        mod.setImg_150x150(image_obj.getString("150x150"));
                                        mod.setImg_75x75(image_obj.getString("75x75"));
                                        mod.setImg_30x30(image_obj.getString("30x30"));
                                    }
                                }
//

//                                JSONObject services_obj = jsonObject.getJSONObject("services");
//                                mod.setService_id(services_obj.getString("id"));
//                                mod.setService_name(services_obj.getString("name"));
//                                mod.setService_created_at(services_obj.getString("created_at"));
//                                mod.setService_updated_at(services_obj.getString("updated_at"));
                                jobapplianceslist.add(mod);
                            }
//                            }
                            model.setJob_appliances_arrlist(jobapplianceslist);
                        }
                        if (!obj.isNull("technicians")) {
                            JSONObject technician_object = obj.getJSONObject("technicians");
                            model.setTechnician_id(technician_object.getString("id"));
                            model.setTechnician_fname(technician_object.getString("first_name"));
                            model.setTechnician_lname(technician_object.getString("last_name"));
                            model.setTechnician_pickup_jobs(technician_object.getString("pickup_jobs"));
                            model.setTechnician_avg_rating(technician_object.getString("avg_rating"));
                            model.setTechnician_scheduled_job_count(technician_object.getString("scheduled_jobs_count"));
                            model.setTechnician_completed_job_count(technician_object.getString("completed_jobs_count"));
                            if (!technician_object.isNull("profile_image")) {
                                JSONObject object_profile_image = technician_object.getJSONObject("profile_image");
                                if (!object_profile_image.isNull("original"))
                                    model.setTechnician_profile_image(object_profile_image.getString("original"));
                            }

                        }
                        if (!obj.isNull("time_slots")){
                            JSONObject time_slot_obj = obj.getJSONObject("time_slots");
                            model.setTime_slot_id(time_slot_obj.getString("id"));
                            model.setTimeslot_start(time_slot_obj.getString("start"));
                            model.setTimeslot_end(time_slot_obj.getString("end"));
                            model.setTimeslot_soft_deleted(time_slot_obj.getString("_soft_deleted"));
                        }
                        if (!obj.isNull("cost_details")) {
                            JSONObject cost_details_obj = obj.getJSONObject("cost_details");

                            model.setCost_details_tripcharges(cost_details_obj.getString("trip_charges"));
                            model.setCost_details_tax(cost_details_obj.getString("tax"));
                            model.setCost_details_tripcharges(cost_details_obj.getString("trip_charges"));
                            model.setCost_details_fixd_fee_percentage(cost_details_obj.getString("fixd_fee_percentage"));
                            model.setCost_details_fixd_fee(cost_details_obj.getString("fixd_fee"));
                            model.setCost_details_pro_earned(cost_details_obj.getString("pro_earned"));
                            model.setCost_details_customer_payment(cost_details_obj.getString("customer_payment"));
                        }

                        if (!obj.isNull("job_customer_addresses")) {
                            JSONObject job_customer_addresses_obj = obj.getJSONObject("job_customer_addresses");
                            model.setJob_customer_addresses_id(job_customer_addresses_obj.getString("id"));
                            model.setJob_customer_addresses_zip(job_customer_addresses_obj.getString("zip"));
                            model.setJob_customer_addresses_city(job_customer_addresses_obj.getString("city"));
                            model.setJob_customer_addresses_state(job_customer_addresses_obj.getString("state"));
                            model.setJob_customer_addresses_address(job_customer_addresses_obj.getString("address"));
                            model.setJob_customer_addresses_address_2(job_customer_addresses_obj.getString("address_2"));
                            model.setJob_customer_addresses_updated_at(job_customer_addresses_obj.getString("updated_at"));
                            model.setJob_customer_addresses_created_at(job_customer_addresses_obj.getString("created_at"));
                            model.setJob_customer_addresses_job_id(job_customer_addresses_obj.getString("job_id"));
                        }
                        CurrentScheduledJobSingleTon.getInstance().setCurrentJonModal(model);
                        handler.sendEmptyMessage(0);
                    }
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
                    InstallOrRepairJobAdapter adapter = new InstallOrRepairJobAdapter(getActivity(),CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getJob_appliances_arrlist(),getResources());
                    lstInstallRepair.addFooterView(getFooterView());
                    lstInstallRepair.setAdapter(adapter);
                    isJobCompleted();
                    break;
                }
                case 1: {
                    showAlertDialog("Fixd-Pro", error_message);
                    break;
                }
            }
        }
    };
    private void isJobCompleted(){
        boolean isAllCompleted = true ;
        for (int i = 0 ; i < CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getJob_appliances_arrlist().size() ; i++){
                if (!CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getJob_appliances_arrlist().get(i).isProcessCompleted()){
                    isAllCompleted = false ;
                    break;
                }
        }
        if (isAllCompleted){
            layout_finish_job.setVisibility(View.VISIBLE);
        }
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
    private View getFooterView(){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            ViewGroup footerView = (ViewGroup) inflater.inflate(R.layout.install_or_repair_footer_item, lstInstallRepair,
                    false);
            ImageView img_AddServices = (ImageView)footerView.findViewById(R.id.img_AddServices);
            img_AddServices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new AddServiceFragment();
                    ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.ADD_SERVICE_FRAGMENT, true, null);
                }
            });
            return footerView;

    }
    private void showCustomDialog() {
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_finish);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView)dialog.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
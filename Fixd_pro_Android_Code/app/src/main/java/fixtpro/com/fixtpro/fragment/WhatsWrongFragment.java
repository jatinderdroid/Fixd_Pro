package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.SignatureActivity;
import fixtpro.com.fixtpro.adapters.WhatsWrongAdapter;
import fixtpro.com.fixtpro.beans.install_repair_beans.ReapirInstallProcessModal;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhatsWrongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhatsWrongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatsWrongFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ProgressBar progressBar ;
    ListView listWhatsWrong ;
    Fragment fragment = null ;
    TextView txtProgress;
    WhatsWrongAdapter adapter = null ;
    float progressText = 0 ;
    int progress = 0;
    public WhatsWrongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatsWrongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatsWrongFragment newInstance(String param1, String param2) {
        WhatsWrongFragment fragment = new WhatsWrongFragment();
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
    public void onResume() {
        super.onResume();
        ((HomeScreenNew) getActivity()).setCurrentFragmentTag(Constants.WHATS_WRONG_FRAGMENT);
        setupToolBar();
        if (adapter != null){
            adapter.notifyDataSetChanged();
            if (CurrentScheduledJobSingleTon.getInstance().getInstallOrRepairModal().getSignature().getSignature_path() != null){
                ((HomeScreenNew) getActivity()).setRightToolBarText("Done");
            }
        }
    }

    private void setupToolBar() {
        ((HomeScreenNew) getActivity()).hideRight();
        ((HomeScreenNew) getActivity()).setTitletext(CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal().getContact_name());
        ((HomeScreenNew) getActivity()).setLeftToolBarText("Back");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whats_wrong, container, false);
        setWidgets(view);
        setListeners();
        setProgress();
        adapter = new WhatsWrongAdapter(getActivity(),CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList(),getResources());
        listWhatsWrong.setAdapter(adapter);
        return view;
    }
    private void setProgress(){
            int size  = CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().size();
                int completedCount = 0;
                for (int i = 0 ; i < size ; i++){
                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(i).isCompleted()){
                        completedCount++;
                    }
                }

                if (completedCount>0){
                    progress = (int)(100 / (size + 1) * completedCount);
                    progressText =  (float)(100 / (size + 1) * completedCount);
                    txtProgress.setText(progressText + "%");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress);
                        }
                    },200);

                }
    }
    private void setWidgets(View view){
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        progressBar.setScaleY(5f);
        listWhatsWrong = (ListView)view.findViewById(R.id.listWhatsWrong);
        txtProgress = (TextView)view.findViewById(R.id.txtProgress);
    }
    private void setListeners(){
        listWhatsWrong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReapirInstallProcessModal modal = CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position);
                CurrentScheduledJobSingleTon.getInstance().setCurrentReapirInstallProcessModal(modal);
                if (modal.getName().equals(Constants.WHATS_WRONG) && position == 0){
//                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position).isCompleted()) {

                        fragment = new TellUsWhatsWrongFragment();
                        ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.TELL_US_WHATS_WRONG_FRAGMENT, true, null);
//                    }

                }else if (modal.getName().equals(Constants.REPAIR_TYPE) && position == 0){
//                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position).isCompleted()) {
                        fragment = new RepairFragment();
                        ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.REPAIR_TYPE_FRAGMENT, true, null);
//                    }

                }else if (modal.getName().equals(Constants.REPAIR_TYPE) && position == 1){
                            if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position -1).isCompleted()){
                                fragment = new RepairFragment();
                                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.REPAIR_TYPE_FRAGMENT, true, null);
                            }

                }else if (modal.getName().equals(Constants.PARTS)){
                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position -1).isCompleted()){
                        fragment = new PartsFragment();
                        ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.PARTS_FRAGMENT, true, null);
                    }

                }else if (modal.getName().equals(Constants.WORK_ORDER)){
                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position -1).isCompleted()){
                        fragment = new WorkOrderFragment();
                        ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.WORK_ORDER_FRAGMENT, true, null);
                    }

                }else if (modal.getName().equals(Constants.REPAIR_INFO)){
                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position -1).isCompleted()){
                        fragment = new RepairInfoFragment();
                        ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.REPAIR_INFO_FRAGMENT, true, null);
                    }

                }else if (modal.getName().equals(Constants.SIGNATURE)){
                    if (CurrentScheduledJobSingleTon.getInstance().getReapirInstallProcessModalList().get(position -1).isCompleted()){
                        Intent intent = new Intent(getActivity(), SignatureActivity.class);
                        startActivityForResult(intent, 200);
//                        fragment = new SignatureFragment();
//                        ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.SIGNATURE_FRAGMENT, true, null);
                    }
                }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.e("","requestCode"+requestCode);
        if (adapter != null){
            adapter.notifyDataSetChanged();
            onResume();
            progressBar.setProgress(100);
            txtProgress.setText("100%");
        }
    }
    public void submitPost(){
        ((HomeScreenNew) getActivity()).popInclusiveFragment(Constants.WHATS_WRONG_FRAGMENT);
        CurrentScheduledJobSingleTon.getInstance().getJobApplianceModal().setIsProcessCompleted(true);
    }
}

package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.install_repair_beans.Parts;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ImageView img_add;
    LinearLayout container_layout;
    ArrayList<Parts> partsArrayList = new ArrayList<Parts>();
    CurrentScheduledJobSingleTon singleTon = null;
    public PartsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartsFragment newInstance(String param1, String param2) {
        PartsFragment fragment = new PartsFragment();
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
        singleTon = CurrentScheduledJobSingleTon.getInstance();
        partsArrayList = singleTon.getInstallOrRepairModal().getPartsArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parts, container, false);
        setWidgets(view);
        setListeners();
        setView();
        if (partsArrayList.size() == 0){
            partsArrayList.add(new Parts());
            setView();
        }
        return view;
    }
    private void setWidgets(View view){
        img_add = (ImageView)view.findViewById(R.id.img_add);
        container_layout = (LinearLayout)view.findViewById(R.id.container_layout);
    }
    private void setListeners(){
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container_layout.removeAllViews();
                partsArrayList.add(new Parts());
                setView();
            }
        });
    }
    private void setView(){
        for (int i = 0 ; i < partsArrayList.size() ; i++){
            LayoutInflater layoutInflater =
                    (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.parts_item, null);
            View line_view = (View)addView.findViewById(R.id.line_view);
            final EditText txtPartDescription = (EditText)addView.findViewById(R.id.txtPartDescription);
            final EditText txtPartNumber = (EditText)addView.findViewById(R.id.txtPartDescription);
            final EditText txtQty = (EditText)addView.findViewById(R.id.txtQty);
            final EditText txtCost = (EditText)addView.findViewById(R.id.txtCost);

            if (i == 0){
                line_view.setVisibility(View.GONE);
            }
            txtPartDescription.setText(partsArrayList.get(i).getDescription());
            txtPartDescription.setTag(i);
            txtPartNumber.setText(partsArrayList.get(i).getNumber());
            txtPartNumber.setTag(i);
            txtQty.setText(partsArrayList.get(i).getQuantity());
            txtQty.setTag(i);
            txtCost.setText(partsArrayList.get(i).getCost());
            txtCost.setTag(i);

            txtPartDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    partsArrayList.get((int) txtPartDescription.getTag()).setDescription(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtPartNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    partsArrayList.get((int)txtPartNumber.getTag()).setNumber(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    partsArrayList.get((int)txtQty.getTag()).setQuantity(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    partsArrayList.get((int)txtCost.getTag()).setCost(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            container_layout.addView(addView);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.PARTS_FRAGMENT);
        setupToolBar();

    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).setRightToolBarText("Done");
        ((HomeScreenNew)getActivity()).setTitletext("Parts Needed");
        ((HomeScreenNew)getActivity()).setLeftToolBarText("Back");
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
    public void submitPost(){
        singleTon.getCurrentReapirInstallProcessModal().setIsCompleted(true);

        ((HomeScreenNew) getActivity()).popInclusiveFragment(Constants.PARTS_FRAGMENT);
    }
    public void clearList(){
        partsArrayList.clear();
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
}

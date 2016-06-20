package com.fixtconsumer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.activities.HomeActivity;
import com.fixtconsumer.adapters.BrandsSpinnerAdapter;
import com.fixtconsumer.beans.AppliancesModal;
import com.fixtconsumer.beans.Brands;
import com.fixtconsumer.beans.TellUsMoreBean;
import com.fixtconsumer.singletons.BrandNamesSingleton;
import com.fixtconsumer.singletons.HomeRequestObjectSingleTon;
import com.fixtconsumer.utils.CheckAlertDialog;
import com.fixtconsumer.utils.Constants;
import com.fixtconsumer.utils.ImageHelper2;
import com.fixtconsumer.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TellUsMoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TellUsMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TellUsMoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imgMain,img_Camra;
    TextView txtTakepic,lblTitle;
    EditText editDescription ;
    Spinner txtBrand;
    Dialog dialog = null ;
    Typeface fontfamily;
    Context _context ;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    public String Path = "",path = "";
    public Uri selectedImageUri;
    int finalHeight, finalWidth;
    LinearLayout lstProblem ;
    private OnFragmentInteractionListener mListener;
    HomeRequestObjectSingleTon requestObjectSingleTon =  HomeRequestObjectSingleTon.getInstance();
    ArrayList<AppliancesModal> arrayList = requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist();
    ArrayList<AppliancesModal> appliancesModalsTemp = new ArrayList<AppliancesModal>();
    ArrayList<Brands> arrayListBrands = BrandNamesSingleton.getInstance().getBrands();
    boolean mAlreadyLoaded  = false ;
    CheckAlertDialog checkALert;
    BrandsSpinnerAdapter spinnerAdapter = null ;
    public TellUsMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TellUsMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TellUsMoreFragment newInstance(String param1, String param2) {
        TellUsMoreFragment fragment = new TellUsMoreFragment();
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
        _context = getActivity() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tell_us_more, container, false);
        checkALert = new CheckAlertDialog();
        setWidgets(view);
        setListeners();
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            appliancesModalsTemp = getList();
            appliancesModalsTemp.get(requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar).setBean(new TellUsMoreBean());
            // Do this code only first time, not after rotation or reuse fragment from backstack
        }else {
            requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar = requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar - 1;
        }
        lblTitle.setText(appliancesModalsTemp.get(requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar).getName());
        lstProblem.setBackgroundResource(Utility.getSelectedHomeServicePhoto(FixdConsumerApplication.selectedAppliance));
//        ((HomeActivity)getActivity()).switchFragment(new WhenDoYouWantCalanderFragment(), Constants.WHEN_DO_YOU_WANT_FRAGMENT, true, null);
        spinnerAdapter = new BrandsSpinnerAdapter(getActivity(),R.layout.item_brands,arrayListBrands,getResources());
        txtBrand.setAdapter(spinnerAdapter);

        return view;
    }
    private void setWidgets(View view){
        lstProblem = (LinearLayout)view.findViewById(R.id.lstProblem);
        img_Camra = (ImageView)view.findViewById(R.id.img_Camra);
        imgMain = (ImageView)view.findViewById(R.id.imgMain);
        txtTakepic = (TextView)view.findViewById(R.id.txtTakepic);
        lblTitle = (TextView)view.findViewById(R.id.lblTitle);
        editDescription = (EditText)view.findViewById(R.id.txtDescription);
        txtBrand = (Spinner)view.findViewById(R.id.txtBrand);

        fontfamily = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        ViewTreeObserver vto = imgMain.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imgMain.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imgMain.getMeasuredHeight();
                finalWidth = imgMain.getMeasuredWidth();
                return true;
            }
        });
    }
    private void setListeners(){
        img_Camra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
        imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
        txtTakepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamraGalleryPopUp();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setCurrentFragmentTag(Constants.TELL_US_MORE_FRAGMEMT);
        setupToolBar();

    }
    private void setupToolBar(){
        ((HomeActivity)getActivity()).setRightToolBarText("Next");
        ((HomeActivity)getActivity()).setTitletext("Appliances - "+requestObjectSingleTon.getHomePlansRequestObject().getTypeofprojectName());
        ((HomeActivity)getActivity()).setLeftToolBarText("Back");
    }
    /*Create Camra Gallery PopUP*/
    private void showCamraGalleryPopUp() {
        dialog = new Dialog(_context);
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dialog_camra_gallery);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView txtTakePicture = (TextView) dialog.findViewById(R.id.txtTakePicture);
        TextView txtCamera = (TextView) dialog.findViewById(R.id.txtCamera);
        TextView txtGallery = (TextView) dialog.findViewById(R.id.txtGallery);
        // set the typeface...
        txtCamera.setTypeface(fontfamily);
        txtGallery.setTypeface(fontfamily);
        txtTakePicture.setTypeface(fontfamily);
        // set the click listner...
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera();
            }
        });
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        dialog.show();
    }
    private  void openCamera(){
        Intent camraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camraIntent, CAMERA_REQUEST);
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CAMERA_REQUEST) {
                try {
                    selectedImageUri = data.getData();
                    Path = ImageHelper2.compressImage(selectedImageUri, getActivity());
                    txtTakepic.setVisibility(View.INVISIBLE);
                    img_Camra.setVisibility(View.INVISIBLE);
                    Picasso.with(getActivity()).load(selectedImageUri)
                            .into(imgMain);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                Path = getPath(selectedImageUri);
                txtTakepic.setVisibility(View.INVISIBLE);
                img_Camra.setVisibility(View.INVISIBLE);
//                imgMain.getLayoutParams().height = finalHeight;
//                imgMain.getLayoutParams().width = finalWidth;
//                imgDriver.setImageBitmap(ImageHelper2.decodeSampledBitmapFromFile(Path, finalWidth, finalHeight));
                Picasso.with(getActivity()).load(selectedImageUri)
                        .into(imgMain);
            }
        }
    }
    // get path from the gallery...
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
    public void submit(){
        if (appliancesModalsTemp.size() > requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar + 1){
                if (editDescription.getText().toString().trim().length() > 0){
                    ((HomeActivity)getActivity()).switchFragment(new TellUsMoreFragment(), Constants.TELL_US_MORE_FRAGMEMT, true, null);
                    TellUsMoreBean moreBean = appliancesModalsTemp.get(requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar).getBean();
//                    moreBean.setBrand(txtBrand.getText().toString().trim());
                    moreBean.setBrandID("1");
                    moreBean.setDescription(editDescription.getText().toString().trim());
                    moreBean.setImgPath(path);
                    ++requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar;
                }else{
                    checkALert.showcheckAlert(getActivity(),getString(R.string.alert_title),"Please enter the description");
                }

        }else {
            ((HomeActivity)getActivity()).switchFragment(new WhenDoYouWantCalanderFragment(),Constants.WHEN_DO_YOU_WANT_FRAGMENT,true,null);
            ++requestObjectSingleTon.getHomePlansRequestObject().powersourceControlvar;

        }
    }
    private ArrayList<AppliancesModal> getList(){

        appliancesModalsTemp.clear();
        for (int i = 0 ; i < requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().size() ; i++){
            if (Integer.parseInt(requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i).getQuantity()) > 0){
                appliancesModalsTemp.add(requestObjectSingleTon.getHomePlansRequestObject().getWhichAppliancelist().get(i));
            }
        }
        return appliancesModalsTemp;
    }
}

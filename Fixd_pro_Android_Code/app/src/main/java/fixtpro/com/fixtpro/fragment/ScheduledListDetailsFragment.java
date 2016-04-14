package fixtpro.com.fixtpro.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import fixtpro.com.fixtpro.DeclineJobActivity;
import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.ScheduledJobListClickActivity;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.views.HorizontalListView;
import fixtpro.com.fixtpro.views.RatingBarView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduledListDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduledListDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduledListDetailsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    AvailableJobModal model;
    private static GoogleMap mMap;
    TextView contactName, address, date, timeinterval, txtUserName, txtJobDetails;
    ImageView  en_routeimg, cancel_jobimg, transparentImageView ;
    HorizontalScrollView horizontalScrollView  = null ;
    LayoutInflater inflater ;
    ImageLoader imageLoader = null ;
    DisplayImageOptions defaultOptions;
    LinearLayout scrollViewLatout,layout_problem ;
    ScrollView scrollViewParent  ;
    RatingBarView custom_ratingbar ;
    Fragment fragment = null ;
    SupportMapFragment mMapFragment = null ;
    public ScheduledListDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduledListDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduledListDetailsFragment newInstance(String param1, String param2) {
        ScheduledListDetailsFragment fragment = new ScheduledListDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            model = (AvailableJobModal) getArguments().getSerializable("SCHEDULED_JOB_DETAIL");
            model = CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal();
//        }
//        / UNIVERSAL IMAGE LOADER SETUP
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        // Create configuration for ImageLoader (all options are optional)

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scheduled_list_details, container, false);
        setWidgets(rootView);
        setListeners();

        contactName.setText(model.getContact_name());
        address.setText(model.getJob_customer_addresses_address() + " - " + model.getJob_customer_addresses_city() + "," + model.getJob_customer_addresses_state());
        date.setText(model.getRequest_date());
        timeinterval.setText(model.getTimeslot_start()+" - "+model.getTimeslot_end());
        custom_ratingbar.setClickable(false);
        custom_ratingbar.setStar(3, true);
        txtUserName.setText(model.getTechnician_fname() + " " + model.getTechnician_lname());
        txtJobDetails.setText(model.getTechnician_fname() + " has " + model.getTechnician_scheduled_job_count() + " jobs scheduled for this time");
        setUpHorizontalScrollView();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap ;
        setUpMap();
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
    public void setWidgets(View view){
        horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollView);
//        mMap = ((SupportMapFragment)getChildFragmentManager()
//                .findFragmentById(R.id.location_map)).getMap();
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.location_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        en_routeimg = (ImageView) view.findViewById(R.id.enroute_job);
        cancel_jobimg = (ImageView) view.findViewById(R.id.canceljob);
        contactName = (TextView) view.findViewById(R.id.contactname);
        address = (TextView) view.findViewById(R.id.address);
        date = (TextView) view.findViewById(R.id.date);
        timeinterval = (TextView) view.findViewById(R.id.timeinterval);
        scrollViewLatout = (LinearLayout)view.findViewById(R.id.scrollViewLatout);
        layout_problem = (LinearLayout)view.findViewById(R.id.layout_problem);
        scrollViewParent = (ScrollView)view.findViewById(R.id.scrollViewParent);
        transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);

        custom_ratingbar = (RatingBarView)view.findViewById(R.id.custom_ratingbar);
        txtJobDetails = (TextView) view.findViewById(R.id.txtJobDetails);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
    }
    public void setListeners(){
        en_routeimg.setOnClickListener(this);
        cancel_jobimg.setOnClickListener(this);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }
    private void setUpMap() {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(model.getLatitude(), model.getLongitude())).title("My Home").snippet("Home Address"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(model.getLatitude(),
                model.getLongitude()), 12.0f));
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.SCHEDULED_LIST_DETAILS_FRAGMENT);
        setupToolBar();
    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).hideRight();
        ((HomeScreenNew)getActivity()).setTitletext("Scheduled Job");
        ((HomeScreenNew)getActivity()).setLeftToolBarImage(R.drawable.screen_cross);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
//                finish();
                ((HomeScreenNew)getActivity()).popStack();
                break;
            case R.id.enroute_job:
                fragment = new StartJobFragment();
                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.START_JOB_FRAGMENT, true, null);

                break;
            case R.id.canceljob:
                Intent i = new Intent(getActivity(), DeclineJobActivity.class);
                i.putExtra("JobType","Scheduled");
                i.putExtra("JobId",model.getId());
                startActivity(i);

                break;
        }
    }
    private void setUpHorizontalScrollView(){
        if (model.getJob_appliances_arrlist().size() > 0){
            inflater =  (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0 ; i < model.getJob_appliances_arrlist().size() ; i++){
                View child = getLayoutInflater(null).inflate(R.layout.jobs_image_title_item, null);
                View child1 = getLayoutInflater(null).inflate(R.layout.problem_text_item, null);
                TextView txtTitle = (TextView)child.findViewById(R.id.txtTypeTitle);
                TextView txtProblem = (TextView)child1.findViewById(R.id.txtProblem);
                txtProblem.setText(model.getJob_appliances_arrlist().get(i).getAppliance_type_name() + " " +model.getJob_appliances_arrlist().get(i).getJob_appliances_service_type() + " - " +model.getJob_appliances_arrlist().get(i).getJob_appliances_appliance_description());
                final ImageView imgType = (ImageView)child.findViewById(R.id.imgType);
                txtTitle.setText(model.getJob_appliances_arrlist().get(i).getAppliance_type_name() +"\n" +model.getJob_appliances_arrlist().get(i).getJob_appliances_service_type());
                scrollViewLatout.addView(child);
                layout_problem.addView(child1);
                imageLoader.loadImage(model.getJob_appliances_arrlist().get(i).getImg_original(), defaultOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        imgType.setImageBitmap(loadedImage);
                    }
                });
            }
        }
    }
}

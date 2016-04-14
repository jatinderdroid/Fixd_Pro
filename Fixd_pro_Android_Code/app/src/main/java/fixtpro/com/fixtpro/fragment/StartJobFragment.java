package fixtpro.com.fixtpro.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.LocationResponseListener;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.GMapV2GetRouteDirection;
import java.util.ArrayList;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartJobFragment extends Fragment implements OnMapReadyCallback,LocationResponseListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AvailableJobModal modal = null ;

    private OnFragmentInteractionListener mListener;
    SupportMapFragment mMapFragment = null ;
    private static GoogleMap mMap;
    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    
    MarkerOptions markerOptions;
    Location location ;
    TextView txttime, txtDistance, txttolocation, txtfromlocation;

    private Dialog dialog;
    private  Context _context ;
    Fragment fragment = null ;
    LayoutInflater inflater ;
    ImageLoader imageLoader = null ;
    DisplayImageOptions defaultOptions;
    public StartJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartJobFragment newInstance(String param1, String param2) {
        StartJobFragment fragment = new StartJobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modal = CurrentScheduledJobSingleTon.getInstance().getCurrentJonModal();
        _context  = getActivity() ;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_job, container, false);
        setWidgets(view);
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        return view;
    }
    private void setWidgets(View v){
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.location_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        txtDistance = (TextView)v.findViewById(R.id.txtDistance);
        txttime = (TextView)v.findViewById(R.id.txttime);
        txtfromlocation = (TextView)v.findViewById(R.id.txtfromlocation);
        txttolocation = (TextView)v.findViewById(R.id.txttolocation);

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
    public void handleLocationResponse(Location location) {
        // got the location
        if (location != null){
            this.location= location ;
            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
//                    toPosition = new LatLng(modal.getLatitude(), modal.getLongitude());
                    toPosition = new LatLng(31.56446666, 76.12112116);
                    GetRouteTask getRoute = new GetRouteTask();
                    getRoute.execute();
                    break;
                }
            }
        }
    };
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
        super.onResume();
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.START_JOB_FRAGMENT);
         setupToolBar();
        /**
         * Get Location to show Path on map
         */
        ((HomeScreenNew)getActivity()).getLocation(StartJobFragment.this);
    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).setRightToolBarText("Start Job");
        ((HomeScreenNew)getActivity()).setTitletext(modal.getContact_name());
        ((HomeScreenNew)getActivity()).setLeftToolBarImage(R.drawable.menu_icon);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap ;
        setUpMap();
    }
    private void setUpMap() {
        // Enabling MyLocation in Google Map
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        markerOptions = new MarkerOptions();
    }


    /**
     *
     *
     * This class Get Route on the map
     *
     */
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(getActivity());
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            mMap.clear();
            if(response.equalsIgnoreCase("Success")){
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(15).color(
                        Color.BLUE);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                // Adding route on the map
                mMap.addPolyline(rectLine);
                markerOptions.position(toPosition);
                markerOptions.draggable(false);
                mMap.addMarker(markerOptions);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(fromPosition);
                builder.include(toPosition);
                final LatLngBounds bounds = builder.build();
                final int padding = 96; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
                Log.e("","document"+document);
                if (document != null){
                    txtDistance.setText(v2GetRouteDirection.getDistanceText(document));
                    txtfromlocation.setText(v2GetRouteDirection.getStartAddress(document));
                    txttime.setText(v2GetRouteDirection.getDurationText(document));
                    txtDistance.setText("("+v2GetRouteDirection.getDistanceText(document)+")");
                }
                txttolocation.setText(modal.getJob_customer_addresses_address());

            }

            Dialog.dismiss();
        }
    }
    public void showStartJobDialog(){
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_startjob);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_close = (ImageView)dialog.findViewById(R.id.img_close);
        Button btnStartJob = (Button)dialog.findViewById(R.id.btnStartJob);
        TextView txtUserName = (TextView)dialog.findViewById(R.id.txtUserName);
        TextView txtUserDetails = (TextView)dialog.findViewById(R.id.txtUserDetails);
        txtUserDetails.setText(modal.getJob_customer_addresses_address());
        txtUserName.setText(modal.getContact_name());
        btnStartJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fragment = new InstallorRepairFragment();
                ((HomeScreenNew) getActivity()).switchFragment(fragment, Constants.INSTALL_OR_REPAIR_FRAGMENT, true, null);
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setUpHorizontalScrollView(dialog);
        dialog.show();
    }

    private void setUpHorizontalScrollView(Dialog dialog) {
        if (modal.getJob_appliances_arrlist().size() > 0) {
            LinearLayout scrollViewLatout = (LinearLayout)dialog.findViewById(R.id.scrollViewLatout);
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < modal.getJob_appliances_arrlist().size(); i++) {
                View child = getLayoutInflater(null).inflate(R.layout.jobs_image_title_item, null);
                View child1 = getLayoutInflater(null).inflate(R.layout.problem_text_item, null);
                TextView txtTitle = (TextView) child.findViewById(R.id.txtTypeTitle);
                TextView txtProblem = (TextView) child1.findViewById(R.id.txtProblem);
                txtProblem.setText(modal.getJob_appliances_arrlist().get(i).getAppliance_type_name() + " " + modal.getJob_appliances_arrlist().get(i).getJob_appliances_service_type() + " - " + modal.getJob_appliances_arrlist().get(i).getJob_appliances_appliance_description());
                final ImageView imgType = (ImageView) child.findViewById(R.id.imgType);
                txtTitle.setText(modal.getJob_appliances_arrlist().get(i).getAppliance_type_name() + "\n" + modal.getJob_appliances_arrlist().get(i).getJob_appliances_service_type());
                scrollViewLatout.addView(child);
                imageLoader.loadImage(modal.getJob_appliances_arrlist().get(i).getImg_original(), defaultOptions, new SimpleImageLoadingListener() {
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
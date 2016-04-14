package fixtpro.com.fixtpro.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fixtpro.com.fixtpro.HomeScreenNew;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.imageupload.ImageHelper2;
import fixtpro.com.fixtpro.utilites.Constants;
import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TellUsWhatsWrongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TellUsWhatsWrongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TellUsWhatsWrongFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ImageView imgMain,img_Camra;
    TextView txtTakepic;
    EditText editDescription ;
    Dialog dialog = null ;
    Typeface fontfamily;
    Context _context ;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    public String Path,path;
    public Uri selectedImageUri;
    int finalHeight, finalWidth;
    public TellUsWhatsWrongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TellUsWhatsWrongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TellUsWhatsWrongFragment newInstance(String param1, String param2) {
        TellUsWhatsWrongFragment fragment = new TellUsWhatsWrongFragment();
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
        _context = getActivity() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tell_us_whats_wrong, container, false);
        setWidgets(view);
        setListeners();
        return view;
    }
    private void setWidgets(View view){
        img_Camra = (ImageView)view.findViewById(R.id.img_Camra);
        imgMain = (ImageView)view.findViewById(R.id.imgMain);
        txtTakepic = (TextView)view.findViewById(R.id.txtTakepic);
        editDescription = (EditText)view.findViewById(R.id.editDescription);
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
        ((HomeScreenNew)getActivity()).setCurrentFragmentTag(Constants.TELL_US_WHATS_WRONG_FRAGMENT);
        setupToolBar();

    }
    private void setupToolBar(){
        ((HomeScreenNew)getActivity()).setRightToolBarText("Done");
        ((HomeScreenNew)getActivity()).setTitletext("Tell Us Whats Wrong");
        ((HomeScreenNew)getActivity()).setLeftToolBarText("Back");
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
    /*Create Camra Gallery PopUP*/
    private void showCamraGalleryPopUp() {
        dialog = new Dialog(_context);
        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camra_gallery);
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
    public void  submitPost(){
        if (Path == null){
            showAlertDialog("Fixd-Pro","Please take picture of problem");
        }else if (editDescription.getText().toString().length() == 0){
            showAlertDialog("Fixd-Pro","Please enter diagnosis and resolution");
        }else {
            CurrentScheduledJobSingleTon.getInstance().getInstallOrRepairModal().getWhatsWrong().setDiagnosis_and_resolution(editDescription.getText().toString());
            CurrentScheduledJobSingleTon.getInstance().getInstallOrRepairModal().getWhatsWrong().setImage(Path);
            CurrentScheduledJobSingleTon.getInstance().getCurrentReapirInstallProcessModal().setIsCompleted(true);
            ((HomeScreenNew) getActivity()).popInclusiveFragment(Constants.TELL_US_WHATS_WRONG_FRAGMENT);
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
}

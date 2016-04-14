package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.adapters.AssignTechAdapter;
import fixtpro.com.fixtpro.beans.AssignTechModal;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;


public class AssignTechnicianActivity extends AppCompatActivity {
    private Context context = AssignTechnicianActivity.this;
    private String TAG  = "AssignTechnicianActivity";

    private Typeface fontfamily;
    private Toolbar toolbar;
    private com.paging.listview.PagingListView assignTechList;
    private ImageView img_Cancel;
    private TextView titletext;
    private AssignTechAdapter mAdp;
    private ArrayList<AssignTechModal> modalList;
    private SharedPreferences _prefs = null ;
    private Context _context  = null;
    String error_message = "";
    String next = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_technician);
        modalList = new ArrayList<AssignTechModal>();
        setTypeface();

        setToolbar();

        setWidgets();

        GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",getTechniciansListener,AssignTechnicianActivity.this,"Getting.");
        getApiResponseAsync.execute(getTechnicianRequestParams());

    }
    ResponseListener getTechniciansListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "" + Response.toString());
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
                    JSONArray results = Response.getJSONObject("RESPONSE").getJSONArray("results");
                    JSONObject pagination = Response.getJSONObject("RESPONSE").getJSONObject("pagination");
                    next = pagination.getString("next");
                    for (int i = 0 ; i < results.length() ; i++){
                    AssignTechModal modal = new AssignTechModal();
                        JSONObject jsonObject = results.getJSONObject(i);
                        modal.setTechId(jsonObject.getString("id"));
                        modal.setFirstName(jsonObject.getString("first_name"));
                        modal.setLasttName(jsonObject.getString("last_name"));
                        if (!jsonObject.isNull("profile_image")){
                            JSONObject profile_image = jsonObject.getJSONObject("profile_image");
                            if (!profile_image.isNull("original"))
                            modal.setImage(profile_image.getString("original"));
                        }
                        modal.setRating(jsonObject.getString("avg_rating"));
                        modalList.add(modal);
                    }
                    handler.sendEmptyMessage(0);
                }else {
                    JSONObject errors = Response.getJSONObject("ERRORS");
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()){
                        String key = (String)keys.next();
                        error_message = errors.getString(key);
                    }
                    handler.sendEmptyMessage(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    AssignTechAdapter assignTechAdapter = new AssignTechAdapter(AssignTechnicianActivity.this,modalList);
                    assignTechList.setAdapter(assignTechAdapter);
                    assignTechList.setHasMoreItems(false);
                    break;
                }
                case 1:{
                    showAlertDialog("Fixd-Pro",error_message);
                    break;
                }
            }
        }
    };
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
    private void setTypeface() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
    }
    private void setToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        img_Cancel = (ImageView)toolbar.findViewById(R.id.img_Cancel);
        img_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titletext = (TextView)toolbar.findViewById(R.id.titletext);
        titletext.setTypeface(fontfamily);

    }
    private void setWidgets(){
        assignTechList = (com.paging.listview.PagingListView)findViewById(R.id.assignTechList);
    }
    private void setAdapter(){
        modalList = new ArrayList<AssignTechModal>();
    }
    private HashMap<String,String> getTechnicianRequestParams(){
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("api","read");
            hashMap.put("object","technicians");
            hashMap.put("select","^*");
            hashMap.put("token","^*");
            hashMap.put("per_page","20");
            hashMap.put("page", "1");
            return hashMap;
    }
}

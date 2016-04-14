package fixtpro.com.fixtpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fixtpro.com.fixtpro.adapters.AvailableJobsPagingAdaper;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class DeclineJobActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView cancel, yesdecline_cancel_img;
    TextView title, areusure_text, backup_text;
    EditText editText;
    String JobType, objectType, JobId, message;
    String error_message =  "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline_job);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
        JobType = getIntent().getStringExtra("JobType");
        JobId = getIntent().getStringExtra("JobId");
        if(JobType.equals("Scheduled")){
            title.setText(getString(R.string.canceljob));
            areusure_text.setText(getString(R.string.areusure_cancel));
            backup_text.setText(getString(R.string.backup_cancel));
            editText.setHint(getString(R.string.canceledittext));
            yesdecline_cancel_img.setImageResource(R.drawable.yescancel);
        }
    }

    public void setWidgets(){
        cancel = (ImageView)findViewById(R.id.cancel);
        title = (TextView)findViewById(R.id.title);
        areusure_text = (TextView)findViewById(R.id.areusure_text);
        backup_text = (TextView)findViewById(R.id.backup_text);
        editText = (EditText)findViewById(R.id.edittext);
        yesdecline_cancel_img = (ImageView)findViewById(R.id.yesdecline_cancel_img);
    }

    public void setListeners(){
        cancel.setOnClickListener(this);
        yesdecline_cancel_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.yesdecline_cancel_img:
                if (JobType.equals("Scheduled")) {
                    objectType = "canceled_jobs";
                }
                else {
                    objectType = "declined_jobs";
                }
                GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", responseListenerDeclineCancel, this, "Loading");
                responseAsync.execute(getRequestParams());
                break;
        }
    }

    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","create");
        hashMap.put("object",objectType);
        hashMap.put("data[job_id]",JobId);
        hashMap.put("data[reason]",editText.getText().toString());
        hashMap.put("token", Utilities.getSharedPreferences(this).getString(Preferences.AUTH_TOKEN, null));

        return hashMap;
    }

    ResponseListener responseListenerDeclineCancel = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("", "Response" + Response.toString());
            try {
                if(Response.getString("STATUS").equals("SUCCESS"))
                {
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
//                    Toast.makeText(getApplicationContext(),"Job is Scheduled",2000).show();
                    showAlertDialog("SUCCESS", "Job is Declined.");
                    break;
                }
                case 1:{
//                    Toast.makeText(getApplicationContext(),"Error!! Job is not Scheduled",2000).show();
                    showAlertDialog("FAILED", error_message);
                    break;
                }
                case 2:{

                    break;
                }
                case 3:{

                    break;
                }
            }
        }
    };

    private void showAlertDialog(String Title,String Message){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                DeclineJobActivity.this);

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
                        finish();
                    }
                });


        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}

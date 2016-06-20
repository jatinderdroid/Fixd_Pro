package com.fixtconsumer.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sahil on 05-05-2016.
 */
public class GetApiResponseAsyncMutipart extends AsyncTask<HashMap<String, String>, Void, JSONObject> {

    ProgressDialog progressDialog;
    IHttpResponseListener listener;

    String Text;
    MultipartUtility multipartUtility = null ;
    IHttpExceptionListener exceptionListener;
    public GetApiResponseAsyncMutipart(MultipartUtility multipartUtility, IHttpResponseListener listener,IHttpExceptionListener exceptionListener,
                                       Activity activity,String Text) {
        this.multipartUtility = multipartUtility;
        this.listener = listener;
        this.Text = Text;
        this.exceptionListener = exceptionListener;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(Text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... arg0) {
        // TODO Auto-generated method stub
        JSONObject result = null;

        try {

            result = new JSONObject(multipartUtility.finish(this.exceptionListener));
            Log.e("", "result " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (listener != null && result != null) {
            listener.handleResponse(result);
        }
    }

}



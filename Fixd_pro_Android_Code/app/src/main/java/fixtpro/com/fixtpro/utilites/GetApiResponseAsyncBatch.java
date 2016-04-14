package fixtpro.com.fixtpro.utilites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

import fixtpro.com.fixtpro.ResponseListener;

/**
 * Created by sahil on 10-03-2016.
 */
public class GetApiResponseAsyncBatch extends AsyncTask<HashMap<String, String>, Void, JSONObject> {

    ProgressDialog progressDialog;
    ResponseListener listener;
    String Method;
    String Text;
    public GetApiResponseAsyncBatch(String Method, ResponseListener listener,
                               Activity activity,String Text) {
        this.Method = Method;
        this.listener = listener;
        this.Text = Text;
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
            JSONParser jsonParser = new JSONParser();
            result = jsonParser.makeHttpRequest(Constants.BASE_URL_BATCH,Method,arg0[0]);
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



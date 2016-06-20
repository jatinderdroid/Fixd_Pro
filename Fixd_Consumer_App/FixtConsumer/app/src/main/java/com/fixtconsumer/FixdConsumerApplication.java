package com.fixtconsumer;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.net.IHttpExceptionListener;
import com.fixtconsumer.net.JSONParser;
import com.fixtconsumer.singletons.HomeServiceSingleTon;
import com.fixtconsumer.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by sahil on 10-05-2016.
 */
public class FixdConsumerApplication  extends Application{
    ArrayList<HomeServiceBeans> arrayList = new ArrayList<HomeServiceBeans>();
    public static String selectedAppliance = "";
    public static String projectType = "";
    @Override
    public void onCreate() {
        super.onCreate();
        //         Getting Trade Skills on App Start
        new AsyncTask<Void, Void, Void>() {
            JSONObject jsonObject = null;

            @Override
            protected Void doInBackground(Void... params) {
                JSONParser jsonParser = new JSONParser();
                jsonObject = jsonParser.makeHttpRequest(Constants.BASE_URL_SINGLE, "POST", getHomeServiceRequestParams(), new IHttpExceptionListener() {
                    @Override
                    public void handleException(String exception) {
                        Log.e("","exception"+exception);
                    }
                });
                if (jsonObject != null) {
                    try {
                        String STATUS = jsonObject.getString("STATUS");
                        if (STATUS.equals("SUCCESS")) {
//                            JSONObject RESPONSE = jsonObject.getJSONObject("RESPONSE");
                            JSONArray results = jsonObject.getJSONArray("RESPONSE");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject object = results.getJSONObject(i);
                                HomeServiceBeans homeService = new HomeServiceBeans();
                                homeService.setId(object.getInt("id"));
                                homeService.setTitle(object.getString("name"));
                                homeService.setStatus(object.getString("status"));
                                homeService.setDisplay_order(object.getString("display_order"));
                                homeService.setImage(object.getString("name"));
                                arrayList.add(homeService);
                            }
                            Collections.sort(arrayList, new Comparator<HomeServiceBeans>() {

                                public int compare(HomeServiceBeans p1, HomeServiceBeans p2) {
                                    return  Integer.parseInt(p1.getDisplay_order()) - Integer.parseInt(p2.getDisplay_order());
                                }
                            });
                            HomeServiceSingleTon.getInstance().setList(arrayList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return null;
            }
        }.execute();
    }
    private HashMap<String, String> getHomeServiceRequestParams() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("api", "read");
        hashMap.put("object", "services");
        hashMap.put("select", "^*");
        hashMap.put("per_page", "999");
        hashMap.put("page", "1");
        return hashMap;
    }
}

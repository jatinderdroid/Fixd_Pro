package com.fixtconsumer.net;

import org.json.JSONObject;

/**
 * Created by sahil on 05-05-2016.
 */
public interface IHttpResponseListener {
    public void handleResponse(JSONObject response);
}

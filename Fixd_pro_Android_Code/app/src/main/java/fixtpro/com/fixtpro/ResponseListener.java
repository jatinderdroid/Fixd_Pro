package fixtpro.com.fixtpro;

import org.json.JSONObject;

/**
 * Created by sony on 08-02-2016.
 */
public interface ResponseListener {
    public void handleResponse(JSONObject Response);
}

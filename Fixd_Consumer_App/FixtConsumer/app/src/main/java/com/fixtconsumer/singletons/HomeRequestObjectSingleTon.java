package com.fixtconsumer.singletons;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.requests_collection.HomePlansRequestObject;

import java.util.ArrayList;

/**
 * Created by sahil on 16-05-2016.
 */
public class HomeRequestObjectSingleTon {


    private static HomeRequestObjectSingleTon HomeRequestObjectSingleTon = new HomeRequestObjectSingleTon( );

    HomePlansRequestObject homePlansRequestObject = null ;
    public static  String requestName  = "";
    public static String address_file_a_claim_id = "";
    public static int background_drawable  = R.drawable.rekey_back;
    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private HomeRequestObjectSingleTon(){ }

    /* Static 'instance' method */
    public static HomeRequestObjectSingleTon getInstance( ) {
        return HomeRequestObjectSingleTon;
    }

    public  void setHomePlansRequestObject (HomePlansRequestObject Object){
        homePlansRequestObject = Object;
    }
    public  HomePlansRequestObject getHomePlansRequestObject(){
        return homePlansRequestObject;
    }
    public void setRequestName(String requestName){
        this.requestName = requestName ;
    }
    public String getRequestName(){
        return requestName;
    }
}

package com.fixtconsumer.requests_collection;

/**
 * Created by sahil on 16-05-2016.
 */
public class RequestMethods {
   public  static HomePlansRequestObject homePlansRequestObject = null ;
    public static void setHomePlansRequestObject (HomePlansRequestObject Object){
        homePlansRequestObject = Object;
    }
    public static HomePlansRequestObject getHomePlansRequestObject(){
        return homePlansRequestObject;
    }
}

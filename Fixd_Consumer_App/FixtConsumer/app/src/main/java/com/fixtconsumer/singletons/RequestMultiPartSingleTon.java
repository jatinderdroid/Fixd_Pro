package com.fixtconsumer.singletons;

import com.fixtconsumer.beans.WarrentyPlanBean;
import com.fixtconsumer.net.MultipartUtility;

import java.util.ArrayList;

/**
 * Created by sahil on 12-05-2016.
 */
public class RequestMultiPartSingleTon {
    private static RequestMultiPartSingleTon requestMultiPartSingleTon = new RequestMultiPartSingleTon( );
    MultipartUtility  multipartUtility = null ;
    public  static  String requestName = "";
    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private RequestMultiPartSingleTon(){ }

    /* Static 'instance' method */
    public static RequestMultiPartSingleTon getInstance( ) {
        return requestMultiPartSingleTon;
    }
    public void setMultipartUtility(MultipartUtility  multipartUtility){
           this.multipartUtility = multipartUtility;
    }
    public MultipartUtility getMultipartUtility(){
        return multipartUtility;
    }
}

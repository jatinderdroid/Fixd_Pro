package com.fixtconsumer.singletons;

import com.fixtconsumer.net.MultipartUtility;

/**
 * Created by sahil on 10-05-2016.
 */
public class MultipartRequestSingleton {
    static String requestName = "";
    static MultipartUtility multiPartRequest = null ;
    static MultipartRequestSingleton multipartRequestSingleton = new MultipartRequestSingleton();
    private MultipartRequestSingleton(){}
    public static MultipartRequestSingleton getInstance( ) {
        return multipartRequestSingleton;
    }
}

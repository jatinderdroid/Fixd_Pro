package com.fixtconsumer.singletons;

import com.fixtconsumer.beans.CreditCardBean;

import java.util.ArrayList;

/**
 * Created by sahil on 30-05-2016.
 */
public class CreditCardsSingleton {
    private static CreditCardsSingleton creditCardsSingleton = new CreditCardsSingleton( );
    public  static ArrayList<CreditCardBean> arrayList = new ArrayList<CreditCardBean>();


    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private CreditCardsSingleton(){ }

    /* Static 'instance' method */
    public static CreditCardsSingleton getInstance( ) {
        return creditCardsSingleton;
    }
}

package com.fixtconsumer.singletons;

import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.beans.WarrentyPlanBean;

import java.util.ArrayList;

/**
 * Created by sahil on 19-05-2016.
 */
public class AddressesSingleTonClasses {
    private static AddressesSingleTonClasses addressesSingleTonClasses = new AddressesSingleTonClasses( );
    public  static ArrayList<AddressesBean> arrayList = new ArrayList<AddressesBean>();
    public  static ArrayList<AddressesBean> arrayListFileAClain = new ArrayList<AddressesBean>();

    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private AddressesSingleTonClasses(){ }

    /* Static 'instance' method */
    public static AddressesSingleTonClasses getInstance( ) {
        return addressesSingleTonClasses;
    }
}

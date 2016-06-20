package com.fixtconsumer.singletons;

import com.fixtconsumer.beans.WarrentyPlanBean;

import java.util.ArrayList;

/**
 * Created by sahil on 06-05-2016.
 */
public class WarrentyPlanSingleton {
    private static WarrentyPlanSingleton warrentyPlanSingleton = new WarrentyPlanSingleton( );
    public ArrayList<WarrentyPlanBean> arrayList = new ArrayList<WarrentyPlanBean>();
    public ArrayList<WarrentyPlanBean> arrayListPurchased = new ArrayList<WarrentyPlanBean>();
    WarrentyPlanBean warrentyPlanBean = null ;
    public  static  String AddressId = "";
    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private WarrentyPlanSingleton(){ }

    /* Static 'instance' method */
    public static WarrentyPlanSingleton getInstance( ) {
        return warrentyPlanSingleton;
    }
    public void setselectedPlan(WarrentyPlanBean warrentyPlanBean){
        this.warrentyPlanBean = warrentyPlanBean ;
    }
    public WarrentyPlanBean getWarrentyPlanBean(){
        return warrentyPlanBean ;
    }

}

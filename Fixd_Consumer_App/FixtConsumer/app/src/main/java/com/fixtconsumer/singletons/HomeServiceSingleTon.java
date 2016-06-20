package com.fixtconsumer.singletons;

import com.fixtconsumer.beans.HomeServiceBeans;

import java.util.ArrayList;

/**
 * Created by sahil on 10-05-2016.
 */
public class HomeServiceSingleTon {

    private static HomeServiceSingleTon singleton = new HomeServiceSingleTon( );
    ArrayList<HomeServiceBeans> arrayList = new ArrayList<HomeServiceBeans>();
    ArrayList<HomeServiceBeans> arrayListFileAClaim = new ArrayList<HomeServiceBeans>();
        /* A private Constructor prevents any other
         * class from instantiating.
         */
        private HomeServiceSingleTon(){ }

        /* Static 'instance' method */
        public static HomeServiceSingleTon getInstance( ) {
            return singleton;
        }
        /* Other methods protected by singleton-ness */
        public  ArrayList<HomeServiceBeans> getList( ) {
            return arrayList;
        }
        public  ArrayList<HomeServiceBeans> getListFileAClaim( ) {
            return arrayListFileAClaim;
        }
        public  ArrayList<HomeServiceBeans> getListChecked( ) {
            ArrayList<HomeServiceBeans> arrayListChecked  = new ArrayList<HomeServiceBeans>();
            arrayListChecked.clear();
            for (int i = 0 ; i < arrayList.size() ; i++){
                if (arrayList.get(i).isChecked()){
                    arrayListChecked.add(arrayList.get(i));
                }
            }
            return arrayListChecked;
        }
        public  void setList(ArrayList<HomeServiceBeans> arrayList ) {
            this.arrayList = arrayList;
        }
        public  void setListFileAClaim(ArrayList<HomeServiceBeans> arrayListFileAClaim ) {
            this.arrayListFileAClaim = arrayListFileAClaim;
        }
}


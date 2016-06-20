package com.fixtconsumer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.HomePlansBean;
import com.fixtconsumer.beans.HowManyLocksBean;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sahil on 05-05-2016.
 */
public class Utility {
    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean hideKeyBoad(Context context, View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }
    public static SharedPreferences getSharedPreferences(Context _context){
        return _context.getSharedPreferences(Preferences.FIXID_CONSUMER_PREFERNCES, Context.MODE_PRIVATE);
    }
//    plantype = rekey || purchase || claim
    public static ArrayList<HomePlansBean> getHomePlansList(String plantype){
        ArrayList<HomePlansBean> arrayList = new ArrayList<HomePlansBean>();
        if (plantype.equals("rekey")){
            arrayList.add(new HomePlansBean(Constants.FREE_RE_KEY, R.drawable.re_key_small));
            arrayList.add(new HomePlansBean(Constants.FILE_CLAIM,R.drawable.purchase_claim_small));
            arrayList.add(new HomePlansBean(Constants.HOME_SERVICE,R.drawable.home_service_small));
        }else if (plantype.equals("purchase")){
            arrayList.add(new HomePlansBean(Constants.PURCHASE_PLAN,R.drawable.purchase_claim_big));
            arrayList.add(new HomePlansBean(Constants.HOME_SERVICE,R.drawable.home_service_big));
        }else if (plantype.equals("claim")){
            arrayList.add(new HomePlansBean(Constants.FILE_CLAIM,R.drawable.purchase_claim_big));
            arrayList.add(new HomePlansBean(Constants.HOME_SERVICE,R.drawable.home_service_big));
        }
        return arrayList;
    }
    public static int getHomeServiceImageResourse(String name){
        if(name.equals("Appliances"))
            return R.drawable.appliance;
        else if (name.equals("Plumbing"))
            return R.drawable.plumbing;
        else if (name.equals("HVAC"))
            return R.drawable.hvac;
        else if (name.equals("Electrical"))
            return R.drawable.electrical;
        else if (name.equals("Electronics"))
            return R.drawable.electronics;
        else if (name.equals("Handyman"))
            return R.drawable.handyman;
        else if (name.equals("Sprinkler"))
            return R.drawable.sprinkler;
        else if (name.equals("Garage Door"))
            return R.drawable.garage_door;
        else if (name.equals("Pool/Spa"))
            return R.drawable.pool_and_spa;
        else if (name.equals("Home Inspection"))
            return R.drawable.home_inspection;
        else if (name.equals("Cleaning"))
            return R.drawable.cleaning;
        else if (name.equals("Fencing"))
            return R.drawable.fencing;
        else if (name.equals("Outdoor & Landscape"))
            return R.drawable.outdoor_and_landscape;
        else if (name.equals("Painting"))
            return R.drawable.painting;
        else if (name.equals("Remodeling"))
            return R.drawable.remodelling;
        else if (name.equals("Flooring"))
            return R.drawable.flooring;
        else if (name.equals("Alarm"))
            return R.drawable.others;
        return R.drawable.others;
    }
    public static int getSelectedHomeServicePhoto(String name){
        if(name.equals("Appliances"))
            return R.drawable.appliance_bg;
        else if (name.equals("Plumbing"))
            return R.drawable.plumbing_bg;
        else if (name.equals("HVAC"))
            return R.drawable.hvac_bg;
        else if (name.equals("Electrical"))
            return R.drawable.electrical_bg;
        else if (name.equals("Electronics"))
            return R.drawable.electronics;
        else if (name.equals("Handyman"))
            return R.drawable.handyman;
        else if (name.equals("Sprinkler"))
            return R.drawable.sprinkler_bg;
        else if (name.equals("Garage Door"))
            return R.drawable.garage_bg;
        else if (name.equals("Pool/Spa"))
            return R.drawable.poolspa_bg;
        else if (name.equals("Home Inspection"))
            return R.drawable.home_inspection;
        else if (name.equals("Cleaning"))
            return R.drawable.cleaning;
        else if (name.equals("Fencing"))
            return R.drawable.fencing;
        else if (name.equals("Outdoor & Landscape"))
            return R.drawable.outdoor_and_landscape;
        else if (name.equals("Painting"))
            return R.drawable.painting;
        else if (name.equals("Remodeling"))
            return R.drawable.remodelling;
        else if (name.equals("Flooring"))
            return R.drawable.flooring;
        else if (name.equals("Alarm"))
            return R.drawable.others;
        else if (name.equals("Re Key"))
            return R.drawable.rekey_back;
        return R.drawable.others;
    }
    public static  int getSelectedPlanPhoto(String name){
        if (name.equals("Complete")){
            return R.drawable.complete_plan_bg;
        }else if (name.equals("System")){
            return R.drawable.hvac_bg;
        }else if (name.equals("Appliance")){
            return R.drawable.appliance_bg;
        }else if (name.equals("Electronics")){
            return R.drawable.electronics_plan_bg;
        }
        return R.drawable.complete_plan_bg;
    }
    public static int getApplianceImage(String appliance){
        if (appliance.equals("Washer"))
            return R.drawable.washer;
        else  if (appliance.equals("Dryer"))
            return R.drawable.fan;
        else  if (appliance.equals("Cooktop"))
            return R.drawable.cooktop;
        else  if (appliance.equals("Oven"))
            return R.drawable.microware;
        else  if (appliance.equals("Range Oven"))
            return R.drawable.range;
        else  if (appliance.equals("Double Oven"))
            return R.drawable.microware;
        else  if (appliance.equals("Range Hood"))
            return R.drawable.range;
        else  if (appliance.equals("Refrigerator"))
            return R.drawable.fridge;
        else  if (appliance.equals("Dishwasher"))
            return R.drawable.dishwasher;
        else  if (appliance.equals("Garbage Disposal"))
            return R.drawable.garbage;
        else  if (appliance.equals("Ice Maker"))
            return R.drawable.ice_maker;
        else  if (appliance.equals("Stand-alone Freezer"))
            return R.drawable.freezer;
        else  if (appliance.equals("Installed Microwave"))
            return R.drawable.microware;
        else  if (appliance.equals("Wine Refrigerator"))
            return R.drawable.fridge;else
        if (appliance.equals("Small Refrigerator"))
            return R.drawable.fridge;
        if (appliance.equals("Mini-Fridge"))
            return R.drawable.fridge;
        if (appliance.equals("Re Key"))
            return R.drawable.key_thumb;
        return R.drawable.microware;
    }
    public static  ArrayList<HowManyLocksBean> getLocksList(){
        ArrayList<HowManyLocksBean> list = new ArrayList<HowManyLocksBean>();
        list.add(new HowManyLocksBean("1-3","Free"));
        list.add(new HowManyLocksBean("4","$50"));
        list.add(new HowManyLocksBean("5","$85"));
        list.add(new HowManyLocksBean("6+","$125"));
        return list ;
    }


    public static int getCreditCardImage(int number){
        if (number == 34 || number == 37){
//            AmericanExpress image
            return R.drawable.check;
        }else if (number == 36){
//            Diners club image
            return R.drawable.check;
        }else if (number == 38){
//            Carte Blanche image
            return R.drawable.check;
        }else if (number >= 51 && number <= 55){
//            Mastercard image
            return R.drawable.check;
        }else if (number >= 51 && number <= 55){
//            Mastercard image
            return R.drawable.check;
        }else if (number == 2014 && number == 2149){
//            EnRoute image
            return R.drawable.check;
        }else if (number == 2131 && number == 1800){
//            JCB image
            return R.drawable.check;
        }else if (number == 6011){
//            Discover image
            return R.drawable.check;
        }else if (number >= 300 && number <= 305){
//            American Diners Club image
            return R.drawable.check;
        }

        return R.drawable.check;
    }

}

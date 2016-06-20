package com.fixtconsumer.requests_collection;

import com.fixtconsumer.beans.AppliancesModal;
import com.fixtconsumer.beans.HomeServiceBeans;
import com.fixtconsumer.beans.TellUsMoreBean;
import com.fixtconsumer.beans.WhatsProblemBean;

import java.util.ArrayList;

/**
 * Created by sahil on 16-05-2016.
 */
public class HomePlansRequestObject {


//    Choose Service
    public HomeServiceBeans chooseService = null ;

//    What type of Project
    public String typeofprojectName = "Repair";

//  Which Appliance
    public ArrayList<AppliancesModal> whichAppliancelist = null ;

    public HomeServiceBeans getChooseService() {
        return chooseService;
    }

    public void setChooseService(HomeServiceBeans chooseService) {
        this.chooseService = chooseService;
    }

    public String getTypeofprojectName() {
        if (typeofprojectName.equals("Repair"))
        return typeofprojectName;
        else if (typeofprojectName.contains("Install"))
            return "Install";
        else if (typeofprojectName.contains("Re Key"))
            return "Re Key";
        else return "Maintain";
    }

    public void setTypeofprojectName(String typeofprojectName) {
        this.typeofprojectName = typeofprojectName;
    }

    public ArrayList<AppliancesModal> getWhichAppliancelist() {
        return whichAppliancelist;
    }

    public void setWhichAppliancelist(ArrayList<AppliancesModal> whichAppliancelist) {
        this.whichAppliancelist = whichAppliancelist;
    }
//    Whats the problem
    WhatsProblemBean whatsProblemBean = null ;

    public WhatsProblemBean getWhatsProblemBean() {
        return whatsProblemBean;
    }

    public void setWhatsProblemBean(WhatsProblemBean whatsProblemBean) {
        this.whatsProblemBean = whatsProblemBean;
    }
    //    power source
    public static  int powersourceControlvar = 0 ;

    ArrayList<TellUsMoreBean> tellUsMoreBeanArrayList = new ArrayList<TellUsMoreBean>() ;

    public ArrayList<TellUsMoreBean> getTellUsMoreBeanArrayList() {
        return tellUsMoreBeanArrayList;
    }

    public void setTellUsMoreBeanArrayList(ArrayList<TellUsMoreBean> tellUsMoreBeanArrayList) {
        this.tellUsMoreBeanArrayList = tellUsMoreBeanArrayList;
    }
//     Time Slot
    String TimeSlotId = "";
    String TimeSlotName = "" ;
    String SchedulingDate = "";

    public static int getPowersourceControlvar() {
        return powersourceControlvar;
    }

    public static void setPowersourceControlvar(int powersourceControlvar) {
        HomePlansRequestObject.powersourceControlvar = powersourceControlvar;
    }

    public String getTimeSlotId() {
        return TimeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        TimeSlotId = timeSlotId;
    }

    public String getTimeSlotName() {
        return TimeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        TimeSlotName = timeSlotName;
    }

    public String getSchedulingDate() {
        return SchedulingDate;
    }

    public void setSchedulingDate(String schedulingDate) {
        SchedulingDate = schedulingDate;
    }
}

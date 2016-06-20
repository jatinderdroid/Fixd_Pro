package com.fixtconsumer.singletons;

import com.fixtconsumer.beans.JobModal;
import com.fixtconsumer.beans.WarrentyPlanBean;

import java.util.ArrayList;

/**
 * Created by sahil on 03-06-2016.
 */
public class MyJobsSingleton {
    private static MyJobsSingleton singleton = new MyJobsSingleton( );

    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private MyJobsSingleton(){ }

    /* Static 'instance' method */
    public static MyJobsSingleton getInstance( ) {
        return singleton;
    }

    ArrayList<JobModal> availablejoblist = new ArrayList<JobModal>();
    ArrayList<JobModal> schedulejoblist  = new ArrayList<JobModal>();
    ArrayList<JobModal> completedjoblist  = new ArrayList<JobModal>();

    public  int pageAvaileble = 1 ;
    public  int pageSheduled = 1 ;
    public  int compltedpage = 1 ;

    public int getPageAvaileble() {
        return pageAvaileble;
    }

    public void setPageAvaileble(int pageAvaileble) {
        this.pageAvaileble = pageAvaileble;
    }

    public int getPageSheduled() {
        return pageSheduled;
    }

    public void setPageSheduled(int pageSheduled) {
        this.pageSheduled = pageSheduled;
    }

    public int getCompltedpage() {
        return compltedpage;
    }

    public void setCompltedpage(int compltedpage) {
        this.compltedpage = compltedpage;
    }

    public String getNextCompleted() {
        return nextCompleted;
    }

    public void setNextCompleted(String nextCompleted) {
        this.nextCompleted = nextCompleted;
    }

    public String getNextSchduled() {
        return nextSchduled;
    }

    public void setNextSchduled(String nextSchduled) {
        this.nextSchduled = nextSchduled;
    }

    public String getNextAvailable() {
        return nextAvailable;
    }

    public void setNextAvailable(String nextAvailable) {
        this.nextAvailable = nextAvailable;
    }

    public  String nextCompleted = "null";
    public  String nextSchduled = "null";
    public  String nextAvailable = "null";
    /* A private Constructor prevents any other
     * class from instantiating.
     */

    public ArrayList<JobModal> getAvailablejoblist() {
        return availablejoblist;
    }

    public void setAvailablejoblist(ArrayList<JobModal> availablejoblist) {
        this.availablejoblist = availablejoblist;
    }

    public ArrayList<JobModal> getSchedulejoblist() {
        return schedulejoblist;
    }

    public void setSchedulejoblist(ArrayList<JobModal> schedulejoblist) {
        this.schedulejoblist = schedulejoblist;
    }

    public ArrayList<JobModal> getCompletedjoblist() {
        return completedjoblist;
    }

    public void setCompletedjoblist(ArrayList<JobModal> completedjoblist) {
        this.completedjoblist = completedjoblist;
    }
}


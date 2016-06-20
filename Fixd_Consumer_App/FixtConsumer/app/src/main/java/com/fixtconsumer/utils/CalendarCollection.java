package com.fixtconsumer.utils;

import java.util.ArrayList;

/**
 * Created by sahil on 11-05-2016.
 */
public class CalendarCollection {
    public String date="";
    public String event_message="";

    public static ArrayList<CalendarCollection> date_collection_arr = new ArrayList<CalendarCollection>();
    public CalendarCollection(String date, String event_message){
        this.date = date;
        this.event_message = event_message;
    }

}

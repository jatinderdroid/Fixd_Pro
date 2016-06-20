package com.fixtconsumer.beans;

import java.io.Serializable;

/**
 * Created by sahil on 10-05-2016.
 */
public class HomePlansBean implements Serializable{
    private String name = "";
    private int photo = 0;
    public  HomePlansBean(String name, int photo){
        this.name = name;
        this.photo = photo ;
    }
    public String getName(){
        return name;
    }
    public int getPhoto(){
        return photo;
    }
}

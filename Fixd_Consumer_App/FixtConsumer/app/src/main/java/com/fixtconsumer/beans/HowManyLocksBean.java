package com.fixtconsumer.beans;

import com.fixtconsumer.activities.HomeActivity;

import java.io.Serializable;

/**
 * Created by sahil on 19-05-2016.
 */
public class HowManyLocksBean implements Serializable{
    String count = "";
    String cost = "";
    public HowManyLocksBean(String count ,String cost){
        this.count = count ;
        this.cost = cost ;
    }
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}

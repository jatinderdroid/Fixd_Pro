package com.fixtconsumer.beans;

import java.io.Serializable;

/**
 * Created by sahil on 20-06-2016.
 */
public class Brands implements Serializable {
    String brand_name = "";
    String id = "";

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


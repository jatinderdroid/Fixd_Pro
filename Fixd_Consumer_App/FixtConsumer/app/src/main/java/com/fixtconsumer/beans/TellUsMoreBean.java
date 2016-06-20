package com.fixtconsumer.beans;

import java.io.Serializable;

/**
 * Created by sahil on 16-05-2016.
 */
public class TellUsMoreBean implements Serializable{
    String imgPath = "";
    String description = "";
    String Brand = "";
    String BrandID = "";

    public String getBrandID() {
        return BrandID;
    }

    public void setBrandID(String brandID) {
        BrandID = brandID;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }
}

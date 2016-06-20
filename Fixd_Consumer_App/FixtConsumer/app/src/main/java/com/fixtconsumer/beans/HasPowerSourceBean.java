package com.fixtconsumer.beans;

/**
 * Created by sahil on 16-05-2016.
 */
public class HasPowerSourceBean {
    private String Name;
    private int Image;
    boolean isChecked = false ;
    public HasPowerSourceBean(String Name,boolean isChecked){
        this.isChecked = isChecked ;
        this.Name = Name;
    }
    public String getName() {
        return Name;
    }
    public void setChecked (boolean isChecked){
        this.isChecked = isChecked ;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int Image) {
        this.Image = Image;
    }
    public boolean isChecked(){
        return isChecked;
    }
}

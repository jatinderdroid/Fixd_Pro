package com.fixtconsumer.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sahil on 06-05-2016.
 */
public class WarrentyPlanBean implements Serializable {
    String id = "";
    String name = "";
    String price = "";
    String price_inclusive_tax = "";
    String tax = "";
    String type = "";
    String _order = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    ArrayList<ApplianceTypeBeans> applianceTypeBeansArrayList = new ArrayList<ApplianceTypeBeans>();
    ArrayList<SimplePlansBean> simplePlansBeanArrayList = new ArrayList<SimplePlansBean>();

    public ArrayList<SimplePlansBean> getSimplePlansBeanArrayList() {
        return simplePlansBeanArrayList;
    }

    public void setSimplePlansBeanArrayList(ArrayList<SimplePlansBean> simplePlansBeanArrayList) {
        this.simplePlansBeanArrayList = simplePlansBeanArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_inclusive_tax() {
        return price_inclusive_tax;
    }

    public void setPrice_inclusive_tax(String price_inclusive_tax) {
        this.price_inclusive_tax = price_inclusive_tax;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String get_order() {
        return _order;
    }

    public void set_order(String _order) {
        this._order = _order;
    }

    public ArrayList<ApplianceTypeBeans> getApplianceTypeBeansArrayList() {
        return applianceTypeBeansArrayList;
    }

    public void setApplianceTypeBeansArrayList(ArrayList<ApplianceTypeBeans> applianceTypeBeansArrayList) {
        this.applianceTypeBeansArrayList = applianceTypeBeansArrayList;
    }
}

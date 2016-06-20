package com.fixtconsumer.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sahil on 06-05-2016.
 */
public class SimplePlansBean implements Serializable{
    String id = "";
    String name = "";
    String price = "";
    String type = "";
    String _order = "";
    String combined_plan_id = "";
    String simple_plan_id = "";
    String warranty_plans_id = "";
    ApplianceTypeBeans applianceTypeBeans = null;

    public ApplianceTypeBeans getApplianceTypeBeans() {
        return applianceTypeBeans;
    }
    ArrayList<ApplianceTypeBeans> arrayList = new ArrayList<ApplianceTypeBeans>();
    public void setApplianceTypeBeans(ApplianceTypeBeans applianceTypeBeans) {
        this.applianceTypeBeans = applianceTypeBeans;
    }

    public ArrayList<ApplianceTypeBeans> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ApplianceTypeBeans> arrayList) {
        this.arrayList = arrayList;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get_order() {
        return _order;
    }

    public void set_order(String _order) {
        this._order = _order;
    }

    public String getCombined_plan_id() {
        return combined_plan_id;
    }

    public void setCombined_plan_id(String combined_plan_id) {
        this.combined_plan_id = combined_plan_id;
    }

    public String getSimple_plan_id() {
        return simple_plan_id;
    }

    public void setSimple_plan_id(String simple_plan_id) {
        this.simple_plan_id = simple_plan_id;
    }

    public String getWarranty_plans_id() {
        return warranty_plans_id;
    }

    public void setWarranty_plans_id(String warranty_plans_id) {
        this.warranty_plans_id = warranty_plans_id;
    }
}

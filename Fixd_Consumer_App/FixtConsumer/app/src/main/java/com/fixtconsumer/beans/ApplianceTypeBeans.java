package com.fixtconsumer.beans;

import java.io.Serializable;

/**
 * Created by sahil on 06-05-2016.
 */
public class ApplianceTypeBeans  implements Serializable{
    String name = "";
    String warranty_plans_id = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarranty_plans_id() {
        return warranty_plans_id;
    }

    public void setWarranty_plans_id(String warranty_plans_id) {
        this.warranty_plans_id = warranty_plans_id;
    }
}

package fixtpro.com.fixtpro.beans;

import java.io.Serializable;

/**
 * Created by sahil on 16-03-2016.
 */
public class JobPartsUsedModal implements Serializable {
    String id = "";
    String part_num = "";
    String part_desc = "";
    String qty = "";
    String part_cost = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPart_num() {
        return part_num;
    }

    public void setPart_num(String part_num) {
        this.part_num = part_num;
    }

    public String getPart_desc() {
        return part_desc;
    }

    public void setPart_desc(String part_desc) {
        this.part_desc = part_desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPart_cost() {
        return part_cost;
    }

    public void setPart_cost(String part_cost) {
        this.part_cost = part_cost;
    }
}

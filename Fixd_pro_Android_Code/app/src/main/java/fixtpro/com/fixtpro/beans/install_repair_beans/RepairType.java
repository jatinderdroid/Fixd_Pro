package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;

/**
 * Created by sahil on 05-04-2016.
 */
public class RepairType implements Serializable {
    String Type = "";
    String Price = "0";
    String Id  = "";
     public RepairType(){

     }
    public RepairType(String Type){
        this.Type = Type ;
    }
    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

}

package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;

/**
 * Created by sahil on 05-04-2016.
 */
public class WorkOrder implements Serializable {
    String disgnostic = "";
    String sub_total = "";
    String tax = "";
    String total = "";

    public String getDisgnostic() {
        return disgnostic;
    }

    public void setDisgnostic(String disgnostic) {
        this.disgnostic = disgnostic;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

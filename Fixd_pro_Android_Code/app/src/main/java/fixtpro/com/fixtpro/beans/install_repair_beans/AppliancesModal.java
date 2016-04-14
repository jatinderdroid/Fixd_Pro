package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;

/**
 * Created by sahil on 06-04-2016.
 */
public class AppliancesModal implements Serializable{
    String id = "";
    String name = "";
    String service_id = "";
    String has_power_source = "";
    String image_original = "";

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

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getHas_power_source() {
        return has_power_source;
    }

    public void setHas_power_source(String has_power_source) {
        this.has_power_source = has_power_source;
    }

    public String getImage_original() {
        return image_original;
    }

    public void setImage_original(String image_original) {
        this.image_original = image_original;
    }
}

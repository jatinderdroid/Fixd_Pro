package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;

/**
 * Created by sahil on 05-04-2016.
 */
public class RepairInfo  implements Serializable{
    String image = "";
    String UnitManufacturer = "";
    String ModalNumber = "";
    String SerialNumber = "";
    String WorkDescription = "";

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnitManufacturer() {
        return UnitManufacturer;
    }

    public void setUnitManufacturer(String unitManufacturer) {
        UnitManufacturer = unitManufacturer;
    }

    public String getModalNumber() {
        return ModalNumber;
    }

    public void setModalNumber(String modalNumber) {
        ModalNumber = modalNumber;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getWorkDescription() {
        return WorkDescription;
    }

    public void setWorkDescription(String workDescription) {
        WorkDescription = workDescription;
    }
}

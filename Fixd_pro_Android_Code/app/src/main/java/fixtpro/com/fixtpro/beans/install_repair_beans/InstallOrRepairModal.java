package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sahil on 05-04-2016.
 */
public class InstallOrRepairModal implements Serializable{
    RepairInfo repairInfo = new RepairInfo();
    RepairType repairType = new RepairType();
    WhatsWrong whatsWrong = new WhatsWrong();
    Signature signature = new Signature();
    WorkOrder workOrder = new WorkOrder();
    ArrayList<Parts> partsArrayList = new ArrayList<Parts>();

    public RepairInfo getRepairInfo() {
        return repairInfo;
    }

    public void setRepairInfo(RepairInfo repairInfo) {
        this.repairInfo = repairInfo;
    }

    public RepairType getRepairType() {
        return repairType;
    }

    public void setRepairType(RepairType repairType) {
        this.repairType = repairType;
    }

    public WhatsWrong getWhatsWrong() {
        return whatsWrong;
    }

    public void setWhatsWrong(WhatsWrong whatsWrong) {
        this.whatsWrong = whatsWrong;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public ArrayList<Parts> getPartsArrayList() {
        return partsArrayList;
    }

    public void setPartsArrayList(ArrayList<Parts> partsArrayList) {
        this.partsArrayList = partsArrayList;
    }
}

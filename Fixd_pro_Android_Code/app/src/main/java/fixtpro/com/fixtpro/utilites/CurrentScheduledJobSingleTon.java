package fixtpro.com.fixtpro.utilites;

import java.util.ArrayList;

import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.beans.install_repair_beans.InstallOrRepairModal;
import fixtpro.com.fixtpro.beans.install_repair_beans.ReapirInstallProcessModal;

/**
 * Created by sahil on 31-03-2016.
 */
public class CurrentScheduledJobSingleTon {

    AvailableJobModal availableJobModal = null;
    JobAppliancesModal jobApplianceModal = null;
    ArrayList<ReapirInstallProcessModal> repairInstallProceessList = new ArrayList<ReapirInstallProcessModal>();
    ReapirInstallProcessModal reapirInstallProcessModal = null ;
    InstallOrRepairModal installOrRepairModal = new InstallOrRepairModal();
    private static CurrentScheduledJobSingleTon CurrentScheduledJobSingleTon = new CurrentScheduledJobSingleTon( );


    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private CurrentScheduledJobSingleTon(){ }

    /* Static 'instance' method */
    public static CurrentScheduledJobSingleTon getInstance( ) {
        return CurrentScheduledJobSingleTon;
    }

    /* Other methods protected by CurrentScheduledJobSingleTon-ness */
    public   void setCurrentJonModal(AvailableJobModal availableJobModal) {
        this.availableJobModal = availableJobModal ;
    }
    public void setSelectedJobApplianceModal(JobAppliancesModal jobApplianceModal){
        this.jobApplianceModal = jobApplianceModal;
        repairInstallProceessList.clear();
        if (jobApplianceModal.getJob_appliances_service_type().equals("Install")){
            repairInstallProceessList.add(new ReapirInstallProcessModal(Constants.WHATS_WRONG));
        }
        repairInstallProceessList.add(new ReapirInstallProcessModal(Constants.REPAIR_TYPE));
        repairInstallProceessList.add(new ReapirInstallProcessModal(Constants.PARTS));
        repairInstallProceessList.add(new ReapirInstallProcessModal(Constants.WORK_ORDER));
        repairInstallProceessList.add(new ReapirInstallProcessModal(Constants.REPAIR_INFO));
        repairInstallProceessList.add(new ReapirInstallProcessModal(Constants.SIGNATURE));
    }
    public JobAppliancesModal getJobApplianceModal(){
        return jobApplianceModal;
    }
    public ArrayList<ReapirInstallProcessModal> getReapirInstallProcessModalList(){
        return repairInstallProceessList;
    }
    public  AvailableJobModal getCurrentJonModal() {
        return availableJobModal;
    }

    public void setCurrentReapirInstallProcessModal(ReapirInstallProcessModal reapirInstallProcessModal){
        this.reapirInstallProcessModal = reapirInstallProcessModal ;
    }
    public ReapirInstallProcessModal getCurrentReapirInstallProcessModal(){
        return reapirInstallProcessModal;
    }
    public InstallOrRepairModal  getInstallOrRepairModal(){
        return installOrRepairModal;
    }
}

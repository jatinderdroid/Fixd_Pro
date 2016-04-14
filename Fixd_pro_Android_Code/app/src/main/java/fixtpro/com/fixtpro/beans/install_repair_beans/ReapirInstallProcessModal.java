package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;

/**
 * Created by sahil on 06-04-2016.
 */
public class ReapirInstallProcessModal implements Serializable{
    String name = "";
    boolean isCompleted = false ;
    public ReapirInstallProcessModal(String name){
        this.name = name ;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}

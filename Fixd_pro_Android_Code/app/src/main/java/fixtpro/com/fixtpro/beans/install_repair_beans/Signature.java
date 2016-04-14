package fixtpro.com.fixtpro.beans.install_repair_beans;

import java.io.Serializable;

/**
 * Created by sahil on 05-04-2016.
 */
public class Signature implements Serializable{
    String signature_path = null;

    public String getSignature_path() {
        return signature_path;
    }

    public void setSignature_path(String signature_path) {
        this.signature_path = signature_path;
    }
}

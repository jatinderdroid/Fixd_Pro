package fixtpro.com.fixtpro.beans;

import java.io.Serializable;

/**
 * Created by sony on 11-02-2016.
 */
public class TechnicianModal implements Serializable{
    String id = "";
    String email = "";
    String role = "" ;
    String phone = "" ;
    String firstName = "";
    String lastName = "";
    boolean ispickjob = false;
    public String getFirstName() {
        return firstName;
    }

    public boolean ispickjob() {
        return ispickjob;
    }

    public void setIspickjob(boolean ispickjob) {
        this.ispickjob = ispickjob;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

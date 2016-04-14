package fixtpro.com.fixtpro.beans;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by sony on 17-02-2016.
 */
public class JobAppliancesModal implements Serializable{
    //    job_appliances Array

    public String getJob_appliances_id() {
        return job_appliances_id;
    }

    public void setJob_appliances_id(String job_appliances_id) {
        this.job_appliances_id = job_appliances_id;
    }

    String job_appliances_id = "";
    String job_appliances_job_id = "";
    String job_appliances_appliance_id ="";

    public boolean isProcessCompleted() {
        return isProcessCompleted;
    }

    public void setIsProcessCompleted(boolean isProcessCompleted) {
        this.isProcessCompleted = isProcessCompleted;
    }

    String job_appliances_appliance_description;
    String job_appliances_service_type = "";

    public String getJob_appliances_customer_compalint() {
        return job_appliances_customer_compalint;
    }

    public void setJob_appliances_customer_compalint(String job_appliances_customer_compalint) {
        this.job_appliances_customer_compalint = job_appliances_customer_compalint;
    }

    String job_appliances_customer_compalint = "";
    //  appliance_type object in job_appliances Array
    String appliance_type_id = "";
    String appliance_type_name = "";
    String appliance_type_service_id = "";
    String appliance_type_has_power_source = "";
    boolean isProcessCompleted = false ;
    public String getJob_appliances_appliance_description() {
        return job_appliances_appliance_description;
    }

    public void setJob_appliances_appliance_description(String job_appliances_appliance_description) {
        this.job_appliances_appliance_description = job_appliances_appliance_description;
    }

    public String getJob_appliances_service_type() {
        return job_appliances_service_type;
    }

    public void setJob_appliances_service_type(String job_appliances_service_type) {
        this.job_appliances_service_type = job_appliances_service_type;
    }

    String appliance_type_soft_deleted = "";

    // image object in job_appliances Array
    String img_original = "";
    String img_160x170 = "";
    String img_150x150 = "";
    String img_30x30 = "";
    String img_75x75 = "";
    Bitmap img_bitmap ;
    // services object in job_appliances Array
    String service_id = "";
    String service_name = "";
    String service_created_at = "";
    String service_updated_at = "";

    public Bitmap getImg_bitmap() {
        return img_bitmap;
    }

    public void setImg_bitmap(Bitmap img_bitmap) {
        this.img_bitmap = img_bitmap;
    }

    //    job_appliances Array Getter Setters

    public String getJob_appliances_job_id() {
        return job_appliances_job_id;
    }

    public void setJob_appliances_job_id(String job_appliances_job_id) {
        this.job_appliances_job_id = job_appliances_job_id;
    }

    public String getJob_appliances_appliance_id() {
        return job_appliances_appliance_id;
    }

    public void setJob_appliances_appliance_id(String job_appliances_appliance_id) {
        this.job_appliances_appliance_id = job_appliances_appliance_id;
    }

// appliance_type object in job_appliances Array

    public String getAppliance_type_id() {
        return appliance_type_id;
    }

    public void setAppliance_type_id(String appliance_type_id) {
        this.appliance_type_id = appliance_type_id;
    }

    public String getAppliance_type_has_power_source() {
        return appliance_type_has_power_source;
    }

    public void setAppliance_type_has_power_source(String appliance_type_has_power_source) {
        this.appliance_type_has_power_source = appliance_type_has_power_source;
    }

    public String getAppliance_type_service_id() {
        return appliance_type_service_id;
    }

    public void setAppliance_type_service_id(String appliance_type_service_id) {
        this.appliance_type_service_id = appliance_type_service_id;
    }

    public String getAppliance_type_name() {
        return appliance_type_name;
    }

    public void setAppliance_type_name(String appliance_type_name) {
        this.appliance_type_name = appliance_type_name;
    }

    public String getAppliance_type_soft_deleted() {
        return appliance_type_soft_deleted;
    }

    public void setAppliance_type_soft_deleted(String appliance_type_soft_deleted) {
        this.appliance_type_soft_deleted = appliance_type_soft_deleted;
    }

    // image object in job_appliances Array


    public String getImg_original() {
        return img_original;
    }

    public void setImg_original(String img_original) {
        this.img_original = img_original;
    }

    public String getImg_160x170() {
        return img_160x170;
    }

    public void setImg_160x170(String img_160x170) {
        this.img_160x170 = img_160x170;
    }

    public String getImg_150x150() {
        return img_150x150;
    }

    public void setImg_150x150(String img_150x150) {
        this.img_150x150 = img_150x150;
    }

    public String getImg_30x30() {
        return img_30x30;
    }

    public void setImg_30x30(String img_30x30) {
        this.img_30x30 = img_30x30;
    }

    public String getImg_75x75() {
        return img_75x75;
    }

    public void setImg_75x75(String img_75x75) {
        this.img_75x75 = img_75x75;
    }


    // services object in job_appliances Array


    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_created_at() {
        return service_created_at;
    }

    public void setService_created_at(String service_created_at) {
        this.service_created_at = service_created_at;
    }

    public String getService_updated_at() {
        return service_updated_at;
    }

    public void setService_updated_at(String service_updated_at) {
        this.service_updated_at = service_updated_at;
    }
}

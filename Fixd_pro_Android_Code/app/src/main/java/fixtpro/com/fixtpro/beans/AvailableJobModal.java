package fixtpro.com.fixtpro.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sony on 16-02-2016.
 */
public class AvailableJobModal implements Serializable{
    String contact_name;
    String created_at;
    String customer_id;
    String customer_notes;
    String finished_at;
    String id;
    String job_id;
    double latitude;
    String locked_by;
    String locked_on;
    double longitude;
    String phone;
    String pro_id;
    String request_date;
    String service_id;
    String service_type;
    String started_at;
    String status;

    String time_slot_id;
    String title;
    String total_cost;
    String updated_at;
    String warranty;

    public ArrayList<JobPartsUsedModal> getJob_parts_used_list() {
        return job_parts_used_list;
    }

    public void setJob_parts_used_list(ArrayList<JobPartsUsedModal> job_parts_used_list) {
        this.job_parts_used_list = job_parts_used_list;
    }

    ArrayList<JobAppliancesModal> job_appliances_arrlist = new ArrayList<>();
    ArrayList<JobPartsUsedModal> job_parts_used_list = new ArrayList<JobPartsUsedModal>();
    // time slots object
    String timeslot_id;
    String timeslot_start;
    String timeslot_end;
    String timeslot_soft_deleted;

    // job customer addresses
    String job_customer_addresses_id = "(null)";
    String job_customer_addresses_zip = "(null)";
    String job_customer_addresses_city = "(null)";
    String job_customer_addresses_state =  "(null)";
    String job_customer_addresses_address = "(null)";
    String job_customer_addresses_address_2 = "(null)";
    String job_customer_addresses_updated_at = "(null)";
    String job_customer_addresses_created_at = "(null)";
    String job_customer_addresses_job_id = "(null)";

//    Cost Details of Job Completed
    String cost_details_tripcharges = "0.00";
    String cost_details_tax = "0.00";
    String cost_details_installation = "";
    String cost_details_fixd_fee = "0.00";
    String cost_details_fixd_fee_percentage = "5";
    String cost_details_pro_earned = "0.00";
    String cost_details_customer_payment = "0.00";
    String cost_details_repair_type = "";
    String cost_details_repair_value = "0.00";

//    Technician information
    String technician_id = "";
    String technician_fname = "";
    String technician_lname = "";
    String technician_pickup_jobs = "";
    String technician_profile_image = "";
    String technician_avg_rating  = "";
    String technician_scheduled_job_count  = "";
    String technician_completed_job_count  = "";

    public String getTechnician_fname() {
        return technician_fname;
    }

    public void setTechnician_fname(String technician_fname) {
        this.technician_fname = technician_fname;
    }

    public String getTechnician_lname() {
        return technician_lname;
    }

    public void setTechnician_lname(String technician_lname) {
        this.technician_lname = technician_lname;
    }

    public String getTechnician_pickup_jobs() {
        return technician_pickup_jobs;
    }

    public void setTechnician_pickup_jobs(String technician_pickup_jobs) {
        this.technician_pickup_jobs = technician_pickup_jobs;
    }

    public String getTechnician_profile_image() {
        return technician_profile_image;
    }

    public void setTechnician_profile_image(String technician_profile_image) {
        this.technician_profile_image = technician_profile_image;
    }

    public String getTechnician_avg_rating() {
        return technician_avg_rating;
    }

    public void setTechnician_avg_rating(String technician_avg_rating) {
        this.technician_avg_rating = technician_avg_rating;
    }

    public String getTechnician_scheduled_job_count() {
        return technician_scheduled_job_count;
    }

    public void setTechnician_scheduled_job_count(String technician_scheduled_job_count) {
        this.technician_scheduled_job_count = technician_scheduled_job_count;
    }

    public String getTechnician_completed_job_count() {
        return technician_completed_job_count;
    }

    public void setTechnician_completed_job_count(String technician_completed_job_count) {
        this.technician_completed_job_count = technician_completed_job_count;
    }

    public String getCost_details_repair_type() {
        return cost_details_repair_type;
    }

    public void setCost_details_repair_type(String cost_details_repair_type) {
        this.cost_details_repair_type = cost_details_repair_type;
    }

    public String getCost_details_repair_value() {
        return cost_details_repair_value;
    }

    public void setCost_details_repair_value(String cost_details_repair_value) {
        this.cost_details_repair_value = cost_details_repair_value;
    }

    public String getJob_repair_job_types_name() {
        return job_repair_job_types_name;
    }

    public void setJob_repair_job_types_name(String job_repair_job_types_name) {
        this.job_repair_job_types_name = job_repair_job_types_name;
    }

    public String getJob_repair_job_types_cost() {
        return job_repair_job_types_cost;
    }

    public void setJob_repair_job_types_cost(String job_repair_job_types_cost) {
        this.job_repair_job_types_cost = job_repair_job_types_cost;
    }

    //    Job Details
    String job_repair_job_types_name  = "";
    String job_repair_job_types_cost  = "";

    public String getCost_details_fixd_fee() {
        return cost_details_fixd_fee;
    }

    public void setCost_details_fixd_fee(String cost_details_fixd_fee) {
        this.cost_details_fixd_fee = cost_details_fixd_fee;
    }

    public String getCost_details_fixd_fee_percentage() {
        return cost_details_fixd_fee_percentage;
    }

    public void setCost_details_fixd_fee_percentage(String cost_details_fixd_fee_percentage) {
        this.cost_details_fixd_fee_percentage = cost_details_fixd_fee_percentage;
    }

    public String getCost_details_pro_earned() {
        return cost_details_pro_earned;
    }

    public void setCost_details_pro_earned(String cost_details_pro_earned) {
        this.cost_details_pro_earned = cost_details_pro_earned;
    }

    public String getCost_details_customer_payment() {
        return cost_details_customer_payment;
    }

    public void setCost_details_customer_payment(String cost_details_customer_payment) {
        this.cost_details_customer_payment = cost_details_customer_payment;
    }

    public String getCost_details_tripcharges() {
        return cost_details_tripcharges;
    }

    public void setCost_details_tripcharges(String cost_details_tripcharges) {
        this.cost_details_tripcharges = cost_details_tripcharges;
    }

    public String getCost_details_tax() {
        return cost_details_tax;
    }

    public void setCost_details_tax(String cost_details_tax) {
        this.cost_details_tax = cost_details_tax;
    }

    public String getCost_details_installation() {
        return cost_details_installation;
    }

    public void setCost_details_installation(String cost_details_installation) {
        this.cost_details_installation = cost_details_installation;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_notes() {
        return customer_notes;
    }

    public void setCustomer_notes(String customer_notes) {
        this.customer_notes = customer_notes;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(String finished_at) {
        this.finished_at = finished_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocked_by() {
        return locked_by;
    }

    public void setLocked_by(String locked_by) {
        this.locked_by = locked_by;
    }

    public String getLocked_on() {
        return locked_on;
    }

    public void setLocked_on(String locked_on) {
        this.locked_on = locked_on;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(String technician_id) {
        this.technician_id = technician_id;
    }

    public String getTime_slot_id() {
        return time_slot_id;
    }

    public void setTime_slot_id(String time_slot_id) {
        this.time_slot_id = time_slot_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public ArrayList<JobAppliancesModal> getJob_appliances_arrlist() {
        return job_appliances_arrlist;
    }

    public void setJob_appliances_arrlist(ArrayList<JobAppliancesModal> job_appliances_arrlist) {
        this.job_appliances_arrlist = job_appliances_arrlist;
    }

    // time slots gettter setter


    public String getTimeslot_id() {
        return timeslot_id;
    }

    public void setTimeslot_id(String timeslot_id) {
        this.timeslot_id = timeslot_id;
    }

    public String getTimeslot_start() {
        return timeslot_start;
    }

    public void setTimeslot_start(String timeslot_start) {
        this.timeslot_start = timeslot_start;
    }

    public String getTimeslot_end() {
        return timeslot_end;
    }

    public void setTimeslot_end(String timeslot_end) {
        this.timeslot_end = timeslot_end;
    }

    public String getTimeslot_soft_deleted() {
        return timeslot_soft_deleted;
    }

    public void setTimeslot_soft_deleted(String timeslot_soft_deleted) {
        this.timeslot_soft_deleted = timeslot_soft_deleted;
    }

    // job customer addresses getter setter


    public String getJob_customer_addresses_id() {
        return job_customer_addresses_id;
    }

    public void setJob_customer_addresses_id(String job_customer_addresses_id) {
        this.job_customer_addresses_id = job_customer_addresses_id;
    }

    public String getJob_customer_addresses_zip() {
        return job_customer_addresses_zip;
    }

    public void setJob_customer_addresses_zip(String job_customer_addresses_zip) {
        this.job_customer_addresses_zip = job_customer_addresses_zip;
    }

    public String getJob_customer_addresses_city() {
        return job_customer_addresses_city;
    }

    public void setJob_customer_addresses_city(String job_customer_addresses_city) {
        this.job_customer_addresses_city = job_customer_addresses_city;
    }

    public String getJob_customer_addresses_state() {
        return job_customer_addresses_state;
    }

    public void setJob_customer_addresses_state(String job_customer_addresses_state) {
        this.job_customer_addresses_state = job_customer_addresses_state;
    }

    public String getJob_customer_addresses_address() {
        return job_customer_addresses_address;
    }

    public void setJob_customer_addresses_address(String job_customer_addresses_address) {
        this.job_customer_addresses_address = job_customer_addresses_address;
    }

    public String getJob_customer_addresses_address_2() {
        return job_customer_addresses_address_2;
    }

    public void setJob_customer_addresses_address_2(String job_customer_addresses_address_2) {
        this.job_customer_addresses_address_2 = job_customer_addresses_address_2;
    }

    public String getJob_customer_addresses_updated_at() {
        return job_customer_addresses_updated_at;
    }

    public void setJob_customer_addresses_updated_at(String job_customer_addresses_updated_at) {
        this.job_customer_addresses_updated_at = job_customer_addresses_updated_at;
    }

    public String getJob_customer_addresses_created_at() {
        return job_customer_addresses_created_at;
    }

    public void setJob_customer_addresses_created_at(String job_customer_addresses_created_at) {
        this.job_customer_addresses_created_at = job_customer_addresses_created_at;
    }

    public String getJob_customer_addresses_job_id() {
        return job_customer_addresses_job_id;
    }

    public void setJob_customer_addresses_job_id(String job_customer_addresses_job_id) {
        this.job_customer_addresses_job_id = job_customer_addresses_job_id;
    }
}

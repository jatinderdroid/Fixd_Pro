package fixtpro.com.fixtpro.beans;

import java.io.Serializable;

/**
 * Created by sahil on 04-03-2016.
 */
public class RatingListModal implements Serializable {

  String job_id = "";
  String customer_id = "";
  String ratings = "0";
  String created_at = "(null)";
  String comments = "(null)";
  String customers_first_name ="(null)";
  String customers_last_name = "(null)";

    String jobs_contact_name = "(null)";
    String jobs_started_at = "";
    String jobs_finished_at = "";
    String Jobs_id = "";
    String Jobs_technilcian_id = "";
    String Jobs_request_date = "";

    public String getJobs_request_date() {
        return Jobs_request_date;
    }

    public void setJobs_request_date(String jobs_request_date) {
        Jobs_request_date = jobs_request_date;
    }

    String Jobs_technilcians_first_name = "(null)";
    String Jobs_technilcians_last_name = "(null)";
    String Jobs_technilcians_avg_rating = "0.00";

    String jobs_job_customers_addresses_id = "";
    String jobs_job_customers_addresses_zip = "";
    String jobs_job_customers_addresses_city = "";
    String jobs_job_customers_addresses_state = "";
    String jobs_job_customers_addresses_address = "";


    public String getJobs_contact_name() {
        return jobs_contact_name;
    }

    public void setJobs_contact_name(String jobs_contact_name) {
        this.jobs_contact_name = jobs_contact_name;
    }

    public String getJobs_started_at() {
        return jobs_started_at;
    }

    public void setJobs_started_at(String jobs_started_at) {
        this.jobs_started_at = jobs_started_at;
    }

    public String getJobs_finished_at() {
        return jobs_finished_at;
    }

    public void setJobs_finished_at(String jobs_finished_at) {
        this.jobs_finished_at = jobs_finished_at;
    }

    public String getJobs_id() {
        return Jobs_id;
    }

    public void setJobs_id(String jobs_id) {
        Jobs_id = jobs_id;
    }

    public String getJobs_technilcian_id() {
        return Jobs_technilcian_id;
    }

    public void setJobs_technilcian_id(String jobs_technilcian_id) {
        Jobs_technilcian_id = jobs_technilcian_id;
    }

    public String getJobs_technilcians_first_name() {
        return Jobs_technilcians_first_name;
    }

    public void setJobs_technilcians_first_name(String jobs_technilcians_first_name) {
        Jobs_technilcians_first_name = jobs_technilcians_first_name;
    }

    public String getJobs_technilcians_last_name() {
        return Jobs_technilcians_last_name;
    }

    public void setJobs_technilcians_last_name(String jobs_technilcians_last_name) {
        Jobs_technilcians_last_name = jobs_technilcians_last_name;
    }

    public String getJobs_technilcians_avg_rating() {
        return Jobs_technilcians_avg_rating;
    }

    public void setJobs_technilcians_avg_rating(String jobs_technilcians_avg_rating) {
        Jobs_technilcians_avg_rating = jobs_technilcians_avg_rating;
    }

    public String getJobs_job_customers_addresses_id() {
        return jobs_job_customers_addresses_id;
    }

    public void setJobs_job_customers_addresses_id(String jobs_job_customers_addresses_id) {
        this.jobs_job_customers_addresses_id = jobs_job_customers_addresses_id;
    }

    public String getJobs_job_customers_addresses_zip() {
        return jobs_job_customers_addresses_zip;
    }

    public void setJobs_job_customers_addresses_zip(String jobs_job_customers_addresses_zip) {
        this.jobs_job_customers_addresses_zip = jobs_job_customers_addresses_zip;
    }

    public String getJobs_job_customers_addresses_city() {
        return jobs_job_customers_addresses_city;
    }

    public void setJobs_job_customers_addresses_city(String jobs_job_customers_addresses_city) {
        this.jobs_job_customers_addresses_city = jobs_job_customers_addresses_city;
    }

    public String getJobs_job_customers_addresses_state() {
        return jobs_job_customers_addresses_state;
    }

    public void setJobs_job_customers_addresses_state(String jobs_job_customers_addresses_state) {
        this.jobs_job_customers_addresses_state = jobs_job_customers_addresses_state;
    }

    public String getJobs_job_customers_addresses_address() {
        return jobs_job_customers_addresses_address;
    }

    public void setJobs_job_customers_addresses_address(String jobs_job_customers_addresses_address) {
        this.jobs_job_customers_addresses_address = jobs_job_customers_addresses_address;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomers_first_name() {
        return customers_first_name;
    }

    public void setCustomers_first_name(String customers_first_name) {
        this.customers_first_name = customers_first_name;
    }

    public String getCustomers_last_name() {
        return customers_last_name;
    }

    public void setCustomers_last_name(String customers_last_name) {
        this.customers_last_name = customers_last_name;
    }
}

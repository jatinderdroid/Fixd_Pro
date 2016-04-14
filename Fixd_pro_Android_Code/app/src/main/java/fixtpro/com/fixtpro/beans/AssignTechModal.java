package fixtpro.com.fixtpro.beans;

import java.io.Serializable;

/**
 * Created by sahil on 05-03-2016.
 */
public class AssignTechModal implements Serializable {
    String Image = "";
    String FirstName = "";
    String LasttName = "";
    String Rating = "";
    String JobSchedule = "";
    String TechId = "";

    public String getTechId() {
        return TechId;
    }

    public void setTechId(String techId) {
        TechId = techId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLasttName() {
        return LasttName;
    }

    public void setLasttName(String lasttName) {
        LasttName = lasttName;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getJobSchedule() {
        return JobSchedule;
    }

    public void setJobSchedule(String jobSchedule) {
        JobSchedule = jobSchedule;
    }
}

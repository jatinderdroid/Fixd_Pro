package fixtpro.com.fixtpro.beans;

import java.io.Serializable;

/**
 * Created by sony on 09-02-2016.
 */
public class SkillTrade implements Serializable {
    int id = 0;
    String title = "";
    boolean isChecked = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}

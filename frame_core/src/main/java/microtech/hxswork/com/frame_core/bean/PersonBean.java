package microtech.hxswork.com.frame_core.bean;

/**
 * Created by microtech on 2017/12/20.
 */

public class PersonBean {
    String gov_id="";
    String dep_id="";
    String dep_name="";
    String dep_title="";
    String dept_name="";
    String desc="";

    public PersonBean(String gov_id, String dep_id, String dep_name, String dep_title, String dept_name, String desc) {
        this.gov_id = gov_id;
        this.dep_id = dep_id;
        this.dep_name = dep_name;
        this.dep_title = dep_title;
        this.dept_name = dept_name;
        this.desc = desc;
    }

    public String getGov_id() {
        return gov_id;
    }

    public void setGov_id(String gov_id) {
        this.gov_id = gov_id;
    }

    public String getDep_id() {
        return dep_id;
    }

    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getDep_title() {
        return dep_title;
    }

    public void setDep_title(String dep_title) {
        this.dep_title = dep_title;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

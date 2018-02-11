package microtech.hxswork.com.frame_core.bean;

/**
 * Created by microtech on 2017/12/18.
 */

public class FristBean {

    String region_id;

    String gov_id;
    String versionname;

    String size;
    String upgradeinfo;
    String updateurl;


    public FristBean(String region_id, String gov_id, String versionname, String size, String upgradeinfo, String updateurl) {
        this.region_id = region_id;
        this.gov_id = gov_id;
        this.versionname = versionname;
        this.size = size;
        this.upgradeinfo = upgradeinfo;
        this.updateurl = updateurl;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUpgradeinfo() {
        return upgradeinfo;
    }

    public void setUpgradeinfo(String upgradeinfo) {
        this.upgradeinfo = upgradeinfo;
    }
    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getGov_id() {
        return gov_id;
    }

    public void setGov_id(String gov_id) {
        this.gov_id = gov_id;
    }
}

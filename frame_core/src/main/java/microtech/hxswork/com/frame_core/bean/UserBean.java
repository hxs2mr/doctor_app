package microtech.hxswork.com.frame_core.bean;

import java.util.List;

import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/12/18.
 */

public class UserBean {
    private String id  = null;
    private String name = null;
    private String avatar = null;
    private String gender = null;
    private String address = "";
    private String birthday = null;
    private String phone = null;

    private String accid = null;
    private String token = null;

    private String id_card=  null;
    private int sign_number = 0;//签约数
    private int visits = 0;//随访数
    private String job_number = null;//工号
    private String peoples = null;//民族


    private String qr_code = null;
    private List<MultipleItemEntity> team_list = null;

    private String team_doctor_name = null;
    private  String team_name = null;
    private  String times = null;

    public UserBean(String id, String name, String avatar, String gender,
                    String address, String birthday, String phone,
                    String accid, String token, String id_card,
                    int sign_number, int visits, String job_number, String peoples, String qr_code,
                    List<MultipleItemEntity> team_list, String team_doctor_name,
                    String team_name,String times) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.gender = gender;
        this.address = address;
        this.birthday = birthday;
        this.phone = phone;
        this.accid = accid;
        this.token = token;
        this.id_card = id_card;
        this.sign_number = sign_number;
        this.visits = visits;
        this.job_number = job_number;
        this.peoples = peoples;
        this.qr_code = qr_code;
        this.team_list = team_list;
        this.team_doctor_name = team_doctor_name;
        this.team_name = team_name;
        this.times = times;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public int getSign_number() {
        return sign_number;
    }

    public void setSign_number(int sign_number) {
        this.sign_number = sign_number;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public List<MultipleItemEntity> getTeam_list() {
        return team_list;
    }

    public void setTeam_list(List<MultipleItemEntity> team_list) {
        this.team_list = team_list;
    }

    public String getTeam_doctor_name() {
        return team_doctor_name;
    }

    public void setTeam_doctor_name(String team_doctor_name) {
        this.team_doctor_name = team_doctor_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }
}

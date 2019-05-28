package telemarketing.model;

import java.util.Date;

public class Phone_record {
    private Integer id;

    private String userPhone;

    private String userName;

    private Date recordBegtime;

    private Date recordEndtime;

    private String soundUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Date getRecordBegtime() {
        return recordBegtime;
    }

    public void setRecordBegtime(Date recordBegtime) {
        this.recordBegtime = recordBegtime;
    }

    public Date getRecordEndtime() {
        return recordEndtime;
    }

    public void setRecordEndtime(Date recordEndtime) {
        this.recordEndtime = recordEndtime;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl == null ? null : soundUrl.trim();
    }
}
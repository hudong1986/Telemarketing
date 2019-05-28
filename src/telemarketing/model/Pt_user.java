package telemarketing.model;

import java.util.Date;

public class Pt_user {
    private Integer id;

    private String phone;

    private String userPwd;

    private Pt_role roleCode;

    private Pt_dept deptId;

    private String realName;

    private String picUrl;

    private Boolean sex;

    private Date addTime;

    private Date loginTime;

    private Byte state;

    private String upDownId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd == null ? null : userPwd.trim();
    }

    public Pt_role getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(Pt_role roleCode) {
        this.roleCode = roleCode;
    }

    public Pt_dept getDeptId() {
        return deptId;
    }

    public void setDeptId(Pt_dept deptId) {
        this.deptId = deptId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getUpDownId() {
        return upDownId;
    }

    public void setUpDownId(String upDownId) {
        this.upDownId = upDownId == null ? null : upDownId.trim();
    }
}
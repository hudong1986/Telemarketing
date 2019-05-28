package telemarketing.model;

import java.util.Date;

public class SoundRecord {
    private Integer id;

    private String fileName;

    private String customerPhone;

    private String localPhone;

    private String userId;

    private String userName;

    private String upDownId;

    private Date soundTime;

    private Integer soundLength;

    private Date addTime;
    
    private String direction;
    
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone == null ? null : customerPhone.trim();
    }

    public String getLocalPhone() {
        return localPhone;
    }

    public void setLocalPhone(String localPhone) {
        this.localPhone = localPhone == null ? null : localPhone.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUpDownId() {
        return upDownId;
    }

    public void setUpDownId(String upDownId) {
        this.upDownId = upDownId == null ? null : upDownId.trim();
    }

    public Date getSoundTime() {
        return soundTime;
    }

    public void setSoundTime(Date soundTime) {
        this.soundTime = soundTime;
    }

    public Integer getSoundLength() {
        return soundLength;
    }

    public void setSoundLength(Integer soundLength) {
        this.soundLength = soundLength;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
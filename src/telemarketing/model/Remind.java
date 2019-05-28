package telemarketing.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Remind {
    private Integer id;

    private Date timeBeg;

    private Date timeEnd;

    private String topic;

    private String context;

    private String userName;

    private String userId;

    private String upDownId;

    private Byte remindType;

    private Byte state;
    
    private Date addTime;
    
    private String cus_phone;
    private String cus_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimeBeg() {
        return timeBeg;
    }

    public void setTimeBeg(Date timeBeg) {
        this.timeBeg = timeBeg;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUpDownId() {
        return upDownId;
    }

    public void setUpDownId(String upDownId) {
        this.upDownId = upDownId == null ? null : upDownId.trim();
    }

    public Byte getRemindType() {
        return remindType;
    }

    public void setRemindType(Byte remindType) {
        this.remindType = remindType;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getCus_phone() {
		return cus_phone;
	}

	public void setCus_phone(String cus_phone) {
		this.cus_phone = cus_phone;
	}

	public String getCus_name() {
		return cus_name;
	}

	public void setCus_name(String cus_name) {
		this.cus_name = cus_name;
	}
}
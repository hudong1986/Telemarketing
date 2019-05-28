package telemarketing.model;

import java.util.Date;

public class Customer {
	private Integer id;

	private String phone = "";

	private String cusName = "";

	private String idPerson = "";

	private String address = "";

	private String whoUse = "";

	private String whoUseName = "";

	private Date whoGetTime;

	private String shareUse = "";

	private String shareUseName = "";

	private Date shareGetTime;

	private Date addTime;

	private Date update_state_time;

	private String remark = "";

	// 0:未联系 1无意向 3:空号 6意向中 9上门 12已签单 15已放款 18已收服务费 21退单
	private Byte state;

	private String dataFrom = "";

	private String who_up_down_id = "";

	private String business_name = "";
	
	private Byte is_common;
	private Date in_common_time;
	private String who_put_common;
	private String who_put_common_name;
	private String recovery_from;
	private String stateName="未知";

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

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName == null ? null : cusName.trim();
	}

	public String getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(String idPerson) {
		this.idPerson = idPerson == null ? null : idPerson.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getWhoUse() {
		return whoUse;
	}

	public void setWhoUse(String whoUse) {
		this.whoUse = whoUse == null ? null : whoUse.trim();
	}

	public String getWhoUseName() {
		return whoUseName;
	}

	public void setWhoUseName(String whoUseName) {
		this.whoUseName = whoUseName == null ? null : whoUseName.trim();
	}

	public Date getWhoGetTime() {
		return whoGetTime;
	}

	public void setWhoGetTime(Date whoGetTime) {
		this.whoGetTime = whoGetTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String from) {
		this.dataFrom = from;
	}

	public String getShareUse() {
		return shareUse;
	}

	public void setShareUse(String shareUse) {
		this.shareUse = shareUse;
	}

	public String getShareUseName() {
		return shareUseName;
	}

	public void setShareUseName(String shareUseName) {
		this.shareUseName = shareUseName;
	}

	public Date getShareGetTime() {
		return shareGetTime;
	}

	public void setShareGetTime(Date shareGetTime) {
		this.shareGetTime = shareGetTime;
	}

	

	public String getWho_up_down_id() {
		return who_up_down_id;
	}

	public void setWho_up_down_id(String who_up_down_id) {
		this.who_up_down_id = who_up_down_id;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public Date getUpdate_state_time() {
		return update_state_time;
	}

	public void setUpdate_state_time(Date update_state_time) {
		this.update_state_time = update_state_time;
	}

	public Byte getIs_common() {
		return is_common;
	}

	public void setIs_common(Byte is_common) {
		this.is_common = is_common;
	}

	public Date getIn_common_time() {
		return in_common_time;
	}

	public void setIn_common_time(Date in_common_time) {
		this.in_common_time = in_common_time;
	}

	public String getWho_put_common() {
		return who_put_common;
	}

	public void setWho_put_common(String who_put_common) {
		this.who_put_common = who_put_common;
	}

	public String getWho_put_common_name() {
		return who_put_common_name;
	}

	public void setWho_put_common_name(String who_put_common_name) {
		this.who_put_common_name = who_put_common_name;
	}

	public String getRecovery_from() {
		return recovery_from;
	}

	public void setRecovery_from(String recovery_from) {
		this.recovery_from = recovery_from;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
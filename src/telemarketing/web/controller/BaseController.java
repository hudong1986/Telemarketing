package telemarketing.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import telemarketing.model.Customer;
import telemarketing.model.Pt_user;
import telemarketing.model.TrackRecord;
import telemarketing.repository.Business_typeMapper;
import telemarketing.repository.CustomerMapper;
import telemarketing.repository.CustomerStateMapper;
import telemarketing.repository.LoginRecordMapper;
import telemarketing.repository.Pt_deptMapper;
import telemarketing.repository.Pt_roleMapper;
import telemarketing.repository.Pt_userMapper;
import telemarketing.repository.RemindMapper;
import telemarketing.repository.RemindStateMapper;
import telemarketing.repository.ReportMapper;
import telemarketing.repository.SoundRecordMapper;
import telemarketing.repository.TrackRecordMapper;
import telemarketing.service.CustomerService;
import telemarketing.service.PagerServiceAdapter;
import telemarketing.service.StaticService;
import telemarketing.service.SystemProperty;
import telemarketing.service.UploadZipService;
import telemarketing.util.StringHelper;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;

public class BaseController {

	@Autowired
	PagerServiceAdapter pagerServer;

	@Autowired
	CustomerService customerService;

	@Autowired
	Pt_userMapper ptUserMapper;

	@Autowired
	CustomerMapper customerMapper;

	@Autowired
	Business_typeMapper busineMapper;

	@Autowired
	TrackRecordMapper trackMapper;

	@Autowired
	SoundRecordMapper soundMapper;

	@Autowired
	SystemProperty systemProperty;

	@Autowired
	ReportMapper reportMapper;

	@Autowired
	TrackRecordMapper trackRecordMapper;

	@Autowired
	Pt_deptMapper deptMapper;

	@Autowired
	Pt_roleMapper roleMapper;

	@Autowired
	RemindMapper remindMapper;

	@Autowired
	RemindStateMapper remindStateMapper;

	@Autowired
	CustomerStateMapper customerStateMapper;

	@Autowired
	LoginRecordMapper loginRecordMapper;

	@Autowired
	UploadZipService uploadZipService;

	@Autowired
	StaticService staticService;

	public void addTrackRecord(String opUserPhone, int customer_id, String content) {

		Customer customer = customerMapper.selectByPrimaryKey(customer_id);
		content = content + " ¿Í»§:" + customer.getPhone() + customer.getCusName();
		TxtLogger.log(content, LogTye.INFO, LogFileCreateType.OneFileEveryDay, "Operator/" + opUserPhone);

		if (content.contains("¹²Ïí")) {
			Pt_user user = ptUserMapper.selectByPhone(opUserPhone);
			TrackRecord record = new TrackRecord();
			record.setAddTime(new Date());
			record.setContent(StringHelper.turn(content));
			record.setCustomerId(customer_id);
			record.setUserId(user.getPhone());
			record.setUserName(user.getRealName());
			trackRecordMapper.insert(record);
		}

	}
}

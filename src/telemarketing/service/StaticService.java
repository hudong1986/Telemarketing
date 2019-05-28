package telemarketing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telemarketing.model.AllowIp;
import telemarketing.model.Business_type;
import telemarketing.model.CustomerState;
import telemarketing.model.Pt_dept;
import telemarketing.model.Pt_role;
import telemarketing.repository.AllowIpMapper;
import telemarketing.repository.Business_typeMapper;
import telemarketing.repository.CustomerStateMapper;
import telemarketing.repository.Pt_deptMapper;
import telemarketing.repository.Pt_roleMapper;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;

@Service
public class StaticService {

	@Autowired
	Pt_deptMapper deptMapper;

	@Autowired
	Pt_roleMapper roleMapper;

	@Autowired
	CustomerStateMapper customerStateMapper;

	@Autowired
	Business_typeMapper business_typeMapper;

	@Autowired
	AllowIpMapper allowIpMapper;

	static List<Pt_dept> Deptlist;

	public static List<Pt_dept> getDeptlist() {
		return Deptlist;
	}

	public static List<Pt_dept> getDeptlist(String upDownId) {
		List<Pt_dept> list = Deptlist.stream().filter(x -> x.getUpDownId().contains(upDownId))
				.collect(Collectors.toList());
		return list;
	}

	public static List<Pt_role> getRoleLst() {
		return RoleLst;
	}

	public static List<CustomerState> getCustomerStates() {
		return CustomerStates;
	}

	public static List<Business_type> getBusiness_types() {
		return Business_types;
	}

	static List<Pt_role> RoleLst;
	// 加载所有客户状态
	static List<CustomerState> CustomerStates;
	// 加载所有业务名
	static List<Business_type> Business_types;

	// 所有允许访问的IP
	List<AllowIp> AllowIps = new ArrayList<>();

	public List<AllowIp> getAllowIps() {
		return AllowIps;
	}

	@PostConstruct
	private void init() {
		try {
			Deptlist = deptMapper.selectALL();
			RoleLst = roleMapper.selectAll();
			CustomerStates = customerStateMapper.selectAll();
			Business_types = business_typeMapper.selectAll();
			AllowIps = allowIpMapper.selectAll();

		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
		}
	}

	public void delAllowIP(int id) {
		allowIpMapper.deleteByPrimaryKey(id);
		AllowIps = allowIpMapper.selectAll();
	}

	public void updateAllowIP(AllowIp allowIp) {
		allowIpMapper.updateByPrimaryKey(allowIp);
		AllowIps = allowIpMapper.selectAll();
	}

	public void addAllowIP(AllowIp allowIp) {
		allowIpMapper.insert(allowIp);
		AllowIps = allowIpMapper.selectAll();
	}

	public boolean isAllowIp(String ip) {
		boolean isAllow = false;
		if (AllowIps != null) {
			for (AllowIp allowIp : AllowIps) {
				if (allowIp.getIp().equals(ip)) {
					isAllow = true;
					break;
				}
			}
		}

		return isAllow;
	}

	// 加入一个自动添加IP的功能
	public void addAutoAllowIP(String user_id, String ip) {
		String location = "auto_" + user_id;
		boolean isExist = false; // 是否存在
		for (AllowIp allowIp : AllowIps) {
			if (allowIp.getLocation().equals(location)) {

				isExist = true;
				if (allowIp.getIp().equals(ip)) {
					return; // 已存在不考虑
				} else {
					// 更新一下IP
					allowIp.setIp(ip);
					allowIpMapper.updateByPrimaryKey(allowIp);
					return;
				}

			}
		}

		if (isExist == false) {
			AllowIp newIp = new AllowIp();
			newIp.setIp(ip);
			newIp.setLocation(location);
			allowIpMapper.insert(newIp);
			AllowIps = allowIpMapper.selectAll();
		}

	}

	//获取指定客户状态名
	public static String getCustomStateName(int state){
		String stateName="未知";
		for (CustomerState item : CustomerStates) {
			if(item.getId()==state){
				stateName = item.getName();
				break;
			}
		}
		
		return stateName;
	}
}

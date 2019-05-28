package telemarketing.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.management.relation.RoleInfoNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import telemarketing.model.AjaxReturn;
import telemarketing.model.Pager;
import telemarketing.model.Pt_dept;
import telemarketing.model.Pt_role;
import telemarketing.model.Pt_user;
import telemarketing.repository.Pt_deptMapper;
import telemarketing.repository.Pt_roleMapper;
import telemarketing.repository.Pt_userMapper;
import telemarketing.service.PagerServiceAdapter;
import telemarketing.service.SystemProperty;
import telemarketing.util.SecretHelper;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	// 获取当前用户的下属
	@RequestMapping(value = "/subordinate", method = RequestMethod.GET)
	public @ResponseBody List<Pt_user> subordinate(@AuthenticationPrincipal User user1) {
		String up_down_id = "";
		String role = "CustomerService";
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		up_down_id = user.getDeptId().getUpDownId();
		if (user.getRoleCode().getRoleCode().equals("CSO")) {
			role = "TeamLeader";
		} else {
			role = "CustomerService";
		}

		List<Pt_user> list = ptUserMapper.selectByDeptAndRole(up_down_id, role);
		return list;
	}

	// 获取权证所有员工
	@RequestMapping(value = "/backline", method = RequestMethod.GET)
	public @ResponseBody List<Pt_user> backline(@AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<Pt_user> list1 = ptUserMapper.selectByDeptId(1010);
		List<Pt_user> list2 = ptUserMapper.selectByDeptId(user.getDeptId().getId());
		list1.addAll(list2);
		if (user.getRoleCode().getRoleCode().equals("CSO") || user.getRoleCode().getRoleCode().equals("TeamLeader")
				|| user.getRoleCode().getRoleCode().equals("BackLineLeader")) {
			List<Pt_user> list3 = ptUserMapper.selectByDeptId(1001);
			List<Pt_user> list4 = ptUserMapper.selectByDeptId(1000);
			if (list3 != null && list3.size() > 0) {
				list1.addAll(list3);
			}
			if (list4 != null && list4.size() > 0) {
				list1.addAll(list4);
			}

		}

		return list1;
	}

	//
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getAllList(Model model, @AuthenticationPrincipal User user1) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchEmployee("", "", -1, 0,
				user.getRoleCode().getRoleCode().equals("ADMIN") ? "" : user.getUpDownId(), "id", "asc", 1);
		model.addAttribute("pager", pager);
		model.addAttribute("deptId", -1);
		model.addAttribute("state", 0);
		return null;
	}

	//
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String searchAll(Model model,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") int deptId,
			@RequestParam(value = "state", required = false, defaultValue = "0") int state,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum, @AuthenticationPrincipal User user1

	) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchEmployee(phone, user_name, deptId, state,
				user.getRoleCode().getRoleCode().equals("ADMIN") ? "" : user.getUpDownId(), orderField, orderType,
				pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("deptId", deptId);
		model.addAttribute("state", state);
		return null;
	}

	// 分页功能
	@RequestMapping(value = "/pager", method = RequestMethod.GET)
	public String pager(Model model, @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") int deptId,
			@RequestParam(value = "state", required = false, defaultValue = "0") int state,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum, @AuthenticationPrincipal User user1)
			throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchEmployee(phone, user_name, deptId, state,
				user.getRoleCode().getRoleCode().equals("ADMIN") ? "" : user.getUpDownId(), orderField, orderType,
				pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("deptId", deptId);
		model.addAttribute("state", state);
		String retStr = "user/list";

		return retStr;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(@RequestParam String phone, @RequestParam String real_name,
			@RequestParam String role_code, @RequestParam int deptId, @RequestParam String pwd, @RequestParam int sex,
			@RequestParam(required = false) MultipartFile file, HttpServletRequest request) {

		AjaxReturn ret = new AjaxReturn();

		try {

			Pt_user old = ptUserMapper.selectByPhone(phone);
			if (old != null) {
				ret.setRetMsg("添加失败，该员工已存在！");
				return ret;
			}
			Pt_dept dept = deptMapper.selectByPrimaryKey(deptId);
			Pt_role role = roleMapper.selectByPrimaryKey(role_code);
			Pt_user user = new Pt_user();
			user.setAddTime(new Date());
			user.setDeptId(dept);
			user.setState((byte) 0);
			user.setPhone(phone);
			user.setRealName(real_name);
			user.setRoleCode(role);
			user.setSex(sex == 1 ? true : false);
			user.setUserPwd(SecretHelper.parseStrToMd5U32(pwd));
			user.setUpDownId(dept.getUpDownId());
			String saveFileName = "default_header.png";
			if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
				String uid = UUID.randomUUID().toString();
				saveFileName = uid + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				String savepath = systemProperty.getHeader_pic_path() + "/" + saveFileName;
				file.transferTo(new File(savepath));
			}

			user.setPicUrl(saveFileName);
			ptUserMapper.insert(user);

			ret.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg(ex.getMessage());
		}

		return ret;
	}

	@RequestMapping(value = "/leaveMore", method = RequestMethod.POST)
	public @ResponseBody int leaveMore(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		int id = -1;
		for (String string : temp) {
			id = Integer.parseInt(string);
			if (user.getId() == id) {
				continue;
			}

			list.add(Integer.parseInt(string));
		}

		ptUserMapper.updateLeaveMore(list);
		return 1;
	}
	
	
	@RequestMapping(value = "/OnWorkMore", method = RequestMethod.POST)
	public @ResponseBody int onWorkMore(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		int id = -1;
		for (String string : temp) {
			id = Integer.parseInt(string);
			if (user.getId() == id) {
				continue;
			}

			list.add(Integer.parseInt(string));
		}

		ptUserMapper.updateOnWorkMore(list);
		return 1;
	}
	

	@RequestMapping(value = "/resetPwdMore", method = RequestMethod.POST)
	public @ResponseBody int resetPwdMore(@RequestParam String ids) {
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		for (String string : temp) {
			list.add(Integer.parseInt(string));
		}

		ptUserMapper.updateRestPwdMore(list, SecretHelper.parseStrToMd5U32("123456"));
		return 1;
	}

	@RequestMapping(value = "/modifyPwd", method = RequestMethod.GET)
	public String addAjax() {
		return "user/modifyPwd";
	}

	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	public String modifyPwd(@RequestParam String old_pwd, @RequestParam String add_pwd1,
			@AuthenticationPrincipal User user1, Model model, @RequestParam(required = false) MultipartFile file,
			HttpServletRequest request) throws IllegalStateException, IOException {
		Pt_user currentUser = ptUserMapper.selectByPhone(user1.getUsername());
		if (SecretHelper.parseStrToMd5U32(old_pwd).equals(currentUser.getUserPwd())) {
			currentUser.setUserPwd(SecretHelper.parseStrToMd5U32(add_pwd1));
			String saveFileName = "default_header.png";
			if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
				String uid = UUID.randomUUID().toString();
				saveFileName = uid + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				String savepath = systemProperty.getHeader_pic_path() + "/" + saveFileName;
				file.transferTo(new File(savepath));
				currentUser.setPicUrl(saveFileName);
			}

			ptUserMapper.updateByPrimaryKey(currentUser);
			model.addAttribute("msg", "修改成功，请退出后重新登录");
		} else {
			model.addAttribute("msg", "修改失败，原密码不对！");
		}

		return null;
	}

	@RequestMapping(value = "/resetDeptRole", method = RequestMethod.POST)
	public @ResponseBody int resetDeptRole(@RequestParam String ids, @RequestParam String deptId,
			@RequestParam String roleId, @AuthenticationPrincipal User user1, Model model, HttpServletRequest request) {

		String[] temp = ids.split(",");
		for (String string : temp) {
			Pt_user user = ptUserMapper.selectByPrimaryKey(Integer.parseInt(string));
			Pt_dept dept = deptMapper.selectByPrimaryKey(Integer.parseInt(deptId));
			Pt_role role = roleMapper.selectByPrimaryKey(roleId);
			user.setDeptId(dept);
			user.setRoleCode(role);
			user.setUpDownId(dept.getUpDownId());
			ptUserMapper.updateByPrimaryKeySelective(user);
			//将客户所属部门改为目标部门
			customerMapper.updateUpdownId(dept.getUpDownId(), user.getPhone());
		}

		return 1;
	}

	@RequestMapping(value = "/leaveMoreAndBackCustomer", method = RequestMethod.POST)
	public @ResponseBody int leaveMoreAndBackCustomer(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<String> list = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		String[] temp = ids.split(",");
		int id = -1;
		for (String string : temp) {
			id = Integer.parseInt(string);
			if (user.getId() == id) {
				continue;
			}

			list2.add(id);
			list.add(ptUserMapper.selectByPrimaryKey(Integer.parseInt(string)).getPhone());
		}

		ptUserMapper.updateLeaveMore(list2);
		ptUserMapper.leaveMoreAndBackCustomer(list, user.getPhone(), user.getRealName(), new Date(),
				user.getUpDownId());
		return 1;
	}
	
	//回收客户
	@RequestMapping(value = "/backCustomer", method = RequestMethod.POST)
	public @ResponseBody int backCustomer(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<String> list = new ArrayList<>();
		String[] temp = ids.split(",");
		int id = -1;
		for (String string : temp) {
			id = Integer.parseInt(string);
			if (user.getId() == id) {
				continue;
			}

			list.add(ptUserMapper.selectByPrimaryKey(Integer.parseInt(string)).getPhone());
		}

		ptUserMapper.leaveMoreAndBackCustomer(list, user.getPhone(), user.getRealName(), new Date(),
				user.getUpDownId());
		return 1;
	}
	
	@RequestMapping(value = "/stopservice", method = RequestMethod.POST)
	public @ResponseBody int stopservice() {
		systemProperty.setStopService(true);
		return 1;
	}
	
	@RequestMapping(value = "/startservice", method = RequestMethod.POST)
	public @ResponseBody int startservice() {
		systemProperty.setStopService(false);
		return 1;
	}
}

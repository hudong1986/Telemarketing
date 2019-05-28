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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import telemarketing.model.AjaxReturn;
import telemarketing.model.AllowIp;
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
@RequestMapping("/allow_ip")
public class AllowIpController extends BaseController {

	 
	//
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getAllList(Model model, @AuthenticationPrincipal User user1) throws SQLException {
		Pager pager =  new Pager();
		List<AllowIp> allowIps = staticService.getAllowIps();
		if(allowIps!=null){
			pager.setData(allowIps);
			pager.setTotalCount(allowIps.size());
		}
		model.addAttribute("pager", pager);
		return "allow_ip/list";
	}


	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(
			@RequestParam String location, 
			@RequestParam String ip) {

		AjaxReturn ret = new AjaxReturn();

		try {
			AllowIp allowIp = new AllowIp();
			allowIp.setLocation(location.trim());
			allowIp.setIp(ip.trim());
			staticService.addAllowIP(allowIp);
			ret.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg(ex.getMessage());
		}

		return ret;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(
			@PathVariable int id) {

		AjaxReturn ret = new AjaxReturn();

		try {
			staticService.delAllowIP(id);
			ret.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg(ex.getMessage());
		}

		return ret;
	}

	 
}

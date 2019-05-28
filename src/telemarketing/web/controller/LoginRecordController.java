package telemarketing.web.controller;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import telemarketing.model.Pager;
import telemarketing.model.Pt_user;

@Controller
@RequestMapping("/login_record")
public class LoginRecordController extends BaseController {

	//
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String searchAll(Model model,
			@RequestParam(required = false, defaultValue = "") String user_phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "1") int pageNum, 
			@AuthenticationPrincipal User user1

	) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchLoginRecord(user_phone, user_name, up_down_id, beg_time, end_time, "", "", pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("user_phone", user_phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		return null;
	}


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String pager(Model model,
			@RequestParam(required = false, defaultValue = "") String user_phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum, 
			@AuthenticationPrincipal User user1)
			throws SQLException {
		Pager pager = pagerServer.searchLoginRecord(user_phone, user_name, up_down_id, beg_time, end_time, orderField, orderType, pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("user_phone", user_phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		String retStr = "login_record/list";
		return retStr;
	}
}

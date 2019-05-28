package telemarketing.web.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import telemarketing.model.AjaxReturn;
import telemarketing.model.Pager;
import telemarketing.model.Pt_user;
import telemarketing.model.Remind;
import telemarketing.model.RemindState;
import telemarketing.util.DateUtil;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("remind")
public class RemindController extends BaseController {

	@RequestMapping(value = "/getMyRemind", method = RequestMethod.GET)
	public @ResponseBody AjaxReturn getMyRemind(@AuthenticationPrincipal User user1) {
		AjaxReturn ajaxReturn = new AjaxReturn();
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<Remind> list = remindMapper.selectUnRead(user.getPhone(), user.getUpDownId());
		if (list != null && list.size() > 0) {
			ajaxReturn.setCode(1);
			ajaxReturn.setObject1(list);
		}

		return ajaxReturn;
	}

	@RequestMapping(value = "/readRemind", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn readRemind(@AuthenticationPrincipal User user1,
			@RequestParam(value = "remindIds") String remindIds) throws Exception {
		AjaxReturn ajaxReturn = new AjaxReturn();
		try {
			String[] idStrings = remindIds.split(",");
			for (String string : idStrings) {
				RemindState oldState = remindStateMapper.selectByUserIdRemindId(Integer.parseInt(string),
						user1.getUsername());
				if (oldState != null) {
					continue;
				}

				Remind remind = remindMapper.selectByPrimaryKey(Integer.parseInt(string));
				RemindState remindState = new RemindState();
				remindState.setAddTime(new Date());
				remindState.setRemindId(Integer.parseInt(string));
				remindState.setState((byte) 1);
				remindState.setUserId(user1.getUsername());
				remindStateMapper.insert(remindState);
				if (remind.getTopic().contains("¹ÙÍø¿Í»§")) {
					remind.setState((byte) 1);
					remindMapper.updateByPrimaryKey(remind);
				}
			}

			ajaxReturn.setCode(1);
		} catch (Exception ex) {
			ex.printStackTrace();
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
		}

		return ajaxReturn;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(name = "orderField", required = false) String orderField,
			@RequestParam(name = "time_date", required = false) String time_date,
			@RequestParam(name = "orderType", required = false) String orderType,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@AuthenticationPrincipal User user1, Model model) throws SQLException {

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchRemind(user.getPhone(), user.getUpDownId(), time_date, orderField, orderType,
				pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("time_date", time_date);
		return "remind/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String postList(
			@RequestParam(name = "time_date", required = false) String time_date,
			@AuthenticationPrincipal User user1, Model model) throws SQLException {

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchRemind(user.getPhone(), user.getUpDownId(), time_date, "id", "asc",
				1);
		model.addAttribute("pager", pager);
		model.addAttribute("time_date", time_date);
		return "remind/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn add(@RequestParam(name = "topic") String topic,
			@RequestParam(name = "context") String context, @RequestParam(name = "timeBeg") String timeBeg,
			@RequestParam(name = "timeEnd") String timeEnd, @RequestParam(required = false) String cus_phone,
			@RequestParam(required = false) String cus_name,
			@RequestParam(name = "remindType", required = false, defaultValue = "0") int remindType,
			@AuthenticationPrincipal User user1) throws SQLException, ParseException {
		AjaxReturn ajaxReturn = new AjaxReturn();
		try{
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Remind remind = new Remind();
		remind.setAddTime(new Date());
		remind.setContext(context);
		remind.setRemindType((byte) remindType);
		remind.setTopic(topic);
		remind.setState((byte) 0);
		remind.setTimeBeg(DateUtil.parse(timeBeg, "yyyy-MM-dd HH:mm:ss"));
		remind.setTimeEnd(DateUtil.parse(timeEnd, "yyyy-MM-dd HH:mm:ss"));
		remind.setUserId(user.getPhone());
		remind.setUpDownId(user.getUpDownId());
		remind.setUserName(user.getRealName());
		remind.setCus_name(cus_name);
		remind.setCus_phone(cus_phone);
		remindMapper.insert(remind);
		ajaxReturn.setCode(1);
		}
		catch(Exception ex){
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
		}
		return ajaxReturn;
	}

	@RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn add(@PathVariable int id, @AuthenticationPrincipal User user1)
			throws SQLException, ParseException {
		AjaxReturn ajaxReturn = new AjaxReturn();
		try {
			//ÏÈÉ¾³ýÌáÐÑ×´Ì¬
			remindStateMapper.deleteByRemindId(id);
			remindMapper.deleteByPrimaryKey(id);
			ajaxReturn.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
		}

		return ajaxReturn;
	}
}

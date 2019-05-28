package telemarketing.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import telemarketing.model.AjaxReturn;
import telemarketing.model.Business_type;
import telemarketing.model.Customer;
import telemarketing.model.CustomerState;
import telemarketing.model.Pager;
import telemarketing.model.Pt_user;
import telemarketing.model.SoundRecord;
import telemarketing.model.TrackRecord;
import telemarketing.repository.CustomerStateMapper;
import telemarketing.util.AudioHelper;
import telemarketing.util.DateUtil;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

	// 公共池
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getAllList(Model model, HttpServletRequest request) throws SQLException {
		Pager pager = pagerServer.searchCustom("", "", "", "", "", "", "", -1, "全部", "", -1, "id", "asc", "", "", 1, 1,
				0, "", "");
		model.addAttribute("pager", pager);
		model.addAttribute("contactState", -1);
		model.addAttribute("selectOrderby", "id asc");
		request.getSession(true).setAttribute("my_cus_data", pager);
		return null;
	}

	// 公共池
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String searchAll(Model model,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String cus_name,
			@RequestParam(required = false, defaultValue = "") String id_person,
			@RequestParam(value = "contactState", required = false, defaultValue = "0") int state,
			@RequestParam(required = false, defaultValue = "全部") String business_name,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "") String who_use_name,
			@RequestParam(required = false, defaultValue = "") String who_put_common_name,
			@RequestParam(required = false, defaultValue = "") String add_time_beg,
			@RequestParam(required = false, defaultValue = "") String add_time_end,
			@RequestParam(required = false, defaultValue = "") String get_time_beg,
			@RequestParam(required = false, defaultValue = "") String get_time_end,
			@RequestParam(required = true, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize, HttpServletRequest request

	) throws SQLException {

		Pager pager = pagerServer.searchCustom("", phone, cus_name, id_person, who_use_name, add_time_beg, add_time_end,
				state, business_name, who_put_common_name, -1, orderField, orderType, get_time_beg, get_time_end,
				pageNum, 1, currentPageSize, upDownId, "");
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("cus_name", cus_name);
		model.addAttribute("id_person", id_person);
		model.addAttribute("contactState", state);
		model.addAttribute("business_name", business_name);
		model.addAttribute("who_put_common_name", who_put_common_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("upDownId", upDownId);
		request.getSession(true).setAttribute("my_cus_data", pager);
		return null;
	}

	// 分页功能
	@RequestMapping(value = "/pager", method = RequestMethod.GET)
	public String pager(Model model, @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = true, defaultValue = "") String cus_name,
			@RequestParam(required = true, defaultValue = "") String id_person,
			@RequestParam(required = true, defaultValue = "-1") int state,
			@RequestParam(required = true, defaultValue = "-1") int has_recovery,
			@RequestParam(required = false, defaultValue = "全部") String business_name,
			@RequestParam(required = false, defaultValue = "") String who_put_common_name,
			@RequestParam(required = true, defaultValue = "") String orderField,
			@RequestParam(required = true, defaultValue = "") String orderType,
			@RequestParam(required = true, defaultValue = "1") int pageNum,
			@RequestParam(required = true, defaultValue = "") String who_use,
			@RequestParam(required = true, defaultValue = "") String who_use_name,
			@RequestParam(required = true, defaultValue = "") String dataFrom,
			@RequestParam(required = true, defaultValue = "") String add_time_beg,
			@RequestParam(required = true, defaultValue = "") String add_time_end,
			@RequestParam(required = false, defaultValue = "") String get_time_beg,
			@RequestParam(required = false, defaultValue = "") String get_time_end,
			@RequestParam(required = true, defaultValue = "") String upDownId,
			@RequestParam(required = true, defaultValue = "1") int searchType,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize,
			@AuthenticationPrincipal User user1, HttpServletRequest request) throws SQLException {

		Pager pager = pagerServer.searchCustom(user1.getUsername(), phone, cus_name, id_person, who_use_name,
				add_time_beg, add_time_end, state, business_name, who_put_common_name, has_recovery, orderField,
				orderType, get_time_beg, get_time_end, pageNum, searchType, currentPageSize, upDownId, dataFrom);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("cus_name", cus_name);
		model.addAttribute("id_person", id_person);
		model.addAttribute("contactState", state);
		model.addAttribute("has_recovery", has_recovery);
		model.addAttribute("business_name", business_name);
		model.addAttribute("selectOrderby", orderField + " " + orderType);
		model.addAttribute("searchType", searchType);
		model.addAttribute("add_time_beg", add_time_beg);
		model.addAttribute("add_time_end", add_time_end);
		model.addAttribute("get_time_beg", get_time_beg);
		model.addAttribute("get_time_end", get_time_end);
		model.addAttribute("who_use_name", who_use_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("who_put_common_name", who_put_common_name);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("dataFrom", dataFrom);
		String retStr = "customer/list";
		if (searchType == 2 || searchType == 3 || searchType == 4) {
			retStr = "customer/my";

		}

		request.getSession(true).setAttribute("my_cus_data", pager);
		return retStr;
	}

	// my
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public String getMyList(Model model, @RequestParam(required = false, defaultValue = "-1") int state,
			@RequestParam(required = false, defaultValue = "全部") String business_name,
			@RequestParam(required = false, defaultValue = "2") int searchType,
			@RequestParam(required = false, defaultValue = "") String who_use,
			@RequestParam(required = false, defaultValue = "") String who_use_name,
			@RequestParam(required = false, defaultValue = "") String who_put_common_name,
			@RequestParam(required = false, defaultValue = "") String add_time_beg,
			@RequestParam(required = false, defaultValue = "") String add_time_end,
			@RequestParam(required = false, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "0") int is_from_reports,
			@AuthenticationPrincipal User user1, HttpServletRequest request) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		if (user.getDeptId().getId() == 1017 || user.getDeptId().getDeptName().equals("贷后部")) {
			searchType = 4;
		}

		Pager pager = pagerServer.searchCustom(StringUtils.isBlank(who_use) ? user1.getUsername() : who_use, phone, "",
				"", "", add_time_beg, add_time_end, state, business_name, who_put_common_name, -1, "id", "asc", "", "",
				1, searchType, 0, upDownId, "");

		if (is_from_reports == 1) { // 来自报表
			if (!user.getPhone().equals(who_use) || !user.getRealName().equals(who_use_name)) {
				searchType = 3;
			}
		}

		model.addAttribute("pager", pager);
		model.addAttribute("searchType", searchType);
		model.addAttribute("contactState", state);
		model.addAttribute("add_time_beg", add_time_beg);
		model.addAttribute("add_time_end", add_time_end);
		model.addAttribute("business_name", business_name);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("who_use_name", who_use_name);
		model.addAttribute("phone", phone);
		model.addAttribute("who_put_common_name", who_put_common_name);
		request.getSession(true).setAttribute("my_cus_data", pager);
		return null;
	}

	// my
	@RequestMapping(value = "/my", method = RequestMethod.POST)
	public String postMyList(Model model, @AuthenticationPrincipal User user1,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String cus_name,
			@RequestParam(required = false, defaultValue = "") String id_person,
			@RequestParam(value = "contactState", required = false, defaultValue = "0") int state,
			@RequestParam(required = true, defaultValue = "-1") int has_recovery,
			@RequestParam(required = false, defaultValue = "全部") String business_name,
			@RequestParam(required = false, defaultValue = "") String selectOrderby,
			@RequestParam(required = false, defaultValue = "2") int searchType,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "") String who_use_name,
			@RequestParam(required = false, defaultValue = "") String add_time_beg,
			@RequestParam(required = false, defaultValue = "") String add_time_end,
			@RequestParam(required = false, defaultValue = "") String get_time_beg,
			@RequestParam(required = false, defaultValue = "") String get_time_end,
			@RequestParam(required = true, defaultValue = "") String upDownId,
			@RequestParam(required = true, defaultValue = "") String dataFrom,
			@RequestParam(required = false, defaultValue = "") String who_put_common_name,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize, HttpServletRequest request

	) throws SQLException {

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		if (user.getDeptId().getId() == 1017 || user.getDeptId().getDeptName().equals("贷后部")) {
			searchType = 4;
		}

		String orderField = "";
		String orderType = "";
		if (StringUtils.isNotBlank(selectOrderby)) {
			String[] orders = selectOrderby.split(" ");
			orderField = orders[0];
			orderType = orders[1];
		}

		Pager pager = pagerServer.searchCustom(user1.getUsername(), phone, cus_name, id_person, who_use_name,
				add_time_beg, add_time_end, state, business_name, who_put_common_name, has_recovery, orderField,
				orderType, get_time_beg, get_time_end, pageNum, searchType, currentPageSize, upDownId, dataFrom);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("cus_name", cus_name);
		model.addAttribute("id_person", id_person);
		model.addAttribute("contactState", state);
		model.addAttribute("has_recovery", has_recovery);
		model.addAttribute("selectOrderby", selectOrderby);
		model.addAttribute("searchType", searchType);
		model.addAttribute("add_time_beg", add_time_beg);
		model.addAttribute("add_time_end", add_time_end);
		model.addAttribute("get_time_beg", get_time_beg);
		model.addAttribute("get_time_end", get_time_end);
		model.addAttribute("who_use_name", who_use_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("business_name", business_name);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("dataFrom", dataFrom);
		model.addAttribute("who_put_common_name", who_put_common_name);

		request.getSession(true).setAttribute("my_cus_data", pager);
		return null;

	}

	@RequestMapping(value = "/getState/{id}", method = RequestMethod.GET)
	public @ResponseBody AjaxReturn getState(@PathVariable int id) {
		AjaxReturn return1 = new AjaxReturn();
		return1.setObject1(customerMapper.selectByPrimaryKey(id));
		return1.setObject2(busineMapper.selectAll());
		return return1;
	}

	@RequestMapping(value = "/getCustomer", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn getCustomer(@RequestParam int id) {
		AjaxReturn return1 = new AjaxReturn();
		return1.setCode(1);
		return1.setObject1(customerMapper.selectByPrimaryKey(id));
		return return1;
	}

	@RequestMapping(value = "/update/up_state", method = RequestMethod.POST)
	public @ResponseBody String updateState(@RequestParam int state, @RequestParam String busiName,
			@RequestParam String ids, @AuthenticationPrincipal User user) {
		CustomerState customerState = customerStateMapper.selectByPrimaryKey(state);
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		String content = "";
		Customer old;
		for (String string : temp) {
			old = customerMapper.selectByPrimaryKey(Integer.parseInt(string));
			list.add(Integer.parseInt(string));
			// 添加跟踪记录
			content = "修改状态:" + customerState.getName() + " 业务:" + busiName;
			addTrackRecord(user.getUsername(), Integer.parseInt(string), content);
		}

		if (list.size() == 0) {
			return "0";
		}

		boolean ret = customerService.updateState(list, state, busiName);
		return ret ? "1" : "0";
	}

	// 更新到公共池
	@RequestMapping(value = "/update/up_common", method = RequestMethod.POST)
	public @ResponseBody String updateToCommon(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		Customer old;
		byte state = 0;
		for (String string : temp) {
			old = customerMapper.selectByPrimaryKey(Integer.parseInt(string));
			state = old.getState();
			if (state != 6 && state != 7 && state != 8) { // 这三种状态不能放入
				list.add(Integer.parseInt(string));
				// 添加跟踪记录
				addTrackRecord(user1.getUsername(), Integer.parseInt(string), "放入公共池");

			}
		}

		if (list.size() == 0) {
			return "0";
		}

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		boolean ret = customerService.updateToCommon(list, user1.getUsername(), user.getRealName());
		return ret ? "1" : "0";
	}

	// 分配到指定用户
	@RequestMapping(value = "/update/up_who_use", method = RequestMethod.POST)
	public @ResponseBody String updateWhoUse(@RequestParam String phone, @RequestParam String ids,
			@AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(phone);
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		for (String string : temp) {
			list.add(Integer.parseInt(string));
			// 添加跟踪记录
			addTrackRecord(user1.getUsername(), Integer.parseInt(string), "分配给用户:" + user.getRealName());
		}

		boolean ret = customerService.updateWhoUse(list, phone, user.getRealName(), user.getUpDownId());
		return ret ? "1" : "0";
	}

	// 回收客户
	@RequestMapping(value = "/backCustomer", method = RequestMethod.POST)
	public @ResponseBody int backCustomer(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		int id = -1;
		for (String string : temp) {
			id = Integer.parseInt(string);
			list.add(id);
		}

		customerMapper.recoveryCustomer(list, user.getPhone(), user.getRealName(), new Date(),
				user.getUpDownId());
		return 1;
	}

	// 共享到指定多个用户
	@RequestMapping(value = "/update/share_who_use", method = RequestMethod.POST)
	public @ResponseBody String updateShareWhoUse(@RequestParam String phones, @RequestParam String ids,
			@AuthenticationPrincipal User user1) {
		String names = "";
		for (String phone : phones.split(",")) {
			Pt_user user = ptUserMapper.selectByPhone(phone);
			names += user.getRealName() + ",";
		}

		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		for (String string : temp) {
			list.add(Integer.parseInt(string));
			// 添加跟踪记录
			addTrackRecord(user1.getUsername(), Integer.parseInt(string), "共享给用户:" + names);
		}

		boolean ret = customerService.updateShareUse(list, phones, names.substring(0, names.length() - 1));
		return ret ? "1" : "0";
	}

	// 取消多个共享
	@RequestMapping(value = "/update/cancelshare", method = RequestMethod.POST)
	public @ResponseBody int cancelshare(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		String[] temp = ids.split(",");
		for (String string : temp) {
			Customer old = customerMapper.selectByPrimaryKey(Integer.parseInt(string));
			// 添加跟踪记录
			addTrackRecord(user1.getUsername(), Integer.parseInt(string), "取消共享给用户:" + old.getShareUseName());
			old.setShareUse("");
			old.setShareUseName("");
			customerMapper.updateByPrimaryKey(old);
		}

		return 1;
	}

	// 导入客户
	@RequestMapping(value = "/importData", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn importData(@RequestPart MultipartFile input_file,
			@AuthenticationPrincipal User user1, HttpServletResponse response) throws IOException {
		String mString = "导入完成成功！";
		List<Customer> list = null;
		try {
			Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input_file.getInputStream()));
			String data = null;
			list = new ArrayList<>();
			while ((data = bufferedReader.readLine()) != null) {
				if (StringUtils.isBlank(data)) {
					continue;
				}
				String[] arrary = data.split("[\t;:,]");
				Customer customer = new Customer();
				customer.setPhone(arrary[0].trim());
				customer.setCusName(arrary.length >= 3 ? arrary[1] : "");
				customer.setDataFrom(arrary.length >= 3 ? arrary[2] : "");
				if (customer.getCusName().length() > 20) {
					customer.setCusName(customer.getCusName().substring(0, 20));
				}
				if (customer.getDataFrom().length() > 50) {
					customer.setDataFrom(customer.getDataFrom().substring(0, 50));
				}
				customer.setIdPerson("");
				customer.setAddress(arrary.length >= 4 ? arrary[3] : "");
				customer.setAddTime(new Date());
				customer.setWhoUse(user.getPhone());
				customer.setWhoUseName(user.getRealName());
				customer.setWhoGetTime(new Date());
				customer.setWho_up_down_id(user.getDeptId().getUpDownId());
				customer.setState((byte) 0);
				customer.setBusiness_name("无业务");
				customer.setUpdate_state_time(new Date());
				list.add(customer);
			}

			mString = customerService.importData(list);
			TxtLogger.log(user.getRealName() + " " + mString, LogTye.INFO, LogFileCreateType.OneFileEveryDay,
					"ImportCustomer");
		} catch (Exception ex) {
			mString = ex.getMessage();
			TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "");
			ex.printStackTrace();
		} finally {
			input_file.getInputStream().close();
		}

		return new AjaxReturn(1, mString);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn add(@AuthenticationPrincipal User user1, @RequestParam String add_phone,
			@RequestParam String add_name, @RequestParam String add_from) {
		AjaxReturn return1 = new AjaxReturn();
		try {
			Customer old = customerMapper.selectByPhone(add_phone);
			if (old != null) {
				return1.setRetMsg("当前手机号已存在,位置:" + (old.getIs_common() == 1 ? "公共池" : "我的客户-" + old.getWhoUseName()));
				return return1;
			}

			Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
			Customer customer = new Customer();
			customer.setPhone(add_phone);
			customer.setCusName(add_name);
			customer.setDataFrom(add_from);
			customer.setIdPerson("");
			customer.setAddress("");
			customer.setAddTime(new Date());
			customer.setWhoUse(user.getPhone());
			customer.setWhoUseName(user.getRealName());
			customer.setWhoGetTime(new Date());
			customer.setWho_up_down_id(user.getDeptId().getUpDownId());
			customer.setState((byte) 0);
			customer.setBusiness_name("无业务");
			customer.setUpdate_state_time(new Date());
			customerMapper.insert(customer);
			return1.setCode(1);

		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			return1.setRetMsg(ex.getMessage());
			ex.printStackTrace();
		}

		return return1;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn edit(@AuthenticationPrincipal User user1, @RequestParam String id,
			@RequestParam String name, @RequestParam String address, @RequestParam String data_from,
			@RequestParam String id_person) {
		AjaxReturn return1 = new AjaxReturn();
		try {
			Customer customer = customerMapper.selectByPrimaryKey(Integer.parseInt(id));
			if (customer == null) {
				return1.setRetMsg("当前客户不存在");
				return return1;
			}
			customer.setCusName(name);
			customer.setDataFrom(data_from);
			customer.setIdPerson(id_person);
			customer.setAddress(address);
			customerMapper.updateByPrimaryKey(customer);
			return1.setCode(1);

		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			return1.setRetMsg(ex.getMessage());
			ex.printStackTrace();
		}

		return return1;
	}

	// 添加和查看跟踪记录
	@RequestMapping(value = "/track_record/{id}")
	public String trackRecord(@PathVariable int id, Model model, @AuthenticationPrincipal User user1) throws Exception {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		// 获取当前客户信息
		Customer customer = customerMapper.selectByPrimaryKey(id);
		if (customer == null) {
			throw new Exception("该客户不存在！");
		}

		if (user.getRoleCode().getRoleCode().equals("CustomerService") && !customer.getWhoUse().equals(user.getPhone())
				&& (customer.getShareUse() != null && !customer.getShareUse().contains(user.getPhone())
						&& customer.getIs_common() == 0)) {
			throw new Exception("对不起，您无权查看此记录！");
		}

		// 获取所有跟踪记录
		List<TrackRecord> trackList = trackMapper.selectByCustomerId(id);
		// 获取所有的业务类型
		List<Business_type> busiTypeList = busineMapper.selectAll();
		model.addAttribute("customer", customer);
		Comparator<TrackRecord> comparator = (h1, h2) -> h1.getAddTime().compareTo(h2.getAddTime());
		trackList.sort(comparator.reversed());
		model.addAttribute("trackList", trackList);
		model.addAttribute("busiTypeList", busiTypeList);

		return "customer/track_record";
	}

	// 添加和查看跟踪记录
	@RequestMapping(value = "/next_cus_track/{id}")
	public String nextTrackRecord(@PathVariable int id, @AuthenticationPrincipal User user1, Model model,
			HttpServletRequest request) {
		Customer customer = null;
		int isLastCustomer = 0;
		// 先获取下一个客户
		if (request.getSession(true).getAttribute("my_cus_data") != null) {
			Pager pager = (Pager) request.getSession(true).getAttribute("my_cus_data");
			if (pager.getTotalCount() > 0) {
				List<Customer> customers = (List<Customer>) pager.getData();
				for (int i = 0; i < customers.size(); i++) {
					if (customers.get(i).getId() == id) {
						if (i < customers.size() - 1) {
							customer = customers.get(i + 1);
						}

						break;
					}
				}
			}
		}

		if (customer == null) {
			// 获取当前客户信息
			customer = customerMapper.selectByPrimaryKey(id);
			isLastCustomer = 1;
		}

		if (isLastCustomer != 1) {
			return "redirect:/customer/track_record/" + customer.getId();
		}

		// 获取所有跟踪记录
		List<TrackRecord> trackList = trackMapper.selectByCustomerId(customer.getId());
		// 获取所有的业务类型
		List<Business_type> busiTypeList = busineMapper.selectAll();
		model.addAttribute("customer", customer);
		Comparator<TrackRecord> comparator = (h1, h2) -> h1.getAddTime().compareTo(h2.getAddTime());
		trackList.sort(comparator.reversed());
		model.addAttribute("trackList", trackList);
		model.addAttribute("busiTypeList", busiTypeList);
		model.addAttribute("isLastCustomer", isLastCustomer);
		return "customer/track_record";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody int delete(@PathVariable int id) {
		Customer customer = customerMapper.selectByPrimaryKey(id);
		if ((new Date().getTime() - customer.getUpdate_state_time().getTime()) >= (7 * 24 * 3600 * 1000)) {
			// customerMapper.insertToBackup(customer);
			customerMapper.deleteByPrimaryKey(id);
			trackRecordMapper.deleteByCustomer(id);
			return 1;
		} else {
			return 0;
		}
	}

	@RequestMapping(value = "/rob/{id}", method = RequestMethod.POST)
	public @ResponseBody int rob(@PathVariable int id, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Customer customer = customerMapper.selectByPrimaryKey(id);
		customer.setWho_up_down_id(user.getUpDownId());
		customer.setWhoGetTime(new Date());
		customer.setWhoUse(user.getPhone());
		customer.setWhoUseName(user.getRealName());
		customer.setIs_common((byte) 0);
		customerMapper.updateByPrimaryKey(customer);
		return 1;
	}

	@RequestMapping(value = "/deleteMore", method = RequestMethod.POST)
	public @ResponseBody int deleteMore(@RequestParam String ids) {
		boolean isNotDele = true;
		for (String id : ids.split(",")) {
			Customer customer = customerMapper.selectByPrimaryKey(Integer.parseInt(id));
			if ((new Date().getTime() - customer.getUpdate_state_time().getTime()) >= (7 * 24 * 3600 * 1000)) {
				// customerMapper.insertToBackup(customer);
				customerMapper.deleteByPrimaryKey(Integer.parseInt(id));
				trackRecordMapper.deleteByCustomer(Integer.parseInt(id));
				isNotDele = false;
			}
		}
		if (isNotDele) {
			return 0;
		} else {
			return 1;
		}
	}

	// 删除七天前公共池
	@RequestMapping(value = "/deleteAllShare", method = RequestMethod.POST)
	public @ResponseBody int deleteAllShare(@AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		if (!user.getRoleCode().getRoleCode().equals("ADMIN") && !user.getRoleCode().getRoleCode().equals("CSO")) {
			return 0;
		}
		boolean isNotDele = false;
		trackMapper.deleteAllCommonTrack();
		customerMapper.deleteAllCommon();
		if (isNotDele) {
			return 0;
		} else {
			return 1;
		}
	}

	@RequestMapping(value = "/robMore", method = RequestMethod.POST)
	public @ResponseBody int robMore(@RequestParam String ids, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		for (String id : ids.split(",")) {
			Customer customer = customerMapper.selectByPrimaryKey(Integer.parseInt(id));
			customer.setWho_up_down_id(user.getUpDownId());
			customer.setWhoGetTime(new Date());
			customer.setWhoUse(user.getPhone());
			customer.setWhoUseName(user.getRealName());
			customer.setIs_common((byte) 0);
			customerMapper.updateByPrimaryKey(customer);
		}
		return 1;
	}

	@RequestMapping(value = "/soundlist", method = RequestMethod.GET)
	public String getSoundList(@RequestParam(required = false, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "") String sound_time_beg,
			@RequestParam(required = false, defaultValue = "") String sound_time_end,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String seconds,
			@RequestParam(required = false, defaultValue = "") String maxseconds,
			@RequestParam(required = false, defaultValue = "") String customer_phone,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum, @AuthenticationPrincipal User user1,
			Model model) throws SQLException {

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		upDownId= StringUtils.isBlank(upDownId) ? user.getUpDownId() : upDownId;
		if(user.getRoleCode().getRoleCode().equals("SaleBacker") || user.getRoleCode().getRoleCode().equals("SaleBackLeader")){
			upDownId="";
		}
		Pager pager = pagerServer.searchSound(customer_phone, "",
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name, seconds,
				maxseconds,upDownId, sound_time_beg,
				sound_time_end, "", "", orderField, orderType, pageNum, currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("sound_time_beg", sound_time_beg);
		model.addAttribute("sound_time_end", sound_time_end);
		model.addAttribute("user_name", user_name);
		model.addAttribute("seconds", seconds);
		model.addAttribute("maxseconds", maxseconds);
		model.addAttribute("customer_phone", customer_phone);
		model.addAttribute("currentPageSize", currentPageSize);
		return "customer/soundlist";
	}

	@RequestMapping(value = "/soundlist", method = RequestMethod.POST)
	public String postSoundList(@RequestParam(required = false, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "") String sound_time_beg,
			@RequestParam(required = false, defaultValue = "") String sound_time_end,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String customer_phone,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize,
			@RequestParam(required = false, defaultValue = "") String seconds,
			@RequestParam(required = false, defaultValue = "") String maxseconds, @AuthenticationPrincipal User user1,
			Model model) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchSound(customer_phone, "",
				user.getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name, seconds, maxseconds,
				upDownId.equals("-1") ? user.getUpDownId() : upDownId, sound_time_beg, sound_time_end, "", "", "", "",
				1, currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("sound_time_beg", sound_time_beg);
		model.addAttribute("sound_time_end", sound_time_end);
		model.addAttribute("user_name", user_name);
		model.addAttribute("seconds", seconds);
		model.addAttribute("maxseconds", maxseconds);
		model.addAttribute("customer_phone", customer_phone);
		model.addAttribute("currentPageSize", currentPageSize);
		return "customer/soundlist";
	}

	@RequestMapping(value = "/importSound", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn importSound(@RequestParam(required = false, defaultValue = "") String remark,
			@RequestParam(required = false, defaultValue = "") String user_id, @RequestParam MultipartFile[] files,
			@AuthenticationPrincipal User user1, HttpServletRequest request) {
		AjaxReturn ajaxReturn = new AjaxReturn();
		String lastSuccessFile = "第一个就出错";
		Pt_user user = ptUserMapper.selectByPhone(StringUtils.isBlank(user_id) ? user1.getUsername() : user_id);
		try {
			if (files != null) {
				String subDir = DateUtil.format(new Date(), "yyyyMMdd");
				String rootDir = systemProperty.getSound_record_path() + "/" + subDir;
				File file = new File(rootDir);
				if (!file.exists()) {
					file.mkdirs();
				}

				for (MultipartFile multipartFile : files) {
					String originalFilename = multipartFile.getOriginalFilename().toLowerCase();
					if (originalFilename.contains(".mp3") || originalFilename.contains(".wav")
							|| originalFilename.contains(".amr") || originalFilename.contains(".zip")) {
						if (originalFilename.contains(".zip")) {
							// 先保存然后调用zip处理服务类
							String zipName = UUID.randomUUID().toString() + "-" + originalFilename;
							multipartFile.transferTo(new File(rootDir + "/" + zipName));// 保存
							uploadZipService.handle(user, systemProperty.getSound_record_path(), subDir, zipName,
									remark);
							continue;
						}

						SoundRecord soundRecord = new SoundRecord();
						soundRecord.setAddTime(new Date());
						soundRecord.setCustomerPhone("");
						soundRecord.setLocalPhone("");
						soundRecord.setRemark(remark);
						soundRecord.setUserId(user.getPhone());
						soundRecord.setUserName(user.getRealName());
						soundRecord.setUpDownId(user.getDeptId().getUpDownId());
						soundRecord.setSoundTime(new Date()); // 这部分要修改，要根据文件来解析时间
						soundRecord.setDirection("呼出");
						String filename = originalFilename;
						String[] temp = filename.split("_");
						if (temp.length == 3) {
							if (temp[0].equalsIgnoreCase("O")) {
								soundRecord.setDirection("呼出");
							} else {
								soundRecord.setDirection("呼入");
							}
							soundRecord.setCustomerPhone(temp[1]);
							String soundTime = temp[2].replace(".wav", "");
							soundRecord.setSoundTime(DateUtil
									.parse(soundTime.length() == 14 ? soundTime : "20" + soundTime, "yyyyMMddHHmmss"));
						} else if (temp.length == 2) {
							soundRecord.setDirection("呼出");
							soundRecord.setCustomerPhone(temp[0]);
							String soundTime = temp[1].replace(".wav", "");
							soundRecord.setSoundTime(DateUtil
									.parse(soundTime.length() == 14 ? soundTime : "20" + soundTime, "yyyyMMddHHmmss"));

						} else {
							filename = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + "-" + user.getPhone()
									+ originalFilename.substring(originalFilename.lastIndexOf('.'));
						}

						if (soundRecord.getCustomerPhone().length() > 11) {
							continue;
						}

						multipartFile.transferTo(new File(rootDir + "/" + filename));// 保存
						if (originalFilename.contains(".mp3")) {
							soundRecord.setSoundLength(AudioHelper.getMp3Seconds2(rootDir + "/" + filename));
							soundRecord.setFileName(subDir + "/" + filename);
						}
						/*
						 * else if (originalFilename.contains(".wav")) {
						 * soundRecord.setSoundLength(AudioHelper.getWavSeconds(
						 * rootDir + "/" + filename));
						 * soundRecord.setFileName(filename); }
						 */
						else {
							String targetName = rootDir + "/" + filename.substring(0, filename.lastIndexOf('.'))
									+ ".mp3";
							AudioHelper.toAudioWithFFmpeg(systemProperty.getFfmpeg_path(), rootDir + "/" + filename,
									targetName);
							soundRecord.setSoundLength(AudioHelper.getMp3Seconds2(targetName));
							soundRecord.setFileName(
									subDir + "/" + filename.substring(0, filename.lastIndexOf('.')) + ".mp3");
						}

						soundMapper.insert(soundRecord);
						lastSuccessFile = multipartFile.getOriginalFilename();
					}
				}

				ajaxReturn.setCode(1);
			} else {
				ajaxReturn.setRetMsg("导入失败");
			}
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ex.printStackTrace();
			ajaxReturn.setRetMsg("导入出错,最后一个成功导入文件为" + lastSuccessFile + ". 如果该文件不是您选择的最后一个文件，说明这文件之后遇到了异常! "
					+ "记住该文件及之前的文件以防下次重复导入!");
		}

		return ajaxReturn;
	}

	@RequestMapping(value = "/deleteSound", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn deleteSound(@RequestParam String ids, HttpServletRequest request) {
		AjaxReturn ajaxReturn = new AjaxReturn();
		String[] temp = ids.split(",");
		for (String string : temp) {
			SoundRecord soundRecord = soundMapper.selectByPrimaryKey(Integer.parseInt(string));
			if (soundRecord == null) {
				continue;
			}

			soundMapper.deleteByPrimaryKey(Integer.parseInt(string));
			String rootDir = systemProperty.getSound_record_path();
			File file = new File(rootDir + "/" + soundRecord.getFileName());
			if (file.exists()) {
				file.delete();
			}
		}

		ajaxReturn.setCode(1);
		return ajaxReturn;
	}

	@RequestMapping(value = "/downloadsound/{id}")
	public String downloadsound(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		SoundRecord record = soundMapper.selectByPrimaryKey(id);
		if (record == null) {
			return null;
		}

		try {
			String rootDir = systemProperty.getSound_record_path();
			os = response.getOutputStream();
			File file = new File(rootDir + "/" + record.getFileName());
			if (!file.exists()) {
				return null;
			}
			response.reset();
			response.setHeader("Content-Disposition", "attachment;filename=" + record.getFileName());
			response.setContentType("application/octet-stream; charset=utf-8");
			os.write(FileUtils.readFileToByteArray(file));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
		}

		return null;
	}

	@RequestMapping(value = "/playsound/{id}")
	public String playsound(@PathVariable Integer id, Model model, HttpServletRequest request,
			@AuthenticationPrincipal User user1) throws Exception {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		SoundRecord record = soundMapper.selectByPrimaryKey(id);
		if (record == null) {
			return null;
		}

		String filePath = request.getContextPath() + "/assets/sharefile/sound/" + record.getFileName();
		model.addAttribute("filepath", filePath);
		return "customer/playSound";
	}

	@RequestMapping(value = "/tracklist", method = RequestMethod.GET)
	public String tracklist(@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false, defaultValue = "") String add_time_beg,
			@RequestParam(required = false, defaultValue = "") String add_time_end,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String user_phone,
			@RequestParam(required = false, defaultValue = "") String customer_phone,
			@RequestParam(required = false, defaultValue = "") String customer_name,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum, @AuthenticationPrincipal User user1,
			Model model) throws SQLException {

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchTrackRecord(customer_phone, customer_name,
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : user_phone, user_name,
				upDownId.equals("all") ? user.getUpDownId() : upDownId, add_time_beg, add_time_end, orderField,
				orderType, pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("add_time_beg", add_time_beg);
		model.addAttribute("add_time_end", add_time_end);
		model.addAttribute("user_name", user_name);
		model.addAttribute("user_phone", user_phone);
		model.addAttribute("customer_phone", customer_phone);
		model.addAttribute("customer_name", customer_name);
		return "customer/tracklist";
	}

	@RequestMapping(value = "/tracklist", method = RequestMethod.POST)
	public String tracklist(@RequestParam(required = false, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "") String add_time_beg,
			@RequestParam(required = false, defaultValue = "") String add_time_end,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String user_phone,
			@RequestParam(required = false, defaultValue = "") String customer_phone,
			@RequestParam(required = false, defaultValue = "") String customer_name,
			@AuthenticationPrincipal User user1, Model model) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchTrackRecord(customer_phone, customer_name,
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : user_phone, user_name,
				upDownId.equals("-1") ? user.getUpDownId() : upDownId, add_time_beg, add_time_end, "", "", 1);

		model.addAttribute("pager", pager);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("add_time_beg", add_time_beg);
		model.addAttribute("add_time_end", add_time_end);
		model.addAttribute("user_name", user_name);
		model.addAttribute("user_phone", user_phone);
		model.addAttribute("customer_phone", customer_phone);
		model.addAttribute("customer_name", customer_name);
		return "customer/tracklist";
	}
}

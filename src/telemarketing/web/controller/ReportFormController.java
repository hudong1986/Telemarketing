package telemarketing.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import telemarketing.model.BusiReport;
import telemarketing.model.Pager;
import telemarketing.model.Pt_user;
import telemarketing.model.TrackReport;
import telemarketing.service.StaticService;
import telemarketing.util.DateUtil;
import telemarketing.util.ExportExcelUtils;

@Controller
@RequestMapping("/reports")
public class ReportFormController extends BaseController{

	

	@RequestMapping(value = "/report1", method = RequestMethod.GET)
	public String report1(Model model, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		model.addAttribute("date", date);
		List<BusiReport> list = reportMapper.searchBusiByDate(date,
				user.getUpDownId(),user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getRealName() : null);
		//设置客户状态名
		if(list!=null && list.size()>0){
			for (BusiReport busiReport : list) {
				busiReport.setStateName(StaticService.getCustomStateName(busiReport.getState()));
			}
		}
		
		model.addAttribute("list", list);
		return "reports/report1";
	}

	@RequestMapping(value = "/report1", method = RequestMethod.POST)
	public String report1(	@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false) String user_name, 
		 Model model, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		model.addAttribute("date", date);
		List<BusiReport> list = reportMapper.searchBusiByDate(date,
				upDownId.equals("all") ? user.getUpDownId() : upDownId,user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getRealName() : user_name);
		//设置客户状态名
		if(list!=null && list.size()>0){
			for (BusiReport busiReport : list) {
				busiReport.setStateName(StaticService.getCustomStateName(busiReport.getState()));
			}
		}
		model.addAttribute("list", list);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("user_name", user_name);
		return null;
	}

	@RequestMapping(value = "/report2", method = RequestMethod.GET)
	public String report2(Model model,
			@RequestParam(required = false,defaultValue="-1") String busiName,
			@RequestParam(required = false,defaultValue="-1") String state,
			@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false,defaultValue="") String user_name, 
			@RequestParam(required = false,defaultValue="") String date_beg, 
			@RequestParam(required = false,defaultValue="") String date_end, 
			@AuthenticationPrincipal User user1,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum) throws SQLException {
 
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchHistoryDaiKuan(upDownId.equals("all") ? user.getUpDownId() : upDownId, 
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name,
				date_beg, date_end, busiName, state, orderField, orderType, pageNum,0,false);
		
		model.addAttribute("pager", pager);
		model.addAttribute("business_name", busiName);
		model.addAttribute("state", state);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("user_name", user_name);
		model.addAttribute("date_beg", date_beg);
		model.addAttribute("date_end", date_end);
		model.addAttribute("orderField", orderField);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNum", pageNum);
		
		return "reports/report2";
	}

	@RequestMapping(value = "/report2", method = RequestMethod.POST)
	public String report2(Model model, 
			@RequestParam(required = false,defaultValue="") String busiName,
			@RequestParam(required = false,defaultValue="") String state,
			@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false,defaultValue="") String user_name, 
			@RequestParam(required = false,defaultValue="") String date_beg, 
			@RequestParam(required = false,defaultValue="") String date_end, 
			@AuthenticationPrincipal User user1) throws ParseException, SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		 
		Pager pager = pagerServer.searchHistoryDaiKuan(upDownId.equals("all") ? user.getUpDownId() : upDownId, 
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name,
				date_beg, date_end, busiName, state, "id", "asc", 1,0,false);
		
		model.addAttribute("pager", pager);
		model.addAttribute("business_name", busiName);
		model.addAttribute("state", state);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("user_name", user_name);
		model.addAttribute("date_beg", date_beg);
		model.addAttribute("date_end", date_end);

		return "reports/report2";
	}
	
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public String exportExcel(
			@RequestParam(required = false,defaultValue="-1") String busiName,
			@RequestParam(required = false,defaultValue="-1") String state,
			@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false,defaultValue="") String user_name, 
			@RequestParam(required = false,defaultValue="") String date_beg, 
			@RequestParam(required = false,defaultValue="") String date_end, 
			@AuthenticationPrincipal User user1,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			HttpServletResponse response) throws ParseException, SQLException, IOException {
		
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchHistoryDaiKuan(upDownId.equals("all") ? user.getUpDownId() : upDownId, 
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name,
				date_beg, date_end, busiName, state, orderField, orderType, pageNum,0,true);
		if(pager.getTotalCount()>5000){
			response.getWriter().println("出于安全与性能考虑，每次导出条数不能超过5000条！");
			response.getWriter().flush();
			return null;
		}
		
		pager = pagerServer.searchHistoryDaiKuan(upDownId.equals("all") ? user.getUpDownId() : upDownId, 
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name,
				date_beg, date_end, busiName, state, orderField, orderType, pageNum,5000,false);
		
		List<BusiReport> list = (List<BusiReport> )pager.getData();
		String[] rowsName=new String[]{"ID","日期","部门","客服手机","客服姓名","业务类型","业务状态","数量"};  
		List<Object[]> listObj = new ArrayList<>();
		Object[] objs = null;  
        for (int i = 0; i < list.size(); i++) {  
        	BusiReport po = list.get(i);  
            objs = new Object[rowsName.length];  
            objs[1] = po.getDateStr(); 
            objs[2] = po.getDeptName();  
            objs[3] = po.getUser_phone();  
            objs[4] = po.getUser_name();  
            objs[5] = po.getBusiName(); 
            objs[6] = po.getStateName();
            objs[7] = po.getCount();  
            listObj.add(objs);  
        }  

		ExportExcelUtils ex = new ExportExcelUtils("业务历史统计报表("+DateUtil.format(new Date(), "yyyyMMddHHmmss")+")", rowsName, listObj,response);  
        ex.exportData();  


		return null;
	}
	
	@RequestMapping(value = "/exportRealTimeExcel", method = RequestMethod.GET)
	public String exportRealTimeExcel(
			@AuthenticationPrincipal User user1,
			@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false) String user_name, 
			HttpServletResponse response) throws ParseException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<BusiReport> list = reportMapper.searchBusiByDate(date,
				upDownId.equals("all") ? user.getUpDownId() : upDownId,user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getRealName() : user_name);
		
		String[] rowsName=new String[]{"ID","日期","部门","客服手机","客服姓名","业务类型","业务状态","数量"};  
		List<Object[]> listObj = new ArrayList<>();
		Object[] objs = null;  
        for (int i = 0; i < list.size(); i++) {  
        	BusiReport po = list.get(i);  
            objs = new Object[rowsName.length];  
            objs[1] = po.getDateStr(); 
            objs[2] = po.getDeptName();  
            objs[3] = po.getUser_phone();  
            objs[4] = po.getUser_name();  
            objs[5] = po.getBusiName(); 
            objs[6] = po.getStateName();
            objs[7] = po.getCount();  
            listObj.add(objs);  
        }  

		ExportExcelUtils ex = new ExportExcelUtils("业务统计报表("+date+")", rowsName, listObj,response);  
        ex.exportData();  
		return null;
	}
	
	@RequestMapping(value = "/soundReport", method = RequestMethod.GET)
	public String getSoundList(@RequestParam(required = false, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "") String sound_time_beg,
			@RequestParam(required = false, defaultValue = "") String sound_time_end,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String seconds,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum, @AuthenticationPrincipal User user1,
			Model model) throws SQLException {

		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchSoundGroupData("", "",
				user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name,seconds, 
						StringUtils.isBlank(upDownId) ? user.getUpDownId() : upDownId,
				sound_time_beg, sound_time_end, "", "", orderField, orderType, pageNum);
		model.addAttribute("pager", pager);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("sound_time_beg", sound_time_beg);
		model.addAttribute("sound_time_end", sound_time_end);
		model.addAttribute("user_name", user_name);
		model.addAttribute("seconds", seconds);
		return "reports/soundReport";
	}

	@RequestMapping(value = "/soundReport", method = RequestMethod.POST)
	public String postSoundList(@RequestParam(required = false, defaultValue = "") String upDownId,
			@RequestParam(required = false, defaultValue = "") String sound_time_beg,
			@RequestParam(required = false, defaultValue = "") String sound_time_end,
			@RequestParam(required = false, defaultValue = "") String user_name, 
			@RequestParam(required = false, defaultValue = "") String seconds,
			@AuthenticationPrincipal User user1,
			Model model) throws SQLException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		Pager pager = pagerServer.searchSoundGroupData("", "",
				user.getRoleCode().equals("CustomerService") ? user.getPhone() : "", user_name,seconds, 
						upDownId.equals("-1") ? user.getUpDownId() : upDownId,
				sound_time_beg, sound_time_end, "", "", "", "", 1);
		model.addAttribute("pager", pager);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("sound_time_beg", sound_time_beg);
		model.addAttribute("sound_time_end", sound_time_end);
		model.addAttribute("user_name", user_name);
		model.addAttribute("seconds", seconds);
		return null;
	}
	
	@RequestMapping(value = "/trackReport", method = RequestMethod.GET)
	public String trackReport(Model model, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		model.addAttribute("date", date);
		List<TrackReport> list = reportMapper.searchTrackReportByDate(date,
				user.getUpDownId(),user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getRealName() : null);
		
		if(list!=null &&list.size()>0){
			TrackReport totalTrack = new TrackReport();
			totalTrack.setWho_use_name("汇总");
			for (TrackReport item : list) {
				totalTrack.setTotal_get(totalTrack.getTotal_get()+item.getTotal_get());
				totalTrack.setTotal_common(totalTrack.getTotal_common()+item.getTotal_common());
				totalTrack.setTrack_count(totalTrack.getTrack_count()+item.getTrack_count());
				totalTrack.setState1(totalTrack.getState1()+item.getState1());
				totalTrack.setState2(totalTrack.getState2()+item.getState2());
				totalTrack.setState3(totalTrack.getState3()+item.getState3());
				totalTrack.setState4(totalTrack.getState4()+item.getState4());
				totalTrack.setState5(totalTrack.getState5()+item.getState5());
				totalTrack.setState11(totalTrack.getState11()+item.getState11());
			}
			
			list.add(totalTrack);
		}
		
		model.addAttribute("list", list);
		return "reports/trackReport";
	}
	
	@RequestMapping(value = "/trackReport", method = RequestMethod.POST)
	public String trackReport(
			@RequestParam(required = false, defaultValue = "all") String upDownId,
			@RequestParam(required = false) String user_name, 
			Model model, @AuthenticationPrincipal User user1) {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		model.addAttribute("date", date);
		List<TrackReport> list = reportMapper.searchTrackReportByDate(date,
				upDownId.equals("all") ? user.getUpDownId() : upDownId,user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getRealName() : user_name);
		
		if(list!=null &&list.size()>0){
			TrackReport totalTrack = new TrackReport();
			totalTrack.setWho_use_name("汇总");
			for (TrackReport item : list) {
				totalTrack.setTotal_get(totalTrack.getTotal_get()+item.getTotal_get());
				totalTrack.setTotal_common(totalTrack.getTotal_common()+item.getTotal_common());
				totalTrack.setTrack_count(totalTrack.getTrack_count()+item.getTrack_count());
				totalTrack.setState1(totalTrack.getState1()+item.getState1());
				totalTrack.setState2(totalTrack.getState2()+item.getState2());
				totalTrack.setState3(totalTrack.getState3()+item.getState3());
				totalTrack.setState4(totalTrack.getState4()+item.getState4());
				totalTrack.setState5(totalTrack.getState5()+item.getState5());
				totalTrack.setState11(totalTrack.getState11()+item.getState11());
			}
			
			list.add(totalTrack);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("upDownId", upDownId);
		model.addAttribute("user_name", user_name);
		return "reports/trackReport";
	}
	
	
	@RequestMapping(value = "/exportTrackReport", method = RequestMethod.GET)
	public String exportTrackReport(
			@RequestParam(required = false, defaultValue = "-1") String upDownId,
			@RequestParam(required = false) String user_name, 
			@AuthenticationPrincipal User user1,
			HttpServletResponse response) throws ParseException {
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<TrackReport> list = reportMapper.searchTrackReportByDate(date,
				upDownId.equals("-1") ? user.getUpDownId() : upDownId,user.getRoleCode().getRoleCode().equals("CustomerService") ? user.getRealName() : user_name);
		
		String[] rowsName=new String[]{"ID","日期","部门","客服手机","客服姓名","总分配","放入公共池","无意向"
				,"空号停机","暂不需要可以跟踪","意向客户","上门"};  
		List<Object[]> listObj = new ArrayList<>();
		Object[] objs = null;  
        for (int i = 0; i < list.size(); i++) {  
        	TrackReport po = list.get(i);  
            objs = new Object[rowsName.length];  
            objs[1] = po.getDateStr(); 
            objs[2] = po.getDept_name(); 
            objs[3] = po.getWho_use();  
            objs[4] = po.getWho_use_name();
            objs[5] = po.getTotal_get();
            objs[6] = po.getTotal_common();
            objs[7] = po.getState1();
            objs[8] = po.getState2();
            objs[9] = po.getState3();
            objs[10] = po.getState4();
            objs[11] = po.getState5();
            listObj.add(objs);  
        }  

		ExportExcelUtils ex = new ExportExcelUtils("今日分配电话效果统计报表("+date+")", rowsName, listObj,response);  
        ex.exportData();  
		return null;
	}
}

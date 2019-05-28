package telemarketing.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import telemarketing.model.LoginRecord;
import telemarketing.model.Pt_dept;
import telemarketing.model.Pt_user;
import telemarketing.service.StaticService;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;

@ControllerAdvice
public class AppWideControllerHandler extends BaseController {

	/*
	 * // 页面可以直接运用
	 * 
	 * @ModelAttribute(value = "ctx") public String
	 * setContextPath(HttpServletRequest request) { return
	 * request.getContextPath(); }
	 */
	// 页面可以直接运用
	@ModelAttribute(value = "mydept")
	public List<Pt_dept> setMydept(@AuthenticationPrincipal User user1, HttpServletRequest request) {
		if (user1 != null) {
			Pt_user user;
			if (request.getSession(true).getAttribute("LoginUser") != null) {
				user = (Pt_user) request.getSession(true).getAttribute("LoginUser");
			} else {
				user = ptUserMapper.selectByPhone(user1.getUsername());
			}

			return StaticService.getDeptlist(user.getUpDownId());
		} else
			return null;
	}

	// 页面可以直接运用
	@ModelAttribute(value = "mySubUsers")
	public List<Pt_user> mySubUsers(@AuthenticationPrincipal User user1, HttpServletRequest request) {
		if (user1 != null) {
			List<Pt_user> list = new ArrayList<>();
			Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
			if (user.getRoleCode().getRoleCode().equals("CustomerService")) {
				list.add(ptUserMapper.selectByPhone(user1.getUsername()));
			} else {
				list.addAll(ptUserMapper.selectByUpDownId(user.getUpDownId()));
			}

			return list;
		} else
			return null;
	}

	// 页面可以直接运用
	@ModelAttribute(value = "usermode")
	public Pt_user setUserMode(@AuthenticationPrincipal User user1, HttpServletRequest request) throws Exception {
		if (request.getServletPath().contains("remind/getMyRemind")) {
			return null;
		}

		Pt_user user = null;
		if (user1 != null && StringUtils.isNotEmpty(user1.getUsername())) {
			// 第一次登录后更新一下登录时间
			if (request.getSession(true).getAttribute("update_login_time") == null) {
				user = ptUserMapper.selectByPhone(user1.getUsername());
				user.setLoginTime(new Date());
				ptUserMapper.updateByPrimaryKey(user);
				request.getSession(true).setAttribute("update_login_time", 1);
				request.getSession(true).setAttribute("LoginUser", user);
				
				//记录登录记录
				LoginRecord loginRecord = new LoginRecord();
				loginRecord.setDeptName(user.getDeptId().getDeptName());
				loginRecord.setLoginIp(request.getRemoteHost());
				loginRecord.setLoginTime(new Date());
				loginRecord.setUpDownId(user.getUpDownId());
				loginRecord.setUserName(user.getRealName());
				loginRecord.setUserPhone(user.getPhone());
				loginRecordMapper.insert(loginRecord);
			} else {
				user = (Pt_user) request.getSession(true).getAttribute("LoginUser");
			}

			// 记录用户操作
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("UserId:" + user1.getUsername() + " ");
			sBuilder.append("ServletPath:" + request.getServletPath() + " ");
			sBuilder.append("QueryString:" + request.getQueryString() + " ");
			sBuilder.append("RequestURL:" + request.getRequestURL().toString() + " ");
			sBuilder.append("RemoteAddr:" + request.getRemoteAddr() + " ");
			TxtLogger.log(sBuilder.toString(), LogTye.INFO, LogFileCreateType.OneFileAnHour, "USER_OPER");

		} else {
			// 没有登录令牌时需要清楚更新登录时间标志
			request.getSession(true).removeAttribute("update_login_time");
			request.getSession(true).removeAttribute("LoginUser");
		}
		
		//判断合法IP，只针对非管理员
		if(user!=null && !user.getRoleCode().getRoleCode().equalsIgnoreCase("ADMIN")
				&& !user.getRoleCode().getRoleCode().equalsIgnoreCase("CSO")
				&& !user.getRoleCode().getRoleCode().equalsIgnoreCase("BackLineLeader")){
			//判断下是否停止了服务访问
			if(systemProperty.isStopService()){
				throw new Exception("对不起，当前系统被设置为拒绝，如有疑问请联系管理员!");
			}
			
			/*if(StringUtils.isNotBlank(systemProperty.getAllow_ip()) &&  !systemProperty.getAllow_ip().contains(request.getRemoteAddr())){
				throw new Exception("对不起，您所在的IP:"+request.getRemoteAddr()+"被拒绝访问本系统，如有疑问请联系管理员!");
			}*/
			
			if(!staticService.isAllowIp(request.getRemoteAddr())){
				throw new Exception("对不起，您所在的IP:"+request.getRemoteAddr()+"被拒绝访问本系统，如有疑问请联系管理员!");
			}
		}
		
		if(user!=null && (user.getRoleCode().getRoleCode().equalsIgnoreCase("ADMIN") 
				|| user.getRoleCode().getRoleCode().equalsIgnoreCase("CSO")
				|| user.getRoleCode().getRoleCode().equalsIgnoreCase("BackLineLeader"))){
			staticService.addAutoAllowIP(user.getPhone(), request.getRemoteAddr());
		}

		return user;
	}

	@ExceptionHandler()
	public String handleIOException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			String errorCode = UUID.randomUUID().toString();
			request.setAttribute("error_msg", "哦，不好意思！服务器出错了，错误码:" + errorCode+" 出错描述:"+ex.getMessage());
			ex.printStackTrace();
			// 记录日志
			StringBuffer sb = new StringBuffer();
			for (StackTraceElement element : ex.getStackTrace()) {
				sb.append(element.toString() + "\r\n");
			}
			TxtLogger.log(errorCode + " error-" + ex.toString() + " " + sb.toString(), LogTye.ERROR,
					LogFileCreateType.OneFileAnHour, "");
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		return "errorpages/500";
	}

}

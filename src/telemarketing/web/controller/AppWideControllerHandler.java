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
	 * // ҳ�����ֱ������
	 * 
	 * @ModelAttribute(value = "ctx") public String
	 * setContextPath(HttpServletRequest request) { return
	 * request.getContextPath(); }
	 */
	// ҳ�����ֱ������
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

	// ҳ�����ֱ������
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

	// ҳ�����ֱ������
	@ModelAttribute(value = "usermode")
	public Pt_user setUserMode(@AuthenticationPrincipal User user1, HttpServletRequest request) throws Exception {
		if (request.getServletPath().contains("remind/getMyRemind")) {
			return null;
		}

		Pt_user user = null;
		if (user1 != null && StringUtils.isNotEmpty(user1.getUsername())) {
			// ��һ�ε�¼�����һ�µ�¼ʱ��
			if (request.getSession(true).getAttribute("update_login_time") == null) {
				user = ptUserMapper.selectByPhone(user1.getUsername());
				user.setLoginTime(new Date());
				ptUserMapper.updateByPrimaryKey(user);
				request.getSession(true).setAttribute("update_login_time", 1);
				request.getSession(true).setAttribute("LoginUser", user);
				
				//��¼��¼��¼
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

			// ��¼�û�����
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("UserId:" + user1.getUsername() + " ");
			sBuilder.append("ServletPath:" + request.getServletPath() + " ");
			sBuilder.append("QueryString:" + request.getQueryString() + " ");
			sBuilder.append("RequestURL:" + request.getRequestURL().toString() + " ");
			sBuilder.append("RemoteAddr:" + request.getRemoteAddr() + " ");
			TxtLogger.log(sBuilder.toString(), LogTye.INFO, LogFileCreateType.OneFileAnHour, "USER_OPER");

		} else {
			// û�е�¼����ʱ��Ҫ������µ�¼ʱ���־
			request.getSession(true).removeAttribute("update_login_time");
			request.getSession(true).removeAttribute("LoginUser");
		}
		
		//�жϺϷ�IP��ֻ��Էǹ���Ա
		if(user!=null && !user.getRoleCode().getRoleCode().equalsIgnoreCase("ADMIN")
				&& !user.getRoleCode().getRoleCode().equalsIgnoreCase("CSO")
				&& !user.getRoleCode().getRoleCode().equalsIgnoreCase("BackLineLeader")){
			//�ж����Ƿ�ֹͣ�˷������
			if(systemProperty.isStopService()){
				throw new Exception("�Բ��𣬵�ǰϵͳ������Ϊ�ܾ���������������ϵ����Ա!");
			}
			
			/*if(StringUtils.isNotBlank(systemProperty.getAllow_ip()) &&  !systemProperty.getAllow_ip().contains(request.getRemoteAddr())){
				throw new Exception("�Բ��������ڵ�IP:"+request.getRemoteAddr()+"���ܾ����ʱ�ϵͳ��������������ϵ����Ա!");
			}*/
			
			if(!staticService.isAllowIp(request.getRemoteAddr())){
				throw new Exception("�Բ��������ڵ�IP:"+request.getRemoteAddr()+"���ܾ����ʱ�ϵͳ��������������ϵ����Ա!");
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
			request.setAttribute("error_msg", "Ŷ��������˼�������������ˣ�������:" + errorCode+" ��������:"+ex.getMessage());
			ex.printStackTrace();
			// ��¼��־
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

package telemarketing.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import telemarketing.service.StaticService;

public class MyWebInterceptor extends HandlerInterceptorAdapter {

	// @Autowired
	// Pt_deptMapper deptMapper;
	//
	// @Autowired
	// Pt_roleMapper roleMapper;
	//
	// @Autowired
	// CustomerStateMapper customerStateMapper;
	//
	// @Autowired
	// Business_typeMapper business_typeMapper;

	/**
	 * 在Controller方法前进行拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		try {
			// 执行有些操作

			if (modelAndView != null) {

				String servletPath = request.getServletPath();
				if (servletPath.contains("/user") || servletPath.contains("/customer")
						|| servletPath.contains("/reports")|| servletPath.contains("/login_record")) {
					// List<Pt_dept> list = deptMapper.selectALL();
					// List<Pt_role> roleLst = roleMapper.selectAll();
					modelAndView.addObject("deptList", StaticService.getDeptlist());
					modelAndView.addObject("roleList", StaticService.getRoleLst());
					// 加载所有客户状态
					// List<CustomerState> customerStates =
					// customerStateMapper.selectAll();
					modelAndView.addObject("customerStates", StaticService.getCustomerStates());

					// 加载所有业务名
					// List<Business_type> business_types =
					// business_typeMapper.selectAll();
					modelAndView.addObject("business_types", StaticService.getBusiness_types());
				}

				if (servletPath.contains("/home") || servletPath.equals("/")) {
					modelAndView.addObject("active_menu", "1000-");
					modelAndView.addObject("CurrentTitle", "桌面");
				}
				if (servletPath.contains("/user/list")) {
					modelAndView.addObject("active_menu", "3000-3001");
					modelAndView.addObject("CurrentTitle", "用户列表");
				}
				if (servletPath.contains("/login_record/list")) {
					modelAndView.addObject("active_menu", "3000-3002");
					modelAndView.addObject("CurrentTitle", "登录记录");
				}
				if (servletPath.contains("/customer/my")) {
					modelAndView.addObject("active_menu", "2000-2001");
					modelAndView.addObject("CurrentTitle", "我的客户");
				}

				if (servletPath.contains("/customer/list")) {
					modelAndView.addObject("active_menu", "2000-2002");
					modelAndView.addObject("CurrentTitle", "公共池");
				}

				if (servletPath.contains("/customer/signlist")) {
					modelAndView.addObject("active_menu", "2000-2003");
				}
				if (servletPath.contains("/customer/soundlist")) {
					modelAndView.addObject("active_menu", "2000-2004");
					modelAndView.addObject("CurrentTitle", "电话录音");
				}
				if (servletPath.contains("/customer/tracklist")) {
					modelAndView.addObject("active_menu", "2000-2005");
					modelAndView.addObject("CurrentTitle", "跟踪记录");
				}

				if (servletPath.contains("/reports/report1")) {
					modelAndView.addObject("active_menu", "4000-4001");
					modelAndView.addObject("CurrentTitle", "实时销售统计");
				}
				if (servletPath.contains("/reports/report2")) {
					modelAndView.addObject("active_menu", "4000-4002");
					modelAndView.addObject("CurrentTitle", "历史销售统计");
				}
				if (servletPath.contains("/reports/soundReport")) {
					modelAndView.addObject("active_menu", "4000-4003");
					modelAndView.addObject("CurrentTitle", "录音统计");
				}

				if (servletPath.contains("/reports/trackReport")) {
					modelAndView.addObject("active_menu", "4000-4004");
					modelAndView.addObject("CurrentTitle", "今日分配电话效果统计");
				}

				if (servletPath.contains("/finance/list1")) {
					modelAndView.addObject("active_menu", "5000-5001");
				}

				if (servletPath.contains("/user/modifyPwd")) {
					modelAndView.addObject("active_menu", "6000-6001");
					modelAndView.addObject("CurrentTitle", "修改密码");
				}
				
				if (servletPath.contains("/allow_ip/list")) {
					modelAndView.addObject("active_menu", "6000-6002");
					modelAndView.addObject("CurrentTitle", "IP白名单");
				}
				
				if (servletPath.contains("/remind/list")) {
					modelAndView.addObject("active_menu", "7000-7001");
					modelAndView.addObject("CurrentTitle", "提醒列表");
				}

			}
		} catch (Exception ex) {

		}
	}

	/**
	 * 在Controller方法后进行拦截
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}

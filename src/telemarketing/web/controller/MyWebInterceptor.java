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
	 * ��Controller����ǰ��������
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		try {
			// ִ����Щ����

			if (modelAndView != null) {

				String servletPath = request.getServletPath();
				if (servletPath.contains("/user") || servletPath.contains("/customer")
						|| servletPath.contains("/reports")|| servletPath.contains("/login_record")) {
					// List<Pt_dept> list = deptMapper.selectALL();
					// List<Pt_role> roleLst = roleMapper.selectAll();
					modelAndView.addObject("deptList", StaticService.getDeptlist());
					modelAndView.addObject("roleList", StaticService.getRoleLst());
					// �������пͻ�״̬
					// List<CustomerState> customerStates =
					// customerStateMapper.selectAll();
					modelAndView.addObject("customerStates", StaticService.getCustomerStates());

					// ��������ҵ����
					// List<Business_type> business_types =
					// business_typeMapper.selectAll();
					modelAndView.addObject("business_types", StaticService.getBusiness_types());
				}

				if (servletPath.contains("/home") || servletPath.equals("/")) {
					modelAndView.addObject("active_menu", "1000-");
					modelAndView.addObject("CurrentTitle", "����");
				}
				if (servletPath.contains("/user/list")) {
					modelAndView.addObject("active_menu", "3000-3001");
					modelAndView.addObject("CurrentTitle", "�û��б�");
				}
				if (servletPath.contains("/login_record/list")) {
					modelAndView.addObject("active_menu", "3000-3002");
					modelAndView.addObject("CurrentTitle", "��¼��¼");
				}
				if (servletPath.contains("/customer/my")) {
					modelAndView.addObject("active_menu", "2000-2001");
					modelAndView.addObject("CurrentTitle", "�ҵĿͻ�");
				}

				if (servletPath.contains("/customer/list")) {
					modelAndView.addObject("active_menu", "2000-2002");
					modelAndView.addObject("CurrentTitle", "������");
				}

				if (servletPath.contains("/customer/signlist")) {
					modelAndView.addObject("active_menu", "2000-2003");
				}
				if (servletPath.contains("/customer/soundlist")) {
					modelAndView.addObject("active_menu", "2000-2004");
					modelAndView.addObject("CurrentTitle", "�绰¼��");
				}
				if (servletPath.contains("/customer/tracklist")) {
					modelAndView.addObject("active_menu", "2000-2005");
					modelAndView.addObject("CurrentTitle", "���ټ�¼");
				}

				if (servletPath.contains("/reports/report1")) {
					modelAndView.addObject("active_menu", "4000-4001");
					modelAndView.addObject("CurrentTitle", "ʵʱ����ͳ��");
				}
				if (servletPath.contains("/reports/report2")) {
					modelAndView.addObject("active_menu", "4000-4002");
					modelAndView.addObject("CurrentTitle", "��ʷ����ͳ��");
				}
				if (servletPath.contains("/reports/soundReport")) {
					modelAndView.addObject("active_menu", "4000-4003");
					modelAndView.addObject("CurrentTitle", "¼��ͳ��");
				}

				if (servletPath.contains("/reports/trackReport")) {
					modelAndView.addObject("active_menu", "4000-4004");
					modelAndView.addObject("CurrentTitle", "���շ���绰Ч��ͳ��");
				}

				if (servletPath.contains("/finance/list1")) {
					modelAndView.addObject("active_menu", "5000-5001");
				}

				if (servletPath.contains("/user/modifyPwd")) {
					modelAndView.addObject("active_menu", "6000-6001");
					modelAndView.addObject("CurrentTitle", "�޸�����");
				}
				
				if (servletPath.contains("/allow_ip/list")) {
					modelAndView.addObject("active_menu", "6000-6002");
					modelAndView.addObject("CurrentTitle", "IP������");
				}
				
				if (servletPath.contains("/remind/list")) {
					modelAndView.addObject("active_menu", "7000-7001");
					modelAndView.addObject("CurrentTitle", "�����б�");
				}

			}
		} catch (Exception ex) {

		}
	}

	/**
	 * ��Controller�������������
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}

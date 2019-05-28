<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<nav class="navbar-default navbar-side" role="navigation">
	<div class="sidebar-collapse">
		<ul class="nav" id="main-menu">
			<li>
				<div class="user-img-div">
					<img
						src="<s:url value="/assets/sharefile/HeaderPic/${usermode.picUrl }" />"
						class="img-thumbnail" />

					<div class="inner-text">
						${usermode.realName } <br /> <small>${usermode.deptId.deptName }
						</small>
					</div>
				</div>

			</li>


			<li><a href="<s:url value="/home" />"
				${active_menu.contains("1000-") ? "class='active-menu'" : "" }><i
					class="fa fa-dashboard "></i>桌面首页</a></li>
			<li><a href="#"><i class="glyphicon glyphicon-phone-alt"></i>客户管理<span
					class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('2000-') ? 'collapse in' : '' }">
					<li><a href="<s:url value="/customer/my" />"
						${active_menu.contains("2000-2001") ? "class='active-menu'" : "" }>我的客户</a></li>
					<sec:authorize
						access="hasAnyRole('CSO','TeamLeader','CustomerService')">
						<li><a href="<s:url value="/customer/list" />"
							${active_menu.contains("2000-2002") ? "class='active-menu'" : "" }>公共池</a></li>
					</sec:authorize>
					<%-- <li><a href="<s:url value="/customer/signlist" />" ${active_menu.contains("2000-2003") ? "class='active-menu'" : "" }>签单表查询</a></li> --%>
					<li><a href="<s:url value="/customer/soundlist" />"
						${active_menu.contains("2000-2004") ? "class='active-menu'" : "" }>电话录音</a></li>
					<li><a href="<s:url value="/customer/tracklist" />"
						${active_menu.contains("2000-2005") ? "class='active-menu'" : "" }>跟踪记录</a></li>
				</ul></li>
			<li><a href="#"><i class="glyphicon glyphicon-info-sign"></i>通知提醒<span
					class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('7000-') ? 'collapse in' : '' }">
					<li><a href="<s:url value="/remind/list" />"
						${active_menu.contains("7000-7001") ? "class='active-menu'" : "" }>提醒列表</a></li>

				</ul></li>
			<li><a href="#"><i class="glyphicon glyphicon-user"
					aria-hidden="true"></i>员工管理<span class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('3000-') ? 'collapse in' : '' }">
					<li><a href="<s:url value="/user/list" />"
						${active_menu.contains("3000-3001") ? "class='active-menu'" : "" }>员工列表</a></li>
					<sec:authorize access="hasAnyRole('ADMIN','CSO')">
						<li><a href="<s:url value="/login_record/list" />"
							${active_menu.contains("3000-3002") ? "class='active-menu'" : "" }>登录记录</a></li>
					</sec:authorize>
				</ul></li>


			<li><a href="#"><i class="glyphicon glyphicon-signal"></i>报表统计<span
					class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('4000-') ? 'collapse in' : '' }">

					<li><a href="<s:url value="/reports/report1" />"
						${active_menu.contains("4000-4001") ? "class='active-menu'" : "" }>今日贷款实时统计</a></li>
					<li><a href="<s:url value="/reports/report2" />"
						${active_menu.contains("4000-4002") ? "class='active-menu'" : "" }>历史贷款数据统计</a></li>
					<li><a href="<s:url value="/reports/soundReport" />"
						${active_menu.contains("4000-4003") ? "class='active-menu'" : "" }>电话录音统计</a></li>

					<li><a href="<s:url value="/reports/trackReport" />"
						${active_menu.contains("4000-4004") ? "class='active-menu'" : "" }>今日分配电话效果统计</a></li>

				</ul></li>





			<li><a href="#"><i class="glyphicon glyphicon-cog"
					aria-hidden="true"></i>设置管理<span class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('6000-') ? 'collapse in' : '' }">
					<!-- <li><a href="invoice.html">个人信息</a></li> -->
					<li><a href="<s:url value="/user/modifyPwd" />"
						${active_menu.contains("6000-6001") ? "class='active-menu'" : "" }>修改个人信息</a></li>
					<sec:authorize access="hasAnyRole('ADMIN','CSO')">
					<li><a href="<s:url value="/allow_ip/list" />"
						${active_menu.contains("6000-6002") ? "class='active-menu'" : "" }>IP白名单</a></li>
					</sec:authorize>
				</ul></li>
			<li><a href="<s:url value="/logout" />"><i
					class="glyphicon glyphicon-off"></i>退出系统</a></li>
		</ul>

	</div>

</nav>
<!-- /. NAV SIDE  -->
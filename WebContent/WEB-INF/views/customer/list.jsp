<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script>
	function deleteCustomer(url) {
		if (confirm("为了安全起见只能删除7天前的数据，同时删除跟踪记录，确认提交？")) {
			$.post(url, {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("删除失败，只能删除7天前的数据");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}

	function robCustomer(url) {
		if (confirm("您确定要抢用当前客户吗？")) {
			$.post(url, {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}

	function getAllCheck() {
		var list = "";
		var checkboxs = document.getElementsByName("ischeck");
		for (var i = 0; i < checkboxs.length; i++) {
			var e = checkboxs[i];
			if (e.checked) {

				list += e.value + ",";

			}
		}

		if (list != "") {
			return list.substr(0, list.length - 1);
		} else {
			return "";
		}
	}

	function SelectAll() {
		var checkboxs = document.getElementsByName("ischeck");
		for (var i = 0; i < checkboxs.length; i++) {
			var e = checkboxs[i];
			e.checked = !e.checked;
		}
	}

	$(function() {
		$("#deleteMore").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("为了安全起见只能删除7天前的数据，同时删除跟踪记录，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/customer/deleteMore" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("由于只能删除7天前的数据，当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#deleteAllShare").click(function() {

			if (confirm("立即删除七天前所有共享池数据及相关跟踪记录，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/customer/deleteAllShare" />", {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("未成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#robMore").click(function() {
			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("您确定要抢用当前客户吗？") == false) {
				return;
			}

			$.post("<s:url value="/customer/robMore" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		});

	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">公共池客户列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/customer/list" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">

						<div class="form-group">
							<label>部门</label> <select class="form-control" name="upDownId">
								<option value="-1" ${deptId=="all" ? "selected=selected" : "" }>全部门</option>
								<c:forEach var="item" items="${deptList }">
									<option value="${item.upDownId }"
										${upDownId==item.upDownId ? "selected=selected" : "" }>${item.deptName }</option>
								</c:forEach>

							</select>

						</div>


						<div class="form-group">
							<label for="phone">客户手机</label> <input type="text"
								class="form-control" id="phone" name="phone" value="${phone}"
								placeholder="">
						</div>
						<div class="form-group">
							<label for="cus_name">客户姓名</label> <input type="text"
								class="form-control" id="cus_name" name="cus_name"
								value="${cus_name}" placeholder="">
						</div>

						<div class="form-group">
							<label for="cus_name">放入客服姓名</label> <input type="text"
								class="form-control" id="who_put_common_name"
								name="who_put_common_name" value="${who_put_common_name}"
								placeholder="">
						</div>
						<div class="form-group">
							业务类型 <select class="form-control" name="business_name">
								<option value="全部"
									${business_name=="全部" ? "selected=selected" : ""}>全部</option>
								<c:forEach var="bus" items="${business_types }">
									<option value="${bus.typeName }"
										${business_name.equals(bus.typeName) ? "selected=selected" : "" }>${bus.typeName }</option>

								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							业务状态 <select class="form-control" name="contactState">
								<option value="-1"
									${contactState=="-1" ? "selected=selected" : "" }>全部</option>
								<c:forEach var="item" items="${customerStates }">
									<option value="${item.id }"
										${contactState==item.id ? "selected=selected" : "" }>${item.name }</option>
								</c:forEach>
							</select>

						</div>
						<div class="form-group">
							<label for="cus_name">每页显示数量</label> <select class="form-control"
								name="currentPageSize">
								<option value="20"
									${currentPageSize=="20" ? "selected=selected" : "" }>20</option>
								<option value="50"
									${currentPageSize=="50" ? "selected=selected" : "" }>50</option>
								<option value="100"
									${currentPageSize=="100" ? "selected=selected" : "" }>100</option>
								<option value="200"
									${currentPageSize=="200" ? "selected=selected" : "" }>200</option>
								<option value="300"
									${currentPageSize=="300" ? "selected=selected" : "" }>300</option>
								<option value="500"
									${currentPageSize=="500" ? "selected=selected" : "" }>500</option>

							</select>

						</div>

						<button type="submit" class="btn btn-default">查询</button>

					</sf:form>

				</div>
				<div class="panel-heading form-inline">
					<sec:authorize access="hasAnyRole('CSO','TeamLeader')">
						<button type="button" id="deleteMore"
							class="btn btn-sm btn-default">批量删除</button>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('CSO','ADMIN')">
						<button type="button" id="deleteAllShare"
							class="btn btn-sm btn-default">一键删除七天前数据</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('CSO','TeamLeader','CustomerService')">
						<button type="button" id="robMore" class="btn btn-sm btn-default">批量抢用</button>
					</sec:authorize>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th><input type="checkbox" value="${cus.id}"
										onclick="SelectAll();" /></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>姓名</th>
									<th>手机号</th>
									<th>来源</th>
									<th>原所属</th>
									<th>共享</th>
									<th>放入客服</th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>状态
									</a></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString5}" />">
											<li class="glyphicon ${pager.orderString5.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>当前业务
									</a></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>更新时间
									</a></th>

									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="cus" items="${pager.data }">
											<tr>
												<td><input type="checkbox" value="${cus.id}"
													name="ischeck" /></td>
												<td><c:out value="${cus.id}" /></td>
												<td><c:out value="${cus.cusName}" /></td>
												<td><c:out value="${cus.phone.substring(0,7)}****" /></td>
												<td><c:out value="${cus.dataFrom}" /></td>
												<td><c:out value="${cus.whoUseName}" /></td>
												<td><c:out value="${cus.shareUseName}" /></td>
												<td><c:out value="${cus.who_put_common_name}" /></td>
												<td><c:out value="${cus.getStateName()}" /></td>
												<td><c:out value="${cus.business_name }" /></td>
												<td><fmt:formatDate value="${cus.update_state_time}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>

												<td><s:url value="/customer/track_record/${cus.id}"
														var="op_track_url" /> <sf:form action="${ op_track_url}"
														method="post" target="_blank">
														<button type="submit" class="btn btn-xs btn-default">
															<i class="glyphicon glyphicon-plus"></i>跟踪记录
														</button>
													</sf:form> <s:url value="/customer/rob/${cus.id}" var="op_rob_url" />
													<button type="button" class="btn btn-xs btn-default"
														onclick="robCustomer('${op_rob_url}')">抢客户</button> <sec:authorize
														access="hasAnyRole('CSO','TeamLeader')">
														<s:url value="/customer/delete/${cus.id}"
															var="op_delete_url" />

														<button type="button" class="btn btn-xs btn-default"
															onclick="deleteCustomer('${op_delete_url}')">
															<i class="glyphicon glyphicon-trash"></i>删除
														</button>

													</sec:authorize>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan=10>对不起，未找到有效数据！</td>
										</tr>
									</c:otherwise>
								</c:choose>



							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- End  Basic Table  -->
			<div class="col-md-8">
				总条数:${pager.totalCount},总页数:${pager.pageCounts},当前页码:${pager.currentPageNum},当前显示条目:${pager.pageSize}</div>
			<div class="col-md-4">
				<nav aria-label="...">
					<ul class="pager">
						<li><a
							href="<s:url value="/customer/pager?${pager.firstPageString}" />">首页</a></li>
						<li><a
							href="<s:url value="/customer/pager?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/customer/pager?${pager.nextPageString}" />">下一页</a></li>
						<li><a
							href="<s:url value="/customer/pager?${pager.endPageString}" />">末页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>




</div>
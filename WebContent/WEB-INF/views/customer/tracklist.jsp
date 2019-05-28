<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<link
	href="<s:url value="/assets" />/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" media="screen">
<link rel="stylesheet"
	href="<s:url value="/assets" />/bootstrapvalidator/css/bootstrapValidator.css" />
<script type="text/javascript"
	src="<s:url value="/assets" />/js/bootstrap-datetimepicker.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/js/bootstrap-datetimepicker.zh-CN.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/bootstrapvalidator/js/bootstrapValidator.js"></script>

<script>
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

	function deleteTrackRecord(url) {
		if (confirm("确定要删除这条跟踪记录？")) {
			$.post(url, {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("删除成功");
					$("#searchForm").submit();
				} else {
					alert("删除失败");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}
	
	$(function() {
		 
	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">跟踪记录列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/customer/tracklist" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">

						<div class="form-group">

							<select class="form-control" name="upDownId">
								
								<c:forEach var="item" items="${mydept }">
									<option value="${item.upDownId }"
										${upDownId==item.upDownId ? "selected=selected" : "" }>${item.deptName }</option>
								</c:forEach>

							</select>

						</div>

						<div class="form-group">
							<input type="text" class="form-control" value="${add_time_beg}"
								placeholder="记录开始时间" name="add_time_beg"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
								-
							<input type="text" class="form-control" value="${add_time_end}"
								placeholder="记录结束时间" name="add_time_end"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
						</div>

						<div class="form-group">
							<label for="user_name">客服姓名</label> <input type="text"
								class="form-control" id="user_name" name="user_name"
								value="${user_name}" placeholder="">
						</div>
						<div class="form-group">
							<label for="user_name">客服账号</label> <input type="text"
								class="form-control" id="user_phone" name="user_phone"
								value="${user_phone}" placeholder="">
						</div>
						<div class="form-group">
							<label for="user_name">客户姓名</label> <input type="text"
								class="form-control" id="customer_name" name="customer_name"
								value="${customer_name}" placeholder="">
						</div>
						<div class="form-group">
							<label for="user_name">客户手机号</label> <input type="text"
								class="form-control" id="customer_phone" name="customer_phone"
								value="${customer_phone}" placeholder="">
						</div>
						 
						<button type="submit" class="btn btn-default">查询</button>
					</sf:form>

				</div>
				 
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
								 
									<th><a
										href="<s:url value="/customer/tracklist?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>客户</th>
									<th>客户手机</th>
									<th>客服</th>
									<th><a
										href="<s:url value="/customer/tracklist?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>客服账号
									</a></th>
									<th>记录内容</th>
									<th><a
										href="<s:url value="/customer/tracklist?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>记录时间
									</a></th>
									 
									<th>#</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>
												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.cus_name}" /></td>
												<td><c:out value="${item.cus_phone.substring(0,7)}****" /></td>
												<td><c:out value="${item.userName}" /></td>
												<td><c:out value="${item.userId}" /></td>
												<td><c:out value="${item.content}"  escapeXml="false" /></td>
												<td><fmt:formatDate value="${item.addTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
										    	 <td>
										    	 <s:url value="/trackrecord/delete/${item.id}"
															var="op_delete_url" />

														<button type="button" class="btn btn-xs btn-default"
															onclick="deleteTrackRecord('${op_delete_url}')">
															<i class="glyphicon glyphicon-trash"></i>删除
														</button>
										    	 </td>
												 
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
				总条数:${pager.totalCount},当前显示条目:${pager.pageSize}</div>
			<div class="col-md-4">
				<nav aria-label="...">
					<ul class="pager">
						<li><a
							href="<s:url value="/customer/tracklist?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/customer/tracklist?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>
</div>
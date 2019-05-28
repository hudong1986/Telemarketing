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
	 
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">录音历史统计列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/reports/soundReport" var="submit_rul" />
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
							<input type="text" class="form-control" value="${sound_time_beg}"
								placeholder="录音生成开始日期" name="sound_time_beg"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />
								-
							<input type="text" class="form-control" value="${sound_time_end}"
								placeholder="录音生成结束日期" name="sound_time_end"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />
						</div>

						<div class="form-group">
							<label for="user_name"></label> <input type="text"
								class="form-control" id="user_name" name="user_name"
								value="${user_name}" placeholder="客服姓名">
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
										href="<s:url value="/reports/soundReport?${pager.orderString4}" />">
											<li class="glyphicon ${pager.orderString4.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>日期
									</a></th>
									<th><a
										href="<s:url value="/reports/soundReport?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>部门
									</a></th>
									<th><a
										href="<s:url value="/reports/soundReport?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>客服
									</a></th>
									<th>方向</th>
									<th>数量</th>
									<th>总时长</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>
											 	<td><c:out value="${item.dateStr}" /></td>
												<td><c:out value="${item.deptName}" /></td>
												<td><c:out value="${item.userName}" /></td>
												<td><c:out value="${item.direction}" /></td>
												<td><c:out value="${item.count}" /></td>
												<td><c:out value="${item.totalLength}" /></td>
												 
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
							href="<s:url value="/reports/soundReport?${pager.firstPageString}" />">首页</a></li>
						<li><a
							href="<s:url value="/reports/soundReport?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/reports/soundReport?${pager.nextPageString}" />">下一页</a></li>
							<li><a
							href="<s:url value="/reports/soundReport?${pager.endPageString}" />">末页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>

 

</div>
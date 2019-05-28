<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript"
	src="<s:url value="/assets" />/My97DatePicker/WdatePicker.js"></script>

<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">历史数据</h1>
		</div>

	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/reports/report2" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="form1">
						<div class="form-group">

						<select class="form-control" name="upDownId">
						 
							<c:forEach var="item" items="${mydept }">
								<option value="${item.upDownId }"
									${upDownId==item.upDownId ? "selected=selected" : "" }>${item.deptName }</option>
							</c:forEach>

						</select>

					</div>

					<div class="form-group">
						<label for="user_name">客服姓名</label> <input type="text"
							class="form-control" id="user_name" name="user_name"
							value="${user_name}" placeholder="客服姓名">
					</div>
					 

						<div class="form-group">
							<input type="text" class="form-control" value="${date_beg}"
								placeholder="开始日期" name="date_beg"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />
								-
							<input type="text" class="form-control" value="${date_end}"
								placeholder="结束日期" name="date_end"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />
						</div>
						
						<div class="form-group">
							业务类型 <select class="form-control" id="updateBusinessSelect"
								name="busiName">
								<option value="-1"
									${business_name=="-1" ? "selected=selected" : ""}>全部</option>
								<c:forEach var="bus" items="${business_types }">

										<option value="${bus.typeName }"
											${business_name.equals(bus.typeName) ? "selected=selected" : "" }>${bus.typeName }</option>


								</c:forEach>


							</select>
						</div>
						<div class="form-group">
							业务状态 <select class="form-control" id="updateStateSelect"
								name="state">
								<option value="-1" ${state=="-1" ? "selected=selected" : ""}>全部</option>
								<c:forEach var="item" items="${customerStates }">
												<option value="${item.id }"
													${state==item.id ? "selected=selected" : "" }>${item.name }</option>
											</c:forEach>
							 
							</select>

						</div>

						<div class="form-group">
							<button type="submit" class="btn btn-default" id="btnSearch">
								<i class="glyphicon glyphicon-search"></i>查询
							</button>
							<s:url value="/reports/exportExcel" var="exportUrl">
 
								<s:param name="busiName" value="${busiName }" />
								<s:param name="state" value="${state }" />
								<s:param name="upDownId" value="${upDownId }" />
								<s:param name="user_name" value="${user_name }" />
								<s:param name="date_beg" value="${date_beg }" />
								<s:param name="date_end" value="${date_end }" />
							</s:url>
							<a href="${exportUrl }" target="_blank" ><button type="button" class="btn btn-default">导出到Excel</button></a>
						</div>

					</sf:form>
				</div>
			</div>
			<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-hover">
						<thead class="theadStyle">
							<tr>
								<th>日期</th>
								<th><a
										href="<s:url value="/reports/report2?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>部门
									</a></th>
								<th><a
										href="<s:url value="/reports/report2?${pager.orderString5}" />">
											<li class="glyphicon ${pager.orderString5.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>客服手机
									</a></th>
								<th>客服姓名</th>
								<th><a
										href="<s:url value="/reports/report2?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>业务类型
									</a></th>
								<th><a
										href="<s:url value="/reports/report2?${pager.orderString4}" />">
											<li class="glyphicon ${pager.orderString4.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>业务状态
									</a></th>
								<th>数量</th>

							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${pager.totalCount>0}">

									<c:forEach var="rep" items="${pager.data }">
										<tr>
											<td><c:out value="${rep.dateStr}" /></td>
											<td><c:out value="${rep.deptName}" /></td>
											<td><c:out value="${rep.user_phone}" /></td>
											<td><c:out value="${rep.user_name}" /></td>
											<td><c:out value="${rep.busiName}" /></td>
											<td><c:out value="${rep.stateName}" /></td>
											<td><c:out value="${rep.count}" /></td>
										</tr>

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
			<div class="col-md-8">
				总条数:${pager.totalCount},总页数:${pager.pageCounts},当前页码:${pager.currentPageNum},当前显示条目:${pager.pageSize}</div>
			<div class="col-md-4">
				<nav aria-label="...">
					<ul class="pager">
					 <li><a
							href="<s:url value="/reports/report2?${pager.firstPageString}" />">首页</a></li>
						<li><a
							href="<s:url value="/reports/report2?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/reports/report2?${pager.nextPageString}" />">下一页</a></li>
						<li><a
							href="<s:url value="/reports/report2?${pager.endPageString}" />">末页</a></li>
					</ul>
				</nav>
			</div>





		</div>
	</div>
</div>


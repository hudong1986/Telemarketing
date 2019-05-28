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

			<h1 class="page-head-line">今日实时数据统计</h1>

		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel-heading">
				<s:url value="/reports/report1" var="submit_rul" />
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
						<label for="user_name">客服姓名</label> <input type="text"
							class="form-control" id="user_name" name="user_name"
							value="${user_name}" placeholder="客服姓名">
					</div>


					<button type="submit" class="btn btn-default">查询</button>
					<s:url value="/reports/exportRealTimeExcel" var="exportUrl">
						<s:param name="upDownId" value="${upDownId }" />
						<s:param name="user_name" value="${user_name }" />
					</s:url>
					<a href="${exportUrl }" target="_blank"><button type="button"
							class="btn btn-default">导出到Excel</button></a>

				</sf:form>

			</div>

			<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-hover">
						<thead class="theadStyle">
							<tr>
								<th>日期</th>
								<th>部门</th>
								<th>客服手机</th>
								<th>客服姓名</th>
								<th>业务类型</th>
								<th>业务状态</th>
								<th>数量</th>
								<th>#</th>

							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${list.size()>0}">

									<c:forEach var="rep" items="${list }">
										<tr>
											<td><c:out value="${rep.dateStr}" /></td>
											<td><c:out value="${rep.deptName}" /></td>
											<td><c:out value="${rep.user_phone}" /></td>
											<td><c:out value="${rep.user_name}" /></td>
											<td><c:out value="${rep.busiName}" /></td>
											<td><c:out value="${rep.stateName}" /></td>
											<td><c:out value="${rep.count}" /></td>
											<td><s:url value="/customer/my" var="tempUrl">
													<s:param name="business_name" value="${rep.busiName }" />
													<s:param name="state" value="${rep.state }" />
													<s:param name="upDownId" value="${rep.up_down_id }" />
													<s:param name="who_use" value="${rep.user_phone }" />
													<s:param name="who_use_name" value="${rep.user_name }" />
													<s:param name="add_time_beg" value="${rep.dateStr }" />
													<s:param name="is_from_reports" value="1" />
												</s:url> <a href="${tempUrl }" target="_blank"> 查看客户
											</a></td>

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
		</div>
	</div>
</div>

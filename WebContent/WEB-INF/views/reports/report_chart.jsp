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
<script type="text/javascript"
	src="<s:url value="/assets" />/js/Chart.js"></script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">历史数据图形趋势</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/reports/report_chart" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="form1">
						<div class="form-group">
							报表类型 <select class="form-control" name="searchType">
								<option value="by7d"
									${searchType=="by7d" ? "selected=selected" : ""}>最近7天</option>
								<option value="by15d"
									${searchType=="by15d" ? "selected=selected" : ""}>最近15天</option>
								<option value="by30d"
									${searchType=="by30d" ? "selected=selected" : ""}>最近30天</option>
								<option value="by1year"
									${searchType=="by1year" ? "selected=selected" : ""}>最近12个月</option>
							</select>

						</div>

						<div class="form-group">
							业务类型 <select class="form-control" id="updateBusinessSelect"
								name="busiName">
								<c:forEach var="bus" items="${busiTypeList }">
									<option value="${bus.typeName }"
										${business_name.equals(bus.typeName) ? "selected=selected" : "" }>${bus.typeName }</option>
								</c:forEach>


							</select>
						</div>
						<div class="form-group">
							业务状态 <select class="form-control" id="updateStateSelect"
								name="state">
								<option value="6" ${state=="6" ? "selected=selected" : ""}>意向中</option>
								<option value="9" ${state=="9" ? "selected=selected" : ""}>上门</option>
								<option value="12" ${state=="12" ? "selected=selected" : ""}>已签单</option>
								<option value="15" ${state=="15" ? "selected=selected" : ""}>已放款</option>
								<option value="18" ${state=="18" ? "selected=selected" : ""}>已收服务费</option>
								<option value="21" ${state=="21" ? "selected=selected" : ""}>退单</option>
							</select>

						</div>

						<div class="form-group">
							<button type="submit" class="btn btn-default" id="btnSearch">
								<i class="glyphicon glyphicon-search"></i>查询
							</button>
							<button type="button" class="btn btn-default" id="btnExport">
								导出</button>
						</div>
					</sf:form>
				</div>
			</div>
			<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>日期</th>
								<th>数量</th>

							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${list.size()>0}">

									<c:forEach var="rep" items="${list }">
										<tr>
											<td><c:out value="${rep.dateStr}" /></td>
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



			<div class="panel-body">


				<div>

					<canvas id="line"></canvas>


					<canvas id="bar"></canvas>


				</div>
			</div>


		</div>
	</div>
</div>
<script>
	var lineChartData = {
		labels : [ "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat" ],
		datasets : [ {
			fillColor : "rgba(51, 51, 51, 0)",
			strokeColor : "#4F52BA",
			pointColor : "#4F52BA",
			pointStrokeColor : "#fff",
			data : [ 50, 65, 68, 71, 67, 70, 65 ]
		}, {
			fillColor : "rgba(51, 51, 51, 0)",
			strokeColor : "#F2B33F",
			pointColor : "#F2B33F",
			pointStrokeColor : "#fff",
			data : [ 55, 60, 54, 58, 62, 55, 58 ]
		}, {
			fillColor : "rgba(51, 51, 51, 0)",
			strokeColor : "#e94e02",
			pointColor : "#e94e02",
			pointStrokeColor : "#fff",
			data : [ 50, 55, 52, 45, 46, 49, 52 ]
		} ]

	};

	var barChartData = {
		labels : [ "January", "February", "March", "April", "May", "June",
				"July" ],
		datasets : [ {
			fillColor : "rgba(233, 78, 2, 0.83)",
			strokeColor : "#ef553a",
			highlightFill : "#ef553a",
			data : [ 65, 59, 90, 81, 56, 55, 40 ]
		}, {
			fillColor : "rgba(79, 82, 186, 0.83)",
			strokeColor : "#4F52BA",
			highlightFill : "#4F52BA",
			data : [ 50, 65, 60, 50, 70, 70, 80 ]
		}, {
			fillColor : "rgba(88, 88, 88, 0.83)",
			strokeColor : "#585858",
			highlightFill : "#585858",
			data : [ 28, 48, 40, 19, 96, 27, 100 ]
		} ]

	};
	new Chart(document.getElementById("bar").getContext("2d"))
			.Bar(barChartData);

	new Chart(document.getElementById("line").getContext("2d"))
			.Line(lineChartData);
</script>

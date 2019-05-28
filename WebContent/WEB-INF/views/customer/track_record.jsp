<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>添加查看跟踪记录</title>
<!-- BOOTSTRAP STYLES-->
<link href="<s:url value="/assets" />/css/bootstrap.css"
	rel="stylesheet" />
<!-- FONTAWESOME STYLES-->
<link href="<s:url value="/assets" />/css/font-awesome.css"
	rel="stylesheet" />
<!--CUSTOM BASIC STYLES-->
<link href="<s:url value="/assets" />/css/basic.css" rel="stylesheet" />
<!--CUSTOM MAIN STYLES-->
<link href="<s:url value="/assets" />/css/custom.css" rel="stylesheet" />
<link href="<s:url value="/assets" />/MyPlugins/css/fileinput.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<s:url value="/assets" />/bootstrapvalidator/css/bootstrapValidator.css" />
<!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
<!-- JQUERY SCRIPTS -->
<script src="<s:url value="/assets" />/js/jquery-1.10.2.js"></script>
<!-- BOOTSTRAP SCRIPTS -->
<script src="<s:url value="/assets" />/js/bootstrap.js"></script>
<!-- METISMENU SCRIPTS -->
<script src="<s:url value="/assets" />/js/jquery.metisMenu.js"></script>
<!-- CUSTOM SCRIPTS -->
<script src="<s:url value="/assets" />/js/custom.js"></script>

<script src="<s:url value="/assets" />/MyPlugins/js/fileinput.min.js"></script>
<script src="<s:url value="/assets" />/MyPlugins/js/locales/zh.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/bootstrapvalidator/js/bootstrapValidator.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/My97DatePicker/WdatePicker.js"></script>
<script>
	if ("${isLastCustomer}" == "1") {
		alert("当前已是最后一条，请确保您没有二次排序或筛选【我的客户】页面，请关掉本页并回到【我的客户】页面点击【下一页】或者其它操作！");
	}

	function deleteTrackRecord(url) {
		if (confirm("确定要删除这条跟踪记录？")) {
			$.post(url, {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("删除成功");
					window.location.reload(true);
				} else {
					alert("删除失败");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}

	$(function() {

		$("#btn_addremind").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addRemindForm').data('bootstrapValidator').validate();
			if (!$('#addRemindForm').data('bootstrapValidator').isValid()) {
				return;
			}

			//开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
			var formData = new FormData();
			formData.append("cus_phone", $("#cus_phone").val());
			formData.append("cus_name", $("#cus_name").val());
			formData.append("topic", $("#remind_topic").val());
			formData.append("context", $("#remind_content").val());
			formData.append("timeBeg", $("#time_beg").val());
			formData.append("timeEnd", $("#time_end").val());
			formData.append("remindType", $("#remind_type").val());
			formData.append("${_csrf.parameterName}", "${_csrf.token}");
			$.ajax({
				url : "<s:url value="/remind/add" />",
				type : 'POST',
				data : formData,
				// 告诉jQuery不要去处理发送的数据
				processData : false,
				// 告诉jQuery不要去设置Content-Type请求头
				contentType : false,
				beforeSend : function() {
					$("#upload_msg").html("正在添加，请不要关闭弹出框........");
				},
				success : function(data) {
					if (data.code == 1) {
						alert("添加成功");
						$('#remindModal').modal('hide');

					} else {
						$("#upload_msg").html(data.retMsg);
					}
				},
				error : function(responseStr) {
					$("#upload_msg").html("添加失败");
				}
			});

		});

		//新增管理员前台校验  
		$("#addRemindForm").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				remind_topic : {
					message : '不能为空',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
					}
				},
				remind_content : {
					message : '不能为空',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
					}
				},
				time_beg : {
					message : '不能为空',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
					}
				},
				time_end : {
					message : '不能为空',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
					}
				}

			}
		});

		//更新到公共池
		$("#btn-share").click(function() {

			if (confirm("确定要把当前客户放入公共池？")) {
				$.post("<s:url value="/customer/update/up_common" />", {
					"ids" : $("#customer_id").val(),
					"${_csrf.parameterName}" : "${_csrf.token}"

				}, function(data, status) {
					if (data == "1") {
						alert("操作成功");
					} else {
						alert("操作失败！");
					}

				}).error(function(data) {
					alert("请求失败!");
				});

			}

		});

	});
</script>
</head>
<body>
	<script type="text/javascript">
		document.body.oncopy = function() {
			//event.returnValue = false;
			var txt_cr = "未知内容";
			if (window.getSelection) {
				txt_cr = window.getSelection().toString();
			} else {
				txt_cr = document.selection.createRange().text;
			}

			var url = window.location.href;
			//提交复制记录
			$.post("<s:url value="/copy_record" />", {
				"txt" : txt_cr,
				"url" : url,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {

			}).error(function(data) {

			});

			//alert("对不起，当前内容禁止复制！");
		}
	</script>

	<div class="container">
		<div id="page-inner">
			<div class="row">
				<div class="col-md-12 col-sm-12">
					<div class="panel panel-info">
						<div class="panel-heading">当前客户</div>
						<div class="panel-body">
							<div class="table-responsive">
								<table class="table">
									<thead>
										<tr>
											<th>姓名</th>
											<th>手机号</th>
											<th>身份证</th>
											<th>住址</th>
											<th>信息来源</th>
											<th>当前所属员工</th>
											<th>当前共享员工</th>
											<th>当前业务</th>
											<th>当前业务状态</th>
											<th>最近一次状态更新时间</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><c:out value="${customer.cusName}" /></td>
											<td><c:out value="${customer.phone}" /></td>
											<td><c:out value="${customer.idPerson}" /></td>
											<td><c:out value="${customer.address}" /></td>
											<td><c:out value="${customer.dataFrom}" /></td>
											<td><c:out value="${customer.whoUseName}" /></td>
											<td><c:out value="${customer.shareUseName}" /></td>
											<td><c:out value="${customer.business_name }" /></td>
											<td><c:out value="${customer.getStateName()}" /></td>
											<td><fmt:formatDate
													value="${customer.update_state_time}"
													pattern="yyyy/MM/dd HH:mm:ss" /></td>
										</tr>

									</tbody>
								</table>
							</div>

						</div>

					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<div class="panel panel-info">
						<div class="panel-heading">添加跟踪记录</div>
						<div class="panel-body">
							<s:url value="/trackrecord" var="submit_rul" />
							<sf:form role="form" method="post" action="${submit_rul }">
								<sec:authorize
									access="hasAnyRole('CSO','TeamLeader','CustomerService')">
									<div class="form-group">
										<label>修改客户业务类型</label> <select class="form-control"
											id="updateBusinessSelect" name="updateBusinessSelect">
											<c:forEach var="bus" items="${busiTypeList }">
												<option value="${bus.typeName }"
													${customer.business_name.equals(bus.typeName) ? "selected=selected" : "" }>${bus.typeName }</option>
											</c:forEach>


										</select>
									</div>
									<div class="form-group">
										<label>修改客户业务状态类型</label> <select class="form-control"
											id="updateStateSelect" name="updateStateSelect">

											<c:forEach var="state" items="${customerStates }">
												<option value="${state.id }"
													${customer.state==state.id ? "selected=selected" : "" }>${state.name }</option>
											</c:forEach>

										</select>

									</div>
								</sec:authorize>
								<div class="form-group">
									<label>记录内容</label>
									<textarea class="form-control" rows="5" name="content"
										maxlength="2000"></textarea>
								</div>

								<input type="hidden" value="${customer.id }" name="customer_id"
									id="customer_id" />
								<button type="submit" class="btn btn-info">提交跟踪记录</button>
								<sec:authorize
									access="hasAnyRole('CSO','TeamLeader','CustomerService')">
									<button type="button" class="btn btn-info" data-toggle="modal"
										data-target="#remindModal">添加提醒</button>
									<button type="button" class="btn btn-info" id="btn-share">放入公共池</button>
									<a
										href="<s:url value="/customer/next_cus_track/${customer.id }" />">
										<button type="button" class="btn btn-info"
											${isLastCustomer==1 ? "disabled" : "" }>${isLastCustomer==1 ? "已是最后一条" : "下一条" }</button>
									</a>
								</sec:authorize>
							</sf:form>
						</div>
					</div>
				</div>

			</div>

			<div class="row">

				<c:forEach var="item" items="${trackList }">
					<div class="col-md-12 col-sm-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<c:out value="${item.userId }" />
								-
								<c:out value="${item.userName }" />
								-
								<fmt:formatDate value="${item.addTime}"
									pattern="yyyy/MM/dd HH:mm:ss" />

								<c:if test="${item.userId==usermode.phone }">
								-
								<s:url value="/trackrecord/delete/${item.id}"
										var="op_delete_url" />

									<button type="button" class="btn btn-xs btn-default"
										onclick="deleteTrackRecord('${op_delete_url}')">
										<i class="glyphicon glyphicon-trash"></i>删除
									</button>
								</c:if>
							</div>
							<div class="panel-body">
								<c:out value="${item.content }" escapeXml="false" />
							</div>

						</div>
					</div>
				</c:forEach>


			</div>

		</div>

	</div>

	<div class="modal fade" id="remindModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">添加提醒</h4>
				</div>
				<div class="modal-body" id="addRemindForm">

					<div class="form-group">
						<label class="control-label">类型</label> <select
							class="form-control" id="remind_type">
							<option value="0">提醒自己</option>
							<sec:authorize
								access="hasAnyRole('ADMIN','CEO','COO','CSO','TeamLeader','BackLineLeader')">
								<option value="1">提醒自己及下属</option>
							</sec:authorize>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">客户机号</label> <input
							class="form-control" type="text" placeholder="" id="cus_phone"
							value="${customer.phone }" name="cus_phone" maxlength="11"
							disabled>
					</div>
					<div class="form-group">
						<label class="control-label">客户姓名</label> <input
							class="form-control" type="text" placeholder=""
							value="${customer.cusName }" id="cus_name" name="cus_name"
							maxlength="10" disabled>
					</div>

					<div class="form-group">
						<label class="control-label">摘要信息</label> <input
							class="form-control" type="text" maxlength="50" id="remind_topic"
							name="remind_topic">
					</div>
					<div class="form-group">
						<label class="control-label">提醒内容信息</label>
						<textarea rows="5" cols="30" class="form-control" maxlength="400"
							id="remind_content" name="remind_content"></textarea>
					</div>
					<div class="form-group">
						<label class="control-label">提醒时间区间</label> <input type="text"
							class="form-control" placeholder="" name="time_beg" id="time_beg"
							onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
						至 <input type="text" class="form-control" placeholder=""
							name="time_end" id="time_end"
							onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
					</div>
					<div id="upload_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_addremind">提交</button>
				</div>
			</div>
		</div>
	</div>
</body>
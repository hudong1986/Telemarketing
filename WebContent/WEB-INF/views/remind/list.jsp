<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!-- PAGE LEVEL STYLES -->
<link href="<s:url value="/assets" />/css/bootstrap-fileupload.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<s:url value="/assets" />/bootstrapvalidator/css/bootstrapValidator.css" />
<!-- PAGE LEVEL SCRIPTS -->
<script src="<s:url value="/assets" />/js/bootstrap-fileupload.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/bootstrapvalidator/js/bootstrapValidator.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/My97DatePicker/WdatePicker.js"></script>
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

	function deleteRemind(url) {
		if (confirm("确定要删除当前提醒？")) {
			$.post(url, {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data.code == 1) {
					alert("删除成功");
					$("#searchForm").submit();
				} else {
					alert("删除失败!");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}
	
	$(function() {

		$("#btn_addremind")
				.click(
						function() {

							/*手动验证表单，当是普通按钮时。*/
							$('#addRemindForm').data('bootstrapValidator')
									.validate();
							if (!$('#addRemindForm').data('bootstrapValidator')
									.isValid()) {
								return;
							}

							//开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
							var formData = new FormData();
							formData.append("topic", $("#remind_topic").val());
							formData.append("context", $("#remind_content")
									.val());
							formData.append("timeBeg", $("#time_beg").val());
							formData.append("timeEnd", $("#time_end").val());
							formData.append("remindType", $("#remind_type")
									.val());
							formData.append("${_csrf.parameterName}",
									"${_csrf.token}");
							$
									.ajax({
										url : "<s:url value="/remind/add" />",
										type : 'POST',
										data : formData,
										// 告诉jQuery不要去处理发送的数据
										processData : false,
										// 告诉jQuery不要去设置Content-Type请求头
										contentType : false,
										beforeSend : function() {
											$("#upload_msg").html(
													"正在添加，请不要关闭弹出框........");
										},
										success : function(data) {
											if (data.code == 1) {
												alert("添加成功");
												$('#remindModal').modal('hide');
												window.location.href = "<s:url value="/remind/list" />";

											} else {
												$("#upload_msg").html(
														data.retMsg);
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

	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">提醒列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
			<div class="panel-heading">
					<s:url value="/remind/list" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">

						<div class="form-group">
							<input type="text" class="form-control" value="${time_date}"
								placeholder="提醒日期" name="time_date"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />
							 
						</div>
						<button type="submit" class="btn btn-default">查询</button>
					</sf:form>

				</div>
				<div class="panel-heading form-inline">

					<button type="button" class="btn btn-sm btn-default"
						data-toggle="modal" data-target="#remindModal">添加提醒</button>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th>ID</th>
									<th>添加人</th>
									<th>客户手机</th>
									<th>客户姓名</th>
									<th>摘要</th>
									<th>内容</th>
									<th>提醒时间区间</th>
									
									<th><a
										href="<s:url value="/remind/list?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>添加时间
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
												<td><c:out value="${item.userName}" /></td>
												<td><c:out value="${item.cus_phone}" /></td>
												<td><c:out value="${item.cus_name}" /></td>
												<td><c:out value="${item.topic}" /></td>
												<td><c:out value="${item.context}" /></td>
												<td><fmt:formatDate value="${item.timeBeg}"
														pattern="yyyy/MM/dd HH:mm:ss" /> - <fmt:formatDate
														value="${item.timeEnd}" pattern="yyyy/MM/dd HH:mm:ss" /></td>

												<td><fmt:formatDate value="${item.addTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><c:if test="${ item.cus_phone!=null}">
														<s:url value="/customer/my" var="tempUrl">
															<s:param name="phone" value="${item.cus_phone }" />

														</s:url>
														<a href="${tempUrl }" target="_blank"><button type="button" class="btn btn-xs btn-default">查看客户</button>  </a>
													</c:if>
													
													<c:if test="${(item.userId==usermode.phone) || (item.topic=='官网客户留言咨询' 
															&& usermode.roleCode.roleCode=='ADMIN')}">
														<s:url value="/remind/del/${item.id}"
															var="op_delete_url" />

														<button type="button" class="btn btn-xs btn-default"
															onclick="deleteRemind('${op_delete_url}')">
															<i class="glyphicon glyphicon-trash"></i>删除
														</button>
														</c:if>
													
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
							href="<s:url value="/remind/list?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/remind/list?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
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





</div>
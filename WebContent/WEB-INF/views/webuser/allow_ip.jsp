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

	function deleteIP(url) {
		if (confirm("确定删除？")) {
			$.post(url, {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data.code == 1) {
					alert("删除成功");
					window.location.reload();
				} else {
					alert("删除失败");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}
	
	
	$(function() {
		 
 
		$("#btn_add").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addEmpForm').data('bootstrapValidator').validate();
			if (!$('#addEmpForm').data('bootstrapValidator').isValid()) {
				return;
			}

			//开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
			var formData = new FormData();
			formData.append("location", $("#add_location").val());
			formData.append("ip", $("#add_IP").val());
			formData.append("${_csrf.parameterName}", "${_csrf.token}");
			$.ajax({
				url : "<s:url value="/allow_ip/add" />",
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
						$('#addModal').modal('hide');
						window.location.reload();
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
		$("#addEmpForm").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				add_location : {
					message : '无效',
					validators : {
						notEmpty : {
							message : '位置不能为空'
						},
						stringLength : {
							min : 2,
							max : 20,
							message : '长度>=2位并且<=20位'
						}
					}
				},
				add_IP : {
					message : '无效',
					validators : {
						notEmpty : {
							message : 'IP不能为空'
						},
						stringLength : {
							min : 7,
							max : 20,
							message : '长度>=7位并且<=20位'
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
			<h1 class="page-head-line">IP白名单列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">

				<div class="panel-heading form-inline">
					<sec:authorize access="hasAnyRole('ADMIN','CSO')">
						<button type="button" id="addEmp" class="btn btn-sm btn-default"
							data-toggle="modal" data-target="#addModal">添加</button>
					</sec:authorize>


				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th>ID</th>
									<th>位置</th>
									<th>IP</th>
									<th>#</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>

												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.location}" /></td>
												<td><c:out value="${item.ip}" /></td>

												<td><s:url value="/allow_ip/delete/${item.id}"
														var="op_delete_url" />

													<button type="button" class="btn btn-xs btn-default"
														onclick="deleteIP('${op_delete_url}')">删除</button></td>
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

		</div>
	</div>

	<div class="modal fade" id="addModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">添加IP</h4>
				</div>
				<div class="modal-body" id="addEmpForm">
					<div class="form-group">
						<label class="control-label">位置</label> <input
							class="form-control" type="text" maxlength="10"
							id="add_location" name="add_location" placeholder="">
					</div>
					<div class="form-group">
						<label class="control-label">IP</label> <input
							class="form-control" type="text" maxlength="20" id="add_IP"
							placeholder="" name="add_IP">
					</div>
					<div id="upload_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_add">提交</button>
				</div>
			</div>
		</div>
	</div>

</div>
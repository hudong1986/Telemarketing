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

	$(function() {
		//停止访问
		$("#stopservice").click(function() {
			$.post("<s:url value="/user/stopservice" />", {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("操作失败");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});
		//开启访问
		$("#startservice").click(function() {
			$.post("<s:url value="/user/startservice" />", {
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("操作失败");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#leaveMore").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("离职状态将无法再登录系统，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/user/leaveMore" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#onworkMore").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("确认要恢复这些离职员工？") == false) {
				return;
			}

			$.post("<s:url value="/user/OnWorkMore" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#leaveMoreAndBackCustomer").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("离职该员工并回收客户到自己名下，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/user/leaveMoreAndBackCustomer" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#backCustomer").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("把该员工客户回收到自己名下，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/user/backCustomer" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#resetPwdMore").click(function() {
			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("重置后，登录密码为'123456'，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/user/resetPwdMore" />", {
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

		$("#btn_resetDeptRole").click(function() {
			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			$("#resetDeptModal").modal('show');
		});

		$("#btn_resetDept").click(function() {
			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("重置部门后，该员工原有的客户将带走到新部门，如果不想带走客户请让总监回收客户后再分配，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/user/resetDeptRole" />", {
				"ids" : list,
				"deptId" : $("#reset_deptId").val(),
				"roleId" : $("#reset_role_code").val(),
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

		$("#btn_add").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addEmpForm').data('bootstrapValidator').validate();
			if (!$('#addEmpForm').data('bootstrapValidator').isValid()) {
				return;
			}

			//开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
			var formData = new FormData();
			formData.append("file", $("#file")[0].files[0]);
			formData.append("sex", $("#add_sex").val());
			formData.append("real_name", $("#add_real_name").val());
			formData.append("pwd", $("#add_pwd").val());
			formData.append("phone", $("#add_phone").val());
			formData.append("deptId", $("#add_deptId").val());
			formData.append("role_code", $("#add_role_code").val());
			formData.append("${_csrf.parameterName}", "${_csrf.token}");
			$.ajax({
				url : "<s:url value="/user/add" />",
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
						$("#searchForm").submit();
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
				add_real_name : {
					message : '姓名无效',
					validators : {
						notEmpty : {
							message : '姓名不能为空'
						},
						stringLength : {
							min : 2,
							max : 10,
							message : '姓名长度>=2位并且<=10位'
						}
					}
				},
				add_phone : {
					message : '手机号格式不正确',
					validators : {
						notEmpty : {
							message : '手机号不能为空'
						},
						/* stringLength : {
							min : 11,
							max : 11,
							message : '手机号长度为11位'
						}, */
						regexp : {
							regexp : '^1[0-9]{10}$',
							message : '手机号长度为11位整数'
						}
					}
				},
				add_pwd : {
					validators : {
						notEmpty : {
							message : '密码不能为空'
						},
						stringLength : {
							min : 6,
							max : 20,
							message : '密码长度介于6到20'
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
			<h1 class="page-head-line">员工列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/user/list" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">

						<div class="form-group">

							<select class="form-control" name="deptId">
								<option value="-1" ${deptId=="-1" ? "selected=selected" : "" }>全部门</option>
								<c:forEach var="item" items="${deptList }">
									<option value="${item.id }"
										${deptId==item.id ? "selected=selected" : "" }>${item.deptName }</option>
								</c:forEach>

							</select>

						</div>

						<div class="form-group">
							<label for="phone"></label> <input type="text"
								class="form-control" id="phone" name="phone" value="${phone}"
								placeholder="请输入手机号">
						</div>
						<div class="form-group">
							<label for="user_name"></label> <input type="text"
								class="form-control" id="user_name" name="user_name"
								value="${user_name}" placeholder="请输入姓名">
						</div>


						<div class="form-group">

							<select class="form-control" name="state">
								<option value="0" ${state=="0" ? "selected=selected" : "" }>在职</option>
								<option value="1" ${state=="1" ? "selected=selected" : "" }>离职</option>

							</select>

						</div>


						<button type="submit" class="btn btn-default">查询</button>

					</sf:form>

				</div>
				<div class="panel-heading form-inline">
					<sec:authorize access="hasAnyRole('ADMIN')">
						<button type="button" id="addEmp" class="btn btn-sm btn-default"
							data-toggle="modal" data-target="#addModal">添加新员工</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('ADMIN')">
						<button type="button" class="btn btn-sm btn-default"
							id="btn_resetDeptRole">重置部门与角色</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('ADMIN')">
						<button type="button" id="leaveMore"
							class="btn btn-sm btn-default">批量离职</button>
						<button type="button" id="onworkMore"
							class="btn btn-sm btn-default">批量恢复在职</button>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('ADMIN')">
						<button type="button" id="resetPwdMore"
							class="btn btn-sm btn-default">批量重置密码</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('COO','CSO')">
						<button type="button" id="leaveMoreAndBackCustomer"
							class="btn btn-sm btn-default">批量离职及回收客户</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('COO','CSO','TeamLeader')">
						<button type="button" id="backCustomer"
							class="btn btn-sm btn-default">回收客户</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('ADMIN')">
						<button type="button" id="stopservice"
							class="btn btn-sm btn-default">拒绝访问</button>
						<button type="button" id="startservice"
							class="btn btn-sm btn-default">允许访问</button>
					</sec:authorize>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th><input type="checkbox" value="${cus.id}"
										onclick="SelectAll();" /></th>
									<th>ID</th>
									<th>姓名</th>
									<th>手机号</th>
									<th>部门</th>
									<th>角色</th>
									<th>状态</th>
									<th>登录时间</th>
									<th>注册时间</th>


								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>
												<td><input type="checkbox" value="${item.id}"
													name="ischeck" /></td>
												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.realName}" /></td>
												<td><c:out value="${item.phone}" /></td>
												<td><c:out value="${item.deptId.deptName}" /></td>
												<td><c:out value="${item.roleCode.roleName}" /></td>
												<td><c:out value="${item.state==0 ? '在职' : '离职'}" /></td>

												<td><fmt:formatDate value="${item.loginTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><fmt:formatDate value="${item.addTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
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
							href="<s:url value="/user/pager?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/user/pager?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>

	<div class="modal fade" id="addModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">添加新员工</h4>
				</div>
				<div class="modal-body" id="addEmpForm">

					<div class="form-group">
						<label class="control-label">性别</label> <select
							class="form-control" id="add_sex">
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">姓名</label> <input
							class="form-control" type="text" maxlength="10"
							id="add_real_name" name="add_real_name" placeholder="请输入员工姓名">
					</div>
					<div class="form-group">
						<label class="control-label">手机号</label> <input
							class="form-control" type="text" maxlength="11" id="add_phone"
							placeholder="请输入11位手机号" name="add_phone">
					</div>
					<div class="form-group">
						<label class="control-label">密码</label> <input
							class="form-control" type="text" placeholder="请输入不超过10位的密码"
							id="add_pwd" maxlength="10" name="add_pwd">
					</div>
					<div class="form-group">
						<label class="control-label">部门</label> <select
							class="form-control" id="add_deptId">
							<c:forEach var="item" items="${deptList }">
								<option value="${item.id }">${item.deptName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">角色</label> <select
							class="form-control" id="add_role_code">
							<c:forEach var="item" items="${roleList }">
								<option value="${item.roleCode }">${item.roleName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-4">预览</label>
						<div class="">
							<div class="fileupload fileupload-new" data-provides="fileupload">
								<div class="fileupload-new thumbnail"
									style="width: 200px; height: 150px;">
									<img src="<s:url value="/assets" />/img/demoUpload.jpg" alt="" />
								</div>
								<div class="fileupload-preview fileupload-exists thumbnail"
									style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
								<div>
									<span class="btn btn-file btn-primary"><span
										class="fileupload-new">选择头像图片</span><span
										class="fileupload-exists">重新选择</span><input type="file"
										id="file"></span> <a href="#"
										class="btn btn-danger fileupload-exists"
										data-dismiss="fileupload">移除</a> <span>(注意:不选择将使用默认头像)</span>
								</div>
							</div>
						</div>
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


	<div class="modal fade" id="resetDeptModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">选择目标部门与角色</h4>
				</div>
				<div class="modal-body">


					<div class="form-group">
						<label class="control-label">部门</label> <select
							class="form-control" id="reset_deptId">
							<c:forEach var="item" items="${deptList }">
								<option value="${item.id }">${item.deptName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">角色</label> <select
							class="form-control" id="reset_role_code">
							<c:forEach var="item" items="${roleList }">
								<option value="${item.roleCode }">${item.roleName }</option>
							</c:forEach>
						</select>
					</div>
					<div id="upload_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_resetDept">提交</button>
				</div>
			</div>
		</div>
	</div>





</div>
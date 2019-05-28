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

<script type="text/javascript">
	$(function() {

		//新增管理员前台校验  
		$("#form1")
				.bootstrapValidator(
						{
							message : 'This value is not valid',
							//反馈图标  
							feedbackIcons : {
								valid : 'glyphicon glyphicon-ok',
								invalid : 'glyphicon glyphicon-remove',
								validating : 'glyphicon glyphicon-refresh'
							},
							fields : {
								old_pwd : {
									message : '密码无效',
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
								},
								add_pwd1 : {
									message : '密码无效',
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
								},
								add_pwd2 : {
									message : '密码无效',
									validators : {
										notEmpty : {
											message : '密码不能为空'
										},
										stringLength : {
											min : 6,
											max : 20,
											message : '密码长度介于6到20'
										},
										identical : {
											field : 'add_pwd1',
											message : '两次密码不一致'
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
			<h1 class="page-head-line">修改密码</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">

			<div class="panel panel-info col-md-5">
				<div class="panel-heading">修改密码</div>
				<div class="panel-body">
					<s:url value="/user/modifyPwd" var="submit_rul" />
					<sf:form role="form" id="form1" action="${submit_rul }" enctype="multipart/form-data">
						<div class="form-group">
							<label class="control-label">原密码</label> <input
								class="form-control" type="password" placeholder="请输入不超过20位的密码"
								id="add_pwd" maxlength="20" name="old_pwd">

						</div>
						<div class="form-group">
							<label class="control-label">新密码</label> <input
								class="form-control" type="password" placeholder="请输入不超过20位的密码"
								id="add_pwd" maxlength="20" name="add_pwd1">

						</div>
						<div class="form-group">
							<label class="control-label">确认新密码</label> <input
								class="form-control" type="password" placeholder="请输入不超过20位的密码"
								id="add_pwd" maxlength="20" name="add_pwd2">

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
										class="fileupload-exists">重新选择</span><input type="file" name="file"></span>
									<a href="#" class="btn btn-danger fileupload-exists"
										data-dismiss="fileupload">移除</a> <span>(注意:不选择将使用默认头像)</span>
								</div>
							</div>
						</div>
					</div>
						<div class="alert alert-info">
							<c:out value="${msg }" />
						</div>

						<button type="submit" class="btn btn-info" id="btn_add">提
							交</button>
					</sf:form>
				</div>
			</div>
		</div>
	</div>
</div>






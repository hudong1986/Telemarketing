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

	$(function() {
		$("#deleteMore").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的项");
				return;
			}

			if (confirm("删除后将无法恢复，确认提交？") == false) {
				return;
			}

			$.post("<s:url value="/customer/deleteSound" />", {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data.code == 1) {
					alert("删除成功");
					$("#searchForm").submit();
				} else {
					alert("当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#btn_add").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addSoundForm').data('bootstrapValidator').validate();
			if (!$('#addSoundForm').data('bootstrapValidator').isValid()) {
				return;
			}

			var formData = new FormData();
			var files = $('#sound_file')[0].files;//这里需要注意下  
			for (var i = 0, l = files.length; i < l; i++) {
				formData.append('files', files[i]);
			}
			formData.append("user_id", $("#add_user_id").val());
			formData.append("remark", $("#add_remark").val());
			formData.append("${_csrf.parameterName}", "${_csrf.token}");
			$.ajax({
				url : "<s:url value="/customer/importSound" />",
				type : 'POST',
				data : formData,
				// 告诉jQuery不要去处理发送的数据
				processData : false,
				// 告诉jQuery不要去设置Content-Type请求头
				contentType : false,
				beforeSend : function() {
					$("#upload_msg").html("正在导入，请不要关闭弹出框........");
				},
				success : function(data) {
					if (data.code == 1) {
						alert("导入成功,服务器正在进行相关转码等操作，或许稍后才能看到实际结果！");
						$('#importMoreModel').modal('hide');
						$("#searchForm").submit();
					} else {
						$("#upload_msg").html(data.retMsg);
					}
				},
				error : function(responseStr) {
					$("#upload_msg").html("导入失败");
				}
			});

		});

		//新增管理员前台校验  
		$("#addSoundForm").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				  
				sound_file : {
					validators : {
						notEmpty : {
							message : '文件不能为空'
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
			<h1 class="page-head-line">录音列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/customer/soundlist" var="submit_rul" />
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
								placeholder="录音生成开始时间" name="sound_time_beg"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							- <input type="text" class="form-control"
								value="${sound_time_end}" placeholder="录音生成结束时间"
								name="sound_time_end"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
						</div>

						<div class="form-group">
							<label for="user_name">客服姓名</label> <input type="text"
								class="form-control" id="user_name" name="user_name"
								value="${user_name}" placeholder="客服姓名">
						</div>
						<div class="form-group">
							<label for="user_name">客户手机号</label> <input type="text"
								class="form-control" id="customer_phone" name="customer_phone"
								value="${customer_phone}" placeholder="客户手机号">
						</div>
						<div class="form-group">
							<label for="user_name">时长</label> <input type="text"
								class="form-control" id="seconds" name="seconds"
								value="${seconds}" placeholder=">=时长(秒)">
								-<input type="text"
								class="form-control" id="maxseconds" name="maxseconds"
								value="${maxseconds}" placeholder="<=时长(秒)">
						</div>
						<div class="form-group">
							<label for="cus_name">每页显示数量</label> <select class="form-control"
								name="currentPageSize">
								<option value="20"
									${currentPageSize=="20" ? "selected=selected" : "" }>20</option>
								<option value="50"
									${currentPageSize=="50" ? "selected=selected" : "" }>50</option>
								<option value="100"
									${currentPageSize=="100" ? "selected=selected" : "" }>100</option>
								<option value="200"
									${currentPageSize=="200" ? "selected=selected" : "" }>200</option>
								<option value="300"
									${currentPageSize=="300" ? "selected=selected" : "" }>300</option>
								<option value="500"
									${currentPageSize=="500" ? "selected=selected" : "" }>500</option>

							</select>

						</div>
						<button type="submit" class="btn btn-default">查询</button>
					</sf:form>

				</div>
				<div class="panel-heading form-inline">

					<button type="button" id="importMore" data-toggle="modal"
						data-target="#importMoreModel" class="btn btn-sm btn-default">批量导入</button>

					<sec:authorize
						access="hasAnyRole('ADMIN','CEO','COO','CSO','TeamLeader')">
						<button type="button" id="deleteMore"
							class="btn btn-sm btn-default">批量删除</button>
					</sec:authorize>

				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th><input type="checkbox" value="${cus.id}"
										onclick="SelectAll();" /></th>
									<th><a
										href="<s:url value="/customer/soundlist?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									 
								 
									<th>客户手机号</th>
									<th>方向</th>
									<th>客服</th>
									<th><a
										href="<s:url value="/customer/soundlist?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>录音生成时间
									</a></th>
									<th><a
										href="<s:url value="/customer/soundlist?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>导入时间
									</a></th>
									<th><a
										href="<s:url value="/customer/soundlist?${pager.orderString4}" />">
											<li class="glyphicon ${pager.orderString4.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>时长(秒)
									</a></th>
									<th>备注</th>
									<th>#</th>

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
												 
												<td><c:out value="${item.customerPhone}" /></td>
												<td><c:out value="${item.direction}" /></td>
												<td><c:out value="${item.userName}" /></td>
												<td><fmt:formatDate value="${item.soundTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><fmt:formatDate value="${item.addTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>

												<td><c:out value="${item.soundLength}" /></td>
												<td><c:out value="${item.remark}" /></td>
												<td><a href="downloadsound/${item.id}" target="_blank">下载</a>
													<a href="playsound/${item.id}" target="_blank">播放</a></td>
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
							href="<s:url value="/customer/soundlist?${pager.firstPageString}" />">首页</a></li>
						<li><a
							href="<s:url value="/customer/soundlist?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/customer/soundlist?${pager.nextPageString}" />">下一页</a></li>
							
						<li><a
							href="<s:url value="/customer/soundlist?${pager.endPageString}" />">末页</a></li>	
					</ul>
				</nav>
			</div>
		</div>
	</div>

	<div class="modal fade" id="importMoreModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">导入录音</h4>
				</div>
				<div class="modal-body" id="addSoundForm">

					<div class="form-group alert alert-info">
						如果导入的录音文件名是由被叫号+主叫号码组成例如:18788728832_13577234223，系统将正确解析号码，
						否则将无法知道主叫与被号号码，备注看情况选填</div>
					<div class="form-group">
						<label class="control-label">选择客服</label>  
						<select class="form-control" name="add_user_id" id="add_user_id">

									<c:forEach var="item" items="${mySubUsers }">
										<option value="${item.phone }">${item.phone }-${item.realName }</option>
									</c:forEach>

								</select>
					</div>
					<div class="form-group">
						<label class="control-label">备注(非必须)</label>
						<textarea class="form-control" id="add_remark" name="add_remark"
							rows="3" name="content" maxlength="300">
									</textarea>
					</div>


					<div class="form-group">
						  <label class="control-label">选择录音文件</label> <input type="file"
							name="sound_file" multiple="multiple" id="sound_file"
							accept=".mp3,.MP3,.wav,.WAV,.amr,.AMR,.zip,.ZIP" />(注意：仅支持mp3/wav/amr/zip格式)
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
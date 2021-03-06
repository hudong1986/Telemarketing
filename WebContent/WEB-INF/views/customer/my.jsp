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
	function SelectAll() {
		var checkboxs = document.getElementsByName("ischeck");
		for (var i = 0; i < checkboxs.length; i++) {
			var e = checkboxs[i];
			e.checked = !e.checked;
		}
	}

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

	function getAllCheckBackLine() {
		var list = "";
		var checkboxs = document.getElementsByName("checkBacklineers");
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

	var editId;

	function openEditModel(id, phone, name, address, id_person, from) {
		editId = id;
		$("#edit_phone").val(phone);
		$("#edit_id_person").val(id_person);
		$("#edit_name").val(name);
		$("#edit_address").val(address);
		$("#edit_from").val(from);
		$('#singerEditModal').modal('show');
	}

	$(function() {

		$("#edit_customer").click(function() {

			$.ajax({
				url : "<s:url value="/customer/edit" />",
				type : 'POST',
				data : {
					id : editId,
					name : $("#edit_name").val(),
					data_from : $("#edit_from").val(),
					id_person : $("#edit_id_person").val(),
					address : $("#edit_address").val()
				},
				success : function(data) {
					if (data.code == 1) {
						alert("修改成功");
						$('#singerEditModal').modal('hide');
						$("#form1").submit();
					} else {
						alert(data.retMsg);
					}
				},
				error : function(responseStr) {
					alert("修改失败");
				}
			});

		});

		//新增管理员前台校验  
		$("#add_customer_form").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				add_phone : {
					message : '号码无效',
					validators : {
						notEmpty : {
							message : '手机号不能为空'
						},
						regexp : {
							regexp : '^1[0-9]{10}$',
							message : '手机号长度为11位整数'
						}
					}
				},
				add_name : {
					message : '姓名无效',
					validators : {
						notEmpty : {
							message : '姓名不能为空'
						},
						stringLength : {
							min : 2,
							max : 10,
							message : '姓名长度介于2到10'
						}
					}
				},
				add_from : {
					message : '来源无效',
					validators : {
						notEmpty : {
							message : '来源不能为空'
						},
						stringLength : {
							min : 2,
							max : 10,
							message : '密码长度介于2到10'
						}
					}
				}

			}
		});

		$("#btn_add_customer").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#add_customer_form').data('bootstrapValidator').validate();
			if (!$('#add_customer_form').data('bootstrapValidator').isValid()) {
				return;
			}

			$.ajax({
				url : "<s:url value="/customer/add" />",
				type : 'POST',
				data : {
					add_phone : $("#add_phone").val(),
					add_name : $("#add_name").val(),
					add_from : $("#add_from").val()
				},
				success : function(data) {
					if (data.code == 1) {
						alert("添加成功");
						$('#singerAddModal').modal('hide');
						$("#form1").submit();
					} else {
						alert(data.retMsg);
					}
				},
				error : function(responseStr) {
					alert("添加失败");
				}
			});

		});

		$("#btnSearch").click(function() {
			$("#form1").submit();
		});

		$('.form_datetime').datetimepicker({
			language : 'zh-CN',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			forceParse : 0,
			showMeridian : 1
		});

		//更新状态
		$("#button3").click(
				function() {
					var list = getAllCheck();
					if (list == "") {
						alert("没有选择项，请先选中要操作的客户！");
						return;
					}

					if (list.split(",").length == 1) {
						//获取一下状态
						$.post(
								"<s:url value="/customer/getCustomer" />",
								{
									"id" : list,
									"${_csrf.parameterName}" : "${_csrf.token}"

								},
								function(data, status) {
									if (data.code == "1") {
										$("#updateStatesModalSelect").val(
												data.object1.state);
										$("#updateBusinessSelect").val(
												data.object1.business_name);

									}

								}).error(function(data) {

						});
					}

					$("#btn_updateState").click(function() {
						$.post("<s:url value="/customer/update/up_state" />", {
							"ids" : list,
							"state" : $("#updateStatesModalSelect").val(),
							"busiName" : $("#updateBusinessSelect").val(),
							"${_csrf.parameterName}" : "${_csrf.token}"

						}, function(data, status) {
							if (data == "1") {
								alert("操作成功");
								$("#form1").submit();
							} else {
								alert("操作失败！");
							}

						}).error(function(data) {
							alert("请求失败!");
						});
					});

					$('#updateStatesModal').modal('show');

				});

		//更新到公共池
		$("#button2").click(function() {
			var list = getAllCheck();
			if (list == "") {
				alert("没有选择项，请先选中要操作的客户！");
				return;
			}

			if (confirm("请再次确认当前操作")) {
				$.post("<s:url value="/customer/update/up_common" />", {
					"ids" : list,
					"${_csrf.parameterName}" : "${_csrf.token}"

				}, function(data, status) {
					if (data == "1") {
						alert("操作成功");
						$("#form1").submit();
					} else {
						alert("操作失败！");
					}

				}).error(function(data) {
					alert("请求失败!");
				});

			}

		});

		//选择分配到下属
		$("#button1").click(
				function() {
					var list = getAllCheck();
					if (list == "") {
						alert("没有选择项，请先选中要操作的客户！");
						return;
					}

					$.ajax({
						type : "GET",
						url : "<s:url value="/user/subordinate" />",
						dataType : "json",
						success : function(data) {
							if (data == null) {
								alert("没有获取到数据");
								return;
							}

							$("#myEmployeeList").empty();
							$.each(data,
									function(n, value) {
										phone = value.phone;
										name = value.realName;
										deptName = value.deptId.deptName;
										txt = deptName + "-" + name + "("
												+ phone + ")";
										$("#myEmployeeList").append(
												"<option value='"+phone+"'>"
														+ txt + "</option>");

									});

							$('#myModal').modal('show');
						},
						error : function(xhr) {
							alert("提交失败！");
						}
					});

				});

		//选择共享到权证或同组
		$("#btn_shareToBack")
				.click(
						function() {
							var list = getAllCheck();
							if (list == "") {
								alert("没有选择项，请先选中要操作的客户！");
								return;
							}

							$
									.ajax({
										type : "GET",
										url : "<s:url value="/user/backline" />",
										dataType : "json",
										success : function(data) {
											if (data == null) {
												alert("没有获取到数据");
												return;
											}

											$("#mulptiCheckBoxDiv").html("");
											$
													.each(
															data,
															function(n, value) {
																phone = value.phone;
																name = value.realName;
																deptName = value.deptId.deptName;
																txt = deptName
																		+ "-"
																		+ name
																		+ "("
																		+ phone
																		+ ")";
																$(
																		"#mulptiCheckBoxDiv")
																		.append(
																				"<div class=\"checkbox\"><label> <input type=\"checkbox\" value='"+phone+"' name='checkBacklineers' />"
																						+ txt
																						+ "</label></div>");

															});

											$('#backLineModal').modal('show');
										},
										error : function(xhr) {
											alert("提交失败！");
										}
									});

						});
		
							//取消共享
							$("#btn_cancelshare").click(
									function() {
											var list = getAllCheck();
											if (list == "") {
												alert("没有选择项，请先选中要操作的客户！");
												return;
											}
											
											$.post("<s:url value="/customer/update/cancelshare" />", {
												"ids" : list,
												"${_csrf.parameterName}" : "${_csrf.token}"

											}, function(data, status) {
												if (data == 1) {
													alert("操作成功");
													$("#form1").submit();
												} else {
													alert("操作失败！");
												}

											}).error(function(data) {
												alert("请求失败!");
											});
									});

		//确定共享到权证
		$("#btn_backLine").click(function() {
			list = getAllCheck();
			phones = getAllCheckBackLine();
			if (phones == "") {
				alert("请选择人员！");
				return;
			}

			$.post("<s:url value="/customer/update/share_who_use" />", {
				"ids" : list,
				"phones" : phones,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == "1") {
					alert("操作成功");
					$("#form1").submit();
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		//确定分配到下属
		$("#btn_dispatch").click(function() {
			list = getAllCheck();
			phone = $("#myEmployeeList").val();
			$.post("<s:url value="/customer/update/up_who_use" />", {
				"ids" : list,
				"phone" : phone,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == "1") {
					alert("操作成功");
					$("#form1").submit();
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});
		});

		$("#btn_import").click(function() {
			var formData = new FormData();
			formData.append('input_file', $('#input_file')[0].files[0]);
			formData.append("${_csrf.parameterName}", "${_csrf.token}");
			$.ajax({
				url : "<s:url value="/customer/importData" />",
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
						alert(data.retMsg);
						$('#dispatchModal').modal('hide');
						$("#form1").submit();
					} else {
						$("#upload_msg").html(data.retMsg);
					}
				},
				error : function(responseStr) {
					$("#upload_msg").html("导入失败");
				}
			});

		});
		
					$("#goUrl").click(function(){
						
						pageCount= $("#pagecounts").val();
					    urlBase = "<s:url value="/customer/pager?${pager.firstPageString}"  />";
						lastIndex = urlBase.lastIndexOf("=");
						url = urlBase.substr(0,lastIndex+1);
						url=url+pageCount;
						window.location.href=url;
					});
					
					
					    $("#backCustomer").click(function() {

							list = getAllCheck();
							if (list == "") {
								alert("请选择要操作的项");
								return;
							}

							if (confirm("把选中的客户回收到自己名下，确认提交？") == false) {
								return;
							}

							$.post("<s:url value="/customer/backCustomer" />", {
								"ids" : list,
								"${_csrf.parameterName}" : "${_csrf.token}"

							}, function(data, status) {
								if (data == 1) {
									alert("操作成功");
									$("#form1").submit();
								} else {
									alert("当前操作失败！");
								}

							}).error(function(data) {
								alert("请求失败!");
							});
						});

	});
</script>


<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">我的客户列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/customer/my" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="form1">
						<div class="form-group">
							<label for="phone">客户手机</label> <input type="text"
								class="form-control" id="phone" name="phone" value="${phone}"
								placeholder="">
						</div>
						<div class="form-group">
							<label for="cus_name">客户姓名</label> <input type="text"
								class="form-control" id="cus_name" name="cus_name"
								value="${cus_name}" placeholder="">
						</div>
						<div class="form-group">
							<label for="dataFrom">数据来源</label> <input type="text"
								class="form-control" id="dataFrom" name="dataFrom"
								value="${dataFrom}" placeholder="">
						</div>
						<div class="form-group">
							<label for="id_person">所属或回收客服姓名</label> <input type="text"
								class="form-control" id="who_use_name" name="who_use_name"
								value="${who_use_name}" placeholder="">
						</div>
						<div class="form-group">
							<label for="business_name">业务类型 </label> <select
								class="form-control" name="business_name">
								<option value="全部"
									${business_name=="全部" ? "selected=selected" : ""}>全部</option>
								<c:forEach var="bus" items="${business_types }">
									<option value="${bus.typeName }"
										${business_name.equals(bus.typeName) ? "selected=selected" : "" }>${bus.typeName }</option>

								</c:forEach>


							</select>
						</div>
						<div class="form-group">
							<label for="cus_name">客户状态</label> <select class="form-control"
								name="contactState">
								<option value="-1"
									${contactState=="-1" ? "selected=selected" : "" }>全部</option>
								<c:forEach var="item" items="${customerStates }">
									<option value="${item.id }"
										${contactState==item.id ? "selected=selected" : "" }>${item.name }</option>
								</c:forEach>
							</select>

						</div>
						<sec:authorize
							access="hasAnyRole('CSO','TeamLeader','BackLineLeader')">
							<div class="form-group">
								<label>部门</label> <select class="form-control" name="upDownId">

									<c:forEach var="item" items="${mydept }">
										<option value="${item.upDownId }"
											${upDownId==item.upDownId ? "selected=selected" : "" }>${item.deptName }</option>
									</c:forEach>

								</select>

							</div>

							<div class="form-group">
								<label for="cus_name">客户分配状态</label> <select
									class="form-control" name="searchType">
									<option value="2"
										${searchType=="2" ? "selected=selected" : "" }>未分配客户</option>
									<option value="3"
										${searchType=="3" ? "selected=selected" : "" }>已分配客户</option>


								</select>

							</div>
						</sec:authorize>

						<sec:authorize access="hasAnyRole('CSO','TeamLeader')">
							<div class="form-group">
								<label>是否来自离职回收</label> <select class="form-control"
									name="has_recovery">
									<option value="-1"
										${has_recovery=="-1" ? "selected=selected" : "" }>全部</option>

									<option value="0"
										${has_recovery=="0" ? "selected=selected" : "" }>否</option>
									<option value="1"
										${has_recovery=="1" ? "selected=selected" : "" }>是</option>


								</select>

							</div>
							<div class="form-group">
								<label for="who_put_common_name">谁放入公共池</label> <input
									type="text" class="form-control" id="who_put_common_name"
									name="who_put_common_name" value="${who_put_common_name}"
									placeholder="">
							</div>

							<%-- 	<div class="form-group">
								<label for="id_person">被回收客服姓名</label> <input type="text"
									class="form-control" id="who_use_name" name="who_use_name"
									value="${who_use_name}" placeholder="">
							</div> --%>

						</sec:authorize>

						<div class="form-group">
							<label for="cus_name">状态更新时间</label> from<input type="text"
								class="form-control" value="${add_time_beg}" placeholder=""
								name="add_time_beg"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							to<input type="text" class="form-control" value="${add_time_end}"
								placeholder="" name="add_time_end"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
						</div>
						<div class="form-group">
							<label for="cus_name">分配时间</label> from<input type="text"
								class="form-control" value="${get_time_beg}" placeholder=""
								name="get_time_beg"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							to<input type="text" class="form-control" value="${get_time_end}"
								placeholder="" name="get_time_end"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
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

					</sf:form>

				</div>
				<div class="panel-heading form-inline">
					<button type="button" class="btn btn-sm btn-default" id="btnSearch">
						<i class="glyphicon glyphicon-search"></i>查询
					</button>
					<sec:authorize access="hasAnyRole('CSO')">
						<button type="button" id="btnImport"
							class="btn btn-sm btn-default" data-toggle="modal"
							data-target="#dispatchModal">批量导入</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('CSO','TeamLeader','CustomerService')">
						<button type="button" id="btnAdd" class="btn btn-sm btn-default"
							data-toggle="modal" data-target="#singerAddModal">逐一添加</button>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('CSO','TeamLeader')">
						<button type="button" id="button1" class="btn btn-sm btn-default">分配到下属</button>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('CSO','TeamLeader')">
						<button type="button" id="backCustomer"
							class="btn btn-sm btn-default">回收客户</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('CSO','TeamLeader','CustomerService','BackLineLeader','BackLiner','SaleBackLeader','SaleBacker')">
						<button type="button" id="btn_shareToBack"
							class="btn btn-sm btn-default">共享给同事</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('CSO','TeamLeader','CustomerService','BackLineLeader')">
						<button type="button" id="btn_cancelshare"
							class="btn btn-sm btn-default">取消共享</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('CSO','TeamLeader','CustomerService')">
						<button type="button" id="button2" class="btn btn-sm btn-default">分配到公共池</button>
						<button type="button" id="button3" class="btn btn-sm btn-default">标记业务与状态</button>
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
										href="<s:url value="/customer/pager?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>客户姓名</th>
									<th>客户手机</th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString4}" />">
											<li class="glyphicon ${pager.orderString4.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>来源
									</a></th>
									<th>所属</th>
									<th>共享</th>
									<sec:authorize access="hasAnyRole('CSO','TeamLeader')">
										<th>谁放入公共池</th>
										<th>回收来自</th>
									</sec:authorize>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString2}" />">
											<li class="glyphicon ${pager.orderString2.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>状态
									</a></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString5}" />">
											<li class="glyphicon ${pager.orderString5.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>当前业务
									</a></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>更新时间
									</a></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString7}" />">
											<li class="glyphicon ${pager.orderString7.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>导入时间
									</a></th>
									<th><a
										href="<s:url value="/customer/pager?${pager.orderString6}" />">
											<li class="glyphicon ${pager.orderString6.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>分配时间
									</a></th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="cus" items="${pager.data }">
											<tr>
												<td><input type="checkbox" value="${cus.id}"
													name="ischeck" /></td>
												<td><c:out value="${cus.id}" /></td>
												<td><c:out value="${cus.cusName}" /></td>
												<td><c:out value="${cus.phone.substring(0,7)}****" /></td>
												<td><c:out value="${cus.dataFrom}" /></td>
												<td><c:out value="${cus.whoUseName}" /></td>
												<td><c:out value="${cus.shareUseName}" /></td>
												<sec:authorize access="hasAnyRole('CSO','TeamLeader')">
													<td><c:out value="${cus.who_put_common_name}" /></td>
													<td><c:out value="${cus.recovery_from}" /></td>
												</sec:authorize>
												<td><c:out value="${cus.stateName}" /></td>
												<td><c:out value="${cus.business_name }" /></td>
												<td><fmt:formatDate value="${cus.update_state_time}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><fmt:formatDate value="${cus.addTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><fmt:formatDate value="${cus.whoGetTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>

												<td>
													<div class="form-inline">
														<div class="form-group">
															<s:url value="/customer/track_record/${cus.id}"
																var="op_track_url" />
															<sf:form action="${ op_track_url}" method="post"
																target="_blank">
																<button type="submit" class="btn btn-xs btn-default">
																	<i class="glyphicon glyphicon-plus"></i>跟踪记录
																</button>
															</sf:form>
														</div>
														<sec:authorize
															access="hasAnyRole('CSO','TeamLeader','CustomerService')">
															<div class="form-group">
																<button type="button" class="btn btn-xs btn-default"
																	onclick="openEditModel('${cus.id}','${cus.phone}','${cus.cusName}','${cus.address}','${cus.idPerson}','${cus.dataFrom}') ">
																	<i class="glyphicon glyphicon-edit"></i>修改信息
																</button>
															</div>
														</sec:authorize>
														 
															<div class="form-group">
																<a
																	href="<s:url
														value='/customer/soundlist?customer_phone=${cus.phone}' />"
																	target="_blank">
																	<button type="button" class="btn btn-xs btn-default">
																		<i class="glyphicon glyphicon-phone-alt"></i>查看录音

																	</button>
																</a>
															</div>
														 
													</div>

												</td>
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
			<!-- End  Basic Table  -->
			<div class="col-md-8">
				总条数:${pager.totalCount},总页数:${pager.pageCounts},当前页码:${pager.currentPageNum},当前显示条目:${pager.pageSize}</div>
			<div class="col-md-4">
				<nav aria-label="...">
					<ul class="pager">
						<li><a
							href="<s:url value="/customer/pager?${pager.firstPageString}" />">首页</a></li>
						<li><a
							href="<s:url value="/customer/pager?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/customer/pager?${pager.nextPageString}" />">下一页</a></li>
						<li><a
							href="<s:url value="/customer/pager?${pager.endPageString}" />">末页</a></li>
					</ul>
				</nav>
			</div>
			<div class="col-md-11 form-inline form-group text-right">
				<label class="control-label">页码</label> <input type="text"
					style="width: 50px;" class="form-control" id="pagecounts"
					value="${pager.currentPageNum}" placeholder="1">
				<button type="button" class="btn btn-sm btn-default" id="goUrl">跳转</button>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">请选择下属</h4>
				</div>
				<div class="modal-body">


					<select class="form-control" id="myEmployeeList">

					</select>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_dispatch">确定</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="backLineModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">请选择目标成员</h4>
				</div>
				<div class="modal-body">
					<div class="form-group" id="mulptiCheckBoxDiv"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_backLine">确定</button>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="updateStatesModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">请选择目标业务与状态</h4>
				</div>
				<div class="modal-body">
					  <label class="control_label">选择业务</label>       <select
						class="form-control" id="updateBusinessSelect">
						<c:forEach var="item" items="${business_types }">
							<option value="${item.typeName }">${item.typeName }</option>
						</c:forEach>

					</select>   <label class="control_label">选择业务状态</label>       <select
						class="form-control" id="updateStatesModalSelect">
						<c:forEach var="item" items="${customerStates }">
							<option value="${item.id }">${item.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_updateState">确定</button>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="dispatchModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">批量导入客户</h4>
				</div>
				<div class="modal-body">
					<div class="form-group alert alert-warning">
						<strong>注意事项</strong>
						目前支持txt文档导入，格式为以下排列字段，字段分隔符可以是逗号、冒号、分号、制表符(excel表粘贴到文本)：<br />
						手机号 姓名 来源 地址
					</div>


					<div class="form-group">

						<label class="control-label">选择文件</label> <input type="file"
							name="input_file" id="input_file" accept=".txt,.TXT" />

					</div>
					<div id="upload_msg"></div>


				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_import">开始导入</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="singerAddModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">添加客户</h4>
				</div>
				<form class="modal-body" id="add_customer_form">
					<div class="form-group">
						<label class="control-label">手机号</label> <input
							class="form-control" type="text" placeholder="请输入11位手机号"
							id="add_phone" name="add_phone" maxlength="11">
					</div>
					<div class="form-group">
						<label class="control-label">姓名</label> <input
							class="form-control" type="text" placeholder="请输入不超过10姓名"
							id="add_name" name="add_name" maxlength="10">
					</div>
					<div class="form-group">
						<label class="control-label">来源</label> <input
							class="form-control" type="text" placeholder="银行、电信、移动、其它等"
							id="add_from" name="add_from" maxlength="10">
					</div>
				</form>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_add_customer">添加</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="singerEditModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">修改客户基本信息</h4>
				</div>
				<form class="modal-body" id="edit_customer_form">
					<div class="form-group">
						<label class="control-label">手机号</label> <input
							class="form-control" type="text" placeholder="" id="edit_phone"
							name="edit_phone" maxlength="11" disabled>
					</div>
					<div class="form-group">
						<label class="control-label">姓名</label> <input
							class="form-control" type="text" placeholder="请输入不超过10姓名"
							id="edit_name" name="edit_name" maxlength="10">
					</div>
					<div class="form-group">
						<label class="control-label">身份证号</label> <input
							class="form-control" type="text" placeholder="长度不超过20个字符"
							id="edit_id_person" name="edit_id_person" maxlength="20">
					</div>
					<div class="form-group">
						<label class="control-label">地址</label> <input
							class="form-control" type="text" placeholder="请输入不超过100字符"
							id="edit_address" name="edit_address" maxlength="100">
					</div>
					<div class="form-group">
						<label class="control-label">来源</label> <input
							class="form-control" type="text" placeholder="银行、电信、移动、其它等"
							id="edit_from" name="edit_from" maxlength="10">
					</div>
				</form>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="edit_customer">修改</button>
				</div>
			</div>
		</div>
	</div>



</div>
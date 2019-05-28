<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>CRM后台-<c:out
		value="${CurrentTitle==null ? '' : CurrentTitle }" /></title>

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
<!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
<!-- JQUERY SCRIPTS -->
<script src="<s:url value="/assets" />/js/jquery-1.10.2.js"></script>
<!-- BOOTSTRAP SCRIPTS -->
<script src="<s:url value="/assets" />/js/bootstrap.js"></script>
<!-- METISMENU SCRIPTS -->
<script src="<s:url value="/assets" />/js/jquery.metisMenu.js"></script>
<!-- CUSTOM SCRIPTS -->
<script src="<s:url value="/assets" />/js/custom.js"></script>

<script>
	var remindIds = "";

	function getMyRemind() {
		var display = $('#realtimeRemindModal').css('display');
		if (display != 'none') {
			setTimeout(getMyRemind, 10000);
			return;
		}

		$.ajax({
			url : "<s:url value="/remind/getMyRemind" />",
			type : 'GET',
			beforeSend : function() {

			},
			success : function(data) {
				if (data.code == 1) {

					$("#realtimeRemindModal_content").empty();
					remindIds = "";
					$.each(data.object1, function(n, value) {

						str = "<div class='alert alert-info'><h5>"
								+ value.userName + "-" + value.topic + "-"
								+ value.addTime + "</h5>";
						str += "<p>" + value.context + "</p></div>";
						$("#realtimeRemindModal_content").append(str);
						remindIds += value.id + ",";
					});

					$('#realtimeRemindModal').modal('show');

				} else {
					setTimeout(getMyRemind, 10000);
				}
			},
			error : function(responseStr) {
				setTimeout(getMyRemind, 10000);
			}
		});

	}

	$(function() {

		setTimeout(getMyRemind, 3000);
		//不再提醒
		$("#btn_readRemind")
				.click(
						function() {
							$
									.post(
											"<s:url value="/remind/readRemind" />",
											{
												"remindIds" : remindIds
														.substr(
																0,
																remindIds.length - 1),
												"${_csrf.parameterName}" : "${_csrf.token}"

											},
											function(data, status) {
												if (data.code == "1") {
													alert("操作成功");
													if (confirm("是否立即前往提醒详细页？")) {
														window.location.href = "<s:url value="/remind/list" />";

													} else {
														$(
																'#realtimeRemindModal')
																.modal('hide');
													}

												} else {
													alert("操作失败！");
												}

											}).error(function(data) {
										alert("请求失败!");
									});
						});

	});

	function copyHandle() {
		alert("对不起，为了数据安全，系统设置为禁止复制！您的复制操作也会被记录，请理解！");
		return false;
	}
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
	<div id="wrapper">
		<div id="header">
			<t:insertAttribute name="header" />
		</div>
		<div id="side">
			<t:insertAttribute name="side" />
		</div>
		<div id="page-wrapper">
			<div id="content">
				<t:insertAttribute name="content" />
			</div>
		</div>
		<!-- /. PAGE WRAPPER  -->
	</div>
	<!-- /. WRAPPER  -->

	<div id="footer-sec" class="text-center">
		<div id="footer">
			<t:insertAttribute name="footer" />
		</div>

	</div>
	<!-- /. FOOTER  -->


	<div class="modal fade" id="realtimeRemindModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="realtimeRemindModal_title">
						<i class="glyphicon glyphicon-info-sign"></i> 通知提醒
					</h4>
				</div>
				<div class="modal-body" id="realtimeRemindModal_content"></div>
				<div class="modal-footer">
					<!-- <button type="button" class="btn btn-default" data-dismiss="modal">刷新后再提醒</button> -->
					<button type="button" class="btn btn-primary" id="btn_readRemind">不再提醒</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title><spring:message code="login.title"/></title>
<body class="login-page" style="background-color: #f2f0f0">
	<jsp:include page="/page/base/commontop.jsp" />
	<spring:url value="/login" var="formAction">
		<spring:param name="method" value="loginProcess"></spring:param>
	</spring:url>
	<c:set var="commandName" scope="request" value="formDataModelAttr" />


	<div id="main" class="container clearfix" style="min-height: 347px; background-color: #f2f0f0">

		<div class="col-md-4 form-login">
			<p class="login-title">
				<img alt="logo_cnctech" style="width: 50%;" src="<spring:url value="/images/logo.png"/>" />
			</p>
			<div class="panel-body">
				<form:form cssClass="form-horizontal login" id="frmLogin" action='${formAction}' method="post"
					modelAttribute="${commandName}">
					<div id="dangnhap">
						<div class="form-body" style="color: #0678B3; font-weight: bold;">
							<div class="form-group">
							<div class='Row' style="padding-left: 0 !important; margin-bottom: 10; padding-bottom: 0;">
								<spring:message code="Login.username" /> 
								<div style="margin-left:auto;padding-bottom:0px !important;"><a href="login?lang=en">EN</a> | <a href="login?lang=vi">VI</a></div>
								
							</div>
							<div class="input-group-icon">
								<i class="fa fa-user" style="color: #333399;"></i>
								<form:input path="user" id="username" cssClass="required form-control" placeholder="" />
							</div>
							</div>
							<div class="form-group">
								<div class='Row' style="padding-left: 0 !important; margin-bottom: 10; padding-bottom: 0;">
								<spring:message code="Login.password" />
								</div>
								<div class="input-icon input-group-icon">
									<i class="fa fa-eye" onclick="hideShowPW(this, 'pw');" style="color: #333399;"></i>
									<form:input path="pass" id="password" type="password" cssClass="required form-control"
										placeholder="" />
									
								</div>
							</div>
						</div>
						<div class="form-actions">
							<%-- <a type="button" style="display: flow-root;" class="btn blue" href="<spring:url value='/login?method=loginProcess'/>">
                                                   <i class="fa fa-edit"></i> Đăng nhập
                        </a> --%>
							<button type="button" onclick="login();" class="btn-login"><spring:message code="Login.btn.login" /></button>
						</div>
					</div>
					<div id="dangki">
						<div class="form-body" style="color: #0678B3; font-weight: bold;">
							<div class="form-group">
								<label for="exampleInputEmail1"><spring:message code="login.old.pw" /></label>
								<div class="input-group-icon">
									<i class="fa fa-eye" onclick="hideShowPW(this, 'olsPW');" style="color: #333399;"></i>
									 <input id="olsPW" type="password"
										Class="required form-control" placeholder="" />
								</div>

							</div>
							<div class="form-group">
								<label><spring:message code="login.new.pw" /></label>
								<div class="input-icon input-group-icon">
									<i class="fa fa-eye" onclick="hideShowPW(this, 'npw');" style="color: #333399;"></i> <input id="newPW" type="password"
										Class="required form-control" placeholder="" />
								</div>
							</div>
							<div class="form-group">
								<label><spring:message code="login.confirm.pw" /></label>
								<div class="input-icon input-group-icon">
									<i class="fa fa-eye" onclick="hideShowPW(this, 'cfpw');" style="color: #333399;"></i>
									 <input id="confirmNewPW" type="password"
										Class="required form-control" placeholder="" />
								</div>
							</div>
						</div>
						<div class="form-actions">
							<button type="button" onclick="changePassword();" class="btn-login">Submit</button>
							<button type="button" onclick="backLogin();" class="btn-login">Back</button>
						</div>
					</div>
					<input type="hidden" id="status" name="status" value="${status}" />
				</form:form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		/* function doLogin() {
			$.ajax({
				url : $('#theForm').attr('action') + '?method=login',
				success : function(severtrave) {
					alert('Login ok');
				},
				error : function(severtrave) {
					alert(severtrave);
				},
				data : $('#theForm').serialize()

			});

		} */
		$(document).ready(function() {
			var status = $('#status').val();
			$('#dangki').css('display', 'none');
			$('#dangnhap').css('display', 'block');
			console.log(status);
			if (status == "false") {
				//jAlert('<s:text name ="Đăng nhập không thành công"/>','<s:text name ="ok"/>');
				alert("<spring:message code="login.error.info" />");
			} else if (status == "firstLogin" || status == "exprired") {
				$('#dangki').css('display', 'block');
				$('#dangnhap').css('display', 'none');
			}
		});
		$(document).keypress(function(e) {
	        if(e.which == 13) {
	        	login();
	        }
	    });
		function backLogin() {
			$('#dangki').css('display', 'none');
			$('#dangnhap').css('display', 'block');
		}

		function changePassword() {
			if ($('#newPW').val().trim().length==0 || $('#confirmNewPW').val().trim().length==0) {
				alert("<spring:message code="login.error.01" />");
				return;
			}
			if ($('#newPW').val() != $('#confirmNewPW').val()) {
				alert("<spring:message code="login.changepw.newpw.incorrect" />");
				return;
			}
			var uri = $('#frmLogin').attr('action');
			var path = uri.split('?');
			var newPass = $('#newPW').val();
			var url = path[0] + '?method=changepassword';
			url = encodeURI(url);
			$.ajax({
				url : url,
				data : {
					"oldPassWord" : $('#olsPW').val(),
					"newPassWord" : newPass,
					"firstLogin" : "firstLogin"
				},
				method : 'POST',
				success : function(_result, status, xhr) {
					if (_result.length > 0) {
						if ("success" === _result) {
							$('#dangki').css('display', 'none');
							$('#dangnhap').css('display', 'block');
							alert("<spring:message code="login.change.pw.success" />");
						} else {
							alert(_result);
						}
					}
				}
			});
		}

		function login() {
			if ($('#username').val().trim().length==0 || $('#password').val().trim().length==0) {
				alert("<spring:message code="login.error.01" />");
				return;
			}
			var uri = $('#frmLogin').attr('action');
			var path = uri.split('?');
			var url = path[0]
			$.ajax({
				url : uri,
				data : {
					"username" : $('#username').val(),
					"password" : $('#password').val()
				},
				method : 'POST',
				success : function(_result, status, xhr) {
					if (_result.length > 0) {
						if ("success" === _result) {
							$('#dangki').css('display', 'none');
							$('#dangnhap').css('display', 'block');
							alert("<spring:message code="login.change.pw.success" />");
							return;
						} else if (_result === "exprired" || _result === "firstLogin") {
							$('#dangki').css('display', 'block');
							$('#dangnhap').css('display', 'none');
							return;
						} else if (_result === "fail") {
							alert("<spring:message code="login.error.info" />");
							return;
						} else if (_result === "login_exprired") {
							alert("<spring:message code="login.error.02" />");
							return;
						} else // Page mac dinh
							window.location.href = _result;

					} else
						window.location.href = "index";
				}
			});
		}
		function hideShowPW(icon, type) {
			var input ;
			if (type === 'pw') {
				input = $("#password");
			} else if (type === 'npw') {
				input = $("#newPW");
			} else if (type === 'cfpw') {
				input = $("#confirmNewPW");
			} else if (type === 'olsPW') {
				input = $("#olsPW");
			}
			if (input.attr("type") === "password") {
				input.attr("type", "text");
				$(icon).removeClass("fa-eye");
				$(icon).addClass("fa-eye-slash");
			} else {
				input.attr("type", "password");
				$(icon).removeClass("fa-eye-slash");
				$(icon).addClass("fa-eye");
			}
		}
	</script>

</body>

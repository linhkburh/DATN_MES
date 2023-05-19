<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<%@ page import="entity.frwk.SysUsers"%>
<%
	ApplicationContext appContext = (ApplicationContext) request.getSession()
			.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
	SysUsers user = null;
	if (appContext != null)
		user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
%>
<style>
#header, #header div, #header ul, #header nav {
	background-color: #f2f0f0 !important;
}

body {
	background-color: f2f1ed !important;
}

#header {
	height: 120px !important;
	border-bottom-color: orange;
	border-bottom-width: 1;
	border-bottom-style: double;
}
</style>
<div id="header" class="clearfix" style="height: 110px;">
	<div class="row" style="max-width: 1140px; margin: 0 auto !important; padding-top: 0; padding-bottom: 0 !important">
		<div class="col-md-4 col-sm-12" style="text-align: center; padding: 5px 0 0px 0;">
			<a class="nav-link"> <img alt="logo_cic" src="<spring:url value="/images/logo.png"/>"
				style="width: 154px; float: left; margin: 10px 0 0 5px;" />
			</a>
			<div style="width: 1px; border-left: 1px solid #cacaca; margin: -5px 0px 0px 20px; float: left; height: 40px;"></div>
			<img id="iso-logo" src="images/iso-logo.gif"
				style="width: 124px; height: 41px; margin-top: -5px; float: left; margin-left: 20px;" />
		</div>
	</div>
	<div class="row" style="max-width: 1140px; margin: 0 auto !important; padding-top: 0; padding-bottom: 0 !important">
		<div class="col-md-12" style="padding: 0px 0px 0px 5px;">
			<div class="row">
				<ul class="nav navbar-nav account"
					style="position: fixed; right: 0px; display: flex; flex-direction: row; top: 0; padding: 5px; background: #fff; border-radius: 5px; z-index: 1000; font-size: 10px">
					<li><a class="no-caret" href="#" onclick="openModalUse()" style="font-size: 10px !important"><i
							class="fa fa-download"></i>HD sử dụng |</a></li>
					<li><a data-toggle="modal" data-target="#modalChangePass" href='#' style="font-size: 10px !important"><i
							class="fa fa-expeditedssl" style="margin-top: 2px;"> </i><%=user.getUsername()%> |</a></li>
					<li><a class="no-caret" href="<spring:url value='/logout'/>"
						style="font-size: 10px !important; padding-right: 10"><i class="fa fa-sign-out"></i> <spring:message
								code="common.btn.logout" /></a></li>
				</ul>
			</div>
			<jsp:include page="/page/base/navbar.jsp" />
		</div>
	</div>
</div>
<div id="modelViewUse" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Hướng dẫn sử dụng</h4>
			</div>
			<div class="modal-body" style="background-color: white">
				<div class='table-responsive' style="width: 100%; table-layout: fixed; margin: 0px;">
					<table class="table table-bordered" id="viewUse">
						<thead>
							<tr style="background-color: #93D5F7">
								<td>STT</td>
								<td>Nội dung</td>
								<td>Download</td>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>

				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn gray" data-dismiss="modal">
					<spring:message code="common.btn.close" />
				</button>
			</div>
		</div>
	</div>
</div>
<div id="modalChangePass" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">
					<spring:message code="login.changepw.title" />
				</h4>
			</div>
			<div class="modal-body">
				<form action='login.action' id='frmChangePass'>
					<div class='Table'>
						<div class='Row' style="height: 40px;">
							<div class='col-md-4 col-lg-4 col-sm-12 col-xs-12' style="height: 40px;">
								<spring:message code="login.old.pw" />
							</div>
							<div class='col-md-6 col-lg-6 col-sm-12 col-xs-12' style="height: 40px;">
								<input id='oldPassWord' type='password' autocomplete="off" style='width: 100%;' class="inputModal" />
							</div>
						</div>
						<div class='Row' style="height: 40px;">
							<div class='col-md-4 col-lg-4 col-sm-12 col-xs-12' style="height: 40px;">
								<spring:message code="login.new.pw" />
							</div>
							<div class='col-md-6 col-lg-6 col-sm-12 col-xs-12' style="height: 40px;">
								<input id='newPassWord' autocomplete="off" style='width: 100%;' type='password' class="inputModal" />
							</div>
						</div>
						<div class='Row' style="height: 40px;">
							<div class='col-md-4 col-lg-4 col-sm-12 col-xs-12' style="height: 40px;">
								<spring:message code="login.confirm.pw" />
							</div>
							<div class='col-md-6 col-lg-6 col-sm-12 col-xs-12' style="height: 40px;">
								<input id='confirmPassWord' autocomplete="off" style='width: 100%;' type='password' class="inputModal" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="ChangePasswordSave();">
					<spring:message code="common.btn.save" />
				</button>
				<button type="button" class="btn gray" data-dismiss="modal">
					<spring:message code="common.btn.close" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	function openModalUse() {
		if ($("#viewUse tr").length > 1) {
			$('#modelViewUse').modal();
			return;
		}
		var url = "userDoc?method=loadHelp"
		var _row = '';

		$.ajax({
			url : url,
			method : 'GET',
			success : function(_result) {

				if (_result != null) {
					for (var i = 0; i < _result.length; i++) {
						console.log(_result[i]);
						_row += '<tr>';
						_row += '<td>' + (i + 1) + '</td>';
						_row += '<td>' + (_result[i].name == null ? '' : _result[i].description) + '</td>';
						_row += '<td onClick="downloadHDSD(\'' + _result[i].id
								+ '\');"><i style="color:blue;" class="fa fa-download"></i></td>';
					}
					$("#viewUse tr:last").after(_row);

				}
			}
		});
		$('#modelViewUse').modal();
	}

	function downloadHDSD(id) {
		var currentUrl = $(location).attr('href').split("/");
		var url = "userDoc?method=downloadDoc&id=" + id;
		window.open(url);
	}
</script>
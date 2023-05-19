<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<style>
.dataTables_filter {
	display: none;
}
</style>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Khách hàng-đối tác" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/customerMng" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<form:hidden path="to" id="to" />
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã khách hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="cuCode" id="cuCode"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Người đại diện</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="cuName" id="cuName"></form:input>
						</div>
					</div>
					<div class="Row">

						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên doanh nghiệp</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orgName" id="orgName"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="email" id="email"></form:input>
						</div>
					</div>
					<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đối tác</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="partner" id="partner"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="cus" id="cus"/>
							</div>
						</div>

					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm" />
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="customer.id" id="id" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã khách hàng<font style="color: red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.code" cssClass="required" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên viết tắt</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.shortName" />
				</div>
			</div>
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên doanh nghiệp<font style="color: red">*</font>
				</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea path="customer.orgName" rows="2" maxLength="300" cssClass="required" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đối tác</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:checkbox path="customer.isPartner"/>
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khách hàng</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:checkbox path="customer.isCustomer"/>
				</div>
			</div>
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa chỉ</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea path="customer.orgAddr" rows="2" maxLength="300"/>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Quốc tịch</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:select class="form-control" path="customer.nationality.id" title="Chọn quốc tịch">
						<option value="">- Chọn -</option>
						<c:forEach items="#{dsNationality}" var="item">
							<option value="${item.id}">
								<c:out value="${item.description}" />
							</option>
						</c:forEach>
					</form:select>
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã số thuế</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.taxCode" />
				</div>


			</div>

			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Giấy phép kinh doanh</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.businessLicenseNo"/>
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngành nghề kinh doanh</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.bissType" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.email" cssClass="email" />
				</div>

				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Điện thoại</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.phone" cssClass="" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Fax</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.fax" />
				</div>

				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày thành lập</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.strDateOfEstalishment" cssClass="toCurrentDate" />
				</div>
			</div>
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea path="customer.description" rows="2" maxLength="300" />
				</div>
			</div>
			<div class="Header2">Người đại diện</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Họ tên</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.representerName" cssClass="" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">CCCD/CMT/HC</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.representerId" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nơi cấp CCCD/CMT/HC</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.representerIdIssuePlace" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày hết hạn CCCD/CMT/HC</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.strRepresenterIdExpireDate" cssClass="toCurrentDate" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày cấp CCCD/CMT/HC</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.strRepresenterIdDate" cssClass="toCurrentDate" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.representerEmail" cssClass="email" />
				</div>
			</div>

			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa chỉ</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.representerAddress" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Điện thoại</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="customer.representerPhone" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>

			</div>

			
		</tiles:putAttribute>

	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				if ($('#to').val() == 'select' && $('#cuCode').val().trim().length == 0)
					notSearchWhenStart = true;
				if ($('#to').val() == 'select') {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Chọn'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã khách hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tên doanh nghiệp'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Quốc tịch'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Người đại diện'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Ngày sinh/Ngày thành lập'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Email'
					} ];
				} else {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã khách hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tên doanh nghiệp'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Quốc tịch'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Người đại diện'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Ngày thành lập'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Email'
					} ];
				}

			}

			$(document).ready(function() {
				$('.btnDtDelete').hide();

			});
			function beforeLoad() {
				var url = window.location.href;
				var tmp = url.split('?');
				if (tmp.length > 1) {
					$('#divDetail').css('display', 'none');
					$('#divGrid').css('display', 'block');
				} else {
					$('#divDetail').css('display', 'none');
					$('#divGrid').css('display', 'block');

				}
			}

			/* function cancel() {
				var url = window.location.href;
				var tmp = url.split('?');
				if (tmp.length > 1) {
					window.close();
				} else {
					$('#divGrid').css('display', 'inline');
					$('#divDetail').css('display', 'none');
				}

			} */

			function instanceSuccess() {
				alert(thuc_hien_thanh_cong, function() {
					window.close();
				});
			}
			function select(id) {
				window.opener.setCustomer(id);
				window.close();
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



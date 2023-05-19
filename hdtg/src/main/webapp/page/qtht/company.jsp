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
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh sách công ty thành viên" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/company" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="codeSearch"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="nameSearch"></form:input>
						</div>
					</div>
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm" />
					</div>
					<!-- <div align="center" class="HeaderText">&#8203;</div> -->

				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="company.id" id="id" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã công ty<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="company.code" cssClass="required" title="Mã không được để trống" />
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên viết tắt</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="company.sortName" />

				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên công ty<font color="red">*</font>
				</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea path="company.name" cssClass="required" />
				</div>

			</div>
			<div class="Row">

				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Địa chỉ<font color="red">*</font>
				</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea path="company.address" cssClass="required" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="company.email" />
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số điện thoại</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="company.phoneNumber" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Giấy chứng nhận kinh doanh</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="company.bssRegCert" />
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã công ty'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên công ty'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên viết tắt'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Địa chỉ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Sô điện thoại'
				} ];
			}
			$(document).ready(function() {
				$('.btnDtDelete').hide();
			});
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



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
	<tiles:putAttribute name="title" value="Tạo lệnh sản xuất" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/lsx" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã khách hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="cusCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên khách hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="cusName" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã đơn hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orderCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="drawingCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên chi tiết</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="qiName" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="manageCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					</div>
					<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn hàng từ ngày</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="fromDateStr" id="fromDate" readonly="true" class="date"></form:input>
					</div><div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="toDateStr" id="toDate" readonly="true" class="date"></form:input>
					</div>
				</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái phân công</div>
						<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
							<form:radiobutton path="assignStatus" value="" label="Tất cả" />
							<form:radiobutton path="assignStatus" value="-1" label="Chưa tạo lệnh sản xuất" />
							<form:radiobutton path="assignStatus" value="0" label="Đang tạo lệnh sản xuất" />
							<form:radiobutton path="assignStatus" value="1" label="Đã tạo lệnh sản xuất" />
						</div>
					</div>

					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm" />
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã đơn hàng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Khách hàng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã bản vẽ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Công đoạn'
				}, {
					"sClass" : "right",
					"bSortable" : false,
					"sTitle" : 'Số lượng chi tiết'
				}, {
					"sClass" : "right",
					"bSortable" : false,
					"sTitle" : 'Số lượng chi tiết đã phân công'
				}, {
					"sClass" : "right",
					"bSortable" : false,
					"sTitle" : 'Số lượng chi tiết chưa phân công'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Phân công lập trình'
				} ];
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



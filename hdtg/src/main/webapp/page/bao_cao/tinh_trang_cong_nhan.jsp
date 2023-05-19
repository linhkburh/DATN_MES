<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>

<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title" value="Tình trạng công nhân" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/workerSts" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
						<div class="Table" id="divSearchInf">
						<div class="HeaderText">Thông tin tìm kiếm</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã công nhân</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="woCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Họ tên</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="naWorker"/>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian từ ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="frDate" cssClass="date"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="toDate"  cssClass="date"/>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="drCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã công đoạn</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="opCode"/>
							</div>
						</div>
						<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã máy</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="maCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái gia công</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="machiningSts">
									<form:option value="">Tất cả</form:option>
									<form:option value="-1">Chậm tiến độ</form:option>
									<form:option value="0">Đúng tiến độ</form:option>
									<form:option value="1">Vượt tiến độ</form:option>
									<form:option value="-3">Chưa tạo LSX</form:option>
									<form:option value="-2">Chưa thực hiện sản xuất</form:option>
								</form:select>
							</div>
						</div>
					</div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>
				        		    
			        	
					</div>
				<div class="HeaderText">Kết quả tìm kiếm</div>
				<c:set var="customDivDataTable" scope="request" value="true" />
				<div id="divDatatable" class="table-responsive">
		            <table id="tblSearchResult" class="table">
						<thead>
							<tr>
								<th>STT</th>
								<th>Công nhân</th>
								<th>Mã lệnh sản xuất</th>
								<th>Số lượng chi tiết</th>
								<th>Thời gian TB chi tiết</th>
								<th>Tổng thời gian</th>
								<th>Thời gian thực tế</th>
								<th>Chậm(phút)</th>
								<th>Chậm(%)</th>
								<th>Tình trạng gia công</th>
							</tr>
						</thead>
					</table>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
			<div class="box-custom">
				
		        
			</div>
	</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){
				tblCfg.bFilter = false;
				// Doan nay can thiet de style
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "right",
					"bSortable" : false
				} ];
				tblCfg.notSearchWhenStart=false;
				tblCfg.buttons = [{
					text : '&#xf019; Export dữ liệu', 
					attr : {
						id : 'exportExcel'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						print();
					}
				} ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			     $('.btnDtAdd').hide();
			});
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

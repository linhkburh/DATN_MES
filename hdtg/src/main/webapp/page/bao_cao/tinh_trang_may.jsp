<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title" value="Tình trạng máy" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/machineSts" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
						<div class="Table" id="divSearchInf">
						<div class="HeaderText">Thông tin tìm kiếm</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã máy</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="maCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên máy</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="maName"/>
							</div>
						</div>
					</div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
					<div class="HeaderText">Kết quả tìm kiếm</div>
				<c:set var="customDivDataTable" scope="request" value="true" />
				<div id="divDatatable" class="table-responsive">
		            <table id="tblSearchResult" class="table">
						<thead>
							<tr>
								<th rowspan='2'>STT</th>
								<th rowspan='2'>Mã máy</th>
								<th colspan='2' style='text-align: center'>Lệnh sản xuất</th>
								<th colspan='3' style='text-align: center'>Đã thực hiện</th>
								<th colspan='2' style='text-align: center'>Còn lại</th>
								<th rowspan='2' style='text-align: center'>Trạng thái</th>
							</tr>
							<tr>
								<th>Số lượng</th>
								<th>Tổng thời gian</th>
								<th>Thời gian theo kế hoạch</th>
								<th>Thời gian thực tế</th>
								<th>Chậm so với kế hoạch</th>
								<th>Theo kế hoạch (phút)</th>
								<th>Thực tế (phút)</th>
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
				}, {
					"sClass" : "right",
					"bSortable" : false
				} ];
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
	
	

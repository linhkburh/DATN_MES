<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title" value="Tình trạng lệnh sản xuất" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/workSts" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
						<div class="Table" id="divSearchInf">
						<form:hidden path="quotationId"/>
						<div class="HeaderText">Thông tin tìm kiếm</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã đơn hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="orderCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="cusName"/>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã lệnh SX</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="workCode" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="drawingCode" />
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã công đoạn</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="stageCode" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã máy</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="machineCode" />
							</div>
						</div>
						<div class="row">
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
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái LSX</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="workSts">
									<form:option value="">Tất cả</form:option>
									<form:option value="-1">Còn thời hạn</form:option>
									<form:option value="0">Đã hoàn thành</form:option>
									<form:option value="1">Hết thời hạn</form:option>
								</form:select>
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
								<th rowspan='2'>Mã lệnh sản xuất</th>
								<th colspan='3' style='text-align: center'>Chi tiết</th>
								<th colspan='5' style='text-align: center'>Đã gia công</th>
								<th colspan='3' style='text-align: center'>Còn lại</th>
								<th rowspan='2'>Trạng thái gia công</th>
								<th rowspan='2'>Trạng thái LSX</th>
							</tr>
							<tr>
								<th>Số lượng</th>
								<th>Thời gian TB chi tiết</th>
								<th>Tổng thời gian</th>
								<th>Số lượng</th>
								<th>Thời gian theo kế hoạch</th>
								<th>Thời gian thực tế</th>
								<th>Chậm so với kế hoạch (phút)</th>
								<th>Chậm so với kế hoạch (%)</th>
								<th>Số lượng</th>
								<th>Thời gian còn so với kế hoạch</th>
								<th>Thời gian còn so với thực tế</th>
							</tr>
						</thead>
					</table>
				</div>
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
					"sClass" : "left",
					"bSortable" : false
				} , {
					"sClass" : "left",
					"bSortable" : false
				}];
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
	
	

<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title" value="Tình trạng đơn hàng" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/orderSts" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
						<div class="Table" id="divSearchInf">
						<div class="HeaderText">Thông tin tìm kiếm</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="cusCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="cusName"/>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Từ ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="orderFrom" class="date"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="orderTo" class ="date"/>
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
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái đơn hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="orderSts">
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
					<div>
					<div class="HeaderText">Kết quả tìm kiếm</div>
						<c:set var="customDivDataTable" scope="request" value="true" />
				<div id="divDatatable" class="table-responsive">
		            <table id="tblSearchResult" class="table">
						<thead>
							<tr>
								<th rowspan='2'>STT</th>
								<th rowspan='2'>Mã đơn hàng</th>
								<th rowspan='2' style="width: 160px">Tên khách hàng</th>
								<th colspan='2' style='text-align: center'>Bản vẽ</th>
								<th colspan='4' style='text-align: center'>Đã thực hiện</th>
								<th colspan='2' style='text-align: center'>Còn lại</th>
								<th colspan='2' style='text-align: center'>Trạng thái</th>
								<th rowspan='2' style="width: 90px"></th>
							</tr>
							<tr>
								<th>Số lượng bản vẽ</th>
								<th>Tổng thời gian</th>
								<th>Thời gian theo kế hoạch</th>
								<th>Khối lượng (%)</th>
								<th>Thời gian thực tế</th>
								<th>Chậm (phút)</th>
								<th>Theo kế hoạch (phút)</th>
								<th>Thực tế (phút)</th>
								<th>Gia công</th>
								<th>Đơn hàng</th>
							</tr>
						</thead>
					</table>
				</div>
					</div>
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
				}, {
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",
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
			
			function detailLSX(orderId) {
				if (orderId === '')
					return;
				window.open('workSts?orderId='+ orderId,'',window_property);
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

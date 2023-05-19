<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
#divDatatable tbody td {
	font-size: 11;
	padding: 0.2rem !important;
}

#divDatatable thead th {
	padding: 0.2rem !important;
}

#divDatatable thead tr.level2 th {
	font-size: 11 !important;
}
</style>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title" value="Công nhân sản xuất" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/workerSts" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText">Thông tin tìm kiếm</div>

					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian từ ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="frDate" cssClass="date" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="toDate" cssClass="date" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="company" id='company' onchange="changeCompany()">
								<option value="">- Chọn -</option>
								<c:forEach items="#{companies}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã công đoạn</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="opCode" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell">Ca làm việc</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12 cell">
							<form:select path="shift">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstShift}" var="item">
									<option value="${item.id}">
										<c:out value="${item.value}" />
									</option>
								</c:forEach>
							</form:select>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell">Xưởng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12 cell">
							<form:select path="factoryUnit">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstFactoryUnit}" var="item">
									<option value="${item.id}">
										<c:out value="${item.code}" />
									</option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công nhân</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="woCode">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstWorker}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.username}" />-
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>
				</div>
				<div class="divaction" align="center">
					<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm" />
				</div>


			</div>
			<div class="HeaderText">Kết quả tìm kiếm</div>
			<c:set var="customDivDataTable" scope="request" value="true" />
			<div id="divDatatable" class="table-responsive">
				<table id="tblSearchResult" class="table">

					<thead>
						<tr>
							<th rowspan='2'>STT</th>
							<th rowspan='2'>Công nhân</th>
							<th colspan='10' style='text-align: center'>Sản xuất</th>
							<th colspan='2' style='text-align: center'>Sửa hàng</th>
							<th colspan='6' style='text-align: center'>Nguội</th>
							<th colspan='2' style='text-align: center'>QC</th>
							<th rowspan='2'>Tổng thời gian</th>
						</tr>
						<tr class="level2">
							<!-- San xuat -->
							<th>Số lượng</th>
							<th>Hoàn thành (OK)</th>
							<th>Hỏng (NG)</th>
							<th>Thời gian dự kiến</th>
							<th>Thời gian setup</th>
							<th>Thời gian thực hiện</th>
							<th>Chậm (phút)</th>
							<th>Chậm (%)</th>
							<th>Số lượng sửa</th>
							<th>Thời gian sửa</th>
							<!-- Sua hang -->
							<th>Số lượng</th>
							<th>Thời gian thực hiện</th>
							<!-- Nguoi -->
							<th>Số lượng</th>
							<th>Hoàn thành (OK)</th>
							<th>Hỏng (NG)</th>
							<th>Thời gian thực hiện</th>
							<th>Số lượng sửa</th>
							<th>Thời gian sửa</th>
							<!-- QC -->
							<th>Số lượng</th>
							<th>Thời gian thực hiện</th>
						</tr>
					</thead>
				</table>
			</div>
			<%@ include file="/page/include/data_table.jsp"%>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<div class="box-custom"></div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
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
				} ];
				tblCfg.notSearchWhenStart = false;
				tblCfg.buttons = [ {
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
			function changeCompany(){
				if($('#company').val().trim().length==0){
					$('#woCode').empty();
					$('#factoryUnit').empty();
					return;
				}
				$.ajax({
					url:'quanlyLSX?method=loadResource',
					data:{
						"companyId":$('#company').val()
					},
					success:function(company, xhr, status){
						// Danh sach NSD
						var data = $.map(company.lstSysUsers, function (obj) {
		                      obj.text = obj.username + '-' +obj.name; 
		                      return obj;
		                    });
						$('#woCode').empty();
						$('#woCode').append('<option value="">- Chọn -</option>');
						$('#woCode').select2({data: data});
						// Danh sach may moc
						var data = $.map(company.lstBssFactoryInit, function (obj) {
		                      obj.text = obj.code; 
		                      return obj;
		                    });
						$('#factoryUnit').empty();
						$('#factoryUnit').append('<option value="">- Chọn -</option>');
						$('#factoryUnit').select2({data: data});
					}
				});
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



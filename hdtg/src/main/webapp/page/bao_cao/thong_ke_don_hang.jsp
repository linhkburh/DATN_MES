<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
#su label {
	font-size: 15px !important;
	font-weight: 500 !important;
	margin-left: 10px !important;
	margin-right: 10px !important;
}

div.text {
	padding-left: 0;
	color: darkblue;
	font-size: 12;
	vertical-align: top;
	font-weight: bold;
}

.dataTables_filter {
	display: none;
}

#divDatatable tbody td {
	font-size: 11;
	padding: 0.2rem !important;
}

#divDatatable thead th {
	font-size: 12;
	padding: 0.2rem !important;
}

#divDatatable thead th.subHeader {
	font-size: 11;
	padding: 0.1rem !important;
}
</style>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title" value="Tình trạng sản xuất" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/bookingSts" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="formDataModelAttr"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText">Thông tin tìm kiếm</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại báo cáo</div>
						<form:hidden path="fistTime" />
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="type" onchange="$('#theForm').submit()" id="type">
								<form:option value="qi">Tình trạng sản xuất</form:option>
								<form:option value="drw">Tình trạng sản xuất theo bản vẽ</form:option>
								<form:option value="q">Tình trạng sản xuất theo đơn hàng</form:option>
								<form:option value="cus">Tình trạng sản xuất theo khách hàng</form:option>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty thành viên</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="company" id='company'>
								<c:forEach items="#{companies}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khách hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="customerCode" id="customerCode">
								<form:option value="" label="- Chọn -" />
								<c:forEach items="#{customers}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.code}" />-<c:out value="${item.orgName}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Nhập đúng mã đơn hàng">Mã đơn hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" title="Nhập đúng mã đơn hàng">
							<form:input path="orderCode" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Ngày tạo mã quản lý">Ngày tạo, từ
							ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" title="Ngày tạo mã quản lý">
							<form:input path="frDate" cssClass="date" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Ngày tạo mã quản lý"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Ngày tạo mã quản lý">Đến</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="toDate" cssClass="date" />
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Bộ phận</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" id="su">
							<form:radiobuttons path="source" items="${lstSource}" itemLabel="name" itemValue="id"></form:radiobuttons>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.draw" />
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
							<form:input path="code" id="code"></form:input>
						</div>
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text">
							<spring:message code="QuotationItem.search.info" />
						</div>
					</div>

					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.manager" />
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
							<form:input path="manageCodeSearch" />
						</div>
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text" style="font-size:">
							<spring:message code="QuotationItem.search.info" />
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
								<th rowspan="2">STT</th>
								<th rowspan="2">Mã khách hàng</th>
								<c:if test="${formDataModelAttr.type!='cus'}">
									<c:if test="${formDataModelAttr.type!='q'}">
										<th rowspan="2">Mã bản vẽ</th>
									</c:if>
									<c:if test="${formDataModelAttr.type=='q'}">
										<th rowspan="2">Mã đơn hàng</th>
									</c:if>
								</c:if>
								<c:if test="${formDataModelAttr.type=='qi'}">
									<th rowspan="2">Mã quản lý</th>
								</c:if>

								<c:if test="${formDataModelAttr.type!='qi'}">
									<th rowspan="2">Số lượng bản vẽ</th>
								</c:if>
								<th colspan='4' style='text-align: center'>Đặt hàng</th>
								<th colspan='6' style='text-align: center'>Sản xuất</th>
								<th colspan='2' style='text-align: center'>Nguội</th>
								<th colspan='2' style='text-align: center'>QC</th>
								<th colspan='2' style='text-align: center'>Sửa hàng</th>
								<th colspan='6' style='text-align: center'>Còn lại</th>
							</tr>
							<tr>
								<!-- Dat hang -->
								<th class="subHeader">Số lượng chi tiết</th>
								<th class="subHeader">Thời gian setup <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Thời gian gia công target <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Thời gian gia công dự kiến <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<!--thuc hien -->
								<th class="subHeader">Số lượng chi tiết</th>
								<th class="subHeader">Thời gian lập trình <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Thời gian setup <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader" title="Tính theo báo giao">Thời gian gia công target <c:if
										test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader" title="Tính theo kế hoạch">Thời gian gia công dự kiến <c:if
										test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader" title="Thời gian thực tế">Thời gian gia công thực tế <c:if
										test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<!-- Nguội -->
								<th class="subHeader">Thời gian gia công <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Số lượng chi tiết</th>
								<!-- QC -->
								<th class="subHeader">Thời gian gia công <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Số lượng chi tiết</th>
								<!-- Sua hang -->
								<th class="subHeader">Thời gian gia công <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Số lượng chi tiết</th>
								<!-- Con lai -->
								<th class="subHeader">Số lượng chi tiết</th>
								<th class="subHeader">Theo dự kiến <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
								<th class="subHeader">Theo thực tế <c:if test="${formDataModelAttr.type=='qi'}">(giờ)</c:if>
									<c:if test="${formDataModelAttr.type!='qi'}">(giờ)</c:if></th>
							</tr>
						</thead>
					</table>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
			</div>
		</tiles:putAttribute>

	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				} ];

				// Ma ban ve hoac ma don hang
				if ($('#type').val() != 'cus')
					tblCfg.aoColumns.push({
						"sClass" : "left",
						"bSortable" : false
					});
				// Ma quan ly
				if ($('#type').val() == 'qi') {
					tblCfg.aoColumns.push({
						"sClass" : "left",
						"bSortable" : false
					});
				} else {
					// So luong ban ve
					tblCfg.aoColumns.push({
						"sClass" : "right",
						"bSortable" : false
					});
				}

				// Cac truong con lai
				tblCfg.aoColumns.push({
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
				});

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
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



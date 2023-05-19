<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.dataTables_filter {
	display: none;
}

.error {
	color: red !important;
}

#divDatatable tbody td {
	font-size: 12 !important;
	padding: 0.3rem !important;
}

#divDatatable thead th {
	padding: 0.3rem !important;
}
</style>

<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		Lịch sử nhận hàng gia công ngoài
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/receivingHis" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			
			<div id="divGrid" align="left">
				<div class="HeaderText">
						<spring:message code="common.info.search" />
					</div>


					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.code.draw" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="drawingCode" id="drawingCode"></form:input>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.code.manager" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="manageCodeSearch" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm nhận hàng, từ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="frDate" id="frDate" class="date" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.date.to" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="tDate" id="tDate" class="date" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị gia công ngoài</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="cusId">
								<option value="">-
									<spring:message code="common.combobox.select" /> -
								</option>
								<c:forEach items="#{lstPartner}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.orgName}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Cán bộ nhập liệu</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="workerSearch">
								<option value="">-
									<spring:message code="common.combobox.select" /> -
								</option>
								<c:forEach items="#{lstSysUser}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.username}" />-<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();"
							value="&#xf002; <spring:message code="common.btn.search"/>" />
					</div>
					<div class="HeaderText">
						<spring:message code="common.search.results" />
					</div>
					<c:set var="customDivDataTable" scope="request" value="true" />
					<div id="divDatatable" class="table-responsive">
						<table id="tblSearchResult" class="table">
							<thead>
								<tr>
									<th rowspan='2'>STT</th>
									<th rowspan='2'>Mã bản vẽ</th>
									<th rowspan='2'>Mã quản lý</th>
									<th rowspan='2'>Loại QC</th>
									<th rowspan='2'>Số lượng đã chuyển</th>
									<th colspan='4' style='text-align: center;'>Nhận hàng</th>
									<th rowspan='2'>Đơn vị gia công ngoài</th>
									<th rowspan='2'>Thời điểm nhận hàng từ</th>
									<th rowspan='2'>Thời điểm nhận hàng đến</th>
									<th rowspan='2'>Cán bộ nhập liệu</th>
									<th rowspan='2'>Cập nhật</th>
								</tr>
								<tr>
									<th style="font-size: 12px !important;">Tổng</th>
									<th style="font-size: 12px !important;">Hoàn thành (OK)</th>
									<th style="font-size: 12px !important;">Lỗi (NG)</th>
									<th style="font-size: 12px !important;">Hủy</th>
								</tr>
							</thead>
						</table>
					</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendReportAction" value="/historyPro/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
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
					"sClass" : "left",
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
				}, {
					"sClass" : "left",
					"bSortable" : false
				} ];
			}
			$(document).ready(
					function() {
						$('.btnDtAdd').hide();
						// Dialog
						$('input[name="qcIn.ngAmount"], input[name="qcIn.amount"], input[name="qcIn.totalAmount"]').on(
								'change', function() {
									var amount = $('input[name="qcIn.amount"]').val().convertStringToNumber();
									var totalAmout = $('input[name="qcIn.totalAmount"]').val().convertStringToNumber();
									var ngAndbroken = totalAmout - amount;
									if ($(this).attr('id') == 'qcIn.ngAmount') {
										var ngAmout = $('input[name="qcIn.ngAmount"]').val().convertStringToNumber();
										var brokenAmount = ngAndbroken - ngAmout;
										$('input[name="qcIn.brokenAmount"]').val(formatSo(brokenAmount));
										return;
									}
									$('input[name="qcIn.ngAmount"]').val(formatSo(ngAndbroken));
									$('input[name="qcIn.brokenAmount"]').val(0);
								});
					});

			function delReceivingHis() {
				jConfirm('Bạn có chắn chắn muốn xóa lịch sử nhận hàng này?', 'OK', 'Cancel', function(r) {
					if (r) {
						$.ajax({
							url : 'receivingHis?method=delReceivingHis',
							type : 'POST',
							async : false,
							data : {
								qcInId : $('input[name="qcIn.id"]').val()
							},
							method : 'GET',
							success : function(data) {
								if (data === '') {
									alert(thuc_hien_thanh_cong, function() {
										window.location.reload();
									});
								} else {
									alert(data);
								}

							}
						});
					}
				});
			}
			//binding du lieu ra popup
			function edit(id, type) {
				$.loader({
					className : "blue-with-image-2"
				});
				clearDiv('modalChangeQcInHistory');
				var tokenIdKey = $('#tokenIdKey').val();
				var tokenId = $('#tokenId').val();
				$.getJSON("receivingHis?method=edit", {
					"id" : id,
					"tokenIdKey" : tokenIdKey,
					"tokenId" : tokenId
				}).done(function(res, xhr, status) {
					if (type == 'qi') {
						$('.wo').css('display', 'none');
						$('.wo').prop('disabled', true);
						$('.qi').css('display', 'flex');
						$('.qi').prop('disabled', false);
					} else {
						$('.wo').css('display', 'flex');
						$('.wo').prop('disabled', false);
						$('.qi').css('display', 'none');
						$('.qi').prop('disabled', true);
					}
					$('#modalChangeQcInHistory').modal('show');
					binding(res, 'modalChangeQcInHistory');
				});
			}
			function changeQcIn(idQcOs,idQcIn){
				window.open('qcIn?hiddenMenu=true&idQcOs='+idQcOs+'&idQcIn='+idQcIn,'','width=1000, height=700, status=yes, scrollbars=yes').focus();
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



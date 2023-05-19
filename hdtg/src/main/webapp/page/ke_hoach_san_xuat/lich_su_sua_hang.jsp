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
<div id="modalChangeProductionHistory" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Thay đổi thông tin</h4>
			</div>
			<div class="modal-body">
				<div class='Table'>
					<form id="editForm">
						<input type="hidden" name="idRepaire">
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Thời điểm bắt đầu<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="startTimeChange" class="dateTime required">
							</div>

						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Thời điểm kết thúc<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="endTimeChange" class="dateTime required">
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng chi tiết<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="totalAmountChange" class="textint required">
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng chi tiết hoàn thành (OK)<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="amountChange" class="textint required">
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Số lượng chi tiết hủy</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="ngAmountChange" class="number" readonly="readonly">
							</div>
						</div>
						<div class=Row>
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Máy gia công</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<select name="machineID">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstAstMachine}" var="item">
										<option value="${item.id}">
											<c:out value="${item.astName}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Công nhân<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<select name="userChange" class="required">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUser}" var="item">
										<option value="${item.id}">
											<c:out value="${item.username}" />-
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Lý do lỗi</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<textarea rows="3" name="ngDesChange"></textarea>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="changeRepaireHistory();">Lưu</button>
				<button type="button" onclick="delRepaireHistory()" class="btn red fa">Xóa</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		Lịch sử sửa hàng
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/repaireHistory" var="formAction" />
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
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="HistoryPro.order.date.form" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="fromDate" id="fromDate" class="date"></form:input>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="HistoryPro.order.date.to" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="toDate" id="toDate" class="date"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="HistoryPro.date.from" />
					</div>
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
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="HistoryPro.code.order" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="orderCode" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="HistoryPro.worker" />
					</div>
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
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="HistoryPro.updator" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="updatorSearch">
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
								<th rowspan='2' width="2%">STT</th>
								<th rowspan='2'>Mã bản vẽ</th>
								<th rowspan='2'>Mã quản lý</th>
								<th colspan='3' style='text-align: center'>Số lượng chi tiết cần sửa</th>
								<th rowspan='2'>Công nhân</th>
								<th rowspan='2'>Thời điểm bắt đầu</th>
								<th rowspan='2'>Thời điểm kết thúc</th>
								<th rowspan='2'>Thời gian thực hiện (phút)</th>
								<th colspan='3' style='text-align: center'>Số lượng chi tiết</th>
								<th rowspan='2'>Cán bộ nhập liệu</th>
								<th rowspan='2' width="2%">Cập nhật</th>
							</tr>
							<tr>
								<th style="font-size: 12px !important;">Tổng</th>
								<th style="font-size: 12px !important;">Đã sửa</th>
								<th style="font-size: 12px !important;">Còn lại</th>
								<!-- Da thuc hien -->
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.quantity" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.done" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.fail" /></th>
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
				} ];
			}

			function changeRepaireHistory() {
				if (!$('#editForm').valid())
					return;
				var ngAmout = $('input[name="ngAmountChange"]').val().convertStringToNumber();
				if (Math.round(ngAmout) != ngAmout) {
					alert('Số lượng chi tiết lỗi phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
					return;
				}
				$.ajax({
					url : 'repaireHistory?method=addRepaire',
					type : 'POST',
					async : false,
					data : {
						repaireId : $('input[name="idRepaire"]').val(),
						startTime : $('input[name="startTimeChange"]').val(),
						endTime : $('input[name="endTimeChange"]').val(),
						amount : $('input[name="amountChange"]').val(),
						userid : $('select[name="userChange"]').val(),
						ngAmount : $('input[name="ngAmountChange"]').val(),
						ngDes : $('textarea[name="ngDesChange"]').val(),
						totalAmount : $('input[name="totalAmountChange"]').val(),
						machineID : $('select[name="machineID"]').val()
					},
					method : 'GET',
					success : function(data) {
						if (data === '') {
							alert(thuc_hien_thanh_cong, function() {
								$('#modalChangeProductionHistory').modal('hide');
								findData();
							});
						} else {
							alert(data);
						}

					}
				});
			}

			$(document).ready(function() {
				$('.btnDtAdd').hide();
				// Dialog
				$('input[name="totalAmountChange"], input[name="amountChange"]').on('change', function() {
					var amount = $('input[name="totalAmountChange"]').val().convertStringToNumber();
					var totalAmout = $('input[name="amountChange"]').val().convertStringToNumber();
					if ((amount - totalAmout) < 0) {
						alert('Số lượng chi tiết thành (OK) phải <= số lượng chi tiết sửa !');
						$('input[name="amountChange"]').val(null);
						$('input[name="ngAmountChange"]').val(null);
					} else
						$('input[name="ngAmountChange"]').val(formatSo(amount - totalAmout));
				});
			});

			function delRepaireHistory() {
				jConfirm('Bạn có chắn chắn muốn xóa lịch sử sửa hàng này?', 'OK', 'Cancel', function(r) {
					if (r) {
						$.ajax({
							url : 'repaireHistory?method=delRepaireHistory',
							type : 'POST',
							async : false,
							data : {
								repaireID : $('input[name="idRepaire"]').val()
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
			function editProductionHistory(idExe, startTime, endTime, amount, userId, totalAmount, ngAmount, ngDes,
					machineID) {
				$('input[name="idRepaire"]').val(idExe);
				$('input[name="amountChange"]').val(amount);
				$('input[name="totalAmountChange"]').val(totalAmount);
				$('input[name="ngAmountChange"]').val(ngAmount);
				$('textarea[name="ngDesChange"]').val(ngDes);
				$('input[name="startTimeChange"]').val(startTime);
				$('input[name="endTimeChange"]').val(endTime);
				$('select[name="userChange"]').val(userId);
				$('select[name="userChange"]').select2();
				$('select[name="machineID"]').val(machineID);
				$('select[name="machineID"]').select2();
				$('#modalChangeProductionHistory').modal('show');
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



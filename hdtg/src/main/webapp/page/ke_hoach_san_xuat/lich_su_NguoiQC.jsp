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
						<input type="hidden" name="idWorkExe">
						<c:if test="${param.type=='CL'}">
							<div class="row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Sửa NG</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<input type="checkbox" name="ngRepaire" id="mdngRepaire" disabled="disabled" />
								</div>
							</div>
						</c:if>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Ngày bắt đầu<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="startTimeChange" class="dateTime required">
							</div>

						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Ngày kết thúc<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="endTimeChange" class="dateTime required">
							</div>
						</div>
						<div class="Row total">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Tổng số chi tiết<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="totalAmountChange" class="textint required" id="mdtotalAmountStr">
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng hoàn thành (OK)<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="amountChange" class="textint required" id="mdsoLuongChiTietDetail">
							</div>
						</div>



						<%-- <c:if test="${param.type == 'QC'}"> --%>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng cần sửa (NG)<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="amountRp" class="textint required" id="mdngAmountStr">
							</div>
						</div>
						<div class="Row total">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Số lượng hủy</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="ngAmountChange" class="number" readonly="readonly" id="mdbrokenAmountStr">
							</div>
						</div>
						<%-- </c:if> --%>
						<%-- <c:if test="${param.type == 'CL'}">
							<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng cần sửa (NG)<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="amountRp" class="textint required" id="mdngAmountStr" readonly="true">
							</div>
						</div>
						</c:if> --%>
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
				<button type="button" class="btn blue" onclick="changeProductionHistory();">Lưu</button>
				<button type="button" onclick="delWorkOrderExe()" class="btn red fa">Xóa</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		<c:if test="${param.type == 'CL'}">
        	 Lịch sử gia công nguội
    	</c:if>
		<c:if test="${param.type == 'QC'}">
    		Lịch sử QC
    	</c:if>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/processExe" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<form:hidden path="type" id="type" />
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText">
						<spring:message code="common.info.search" />
					</div>
					<c:if test="${param.type=='QC'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại QC</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="hisType" onchange="$('#theForm').attr('action','osHistory').submit()">
									<form:option value="hQc">Thành phẩm</form:option>
									<form:option value="hQcO">Bán thành phẩm</form:option>
								</form:select>
							</div>
						</div>
					</c:if>

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
							<form:input path="manageCode" />
						</div>
					</div>
					<%-- <div class="row">
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
					</div> --%>
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
							<spring:message code="HistoryPro.worker" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="worker">
								<option value="">-
									<spring:message code="common.combobox.select" /> -
								</option> 
								<c:forEach items="#{lstSysUser}" var="item">
									<c:if test=""></c:if>
									<form:option value="${item.id}">
										<c:out value="${item.username}" />-<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
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
					<%-- <div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.code.order" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orderCode" />
						</div>
						
					</div> --%>
					
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();"
							value="&#xf002; <spring:message code="common.btn.search"/>" />
					</div>
				</div>
				<div class="HeaderText">
					<spring:message code="common.search.results" />
				</div>
				<c:set var="customDivDataTable" scope="request" value="true" />
				<div id="divDatatable" class="table-responsive">
					<c:if test="${param.type=='CL'}">
						<table id="tblSearchResult" class="table">
							<thead>
								<tr>
									<th rowspan='2' width="2%">STT</th>
									<th rowspan='2'>Mã bản vẽ</th>
									<th rowspan='2'>Mã quản lý</th>
									<th rowspan='2'>Công nhân</th>
									<th rowspan='2'>Thời điểm bắt đầu</th>
									<th rowspan='2'>Thời điểm kết thúc</th>
									<th colspan='4' style='text-align: center'>Thời gian thực hiện (phút)</th>
									<th colspan='4' style='text-align: center'>Tổng số chi tiết</th>
									<th rowspan='2'>Cán bộ nhập liệu</th>
									<th rowspan='2'>Loại gia công</th>
								</tr>
								<tr>
									<!-- Thoi gian thuc hien -->
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.time.plan" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.time.real" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.slow.per" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.slow.minute" /></th>
									<!-- Da thuc hien -->
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.quantity" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.done" /></th>
									<th style="font-size: 12px !important;">Số lượng cần sửa (NG)</th>
									<th style="font-size: 12px !important;">Hủy</th>
								</tr>
							</thead>
						</table>
					</c:if>
					<c:if test="${param.type=='QC'}">
						<table id="tblSearchResult" class="table">
							<thead>
								<tr>
									<th rowspan='2' width="2%">STT</th>
									<th rowspan='2'>Mã bản vẽ</th>
									<th rowspan='2'>Mã quản lý</th>
									<th rowspan='2'>Công nhân</th>
									<th rowspan='2'>Thời điểm bắt đầu</th>
									<th rowspan='2'>Thời điểm kết thúc</th>
									<th colspan='4' style='text-align: center'>Thời gian thực hiện (phút)</th>
									<th colspan='4' style='text-align: center'>Tổng số chi tiết</th>
									<th rowspan='2'>Cán bộ nhập liệu</th>
									<th rowspan='2' width="2%">Cập nhật</th>
								</tr>
								<tr>
									<!-- Thoi gian thuc hien -->
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.time.plan" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.time.real" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.slow.per" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.slow.minute" /></th>
									<!-- Da thuc hien -->
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.quantity" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.done" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.fail" /></th>
									<th style="font-size: 12px !important;">Hủy</th>
								</tr>
							</thead>
						</table>
					</c:if>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendReportAction" value="/historyPro/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				if ($('#type').val() === 'CL') {
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
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "right",//Thời gian setup
						"bSortable" : false
					}, {
						"sClass" : "right",//Thời điểm kết thúc
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
					} ];
				} else {
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
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "left",//Thời gian setup
						"bSortable" : false
					}, {
						"sClass" : "right",//Thời điểm kết thúc
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
					} ];
				}
				tblCfg.buttons = [{
					text : '&#xf019; <spring:message code="HistoryPro.table.export.data"/>', 
					attr : {
						id : 'exportExcel'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						exportExcel();
					}
				} ]

			}
			
			function exportExcel(){
				$.ajax({
			        url: 'processExe?method=exportExcel',
			        method: 'POST',
			        data: $('#theForm').serializeObject(),
			        xhrFields: {
			            responseType: 'blob'
			        },
			        success: function (data, status, xhr) {
			        	var contentType = xhr.getResponseHeader("content-type") || "";
			            if (contentType.indexOf('text/plan') > -1) {
			              outurl = URL.createObjectURL(data);
			              fetch(outurl)
			              .then(res => res.text()) 
			              .then(data => {
			            	  if(data=="Không tồn tại dữ liệu xuất!"){
			            		  alert(data, function(){
				            	  		findData();
				            	  	});
			            	  }
			            	  else alert(data);
			              });
			              return;
			            }
			            var a = document.createElement('a');
			            var url = window.URL.createObjectURL(data);
			            a.href = url;
			            a.download = 'lich_su_nguoi_qc.xlsx';
			            document.body.append(a);
			            a.click();
			            a.remove();
			            window.URL.revokeObjectURL(url);
			            findData();
			        },
			        error:function(){
			        }
			    });
			}

			function changeProductionHistory() {
				if (!$('#editForm').valid())
					return;
				var ngAmout =$('input[name="ngAmountChange"]').length>0? $('input[name="ngAmountChange"]').val().convertStringToNumber():"0";
				if (Math.round(ngAmout) != ngAmout) {
					alert('Số lượng chi tiết lỗi phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
					return;
				}
				$.ajax({
					url : 'processExe?method=addProcessExe',
					type : 'POST',
					async : false,
					data : {
						processExeId : $('input[name="idWorkExe"]').val(),
						startTime : $('input[name="startTimeChange"]').val(),
						endTime : $('input[name="endTimeChange"]').val(),
						amount : $('input[name="amountChange"]').val(),
						userid : $('select[name="userChange"]').val(),
						ngAmount : $('input[name="ngAmountChange"]').val(),
						ngDes : $('textarea[name="ngDesChange"]').val(),
						totalAmount : $('input[name="totalAmountChange"]').val(),
						rpAmount : $('input[name="amountRp"]').val(),
						type : $('#type').val()
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

			$(document)
					.ready(
							function() {
								$('.btnDtAdd').hide();
								// Dialog
								$('#mdsoLuongChiTietDetail,#mdtotalAmountStr,#mdngAmountStr,#mdngRepaire').on(
										'change',
										function() {
											if ($(this).attr('id') == 'mdngRepaire') {
												$('#mdngAmountStr').prop('disabled', $(this).prop('checked'));
												$('#mdngAmountStr').css('display',$(this).prop('checked') ? 'none' : 'inline');
											}
											// Cha ton tai tong so hoac so luong thanh cong
											if ($('#mdsoLuongChiTietDetail').val().trim().length == 0
													|| $('#mdtotalAmountStr').val().trim().length == 0)
												return;
											var amount = $('#mdsoLuongChiTietDetail').val().convertStringToNumber();
											var totalAmout = $('#mdtotalAmountStr').val().convertStringToNumber();
											if (amount == 0 && totalAmout == 0)
												return;
											var ngAndbroken = totalAmout - amount;
											// repaire
											if ($('#mdngRepaire').prop('checked')) {
												// kg co NG
												$('#mdbrokenAmountStr').val(formatSo(ngAndbroken));
												return;
											}
											// Thay doi so luong Ng
											if ($(this).attr('id') == 'mdngAmountStr') {
												var ngAmount = $('#mdngAmountStr').val().convertStringToNumber();
												var brokenAmount = ngAndbroken - ngAmount;
												$('#mdbrokenAmountStr').val(formatSo(brokenAmount));
												return;
											}
											// Mac dinh NG (da so la NG, truong hop da so)
											$('#mdngAmountStr').val(formatSo(ngAndbroken));
											$('#mdbrokenAmountStr').val(0);
										});
							});

			function delWorkOrderExe() {
				jConfirm('Bạn có chắn chắn muốn xóa lịch sử sản xuất này?', 'OK', 'Cancel', function(r) {
					if (r) {
						$.ajax({
							url : 'processExe?method=delWorkOrderExe',
							type : 'POST',
							async : false,
							data : {
								processExeId : $('input[name="idWorkExe"]').val()
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
				});
			}
			function editProductionHistory(idExe, startTime, endTime, amount, userId, totalAmount, ngAmount, rpAmount,
					ngDes, ngRepaire) {
				$('input[name="idWorkExe"]').val(idExe);
				$('input[name="amountChange"]').val(amount);
				$('input[name="totalAmountChange"]').val(totalAmount);
				$('input[name="ngAmountChange"]').val(ngAmount);
				$('textarea[name="ngDesChange"]').val(ngDes);
				$('input[name="startTimeChange"]').val(startTime);
				$('input[name="endTimeChange"]').val(endTime);
				$('input[name="amountRp"]').val(rpAmount);
				$('select[name="userChange"]').val(userId);
				$('select[name="userChange"]').select2();
				$('input[name="ngRepaire"]').prop('checked', "true"===ngRepaire);
				$('#modalChangeProductionHistory').modal('show');
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



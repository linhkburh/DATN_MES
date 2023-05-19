<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
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

.error {
	color: red !important;
}
#divDatatable tbody td{
	font-size: 12 !important;
	padding: 0.3rem !important;
}
#divDatatable thead th{
	padding: 0.3rem !important;
}
</style>
<div id="modalChangeOutSourcedHistory" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Thay đổi thông tin chuyển GCN</h4>
			</div>
			<div class="modal-body">
				<div class='Table'>
					<form id="editForm">
					<input type="hidden" name="qcOs.id"><!-- 
					<input type="hidden" name="qcosdetail.id"> -->
					<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Mã bản vẽ<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="qcOs.quotationItem.code" class="required" readOnly="true">
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Mã quản lý<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="qcOs.dsmaql" class="required" readOnly="true">
							</div>
						</div>
						<div class="Row wo">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Mã LSX<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="qcOs.workOrder.code" class="required" readOnly="true">
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="qcOs.workOrder.qcChkAmount" class="textint required wo" readOnly="true">
								<input type="text" name="qcOs.amount" class="textint required qi" readOnly="true">
							</div>
						</div>
						<div class="Row wo">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Tổng số lượng đã chuyển<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="qcOs.amount" class="textint required " readOnly="true">
							</div>
						</div>
						<div class=Row>
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Đơn vị gia công ngoài<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<select name="qcOs.customers.id" class="required" disabled="true">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstPartner}" var="item">
										<option value="${item.id}">
											<c:out value="${item.orgName}" />
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
								<select name="qcOs.sysUsers.id" class="required" disabled="true">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUser}" var="item">
										<option value="${item.id}">
											<c:out value="${item.username}" />-<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<!-- <button type="button" class="btn blue" onclick="changeOutSourcedHistory();">Lưu</button> -->
				<button type="button" onclick="delOutSourcedHistory()" class="btn red fa">Xóa</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		Lịch sử chuyển gia công ngoài
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/outSourced" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText"><spring:message code="common.info.search"/></div>
					
				<%-- 	
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
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="quotationItemCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên chi tiết</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="quotationItemName" />
						</div>
					</div> --%>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.draw" />
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
							<form:input path="drwingCode" id="drwingCode"></form:input>
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
							<form:input path="managerCode" />
						</div>
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text" style="font-size:">
							<spring:message code="QuotationItem.search.info" />
						</div>
					</div>
					<div class="row">
						
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị gia công ngoài</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="cus" >
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstPartner}" var="item">
										<option value="${item.id}">
											<c:out value="${item.orgName}" />
										</option>
									</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã LSX</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="workCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công nhân thực hiện</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="user" >
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUser}" var="item">
										<option value="${item.id}">
											<c:out value="${item.username}" />-<c:out value="${item.name}" />
										</option>
									</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày thực hiện, từ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="frDate" cssClass="date"/>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="tDate" cssClass="date"/>
						</div>
					</div>
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; <spring:message code="common.btn.search"/>" />
					</div>
					<div class="HeaderText"><spring:message code="common.search.results"/></div>
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
									<th colspan='3' style='text-align: center;'>Số lượng</th>
									<th rowspan='2'>Đơn vị gia công ngoài</th>
									<th rowspan='2'>Ngày chuyển</th>
									<th rowspan='2'>Người thực hiện</th>
									<th rowspan='2'></th>
								</tr>
								<tr>
									<th style="font-size: 12px !important;">Tại QC</th>
									<th style="font-size: 12px !important;">Số lượng chuyển</th>
									<th style="font-size: 12px !important;">Đã nhận</th>
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
					"sClass" : "right",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				},{
					"sClass" : "left",
					"bSortable" : false
				},{
					"sClass" : "left",
					"bSortable" : false
				},{
					"sClass" : "left",
					"bSortable" : false,
				}];
			}
			
			$(document).ready(function() {
				$('.btnDtAdd').hide();
			});
			
			function edit(id,type) {
				$.loader({
					className : "blue-with-image-2"
				});
				clearDiv('modalChangeOutSourcedHistory');
				var tokenIdKey = $('#tokenIdKey').val();
				var tokenId = $('#tokenId').val();
				$.getJSON("outSourced?method=edit",{
					"id" : id,
					"tokenIdKey" : tokenIdKey,
					"tokenId" : tokenId
				}).done(function(res, xhr, status){
					if(type == 'qi'){
						$('.wo').css('display','none');
						$('.wo').prop('disabled', true);
						$('.qi').css('display','flex');
						$('.qi').prop('disabled', false);
					}else{
						$('.wo').css('display','flex');
						$('.wo').prop('disabled', false);
						$('.qi').css('display','none');
						$('.qi').prop('disabled', true);
						
					} 
					$('#modalChangeOutSourcedHistory').modal('show');
						binding(res, 'modalChangeOutSourcedHistory');
					});
			}
			
			function changeOutSourcedHistory() {
				if (!$('#editForm').valid())
					return;
				var ngAmout = $('input[name="ngAmountChange"]').val().convertStringToNumber();
				if(Math.round(ngAmout)!=ngAmout){
					alert('Số lượng chi tiết lỗi phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
					return;
				}
				$.ajax({
					url : 'outSourced?method=saveOutSourcedDetail',
					type : 'POST',
					async : false,
					data : {
						idOS : $('input[name="idOS"]').val(),
						manageCode : $('input[name="manageCode"]').val(),
						qcQuatity : $('input[name="qcQuatity"]').val(),
						totalToOs : $('input[name="totalToOs"]').val(),
						idCustomer : $('select[name="idCustomer"]').val(),
						idWorker : $('select[name="idWorker"]').val()
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
			function delOutSourcedHistory() {
				jConfirm('Bạn có chắn chắn muốn xóa lịch sử chuyển GCN này?', 'OK', 'Cancel', function(r) {
					if (r) {
						$.ajax({
							url : 'outSourced?method=delOutSourcedHistory',
							type : 'POST',
							async : false,
							data : {
								idOS : $('input[name="qcOs.id"]').val()
							},
							method : 'GET',
							success : function(data) {
								if (data === '') {
									alert(thuc_hien_thanh_cong, function() {
										$('#modalChangeOutSourcedHistory').modal('hide');
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
			function getItems(idQcOsDetail) {
				window.open('qcIn?hiddenMenu=true&idQcOs=' + idQcOsDetail,window_property);
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



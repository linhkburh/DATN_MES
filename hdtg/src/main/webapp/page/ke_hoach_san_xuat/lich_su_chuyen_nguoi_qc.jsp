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
#divDatatable tbody td{
	font-size: 12 !important;
	padding: 0.3rem !important;
}
#divDatatable thead th{
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
								Số lượng chi tiết hoàn thành<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="amountChange" class="textint required">
							</div>
						</div>
						<c:if test="${param.type=='QC'}">
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng chi tiết cần sửa<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="amountRp" class="textint required">
							</div>
						</div>
						</c:if>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Số lượng chi tiết lỗi</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" name="ngAmountChange" class="textint" readonly="readonly">
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
											<c:out value="${item.username}" />-<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<%-- <div class=Row>
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Máy gia công<font color="red">*</font>
							</div>
							<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<select name="machineID" class="required">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstAstMachine}" var="item">
										<option value="${item.id}">
											<c:out value="${item.astName}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div> --%>
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
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		<c:if test="${param.type == 'CL'}">
        	Lịch sử chuyển nguội
    	</c:if>
    	<c:if test="${param.type == 'QC'}">
    		Lịch sử chuyển QC
    	</c:if>
    	<c:if test="${param.to == 'RP'}">
    		Sửa hàng
    	</c:if>
</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/itemProcess" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
		<form:hidden path="type" id="type"/>
		<form:hidden path="to" id="to"/>
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText"><spring:message code="common.info.search"/></div>
					
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="managerCode"/>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="quotationItemCode" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khách hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="cusCode" id="cusCode">
								<form:option value="" label="- Chọn -" />
								<c:forEach items="#{customers}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.code}" />-<c:out value="${item.orgName}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã đơn hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orderCode" />
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
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Người thực hiện</div>
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
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="status">
								<form:option value="">Tất cả</form:option>
								<form:option value="1">Đã hoàn thành</form:option>
								<form:option value="0">Chưa hoàn thành</form:option>
							</form:select>
						</div>
					</div>
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; <spring:message code="common.btn.search"/>" />
					</div>
					<div class="HeaderText"><spring:message code="common.search.results"/></div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendReportAction" value="/historyPro/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				// Sua hang
				if ($('#to').val() == 'RP') {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã đơn hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã khách hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã bản vẽ'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã quản lý'
					},{
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Số lượng cần sửa'
					},{
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Số lượng đã sửa'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Cán bộ QC'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời điểm QC'
					} ];
				}else{
					// Lich su nguoi/qc
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã đơn hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã khách hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã bản vẽ'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã quản lý'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Tổng số lượng đã chuyển'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng đã thực hiện'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng còn lại'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Người chuyển'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Ngày chuyển'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : ''
					} ];
				}
			}
			function download() {
				window.open('quanlyLSX?method=download').focus();
			}
			function editProductionHistory(idExe, totalAmount) {
				$('input[name="idWorkExe"]').val(idExe);
				//$('input[name="amountChange"]').val(amount);
				$('input[name="totalAmountChange"]').val(totalAmount);
				//$('input[name="ngAmountChange"]').val(ngAmount);
				//$('textarea[name="ngDesChange"]').val(ngDes);
				//$('select[name="userChange"]').val(userId);
				//$('input[name="setupTimeStr"]').val(setupTime); 
				$('select[name="userChange"]').select2();
				$('#modalChangeProductionHistory').modal('show');
			}
			function changeProductionHistory() {
				if (!$('#editForm').valid())
					return;
				var ngAmout = $('input[name="ngAmountChange"]').val().convertStringToNumber();
				if(Math.round(ngAmout)!=ngAmout){
					alert('Số lượng chi tiết lỗi phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
					return;
				}
				$.ajax({
					url : 'itemProcess?method=addProcessExe',
					type : 'POST',
					async : false,
					data : {
						processItemId : $('input[name="idWorkExe"]').val(),
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
			function deleteQIP(id) {
				jConfirm('Bạn có chắn chắn muốn xóa ?', 'OK', 'Cancel', function(r) {
					if (!r) return;
					$.ajax({
						url : delUrl,
						type : 'POST',
						async : false,
						data : {
							"quotationItemProcess.id" : id
						},
						method : 'GET',
						success : function(data) {
							if (data === '') {
								alert(thuc_hien_thanh_cong, function() {
									findData();
								});
							} else {
								alert(data);
							}

						}
					});
				});

			}
			function uploadItem() {
				var files = document.getElementById('file-select_imp').files;
				if (files.length == 0) {
					alert('Chưa chọn file!');
					return;
				}
				var formData = new FormData();
				formData.append("fileName", files[0].name);
				formData.append("inputFile", files[0]);
				formData.append("tokenIdKey", $('#tokenIdKey').val());
				formData.append("tokenId", $('#tokenId').val());
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${sendReportAction}', true);
				xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
				$.loader({
					className : "blue-with-image-2"
				});

				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						$('#modal_dialog_file').modal('hide');
						if (xhr.responseText == '') {
							alert('Thực hiện thành công!', function() {
								window.location.reload();
							});
						} else {
							alert(xhr.responseText)
						}

					} else {
						alert('Import không thành công');
					}
				};
				xhr.send(formData);

			}
			function uploadDetails() {
				$('#file-select_imp').val('');
				$('#modal_dialog_file').modal();
			}
			function exportExcel(){
				$.ajax({
			        url: 'historyPro?method=exportExcel',
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
			            a.download = 'lich su san xuat.xlsx';
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
			$(document).ready(function(){
				$('.btnDtAdd').hide();
				// Dialog
				$('input[name="amountChange"], input[name="totalAmountChange"], input[name="amountRp"]').on('change', function() {
					var amount = $('input[name="amountChange"]').val().convertStringToNumber();
					var totalAmout = $('input[name="totalAmountChange"]').val().convertStringToNumber();
					var ngAmount = 0;
					if ($('#type').val() === 'QC') {
						var rpAmout = $('input[name="amountRp"]').val().convertStringToNumber();
						ngAmount = totalAmout - amount - rpAmout;
					} else {
						ngAmount = totalAmout - amount;
					}
					
					$('input[name="ngAmountChange"]').val(formatSo(ngAmount));

				});
			});
			function itemProcess(type, managerCode) {
				if(type == "RP"){
					window.open('quotationRepaireExe?hiddenMenu=true&to='+type+'&manageCode='+ managerCode,'','width=1000, height=700, status=yes, scrollbars=yes').focus();
				}else{
					window.open('processExeQR?hiddenMenu=true&type='+type+'&manageCode='+ managerCode,'','width=1000, height=700, status=yes, scrollbars=yes').focus();
				}	
			}

			function refeshItem() {
				 findData();
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



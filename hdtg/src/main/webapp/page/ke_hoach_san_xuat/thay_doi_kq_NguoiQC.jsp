<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="common.util.FormatNumber"%>
<div id="modalChangeWorkExeDetail" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg" id='repaireRs'>
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Thay đổi thông tin</h4>
			</div>
			<div class="modal-body">
				<div class='Table'>
						
						<input type="hidden" name="processExe.id"> 
						<input type="hidden" name="processExe.quotationItem.id"> 
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Ngày bắt đầu<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:input  path="processExe.startTimeStr" class="dateTime required" />
								<form:input type="hidden" path="processExe.type"/>
							</div>

						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Ngày kết thúc<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:input  path="processExe.endTimeStr" class="dateTime required" />
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng chi tiết<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:input  path="processExe.totalAmountStr" class="textint required" />
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng chi tiết hoàn thành<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:input  path="processExe.amountStr" class="textint required" />
							</div>
						</div>
						<c:if test="${param.type=='QC'}">
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Số lượng chi tiết cần sửa<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:input  path="processExe.rpAmountStr" class="textint required" />
							</div>
						</div>
						</c:if>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Số lượng chi tiết lỗi</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:input  path="processExe.ngAmountStr" class="number" readonly="true" />
							</div>
						</div>
						
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
								Công nhân<font color="red">*</font>
							</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:select path="processExe.sysUser.id" class="required">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUser}" var="item">
										<option value="${item.id}">
											<c:out value="${item.username}" />-
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Lý do lỗi</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<form:textarea rows="3" path="processExe.ngDescription"></form:textarea>
							</div>
						</div>
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
<script type="text/javascript" lang="javascript">
	function changeProductionHistory() {
		if (!$('#repaireRs').find('input, select, textarea').valid())
			return;
		var ngAmout = $('input[name="processExe.ngAmountStr"]').val().convertStringToNumber();
		if(Math.round(ngAmout)!=ngAmout){
			alert('Số lượng chi tiết lỗi phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
			return;
		}
		var data = $('#modalChangeWorkExeDetail').find('input, select, textarea').serialize();
		$.ajax({
			url : 'processExe?method=addProcessExe',
			type : 'POST',
			async : false,
			data : data,
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
	function delWorkOrderExe() {
		jConfirm('Bạn có chắn chắn muốn xóa lịch sử này?', 'OK', 'Cancel', function(r) {
			if (r) {
				$.ajax({
					url : 'processExe?method=delWorkOrderExe',
					type : 'POST',
					async : false,
					data : {
						processExeId : $('input[name="processExe.id"]').val()
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

	// override
	function edit(id) {
		$.loader({
			className : "blue-with-image-2"
		});
		clearDiv('modalChangeWorkExeDetail');
		var tokenIdKey = $('#tokenIdKey').val();
		var tokenId = $('#tokenId').val();
		$.getJSON("processExe?method=edit", {
			"id" : id,
			"tokenIdKey" : tokenIdKey,
			"tokenId" : tokenId
		}).done(function(res) {
			$('#modalChangeWorkExeDetail').modal('show');
			binding(res, 'modalChangeWorkExeDetail');
			$.loader('close');
			if (res.ngRepaire) {
				$('div#repaireRs .row.exe').css('display', 'none');
			} else {
				$('div#repaireRs .row.exe').css('display', 'flex');
			}
		});

	}

	window.onload = function() {
		$('select[name="processExe.sysUser.id"]').select2();
		// Dialog
		$('input[name="processExe.totalAmountStr"], input[name="processExe.amountStr"], input[name="processExe.rpAmountStr"]').on('change', function() {
			var amount = $('input[name="processExe.amountStr"]').val().convertStringToNumber();
			var totalAmout = $('input[name="processExe.totalAmountStr"]').val().convertStringToNumber();
			var rpAmount = 0;
			if($('input[name="processExe.rpAmountStr"]').length> 0 && $('input[name="processExe.rpAmountStr"]').val()!='')
				rpAmount = $('input[name="processExe.rpAmountStr"]').val().convertStringToNumber();
			var ngAmount = totalAmout - amount- rpAmount;
			$('input[name="processExe.ngAmountStr"]').val(formatSo(ngAmount));
		});
	}
</script>
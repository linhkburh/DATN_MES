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
				<c:choose>
					<c:when test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code=="OS"}'>
						<h4 class="modal-title">Chỉnh sửa nội dung chuyển QC</h4>
					</c:when>
					<c:otherwise>
						<h4 class="modal-title">Chỉnh sửa kết quả sản xuất</h4>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="modal-body">
				<div class='Table'>
					<input type="hidden" name="workOrderExe.id"> <input type="hidden" name="workOrderExe.workOrderId.id">
					<c:choose>
						<c:when test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code=="OS"}'>
							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Thời điểm bắt đầu<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="datetime required" path="workOrderExe.startTimeStr" />
								</div>
							</div>
							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Thời điểm kết thúc<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="datetime required" path="workOrderExe.endTimeStr" />
								</div>
							</div>
							<div class="Row exe">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Tổng số chi tiết<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="number required" id="changetotalAmount" path="workOrderExe.totalAmountStr" />
								</div>
							</div>
							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Công nhân<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:select path="workOrderExe.sysUser.id" class="required">
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
						</c:when>
						<c:otherwise>
							<div class="Row notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Sửa NG</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:checkbox disabled="true" value="true" path="workOrderExe.ngRepaire" id="changeNgRepaire" />
								</div>
							</div>
							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Thời điểm bắt đầu<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="datetime required" path="workOrderExe.startTimeStr" />
								</div>
							</div>
							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Thời điểm kết thúc<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="datetime required" path="workOrderExe.endTimeStr" />
								</div>
							</div>
							<div class="Row notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Thời gian setup (phút)</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="textint" path="workOrderExe.setupTime" />
								</div>

							</div>

							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Tổng số chi tiết<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="number required" id="changetotalAmount" path="workOrderExe.totalAmountStr" />
								</div>
							</div>
							<div class="Row notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Số lượng hoàn thành (OK)<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="number required" id="changeAmount" path="workOrderExe.amountStr" />
								</div>
							</div>
							<div class="Row notos exe">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Số lượng cần sửa (NG)</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="required number" id='changeNgAmountStr' readonly="false"
										path="workOrderExe.ngAmountStr" />
								</div>
							</div>
							<div class="Row notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Số lượng hủy</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:input type="text" class="required number" id='changeBrokenAmountStr' readonly="true"
										path="workOrderExe.brokenAmountStr" />
								</div>
							</div>
							<div class="Row">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Công nhân<font color="red">*</font>
								</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:select path="workOrderExe.sysUser.id" class="required">
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
							<div class="Row notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
									Máy gia công<font color="red">*</font>
								</div>
								<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
									<form:select path="workOrderExe.astMachine.id" cssClass="required">
										<form:option value="">- Chọn -</form:option>
										<c:forEach items="#{lstAstMachine}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.astName}" />
											</form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>
							<div class="Row error_cause notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Nguyên nhân lỗi</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:select path="workOrderExe.errorCause.id">
										<option value="">- Chọn -</option>
										<c:forEach items="#{lstErrorCause}" var="item">
											<option value="${item.id}">
												<c:out value="${item.value}" />
											</option>
										</c:forEach>
									</form:select>
								</div>
							</div>
							<div class="Row notos">
								<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">Mô tả lỗi</div>
								<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
									<form:textarea rows="3" path="workOrderExe.ngDescription" />
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="changeWorkExe();">Lưu</button>
				<button type="button" onclick="delWorkOrderExe()" class="btn red fa">Xóa</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" lang="javascript">
	function changeWorkExe() {
		if (!$('#repaireRs').find('input, select, textarea').valid())
			return;
		if ($('#stepTypeCode').val() != 'OS') {
			var ngAmout = $('#changeNgAmountStr').val().convertStringToNumber();
			if (!$('input[name="changeNgRepaire"]').prop('checked') && Math.round(ngAmout) != ngAmout) {
				alert('Số lượng chi tiết lỗi phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
				return;
			}
			var brokenAmout = $('#changeBrokenAmountStr').val().convertStringToNumber();
			if ($('input[name="changeNgRepaire"]').prop('checked') && Math.round(brokenAmout) != brokenAmout) {
				alert('Số lượng chi tiết hủy phải là số nguyên (kiểm tra lại tổng số và số lương hoàn thành) !');
				return;
			}
		}
		// Disable tam thoi de submit len
		$('#changeNgRepaire').prop('disabled', false);
		var data = $('#modalChangeWorkExeDetail').find('input, select, textarea').serialize();
		$('#changeNgRepaire').prop('disabled', true);
		$.ajax({
			url : 'workOderDetail?method=addWorkOrderDetail',
			type : 'POST',
			async : false,
			data : data,
			method : 'GET',
			success : function(data) {
				if (data === '') {
					alert(thuc_hien_thanh_cong, function() {
						// Phai load lai page de refresh cac gia tri phia tren
						if(location.pathname.indexOf('historyPro')>0){
							$('#modalChangeWorkExeDetail').modal('hide');
							findData();
						}
						else
							window.location.reload();
					});
				} else {
					alert(data);
				}

			}
		});
	}
	function delWorkOrderExe() {
		jConfirm('Bạn có chắn chắn muốn xóa thông tin sản xuất này?', 'OK', 'Cancel', function(r) {
			if (r) {
				$.ajax({
					url : 'workOderDetail?method=deleteWorkOrderDetail',
					type : 'POST',
					async : false,
					data : {
						workOrderExeId : $('input[name="workOrderExe.id"]').val()
					},
					method : 'GET',
					success : function(data) {
						if (data === '') {
							alert(thuc_hien_thanh_cong, function() {
								// Phai load lai page de refresh cac gia tri phia tren
								if(location.pathname.indexOf('historyPro')>0){
									$('#modalChangeWorkExeDetail').modal('hide');
									findData();
								}
								else
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
	function edit(id, type) {
		$.loader({
			className : "blue-with-image-2"
		});
		clearDiv('modalChangeWorkExeDetail');
		var tokenIdKey = $('#tokenIdKey').val();
		var tokenId = $('#tokenId').val();
		$.getJSON("workOderDetail?method=edit", {
			"id" : id,
			"tokenIdKey" : tokenIdKey,
			"tokenId" : tokenId
		}).done(function(res) {
			$('#modalChangeWorkExeDetail').modal('show');
			binding(res, 'modalChangeWorkExeDetail');
			$.loader('close');
			if (res.ngRepaire) {
				$('#changeNgRepaire').prop('checked', true);
				$('div#repaireRs .row.exe').css('display', 'none');
				$('div#repaireRs .row.exe').find('select, input, textarea').prop('disabled', true);

			} else {
				$('#changeNgRepaire').prop('checked', false);
				$('div#repaireRs .row.exe').css('display', 'flex');
				$('div#repaireRs .row.exe').find('select, input, textarea').prop('disabled', false);
				if (type == "OS") {
					$('.notos').css('display', 'none');
				} else {
					$('.notos').css('display', 'flex');
				}
			}
		});

	}

	window.onload = function() {
		$('#changeAmount,#changetotalAmount,#changeNgAmountStr')
				.on(
						'change',
						function() {
							if ($(this).attr('id') == 'changeNgRepaire') {
								$('#realTime .row.exe,#noneRealTime .row .rp').find('select, input, textarea').prop(
										'disabled', $(this).prop('checked'));
								$('#realTime .row.exe,#noneRealTime .row .rp').css('display',
										$(this).prop('checked') ? 'none' : 'inline');
							}
							// Cha ton tai tong so hoac so luong thanh cong
							if ($('#changeAmount').val().trim().length == 0
									|| $('#changetotalAmount').val().trim().length == 0)
								return;
							var amount = $('#changeAmount').val().convertStringToNumber();
							var totalAmout = $('#changetotalAmount').val().convertStringToNumber();
							if (amount == 0 && totalAmout == 0)
								return;
							var ngAndbroken = totalAmout - amount;
							// repaire
							if ($('#changeNgRepaire').prop('checked')) {
								// kg co NG
								$('#changeBrokenAmountStr').val(formatSo(ngAndbroken));
								return;
							}
							// Thay doi so luong Ng
							if ($(this).attr('id') == 'changeNgAmountStr') {
								var ngAmount = $('#changeNgAmountStr').val().convertStringToNumber();
								var brokenAmount = ngAndbroken - ngAmount;
								$('#changeBrokenAmountStr').val(formatSo(brokenAmount));
								return;
							}
							// Mac dinh NG (da so la NG, truong hop da so)
							$('#changeNgAmountStr').val(formatSo(ngAndbroken));
							$('#changeBrokenAmountStr').val(0);
						});

	}
</script>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title">
		Sửa hàng
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/quotationRepaireExe" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<div class='Table' id="test-area-qr-code-webcam">
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã quản lý<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="manageCode" readonly="true" class="required" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng chi tiết cần sửa<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.quotationItem.qcRepaire" class="required" readonly="true" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng đã sửa<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.quotationItem.repairingAmount" class="required" readonly="true" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng đã sửa thành công<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.quotationItem.repairedAmount" class="required" readonly="true" />
					</div>
				</div>
				
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<font color="red">Số lượng còn lại</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.quotationItem.todoRepaire" class="required" readonly="true" id="todoRepaire" />
					</div>
					
				</div>

				<div class="Header1" style="font-size: 0">&ZeroWidthSpace;</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
						<a href="javascript:;" onclick="finish()" title="Hoàn thành" class="fa fa-check"
							style="font-size: 25; font-weight: bold;"></a>

					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời điểm bắt đầu<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.startTime" class="dateTime required" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời điểm kết thúc<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.endTime" class="dateTime required" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng sửa<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.totalAmount" class="textint required"  id = "totalAmountStr"/>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Sửa thành công">
						Số lượng hoàn thành (OK)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" title="Sửa thành công">
						<form:input path="quotationRepaire.amount" class="textint required"  id="soLuongChiTietDetail"/>
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng hủy<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationRepaire.ngAmount" class="number required" readonly="true" id="brokenAmountStr"/>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Máy gia công</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="quotationRepaire.astMachine.id">
							<option value="">- chọn -</option>
							<c:forEach items="#{lstAstMachine}" var="item">
								<form:option value="${item.id}">
									<c:out value="${item.astName}" />
								</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Công nhân<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="quotationRepaire.sysUser.id" class="required">
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
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lý do lỗi</div>
					<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
						<form:textarea rows="3" path="quotationRepaire.ngDescription"></form:textarea>
					</div>
				</div>

				<div align="center" class="HeaderText">&ZeroWidthSpace;</div>
				<div align="center" class="divaction">
					<input type="button" onclick="addQuotationRepaire()" value="&#xf0c7; Lưu" id="btnSave" class="btn blue fa">
					<input type="button" onclick="if(window.opener !=undefined) window.close(); else window.history.back()"
						value="&#xf112; Bỏ qua" id="btlCancel" class="btn gray fa">
				</div>
			</div>
		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			$(document).ready(
					function() {
						$('select').select2();
						// Dialog
						$('input[name="quotationRepaire.totalAmount"], input[name="quotationRepaire.amount"]').on(
								'change',
								function() {
									if($(this).attr('name')=='quotationRepaire.totalAmount'){
										$('input[name="quotationRepaire.amount"]').val($(this).val());
										$('input[name="quotationRepaire.ngAmount"]').val('0');
										return;
									}
									var amount = $('input[name="quotationRepaire.amount"]').val()
											.convertStringToNumber();
									var totalAmout = $('input[name="quotationRepaire.totalAmount"]').val()
											.convertStringToNumber();
									$('input[name="quotationRepaire.ngAmount"]').val(formatSo(totalAmout - amount));
								});
					});

			function addQuotationRepaire() {
				if (!$('#theForm').valid())
					return;
				var ngAmout = $('input[name="quotationRepaire.ngAmount"]').val().convertStringToNumber();
				if (Math.round(ngAmout) != ngAmout) {
					alert('Số lượng chi tiết hủy phải là số nguyên (kiểm tra lại tổng số và số lượng hoàn thành) !');
					return;
				}
				$.ajax({
					url : 'quotationRepaireExe?method=addQuotationRepaire',
					type : 'POST',
					async : false,
					data : $('#theForm').serializeObject(),
					method : 'GET',
					success : function(data, xhr, status) {
						if (data == '') {
							alert(thuc_hien_thanh_cong, function(){
								window.close();
								opener.findData();
							});
						} else {
							alert(data);
						}
					}
				});
			}
			function finish() {
				$('#totalAmountStr, #soLuongChiTietDetail').val($('#todoRepaire').val());
				$('#ngAmountStr, #brokenAmountStr').val(0);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title">
		<c:if test="${param.type == 'CL'}">
        	 gia công nguội
    	</c:if>
		<c:if test="${param.type == 'QC'}">
    		Thực hiện QC
    	</c:if>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/processExeQR" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<form:hidden path="processExe.type" id="type" />
			<div class='Table' id="test-area-qr-code-webcam">
				<div id="noneRealTime">
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="manageCode" readonly="true" class="required" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổng số chi tiết</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.qiTotalAmount" readonly="true" />
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng đã thực hiện</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.qiDoneAmount" readonly="true" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng hủy</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.qiBrokenAmount" readonly="true" />
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng cần sửa (NG)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.qiNgAmount" readonly="true" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><font color="red">Số lượng còn lại</font></div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.qiOustandingAmount" readonly="true" />
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Thời gian dự kiến thực hiện một chi tiết
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.estTime" readonly="true" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Thời gian dự kiến hoàn thành
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.totalEstTime" readonly="true" />
						</div>
					</div>
					<div class="Header1" style="font-size: 0">&ZeroWidthSpace;</div>
					<c:if test="${param.type == 'CL'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Sửa NG</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:checkbox path="processExe.ngRepaire" id="ngRepaire" />
							</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<a href="javascript:;" onclick="finish()" title="Hoàn thành" class="fa fa-check"
									style="font-size: 25; font-weight: bold;"></a>

							</div>
						</div>
					</c:if>
					<c:if test="${param.type != 'CL'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<a href="javascript:;" onclick="finish()" title="Hoàn thành" class="fa fa-check"
									style="font-size: 25; font-weight: bold;"></a>

							</div>
						</div>
					</c:if>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Thời điểm bắt đầu<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.startTimeStr" class="dateTime required" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Thời điểm kết thúc<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.endTimeStr" class="dateTime required" />
						</div>
					</div>
					<div class="Row "></div>
					<div class="Row exe">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Tổng số chi tiết<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.totalAmountStr" class="textint required" id="totalAmountStr" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Số lượng hoàn thành (OK)<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="processExe.amountStr" class="textint required" id="soLuongChiTietDetail" />
						</div>
					</div>

					<%-- <c:if test="${param.type == 'CL'}">
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Số lượng cần sửa (NG)<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="processExe.ngAmountStr" class="textint required" id="ngAmountStr" readonly="true" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell exe"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 ">
								Công nhân<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
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
					</c:if> --%>
					<%-- <c:if test="${param.type == 'QC'}"> --%>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Số lượng cần sửa (NG)<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="processExe.ngAmountStr" class="textint required" id="ngAmountStr" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell exe"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell exe">Số lượng hủy</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12 cell exe">
								<form:input path="processExe.brokenAmountStr" class="number" readonly="true" id="brokenAmountStr" />
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 ">
								Công nhân<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
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
					<%-- </c:if> --%>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
						<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
							<form:textarea rows="3" path="processExe.ngDescription"></form:textarea>
						</div>
					</div>

					<div align="center" class="HeaderText">&ZeroWidthSpace;</div>
					<div align="center" class="divaction">
						<input type="button" onclick="save()" value="&#xf0c7; Lưu" id="btnSave" class="btn blue fa"> <input
							type="button" onclick="if(window.opener !=undefined) window.close(); else window.history.back()"
							value="&#xf112; Bỏ qua" id="btlCancel" class="btn gray fa">
					</div>
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
						$('#soLuongChiTietDetail,#totalAmountStr,#ngAmountStr,#ngRepaire').on(
								'change',
								function() {
									if ($(this).attr('id') == 'ngRepaire') {
										$('#ngAmountStr').prop('disabled', $(this).prop('checked'));
									}
									// Cha ton tai tong so hoac so luong thanh cong
									if ($('#soLuongChiTietDetail').val().trim().length == 0
											|| $('#totalAmountStr').val().trim().length == 0)
										return;
									var amount = $('#soLuongChiTietDetail').val().convertStringToNumber();
									var totalAmout = $('#totalAmountStr').val().convertStringToNumber();
									if (amount == 0 && totalAmout == 0)
										return;
									var ngAndbroken = totalAmout - amount;
									// repaire
									if ($('#ngRepaire').prop('checked')) {
										// kg co NG
										$('#brokenAmountStr').val(formatSo(ngAndbroken));
										return;
									}
									// Thay doi so luong Ng
									if ($(this).attr('id') == 'ngAmountStr') {
										var ngAmount = $('#ngAmountStr').val().convertStringToNumber();
										var brokenAmount = ngAndbroken - ngAmount;
										$('#brokenAmountStr').val(formatSo(brokenAmount));
										return;
									}
									// Mac dinh NG (da so la NG, truong hop da so)
									$('#ngAmountStr').val(formatSo(ngAndbroken));
									$('#brokenAmountStr').val(0);
								});
					});

			function save() {
				if (!$('#theForm').valid())
					return;
				$.loader({
					className : "blue-with-image-2"
				});
				$.ajax({
					url : 'processExeQR?method=addProcessExe',
					type : 'POST',
					async : false,
					data : $('#theForm').serialize(),
					method : 'POST',
					success : function(data) {
						$.loader("close");
						if (data == '') {
							var url = new URL(window.location.href);
							const param = url.searchParams.get('hiddenMenu');
							if (param) {
								alert(thuc_hien_thanh_cong, function() {
									window.opener.refeshItem();
									window.close();
								});
								return;
							}
							alert(thuc_hien_thanh_cong, function() {
								window.history.back();
							});
						} else {
							alert(data);
						}
					}
				});
			}
			function finish() {
				if (!$('#ngRepaire').prop('checked')) {
					$('#totalAmountStr, #soLuongChiTietDetail').val($('input[name="processExe.qiOustandingAmount"]').val());
					$('#ngAmountStr, #brokenAmountStr').val(0);
				} else {
					$('#totalAmountStr, #soLuongChiTietDetail').val($('input[name="processExe.qiNgAmount"]').val());
					$('#brokenAmountStr').val(0);
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
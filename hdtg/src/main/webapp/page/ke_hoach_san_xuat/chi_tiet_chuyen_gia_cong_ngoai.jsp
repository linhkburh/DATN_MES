<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title" value="QC hàng gia công ngoài" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/QcChkOutSrc" var="formAction" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="formDataModelAttr"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<div id="divDetail">
				<div style="display: none">
					<select style="display: none" id="exeSteps" name="exeSteps">
						<option value=""></option>
						<c:forEach items="#{exeSteps}" var="item">
							<option value="${item.id}">
								<c:out value="${item.quotationItemExe.exeStepId.stepType.name}" /> -
								<c:out value="${item.quotationItemExe.exeStepId.stepName}" />
							</option>
						</c:forEach>
					</select>
				</div>
				<div style="display: none">
					<select style="display: none" id="lsErrorCause" name="lsErrorCause">
						<option value=""></option>
						<c:forEach items="#{lstErrorCause}" var="item">
							<option value="${item.id}">
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
					</select>
				</div>
				<form:hidden path="qcChkOutSrc.workOrder.id" id="workOrderId" />
				<form:hidden path="qcChkOutSrc.id" id="id" />
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcChkOutSrc.workOrder.quotationItemExe.quotationItemId.manageCode" readonly="true" />
					</div>
					<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã LSX</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input readonly="true" path="qcChkOutSrc.workOrder.code" />
					</div>
				</div>
				<div class="Row">
					
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công đoạn</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input readonly="true" path="qcChkOutSrc.workOrder.quotationItemExe.exeStepId.stepCode" />
					</div>
					<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng SX chuyển<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcChkOutSrc.totalAmount" class="number" readonly="true" />
					</div>
				</div>
				
				<div class="Header1" style="font-size: 0">&ZeroWidthSpace;</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời gian bắt đầu<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcChkOutSrc.startTime" class="dateTime required" />
					</div>
					<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời gian kết thúc<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcChkOutSrc.endTime" class="dateTime required" />
					</div>
				</div>

				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Công nhân<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="qcChkOutSrc.worker.id" class="required">
							<form:option value="">- Chọn -</form:option>
							<c:forEach items="#{lstSysUser}" var="item">
								<form:option value="${item.id}">
									<c:out value="${item.username}" />-<c:out value="${item.name}" />
								</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Chuyển gia công ngoài">
						Số lượng chuyển gia công ngoài<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcChkOutSrc.amount" class="number" readonly="true" onInput="changeAmount2()" />
					</div>
				</div>
				
				<div class="Header2">Danh sách NG/Hủy (trả lại sản xuất)</div>
				<div class="table-responsive">
					<table class="table table-bordered" style="table-layout: fixed;" id="table-OS-detail">
						<thead>
							<tr>
								<th>Công đoạn<font color="red">*</font></th>
								<th>Số lượng cần sửa (NG)</th>
								<th>Số lượng hủy</th>
								<th>Nguyên nhân lỗi</th>
								<th>Mô tả lỗi</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${formDataModelAttr.qcChkOutSrc.qcChkOutSrcDetails}" var="pro" varStatus="loop">
								<tr>
									<td><form:select path="qcChkOutSrc.qcChkOutSrcDetails[${loop.index}].workOrder.id"
											class="exeStepId notnull required">
											<option value="">- Chọn -</option>
											<c:forEach items="#{exeSteps}" var="item">
												<form:option value="${item.id}">
													<c:out value="${item.quotationItemExe.exeStepId.stepType.name}" /> - <c:out
														value="${item.quotationItemExe.exeStepId.stepName}" />
												</form:option>
											</c:forEach>
										</form:select></td>
									<td><form:input onchange="changeAmount();" class="number amountDetail"
											path="qcChkOutSrc.qcChkOutSrcDetails[${loop.index}].ngAmount" /> <form:hidden
											path="qcChkOutSrc.qcChkOutSrcDetails[${loop.index}].qcChkOutSrc.id" /></td>
									<td><form:input onchange="changeAmount();" class="number brokenAmountDetail"
											path="qcChkOutSrc.qcChkOutSrcDetails[${loop.index}].brokenAmount" /></td>
									<td><form:select path="qcChkOutSrc.qcChkOutSrcDetails[${loop.index}].errorCause.id">
											<option value="">- Chọn -</option>
											<c:forEach items="#{lstErrorCause}" var="item">
												<form:option value="${item.id}">
													<c:out value="${item.value}" />
												</form:option>
											</c:forEach>
										</form:select></td>
									<td><form:input path="qcChkOutSrc.qcChkOutSrcDetails[${loop.index}].ngDescription" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div align="center" class="HeaderText" style="font-size: 0 !important;">&ZeroWidthSpace;</div>
			<div align="center" class="divaction">
				<c:if test="${save}">
					<input type="button" onclick="save()" value="Lưu" id="btnSave" class="btn blue">
				</c:if>
				<c:if test="${param.id != null}">
					<input type="button" onclick="del()" value="Xóa" id="btnDel" class="btn red">
				</c:if>
				<input type="button" onclick="window.top.close();" value="Bỏ qua" id="btlCancel" class="btn gray">
			</div>
		</tiles:putAttribute>

	</form:form>

	<tiles:putAttribute name="extra-scripts">
		<%-- <script src="<spring:url value="/page/bao_gia/chi_tiet_bao_gia.js?v=2" />"></script> --%>
		<script type="text/javascript">
			var tableOS = null;
			function initTable() {
				var tableObject = new TFOnline.DataTable({
					id : 'table-OS-detail',
					jQueryUI : true,
					rowTemp : initRowTable("qcChkOutSrc"),
					afterAddRow : function() {
						afterAddRow('table-OS-detail');
					},
					afterDeleteRow : function() {
						changeAmount();
					},
					hasCheck : true,
					hasOrder : true,
					addOveride : true,
					delOveride : true,
					maxRow : 100
				});
				tableOS = tableObject;

			}

			function afterAddRow() {
				$('.exeStep').select2();
			}

			function initRowTable(obj) {
				var rowTemp = new Array();
				rowTemp.push('');
				rowTemp
						.push('<div class="line-table"><select class="exeStep notnull required" name="qcChkOutSrc.qcChkOutSrcDetails[].workOrder.id">'
								+ $('#exeSteps').html() + '</select>  </div>');
				rowTemp
						.push('<div class="line-table"><input type="text" onchange="changeAmount();"  class="number amountDetail" name="qcChkOutSrc.qcChkOutSrcDetails[].ngAmount"> </div>');
				rowTemp
						.push('<div class="line-table"><input type="text" onchange="changeAmount();"  class="number brokenAmountDetail" name="qcChkOutSrc.qcChkOutSrcDetails[].brokenAmount"> </div>');
				rowTemp.push('<div class="line-table"><select name="qcChkOutSrc.qcChkOutSrcDetails[].errorCause">'
						+ $('#lsErrorCause').html() + '</select>  </div>');
				rowTemp
						.push('<div class="line-table"><input type="text"  name="qcChkOutSrc.qcChkOutSrcDetails[].ngDescription"> </div>');
				return rowTemp;
			}

			$(document).ready(function() {
				initTable();
				var amount = $('input[name="qcChkOutSrc.amount"]').val().convertStringToNumber();
			});

			function save(saveData) {

				// Truoc khi validate    
				if (typeof beforeValidate == 'function')
					beforeValidate();

				// customize validate
				if (typeof instanceValidate == 'function') {
					if (!instanceValidate())
						return;
				} else {
					if (!$('#theForm').valid()) {
						alert('Thực hiện không thành công, vui lòng kiểm tra thông tin nhập liệu!');
						return;
					}
				}

				// Before save
				if (typeof beforeSave == 'function')
					beforeSave();
				$('input[type="checkbox"]').each(function() {
					$(this).val($(this).prop('checked'));
				});
				$('input[type="radio"]').each(function() {
					try {
						if ($(this).prop('checked')) {
							var arrName = $(this).attr('name').split('.');
							var realId = arrName[arrName.length - 1];
							$(this).val($(this).attr('id').substring(realId.length));
						}
					} catch (err) {
					}

				});
				$.loader({
					className : "blue-with-image-2"
				});
				$.ajax({
					method : 'POST',
					async : false,
					type : "POST",
					url : saveUrl
					//, data : saveData?saveData: $('#theForm').serialize()
					,
					data : $('#theForm').serialize(),
					success : function(data, status, xhr) {
						$.loader("close");
						var jsnResult = chkJson(data, xhr);
						// Khong tra ve json, truong hop tra ve loi hoac khong giu nguyen man hinh
						if (!jsnResult) {
							if (data.trim() != '') {
								alert(data);
							}
						} else {
							alert(thuc_hien_thanh_cong, function() {
								if ($('#id').val().trim().length != 0){
									window.location.reload();
									opener.findData();
								}
								else
									window.location.href = 'QcChkOutSrc?hiddenMenu=true&workOrderId='
											+ $('#workOrderId').val() + '&id=' + data.id;
							});
						}

					},
					error : function(data, xhr) {
						$.loader("close");
						var result = chkJson(data);
						if (typeof instanceSaveFalse == 'function') {
							instanceSaveFalse(!result ? data : result);
							return;
						}
						alert(data);
					}
				});

			}

			function changeAmount() {
				var totalAmout = $('input[name="qcChkOutSrc.totalAmount"]').val().convertStringToNumber();
				var subAmount = 0;
				$('.amountDetail').each(function(i, obj) {
					subAmount = subAmount + $(this).val().convertStringToNumber();
				});

				var brokenAmount = 0;
				$('.brokenAmountDetail').each(function(i, obj) {
					brokenAmount = brokenAmount + $(this).val().convertStringToNumber();
				});

				var amount = totalAmout - subAmount - brokenAmount;
				$('input[name="qcChkOutSrc.amount"]').val(formatSo(amount));
			}

			function del() {
				if ($('#id').val().trim().length <= 0) {
					alert(not_select);
					return;
				}
				ShowConfirmDel();
			}
			// Override kg lam gi ca
			function beforeSave() {

			}
			function delSuccess() {
				alert(thuc_hien_thanh_cong, function() {
					window.close();
					window.opener.findData();
				})
			}
		</script>

	</tiles:putAttribute>
</tiles:insertDefinition>
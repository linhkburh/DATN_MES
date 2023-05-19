<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div style="display: none">
	<select id="lstWorker">
		<option value="">- Chọn -</option>
		<c:forEach items="#{lstWorker}" var="item">
			<option value="${item.id}">
				<c:out value="${item.name}" />
			</option>
		</c:forEach>
	</select>
</div>
<div id="modal_dialog_file" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Upload chi tiết báo giá</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form1" action="handler.php" method="POST" enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">File báo giá</label> <input type="file" id="file-select_imp"
									accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
									class="file-modal_imp" name="inputFile" />
							</div>

						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="uploadItem();"><i class="fa fa-upload"></i>Upload</a> <a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>Thoát</a>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Thông tin đơn hàng-báo giá" />
	<tiles:putAttribute name="search_infor_title" value="&ZeroWidthSpace;" />
	<tiles:putAttribute name="search_result_title" value="Danh sách bản vẽ" />
	<tiles:putAttribute name="detail_title" value="Thông tin chi tiết (bản vẽ)" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/item" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>

	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<form:hidden id="to" path="to" />
			<form:hidden id="itemCode" path="itemCode" />
			<c:if test="${to == null}">
				<div class="Table" id="divSearchInf">
					<form:hidden path="quotation.status" id="status" />
					<input type="hidden" id="defaultCurrency" value="${defaultCurrency}"> <input type="hidden"
						id="defaultWaste" value="${defaultWaste}">

					<div id="quotationInf">
						<form:hidden path="quotation.id" id="quotationId" />
						<div class="row">

							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Mã khách hàng<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input type="text" class="required" path="quotation.customer.code" value="${quotation.customer.code }"
									id="qCustomerCode" onchange="selectCustomer(1)"></form:input>
								<form:hidden path="quotation.customer.id" id="customerId" value="${quotation.customer.id}" />

							</div>
							<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12" style="padding-left: 0; margin-left: -10;">
								<a href="#" onclick="selectCustomer(1)" class="pickUp" style="border: none"><i class="fa fa-search"></i></a>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-left: 10;">
								Tên khách hàng<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<div class="row" style="padding: 0;">
									<div class="col-md-11 col-lg-11 col-sm-12 col-xs-12" style="padding: 0">
										<form:input type="text" class="required" path="quotation.customer.orgName"
											value="${quotation.customer.orgName }" id="qCustomerName" onchange="selectCustomer(2)"></form:input>
									</div>
									<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" style="padding-left: 5px;" align="center">
										<a href="#" onclick="selectCustomer(2)" class="pickUp" style="border: none"><i class="fa fa-search"></i></a>
									</div>

								</div>

							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Ngày đơn hàng<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<c:choose>
									<c:when test="${quotation.status == null || quotation.status==0 || quotation.status==3}">
										<form:input path="quotation.quotationDateStr" class="required date" onchange="updateExchangePrice()"
											id="quotationDateStr" />
									</c:when>
									<c:otherwise>
										<form:input path="quotation.quotationDateStr" class="required" readonly="true"
											onchange="updateExchangePrice()" id="quotationDateStr" />
									</c:otherwise>
								</c:choose>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Ngày giao hàng
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<c:choose>
									<c:when test="${quotation.status == null || quotation.status==0 || quotation.status==3}">
										<form:input path="quotation.quotationEndDateStr" id="quotationEndDateStr" cssClass="dateTime"
											onchange="updateExchangePrice()" />
									</c:when>
									<c:otherwise>
										<form:input path="quotation.quotationEndDateStr" id="quotationEndDateStr" readonly="true"
											onchange="updateExchangePrice()" />
									</c:otherwise>
								</c:choose>
							</div>

						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Rate giờ máy(%)<font color="red">*</font>
							</div>
							<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="quotation.ratMachineStr" class='number' presicion='5' scale='2' maxVal='100' id="ratMachine"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Loại tiền<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<c:if test="${quotation.currency == null}">
									<select class="notnull required" onchange="updateExchangePrice()" name="quotation.currency.id"
										id="quotation_currency">
										<option value="">- Chọn -</option>
										<c:forEach items="#{listCurrency}" var="item">
											<option value="${item.id}">
												<c:out value="${item.value}" />
											</option>
										</c:forEach>
									</select>
								</c:if>
								<c:if test="${quotation.currency != null}">
									<form:select class="notnull required" onchange="updateExchangePrice()" path="quotation.currency.id"
										id="quotation_currency">
										<option value="">- Chọn -</option>
										<c:forEach items="#{listCurrency}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.value}" />
											</form:option>
										</c:forEach>
									</form:select>
								</c:if>

							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Mã đơn hàng<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="quotation.code" id="qCode" cssClass="required" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Công ty thành viên<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="quotation.company.id" class="required">
										<option value="">- Chọn -</option>
										<c:forEach items="#{lstCompany}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.name}" />
											</form:option>
										</c:forEach>
									</form:select>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổng tiền (USD)</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<span style="color: red; font-weight: bold;" id="quotationTotal">${quotation.priceStr }</span>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổng tiền quy đổi</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<span id="sExchangePriceStr" style="color: red; font-weight: bold;">${quotation.exchangePriceStr }</span>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ghi chú</div>
							<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
								<form:textarea path="quotation.note" rows="3" id="qNote" />
							</div>
						</div>

						<c:choose>
							<c:when test="${quotation.status == 2}">
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Báo giá thành công</div>
									<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
										<form:checkbox path="quotation.done" id="qDone" />
									</div>

								</div>
							</c:when>
							<c:when test="${quotation.status>=2 && quotation.status!=3}">
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Báo giá thành công</div>
									<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
										<form:checkbox path="quotation.done" id="qDone" disabled="true" />
									</div>

								</div>

							</c:when>

						</c:choose>
					</div>


					<div align="center" style="margin-bottom: -25">
						<input type="button" onclick="processQuotation();" value="&#xf0c7; Cập nhập" class="btn blue fa">
						<c:if test="${add && (quotation.status == null || quotation.status==0 || quotation.status==3)}">
							<input type="button" onclick="delQuotation();" value="&#xf2d3; Xóa đơn hàng" class="btn red fa">
						</c:if>
						<c:if test="${!add}">
							<script type="text/javascript">
								disabledDiv('quotationInf', true);
								$('#qNote').prop('disabled', false);
							</script>

						</c:if>


					</div>
					<c:if test="${quotation.status>=1 && quotation.status!=3}">
						<script type="text/javascript">
							$(document).ready(function() {
								disabledDiv('quotationInf', true);
								$('#qNote').prop('disabled', false);
							});
						</script>
					</c:if>
					<c:if test="${quotation.status>=2 && quotation.status!=3}">
						<script type="text/javascript">
							$(document).ready(function() {
								disabledDiv('quotationInf', true);
								$('#qDone').prop('disabled', false);
								$('#qNote').prop('disabled', false);
							});
						</script>
					</c:if>
				</div>
			</c:if>


		</tiles:putAttribute>




		<tiles:putAttribute name="catDetail" cascade="true">
			
			<%@ include file="/page/dung_chung/upload_file_ftp.jsp"%>
			<%@ include file="/page/ke_hoach_san_xuat/chi_tiet_chi_tiet.jsp"%>
		</tiles:putAttribute>

	</form:form>

	<spring:url var="sendReportAction" value="/item/upload"></spring:url>




	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function selectCustomer(p_type) {
				var cusCode, cusName;
				if (p_type == 1) {
					if ($('#qCustomerCode').val().trim().length == 0) {
						window.open('customerMng?hiddenMenu=true&to=select', '', window_property).focus();
						return;
					}
					cusCode = $('input[name="quotation.customer.code"]').val().trim();
				} else {
					if ($('#qCustomerName').val().trim().length == 0) {
						window.open('customerMng?hiddenMenu=true&to=select', '', window_property).focus();
						return;
					}
					cusName = $('input[name="quotation.customer.orgName"]').val().trim();
				}

				$.getJSON('quotation?method=getCustomerIfOnly', {
					"tokenIdKey" : $('#tokenIdKey').val(),
					"tokenId" : $('#tokenId').val(),
					"cusCode" : cusCode,
					"cusName" : cusName
				}).done(function(res) {
					// Cap nhat id, code
					$('input[name="quotation.customer.code"]').val(res.code);
					$('input[name="quotation.customer.orgName"]').val(res.orgName);
					$('#customerId').val(res.id);
				}).fail(
						function() {
							if (p_type == 1) {
								window.open('customerMng?hiddenMenu=true&to=select&cuCode=' + cusCode, '',
										window_property).focus();
							} else {
								window.open('customerMng?hiddenMenu=true&to=select&orgName=' + cusName, '',
										window_property).focus();
							}
						});

			}

			function setCustomer(customerId) {
				$.ajax({
					url : 'quotation?method=getCustomerById',
					data : {
						customerId : customerId
					},
					method : 'GET',
					success : function(res) {
						$('input[name="quotation.customer.code"]').val(res.code);
						$('input[name="quotation.customer.orgName"]').val(res.orgName);
						$('#customerId').val(res.id);
					}
				});
			}
			function refreshQie() {
				edit($('#id').val());
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
				formData.append("quotationId", $("#quotationId").val());
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${sendReportAction}', true);
				xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
				$.loader({
					className : "blue-with-image-2"
				});

				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						alert('Thực hiện thành công!', function() {
							window.location = 'item?quotationId=' + xhr.responseText;
						});

					} else {
						alert('Import không thành công');

					}
				};
				xhr.send(formData);

			}
		</script>
		<script src="<spring:url value="/page/bao_gia/chi_tiet_bao_gia.js?v=4" />"></script>
	</tiles:putAttribute>
</tiles:insertDefinition>
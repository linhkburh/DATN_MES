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

#divDatatable tbody td {
	font-size: 12 !important;
	padding: 0.3rem !important;
}

#divDatatable thead th {
	padding: 0.3rem !important;
}
</style>
<div>
	<select style="display: none" id="lstCus" name="lstCus">
		<option value="">- Chọn -</option>
		<c:forEach items="#{lstCus}" var="item">
			<option value="${item.id}">
				<c:out value="${item.orgName}" />
			</option>
		</c:forEach>
	</select>
</div>

<div id="lstPackageDialog" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<c:choose>
					<c:when test="${param.type == 'OS'}">
						<h4 class="modal-title">Danh sách chuyển gia công ngoài</h4>
					</c:when>
					<c:otherwise>
						<h4 class="modal-title">Danh sách đóng gói</h4>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="modal-body ">
				<div class="table-responsive">
					<table class="table table-bordered" id="tblLstPackage">
						<thead>
							<tr>
								<th>Mã bản vẽ</th>
								<th>Mã quản lý</th>
								<th>Số lượng</th>
								<th style="text-align: left; width: 150px !important;">Đơn vị GCN<font color="red">*</font></th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="modal-footer">
					<input type="button" onclick="ok()" value="Chuyển" class="btn blue">
					<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
				</div>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		<c:if test="${param.type == 'OS'}">
	        Chuyển gia công ngoài
	   	</c:if>
		<c:if test="${param.type != 'OS'}">
	   		Lịch sử QC
	   	</c:if>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/osHistory" var="formAction" />
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
					<c:if test="${param.type != 'OS'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại QC</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="hisType" onchange="$('#theForm').attr('action','processExe').submit()">
									<form:option value="hQc">Thành phẩm</form:option>
									<form:option value="hQcO">Bán thành phẩm</form:option>
								</form:select>
							</div>
						</div>
					</c:if>

					<c:choose>
						<c:when test="${param.type == 'OS'}">
							<div class="row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chuyển GCN</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<form:select path="qcType" onchange="$('#theForm').attr('action','proExport').submit()">
										<form:option value="tp">Thành phẩm</form:option>
										<form:option value="btp">Bán thành phẩm</form:option>
									</form:select>
								</div>
							</div>
							<div class="row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
									<spring:message code="QuotationItem.code.draw" />
								</div>
								<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
									<form:input path="drawingCode"></form:input>
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
									<form:input path="manageCode" />
								</div>
								<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text" style="font-size:">
									<spring:message code="QuotationItem.search.info" />
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
						</c:when>
						<c:otherwise>
							<div class="row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã LSX</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<form:input path="workCode" />
								</div>
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<form:input path="manageCode" />
								</div>

							</div>

						</c:otherwise>
					</c:choose>
					<c:if test="${param.type == 'OS'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm nhận từ sản xuất, từ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="frDate" cssClass="date" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="tDate" cssClass="date" />
							</div>
						</div>
					</c:if>
					<c:if test="${param.type != 'OS'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày tạo mã quản lý, từ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="frDate" cssClass="date" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="tDate" cssClass="date" />
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công nhân</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="worker">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUser}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.username}" />-<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Cán bộ nhập liệu</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="updatorSearch">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUser}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.username}" />-<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</c:if>
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();"
							value="&#xf002; <spring:message code="common.btn.search"/>" />
					</div>
					<div class="HeaderText">
						<spring:message code="common.search.results" />
					</div>
				</div>
				<c:if test="${param.type!='OS'}">
					<c:set var="customDivDataTable" scope="request" value="true" />
					<div id="divDatatable" class="table-responsive">

						<table id="tblSearchResult" class="table">
							<thead>
								<tr>
									<th rowspan='2' width="2%">STT</th>
									<th rowspan='2'>Mã LSX</th>
									<th rowspan='2'>Mã quản lý</th>
									<th rowspan='2'>Công nhân</th>
									<th rowspan='2'>Thời điểm bắt đầu</th>
									<th rowspan='2'>Thời điểm kết thúc</th>
									<th rowspan='2'>Thời gian thực hiện</th>
									<th colspan='4' style='text-align: center'>Tổng số chi tiết</th>
									<th rowspan='2'>Cán bộ nhập liệu</th>
									<th rowspan='2'>Cập nhật</th>
								</tr>
								<tr>
									<!-- Da thuc hien -->
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.quantity" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.done" /></th>
									<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.fail" /></th>
									<th style="font-size: 12px !important;">Hủy</th>
								</tr>
							</thead>
						</table>
					</div>
				</c:if>
			</div>
		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendReportAction" value="/historyPro/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				if ($('#type').val() == 'OS') {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã LSX'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã đơn hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Khách hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã bản vẽ'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Ngày giao hàng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã quản lý'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời điểm nhận từ sản xuất'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng chi tiết'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng đã chuyển'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : '<input type="checkbox" onchange="selectAll($(this).prop(\'checked\'))">'
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
						"sClass" : "left",//Thời gian setup
						"bSortable" : false
					}, {
						"sClass" : "left",//Thời điểm kết thúc
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
				if ($('#type').val() == 'OS') {
					tblCfg.buttons = [ {
						text : 'Chuyển GCN',
						attr : {
							id : 'ProductExport'
						},
						className : 'mainGrid btnImp btn blue',
						action : function(e, dt, node, config) {
							preview();
						}
					} ];
				}
			}

			$(document)
					.ready(
							function() {
								$('.btnDtAdd').hide();
								var rowTmp;
								if ($('#type').val() == 'OS') {
									rowTmp = [
											'<font></font>',
											/* '<span name="lstQcOs[].workOrderCode"></span>',
											'<span name="lstQcOs[].manageCode"></span>', */
											'<span name="lstQcOs[].drawingCode"></span>',
											'<span name="lstQcOs[].dsmaql"></span><input type="hidden" name="lstQcOs[].dsmaql">',
											'<span name="lstQcOs[].qlt"></span><input type="hidden" name="lstQcOs[].amount">',
											'<select name="lstQcOs[].customers.id" style="text-align: left;width:100%">'
													+ $('#lstCus').html()
													+ '</select><input type="hidden" name="lstQcOs[].ids"><input type="hidden" name="lstQcOs[].amounts">' ];
								}
								tblLstPackage = new TFOnline.DataTable({
									id : 'tblLstPackage',
									jQueryUI : true,
									rowTemp : rowTmp,
									hasCheck : false,
									hasOrder : true,
									readOnly : true,
									maxRow : 100
								});
							});

			function openDetail(osId, workOrderId) {
				window.open('QcChkOutSrc?hiddenMenu=true&workOrderId=' + workOrderId + '&id=' + osId, '',
						'width=1100, height=700, status=yes, scrollbars=yes');
			}
			function preview() {
				var lst = '';
				var lstData = '';
				$('.selectRow:checked').each(function() {
					lst += this.value + "_";
					lstData += $('input[name="' + this.value + '"]').val() + "_";
				});
				if (lst === '') {
					alert('Bạn chưa chọn bản ghi');
					return;
				}
				$.ajax({
					type : "POST",
					url : $('#theForm').attr('action'),
					data : {
						lstId : lst,
						lstData : lstData,
						method : 'preview',
						type : $('#type').val()
					},
					success : function(lstPackage, status, xhr) {
						if (!chkJson(lstPackage, xhr)) {
							alert(lstPackage);
							return;
						}
						$('#lstPackageDialog').modal('show');
						tblLstPackage.resize(lstPackage.length);

						for (var i = 0; i < lstPackage.length; i++) {
							$('input[name="lstQcOs[' + i + '].ids"]').val(lstPackage[i].ids);
							//$('span[name="lstQcOs[' + i + '].workOrderCode"]').html(lstPackage[i].workOrderCode);
							$('span[name="lstQcOs[' + i + '].drawingCode"]').html(
									lstPackage[i].manageCode);
							$('span[name="lstQcOs[' + i + '].dsmaql"]').html(lstPackage[i].dsmaql);
							$('input[name="lstQcOs[' + i + '].dsmaql"]').val(lstPackage[i].dsmaql);
							$('span[name="lstQcOs[' + i + '].qlt"]').html(lstPackage[i].amount);
							$('input[name="lstQcOs[' + i + '].amount"]').val(lstPackage[i].amount);
							$('input[name="lstQcOs[' + i + '].amounts"]').val(lstPackage[i].amounts);
						}
						initControl();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(textStatus, errorThrown);
					}
				});
			}
			function ok() {
				jConfirm('Bạn có chắc chắn muốn GCN danh sách?', 'OK', 'Cancel', function(r) {
					if (r) {
						var lst = '';
						var lstData = '';
						$('.selectRow:checked').each(function() {
							lst += this.value + "_";
							lstData += $('input[name="' + this.value + '"]').val() + "_";
						});
						if (lst === '') {
							alert('Bạn chưa chọn bản ghi');
							return;
						}
						$.ajax({
							type : "POST",
							url : location.pathname + '?method=saveQcOs&type=' + $('#type').val(),
							data : $('#tblLstPackage').find('input,select').serialize(),
							success : function(data, status, xhr) {
								// Resource exception
								if (data != '') {
									alert(data);
									return;
								}
								alert(thuc_hien_thanh_cong, function() {
									$('#lstPackageDialog').modal('hide');
									findData();
								});

							}
						});
					}
				});

			}
			function selectAll(isSelect) {
				$('input.selectRow[type="checkbox"]').each(function() {
					$(this).prop('checked', isSelect);
					changeNumberFinish($(this));
				});
			}
			function changeNumberFinish(sel) {
				var numberFinishName = $(sel).attr("value");
				if ($('input[name="chk_' + numberFinishName + '"]').prop('checked')) {
					var number = $(sel).attr("outStandingAmount");
					$('input[name="' + numberFinishName + '"]').prop('disabled', false);
					if ($('input[name="' + numberFinishName + '"]').val() == '')
						$('input[name="' + numberFinishName + '"]').val(number);
				} else {
					$('input[name="' + numberFinishName + '"]').prop('disabled', true);
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



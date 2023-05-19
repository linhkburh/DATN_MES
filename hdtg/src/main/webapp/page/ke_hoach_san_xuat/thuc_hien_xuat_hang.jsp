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
</style>
<div style="display: none">
	<select style="display: none" id="lstCus" name="lstCus">
		<option value=""></option>
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
								<c:if test="${param.type != 'OS'}">
									<th>Khách hàng</th>
								</c:if>
								<th>Mã bản vẽ</th>
								<th>Tên chi tiết</th>
								<th>Mã quản lý</th>
								<th>Số lượng</th>
								<c:choose>
									<c:when test="${param.type == 'OS'}">
										<th style="text-align: left; width: 150px !important;">Đơn vị GCN</th>
									</c:when>
									<c:otherwise>
										<th style="text-align: right;">Số lượng chi tiết/gói</th>
									</c:otherwise>
								</c:choose>
							</tr>
						</thead>
					</table>
				</div>
				<div class="modal-footer">
				<c:choose>
					<c:when test="${param.type == 'OS'}">
						<input type="button" onclick="ok()" value="Chuyển" class="btn blue">
					</c:when>
					<c:otherwise>
						<input type="button" onclick="ok()" value="Đóng gói" class="btn blue">
					</c:otherwise>
				</c:choose>
					<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
				</div>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title">
		<c:if test="${param.type == 'CL'}">
        	 Chuyển nguội
    	</c:if>
		<c:if test="${param.type == 'QC'}">
    		Chuyển QC
    		</c:if>
    	<c:if test="${param.type == 'OS'}">
    		Chuyển gia công ngoài
    	</c:if>
		<c:if test="${param.type == null}">
    		Thực hiện đóng gói
    	</c:if>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/proExport" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<form:hidden path="type" id="type" />
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
				<c:if test="${param.type == 'OS'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chuyển GCN</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="qcType" onchange="$('#theForm').attr('action','osHistory').submit()">
									<form:option value="tp">Thành phẩm</form:option>
									<form:option value="btp">Bán thành phẩm</form:option>
								</form:select>
							</div>
						</div>
				</c:if>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.draw" />
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
							<form:input path="drawingCode"></form:input>
						</div>
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text"><spring:message code="QuotationItem.search.info" /></div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.manager" />
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
							<form:input path="manageCode" />
						</div>
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text" style="font-size:"><spring:message code="QuotationItem.search.info" /></div>
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
					<c:if test="${param.type == 'OS'}">
			        	 <div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm tạo bản vẽ, từ</div>
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
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày giao hàng từ ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="fromDate" id="fromDate" class="date"></form:input>
						</div>    
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="toDate" id="toDate" class="date"></form:input>
						</div>
					</div>
			    	</c:if>
			    	
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm" />
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				if ($('#type').val() == 'OS'){
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
						"sTitle" : 'Tên khách hàng'
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
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng hoàn thành/Tổng số'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng đã chuyển/Số lượng tại QC'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : '<input type="checkbox" onchange="selectAll($(this).prop(\'checked\'))">'
					} ];
				}else{
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
						"sTitle" : 'Tên khách hàng'
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
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng hoàn thành/Tổng số'
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
				}
				
				if ($('#type').val() == 'CL') {
					tblCfg.buttons = [ {
						text : 'Nguội',
						attr : {
							id : 'ProductExport'
						},
						className : 'mainGrid btnImp btn blue',
						action : function(e, dt, node, config) {
							next();
						}
					} ];
				} else if ($('#type').val() == 'QC') {
					tblCfg.buttons = [ {
						text : 'QC',
						attr : {
							id : 'ProductExport'
						},
						className : 'mainGrid btnImp btn blue',
						action : function(e, dt, node, config) {
							next();
						}
					} ];
				} else if ($('#type').val() == 'OS') {
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
				} else {
					tblCfg.buttons = [ {
						text : 'Đóng gói',
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
			function ok() {
				var cf = 'Bạn có chắc chắn muốn đóng gói danh sách?';
				if ($('#type').val() == 'OS') {
					cf = 'Bạn có chắc chắn muốn GCN danh sách?';
				}
				jConfirm(cf, 'OK', 'Cancel', function(r) {
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
							url : location.pathname + '?method=save&type=' + $('#type').val(),
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
			$(document)
					.ready(
							function() {
								var rowTmp;
								if ($('#type').val() == 'OS'){
									rowTmp = [
										'<font></font>',
										'<span name="lstQcOs[].quotationItemCode"></span>',
										'<span name="lstQcOs[].quotationItemName"></span>',
										'<span name="lstQcOs[].dsmaql"></span><input type="hidden" name="lstQcOs[].dsmaql">',
										'<span name="lstQcOs[].qlt"></span><input type="hidden" name="lstQcOs[].amount">',
										'<select name="lstQcOs[].customers.id" style="text-align: left;width:100%">'+ $('#lstCus').html() + '</select><input type="hidden" name="lstQcOs[].ids"><input type="hidden" name="lstQcOs[].amounts">'
										];
								}
								else{
									rowTmp = [
										'<font></font>',
										'<span name="lstPackage[].customer.code"></span>',
										'<span name="lstPackage[].quotationItemCode"></span>',
										'<span name="lstPackage[].quotationItemName"></span>',
										'<span name="lstPackage[].qrContent"></span>',
										'<span name="lstPackage[].qlt"></span><input type="hidden" name="lstPackage[].qualityStr">',
										'<input class="textint" name="lstPackage[].numPerPackageStr" style="text-align: right;width:100%"><input type="hidden" name="lstPackage[].ids"><input type="hidden" name="lstPackage[].amounts">'
										];
								}
								tblLstPackage = new TFOnline.DataTable(
										{
											id : 'tblLstPackage',
											jQueryUI : true,
											rowTemp : rowTmp,
											hasCheck : false,
											hasOrder : true,
											readOnly : true,
											maxRow : 100
										});
							});
			function next() {
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
					url : location.pathname + '?method=next',
					data : {
						lstId : lst,
						lstData : lstData,
						type : $('#type').val()
					},
					success : function(data, status, xhr) {
						if (data == '')
							alert(thuc_hien_thanh_cong, findData);
						else
							alert(data);

					}
				});
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
					url : location.pathname,
					data : {
						lstId : lst,
						lstData : lstData,
						method:'preview',
						type:$('#type').val()
					},
					success : function(lstPackage, status, xhr) {
						if (!chkJson(lstPackage, xhr)) {
							alert(lstPackage);
							return;
						}
						$('#lstPackageDialog').modal('show');
						tblLstPackage.resize(lstPackage.length);
						
						
						if ($('#type').val() == 'OS'){
							clearDiv('tblLstPackage');
							for (var i = 0; i < lstPackage.length; i++) {
								$('input[name="lstQcOs[' + i + '].ids"]').val(lstPackage[i].ids);
								$('span[name="lstQcOs[' + i + '].quotationItemCode"]').html(
										lstPackage[i].quotationItemCode);
								$('span[name="lstQcOs[' + i + '].quotationItemName"]').html(
										lstPackage[i].quotationItemName);
								$('span[name="lstQcOs[' + i + '].dsmaql"]').html(lstPackage[i].dsMaQl);
								$('input[name="lstQcOs[' + i + '].dsmaql"]').val(lstPackage[i].dsMaQl);
								$('span[name="lstQcOs[' + i + '].qlt"]').html(lstPackage[i].qualityStr);
								$('input[name="lstQcOs[' + i + '].amount"]').val(lstPackage[i].qualityStr);
								$('input[name="lstQcOs[' + i + '].amounts"]').val(lstPackage[i].amounts);

							}
						}else{
							for (var i = 0; i < lstPackage.length; i++) {
								$('input[name="lstPackage[' + i + '].ids"]').val(lstPackage[i].ids);
								$('span[name="lstPackage[' + i + '].customer.code"]').html(lstPackage[i].customer.code);
								$('span[name="lstPackage[' + i + '].quotationItemCode"]').html(
										lstPackage[i].quotationItemCode);
								$('span[name="lstPackage[' + i + '].quotationItemName"]').html(
										lstPackage[i].quotationItemName);
								$('span[name="lstPackage[' + i + '].qrContent"]').html(lstPackage[i].dsMaQl);
								$('span[name="lstPackage[' + i + '].qlt"]').html(lstPackage[i].qualityStr);
								$('input[name="lstPackage[' + i + '].qualityStr"]').val(lstPackage[i].qualityStr);
								$('input[name="lstPackage[' + i + '].numPerPackageStr"]').val(lstPackage[i].qualityStr);
								$('input[name="lstPackage[' + i + '].amounts"]').val(lstPackage[i].amounts);

							}
						}
						
						initControl();
					}
				});
			}
			function productExport() {
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
					url : $('#theForm').attr('action') + '?method=getAllQuotationItemFinish',
					data : {
						lstId : lst,
						lstData : lstData
					},
					success : function(data) {
						if (data != '') {
							alert(data);
						} else {
							alert(thuc_hien_thanh_cong, function() {
								findData();
							});
						}
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
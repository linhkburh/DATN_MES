<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<select id="selectAstMachine" style="display: none;">
	<option value="">- Chọn -</option>
	<c:forEach items="#{lstAstMachine}" var="item">
		<option value="${item.id}">
			<c:out value="${item.astName}" />
		</option>
	</c:forEach>
</select>
<div id="modal_dialog_file" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Upload LSX</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST" enctype="multipart/form-data">
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
	<tiles:putAttribute name="title" value="Tạo LSX" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/datlenhsx" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="divaction" align="center">
						<input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm" />
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="quotationItemExe.id" id="id" />
			<form:hidden path="quotationItemExe.exeStepId.id" />
			<form:hidden path="quotationItem.id" />
			<form:hidden path="makeWoByQi" id="makeWoByQi" />
			<c:if test="${formDataModelAttr.makeWoByQi}">
				<div class="Header2">Chi tiết</div>
			</c:if>
			<c:if test="${!formDataModelAttr.makeWoByQi}">
				<div class="Header2">Công đoạn</div>
			</c:if>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã đơn hàng</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="quotationItemExe.orderCode" readonly="true" title="Mã không được để trống" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm giao hàng</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input readonly="true" path="quotationItemExe.quotationItemId.deliverDateStr" />
				</div>

			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên khách hàng</div>
				<div class="col-md-5 col-lg-5 col-sm-12 col-xs-12">
					<form:input readonly="true" path="quotationItemExe.quotationItemId.quotationId.customer.orgName" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian còn lại (giờ)</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<span style="font-weight: bold;"><font color="red">${formDataModelAttr.quotationItemExe.quotationItemId.remainingTimeStr}</font></span>


				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input readonly="true" path="quotationItemExe.quotationItemId.code" cssClass="" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>

				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng chi tiết</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input onchange="changeAmount(this);" readonly="true" path="quotationItemExe.quotationItemId.qualityStr"
						cssClass="number" />
				</div>
			</div>
			<c:if test="${formDataModelAttr.makeWoByQi}">
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian TB/Chi tiết (phút)</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItem.avageEstTimeStr" cssClass="number" readonly="true" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổng thời gian (giờ)</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItem.totalEstTimeStr" cssClass="number" readonly="true" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItem.manageCode" readonly="true" id='manageCode' />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian setup (phút)</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItem.bookingSetupTimeStr" readonly="true" id='manageCode' />
					</div>
				</div>
			</c:if>
			<c:if test="${!formDataModelAttr.makeWoByQi}">
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công đoạn</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input readonly="true" path="quotationItemExe.exeStepId.stepName" />
					</div>

					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian TB/Chi tiết (phút)</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItemExe.unitExeTimeStr" cssClass="number" readonly="true" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian dự kiến (giờ)</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItemExe.totalEstTimeStr" cssClass="number" readonly="true" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="color: red">Số lượng chi tiết chưa
						phân công</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItemExe.remainStr" cssClass="number" readonly="true" />
					</div>
				</div>
			</c:if>


			<c:if test="${formDataModelAttr.makeWoByQi}">

				<div class="Header2">Tạo LSX</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng dây chuyền<font color="red">*</font>
					</div>
					<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12" style="padding-right: 0">
						<form:input path="numOfMachine" cssClass="number required" id='numOfMachine' maxlength="3" />
					</div>
					<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" style="margin-top: -15px; padding: 0">
						<input type="button" value="&#xf085; Tạo lệnh" class="btn blue fa" onclick="makeWorkOrder('numOfMachine')"
							style="font-size: 12" />
					</div>
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12"></div>

					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" style="margin-top: -15px">
						<input type="button" value="&#xf085; In lệnh sản xuất" class="btn blue fa" onclick="printfQr()"
							style="font-size: 12" />
					</div>

				</div>
			</c:if>


			<c:if test="${formDataModelAttr.makeWoByQi}">
				<div class="Header3">Danh sách dây chuyền</div>
				<div class="table-responsive" style="width: 70%">
					<table class="table table-bordered" id="table-production_line">
						<thead style="background-color: steelblue">
							<tr>
								<th style="width: 10%">STT</th>
								<th style="width: 30%; text-align: right;">Số lượng chi tiết</th>
								<th style="width: 30%; text-align: right;">Thời gian setup (phút)</th>
								<th style="width: 30%; text-align: right">Thời gian thực hiện (giờ)</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${formDataModelAttr.quotationItem.lstProductionLine}" varStatus="loop">
								<tr>
									<td>${loop.index+1}</td>
									<td align="right">${item.amount}</td>
									<td align="right">${item.qi.setupTimeAvgStr}</td>
									<td align="right">${item.totalEstTimeStr}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</c:if>


			<div class="Header2">Danh sách LSX</div>
			<c:if test="${formDataModelAttr.makeWoByQi}">
				<div class="table-responsive">
					<table class="table table-bordered" style="table-layout: fixed;" id="table-exe">
						<thead>
							<tr>
								<th style="width: 20%">Mã LSX<font color="red">*</font></th>
								<th style="width: 15%">Công đoạn<font color="red">*</font></th>
								<th style="width: 15%">Máy gia công</th>
								<th style="width: 10%">Thời gian chờ (giờ)</th>
								<th style="width: 10%">Số lượng chi tiết<font color="red">*</font></th>
								<th style="width: 10%">Số lượng chi tiết đã sản xuất</th>
								<th style="width: 10%">Thời gian thực hiện (giờ)</th>
								<th style="width: 10%">Tổng thời gian chờ (giờ)</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${formDataModelAttr.quotationItem.workOrders}" varStatus="loop">
								<tr>
									<td><input type="hidden" name="quotationItem.workOrders[].id" value="${item.id}" /> <input
										type="hidden" name="quotationItem.workOrders[].productionLineId" value="${item.productionLineId}" /> <input
										type="text" readonly="readonly" name="quotationItem.workOrders[].code" class="required"
										value="${item.code}" /></td>
									<td><input type="hidden" name="quotationItem.workOrders[].quotationItemExe.id"
										value="${item.quotationItemExe.id}" /> <input type="text" readonly="readonly"
										name="quotationItem.workOrders[].quotationItemExe.exeStepId.stepName" class="required"
										value="${item.quotationItemExe.exeStepId.stepName}" /></td>
									<td><select onchange="changeMachine(this);" name="quotationItem.workOrders[].astMachine.id">
											<option value=""></option>
											<c:forEach items="#{lstAstMachine}" var="items">
												<c:choose>
													<c:when test="${item.astMachine.id == items.id}">
														<option value="${items.id}" selected="selected">
															<c:out value="${items.astName}" />
														</option>
													</c:when>
													<c:otherwise>
														<option value="${items.id}">
															<c:out value="${items.astName}" />
														</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
									</select></td>
									<td><input readonly="readonly" type="text" name="quotationItem.workOrders[].waitTimeStr"
										value="${item.waitTimeStr}" /></td>
									<td><input type="text" onchange="changeAmountOrder(this);" name="quotationItem.workOrders[].amountStr"
										readonly="readonly" class="required" value="${item.amountStr}" /></td>
									<td><form:input path="quotationItem.workOrders[${loop.index}].numOfFinishItemStr" readonly="true" /></td>
									<td><input readonly="readonly" type="text" name="quotationItem.workOrders[].totalEstTimeStr"
										value="${item.totalEstTimeStr}" /></td>
									<td><input readonly="readonly" type="text" name="quotationItem.workOrders[].totalTimeDelay"
										value="${item.totalTimeDelay}" /></td>


								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>

			<c:if test="${!formDataModelAttr.makeWoByQi}">
				<div class="table-responsive">
					<table class="table table-bordered" style="table-layout: fixed;" id="table-exe">
						<thead>
							<tr>
								<th style="width: 25%">Mã LSX<font color="red">*</font></th>
								<th style="width: 15%">Máy gia công</th>
								<th style="width: 10%">Thời gian chờ (giờ)</th>
								<th style="width: 10%">Số lượng chi tiết<font color="red">*</font></th>
								<th style="width: 14%">Số lượng chi tiết đã sản xuất</th>
								<th style="width: 13%">Thời gian thực hiện (giờ)</th>
								<th style="width: 13%">Tổng thời gian chờ (giờ)</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${formDataModelAttr.quotationItemExe.workOrders}" varStatus="loop">
								<tr>
									<td><input type="hidden" name="quotationItemExe.workOrders[].id" value="${item.id}" /> <input type="text"
										readonly="readonly" name="quotationItemExe.workOrders[].code" class="required" value="${item.code}" /></td>
									<td><select onchange="changeMachine(this);" name="quotationItemExe.workOrders[].astMachine.id">
											<option value="">- Chọn -</option>
											<c:forEach items="#{lstAstMachine}" var="items">
												<c:choose>
													<c:when test="${item.astMachine.id == items.id}">
														<option value="${items.id}" selected="selected">
															<c:out value="${items.astName}" />
														</option>
													</c:when>
													<c:otherwise>
														<option value="${items.id}">
															<c:out value="${items.astName}" />
														</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
									</select></td>
									<td><input readonly="readonly" type="text" name="quotationItemExe.workOrders[].waitTimeStr"
										value="${item.waitTimeStr}" /></td>
									<td><input type="text" onchange="changeAmountOrder(this);" name="quotationItemExe.workOrders[].amountStr"
										readonly="readonly" class="required" value="${item.amountStr}" /></td>
									<td><form:input path="quotationItemExe.workOrders[${loop.index}].numOfFinishItemStr" readonly="true" /></td>
									<td><input readonly="readonly" type="text" name="quotationItemExe.workOrders[].totalEstTimeStr"
										value="${item.totalEstTimeStr}" /></td>
									<td><input readonly="readonly" type="text" name="quotationItemExe.workOrders[].totalTimeDelay"
										value="${item.totalTimeDelay}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>

		</tiles:putAttribute>
	</form:form>

	<spring:url var="sendReportAction" value="/datlenhsx/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function printfQr(){
				$.ajax({
			        url: 'datlenhsx?method=printfQr',
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
			            	  alert(data);
			              });
			              return;
			            }
			            var a = document.createElement('a');
			            var url = window.URL.createObjectURL(data);
			            a.href = url;
			            a.download = 'lenh san xuat ' +$('#manageCode').val()+ '.pdf';
			            document.body.append(a);
			            a.click();
			            a.remove();
			            window.URL.revokeObjectURL(url);
			        },
			        error:function(){
			        }
			    });
			}
			function initParam(tblCfg) {
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số lượng từ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'đến'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mức giảm giá'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mô tả'
				} ];
				tblCfg.notSearchWhenStart = true;
				tblCfg.buttons = [ {
					text : 'Import LSX',
					attr : {
						id : 'import'
					},
					className : 'mainGrid btnImp btn blue',
					action : function(e, dt, node, config) {
						uploadDetails();
					}
				}, {
					text : 'Download template',
					attr : {
						id : 'download'
					},
					className : 'mainGrid btnImp btn blue',
					action : function(e, dt, node, config) {
						download();
					}
				} ];
			}
			$(document).ready(function() {
				$('div.detail_title.HeaderText').hide();
				$('.btnDtDelete').hide();
				if ($('#makeWoByQi').val() != 'true') {
					rowTemp = initRowTableWorkOderExe('quotationItemExe');
					$('#btnSave, #btnDel').remove();
				}else{
					rowTemp = initRowTableWorkOderExe('quotationItem');
				}
				tableWorkOrder = new TFOnline.DataTable({
					id : 'table-exe',
					jQueryUI : true,
					hasOrder : true,
					readOnly : true,
					rowTemp : rowTemp,
					afterAddRow : function() {
						initControl();
					},
					maxRow : 100
				});
				makeTableProductionLine();
			});
			function makeTableProductionLine(data){
				if(data==undefined){
					return $('#table-production_line').DataTable(
							{
								"ordering": false,
								dom: "t<'row'<'col-sm-5'i><'col-sm-7'p>>",
								"lengthChange": false,
								"bFilter" : false,
								"oLanguage" : {
									"sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
									"sZeroRecords" : " ",
									"sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ "
											+ "bản ghi",
									"sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số"
											+ " 0 " + "bản ghi",
									"sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
									"oPaginate" : {
										"sFirst" : "<i class='fa fa-fast-backward'></i>",
										"sLast" : "<i class='fa fa-fast-forward'></i>",
										"sPrevious" : "<i class='fa fa-backward'></i>",
										"sNext" : "<i class='fa fa-forward'></i>"
									},
									"sSearch" : "Từ khóa"
								},
								"bDestroy" : true
							});
				}else{
					return $('#table-production_line').DataTable(
							{	"ordering": false,
								dom: "t<'row'<'col-sm-5'i><'col-sm-7'p>>",
								"data":data,
								columns : [ {
									data : 'orderNum'
								}, {
									data : 'amount',
									"sClass" : "right"
								}, {
									data : 'setupTimeAvgStr',
									"sClass" : "right"
								}, {
									data : 'totalEstTime',
									"sClass" : "right"
								}],
								"sdom":"",
								"lengthChange": false,
								"bFilter" : false,
								"oLanguage" : {
									"sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
									"sZeroRecords" : " ",
									"sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ "
											+ "bản ghi",
									"sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số"
											+ " 0 " + "bản ghi",
									"sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
									"oPaginate" : {
										"sFirst" : "<i class='fa fa-fast-backward'></i>",
										"sLast" : "<i class='fa fa-fast-forward'></i>",
										"sPrevious" : "<i class='fa fa-backward'></i>",
										"sNext" : "<i class='fa fa-forward'></i>"
									},
									"sSearch" : "Từ khóa"
								},
								"bDestroy" : true
							});
				}
				
			}
			function download() {
				window.open('datlenhsx?method=download').focus();
			}
			function beforeLoad() {
				var url = window.location.href;
				var tmp = url.split('?');
				if (tmp.length > 1) {
					$('#divDetail').css('display', 'block');
					$('#divGrid').css('display', 'none');
				} else {
					$('#divDetail').css('display', 'none');
					$('#divGrid').css('display', 'block');

				}
			}

			function initRowTableWorkOderExe(obj) {
				if ($('#makeWoByQi').val() == 'true') {
					return [
							'',
							'<input type="hidden" name="quotationItem.workOrders[].id"><input type="hidden" name="quotationItem.workOrders[].productionLineId"><input readonly="readonly"  type="text" name="' + obj + '.workOrders[].code" class="form-control required"/>',
							'<input type="hidden" name="quotationItem.workOrders[].quotationItemExe.id"><input readonly="readonly"  type="text" name="' + obj + '.workOrders[].quotationItemExe.exeStepId.stepName" class="form-control required"/>',
							'<select onchange="changeMachine(this);" name="' + obj
									+ '.workOrders[].astMachine.id" class="form-control" >'
									+ $('#selectAstMachine').html() + '</select>',
							'<input readonly="readonly" type="text" name="' + obj + '.workOrders[].waitTimeStr" class="form-control required" />',
							'<input onchange="changeAmountOrder(this);" type="text" name="' + obj
									+ '.workOrders[].amountStr" class="form-control" readonly="readonly"/>',
							'<input type="text" name="' + obj
								+ '.workOrders[].numOfFinishItemStr" readonly="true" class="form-control" />',
							'<input readonly="readonly" type="text" name="' + obj + '.workOrders[].totalEstTimeStr" class="form-control" /> ',
							'<input readonly="readonly" type="text" name="' + obj + '.workOrders[].totalTimeDelay" class="form-control" />'];
				}
				return [
						'',
						'<input type="hidden" name="quotationItemExe.workOrders[].id"><input type="hidden" name="quotationItemExe.workOrders[].productionLineId"><input readonly="readonly"  type="text" name="' + obj + '.workOrders[].code" class="form-control required"/>',
						'<select onchange="changeMachine(this);" name="' + obj
								+ '.workOrders[].astMachine.id" class="form-control" >'
								+ $('#selectAstMachine').html() + '</select>',
						'<input readonly="readonly" type="text" name="' + obj + '.workOrders[].waitTimeStr" class="form-control required" />',
						'<input onchange="changeAmountOrder(this);" type="text" name="' + obj
								+ '.workOrders[].amountStr" class="form-control" readonly="readonly"/>',
						'<input type="text" name="' + obj
							+ '.workOrders[].numOfFinishItemStr" readonly="true" class="form-control" />',
						'<input readonly="readonly" type="text" name="' + obj + '.workOrders[].totalEstTimeStr" class="form-control" /> ',
						'<input readonly="readonly" type="text" name="' + obj + '.workOrders[].totalTimeDelay" class="form-control" />'];

			}

			function saveRowExeStep() {

			}
			function changeAmount(amountStr) {
				var amount = $(amountStr).val().replaceAll(',', '');
				var exeTime = $('input[name="quotationItemExe.unitExeTimeStr"]').val().replaceAll(',', '');

				var estTime = parseFloat(amount) * parseFloat(exeTime);
				$('input[name="workOrder.totalEstTimeStr"]').val(estTime);
				initControl();
			}
			function cancel() {
				window.close();
			}

			function uploadItem() {
				var files = [];
				$('.file-modal_imp').each(function() {
					files.push($(this)[0].files);
				});
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
				$.loader({
					className : "blue-with-image-2"
				});

				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						alert('Thực hiện thành công!', function() {
							findData();
						});

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

			function changeAmountOrder(amount) {
				if ($(amount).val() === '')
					return;
				var amountVal = $(amount).val().replaceAll(",", "");
				var nameValue = $(amount).attr('name');
				var iLastIndexOf = nameValue.lastIndexOf('.');
				var nameValuePrefix = nameValue.substring(0, iLastIndexOf);

				var timeForOne = $('input[name="quotationItemExe.unitExeTimeStr"]').val().replaceAll(",", "");
				var totalEstTimeStr = parseFloat(amountVal) * parseFloat(timeForOne) / 60;
				var timeToDelay = $('input[name="' + nameValuePrefix + '.waitTimeStr"]').val().replaceAll(",", "");
				var totalTime = +totalEstTimeStr + +timeToDelay;

				$('input[name="' + nameValuePrefix + '.totalEstTimeStr"]').val(totalEstTimeStr).digits();
				$('input[name="' + nameValuePrefix + '.totalTimeDelay"]').val(totalTime).digits();
				//initControl();
			}

			$.fn.digits = function() {
				return this.each(function() {
					$(this).text($(this).text().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
				});
			}

			
			function changeMachine(machine) {
				if ($(machine).val().trim() == '')
					return;
				var machineIdx = getIndexByName($(machine).attr('name'));
				var qieId;
				var amountStr;
				if ($('#makeWoByQi').val() == 'true'){
					amountStr = $('input[name="quotationItem.workOrders[' + machineIdx + '].amountStr"]').val();
					qieId = $('input[name="quotationItem.workOrders['+ machineIdx +'].quotationItemExe.id"]').val();
				} 
				else {
					amountStr = $('input[name="quotationItemExe.workOrders[' + machineIdx + '].amountStr"]').val();
					qieId = $('#id').val();
				}
				var model = 'quotationItemExe.workOrders[' + machineIdx + ']';
				$.ajax({
					url : 'datlenhsx?method=getInfoMachine',
					data : {
						machineId : $(machine).val(),
						'quotationItemExe.id' : qieId,
						'amountStr' : amountStr
					},
					method : 'GET',
					success : function(res, status, xhr) {
						if ($('#makeWoByQi').val() == 'true'){
							$('input[name="quotationItem.workOrders[' + machineIdx + '].waitTimeStr"]').val(
									res.waitTime);
							$('input[name="quotationItem.workOrders[' + machineIdx + '].totalTimeDelay"]').val(
									res.totalTimeDelay);

						}else{
							$('input[name="quotationItemExe.workOrders[' + machineIdx + '].waitTimeStr"]').val(
									res.waitTime);
							$('input[name="quotationItemExe.workOrders[' + machineIdx + '].totalTimeDelay"]').val(
									res.totalTimeDelay);

						}
					}
				});
			}
			function makeWorkOrder(mWoMethod) {
				validobj.resetForm();
				if (mWoMethod == 'woDuration') {
					if (!$('#woDuration').valid())
						return;
				} else if (mWoMethod == 'numOfMachine') {
					if (!$('#numOfMachine').valid())
						return;
					var soLuongLsxDaThucHien = $('#table-exe').find('.rowSelect:disabled').length;
					var soLuongMay = $('#numOfMachine').val().convertStringToNumber();
					if (soLuongMay == 0) {
						alert('Số lượng máy cần >0');
						return;
					}
					if (soLuongMay < soLuongLsxDaThucHien) {
						alert('Số lượng LSX không được nhỏ hơn số lượng LSX đã thực hiện!');
						return;
					}
				} else if (mWoMethod == 'qiExtTime') {
					if (!$('#woTotalTime').valid())
						return;
				}
				$.ajax({
					url : 'datlenhsx?method=makeWorkOrder&mWoMethod=' + mWoMethod,
					data : $('#divDetail').find('input, select, textarea').not(
							'#table-exe input, #table-exe select, #table-exe textarea').serialize(),
					success : function(res, status, xhr) {
						var jsnResult = chkJson(res, xhr);
						if (jsnResult) {
							tableWorkOrder.resize(res.workOrders.length);
							$('#numOfMachine').val(res.lstProductionLine.length);
							makeTableProductionLine(res.lstProductionLine);
							binding(res, 'table-exe');
							initControl();
						} else {
							alert(res);
						}
					}
				});
			}
			function instanceValidate() {
				if (!$('#table-exe').find('input, select, textarea').valid()) {
					alert('Thực hiện không thành công, vui lòng kiểm tra thông tin nhập liệu!');
					return false;
				}
				return true;
			}
			function instanceSuccess(data, xhr) {
				alert(thuc_hien_thanh_cong, function() {
					if ($('#makeWoByQi').val() != 'true') {
						window.opener.refreshQie();
					}
				});
			}
			function delSuccess(){
				alert(succ_del,function(){
					if ($('#makeWoByQi').val() != 'true') {
						window.opener.refreshQie();
					}
					window.location.reload();
				});
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



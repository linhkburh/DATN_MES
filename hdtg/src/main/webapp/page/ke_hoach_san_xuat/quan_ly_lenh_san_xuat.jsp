<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.dataTables_filter {
	display: none;
}

#divDatatable tbody td, .subHeader {
	font-size: 11 !important;
	padding: 0.3rem !important;
}

#divDatatable thead th {
	padding: 0.3rem !important;
}
</style>

<div id="test-area-qr-code-webcam" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">
					<spring:message code="WorkOrder.scan" />
				</h4>
			</div>
			<div class="modal-body">
				<div class='Table'>
					<div class="Row">
						<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" id='abc' align="center">
							<video id="video" width="480" height="270"></video>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id='modalBtnClose' class="btn gray" data-dismiss="modal">
					<spring:message code="common.btn.close" />
				</button>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		<c:if test="${param.to == 'QCO'}">
        	 QC hàng gia công ngoài
    	</c:if>
		<c:if test="${param.to == null}">
			<spring:message code="WorkOrder.title" />
		</c:if>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/quanlyLSX" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<form:hidden path="to" id="to" />
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText">
						<spring:message code="common.info.search" />
					</div>
					<div class="row">
						<c:if test="${param.to == null}">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.code" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="workCode" id="workCode" />
							</div>
							<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12" style="padding-left: 0">
								<a href="javascript:;" class="fa fa-qrcode" id="qrInput"
									style="font-size: 25; font-weight: bold; margin-left: -10"></a>
							</div>
						</c:if>
						<c:if test="${param.to == 'QCO'}">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.code" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="workCode" id="workCode" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						</c:if>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="WorkOrder.code.manager" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="managerCode" id="managerCode" />
						</div>

					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="WorkOrder.code.draw" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="drawingCode" id="drawingCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-left: 0;">
							<spring:message code="WorkOrder.code.order" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orderCode" id="orderCode" />
						</div>
					</div>


					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="WorkOrder.creatdate.from" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="frDate" cssClass="date" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="WorkOrder.creatdate.to" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="toDate" cssClass="date" />
						</div>
					</div>
					<c:if test="${param.to != 'QCO'}">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><spring:message code="common.company.member" /></div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="companyId" onchange="updateLstUser()">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstCompany}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.updator" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="creator" cssClass="cboCompanyUser">
									<option value="">-
										<spring:message code="common.combobox.select" /> -
									</option>
									<c:if test="${lstSysUser!=null}">
										<c:forEach items="#{lstSysUser}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.username}" />-<c:out value="${item.name}" />
											</form:option>
										</c:forEach>
									</c:if>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>

						</div>
						<div class="row">

							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.status" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="workSts" id="workSts">
									<form:option value="">
										<spring:message code="WorkOrder.status.all" />
									</form:option>
									<form:option value="-1">
										<spring:message code="WorkOrder.status.notDone" />
									</form:option>
									<form:option value="0">
										<spring:message code="WorkOrder.status.progress" />
									</form:option>
									<form:option value="1">
										<spring:message code="WorkOrder.status.completed" />
									</form:option>
								</form:select>
							</div>

						</div>

					</c:if>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<a href="javascript:;" onclick="onOfAdvance()" style="font-size: 12; font-weight: bold;"> <font
								id="onAdvanceMode" style="display: none">-</font> <font id="offAdvanceMode" style="display: inline">+</font> <spring:message
									code="QuotationItem.advancedSearch" /></a>
						</div>
					</div>
					<div class="advanceSearch" style="display: none">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.name.cus" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="cusName" id="cusName">
									<form:option value="" label="- Chọn -" />
									<c:forEach items="#{customers}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.code}" />-<c:out value="${item.orgName}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.quotationitem.name" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="itemName" id="itemName" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						</div>
						<c:if test="${param.to == null}">
							<div class="row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
									<spring:message code="WorkOrder.code.machine" />
								</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<form:select path="machineCode">
										<form:option value="">- <spring:message code="common.combobox.select" /> -</form:option>
										<c:forEach items="#{lstAstMachine}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.astName}" />
											</form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>
						</c:if>

					</div>

					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();"
							value="&#xf002; <spring:message code="common.btn.search"/>" />
					</div>
					</div>
					<div class="HeaderText">
						<spring:message code="common.search.results" />
					</div>

				
				<c:set var="customDivDataTable" scope="request" value="true" />
				<div id="divDatatable" class="table-responsive">
					<table id="tblSearchResult" class="table">
						<thead>
							<tr>
								<th rowspan='2' width="2%"><spring:message code="WorkOrder.table.order" /></th>
								<th rowspan='2'><spring:message code="WorkOrder.table.code" /></th>
								<th rowspan='2'><spring:message code="WorkOrder.table.orderCode" /></th>
								<th rowspan='2'><spring:message code="WorkOrder.table.drawCode" /></th>
								<th rowspan='2'><spring:message code="WorkOrder.table.manager" /></th>
								<th rowspan='2'><spring:message code="WorkOrder.table.op" /></th>

								<c:choose>
									<c:when test="${param.to == 'QCO'}">
										<th colspan='4' style='text-align: center'><spring:message code="amount" /></th>
										<tr>
											<th class="subHeader"><spring:message code="from_machining_amount" /></th>
											<th class="subHeader"><spring:message code="have_checked" /></th>
											<th class="subHeader"><spring:message code="to_os_amount" /></th>
											<th class="subHeader"><spring:message code="remain" /></th>
										</tr>
									</c:when>
									<c:otherwise>

										<th colspan='2' style='text-align: center'><spring:message code="WorkOrder.table.plan" /></th>
										<th colspan='7' style='text-align: center'><spring:message code="WorkOrder.table.made" /></th>
										<th colspan='2' style='text-align: center'><spring:message code="WorkOrder.table.remaining" /></th>
										<tr>
											<!-- Ke hoach -->
											<th class="subHeader"><spring:message code="WorkOrder.table.plan.amount" /></th>
											<th class="subHeader"><spring:message code="WorkOrder.table.plan.time" /></th>
											<!-- Da thuc hien -->
											<th class="subHeader"><spring:message code="WorkOrder.table.made.from" /></th>
											<th class="subHeader"><spring:message code="WorkOrder.table.made.to" /></th>
											<th class="subHeader"><spring:message code="setup_time" />(<spring:message code="minute" />)</th>
											<th class="subHeader"><spring:message code="amount" /></th>
											<th class="subHeader"><spring:message code="WorkOrder.table.made.time" /></th>
											<th class="subHeader"><spring:message code="WorkOrder.table.slowHours" /></th>
											<th class="subHeader"><spring:message code="WorkOrder.table.slowPercent" /></th>
											<!-- Con lai -->
											<th class="subHeader"><spring:message code="WorkOrder.table.remaining.amount" /></th>
											<th class="subHeader"><spring:message code="WorkOrder.table.remaining.time" /></th>
										</tr>
									</c:otherwise>
								</c:choose>
							</tr>

						</thead>
					</table>
				</div>
				</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			Chi tiết
		</tiles:putAttribute>
	</form:form>

	<spring:url var="sendReportAction" value="/datlenhsx/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="module" src="<spring:url value="/js/zxing-browser.js" />"></script>
		<script type="module" src="<spring:url value="/js/video.js" />"></script>
		<script type="module">			
			import {initCamera} from '<spring:url value="/js/video.js" />';
			$('#qrInput').on('click', function(){ 
				initCamera('test-area-qr-code-webcam',callBack);
			})
			function callBack(qrCode){
				$('#test-area-qr-code-webcam').modal('hide');
				$.ajax({
					url:'workOderDetail?method=checkWoCode&code=' + qrCode,
					success:function(res){
						if(res=='true')
							window.location = 'workOderDetail?workOrder.code=' + qrCode;
						else{
							alert('Mã LSX '+qrCode+' không tồn tại!', function(){
								window.location.reload();
							});
						}
					}
					
				});
			}
			
		</script>
		<script type="text/javascript">
			var chon = '<spring:message code="common.combobox.select" />';
			function updateLstUser(){
				if($('#companyId').val().trim().length==0){
					$('select.cboCompanyUser').empty();
					$('select[name="machineCode"]').empty();
					return;
				}
				$.ajax({
					url:'quanlyLSX?method=loadResource',
					data:{
						"companyId":$('#companyId').val()
					},
					success:function(company, xhr, status){
						// Danh sach NSD
						var data = $.map(company.lstSysUsers, function (obj) {
		                      obj.text = obj.username + '-' +obj.name; 
		                      return obj;
		                    });
						$('select.cboCompanyUser').empty();
						$('select.cboCompanyUser').append('<option value="">'+chon+'</option>');
						$('select.cboCompanyUser').select2({data: data});
						// Danh sach may moc
						var data = $.map(company.lstAstMachine, function (obj) {
		                      obj.text = obj.astName; 
		                      return obj;
		                    });
						$('select[name="machineCode"]').empty();
						$('select[name="machineCode"]').append('<option value="">'+chon+'</option>');
						$('select[name="machineCode"]').select2({data: data});
					}
				});
			}
			function initParam(tblCfg) {
				if($('#to').val()=='QCO'){
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
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "right",//Số lượng chi tiết	
						"bSortable" : false
					}, {
						"sClass" : "right",//Số lượng chi tiết	
						"bSortable" : false
					}, {
						"sClass" : "right",//Số lượng chi tiết	
						"bSortable" : false
					}, {
						"sClass" : "right",//Số lượng chi tiết	
						"bSortable" : false
					}  ];
				}else{
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
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "left",
						"bSortable" : false
					}, {
						"sClass" : "right",//Số lượng chi tiết	
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
					}, {
						"sClass" : "right",//Thời gian setup (phút)
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
						"sClass" : "right",
						"bSortable" : false
					} ];
				}
				
				notSearchWhenStart = true;
				tblCfg.buttons = [ 
				/* {
					text : 'Import kết quả sản xuất',
					attr : {
						id : 'import'
					},
					className : 'mainGrid btnImp btn blue',
					action : function(e, dt, node, config) {
						uploadDetails();
					}
				},  */
				{
					text : '&#xf019; <spring:message code="WorkOrder.table.export.data"/>', 
					attr : {
						id : 'exportExcel'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						exportExcel();
					}
				}						
				];
			}
			scannerInput ="";
			lastClear=0;
			document.addEventListener("keypress", function(e) {			
		        clearTimeout(lastClear);
		        lastClear=window.setTimeout(function(){
		           scannerInput="";
		        },20);
		    	
		        console.log('e.keyCode: ' +e.keyCode);
		        if(e.keyCode!=13)
		        	scannerInput+=e.key;      
		        else{
			        console.log(scannerInput);
		        	handleQrcode(scannerInput);
		        }
			}); 
			function handleQrcode(qrCode){
				if( !$('#test-area-qr-code-webcam').is(':visible'))
					return;
				window.location='workOderDetail?workOrder.code=' + qrCode;
			}
			$('#test-area-qr-code-webcam').on('shown.bs.modal', function () {
				$('#modalBtnClose').focus();
			})
			$(document).ready(function() {
				$('.btnDtDelete').hide();
				var obj = "workOrder";
				var rowTempExeStep = initRowTableWorkOderExe(obj);
				tableOExeStep = new TFOnline.DataTable({
					id : 'table-exe',
					jQueryUI : true,
					rowTemp : rowTempExeStep,
					hasCheck : true,
					addOveride : true,
					delOveride : true,
					addButton : 'tableAllAst_add',
					delButton : 'tableAllAst_del',
					maxRow : 100
				});
			});

			function initRowTableWorkOderExe(obj) {
				return [
						'<div class="line-table"><input type="text" name="' + obj + '.workOrderExes[]." class="form-control" /></div>',
						'<div class="line-table"><select name="' + obj + '.workOrderExes[]." class="form-control" > '
								+ $('#selectAstMachine').html() + '</div>',
						'<div class="line-table"><input type="text" name="' + obj + '.workOrderExes[]." class="form-control" />  </div>',
						'<div class="line-table"><input type="text" name="' + obj + '.workOrderExes[]." class="form-control" />  </div>',
						'<div class="line-table"><input type="text" name="' + obj + '.workOrderExes[]." class="form-control" />  </div>',
						'<div class="line-table"><input type="text" name="' + obj + '.workOrderExes[]." class="form-control" />  </div>' ];
			}
			function addRowExeStep() {
				tableOExeStep.addRow();
				$('#table-exe select').select2();
				initControl();
			}
			function delRowExeStep() {
				tableOExeStep._deleteRow();
			}

			function saveRowExeStep() {

			}
			function changeAmount(amountStr) {
				var amount = $(amountStr).val().replaceAll(',', '');
				var exeTime = $('input[name="workOrder.quotationItemExe.unitExeTimeStr"]').val().replaceAll(',', '');

				var estTime = parseFloat(amount) * parseFloat(exeTime);
				$('input[name="workOrder.estTimeStr"]').val(estTime);
				initControl();
			}
			function cancel() {
				window.close();
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
			        url: 'quanlyLSX?method=exportExcel',
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
			            a.download = 'danh sach LSX.xlsx';
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
			function onOfAdvance(){
				$('#onAdvanceMode').css('display',$('#onAdvanceMode').css('display')=='none'?'inline':'none');
				$('#offAdvanceMode').css('display',$('#offAdvanceMode').css('display')=='none'?'inline':'none');
				if($('#onAdvanceMode').css('display')=='none'){
					disabledItems($('div.advanceSearch'), true);
					$('.advanceSearch').css('display','none')
				}else{
					disabledItems($('div.advanceSearch'), false);
					$('.advanceSearch').css('display','inline')
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



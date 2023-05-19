<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.dataTables_filter {
	display: none;
}

.error {
	color: red !important;
}

#divDatatable tbody td {
	font-size: 12 !important;
	padding: 0.2rem !important;
}

#divDatatable thead th {
	padding: 0.2rem !important;
}

#su label {
	font-size: 15px !important;
	font-weight: 500 !important;
	margin-left: 10px !important;
	margin-right: 10px !important;
}
</style>
<div id="modal_dialog_file" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">
					<spring:message code="upload_machining_result"></spring:message>
				</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST" enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;"><spring:message code="select_file"></spring:message></label> <input
									type="file" id="file-select_imp"
									accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
									class="file-modal_imp" name="inputFile" />
							</div>

						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="uploadItem();"><i class="fa fa-upload"></i>Upload</a> <a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>
				<spring:message code="close"></spring:message></a>
			</div>
		</div>
	</div>
</div>

<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		<spring:message code="HistoryPro.title" />
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/historyPro" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="more">
			<%@ include file="thay_doi_ket_qua_san_xuat.jsp"%>
		</tiles:putAttribute>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText">
						<spring:message code="common.info.search" />
					</div>


					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.workOrder.code" />
							
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="woCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.code.manager" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="manageCode" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.code.draw" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="drawingCode" id="drawingCode"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="op_type"></spring:message>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="typeWorkExe">
								<option value="">-
									<spring:message code="common.combobox.select" /> -
								</option>
								<form:option value="1">
									<spring:message code="fix_ng"></spring:message>
								</form:option>
								<form:option value="0">
									<spring:message code="machining"></spring:message>
								</form:option>
								<form:option value="-1">
									<spring:message code="to_qc"></spring:message>
								</form:option>
							</form:select>
						</div>

					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="start_time"></spring:message>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="frDate" id="frDate" class="dateTime" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.date.to" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="tDate" id="tDate" class="dateTime" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="common.company.member" />
						</div>
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
							<spring:message code="domain" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" id="su">
							<form:radiobutton path="source" value="-1" id="allRad" />
							<label for="allRad"><spring:message code="all" /></label>

							<form:radiobutton path="source" value="0" id="cncRad" />
							<label for="cncRad"><spring:message code="cnc" /></label>
							<form:radiobutton path="source" value="1" id="plasticRad" />
							<label for="plasticRad"><spring:message code="plastic" /></label>
						</div>
					</div>
					<div class="row">


						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.worker" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="worker" cssClass="cboCompanyUser">
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


						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="HistoryPro.updator" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="updatorSearch" cssClass="cboCompanyUser">
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


					</div>
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
								<spring:message code="HistoryPro.code.order" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="orderCode" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code='customer' />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="cusCode" id="keyword_code">
									<form:option value="" label="- Chọn -" />
									<c:forEach items="#{customers}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.code}" />-<c:out value="${item.orgName}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="HistoryPro.order.date.form" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="fromDate" id="fromDate" class="date"></form:input>
							</div>
							<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="HistoryPro.order.date.to" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="toDate" id="toDate" class="date"></form:input>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell">
								<spring:message code="common.shift" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12 cell">
								<form:select path="shift">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstShift}" var="item">
										<option value="${item.id}">
											<c:out value="${item.value}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
							<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell">
								<spring:message code="factory_unit" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12 cell">
								<form:select path="factoryUnit">
									<option value="">-
										<spring:message code="common.combobox.select" /> -
									</option>
									<c:forEach items="#{lstFactoryUnit}" var="item">
										<option value="${item.id}">
											<c:out value="${item.value}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="HistoryPro.machine" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="machineSearch" cssClass="machineCode">
									<option value="">-
										<spring:message code="common.combobox.select" /> -
									</option>
									<c:forEach items="#{lstAstMachine}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.astName}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><spring:message code="op_type" /></div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="stepName">
									<option value="">-
										<spring:message code="common.combobox.select" /> -
									</option>
									<c:forEach items="#{lstStepType}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.code}" /> - <c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</div>
				</div>
				<div class="divaction" align="center">
					<input class="btn blue fa" type="button" onclick="findData();"
						value="&#xf002; <spring:message code="common.btn.search"/>" />
				</div>
				<div class="HeaderText">
					<spring:message code="common.search.results" />
				</div>
				<c:set var="customDivDataTable" scope="request" value="true" />
				<div id="divDatatable" class="table-responsive">
					<table id="tblSearchResult" class="table">
						<thead>
							<tr>
								<th rowspan='2' width="2%"><spring:message code="HistoryPro.table.stt" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.workOrder.code" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.code.draw" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.code.manager" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.op" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.worker" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.from" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.to" /></th>
								<th rowspan='2'><spring:message code="setup_time" /> (<spring:message code="minute" />)</th>
								<th colspan='4' style='text-align: center'><spring:message code="HistoryPro.table.time" /></th>
								<th colspan='4' style='text-align: center'><spring:message code="HistoryPro.table.amount.detail" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.updator" /></th>
								<th rowspan='2'><spring:message code="HistoryPro.table.update" /></th>
							</tr>
							<tr>
								<!-- Thoi gian thuc hien -->
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.time.plan" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.time.real" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.slow.per" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.slow.minute" /></th>
								<!-- Da thuc hien -->
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.quantity" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.done" /></th>
								<th style="font-size: 12px !important;"><spring:message code="HistoryPro.table.fail" /></th>
								<th style="font-size: 12px !important;"><spring:message code="common.broken" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendReportAction" value="/historyPro/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
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
					$('select[name="machineCode"], select.machineCode').empty();
					$('select[name="machineCode"], select.machineCode').append('<option value="">'+chon+'</option>');
					$('select[name="machineCode"], select.machineCode').select2({data: data});
				}
			});
		}
			function initParam(tblCfg) {
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
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "right",//Thời gian setup
					"bSortable" : false
				}, {
					"sClass" : "right",//Thời điểm kết thúc
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
				tblCfg.buttons = [ {
					text : '&#xf019; Download template',
					attr : {
						id : 'download'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						download();
					}
				},{
					text : '&#xf093; <spring:message code="HistoryPro.table.import.data"/>',
					attr : {
						id : 'imports'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						uploadDetails();
					}
				},{
					text : '&#xf019; <spring:message code="HistoryPro.table.export.data"/>', 
					attr : {
						id : 'exportExcel'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						exportExcel();
					}
				} ];
			}
			function download() {
				window.open('historyPro?method=download').focus();
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
								findData();
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
			        url: 'historyPro?method=exportExcel',
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
			            a.download = 'lich su san xuat.xlsx';
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



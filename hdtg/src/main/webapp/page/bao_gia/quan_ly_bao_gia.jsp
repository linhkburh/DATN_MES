<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="hidden" value="${param.to}" id="to">
<div id="modal_dialog_file" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title"><spring:message code="import_po"/></h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form1" action="handler.php" method="POST" enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">File</label> <input type="file" id="file-select_imp"
									accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
									class="file-modal_imp" name="inputFile" />
							</div>

						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="uploadItem();"><i class="fa fa-upload"></i>Upload</a> <a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i><spring:message code="close"/></a>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title">
		<spring:message code="Quotation.title" />
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/quotation" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>

	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>

		<tiles:putAttribute name="catGrid">
			<div class="Table" id="divSearchInf">
				<%--Tam thoi bo qua tieu chi nay
				
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="companyId" id='companyId' onchange="reloadDepartment($(this).val())">
							<option value="">- Chọn -</option>
							<c:forEach items="#{lstCompany}" var="item">
								<form:option value="${item.id}">
									<c:out value="${item.name}" />
								</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<c:if test="${lstDepartment!=null}">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Phòng ban</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="departmentId" id='departmentId'>
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstDepartment}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</c:if>
				</div>
				
				--%>

				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="Quotation.code.order" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="code" id="code"></form:input>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><spring:message code='customer'/></div>
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
						<spring:message code="Quotation.order.from" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="fromDate" id="fromDate" class="date"></form:input>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="Quotation.order.to" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="toDate" id="toDate" class="date"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="Quotation.drawCode" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="drawingCode" id="drawingCode"></form:input>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="Quotation.manager" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="managerCode" id="managerCode"></form:input>
					</div>

				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<spring:message code="Quotation.status" />
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%--<form:select path="status" id="status" class="digits">
							<form:option value=""></form:option>
							<form:option value="0">Tạo mới</form:option>
							<form:option value="1">Chờ duyệt</form:option>
							<form:option value="2">Đã duyệt</form:option>
							<form:option value="3">Không duyệt</form:option>
							<form:option value="4">Đang sản xuất</form:option>
							<form:option value="5">Kết thúc</form:option>
						</form:select> --%>

						<form:select path="status" id="status">
							<form:option value="">
								<spring:message code="Quotation.status.all" />
							</form:option>
							<form:option value="-1">
								<spring:message code="Quotation.status.notDone" />
							</form:option>
							<form:option value="0">
								<spring:message code="Quotation.status.progress" />
							</form:option>
							<form:option value="1">
								<spring:message code="Quotation.status.completed" />
							</form:option>
						</form:select>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><spring:message code="common.company.member" /></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="companyId">
							<option value="">- Chọn -</option>
							<c:forEach items="#{lstCompany}" var="item">
								<form:option value="${item.id}">
									<c:out value="${item.name}" />
								</form:option>
							</c:forEach>
						</form:select>
					</div>
					<%--<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Báo giá thành công</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:checkbox path="done" id="done" value="1"></form:checkbox>
					</div> --%>

				</div>
				<div class="divaction" align="center">
					<input class="btn blue fa" type="button" onclick="findData();"
						value="&#xf002; <spring:message code="common.btn.search"/>" /> <input class="btn blue fa" type="button"
						onclick="window.location='<spring:url value='/quotation'><spring:param name="to" value="createNew" /></spring:url>'"
						value="&#xf067; <spring:message code = 'create_po'/>" />
				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="quotation.id" id="id" />
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã khách hàng<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="quotation.customer.code" id="customerCode" cssClass="required" onchange="selectCustomer(1)"></form:input>
					<form:hidden path="quotation.customer.id" id="customerId" />

				</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12" style="padding-left: 0; margin-left: -10;">
					<a href="#" onclick="selectCustomer(1)" class="pickUp" style="border: none"><i class="fa fa-search"></i></a>
				</div>

			</div>
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên khách hàng<font color="red">*</font>
				</div>
				<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
					<form:input path="quotation.customer.orgName" id="customerName" cssClass="required" onchange="selectCustomer(2)"></form:input>

				</div>
				<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" style="padding-left: 0; margin-left: -10;">
					<a href="#" onclick="selectCustomer(2)" class="pickUp" style="border: none"><i class="fa fa-search"></i></a>
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="padding-left: 0; margin-left: 10;">
					Ngày đơn hàng<font color="red">*</font>
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					<form:input path="quotation.quotationDateStr" class='date required' id="quotation_quotationDate"></form:input>
				</div>
			</div>

			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã đơn hàng<font color="red">*</font>
				</div>
				<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input cssStyle="text-transform: uppercase;" path="quotation.code" class='required'></form:input>

				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm giao hàng</div>
				<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="quotation.quotationEndDateStr" class='dateTime' id="quotation_quotationEndDate"></form:input>
				</div>

			</div>
			<div class="row">
				<c:if test="${param.to=='createNew'}">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Loại tiền<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select class="notnull required" path="quotation.currency.id" id="quotation_currency">
							<option value="">- Chọn -</option>
							<c:forEach items="#{listCurrency}" var="item">
								<form:option value="${item.id}">
									<c:out value="${item.value}" />
								</form:option>
							</c:forEach>
						</form:select>
					</div>
				</c:if>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Rate giờ máy(%)</div>
				<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="quotation.ratMachineStr" class='number' presicion='5' scale='2' maxVal='100' id="ratMachine"></form:input>

				</div>

			</div>
		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendReportAction" value="/quotation/upload"></spring:url>

	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function instanceSuccess(data, xhr){
				alert(thuc_hien_thanh_cong, function(){
					window.location ='item?quotationId=' + data.id;
				});
				
			}
			function initCatalogUi(){
				if($('#to').val()!='createNew')
					$('#divDetail').css('display', 'none');
			}
				
				
			function initParam(tblCfg) {
				if($('#to').val()=='createNew'){
					tblCfg.notSearchWhenStart = true;
					$('#divGrid').css('display','none');
					$('#divDetail').css('display','inline');
					return;
				}
					
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.stt"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.codeOrder"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.codeCus"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.nameCus"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.drawDone"/>'
				}, {
					"sClass" : "right",
					"bSortable" : false,
					"sTitle" : 'Số giờ dự kiến'
				}, {
					"sClass" : "right",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.orderValue"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.orderDate"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.deliveryDate"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.machiningTimeFrom"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.machiningTimeTo"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.runningTime"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.proStatus"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<spring:message code="Quotation.table.orderStatus"/>'
				} ];
				
				tblCfg.buttons = [{
					text : '&#xf093; <spring:message code="import_po"/>',
					attr : {
						id : 'import'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						uploadDetails();
					}
				}, {
					text : '&#xf019; Download template', 
					attr : {
						id : 'download'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						download();
					}
				}];
			}

			

			$(document).ready(function() {
				$('.btnDtDelete, #btnDel,.btnDtAdd').hide();
			});
			function cancel() {
			    window.history.back();
			}
			
			function reloadDepartment(conpanyId){
				$.ajax({
	                url: $('#theForm').attr('action') + "?method=reloadDepartment"
	                , success: function(res){
	                    var data = $.map(res, function (obj) {
	                      obj.text = obj.name; 
	                      return obj;
	                    });
	                    $('#departmentId').empty();
	                    $('#departmentId').append('<option value=""></option>');
	                    $('#departmentId').select2({data: data});
	                }
	                , data:{conpanyId:conpanyId}
	            });
			}
			function selectCustomer(p_type) {
				var cusCode,cusName;
				if(p_type==1){
					if($('#customerCode').val().trim().length == 0){
						window.open('customerMng?hiddenMenu=true&to=select', '', window_property).focus();
						return;
					}
					cusCode = $('input[name="quotation.customer.code"]').val().trim();
				}else{
					if($('#customerName').val().trim().length == 0){
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
					selectCus(res)
				}).fail(
						function() {
							if(p_type==1){
								window.open('customerMng?hiddenMenu=true&to=select&cuCode='+cusCode, '', window_property).focus();
							}else{
								window.open('customerMng?hiddenMenu=true&to=select&orgName='+cusName, '', window_property).focus();
							}
							// xoa id
							$('input[name="quotation.customer.code"]').val('');
							$('input[name="quotation.customer.orgName"]').val('');
							$('#customerId').val('');
						});
				
			}
			function selectCus(cus){
				// Cap nhat id, code
				$('input[name="quotation.customer.code"]').val(cus.code);
				$('input[name="quotation.customer.orgName"]').val(cus.orgName);
				$('#customerId').val(cus.id);
				if($('#to').val()=='createNew')
					$('input[name="quotation.code"]').val(cus.newOrderCode);
			}
			function setCustomer(customerId){
				$.ajax({
					url : 'quotation?method=getCustomerById',
					data : {
						customerId : customerId
					},
					method : 'GET',
					success : function(res) {
						selectCus(res)
					}
				});
			}
			
			function download() {
				window.open('quotation?method=download').focus();
			}
			
			function uploadDetails() {
				$('#file-select_imp').val('');
				$('#modal_dialog_file').modal();
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
								window.findData();
							});
						} else {
							alert(xhr.responseText)
						}

					} else {
						alert('Import không thành công ');

					}
				};
				xhr.send(formData);

			}
			
			function exportExcel(){
				$.ajax({
			        url: 'quotation?method=exportExcel',
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
			            	  if(data=="Không tồn tại dữ liệu xuất hàng!"){
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
			            a.download = 'danh sach don hang.xlsx';
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
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
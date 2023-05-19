<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.dataTables_filter {
	display: none;
}

div.text {
	padding-left: 0;
	color: darkblue;
	font-size: 12;
	vertical-align: top;
	font-weight: bold;
}

#divDatatable tbody td {
	font-size: 11;
	padding: 0.2rem !important;
}

#divDatatable thead th {
	padding: 0.2rem !important;
}
</style>
<tiles:insertDefinition name="catalog2">
	<tiles:putAttribute name="title">
		<spring:message code="QuotationItem.title" />
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/quanlyCT" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<form:hidden path="to"/>
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="HeaderText">
						<spring:message code="common.info.search" />
					</div>
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
							<spring:message code="customer" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="cusCode">
								<form:option value="" label="- Chọn -" />
								<c:forEach items="#{lstCus}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.code}" />-<c:out value="${item.orgName}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.draw" />
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="padding-right: 5">
							<form:input path="code" id="code"></form:input>
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
							<form:input path="manageCodeSearch" />
						</div>
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12 text" style="font-size:">
							<spring:message code="QuotationItem.search.info" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.code.order" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orderCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<spring:message code="QuotationItem.status" />
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="status" id="status">
								<form:option value="">
									<spring:message code="QuotationItem.status.all" />
								</form:option>
								<form:option value="-1">
									<spring:message code="QuotationItem.status.notDone" />
								</form:option>
								<form:option value="0">
									<spring:message code="QuotationItem.status.progress" />
								</form:option>
								<form:option value="1">
									<spring:message code="QuotationItem.status.completed" />
								</form:option>
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
								<spring:message code="QuotationItem.updator" />
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
							
							
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="QuotationItem.delivery.date.form" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="fromDate" id="fromDate" class="date"></form:input>
							</div>
							<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="QuotationItem.delivery.date.to" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="toDate" id="toDate" class="date"></form:input>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="QuotationItem.technical" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="technical" cssClass="cboCompanyUser">
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
								<spring:message code="QuotationItem.officers.ac" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="accountManager" cssClass="cboCompanyUser">
									<option value="">-
										<spring:message code="common.combobox.select" /> -
									</option>
									<c:if test="${lstAc!=null}">
										<c:forEach items="#{lstAc}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.username}" />-<c:out value="${item.name}" />
											</form:option>
										</c:forEach>
									</c:if>
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.creatdate.from" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="createDateForm" cssClass="date" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="WorkOrder.creatdate.to" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="createDateTo" cssClass="date" />
							</div>
						</div>
						<!-- 
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								<spring:message code="QuotationItem.repaired" />
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="repaired" />
							</div>
						</div>
						 -->

						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><spring:message code="have_foward_polishing"/></div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="toPolishing" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><spring:message code="have_foward_Qc"/></div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="toQc" />
							</div>
						</div>
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
								<th rowspan='2'><spring:message code="QuotationItem.table.stt" /></th>
								<th rowspan='2'><spring:message code="QuotationItem.table.code.draw" /></th>
								<th rowspan='2'><spring:message code="QuotationItem.table.name" /></th>
								<th rowspan='2'><spring:message code="QuotationItem.table.code.manager" /></th>
								<th rowspan='2'><spring:message code="QuotationItem.table.code.order" /></th>
								<th rowspan='2'><spring:message code="QuotationItem.table.code.cus" /></th>
								<th rowspan='2'><spring:message code="QuotationItem.table.delivery.date" /></th>
								<th colspan='3' style='text-align: center'><spring:message code="QuotationItem.table.plan" /></th>
								<th colspan='6' style='text-align: center'><spring:message code="QuotationItem.table.made" /></th>
								<th rowspan='2'><spring:message code="common.polishing" /></th>
								<th rowspan='2'><spring:message code="common.qc" /></th>
								<th colspan='2' style='text-align: center'><spring:message code="QuotationItem.table.remaining" /></th>
							</tr>
							<tr>
								<!-- Ke hoach -->
								<th style="font-size: 12"><spring:message code="QuotationItem.table.amount.detail" /></th>
								<th style="font-size: 12"><spring:message code="setup_time"/> (<spring:message code="minute"/>)</th>
								<th style="font-size: 12"><spring:message code="QuotationItem.table.time.taken" /></th>
								<!-- Da thuc hien -->
								<th style="font-size: 12"><spring:message code="QuotationItem.table.from" /></th>
								<th style="font-size: 12"><spring:message code="QuotationItem.table.to" /></th>
								<th style="font-size: 12"><spring:message code="setup_time"/> (<spring:message code="minute"/>)</th>
								<th style="font-size: 12"><spring:message code="amount"/></th>
								<th style="font-size: 12"><spring:message code="QuotationItem.table.run.actual" /></th>
								<th style="font-size: 12"><spring:message code="QuotationItem.table.slow.hours" /></th>
								<!-- Con lai -->
								<th style="font-size: 12"><spring:message code="QuotationItem.table.amount" /></th>
								<th style="font-size: 12"><spring:message code="QuotationItem.table.time" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</tiles:putAttribute>
		
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var chon = '<spring:message code="common.combobox.select" />';
			function updateLstUser(){
				if($('#companyId').val().trim().length==0){
					$('select.cboCompanyUser').empty();
					return;
				}
				$.ajax({
					url:'quanlyLSX?method=loadResource',
					data:{
						"companyId":$('#companyId').val()
					},
					success:function(company, xhr, status){
						var data = $.map(company.lstSysUsers, function (obj) {
		                      obj.text = obj.username + '-' +obj.name; 
		                      return obj;
		                    });
						$('select.cboCompanyUser').empty();
						$('select.cboCompanyUser').append('<option value="">'+chon+'</option>');
						$('select.cboCompanyUser').select2({data: data});
					}
				});
			}
			function initParam(tblCfg) {
				tblCfg.aoColumns = [ {
					"sClass" : "left",//stt
					"bSortable" : false
				}, {
					"sClass" : "left",//Mã bản vẽ
					"bSortable" : false
				}, {
					"sClass" : "left",//Tên chi tiết
					"bSortable" : false
				}, {
					"sClass" : "left",//Mã quản lý
					"bSortable" : false
				}, {
					"sClass" : "left",//Mã đơn hàng
					"bSortable" : false
				}, {
					"sClass" : "left",//Mã khách hàng
					"bSortable" : false
				}, {
					"sClass" : "left",//Ngày giao hàng
					"bSortable" : false
				}, {
					"sClass" : "right",//Số lượng chi tiết
					"bSortable" : false
				}, {
					"sClass" : "right",//Thời gian setup (giờ)
					"bSortable" : false
				}, {
					"sClass" : "right",//Thời gian thực hiện (giờ)
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",//Từ
					"bSortable" : false
				}, {
					"sClass" : "right",//Đến
					"bSortable" : false
				}, {
					"sClass" : "right",//Thời gian setup (phút)
					"bSortable" : false
				}, {
					"sClass" : "right",//Số lượng (Tổng/OK/NG)
					"bSortable" : false
				}, {
					"sClass" : "right",//Chậm (giờ)
					"bSortable" : false
				}, {
					"sClass" : "left",
					"bSortable" : false
				}, {
					"sClass" : "left",//Từ
					"bSortable" : false
				}, {
					"sClass" : "right",//Chậm (%)
					"bSortable" : false
				}, {
					"sClass" : "right",//Thời gian (giờ)
					"bSortable" : false
				} ];
				tblCfg.buttons = [{
					text : '&#xf019; <spring:message code="QuotationItem.table.export.data"/>', 
					attr : {
						id : 'exportExcel'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						exportExcel();
					}
				},{
					text : '&#xf02f; In QR code',
					attr : {
						id : 'printsList'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						exportPoQrCode();
					}
				}  ];

			}
			$(document).ready(function() {
				$('.btnDtAdd').hide();
			});
			function exportExcel(){
				$.ajax({
			        url: 'quanlyCT?method=exportExcel',
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
			            a.download = 'danh sach chi tiet.xlsx';
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
			
			function exportPoQrCode(){
				$.ajax({
			        url: 'quanlyCT?method=exportPoQrCode',
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
			            a.download = 'danh sach sua.pdf';
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

			function openQi(qiManagerCode){
				window.open('quanlyLSX?managerCode='+encodeURIComponent(qiManagerCode));
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



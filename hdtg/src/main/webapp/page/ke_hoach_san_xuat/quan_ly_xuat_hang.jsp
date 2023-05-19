<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<style>
.dataTables_filter {
	display: none;
}
</style>
<div id="modalPckDetail" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Chi tiết gói</h4>
			</div>
			<div class="modal-body ">
				<div class="table-responsive">
					<input type="hidden" name="idPackage">
					<table class="table" id="tblDLVPackageDetail">
						<thead style="background-color: #dfeffc; color: #2e6e9e">
							<tr>
								<th width="6%" align="left">STT</th>
								<th width="16%" align="left">Mã đơn hàng</th>
								<th width="16%" align="left">Mã bản vẽ</th>
								<th width="20%" align="left">Tên chi tiết</th>
								<th width="16%" align="left">Mã quản lý</th>
								<th width="16%" align="left">Ngày giao hàng</th>
								<th width="10%" align="right">Số lượng</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" onclick="delPackage()" class="btn red fa">Xóa</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="In QRcode xuất hàng" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/packageMng" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="packageCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã đơn hàng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="orderCode" />
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
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="managerCode" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="quotationItemCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Cán bộ đóng gói</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="creator">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstWorker}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.username}" />-<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>


					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày thực hiện, từ</div>
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
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="status">
								<form:option value="">Tất cả</form:option>
								<form:option value="0">Chưa in</form:option>
								<form:option value="1">Đã in</form:option>
							</form:select>
						</div>
					</div>
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
					"sTitle" : 'Khách hàng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã bản vẽ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã quản lý'
				}, {
					"sClass" : "right",
					"bSortable" : false,
					"sTitle" : 'Số lượng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Người đóng gói'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm đóng gói'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm in phiếu'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				}];
				tblCfg.buttons = [ {
					text : '&#xf02f; In QR code',
					attr : {
						id : 'printsList'
					},
					className : 'mainGrid btnImp btn blue fa',
					action : function(e, dt, node, config) {
						exportPoQrCode();
					}
				} ];
			}
			
			function exportPoQrCode(){
				$.ajax({
			        url: 'packageMng?method=exportPoQrCode',
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
			            a.download = 'danh sach hang xuat.pdf';
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
			
			function openDetail(pckId) {
				$('input[name="idPackage"]').val(pckId);
				$.getJSON($('#theForm').attr('action') + '?method=getDetail', {
					"tokenIdKey" : $('#tokenIdKey').val(),
					"tokenId" : $('#tokenId').val(),
					"pckId" : pckId
					}).done(function(res) {
						console.log(res);
						$('#tblDLVPackageDetail tbody').empty();
						for (var i=0; i < res.lstPackDetail.length; i++) {
							$('#tblDLVPackageDetail tbody').append('<tr>'+
									'<td style="text-align: left;">'+(i+1)+'</td>' +
									'<td style="text-align: left;">'+ res.lstPackDetail[i].quotationCode +'</td>' +
									'<td style="text-align: left;">'+ res.lstPackDetail[i].quotationItem.code +'</td>' +
									'<td style="text-align: left;">'+ res.lstPackDetail[i].quotationItem.name +'</td>' +
									'<td style="text-align: left;">'+ res.lstPackDetail[i].quotationItem.manageCode +'</td>' +
									'<td style="text-align: left;">'+ res.lstPackDetail[i].quotationItem.deliverDateStr +'</td>' +
									'<td style="text-align: right;">'+ res.lstPackDetail[i].amountStr +'</td>' +
							'</tr>');
						}
						$('#modalPckDetail').modal('show');
					});
			}
			function delPackage() {
				jConfirm('Bạn có chắn chắn muốn xóa gói này?', 'OK', 'Cancel', function(r) {
					if (r) {
						$.ajax({
							url : 'packageMng?method=delPackage',
							type : 'POST',
							async : false,
							data : {
								id : $('input[name="idPackage"]').val()
							},
							method : 'GET',
							success : function(data) {
								if (data === '') {
									alert(thuc_hien_thanh_cong, function() {
										$('#modalPckDetail').modal('hide');
										findData();
									});
								} else {
									alert(data);
								}

							}
						});
					}
				});

			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



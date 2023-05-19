<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<%
	ApplicationContext appContext = (ApplicationContext) request.getSession()
			.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
	SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	String companyId = user.getCompany().getId();
%>
<script type="text/javascript">
	function defaultValue() {
		$('select[name="astMachine.company.id"]').val('<%=companyId%>');
		$('select[name="astMachine.company.id"]').select2();
	}
</script>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh mục máy gia công" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/sysAstMachine" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="astName"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số giấy chứng nhận</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="astCertiNo"></form:input>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày tạo,Từ ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="fromDate" id="fromDate" class="date"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="toDate" id="toDate" class="date"></form:input>
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Người tạo</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="userAd" id="fromDate" class="date"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="companyId" id='scompanyId'>
								<option value="">- Chọn -</option>
								<c:forEach items="#{companies}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:checkbox path="isUsed" />
						</div>
					</div>
					<div class="divaction" align="center">
						<input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm" />
					</div>
					<!-- <div align="center" class="HeaderText">&#8203;</div> -->

				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="astMachine.id" id="id" />
			<div style="display: none">
				<select name="lstExeStepTypes" id="lstExeStepTypes">
					<option value=""></option>
					<c:forEach items="#{lstExeStepTypes}" var="item">
						<option value="${item.id}">
							<c:out value="${item.code}" /> -
							<c:out value="${item.name}" />
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên máy móc<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="astMachine.astName" cssClass="required" title="Tên không được để trống" />
				</div>
				<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Công ty<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:select path="astMachine.company.id" cssClass="required">
						<form:option value="">Hãy chọn</form:option>
						<c:forEach items="#{companies}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.name}" />
							</option>
						</c:forEach>
					</form:select>
				</div>
			</div>

			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã máy<font color="red">*</font></div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
					<form:input path="astMachine.code" cssClass="required" />
				</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nhãn hiệu</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
					<form:input path="astMachine.brand" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chủng loại</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
					<form:input path="astMachine.brandType" />
				</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nơi SX, lắp ráp</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
					<form:input path="astMachine.producePlace" />
				</div>
			</div>
			<div class="Row" style="background-color: #93d5f7; color: darkorange; font-size: 16px; font-weight: 600;">
				<div class="col-md-6 col-lg-6">Loại tài sản</div>
				<div class="col-md-6 col-lg-6">Đặc tính kỹ thuật</div>
			</div>
			<div class="Row">
				<div class="col-md-6 col-lg-6" style="border-right: 1px solid #95b8e7;">
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Loại, số, ngày cấp, nơi cấp GCN</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:textarea path="astMachine.certiNo" cssClass="" maxLength="2000"></form:textarea>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Trạng thái</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:select path="astMachine.astStatus">
								<option value="">- Chọn -</option>
								<c:forEach items="#{statusMachines}" var="item">
									<form:option value="${item.code}">
										<c:out value="${item.value}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Năm sản xuất</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input path="astMachine.prodYear" class="digits" maxlength="4"></form:input>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Mô tả khác</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:textarea path="astMachine.astDesc" maxLength="1000"></form:textarea>
						</div>
					</div>
				</div>
				<div class="col-md-6 col-lg-6">
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Biển số (nếu có)</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input maxLength="20" path="astMachine.licensePlate"></form:input>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Số khung</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input maxLength="100" path="astMachine.frameNo"></form:input>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Số máy</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input maxLength="100" path="astMachine.machineNo"></form:input>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Model</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input maxLength="100" path="astMachine.model"></form:input>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Tình trạng mua BH</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input maxLength="300" path="astMachine.insurStatus"></form:input>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Tình trạng đăng kiểm</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<form:input maxLength="300" path="astMachine.regStatus"></form:input>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-12">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Chức năng gia công</h1>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered" id="table-param-product">
						<thead>
							<tr>
								<th>Chức năng</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>

	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên máy'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Công ty'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số giấy chứng nhận	Số khung'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số máy'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số Khung'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Ngày tạo'
				} ];
			}
			$(document).ready(function() {
				$('.btnDtDelete').hide();
				initTableSerProduct()
			});
			function initTableSerProduct() {
				var rowTempServiceProduct = initRowTableSerProduct();
				var tableObject = new TFOnline.DataTable({
					id : 'table-param-product',
					jQueryUI : true,
					rowTemp : rowTempServiceProduct,
					hasCheck : true,
					delCaption : 'Xóa',
					addOveride : true,
					delOveride : true,
					maxRow : 100
				});
				tableSerProduct = tableObject;
				initControl();
			}
			function initRowTableSerProduct() {
				var rowTemp = [];
				var listObject = "astMachine";
				var strHidden = '<input type="hidden" class="form-control" name="' + listObject + '.exeStepTypes[].id" />';
				rowTemp = [ '<div class="line-table"><select class="form-control exeStepTypes" name="' + listObject + '.exeStepTypes[].exeStepType.id">'
						+ $('#lstExeStepTypes').html() + '</select></div>' + strHidden ];
				return rowTemp;
			}
			function afterEdit(id, res) {

				tableSerProduct.deleteAllRow();
				var _serverProducts = res.exeStepTypes;
				if (_serverProducts != null) {
					console.log(_serverProducts);
					for (var i = 0; i < _serverProducts.length; i++) {
						tableSerProduct.addRow();
						$('input[type="hidden"][name="astMachine.exeStepTypes[' + i + '].id"]').val(
								_serverProducts[i].id);
						if (_serverProducts[i].exeStepType != null)
							$('select[name="astMachine.exeStepTypes[' + i + '].exeStepType.id"]').val(
									_serverProducts[i].exeStepType.id);

						$('select[name="astMachine.exeStepTypes[' + i + '].exeStepType.id"]').select2();
					}
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



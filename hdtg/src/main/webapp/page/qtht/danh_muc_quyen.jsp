<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<tiles:insertDefinition name="simple_catalog">
	<tiles:putAttribute name="title" value="Danh sách quyền" />
	<%--<tiles:putAttribute name="detail_title" value="Chi tiết quyền" /> --%>
	<tiles:putAttribute name="formInf">
		<spring:url value="/right" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="sysObj.id" id="id" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysObj.objectId" cssClass="required"
						title="Mã không được để trống" />
				</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysObj.name" cssClass="required"
						title="Tên không được để trống" />
				</div>

			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Action<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysObj.action" cssClass="required"
						title="Action không được để trống" />
				</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Quyền
					cha</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<select class="form-control" name="sysObj.sysObjects.id"
						id="RightList">
						<option value="">- Chọn -</option>
						<c:forEach items="#{danhSachQuyen}" var="item">
							<option value="${item.id}">
								<c:out value="${item.objectId}" /> -
								<c:out value="${item.name}" />
							</option>
						</c:forEach>
					</select>
				</div>

				

			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			function initParam(tblCfg) {
				tblCfg.aoColumns = [ {
					"sWidth" : "5%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sWidth" : "10%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã'
				}, {
					"sWidth" : "20%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên'
				}, {
					"sWidth" : "28%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Hành động'
				}, {
					"sWidth" : "10%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Quyền cha'
				} ];
			}

			$(document).ready(function() {
				$('.btnDtDelete').hide();
			});

			function defaultValue() {
				$('#status').val(true);
				$('#statustrue').prop('checked', true);

			}
			function instanceValidate() {
				if ($('#id').val() === "") {
					createNew = true;
				}
				if ($('#RightList').val() == $('#id').val() && !createNew) {
					alert('Quyền cha không được trùng với quyền con');
					return false;
				}
				return true;
			}
		</script>
	</tiles:putAttribute>


</tiles:insertDefinition>
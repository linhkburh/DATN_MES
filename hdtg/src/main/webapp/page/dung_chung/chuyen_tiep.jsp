<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title" value="${formDataModelAttr.title}" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/index" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<form:hidden path="qrTitle" id="qrTitle"/>
			<form:hidden path="quotationItemProcess.type"/>
			<div class="Table">
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã quản lý<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItem.manageCode" readonly="true" />
					</div>
					<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" style="padding-left: 0; margin-left: -10;">
						<a href="javascript:;" class="fa fa-qrcode" id="qrInput" onclick="scan()" style="font-size: 25; font-weight: bold;"></a>
					</div>
					<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12" style="margin-left: 10;"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng<font color="red">*</font> </div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="quotationItemProcess.qualityStr" cssClass="textint required"/>
					</div>
				</div>
				<div align="center" class="HeaderText">&ZeroWidthSpace;</div>
				<div align="center" class="divaction">
		        	<input type="button" onclick="save()" value="&#xf0c7; Lưu" id="btnSave" class="btn blue fa"> 
		        </div>
			</div>
		</tiles:putAttribute>

	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script src="<spring:url value="/js/highcharts.js" />"></script>
		<script type="text/javascript">
			function retrieve(qrCode){
				$('input[name="quotationItem.manageCode"]').val(qrCode);
			}
			function scan(){
				window.open('qr?hiddenMenu=true&title='+$('#qrTitle').val(), '', 'width=1100, height=500, status=yes, scrollbars=yes').focus();
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
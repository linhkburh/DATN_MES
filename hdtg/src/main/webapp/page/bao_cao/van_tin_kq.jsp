<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<tiles:insertDefinition name="base">
<tiles:putAttribute name="title" value="Vấn tin kết quả" />
<tiles:putAttribute name="body">
	<spring:url value="/rp" var="formAction" />
	<c:set var="commandName" scope="request" value="formDataModelAttr" />
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		</br>
		<div class="Table">
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên tệp báo cáo</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="fileName" id="fileName"></form:input>
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"><input class="btn blue" type="button" onclick="queryRs();"
					value="Vấn tin" /></div>
			</div>
		
			<div class="row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Kết quả</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea path="result" id="result" rows="30" cssStyle="height:auto" ></form:textarea>
				</div>
			</div>
			
		</div>
	</form:form>
	<script type="text/javascript">
		function queryRs(){
			$('#result').val('');
			if (!$('#theForm').valid()) {
		        return;
		    }
			$.ajax({
				url : $('#theForm').attr('action') + '?method=queryRs',
				data : $('#theForm').serialize(),
				method : 'GET',
				success : function(_result, status, xhr) {
					$('#result').val(_result);
				}
			});
		}
	</script>
</tiles:putAttribute>

</tiles:insertDefinition>



<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<jsp:include page="/page/base/commonheader.jsp"/>


<style>
	.divaction{
		margin-bottom: 50px !important;
	}
</style>
<div id="main" class="container" style="background: #FFF;">
	<tiles:putAttribute name="formInf"/>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
	
   	<h1 class="title"><tiles:getAsString name="title"></tiles:getAsString></h1>
   	
	<div align="center" class="HeaderText">&ZeroWidthSpace;</div>
	<tiles:insertAttribute name="more"/>
	<tiles:insertAttribute name="body"/>
	<%@ include file="/page/include/data_table.jsp"%>
<div align="center" class="HeaderText"  style="font-size: 0 !important;">&ZeroWidthSpace;</div>
	<tiles:insertAttribute name="divaction"/>
	
	</form:form>    
</div>
<jsp:include page="/page/base/footer.jsp"/>
<tiles:insertAttribute name="extra-scripts"/>
<script type="text/javascript">
	


</script>
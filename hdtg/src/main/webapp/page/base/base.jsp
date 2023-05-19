<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<jsp:include page="/page/base/commonheader.jsp"/>


<div id="main" class="container" style="background: #FFF;">
	<tiles:putAttribute name="formInf"/>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
	
   	<h1 class="title"><tiles:getAsString name="title"></tiles:getAsString></h1>
   	
	<div align="center" class="HeaderText">&ZeroWidthSpace;</div>
	<tiles:insertAttribute name="body"/>
	</form:form>    
</div>
<c:if test='${param.hiddenMenu!="true"}'>
	<jsp:include page="/page/base/footer.jsp"/>
</c:if>
<tiles:insertAttribute name="extra-scripts"/>
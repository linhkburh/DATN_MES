<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ page import="entity.frwk.SysUsers" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext" %>
<%
       ApplicationContext appContext = (ApplicationContext)request.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
       SysUsers user = null;
       if(appContext!=null)
        user = (SysUsers)appContext.getAttribute(ApplicationContext.USER);
       WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request,null);
       SysParamDao sysParamDao = (SysParamDao) ac.getBean("sysParamDao");
       entity.frwk.SysParam LDAP_AUTHEN =  sysParamDao.getSysParamByCode("LDAP_AUTHEN");
%>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Loại từ điển"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/sysType" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="code" ></form:input>
		                    </div>
		                    <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="name" ></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="sysDictType.id" id="id"/> 
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysDictType.code" cssClass="required" title="mã không được để trống"/>
		            </div>
		            <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysDictType.name" cssClass="required" title="tên không được để trống"/>
		            </div>
		            
		        </div>
		        <div class="Row">
		        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysDictType.description" cssClass="required" title="mô tả không được để trống"/>
		            </div>
		        </div>
	</tiles:putAttribute>
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){	
			tblCfg.bFilter = false;
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'}
            ];
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
		});
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
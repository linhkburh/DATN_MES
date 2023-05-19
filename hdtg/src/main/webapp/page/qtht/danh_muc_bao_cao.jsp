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
	<tiles:putAttribute name="title" value="Danh mục báo cáo"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/rptype" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh mục báo cáo</h1>
							</div>
						</div>
						<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã biểu mẫu</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input id="templateCode" path = "templateCode"/>
		            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã báo cáo</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input id="khongquantam" path = "id"/>
	            </div>
	            
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại tệp</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select path = "fileType" >
	            		<option value="">- Chọn -</option>
						<c:forEach items="#{mapFileType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại dữ liệu</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select cols="5" path = "dataType" >
	            		<option value="">- Chọn -</option>
						<c:forEach items="#{mapDataType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	            
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại khác hàng</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select cols="3" path = "cusType">
	            		<option value="">- Chọn -</option>
						<c:forEach items="#{mapCusType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại báo cáo</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select path = "reportType">
	            		<option value="">- Chọn -</option>
						<c:forEach items="#{mapReportType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            	
	            	
	            	
	            </div>
	           
	        </div>
	        <div class="Row">
	        	 <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hoạt động</div>
	            	<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            		<form:checkbox path="active"/>
	            	</div>
	        </div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>	    
					</div>
				</div>
					<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<div class="box-custom">
			<div class="row title-page" style="adding-bottom: 20px;">
				<h1>Thông tin chi tiết</h1>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã biểu mẫu<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input id="id" path = "rpType.templateCode" disabled="true"/>
	            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã báo cáo<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:hidden path="rpType.id"/>
	            	<form:hidden path="rpType.tableName"/>
	            	<form:input id="id" path = "rpType.id" disabled="true" />
	            </div>
	            
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại tệp<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select path = "rpType.fileType"  disabled="true">
						<c:forEach items="#{mapFileType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại dữ liệu</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select path = "rpType.dataType" disabled="true">
						<c:forEach items="#{mapDataType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	            
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại khác hàng</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select cols="3" path = "rpType.cusType" disabled="true">
						<c:forEach items="#{mapCusType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại báo cáo</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select path = "rpType.reportType" disabled="true">
						<c:forEach items="#{mapReportType}" var="item">
							<option value="${item.key}">
								<c:out value="${item.key}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
	            	</form:select>
	            </div>
	            
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngừng hoạt động</div>
	            	<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            		<form:checkbox path="rpType.inActive"/>
	            	</div>
	        </div>
		</div>
	</tiles:putAttribute> 
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){		
			tblCfg.bFilter = true;
            tblCfg.bScrollX = true;
            tblCfg.aoColumns = [			 
                            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Mã biểu mẫu'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Mã báo cáo'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Loại tệp'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Loại dữ liệu'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Loại khách hàng'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Loại báo cáo'},
                            {"sClass": "left","bSortable" : false,"sTitle":'Trạng thái'}
            ];
		}
		
		$(document).ready(function() {
		     $('.btnDtDelete').hide();
		     $('.btnDtAdd').hide();
		     $('.btnSave').hide();
		     $('.btnDel').hide();
		});
		
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
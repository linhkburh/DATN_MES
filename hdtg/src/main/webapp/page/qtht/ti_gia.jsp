<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh mục tỉ giá" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/exchRate" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại tiền</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:select path="currency">
				            		<form:option value="">Hãy chọn</form:option>
				            			<c:forEach items="#{lstCurrency}" var="item">
											<option value="${item.id}">
												<c:out value="${item.value}" /> -
												<c:out value="${item.description}" />
											</option>
										</c:forEach>
				            	</form:select>
		                    </div>
		                    <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày tỉ giá</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="exchDate" ></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>
					</div>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="exchRate.id" id="id"/>
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:select path="exchRate.currency.id" cssClass="required">
		            		<form:option value="">Hãy chọn</form:option>
		            			<c:forEach items="#{lstCurrency}" var="item">
									<option value="${item.id}">
										<c:out value="${item.value}" /> -
										<c:out value="${item.description}" />
									</option>
								</c:forEach>
		            	</form:select>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày tỉ giá<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "exchRate.exchDateStr" cssClass="required date" title="Ngày tỉ giá không được để trống"/>
		            </div>
		        </div>
		        
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tỉ giá<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "exchRate.exchRateStr" cssClass="required number" precision="10" scale="2" title="Tỉ giá không được để trống"/>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nguồn tỉ giá<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "exchRate.exchSource"/>
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
		            {"sClass": "left","bSortable" : false,"sTitle":'Loại tiền'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Ngày tỉ giá'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tỉ giá'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Nguồn tỉ giá'}
	            ];
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

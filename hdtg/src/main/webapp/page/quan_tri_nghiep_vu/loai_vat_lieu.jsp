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
	<tiles:putAttribute name="title" value="Danh mục loại vật liệu" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/materialType" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id = "divSearchInf">
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã </div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="codeSearch" ></form:input>
		                    </div>
		                    <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên </div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="nameSearch" ></form:input>
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
		<form:hidden path="materialType.id" id="id"/>
			<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã loại vật liệu<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "materialType.code" cssClass="required" title="Mã không được để trống"/>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên loại vật liệu<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "materialType.name" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá gia công chi tiết khó (K)<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "materialType.initPriceKStr" cssClass="required number" precision='10' scale='2'></form:input>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá gia công chi tiết trung bình (TB)<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "materialType.initPriceTBStr" cssClass="required number" precision='10' scale='2'/>
		            </div>
		        </div>
		        
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá gia công chi tiết dễ (D)<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "materialType.initPriceDStr" cssClass="required number" precision='10' scale='2'/>
		            </div>
		        </div>
		        <div class="Row">
		        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
		            <div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
		            	<form:textarea path = "materialType.description" rows="4"></form:textarea>
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
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã loại vật liệu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên loại vật liệu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Đơn giá gia công chi tiết khó'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Đơn giá gia công chi tiết trung bình'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Đơn giá gia công chi tiết dễ'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'}
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			
			
			
			function defaultValue(){
				$('select[name="material.currency.id"]').prop('selectedIndex', 0);
				$('select[name="material.currency.id"]').select2()
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

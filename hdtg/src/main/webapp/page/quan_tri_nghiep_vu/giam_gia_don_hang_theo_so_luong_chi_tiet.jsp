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


<tiles:insertDefinition name="simple_catalog">
	<tiles:putAttribute name="title" value="Mức giảm giá gia công theo số lượng chi tiết" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/exeStep" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
					</div>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="reduceByAmount.id" id="id"/> 
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "reduceByAmount.code" cssClass="required" title="Mã không được để trống"/>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mức giảm giá<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "reduceByAmount.amountReduceStr" cssClass="required number"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng từ</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "reduceByAmount.amountFromStr" cssClass="number" />
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "reduceByAmount.amountToStr" cssClass="number" />
		            </div>
		        </div>
		        
		        <div class="Row">
		        <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
		            <div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
		            	<form:textarea rows="4" path = "reduceByAmount.description" />
		            </div>
		        
		        </div>
	</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){	
	            tblCfg.aoColumns = [			 
		            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Số lượng từ'},
		            {"sClass": "left","bSortable" : false,"sTitle":'đến'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mức giảm giá(%)'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			function defaultValue(){
				$('select[name="exeStep.currency.id"]').prop('selectedIndex', 0);
				$('select[name="exeStep.currency.id"]').select2()
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

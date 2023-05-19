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
<select class="form-control" id ="lstFreeType" style="display: none">
			<option value="">- Chọn -</option>
			<c:forEach items="#{lstFreeTypes}" var="item">
				<option value="${item.id}">
					<c:out value="${item.code}" /> -
					<c:out value="${item.name}" />
				</option>
			</c:forEach>
		</select>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh mục biểu phí"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/catCharge" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		
		
									
									
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh mục biểu phí</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã Tham số</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="scode" id="scode"></form:input>
		                    </div>
		                    <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên Tham số</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="sname" id="sname"></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="catCharge.id" id="id"/>
		<div class="box-custom">
			<div class="row title-page" style="adding-bottom: 20px;">
				<h1>Thông tin chi tiết biểu phí</h1>
			</div>	
			<div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "catCharge.code" cssClass="required" title="Mã không được để trống"/>
	            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "catCharge.name" cssClass="required" title="Tên không được để trống"/>
	            </div>
	        </div>
	        <div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
	            <div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
	            	<form:textarea cols="5" path = "catCharge.description"/>
	            </div>
	        </div>
	        <div class="Row">
	            <h1>Danh sách phí</h1>
	        </div>
	        <div class="table-responsive" >
					<table class="table table-bordered" id="table-charge">
						<thead>
							<tr>
								<th width="600px">Loại phí</th>
								<th width="400px">Đơn giá</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				
	        
		</div>
	</tiles:putAttribute> 
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){		
			tblCfg.bFilter = false;
            tblCfg.bScrollX = true;
            tblCfg.aoColumns = [			 
                    {"sClass": "left","bSortable" : false,"sTitle":'STT'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'}
                            
            ];
		}
		
		$(document).ready(function() {
		     tableOExeStep = new TFOnline.DataTable({
					id : 'table-charge',
					jQueryUI : true,
					rowTemp : [
						'<select class="exeStepType notnull" name="catCharge.lstCharge[].freeType.id">'+ $('#lstFreeType').html()+ '</select>',
						'<input type="text" name="catCharge.lstCharge[].valueStr" class="form-control number"/>'],
					maxRow : 100
				});
		});
		
		function beforeEdit(res){
			var iTemp = res.lstCharge.length - tableOExeStep.toTalRow();
			if(iTemp>0){
				// Bo sung mot so dong
				for(var i = 0; i< iTemp; i++)
					tableOExeStep.addRowWithoutReIndex();
				tableOExeStep.reIndex();
			}else{
				// Xoa 1 so dong
				iTemp=Math.abs(iTemp);
				var totalRow = tableOExeStep.toTalRow();
				for(var i = 0; i< iTemp; i++)
					tableOExeStep.deleteRow(totalRow-(i+1));
			}
		}
		
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
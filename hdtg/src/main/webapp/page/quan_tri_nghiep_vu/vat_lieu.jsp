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
	<tiles:putAttribute name="title" >
		<spring:message code="material.title"/>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/material" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div class="Table" id = "divSearchInf">
					<div class="Row">
	                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
	                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	                    	<form:input path="code" ></form:input>
	                    </div>
	                    <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
	                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
	                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	                       <form:input path="name" ></form:input>
	                    </div>
					</div>
					<div align="center">
			            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
			        </div>
				</div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="material.id" id="id"/>
		<div class="Table">
			<div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã vật liệu<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "material.code" cssClass="required" title="Mã không được để trống"/>
	            </div>
	            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên vật liệu<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "material.name" cssClass="required" title="Tên không được để trống"/>
	            </div>
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
	            <div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
	            	<form:textarea path = "material.description" rows="4"></form:textarea>
	            </div>    
	        
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại vật liệu<font color="red">*</font></div>
	            <div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
	            	<form:select cssClass="required" path="material.materialType.id">
	            		<form:option value="">Hãy chọn</form:option>
	            			<c:forEach items="#{materialTypes}" var="item">
								<option value="${item.id}">
									<c:out value="${item.code}" /> -
									<c:out value="${item.name}" />
								</option>
							</c:forEach>
	            	</form:select>
	            </div>    
	        
	        </div>
	        <div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khối lượng riêng (kg/m<sup>3</sup>)<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "material.densityStr" cssClass="required number" title="mô tả không được để trống"/>
	            </div> 
	            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "material.initPriceStr" cssClass="required number"  title="Tên không được để trống"/>
	            </div>    
	        </div>
	        <div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị tính<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:select cssClass="required" path="material.unit.id">
	            		<form:option value="">Hãy chọn</form:option>
	            			<c:forEach items="#{units}" var="item">
								<option value="${item.id}">
									<c:out value="${item.code}" /> -
									<c:out value="${item.value}" />
								</option>
							</c:forEach>
	            	</form:select>
	            </div> 
	            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại tiền</div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12"><form:select path="material.currency.id">
			            			<c:forEach items="#{lstCurrency}" var="item">
										<option value="${item.id}">
											<c:out value="${item.value}" /> -
											<c:out value="${item.description}" />
										</option>
									</c:forEach>
			            	</form:select></div>
	            
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
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã vật liệu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên vật liệu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Loại vật liệu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Khối lượng riêng'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Đơn giá'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Đơn vị tính'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'}
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			
			function exportExcel() {
				window.location.href = "material/export";
			}
			
			function defaultValue(){
				$('select[name="material.currency.id"]').prop('selectedIndex', 0);
				$('select[name="material.currency.id"]').select2()
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

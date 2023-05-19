<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="simple_catalog">
	<tiles:putAttribute name="title" value="Phân xưởng sản xuất" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/factoryUnit" var="formAction" />
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
		<form:hidden path="bssFactoryUnit.id" id="id"/> 
		<div class="box-custom">
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã xưởng<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="bssFactoryUnit.code"
						cssClass="required uppercase ascii" title="Mã không được để trống" />
				</div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên Xưởng<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="bssFactoryUnit.name" cssClass="required"
						title="Tên không được để trống" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Công ty</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:select path="bssFactoryUnit.company.id" id="companyId">
		            		<form:option value="">Hãy chọn</form:option>
		            			<c:forEach items="#{companies}" var="item">
									<option value="${item.id}">
										<c:out value="${item.code}" /> -
										<c:out value="${item.name}" />
									</option>
								</c:forEach>
		            	</form:select>
				</div>
			</div>

			

		</div>
	</tiles:putAttribute>
	</form:form>
	<spring:url var="sendXmlCfgFile" value="/userDoc/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){	
	            tblCfg.aoColumns = [			 				
		            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Công ty'},
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			
			

		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

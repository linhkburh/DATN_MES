<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh mục phòng ban" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/company" var="formAction" />
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
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="companyId" id = 'scompanyId' onchange="reloadDepartment($(this).val())">
									<option value="">- Chọn -</option>
									<c:forEach items="#{companies}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
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
		<form:hidden path="department.id" id="id"/>
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã phòng ban<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "department.code" cssClass="required" title="Mã không được để trống"/>
		            </div><div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên phòng ban<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "department.name" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên viết tắt<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "department.sortName" cssClass="required" title="Mã không được để trống"/>
		            </div><div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty<font color="red">*</font></div>
			            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			            	<%-- <form:input path = "exeStep.code" cssClass="required" title="Mã không được để trống"/> --%>
			            	<form:select path="department.company.id">
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
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "department.email" cssClass="required" title="Mã không được để trống"/>
		            </div><div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số điện thoại<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "department.phoneNumber" cssClass="required" title="Tên không được để trống"/>
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
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã phòng ban'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên phòng ban'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên viết tắt'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Địa chỉ'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Sô điện thoại'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên công ty'}
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

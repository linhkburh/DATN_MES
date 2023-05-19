<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh mục công đoạn gia công" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/exeStep" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id = "divSearchInf">
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hình thức gia công</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:select path="stepTypeId">
				            		<form:option value="">Hãy chọn</form:option>
				            			<c:forEach items="#{stepType}" var="item">
											<option value="${item.id}">
												<c:out value="${item.name}" />
											</option>
										</c:forEach>
				            	</form:select>
		                    </div>
		                    <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="code" ></form:input>
		                    </div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="name" ></form:input>
		                    </div>
		                    <div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công đoạn lập trình</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:checkbox path="isProgram" ></form:checkbox>
		                    </div>
						</div>
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Phí cố định</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:checkbox path="fixFree" ></form:checkbox>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>	    
			        	
					</div>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="exeStep.id" id="id"/> 
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "exeStep.stepCode" cssClass="required" title="Mã không được để trống"/>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "exeStep.stepName" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hình thức gia công<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:select path="exeStep.stepType.id" cssClass="required" >
		            		<form:option value="">Hãy chọn</form:option>
		            			<c:forEach items="#{types}" var="item">
									<option value="${item.id}">
										<c:out value="${item.code}" /> -
										<c:out value="${item.name}" />
									</option>
								</c:forEach>
		            	</form:select>
		            </div>
		            <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá/giờ<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "exeStep.initPriceStr" cssClass="number required"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại tiền</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12"><form:select path="exeStep.currency.id">
				            			<c:forEach items="#{lstCurrency}" var="item">
											<option value="${item.id}">
												<c:out value="${item.value}" /> -
												<c:out value="${item.description}" />
											</option>
										</c:forEach>
				            	</form:select></div>
				    <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lập trình</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:checkbox path="exeStep.program"/>
				    </div>
		        </div>
		        <div class="Row">
		        <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
		            <div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
		            	<form:textarea rows="4" path = "exeStep.description" title="Tên không được để trống"/>
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
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Hình thức gia công'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Đơn giá/giờ'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Lập trình'}
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
	
	

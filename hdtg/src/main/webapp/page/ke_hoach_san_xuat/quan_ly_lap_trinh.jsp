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
<style>
	.dataTables_filter {
		display: none;
	}
</style>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Quản lý lập trình" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/lenhsx" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id="divSearchInf">
					<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã đơn hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="orderCode" id="orderCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="cusName" id="cusName"/>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="drCode" id="drCode"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div><div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hình thức gia công</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="stepType" >
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstStepType}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.code}" /> - <c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lập trình viên</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="programer" >
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUsers}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian từ ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="frDate" id="frDate" class="date"/>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="toDate" id="toDate" class="date"/>
							</div>
						</div>
						<div class="row">
							
							<%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã công đoạn</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="opCode" id="opCode"/>
							</div> --%>
						</div>
							
						<div class="divaction" align="center">
				            <input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm"/>
				        </div>
					</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="workPro.id" id="id"/>
		<form:hidden path="workPro.quotationItemExe.id" id="qiExeId"/>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã LSX</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "workPro.code" readonly="true"  title="Mã không được để trống"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên khách hàng</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input readonly="true"   path = "workPro.customerName" />
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input readonly="true"   path = "workPro.drawingCode" cssClass="" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hình thức gia công</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input readonly="true"  path = "workPro.quotationItemExe.exeStepId.stepType.name" />
		            </div>
		        </div>
		        
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian dự kiến</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "workPro.estimateTime" cssClass="number" readonly="true"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lập trình viên<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:select path="workPro.programer.id" cssClass="required">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstSysUsers}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
		            </div>
		        </div>
		         <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm bắt đầu</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "workPro.startTimeStr" cssClass="dateTime" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm kết thúc</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "workPro.endTimeStr" cssClass="dateTime" />
		            </div>
		        </div>
	</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){	
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã lệnh lập trình'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã đơn hàng'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã bản vẽ'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên khách hàng'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Hình thức gia công'}, 
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời gian kế hoạch (phút)'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời điểm bắt đầu'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời điểm kết thúc'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời gian thực hiện (phút)'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Lập trình viên'},
            ];
            /* tblCfg. */notSearchWhenStart = true;
		}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			     $('.btnDtAdd').hide();
			});
			
			function beforeLoad() {
				var url = window.location.href;
				var tmp = url.split('?');
				if (tmp.length > 1) {
					$('#divDetail').css('display','block');
					$('#divGrid').css('display','none');
				} else {
					$('#divDetail').css('display','none');
					$('#divGrid').css('display','block');
					
				}
			}
			
			function cancel() {
				var url = window.location.href;
				var tmp = url.split('?');
				if (tmp.length > 1) {
					window.close();
				} else {
					$('#divGrid').css('display', 'inline');
				    $('#divDetail').css('display', 'none');
				}
				
			}
			
			function instanceSuccess() {
				alert(thuc_hien_thanh_cong, function () {    
					window.close();
					window.opener.refreshQie();
                });
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

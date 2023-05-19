<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<c:choose>
		<c:when test="${to == 'select'}">
			<jsp:include page="/page/base/commontop.jsp"/>
			
		</c:when>
		<c:otherwise>
			<jsp:include page="/page/base/commonheader.jsp"/>
		</c:otherwise>
	</c:choose>
	

<%-- <title><tiles:getAsString name="title"></tiles:getAsString></title> --%>
<script type="text/javascript">
function initCatalogUi(){
	$('#divDetail').css('display', 'none');
}
</script>
<style>
	#divGrid, div.box-custom, #divDetail{
		margin-top: 5px !important;
	}
</style>
<div id="main" class="container" style="padding-bottom: 25px;">
	<tiles:putAttribute name="formInf"/>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
	
	<form:hidden path="tokenId" id="tokenId"></form:hidden> 
    <form:hidden path="tokenIdKey" id="tokenIdKey"></form:hidden>
    
    <c:if test="${approveForm}">
   		<input type="hidden" name="formTypeApprove" value="true" id = "formTypeApproveId"> 
   	</c:if>
   	<h1 class="title"><tiles:getAsString name="title"></tiles:getAsString></h1>
   	<div id="divGrid">
   		<tiles:insertAttribute name="more"/>    	
		<tiles:insertAttribute name="catGrid"/>    	
	    <%@ include file="/page/include/data_table.jsp"%>
    </div>
    
    <div id="divDetail">
    	<div align="left" class="HeaderText">Chi tiết</div>
    	<!-- <div class="HeaderText">Thông tin chi tiết</div>   -->
    	
    	<tiles:insertAttribute name="catDetail"/>
    	<c:choose>
			<c:when test="${to == 'select'}">
				<div align="center" class="divaction">
					<input type="button" onclick="cancel()" value="Bỏ qua" id="btlCancel" class="btn gray">		
					<input type="button" onclick="selectItem()" value="Chọn" id="btnSelect" class="btn gray">
				</div>
			</c:when>
			<c:otherwise>
				<div align="center" class="HeaderText"  style="font-size: 0 !important;">&ZeroWidthSpace;</div>
				<div align="center" class="divaction">
		        	<c:if test="${save}">
		        		<input type="button" onclick="save()" value="Lưu" id="btnSave" class="btn blue"> 
		        	</c:if>
		        	<c:if test="${del}">
		        		<input type="button" onclick="del()" value="Xóa" id="btnDel" class="btn red"> 
		        	</c:if>
		        	<c:if test="${toApprove}">
		        		<input type="button" onclick="toApprove()" value="Chuyển phê duyệt" id="btnToApprove" class="btn blue"> 
		        		<input type="button" onclick="unToApprove()" value="Chỉnh sửa" id="btnUnToApprove" class="btn red"> 
		        	</c:if>
		        	<c:if test="${approve}">
		        		<input type="button" onclick="approve()" value="Duyệt" id="btnApprove" class="btn blue"> 
		        		<input type="button" onclick="unApprove()" value="Từ chối" id="btnUnApprove" class="btn red"> 
		        		<input type="button" onclick="cancelApprove()" value="Hủy duyệt" id="btnCancelApprove" class="btn red"> 
		        	</c:if>
		            <input type="button" onclick="$('#btnNext,#btnPre').css('display','none');cancel()" value="Bỏ qua" id="btlCancel" class="btn gray">		
		            <input type="button" onclick="previos()" value="Previous" id="btnPre" style="display:none" class="btn blue"> 	 	
		            <input type="button" onclick="next()" value="Next" id="btnNext" style="display:none" class="btn blue"> 
		        	
		        </div>
			</c:otherwise>
		</c:choose>
    	
        
    </div>
    
	</form:form>
</div>
<c:if test='${param.hiddenMenu!="true"}'>
	<jsp:include page="/page/base/footer.jsp"/>
</c:if>

<tiles:insertAttribute name="extra-scripts"/>

<script type="text/javascript">
	$(document).ready(function(){		
		if(typeof tblCfg !== 'undefined'){
			if(tblCfg.approveInMain){
				$('.divaction #btnToApprove, .divaction #btnUnToApprove, .divaction #btnApprove, .divaction #btnUnApprove, .divaction #btnCancelApprove').remove();
			}else{
				$('.mainGrid#btnToApprove, .mainGrid#btnUnToApprove, .mainGrid#btnApprove, .mainGrid#btnUnApprove, .mainGrid#btnCancelApprove').remove();
			}
		}
		
		// An hien theo trang thai
		if($('#status').length==1){
			// Tao moi hoac khong duyet
			if($('#status').val()==undefined || $('#status').val()=='' || $('#status').val()==0 || $('#status').val()==3){
	    		$('#btnUnToApprove, #btnApprove, #btnUnApprove, #btnCancelApprove').remove();
	        }else if($('#status').val()==1){// Cho duyet
	        	$('#btnDtAdd, #btnSave, #btnDel, #btnToApprove, #btnCancelApprove,#import').remove();
	    	}else if($('#status').val()==2){// Da duyet
	    		$('#btnDtAdd, #btnSave, #btnDel, #btnToApprove,#btnUnToApprove, #btnApprove, #btnUnApprove,#import').remove();
	    	}else if($('#status').val() > 2){ // Da hieu luc
	    		$('#btnDtAdd, #btnSave, #btnDel, #btnToApprove,#btnUnToApprove, #btnApprove, #btnUnApprove, #btnCancelApprove,#import').remove();
	    	}
		}
		if (typeof beforeLoad == 'function') {
       	 beforeLoad();
       	 return;
        }
		initCatalogUi();
		
	}); 
	
	
</script>

<c:if test="${!add}">
<script type="text/javascript">
	$(document).ready(function(){
		$(':button.btnDtAdd, #import').remove();
	}); 
	
</script>
</c:if>


<c:if test="${!toApprove}">
<script type="text/javascript">
	$(document).ready(function(){
		$('#btnToApprove,#btnUnToApprove').remove();
	}); 
</script>
</c:if>


<c:if test="${!approve}">
<script type="text/javascript">
	$(document).ready(function(){
		$('#btnApprove,#btnUnApprove, #btnCancelApprove').remove();
	}); 
</script>
</c:if>
        	
        	
        	

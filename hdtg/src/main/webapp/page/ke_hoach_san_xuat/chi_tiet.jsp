<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title" value="Chi tiết bản vẽ" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/quanlyCT" var="formAction" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="formDataModelAttr"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<div id="divDetail">
				<form:hidden path="quotation.id" id="quotationId" />
				<%@ include file="/page/ke_hoach_san_xuat/chi_tiet_chi_tiet.jsp"%>
				<%@ include file="/page/dung_chung/upload_file_ftp.jsp"%>
			</div>
			<div align="center" class="HeaderText" style="font-size: 0 !important;">&ZeroWidthSpace;</div>
			<div align="center" class="divaction">
				<c:if test="${save}">
					<input type="button" onclick="save()" value="&#xf0c7; Lưu" id="btnSave" class="btn blue fa">
				</c:if>
				<c:if test="${del}">
					<input type="button" onclick="del()" value="&#xf2d3; Xóa" id="btnDel" class="btn red fa">
				</c:if>
				<input type="button" onclick="window.close()" value="&#xf112; Bỏ qua" id="btlCancel"
					class="btn gray fa">
			</div>
		</tiles:putAttribute>

	</form:form>

	<tiles:putAttribute name="extra-scripts">
		<script src="<spring:url value="/page/bao_gia/chi_tiet_bao_gia.js?v=3" />"></script>
		<script type="text/javascript">
		function save(saveData) {

		    // Truoc khi validate    
		    if (typeof beforeValidate == 'function')
		        beforeValidate();
		    
		    // customize validate
		    if (typeof instanceValidate == 'function') {
		        if (!instanceValidate())
		            return;
		    }else{
		    	if (!$('#theForm').valid()) {
		    		alert('Thực hiện không thành công, vui lòng kiểm tra thông tin nhập liệu!');
		            return;
		        }
		    }

		    // Before save
		    if (typeof beforeSave == 'function')
		        beforeSave();
		    $('input[type="checkbox"]').each(function () {
		        $(this).val($(this).prop('checked'));
		    });
		    $('input[type="radio"]').each(function () {
		        try {
		            if ($(this).prop('checked')) {
		                var arrName = $(this).attr('name').split('.');
		                var realId = arrName[arrName.length - 1];
		                $(this).val($(this).attr('id').substring(realId.length));
		            }
		        }
		        catch (err) {
		        }

		    });
		    $.loader( {
		        className : "blue-with-image-2"
		    });
		    $.ajax( {
		        method : 'POST', async : false, type : "POST"
		        , url : saveUrl
		        //, data : saveData?saveData: $('#theForm').serialize()
		        , data : $('#theForm').serialize()
		        , success : function (data, status, xhr) {
		            $.loader("close");
		            var jsnResult = chkJson(data, xhr);
		            // Khong tra ve json, truong hop tra ve loi hoac khong giu nguyen man hinh
		            if (!jsnResult) {
		                if (data.trim() != '') {
		                    alert(data, function(){
				            	window.location.reload();
		                    });
		                }
		            }else{
		            	alert(thuc_hien_thanh_cong, function(){
			            	window.location.reload();
		            	});
		            }

		        },
		        error : function (data, xhr) {
		            $.loader("close");
		            var result = chkJson(data);
		            if (typeof instanceSaveFalse == 'function') {
		                instanceSaveFalse(!result ? data : result);
		                return;
		            }
		            alert(data);
		        }
		    });

		}
		// Override kg lam gi ca
		function beforeSave(){
			
		}	
		function delSuccess(){
			alert(thuc_hien_thanh_cong, function(){
				window.close();
				window.opener.findData();
			})
		}
		</script>

	</tiles:putAttribute>
</tiles:insertDefinition>
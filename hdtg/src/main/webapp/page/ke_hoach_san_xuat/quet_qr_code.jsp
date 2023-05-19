<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title">
		<c:if test="${param.process == 'CL' || param.process == 'QC' || param.process == 'RP'}">
        	 Scan mã quản lý
    	</c:if>
    	<c:if test="${param.process == null}">
    		Scan mã lệnh sản xuất - Gia công
    	</c:if>
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/index" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
		<form:hidden path="process" id="process" />
		<form:hidden path="type" id="type" />
			<div class='Table' id="test-area-qr-code-webcam">
				<div class="Row">
					<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" id='abc' align="center">
						<video id="video" width="480" height="270"></video>
					</div>
				</div>
				
				<c:if test="${param.process == 'CL' || param.process == 'QC'}">
					<c:if test="${param.type == 'to'}">
						<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý<font color="red">*</font></div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="qrManageCode" id="qrManageCode" readonly="true" class="required"/>
						</div>
						<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng<font color="red">*</font></div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="qrAmount" id="qrAmount" class="number required"/>
						</div>
					</div>
					</c:if>
				</c:if>
				<div align="right">
				<c:if test="${param.process == 'CL' || param.process == 'QC'}">
					<c:if test="${param.type == 'to'}">
						<input type="button" value="Lưu" onclick="saveDataQR($('#process').val())" id="btSave" class="btn blue fa">
					</c:if>
				</c:if>
					<input type="button" value="&#xf029; Quét" onclick="window.location.reload()" id="btnScan" class="btn blue fa">
					<input type="button" value="&#xf04d; Tạm dừng" id="btnStop" class="btn gray fa">	
				</div>
			</div>
		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="module" src="<spring:url value="/js/zxing-browser.js" />"></script>
		<script type="module" src="<spring:url value="/js/video.js" />"></script>
		<script type="module">
			function callBack(qrCode){
				if ($('#type').val() == 'action') {
					window.location='processExeQR?type=' +$('#process').val()+ '&manageCode=' + qrCode;
				}else if($('#process').val() == 'RP'){ 
					window.location='quotationRepaireExe?manageCode=' + qrCode;
					}
					else {
					$.ajax({
						url:'workOderDetail?method=checkWoCode',
						data:{
							process: $('#process').val(),
							code: qrCode,		
						},
						success:function(res){
							if(res=='true')
								if ($('#process').val() == 'CL' || $('#process').val() == 'QC')
									$('input[name="qrManageCode"]').val(qrCode);
		      		  			else
									window.location='workOderDetail?workOrder.code=' + qrCode;
							else{
								if($('#process').val() == 'CL' || $('#process').val() == 'QC'){
									alert('Mã quản lý '+qrCode+' không tồn tại!', function(){
									window.location.reload();
									});
								}else{
									alert('Mã LSX '+qrCode+' không tồn tại!', function(){
									window.location.reload();
									});
								}		
							}
						}	
					});
				}
				
			}
			function initCamera(callBack) {
				const codeReader = new ZXingBrowser.BrowserQRCodeReader();
				const previewElem = document.querySelector('#abc > video');
				const controls = codeReader.decodeFromVideoDevice(undefined, previewElem, (result, error, controls) => {
					if(typeof result !== typeof undefined){
						callBack(result.text);
					}
					
					$('#btnStop').one('click', function () {
						controls.stop();
					});
				});
			}
			$(document).ready(function(){
				initCamera(callBack);
			});


		</script>
		<script type="text/javascript">
		$(document).ready(function(){
			// Thiet bi cam tay
			scannerInput ="";
		    lastClear=0;
		    document.addEventListener("keypress", function(e) {
		        console.log(e.key);
		        clearTimeout(lastClear);
		        lastClear=window.setTimeout(function(){
		           scannerInput="";
		        },100);
		        if(e.keyCode!=13)
		        	scannerInput+=e.key;      
		        else{
		        	if ($('#process').val() == 'CL' || $('#process').val() == 'QC')
							$('input[name="qrManageCode"]').val(scannerInput);
		        	else
		        		window.location='workOderDetail?workOrder.code=' + scannerInput;
		        }
		        e.preventDefault();
		   }); 
		});
		function saveDataQR(process){
			$.ajax({
				url : 'workOderDetail?method=addWorkOrderDetail&process=' + process,
				data : {
					qrManageCode : $('input[name="qrManageCode"]').val(),
					qrAmount : $('input[name="qrAmount"]').val(),
				},
				success : function(data, xhr, status) {
					if(data==''){
						alert(thuc_hien_thanh_cong);
					}else{
						alert(data);
					}
				}
			});
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title">
		${formDataModelAttr.title}
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/qr" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<form:hidden path="to"/>
			<div class='Table' id="test-area-qr-code-webcam">
				<div class="Row">
					<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" id='abc' align="center">
						<video id="video" width="480" height="270"></video>
					</div>
				</div>
				<div align="right">
					<input type="button" value="&#xf029; Quét" onclick="window.location.reload()" id="btnScan" class="btn blue fa">
					<input type="button" value="&#xf04d; Tạm dừng" id="btnStop" class="btn gray fa">

				</div>
			</div>
		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="module" src="<spring:url value="/js/zxing-browser.js" />"></script>
		<script type="module">
			import {redirect} from '<spring:url value="/page/dung_chung/xu_ly_qr.js" />';
			function callBack(qrCode){
				if($('#to').val().length!=0){
					redirect($('#to').val(),qrCode);
					return;
				}else if(opener!=undefined && typeof(opener.retrieve)=='function'){
					opener.retrieve(qrCode);
					window.close();
					return;
				}else{
					alert('Bổ sung implement');
				}
			}
			$(document).ready(function(){
				initCamera(callBack);
				initHid(callBack);
			});
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
			function initHid() {
				// Thiet bi cam tay
				var scannerInput = "";
				var lastClear = 0;
				document.addEventListener("keypress", function(e) {
					console.log(e.key);
					clearTimeout(lastClear);
					lastClear = window.setTimeout(function() {
						scannerInput = "";
					}, 100);
					if (e.keyCode != 13)
						scannerInput += e.key;
					else {
						callBack(scannerInput);
					}
					e.preventDefault();
				});
			}

		</script>
		
	</tiles:putAttribute>
</tiles:insertDefinition>
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
		<spring:url value="/photo" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<form:hidden path="to"/>
			<div class='Table' id="test-area-qr-code-webcam">
				<div class="Row">
					<!-- <div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" id='abc' align="center">
						<video id="video" width="480" height="270"></video>
					</div> -->
					<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" align="center">
				         <video id="video" ></video>
				         <canvas id="canvas" style="display: none;"></canvas>
				         <img id="photo" style="display: none;" src="#" alt="Ảnh chụp">
			         </div>
			          
				</div>
				<div align="right">
					<input type="button" style="display: none;" value="&#xf0e2; Chụp lại" onclick="window.location.reload()" id="btnScan" class="btn blue fa">
					<input id="startbutton" type="button" value="&#xf030; Chụp ảnh" class="btn gray fa">
					<input type="button" style="display: none;" value="&#xf0c7; Lưu" id="btnSave" class="btn gray fa">

				</div>
			</div>
		</tiles:putAttribute>


	</form:form>
	<spring:url var="sendPhotoCap" value="/userDoc/sendPhotoCap"></spring:url>
	<spring:url var="sendReportAction" value="/item/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<%-- <script type="module" src="<spring:url value="/js/zxing-browser.js" />"></script>
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

		<script type="text/javascript" src="/webjars/adapterjs/0.15.0/adapter.min.js"></script>
      <script type="text/javascript" src="/webjars/webrtc-adapter/7.6.2/adapter.min.js"></script>
      <script type="text/javascript">
         var video = null;
         var canvas = null;
         var photo = null;
         var startbutton = null;
         
         function startup() {
            video = document.getElementById('video');
            canvas = document.getElementById('canvas');
            photo = document.getElementById('photo');
            startbutton = document.getElementById('startbutton');
         
            navigator.mediaDevices.getUserMedia({
               video: true,
               audio: false
            }).then(function(stream) {
               video.srcObject = stream;
               video.play();
            }).catch(function(err) {
               console.log("An error occurred: " + err);
            });
         
            startbutton.addEventListener('click', function(ev){
               takepicture();
               ev.preventDefault();
            }, false);
         
            clearphoto();
         }
         
         function clearphoto() {
            var context = canvas.getContext('2d');
            context.fillStyle = "#AAA";
            context.fillRect(0, 0, canvas.width, canvas.height);
         
            var data = canvas.toDataURL('image/png');
            photo.setAttribute('src', data);
         }
         
         function takepicture() {
             var context = canvas.getContext('2d');
             if (video.paused || video.ended) {
                return;
             }
             canvas.width = video.videoWidth;
             canvas.height = video.videoHeight;
             context.drawImage(video, 0, 0, canvas.width, canvas.height);
          
             var data = canvas.toDataURL('image/png');
             photo.setAttribute('src', data);
             $('#video').css('display','none');
             $('#startbutton').css('display','none');
             $('#photo').css('display','flex');
             $('#btnScan').css('display','flex');
             $('#btnSave').css('display','flex');
             btnSave = document.getElementById('btnSave');
             btnSave.addEventListener('click', function(ev){
             	 sendPhoto(data);
              }, false);
          }

          function sendPhoto(data) {
         	var formData = new FormData();
 			formData.append("photoData", data);
 			formData.append("id", $('#id').val());
             var xhr = new XMLHttpRequest();
             xhr.open('POST', '${sendPhotoCap}', true);
             xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
             xhr.onreadystatechange = function() {
                if(xhr.readyState === 4 && xhr.status === 200) {
             	   alert('Thực hiện thành công!', function() {
 						
 					});
                }
             }
             xhr.send(formData);
          }
          
          window.addEventListener('load', startup, false);
      </script>
	</tiles:putAttribute>
</tiles:insertDefinition>
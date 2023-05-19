<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title">
		Detect Object On web
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/detect" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
		<div class="Row">
			<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" id="container" align="center">    
            	<canvas class="center-block" id="canvasOutput" style="max-width: 100%;" width="640" height="480"></canvas>
	        </div>
	        <div class="invisible">
	            <video id="video" class="hidden" width="480" height="360">Your browser does not support the video tag.</video>
	        </div>
		</div>
		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script src="<spring:url value="/js/jquery.min.js" />"></script>
		<script>  
        function opencvIsReady() {
            console.log('OpenCV.js is ready');
            startCamera();
        }

        var Module = {
            wasmBinaryFile: 'https://huningxin.github.io/opencv.js/build/wasm/opencv_js.wasm',
            preRun: [function() {
                //Module.FS_createPreloadedFile('/', 'haarcascade_frontalface_default.xml', 'https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml', true, false); 
            	Module.FS_createPreloadedFile('/', 'haarcascade_frontalface_default.xml', 'https://raw.githubusercontent.com/linhkburh/HaarCascade/main/XMLFile/Phone_Cascade.xml', true, false);
            }],
            _main: function() {opencvIsReady();}
        };
        let videoWidth, videoHeight;

        // whether streaming video from the camera.
        let streaming = false;

        let video = document.getElementById('video');
        let canvasOutput = document.getElementById('canvasOutput');
        let canvasOutputCtx = canvasOutput.getContext('2d');
        let stream = null;

        function startCamera() 
        {
            if (streaming) return;
            navigator.mediaDevices.getUserMedia({video: true, audio: false})
                .then(function(s) {
                    stream = s;
                    video.srcObject = s;
                    video.play();
                })
                .catch(function(err) {
                console.log("An error occured! " + err);
            });

            video.addEventListener("canplay", function(ev)
            {
                if (!streaming) 
                {                
                    videoWidth = 480;
                    videoHeight = 360;
                    video.setAttribute("width", videoWidth);
                    video.setAttribute("height", videoHeight);
                    canvasOutput.width = videoWidth;
                    canvasOutput.height = videoHeight;
                    streaming = true;
                }
                startVideoProcessing();
            }, false);
        }

        let faceClassifier = null;

        let src = null;

        let canvasInput = null;
        let canvasInputCtx = null;

        let canvasBuffer = null;
        let canvasBufferCtx = null;

        function startVideoProcessing() 
        {
            if (!streaming) { console.warn("Please startup your webcam"); return; }
            stopVideoProcessing();
            canvasInput = document.createElement('canvas');
            canvasInput.width = videoWidth;
            canvasInput.height = videoHeight;
            canvasInputCtx = canvasInput.getContext('2d');
            
            canvasBuffer = document.createElement('canvas');
            canvasBuffer.width = videoWidth;
            canvasBuffer.height = videoHeight;
            canvasBufferCtx = canvasBuffer.getContext('2d');
            
            srcMat = new cv.Mat(videoHeight, videoWidth, cv.CV_8UC4);
            grayMat = new cv.Mat(videoHeight, videoWidth, cv.CV_8UC1);
            
            faceClassifier = new cv.CascadeClassifier();
            faceClassifier.load('haarcascade_frontalface_default.xml');
            
            requestAnimationFrame(processVideo);
        }

        var numFrameAppearFace = 0;

        function processVideo() 
        {
            canvasInputCtx.drawImage(video, 0, 0, videoWidth, videoHeight);
            let imageData = canvasInputCtx.getImageData(0, 0, videoWidth, videoHeight);
            srcMat.data.set(imageData.data);
            cv.cvtColor(srcMat, grayMat, cv.COLOR_RGBA2GRAY);
            let faces = [];
            let size;

            
            let faceVect = new cv.RectVector();
            let faceMat = new cv.Mat();

            cv.pyrDown(grayMat, faceMat);
            cv.pyrDown(faceMat, faceMat);
            size = faceMat.size();    

            faceClassifier.detectMultiScale(faceMat, faceVect);
            for (let i = 0; i < faceVect.size(); i++) {
                let face = faceVect.get(i);
                faces.push(new cv.Rect(face.x, face.y, face.width, face.height));
            }
            faceMat.delete();
            faceVect.delete();        
            
            canvasOutputCtx.drawImage(canvasInput, 0, 0, videoWidth, videoHeight);
            drawResults(canvasOutputCtx, faces, 'green', size);
            timeout = 50

            requestAnimationFrame(processVideo);
        }
        let countedObjects = [];
        function drawResults(ctx, results, color, size) {
        	ctx.font = "20px Arial";
	       	ctx.fillStyle = 'red';
	       	let objectsCount = 0; // Biến đếm số lượng vật thể
	       	const yThreshold = 360 / 2;
	        // Vẽ ranh giới ngang
	        ctx.beginPath();
	        ctx.moveTo(0, yThreshold);
	        ctx.lineTo(480, yThreshold);
	        ctx.lineWidth = 2;
	        ctx.strokeStyle = 'blue';
	        ctx.stroke();
            for (let i = 0; i < results.length; ++i) {
                let rect = results[i];
                let xRatio = videoWidth/size.width;
                let yRatio = videoHeight/size.height;
                ctx.lineWidth = 2;
                ctx.strokeStyle = color;
                ctx.strokeRect(rect.x*xRatio, rect.y*yRatio, rect.width*xRatio, rect.height*yRatio);
             // Kiểm tra nếu vật thể đi qua ranh giới ngang
                if (rect.y * yRatio > yThreshold) {
                  // Kiểm tra xem vật thể đã được đếm qua chưa
                  const objectKey = rect.x + '_' + rect.y;
                  if (!countedObjects.includes(objectKey)) {
                    countedObjects.push(objectKey); // Thêm vật thể vào danh sách đã đếm
                    objectsCount++; // Tăng số lượng vật thể mới lên 1
                  }
                }
            }
            ctx.fillText("Objects: " + objectsCount, 10, 30);
        }

        function stopVideoProcessing() {
            if (src != null && !src.isDeleted()) src.delete();           
        }

        function stopCamera() {
            if (!streaming) return;
            stopVideoProcessing();
            document.getElementById("canvasOutput").getContext("2d").clearRect(0, 0, videoWidth, videoHeight);
            video.pause();
            video.srcObject=null;
            stream.getVideoTracks()[0].stop();
            streaming = false;
        }        

    </script>
	<script src="<spring:url value="/js/opencv.js" />"></script>
	</tiles:putAttribute>
</tiles:insertDefinition>
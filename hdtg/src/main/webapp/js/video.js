//const codeReader = new ZXingBrowser.BrowserQRCodeReader();
//	
//const videoInputDevices = await ZXingBrowser.BrowserCodeReader.listVideoInputDevices();
//
//// choose your media device (webcam, frontal camera, back camera, etc.)
//const selectedDeviceId = videoInputDevices[0].deviceId;
//
//console.log(`Started decode from camera with id ${selectedDeviceId}`);
//
//const previewElem = document.querySelector('#abc > video');
//
//// you can use the controls to stop() the scan or switchTorch() if available
//const controls = await codeReader.decodeFromVideoDevice(selectedDeviceId, previewElem, (result, error, controls) => {
//	$('#test-area-qr-code-webcam').modal('show');
//	if(typeof result !== typeof undefined){
//		document.querySelector('#content').innerText = result.text;
//		$('#test-area-qr-code-webcam').modal('hide');
//	}
//});
//// stops scanning after 20 seconds
//setTimeout(() => controls.stop(), 20000);
//export function getControls() {
//	return controls;
//}

export function initCamera(modalCameraId, callBack) {
	$('#' + modalCameraId).modal('show');
	const codeReader = new ZXingBrowser.BrowserQRCodeReader();
	// const videoInputDevices = ZXingBrowser.BrowserCodeReader.listVideoInputDevices();
	// choose your media device (webcam, frontal camera, back camera, etc.)
	//const selectedDeviceId = videoInputDevices[0].deviceId;
	//console.log(`Started decode from camera with id ${selectedDeviceId}`);
	const previewElem = document.querySelector('#abc > video');
	// you can use the controls to stop() the scan or switchTorch() if available
	const controls = codeReader.decodeFromVideoDevice(undefined, previewElem, (result, error, controls) => {
	// use the result and error values to choose your actions
	// you can also use controls API in this scope like the controls
	// returned from the method.
		//alert(result);
		//console.log(result);
//		$("#test-area-qr-code-webcam").unbind("hide.bs.modal").bind("hide.bs.modal", function () {
//			controls.stop();
//		});
		$('#' + modalCameraId).one('hide.bs.modal', function () {
			controls.stop();
		})
		if(typeof result !== typeof undefined){
			controls.stop();
			callBack(result.text);
		}
		
	});
	
	return controls;
	}

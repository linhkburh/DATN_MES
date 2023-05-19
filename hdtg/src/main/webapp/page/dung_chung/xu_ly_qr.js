export function redirect(to, qr_code) {
	if(to=='CL')
		window.location='processExeQR?type=CL&manageCode='+qr_code;
	else if(to=='QC')
		window.location='processExeQR?type=QC&manageCode='+qr_code;
	else if(to=='RP')
		window.location='quotationRepaireExe?manageCode='+qr_code;
}
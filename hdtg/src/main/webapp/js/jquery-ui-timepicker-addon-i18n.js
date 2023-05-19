(function($) {
	$.datepicker.regional["vi"] =
	{
		closeText: "Đóng",
		prevText: "Trước",
		nextText: "Sau",
		currentText: "Hôm Nay",
		monthNames: ["Tháng Giêng", "Tháng Hai", "Tháng Ba", "Tháng Tư", "Tháng Năm", "Tháng Sáu", "Tháng Bảy", "Tháng Tám", "Tháng Chín", "Tháng Mười", "Tháng Mười Một", "Tháng Mười Hai"],
		monthNamesShort: ["Một", "Hai", "Ba", "Bốn", "Năm", "Sáu", "Bảy", "Tám", "Chín", "Mười", "Mười Một", "Mười Hai"],
		dayNames: ["Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Bốn", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"],
		dayNamesShort: ["CN", "Hai", "Ba", "Tư", "Năm", "Sáu", "Bảy"],
		dayNamesMin: ["CN", "T2", "T3", "T4", "T5", "T6", "T7"],
		weekHeader: "Tuần",
		dateFormat: "dd/mm/yy",
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ""
	};

	$.datepicker.setDefaults($.datepicker.regional["vi"]);
	$.timepicker.regional['vi'] = {
		timeOnlyTitle : 'Chọn giờ',
		timeText : 'Thời gian',
		hourText : 'Giờ',
		minuteText : 'Phút',
		secondText : 'Giây',
		millisecText : 'Mili giây',
		microsecText : 'Micrô giây',
		timezoneText : 'Múi giờ',
		currentText : 'Hiện thời',
		closeText : 'Đóng',
		timeFormat : 'HH:mm',
		timeSuffix : '',
		amNames : [ 'SA', 'S' ],
		pmNames : [ 'CH', 'C' ],
		isRTL : false
	};
	$.timepicker.setDefaults($.timepicker.regional['vi']);
})(jQuery);
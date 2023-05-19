/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: VI (Vietnamese; Tiếng Việt)
 */
(function($) {
	$.validator.addMethod("date", function(value, element) {
		var bits = value.match(/([0-9]+)/gi), str;
		if (!bits)
			return this.optional(element) || false;
		str = bits[1] + '/' + bits[0] + '/' + bits[2];
		return this.optional(element) || !/Invalid|NaN/.test(new Date(str));
	}, "Nhập ngày tháng theo định dạng dd/mm/yyyy");
	var messagesNumberClsErorr = $.validator.messages.number;
	$.validator
			.addMethod(
					"number",
					function(value, element) {
						if ($(element).val().length == 0)
							return true;
						var numTemp = $(element).val().convertStringToNumber();
						if (numTemp == undefined || numTemp.toString() === 'NaN') {
							return false;
						}
						var mV = $(element).attr("maxVal");
						if (mV != undefined && mV.trim().length > 0) {
							if ($(element).val().convertStringToNumber() <= $(element).attr("maxVal")
									.convertStringToNumber())
								return true;
							$.validator.messages.number = "Gi\u00E1 tr\u1ECB t\u1ED1i nh\u1EADp t\u1ED1i \u0111a "
									+ formatSo(mV);
							return false;
						}
						// scale
						var scaleAttr = $(element).attr("scale");
						// precision
						var precisionAttr = $(element).attr("precision");
						if (scaleAttr != undefined && precisionAttr != undefined) {
							var scale = scaleAttr.trim().length == 0 ? 0 : parseInt(scaleAttr);
							var precision = precisionAttr.trim().length == 0 ? 0 : parseInt(precisionAttr);
							var smaxValue = "".padStart(precision - scale, "9") + "." + "".padStart(scale, "9");
							if (smaxValue.convertStringToNumber() >= $(element).val().convertStringToNumber()) {
								$.validator.messages.number = messagesNumberClsErorr;
								return true;
							} else {
								$.validator.messages.number = "Gi\u00E1 tr\u1ECB nh\u1EADp kh\u00F4ng \u0111\u01B0\u1EE3c l\u1EDBn h\u01A1n "
										+ formatSo(smaxValue, scale);
								return false;
							}
						}
						return true;
					}, "Hãy nhập số.");

	$.validator
			.addMethod(
					"addressIp",
					function(value, element) {
						if ($(element).val().length == 0) {
							$.validator.messages.addressIp = "\u0110\u1ECBa ch\u1EC9 IP kh\u00F4ng \u0111\u01B0\u1EE3c \u0111\u1EC3 tr\u1ED1ng";
							return false;
						}
						if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
								.test(value)) {
							return true;
						}
						$.validator.messages.addressIp = "\u0110\u1ECBa ch\u1EC9 IP kh\u00F4ng \u0111\u00FAng \u0111\u1ECBnh d\u1EA1ng";
						return false;
					}, "H\u00E3y nh\u1EADp s\u1ED1");

	$.validator.addMethod("notnull", function(value, element) {
		if ($(element).val() == null || $(element).val().length == 0) {
			$.validator.messages.notnull = "Kh\u00F4ng \u0111\u1EC3 tr\u1ED1ng";
			return false;
		}
		return true;
	});

	$.validator.addMethod("cmt", function(value, element) {
		if ($(element).val().length == 0) {
			/*
			 * $.validator.messages.cmt = "CMT kh\u00F4ng \u0111\u01B0\u1EE3c
			 * \u0111\u1EC3 tr\u1ED1ng";
			 */
			/* return false; */
			return true;
		}
		var regex = new RegExp("^([0-9]{9}$|[0-9]{12})$");
		if (regex.test(value)) {
			return true;
		} else {
			$.validator.messages.cmt = "CMT kh\u00F4ng \u0111\u00FAng \u0111\u1ECBnh d\u1EA1ng";
			return false;
		}
	})

	$.validator.addMethod("textint", function(value, element) {
		return isNumber(element, 'i');
	}, "H\u00E3y nh\u1EADp s\u1ED1 nguy\u00EAn");

	$.validator.addMethod("maxVal", function(value, element) {
		if ($(element).val() != null) {
			if ($(element).val().convertStringToNumber() > $(element).attr("maxVal").convertStringToNumber()) {
				$.validator.messages.maxVal = "Gi\u00E1 tr\u1ECB kh\u00F4ng v\u01B0\u1EE3t qu\u00E1 "
						+ formatSo($(element).attr("maxVal"));
				return false;
			}
		}
		return true;
	});

	$.validator.addMethod("digitsNumber", function(value, element) {
		if ($(element).val().indexOf('.') > 0) {
			$.validator.messages.digitsNumber = "";
			$(element).val('');
			return false;
		}
		;
		$(element).val(formatSo($(element).val()));
		return true;
	});

	$.validator.addMethod("partnerCode", function(value, element) {
		value = value.replace(',', '');
		if (value.length > 3) {
			$.validator.messages.partnerCode = "mã không được vượt quá 3 kí tự"
			return false;
		}
		return true;
	}, "Hãy nhập số");

	$.validator.addMethod("hhmmss", function(value, element) {
		if (value == '')
			return true;
		if (value.length != 6)
			return false;
		var hh = parseInt(value.substring(0, 2));
		if (hh == 'NaN')
			return false;
		if (hh >= 24)
			return false;
		var mm = parseInt(value.substring(2, 4));
		if (mm == 'NaN')
			return false;
		if (mm >= 60)
			return false;
		var ss = parseInt(value.substring(4, 6));
		if (ss == 'NaN')
			return false;
		if (ss >= 60)
			return false;
		return true;

	}, "Sai &#273;&#7883;nh d&#7841;ng hhmmss");
	$.extend($.validator.messages, {
		required : "Hãy nhập.",
		remote : "Hãy sửa cho đúng.",
		email : "Hãy nhập email.",
		url : "Hãy nhập URL.",
		date : "Hãy nhập ngày.",
		dateISO : "Hãy nhập ngày (ISO).",
		number : "Hãy nhập số.",
		digits : "Hãy nhập chữ số.",
		creditcard : "Hãy nhập số thẻ tín dụng.",
		equalTo : "Hãy nhập thêm lần nữa.",
		accept : "Phần mở rộng không đúng."
	});
}(jQuery));


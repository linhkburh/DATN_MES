var saveUrl, editUrl, delUrl, datatableUrl = '', printUrl;
var closeOnSaveSuccess = false;
$(document).ready(function() {
	if (datatableUrl == '')
		datatableUrl = location.pathname + '?method=datatable';
	saveUrl = location.pathname + '?method=save';
	editUrl = location.pathname + '?method=edit';
	delUrl = location.pathname + '?method=del';
	copyUrl = location.pathname + '?method=copy';
	printUrl = location.pathname + '?method=print';
	uploadUrl = location.pathname + '?method=upload';
	toApproveUrl = location.pathname + '?method=toApprove';
	unToApproveUrl = location.pathname + '?method=unToApprove';
	approveUrl = location.pathname + '?method=approve';
	unApproveUrl = location.pathname + '?method=unApprove';
	cancelApproveUrl = location.pathname + '?method=cancelApprove';
	$(document).keypress(function(e) {
		if (e.which == 13) {
			if ($('#divGrid').css('display') == 'inline' || $('#divGrid').css('display') == 'block')
				findData();
		}
	});
});

function ShowConfirmDel() {
	jConfirm(sure_delete, 'OK', 'Cancel', function(r) {
		if (r)
			delAcction();

	});
}

function print() {
	if ($('select[name="reportType"]').length > 0
			&& ($('select[name="reportType"]').val() == 'excel' || $('select[name="reportType"]').val() == 'pdf')) {
		$.ajax({
			url : printUrl,
			data : $('#theForm').serialize(),
			success : function() {
			},
			error : function() {
				alert(in_loi);
			}
		});
	} else {
		var cacheUrl = $('#theForm').attr('action');
		$('#theForm').attr('action', printUrl);
		$('#theForm').submit();
		$('#theForm').attr('action', cacheUrl);
	}

}

function addNew() {
	validobj.resetForm();
	clearDiv('divDetail');
	$('#divDetail select').each(function(iIndex) {
		$(this).select2();
	});
	$('#divGrid').css('display', 'none');
	$('#divDetail').css('display', 'inline');
	$('#btnDel').css('display', 'none');
	if (typeof defaultValue == 'function')
		defaultValue();

}

function chkJson(jsonString, xhr) {
	try {
		// return JSON.parse(jsonString);
		if (xhr.getResponseHeader("content-type") == "application/json;charset=utf-8")
			return true;
		return false;
	} catch (err) {
		return false;
	}

}

function cancel() {
	$('#divGrid').css('display', 'inline');
	$('#divDetail').css('display', 'none');
}
function copy(saveData) {
	$('#id').prop('disabled', false);

	$.loader({
		className : "blue-with-image-2"
	});
	$.ajax({
		method : 'POST',
		async : false,
		type : "POST",
		url : copyUrl,
		data : saveData ? saveData : $('#theForm').serialize(),
		success : function(data, status, xhr) {
			$.loader("close");
			var result = chkJson(data, xhr);
			// Khong tra ve json
			if (!result) {
				if (data.trim() != '') {
					alert(data);
				}
				// Thuc hien thanh cong thi data=''
				else {
					alert(thuc_hien_thanh_cong, function() {
						findData();
						$('#divGrid').css('display', 'inline');
						$('#divDetail').css('display', 'none');
					});
				}
			} else {
				alert('Copy th\u00E0nh c\u00F4ng!', function() {
					if (typeof instanceCopyOk == 'function') {
						if (!instanceCopyOk(data))
							return;
					}
					findData();
					edit(data['id']);

				});
			}
		},
		error : function(data, xhr) {
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
function save(saveData) {
	$('#id').prop('disabled', false);

	// Truoc khi validate
	if (typeof beforeValidate == 'function')
		beforeValidate();

	// customize validate
	if (typeof instanceValidate == 'function') {
		if (!instanceValidate())
			return;
	} else {
		if (!$('#theForm').valid()) {
			alert('Thực hiện không thành công, vui lòng kiểm tra thông tin nhập liệu!');
			return;
		}
	}

	// Before save
	if (typeof beforeSave == 'function')
		beforeSave();

	$.loader({
		className : "blue-with-image-2"
	});
	$.ajax({
		method : 'POST',
		async : false,
		type : "POST",
		url : saveUrl
		// , data : saveData?saveData: $('#theForm').serialize()
		,
		data : $('#theForm').serialize(),
		success : function(data, status, xhr) {
			$.loader("close");
			var jsnResult = chkJson(data, xhr);
			// Khong tra ve json, truong hop tra ve loi hoac khong giu nguyen
			// man hinh
			if (!jsnResult) {
				if (data.trim() != '') {
					alert(data);
					if (typeof saveFail == 'function') {
						saveFail(data, xhr);
						return;
					}
				}
				// Thuc hien thanh cong thi data=''
				else {
					if (typeof instanceSuccess == 'function') {
						instanceSuccess(data, xhr);
						return;
					}
					alert(thuc_hien_thanh_cong, function() {
						findData();
						$('#divGrid').css('display', 'inline');
						$('#divDetail').css('display', 'none');
					});
				}
			} else {

				if (typeof instanceSuccess == 'function') {
					instanceSuccess(data, xhr);
					return;
				}
				alert(thuc_hien_thanh_cong, function() {
					if (closeOnSaveSuccess) {
						$('#divGrid').css('display', 'inline');
						$('#divDetail').css('display', 'none');
					} else {
						binding(data);
						findData();
					}

				});
			}

		},
		error : function(data, xhr) {
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
function getValByPath(jsonObj, path) {
	if (path === undefined)
		return "";
	var arr = path.split('.');
	for (var i = 0; i < arr.length; i++) {
		if (i == 0)
			rs = jsonObj[arr[i]];
		else
			rs = rs[arr[i]];
		if (rs === undefined)
			return "";
	}
	return rs;
}
function edit(id) {
	validobj.resetForm();
	$.loader({
		className : "blue-with-image-2"
	});
	clearDiv('divDetail');
	var tokenIdKey = $('#tokenIdKey').val();
	var tokenId = $('#tokenId').val();
	$.getJSON(editUrl, {
		"id" : id,
		"tokenIdKey" : tokenIdKey,
		"tokenId" : tokenId
	}).done(function(res) {
		if (typeof beforeEdit == 'function') {
			beforeEdit(res);
		}
		binding(res);
		$('#btnDel').css('display', '');
		$('#divGrid').css('display', 'none');
		$('#divDetail').css('display', 'inline');
		if (typeof formTypeApproveId !== 'undefined') {
			if (res.status == undefined || res.status == 0) {
				// Enable action
				$('#btnSave').css('display', 'inline');
				$('#btnDel').css('display', 'inline');
				$('#btnToApprove').css('display', 'inline');
				// Disable action
				$('#btnUnToApprove').css('display', 'none');
				$('#btnApprove').css('display', 'none');
				$('#btnUnApprove').css('display', 'none');
				$('#btnCancelApprove').css('display', 'none');
			} else if (res.status == 1) {
				// Enable action
				$('#btnUnToApprove').css('display', 'inline');
				// Disable action
				$('#btnSave').css('display', 'none');
				$('#btnDel').css('display', 'none');
				$('#btnToApprove').css('display', 'none');
				$('#btnApprove').css('display', 'none');
				$('#btnUnApprove').css('display', 'none');
				$('#btnCancelApprove').css('display', 'none');
			} else if (res.status == 2) {
				// Enable action
				$('#btnCancelApprove').css('display', 'inline');
				// Disable action
				$('#btnSave').css('display', 'none');
				$('#btnDel').css('display', 'none');
				$('#btnToApprove').css('display', 'none');
				$('#btnUnToApprove').css('display', 'none');
				$('#btnApprove').css('display', 'none');
				$('#btnUnApprove').css('display', 'none');
			}
		}

		if (typeof afterEdit == 'function')
			afterEdit(id, res);
		$.loader('close');
	});

}

function delAcction() {
	$.loader({
		className : "blue-with-image-2"
	});
	var tokenIdKey = $('#tokenIdKey').val();
	var tokenId = $('#tokenId').val();
	$.ajax({
		async : false,
		type : "POST",
		url : delUrl,
		data : $('#theForm').serialize(),
		success : function(data, status, xhr) {
			$.loader("close");
			var result = chkJson(data, xhr);
			if (!result) {// Khong phai json
				if (data.trim() != '') {
					// Thuc hien thanh cong thi data=''
					if (data == 'ConstraintViolationException') {
						alert(used_del);
					} else {
						alert(data);
					}

				} else {
					if (typeof delSuccess == 'function') {
						delSuccess();
						return;
					}
					alert(succ_del, function() {
						$('#divGrid').css('display', 'inline');
						$('#divDetail').css('display', 'none');
						findData();
					});

				}
			}

			if (typeof afterDelete == 'function')
				afterDelete(id, !result ? data : result);

		},
		error : function(xhr, status, error) {
			$.loader("close");
			alert(not_del);
		}
	});

}

function del() {
	if ($('#id').val().trim().length <= 0) {
		alert(not_select);
		return;
	}
	ShowConfirmDel();
}

function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL.split('&'), sParameterName, i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
};
function binding(res, divId) {
	// Binding
	var bindingDev;
	if (divId == undefined || divId.trim().length == 0)
		bindingDev = 'divDetail';
	else
		bindingDev = divId;
	$('#' + bindingDev + ' input[type!="button"],#' + bindingDev + ' select,#' + bindingDev + ' textarea').each(
			function() {
				if ($(this).attr('name') === undefined || $(this).attr('name').trim().length == 0)
					return;

				var value;
				try {
					value = eval('res' + $(this).attr('name').substring($(this).attr('name').indexOf('.')));
				} catch (err) {
					if ($(this).is("select")) {
						$(this).val('');
						$(this).select2();
					}
					return;
				}
				if ($(this).is("select")) {
					if (value === true)
						value = 'true';
					else if (value === false)
						value = 'false';
					$(this).val(value);
					$(this).select2();
					return;
				}
				if ($(this).prop('type') == 'radio') {
					$('input[name="' + $(this).attr('name') + '"][value="' + value + '"]').prop('checked', true);
				} else if ($(this).prop('type') == 'checkbox') {
					$(this).prop('checked', value);
				} else {
					$(this).val(value);
				}
			});
}

function toApprove() {
	jConfirm('Bạn có đồng ý chuyển phê duyệt', 'OK', 'Cancel', function(r) {
		if (!r)
			return;
		doAction(toApproveUrl);
	});
}
function unToApprove() {
	jConfirm('Bạn có đồng ý thực hiện lại', 'OK', 'Cancel', function(r) {
		if (!r)
			return;
		doAction(unToApproveUrl);
	});
}
function approve() {
	jConfirm('Bạn có đồng ý phê duyệt', 'OK', 'Cancel', function(r) {
		if (!r)
			return;
		doAction(approveUrl);
	});
}
function unApprove() {
	jConfirm('Bạn có đồng ý không phê duyệt', 'OK', 'Cancel', function(r) {
		if (!r)
			return;
		doAction(unApproveUrl);
	});
}
function cancelApprove() {
	jConfirm('Bạn có đồng ý hủy duyệt', 'OK', 'Cancel', function(r) {
		if (!r)
			return;
		doAction(cancelApproveUrl);
	});
}
function doAction(actionUrl) {
	if ($('#id').val().trim().length <= 0) {
		alert(not_select);
		return;
	}
	$.loader({
		className : "blue-with-image-2"
	});
	var tokenIdKey = $('#tokenIdKey').val();
	var tokenId = $('#tokenId').val();
	$.ajax({
		async : false,
		type : "POST",
		url : actionUrl,
		data : $('#theForm').serialize(),
		success : function(data, status, xhr) {
			$.loader("close");
			var result = chkJson(data, xhr);
			if (!result) {// Khong phai json
				if (data.trim() != '') {
					// Thuc hien thanh cong thi data=''
					if (data == 'ConstraintViolationException') {
						alert(used_del);
					} else {
						alert(data);
					}

				} else {
					alert(succ_del, function() {
						$('#divGrid').css('display', 'inline');
						$('#divDetail').css('display', 'none');
						findData();
					});

				}
			}

			if (typeof afterDelete == 'function')
				afterDelete(id, !result ? data : result);

		},
		error : function(xhr, status, error) {
			$.loader("close");
			alert(thuc_hien_khong_thanh_cong);
		}
	});
}
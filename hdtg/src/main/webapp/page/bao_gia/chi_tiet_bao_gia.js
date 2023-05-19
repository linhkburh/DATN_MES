var createNew = false;
var tableOExeStep = null;
function initParam(tblCfg) {
	if ($('#to').val() != 'select' && $('#quotationId').val().trim().length == 0) {
		tblCfg.notSearchWhenStart = true;
		return;
	}
	// tblCfg.pageSize = 20;
	tblCfg.bFilter = true;
	tblCfg.approveInMain = true;
	if ($('#to').val() != 'select') {

		tblCfg.aoColumns = [ {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'STT'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Số lệnh sản xuất'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Mã bản vẽ'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Tên chi tiết'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Mã quản lý'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Vật liệu/Vật liệu thay thế'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Kích thước/Khối lượng'
		}, {
			"sClass" : "right",
			"bSortable" : false,
			"sTitle" : 'Đơn giá chi tiết'
		}, {
			"sClass" : "right",
			"bSortable" : false,
			"sTitle" : 'Số lượng chi tiết'
		}, {
			"sClass" : "right",
			"bSortable" : false,
			"sTitle" : 'Thành tiền'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Trạng thái'
		} ];

		tblCfg.buttons = [ {
			text : '&#xf093; Import',
			attr : {
				id : 'import'
			},
			className : 'mainGrid btnImp btn blue fa',
			action : function(e, dt, node, config) {
				uploadDetails();
			}
		}, {
			text : '&#xf019; Download template',
			attr : {
				id : 'download'
			},
			className : 'mainGrid btnImp btn blue fa',
			action : function(e, dt, node, config) {
				downloadTemplate();
			}
		}, {
			text : '&#xf067; Thêm bản vẽ',
			attr : {
				id : 'btnDtAdd'
			},
			className : 'mainGrid btnDtAdd btn blue fa',
			action : function(e, dt, node, config) {
				addNew();
			}
		}
		/*
		 * , { text : '&#xf051; Gửi duyệt', attr : { id : 'btnToApprove' },
		 * className : 'mainGrid ToApprove btn blue fa', action : function(e,
		 * dt, node, config) { } }
		 */
		, {
			text : 'Chỉnh sửa',
			attr : {
				id : 'btnUnToApprove'
			},
			className : 'mainGrid UnToApprove btn red',
			action : function(e, dt, node, config) {
			}
		}, {
			text : 'Duyệt',
			attr : {
				id : 'btnApprove'
			},
			className : 'mainGrid Approver btn blue',
			action : function(e, dt, node, config) {
			}
		}, {
			text : 'Từ chối',
			attr : {
				id : 'btnUnApprove'
			},
			className : 'mainGrid UnApprove btn red',
			action : function(e, dt, node, config) {
			}
		}, {
			text : 'Hủy duyệt',
			attr : {
				id : 'btnCancelApprove'
			},
			className : 'mainGrid CancelApprove btn red',
			action : function(e, dt, node, config) {
			}
		}, {
			text : '&#xf04b; Preview',
			attr : {
				id : 'btnPreview'
			},
			className : 'mainGrid Preview btn blue fa',
			action : function(e, dt, node, config) {
				preview();
			}
		}/* , {
			text : '&#xf019; Kết xuất công thức',
			attr : {
				id : 'btnExpPdf'
			},
			className : 'mainGrid ExpPdf btn blue fa',
			action : function(e, dt, node, config) {
				expPdf()
			}
		}, {
			text : '&#xf019; Kết xuất báo giá',
			attr : {
				id : 'btnExpExcelJrxl'
			},
			className : 'mainGrid ExpExcel btn blue fa',
			action : function(e, dt, node, config) {
				expExelJrxl()
			}
		}  */];
	} else if ($('#to').val() == 'select') {
		$('#btnDtAdd').remove();
		tblCfg.aoColumns = [ {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'STT'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Mã bản vẽ'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Tên chi tiết'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Mã quản lý'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Mã khách hàng'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Tên khách hàng'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Ngày báo giá'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Vật liệu/Vật liệu thay thế'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Kích thước/Khối lượng'
		}, {
			"sClass" : "right",
			"bSortable" : false,
			"sTitle" : 'Đơn giá chi tiết'
		}, {
			"sClass" : "right",
			"bSortable" : false,
			"sTitle" : 'Số lượng chi tiết'
		}, {
			"sClass" : "right",
			"bSortable" : false,
			"sTitle" : 'Thành tiền'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Ngày tạo'
		}, {
			"sClass" : "left",
			"bSortable" : false,
			"sTitle" : 'Người tạo'
		} ];

		tblCfg.buttons = [];
	}

}
function expPdf() {
	window.open('item?method=exp&type=pdf&quotationId=' + $("#quotationId").val()).focus();

}
function expExel() {
	window.open('item?method=exp&type=xls&quotationId=' + $("#quotationId").val()).focus();
}
function expExelJrxl() {
	window.open('item?method=expExelJrxl&quotationId=' + $("#quotationId").val()).focus();
}

function instanceFindComplete(iNumOfResult) {
	if (iNumOfResult >= 2)
		$('#btnPreview').css('display', 'inline');
	else
		$('#btnPreview').css('display', 'none');

}
function resetPreview() {
	currentNav = null;
	iCurrentIndex = null;
	lstPreviewItem = null;
	currentNav = null;
}
var currentNav;
function next() {
	currentNav = 'next';
	$('#btnPre').css('display', 'inline');
	iCurrentIndex++;
	if (iCurrentIndex == lstPreviewItem.length - 1)
		$('#btnNext').css('display', 'none');
	edit(lstPreviewItem[iCurrentIndex].id);

}
function previos() {
	currentNav = 'previos';
	$('#btnNext').css('display', 'inline');
	iCurrentIndex--;
	if (iCurrentIndex == 0)
		$('#btnPre').css('display', 'none');
	edit(lstPreviewItem[iCurrentIndex].id);

}

var lstPreviewItem = null;
var iCurrentIndex = null;
function preview() {
	$.ajax({
		url : 'item?method=preview',
		data : {
			squotationId : $("#quotationId").val()
		},
		method : 'GET',
		success : function(quotation, textStatus, jqXHR) {
			if(!chkJson(quotation, jqXHR)){
				alert(quotation);
				return;
			}
			lstPreviewItem = quotation.quotationItemList;
			iCurrentIndex = 0;
			currentNav = 'next';
			if (lstPreviewItem.length > 1)
				$('#btnNext').css('display', 'inline');
			edit(lstPreviewItem[iCurrentIndex].id);

		}
	});
}
function cancel() {
	if ($('#quotationId').val().trim().length == 0) {
		window.history.back()
		return;
	}
	$('#divGrid').css('display', 'inline');
	$('#divDetail').css('display', 'none');
}
function defaultValue() {
	$('#quotation_quotationDate').val(moment(new Date()).format("DD/MM/YYYY"));
	$('#waste').val($('#defaultWaste').val());
	$('#itemQuality', '#materialQuality').val('1');
	$('#quotation_currency').val($('#defaultCurrency').val());
	$('#quotation_currency').select2();
	if ($('#quotationId').val().trim().length != 0) {
		tableOExeStep.deleteAllRow();
		tablePro.deleteAllRow();
	}

}
function instanceSuccess(data, xhr) {
	if ($('#quotationId').val().trim().length == 0)
		alert(thuc_hien_thanh_cong, function() {
			window.location = 'item?quotationId=' + data.quotationId.id;
		});
	else {
		alert(thuc_hien_thanh_cong, function() {
			refreshQuotation();
			if (lstPreviewItem == null) {
				findData();
				// refresh data
				edit(data.id);
			}
		});
	}
}
function afterDelete() {
	resetPreview();
	refreshQuotation();

}
function refreshQuotation() {

	$.ajax({
		url : 'item?method=refreshQuotation',
		async : false,
		data : {
			quotationId : $('#quotationId').val()
		},
		method : 'GET',
		success : function(data) {
			$('#quotationTotal').html(data.priceStr);
		}
	});
}

function initOpPrice() {
	var drawingType = $('#drawingType').val();
	var marterialId = $('#marterialId').val();
	var backUpMarterialId = $('#backUpMarterialId').val();

	if (drawingType === '' || drawingType === null || drawingType === 'undefine')
		return;

	if ($('#marterialId').val().length == 0 && $('#backUpMarterialId').val().length == 0)
		return;

	$.ajax({
		url : 'item?method=initOpPrice',
		async : false,
		data : {
			drawingType : drawingType,
			marterialId : marterialId,
			backUpMarterialId : backUpMarterialId,
			quotationId : $('#quotationId').val()
		},
		method : 'GET',
		success : function(data) {
			$('#itemOpInitPrice').val(data);
			updateExeStepPrice();
		}
	});
}

function initForm() {
	makeTblExe();
	makeTblPro();
	// Mo link xem chi tiet
	if ($('#id').val().trim().length != 0) {
		sumary();
	} else {
		if ($('#to').val() != 'select') {
			if ($('#quotationId').length == 0 || $('#quotationId').val().trim().length == 0) {
				addNew();
				return;
			}
		}
	}
	// $(".btnDtAdd").text("Thêm chi tiết");
	if ($('#to').val() == "select")
		$('#btnSelect').val('Sử dụng');
	cfgActionBtn();
	// Chi cap nhat thong tin khac khang khi o trang thai draft
	if ($('#status').val() >= 1 && $('#status').val() != 3)
		$('#qCustomerCode, #qCustomerName').prop('readOnly', true);
}
$(document).ready(function() {
	initForm();
});

function sumary() {
	var totalExe = 0;
	var index = 0;
	while (true) {
		if ($('input[name="quotationItem.quotationItemExeList[' + index + '].priceStr"]').length <= 0)
			break;
		totalExe += $('input[name="quotationItem.quotationItemExeList[' + index + '].priceStr"]').val()
				.convertStringToNumber();
		index++;
	}

	index = 0;
	var totalPro = 0;
	while (true) {
		if ($('input[name="quotationItem.quotationItemProList[' + index + '].priceStr"]').length <= 0)
			break;
		totalPro += $('input[name="quotationItem.quotationItemProList[' + index + '].priceStr"]').val()
				.convertStringToNumber();
		index++;
	}
	$('div#table-exe_divAction span').text(formatSo(totalExe));
	$('div#table-pro_divAction span').text(formatSo(totalPro));
}
function afterAddRow(tableId) {
	var currentRowIdx = tableId == 'table-exe' ? tableOExeStep.toTalRow() - 1 : tablePro.toTalRow() - 1;
	var stepTypeControlName = "quotationItem."
			+ (tableId == 'table-exe' ? 'quotationItemExeList' : 'quotationItemProList') + "[" + currentRowIdx
			+ "].exeStepId.stepType.id";
	// combo box hinh thuc gia cong
	var mapData = $.map(lstStepType, function(obj) {
		var x = new Object();
		x.id = obj.id;
		x.text = obj.name;
		return x;
	});
	var stepTypeControl = $('select[name="' + stepTypeControlName + '"]');
	stepTypeControl.append('<option value=""></option>');
	stepTypeControl.select2({
		data : mapData
	});
	$('#' + tableId+ " .number").not('.textint').blur(function(){
        return isNumber(this,'d');
    }).keypress(function(){
        return formatNumber(this);
    });
}
function makeTblPro() {
	tablePro = new TFOnline.DataTable({
		id : 'table-pro',
		jQueryUI : true,
		rowTemp : initRowTableExeStep("pro"),
		afterDeleteRow : function() {
			caculatorAllExeStep();
		},
		afterAddRow : function() {
			afterAddRow('table-pro');
		},
		hasCheck : true,
		hasOrder : true,
		maxRow : 100
	});
	$('#table-pro_add').before(
			'<h7 style="float: left;padding-left: 10;font-weight: bold;">Tổng: <span>0</span> USD</h7>');

}
function makeTblExe() {
	tableOExeStep = new TFOnline.DataTable({
		id : 'table-exe',
		jQueryUI : true,
		rowTemp : initRowTableExeStep("exe"),
		beforeAddRow : function(callback) {
			callback(validateDrwInf());
		},
		afterDeleteRow : function() {
			caculatorAllExeStep();
		},
		afterAddRow : function() {
			afterAddRow('table-exe');
		},
		hasCheck : true,
		hasOrder : true,
		maxRow : 100
	});
	$('#table-exe_add').before(
			'<h7 style="float: left;padding-left: 10;font-weight: bold;">Tổng: <span>0</span> USD</h7>');

}
function cfgActionBtn() {
	if ($('#to').val() == 'select')
		return;
	if ($("#quotationId").val().length == 0)
		$('#btnPreview').remove();
	else {
		if ($('#status').val() == '' || $('#status').val() == '0' || $('#status').val() == '3') {
			// Enable action
			$('#btnSave').css('display', 'inline');
			$('#btnDel').css('display', 'inline');
			$('#btnToApprove').css('display', 'inline');
			// Disable action
			$('#btnUnToApprove').css('display', 'none');
			$('#btnApprove').css('display', 'none');
			$('#btnUnApprove').css('display', 'none');
			$('#btnCancelApprove').css('display', 'none');
		} else if ($('#status').val() == 1) {
			// Enable action
			$('#btnUnToApprove').css('display', 'inline');
			// Disable action
			$('#btnSave').css('display', 'none');
			$('#btnDel').css('display', 'none');
			$('#btnToApprove').css('display', 'none');
			$('#btnCancelApprove').css('display', 'none');
		} else if ($('#status').val() >= 2) {
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

}

var myJSON;

function configValidate(quotationItemId) {
	$('#divGrid').css('display', 'none');
	$('#divDetail').css('display', 'inline');
	initQuotationItem(quotationItemId);
}
function prepare(tblObject, res) {
	var listName = tblObject.id == 'table-exe' ? 'quotationItemExeList' : 'quotationItemProList';
	tblObject.resize(res[listName].length);
	// combo box hinh thuc gia cong
	var mapData = $.map(lstStepType, function(obj) {
		var x = new Object();
		x.id = obj.id;
		x.text = obj.name;
		return x;
	});
	for (var i = 0; i < res[listName].length; i++) {
		var jCboExeStepTypeName = 'select[name^="quotationItem.' + listName + '[' + i
				+ '"][name$="].exeStepId.stepType.id"]';
		var jCboExeStepType = $(jCboExeStepTypeName);
		jCboExeStepTypeName = jCboExeStepType.attr('name');
		var path = jCboExeStepTypeName.substring(jCboExeStepTypeName.indexOf('.') + 1);
		if (mapData.length > 1) {
			jCboExeStepType.append('<option value=""></option>');
		}
		jCboExeStepType.select2({
			data : mapData
		});
		jCboExeStepType.val(eval('res.' + path));
		fillCboExeStep(jCboExeStepType);
	}
}
function beforeEdit(res) {
	prepare(tableOExeStep, res);
	prepare(tablePro, res);
}
var stepTypeMap = new Map()
function getStep(stepTypeId) {
	if (stepTypeMap.get(stepTypeId) == undefined) {
		$.ajax({
			url : 'item?method=loadStepByType',
			async : false,
			data : {
				stepTypeId : stepTypeId
			},
			method : 'GET',
			success : function(data) {
				stepTypeMap.set(stepTypeId, data);
			}
		});
	}

	return stepTypeMap.get(stepTypeId)

}
function afterEdit(id, res) {
	var totalExe = 0;
	for (var index = 0; index < tableOExeStep.toTalRow(); index++) {
		totalExe += $('input[name="quotationItem.quotationItemExeList[' + index + '].priceStr"]').val()
				.convertStringToNumber();
		if (res.quotationItemExeList[index].remain == 0) {
			$('a[name="quotationItem.quotationItemExeList[' + index + '].id"]').css('color', 'blue');
			$('a[name="quotationItem.quotationItemExeList[' + index + '].id"]').text('Chi tiết lệnh sản xuất');
		}
	}

	var totalPro = 0;
	for (var index = 0; index < tablePro.toTalRow(); index++) {
		totalPro += $('input[name="quotationItem.quotationItemProList[' + index + '].priceStr"]').val()
				.convertStringToNumber();
		// Da phan cong
		if (res.quotationItemProList[index].workPros != undefined
				&& res.quotationItemProList[index].workPros.length > 0
				&& res.quotationItemProList[index].workPros[0].programer != undefined) {
			$('a[name="quotationItem.quotationItemProList[' + index + '].id"]').css('color', 'blue');
			$('a[name="quotationItem.quotationItemProList[' + index + '].id"]').text(
					res.quotationItemProList[index].workPros[0].programer.name);
		} else {
			$('a[name="quotationItem.quotationItemProList[' + index + '].id"]').css('color', 'red');
			$('a[name="quotationItem.quotationItemProList[' + index + '].id"]').text('Phân công lập trình');
		}
	}
	$('div#table-exe_divAction span').text(formatSo(totalExe));
	$('div#table-pro_divAction span').text(formatSo(totalPro));
	$("#price-exeStep span").text(res.stepPriceStr);
	$('input[name="quotationItem.bookingSetupTimeStr"]').val(res.bookingSetupTime);
	initControl();
}
function initQuotationItem(quotationItemId) {
	$.ajax({
		url : 'item?method=loadQuotationItem',
		data : {
			id : quotationItemId
		},
		method : 'GET',
		success : function(_result) {
			if (_result != null) {
				myJSON = JSON.parse(_result);
				binding(myJSON);
				console.log(myJSON);
				initDataTable(myJSON);
				$("#divDetail select").select2();

			}
		}
	});

}

function initDataTable(res) {
	tableOExeStep.deleteAllRow();
	if (res != null) {
		if (res.quotationItemMaterialList != null) {
			for (var idx = 0; idx < res.quotationItemMaterialList.length; idx++) {
				$('input[name="quotationItem.quotationItemMaterialList[' + idx + '].id"]').val(
						res.quotationItemMaterialList[idx].id);
				$('select[name="quotationItem.quotationItemMaterialList[' + idx + '].marterialId.id"]').val(
						res.quotationItemMaterialList[idx].marterialId.id);
				$('select[name="quotationItem.quotationItemMaterialList[' + idx + '].marteriaBackupId.id"]').val(
						res.quotationItemMaterialList[idx].marteriaBackupId.id);
				$('input[name="quotationItem.quotationItemMaterialList[' + idx + '].sizeLongStr"]').val(
						res.quotationItemMaterialList[idx].sizeLongStr);
				$('input[name="quotationItem.quotationItemMaterialList[' + idx + '].sizeWidthStr"]').val(
						res.quotationItemMaterialList[idx].sizeWidthStr);
				$('input[name="quotationItem.quotationItemMaterialList[' + idx + '].sizeHeightStr"]').val(
						res.quotationItemMaterialList[idx].sizeHeightStr);
				$('input[name="quotationItem.quotationItemMaterialList[' + idx + '].qualityStr"]').val(
						res.quotationItemMaterialList[idx].qualityStr);
				$('input[name="quotationItem.quotationItemMaterialList[' + idx + '].sizeWeighStr"]').val(
						res.quotationItemMaterialList[idx].sizeWeighStr);
				$('select[name="quotationItem.quotationItemMaterialList[' + idx + '].marterialId.id"]').change();
			}
		}
		if (res.quotationItemExeList != null) {
			for (var idx = 0; idx < res.quotationItemExeList.length; idx++) {
				tableOExeStep.addRow();
				$('input[name="quotationItem.quotationItemExeList[' + idx + '].id"]').val(
						res.quotationItemExeList[idx].id);
				$('select[name="quotationItem.quotationItemExeList[' + idx + '].exeStepId.stepType.id"]').val(
						res.quotationItemExeList[idx].exeStepId.stepType.id);
				$('select[name="quotationItem.quotationItemExeList[' + idx + '].exeStepId.stepType.id"]').change();
				$('select[name="quotationItem.quotationItemExeList[' + idx + '].exeStepId.id"]').val(
						res.quotationItemExeList[idx].exeStepId.id);
				$('input[name="quotationItem.quotationItemExeList[' + idx + '].unitExeTimeStr"]').val(
						res.quotationItemExeList[idx].unitExeTimeStr);
				$('select[name="quotationItem.quotationItemExeList[' + idx + '].exeStepId.id"]').change();

			}
		}
	}
	initControl();
}

function initRowTableExeStep(obj) {
	var listName = obj == 'exe' ? 'quotationItemExeList' : 'quotationItemProList';
	var rowTemp = new Array();
	rowTemp.push('');
	rowTemp.push('<div class="line-table"><select class="exeStepType notnull" name="quotationItem.' + listName
			+ '[].exeStepId.stepType.id"/></div>');
	rowTemp.push('<div class="line-table"><select class="exeStepId notnull" name="quotationItem.' + listName
			+ '[].exeStepId.id"></select></div>');
	rowTemp
			.push('<div class="line-table"><input type="text" name="quotationItem.'
					+ listName
					+ '[].initPriceStr" style="text-align: right;" class="form-control number initPrice" readOnly="readOnly"/></div>');
	if (obj == 'exe') {
		rowTemp.push('<div class="line-table"><input type="text" class="form-control number" name="quotationItem.'
				+ listName + '[].quotationExeTimeStr"  style="text-align: right;"/></div>');
	}
	rowTemp
			.push('<div class="line-table"><input type="text" class="form-control notnull number exeTime"  name="quotationItem.'
					+ listName + '[].unitExeTimeStr"  style="text-align: right;"/></div>');
	rowTemp
			.push('<div class="line-table"><input type="text" name="quotationItem.'
					+ listName
					+ '[].priceStr" class="form-control number totalPriceExe" readOnly="readOnly"  style="text-align: right;"/></div>');
	if (obj == 'exe') {
		rowTemp.push('<input type="hidden" class="form-control" name="quotationItem.' + listName
				+ '[].id" /> <a style="color:black;" name="quotationItem.' + listName
				+ '[].id"  href="javascript:;" onclick=lenhsx($(this))></a>');
	} else {
		rowTemp.push('<input type="hidden" class="form-control" name="quotationItem.' + listName
				+ '[].id" /> <a style="color:black;" name="quotationItem.' + listName
				+ '[].id"  href="javascript:;" onclick=lenhsx($(this))>Phân công lập trình</a>');
		
		rowTemp.push('<a class="upload" id="quotationItem.' + listName+ '[].id" href="javascript:;" style="float: left;"><i class="fa fa-upload"></i></a> '+
				'<a class="download" id="quotationItem.' + listName+ '[].id" href="javascript:;"  style="float: left; margin-left: 10;"><i class="fa fa-download"></i></a>');
			
			
	}
	return rowTemp;
}

function lenhsx(jHrefAct) {
	var id = $('input[name="' + $(jHrefAct).attr('name') + '"]').val();
	// Them hiddenMenu=true de an menu trong cua so con bat ra
	if ($(jHrefAct).attr('name').indexOf('quotationItemProList') > 0) {
		if (id.trim().length == 0) {
			alert('Cần lưu thông tin trước!');
			return;
		}
		window.open('lenhsx?hiddenMenu=true&id=' + id, '',
				'width=1200, height=800, status=yes, scrollbars=yes');
	} else {
		if ($(jHrefAct).text() == '')
			return;
		window.open('datlenhsx?hiddenMenu=true&quotationItemExe.id=' + id, '',
				'width=1200, height=800, status=yes, scrollbars=yes');
	}

}
function makeWorkOrderByItem() {
	if (!$('.table-responsive').find('input, select ,textarea').valid()) {
		alert('Chưa nhập đủ thông tin bắt buộc, cần nhập và lưu lại thông tin!');
		return;
	}
	for (var i = 0; i < $('input[name^="quotationItem.quotationItemExeList["][name$="].id"]').length; i++) {
		if ($('input[name="quotationItem.quotationItemExeList[' + i + '].id"]').val() == '') {
			alert('Cần lưu thông tin trước khi tạo lệnh sản xuất!');
			return;
		}

	}
	window.open('datlenhsx?hiddenMenu=true&makeWoByQi=true&quotationItem.id=' + $('#id').val(), '',
			'width=1200, height=800, status=yes, scrollbars=yes');
}
function validateDrwInf() {
	if ($('#itemQuality').val().trim().length == 0) {
		alert('Cần nhập số lượng để xác định mức giảm trừ', function() {
			$('#itemQuality').focus();
		});
		return false;
	}
	if ($('#drawingType').val().trim().length == 0) {

		alert('Cần nhập chủng loại bản vẽ để xác định đơn giá gia công', function() {
			$('#drawingType').focus();
		});

		return false;
	}

	if ($('#backUpMarterialId').val().trim().length == 0 && $('#marterialId').val().trim().length == 0) {

		alert('Cần nhập vật liệu để xác định đơn giá gia công', function() {
			$('#marterialId').focus();
		});

		return false;
	}
	return true;
}
function delRowExeStep() {
	tableOExeStep._deleteRow();
	caculatorAllExeStep();
}
function fillCboExeStep(jCboExeStepType) {
	var sel = $('select[name="' + jCboExeStepType.attr('name').replace('exeStepId.stepType.id', 'exeStepId.id') + '"]');
	$(sel).children().remove();
	for (var i = 0; i < lstStepType.length; i++) {
		if (lstStepType[i].id != jCboExeStepType.val())
			continue;
		var lstExeStep = jCboExeStepType.attr('name').indexOf('quotationItemExeList') >= 0 ? lstStepType[i].lstExe
				: lstStepType[i].lstExePro;
		map = $.map(lstExeStep, function(obj) {
			var x = new Object();
			x.id = obj.id;
			x.text = obj.stepName;
			return x;
		});
		if (lstExeStep.length == 1) {
			$(sel).val(lstExeStep[0].id);
			$(sel).select2({
				data : map
			});
			var unitPriceCtrName = jCboExeStepType.attr('name').replace("exeStepId.stepType.id", "initPriceStr");
			$('input[name="' + unitPriceCtrName + '"]').val(lstExeStep[0].initPriceStr);
		} else {
			$(sel).empty();
			$(sel).append('<option value=""></option>');
			$(sel).select2({
				data : map
			});
		}
		return;
	}
}

$(document.body).on("change", "select.selectMaterial, select.selectMaterialBackup", function() {
	var materialId;
	// Truong hop vat lieu chinh
	if ($(this).attr("class") == $("select.selectMaterial").attr("class")) {
		if ($("select.selectMaterialBackup").val() != "")
			return;
		else
			materialId = this.value;
	} else {
		// Un select vat lieu thay the
		if (this.value == '' && $("select.selectMaterial").val() != '')
			materialId = $("select.selectMaterial").val();
		else
			materialId = this.value;
	}
	var tr = $(this).parent().parent().parent();
	var initPrice = tr.find('input.material.initPrice');
	var totalPrice = tr.find('input.material.totalPrice');
	if (materialId == '') {
		initPrice.val('');
		totalPrice.val('');
		return;
	}

	var sizeLong = tr.find('input.material.sizeLong').val();
	var sizeWidth = tr.find('input.material.sizeWidth').val();
	var sizeHeight = tr.find('input.material.sizeHeight').val();
	var sizeWeigh = tr.find('input.material.sizeWeigh').val();
	var quality = tr.find('input.material.quality').val();

	if (materialId != '') {
		$.ajax({
			url : 'item?method=loadPriceByMaterial',
			data : {
				materialId : materialId
			},
			method : 'GET',
			async : false,
			success : function(data, status, xhr) {
				if (chkJson(data, xhr)) {
					initPrice.val(data.initPriceStr);
					$("#marterialDensity").val(data.densityStr);
				} else {
					alert(data)
				}
			}
		});
	}
	caculatorMaterial(materialId, sizeLong, sizeWidth, sizeHeight, sizeWeigh, quality, totalPrice);
	caculatorAllMaterial();
});

$(document.body).on(
		"change",
		"input.sizeLong,input.sizeWidth,input.sizeHeight,input.quality, input.sizeWeigh, input.waste",
		function() {
			var tr = $(this).parent().parent().parent();
			var initPrice = tr.find('input.initPrice');
			var sizeLong = tr.find('input.sizeLong').val();
			var sizeWidth = tr.find('input.sizeWidth').val();
			var sizeHeight = tr.find('input.sizeHeight').val();
			var sizeWeigh = tr.find('input.sizeWeigh').val();

			var quality = tr.find('input.quality').val();

			var totalPrice = tr.find('input.totalPrice');
			var material = tr.find('select.selectMaterial').val();
			var isChangeSizeWeight = $(this).attr('id') == 'marterialSizeWeigh';
			caculatorMaterial(material, sizeLong, sizeWidth, sizeHeight, sizeWeigh, quality, totalPrice,
					isChangeSizeWeight);
		});

function findExeStepType(jCboStepType) {
	for (var i = 0; i < lstStepType.length; i++) {
		if (lstStepType[i].id == jCboStepType.val())
			return lstStepType[i];
	}

}
function findExeStep(jCboStep) {
	var cboStepTypeName = jCboStep.attr('name').replace('.exeStepId.id', '.exeStepId.stepType.id');
	var jCboStepType = $('select[name="' + cboStepTypeName + '"]');
	var stepType = findExeStepType(jCboStepType);
	var lstSts = jCboStepType.attr('name').indexOf('quotationItemProList') >= 0 ? stepType.lstExePro : stepType.lstExe;
	for (var i = 0; i < lstSts.length; i++) {
		if (lstSts[i].id == jCboStep.val())
			return lstSts[i];
	}
}


function caculatorMaterial(marterialId, sizeLong, sizeWidth, sizeHeight, sizeWeigh, quality, totalPrice,
		isChangeSizeWeight) {
	if ($('#marterialId').val().length == 0 && $('#backUpMarterialId').val().length == 0)
		return;
	if (sizeWidth == '' && sizeHeight == '' && sizeWeigh == '')
		return;

	$.ajax({
		url : 'item?method=caculatorMaterial',
		data : {
			marterialId : marterialId,
			backUpMarterialId : $('#backUpMarterialId').val(),
			sizeLong : sizeLong,
			sizeWidth : sizeWidth,
			sizeHeight : sizeHeight,
			sizeWeigh : sizeWeigh,
			quality : quality,
			waste : $('#waste').val(),
			isChangeSizeWeight : isChangeSizeWeight,
			quotationDate : $('#quotation_quotationDate').val(),
			quotationId : $("#quotationId").val(),
			currenPriceStr : $('#marterialTotalPrice').val()
		},
		method : 'GET',
		async : false,
		success : function(data, status, xhr) {
			if (chkJson(data, xhr)) {
				$('#marterialTotalPrice').val(data.priceStr);
				// Khong phai thay doi trong luong
				if (!isChangeSizeWeight)
					$('#marterialSizeWeigh').val(data.sizeWeighStr);
			} else {
				alert(data)
			}
		}
	});
}
function caculatorAllMaterial() {
	var total = 0;
	for (var i = 0; i < $("input.totalPrice").length; i++) {
		if ($("input.totalPrice").get(i).value != null)
			total += Number($("input.totalPrice").get(i).value.replace(new RegExp(',', 'g'), ""));
	}
	$("#price-material span").text(total.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}

function caculatorExeStep(exeStepId, exeTime, totalPriceExe) {
	if (exeStepId == '' || exeTime != '') {
		return;
	}
	$.ajax({
		url : 'item?method=caculatorExeStep',
		data : {
			exeStepId : exeStepId,
			exeTime : exeTime,
			quotationDate : $('#quotation_quotationDate').val(),
			quotationId : $("#quotationId").val()
		},
		method : 'GET',
		async : false,
		success : function(data) {
			totalPriceExe.val(data.priceStr);
		}
	});
}

function updateTotalPrice(jTriggerCtrl) {
	var model = jTriggerCtrl.attr('name').indexOf('quotationItemExeList') > 0 ? "quotationItem.quotationItemExeList[0]"
			: "quotationItem.quotationItemProList[0]";
	model = model.replace(/(\[\d*\])(?!.*(\[\d*\]))/, '[' + getIndexByName(jTriggerCtrl.attr('name')) + ']');
	var currentPrice = $('input[name="' + model + '.priceStr"]').val().convertStringToNumber();

	var initPrice = $('input[name="' + model + '.initPriceStr"]').val().convertStringToNumber();
	var exeTime = $('input[name="' + model + '.unitExeTimeStr"]').val().convertStringToNumber();
	var newPrice = initPrice * exeTime / 60;
	$('input[name="' + model + '.priceStr"]').val(formatSo(newPrice));

	// Tinh span nao
	var totalSpan, totalOther;
	if (jTriggerCtrl.attr('name').indexOf('quotationItemExeList') > 0) {
		totalSpan = $("#table-exe_divAction span");
		totalOther = $("#table-pro_divAction span").text().convertStringToNumber();
	} else {
		totalSpan = $("#table-pro_divAction span");
		totalOther = $("#table-exe_divAction span").text().convertStringToNumber();
	}
	var totalPrice = totalSpan.text().convertStringToNumber();

	var newTotalPrice = totalPrice - currentPrice + newPrice;
	totalSpan.text(formatSo(newTotalPrice));

	// Tong gia cong
	if (jTriggerCtrl.attr('name').indexOf('quotationItemExeList') > 0)
		$('#price-exeStep span').text(
				formatSo(newTotalPrice * $('#itemQuality').val().convertStringToNumber() + totalOther));
	else
		$('#price-exeStep span').text(
				formatSo(newTotalPrice + totalOther * $('#itemQuality').val().convertStringToNumber()));

}

$(document.body).on("change", "select.exeStepType", function() {
	fillCboExeStep($(this));
});
$(document.body).on(
		"change",
		"select.exeStepId",
		function() {
			var initPriceCtrlName = $(this).attr('name').replace('.exeStepId.id', '.initPriceStr');
			var initPriceCtrl = $('input[name="' + initPriceCtrlName + '"]');
			var priceCtrlName = $(this).attr('name').replace('.exeStepId.id', '.priceStr');
			var priceCtrl = $('input[name="' + priceCtrlName + '"]');
			if ($(this).val().trim().length == 0) {
				initPriceCtrl.val('');
				priceCtrl.val('');
			} else {
				var exeStep = findExeStep($(this));
				if (exeStep.initPriceStr == undefined || exeStep.initPriceStr == '')
					initPriceCtrl.val(formatSo($('#itemOpInitPrice').val().convertStringToNumber()
							- $('#itemQualityReduce').val().convertStringToNumber()));
				else
					initPriceCtrl.val(exeStep.initPriceStr);
			}
		});
$(document.body).on("change", "select.exeStepType, select.exeStepId, input.initPrice, input.exeTime", function() {
	updateTotalPrice($(this));
});

function caculatorAllExeStep() {
	var totalPro = 0;
	$("#table-pro input.totalPriceExe").each(function() {
		totalPro += $(this).val().convertStringToNumber();
	})
	$("#table-pro_divAction span").text(formatSo(totalPro));

	var totalExe = 0;
	$("#table-exe input.totalPriceExe").each(function() {
		totalExe += $(this).val().convertStringToNumber();
	})
	$("#table-exe_divAction span").text(formatSo(totalExe));

	$("#price-exeStep span").text(formatSo(totalPro + totalExe * $('#itemQuality').val().convertStringToNumber()));
}

$(document.body).on("click", "button.ToApprove", function() {
	var v1 = validobj.element("#qCustomerCode");
	var v2 = validobj.element("#qCustomerName");
	var v3 = validobj.element("#quotationDateStr");
	var v4 = validobj.element("#quotation_currency");
	var v5 = validobj.element("#ratMachine");
	if (!v1 || !v2 || !v3 || !v4 || !v5)
		return;
	jConfirm("Chuyển phê duyệt?", 'Đồng Ý', 'Hủy', function(r) {
		if (r) {
			$.ajax({
				url : 'item?method=toApprove',
				data : {
					quotationId : $("#quotationId").val(),
					customerCode : $("#qCustomerCode").val(),
					customerName : $("#qCustomerName").val(),
					note : $("#qNote").val(),
					done : $("#qDone").is(":checked"),
					quotationDate : $("#quotationDateStr").val(),
					quotation_currency : $("#quotation_currency").val(),
					ratMachine : $("#ratMachine").val()
				},
				method : 'GET',
				success : function(data, textStatus, jqXHR) {
					if (data.trim() != '') {
						alert(data);
						return;
					}
					alert("Gửi duyệt thành công", function() {
						window.location.reload();
					});
				}
			});
		}
	});
});

$(document.body).on("click", "button.UnToApprove", function() {

	jConfirm("Bạn có chắc chắn muốn chỉnh sửa thông tin?", 'Đồng Ý', 'Hủy', function(r) {
		if (r) {
			$.ajax({
				url : 'item?method=cancelToApprove',
				data : {
					quotationId : $("#quotationId").val()
				},
				method : 'GET',
				success : function(data, textStatus, jqXHR) {
					alert(thuc_hien_thanh_cong, function() {
						window.location.reload();
					});
				}
			});
		}
	});
});

$(document.body).on("click", "button.Approver", function() {
	jPrompt("Chắn chắn muốn phê duyệt?");
	$("#action").val("2");
	$("#method").val("approve");
});

$(document.body).on("click", "button.UnApprove", function() {
	jPrompt("Chắn chắn muốn từ chối?");
	$("#action").val("3");
	$("#method").val("approve");
});

$(document.body).on("click", "button.CancelApprove", function() {
	jPrompt("Bạn có chắc chắn muốn hủy duyệt?");
	$("#action").val("3");
	$("#method").val("cancelApprove");
});

function processQuotation() {
	var v1 = validobj.element("#qCustomerCode");
	var v2 = validobj.element("#qCustomerName");
	var v3 = validobj.element("#quotationDateStr");
	var v4 = validobj.element("#quotation_currency");
	var v5 = validobj.element("#ratMachine");
	if (!v1 || !v2 || !v3 || !v4 || !v5)
		return;

	$.ajax({
		url : 'item?method=updateQuatation',
		data : serializeDiv('quotationInf'),
		method : 'GET',
		success : function(data, status, xhr) {
			alert("Cập nhập thành công", function() {
				if (chkJson(data, xhr))
					window.location.reload();

			});
		}
	});
}

function promptOk(val, action, method) {
	$.ajax({
		url : 'item?method=' + method,
		data : {
			quotationId : $("#quotationId").val(),
			status : action,
			note : val

		},
		method : 'GET',
		success : function(data, textStatus, jqXHR) {
			alert(thuc_hien_thanh_cong, function() {
				window.location.reload();
			});
		}
	});
}

function cancel() {
	if ($('#quotationId').val() == undefined || $('#quotationId').val().trim().length == 0)
		window.history.back();
	$('#divGrid').css('display', 'inline');
	$('#divDetail').css('display', 'none');
	if (lstPreviewItem != null)
		resetPreview();
}

function delQuotation() {
	jConfirm(sure_delete, 'OK', 'Cancel', function(r) {
		if (r) {
			$.ajax({
				url : 'item?method=delQuotation',
				data : {
					quotationId : $("#quotationId").val()
				},
				method : 'GET',
				success : function(data, textStatus, jqXHR) {
					if (data == '') {
						alert(thuc_hien_thanh_cong, function() {
							window.location.href = 'quotation';
						});
					} else {
						alert(data);
					}

				}
			});
		}
	});

}

function selectItem(quotationItemId) {
	$.loader({
		className : "blue-with-image-2"
	});
	// Cache Id
	var currentId = $('#id').val();
	var currentQuotationId = $('#quotationId').val();
	var manageCode = $('#manageCode').val();
	
	var tokenIdKey = $('#tokenIdKey').val();
	var tokenId = $('#tokenId').val();
	$
			.getJSON(editUrl, {
				"id" : quotationItemId,
				"tokenIdKey" : tokenIdKey,
				"tokenId" : tokenId
			})
			.done(
					function(res) {
						if (typeof beforeEdit == 'function') {
							beforeEdit(res);
						}
						clearDiv('divDetail');
						binding(res);
						$('#quotationId').val(currentQuotationId);
						$('#id').val(currentId);
						$('#manageCode').val(manageCode);
						$(
								'input[name^="quotationItem.quotationItemExeList["][name$="].id"], input[name^="quotationItem.quotationItemProList["][name$="].id"]')
								.val('');
						$('#qiMaterialId').val('');
						$.loader('close');
					});

}
function checkItemCode() {
	if ($('#code').val().trim().length == 0) {
		alert('Chưa nhập mã bản vẽ!');
		return;
	}
	$.ajax({
		url : 'item?method=checkCode',
		data : {
			code : $('#code').val(),
			id : $('#id').val(),
			to : $('#to').val()
		},
		method : 'GET',
		success : function(data, textStatus, jqXHR) {
			if (data == 'false') {
				alert('Mã bản vẽ chưa tồn tại!');
			} else {
				window.open('quanlyCT?hiddenMenu=true&to=select&code=' + $('#code').val(), '', big_window_property)
						.focus();
			}
		}
	});

}

function updateExchangePrice() {
	$.ajax({
		url : 'item?method=updateExchangePrice',
		data : {
			quotationId : $("#quotationId").val(),
			quotationDate : $("#quotationDateStr").val(),
			currency : $("#quotation_currency").val()
		},
		method : 'GET',
		success : function(data, textStatus, jqXHR) {
			$('#sExchangePriceStr').html(data);
		}
	});
}
function downloadTemplate() {
	window.open('item?method=download&quotationId=' + $("#quotationId").val()).focus();
}
function uploadDetails() {
	$('#file-select_imp').val('');
	$('#modal_dialog_file').modal();
}
function reloadReduce(amount) {
	var marterialId = $("#marterialId").val().trim();
	var marteriaBackupId = $("#backUpMarterialId").val().trim();
	var amount = $("#itemQuality").val().trim();
	if (amount.length == 0) {
		$('#itemQualityReduce').val('');
		return;
	}

	$.ajax({
		url : 'item?method=reloadReduce',
		data : {
			quality : amount,
			marterial : marteriaBackupId.length == 0 ? marterialId : marteriaBackupId
		},
		method : 'GET',
		success : function(data, textStatus, jqXHR) {
			$('#itemQualityReduce').val(data);
			updateExeStepPrice();
		}
	});
}
function updateExeStepPrice() {
	$('select.exeStepId').each(
			function() {
				if ($(this).children("option:selected").attr('fixPrice') == 'false') {
					var newUnitPrice = ($('#itemOpInitPrice').val().convertStringToNumber() - $('#itemQualityReduce')
							.val().convertStringToNumber())
							* $('#ratMachine').val().convertStringToNumber() / 100;
					var exeTime = $(
							'input[name="' + $(this).attr('name').replace('exeStepId.id', 'unitExeTimeStr') + '"]')
							.val().convertStringToNumber() / 60;
					var newPrice = exeTime * newUnitPrice;
					$('input[name="' + $(this).attr('name').replace('exeStepId.id', 'initPriceStr') + '"]').val(
							formatSo(newUnitPrice));
					$('input[name="' + $(this).attr('name').replace('exeStepId.id', 'priceStr') + '"]').val(
							formatSo(newPrice));
				}
			});
	caculatorAllExeStep();

}
function saveFail() {
	if ($('#id').val().trim().length > 0)
		edit($('#id').val());
}
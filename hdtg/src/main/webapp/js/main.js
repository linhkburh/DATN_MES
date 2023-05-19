/**
 * @author Nguyen Tien Dung
 */
    window_property = "width=1100, height=700, status=yes, scrollbars=yes";
    big_window_property = 'width=1200, height=800, status=yes, scrollbars=yes';
    function validatedate(inputText) {
	var dateformat = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
	// Match the date format through regular expression
	if (inputText.val().match(dateformat)) {
		// document.form1.text1.focus();
		// Test which seperator is used '/' or '-'
		var opera1 = inputText.val().split('/');
		var opera2 = inputText.val().split('-');
		lopera1 = opera1.length;
		lopera2 = opera2.length;
		// Extract the string into month, date and year
		if (lopera1 > 1) {
			var pdate = inputText.val().split('/');
		} else if (lopera2 > 1) {
			var pdate = inputText.val().split('-');
		}
		var dd = parseInt(pdate[0]);
		var mm = parseInt(pdate[1]);
		var yy = parseInt(pdate[2]);
		// Create list of days of a month [assume there is no leap year by
		// default]
		var ListofDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		if (mm == 1 || mm > 2) {
			if (dd > ListofDays[mm - 1]) {
				return false;
			}
		}
		if (mm == 2) {
			var lyear = false;
			if ((!(yy % 4) && yy % 100) || !(yy % 400)) {
				lyear = true;
			}
			if ((lyear == false) && (dd >= 29)) {
				return false;
			}
			if ((lyear == true) && (dd > 29)) {
				return false;
			}
		}
                return true;
	} else {
            return true;
	}
    }
    
    String.prototype.format = function (args) {
        var str = this;
        return str.replace(String.prototype.format.regex, function(item) {
            var intVal = parseInt(item.substring(1, item.length - 1));
            var replace;
            if (intVal >= 0) {
                    replace = args[intVal];
            } else if (intVal === -1) {
                    replace = "{";
            } else if (intVal === -2) {
                    replace = "}";
            } else {
                    replace = "";
            }
            return replace;
        });
    };
    String.prototype.format.regex = new RegExp("{-?[0-9]+}", "g");

        function getCurrentDate(){
            var d = new Date();
            var day;
            if(d.getDate()<10)
                day = '0' + d.getDate();
                else
                day = d.getDate();
                var moth;
            if(d.getMonth()<9)
                moth = '0' + (d.getMonth() + 1);
                else
                moth = d.getMonth() + 1;
                return day+ '/' + moth + '/' + d.getFullYear();
        }
        // lay ngay hien tai + 1
        function getCurrentDateCong1(){
            var d = new Date();
            d.setDate(d.getDate() + 1);
            var day;
            if(d.getDate()<10)
                day = '0' + d.getDate();
                else
                day = d.getDate();
                var moth;
            if(d.getMonth()<9)
                moth = '0' + (d.getMonth() + 1);
                else
                moth = d.getMonth() + 1;
                return day+ '/' + moth + '/' + d.getFullYear();
        }
        
        function yymmddForGetCurrentDate(){
            var d = new Date();
            var day;
            if(d.getDate()<10)
                day = '0' + d.getDate();
                else
                day = d.getDate();
                var moth;
            if(d.getMonth()<9)
                moth = '0' + (d.getMonth() + 1);
                else
                moth = d.getMonth() + 1;
                return d.getFullYear() + '/' + moth + '/' + day;
        }
        function date2int(sdate){
            var arrdate= sdate.split('/');
            return parseInt(arrdate[2]+arrdate[1]+arrdate[0])
            
            
        }
	function clearDiv(divId){
		$('#' + divId + ' input:not(input[type="button"], input[type="radio"], input[type="checkbox"]), #' + divId + ' select, #' + divId + ' textarea').val('');		
		$('#' + divId + ' input[type="radio"]' + ', #' + divId + ' input[type="checkbox"]').prop("checked", false);
		$('#' + divId + ' select').select2();
		
	}
	
	jQuery.fn.extractCookieValue = function(value) {
		  if ((endOfCookie = document.cookie.indexOf(";", value)) == -1) {
		     endOfCookie = document.cookie.length;
		  }
		  return unescape(document.cookie.substring(value, endOfCookie));
	};
	jQuery.fn.createCookie = function(cookie_name, value) {
		var expiry_date = new Date(2037, 01, 01); // virtually, never expire!
		document.cookie = cookie_name + "=" + escape(value) + ";expires=" + expiry_date.toUTCString();
	};
	
	jQuery.fn.readCookie = function(cookie_name) {
		  var numOfCookies = document.cookie.length;
		  var nameOfCookie = cookie_name + "=";
		  var cookieLen = nameOfCookie.length;
		  var x = 0;
		  while (x <= numOfCookies) {
		        var y = (x + cookieLen);
		        if (document.cookie.substring(x, y) == nameOfCookie)
		           return (this.extractCookieValue(y));
		           x = document.cookie.indexOf(" ", x) + 1;
		           if (x == 0){
		              break;
		           }
		  }
		  return 0;
	};
	function disabledItems(jContainer, isDisable){
		jContainer.find('input[type != "button"], select, textarea').prop("disabled", isDisable);
	}
	var beforActionSts;
	function disabledDiv(divId, isDisable){
		disabledItems($('#'+divId),isDisable);
	}
    function disabledAllDiv(divId, isDisable){
	 $('#' + divId + ' input:not([type = "button"]), #' + divId + ' select, #' + divId + ' textarea').prop("disabled", isDisable);
	}
	
	var approveUrl='';
	function changeSts(newSts, msgSuccess, msgFalse){
		if(approveUrl == ''){
			alert('Chưa khởi tạo hết tham số');
			return;
		}
		$.ajax({
			async : false,
			type : "POST",
			url : approveUrl,
			data : {
				newSts: newSts,
				id: $('#id').val()
			},
			success: function(){
				if(msgSuccess){
					alert(msgSuccess);
					return;
				}
				alert('Thực hiện thành công!');
				findData();
				
				$('.divGrid').css('display', 'block');
				$('#divDetail').css('display', 'none');
			},
			error: function(){
				if(msgFalse)
					alert(msgFalse);
				else
					alert('Thực hiện không thành công!');
			}
		});
	}
	
	var prev_val;
	function readOnlyAllField(divId){	
		
		if($('#to').val() == 'create'){
			if($('#approvestatus').val() == '1' || $('#approvestatus').val() == '4')
				readOnlyV = false;
			else
				readOnlyV = true;
		}			
		else
			readOnlyV = true;
		
		// Input
		$('#' + divId + ' input:not(input[id="approveusername"],input[id="createusername"]), #' + divId + ' textarea').prop("readonly",readOnlyV);
		
		// select
		if(readOnlyV){
			$('#' + divId + ' select').focus(function() {
			    prev_val = $(this).val();
			}).change(function() {
			     $(this).blur();    
			    alert('Không được thay đồi giá trị!');
				$(this).val(prev_val);
			});
		}else{
// $('#' + divId + ' select').focus(function() {
// return;
// }).change(function() {
// return;
// });
			$('#' + divId + ' select').unbind();
		}
		
		
		// Button
		$('#' + divId + ' input[type="button"]').prop("disabled",readOnlyV);
		$('input[value="Duyệt"], input[value="Không duyệt"], input[value="Hủy duyệt"], input[value="B�? qua"]').prop("disabled",false);
		
		// Duyet, khong duyet, huy duyet
		/*
		 * $('#' + divId + 'input[value="Duyệt"]').prop("disabled",!readOnlyV);
		 * $('#' + divId + 'input[value="Không
		 * duyệt"]').prop("disabled",!readOnlyV); $('#' + divId +
		 * 'input[value="Hủy duyệt"]').prop("disabled",!readOnlyV);
		 */
		
		
	}
	
	$(document).ajaxStart(function() {
            $.loader({
                className:"blue-with-image-2"
            });
            $("#jquery-loader-background").css("background-color", "rgba(87, 85, 99, 0.48)");
	});
	
	$(document).ajaxStop(function() {
          	$.loader("close");
          	$("#jquery-loader-background").css("background-color", "none");
	}); 
        
        
        var fnCallBackAfterRenewToken;
	
	function addFormData(aoData,serializeData){
        for (var key in serializeData) {
            if (serializeData.hasOwnProperty(key)) {
                aoData.push({
                    name: key,
                    value: serializeData[key]
                });
            }
        }
	}
	$.fn.serializeObject = function () {
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function () {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	}; 
	function isEmpty(str){
		return str == undefined || trim(str).length == 0;
		
	}
	function compareTime(time1Control, time2Control, operation, strMessage){	
		if(isEmpty(time1Control.val()) || isEmpty(time2Control.val()) || isEmpty(operation))
			return true;
		
		temp = time1Control.val().split(' ');
		arrDate = temp[0].split('/');
		arrTime = temp[1].split(':');
		time1ToString = arrDate[2] + arrDate[1] + arrDate[0] + arrTime[0] + arrTime[1];
		
		temp = time2Control.val().split(' ');
		arrDate = temp[0].split('/');
		arrTime = temp[1].split(':');
		time2ToString = arrDate[2] + arrDate[1] + arrDate[0] + arrTime[0] + arrTime[1];
		
		
		if(parseInt(time1ToString) > parseInt(time2ToString) && operation == '>')
			return true;
		if(parseInt(time1ToString) >= parseInt(time2ToString) && operation == '>=')
			return true;
		if(parseInt(time1ToString) <= parseInt(time2ToString) && operation == '<=')
			return true;
		if(parseInt(time1ToString) < parseInt(time2ToString) && operation == '<')
			return true;
		else{
			try{
				$(time1Control).effect('highlight',null,1000);
			}catch(err1){
			}
			try{
				$(time2Control).effect('highlight',null,1000);
			}catch(err1){
			}
			
			if(strMessage){
				alert(strMessage);
			}else{
				alert('Sai logic v\u1EC1 th\u1EDDi gian!');
			}
			return false;			
		}
	}
	
function compareDate(fromDate,toDate, operator){
	if(fromDate.val().trim()=='' || toDate.val().trim()=='')
		return true;
	arrFromDate = fromDate.val().split('/');
	iFromDate = parseInt(arrFromDate[2] + arrFromDate[1] + arrFromDate[0]);
	arrToDate = toDate.val().split('/');
	iToDate = parseInt(arrToDate[2] + arrToDate[1] + arrToDate[0]);
	return (operator == '>' && iFromDate > iToDate) || (operator == '<' && iFromDate < iToDate) || (operator == '>=' && iFromDate >= iToDate)
	|| (operator == '<=' && iFromDate <= iToDate) || (operator == '=' && iFromDate == iToDate);
}
function renewData(combobox, data){
	combobox.empty();
	combobox.append('<option value=""></option>'); 
	arrData = data.split('###');	
	var temp;
	for(var i = 0; i< arrData.length - 1; i++){
		temp = arrData[i].split('$$$');
		combobox.append('<option value="' + temp[0] + '">' + temp[1] + '</option>');
	}       
}
TFOnline = {
	DataTable: function(config) {
		var instance = this;
		instance.id = config.id;
		instance.sortable = config.sortable;
		instance.paginable = config.paginable;
		instance.hasCheck = config.hasCheck;
		instance.hasOrder = config.hasOrder;
		instance.addButton = config.addButton;
		instance.afterAddRow = config.afterAddRow;
        instance.beforeAddRow = config.beforeAddRow;
		instance.delButton = config.delButton;
		instance.afterDeleteRow = config.afterDeleteRow;
		instance.rowTemp = config.rowTemp;
		instance.hasFilter = config.hasFilter;
		instance.filterTitle = config.filterTitle;
		instance.sScrollY = config.sScrollY;
		instance.showInfo = config.showInfo;
		instance.maxRow = config.maxRow;
		instance.jQueryUI = config.jQueryUI;
		instance.aaSorting = config.aaSorting;
        instance.addCaption = config.addCaption;
        instance.delCaption = config.delCaption;
		instance.dataTable = {};
		instance.table = $('#' + instance.id);		
        instance.readOnly = config.readOnly;
        instance.addOveride = config.addOveride;
        instance.delOveride = config.delOveride;
		instance._init();
	}, 
	
	
	Util: {
		buildSelect: function(config) {	
			var select = jQuery('#' + config.id);
			if (select) {
				var selectOption = [];
				var data = config.data;
				var labelField = config.labelField;
				var valueField = config.valueField;
				var selectVal = config.selectVal != undefined ? config.selectVal : 0;
				var selectNullable = (config.selectNullable != undefined && config.selectNullable === true) ? true : false;
				var nullValue = config.nullValue ? config.nullValue : '';
				if (selectNullable) selectOption.push('<option value="' + nullValue + '"></option>');
				jQuery.each(data,function() {
						var item = this;
						var value = item[valueField];
						var label = item[labelField];
						selectOption.push('<option value="' + value + '">' + label + '</option>');
					}
				);
				selectOption = selectOption.join('');
				select.empty().append(selectOption);
				select.val(selectVal);
			}
		},
		
		validateForm: function(formId) {
			$('#' + formId).validate(); 
		},
		
		getAjaxReturn: function (xml){
			return xml.split('PREFIX')[1];
		}
	}, 
	
	
};

TFOnline.DataTable.prototype = {		
	_init: function() { 
		var instance = this;
		if (instance.hasCheck && !instance.readOnly) {
            $('#' + instance.id + ' thead tr').append('<th width="10px" style="padding-right: 10px !important;"><input class="checkAll" type="checkbox"/></th>');
			$('#' + instance.id + ' tbody tr').append('<td width="10px"><input class="rowSelect" type="checkbox"/></td>');
            instance.rowTemp[instance.rowTemp.length] = '<input class="rowSelect" type="checkbox"/>';
		}
		if(instance.hasOrder){
            $('#' + instance.id + ' thead tr').prepend('<th width="10px">STT</th>');
			$('#' + instance.id + ' tbody tr').prepend('<td width="10px"><font></font></td>');
			instance.rowTemp[0] = '<font></font>';
		}
		
		instance.table.find('.checkAll').click(function(e) {
			instance._onCheckAllClick(e);
		});
                
        if(!instance.readOnly){
            if (instance.addButton && !instance.addOveride) {
				$('#' + instance.addButton).click(function (e) {
					instance._addRow('x');
				});
            }
            
            if (instance.delButton && !instance.delOveride) {
                $('#' + instance.delButton).click(function (e) {
                        instance._deleteRow();
                });
            }
            if(!instance.delButton && !instance.addButton){
                $('#'+instance.id).after('<div align="right" style="text-align:right" id="'+instance.id+'_divAction"><input type="button" id="'+ instance.id +'_add" value="'+(instance.addCaption?instance.addCaption:'&#xf067; TH\u00CAM') +'" class="btn blue  fa" style="font-size: 10px; height: auto; padding: 5px 12px;"><input type="button" id="'+instance.id+'_del" value="'+(instance.delCaption?instance.delCaption:'&#xf2d3; X\u00D3A') +'" class="btn red fa" style="font-size: 10px;height: auto;padding: 5px 12px;"></div>');
                $('#'+instance.id + '_add').click(function(e) {
                    instance._addRow('x');
                });
                $('#'+instance.id + '_del').click(function(e) {
                    instance._deleteRow('x');
                });
            }
        }
		
		instance.dataTable = $('#' + instance.id).dataTable({
			"bJQueryUI": instance.jQueryUI,
			dom:"",
			"oLanguage": {
	            "sLengthMenu": "Hiển thị _MENU_ bản ghi",
	            "sSearch": instance.filterTitle != undefined ? instance.filterTitle : "Tìm: ",
	            "sZeroRecords": " ",
	            "sInfo": "Hiển thị _START_ tới _END_ của _TOTAL_ bản ghi",
	            "sInfoEmpty": "Hiển thị từ 0 tới 0 trên tổng số 0 bản ghi",
	            "sInfoFiltered": "(�?ã l�?c từ _MAX_ tổng số bản ghi)"
	        },
	        "sScrollY": instance.sScrollY != undefined ? instance.sScrollY : "",
	        "bPaginate": instance.paginable,
			"bSort": instance.sortable,
			"bFilter": instance.hasFilter,
			"bInfo": instance.showInfo,
			"iDisplayLength": instance.maxRow != undefined ? instance.maxRow : 10,
			"sScrollX": instance.sScrollX,
			"sScrollXInner": instance.sScrollXInner,
			"bScrollCollapse": instance.bScrollCollapse,
			"iDisplayLength":20,
			"aaSorting": instance.aaSorting!=null?instance.aaSorting:[]
			// "aaSorting":[ 2, "asc" ]
		});
        instance._reindex();
	},
        disableUnselect: function(toVal){
            var instance = this;
            instance.table.find('tbody tr').each(function() {
                var checkbox = $(this).find('td input[type=checkbox][class=rowSelect]');
                if(!$(checkbox[0]).prop('checked')) {
                    $(this).find('td input,td select, td textarea').prop('disabled',toVal);
                }
            });
        },
        getSectedRow:function(){
            var instance = this;
            var lstItem = [];
            instance.table.find('tbody tr').each(function() {
                var checkbox = $(this).find('td input[type=checkbox][class=rowSelect]');
                if($(checkbox[0]).prop('checked')) {
                    lstItem.push($(this));
                }
            });
            return lstItem;
        }
	,addRow: function () {
		var instance = this;
		instance.dataTable.fnAddData(instance.rowTemp);
		instance._reindex();
	},
	resize:function(newSize){
		var instance = this;
		var iTemp = newSize - instance.toTalRow();
		if (iTemp > 0) {
			// Bo sung mot so dong
			for (var i = 0; i < iTemp; i++)
				instance.addRowWithoutReIndex();
		} else {
			// Xoa 1 so dong
			iTemp = Math.abs(iTemp);
			var totalRow = instance.toTalRow();
			for (var i = 0; i < iTemp; i++)
				instance.deleteRow(totalRow - (i + 1));
		}
		instance.reIndex();
	},
	addRowWithoutReIndex: function(){
		var instance = this;
		instance.dataTable.fnAddData(instance.rowTemp);
        	
		
	},
	toTalRow: function(){
		var instance = this;
		return instance.dataTable.fnGetNodes().length;
	},
	reIndex: function() {
		var instance = this;
		var nodes = instance.dataTable.fnGetNodes();
		
		$.each(nodes, function(index, node) {
			var row = this;
			$(row).find('td a, td input, td select, td span').each(function() {
                             if($(this).attr('name') && $(this).attr('name')!=null){
                                
                               
                                
                                
                                $(this).attr('name',$(this).attr('name').replace(/(\[\d*\])(?!.*(\[\d*\]))/,'[' + index + ']'));
                                
                                
                                // $(this).attr('name',$(this).attr('name').replace(/\[\d*\]/i,
								// '[' + index + ']'));
                             }
                            if($(this).attr('id') && $(this).attr('id')!=null){
                                $(this).attr('id',$(this).attr('id').replace(/(\[\d*\])(?!.*(\[\d*\]))/,'[' + index + ']'));
                                // $(this).attr('id',$(this).attr('id').replace(/\[\d*\]/i,
								// '[' + index + ']'));
                            }
                                
			});
			$(row).find('td font').each(function(){
				$(this).html(index + 1);
			});
		});		
		
		
		instance.dataTable.fnDraw();
	},
	deleteAllRow: function(){
		var instance = this;
		instance.dataTable.fnClearTable();
// instance.table.find('tbody tr').each(function() {
// instance.dataTable.fnDeleteRow(instance.dataTable.fnGetPosition(this));
// });
// instance._reindex();
	},
	deleteRow:function(iRowIndex){
		var instance = this;
		instance.dataTable.fnDeleteRow(iRowIndex);	
	},
	_deleteRow: function () {
		var instance = this;
			
		var iNumOfDel=0;
		instance.table.find('tbody tr').each(function() {
			
			var checkbox = $(this).find('td input[type=checkbox][class=rowSelect]');
			if($(checkbox[0]).prop('checked')) {
				iNumOfDel ++;
				instance.dataTable.fnDeleteRow(instance.dataTable.fnGetPosition(this));
			}
			
			
		});
		if(iNumOfDel==0){
			alert('B\u1EA1n ch\u01B0a ch\u1ECDn b\u1EA3n ghi n\u00E0o!');
			return;
		}
		instance._reindex();	
		if (instance.afterDeleteRow) {
			instance.afterDeleteRow();
		}
	}, 
	_deleteRowReIndex: function () {
		var instance = this;
			
		var iNumOfDel=0;
		instance.table.find('tbody tr').each(function() {
			
			var checkbox = $(this).find('td input[type=checkbox][class=rowSelect]');
			if($(checkbox[0]).prop('checked')) {
				iNumOfDel ++;
				instance.dataTable.fnDeleteRow(instance.dataTable.fnGetPosition(this));
			}
			
			
		});
		if(iNumOfDel==0){
			alert('B\u1EA1n ch\u01B0a ch\u1ECDn b\u1EA3n ghi n\u00E0o!');
			return;
		}
	}, 
	_onCheckAllClick: function (e) {		
		var instance = this;
		if (e.target.checked == true) {
			instance.table.find('.rowSelect:enabled').each(function() {this.checked = true;});
		} else {
			instance.table.find('.rowSelect').each(function() {this.checked = false;});
		}
	},
	
	_addRow: function () {
		var instance = this;
		
                if (instance.beforeAddRow) {
                    instance.beforeAddRow(function(isCheck){
                        if(!isCheck) return;
                        
                        if (instance.hasCheck && !instance.readOnly) {
                            instance.rowTemp[instance.rowTemp.length] = '<input class="rowSelect" type="checkbox"/>';
                        }
                        if (instance.hasOrder) {
                                instance.rowTemp[0] = '<font></font>';
                        }
                        
                        instance.dataTable.fnAddData(instance.rowTemp);
                        instance.dataTable.fnPageChange('last');
                        instance._reindex();
                        
                        if (instance.afterAddRow) {
                                instance.afterAddRow();
                        }
                    });   
		}else{
                     if (instance.hasCheck) {
			instance.rowTemp[instance.rowTemp.length] = '<input class="rowSelect" type="checkbox"/>';
                        }
                        if (instance.hasOrder) {
                                instance.rowTemp[0] = '<font></font>';
                        }
                        
                        instance.dataTable.fnAddData(instance.rowTemp);
                        instance.dataTable.fnPageChange('last');
                        instance._reindex();
                        
                        if (instance.afterAddRow) {
                                instance.afterAddRow();
                        }
                }
		
	},
	
	_reindex: function() {
		var instance = this;
		var nodes = instance.dataTable.fnGetNodes();
		
		$.each(nodes, function(index, node) {
			var row = this;
			$(row).find('td a, td input, td select, td span').each(function() {
                             if($(this).attr('name') && $(this).attr('name')!=null){
                                $(this).attr('name',$(this).attr('name').replace(/(\[\d*\])(?!.*(\[\d*\]))/,'[' + index + ']'));
                             }
                            if($(this).attr('id') && $(this).attr('id')!=null){
                                $(this).attr('id',$(this).attr('id').replace(/(\[\d*\])(?!.*(\[\d*\]))/,'[' + index + ']'));
                            }
			});
			$(row).find('td font').each(function(){
				$(this).html(index + 1);
			});
		});		
		instance.dataTable.fnDraw();
	}
};
function replaceLast(sinput, sreplace, replacement){
    reg = '/'+sreplace+'([^'+sreplace+']*)$/';
    sinput = sinput.replace(reg,replacement+'$1'); 
    return sinput;
}
function isTime(ctrName)
{
   ctr=eval(ctrName);
   strDate=ctr.value;
   if (isEmpty(strDate)) return true;
   len=strDate.length;
   var strTime;
   if (len!=14&&len!=16)
   {
           
    // alert(message_error_ngay_khong_hop_le);
    alert('Th\u1EDDi \u0111i\u1EC3m kh\u00F4ng h\u1EE3p l\u1EC7!')
    $(ctr).effect('highlight',null,1000).focus().select();
    return false;
   }
   var arr=strDate.split(date_separator),strD,strM,strY;
   if (arr.length!=3)
   {
      alert(message_error_ngay_khong_hop_le);
      $(ctr).effect('highlight',null,1000).focus().select();
      return false;
   }
   else
   {      
        strY=arr[2];       
        if (strY.length==2) strY="20"+strY;
        strD=arr[0].split(' ')[1];       
        strM=arr[1];
   }
   if (!validDate(strD,strM,strY))
    {
      alert('Th\u1EDDi \u0111i\u1EC3m kh\u00F4ng h\u1EE3p l\u1EC7!')
      $(ctr).effect('highlight',null,1000).focus().select();
      return false;
    }
    strTime = strDate.split(' ')[0];
    arrTime = strTime.split(':')
    hh = arrTime[0];
    mm = arrTime[1];
    if(!(parseInt(mm) < 60 && parseInt(hh) < 24)){
            alert('Th\u1EDDi \u0111i\u1EC3m kh\u00F4ng h\u1EE3p l\u1EC7!')
                $(ctr).effect('highlight',null,1000).focus().select();
                return false;
    }
    if (len==14) ctr.value= strTime + ' ' + strD+date_separator+strM+date_separator+strY;
    return true;
}

function isDate(ctrName)
{
   var date_separator = "/";
   ctr=$(ctrName).val();
   strDate=$(ctrName).val();
   if (strDate == '') return true;
   len=strDate.length;
   if (len!=8&&len!=10)
   {
    alert('Ngày không hợp lệ');
    $(ctr).effect('highlight',null,1000).focus().select();
    return false;
   }
   var arr=strDate.split(date_separator),strD,strM,strY;
   if (arr.length!=3)
   {
      alert('Ngày không hợp lệ');
      $(ctr).effect('highlight',null,1000).focus().select();
      return false;
   }
   else
   {
        strY=arr[2];
        if (strY.length==2) strY="20"+strY;
        strD=arr[0];
        strM=arr[1];
   }
   if (!validDate(strD,strM,strY))
    {
	  alert('Ngày không hợp lệ');
      $(ctr).effect('highlight',null,1000).focus().select();
      return false;
    }
    if (len==8) ctr.value=strD+date_separator+strM+date_separator+strY;
    return true;
}

function LeapYear(intY)
{
           
    if (intY % 100 == 0)
          if (intY % 400 == 0)   return true;   
    else   
      if ((intY % 4) == 0) return true;   
    return false;
}

function validDateTime(ctrName,msg){
    var ctr=eval(ctrName);
    var val=ctr.value;
    var REG_TIME_DATE = /^(0[0-9]|[1][0-9]|2[0-4])\:([0-5][0-9]|60) (0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[012])\/(19|20)\d\d$/;
    if(!REG_TIME_DATE.test(val)){
                if(msg!=null) alert(msg);
                else alert(' Nh\u1EADp sai \u0111\u1ECBnh d\u1EA1ng ng\u00E0y gi\u1EDD\n'+
                                                    'V\u00ED d\u1EE5 \u0111\u1ECBnh d\u1EA1ng \u0111\u00FAng : \n'+
                                                    '1. hh:mm dd/mm/yyyy\n'+
                                                    '2. 24:59 30/02/2012\n'+
                                                    '3. 13:30 15/11/1999');
                ctr.focus();
                return false;
    }
    return true;
}

function validDate(strD,strM,strY)
{
    intY=parseInt(strY,10);
    if (isNaN(intY)) return false;
    intD=parseInt(strD,10);
    if (isNaN(intD)) return false;
    if (intD<1)      return false;
    intM=parseInt(strM,10);
    if (isNaN(intM))      return false;
    if (intM>12||intM<1)  return false;
    if ((intM == 1 || intM == 3 || intM == 5 || intM == 7 || intM == 8 || intM == 10 || intM == 12) && (intD > 31)) return false;
    if ((intM == 4 || intM == 6 || intM == 9 || intM == 11) && (intD > 30)) return false;
    if (intM == 2) {
     if (LeapYear(intY))    
      if (intD>29) return false;    
     else    
      if (intD>28) return false;    
    }
    return true;
}

function onlyNumber(event)
{
   // return (event.charCode >= 48 && event.charCode <= 57) || event.charCode
	// == 0 || event.charCode == 46;
    var regex = new RegExp("^[0-9\.]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        if(event.charCode != 0){ 
            event.preventDefault();
            return false;
        }
    }      
}

function onlyNumberAndChar(event)
{
   // return (event.charCode >= 48 && event.charCode <= 57) || event.charCode
	// == 0 || event.charCode == 46;
    var regex = new RegExp("[a-z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        if(event.charCode != 0){ 
            event.preventDefault();
            return false;
        }
    }      
}


function onlyNumberNotDot(event)
{
   // return (event.charCode >= 48 && event.charCode <= 57) || event.charCode
	// == 0 || event.charCode == 46;
    var regex = new RegExp("^[0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        if(event.charCode != 0){ 
            event.preventDefault();
            return false;
        }
    }      
}
function toFixed(x) {
  if (Math.abs(x) < 1.0) {
    var e = parseInt(x.toString().split('e-')[1]);
    if (e) {
        x *= Math.pow(10,e-1);
        x = '0.' + (new Array(e)).join('0') + x.toString().substring(2);
    }
  } else {
    var e = parseInt(x.toString().split('+')[1]);
    if (e > 20) {
        e -= 20;
        x /= Math.pow(10,e);
        x += (new Array(e+1)).join('0');
    }
  }
  return x;
}
function FormatRate(control, decimaldigits)
{
    if(control == null)
        return;
        number = control.value;
    number = Math.round(number * Math.pow(10, decimaldigits)) / Math.pow(10, decimaldigits);
    control.value = number;
}

function FormatNumberV2(number,decimaldigits, dec, group){
    b='';
    if(number.indexOf(dec) > -1){
        b = dec; 
    }
    number = RemoveSpace(number);
    number = RemoveGroup(number, group);
    number = new BigNumber(number);
    e = number + '';
    f = e.split(dec);
    if (!f[0]) {
        f[0] = '0';
    }
    if (!f[1]) {
        f[1] = '';
    }
    if(f[1].length > decimaldigits){
        g = f[1].substring(0, decimaldigits);
        f[1] = g;
    }
    if(group != '' && f[0].length > 3) {
        h = f[0];
        f[0] = '';
        for(j = 3; j < h.length; j+=3) {
            i = h.slice(h.length - j, h.length - j + 3);
            f[0] = group + i +  f[0] + '';
        }
        j = h.substr(0, (h.length % 3 == 0) ? 3 : (h.length % 3));
        f[0] = j + f[0];
    }
    dec = (decimaldigits <= 0) ? '' : dec;
    if(f[1].length > 0) return f[0] + dec + f[1];
    else return f[0] + b;
}

function FormatNumber(number, decimaldigits, dec, group) {
 number = RemoveSpace(number);
 number = RemoveGroup(number, group);
 number = new BigNumber(number);
 // number = Math.round(number * Math.pow(10, decimaldigits)) / Math.pow(10,
	// decimaldigits);

 e = number + '';
 f = e.split('.');
 if (!f[0]) {
  f[0] = '0';
 }
 if (!f[1]) {
  f[1] = '';
 }
 if (f[1].length < decimaldigits) {
  g = f[1];
  for (i=f[1].length + 1; i <= decimaldigits; i++) {
   g += '0';
  }
  f[1] = g;
 }
 if(group != '' && f[0].length > 3) {
  h = f[0];
  f[0] = '';
  for(j = 3; j < h.length; j+=3) {
   i = h.slice(h.length - j, h.length - j + 3);
   f[0] = group + i +  f[0] + '';
  }
  j = h.substr(0, (h.length % 3 == 0) ? 3 : (h.length % 3));
  f[0] = j + f[0];
 }
 dec = (decimaldigits <= 0) ? '' : dec;
 if(f[0] == "0" && f[1] == "0"){
    return f[0];
 }else{
    return f[0] + dec + f[1];
 }
}
function RemoveGroup(number, group)
{
    try{
        return number.split(group).join('');
    }
    catch(err)
    {
        return number;
    }
}
function RemoveSpace(number)
{
    try{
        return number.split(' ').join('');
    }
    catch(err)
    {
        return number;
    }
}

function FormatNumberTXTB (decimaldigits, textbox){
    textbox.value = FormatNumberV2(textbox.value, decimaldigits ,'.',',');
}

// only intNumber same numberCard : 111111111
function FormatNumberArr(textbox){
    number = textbox.value;
    number = RemoveSpace(number);
    number = new BigNumber(number);
    textbox.value = number;
}

function FormatMoney(Currency, textbox)
{    
// if($(textbox).attr('id') == 'strTransAmount')
// {
// strValue = textbox.value.split('.')[0].replace(',','')
// if(strValue.length > 25){
// alert('S\u1ED1 ti\u1EC1n kh\u00F4ng \u0111\u01B0\u1EE3c v\u01B0\u1EE3t
// qu\u00E1 25 k\u00ED t\u1EF1!');
// }
// }
    
    if(Currency == 'VND')
        textbox.value = FormatNumber(textbox.value,0,'.',',');
    else
        textbox.value = FormatNumber(textbox.value,2,'.',','); 
// $(textbox).formatCurrency();
}
function FormatMoney2(amount, currency){
    var dautru = "";
    if(amount<0){
        amount = Math.abs(amount);
        dautru= '-';
    }
        
    if(currency == 'VND')
        return dautru + FormatNumber(amount,0,'.',',');
    else
        return dautru + FormatNumber(amount,2,'.',','); 
}
// function formatNumber(input,sepChar){
// var a = input;
// a = a.replace(new RegExp("^(\\d{" + (a.length%3?a.length%3:0) + "})(\\d{3})",
// "g"), "$1 $2").replace(/(\d{3})+?/gi, "$1 ").trim();
// a = a.replace(/\s/g, sepChar);
// return a;
// }

/*
 * bat su kien go phim tren 1 control, bat buoc phai nhap cac ky tu so
 */


function strTransAmount_onKeyPress(con, event, maxlength, isSelect, controlSelect, currency)
{       
    if(controlSelect != null)
        controlSelect.val('false');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);    
    var arrValue = con.value.split('.');      
    if(arrValue.length > 1)
    {
        var dotIndex = $('#' + con.id).val().indexOf('.');
        var keyIndex = $('#' + con.id).caret().start;
        sValue = RemoveGroup(arrValue[0],',');
        sDecimal = arrValue[1];
        if(keyIndex <= dotIndex){
            if(sValue.length >= maxlength && event.charCode != 0 && !isSelect)
                return false;
            else{
                if(currency == 'VND')
                    return onlyNumberNotDot(event);
                else
                    return onlyNumber(event);
            }
        }
        else if(keyIndex > dotIndex)
        {
            if(sDecimal.length >= 2 && event.charCode != 0 && !isSelect)
                return false;
            else{
                if(currency == 'VND')
                    return onlyNumberNotDot(event);
                else
                    return onlyNumber(event);
            }
        }
    }
    else{
        var strValue = RemoveGroup(arrValue[0],',');
        if(strValue.length >= maxlength && event.charCode != 0 && !isSelect)
        {
            return false;
        }
        else{
            if(currency == 'VND')
                return onlyNumberNotDot(event);
            else
                return onlyNumber(event);
        }
    }   
}

    jQuery.uaMatch = function( ua ) {
        ua = ua.toLowerCase();

        var match = /(chrome)[ /]([w.]+)/.exec( ua ) ||
                /(webkit)[ /]([w.]+)/.exec( ua ) ||
                /(opera)(?:.*version|)[ /]([w.]+)/.exec( ua ) ||
                /(msie) ([w.]+)/.exec( ua ) ||
                ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([w.]+)|)/.exec( ua ) ||
                [];

        return {
                browser: match[ 1 ] || "",
                version: match[ 2 ] || "0"
        };
};

// Don't clobber any existing jQuery.browser in case it's different
if ( !jQuery.browser ) {
        matched = jQuery.uaMatch( navigator.userAgent );
        browser = {};

        if ( matched.browser ) {
                browser[ matched.browser ] = true;
                browser.version = matched.version;
        }

        // Chrome is Webkit, but Webkit is also Safari.
        if ( browser.chrome ) {
                browser.webkit = true;
        } else if ( browser.webkit ) {
                browser.safari = true;
        }

        jQuery.browser = browser;
}
// Overide function alert()
window.alert = function(message, funcCallback){
	var myCookies = getCookies();
	var lang = myCookies["org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE"];
	if(funcCallback)
	{
	    jAlert(message, lang,funcCallback);
	    return;
	}
	jAlert(message, lang);
    /*
	 * $.jAlert('attach'); if(funcCallback){
	 * 
	 * $.jAlert({ 'title': 'Th\u00F4ng b\u00E1o', 'content': message, 'theme':
	 * 'blue', 'size': 'md', 'showAnimation': 'fadeInUp', 'hideAnimation':
	 * 'fadeOutDown', 'btns': [ {'text':'Ok', 'closeAlert':true, 'onClick':
	 * funcCallback}, ] }); }else{ $.jAlert({ 'title': 'Th\u00F4ng b\u00E1o',
	 * 'content': message, 'theme': 'blue', 'size': 'md', 'showAnimation':
	 * 'fadeInUp', 'hideAnimation': 'fadeOutDown', 'btns': [ {'text':'Ok',
	 * 'closeAlert':true}, ] }); }
	 */
    
};

function getCookies(){
	  var pairs = document.cookie.split(";");
	  var cookies = {};
	  for (var i=0; i<pairs.length; i++){
	    var pair = pairs[i].split("=");
	    cookies[(pair[0]+'').trim()] = unescape(pair.slice(1).join('='));
	  }
	  return cookies;
	}
function getLines(strTextAreaId) {
    var oTextarea = document.getElementById(strTextAreaId);
    if (oTextarea.wrap) {
        oTextarea.setAttribute("wrap", "off");
    }
    else {
        oTextarea.setAttribute("wrap", "off");
        var newArea = oTextarea.cloneNode(true);
        newArea.value = oTextarea.value;
        oTextarea.parentNode.replaceChild(newArea, oTextarea);
        oTextarea = newArea;
    }

    var strRawValue = oTextarea.value;
    oTextarea.value = "";
    var nEmptyWidth = oTextarea.scrollWidth;
    var nLastWrappingIndex = -1;
    for (var i = 0; i < strRawValue.length; i++) {
        var curChar = strRawValue.charAt(i);
        if (curChar == ' ' || curChar == '-' || curChar == '+')
            nLastWrappingIndex = i;
        oTextarea.value += curChar;
        if (oTextarea.scrollWidth > nEmptyWidth) {
            var buffer = "";
            if (nLastWrappingIndex >= 0) {
                for (var j = nLastWrappingIndex + 1; j < i; j++)
                    buffer += strRawValue.charAt(j);
                nLastWrappingIndex = -1;
            }
            buffer += curChar;
            oTextarea.value = oTextarea.value.substr(0, oTextarea.value.length - buffer.length);
            oTextarea.value += "\n" + buffer;
        }
    }
    oTextarea.setAttribute("wrap", "");
}
String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};
function addCommas(nStr)
{
        nStr = nStr.replaceAll(",","");
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}        
	return x1 + x2;
}
$(document).ready(function(){
    $('.tv-khong-dau').each(function(){
        $(this).keypress(function(e){
            return chanTiemgVietKhongDau(e);
        });
    });
    $(".currency").change(function(){
         var value = $(this).val();
         // value = value.replace(/\D/g,'');
         var formatedValue = $(this).val();
         var max_length = $(this).attr("number-maxlength");
         var arrValue = value.split('.');
         // console.log(formatedValue);
         var real_number = arrValue[0].replaceAll(",","");
         if(max_length != undefined){
            if(real_number.length > max_length){
                alert("Vui l\u00F2ng nh\u1EADp t\u1ED1i \u0111a "+max_length+" k\u00ED t\u1EF1");
                if($(this).val().indexOf(".") > -1 ){
                    var _number_parts = formatedValue.replaceAll(",","").split(".");
                    if(_number_parts[0].length > max_length &&  _number_parts[1].length == 2){
                        _number_parts[0] = _number_parts[0].substring(0, max_length);
                        // console.log("_number_parts[0] : "+_number_parts[0]);
                        $(this).val(_number_parts[0]+"."+_number_parts[1]);
                    }else{
                        $(this).val(real_number.substring(0,max_length));
                    }
                }else{
                    $(this).val(real_number.substring(0,max_length));
                }
                
                formatedValue =  $(this).val();
                // console.log("formatedValue : "+formatedValue);
                $(this).val(addCommas(formatedValue));
                if($(this).val().indexOf(".") > -1 ){
                        var index = $(this).val().indexOf(".");
                        // console.log(index);
                        var length = $(this).val().length;
                        if(length - index > 2){
                            $(this).val($(this).val().substring(0,index+3));
                        }
                }else{
                    formatedValue = $(this).val().replaceAll(",","");
                    formatedValue = formatedValue.substring(0, formatedValue.length)+".00";
                    $(this).val(addCommas(formatedValue));
                }
                return;
            }
         }
         $(this).val(addCommas($(this).val()));
         formatedValue = $(this).val();
         var currency_unit = $(this).attr("currency-unit");
         if(currency_unit != undefined){
             currency_unit = currency_unit.replace(".","\\.");
             var currency = $("#"+currency_unit).val();
             // console.log(currency);
             if (typeof currency !== typeof undefined && currency !== false) {
                if(currency == 'VND' || currency == 'JPY' || currency == 'KRW'){
                    if($(this).val().indexOf(".") > -1 )
                        $(this).val($(this).val().substring(0,$(this).val().indexOf(".")));
                }else{
                    if($(this).val().indexOf(".") > -1 ){
                        var index = $(this).val().indexOf(".");
                        // console.log(index);
                        var length = $(this).val().length;
                        if(length - index > 2){
                            $(this).val($(this).val().substring(0,index+3));
                        }
                    }else{
                        formatedValue = $(this).val().replaceAll(",","");
                        // console.log("formatedValue : "+formatedValue);
                        if((formatedValue+".00").length > max_length){
                            var sub_index = max_length;
                            formatedValue = formatedValue.substring(0, sub_index )+".00";
                            // console.log(formatedValue);
                        }else{
                            formatedValue = formatedValue + ".00";
                        }
                        $(this).val(addCommas(formatedValue));
                    }
                }
             }
         }
    });
    $(".begin_end_character_validate").change(function(){
            var value = $(this).val();
            if(value != null && value != '' && value != undefined){
                    var i = false;
                    while(!i){
                        if(value.startsWith("-") || value.startsWith(",") || value.startsWith(" ")){
                                // console.log(123123);
                                value = value.substring(1);
                        }else{
                            i = true;
                        }
                    }
                    i = false;
                    while(!i){
                        if(value.endsWith(",") || value.endsWith("-")  || value.startsWith(" ")){
                                // console.log(12312344);
                                value = value.substring(0, value.length-1);
                        }else{
                            i = true;
                        }
                    }
            }
            $(this).val(value.trim());
     });
    $(".trim").change(function(){
        $(this).val($(this).val().trim());
    });
    $(".alphanum").keypress(function(e){
        var template = "0123456789.";
        var _char = String.fromCharCode(e.which);
        // console.log(_char);
        if(template.indexOf(_char) < 0)
            return false;
        
    });
    $(".biccode").change(function(){
        var value = $(this).val();
        var control = $(this);
        if(value != null && value != '' && value != undefined){
            
            var check_target = control.attr("check-dupe");
            // console.log(check_target);
            var check = true;
            if(check_target != undefined && check_target != null && check_target != ''){
                $(check_target).each(function(){
                    var value_current = control.val();
                    // console.log(value_current);
                    var id_current = control.prop("id");
                    var control_current = control;
                    if(value_current != ''){
                        $(".103_duplicate").each(function(){
                            _value = $(this).val();
                            id = $(this).prop("id");
                            if(id_current != id && value_current == _value){
                                alert("Kh\u00F4ng \u0111\u01B0\u1EE3c tr\u00F9ng m\u00E3 Biccode");
                                control_current.val('');
                                var _target_control = '#'+id_current+"_name";
                                // console.log(_target_control);
                                $(_target_control).val('');
                                check = false;
                                return;
                            }
                        });
                    }
                });
            }
            if(check){
                // console.log("processing");
                var base_uri = $("#footer").attr("uri");
                var target_control = '#'+$(this).prop("id")+"_name";
                // console.log(value);
                $.get("common.action?method=checkBicCode&biccode="+value, function( data ) {
                    var res = data;
                    // console.log(data);
                    if(res != '0'){
                        if($(target_control).is('input')){
                            $(target_control).css("color","blue");
                            $(target_control).val(res);
                        }else{
                            $(target_control).css("color","blue");
                            $(target_control).text(res);
                        }
                        control.attr("biccode_ok","1");
                    }else{
                        if($(target_control).is('input')){
                            $(target_control).css("color","red");
                            $(target_control).val('Kh\u00F4ng t\u00ECm th\u1EA5y \u0111\u1ED1i t\u00E1c n\u00E0y');
                        }else{
                            $(target_control).text('Kh\u00F4ng t\u00ECm th\u1EA5y \u0111\u1ED1i t\u00E1c n\u00E0y');
                            $(target_control).css("color","red");
                        }
                        control.attr("biccode_ok","0");
                    }
                })
            }
        }
    });
    
    $(".uppercase").change(function(){
        var value = $(this).val();
        var formated = value.toUpperCase();
        $(this).val(formated);
    });
    $.ajaxSetup({
        success:function(data){
            if("SessionTimeout" == data){
                window.location = 'login.action?request_locale=vi';
                return;
            }else if("Invalid token"== data){
                alert("invalid token");
                return;
            }
            
            
        },
        complete: function(xhr, status) {
            if (xhr.responseText == 'SessionTimeout') {
                window.location = "login.action?request_locale=vi";
                return;
                
            }
            if($('#tokenId').length>0){// Chuc nang co su dung token
            	// Khong phai ajax refresh token thi can sinh lai token
                if(xhr.responseText!= undefined && xhr.responseText.indexOf('newTokenId:')!=0 && this.url.indexOf('checkSession') < 0 ){
                    $.ajax({
                        url:'RenewToken?method=newTokenId&tokenId=' + $('#tokenId').val() + '&tokenIdKey='+ $('#tokenIdKey').val(),
                        type:'GET',
                        success:function(newTokenKeyandData){
                            $('#tokenId').val(newTokenKeyandData.substring('newTokenId:'.length));
                            if(fnCallBackAfterRenewToken)
                                fnCallBackAfterRenewToken();
                        }
                    });
                }    
                return;
            }
            try{
                var obj = JSON.parse(xhr.responseText);
                if(obj['MSG_CODE'] == 'BSN_ERR'){
                    if(obj['MSG_DETAIL']!=''){
                        alert(obj['MSG_DETAIL']);
                        return;
                    }
                        
                }          
                    
            }catch(err){
               // console.print("khong phai truong hop can quan tam");
            }
            
            
        },beforeSend: function (jqXHR, settings) {
            // if($('#comontokenid').length>0)
                // settings.url+="&comontokenid=" + $('#comontokenid').val();
        }
        
    });
    
    $(document).ajaxError(function(event, request, settings) {
         
    });    
    $('.textbox-money').each(function(){
        $(this).select(function(){
            $(this).attr("select", "1");
        });
        $(this).blur(function(){
            var currencyunit = $(this).attr("currency-unit");
            currencyunit = currencyunit.split('.').join('\\.');            
            var currency = $('#'+ currencyunit).val();
            var textboxValue = !$.isNumeric($(this).val()) && $(this).val() == "" ? "0" : $(this).val();
            var maxDecimalNumber = $(this) == null && $(this).attr("maxDecimalNumber") == null && $(this).attr("maxDecimalNumber") == "" && (currency == "JPY" || currency == "KRW") ? 0 : $(this).attr("maxDecimalNumber"); 
            var maxRealNumber =  $(this) == null &&  $(this).attr("maxRealNUmber") == null &&  $(this).attr("maxRealNUmber") == "" ? 0 :  $(this).attr("maxRealNUmber");
            var arrValue = textboxValue.split('.'); 
            var countRealNumber = RemoveGroup(arrValue[0], ',').length;  
            var countDecimalNumber = 0;
            if(arrValue.length > 1)
                countDecimalNumber = arrValue[1].length;
            
            if(arrValue.length > 2)
                textboxValue = arrValue[0] + arrValue[1];
                
            if(countRealNumber > maxRealNumber)    
                arrValue[0] = RemoveGroup(arrValue[0], ',').substring(0, maxRealNumber);
                
            if(countDecimalNumber > maxDecimalNumber)    
                arrValue[1] = arrValue[1].substring(0, maxRealNumber);
                
            textboxValue = FormatNumber(arrValue.join('.'), maxDecimalNumber, '.', ',');            
            $(this).val(textboxValue);    
        });
        $(this).keypress(function(xxx){
            var currencyunit = $(this).attr("currency-unit");
            currencyunit = currencyunit.split('.').join('\\.');            
            var currency = $('#'+ currencyunit).val();
            var textboxValue = !$.isNumeric($(this).val()) && $(this).val() == "" ? "0" : $(this).val();
            var maxRealNumber =  $(this) == null &&  $(this).attr("maxRealNUmber") == null &&  $(this).attr("maxRealNUmber") == "" ? 0 :  $(this).attr("maxRealNUmber");
            var maxDecimalNumber =  $(this) == null &&  $(this).attr("maxDecimalNumber") == null &&  $(this).attr("maxDecimalNumber") == "" && (currency == "JPY" || currency == "KRW") ? 0 :  $(this).attr("maxDecimalNumber");
            var isSelect =  $(this).attr("select") == null &&  $(this).attr("select") == "" && !$.isNumeric( $(this).attr("select")) ? false : Boolean(parseInt( $(this).attr("select")));
            var arrValue = textboxValue.split('.');
            $(this).attr("select", "0");
            
            if(arrValue.length > 1){
                var dotIndex =  $(this).val().indexOf('.');
                var keyIndex =  $(this).caret().start;
                sValue = RemoveGroup(arrValue[0],',');
                sDecimal = arrValue[1];
                if(keyIndex <= dotIndex){
                        if(sValue.length >= maxRealNumber && xxx.charCode != 0 && !isSelect)
                                return false;
                        else{
                            if(currency == "JPY" || currency == "KRW")
                                return onlyNumberNotDot(xxx);
                            else
                                return onlyNumber(xxx);
                        }
                }
                else if(keyIndex > dotIndex)
                {
                        if(sDecimal.length >= maxDecimalNumber && xxx.charCode != 0 && !isSelect)
                                return false;
                        else{
                            if(currency == "JPY" || currency == "KRW")
                                return onlyNumberNotDot(xxx);
                            else
                                return onlyNumber(xxx);
                        }
                }
            }
            else{
                    var strValue = RemoveGroup(arrValue[0],',');
                    if(strValue.length >= maxRealNumber && xxx.charCode != 0 && !isSelect && xxx.charCode != 46)
                    {
                            return false;
                    }
                    else{
                        if(currency == "JPY" || currency == "KRW")
                            return onlyNumberNotDot(xxx);
                        else
                            return onlyNumber(xxx);
                    }
            } 
        });
    });
});


function ShowNotificationUpload(mess)
{  
    try{
        $(dialog_Result).dialog({ autoOpen: false,modal:true,Width:300,
                              buttons: {
                                "Ok" : function(){
                                    $("#dialog_Result").dialog( "close" );   
                                }},
                              open: function(){
                                $('#lblNotification').text(mess);
                              },
                              close:function(){
                                   $('#dialog_Result').dialog('close');
                              }
        });
        $('#dialog_Result').dialog('open');
    }catch(err){
    }  
}
String.prototype.replaceAt=function(index, character) {
    return this.substr(0, index) + character + this.substr(index+character.length);
}

function chanTiemgVietKhongDau(event){
    var regex = new RegExp(/^[0-9a-zA-Z ... ]+$/);
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        if(event.charCode != 0){ 
            event.preventDefault();
            return false;
        }  
    }
}
// chan cac ky tu dac biet doi voi cac dien di MT103/195/196/199: � � ^ # | * @
// ` ~ ! % { } [ ] ? < > $ &
function chanKyTuDacBiet(event){
    var arr = [34 ,39 ,94 ,35 ,124 ,42 , 64, 96, 126, 33, 37, 123, 125, 91, 93, 63, 60, 62, 36, 38];
    // chanTiemgVietKhongDau(event);
    if(jQuery.inArray(event.charCode, arr) < 0)
        return true;
    else
        return false;
}
function removeDiacritics (str) {
    var diacriticsMap = {
        A: /[\u0041\u24B6\uFF21\u00C0\u00C1\u00C2\u1EA6\u1EA4\u1EAA\u1EA8\u00C3\u0100\u0102\u1EB0\u1EAE\u1EB4\u1EB2\u0226\u01E0\u00C4\u01DE\u1EA2\u00C5\u01FA\u01CD\u0200\u0202\u1EA0\u1EAC\u1EB6\u1E00\u0104\u023A\u2C6F]/g,
        AA: /[\uA732]/g,
        AE: /[\u00C6\u01FC\u01E2]/g,
        AO: /[\uA734]/g,
        AU: /[\uA736]/g,
        AV: /[\uA738\uA73A]/g,
        AY: /[\uA73C]/g,
        B: /[\u0042\u24B7\uFF22\u1E02\u1E04\u1E06\u0243\u0182\u0181]/g,
        C: /[\u0043\u24B8\uFF23\u0106\u0108\u010A\u010C\u00C7\u1E08\u0187\u023B\uA73E]/g,
        D: /[\u0044\u24B9\uFF24\u1E0A\u010E\u1E0C\u1E10\u1E12\u1E0E\u0110\u018B\u018A\u0189\uA779]/g,
        DZ: /[\u01F1\u01C4]/g,
        Dz: /[\u01F2\u01C5]/g,
        E: /[\u0045\u24BA\uFF25\u00C8\u00C9\u00CA\u1EC0\u1EBE\u1EC4\u1EC2\u1EBC\u0112\u1E14\u1E16\u0114\u0116\u00CB\u1EBA\u011A\u0204\u0206\u1EB8\u1EC6\u0228\u1E1C\u0118\u1E18\u1E1A\u0190\u018E]/g,
        F: /[\u0046\u24BB\uFF26\u1E1E\u0191\uA77B]/g,
        G: /[\u0047\u24BC\uFF27\u01F4\u011C\u1E20\u011E\u0120\u01E6\u0122\u01E4\u0193\uA7A0\uA77D\uA77E]/g,
        H: /[\u0048\u24BD\uFF28\u0124\u1E22\u1E26\u021E\u1E24\u1E28\u1E2A\u0126\u2C67\u2C75\uA78D]/g,
        I: /[\u0049\u24BE\uFF29\u00CC\u00CD\u00CE\u0128\u012A\u012C\u0130\u00CF\u1E2E\u1EC8\u01CF\u0208\u020A\u1ECA\u012E\u1E2C\u0197]/g,
        J: /[\u004A\u24BF\uFF2A\u0134\u0248]/g,
        K: /[\u004B\u24C0\uFF2B\u1E30\u01E8\u1E32\u0136\u1E34\u0198\u2C69\uA740\uA742\uA744\uA7A2]/g,
        L: /[\u004C\u24C1\uFF2C\u013F\u0139\u013D\u1E36\u1E38\u013B\u1E3C\u1E3A\u0141\u023D\u2C62\u2C60\uA748\uA746\uA780]/g,
        LJ: /[\u01C7]/g,
        Lj: /[\u01C8]/g,
        M: /[\u004D\u24C2\uFF2D\u1E3E\u1E40\u1E42\u2C6E\u019C]/g,
        N: /[\u004E\u24C3\uFF2E\u01F8\u0143\u00D1\u1E44\u0147\u1E46\u0145\u1E4A\u1E48\u0220\u019D\uA790\uA7A4]/g,
        NJ: /[\u01CA]/g,
        Nj: /[\u01CB]/g,
        O: /[\u004F\u24C4\uFF2F\u00D2\u00D3\u00D4\u1ED2\u1ED0\u1ED6\u1ED4\u00D5\u1E4C\u022C\u1E4E\u014C\u1E50\u1E52\u014E\u022E\u0230\u00D6\u022A\u1ECE\u0150\u01D1\u020C\u020E\u01A0\u1EDC\u1EDA\u1EE0\u1EDE\u1EE2\u1ECC\u1ED8\u01EA\u01EC\u00D8\u01FE\u0186\u019F\uA74A\uA74C]/g,
        OI: /[\u01A2]/g,
        OO: /[\uA74E]/g,
        OU: /[\u0222]/g,
        P: /[\u0050\u24C5\uFF30\u1E54\u1E56\u01A4\u2C63\uA750\uA752\uA754]/g,
        Q: /[\u0051\u24C6\uFF31\uA756\uA758\u024A]/g,
        R: /[\u0052\u24C7\uFF32\u0154\u1E58\u0158\u0210\u0212\u1E5A\u1E5C\u0156\u1E5E\u024C\u2C64\uA75A\uA7A6\uA782]/g,
        S: /[\u0053\u24C8\uFF33\u1E9E\u015A\u1E64\u015C\u1E60\u0160\u1E66\u1E62\u1E68\u0218\u015E\u2C7E\uA7A8\uA784]/g,
        T: /[\u0054\u24C9\uFF34\u1E6A\u0164\u1E6C\u021A\u0162\u1E70\u1E6E\u0166\u01AC\u01AE\u023E\uA786]/g,
        TZ: /[\uA728]/g,
        U: /[\u0055\u24CA\uFF35\u00D9\u00DA\u00DB\u0168\u1E78\u016A\u1E7A\u016C\u00DC\u01DB\u01D7\u01D5\u01D9\u1EE6\u016E\u0170\u01D3\u0214\u0216\u01AF\u1EEA\u1EE8\u1EEE\u1EEC\u1EF0\u1EE4\u1E72\u0172\u1E76\u1E74\u0244]/g,
        V: /[\u0056\u24CB\uFF36\u1E7C\u1E7E\u01B2\uA75E\u0245]/g,
        VY: /[\uA760]/g,
        W: /[\u0057\u24CC\uFF37\u1E80\u1E82\u0174\u1E86\u1E84\u1E88\u2C72]/g,
        X: /[\u0058\u24CD\uFF38\u1E8A\u1E8C]/g,
        Y: /[\u0059\u24CE\uFF39\u1EF2\u00DD\u0176\u1EF8\u0232\u1E8E\u0178\u1EF6\u1EF4\u01B3\u024E\u1EFE]/g,
        Z: /[\u005A\u24CF\uFF3A\u0179\u1E90\u017B\u017D\u1E92\u1E94\u01B5\u0224\u2C7F\u2C6B\uA762]/g,
        a: /[\u0061\u24D0\uFF41\u1E9A\u00E0\u00E1\u00E2\u1EA7\u1EA5\u1EAB\u1EA9\u00E3\u0101\u0103\u1EB1\u1EAF\u1EB5\u1EB3\u0227\u01E1\u00E4\u01DF\u1EA3\u00E5\u01FB\u01CE\u0201\u0203\u1EA1\u1EAD\u1EB7\u1E01\u0105\u2C65\u0250]/g,
        aa: /[\uA733]/g,
        ae: /[\u00E6\u01FD\u01E3]/g,
        ao: /[\uA735]/g,
        au: /[\uA737]/g,
        av: /[\uA739\uA73B]/g,
        ay: /[\uA73D]/g,
        b: /[\u0062\u24D1\uFF42\u1E03\u1E05\u1E07\u0180\u0183\u0253]/g,
        c: /[\u0063\u24D2\uFF43\u0107\u0109\u010B\u010D\u00E7\u1E09\u0188\u023C\uA73F\u2184]/g,
        d: /[\u0064\u24D3\uFF44\u1E0B\u010F\u1E0D\u1E11\u1E13\u1E0F\u0111\u018C\u0256\u0257\uA77A]/g,
        dz: /[\u01F3\u01C6]/g,
        e: /[\u0065\u24D4\uFF45\u00E8\u00E9\u00EA\u1EC1\u1EBF\u1EC5\u1EC3\u1EBD\u0113\u1E15\u1E17\u0115\u0117\u00EB\u1EBB\u011B\u0205\u0207\u1EB9\u1EC7\u0229\u1E1D\u0119\u1E19\u1E1B\u0247\u025B\u01DD]/g,
        f: /[\u0066\u24D5\uFF46\u1E1F\u0192\uA77C]/g,
        g: /[\u0067\u24D6\uFF47\u01F5\u011D\u1E21\u011F\u0121\u01E7\u0123\u01E5\u0260\uA7A1\u1D79\uA77F]/g,
        h: /[\u0068\u24D7\uFF48\u0125\u1E23\u1E27\u021F\u1E25\u1E29\u1E2B\u1E96\u0127\u2C68\u2C76\u0265]/g,
        hv: /[\u0195]/g,
        i: /[\u0069\u24D8\uFF49\u00EC\u00ED\u00EE\u0129\u012B\u012D\u00EF\u1E2F\u1EC9\u01D0\u0209\u020B\u1ECB\u012F\u1E2D\u0268\u0131]/g,
        j: /[\u006A\u24D9\uFF4A\u0135\u01F0\u0249]/g,
        k: /[\u006B\u24DA\uFF4B\u1E31\u01E9\u1E33\u0137\u1E35\u0199\u2C6A\uA741\uA743\uA745\uA7A3]/g,
        l: /[\u006C\u24DB\uFF4C\u0140\u013A\u013E\u1E37\u1E39\u013C\u1E3D\u1E3B\u017F\u0142\u019A\u026B\u2C61\uA749\uA781\uA747]/g,
        lj: /[\u01C9]/g,
        m: /[\u006D\u24DC\uFF4D\u1E3F\u1E41\u1E43\u0271\u026F]/g,
        n: /[\u006E\u24DD\uFF4E\u01F9\u0144\u00F1\u1E45\u0148\u1E47\u0146\u1E4B\u1E49\u019E\u0272\u0149\uA791\uA7A5]/g,
        nj: /[\u01CC]/g,
        o: /[\u006F\u24DE\uFF4F\u00F2\u00F3\u00F4\u1ED3\u1ED1\u1ED7\u1ED5\u00F5\u1E4D\u022D\u1E4F\u014D\u1E51\u1E53\u014F\u022F\u0231\u00F6\u022B\u1ECF\u0151\u01D2\u020D\u020F\u01A1\u1EDD\u1EDB\u1EE1\u1EDF\u1EE3\u1ECD\u1ED9\u01EB\u01ED\u00F8\u01FF\u0254\uA74B\uA74D\u0275]/g,
        oi: /[\u01A3]/g,
        ou: /[\u0223]/g,
        oo: /[\uA74F]/g,
        p: /[\u0070\u24DF\uFF50\u1E55\u1E57\u01A5\u1D7D\uA751\uA753\uA755]/g,
        q: /[\u0071\u24E0\uFF51\u024B\uA757\uA759]/g,
        r: /[\u0072\u24E1\uFF52\u0155\u1E59\u0159\u0211\u0213\u1E5B\u1E5D\u0157\u1E5F\u024D\u027D\uA75B\uA7A7\uA783]/g,
        s: /[\u0073\u24E2\uFF53\u015B\u1E65\u015D\u1E61\u0161\u1E67\u1E63\u1E69\u0219\u015F\u023F\uA7A9\uA785\u1E9B]/g,
        ss: /[\u00DF]/g,
        t: /[\u0074\u24E3\uFF54\u1E6B\u1E97\u0165\u1E6D\u021B\u0163\u1E71\u1E6F\u0167\u01AD\u0288\u2C66\uA787]/g,
        tz: /[\uA729]/g,
        u: /[\u0075\u24E4\uFF55\u00F9\u00FA\u00FB\u0169\u1E79\u016B\u1E7B\u016D\u00FC\u01DC\u01D8\u01D6\u01DA\u1EE7\u016F\u0171\u01D4\u0215\u0217\u01B0\u1EEB\u1EE9\u1EEF\u1EED\u1EF1\u1EE5\u1E73\u0173\u1E77\u1E75\u0289]/g,
        v: /[\u0076\u24E5\uFF56\u1E7D\u1E7F\u028B\uA75F\u028C]/g,
        vy: /[\uA761]/g,
        w: /[\u0077\u24E6\uFF57\u1E81\u1E83\u0175\u1E87\u1E85\u1E98\u1E89\u2C73]/g,
        x: /[\u0078\u24E7\uFF58\u1E8B\u1E8D]/g,
        y: /[\u0079\u24E8\uFF59\u1EF3\u00FD\u0177\u1EF9\u0233\u1E8F\u00FF\u1EF7\u1E99\u1EF5\u01B4\u024F\u1EFF]/g,
        z: /[\u007A\u24E9\uFF5A\u017A\u1E91\u017C\u017E\u1E93\u1E95\u01B6\u0225\u0240\u2C6C\uA763]/g
    };
    for (var x in diacriticsMap) {
        // Iterate through each keys in the above object and perform a replace
        str = str.replace(diacriticsMap[x], x);
    }
    return str;
}


function checkEmail(control){
    if (control.val().trim().length == 0) {
        var divErrorOld = $('div[for="'+control.attr('id')+'"]');
        if(divErrorOld != null)
            divErrorOld.remove();
            return true;
    }
    var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var check = regex.test(control.val());
    var thisParent = control.parent();
    var divErrorOld = $('div[for="'+control.attr('id')+'"]');
    if(divErrorOld != null)
        divErrorOld.remove();
    var divError = $('<div class="kythue-error" for="'+control.attr('id')+'">Sai \u0111\u1ECBnh d\u1EA1ng.<\/div>');
    if(!check){
        thisParent.append(divError);
    }else{
        var divErrorOld = $('div[for="'+control.attr('id')+'"]');
        if(divErrorOld != null)
            divErrorOld.remove();
    }
    return check;
}

function checkPhoneNumber(control){
    if (control.val().trim().length == 0) {
        var divErrorOld = $('div[for="'+control.attr('id')+'"]');
        if(divErrorOld != null)
            divErrorOld.remove();
            return true;
    }
    var regex = new RegExp("^[0-9]+$");
    var check = regex.test(control.val());
    var thisParent = control.parent();
    var divErrorOld = $('div[for="'+control.attr('id')+'"]');
    if(divErrorOld != null)
        divErrorOld.remove();
    var divError = $('<div class="kythue-error" for="'+control.attr('id')+'">Sai \u0111\u1ECBnh d\u1EA1ng.<\/div>');
    if(!check){
        thisParent.append(divError);
    }else{
        var divErrorOld = $('div[for="'+control.attr('id')+'"]');
        if(divErrorOld != null)
            divErrorOld.remove();
    }
    return check;
}

function checkMST(control){
    if (control.val().trim().length == 0) {
        var divErrorOld = $('div[for="'+control.attr('id')+'"]');
        if(divErrorOld != null)
            divErrorOld.remove();
    }
    if(!control.val())
        return true;
    var regexp = new RegExp(/^\d{10}(-\d{3}){0,1}$/);
    var check = regexp.test(control.val());
    var thisParent = control.parent();
    var divErrorOld = $('div[for="'+control.attr('id')+'"]');
    if(divErrorOld != null)
        divErrorOld.remove();
    var divError = $('<div class="kythue-error" for="'+control.attr('id')+'">Sai \u0111\u1ECBnh d\u1EA1ng.<\/div>');
    if(!check){
        thisParent.append(divError);
    }else{
        var divErrorOld = $('div[for="'+control.attr('id')+'"]');
        if(divErrorOld != null)
            divErrorOld.remove();
    }
    return check;
}

function calculateBetweenDate(control, control2){
    var day = 0;
    var date1 = $(control).datepicker('getDate');
    var date2 =  $(control2).datepicker('getDate');
    if(date1 != null && date2 != null)
        day = (date1 - date2) / 86400000;
    return day;
}

function addDayToDate(date, day){
    date.setDate(date.getDate() + day)
    return date;
}

function FormatDate(date){
    var dd = date.getDate();
    var MM = date.getMonth() + 1;
    var yyyy = date.getFullYear();
    if(dd < 10)
        dd = '0' + dd;
    if(MM < 10)
        MM = '0' + MM;
    return dd + '/' + MM + '/' + yyyy;
}

function calculateWeeks(date){
    var year = date.getFullYear();
    var newYear = new Date(year-1 + '-12-31T00:00:00Z');
    var day = (date - newYear)/86400000;
    var weeks = parseInt(day/7);
    return weeks;
}



var message_error_du_lieu_khong_hop_le="D\u1EEF li\u1EC7u kh\u00F4ng h\u1EE3p l\u1EC7";

// var sep=" ",empty="",comma=",",dot=".",numCheck="0123456789";
var empty="",comma=",",dot=".",numCheck="0123456789";
// Ky tu phan cach lop
var sep=",";
// var dec=",",date_separator="/";
// Tu tu ngan cach phan thap phan
var dec=".";
var date_separator="/";

/*
 * bat su kien go phim tren 1 control, bat buoc phai nhap cac ky tu so
 */
function formatNumber(ctrName, type)
{
  var strCheck=numCheck;
  if(type==undefined || type!='i'){
	    strCheck +=sep+dec;
  }
  key=String.fromCharCode(event.keyCode);
  if (strCheck.indexOf(key)==-1) return false;
  return true;
}

 function isNumber(ctrName,type,precision) {
        var ctr=eval(ctrName);
        var val=ctr.value;
        if (val.trim().length<1) {
                return true;
        }

        // Xoa het ky tu trang
        val=val.toString().replace(/\s/g,empty);
        val=val.split(String.fromCharCode(160)).join(empty);
        // Xoa ky ngan cach lop sep
        if(locale=='vi'){
        	val=val.toString().split(sep).join(empty);
        	// Thay dau phay boi dau cham
        	val=val.replace(dec,sep);
        }
        else
        	val=val.toString().replaceAll(sep,empty);
        	
        // Su dung regular expression
        // Truong hop so nguyen
        if (type=="i" || type== "i>0" || type == "i>=0" || type=="i<0")  
        	objRegExp=/^[-]?\d+$/;      
        else{
        	objRegExp =/^([-]?[0-9]*|[-]?\d*[\,\.]\d{1}?\d*)$/;
        } 
        	
        if (!objRegExp.test(val)) 
        	return  false;
        //val=val.split(sep).join(empty);
        //val = val.replace(',','.');
		
		var ok = true;
		
        if (type=="%" && (val>100 || val < 0)) {
            return  false;
        }
        if (type==">0" && val <= 0) {
            return  false;
        }
        if (type==">=0" && val < 0) {
            return  false;
        }
		if (type == "i>=0" && val < 0) {
            return  false;
		}
		if (type == "i>0" && val <= 0) {
            return  false;
		}
		if (type == "i<0" && val >= 0) {
            return  false;
		}
        ctr.value=formatSo(val,precision);
        return  true;
    }
    
    /*
	 * Dinh dang 1 so . Dau vao String num la kieu so Dau vao: Chuoi can format
	 * Optional: So chu so thap phan sau dau phay Dau ra: chuoi da duoc format
	 */

function formatSo(num,precision) {
	if (isEmpty(precision)){
		precision=2;
	} 
	var x=Math.pow(10,precision);
	var minus = "";
	// Remove space
	num=num.toString().replace(/\s/g,empty);   
	num=num.split(String.fromCharCode(160)).join(empty);      
	// Remove sep
	// Thay dau phay bang dau cham de chuyen ve so
	if(locale!='vi')
		num=num.toString().replace(/\,/g,dot);     
	if(isNaN(num))  
		num="0";
	if (num < 0) {
		minus = "-";
		num = Math.abs(num);
	}
	num=Math.floor(num*x+0.50000000001);
	var cents=num%x;
	num=Math.floor(num/x).toString();
	if (cents!=0)  {
		var len=cents.toString().length;
		if (len<precision){
			for (var i=1;i<=precision-len;i++)  
				cents="0"+cents;
		}	
	}
	for (var i=0;i<Math.floor((num.length-(1+i))/3); i++)
		num=num.substring(0,num.length-(4*i+3))+sep+num.substring(num.length-(4*i+3));
	if (cents==0)  
		return (minus + num);
	else 
		return (minus + num+dec+cents);
}

String.prototype.convertStringToNumber = function(){
    if (trim(this).length<1) 
    	return 0;
    // Remove space
    var num=this.replace(/\s/g,empty);   
    // Remove sep
    num=num.split(sep).join(empty);   
    if(locale=='vi')
    	num=num.replace(dec,'.');
    return Number(num);       
}
function trim(s) { return s.toString().replace(/^\s*/,"").replace(/\s*$/, "");}

function vi2en( alias )
{
    var str = alias;
    
    return str;
}
function listUlById(ulId){
	ulInforCode = '';
	ulInfor='';
	ulInforParent = '';
	ulInforValue = '';
	ulActions = '';
	listUl($('ul#' + ulId));
	console.log(ulInforParent);
	console.log(ulInforCode);
	console.log(ulInforValue);
	console.log(ulInfor);
	console.log(ulActions);
	
	
}
function listUl(ul, uLIdx){
	$.each($(ul).find('>li'),function(idx,item){
		var fullIdx;
		if(uLIdx==undefined)
			fullIdx = (idx+1);
		else fullIdx = uLIdx + '.' + (idx+1);
		listLi(item,fullIdx,uLIdx);
	});
}

function listLi(li,liIdx,uLIdx){
	$.each($(li).find('>a'), function(idx,item){
		ulInforCode += liIdx + '\n';
		ulInforParent+= uLIdx + '\n';
		ulInfor +=liIdx + '.'  + $(item).html() + '\n';
		ulInforValue += $(item).html() + '\n';
		ulActions += $(item).attr('href')+ '\n';
		
	});
	$.each($(li).find('>ul'), function(idx,item){
		listUl(item, liIdx);
	});
}
function listInput(){
	$.each($('#divDetail .label-static.col-md-2.col-lg-2.col-sm-12.col-xs-12'), function(idx){
		if($(this).html().trim().length!=0)
		 console.log($(this).html());
		
	});
	
	$.each($('#' + 'divDetail' + ' input:not(input[type="button"], input[type="radio"], input[type="hidden"]), #' + 'divDetail' + ' select, #' + 'divDetail' + ' textarea'), function(idx){
		 console.log($(this).attr('name'));
		
	});
}


function listInputNew(divId){
	if(divId==undefined)
		divId ='divDetail';
	$.each($('#' + divId + ' input:not(input[type="button"], input[type="radio"], input[type="hidden"]), #' + 'divDetail' + ' select, #' + 'divDetail' + ' textarea'), function(idx){
		 console.log($(this).parent().prev().html());
		
	});
	$.each($('#' + divId + ' input:not(input[type="button"], input[type="radio"], input[type="hidden"]), #' + 'divDetail' + ' select, #' + 'divDetail' + ' textarea'), function(idx){
		 console.log($(this).attr('name'));
		
	});
	
}
// Tao onblur de khong bi mat focus popup window
window.onactivate = window.document.onfocus = window.document.onactive = window.document.onclick = function() {
	try {
		if (globalLOVWindow != null && !globalLOVWindow.closed)
			globalLOVWindow.focus();
	} catch (e) {
	}
}
var globalLOVWindow=null;
function openWindow(url,w,h,features)
{
   if (globalLOVWindow && !globalLOVWindow.closed) globalLOVWindow.close();
   url=encodeURL(url);
   url+=(url.indexOf("?")!=-1)?"&lovWindow=true":"?lovWindow=true";
   ww=(isEmpty(w))?600:w;
   hh=(isEmpty(h))?400:h;
   if (!w || !h)
   {
     globalLOVWindow=window.open(url);
   }
   else
   {
      var wfeature='height='+hh+',width='+ww+',top='+(screen.height-hh)/2+',left='+(screen.width-ww)/2+',';
      wfeature +=(!features)?'status=1,scrollbars=1,resizable=1':features;
      globalLOVWindow=window.open(url,"",wfeature);
   }
   // globalLOVWindow.focus();
   sysfocus(globalLOVWindow);
   return globalLOVWindow;
}

function clone(obj){
	return JSON.parse(JSON.stringify(obj ));
}
function removeItem(arr, value) {
  var index = arr.indexOf(value);
  if (index > -1) 
	  arr.splice(index, 1);
}
function getIndexByName(ctrlName){
	var idxReg = /(\[(\d*)\])(?!.*(\[(\d*)\]))/;
	var m = idxReg.exec(ctrlName);
	return m[2];
}
function serializeDiv(divId) {
	return $('#' + divId + ' input:not([type = "button"]), #' + divId + ' select, #' + divId + ' textarea, #id')
			.serialize();
}
function isInputFocus(){
	var jAActiveElement = $(document.activeElement);
	var isInput = jAActiveElement.is('select')|| jAActiveElement.is('textarea');
	if(isInput)
		return isInput;
	if(!jAActiveElement.is('input'))
		return false;
	return jAActiveElement.attr('type')=='text';
}
window.open = function (open) {
    return function (url, name, features) {
    	if(features!=undefined){
    		var arrFeature = features.split(',');
        	// Left
        	var width = arrFeature[0].split("=")[1].trim();
        	if(width> screen.width)
        		width = screen.width;
        	var left = (screen.width - width)/2;
        	// Top height=700
        	var height = arrFeature[1].split("=")[1].trim();
        	if(height> screen.height)
        		height = screen.height;
        	var top = (screen.height - height)/2;
        	
        	features = 'left=' + left + ", top=" + top + "," + features;
    	}
    	if(features!=undefined){
    		globalLOVWindow = open.call(window, url, name, features);
            return globalLOVWindow;
    	}
    	return open.call(window, url, name, features);	
    };
}(window.open);


var saveUrl, editUrl, delUrl, datatableUrl = '', printUrl;
var closeOnSaveSuccess = false;
$(document).ready(function () {
    if (datatableUrl == '')
        datatableUrl = $('#theForm').attr('action') + '?method=datatable';
    saveUrl = $('#theForm').attr('action') + '?method=save';
    editUrl = $('#theForm').attr('action') + '?method=edit';
    delUrl = $('#theForm').attr('action') + '?method=del';
    printUrl = $('#theForm').attr('action') + '?method=print';
    uploadUrl = $('#theForm').attr('action') + '?method=upload';
    copyUrl = $('#theForm').attr('action') + '?method=copy';
    $(document).keypress(function(e) {
        if(e.which == 13) {
            if($('#divGrid').css('display')=='inline' || $('#divGrid').css('display')=='block')
                findData();
        }
    });
});

function ShowConfirmDel() {
    jConfirm(sure_delete, 'OK', 'Cancel', function (r) {
        if (r)
            delAcction();

    });
}

function print() {
    if ($('select[name="reportType"]').length > 0 && ($('select[name="reportType"]').val() == 'excel' || $('select[name="reportType"]').val() == 'pdf')) {
        $.ajax( {
            url : printUrl, data : $('#theForm').serialize(), success : function () {
            },
            error : function () {
                alert(in_loi);
            }
        });
    }
    else {
        $('#theForm').attr('action', printUrl);
        $('#theForm').submit();
    }

}

function addNew() {
    clearDiv('divDetail');
    $('#divDetail select').each(function (iIndex) {
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
        //return JSON.parse(jsonString);
        if(xhr.getResponseHeader("content-type")=="application/json;charset=utf-8")
            return true;
        return false;
    }
    catch (err) {
        return false;
    }

}

function cancel() {
    $('#divGrid').css('display', 'inline');
    $('#divDetail').css('display', 'none');
}

function save(saveData) {
    $('#id').prop('disabled', false);

    // Truoc khi validate    
    if (typeof beforeValidate == 'function')
        beforeValidate();

    if (!$('#theForm').valid()) {
        return;
    }
    // customize validate
    if (typeof instanceValidate == 'function') {
        if (!instanceValidate())
            return;
    }

    // Before save
    if (typeof beforeSave == 'function')
        beforeSave();
        
    $('input[type="checkbox"]').each(function () {
        $(this).val($(this).prop('checked'));
    });
    $('input[type="radio"]').each(function () {
        try {
            if ($(this).prop('checked')) {
                var arrName = $(this).attr('name').split('.');
                var realId = arrName[arrName.length - 1];
                $(this).val($(this).attr('id').substring(realId.length));
            }
        }
        catch (err) {
        }

    });
    $.ajax( {
        method : 'POST', async : false, type : "POST"
        , url : saveUrl
        , data : saveData?saveData: $('#theForm').serialize()
        , success : function (data, status, xhr) {
            var result = chkJson(data, xhr);
            if (typeof instanceSuccess == 'function') {
                instanceSuccess(data);
                return;
            }
            // Khong tra ve json
            if (!result) {
                if (data.trim() != '') {
                    alert(data);
                }
                // Thuc hien thanh cong thi data=''           
                else {
                    alert(thuc_hien_thanh_cong, function () {
                        findData();
                        $('#divGrid').css('display', 'inline');
                        $('#divDetail').css('display', 'none');
                    });
                }
            }
            else{    
                if (typeof instanceSuccess == 'function') {
                    instanceSuccess(data, xhr);
                }
                alert(thuc_hien_thanh_cong, function () {    
                    if(closeOnSaveSuccess){
                        $('#divGrid').css('display', 'inline');
                        $('#divDetail').css('display', 'none');
                    }else{
                    	binding(data);
                        findData();
                    }
                    	
                });
            }

        },
        error : function (data, xhr) {
            var result = chkJson(data);
            if (typeof instanceSaveFalse == 'function') {
                instanceSaveFalse(!result ? data : result);
                return;
            }

            alert(data);
        }
    });

}
function copy(saveData) {
    $('#id').prop('disabled', false);

    $.loader( {
        className : "blue-with-image-2"
    });
    $.ajax( {
        method : 'POST', async : false, type : "POST"
        , url : copyUrl
        , data : saveData?saveData: $('#theForm').serialize()
        , success : function (data, status, xhr) {
            $.loader("close");
            
            var result = chkJson(data, xhr);
            // Khong tra ve json
            if (!result) {
                if (data.trim() != '') {
                    alert(data);
                }
                // Thuc hien thanh cong thi data=''           
                else {
                    alert(thuc_hien_thanh_cong, function () {
                        findData();
                        $('#divGrid').css('display', 'inline');
                        $('#divDetail').css('display', 'none');
                    });
                }
            }
            else{
                alert('Copy th\u00E0nh c\u00F4ng!', function(){
                    if (typeof instanceCopyOk == 'function') {
                        if (!instanceCopyOk(data))
                            return;
                    }
                    findData();
                    edit(data['id']);
                });
            }
        },
        error : function (data, xhr) {
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
function catalogBinding(res){
    for (var prop in res) {
        // select
        $("select[name$='." + prop + "']").each(function () {
            var temp = res[prop];
            if (temp === true)
                temp = 'true';
            if (temp === false)
                temp = 'false';
            $(this).val(temp);
        });

        // input, textarea
        $("input[name$='." + prop + "'][type!='radio']" + ", textarea[name$='." + prop + "']").each(function () {
            $(this).val(res[prop]).trigger('change');
        });

        // radio
        $('input[type="radio"][name$=".' + prop + '"]').each(function () {
            // Gan gia tri
            //$(this).val(res[prop]);
            // Check theo id
            //$('#'+prop+res[prop]).prop('checked', true);
            $('input[name$=".' + prop + '"][value="' + res[prop] + '"]').prop('checked', true);
        });

        // checkbox
        $('input[type="checkbox"][name$=".' + prop + '"]').each(function () {
            $(this).prop('checked', res[prop]);
        });
    }
    //select2
    $('#divDetail select').select2();
}
function edit(id) {
    $.loader( {
        className : "blue-with-image-2"
    });
    clearDiv('divDetail');
    var tokenIdKey = $('#tokenIdKey').val();
    var tokenId = $('#tokenId').val();
    $.getJSON(editUrl, 
    {
        "id" : id, "tokenIdKey" : tokenIdKey, "tokenId" : tokenId
    }).done(function (res) {
        if (typeof beforeEdit == 'function') {
            beforeEdit(res);
        }
        catalogBinding(res);
        
        $('#btnDel').css('display', '');
        $('#divGrid').css('display', 'none');
        $('#divDetail').css('display', 'inline');
        if (typeof afterEdit == 'function')
            afterEdit(id, res);
        $.loader('close');
    });

}

function delAcction() {
    $.loader( {
        className : "blue-with-image-2"
    });
    var tokenIdKey = $('#tokenIdKey').val();
    var tokenId = $('#tokenId').val();
    $.ajax( {
        async : false, type : "POST", url : delUrl, data :  {
            id : $('#id').val(), "tokenIdKey" : tokenIdKey, "tokenId" : tokenId
        },
        success : function (data, status, xhr) {
            $.loader("close");
            var result = chkJson(data, xhr);
            if (!result) {// Khong phai json
                if (data.trim() != '') {
                    // Thuc hien thanh cong thi data=''                 
                    if (data == 'ConstraintViolationException') {
                        alert(used_del);
                    }
                    else {
                        alert(data);
                    }

                }
                else {
                    alert(succ_del, function () {
                        $('#divGrid').css('display', 'inline');
                        $('#divDetail').css('display', 'none');
                        findData();
                    });

                }
            }

            if (typeof afterDelete == 'function')
                afterDelete(id, !result ? data : result);

        },
        error : function (xhr, status, error) {
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
            var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                sURLVariables = sPageURL.split('&'),
                sParameterName,
                i;
        
            for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');
        
                if (sParameterName[0] === sParam) {
                    return sParameterName[1] === undefined ? true : sParameterName[1];
                }
            }
        };
var listBaseStateRMCU = [];
var initRmcodeUrl = '', tblDataRmcode;
$(document).ready(function () {
    initRmcodeUrl = $('#theForm').attr('action') + '?method=initTableRmcode';
    $('input[type="checkbox"]').click(function () {
        $(this).val($(this).prop('checked'));
    });
    $('#isRSA').switchbutton( {
        checked : false, onText : 'Yes', offText : 'No', value : false, onChange : function (checked) {
            $('#isRSA').switchbutton('setValue', checked);
            findData();
        }
    });
    $('#sTab01').css('display', 'inline');
    $('#sTab02').css('display', 'none');
    $('#tab01').addClass("current");
    $('#tab02').removeClass("current");
    $('#tab01').css('color', '#006ecb');
    $('#tab02').css('color', '#8E8E8E');
    
});


function loadTableRmcode(id, companyId) {
    var tokenIdKey = $('#tokenIdKey').val();
    var tokenId = $('#tokenId').val();
   
    tblDataRmcode = $('#tblDsNoiDung123').dataTable( {
        "bJQueryUI" : true, 
        "sPaginationType" : "full_numbers", 
        "iDisplayLength" : 10,
        "bProcessing" : true, 
        "bFilter" : bFilter, 
        "bServerSide" : true, 
        "sAjaxSource" : initRmcodeUrl, 
        "ordering": false,
        "fnServerData" : function (initRmcodeUrl, aoData, fnCallback) {
            addFormData(aoData, $('#theForm').serializeObject()); 
            aoData.push({name: "userId" , value : id});
            aoData.push({name: "tokenIdKey", value : tokenIdKey});
            aoData.push({name: "tokenId", value : tokenId});
            aoData.push({name: "companyId", value : companyId});
            $.ajax( {
                "dataType" : 'json', 
                "type" : "POST", 
                "url" : initRmcodeUrl, 
                "data" : aoData, 
                "success" : function (result) {
                    fnCallback(result);
                    try {
                        instanceUseResult(result);
                        
                    }
                    catch (err) {
                        // console.print('Instance does not use result more');
                    }
                    $.loader("close");
                    $(".date").datepicker({ 
                        dateFormat: 'dd/mm/yy',
                showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        yearRange: "-50:+50"    
                    });     
                    initCheck();
                    
                }
            });
        },
        "initComplete" : function (settings, json) {
                try {
                    instanceFindComplete(tblDataRmcode.fnGetData().length);
                    $("#input-datepicker").datepicker({ 
                        dateFormat: 'dd/mm/yy',
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        yearRange: "-50:+50" 
                    });     
                }
                catch (err) {
    
                }
            },
        "oLanguage" :  {
                "sLengthMenu" : hien_thi + "_MENU_" + ban_ghi, 
                "sZeroRecords" : " ", 
                "sInfo" : hien_thi + " _START_ " + den + " _END_ " + cua + " _TOTAL_ " + ban_ghi, 
                "sInfoEmpty" : hien_thi + " " + tu + " 0 " + toi + " 0 " + tren_tong_so + " 0 " + ban_ghi, 
                "sInfoFiltered" : "( " + da_loc_tu + " _MAX_ " + tong_so_ban_ghi + " )", 
                "oPaginate" :  {
                    "sFirst" : "&#xf049;", "sLast" : "&#xf050;", "sEnter" : "&#xf061;", "sPrevious" : "&#xf04a;", "sNext" : "&#xf04e;"
                },
                "sSearch" : tu_khoa
            },
        "bDestroy" : true,
        scrollX : bScrollX, 
        "fnDrawCallback" : function (oSettings) {
                if (typeof fnDrawCallback != typeof undefined) {
                    fnDrawCallback(oSettings);
                }
            }
        });
}


function updateDataTableRm(){
    
}

function editRmcodeUser(id){
    $.loader( {
        className : "blue-with-image-2"
    });
    clearDiv('divDetail');
    var tokenIdKey = $('#tokenIdKey').val();
    var tokenId = $('#tokenId').val();
    $.getJSON($('#theForm').attr('action') + '?method=editRmcodeUser', 
    {
        "id" : id, "tokenIdKey" : tokenIdKey, "tokenId" : tokenId
    }).done(function (res,status, xhr) {
        var result = chkJson(res, xhr);
        if(!result){
            alert(res);
            return;
        }
        listBaseStateRMCU = JSON.parse(res.rmCodeUsesBinding);
        loadTableRmcode(id);
        $('#btnDel').css('display', '');
        $('#divGrid').css('display', 'none');
        $('#divDetail').css('display', 'inline');
        $("#id").val(res.id);
        $.loader('close');
    }).error(function (res) {
        alert(res.responseText, function(){
             $.loader('close');
        });
    });
    
}

function editUser(id) {
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
                $(this).val(res[prop]);
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
        $('#divDetail select').each(function (iIndex) {
            $(this).select2();
        });
        $('#btnDel').css('display', '');
        $('#divGrid').css('display', 'none');
        $('#divDetail').css('display', 'inline');

        if (typeof afterEdit == 'function')
            afterEdit(id, res);
        $.loader('close');

    });
}


function initCheck() {
    for (var i = 0;i < listBaseStateRMCU.length;i++) {
            var itemdata = listBaseStateRMCU[i];
            $("#"+ itemdata.rmCd).checked = itemdata.check;
            $("#st-"+ itemdata.rmCd).checked = itemdata.startDt;
            $("#en-"+ itemdata.rmCd).checked = itemdata.expireDt;
            $("#mt-"+ itemdata.rmCd).checked = itemdata.masterRM;
            if (!itemdata.check) {
                $("#st-"+ itemdata.rmCd).css('display', 'none');
                $("#en-"+ itemdata.rmCd).css('display', 'none');
                $("#mt-"+ itemdata.rmCd).css('display', 'none');
            }
            else{
                $("#st-"+ itemdata.rmCd).css('display', 'block');
                $("#en-"+ itemdata.rmCd).css('display', 'block');
                $("#mt-"+ itemdata.rmCd).css('display', 'block');
            }
        }
    var checkall = true;
    for(var i = 0;i < listBaseStateRMCU.length;i++){
        if(listBaseStateRMCU[i].check == null || listBaseStateRMCU[i].check == false){
            checkall = false;
        }
    }
    $('#sla').prop('checked', checkall);
}


function selectAll(isSlAll) {
    $('.sli').prop('checked', isSlAll == true);
    for (var i = 0;i < listBaseStateRMCU.length;i++) {
        listBaseStateRMCU[i].check = isSlAll;
    }
    initCheck();

}

function selectItemMasterRM(ctrItem, index){
     var item = listBaseStateRMCU[index];
    item.ischange = true;
    item.masterRM = ctrItem.checked;
    if(ctrItem.checked){
       for (var i = 0;i < listBaseStateRMCU.length;i++) {
        var itemdata = listBaseStateRMCU[i];
        if(item.rmCd != itemdata.rmCd){
            itemdata.masterRM = false;
            $('#mt-'+itemdata.rmCd).prop('checked', false);
        }
        } 
    }
    
    
}

function selectItem(ctrItem, index) {
    var item = listBaseStateRMCU[index];
    if (ctrItem.checked) {
        $("#st-"+ item.rmCd).css('display', 'block');
        $("#en-"+ item.rmCd).css('display', 'block');
        $("#mt-"+ item.rmCd).css('display', 'block');
    }
    else {
        $("#st-"+ item.rmCd).css('display', 'none');
        $("#en-"+ item.rmCd).css('display', 'none');
        $("#mt-"+ item.rmCd).css('display', 'none');
    }
    $("#"+ item.rmCd).checked = ctrItem.checked;

    item.ischange = true;
    item.check = ctrItem.checked;
    
    var checkAll = true;
    for(var i = 0; i < listBaseStateRMCU.length; i ++){
        if(listBaseStateRMCU[i].check == false){
             checkAll = false;
        }
    }
    $('#sla').prop('checked', checkAll);
}

function changeStDate(ctrItem, index) {
    var item = listBaseStateRMCU[index];
    item.startDt = ctrItem.value;
}

function changeEnDate(ctrItem, index) {
    var item = listBaseStateRMCU[index];
    item.expireDt = ctrItem.value;
}


function loadcbHanMuc() {
    reloadCbHanMuc('', '');
    reloadCbHanMucQt('', '');
}

function defaultValue() {
    $('#btnResetPass').css('display', 'none');
    //$('#active').val(true);
    $('input[name="su.active"]').prop('checked', true);
    //$("#theForm_su_partnerId option[value='']").remove();
    initRoleTree();
    formatLayout();

}

function resetPass() {
    if ($('input[name="su.email"]').val().trim() == "") {
        alert('Vui l�ng nh?p ??a ch? Email');
        return false;
    }
    else {
        $.ajax( {
            url : $('#theForm').attr('action') + '?method=resetPass', method : 'POST', data :  {
                id : $('#id').val(), tokenId : $('#tokenId').val(), tokenIdKey : $('#tokenIdKey').val()
            },
            success : function (severtrave) {
                alert('<s:text name = "re_succ"/>');
            },
            err : function (severtrave) {
                alert('<s:text name = "no_reset"/>');
            }
        });
    }
}

function initRoleTree() {
    var sUrl = $('#theForm').attr('action') + '?method=treeRole' + '&suID=' + $('#id').val();
    if ($('select[name="su.jobId"]').val().trim() != '')
        sUrl += '&parentRoleId=' + $('select[name="su.jobId"]').val().trim();
    $.ajax( {
        async : false, type : "POST", url : sUrl, data :  {
            tokenId : $('#tokenId').val(), tokenIdKey : $('#tokenIdKey').val()
        },
        success : function (data1) {
            var myJSON = JSON.parse(data1);
            $('#danhSachQuyen').tree( {
                data : myJSON.treeRoles
            });
        },
        error : function (data1) {

        }
    });
}

function formatLayout() {
    //$('.dataTables_scrollHead').width($('#tblRight').width());
    $('.dataTables_scrollHeadInner').width("100%");
    //$('.dataTables_scrollHead').width($('#tblRight').width());
    $('.dataTable.no-footer').css('width', '100%');
}

function changeRootRole() {
    initRoleTree($('#input[name="su.jobId"]').val());
    var arrRight = $('input[name="su.roles"]').val().split(',');
    for (var i = 0;i < arrRight.length;i++) {
        $('td input[type="checkbox"][id^="chk"][id$="' + arrRight[i].trim() + '"]').prop('checked', true);
    }
}

function afterEdit(sid, res) {
    $('#sTab01').css('display', 'inline');
    $('#sTab02').css('display', 'none');
    $('#tab01').addClass("current");
    $('#tab02').removeClass("current");
    $('#tab01').css('color', '#006ecb');
    $('#tab02').css('color', '#8E8E8E');
    initRoleTree($('#input[name="su.jobId"]').val());
    $('#btnResetPass').css('display', 'inline');
    /*var arrRight = $('input[name="su.roles"]').val().split(',');
        for(var i=0; i< arrRight.length; i++){
            $('td input[type="checkbox"][id^="chk"][id$="'+arrRight[i].trim()+'"]').prop('checked',true);
        }*/
    formatLayout();

    //$('#spartnerId').val($('#hidPartnerId').val());
}

function reloadCbHanMucQt(hanmucGiaoDich, hanMucNgay) {
    // Truong hop Vietin nhap nsd cho doi tac can rebuil combo suhanMucGiaoDichId suhanMucNgayid
    $.ajax( {
        async : false, type : "POST", url : 'LtdCategory.action?method=getHm&typex=qt&partnerId=' + $('select[name="su.partnerId"]').val(), success : function (res) {
            $('#hanMucGiaoDichChuyenTienQTId').html(res['hanMucGd']);
            $('#hanMucNgayChuyenTienQTId').html(res['hanMucNgay']);
            $('#hanMucGiaoDichChuyenTienQTId').val(hanmucGiaoDich);
            $('#hanMucNgayChuyenTienQTId').val(hanMucNgay);

        },
        error : function (data1) {

        }
    });
}

function reloadCbHanMuc(hanmucGiaoDich, hanMucNgay) {
    // Truong hop Vietin nhap nsd cho doi tac can rebuil combo suhanMucGiaoDichId suhanMucNgayid
    $.ajax( {
        async : false, type : "POST", url : 'LtdCategory.action?method=getHm&partnerId=' + $('select[name="su.partnerId"]').val(), success : function (res) {
            $('#suhanMucGiaoDichId').html(res['hanMucGd']);
            $('#suhanMucNgayid').html(res['hanMucNgay']);
            $('#suhanMucGiaoDichId').val(hanmucGiaoDich);
            $('#suhanMucNgayid').val(hanMucNgay);

        },
        error : function (data1) {

        }
    });
}

function expand(idquyen, pathQuyen) {
    if ($('#tr' + pathQuyen).hasClass('open')) {
        $('#tr' + pathQuyen).removeClass('open');
        $('#tr' + pathQuyen).addClass('close');
        $('#fnt' + pathQuyen).html('(-) ');
    }
    else {
        $('#tr' + pathQuyen).removeClass('close');
        $('#tr' + pathQuyen).addClass('open');
        $('#fnt' + pathQuyen).html('(+) ');
    }
    if ($('#tr' + pathQuyen).hasClass('close'))// Truong hop dong                
        $('tr[id^="tr' + pathQuyen + '"][id!="tr' + pathQuyen + '"]').css('display', 'none');
    else {
        // Truong hop mo, mo de quy
        openRecur(idquyen, pathQuyen);
    }
    formatLayout();
}

function openRecur(idquyen, pathQuyen) {
    if ($('#tr' + pathQuyen).hasClass('close'))
        return;
    $('.' + idquyen).each(function () {
        $(this).css('display', 'table-row');
        if ($(this).hasClass('trparent')) {
            var trId = $(this).attr('id');
            var childIdquyen = trId.split(pathQuyen + '_')[1];
            var childPathQuyen = pathQuyen + '_' + childIdquyen;
            openRecur(childIdquyen, childPathQuyen);
        }
    });
}

function selectRight(idQuyen, pathQuyen, isSelect) {
    $('input[type="checkbox"][id^="chk' + pathQuyen + '"]').prop('checked', isSelect);
}

function beforeSave() {
    /*var rights="";
            $( "td input:checked" ).each(function(){
                rights+= $(this).attr('class') + ",";
            });*/
    var nodes = $('#danhSachQuyen').tree('getChecked', ['checked', 'indeterminate']);
    var rights = '';
    for (var i = 0;i < nodes.length;i++) {
        if (rights != '')
            rights += ',';
        rights += nodes[i].id;
    }
    $('input[name="su.roles"]').val(rights);
}

function extraClear() {
    $('td input[type="checkbox"][id^="chk"]').prop('checked', false);
}


function LoadDeviceSecurity(partnerCode) {
    $.loader( {
        className : "blue-with-image-2"
    });
    $.ajax( {
        url : $('#theForm').attr('action') + '?method=loadDevice&partnerCode=' + partnerCode, method : 'POST', success : function (severtrave) {
            $('input[name="secDiviceCode"]').val(severtrave);
            $('#txtUsername').val($('input[name="su.username"]').val());
            $('#txtName').val($('input[name="su.name"]').val());
            $('#oldPIN').val($('input[name="su.pin"]').val());
            $.loader("close");
        },
        error : function () {
            alert('Kh�ng k?t n?i ???c v?i Server vui l�ng li�n h? v?i qu?n tr? h? th?ng!');
            $.loader("close");
        }
    });
}

function ValidateData() {
    var mess = '<s:text name="required"/>';
    if ($('input[name="su.secDiviceCode"]').val() == '') {
        $('input[name="su.secDiviceCode"]').attr("placeholder", mess);
        return false;
    }
    if ($('input[name="su.pin"]').val() == '') {
        $('input[name="su.pin"]').attr("placeholder", mess);
        return false;
    }
    else {
        if ($('input[name="su.pin"]').val().length != 6) {
            alert('M� PIN ph?i g?m 6 ch? s?.');
            return false;
        }
        else if (!$.isNumeric($('input[name="su.pin"]').val())) {
            alert('M� PIN ph?i g?m 6 ch? s?.');
            return false;
        }
    }
    return true;
}

function changePINRSA() {
    if ($('input[name="su.pin"]').val() == $('#oldPIN').val())
        alert('Vui l�ng thay ??i th�ng tin.');
    else 
        $("#modal_OTP").modal();
}

function changePIN(OTP) {
    var username = $('#txtUsername').val();
    var Pin = $('input[name="su.pin"]').val();;

    $.loader( {
        className : "blue-with-image-2"
    });

    $.ajax( {
        url : $('#theForm').attr('action') + '?method=changePIN&UserID=' + $('#id').val() + '&oldPin=' + $('#oldPIN').val() + '&PIN=' + Pin + '&username=' + username + '&OTP=' + OTP, method : 'POST', success : function (severtrave) {
            if (severtrave == "0") {
                alert(thuc_hien_thanh_cong, function () {
                    $("#modal_OTP").modal('hide');
                });
            }
            else 
                alert(severtrave);

            $.loader("close");
        },
        error : function () {
            alert('Kh�ng x�c th?c ???c m� OTP');
            $.loader("close");
        }
    });
}

function subscriberRSA() {
    $.loader( {
        className : "blue-with-image-2"
    });
    var username = $('#txtUsername').val();
    var name = $('#txtName').val();
    var secDiviceCode = $('input[name="su.secDiviceCode"]').val();
    var Pin = $('input[name="su.pin"]').val();
    var errorMess1 = '<s:text name="PRINCIPAL_USERID_EXISTS_IN_REALM"/>';
    var errorMess2 = '<s:text name="TOKENMGT_TOKEN_ALREADY_ASSIGNED"/>';
    $.ajax( {
        url : $('#theForm').attr('action') + '?method=subscriber&action=0&UserID=' + $('#id').val() + '&username=' + username + '&lastname=' + name + '&secDiviceCode=' + secDiviceCode + '&Pin=' + Pin, method : 'POST', success : function (severtrave) {
            //alert('??ng k� th�nh c�ng.');
            if (severtrave == "OTP") {
                // UAT: change PIN
                //                        $('#modal_OTP').modal();
                // PROD: user ko su dung PIN khi xac thuc RSA
                alert('??ng k� th�nh c�ng.');
            }
            else {
                if (severtrave == 'PRINCIPAL_USERID_EXISTS_IN_REALM')
                    alert(errorMess1);
                else if (severtrave == 'TOKENMGT_TOKEN_ALREADY_ASSIGNED')
                    alert(errorMess2);
                else 
                    alert(severtrave);
            }

            $.loader("close");
        },
        error : function () {
            alert('??ng k� kh�ng th�nh c�ng.');
            $.loader("close");
        }
    });
}

function unsubscriber() {
    $.loader( {
        className : "blue-with-image-2"
    });
    var username = $('#txtUsername').val();
    var name = $('#txtName').val();
    var secDiviceCode = $('input[name="su.secDiviceCode"]').val();
    var Pin = $('input[name="su.pin"]').val();
    var errorMess1 = '<s:text name="PRINCIPAL_USERID_EXISTS_IN_REALM"/>';
    var errorMess2 = '<s:text name="TOKENMGT_TOKEN_ALREADY_ASSIGNED"/>';
    $.ajax( {
        url : $('#theForm').attr('action') + '?method=unsubscriber&action=0&UserID=' + $('#id').val() + '&username=' + username + '&lastname=' + name + '&secDiviceCode=' + secDiviceCode + '&Pin=' + Pin, method : 'POST', success : function (severtrave) {
            if (severtrave == "SUCCESS") {
                alert('H?y ??ng k� th�nh c�ng.');
                findData();
                $('#divGrid').css('display', 'inline');
                $('#divDetail').css('display', 'none');
            }
            else {
                alert(severtrave);
            }
            $.loader("close");
        },
        error : function () {
            alert('L?i kh�ng x�c ??nh.');
            $.loader("close");
        }
    });
}

function subscriber() {
    if (ValidateData()) {
        subscriberRSA();

    }
    else 
        return false;
}



function btnSave_click() {
    $('#id').prop('disabled', false);
    try {
        // Cac thao tac thuc hien truoc khi validate
        beforeValidate();
    }
    catch (err) {
    }
    if (!$('#theForm').valid())
        return;
    try {

        if (!instanceValidate())
            return;

    }
    catch (err) {
    }
    try {
        // Cac thao tac thuc hien truoc khi validate
        beforeSave();
    }
    catch (err) {
    }
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
    $.loader( {
        className : "blue-with-image-2"
    });
    $.ajax( {
        async : false, type : "POST", url : saveUrl, data : $('#theForm').serialize(), success : function (data) {
            $.loader("close");
            try {
                instanceSuccess(data);
                return;
            }
            catch (err1) {
            }
            if (data.trim() != '') {
                // Thuc hien thanh cong thi data=''                 
                alert(data);
            }
            else {
                if ($('#id').val() == '') {
                    $('#sTab02').css('display', 'inline');
                    $('#sTab01').css('display', 'inline');
                    $('#tab02').addClass("current");
                    LoadDeviceSecurity($('select[name="su.partnerId"]').val());
                }
                alert(thuc_hien_thanh_cong, function () {
                    findData();
                    $('#divGrid').css('display', 'inline');
                    $('#divDetail').css('display', 'none');
                });
            }
        },
        error : function (data) {
            $.loader("close");
            alert(data);
        }
    });

}
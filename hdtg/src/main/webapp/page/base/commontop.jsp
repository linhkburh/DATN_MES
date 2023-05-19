<%@page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ page import="frwk.utils.ApplicationContext" %>
 
 <%@ page import="entity.frwk.SysUsers" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%  
ApplicationContext appContext = (ApplicationContext)request.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
SysUsers user = null;
if(appContext!=null)
    user = (SysUsers)appContext.getAttribute(ApplicationContext.USER);
%>
      
<head>
	 <!-- jquery-datatables -->
    <link href="<spring:url value="/plugin/Bootstrap4_jQuery3_DataTables/datatables.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/Bootstrap4_jQuery3_DataTables/datatables.min.js" />"></script>
    
    <link href="<spring:url value="/plugin/Bootstrap4_jQuery3_DataTables/Bootstrap-4-4.1.1/css/bootstrap.min.css"/>" rel="stylesheet" />
  
    
     <link href="<spring:url value="/plugin/jquery-ui-1.12.1.custom/jquery-ui.css"/>" rel="stylesheet" />
     <script src="<spring:url value="/plugin/jquery-ui-1.12.1.custom/jquery-ui.js" />"></script>
     
    
    
    <!-- jQuery_alert -->
    <link href="<spring:url value="/plugin/jQuery_alert/jquery.alerts.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/jQuery_alert/jquery.alerts.js?v=1" />"></script>
    
     <!--jquery.loader-->
    <script src="<spring:url value="/plugin/jquery.loader/jquery.loader.min.js" />"></script>
    
    <!-- select2 -->
    <link href="<spring:url value="/plugin/select2/select2.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/select2/select2.min.js" />"></script>
    
    <!--  -->
    <link href="<spring:url value="/plugin/jquery-easyui/easyui.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/jquery-easyui/jquery.easyui.min.js" />"></script>
    
    
    
     <script src="<spring:url value="/js/jquery.validate.min.js" />"></script>
     <script src="<spring:url value="/js/moment-with-locales.js" />"></script>
     <script src="<spring:url value="/js/jquery-ui-timepicker-addon.js" />"></script>
     <script src="<spring:url value="/js/jquery-ui-timepicker-addon-i18n.js" />"></script>
     <script src="<spring:url value="/js/messages_vi.js" />"></script>
     <link href="<spring:url value="/css/jquery-ui-timepicker-addon.css"/>" rel="stylesheet" />
    <!-- framework -->
    <link href="<spring:url value="/css/styles1.css"/>" rel="stylesheet" />
    <link href="<spring:url value="/css/font-awesome.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/js/main.js?v=9" />"></script>
    <script src="<spring:url value="/js/menu.js?v=7" />"></script>
    <link type="image/x-icon" rel="shortcut icon" href="<spring:url value="/images/favicon.ico" />"/>
	
     <style type="text/css">
        container {
            
            margin-right: 1%;
            margin-left: 1%;
        }
     </style>   
     <script src="<spring:url value="/js/catalog_v2.js?v=2" />"></script>
	<script src="<spring:url value="/js/common.js" />"></script>
	<script src="<spring:url value="/js/dataTables.buttons.min.js" />"></script>
   </head>
   
   <script type="text/javascript" >  
        var thuc_hien_thanh_cong = '<spring:message code="thuc_hien_thanh_cong" />';
        var thuc_hien_khong_thanh_cong = '<spring:message code="common.alert.error.msg" />';
        var not_select = '<spring:message code="common.not.select" />';
        var sure_delete = '<spring:message code="common.sure.delete" />?';
        var succ_del = '<spring:message code="common.success.delete" />';
        var used_del = '<spring:message code="common.data.used" />';
        var not_del = '<spring:message code="common.delete.error" />';
        var ngay_khong_dung = '<spring:message code="common.date.format.fail" />';
        var tu_ngay_khong_duoc_lon_hon_den_ngay = '<spring:message code="common.todate.fromdate.error" />';
        
        
        
        var locale;
        function initControl(){
        	$(".dateTime").datetimepicker({
	       		controlType : 'select',
	       		dateFormat : "dd/mm/yy",
	       		timeFormat : "HH:mm",		
	       		oneLine: true
            });
        	$('.datetime,.date').attr('autocomplete','off');
        	locale = "${pageContext.response.locale}";
        	// Dinh dang so
        	if(locale=='vi'){
        		sep = ".";
        		dec=",";
        	}
            $.timepicker.setDefaults($.timepicker.regional[locale]);
            $.datepicker.setDefaults($.datepicker.regional[locale]); 	
            
            
            // Select 2 and validate
            $( ".toCurrentDate" ).datepicker({ 
                dateFormat: 'dd/mm/yy',
                showButtonPanel: true,
                changeMonth: true,
                changeYear: true,
                yearRange: "-50:+0",
                maxDate:0                
            });     
            $('#divGrid select:not([name="tblSearchResult_length"])').select2().on('change', function() {
                $(this).valid();
            });
            $('#divDetail select:not([name="tblSearchResult_length"])').select2().on('change', function() {
                $(this).valid();
            });
            $('#divGrid select:not([name="tblSearchResult_length"])').select2().on('change', function() {
                $(this).valid();
            });
            $('#divDetail select:not([name="tblSearchResult_length"])').select2().on('change', function() {
                $(this).valid();
            });
            validobj = FnInitValidate($('#theForm'));
            $(document).on("change", ".select2-offscreen", function () {
                if (!$.isEmptyObject(validobj.submitted)) {
                    validobj.form();
                }
            });
            $(document).on("select2-opening", function (arg) {
                var elem = $(arg.target);
                if ($("#s2id_" + elem.attr("id") + " ul").hasClass("myErrorClass")) {
                    $(".select2-drop ul").addClass("myErrorClass");
                } else {
                    $(".select2-drop ul").removeClass("myErrorClass");
                }
            });
            $('.select2-container').css('width','100%');
            
 
            $('.textdigit').removeClass('numberformat');
            $('.textdigit').removeClass('number');
            $('.textdigit').addClass('digits');
            
            
             $(".numberformat, .number").not('.textint').blur(function(){
                return isNumber(this,'d');
            }).keypress(function(){
                return formatNumber(this);
            });
            
            $(".number.textint").removeClass('number');
             $(".textint").blur(function(){
                return isNumber(this,'i');
            }).keypress(function(){
                return formatNumber(this,'i');
            });

            
            initChangePassWordDialogModal();
            
            $( ".date" ).datepicker({ 
                format: 'dd/mm/yy',
                dateFormat: 'dd/mm/yy',
                showButtonPanel: true,
                changeMonth: true,
                changeYear: true,
                yearRange: "-50:+50"
            });
        }
        $(document).ready(function(){
            // tokenId           
            if($('#tokenId').length <=0){
                var tokenIDValue = '${tokenId}';
                $('<input>').attr({
                    type: 'hidden',
                    id: 'tokenId',
                    name: 'tokenId',
                    value: tokenIDValue
                }).appendTo($('#theForm'));
            }
            
            // tokenIdKey    
            if($('#tokenIdKey').length <=0){
                $('<input>').attr({
                    type: 'hidden',
                    id: 'tokenIdKey',
                    name: 'tokenIdKey',
                    value:'${tokenIdKey}'
                }).appendTo($('#theForm'));
            }  
            initControl();
        });
        
        
        function FnInitValidate (oForm) {
            var temp = oForm.validate({
                errorElement: 'div',
                onkeyup: false,
                errorPlacement: function (error, element) {
                  if (element.is('select')){
                    error.addClass('select2Error'); 
                    element.parent().append(error);
                  }                    
                 else {
                    element.css("position", "inherit");
                    error.insertAfter(element);
                 }
                },
                highlight: function (element, errorClass, validClass) {
                    var elem = $(element);
                    if (elem.hasClass("select2-offscreen")) {
                        $("#s2id_" + elem.attr("id") + " ul").addClass(errorClass);
                    } else {
                        elem.addClass(errorClass);
                    }
                },
                unhighlight: function (element, errorClass, validClass) {
                    var elem = $(element);
                    if (elem.hasClass("select2-offscreen")) {
                        $("#s2id_" + elem.attr("id") + " ul").removeClass(errorClass);
                    } else {
                        elem.removeClass(errorClass);
                    }
                }
            });
            return temp;
        }
        function logOut(){
        	window.location='logout';
        }
        function ChangePasswordSave()
        {
        	var newPassWord = decodeURI($('#newPassWord').val());
        	var oldPassWord = decodeURI($('#oldPassWord').val());
            if(validateData()){
                $.ajax({
                    url:$('#frmChangePass').attr('action') + '?method=changepassword',
                    method: 'GET',
                    data: {
                        newPassWord: newPassWord,
                        oldPassWord: oldPassWord
                    },
                    success:function(severtrave){    
                        if(severtrave == "saimatkhau")                                        
                            alert('<sping:message code="Login.changepw.incorrect" />');
                        else if(severtrave!=''){
                            alert(severtrave);   
                             $('#myModal').modal('hide');
                        }
                        else{                                        
                            $('#myModal').modal('hide');
                            alert("<spring:message code="login.change.pw.success" />",function(){
                            	logOut();
                            });
                        }
                    },                                
                    err: function(severtrave){                                    
                        alert('<spring:message code="login.change.pw.error" />');
                    }
                });
            }    
        }
        function initChangePassWordDialogModal(){
            $("#dialog_1").dialog({ autoOpen: false,Width:400,maxWidth:400,minWidth:400,Height:200,maxHeight:200,minHeight:200,modal:true, buttons: {
                Hủy: function() {
                  $("#dialog_1").dialog( "close" );
                    },
                "Lưu": function(){
                    if(validateData()){
                        $.ajax({
                            url:$('#frmChangePass').attr('action') + '?method=changepassword',
                            method: 'GET',
                            data: {
                                newPassWord: $('#newPassWord').val(),
                                oldPassWord: $('#oldPassWord').val()
                            },
                            success:function(severtrave){    
                                if(severtrave == "saimatkhau")                                        
                                    alert('<sping:message code="Login.changepw.incorrect" />');
                                else if(severtrave!=''){
                                    alert(severtrave);
                                    
                                    }
                                else{                                        
                                    alert('<spring:message code="login.change.pw.success" />');
                                    $("#dialog_1").dialog( "close" );
                                }
                            },                                
                            err: function(severtrave){                                    
                                alert('<spring:message code="login.change.pw.error" />');
                            }
                        });
                    }    
                }
              }});
        }
        
        var createTile = false;
        function validateData(){
            var pass = $('#newPassWord').val();
            var confirm = $('#confirmPassWord').val();
            var lblError = $('#lblError');
                    
            if(pass != confirm){
                lblError.text('<spring:message code="login.change.pw.error" />');
                return false;
            }
            else{
                lblError.text("");
                return true;
            }            
        }        
        
        function ShowDialogChangePassWord() {
            $('#dialog_1').dialog('open');
            $('#newPassWord').val('');
            $('#oldPassWord').val('');
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
        function vi2en( alias )
        {
            var str = alias;
            str= str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g,"a");
            str= str.replace(/À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ/g,"A");
            str= str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g,"e");
            str= str.replace(/È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ/g,"E");	
            str= str.replace(/ì|í|ị|ỉ|ĩ/g,"i");
            str= str.replace(/Ì|Í|Ị|Ỉ|Ĩ/g,"I");	
            str= str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g,"o");
            str= str.replace(/Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ/g,"O");	
            str= str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g,"u");
            str= str.replace(/Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ/g,"U");
            str= str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g,"y");
            str= str.replace(/Ỳ|Ý|Ỵ|Ỷ|Ỹ/g,"Y");	
            str= str.replace(/đ/g,"d");
            str= str.replace(/Đ/g,"D");
            str= str.replace(/[`~!@$%^*|"\{\}\[\]\?\<\>]/gi,"");
            return str;
        }
        
        function checkSpecialChar( alias )
        {
            var str = alias;
            str= str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g,"a");
            str= str.replace(/À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ/g,"A");
            str= str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g,"e");
            str= str.replace(/È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ/g,"E");	
            str= str.replace(/ì|í|ị|ỉ|ĩ/g,"i");
            str= str.replace(/Ì|Í|Ị|Ỉ|Ĩ/g,"I");	
            str= str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g,"o");
            str= str.replace(/Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ/g,"O");	
            str= str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g,"u");
            str= str.replace(/Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ/g,"U");
            str= str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g,"y");
            str= str.replace(/Ỳ|Ý|Ỵ|Ỷ|Ỹ/g,"Y");	
            str= str.replace(/đ/g,"d");
            str= str.replace(/Đ/g,"D");
            str= str.replace(/[\<\!\&\|\$\*\;\^\%\_\>\`\#\@\=\"\~\[\]\{\}\\]/gi,"");
            if(str != alias)
                return false;
            else
                return true;
        }
        
        function vi2enforFR( alias )
        {
            var str = alias;
            str= str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g,"a");
            str= str.replace(/À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ/g,"A");
            str= str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g,"e");
            str= str.replace(/È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ/g,"E");	
            str= str.replace(/ì|í|ị|ỉ|ĩ/g,"i");
            str= str.replace(/Ì|Í|Ị|Ỉ|Ĩ/g,"I");	
            str= str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g,"o");
            str= str.replace(/Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ/g,"O");	
            str= str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g,"u");
            str= str.replace(/Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ/g,"U");
            str= str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g,"y");
            str= str.replace(/Ỳ|Ý|Ỵ|Ỷ|Ỹ/g,"Y");	
            str= str.replace(/đ/g,"d");
            str= str.replace(/Đ/g,"D");
            str= str.replace(/[`~!@$%^*|"\{\}\[\]\?\<\>\#\&\_\=\;\'\\]/gi,"");
            return str;
        }
        // ntdung dat lai thoi gian tuong duong voi session timout: 10 phut
        var checkTimer = setInterval(checkSession, 600000);
        function checkSession(){
            $.get( "<%= request.getContextPath() %>/common?method=checkSession", function( data ) {
                if(data == '0'){
                    alert('<s:text name="phien_lam_viec_da_ket_thuc" />',window.location.href='<%= request.getContextPath() %>/login.action?request_locale=vi');
                }
            });
        }
        
        
   </script>
   
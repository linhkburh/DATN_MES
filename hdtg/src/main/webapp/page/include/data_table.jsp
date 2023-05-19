<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test='${customDivDataTable!="true"}'>
	<div id="divDatatable" class="table-responsive">
		<table id="tblSearchResult" class="table">
		</table>
	</div>
</c:if>

<script type="text/javascript">
	var divDatatablezize;
	var datatableUrl, tblData, bScrollX = false, bFixedColumns = 3, bFirstSearch=true;
	$(document).ready(function(){
          divDatatablezize = $('#divDatatable').width();
          if (typeof initParam == 'function')
    		  initParam(tblCfg);
    	  else{
    		  alert('Chưa định nghĩa hàm initParam(tblCfg)');
    		  return
    	  } 
          if(tblCfg.notSearchWhenStart)
              return
          findData(); 
	});
	var tblCfg = {pageSize:10,aoColumns:'',notSearchWhenStart:false, bFilter:true, approveInMain:false, buttons:[ 
    	{ text: '&#xf067; Thêm mới',attr:  {id:'btnDtAdd'}, className: 'mainGrid btnDtAdd btn blue fa', action: function ( e, dt, node, config ) { addNew(); } }
    ]};
	var jQuerySearchSelector;
	function findData(){         
            if (typeof validateSearch == 'function')
                validateSearch();
             if (typeof beforeSearch == 'function')
                beforeSearch();
             if (typeof beforeLoad == 'function') {
            	 beforeLoad();
            	 //return;
             }
            if(!bFirstSearch){
                if(tblCfg.bFilter!=undefined && tblCfg.bFilter)
                    tblData.fnFilter($('.dataTables_filter input[type="search"]').val().trim());
                else
                    tblData.fnFilter();
                
                return;
            }
            bFirstSearch = false;
			tblData = $('#tblSearchResult').dataTable({
				"bJQueryUI" : true,
				dom: 'B<"clear">lf' + "t<'row'<'col-sm-5'i><'col-sm-7'p>>",
				
				//"dom": '<lf<t>>' + "<'row'<'col-sm-5'i><'col-sm-7'p>>",
				//"dom": "<'row'<'col-sm-5'l><'col-sm-7'f>>" + '<<t>>' + "<'row'<'col-sm-5'i><'col-sm-7'p>>",
				//dom: "<'row'<'col-sm-3'l><'col-sm-5'f><'col-sm-4'B<'clear'>>>" + "t<'row'<'col-sm-5'i><'col-sm-7'p>>",
				 buttons: {
			        name: 'primary',
			        buttons: tblCfg.buttons
			    }
				,
				"sPaginationType" : "full_numbers",
				"iDisplayLength" : tblCfg.pageSize,
				"bProcessing" : false,
	            "bFilter":tblCfg.bFilter,
				"bServerSide" : true,	
				"sAjaxSource" : datatableUrl,
				"fnServerData": function ( sSource, aoData, fnCallback ) {
	                  addFormData(aoData,jQuerySearchSelector==undefined? $('#theForm').serializeObject():jQuerySearchSelector.serializeObject());
	                  $("#some-element").hide();
	                  $.ajax( {
	                          "dataType": 'json',
	                          "type": "POST",
	                          "url": sSource,
	                          "data": aoData,
	                          "success": function(result){
	                              fnCallback(result);
	                              if (typeof instanceUseResult == 'function')
	                                  instanceUseResult(result);
	                          }, error : function(xhr, textStatus, errorThrown){
	                              alert(xhr.responseText);
	                          }
	
	                  } );
		                  
		          },
	             
	          	"initComplete": function(settings, json) {      
		          	if (typeof instanceFindComplete == 'function')
		          		instanceFindComplete(tblData.fnGetData().length);
	              
	            },
                          
				"aoColumns" : tblCfg.aoColumns,
				"oLanguage":{
		            "sLengthMenu" : "<spring:message code="datatable.display"/>" + "_MENU_" + "<spring:message code="datatable.record"/>",
		            "sZeroRecords" : " ",
		            "sInfo" : "<spring:message code="datatable.show"/>" + " _START_ " + "<spring:message code="datatable.to"/>" + " _END_ " + "<spring:message code="datatable.of"/>" + " _TOTAL_ " + "<spring:message code="datatable.record"/>",
		            "sInfoEmpty" : "<spring:message code="datatable.show"/>" + " " + "<spring:message code="datatable.from"/>" + " 0 " + "<spring:message code="datatable.to"/>" + " 0 " + "<spring:message code="datatable.total.record"/>" + " 0 " + "<spring:message code="datatable.record"/>",
		            "sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
		            "oPaginate":{
		                "sFirst" :"<i class='fa fa-fast-backward'></i>",
		                "sLast" :"<i class='fa fa-fast-forward'></i>",
		                "sPrevious":"<i class='fa fa-backward'></i>",
		                "sNext" : "<i class='fa fa-forward'></i>"
	            },
	            "sSearch":"<spring:message code="datatable.search"/>"
		        },
				"bDestroy" : false,
//                      "bDestroy" : true,
                        scrollX: bScrollX,
                        //scrollY:        false,
//                        "scrollX": true,
//                        "ScrollCollapse": true,
//                        paging:         true,
                 "fnDrawCallback": function( oSettings ) {
                    if(typeof fnDrawCallback != typeof undefined){
                        fnDrawCallback(oSettings);
                    }
                 }
		});
		
		$('.dataTables_filter input')
		    .unbind() // Unbind previous default bindings
		    .bind('keypress keyup', function(e){
		      if(e.keyCode == 13)      
		      	tblData.fnFilter($(this).val().trim());		 
	    });
		if(tblCfg.bFilter)
             $('.dataTables_filter').append('<a href="#!" class="btn blue" onclick="findData();"><i class="fa fa-search"></i></a>');
         
	    	    
	    $('#tblSearchResult tbody').on( 'click', 'tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	            tblData.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        }
	    } );
	 
	     $('#button').click( function () {
	        tblData.row('.selected').remove().draw( false );
	    } ); 
         if(bScrollX)
             $("#divDatatable").width($("#divGrid").width());      
            
	}
        
        
        
</script>
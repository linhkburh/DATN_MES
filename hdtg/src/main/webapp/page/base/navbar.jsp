<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="frwk.utils.ApplicationContext" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@ page import="entity.frwk.SysUsers"%>
<nav class="navbar navbar-expand-lg navbar-light bg-light" style="width: 100%">
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
<div class="navbar-collapse collapse navbar-inverse-collapse">
  	<ul class="nav navbar-nav" id="navbar-nav">
    </ul>
  </div>
</nav>
<script lang="javascript" type="text/javascript">
	$(document).ready(function(){
		var vitualRootMenuJson = '${menu}';
		var locale = "${pageContext.response.locale}";
		menuAll(locale, vitualRootMenuJson);
	});
	function downloadTemplate() {
		window.open(
				'item?method=download&quotationId=' + $("#quotationId").val())
				.focus();
	}
	function menu(){
        var last_menu = null;    
        $(".nav li a.dropdown-toggle").click(function(){
            //$(".nav .open").removeClass("open");
            $(this).parent().toggleClass("open");
        })
        
        $('.dropdown-submenu a.subdrop').on("mouseenter", function() {
            $(this).closest('ul').show();
            $(this).closest('ul').find('ul').hide();
            $(this).next('ul').toggle();    
            last_menu = $(this).next('ul');
//            $(this).next('ul').toggle();
//            e.stopPropagation();
//            e.preventDefault();
        });
       
        
    }
</script>





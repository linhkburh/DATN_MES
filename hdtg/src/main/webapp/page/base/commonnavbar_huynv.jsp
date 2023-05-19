    <%@page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="frwk.utils.ApplicationContext" %>
 
 <%@ page import="entity.frwk.SysUsers" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>

<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>

<style>
.dropdown-submenu {
    position: relative;
}

.dropdown-submenu .dropdown-menu {
    top: 0;
    left: 100%;
    margin-top: -1px;
}
a.subdrop{
    padding: 10px !important;
    font-size: 14px !important;
    font-weight: bold !important;
    color: #005691 !important;

}
.inputModal{
    vertical-align: middle;
    width: 100%;
    color: #214b85;
    padding-left: 6px;
    padding-right: 6px;
    margin-top: -6px;
    height: 35px;
    border: 1px solid #95B8E7;
    border-radius: 5px;
}

</style>

<%  
    ApplicationContext appContext = (ApplicationContext)request.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
    SysUsers user = null;
    if(appContext!=null)
     user = (SysUsers)appContext.getAttribute(ApplicationContext.USER);
    WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request,null);
    SysParamDao sysParamDao = (SysParamDao) ac.getBean("sysParamDao");
    entity.frwk.SysParam LDAP_AUTHEN =  sysParamDao.getSysParamByCode("LDAP_AUTHEN");
    boolean ldapAuthen = LDAP_AUTHEN!=null && "true".equalsIgnoreCase(LDAP_AUTHEN.getValue());
%>
<div class="navbar-inverse" id="navbar" style="height: auto;    padding-bottom: -0px;">
      	<ul class="nav navbar-nav" id="navbar-nav" style="display: none;flex-direction: inherit;">
      		<li class="active"><a class="no-caret" href="/cic/login" style="background-color: transparent;">Trang chủ</a></li>
      		<li class=""><a class="no-caret" href="/cic/right" style="background-color: transparent;">Danh sách quyền</a></li>
      		<li class=""><a class="no-caret" href="/cic/role" style="background-color: transparent;">Danh sách nhóm quyền</a></li>
      		<li class=""><a class="no-caret" href="/cic/param" style="background-color: transparent;">Tham số hệ thống</a></li>
      		<li class=""><a class="no-caret" href="/cic/audit" style="background-color: transparent;">Danh sách Audit hệ thống</a></li>
      		<li class=""><a class="no-caret" href="/cic/qnaInMaster" style="background-color: transparent;">Theo dõi hỏi tin</a></li>
      		<li class=""><a class="no-caret" href="/cic/manageUser" style="background-color: transparent;">Người dùng</a></li>
      		<li class=""><a class="no-caret" href="/cic/partner" style="background-color: transparent;">Danh mục Đối tác</a></li>
      		<li class=""><a class="no-caret" href="/cic/entParIdx" style="background-color: transparent;">Danh mục Chỉ tiêu</a></li>
      		<li class=""><a class="no-caret" href="/cic/catProduct" style="background-color: transparent;">Tạo lập sản phẩm</a></li>
      	</ul>  
</div>


<script lang="javascript" type="text/javascript">
    
    $(document).ready(function(){
        function dequydaucadaumenungang(control){
                control.children('li').each(function(){
                    var aChildren = $(this).children('a');
                    if(aChildren.length > 0 && aChildren.attr('href') == '#'){	
                        if($(this).children('ul').length > 0){
                            var ulChildren = $(this).children('ul');
                            if(ulChildren.children('li').length == 0){
                                $(this).remove();
                                dequydaucadaumenungang(control.parent().parent());
                            }
                            else{
                                dequydaucadaumenungang(ulChildren);
                            }
                        }
                        else{
                            $(this).remove();
                        }
                    }
                });
            }            
            dequydaucadaumenungang($('#navbar-nav'));
            $('#navbar-nav').css('display', '');
            
            $('#navbar-nav > li > ul').each(function(){
                var parentWidth = $(this).parent('li').width();
                var minWidth = $(this).width();
                parentWidth = parentWidth < minWidth ? minWidth : parentWidth;
                $(this).css('width', parentWidth);
                $(this).css('min-width', parentWidth);
            });
    });
    </script>
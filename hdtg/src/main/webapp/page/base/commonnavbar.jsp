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
<div class="navbar navbar-inverse" id="navbar">
    <div class="container-fluid">
      <div class="navbar-header">
         <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-inverse-collapse">
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         </button>         
      </div>
      
        <div class="navbar-collapse collapse navbar-inverse-collapse">
         <ul class="nav navbar-nav" id="navbar-nav" style="display: none;">
             <%if(appContext!=null){%>
            <li class="active"><a class="no-caret" href="home" style="background-color: transparent;"><i class="fa fa-home"></i>Trang chủ</a></li>
            
            <!--Quản trị hệ thống-->
            <li>
                <a  href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user-secret"></i>Quản trị hệ thống</a>
                
                <ul class="dropdown-menu multi-level">
                       
                <li class="dropdown-submenu mnfunction">
                    <a class="subdrop" href="#">Quản trị hệ thống</a>
                    <ul class="dropdown-menu">
                      				<li class="mnfunction"><a  href="companyCat.action?type=branch" class="sub-menu" >Danh mục chi nhánh</a>
                                     </li>
                    
                                    <li class="mnfunction"><a  href="manageUser" class="sub-menu" >Quản lý người dùng></a>
                                    </li>
                                    <li class="mnfunction"><a  class="sub-menu"
                                                href="right">Quản lý quyền</a>
                                    </li>
                                     <li class="mnfunction"><a  class="sub-menu"
                                                href="role">Quản lý nhóm quyền</a>
                                    </li>
                                   
                                     <li class="mnfunction"><a  class="sub-menu"
                                            href="sysParam">Tham số hệ thống</a>
                                    </li>
                                    <li class="mnfunction"><a class="sub-menu"
                                            href="userlog">Giám sát truy cập</a>
                                    </li>
                                     <li class="mnfunction"><a class="sub-menu"
                                            href="sysDictType">Danh mục loại từ điển</a>
                                    </li>
                                     <li class="mnfunction"><a class="sub-menu"
                                            href="sysDictParam">Từ điển hệ thống</a>
                                    </li>
                                    
                      
                    </ul>
                </li>     
                            
                <li class="dropdown-submenu mnfunction">
                    <a class="subdrop" href="#">Quản trị ứng dụng</a>
                    <ul class="dropdown-menu">
                     
                        <li class="mnfunction"><a  class="sub-menu"
                                    href="sysProduct">Danh mục sản phẩm</a>
                            </li>
                       
                              
                    </ul>
                </li>        
                </ul>
            </li>
            <!--End Quản trị hệ thống-->
            
            
            
            
            
                <li>
                     <a  href="#" class="dropdown-toggle" data-toggle="dropdown">Báo cáo</a>
                     <ul class="dropdown-menu multi-level">
                       
                <li class="dropdown-submenu mnfunction">
                    <li class="mnfunction"><a  href="index.action" 
                        class="sub-menu no-caret" >Quản lý Danh sách khách hàng</a>
                    </li>
                    
                        
                    <li class="mnfunction"><a  href="managerProfile.action?prodGrpId=1" 
                            class="sub-menu no-caret" >Hợp đồng tín dụng</a>
                        </li>                 
                   
                </ul>
                </li>    
              
                </ul>
                     </li>
               
                
           
            
           
                 <li>
                    <a  href="#" class="dropdown-toggle" data-toggle="dropdown">Hỏi tiin</a>
                    
                    <ul class="dropdown-menu multi-level">
                    <li class="mnfunction"><a  class="sub-menu"
                            href="printfBalance.action?prodId=11">Xác nhận số dư</a>
                    </li>
                    
                    <li class="mnfunction"><a  class="sub-menu"
                            href="balanceHis.action?prodId=11">Lịch sử xác nhận số dư</a>
                    </li>
               </ul>
               </li>
               
            
                      
                  <%}%> 
                  
            
            
            
             
            </li>
            
         </ul>
         <ul class="nav navbar-nav account">
            <%-- <%if(!ldapAuthen && (user!=null && user.getExpireDay() > 0)){%> --%>
                 <li class="open">
                    <a class="warning dropdown-toggle no-caret" data-toggle="dropdown" href="#"><sub><i class="fa fa-warning"></i></sub><span>1</span></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a class="sub-menu" href="#" onclick="if($(window).width() < 740){$('.navbar-toggle').click();}$('#modalChangePass').modal();">
                                <%= String.format("Mật khẩu hết hạn %s", user.getExpireDay()) %></br>
                                <i><s:text name="change_now"/></i>
                            </a>
                        </li>
                    </ul>
                </li>
            <%-- <%}%>
            <%if(!ldapAuthen && appContext!=null){%> --%>
                    <li>
                        <a class="no-caret" href="#" onclick="if($(window).width() < 740){$('.navbar-toggle').click();}$('#modalChangePass').modal();"><i class="fa fa-expeditedssl"></i><s:text name = "change_pass"/></a>
                    </li>
                <%--  <%}%> --%>
            
            <li>
                 <%if(appContext!=null){%>
                    <a class="no-caret" href="logout.action?method=logout">Thoát<i class="fa fa-sign-out"></i></a>
                 <%}else{%>
                     <a class="no-caret" href="login.action?method=login">Đăng nhập<i class="fa fa-sign-in"></i></a>
                 <%}%>
                
            </li>
         </ul>
      </div>
      
      
      
    </div>   
</div>
<div class="container-fluid">
    <span id="sub-nav" class="sub-nav-open"  title="Show Menu">Show Menu</span>
    <%if(user!=null){%>
        <div class="user-box">        
            <p>
            	<%-- <strong style="color:#c52821;"><form:label path="lblUser"><%=user.getUsername()%></form:label></strong>| <span style="color:#0077bc;"><s:label name="lblPatner"><%=user.getCompany().getName()%></s:label></span>--%>
                
            </p>
        </div> 
    <%}%>
    
</div> <%--    
<%
 
if(!ldapAuthen){%> --%>
    <div id="modalChangePass" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title"><s:text name = "change_pass"/></h4>
      </div>
      <div class="modal-body">
        <form action='login.action' id='frmChangePass'>
            <div class='Table'>
                <div class='Row' style="height: 40px;">
                    <div class='col-md-4 col-lg-4 col-sm-12 col-xs-12' style="height: 40px;"><s:text name = "old_pass"/></div>
                    <div class='col-md-6 col-lg-6 col-sm-12 col-xs-12' style="height: 40px;"><input id='oldPassWord' type='password' style='width:100%;' class="inputModal"/></div>
                </div>  
                <div class='Row' style="height: 40px;">
                    <div class='col-md-4 col-lg-4 col-sm-12 col-xs-12'  style="height: 40px;"><s:text name = "new_pass"/></div>
                    <div class='col-md-6 col-lg-6 col-sm-12 col-xs-12' style="height: 40px;"><input id='newPassWord' style='width:100%;' type='password' class="inputModal"/></div>
                </div>
                 <div class='Row' style="height: 40px;">
                     <div class='col-md-4 col-lg-4 col-sm-12 col-xs-12'  style="height: 40px;"><s:text name = "confirm_pass"/></div>
                     <div class='col-md-6 col-lg-6 col-sm-12 col-xs-12' style="height: 40px;"><input id='confirmPassWord' style='width:100%;' type='password' class="inputModal"/></div>
                 </div> 
                 <!--div class='Row'>
                     <div class='SpanCell'><label id='lblError' style='color: red;'></label></div>
                 </div-->
             </div>
         </form>
      </div>
      <div class="modal-footer">        
        <button type="button" class="btn blue" onclick="ChangePasswordSave();">&#xf0c7; <s:text name = "save"/></button>
        <button type="button" class="btn default" data-dismiss="modal">&#xf08b; <s:text name = "exit"/></button>
      </div>
    </div>
  </div>
</div>
 
 <%-- <%}
%> --%>

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
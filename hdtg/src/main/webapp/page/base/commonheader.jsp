<%@page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="frwk.utils.ApplicationContext" %>
 
 <%@ page import="entity.frwk.SysUsers" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>

   <title><tiles:getAsString name="title" /></title>
   <jsp:include page="/page/base/commontop.jsp"/> 
   <c:if test='${param.hiddenMenu!="true"}'>
   		<jsp:include page="/page/base/banner.jsp"/>
   </c:if>
   
         
         
         
   
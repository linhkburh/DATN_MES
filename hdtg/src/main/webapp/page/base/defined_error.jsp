
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link type="image/x-icon" rel="shortcut icon" href="<spring:url value='/images/favicon.ico' />" />

<jsp:include page="/page/base/commontop.jsp"/>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
   <title>Lỗi thực hiện</title>
   
  
  <script language="javascript" type="text/javascript">   
  $(document).ready(function(){
    alert('${errorMsg}', function(){
        if(history.length ==1) window.close(); 
        else history.back();
    })
    
  })
        
    </script>

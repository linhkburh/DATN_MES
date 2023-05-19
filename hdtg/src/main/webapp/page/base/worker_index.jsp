<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<style>
body {
	font-family: Arial;
	margin: 0;
}

* {
	box-sizing: border-box;
}

img {
	vertical-align: middle;
}

/* Position the image container (needed to position the left and right arrows) */
.container {
	position: relative;
}

/* Hide the images by default */
.mySlides {
	display: none;
}

/* Add a pointer when hovering over the thumbnail images */
.cursor {
	cursor: pointer;
}

/* Next & previous buttons */
.prev, .next {
	cursor: pointer;
	position: absolute;
	top: 40%;
	width: auto;
	padding: 16px;
	margin-top: -50px;
	color: white;
	font-weight: bold;
	font-size: 20px;
	border-radius: 0 3px 3px 0;
	user-select: none;
	-webkit-user-select: none;
}

/* Position the "next button" to the right */
.next {
	right: 0;
	border-radius: 3px 0 0 3px;
}

/* On hover, add a black background color with a little bit see-through */
.prev:hover, .next:hover {
	background-color: rgba(0, 0, 0, 0.8);
}

/* Number text (1/3 etc) */
.numbertext {
	font-size: 12px;
	padding: 8px 12px;
	position: absolute;
	top: 0;
}

/* Container for image text */
.caption-container {
	text-align: center;
	background-color: #222;
	padding: 2px 16px;
	color: white;
}

.row:after {
	content: "";
	display: table;
	clear: both;
}

/* Six columns side by side */
.column {
	float: left;
	width: 16.66%;
}

/* Add a transparency effect for thumnbail images */
.demo {
	opacity: 0.6;
}

.active, .demo:hover {
	opacity: 1;
}
</style>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title" value="" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/workerIndex" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<div class="container">
				<div class="mySlides" style="text-align: center;">
					<img src="images/anh_0.jpg" style="width: 100%">
				</div>

				<div class="mySlides" style="text-align: center;">
					<img src="images/anh_1.jpg" style="width: 100%">
				</div>

				<div class="mySlides" style="text-align: center;">
					<img src="images/anh_2.jpg" style="width: 100%">
				</div>

				<div class="mySlides" style="text-align: center;">
					<img src="images/anh_3.jpg" style="width: 46%">
				</div>

				<div class="mySlides" style="text-align: center;">
					<img src="images/anh_4.jpg" style="width: 46%">
				</div>

				<div class="mySlides" style="text-align: center;">
					<img src="images/anh_5.jpg" style="width: 46%">
				</div>

				<a class="prev" onclick="plusSlides(-1)">❮</a> <a id="next" class="next" onclick="plusSlides(1)">❯</a>

				<div class="caption-container">
					<p id="caption"></p>
				</div>

				<div class="row">
					<div class="column" style="text-align: center;">
						<img class="demo cursor" src="images/anh_0.jpg" style="width: 100%" onclick="currentSlide(1)"
							alt="Nhà máy thăng long">
					</div>
					<div class="column" style="text-align: center;">
						<img class="demo cursor" src="images/anh_1.jpg" style="width: 100%" onclick="currentSlide(2)" alt="Gia công">
					</div>
					<div class="column" style="text-align: center;">
						<img class="demo cursor" src="images/anh_2.jpg" style="width: 100%" onclick="currentSlide(3)"
							alt="Nhà sản xuất phụ trợ trọn gói">
					</div>
					<div class="column" style="text-align: center;">
						<img class="demo cursor" src="images/anh_3.jpg" style="width: 46%" onclick="currentSlide(4)" alt="Gia công CNC">
					</div>
					<div class="column" style="text-align: center;">
						<img class="demo cursor" src="images/anh_4.jpg" style="width: 46%" onclick="currentSlide(5)"
							alt="Từ ép phun nhựa đến lắp ráp">
					</div>
					<div class="column" style="text-align: center;">
						<img class="demo cursor" src="images/anh_5.jpg" style="width: 46%" onclick="currentSlide(6)"
							alt="Dịch vụ Chế tạo Khuôn">
					</div>
				</div>
			</div>

		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script src="<spring:url value="/js/highcharts.js" />"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$('.HeaderText').remove();
				slideIndex = 3;
				showSlides(slideIndex);

				// Start autoplaying automatically
				autoplayInterval = setInterval(function() {

					// Get element via id and click next
					document.getElementById("next").click();

				}, 10000); // Do this every 1 second, increase this!
			});
			

			// Stop function added to button
			function stopAutoplay() {

				// Stop the autoplay
				clearInterval(autoplayInterval);

			}
			function plusSlides(n) {
				showSlides(slideIndex += n);
			}

			function currentSlide(n) {
				showSlides(slideIndex = n);
			}

			function showSlides(n) {
				let i;
				let slides = document.getElementsByClassName("mySlides");
				let dots = document.getElementsByClassName("demo");
				let captionText = document.getElementById("caption");
				if (n > slides.length) {
					slideIndex = 1
				}
				if (n < 1) {
					slideIndex = slides.length
				}
				for (i = 0; i < slides.length; i++) {
					slides[i].style.display = "none";
				}
				for (i = 0; i < dots.length; i++) {
					dots[i].className = dots[i].className.replace(" active", "");
				}
				slides[slideIndex - 1].style.display = "block";
				dots[slideIndex - 1].className += " active";
				captionText.innerHTML = dots[slideIndex - 1].alt;
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
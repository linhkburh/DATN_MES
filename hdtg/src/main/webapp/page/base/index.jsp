<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title" value="Dashboard" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/index" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<div class="Table" id="divSearchInf">
				<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty thành viên</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="companyId" onchange="changeCompany();">
									<form:option value="">- Chọn -</form:option>
									<c:forEach items="#{companies}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.code}" />-<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
							</form:select>
						</div>
					</div>
			</div>
			<div class="Table">
				<div class="Header2">Trạng thái bản vẽ</div>
				<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày tạo, từ ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input onchange="findByDate();" path="fromDate" class="date" id="fromDate"/>
						</div>
						<div class="col-md-2 col-lg-2"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input onchange="findByDate();" path="toDate" class="date" id="toDate"/>
						</div>
					</div>
				<div class="Row nq pie">
					<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
						<figure class="highcharts-figure">
							<div id="nqpieOrder"></div>
						</figure>
					</div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<figure class="highcharts-figure">
							<div id="gnqpieOder"></div>
						</figure>
					</div>
				</div>

				<div class="Header3">Tiến độ sản xuất</div>
				<div class="Row nq pie">
					<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
						<figure class="highcharts-figure">
							<div id="nqpieWork"></div>
						</figure>
					</div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<figure class="highcharts-figure">
							<div id="gnqpieWork"></div>
						</figure>
					</div>
				</div>
				<div class="Header2" style="display: none;">Tỷ lệ NG, Số lượng đơn hàng</div>
				<div class="Row" style="display: none;">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại báo cáo</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select onchange="changeReportType();" path="reportType">
							<form:option value="d">Ngày</form:option>
							<form:option value="w">Tuần</form:option>
							<form:option value="m">Tháng</form:option>
							<form:option value="q">Quý</form:option>
							<form:option value="y">Năm</form:option>
						</form:select>
					</div>
				</div>
				<div class="Header3" style="display: none;">Tỷ lệ OK/NG/Hủy</div>
				<div class="Row" style="display: none;">
					<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
						<figure class="highcharts-figure">
							<div id="stacked"></div>
						</figure>
					</div>
				</div>
				<div class="Header3">Số lượng đơn hàng-báo giá</div>
				
				<div class="Row nq pie">
					<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
						<figure class="highcharts-figure">
							<div id="line"></div>
						</figure>
					</div>
				</div>
				
			</div>
			<input type="hidden" id="strChart" value='${jsonString}' />
			<input type="hidden" id="strChartLine" value='${jsonMapLine}' />
			<input type="hidden" id="expriredDay" name="expriredDay" value="${expriredDay}" />
		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script src="<spring:url value="/js/highcharts.js" />"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('select[name="companyId"]').select2();
				$('select[name="reportType"]').select2();
				var expriredDay = $('#expriredDay').val();
				if (expriredDay != "") {
					alert("Thời gian hiệu lực còn lại của mật khẩu là " + expriredDay + " ngày!");
				}
				var jsonChart = JSON.parse($("#strChart").val());
				var jsonChartLine = JSON.parse($('#strChartLine').val());
				dashBoard(jsonChart, jsonChartLine);
				changeReportType();
			});
			function findByDate(val) {
				ajaxParam($('input[name="fromDate"]').val(), $('input[name="toDate"]').val(), $('select[name="companyId"]').val());
			}

			function dashBoard(jsonChart, jsonChartLine) {
				var dataChartOrder = [];
				var dataChartWork = [];
				for (var i = 0; i < jsonChart.orderStatus.length; i++) {
					var color = '';
					if ('Còn Thời Hạn' === jsonChart.orderStatus[i][0]) {
						color = '#f7a35c';
					} else if ('Chưa Thực Hiện' === jsonChart.orderStatus[i][0]) {
						color = '#333333';
					} else if ('Hoàn Thành' === jsonChart.orderStatus[i][0]) {
						color = '#90ed7d';
					} else if ('Hết Thời Hạn' === jsonChart.orderStatus[i][0]) {
						color = 'red';
					}else if("Còn Thời Hạn (chậm)"===jsonChart.orderStatus[i][0]){
						color = 'pink';
					}
					dataChartOrder.push({
						name : jsonChart.orderStatus[i][0],
						y : parseInt(jsonChart.orderStatus[i][1]),
						color : color
					});
				}
				for (var i = 0; i < jsonChart.workStatus.length; i++) {
					var color = '';
					if ('Chậm Tiến Độ' === jsonChart.workStatus[i][0]) {
						color = 'red';
					} else if ('Đúng Tiến Độ' === jsonChart.workStatus[i][0]) {
						color = '#f2d918';
					} else if ('Vượt Tiến Độ' === jsonChart.workStatus[i][0]) {
						color = '#90ed7d';
					}
					dataChartWork.push({
						name : jsonChart.workStatus[i][0],
						y : parseInt(jsonChart.workStatus[i][1]),
						color : color
					});
				}
				designChart(dataChartOrder, 'nqpieOrder');
				designChart(dataChartWork, 'nqpieWork');
				axes(dataChartOrder, 'gnqpieOder');
				axes(dataChartWork, 'gnqpieWork');
				if (jsonChartLine != undefined & jsonChartLine != null & jsonChartLine != '') {
					var dataChartLine = [];
					var dataCategories = [];
					for ( var name in jsonChartLine.line) {
						if (name === 'Thời gian') {
							for (var i = 0; i < jsonChartLine.line[name].length; i++) {
								dataCategories.push(jsonChartLine.line[name][i]);
							}
						} else {
							dataChartLine.push({
								name : name,
								data : jsonChartLine.line[name]
							});
						}

					}
					line(dataChartLine, dataCategories, 'line');
				}
			}

			function ajaxParam(fromDate, toDate, companyIdVal) {
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadFilter',
					data : {
						fromDate : fromDate,
						toDate : toDate,
						companyId: companyIdVal
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null && _result != "" & _result.size != 0) {
							/* jsonChart = JSON.parse(_result.workStatus); */
							dashBoard(_result);
							changeReportType();
						}
					}
				});
			}

			function axes(dataChart, divId) {
				Highcharts.chart(divId, {
					chart : {
						type : 'column'
					},
					title : {
						align : 'left',
						text : ''
					},
					subtitle : {
						align : 'left',
						text : ''
					},
					accessibility : {
						announceNewData : {
							enabled : true
						}
					},
					xAxis : {
						type : 'category'
					},
					yAxis : {
						title : {
							text : 'Số lượng'
						}

					},
					legend : {
						enabled : false
					},
					plotOptions : {
						series : {
							borderWidth : 0,
							dataLabels : {
								enabled : true,
								format : '{point.y:f}'
							},
							point : {
								events : {
									click : function() {
										console.log(this.name);
									}
								}
							}
						}
					},

					tooltip : {
						headerFormat : '<span style="font-size:11px"></span><br>',
						pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:f}</b>'
					},

					series : [ {
						data : dataChart
					} ]
				});

			}

			function designChart(dataChart, divId) {
				Highcharts.chart(divId, {
					chart : {
						plotBackgroundColor : null,
						plotBorderWidth : null,
						plotShadow : false,
						type : 'pie'
					},
					title : {
						text : ''
					},
					tooltip : {
						pointFormat : '{series.name}: <b>{point.y}</b>'
					},
					credits : {
						enabled : false
					},
					exporting : {
						enabled : false
					},
					legend : {
						enabled : true
					},
					accessibility : {
						point : {
							valueSuffix : ''
						}
					},
					plotOptions : {
						pie : {
							allowPointSelect : true,
							cursor : 'pointer',
							startAngle : 45,
							dataLabels : {
								enabled : false,
								format : '<b>{point.name}</b>: - {y}'
							},
							showInLegend : true
						},
						series : {
							point : {
								events : {
									click : function() {
										console.log(this.name);
									}
								}
							}
						}
					},
					series : [ {
						name : 'S\u1ED1 l\u01B0\u1EE3ng',
						colorByPoint : true,
						turboThreshold : 20,
						data : dataChart
					} ]
				});
			}
			
			function changeCompany() {
				var compnayId = $('select[name="companyId"]').val();
				var fromDate = $('input[name="fromDate"]').val();
				var toDate = $('input[name="toDate"]').val();
				ajaxParam(fromDate, toDate, compnayId);
			}

			function changeReportType() {
				var reportType = $('select[name="reportType"]').val();
				var compnayIdVal = $('select[name="companyId"]').val();
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadReportType',
					data : {
						reportType : reportType,
						compnayId : compnayIdVal
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null && _result != "" & _result.size != 0) {
							var jsonChartLine = JSON.parse(_result);
							var dataChartLine = [];
							var dataCategories = [];
							for ( var name in jsonChartLine.line) {
								if (name === 'Thời gian') {
									for (var i = 0; i < jsonChartLine.line[name].length; i++) {
										dataCategories.push(jsonChartLine.line[name][i]);
									}
								} else {
									dataChartLine.push({
										name : name,
										data : jsonChartLine.line[name]
									});
								}

							}
							line(dataChartLine, dataCategories, 'line');
							initReportNg();
						}
					}
				});
			}
			
			function initReportNg() {
				var reportType = $('select[name="reportType"]').val();
				var compnayIdVal = $('select[name="companyId"]').val();
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadReportNg',
					data : {
						reportType : reportType,
						compnayId : compnayIdVal
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null && _result != "" & _result.size != 0) {
							var jsonChart = JSON.parse(_result);
							var dataChart = [];
							var dataCategories = jsonChart.categories;
							dataChart.push({
								name : 'Khó-Hủy',
								data : jsonChart.lstDifficultG,
								stack : 'Khó',
								color : '#6d000b'
							});
							dataChart.push({
								name : 'Khó-NG',
								data : jsonChart.lstDifficultNg,
								stack : 'Khó',
								color : 'red'
							});
							dataChart.push({
								name : 'Khó-OK',
								data : jsonChart.lstDifficultG,
								stack : 'Khó',
								color : '#f67c88'
							});
							
							dataChart.push({
								name : 'Trung bình-Hủy',
								data : jsonChart.lstMediumG,
								stack : 'Trung bình',
								color : '#918937'
							});
							dataChart.push({
								name : 'Trung bình-NG',
								data : jsonChart.lstMediumNg,
								stack : 'Trung bình',
								color : '#ebb513'
							});
							dataChart.push({
								name : 'Trung bình-OK',
								data : jsonChart.lstMediumG,
								stack : 'Trung bình',
								color : '#549aee'
							});
							
							dataChart.push({
								name : 'Dễ-Hủy',
								data : jsonChart.lstEasyG,
								stack : 'Dễ',
								color : '#beb239'
							});
							dataChart.push({
								name : 'Dễ-NG',
								data : jsonChart.lstEasyNg,
								stack : 'Dễ',
								color : '#eee354'
							});
							dataChart.push({
								name : 'Dễ-OK',
								data : jsonChart.lstEasyG,
								stack : 'Dễ',
								color : '#0ae33c'
							});
							
							
							stacked('stacked', dataChart, dataCategories);
						}
					}
				});
			}

			function line(dataChart, dataCategories, divId) {
				Highcharts.chart(divId, {
					title : {
						text : ''
					},

					subtitle : {
						text : ''
					},

					yAxis : {
						title : {
							text : ''
						}
					},

					xAxis : {
						categories : dataCategories,
						crosshair : true
					},

					legend : {
						layout : 'vertical',
						align : 'right',
						verticalAlign : 'middle'
					},

					plotOptions : {
						series : {
							label : {
								connectorAllowed : false
							}
						}
					},

					series : dataChart,

					responsive : {
						rules : [ {
							condition : {
								maxWidth : 500
							},
							chartOptions : {
								legend : {
									layout : 'horizontal',
									align : 'center',
									verticalAlign : 'bottom'
								}
							}
						} ]
					}

				});
			}
			function stacked(divId, dataChart, dataCategories) {
				Highcharts.chart(divId, {
					chart: {
				        type: 'column'
				    },
				    title: {
				        text: ''
				    },
				    subtitle: {
				        text: ''
				    },
				    xAxis: {
				        categories: dataCategories,
				        crosshair: true
				    },
				    yAxis: {
				        min: 0,
				        max:100,
				        title: {
				            text: 'Phần trăm'
				        }
				    },
				    tooltip: {},
				    plotOptions: {
				        column: {
				            pointPadding: 0.2,
				            borderWidth: 0,
				            stacking: 'normal',
				        }
				    },
				    series: dataChart
				});
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
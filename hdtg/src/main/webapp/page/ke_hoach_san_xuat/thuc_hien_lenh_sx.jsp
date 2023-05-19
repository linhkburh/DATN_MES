<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.dataTables_filter {
	display: none;
}

.error {
	color: red !important;
}
</style>


<div id="test-area-qr-code-webcam" class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Scan mã lệnh sản xuất</h4>
			</div>
			<div class="modal-body">
				<div class='Table'>
					<div class="Row">
						<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" id='abc' align="center">
							<video id="video" width="480" height="270"></video>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id='modalBtnClose' class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>


<tiles:insertDefinition name="simple_base">
	<c:choose>
		<c:when test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code=="OS"}'>
			<tiles:putAttribute name="title" value="Chuyển gia công ngoài" />
		</c:when>
		<c:otherwise>
			<tiles:putAttribute name="title" value="Thực hiện sản xuất" />
		</c:otherwise>
	</c:choose>
	<tiles:putAttribute name="formInf">
		<spring:url value="/workOderDetail" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="more">
			<%@ include file="thay_doi_ket_qua_san_xuat.jsp"%>
		</tiles:putAttribute>
		<tiles:putAttribute name="body">

			<!-- hidden workOrderId-->
			<form:hidden path="workOrder.id" id="woId" cssClass="woInf" />
			<form:hidden path="workOrderExe.workOrderId.id" id="woId" cssClass="woInf" />
			<form:hidden path="workOrder.quotationItemExe.exeStepId.stepType.code" id="stepTypeCode" />
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="Header1">Thông tin lệnh sản xuất</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã LSX</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="workOrder.code" readonly="true" />
						</div>
						<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" style="padding-left: 0; margin-left: -10;">
							<a href="javascript:;" class="fa fa-qrcode" id="qrInput" style="font-size: 25; font-weight: bold;"></a>
						</div>
						<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12" style="margin-left: 10;"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã bản vẽ</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.quotationItemExe.quotationItemId.code" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã quản lý</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.quotationItemExe.quotationItemId.manageCode" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công đoạn</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.quotationItemExe.exeStepId.fullName" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng chi tiết</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" class="number" path="workOrder.amountStr" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian TB/Chi tiết (phút)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.quotationItemExe.unitExeTimeStr" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian dự kiến (giờ)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.totalEstTimeStr" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="color: red">Tổng số chi tiết đã thực
							hiện</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.TotalExeAmountStr" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian đã thực hiện (giờ)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.exeTimeStr" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian còn lại (giờ)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.remainTimeStr" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng chi tiết đã hoàn thành</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.numOfFinishItemStr" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian trung bình (phút)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.exeAvageTimeStr" />
						</div>
					</div>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chậm (giờ)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.lateTimeStr" />
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chậm (%)</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input readonly="true" path="workOrder.latePercent" />
						</div>
					</div>
					<c:if test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code!="OS"}'>
						<div class="Header2" style="font-size: 0">&ZeroWidthSpace;</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" title="Hủy trong giai đoạn sản xuất">Sản
								xuất hủy</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:input path="workOrder.brokenAmountStr" readonly="true" />
							</div>
							<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12" title="QC hủy trong giai đoạn sản xuất">QC
								Hủy (SX)</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" title="QC hủy trong giai đoạn sản xuất">
								<form:input path="workOrder.qcBrokenAmountStr" readonly="true" />
							</div>
							<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12" title="Hủy trong giai đoạn QC">QC hủy (QC)</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" title="Hủy trong giai đoạn QC">
								<form:input path="workOrder.qcDestroyAmout" readonly="true" />
							</div>
							<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12" title="Hủy trong giai đoạn nguội">Nguội hủy</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12" title="Hủy trong giai đoạn nguội">
								<form:input path="workOrder.polishingDestroyAmout" readonly="true" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="color: red">Tổng số chi tiết cần sản
								xuất</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:input path="workOrder.todoAmount" readonly="true" />
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">NG sản xuất</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:input path="workOrder.ngAmountStr" readonly="true" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">NG QC</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:input path="workOrder.qcNgAmount" readonly="true" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">NG đã sửa</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:input path="workOrder.repairedNgAmountStr" readonly="true" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="color: red">NG cần sửa</div>
							<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
								<form:input path="workOrder.todoNgAmount" readonly="true" />
							</div>

						</div>
					</c:if>

					<c:choose>
						<c:when test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code=="OS"}'>
							<div class="Header1">Chuyển gia công ngoài</div>
						</c:when>
						<c:otherwise>
							<div class="Header1">Sản xuất</div>
						</c:otherwise>
					</c:choose>
					<%--
					<div id="realTime">
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Chi tiết đang gia công<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input id="chiTietDangGiaCongDetail" path="workOrder.nextItemStr" readonly="true" class="number required" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
								Thời gian bắt đầu<font color="red">*</font>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input type="text" id="thoiGianBatDauDetail" class="datetime required" path="workOrderExe.startTimeStr" />
							</div>
							<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"
								style="text-align: center; margin-top: -18px; padding-left: 50px;">
								<input class="btn blue" type="button" onclick="addWorkOrderDetail('');" value="Hoàn Thành" />
							</div>
						</div>
					</div>
					 --%>
					<div id="noneRealTime" tabindex="-1">
						<c:choose>
							<c:when test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code=="OS"}'>
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Thời gian bắt đầu<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="thoiGianBatDauDetail1" class="datetime required" path="workOrderExe.startTimeStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Thời gian kết thúc<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="thoiGianKetThucDetail1" class="datetime required" path="workOrderExe.endTimeStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"
										title="Số lượng chi tiết đã hoàn thành các bước gia công trước">Số lượng chi tiết chuyển tối đa</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"
										title="Số lượng chi tiết đã hoàn thành các bước gia công trước">
										<form:input type="text" readonly="true" disabled="true" class="number" path="workOrderExe.totalAmountStr" />
									</div>
								</div>

								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Số lượng chi tiết<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="totalAmountStr" class="number required" path="workOrderExe.totalAmountStr" />
										<form:hidden id="soLuongChiTietDetail" class="number required" path="workOrderExe.amountStr" />
										<form:hidden id="ngAmountStr" class="required number" readonly="true" path="workOrderExe.ngAmountStr" />
									</div>

									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Công nhân thực hiện<font color="red">*</font>
									</div>
									<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
										<form:select path="workOrderExe.sysUser.id" id="selectSysUsers" class="required">
											<option value="">- Chọn -</option>
											<c:forEach items="#{lstSysUser}" var="item">
												<c:choose>
													<c:when test="${item.id==formDataModelAttr.workOrderExe.sysUser.id}">
														<option value="${item.id}" selected="selected">
															<c:out value="${item.username}" />-
															<c:out value="${item.name}" />
														</option>
													</c:when>
													<c:otherwise>
														<option value="${item.id}">
															<c:out value="${item.username}" />-
															<c:out value="${item.name}" />
														</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</form:select>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Sửa NG</div>
									<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
										<form:checkbox path="workOrderExe.ngRepaire" id="ngRepaire" />
									</div>
									<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
										<a href="javascript:;" onclick="finish()" title="Hoàn thành" class="fa fa-check"
											style="font-size: 25; font-weight: bold;"></a>

									</div>
									<c:if test='${formDataModelAttr.workOrder.quotationItemExe.corresponding!=null}'>
										<div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
											<a href="javascript:;" onclick="downloadProDoc()" title="Download file chương trình"
												style="float: left; margin-left: 10;"><i class="fa fa-download"></i></a>
										</div>
									</c:if>
								</div>
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Thời gian bắt đầu<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="thoiGianBatDauDetail1" class="datetime required" path="workOrderExe.startTimeStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Thời gian kết thúc<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="thoiGianKetThucDetail1" class="datetime required" path="workOrderExe.endTimeStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian setup (phút)</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="setupTimeStr" class="textint" path="workOrderExe.setupTimeStr" />
									</div>

								</div>
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Tổng số chi tiết<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="totalAmountStr" class="number required" path="workOrderExe.totalAmountStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Số lượng hoàn thành (OK)<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="soLuongChiTietDetail" class="number required" path="workOrderExe.amountStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 rp">
										Số lượng cần sửa (NG)<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12 rp">
										<form:input type="text" id="ngAmountStr" class="required number" readonly="false"
											path="workOrderExe.ngAmountStr" />
									</div>

								</div>
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Số lượng hủy<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:input type="text" id="brokenAmountStr" class="required number" readonly="true"
											path="workOrderExe.brokenAmountStr" />
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Công nhân thực hiện<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:select path="workOrderExe.sysUser.id" id="selectSysUsers" class="required">
											<option value="">- Chọn -</option>
											<c:forEach items="#{lstSysUser}" var="item">
												<option value="${item.id}">
													<c:out value="${item.username}" />-
													<c:out value="${item.name}" />
												</option>
											</c:forEach>
										</form:select>
									</div>
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
										Máy gia công<font color="red">*</font>
									</div>
									<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
										<form:select id="astMachine" path="workOrderExe.astMachine.id" cssClass="required">
											<form:option value="">- Chọn -</form:option>
											<c:forEach items="#{lstAstMachine}" var="item">
												<form:option value="${item.id}">
													<c:out value="${item.astName}" />
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								<div class="row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12 cell">Nguyên nhân lỗi</div>
									<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12 cell">
										<form:select path="workOrderExe.errorCause.id" id="selectErrorCause">
											<option value="">- Chọn -</option>
											<c:forEach items="#{lstErrorCause}" var="item">
												<option value="${item.id}">
													<c:out value="${item.value}" />
												</option>
											</c:forEach>
										</form:select>
									</div>
									<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12">Mô tả lỗi</div>
									<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
										<form:textarea id="ngDescription" path="workOrderExe.ngDescription" />
									</div>
								</div>
							</c:otherwise>
						</c:choose>

						<div style="float: right; margin-top: -18px; padding-right: 18; margin-top: 0 !important;">
							<input class="btn blue fa" type="button" onclick="addWorkOrderDetail('total');" value="&#xf0c7; Hoàn Thành" />
						</div>
					</div>

				</div>
				<c:choose>
					<c:when test='${formDataModelAttr.workOrder.quotationItemExe.exeStepId.stepType.code=="OS"}'>
						<div class="Header1">Lịch sử chuyển gia công ngoài</div>
					</c:when>
					<c:otherwise>
						<div class="Header1">Lịch sử sản xuất</div>
					</c:otherwise>
				</c:choose>
			</div>

		</tiles:putAttribute>

		<tiles:putAttribute name="divaction" cascade="true">
			<div align="center" class="divaction">
				<input type="button" onclick="cancel()" value="&#xf112; Bỏ qua" id="btlCancel" class="btn gray fa">
			</div>
		</tiles:putAttribute>
	</form:form>


	<tiles:putAttribute name="extra-scripts">
		<script type="module" src="<spring:url value="/js/zxing-browser.js" />"></script>
		<script type="module" src="<spring:url value="/js/video.js" />"></script>
		<script type="module">			
			import {initCamera} from '<spring:url value="/js/video.js" />';
			$('#qrInput').on('click', function(){
				initCamera('test-area-qr-code-webcam',callBack);
			})
			function callBack(qrCode){
				$.ajax({
					url:'workOderDetail?method=checkWoCode&workOrder.code=' + qrCode,
					success:function(res){
						if(res=='true')
							window.location = 'workOderDetail?workOrder.code=' + qrCode;
						else{
							alert('Mã LSX '+qrCode+' không tồn tại!', function(){
								window.location.reload();
							});
						}
					}
					
				});
			}
		</script>
		<script type="text/javascript">
			function downloadProDoc() {
				window
						.open(
								'qiFile?hiddenMenu=true&readOnly=true&to=pro&parentId=${formDataModelAttr.workOrder.quotationItemExe.corresponding.id}',
								'', 'width=1000, height=600, status=yes, scrollbars=yes').forcus();
			}
			function initParam(tblCfg) {
				jQuerySearchSelector = $('#woId');
				if ($('#stepTypeCode').val() == 'OS') {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Công nhân'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời gian bắt đầu'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời gian kết thúc'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng chi tiết'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : '<spring:message code="HistoryPro.updator"/>'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Loại gia công'
					} ];
				} else {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Công nhân'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời gian bắt đầu'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời gian kết thúc'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Thời gian dự kiến (phút)'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Thời gian thực tế (phút)'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Thời gian setup (phút)'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Tổng số chi tiết'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng hoàn thành (OK)'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng cần sửa (NG)'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Số lượng hủy'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Thời gian trung bình'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Chậm (phút)'
					}, {
						"sClass" : "right",
						"bSortable" : false,
						"sTitle" : 'Chậm (%)'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : '<spring:message code="HistoryPro.updator"/>'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Loại gia công'
					} ];
				}

			}
			$(document).ready(
					function() {
						$("div[tabindex='-1']").focus();
						$('.btnDtDelete').hide();
						$('.btnDtAdd').hide();
						$('#soLuongChiTietDetail,#totalAmountStr,#ngAmountStr,#ngRepaire').on(
								'change',
								function() {
									if ($(this).attr('id') == 'ngRepaire') {
										$('#realTime .row.exe,#noneRealTime .row .rp').find('select, input, textarea')
												.prop('disabled', $(this).prop('checked'));
										$('#realTime .row.exe,#noneRealTime .row .rp').css('display',
												$(this).prop('checked') ? 'none' : 'inline');
									}
									// Cha ton tai tong so hoac so luong thanh cong
									if ($('#soLuongChiTietDetail').val().trim().length == 0
											|| $('#totalAmountStr').val().trim().length == 0)
										return;
									var amount = $('#soLuongChiTietDetail').val().convertStringToNumber();
									var totalAmout = $('#totalAmountStr').val().convertStringToNumber();
									if (amount == 0 && totalAmout == 0)
										return;
									var ngAndbroken = totalAmout - amount;
									// repaire
									if ($('#ngRepaire').prop('checked')) {
										// kg co NG
										$('#brokenAmountStr').val(formatSo(ngAndbroken));
										return;
									}
									// Thay doi so luong Ng
									if ($(this).attr('id') == 'ngAmountStr') {
										var ngAmount = $('#ngAmountStr').val().convertStringToNumber();
										var brokenAmount = ngAndbroken - ngAmount;
										$('#brokenAmountStr').val(formatSo(brokenAmount));
										return;
									}
									// Mac dinh NG (da so la NG, truong hop da so)
									$('#ngAmountStr').val(formatSo(ngAndbroken));
									$('#brokenAmountStr').val(0);
								});
					});
			scannerInput = "";
			lastClear = 0;
			document.addEventListener("keypress", function(e) {
				clearTimeout(lastClear);
				lastClear = window.setTimeout(function() {
					scannerInput = "";
				}, 20);

				console.log('e.keyCode: ' + e.keyCode);
				if (e.keyCode != 13)
					scannerInput += e.key;
				else {
					console.log(scannerInput);
					handleQrcode(scannerInput);
				}
			});
			function handleQrcode(qrCode) {
				if (!$('#test-area-qr-code-webcam').is(':visible'))
					return;
				window.location = 'workOderDetail?workOrder.code=' + qrCode;
			}
			$('#test-area-qr-code-webcam').on('shown.bs.modal', function() {
				$('#modalBtnClose').focus();
			})

			function addWorkOrderDetail(type) {
				validobj.resetForm();
				var smtSelector = 'total' == type ? '#woId, #noneRealTime select, #noneRealTime input, #noneRealTime textarea, .woInf input[name="workOrderExe.workOrderId.id"].woInf'
						: '#woId, #realTime select, #realTime input, #realTime textarea, input[name="workOrderExe.workOrderId.id"].woInf';
				if (!$(smtSelector).valid())
					return;
				var smtData = $(smtSelector).serialize();
				$.ajax({
					url : 'workOderDetail?method=addWorkOrderDetail&type=' + type,
					type : 'POST',
					async : false,
					data : smtData,
					method : 'GET',
					success : function(data) {
						if (data === '') {
							alert(thuc_hien_thanh_cong, function() {
								// Phai load lai page de refresh cac gia tri phia tren
								window.location.reload();
							});
						} else {
							alert(data);
						}

					}
				});
			}
			function cancel() {
				window.history.back();
			}
			function finish() {
				if (!$('#ngRepaire').prop('checked')) {
					$('#totalAmountStr, #soLuongChiTietDetail').val($('input[name="workOrder.todoAmount"]').val());
					$('#ngAmountStr, #brokenAmountStr').val(0);
				} else {
					$('#totalAmountStr, #soLuongChiTietDetail').val($('input[name="workOrder.todoNgAmount"]').val());
					$('#brokenAmountStr').val(0);
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>



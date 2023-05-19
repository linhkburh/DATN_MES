<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="display: none">
	<select style="display: none" id="lstQcOsDetail" name="lstQcOsDetail">
		<option value=""></option>
		<c:forEach items="#{lstQcOsDetail}" var="item">
			<option value="${item.id}">
				<c:out value="${item.quotationItem.manageCode}" />
			</option>
		</c:forEach>
	</select>
</div>
<tiles:insertDefinition name="base">
	<tiles:putAttribute name="title">
		Nhận hàng
	</tiles:putAttribute>
	<tiles:putAttribute name="formInf">
		<spring:url value="/qcIn" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="body">
			<div class='Table' id="test-area-qr-code-webcam">
				<form:hidden path="qcIn.quotationItem.id" />
				<form:hidden path="qcIn.qcOs.id" />
				<form:hidden path="qcIn.id" id = "id"/>
				<div class="Row">
					<c:if test="${formDataModelAttr.qcIn.workOrder!=null}">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Mã quản lý<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="qcIn.quotationItem.manageCode" readonly="true" class="required" />
						</div>
					</c:if>
					<c:if test="${formDataModelAttr.qcIn.workOrder==null}">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Mã quản lý<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="qcIn.qcOs.dsmaql" readonly="true" class="required" />
						</div>
					</c:if>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã bản vẽ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.quotationItem.code" class="required" readonly="true" />
					</div>
				</div>
				<c:if test="${formDataModelAttr.qcIn.workOrder!=null}">
					<form:hidden path="qcIn.workOrder.id" />
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Mã lệnh sản xuất<font color="red">*</font>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="qcIn.workOrder.code" class="required" readonly="true" />
						</div>
					</div>
				</c:if>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng đã chuyển<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.qcOs.amount" id="totalToOs" class="required" readonly="true" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng đã nhận<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.qcOs.partnerDone" id="partnerDone" class="required" readonly="true" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị gia công ngoài</div>
					<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
						<form:hidden path="qcIn.customers.id" />
						<form:select path="qcIn.customers.id" disabled="true">
							<c:forEach items="#{lstPartner}" var="item">
								<c:if test="${item.id==formDataModelAttr.qcIn.customers.id}">
									<option value="${item.id}" selected="selected">
										<c:out value="${item.orgName}" />
									</option>
								</c:if>
							</c:forEach>
						</form:select>
					</div>
				</div>
				<div class="Header1" style="font-size: 0">&ZeroWidthSpace;</div>
				<c:if test="${formDataModelAttr.qcIn.qcOs.qcOsDetails.size() == 1 && formDataModelAttr.qcIn.id==null}">
				<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<a href="javascript:;" onclick="finish()" title="Hoàn thành" class="fa fa-check"
							style="font-size: 25; font-weight: bold;"></a>
					</div>
				</div>
				</c:if>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời điểm nhận từ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.receiptDate" class="dateTime required" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời điểm nhận đến<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.receiptDateTo" class="dateTime required" />
					</div>
					
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng nhận<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.totalAmount" id="totalAmountStr" class="textint required" />
					</div>
					<c:if test="${formDataModelAttr.qcIn.qcOs.qcOsDetails.size() == 1}">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng hoàn thành (OK)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.amount" class="textint required" id="soLuongChiTietDetail" />
					</div>
					</c:if>
				</div>
				<c:if test="${formDataModelAttr.qcIn.qcOs.qcOsDetails.size() == 1}">
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng lỗi(NG)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.ngAmount" class="textint required" id="ngAmountStr" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số lượng hủy<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qcIn.brokenAmount" class="number required" readonly="true" id="brokenAmountStr" />
					</div>

				</div>
				</c:if>
				<c:if test="${formDataModelAttr.qcIn.qcOs.qcOsDetails.size() > 1}">
				<div class="Header2">Danh sách nhận</div>
				<div class="table-responsive">
					<table class="table table-bordered" style="table-layout: fixed;" id="table-OS-detail">
						<thead>
							<tr>
								<th>Mã quản lý<font color="red">*</font></th>
								<th>Số lượng đã chuyển</th>
								<th>Số lượng đã nhận</th>
								<th>Số lượng hoàn thành(OK)</th>
								<th>Số lượng cần sửa (NG)</th>
								<th>Số lượng hủy</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${formDataModelAttr.qcIn.qcInDetails}" var="pro" varStatus="loop">
								<tr>
									<td><form:select onchange="changeOsDetail($(this))" path="qcIn.qcInDetails[${loop.index}].qcOsDetail.id"
											class="lsQcOsDetail notnull required">
											<option value="">- Chọn -</option>
											<c:forEach items="#{lstQcOsDetail}" var="item">
												<form:option value="${item.id}">
													<c:out value="${item.quotationItem.manageCode}" />
												</form:option>
											</c:forEach>
										</form:select></td>
										<td><form:input readOnly="true" class="number"
											path="qcIn.qcInDetails[${loop.index}].qcOsDetail.amount" /></td>
										<td><form:input readOnly="true" class="number"
										path="qcIn.qcInDetails[${loop.index}].qcOsDetail.partnerDoneAmount" /></td>
									<td><form:input onchange="changeAmount();" class="textint amountDetail"
											path="qcIn.qcInDetails[${loop.index}].amount" /> <form:hidden
											path="qcIn.qcInDetails[${loop.index}].qcIn.id" /></td>
											<td><form:input onchange="changeAmount();" class="textint ngAmountDetail"
											path="qcIn.qcInDetails[${loop.index}].ngAmount" /></td>
									<td><form:input onchange="changeAmount();" class="textint brokenAmountDetail"
											path="qcIn.qcInDetails[${loop.index}].brokenAmount" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				</c:if>
				
				<div align="center" class="HeaderText">&ZeroWidthSpace;</div>
				<div align="center" class="divaction">
					<input type="button" onclick="addQcIn()" value="&#xf0c7; Lưu" id="btnSave" class="btn blue fa"> 
					<c:if test="${formDataModelAttr.qcIn.id!=null}">
						<input type="button" onclick="delReceivingHis()" value="&#xf00d; Xóa" id="btlCancel" class="btn red fa">
					</c:if>
					<input type="button" onclick="closeTabs()" value="&#xf112; Bỏ qua" id="btlCancel" class="btn gray fa">
					
				</div>
			</div>
		</tiles:putAttribute>


	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		var tableOS = null;
		function initTable() {
			var tableObject = new TFOnline.DataTable({
				id : 'table-OS-detail',
				jQueryUI : true,
				rowTemp : initRowTable("qcIn"),
				afterAddRow : function() {
					afterAddRow('table-OS-detail');
				},
				afterDeleteRow : function() {
					changeAmount();
				},
				hasCheck : true,
				hasOrder : true,
				addOveride : true,
				delOveride : true,
				maxRow : 100
			});
			tableOS = tableObject;

		}

		function afterAddRow() {
			$('.lsQcOsDetail').select2();
			initControl();
		}

		function initRowTable(obj) {
			var rowTemp = new Array();
			rowTemp.push('');
			rowTemp
					.push('<div class="line-table"><select onchange="changeOsDetail($(this))" class="lsQcOsDetail notnull required" name="qcIn.qcInDetails[].qcOsDetail.id">'
							+ $('#lstQcOsDetail').html() + '</select>  </div>');
			rowTemp
					.push('<div class="line-table"><input type="text" readOnly="true"  class="textint amount" name="qcIn.qcInDetails[].qcOsDetail.amount"> </div>');
			rowTemp
					.push('<div class="line-table"><input type="text" readOnly="true"  class="textint amount" name="qcIn.qcInDetails[].qcOsDetail.partnerDoneAmount"> </div>');
			rowTemp
					.push('<div class="line-table"><input type="text" onchange="changeAmount();"  class="textint amountDetail " name="qcIn.qcInDetails[].amount"> </div>');
			rowTemp
					.push('<div class="line-table"><input type="text" onchange="changeAmount();"  class="textint ngAmountDetail " name="qcIn.qcInDetails[].ngAmount"> </div>');
			rowTemp
					.push('<div class="line-table"><input type="text" onchange="changeAmount();"  class="textint brokenAmountDetail " name="qcIn.qcInDetails[].brokenAmount"> </div>');
			return rowTemp;
		}
		function changeOsDetail(jTriggerCtrl){
			var val = jTriggerCtrl.val();
			$.each(lstQcOsDetail, function(index, osd) {
				if (osd.id==val) {
					// qcIn.qcInDetails[0].qcOsDetail.amount
					// qcIn.qcInDetails[0].qcOsDetail.id
					$('input[name="' +jTriggerCtrl.attr('name').replace('.id','.amount') +  '"]').val(osd.amount);
					$('input[name="' +jTriggerCtrl.attr('name').replace('.id','.partnerDoneAmount') +  '"]').val(osd.partnerDoneAmount);
				}
			});
		}

			$(document).ready(
					function() {
						initTable();
						lstQcOsDetail= JSON.parse('${lstQcOsDetailStr}');
						
						$('select').select2();
						// Dialog
						if ($('#totalToOs').val() == $('#partnerDone').val() && $('#id').val().length==0) {
							alert('Đã nhận đủ số hàng!', function() {
								window.close();
								window.history.back();
							});
						}
						$('#soLuongChiTietDetail, #totalAmountStr, #ngAmountStr').on(
								'change',
								function() {
									if ($('#soLuongChiTietDetail').val().trim().length == 0
											|| $('#totalAmountStr').val().trim().length == 0)
										return;
									var amount = $('#soLuongChiTietDetail').val().convertStringToNumber();
									var totalAmout = $('#totalAmountStr').val().convertStringToNumber();
									if (amount == 0 && totalAmout == 0)
										return;
									var ngAndbroken = totalAmout - amount;
									
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

			function addQcIn() {
				if (!$('#theForm').valid())
					return;
				
				$.ajax({
					url : 'qcIn?method=addQcIn',
					type : 'POST',
					async : false,
					data : $('#theForm').serializeObject(),
					method : 'GET',
					success : function(data, xhr, status) {
						if (data == '') {
							alert('Thực hiện thành công!', function() {
								if($('#id').val().length==0){
									window.location.reload();
								}else{
									window.close();
								}
								window.opener.findData();
							});
						} else {
							alert(data);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(textStatus, errorThrown);
					}
				});
			}
			function closeTabs() {
				window.close();
				window.opener.findData();
			}
			function delReceivingHis() {
				jConfirm('Bạn có chắn chắn muốn xóa lịch sử nhận hàng này?', 'OK', 'Cancel', function(r) {
					if (r) {
						$.ajax({
							url : 'receivingHis?method=delReceivingHis',
							type : 'POST',
							async : false,
							data : {
								qcInId : $('input[name="qcIn.id"]').val()
							},
							method : 'GET',
							success : function(data) {
								if (data === '') {
									alert(thuc_hien_thanh_cong, function() {
										window.close();
										window.opener.findData();
									});
								} else {
									alert(data);
								}

							}
						});
					}
				});
			}
			function finish() {
				var amountBinding = $('input[name="qcIn.qcOs.amount"]').val().convertStringToNumber();
				var partnerDoneBinding = $('input[name="qcIn.qcOs.partnerDone"]').val().convertStringToNumber();
				$('#totalAmountStr, #soLuongChiTietDetail').val(amountBinding - partnerDoneBinding);
				$('#ngAmountStr, #brokenAmountStr').val(0);
			}
			function changeAmount() {
				
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form:hidden path="quotationItem.quotationId.id" id="quotationId" />
<form:hidden path="quotationItem.id" id="id" />
<input type="hidden" value="${toApprove}" id="toApprove" />
<input type="hidden" value="${approve}" id="approve" />
<div class="table">

	<div class="row">

		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Mã bản vẽ<font color="red">*</font>
		</div>
		<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
			<form:input path="quotationItem.code" id="code" cssClass="required"></form:input>
		</div>
		<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12">
			<c:if test="${to != 'select'}">
				<a href="javascript:;" onclick="checkItemCode()" title="Kiểm tra"
					style="float: left; padding-left: 0; margin-left: -25px;"><i class="fa fa-search"></i></a>
			</c:if>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Mã quản lý<font color="red">*</font>
		</div>
		<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
			<form:input path="quotationItem.manageCode" id="manageCode" cssClass="required"></form:input>
		</div>
		<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12" style="padding-left: 0;">
			<a href="javascript:;" onclick="upload()" title="Download tài liệu bản vẽ" style="float: left;"><i
				class="fa fa-upload"></i></a> <a href="javascript:;" onclick="downloaddrw(1)"
				title="Download tài liệu bản vẽ" style="float: left; margin-left: 10;"><i
				class="fa fa-download"></i></a>
		</div>

	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Tên chi tiết<font color="red">*</font>
		</div>
		<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
			<form:input path="quotationItem.name" id="name" cssClass="required"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Mã quản lý gốc
		</div>
		<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
			<form:input path="quotationItem.rootManageCode"></form:input>
		</div>
	</div>
	<div class="Row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Số lệnh sản xuất<font color="red">*</font>
		</div>
		<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12">
			<form:input path="quotationItem.workOderNumber" id="workOderNumber" cssClass="required"></form:input>
		</div>
	</div>
	<div class="Header2"></div>
	<div class="row">

		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Số lượng<font color="red">*</font>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.qualityStr" id="itemQuality" cssClass="required textint" onchange="reloadReduce()"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng sản xuất thêm</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.extraQuatity" id="extraQuatity" cssClass="textint"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày giao hàng</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.deliverDateStr" id="deliverDate" cssClass="date"></form:input>
		</div>


	</div>

	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng chi tiết chạy thử</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.trialItem" id="trialItem" cssClass="textint maxVal" maxVal="9999999999"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Chủng loại bản vẽ<font color="red">*</font>
		</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:select path="quotationItem.drawingType.id" id="drawingType" cssClass="required" onchange="initOpPrice();">
				<form:option value="" label="- Chọn -" />
				<form:options items="${drawingTypes}" itemValue="id" itemLabel="value" />
			</form:select>
		</div>





		<!-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời gian chuẩn bị</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<form:input path="quotationItem.prepareTime" id="prepareTime"
							cssClass="textint maxVal" maxVal="9999999999"
							></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Chi phí chuẩn bị</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<form:input path="quotationItem.prepareCostStr" id="prepareCost"
							cssClass="number" precision="27" scale="2"></form:input>
					</div> -->
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chi phí gá đồ</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.shelfCostStr" id="shelfCost" cssClass="number" precision="27" scale="2"></form:input>
		</div>
	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chi phí thuê ngoài</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.outsourceCodeStr" id="outsourceCode" cssClass="number" precision="27" scale="2"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chi phí vận chuyển</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.transportCostStr" id="transportCost" cssClass="number" precision="27" scale="2"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chi phí nhiệt luyện</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.fireCostStr" id="fireCost" cssClass="number" precision="27" scale="2"></form:input>
		</div>
	</div>

	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chi phí khác</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.otherCostStr" id="otherCost" cssClass="number" precision="27" scale="2"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổng thời gian setup (phút)</div>
		<div class="label-static col-md-2 col-md-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.bookingSetupTimeStr" cssClass="textint"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian dự kiến QC một chi tiết (phút)</div>
		<div class="label-static col-md-2 col-md-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.qcItmEstTime" cssClass="textint"></form:input>
		</div>
	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian dự kiến Nguội một chi tiết (phút)</div>
		<div class="label-static col-md-2 col-md-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.plshItmEstTime" cssClass="textint"></form:input>
		</div>
	</div>

	<!-- <div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Chi phí mài</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<form:input path="quotationItem.grinDingCostStr" id="grinDingCost"
							cssClass="number" precision="27" scale="2"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Chi phí mài đánh bóng</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<form:input path="quotationItem.polishCostStr" id="polishCost"
							cssClass="number" precision="27" scale="2"></form:input>
					</div>

				</div> -->


	<div class="Header1">Vật liệu</div>
	<form:hidden class="form-control" path="quotationItem.quotationItemMaterialList[0].id" id="qiMaterialId" />
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Vật liệu<font color="red">*</font>
		</div>


		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:select class="notnull selectMaterial" id="marterialId" onchange="initOpPrice();"
				path="quotationItem.quotationItemMaterialList[0].marterialId.id">
				<option value="">- Chọn -</option>
				<c:forEach items="#{listMaterial}" var="item">
					<form:option value="${item.id}">
						<c:out value="${item.materialType.name}" />-<c:out value="${item.code}" />
					</form:option>
				</c:forEach>
			</form:select>
		</div>


		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Vật liệu thay thế</div>


		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<c:if test="${formDataModelAttr.quotationItem.quotationItemMaterialList[0].marteriaBackupId!=null}">
				<form:select class="selectMaterialBackup" onchange="initOpPrice();"
					path="quotationItem.quotationItemMaterialList[0].marteriaBackupId.id" id='backUpMarterialId'>
					<option value="">- Chọn -</option>
					<c:forEach items="#{listMaterial}" var="item">
						<form:option value="${item.id}">
							<c:out value="${item.materialType.name}" />-<c:out value="${item.code}" />
						</form:option>
					</c:forEach>
				</form:select>
			</c:if>
			<c:if test="${formDataModelAttr.quotationItem.quotationItemMaterialList[0].marteriaBackupId==null}">
				<select class="selectMaterialBackup" onchange="initOpPrice();"
					name="quotationItem.quotationItemMaterialList[0].marteriaBackupId.id" id='backUpMarterialId'>
					<option value="">- Chọn -</option>
					<c:forEach items="#{listMaterial}" var="item">
						<option value="${item.id}">
							<c:out value="${item.materialType.name}" />-
							<c:out value="${item.code}" />
						</option>
					</c:forEach>
				</select>
			</c:if>
		</div>


		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input type="text" class="form-control number material initPrice"
				name="quotationItem.quotationItemMaterialList[0].initPriceStr" readonly="readOnly">
		</div>
	</div>
	<div class="row">

		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khối lượng riêng</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input type="text" class="form-control number material density"
				name="quotationItem.quotationItemMaterialList[0].marterialId.densityStr" readonly="readonly" id="marterialDensity">
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chiều dài (mm)</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input name="quotationItem.quotationItemMaterialList[0].sizeLongStr" class="form-control number material sizeLong"
				precision="27" scale="2" />
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chiều cao (mm)</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.quotationItemMaterialList[0].sizeHeightStr"
				cssClass="form-control number material sizeHeight" precision="27" scale="2"></form:input>
		</div>
	</div>

	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chiều rộng/Đường kính (mm)</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.quotationItemMaterialList[0].sizeWidthStr"
				cssClass="form-control number material sizeWidth" precision="27" scale="2"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Khối lượng (g)</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.quotationItemMaterialList[0].sizeWeighStr" id="marterialSizeWeigh"
				cssClass="form-control number material sizeWeigh" precision="27" scale="2"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.quotationItemMaterialList[0].qualityStr" id="materialQuality"
				cssClass="form-control textint material quality" precision="27" scale="2"></form:input>
		</div>
	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hao phí vật liệu (%)</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:input path="quotationItem.quotationItemMaterialList[0].wasteStr" id="waste"
				cssClass="form-control number waste" precision="10" scale="2"></form:input>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thành tiền</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input type="text" name="quotationItem.quotationItemMaterialList[0].priceStr"
				class="form-control number material totalPrice" readonly="readOnly" id="marterialTotalPrice">
		</div>

		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Dài phôi gốc</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input type="text" name="quotationItem.quotationItemMaterialList[0].angleWorkpiece" class="form-control number"
				id="angleWorkpiece">
		</div>
	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Cán nóng/Cán nguội</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input type="text" name="quotationItem.quotationItemMaterialList[0].coldRolled" class="form-control" id="coldRolled">
		</div>

		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Cán bộ kỹ thuật<font color="red">*</font>
		</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:select path="quotationItem.technicalId.id" id="technicalId" cssClass="required">
				<option value="">- Chọn -</option>
				<c:forEach items="#{lstTech}" var="item">
					<form:option value="${item.id}">
						<c:out value="${item.username}" />-<c:out value="${item.name}" />
					</form:option>
				</c:forEach>
			</form:select>
		</div>

		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Cán bộ kế hoạch<font color="red">*</font>
		</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:select path="quotationItem.planner.id" id="planner" cssClass="required">
				<option value="">- Chọn -</option>
				<c:forEach items="#{lstPlan}" var="item">
					<form:option value="${item.id}">
						<c:out value="${item.username}" />-<c:out value="${item.name}" />
					</form:option>
				</c:forEach>
			</form:select>
		</div>
	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Cán bộ AC<font color="red">*</font>
		</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:select path="quotationItem.acId.id" id="acId" cssClass="required">
				<option value="">- Chọn -</option>
				<c:forEach items="#{lstAc}" var="item">
					<form:option value="${item.id}">
						<c:out value="${item.username}" />-<c:out value="${item.name}" />
					</form:option>
				</c:forEach>
			</form:select>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nơi sản xuất</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<input type="text" name="quotationItem.quotationItemMaterialList[0].sourcePro" class="form-control" id="sourcePro">
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Phân xưởng</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			
			<c:if test="${formDataModelAttr.quotationItem.factoryUnit.id!=null}">
				<form:select path="quotationItem.factoryUnit.id" id="factoryUnit">
					<option value="">- Chọn -</option>
					<c:forEach items="#{lstFactoryUnit}" var="item">
						<form:option value="${item.id}">
							<c:out value="${item.code}" />
						</form:option>
					</c:forEach>
				</form:select>
			</c:if>
			<c:if test="${formDataModelAttr.quotationItem.factoryUnit.id==null}">
				<select name="quotationItem.factoryUnit.id" id='factoryUnit'>
					<option value="">- Chọn -</option>
					<c:forEach items="#{lstFactoryUnit}" var="item">
						<option value="${item.id}"><c:out value="${item.code}" />
						</option>
					</c:forEach>
				</select>
			</c:if>
			<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			
		</div>
		</div>

	</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
			Loại hàng<font color="red">*</font>
		</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
			<form:select path="quotationItem.siggle.id" id="siggle" cssClass="required">
				<option value="">- Chọn -</option>
				<c:forEach items="#{lstsiggle}" var="item">
					<form:option value="${item.id}">
						<c:out value="${item.description}" />
					</form:option>
				</c:forEach>
			</form:select>
		</div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lưu ý phôi</div>
		<div class="label-static col-md-6 col-lg-6 col-sm-12 col-xs-12">
			<textarea class="form-control" rows="3" name="quotationItem.quotationItemMaterialList[0].embryoNote" id="embryoNote"></textarea>
		</div>
	</div>
	<div class="Header1">Lập trình - gia công</div>
	<div class="row">
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn giá gia công</div>
		<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
			<form:input path="quotationItem.opInitPriceStr" id="itemOpInitPrice" cssClass="number" readonly="true"></form:input>
		</div>
		<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
		<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mức giảm trừ theo số lượng</div>
		<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">
			<form:input path="quotationItem.reduceStr" id="itemQualityReduce" cssClass="number" readonly="true"></form:input>
		</div>
	</div>
	<div class="table-responsive">
		<table class="table table-bordered" style="table-layout: fixed;" id="table-pro">
			<thead>
				<tr>
					<th>Hình thức gia công<font color="red">*</font></th>
					<th>Công đoạn<font color="red">*</font></th>
					<th style="text-align: right;">Đơn giá (giờ)</th>
					<th style="text-align: right;">Thời gian lập trình (phút)<font color="red">*</font></th>
					<th style="text-align: right;">Thành tiền</th>
					<th>Lập trình viên</th>
					<th>File chương trình</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${formDataModelAttr.quotationItem.quotationItemProList}" var="pro" varStatus="loop">
					<tr>
						<td><form:select path="quotationItem.quotationItemProList[${loop.index}].exeStepId.stepType.id"
								class="exeStepType notnull">
								<option value="">- Chọn -</option>
								<c:forEach items="#{listExeStepType}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select></td>
						<td><form:select path="quotationItem.quotationItemProList[${loop.index}].exeStepId.id"
								class="exeStepId notnull">
								<option value="">- Chọn -</option>
								<c:forEach items="#{pro.exeStepId.stepType.lstExePro}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.stepName}" />
									</form:option>
								</c:forEach>
							</form:select></td>
						<td><form:input path="quotationItem.quotationItemProList[${loop.index}].initPriceStr"
								style="text-align: right;" class="form-control number initPrice" readOnly="readOnly"></form:input></td>
						<td><form:input path="quotationItem.quotationItemProList[${loop.index}].unitExeTimeStr"
								class="form-control notnull number exeTime" style="text-align: right;"></form:input></td>
						<td><form:input path="quotationItem.quotationItemProList[${loop.index}].priceStr"
								class="form-control number totalPriceExe" readOnly="readOnly" style="text-align: right;"></form:input></td>
						<td><form:hidden path="quotationItem.quotationItemProList[${loop.index}].id"></form:hidden> <c:if
								test="${pro.workPros!=null && !pro.workPros.isEmpty()}">
								<a style="color: blue;" name="quotationItem.quotationItemProList[${loop.index}].id" href="javascript:;"
									onclick="lenhsx($(this))">${pro.workPros[0].programer.name}</a>
							</c:if> <c:if test="${pro.workPros==null || pro.workPros.isEmpty()}">
								<a style="color: red;" name="quotationItem.quotationItemProList[${loop.index}].id" href="javascript:;"
									onclick="lenhsx($(this))">Phân công lập trình</a>
							</c:if></td>
						<td><a href="javascript:;" class="upload"  id="quotationItem.quotationItemProList[${loop.index}].id" style="float: left;"><i class="fa fa-upload"></i></a> <a
							href="javascript:;" class="download" id="quotationItem.quotationItemProList[${loop.index}].id" style="float: left; margin-left: 10;"><i
								class="fa fa-download"></i></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
	<div class="Header2">Gia công</div>
	<div class="table-responsive">
		<table class="table table-bordered" style="table-layout: fixed;" id="table-exe">
			<thead>
				<tr>
					<th>Hình thức gia công<font color="red">*</font></th>
					<th>Công đoạn<font color="red">*</font></th>
					<th style="text-align: right;">Đơn giá (giờ)</th>
					<th style="text-align: right;" title="Thời gian theo báo giá">Thời gian báo giá (phút)</th>
					<th style="text-align: right;" title="Thời gian do lập trình xác định">Thời gian gia công (phút)<font
						color="red">*</font></th>
					<th style="text-align: right;">Thành tiền</th>
					<th>Chi tiết lệnh sản xuất</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${formDataModelAttr.quotationItem.quotationItemExeList}" var="pro" varStatus="loop">
					<tr>
						<td><form:select path="quotationItem.quotationItemExeList[${loop.index}].exeStepId.stepType.id"
								class="exeStepType notnull">
								<option value="">- Chọn -</option>
								<c:forEach items="#{listExeStepType}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select></td>
						<td><form:select path="quotationItem.quotationItemExeList[${loop.index}].exeStepId.id"
								class="exeStepId notnull">
								<option value="">- Chọn -</option>
								<c:forEach items="#{pro.exeStepId.stepType.lstExe}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.stepName}" />
									</form:option>
								</c:forEach>
							</form:select></td>
						<td><form:input path="quotationItem.quotationItemExeList[${loop.index}].initPriceStr"
								style="text-align: right;" class="form-control number initPrice" readOnly="readOnly"></form:input></td>
						<td><form:input class="form-control number"
								path="quotationItem.quotationItemExeList[${loop.index}].quotationExeTimeStr" style="text-align: right;"></form:input></td>
						<td><form:input class="form-control notnull number exeTime"
								path="quotationItem.quotationItemExeList[${loop.index}].unitExeTimeStr" style="text-align: right;"></form:input></td>
						<td><form:input path="quotationItem.quotationItemExeList[${loop.index}].priceStr"
								class="form-control number totalPriceExe" readOnly="readOnly" style="text-align: right;"></form:input></td>
						<td><form:hidden path="quotationItem.quotationItemExeList[${loop.index}].id"></form:hidden> <c:if
								test="${pro.remain == 0}">
								<a style="color: blue;" name="quotationItem.quotationItemExeList[${loop.index}].id" href="javascript:;"
									onclick='lenhsx($(this))'>Chi tiết lệnh sản xuất</a>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
			
		</table>

	</div>

	<div align="right" style="text-align: right; width: 100%">
		<h6 id="price-exeStep" style="float: left; padding-left: 10; font-weight: bold;">
			Tổng tiền gia công: <span>0</span> USD
		</h6>
		<input type="button" value="&#xf085; Tạo/xem lệnh sản xuất" class="btn blue fa" onclick="makeWorkOrderByItem()"
			style="font-size: 12" />
	</div>


</div>

<div id="modal_ds_file_sx" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Danh sách file</h4>
			</div>
			<div class="modal-body" style="background-color: white">
				<table class="table table-bordered" id="viewFileSX">
						<thead>
							<tr style="background-color: #93D5F7">
								<td>STT</td>
								<td>Tên file</td>
								<td>Mã quản lý</td>
								<td>Người upload</td>
								<td>Ngày upload</td>
								<td>Loại tài liệu</td>
								<td></td>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn gray" data-dismiss="modal">
					<spring:message code="common.btn.close" />
				</button>
			</div>
		</div>
	</div>
</div>

<script lang="javascript">
	$(document).ready(function() {
		lstStepType = JSON.parse('${jaExeType}');
		lstFileType = JSON.parse('${lstFile}');
		lstProFileType= JSON.parse('${lstProFileType}');
		if ($('#id').val().trim().length != 0) {
			//edit($('#id').val());

			$("#price-exeStep span").text('${formDataModelAttr.quotationItem.stepPriceStr}');
			//initControl();
		}
		
		
		$(document.body).on("click", "table#table-pro a.upload", function() {
			var proIdFieldName=	$(this).attr('id');
			fileRecordId = $('input[name="'+proIdFieldName+'"]').val();
			if(fileRecordId==''){
				alert('Cần lưu thông tin trước!')
				return;
			}
			new FileUpload(fileRecordId, undefined, lstProFileType).open();
		});
		$(document.body).on("click", "table#table-pro a.download", function() {
			var proIdFieldName=	$(this).attr('id');
			fileRecordId = $('input[name="'+proIdFieldName+'"]').val();
			if(fileRecordId==''){
				alert('Cần lưu thông tin trước!')
				return;
			}
			downloaddrw(fileRecordId);
		})
	});
	
	function uploadProFile(id,jHrefAct){
	    //fileSelect.files = null;
	    if( id != null && id != ''){
	        $('#file-select').val('');
	        proListId = id;
	        $('#modal_upload_file_sx').modal('show');
	    }else{
	    	id = $('input[name="' + $(jHrefAct).attr('id') + '"]').val();
	    	if (id != null && id != '') {
	    		proListId = id;
		        $('#modal_upload_file_sx').modal('show');
	    	} else {
	    		alert("Phải lưu biểu mẫu trước khi tải phải lên");
	    	}
	    }
	}

	var proListId;
	
	
	
	function downloadProFile(id,jHrefAct) {
		$("#viewFileSX").find("tbody tr").remove();
		if (id == null || id === '')
			id = $('input[name="' + $(jHrefAct).attr('id') + '"]').val();
		var url = "sysFile?method=loadFiles&id=" + id;
		var _row = '';

		$.ajax({
			url : url,
			method : 'GET',
			success : function(_result) {
				if (_result != null & _result.length > 0) {
					for (var i = 0; i < _result.length; i++) {
						_row += '<tr>';
						_row += '<td>' + (i + 1) + '</td>';
						_row += '<td>' + (_result[i].name) + '</td>';
						_row += '<td>' + (_result[i].owner) + '</td>';
						_row += '<td>' + (_result[i].sysUsers.name) + '</td>';
						_row += '<td>' + (_result[i].uploadDate) + '</td>';
						_row += '<td>' + (_result[i].sysDictParam.code + '-' + _result[i].sysDictParam.value) + '</td>';
						_row += '<td onClick="downloadProFileDetail(\'' + _result[i].id
								+ '\');"><i style="color:blue;" class="fa fa-download"></i></td>';
					}
					$("#viewFileSX > tbody:last-child").append(_row);

				}
			}
		});
		$('#modal_ds_file_sx').modal();
	}
	
	function downloadProFileDetail(id,jHrefAct) {
		if (id == null || id === '')
			id = $('input[name="' + $(jHrefAct).attr('id') + '"]').val();
		var cacheAction = $('#theForm').attr('action');
        $('#theForm').attr('action',$('#theForm').attr('action') + '&method=downloadFTP&id=' + id);
        document.getElementById("theForm").submit();
        $('#theForm').attr('action',cacheAction);
	}
	window_property_pop_up = 'width=1000, height=600, status=yes, scrollbars=yes';
	function downloaddrw(type) {
		var id = $('#id').val();
		if(type == 1){
			window.open('qiFile?hiddenMenu=true&to=drw&parentId='+id ,'',window_property_pop_up).forcus();
		}else{
			window.open('qiFile?hiddenMenu=true&to=pro&parentId='+type,'',window_property_pop_up).forcus();
		}
			
	}
	
	function upload() {
		new FileUpload($('#id').val(), undefined, lstFileType).open();
	}

</script>
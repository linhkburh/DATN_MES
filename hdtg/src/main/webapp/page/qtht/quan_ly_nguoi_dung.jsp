<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="modal_dialog_file" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Upload người dùng</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST" enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">File</label> <input type="file" id="file-select_imp"
									accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
									class="file-modal_imp" name="inputFile" />
							</div>

						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="upload();"><i class="fa fa-upload"></i>Upload</a> <a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>Thoát</a>
			</div>
		</div>
	</div>
</div>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Quản lý người dùng" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/manageUser" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Công ty</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="companyId" id='scompanyId' onchange="reloadDepartment($(this).val())">
								<option value="">- Chọn -</option>
								<c:forEach items="#{companies}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<c:if test="${lstDepartment!=null}">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Phòng ban</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="departmentId" id='sdepartmentId'>
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstDepartment}" var="item">
										<form:option value="${item.id}">
											<c:out value="${item.name}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
						</c:if>
					</div>
					<div class="Row">

						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên đăng nhập</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="strusername" id="strusername"></form:input>
						</div>
						<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên người dùng</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="strname" id="strname"></form:input>
						</div>
					</div>
					<!-- <div align="center" class="HeaderText">&#8203;</div> -->
					<div class="divaction" align="center">
						<input class="btn blue fa" type="button" onclick="findData();" value="&#xf002; Tìm kiếm" />
						<%-- Scripting elements ( &lt;%!, &lt;jsp:declaration, &lt;%=, &lt;jsp:expression, &lt;%, &lt;jsp:scriptlet ) are disallowed here.
		                            if(LDAP_AUTHEN == null || !"true".equals(LDAP_AUTHEN.getValue())){--%>
						<!-- <input type="button" onclick="addNew()" value="Thêm mới" class="btn blue"/> -->
						<%--} 
		                        --%>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="su.id" id="id" />
			<form:hidden path="su.roles" id="roles" />
			<form:hidden path="su.pwdDatestr" id="pwdDatestr" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.name" title="Tên không được để trống" cssClass="required"></form:input>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên đăng nhập<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.username" cssClass="required" cssStyle="text-transform: lowercase;"
						onkeypress="return onlyNumberAndChar(event);" title="Chỉ được nhập chữ thường và số"></form:input>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ca làm việc</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:select class="form-control" path="su.shift">
						<option value="">- Chọn -</option>
						<c:forEach items="#{lstShift}" var="item">
							<option value="${item.id}">
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
					</form:select>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Xưởng</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:select class="form-control" path="su.factoryUnit">
						<option value="">- Chọn -</option>
					</form:select>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chức vụ</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.jobName" title="Vị trí không được để trống"></form:input>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Giới tính<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:select class="form-control" path="su.gender" title="Chọn giới tính" cssClass="required">
						<option value="">- Chọn -</option>
						<c:forEach items="#{dsGender}" var="item">
							<option value="${item.id}">
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Phone</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.phone" onkeypress="return onlyNumber(event);"></form:input>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mobile</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.cellPhone" onkeypress="return onlyNumber(event);" title="Mobile không được để trống"></form:input>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.email" cssClass="email" title="Email không được để trống"></form:input>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">CMT</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.identification" cssClass="cmt"></form:input>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày cấp</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.strdateIssue" cssClass="toCurrentDate"></form:input>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nơi cấp</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.placeIssue"></form:input>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đối tác sử dụng</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:checkbox path="su.isPartner" onchange="checkPartner();"></form:checkbox>
				</div>
			</div>
			<div class="company">
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Công ty<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="su.company.id" id="companyId" onchange="loadDepartmentByCompany();">
							<form:option value="">Hãy chọn</form:option>
							<c:forEach items="#{companies}" var="item">
								<option value="${item.id}">
									<c:out value="${item.code}" /> -
									<c:out value="${item.name}" />
								</option>
							</c:forEach>
						</form:select>
					</div>
					<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Phòng ban<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="su.department.id" id="departmentId" cssClass="required">
							<form:option value=""></form:option>
							<%-- <c:forEach items="#{departments}" var="item">
									<option value="${item.id}">
										<c:out value="${item.code}" /> -
										<c:out value="${item.name}" />
									</option>
								</c:forEach> --%>
						</form:select>
					</div>
				</div>
			</div>
			<div class="Row" style="display: none">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Vai trò</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12"></div>
				<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đang sử dụng</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:checkbox path="su.active" value="true"></form:checkbox>
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mật khẩu</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="su.passWordPlainText" disabled="true"></form:input>
					<a id="btnResetPass" href="#" onclick="resetPass()">Đặt lại mật khẩu</a>
				</div>

			</div>
			<div class="isPartner" style="display: none">
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đối tác</div>
					<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
						<form:select path="su.partner.id" id="partneriD">
							<form:option value="">Hãy chọn</form:option>
							<c:forEach items="#{customers}" var="item">
								<option value="${item.id}">
									<c:out value="${item.code}" /> -
									<c:out value="${item.orgName}" />
								</option>
							</c:forEach>
						</form:select>

					</div>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tình trạng mật khẩu</div>
				<div class="label-static col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:input disabled="true" style="color: red; text-align: left;" class="label-static" path="su.pWExpriredDateStr"></form:input>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
					<div class="row title-page" style="adding-bottom: 20px;">
						<div class="Header3">Vai trò</div>
					</div>
					<div class="easyui-panel" style="padding: 5px">
						<ul id="danhSachQuyen" class="easyui-tree" data-options="animate:true,checkbox:true,cascadeCheck:true,lines:true"></ul>
					</div>
				</div>
				<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
					<div class="row title-page" style="adding-bottom: 20px;">
						<div class="Header3">Nhận email cảnh báo</div>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered" style="table-layout: fixed;" id="table-MailWrng">
							<thead>
								<tr>
									<th>Loại cảnh báo<font color="red">*</font></th>
									<th>Chọn</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="#{lstMailWrngType}" var="pa" varStatus="loop">
									<tr>
										<td width="80%"><form:hidden path="su.lstMail[${loop.index}].id" /> <c:out value="${pa.value}" /></td>
										<td width="20%"><form:checkbox path="su.lstMail[${loop.index}].sysDictParam.id" value='${pa.id}' /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>



		</tiles:putAttribute>
	</form:form>

	<script src="<spring:url value="/page/qtht/quan_ly_nguoi_dung.js" />"></script>
	<spring:url var="sendReportAction" value="/manageUser/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên đăng nhập'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên người dùng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Chức vụ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Công ty'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Phòng ban'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Ca làm việc'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Xưởng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Email'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mobile'
				} ];
				tblCfg.buttons = [ {
					text : 'Download Template',
					attr : {
						id : 'downloadTemp'
					},
					className : 'mainGrid btnImp btn blue',
					action : function(e, dt, node, config) {
						download();
					}
				}, {
					text : 'Import',
					attr : {
						id : 'import'
					},
					className : 'mainGrid btnImp btn blue',
					action : function(e, dt, node, config) {
						uploadDetails();
					}
				}, {
					text : '&#xf067; Thêm mới',
					attr : {
						id : 'btnDtAdd'
					},
					className : 'mainGrid btnDtAdd btn blue fa',
					action : function(e, dt, node, config) {
						addNew();
					}
				} ];
			}

			function uploadDetails() {
				$('#file-select_imp').val('');
				$('#modal_dialog_file').modal();
			}

			function download() {
				window.open('manageUser?method=download').focus();
			}

			function upload() {
				var files = document.getElementById('file-select_imp').files;
				if (files.length == 0) {
					alert('Chưa chọn file!');
					return;
				}
				var formData = new FormData();
				formData.append("fileName", files[0].name);
				formData.append("inputFile", files[0]);
				formData.append("tokenIdKey", $('#tokenIdKey').val());
				formData.append("tokenId", $('#tokenId').val());
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${sendReportAction}', true);
				xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
				$.loader({
					className : "blue-with-image-2"
				});

				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						$('#modal_dialog_file').modal('hide');
						if (xhr.responseText == '') {
							alert('Thực hiện thành công!', function() {
								window.location.reload();
							});
						} else {
							alert(xhr.responseText)
						}

					} else {
						alert('Import không thành công');
					}
				};
				xhr.send(formData);

			}

			var listBaseStateRMCU = [];
			var initRmcodeUrl = '', tblDataRmcode;
			$(document).ready(function() {
				$('#sTab01').css('display', 'inline');
				$('#sTab02').css('display', 'none');
				$('#tab01').addClass("current");
				$('#tab02').removeClass("current");
				$('#tab01').css('color', '#006ecb');
				$('#tab02').css('color', '#8E8E8E');

			});

			$(document).ready(function() {
				$('.btnDtDelete').hide();
			});

			function loadTableRmcode(id, companyId) {
				var tokenIdKey = $('#tokenIdKey').val();
				var tokenId = $('#tokenId').val();

				tblDataRmcode = $('#tblDsNoiDung123').dataTable({
					"bJQueryUI" : true,
					"sPaginationType" : "full_numbers",
					"iDisplayLength" : 10,
					"bProcessing" : true,
					"bFilter" : bFilter,
					"bServerSide" : true,
					"sAjaxSource" : initRmcodeUrl,
					"ordering" : false,
					"fnServerData" : function(initRmcodeUrl, aoData, fnCallback) {
						addFormData(aoData, $('#theForm').serializeObject());
						aoData.push({
							name : "userId",
							value : id
						});
						aoData.push({
							name : "tokenIdKey",
							value : tokenIdKey
						});
						aoData.push({
							name : "tokenId",
							value : tokenId
						});
						aoData.push({
							name : "companyId",
							value : companyId
						});
						$.ajax({
							"dataType" : 'json',
							"type" : "POST",
							"url" : initRmcodeUrl,
							"data" : aoData,
							"success" : function(result) {
								fnCallback(result);
								try {
									instanceUseResult(result);

								} catch (err) {
									// console.print('Instance does not use result more');
								}
								$.loader("close");
								$(".date").datepicker({
									dateFormat : 'dd/mm/yy',
									showButtonPanel : true,
									changeMonth : true,
									changeYear : true,
									yearRange : "-50:+50"
								});
								initCheck();

							}
						});
					},
					"initComplete" : function(settings, json) {
						try {
							instanceFindComplete(tblDataRmcode.fnGetData().length);
							$("#input-datepicker").datepicker({
								dateFormat : 'dd/mm/yy',
								changeMonth : true,
								changeYear : true,
								showButtonPanel : true,
								yearRange : "-50:+50"
							});
						} catch (err) {

						}
					},
					"oLanguage" : {
						"sLengthMenu" : hien_thi + "_MENU_" + ban_ghi,
						"sZeroRecords" : " ",
						"sInfo" : hien_thi + " _START_ " + den + " _END_ " + cua + " _TOTAL_ " + ban_ghi,
						"sInfoEmpty" : hien_thi + " " + tu + " 0 " + toi + " 0 " + tren_tong_so + " 0 " + ban_ghi,
						"sInfoFiltered" : "( " + da_loc_tu + " _MAX_ " + tong_so_ban_ghi + " )",
						"oPaginate" : {
							"sFirst" : "&#xf049;",
							"sLast" : "&#xf050;",
							"sEnter" : "&#xf061;",
							"sPrevious" : "&#xf04a;",
							"sNext" : "&#xf04e;"
						},
						"sSearch" : tu_khoa
					},
					"bDestroy" : true,
					scrollX : bScrollX,
					"fnDrawCallback" : function(oSettings) {
						if (typeof fnDrawCallback != typeof undefined) {
							fnDrawCallback(oSettings);
						}
					}
				});
			}

			function checkPartner() {
				if ($("input[name='su.isPartner']:checked").length > 0) {
					$('.isPartner').css("display", 'block');
					$('.company').css("display", 'none');
				} else {
					$('.isPartner').css("display", 'none');
					$('.company').css("display", 'block');
				}
			}

			function updateDataTableRm() {

			}

			function editRmcodeUser(id) {
				$.loader({
					className : "blue-with-image-2"
				});
				clearDiv('divDetail');
				var tokenIdKey = $('#tokenIdKey').val();
				var tokenId = $('#tokenId').val();
				$.getJSON($('#theForm').attr('action') + '?method=editRmcodeUser', {
					"id" : id,
					"tokenIdKey" : tokenIdKey,
					"tokenId" : tokenId
				}).done(function(res, status, xhr) {
					var result = chkJson(res, xhr);
					if (!result) {
						alert(res);
						return;
					}
					listBaseStateRMCU = JSON.parse(res.rmCodeUsesBinding);
					loadTableRmcode(id);
					$('#btnDel').css('display', '');
					$('#divGrid').css('display', 'none');
					$('#divDetail').css('display', 'inline');
					$("#id").val(res.id);
					$.loader('close');
				}).error(function(res) {
					alert(res.responseText, function() {
						$.loader('close');
					});
				});

			}

			function editUser(id) {
				$.loader({
					className : "blue-with-image-2"
				});
				clearDiv('divDetail');
				var tokenIdKey = $('#tokenIdKey').val();
				var tokenId = $('#tokenId').val();
				$.getJSON(editUrl, {
					"id" : id,
					"tokenIdKey" : tokenIdKey,
					"tokenId" : tokenId
				}).done(function(res) {
					if (typeof beforeEdit == 'function') {
						beforeEdit(res);
					}
					$('#departmentId').html(res.strDepartment);
					$('select[name="su.factoryUnit"]').html(res.strBssFactoryUnits);
					binding(res);
					/*     for (var prop in res) {
					        // select
					        $("select[name$='." + prop + "']").each(function () {
					            var temp = res[prop];
					            if (temp === true)
					                temp = 'true';
					            if (temp === false)
					                temp = 'false';
					            $(this).val(temp);
					        });
					        if ('company' === prop) {
					        	$("select[name$='." + prop + ".id']").each(function () {
					                var temp = res[prop];
					                if (temp === true)
					                    temp = 'true';
					                if (temp === false)
					                    temp = 'false';
					                $(this).val(temp);
					            });
					        }

					        // input, textarea
					        $("input[name$='." + prop + "'][type!='radio']" + ", textarea[name$='." + prop + "']").each(function () {
					            $(this).val(res[prop]);
					        });

					        // radio
					        $('input[type="radio"][name$=".' + prop + '"]').each(function () {
					            // Gan gia tri
					            //$(this).val(res[prop]);
					            // Check theo id
					            //$('#'+prop+res[prop]).prop('checked', true);
					            $('input[name$=".' + prop + '"][value="' + res[prop] + '"]').prop('checked', true);
					        });

					        // checkbox
					        $('input[type="checkbox"][name$=".' + prop + '"]').each(function () {
					            $(this).prop('checked', res[prop]);
					        });
					    } */
					//select2
					$('#divDetail select').each(function(iIndex) {
						$(this).select2();
					});
					$('#btnDel').css('display', '');
					$('#divGrid').css('display', 'none');
					$('#divDetail').css('display', 'inline');

					if (typeof afterEdit == 'function')
						afterEdit(id, res);
					$.loader('close');

				});
			}
			function binding(res) {
				$('#divDetail input[type!="button"],#divDetail select,#divDetail textarea').each(function() {
					if ($(this).attr('name') === undefined || $(this).attr('name').trim().length == 0)
						return;

					var value;
					try {
						value = eval('res' + $(this).attr('name').substring($(this).attr('name').indexOf('.')));
					} catch (err) {
						if ($(this).is("select")) {
							$(this).val('');
							$(this).select2();
						}
						return;
					}
					if ($(this).is("select")) {
						if (value === true)
							value = 'true';
						else if (value === false)
							value = 'false';
						$(this).val(value);
						$(this).select2();
						return;
					}
					if ($(this).prop('type') == 'radio') {
						$('input[name="' + $(this).attr('name') + '"][value="' + value + '"]').prop('checked', true);
					} else if ($(this).prop('type') == 'checkbox') {
						$(this).prop('checked', value);
					} else {
						$(this).val(value);
					}
				});
			}

			function loadDepartmentByCompany() {
				var companyId = $('#companyId').val();
				if (companyId === null || companyId === '') {
					$("#departmentId").html('');
					$('select[name="su.factoryUnit"]').html('');
					return;
				}

				$.loader({
					className : "blue-with-image-2"
				});
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadDepartmentByCompany&companyId=' + companyId,
					method : 'GET',
					success : function(res) {
						//$("#departmentId").html(res);
						$("#departmentId").html('');
						$('select[name="su.factoryUnit"]').html('');
						var departments = res.lstDepartment;
						var factoryUnits = res.lstBssFactoryInit;
						if (factoryUnits != undefined) {
							var factoryUnit = $('select[name="su.factoryUnit"]');
							factoryUnit.append($("<option>").attr('value', '').text('--Chọn--'));
							;
							for (var i = 0; i < factoryUnits.length; i++) {
								factoryUnit.append($("<option>").attr('value', factoryUnits[i].id).text(
										factoryUnits[i].code));
								;
							}
						}
						if (departments != undefined) {
							var department = $('#departmentId');
							department.append($("<option>").attr('value', '').text('--Chọn--'));
							;
							for (var i = 0; i < departments.length; i++) {
								department.append($("<option>").attr('value', departments[i].id).text(
										departments[i].code + '-' + departments[i].name));
								;
							}
						}
						$.loader("close");
					},
					error : function() {
						alert('Không k?t n?i ???c v?i Server vui ḷng liên h? v?i qu?n tr? h? th?ng!');
						$.loader("close");
					}
				});
			}

			function initCheck() {
				for (var i = 0; i < listBaseStateRMCU.length; i++) {
					var itemdata = listBaseStateRMCU[i];
					$("#" + itemdata.rmCd).checked = itemdata.check;
					$("#st-" + itemdata.rmCd).checked = itemdata.startDt;
					$("#en-" + itemdata.rmCd).checked = itemdata.expireDt;
					$("#mt-" + itemdata.rmCd).checked = itemdata.masterRM;
					if (!itemdata.check) {
						$("#st-" + itemdata.rmCd).css('display', 'none');
						$("#en-" + itemdata.rmCd).css('display', 'none');
						$("#mt-" + itemdata.rmCd).css('display', 'none');
					} else {
						$("#st-" + itemdata.rmCd).css('display', 'block');
						$("#en-" + itemdata.rmCd).css('display', 'block');
						$("#mt-" + itemdata.rmCd).css('display', 'block');
					}
				}
				var checkall = true;
				for (var i = 0; i < listBaseStateRMCU.length; i++) {
					if (listBaseStateRMCU[i].check == null || listBaseStateRMCU[i].check == false) {
						checkall = false;
					}
				}
				$('#sla').prop('checked', checkall);
			}

			function selectAll(isSlAll) {
				$('.sli').prop('checked', isSlAll == true);
				for (var i = 0; i < listBaseStateRMCU.length; i++) {
					listBaseStateRMCU[i].check = isSlAll;
				}
				initCheck();

			}

			function selectItemMasterRM(ctrItem, index) {
				var item = listBaseStateRMCU[index];
				item.ischange = true;
				item.masterRM = ctrItem.checked;
				if (ctrItem.checked) {
					for (var i = 0; i < listBaseStateRMCU.length; i++) {
						var itemdata = listBaseStateRMCU[i];
						if (item.rmCd != itemdata.rmCd) {
							itemdata.masterRM = false;
							$('#mt-' + itemdata.rmCd).prop('checked', false);
						}
					}
				}

			}

			function selectItem(ctrItem, index) {
				var item = listBaseStateRMCU[index];
				if (ctrItem.checked) {
					$("#st-" + item.rmCd).css('display', 'block');
					$("#en-" + item.rmCd).css('display', 'block');
					$("#mt-" + item.rmCd).css('display', 'block');
				} else {
					$("#st-" + item.rmCd).css('display', 'none');
					$("#en-" + item.rmCd).css('display', 'none');
					$("#mt-" + item.rmCd).css('display', 'none');
				}
				$("#" + item.rmCd).checked = ctrItem.checked;

				item.ischange = true;
				item.check = ctrItem.checked;

				var checkAll = true;
				for (var i = 0; i < listBaseStateRMCU.length; i++) {
					if (listBaseStateRMCU[i].check == false) {
						checkAll = false;
					}
				}
				$('#sla').prop('checked', checkAll);
			}

			function changeStDate(ctrItem, index) {
				var item = listBaseStateRMCU[index];
				item.startDt = ctrItem.value;
			}

			function changeEnDate(ctrItem, index) {
				var item = listBaseStateRMCU[index];
				item.expireDt = ctrItem.value;
			}

			function loadcbHanMuc() {
				reloadCbHanMuc('', '');
				reloadCbHanMucQt('', '');
			}

			function defaultValue() {
				$('#btnResetPass').css('display', 'none');
				//$('#active').val(true);
				$('input[name="su.active"]').prop('checked', true);
				//$("#theForm_su_partnerId option[value='']").remove();
				initRoleTree();
				formatLayout();

			}

			/*function resetPass() {
			    if ($('input[name="su.email"]').val().trim() == "") {
			        alert('Vui ḷng nh?p ??a ch? Email');
			        return false;
			    }
			    else {
			        $.ajax( {
			            url : $('#theForm').attr('action') + '?method=resetPass', method : 'POST', data :  {
			                id : $('#id').val(), tokenId : $('#tokenId').val(), tokenIdKey : $('#tokenIdKey').val()
			            },
			            success : function (severtrave) {
			                alert('<s:text name = "re_succ"/>');
			            },
			            err : function (severtrave) {
			                alert('<s:text name = "no_reset"/>');
			            }
			        });
			    }
			}
			 */

			function initRoleTree() {
				var sUrl = $('#theForm').attr('action') + '?method=treeRole' + '&suID=' + $('#id').val();
				if ($('select[name="su.jobId"]') != undefined && $('select[name="su.jobId"]').val() != undefined
						&& $('select[name="su.jobId"]').val().trim() != '')
					sUrl += '&parentRoleId=' + $('select[name="su.jobId"]').val().trim();
				$.ajax({
					async : false,
					type : "POST",
					url : sUrl,
					data : {
						tokenId : $('#tokenId').val(),
						tokenIdKey : $('#tokenIdKey').val()
					},
					success : function(data1) {
						var myJSON = JSON.parse(data1);
						$('#danhSachQuyen').tree({
							data : myJSON.treeRoles
						});
					},
					error : function(data1) {

					}
				});
			}

			function formatLayout() {
				//$('.dataTables_scrollHead').width($('#tblRight').width());
				$('.dataTables_scrollHeadInner').width("100%");
				//$('.dataTables_scrollHead').width($('#tblRight').width());
				$('.dataTable.no-footer').css('width', '100%');
			}

			function changeRootRole() {
				initRoleTree($('#input[name="su.jobId"]').val());
				var arrRight = $('input[name="su.roles"]').val().split(',');
				for (var i = 0; i < arrRight.length; i++) {
					$('td input[type="checkbox"][id^="chk"][id$="' + arrRight[i].trim() + '"]').prop('checked', true);
				}

			}

			function afterEdit(sid, res) {
				$('#sTab01').css('display', 'inline');
				$('#sTab02').css('display', 'none');
				$('#tab01').addClass("current");
				$('#tab02').removeClass("current");
				$('#tab01').css('color', '#006ecb');
				$('#tab02').css('color', '#8E8E8E');
				initRoleTree($('#input[name="su.jobId"]').val());
				$('#btnResetPass').css('display', 'inline');
				/*var arrRight = $('input[name="su.roles"]').val().split(',');
				    for(var i=0; i< arrRight.length; i++){
				        $('td input[type="checkbox"][id^="chk"][id$="'+arrRight[i].trim()+'"]').prop('checked',true);
				    }*/
				formatLayout();
				checkPartner();
				//$('#spartnerId').val($('#hidPartnerId').val());
			}

			function reloadCbHanMucQt(hanmucGiaoDich, hanMucNgay) {
				// Truong hop Vietin nhap nsd cho doi tac can rebuil combo suhanMucGiaoDichId suhanMucNgayid
				$.ajax({
					async : false,
					type : "POST",
					url : 'LtdCategory.action?method=getHm&typex=qt&partnerId='
							+ $('select[name="su.partnerId"]').val(),
					success : function(res) {
						$('#hanMucGiaoDichChuyenTienQTId').html(res['hanMucGd']);
						$('#hanMucNgayChuyenTienQTId').html(res['hanMucNgay']);
						$('#hanMucGiaoDichChuyenTienQTId').val(hanmucGiaoDich);
						$('#hanMucNgayChuyenTienQTId').val(hanMucNgay);

					},
					error : function(data1) {

					}
				});
			}

			function reloadCbHanMuc(hanmucGiaoDich, hanMucNgay) {
				// Truong hop Vietin nhap nsd cho doi tac can rebuil combo suhanMucGiaoDichId suhanMucNgayid
				$.ajax({
					async : false,
					type : "POST",
					url : 'LtdCategory.action?method=getHm&partnerId=' + $('select[name="su.partnerId"]').val(),
					success : function(res) {
						$('#suhanMucGiaoDichId').html(res['hanMucGd']);
						$('#suhanMucNgayid').html(res['hanMucNgay']);
						$('#suhanMucGiaoDichId').val(hanmucGiaoDich);
						$('#suhanMucNgayid').val(hanMucNgay);

					},
					error : function(data1) {

					}
				});
			}

			function expand(idquyen, pathQuyen) {
				if ($('#tr' + pathQuyen).hasClass('open')) {
					$('#tr' + pathQuyen).removeClass('open');
					$('#tr' + pathQuyen).addClass('close');
					$('#fnt' + pathQuyen).html('(-) ');
				} else {
					$('#tr' + pathQuyen).removeClass('close');
					$('#tr' + pathQuyen).addClass('open');
					$('#fnt' + pathQuyen).html('(+) ');
				}
				if ($('#tr' + pathQuyen).hasClass('close'))// Truong hop dong                
					$('tr[id^="tr' + pathQuyen + '"][id!="tr' + pathQuyen + '"]').css('display', 'none');
				else {
					// Truong hop mo, mo de quy
					openRecur(idquyen, pathQuyen);
				}
				formatLayout();
			}

			function openRecur(idquyen, pathQuyen) {
				if ($('#tr' + pathQuyen).hasClass('close'))
					return;
				$('.' + idquyen).each(function() {
					$(this).css('display', 'table-row');
					if ($(this).hasClass('trparent')) {
						var trId = $(this).attr('id');
						var childIdquyen = trId.split(pathQuyen + '_')[1];
						var childPathQuyen = pathQuyen + '_' + childIdquyen;
						openRecur(childIdquyen, childPathQuyen);
					}
				});
			}

			function selectRight(idQuyen, pathQuyen, isSelect) {
				$('input[type="checkbox"][id^="chk' + pathQuyen + '"]').prop('checked', isSelect);
			}

			function beforeSave() {
				var nodes = $('#danhSachQuyen').tree('getChecked', [ 'checked' ]);
				var rights = '';
				for (var i = 0; i < nodes.length; i++) {
					if (rights != '')
						rights += ',';
					rights += nodes[i].id;
				}
				$('input[name="su.roles"]').val(rights);

			}

			function extraClear() {
				$('td input[type="checkbox"][id^="chk"]').prop('checked', false);
			}

			function LoadDeviceSecurity(partnerCode) {
				$.loader({
					className : "blue-with-image-2"
				});
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadDevice&partnerCode=' + partnerCode,
					method : 'POST',
					success : function(severtrave) {
						$('input[name="secDiviceCode"]').val(severtrave);
						$('#txtUsername').val($('input[name="su.username"]').val());
						$('#txtName').val($('input[name="su.name"]').val());
						$('#oldPIN').val($('input[name="su.pin"]').val());
						$.loader("close");
					},
					error : function() {
						alert('Không k?t n?i ???c v?i Server vui ḷng liên h? v?i qu?n tr? h? th?ng!');
						$.loader("close");
					}
				});
			}

			function ValidateData() {
				var mess = '<s:text name="required"/>';
				if ($('input[name="su.secDiviceCode"]').val() == '') {
					$('input[name="su.secDiviceCode"]').attr("placeholder", mess);
					return false;
				}
				if ($('input[name="su.pin"]').val() == '') {
					$('input[name="su.pin"]').attr("placeholder", mess);
					return false;
				} else {
					if ($('input[name="su.pin"]').val().length != 6) {
						alert('Mă PIN ph?i g?m 6 ch? s?.');
						return false;
					} else if (!$.isNumeric($('input[name="su.pin"]').val())) {
						alert('Mă PIN ph?i g?m 6 ch? s?.');
						return false;
					}
				}
				return true;
			}

			function changePIN(OTP) {
				var username = $('#txtUsername').val();
				var Pin = $('input[name="su.pin"]').val();
				;

				$.loader({
					className : "blue-with-image-2"
				});

				$.ajax({
					url : $('#theForm').attr('action') + '?method=changePIN&UserID=' + $('#id').val() + '&oldPin='
							+ $('#oldPIN').val() + '&PIN=' + Pin + '&username=' + username + '&OTP=' + OTP,
					method : 'POST',
					success : function(severtrave) {
						if (severtrave == "0") {
							alert(thuc_hien_thanh_cong, function() {
								$("#modal_OTP").modal('hide');
							});
						} else
							alert(severtrave);

						$.loader("close");
					},
					error : function() {
						alert('Không xác th?c ???c mă OTP');
						$.loader("close");
					}
				});
			}

			
			function resetPass() {
				$.loader({
					className : "blue-with-image-2"
				});
				$.ajax({
					url : $('#theForm').attr('action') + '?method=resetPass',
					data : {
						"su.id" : $('#id').val()
					},
					success : function(data, status, xhr) {
						$.loader("close");
						var jsnResult = chkJson(data, xhr);
						if (jsnResult) {
							alert('Thực hiện thành công!', function() {
								$('input[name="su.passWordPlainText"]').val(data['passWordPlainText']);
							});
						}
					},
					error : function(data) {
						$.loader("close");
						alert('Thực hiện không thành công');
					}
				});

			}

			function save() {
				$('#id').prop('disabled', false);
				if (!$('#theForm').valid())
					return;
				beforeSave();
				$.loader({
					className : "blue-with-image-2"
				});
				$.ajax({
					async : false,
					type : "POST",
					url : saveUrl,
					data : $('#theForm').serialize(),
					success : function(data, status, xhr) {
						$.loader("close");
						if(chkJson(data,xhr)){
							alert(thuc_hien_thanh_cong, function(){
								binding(data);
								findData();
							});
						}else{
							alert(data);
						}
					}
				});

			}

			function reloadDepartment(conpanyId) {
				$.ajax({
					url : $('#theForm').attr('action') + "?method=reloadDepartment",
					success : function(res) {
						var data = $.map(res, function(obj) {
							obj.text = obj.name;
							return obj;
						});
						$('#sdepartmentId').empty();
						$('#sdepartmentId').append('<option value=""></option>');
						$('#sdepartmentId').select2({
							data : data
						});
					},
					data : {
						conpanyId : conpanyId
					}
				});
			}
		</script>
	</tiles:putAttribute>


</tiles:insertDefinition>
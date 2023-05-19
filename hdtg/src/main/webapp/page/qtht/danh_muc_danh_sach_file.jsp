<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="catalog">
	<%-- <tiles:putAttribute name="title" value="Danh sách File" /> --%>
	<tiles:putAttribute name="formInf">
		<spring:url value="/qiFile" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>

	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>

		<tiles:putAttribute name="catGrid">
			<form:hidden path="to" id="to" />
			<form:hidden path="readOnly" id="readOnly" />
			<form:hidden path="parentId" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Loại tài liệu
				</div>
				<div class="col-md-5 col-lg-5 col-sm-12 col-xs-12">
					<form:select path="type" onchange="findData()">
						<form:option value="">- Chọn -</form:option>
						<c:forEach items="#{lstFile}" var="item">
							<form:option value="${item.id}">
								<c:out value="${item.value}" />
							</form:option>
						</c:forEach>
					</form:select>
				</div>

			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="sysFile.id" id="id" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Loại tài liệu<font color="red">*</font>
				</div>
				<div class="col-md-5 col-lg-5 col-sm-12 col-xs-12">
					<form:select path="sysFile.sysDictParam.id" cssClass="required">
						<form:option value="">- Chọn -</form:option>
						<c:forEach items="#{lstFile}" var="item">
							<form:option value="${item.id}">
								<c:out value="${item.value}" />
							</form:option>
						</c:forEach>
					</form:select>
				</div>

			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chọn</div>
				<div class="col-md-5 col-lg-5 col-sm-12 col-xs-12">
					<input type="file" id="file-xmlCfg" class="file-xmlCfg" name="inputFile" accept=".docx, .pdf" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<form:textarea cols="5" path="sysFile.descripton" />
				</div>
			</div>
			<div class="Row">

				<!-- <div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12">
					<input type="button" onclick="uploadXmlCfg()" value="Upload" id=""
						class="sm-btn"
						style="font-size: 12px; padding: 0 10px; background: #339; color: #fff; border: 0; border-radius: 3px;">
				</div> -->
			</div>

		</tiles:putAttribute>
	</form:form>
	<spring:url var="sendXmlCfgFile" value="/qiFile/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.bScrollX = true;
				if ($('#to').val() == 'pro') {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Loại tài liệu'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Ngày upload'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tên file'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Kích thước(Mb)'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Công đoạn'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã quản lý'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Người upload'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : ''
					}

					];
				} else {
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Loại tài liệu'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Ngày upload'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tên file'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Kích thước(Mb)'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã quản lý'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Người upload'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : ''
					}

					];
				}

			}

			$(document).ready(function() {
				lstFileJson = JSON.parse('${lstFileJson}');
				$('.btnDtDelete').hide();
				$('.HeaderText').hide();
				$('#btnDel').hide();
				if($('#readOnly').val())
					$('#btnDtAdd').hide();
			});

			function defaultValue() {
				$('.rowXmlCfg, .dataXml').css('display', 'none');
			}

			function upload() {
				//fileSelect.files = null;
				var templateId = $('#id').val();
				if (templateId != null && templateId != '') {
					$('#file-select').val('');
					$('#modal_upload_file_sx').modal();
				} else {
					alert("Phải lưu biểu mẫu trước khi tải phải lên");
				}
			}

			var getUrlParameter = function getUrlParameter(sParam) {
				var sPageURL = window.location.search.substring(1), sURLVariables = sPageURL.split('&'), sParameterName, i;

				for (i = 0; i < sURLVariables.length; i++) {
					sParameterName = sURLVariables[i].split('=');

					if (sParameterName[0] === sParam) {
						return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
					}
				}
				return false;
			};

			function save() {
				$('#file-select').val('');
				var files = [];
				$('.file-xmlCfg').each(function() {
					files.push($(this)[0].files);
				});
				var files = document.getElementById('file-xmlCfg').files;
				if (files.length == 0) {
					alert('Chưa chọn tệp!');
					return;
				}
				var formData = new FormData();
				for (var i = 0; i < files.length; i++) {
					var file = files[i];
					formData.append("fileName", file.name);
					formData.append("inputFile", file);
					formData.append("id", $('#id').val());
					formData.append("type", $('select[name="sysFile.sysDictParam.id"]').val());
					formData.append("descripton", $('textarea[name="sysFile.descripton"]').val());
					formData.append("parentId", getUrlParameter('parentId'));
				}
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${sendXmlCfgFile}', true);
				xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
				$.loader({
					className : "blue-with-image-2"
				});
				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						if (xhr.responseText == '') {
							alert('Thực hiện thành công!', function() {
								$("#spanXmlFileName").text(files[0].name);
								$('.dataXml').css('display', 'inline-flex');
								$("#xmlCfgFileName").val(files[0].name);
								cancel();
								findData();
							});
						} else {
							$('#result').val(xhr.responseText);
						}
					} else {
						alert('Upload không thành công');
					}
				};
				xhr.send(formData);
			}
			function downloadFile(id) {
				window.open($('#theForm').attr('action') + '&method=download&id=' + id);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
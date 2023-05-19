<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="simple_catalog">
	<tiles:putAttribute name="title" value="Tài liệu hướng dẫn sử dụng" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/userDoc" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
					<div class="Table" id = "divSearchInf">
					</div>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="userDocumentation.id" id="id"/> 
		<div class="box-custom">
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã template<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="userDocumentation.code"
						cssClass="required uppercase ascii" title="Mã không được để trống" />
				</div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên template<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="userDocumentation.name" cssClass="required"
						title="Tên không được để trống" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mô tả</div>
				<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
					<form:input path="userDocumentation.description" />
				</div>
			</div>

			<div class="Row dataXml">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tải tài liệu</div>
				<div class="col-md-7 col-lg-7 col-sm-12 col-xs-12">
					<a title="Tải file" href="#" onclick="downloadXml()"><span
						id="spanXmlFileName"></span></a>
				</div>

			</div>
			<div class="Row rowXmlCfg">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Chọn</div>
				<div class="col-md-7 col-lg-7 col-sm-12 col-xs-12">
					<input type="file" id="file-xmlCfg" class="file-xmlCfg"
						name="inputFile" accept=".docx, .pdf" />
				</div>
				<div class="label-static col-md-1 col-lg-1 col-sm-12 col-xs-12">
					<input type="button" onclick="uploadXmlCfg()" value="Upload" id=""
						class="sm-btn"
						style="font-size: 12px; padding: 0 10px; background: #339; color: #fff; border: 0; border-radius: 3px;">
				</div>
			</div>

		</div>
	</tiles:putAttribute>
	</form:form>
	<spring:url var="sendXmlCfgFile" value="/userDoc/upload"></spring:url>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){	
	            tblCfg.aoColumns = [			 				
		            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			
			$(document).on('click', '.btnDtAdd', function() {
				document.getElementById("userDocumentation.code").disabled = false;
			});

			function beforeSave() {
				document.getElementById("userDocumentation.code").disabled = false;
			}

			function beforeEdit(res) {
				document.getElementById("userDocumentation.code").disabled = true;

				console.log(res);
				if (res.enableReExec == "1") {
					$("#enableReExecCombobox").prop("checked", "checked");
				}
				if (res.enableResend == "1") {
					$("#enableResendCombobox").prop("checked", "checked");
				}
			}

			function afterEdit(id, res) {
				$("#spanXmlFileName").text(res.xmlCfgFileName);
				$('input[name="userDocumentation.xmlCfgFileName"]').val(res.xmlCfgFileName);
				$('.rowXmlCfg').css('display', 'inline-flex');
				if (res.xmlCfgFileName != undefined) {
					$(".dataXml").css("display", "inline-flex");
				} else {
					$(".dataXml").css("display", "none");

				}
			}

			function defaultValue() {
				$('.rowXmlCfg, .dataXml').css('display', 'none');
			}
			function upload(){
	            //fileSelect.files = null;
	            var templateId = $('#id').val();
	            if( templateId != null && templateId != ''){
	                 $('#file-select').val('');
	                $('#modal_upload_file').modal();
	            }else{
	                alert("Phải lưu biểu mẫu trước khi tải phải lên");
	            }
	        }
	        
			function uploadXmlCfg() {
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
			function downloadXml() {
				var cacheAction = $('#theForm').attr('action');
	            $('#theForm').attr('action',$('#theForm').attr('action') + '?method=downloadDoc&id=' + $('#id').val());
	            document.getElementById("theForm").submit();
	            $('#theForm').attr('action',cacheAction);
			}

		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	

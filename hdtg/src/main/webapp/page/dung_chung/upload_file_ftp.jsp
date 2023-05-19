<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="common.util.FormatNumber"%>
<spring:url var="sendXmlCfgFile" value="/qiFile/upload"></spring:url>
<div id="modalChangeWorkExeDetail"
	class="modal fade bd-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg" id='repaireRs'>
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Upload file</h4>
			</div>
			<div class="modal-body">
				<div class='Table'>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Loại tài liệu<font color="red">*</font>
						</div>
						<div class="col-md-5 col-lg-5 col-sm-12 col-xs-12">
							<select name="sysFile.sysDictParam.id" cssClass="required" id="lstFile">
							</select>
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Chọn
						</div>
						<div class="col-md-5 col-lg-5 col-sm-12 col-xs-12">
							<input type="file" id="file-xmlCfg" class="file-xmlCfg"
								name="inputFile" />
						</div>
					</div>
					<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô
							tả</div>
						<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
							<textarea rows="3" name="sysFile.descripton" id='fileDes'></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="fileUpload.upload();">Upload</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" lang="javascript">
	var fileUpload;
	function FileUpload(fileRecordId, fileId, lstFileType) {
	    //properties
	    this.fileRecordId = fileRecordId;
	    this.fileId = fileId;
	    this.lstFileType = lstFileType;
	    fileUpload = this;
	}
	
	FileUpload.prototype.open = function(){
		//lstFileType
		clearDiv('modalChangeWorkExeDetail');
		// Ve combo
		var mapData = $.map(this.lstFileType, function(obj) {
			var x = new Object();
			x.id = obj.id;
			x.text = obj.value;
			return x;
		});
		$('#lstFile').empty();
		if(this.lstFileType.length!=1){
			$('#lstFile').append('<option value="">- Chọn -</option>');
		}
		$('#lstFile').select2({
			data : mapData
		});
		
		$('#modalChangeWorkExeDetail').modal('show');
	};
	FileUpload.prototype.upload = function(){
		var files = document.getElementById('file-xmlCfg').files;
		if (files.length == 0) {
			alert('Chưa chọn tệp!');
			return;
		}
		var formData = new FormData();
		for (var i = 0; i < files.length; i++) {
	          var file = files[i];
			formData.append("fileName", file.name);
			formData.append("type", $('select[name="sysFile.sysDictParam.id"]').val());
			formData.append("descripton", $('#fileDes').val());
			formData.append("inputFile", file);
			if(this.fileId!=undefined)
				formData.append("id", this.fileId);
			formData.append("parentId", this.fileRecordId);
		
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
						$('#modalChangeWorkExeDetail').modal('hide');
					});
				} else {
					$('#result').val(xhr.responseText);
				}
			}
		};
		xhr.send(formData);
	}
</script>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="simple_catalog">
	<tiles:putAttribute name="title" value="Danh sách nhóm quyền" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/role" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>

	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="Table" id="divSearchInf">
					<div class="Row"></div>
					<!-- <div class="divaction" align="center">
				            <input type="button" onclick="createNew=true;addNew();reloadTree();" value="Thêm mới" class="btn blue" aria-invalid="false"/> 
				        </div> -->
					<!-- <div align="center" class="HeaderText">&#8203;</div> -->

				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="sysRoles.id" id="id" />
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Mã nhóm<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysRoles.code" cssClass="required" title="Mã nhóm không được để trống" />
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Tên nhóm<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysRoles.descriptionVi" cssClass="required" title="Tên nhóm không được để trống" />
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nhóm quyền cha</div>
				<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
					<select class="form-control" name="sysRoles.sysRoles.id" id="RoleList">
						<option value="">- Chọn -</option>
						<c:forEach items="#{danhSachNhomQuyen}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.descriptionVi}" />
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="Row">
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Page mặc định<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysRoles.defaultPage" title="Mã nhóm không được để trống" />
				</div>
				<div class='col-md-2 col-lg-2 col-sm-12 col-xs-12'></div>
				<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
					Thứ tự ưu tiên<font color="red">*</font>
				</div>
				<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
					<form:input path="sysRoles.defaultPagePriority" class="digits" title="Tên nhóm không được để trống" />
				</div>
			</div>
			<div class="Row">
				<div class="easyui-panel" style="padding: 5px">
					<ul id="danhSachQuyen" class="easyui-tree" data-options="animate:true,checkbox:true,cascadeCheck:true,lines:true"></ul>
				</div>
			</div>
			<form:hidden path="sysRoles.rights" />
		</tiles:putAttribute>
	</form:form>

	<%-- <link href="<spring:url value="/plugin/jquery-easyui/easyui.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/jquery-easyui/jquery.easyui.min.js" />"></script> --%>

	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			function initParam(tblCfg) {
				tblCfg.aoColumns = [ {
					"sWidth" : "5%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sWidth" : "10%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã'
				}, {
					"sWidth" : "20%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên'
				}, {
					"sWidth" : "10%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Nhóm quyền cha'
				}, {
					"sWidth" : "20%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Page mặc đinh'
				}, {
					"sWidth" : "10%",
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thứ tự ưu tiên'
				} ];
			}

			$(document).ready(function() {
				$('.btnDtDelete').hide();
			});

			$(document).on('click', '.btnDtAdd', function() {
				createNew = true;
				addNew();
				reloadTree();
			});

			function defaultValue() {
				$('#status').val(true);
				$('#statustrue').prop('checked', true);
				formatLayout();
			}
			$(document).ready(function() {
				// Khoi tao cay quyen
				initRightTree();
				var windowWidth = window.innerWidth;
				window.onresize = function(event) {
					if (windowWidth > 740 && $('#divGrid').css('display') == 'none')
						$('#divDetail').css('display', 'inline');
					else if (windowWidth < 740 && $('#divGrid').css('display') == 'none')
						$('#divDetail').css('display', 'inline-grid');
				};
			});
			// build lai combo
			function instanceValidate() {
				if ($('#id').val() != '') {// Sua
					if ($('#parentId').val() == $('#id').val()) {// id cha trung id con
						alert('<s:text name="quyen_cha_khong_duoc_trung_voi_quyen_con"/>');
						return false;
					}
				}
				return true;
			}
			function instanceSuccess(data) {
				console.log('data response : ' + data);
				if ("ConstraintViolationException" == data) {
					alert('Dữ liệu đã tồn tại!');
					return;
				} else if (data != '') {
					alert(data);
					return;
				} else {
					alert(thuc_hien_thanh_cong, function() {
						findData();

						$('#divGrid').css('display', 'inline');
						$('#divDetail').css('display', 'none');
						$.ajax({
							url : $('#theForm').attr('action') + '?method=reload&id=' + $('#id').val(),
							method : 'GET',
							data : {
								tokenId : $('#tokenId').val(),
								tokenIdKey : $('#tokenIdKey').val()
							},
							success : function(severtrave) {
								$('#parentId').html(severtrave);
							}
						});
					});
				}

			}
			function afterDelete() {
				$.ajax({
					url : $('#theForm').attr('action') + '?method=reload&id=' + $('#id'),
					method : 'GET',
					success : function(severtrave) {
						$('#parentId').html(severtrave);
					}
				});
			}
			//end build

			var myJSON;
			function initRightTree() {
				$('#danhSachQuyen').tree('reload');
				$.ajax({
					async : false,
					type : "GET",
					url : $('#theForm').attr('action') + '?method=treeRight',
					data : {
						tokenId : $('#tokenId').val(),
						tokenIdKey : $('#tokenIdKey').val()
					},
					success : function(data1) {
						myJSON = JSON.parse(data1);
						treeDanhSachQuyen = $('#danhSachQuyen').tree({
							data : myJSON
						});
					},
					error : function(data1) {

					}
				});
			}

			function reloadTree() {
				$('#danhSachQuyen').tree('reload');
				initRightTree();
				$('#danhSachQuyen').tree({
					data : myJSON.treeRight
				});
			}

			function formatLayout() {
				//$('.dataTables_scrollHead').width($('#tblRight').width());
				$('.dataTables_scrollHeadInner').width("100%");
				//$('.dataTables_scrollHead').width($('#tblRight').width());
				$('.dataTable.no-footer').css('width', '100%');
				var windowWidth = window.innerWidth;
				if (windowWidth > 740 && $('#divGrid').css('display') == 'none')
					$('#divDetail').css('display', 'inline');
				else if (windowWidth < 740 && $('#divGrid').css('display') == 'none')
					$('#divDetail').css('display', 'inline-grid');
			}
			function afterEdit(sid) {
				//reload tree data
				$('#danhSachQuyen').tree('reload');
				$('#danhSachQuyen').tree({
					data : myJSON.treeRight
				});
				var arrRight = $('input[name="sysRoles.rights"]').val().split(',');
				for (var i = 0; i < arrRight.length; i++) {
					if (arrRight[i] != null && arrRight[i] != '' && arrRight[i] != 'null') {
						//var node = $('#danhSachQuyen').tree('find', arrRight[i].trim());
						//$('#danhSachQuyen').tree('check', node.target);
						var allChildrenNode = $('#danhSachQuyen').tree('getChildren');
						for (var x = 0; x < allChildrenNode.length; x++) {
							if (allChildrenNode[x].children == null) {
								if (allChildrenNode[x].id == arrRight[i]) {
									$('#danhSachQuyen').tree('check', allChildrenNode[x].target);
								}
							}
						}
					}
				}
				formatLayout();
				//$("#spartnerId option[value='']").remove();
			}
			function expand(idquyen, pathQuyen) {
				if ($('#tr' + pathQuyen).hasClass('open')) {
					$('#tr' + pathQuyen).removeClass('open');
					$('#tr' + pathQuyen).addClass('close');
					$('#fnt' + pathQuyen).html('(+) ');
				} else {
					$('#tr' + pathQuyen).removeClass('close');
					$('#tr' + pathQuyen).addClass('open');
					$('#fnt' + pathQuyen).html('(-) ');
				}
				if ($('#tr' + pathQuyen).hasClass('close'))// Truong hop dong                
					$('tr[id^="tr' + pathQuyen + '"][id!="tr' + pathQuyen + '"]').css('display', 'none');
				else {// Truong hop mo, mo de quy
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
				//var nodes = $('#danhSachQuyen').tree('getChecked', ['checked','indeterminate']);
				var nodes = $('#danhSachQuyen').tree('getChecked', [ 'checked' ]);
				var rights = '';
				for (var i = 0; i < nodes.length; i++) {
					if (rights != '')
						rights += ',';
					rights += nodes[i].id;
				}
				$('input[name="sysRoles.rights"]').val(rights);
			}
			function extraClear() {
				$('td input[type="checkbox"][id^="chk"]').prop('checked', false);
			}
		</script>
	</tiles:putAttribute>

</tiles:insertDefinition>
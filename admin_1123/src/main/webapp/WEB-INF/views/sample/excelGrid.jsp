<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="/resources/js/sample/excelController.js"></script>

<article class="con_box">
	<div data-ng-controller="ExcelController as vm">
		<button type="button" class="btn btn-success" ng-click="reset()">Reset Grid</button>
		<br /> <br />
		STORE ID<input type="text" data-ng-model="vm.searchDataReq.storeId">
		DISPLAY ID<input type="text" data-ng-model="vm.searchDataReq.displayId">
		INS ID<input type="text" data-ng-model="vm.searchDataReq.insId">
		<input type="button" ng-click="search()" value="SEARCH">
		<div class="gridbox">
		<%-- 
			<input type="file"> 에 아래 내용 추가 
			accept=".xls,.xlsx"    : accpet = upload 할 excel 확장자 선언
			fileread=""			   : fileread = excelDirective 지시자선언부
			opts="vm.gridOptions"  : opts = excelDirective 내에 scope value, vm.gridOptions = controller 내 그리드 설정내용
			multiple="false"
		--%>
		<%-- 
			vm.exportXLSX(vm.gridOptions)
			exportXLSX 함수 안아 그리드 내용을 바인딩해준다.
		--%>
			<div class="myMenuArea">
			<input id="myMenu1" type="file" accept=".xls,.xlsx" fileread="" opts="gridOptions" multiple="false" />
			<input id="myMenu2" type="button" class="btn btn-success" ng-click="exportXLSX(gridOptions)" value="Export as XLSX">
			</div>
			<div class="grid" data-ui-grid="gridOptions" 
				data-ui-grid-move-columns
				data-ui-grid-resize-columns
				data-ui-grid-pagination
				data-ui-grid-auto-resize
				data-ui-grid-selection
				data-ui-grid-exporter
				data-ui-grid-edit >
			</div>
		</div>
	</div>
</article>
<script type="text/javascript">
	window.onload = function() {
		console.log("b");
		var grid = $('.tb_util');
		var panel = $('.ui-grid-pager-panel');
		grid.append(panel);
	}
</script>
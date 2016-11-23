<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>

<script type="text/javascript" src="/resources/js/app/pms.app.product.list.js"></script>

<article class="con_box" >

<div class="box_type1" style="margin-top:10px;">
	<div ng-app="productListApp"  ng-controller="pms_productListApp_controller as ctrl" class="gridbox gridbox600" >
	  <div ui-grid="grid_product" data-ui-grid-resize-columns 
							data-ui-grid-auto-resize
							data-ui-grid-pagination
							data-ui-grid-cell-nav
							data-ui-grid-selection 
							data-ui-grid-exporter
							data-ui-grid-edit
							data-ui-grid-validate
							ui-grid-pinning class="grid"></div>
	</div>

</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>
<script>
var productListApp = angular.module("productListApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule'
                                                       , 'ui.date']);
Constants.message_keys = ["common.label.confirm.save"];

productListApp.controller("pms_productListApp_controller", function($window, $scope, $filter, productService, commonService, gridService, commonPopupService) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	var columnDefs = [
			             { field: 'productId',			width:'100', colKey:"pmsProduct.productId", 	linkFunction : "openProductPopup", pinnedLeft:true},      			
			             { field: 'name',				width:'400', colKey:"pmsProduct.name", 			linkFunction : "openProductPopup" ,pinnedLeft:true},      
			             { field: 'salePrice',			width:'100', colKey:"pmsProduct.salePrice", 	linkFunction : "openProductPopup" ,pinnedLeft:true},          
			             { field: 'productTypeName',	width:'100', colKey:"pmsProduct.productTypeCd",	vKey:"pmsProduct.productTypeCd" }, 
			             { field: 'saleStateName',		width:'100', colKey:"pmsProduct.saleStateCd", 	vKey:"pmsProduct.saleStateCd"	}, 
			             { field: 'pmsBrand.name',		width:'100', colKey:"pmsBrand.name"},            
			             { field: 'productGubun', 		width:'100', colKey:"c.pms.product.gubun"},                         
			             { field: 'pmsCategory.name',	width:'100', colKey:"c.pmsCategory.category" },
			             { field: 'dmsCategoryName',	width:'100', colKey:"c.dmsDisplaycategory.category" },
			             { field: 'saleStartDt', 		width:150, colKey:"pmsProduct.saleStartDt", 	cellFilter : "yyyymmdd"},
			             { field: 'saleEndDt', 			width:150, colKey:"pmsProduct.saleEndDt", 	type:'date', enableCellEdit : true},
						 { field: 'insId', 				width:'100', colKey:"c.grid.column.insId"  },
						 { field: 'insDt', 				width:'100', colKey:"c.grid.column.insDt"}
			         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_product",	//mandatory
			url :  '/api/pms/product',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			
			gridOptions : {             //optional
				checkBoxEnable : false
			},
			callbackFn : function(){	//optional
				$scope.myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
});


</script>
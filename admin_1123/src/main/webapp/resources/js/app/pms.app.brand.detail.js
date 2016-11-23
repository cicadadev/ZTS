var brandDetailApp = angular.module("brandDetailApp", ['commonServiceModule', 'pmsServiceModule', 'dmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule', 'ui.date']);


// 브랜드 정보 영역 컨트롤러
brandDetailApp.controller("pms_brandBasicInfoController", function($window, $scope, brandService, commonService, commonPopupService) {
	
	// 부모창의 scope
	var pScope = $window.opener.$scope;
	
	// 객체 생성
	$scope.search = {};
	
	// 브랜드 정보 조회
	$scope.pmsBrand = {};
	this.getBrand = function() {
		$scope.templateList = angular.copy(pScope.templateList);
		
		brandService.getBrand(pScope.search.brandId, function(response) {
			$scope.pmsBrand = response;
			
			angular.forEach($scope.templateList, function(template) {
				if (response.templateId == template.templateId) {
					$scope.pmsBrand.templateId = response.templateId;
					$scope.pmsBrand.name = response.name;
					
					pScope.templateId = response.templateId;
					pScope.templateName = template.name;
				}
			});
		});
	}
	
	// 탭 전환 버튼 클릭
	this.changeTab = function(tab) {
		if(confirm(pScope.MESSAGES["dms.catalog.moveTap.message1"])) {
			if (tab == 'shop') {
				$window.location.href="/pms/brand/popup/shopDetail";
			}	
		}
	}
		
	// 브랜드 정보 수정
	this.updateBrandInfo = function() {
		if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			//폼 체크
			if (!commonService.checkForm($scope.form)) {
				return;
			}
			
			// 로고 이미지 체크
			if ($scope.pmsBrand.logoImg == null || $scope.pmsBrand.logoImg == '') {
				alert(pScope.MESSAGES["pms.brand.logoImg.validation"]);
				return;
			}
			
			var brandList = [];
			brandList.push($scope.pmsBrand);
			
			brandService.updateBrand(brandList, function(response) {
				if (response.success) {
					$scope.search.templateId = $scope.pmsBrand.templateId;
					
					alert(pScope.MESSAGES["common.label.alert.save"]);					
					pScope.brand_grid.loadGridData();
				} else {
					alert(data.resultMessage);
				}
			});
		};		
	}
	
	//이미지 미리보기 삭제
	this.deletelogoImg = function(name) {
		commonService.deleteFile($scope.pmsBrand.logoImg, function(data) {
			$scope.pmsBrand.logoImg = null;
	    });
		angular.element(document.querySelector('img[name='+ name+']')).attr("src", null);
		angular.element(document.querySelector('#' + name + 'Path')).val(null);
	}
	
	// 취소(닫기)
	this.close = function() {
		$window.close();
	}
	
});

// 브랜드샵 정보 영역 컨트롤러
brandDetailApp.controller("pms_brandShopInfoController", function($window, $scope, $filter,
		brandService, commonService, gridService, displayService, commonPopupService) {
	
	// 부모창의 scope
	var pScope = $window.opener.$scope;
	
	$window.$scope = $scope;
	
	// 객체생성
	$scope.search = {displayItemDivId : pScope.search.brandId };
	$scope.pmsBrand = {};
	
	// 코너 목록 그리드
	var cornerGrid_columnDefs =  [        
	    { field: 'displayId', colKey: "c.dmsDisplay.cornerId", vKey: "c.dmsDisplay.cornerId" }
		, { field: 'name', colKey: "c.dmsDisplay.cornerName" }
		, { field: 'displayItemType', colKey: "c.dmsDisplay.corner.cornerType", vKey: "c.dmsDisplay.corner.cornerType" }
		, { field: 'insId', colKey: 'c.grid.column.insId' , userFilter :'insId,insName'}
	    , { field: 'insDt', colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
	    , { field: 'updId', colKey: 'c.grid.column.updId' , userFilter :'updId,updName'}
	    , { field: 'updDt', colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
	];
		
	var cornerGrid_Param = {
		scope : $scope, 
		gridName : 'grid_corner',
		url : '/api/dms/category/corner',
		searchKey : "search", 
		columnDefs : cornerGrid_columnDefs,
		gridOptions : {
			checkBoxEnable: false,
			enableRowSelection: true,
			rowSelectionFn : function(row){
				$scope.getCornerDetail(row.entity);
			}
		},
		callbackFn : function() {
			$scope.search.templateId = pScope.templateId;
			$scope.corner_grid.loadGridData();
		}
	};
	$scope.corner_grid = new gridService.NgGrid(cornerGrid_Param);
	
	// 상품 목록 그리드
	$scope.cornerItemGrid_Param = {
		scope : $scope, 
		gridName : 'grid_cornerItemList',
		url : '',
		searchKey : "search", 
		columnDefs : [],
		gridOptions : {
		},
		callbackFn : function() {
		}
	};
	$scope.cornerItem_grid = new gridService.NgGrid($scope.cornerItemGrid_Param);
	
	// 배너 목록 그리드(C_Type 템플릿)
	var bannerGrid_columnDefs =  [        
	    { field: 'text1', colKey: "c.dmsDisplayitem.imgbanner.text", linkFunction: "openImgPopup" }
	    , { field: 'img1', colKey: "c.dmsDisplayitem.imgbanner", linkFunction: "openImgPopup" }
		, { field: 'url1', colKey: "c.dmsDisplayitem.imgbanner.url", linkFunction: "openImgPopup" }
		, { field: 'addValue', colKey: "dmsDisplayitem.addValue", vKey: "dmsDisplayitem.addValue", enableCellEdit: true }
		, { field: 'sortNo', colKey: 'c.dmsDisplayitem.sortNo', vKey: "c.dmsDisplayitem.sortNo", enableCellEdit: true }
        , { field: 'startDt', colKey: "dmsDisplayitem.startDt",	vKey: "dmsDisplayitem.startDt",  enableCellEdit: true, type:'datetime', periodStart : true}
        , { field: 'endDt', colKey: "dmsDisplayitem.endDt", vKey: "dmsDisplayitem.endDt",    enableCellEdit: true, type:'datetime', periodEnd : true }	
	    , { field: 'displayYn', colKey: 'dmsDisplayitem.displayYn' , enableCellEdit: true, cellFilter: 'displayYnFilter' }
        , { field: 'insId', colKey: 'c.grid.column.insId' , userFilter :'insId,insName'}
        , { field: 'insDt', colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
	    , { field: 'updId', colKey: 'c.grid.column.updId' , userFilter :'updId,updName'}
	    , { field: 'updDt', colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
	];
		
//	var bannerGrid_Param = {
//		scope : $scope, 
//		gridName : 'grid_banner',
//		url : '/api/dms/corner/items/img',
//		searchKey : "search", 
//		columnDefs : bannerGrid_columnDefs,
//		gridOptions : {
//		},
//		callbackFn : function() {
//		}
//	};
//	$scope.banner_grid = new gridService.NgGrid(bannerGrid_Param);
	
	// 코너 상세
//	$scope.brandCorner = {};
	$scope.getCornerDetail = function(data) {
		$scope.search.displayId = data.displayId;
		$scope.search.displayItemTypeCd = data.displayItemTypeCd;
//		$scope.brandCorner.cornerItemTypeCd = data.displayItemTypeCd;
		
//		var templateName = pScope.templateName;
//		if (templateName.indexOf("C_Type") >= 0) {
//			$scope.templateType = "C";			
//		} else if (templateName.indexOf("A_Type") >= 0) {
//			$scope.templateType = "A";
//		} else if (templateName.indexOf("B_Type") >= 0) {
//			$scope.templateType = "B";			
//		}
		
//		if ($scope.templateType == "C") {
//			$scope.banner_grid.loadGridData();
//		} else {
		$scope.cornerItemGrid_Param.scope = $scope;
		$scope.cornerItemGrid_Param.gridName = "grid_cornerItemList";
		$scope.cornerItemGrid_Param.searchKey = "search";
		$scope.cornerItemGrid_Param.url = displayService.getCornerItemUrl(data.displayItemTypeCd);
		$scope.cornerItemGrid_Param.columnDefs = displayService.getCornerItemColDef($scope, data.displayItemTypeCd);
		$scope.cornerItemGrid_Param.callbackFn = function() {
			$scope.cornerItem_grid.loadGridData();
		};		
		$scope.cornerItem_grid = new gridService.NgGrid($scope.cornerItemGrid_Param);
//		}
		$scope.cornerGrid = true;
	}
	
	// 아이템 등록 팝업 (상품, 기획전, 이미지, html)
	$scope.openItemRegPopup = function(itemType) {
		
		$scope.search.displayItemNo = null;
		var gridName = "";
//		if ($scope.templateName == "C") {
//			gridName = 'banner_grid';
//		} else {
			gridName = 'cornerItem_grid';
//		}
		var param = {
			displayId : $scope.search.displayId,
			displayItemDivId :  $scope.search.displayItemDivId,
			gridName : gridName,
			itemType : itemType
		};
		
		displayService.initCornerItemReg($scope, param);
	}
	
	// 이미지 상세 팝업
	$scope.openImgPopup = function(fieldValue, rowEntity){
    	$scope.search.displayId = rowEntity.displayId;
		$scope.search.displayItemNo = rowEntity.displayItemNo;
		popupwindow("/dms/corner/popup/imageDetail", "배너 상세", 1000, 600);
    }
	
	// 탭 전환 버튼 클릭
	this.changeTab = function(tab) {
		if(confirm(pScope.MESSAGES["dms.catalog.moveTap.message1"])) {
			if (tab == 'brand') {
				$window.location.href="/pms/brand/popup/detail";
			} else if (tab == 'corner') {
				$window.location.href="/pms/brand/popup/cornerDetail";
			}	
		}
	}
	
});


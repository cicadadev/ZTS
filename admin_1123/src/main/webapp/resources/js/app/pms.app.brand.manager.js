var brandManagerApp = angular.module("brandManagerApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'dmsServiceModule', 'commonPopupServiceModule',
                                                         'ui.date', 'ngCkeditor']);

//메시지
Constants.message_keys = ["common.label.confirm.save", "common.label.alert.save", "pms.brand.template.null.message", 
                          "pms.brand.logoImg.validation", "pms.brand.img1.validation", "pms.brand.img2.validation",
                          "dms.catalog.moveTap.message1"];


// 브랜드 관리 컨트롤러
brandManagerApp.controller("pms_brandManagerApp_controller", function($compile, $window, $scope, $filter,
		brandService, commonService, gridService, displayService) {
	
	// 팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	// 객체 생성
	$scope.search = {};
	
	angular.element(document).ready(function() {
		$scope.search.searchKeyword = 'name';
		
		// 템플릿 유형 selectbox
		$scope.templateList = [];
		$scope.search.templateTypeCd = 'TEMPLATE_TYPE_CD.BRAND';
		displayService.getTemplateListByType($scope.search, function(response) {
			angular.forEach(response, function(template) {
				$scope.templateList.push({
					templateId : template.templateId,
					name : template.name
				});
			});		
		});
		
		commonService.init_search($scope,'search');
	});
	
	// 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	// 브랜드 목록 그리드	
	var columnDefs = [        
       { field: 'brandId', colKey: 'c.pmsBrand.brandId', linkFunction: 'openPopupDetail(row.entity)', type: 'number' }
       , { field: 'name', colKey: 'pmsBrand.name', linkFunction: 'openPopupDetail(row.entity)' }
       , { field: 'displayYn', colKey: 'pmsBrand.displayYn', enableCellEdit: true, cellFilter: 'displayYnFilter' }
       , { field: 'dmsTemplate.templateTypeCd', colKey: 'c.pmsBrand.templateType' }
       , { field: 'insId', colKey: 'c.grid.column.insId' , userFilter :'insId,insName'}
       , { field: 'insDt', colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
       , { field: 'updId', colKey: 'c.grid.column.updId' , userFilter :'updId,updName'}
       , { field: 'updDt', colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
	]
	
	var gridParam = {
		scope : $scope, 
		gridName : 'grid_brand',
		url : '/api/pms/brand',
		searchKey : 'search', 
		columnDefs : columnDefs,
		gridOptions : {		
		},
		callbackFn : function() {
		}
	};
	$scope.brand_grid = new gridService.NgGrid(gridParam);
	
	// 브랜드 검색
	$scope.searchBrandData = function() {
		if ($scope.search.searchKeyword == 'brandId') {
			var brandIds = $scope.search.brand;
			
			if (brandIds != null && brandIds != '') {
				$scope.search.brandIds = '';
				
				angular.forEach(brandIds.split(','), function(brandId) {
					$scope.search.brandIds += "'"+brandId+"',";
				});
				
				brandIds = $scope.search.brandIds;
				$scope.search.brandIds = brandIds.substring(0, brandIds.length-1);
			}
		}
		$scope.brand_grid.loadGridData();
	}
	
	$scope.$watch('search.searchKeyword', function(newValue, oldValue) {
		if (newValue != oldValue) {
			$scope.search.brand = null;
		}
	});
	
	// 브랜드 상세 팝업 호출
	$scope.openPopupDetail = function(data) {
		$scope.search.brandId = data.brandId;
		
		var winURL = Rest.context.path + "/pms/brand/popup/detail";
		popupwindow(winURL, "브랜드 상세", 960, 550);
	}
	
	// 브랜드 등록 팝업 호출
	this.openPopupInsert = function() {		
		var winURL = Rest.context.path + "/pms/brand/popup/insert";		
		popupwindow(winURL, "브랜드 등록", 960, 550);
	}
	
	// 브랜드 전시여부 수정
	this.saveGridData = function() {
		var gridData = $scope.brand_grid.getData();
		angular.forEach(gridData, function(brand) {
			brand.templateId = brand.dmsTemplate.templateId;
		});
		
		var pUrl = Rest.context.path + "/api/pms/brand/update";
		$scope.brand_grid.saveGridData(pUrl);
	}
	
});


// 브랜드 등록 팝업 컨트롤러
brandManagerApp.controller("insertPopup_controller", function($window, $scope, $rootScope, 
		brandService, displayService,  commonService, displayTreeService) {
	
	// 부모창의 scope
	var pScope = $window.opener.$scope;
	
	// 브랜드 등록 팝업 init Method
	$scope.pmsBrand = {};
	this.registerInit = function() {
		$scope.pmsBrand.displayYn = 'Y';
		
		// 브랜드 템플릿 selectbox
		$scope.templateList = angular.copy(pScope.templateList);
	}
	
	//이미지 미리보기 삭제
	this.deletelogoImg = function(name) {
		commonService.deleteFile($scope.pmsBrand.logoImg, function(data) {
			$scope.pmsBrand.logoImg = null;
	    });
		angular.element(document.querySelector('img[name='+ name+']')).attr("src", null);
		angular.element(document.querySelector('#' + name + 'Path')).val(null);
	}

	// 브랜드 등록
	this.insertBrand = function(){
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		// 로고 이미지 체크
		if ($scope.pmsBrand.logoImg == null || $scope.pmsBrand.logoImg == '') {
			alert(pScope.MESSAGES["pms.brand.logoImg.validation"]);
			return;
		}

        brandService.insertBrand($scope.pmsBrand, function(response){
        	if (response.content != null) {
        		$scope.pmsBrand.brandId = response.content;
        		
				alert(pScope.MESSAGES["common.label.alert.save"]);
				pScope.searchBrandData();
				
				common.safeApply(pScope);
//				$window.close();
			} else {
				alert(data.resultMessage);
			}			
		});
	}
	
	// 취소(닫기)
	this.close = function() {
		$window.close();
	}
	
});

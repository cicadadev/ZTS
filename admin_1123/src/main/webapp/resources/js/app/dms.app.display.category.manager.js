var displayCategoryApp = angular.module("displayCategoryApp", ['ngRoute', 'commonServiceModule', 'dmsServiceModule','pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule', 'ui.date']);

//메시지
Constants.message_keys = ["common.label.confirm.save", "common.label.confirm.delete", "common.label.alert.save", "common.label.alert.delete", 
                          "pms.category.register", "pms.category.register.validation1", "pms.category.delete.validation1", "pms.category.attribute.register.validation1",
                          "pms.category.attribute.register.validation2", "pms.category.attribute.register", 
                          "pms.category.attribute.register.validation3", "pms.category.attribute.register.null", 
                          "pms.category.attribute.register.validation4", "pms.category.register.validation2", 
                          "pms.category.rating.null.message", "pms.category.rating.alert.register", "c.dmsCatalog.moveTap.message1",
                          "common.label.alert.cancel", "common.label.confirm.cancel"];


//탭을 위한 라우트 설정
displayCategoryApp.config(function ($routeProvider) {
	$routeProvider
	    .when('/tab1', {templateUrl: '/dms/displayCategory/tab1', controller: 'ctrlTab1'})
	    .when('/tab2', {templateUrl: '/dms/displayCategory/tab2', controller: 'ctrlTab2'})
	    .when('/empty', {template: '<div>전시카테고리를 선택해 주세요.</div>'})
	    .otherwise({redirectTo: '/empty'});
});

// 카테고리 관리 메인 컨트롤러
displayCategoryApp.controller("dms_displayCategoryManagerApp_controller", function($compile, $window, $rootScope, $scope, $filter, $location,
		displayCategoryService, commonService, commonPopupService, displayTreeService, gridService) {

	// 팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	// 표준카테고리 조회
	$scope.trees = {};
//	$scope.getCategories = function() {
//		// 카테고리 트리 목록 조회
//		displayCategoryService.getCategories({}, function(response) {
//			$scope.trees = response;
//		});
//	}
		
	
	// 카테고리 하위 열기/닫기 
	$scope.openFolder = function(index, icon) {
		displayTreeService.openTree(index, icon, $scope.trees, "displayCategoryId", "upperDisplayCategoryId");
	}
	
	// 카테고리 상세 조회
	$scope.getCategoryDetail = function(e, dmsDisplaycategory) {
		
		// 등록중인 카테고리 선택시
		if(common.isEmpty(dmsDisplaycategory.displayCategoryId)){
			return;
		}
		
		// 카테고리를 등록중일 때 확인 
		if($scope.regFlag && !confirm("카테고리 등록을 취소하시겠습니까?")){
			return;
		}
		
		//등록중이던 카테고리가 있으면 삭제
		if($scope.regFlag){
			displayTreeService.deleteNewNode($scope.trees, "displayCategoryId");
			$scope.regFlag = false;
		}
		
		// 클릭한 노드 선택
		if(e && !$(e.target).hasClass("active")){
			$(".category .list_dep a").removeClass("active");
			$(e.target).addClass("active");
		}
		
		
		// TODO 확인
		$rootScope.dmsDisplaycategory = angular.copy(dmsDisplaycategory);// tab1에서 사용
		$rootScope.selectedCategory = angular.copy(dmsDisplaycategory);//선택된 객체
		$location.url('/tab1');
	}
	
	// 카테고리 등록 버튼 클릭
	this.register = function() {
		
		if(common.isEmptyObject($rootScope.dmsDisplaycategory)){
			alert('상위 카테고리를 선택해 주세요.');
			return;
		}
//		if($rootScope.dmsDisplaycategory.leafYn=='Y'){
//			alert("리프여부가 Y에는 하위 카테고리를 등록 할 수 없습니다.");
//			return;
//		}
		//  카테고리번호가 없으면 등록중 화면 ( 등록중인 카테고리 선택시 무반응 처리)
		if(common.isEmpty($rootScope.dmsDisplaycategory.displayCategoryId)){
			return;
		}
		$location.url('/tab1');
		$scope.regFlag = true;// 카테고리 등록중 플래그
		
		
		// 부모 노드 ID
		var upperTreeId = $rootScope.dmsDisplaycategory.displayCategoryId;
		
		// 등록 화면 기본값 설정
		$rootScope.dmsDisplaycategory = { 
			upperDisplayCategoryId : upperTreeId,
			useYn : "Y",
			leafYn : "N",
			displayYn : "Y"
		};
		
		// 신규 노드 삽입
		var newTree = {
			displayCategoryId : null,
			upperDisplayCategoryId : upperTreeId,
			name : "신규 카테고리"// 트리에서 보일 임시 카테고리 명
		};
		displayTreeService.insertNewNode($scope.trees, newTree, "displayCategoryId", "upperDisplayCategoryId");
	}
	
	// 등록 취소
	$scope.cancelReg = function(){
		if(!confirm("등록을 취소 하시겠습니까?")){
			return;
		}
		displayTreeService.deleteNewNode($scope.trees, "displayCategoryId");
		$scope.regFlag = false;// 등록중 플래그 false
		$rootScope.dmsDisplaycategory = null;//선택객체 초기화
		$rootScope.selectedCategory=false;
		$location.url('/empty');
	}
	
	//트리 재조회 & 현재 카테고리 선택
	$scope.reloadTreeAndSelectItem = function(category){	
		
		// 트리조회
		displayCategoryService.getDisplayCategories({}, function(response) {
			$scope.trees = response;
			
			if(!common.isEmptyObject(category)){
				// 트리에서 현재 노드 선택
				displayTreeService.selectTreeItem($scope.trees, category.displayCategoryId, category.upperDisplayCategoryId, "displayCategoryId", "upperDisplayCategoryId");
					
				// 카테고리 상세 조회
				$scope.getCategoryDetail(null, category);
			}
				
			common.safeApply($scope);
			
		});		
	}

	
	// 카테고리 정보 저장
	$scope.updateCategory = function() {
		
		if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
			return;
		}
		
		// 폼 유효성 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		// 디비 저장
		displayCategoryService.updateDisplayCategoryInfo($rootScope.dmsDisplaycategory, function(response) {
			
			alert($scope.MESSAGES["common.label.alert.save"]);
			
			$rootScope.dmsDisplaycategory.displayCategoryId = response.content;
			
			// 트리 재구성
			$scope.reloadTreeAndSelectItem($rootScope.dmsDisplaycategory);

			// 등록중 플래그 false
			$scope.regFlag = false;
			
		});
	}
	
	
	
	// 카테고리 삭제
	$scope.deleteCategory = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.delete"])) {
			return;
		}
//		var param = { categoryId : $scope.dmsDisplaycategory.categoryId };
		displayCategoryService.deleteDisplayCategory($rootScope.dmsDisplaycategory.displayCategoryId, function(response) {
			if (response.success) {
				
				alert($scope.MESSAGES["common.label.alert.delete"]);
				
				$rootScope.dmsDisplaycategory = null;
				$scope.reloadTreeAndSelectItem($rootScope.dmsDisplaycategory);
				
				$location.url("/empty");
			}else{
				alert(response.resultMessage);
			}
		});
	}
	
	$scope.tabClick = function(tab){
		if ($rootScope.dmsDisplaycategory.displayCategoryId == null || $rootScope.dmsDisplaycategory.displayCategoryId == undefined) {
			alert("기본정보 저장후 이동가능합니다.");
			return;
		}
		
		if (!confirm("저장되지 않은 정보는 사라집니다. 이동하시겠습니까?")) {
			return;
		}
		
		// 탭이동시마다 객체 초기화
		$rootScope.dmsDisplaycategory = angular.copy($rootScope.selectedCategory);//
		
		$location.url('/'+tab);
	}
	
//	// 트리 로딩
//	$scope.reloadTreeAndSelectItem(null);
	
	// promotion 상세에서 호출
	if(angular.isDefined($window.opener) && $window.opener != null) {
		var pScope = $window.opener.$scope;
		$scope.reloadTreeAndSelectItem(pScope.paramCategory);
	}else{
		// 트리 로딩
		$scope.reloadTreeAndSelectItem(null);
	}
	
});



//전시카테고리 기본정보 탭 컨트롤러
displayCategoryApp.controller("ctrlTab1", function($rootScope, $scope, displayService) {
	
	$rootScope.tab1 = "on";
	$rootScope.tab2 = "";
	
	$scope.search = {};
	
	$scope.search.templateTypeCd = 'TEMPLATE_TYPE_CD.DISPLAYCATEGORY';
	displayService.getTemplateListByType($scope.search, function(response) {
		$scope.templateList = response;
	});
	
	common.resizeIframe();//iframe높이 조절
});


//전시카테고리 코너정보 탭 컨트롤러
displayCategoryApp.controller("ctrlTab2", function($compile, $window, $rootScope, $scope, $filter, $location,
		displayCategoryService, displayService, commonService, displayTreeService, gridService) {
	
	$rootScope.tab1 = "";
	$rootScope.tab2 = "on";
	$window.$scope = $scope;
	
	$scope.search = { 
		templateId : $rootScope.dmsDisplaycategory.templateId,
		displayItemDivId : $rootScope.dmsDisplaycategory.displayCategoryId		
	};	
	
	
	// 코너 목록 그리드
	var gridName1 = 'grid_displayCategoryCornerList';
	var cornerGrid_columnDefs =  [        
	    { field: 'displayId', colKey: "c.dmsDisplay.cornerId", vKey: "c.dmsDisplay.cornerId" }
		, { field: 'name', colKey: "c.dmsDisplay.cornerName" }
		, { field: 'displayItemType', colKey: "c.dmsDisplay.corner.cornerType", vKey: "c.dmsDisplay.corner.cornerType" }
		, { field: 'updId', userFilter :'updId,updName',	colKey: "c.grid.column.updId" }
		, { field: 'updDt', colKey: "c.grid.column.updDt", enableCellEdit: false, cellFilter: 'date:\'yyyy/MM/dd\'' }
	];
		
	var cornerGrid_Param = {
		scope : $scope, 
		gridName : gridName1,
		url : '/api/dms/category/corner',
		searchKey : "search", 
		columnDefs : cornerGrid_columnDefs,
		gridOptions : {
			checkBoxEnable: false,
			enableRowSelection: true,
			noUnselect : true,
			rowSelectionFn : function(row){
				$scope.getCornerDetail(row.entity);
			},
		},
		callbackFn : function() {
//			$scope.search.templateId = categoryArray.templateId;
			$scope.corner_grid.loadGridData();
		}
	};
	$scope.corner_grid = new gridService.NgGrid(cornerGrid_Param);
	
	// 코너 상세
	$scope.getCornerDetail = function(data) {
		$scope.search.displayId = data.displayId;
		$scope.search.displayItemTypeCd = data.displayItemTypeCd;
//		$scope.displayCategoryInfo.displayItemTypeCd = data.displayItemTypeCd;
	
		$scope.itemGridShow = true;
		
	    var gridUrl =  displayService.getCornerItemUrl(data.displayItemTypeCd);
	    var columnDefs = displayService.getCornerItemColDef($scope, data.displayItemTypeCd);
		    
	    var param = {
	    	scope : $scope,
	    	gridName : 'grid_cornerItemList',
	    	searchKey : "search",
    		url : gridUrl,
    		columnDefs : columnDefs,
    		gridOptions : { 
    			//pagination : false
			},
    		callbackFn : function() {
    			$scope.cornerItem_grid.loadGridData();
    		}
	    }
		$scope.cornerItem_grid = new gridService.NgGrid(param);
	}
	
	//아이템 등록 팝업 (상품, 기획전, 이미지, html)
	$scope.openItemRegPopup = function(itemType){
		var param = {
			displayId : $scope.search.displayId,
			displayItemDivId : $scope.search.displayItemDivId,
			gridName : 'cornerItem_grid',
			itemType : itemType
		};
		
		displayService.initCornerItemReg($scope, param);
	}
	
	
	// 코너 상품 삭제
	$scope.deleteGridData = function() {
		var pUrl = Rest.context.path + "/dms/corner/items/"+$scope.search.displayItemTypeCd+"/delete";
		$scope.cornerItem_grid.deleteGridData();
	}
	
	// 코너 상품 등록
	$scope.saveGridData = function() {
		$scope.cornerItem_grid.saveGridData(null, function(response) {
			
			$scope.getCornerDetail($scope.search);
			
//			$scope.cornerItem_grid.loadGridData();
//			common.safeApply($scope);
		});
	}
	
	// 그리드 리로드
//	$scope.reloadItemGrid = function(){
//		$scope.cornerItem_grid.loadGridData();
//		common.safeApply($scope);
//	}
	
	common.resizeIframe();//iframe높이 조절
});

var categoryApp = angular.module("categoryApp", ['ngRoute', 'commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);

//메시지
Constants.message_keys = ["common.label.confirm.save", "common.label.confirm.delete", "common.label.alert.save", "common.label.alert.delete", 
                          "pms.category.register", "pms.category.register.validation1", "pms.category.delete.validation1", "pms.category.attribute.register.validation1",
                          "pms.category.attribute.register.validation2", "pms.category.attribute.register", 
                          "pms.category.attribute.register.validation3", "pms.category.attribute.register.null", 
                          "pms.category.attribute.register.validation4", "pms.category.register.validation2", 
                          "pms.category.rating.null.message", "pms.category.rating.alert.register", "c.dmsCatalog.moveTap.message1"];

// 표준카테고리 관리 메인 컨트롤러
categoryApp.controller("pms_categoryManagerApp_controller", function($compile, $window, $scope, $filter, $location,
		categoryService, commonService, commonPopupService, displayTreeService, gridService, attributeService) {

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
//		categoryService.getCategories({}, function(response) {
//			$scope.trees = response;
//		});
//	}
		
	$scope.newCategory = false;
	

	
	
	
	var initGrid = function(){
		if(common.isEmpty($scope.pmsCategory.categoryId)){
			$scope.newCategory = true;
		}else{
			$scope.newCategory = false;
		}
		// 별점항목 그리드
		var columnDefs =  [        
	           /*{ field: 'ratingId', colKey: 'pmsCategoryrating.ratingId'},*/
	           { field: 'name', 	colKey: 'pmsCategoryrating.name', 		enableCellEdit: true , vKey : "pmsCategoryrating.name"},
	           { field: 'sortNo', 	colKey: 'pmsCategoryrating.sortNo', 	enableCellEdit: true , vKey : "pmsCategoryrating.sortNo"}
			]
		
		// 속성 목록 그리드
		var attribute_columnDefs =  [
		    /*{ field: 'attributeId', colKey: "c.pmsCategory.attributeId", vKey: "c.pmsCategory.attributeId" , linkFunction: "attributeDetail" }*/
		    { field: 'attributeName', colKey: "c.pmsCategory.attributeName" , linkFunction: "attributeDetail" }	    
		    , { field: 'pmsAttribute.attributeTypeName', colKey: "c.pmsAttribute.type", vKey: "c.pmsAttribute.type" }
		    , { field: 'sortNo', 	colKey: 'pmsCategoryattribute.sortNo', 	enableCellEdit: true , vKey : "pmsCategoryattribute.sortNo"}
		]
		
		var gridParam = {
				scope : $scope, 
				gridName : 'grid_categoryRating',
				columnDefs : columnDefs,
				gridOptions : {
					checkBoxEnable: $scope.newCategory,
					pagination: false
				},
				callbackFn : function(){	//optional
					$scope.categoryRating_grid.setData($scope.pmsCategory.pmsCategoryratings);
				}
			};
			
		
		var attributeGrid_Param = {
			scope : $scope, 
			gridName : 'grid_attributeList',
			columnDefs : attribute_columnDefs,
			gridOptions : {
				checkBoxEnable: $scope.newCategory,
				pagination: false
			},
			callbackFn : function(){	//optional
				$scope.attribute_grid.setData($scope.pmsCategory.pmsCategoryattributes);
			}
		};
		$scope.categoryRating_grid = new gridService.NgGrid(gridParam);
		$scope.attribute_grid = new gridService.NgGrid(attributeGrid_Param);
	}
	
	
	// 표준카테고리 하위 열기/닫기 
	$scope.openFolder = function(index, icon) {
		displayTreeService.openTree(index, icon, $scope.trees, "categoryId", "upperCategoryId");
	}
	
	// 표준카테고리 상세 조회
	$scope.getCategoryDetail = function(e, pmsCategory) {
		
		// 등록중인 카테고리 선택시
		if(common.isEmpty(pmsCategory.categoryId)){
			return;
		}
		
		// 카테고리를 등록중일 때 확인 
		if($scope.regFlag && !confirm("카테고리 등록을 취소하시겠습니까?")){
			return;
		}
		
		//등록중이던 카테고리가 있으면 삭제
		if($scope.regFlag){
			displayTreeService.deleteNewNode($scope.trees, "categoryId");
			$scope.regFlag = false;
		}
		
		// 클릭한 노드 선택
		if(e && !$(e.target).hasClass("active")){
			$(".category .list_dep a").removeClass("active");
			$(e.target).addClass("active");
		}
		
		// 카테고리 정보 조회 ( 별점, 속성 정보 포함 )
		var param = {categoryId : pmsCategory.categoryId};
		categoryService.getCategoryDetail(param,function(response) {
			$scope.pmsCategory = response;
			$scope.pmsCategory.depth = pmsCategory.depth;
			initGrid();// 그리드 초기화
		});
	}
	
	// 표준카테고리 등록 버튼 클릭
	this.register = function() {
		
		if(common.isEmptyObject($scope.pmsCategory)){
			alert('상위 카테고리를 선택해 주세요.');
			return;
		}
		if($scope.pmsCategory.leafYn=='Y'){
			alert("리프여부가 Y에는 하위 카테고리를 등록 할 수 없습니다.");
			return;
		}
		// 4뎁스는 등록 불가
		if($scope.pmsCategory.depth >= 4){
			alert("4뎁스 이상 카테고리 등록 불가 합니다.");
			return;
		}
		
		//  카테고리번호가 없으면 등록중 화면 ( 등록중인 카테고리 선택시 무반응 처리)
		if(common.isEmpty($scope.pmsCategory.categoryId)){
			return;
		}
		
		$scope.regFlag = true;// 카테고리 등록중 플래그
		
		
		// 부모 노드 ID
		var upperTreeId = $scope.pmsCategory.categoryId;
		
		// 등록 화면 기본값 설정
		$scope.pmsCategory = { 
			upperCategoryId : upperTreeId,
			useYn : 'Y',
			leafYn : 'N',
			newIconYn : 'N',
			pointSaveRate : 0,
			secondApprovalYn : 'N'
		};
		
		// 신규 노드 삽입
		var newTree = {
			categoryId : null,
			upperCategoryId : upperTreeId,
			name : "신규 카테고리"// 트리에서 보일 임시 카테고리 명
		};
		
		initGrid();// 그리드 초기화
		
		displayTreeService.insertNewNode($scope.trees, newTree, "categoryId", "upperCategoryId");
		
	}
	
	// 등록 취소
	$scope.cancelReg = function(){
		if(!confirm("등록을 취소 하시겠습니까?")){
			return;
		}
		displayTreeService.deleteNewNode($scope.trees, "categoryId");
		$scope.regFlag = false;// 등록중 플래그 false
		$scope.pmsCategory = null;//선택객체 초기화
	}
	
	//트리 재조회 & 현재 카테고리 선택
	$scope.reloadTreeAndSelectItem = function(category){	
		
		// 트리조회
		categoryService.getCategories({}, function(response) {
			$scope.trees = response;
			
			if(!common.isEmptyObject(category)){
				// 트리에서 현재 노드 선택
				displayTreeService.selectTreeItem($scope.trees, category.categoryId, category.upperCategoryId, "categoryId", "upperCategoryId");
					
				// 카테고리 상세 조회
				$scope.getCategoryDetail(null, category);
			}
				
			common.safeApply($scope);
			
		});		
	}

	
	// 담당 MD 검색
	$scope.searchMD = function(){
		commonPopupService.userPopup($scope, "callback_user", false, "USER_TYPE_CD.MD");
	}
	$scope.callback_user = function(data){
		$scope.pmsCategory.userId = data[0].userId;
		$scope.pmsCategory.mdName = data[0].name;
		common.safeApply($scope);
	}
	
	// 지우개
	$scope.eraser = function() {
		$scope.pmsCategory.userId = "";
		$scope.pmsCategory.mdName = "";
	}
	
	
	// 별점항목 추가
	$scope.addRatingType = function() {
		$scope.categoryRating_grid.addRow({
			name : '',
			useYn : 'Y',
			sortNo : 0,
			categoryId : $scope.pmsCategory.categoryId
		});
	}
	
	// 별점항목 삭제
	this.deleteRatingType = function(){
		$scope.categoryRating_grid.deleteGridData(null, function(){
		});
	}
	
	// 표준카테고리 정보 저장
	$scope.updateCategory = function() {
		
		if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
			return;
		}
		
		// 폼 유효성 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		//그리드 유효성 체크
		if(!$scope.categoryRating_grid.validate()){
			return;
		}
		if(!$scope.attribute_grid.validate()){
			return;
		}
		
		// 그리드 데이터 담기
		if($scope.pmsCategory.leafYn=='Y'){
			$scope.pmsCategory.pmsCategoryattributes = $scope.attribute_grid.getData();
			$scope.pmsCategory.pmsCategoryratings = $scope.categoryRating_grid.getData();
		}else{
			$scope.pmsCategory.pmsCategoryattributes = [];
			$scope.pmsCategory.pmsCategoryratings = [];
		}
		
		// 디비 저장
		categoryService.updateCategoryInfo($scope.pmsCategory, function(response) {
			
			if (response.content != "fail") {					
				$scope.pmsCategory.categoryId = response.content;
				
				// 트리 재구성
				$scope.reloadTreeAndSelectItem($scope.pmsCategory);

				// 등록중 플래그 false
				$scope.regFlag = false;
				
				alert($scope.MESSAGES["common.label.alert.save"]);
				
			} else {
				alert($scope.MESSAGES["common.label.alert.fail"]);
			}
			
		});
	}
	
	
	// 속성 등록 팝업 호출
	$scope.registerCategoryAttribute = function() {
		$scope.attributeIds = [];
		
		if ($scope.pmsCategory.categoryId == '') {
			alert($scope.MESSAGES["pms.category.attribute.register.validation1"]);
		} else {
			if ($scope.pmsCategory.leafYn == 'N') {
				alert($scope.MESSAGES["pms.category.attribute.register.validation2"]);
			} else {
				var attributeGrid = $scope.attribute_grid.getGridScope();
				for (i in attributeGrid.data) {
					$scope.attributeIds.push(attributeGrid.data[i].attributeId);
				}
				var url = Rest.context.path + '/pms/attribute/popup/search';
				popupwindow(url, $scope.MESSAGES["pms.category.attribute.register"], 840, 600);
			}		
		}	
	}
	
	// 속성 그리드 삭제
	this.deleteCategoryAttribute = function(){
		$scope.attribute_grid.deleteGridData(null, function(){
		});
	}
	
	
	// 표준카테고리 삭제
	$scope.deleteCategory = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.delete"])) {
			return;
		}
		var param = { categoryId : $scope.pmsCategory.categoryId };
		categoryService.deleteCategory(param, function(response) {
			
			if (response.success) {
				
				alert($scope.MESSAGES["common.label.alert.delete"]);
				
				$scope.pmsCategory = null;
				$scope.reloadTreeAndSelectItem($scope.pmsCategory);
				
				
			}else{
				alert(response.resultMessage);
			}
		});
	}
	
	
	$scope.attributeDetail = function(fieldValue, rowEntity) {
		$scope.attributeId = rowEntity.attributeId;
		$scope.saveBtn = false;
		attributeService.openAttributeDetailPopup();
	}
	
	// promotion 상세에서 호출

	if(angular.isDefined($window.opener) && $window.opener != null) {
		var pScope = $window.opener.$scope;
		$scope.reloadTreeAndSelectItem(pScope.paramCategory);
	}else{
		$scope.reloadTreeAndSelectItem(null);
	}

	// 그리드에 속성값 추가
	$scope.addAttrGrid = function(data){
		
		for (row in data) {			
			if ($scope.attributeIds.indexOf(data[row].attributeId) >= 0) {
				alert($scope.MESSAGES["pms.category.attribute.register.validation3"]);
//				insertYn = 'N';
				row = data.length;	// break;
			} else {
				
				$scope.attribute_grid.addRow({
					attributeId: data[row].attributeId,
					categoryId: $scope.pmsCategory.categoryId,
					attributeName: data[row].name,
					pmsAttribute : {attributeTypeName: data[row].attributeTypeName}
				});
				
			}
		}
		common.safeApply($scope);
		
	}
	
	
});



// 표준카테고리 속성 등록 컨트롤러
categoryApp.controller("pms_categoryAttributeInsertPopApp_controller", function($window, $scope, $filter, 
		categoryService, commonService, gridService) {
	
	pScope = $window.opener.$scope;
	
	// 속성 등록(표준카테고리&속성 매핑) 팝업 init Method
	this.initPopup = function() {
		var	columnDefs = [
	         { field: 'attributeId', width: '10%', colKey: 'c.pmsAttribute.id', vKey: 'c.pmsAttribute.id'},
	         { field: 'attributeTypeName', width: '15%', colKey: 'c.pmsAttribute.type' },
	         { field: 'name', width: '*', colKey: 'pmsAttribute.name' },
	         { field: 'insId', width: '10%', userFilter :'insId,insName',	colKey: 'c.grid.column.insId' },
	         { field: 'insDt', width: '10%', colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" },
	         { field: 'updId', width: '12%', userFilter :'updId,updName',	colKey: 'c.grid.column.updId' },
	         { field: 'updDt', width: '12%', colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
	    ];
	
		var gridParam = {
			scope : $scope,
			gridName : "grid_attr",
			url :  '/api/pms/attribute',
			searchKey : "search",
			columnDefs : columnDefs,
			gridOptions : {
			},
			callbackFn : function() {
			}
		};

		//그리드 객체생성
		$scope.myGrid = new gridService.NgGrid(gridParam);
	}
	
	// 속성 검색
	this.searchAttribute = function(attributeTypeCd) {
		$scope.search.attributeTypeCd = attributeTypeCd;
		$scope.myGrid.loadGridData();
	}
	
	// 속성 목록 그리드에 팝업에서 선택한 속성 저장
	this.saveCategoryAttribute = function() {		
		var insertAttributes = [];
		
		// 등록 여부 체크
//		var insertYn = 'Y';
		var selectedRows = $scope.myGrid.getSelectedRows();
		
		if (selectedRows.length == 0) {
			alert("선택된 속성이 없습니다.");
		} else {
			pScope.addAttrGrid(selectedRows);
			$window.close();
		}
	}
	
	
	this.close = function(){
		$window.close();
	}
});
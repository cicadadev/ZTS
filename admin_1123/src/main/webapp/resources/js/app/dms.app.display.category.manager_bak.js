var displayCategoryApp = angular.module("displayCategoryApp", ['ngRoute', 'commonServiceModule', 'dmsServiceModule', 'commonPopupServiceModule', 'pmsServiceModule','gridServiceModule', 'ui.date', 'commonPopupServiceModule']);

// 메시지
Constants.message_keys = ["common.label.confirm.save", "common.label.confirm.delete", "common.label.alert.save", "common.label.alert.delete",
                          "pms.category.register.validation1", "dms.displaycategory.delete.validation1", "dms.displaycategory.delete.validation2",
                          "dms.displaycategory.update.template.validation", "pms.category.register.validation2"];

//탭을 위한 라우트 설정
displayCategoryApp.config(function ($routeProvider) {
	$routeProvider
	    .when('/tab1', {templateUrl: '/dms/displayCategory/tab1', controller: 'ctrlTab1'})
	    .when('/tab2', {templateUrl: '/dms/displayCategory/tab2', controller: 'ctrlTab2'})
	    .when('/empty', {template: '<div>전시카테고리를 선택해 주세요.</div>'})
	    .otherwise({redirectTo: '/empty'});
});

displayCategoryApp.controller("dms_displayCategoryManagerApp_controller", function($compile, $window, $rootScope, $scope, $filter, $location,
		displayCategoryService, displayService, commonService, displayTreeService) {
	
	// 객체 생성
	$scope.search = {};
	$rootScope.templateList = [];
	$rootScope.displayCategorySelected = false;
	
	// 팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	//message
	commonService.getMessages(function(response){
		$rootScope.MESSAGES = response;
	});
	

	// 전시카테고리 조회
	$rootScope.displayCategories = [];
	$scope.getDisplayCategories = function() {
		
		displayCategoryService.getDisplayCategories($scope.search, function(response) {
			$rootScope.displayCategories = response;
			
			// 전시카테고리 조회 완료 후, 카테고리 템플릿 selectbox option 완성
			var tempIdList = [];
			for (i in response) {
				var templates = response[i].dmsTemplates;
				for (t in templates) {
					var template = templates[t];
					
					if (template.templateId != null) {
						if (tempIdList.indexOf(template.templateId) < 0) {
							$rootScope.templateList.push({
								templateId: template.templateId,
								name: template.name
							});
						}
						tempIdList.push(template.templateId);
					}
				}					
			}
		});
		$rootScope.initDpCategory($scope, 'displayCategoryInfo');
	}
	
	$rootScope.$watch('displayCategories', function(newList, oldList) {
		$rootScope.displayCategorySelected = true;
		
		$rootScope.locationUrl = '';
		$location.url('/empty');
	});
	
	// 객체 초기화
	$rootScope.initDpCategory = function(scope, scopeName) {
		scope[scopeName] = {
			displayCategoryId: '', 
			upperDisplayCategoryId: '',
			name: '',
			upperDisplayCategoryName: '',
			displayItemTypeCd: '',
			sortNo: '', 
			leafYn: '', 
			displayYn: '',
			dmsTemplate: {}
		}
	}
	
	//트리 재조회 & 카테고리 선택
	$rootScope.reloadTreeAndSelectItem = function(category){
		displayCategoryService.getDisplayCategories($scope.search, function(response) {
			$rootScope.displayCategories = response;
			
		// 트리에서 현재 카테고리 선택
		displayTreeService.selectTreeItem($rootScope.displayCategories, category.displayCategoryId, 
				category.upperDisplayCategoryId, "displayCategoryId", "upperDisplayCategoryId");
			
		common.safeApply($rootScope);
		});		
	}
	
	// 전시카테고리 하위 열기/닫기 
	this.openFolder = function(index, icon) {
		displayTreeService.openTree(index, icon, $rootScope.displayCategories, "displayCategoryId", "upperDisplayCategoryId");
	}

	// 전시카테고리 기본정보 & 코너 목록 조회
	this.getCategoryCorners = function(e, categoryArray) {		
		var aBtn = $(e.target);
		
		//선택
		if(!aBtn.hasClass("active")){
			$(".category .list_dep a").removeClass("active");
			aBtn.addClass("active");
		}
		
		$rootScope.displayCategorySelected = true;
		$rootScope.categoryArray = angular.copy(categoryArray);

		$rootScope.locationUrl = '#tab1';		
		$location.url('/tab1');
		
	}
	
	// 전시카테고리 등록 팝업 호출
	$rootScope.popupDisplayCategoryInfo = {};
	this.register = function(chooseDisplayCategory) {
		var leafYn = chooseDisplayCategory.leafYn;

		if ("Y" == leafYn) {
			alert($rootScope.MESSAGES["pms.category.register.validation1"]);
			return;
		} else {
			if (chooseDisplayCategory.displayCategoryId == '') {
				alert($rootScope.MESSAGES["pms.category.register.validation2"]);
			} else {
				$rootScope.popupDisplayCategoryInfo = angular.copy($scope.displayCategoryInfo);
				$rootScope.initDpCategory('popupDisplayCategoryInfo');
				
				$rootScope.popupDisplayCategoryInfo.upperDisplayCategoryId = chooseDisplayCategory.displayCategoryId;
				$rootScope.popupDisplayCategoryInfo.upperDisplayCategoryName = chooseDisplayCategory.name;
				
				var url = Rest.context.path + "/dms/displayCategory/popup/insert?upperDisplayCategoryId="+chooseDisplayCategory.displayCategoryId;
				popupwindow(url, $rootScope.MESSAGES["dms.displaycategory.register"], 1070, 360);
			}			
		}
	}
	
});


// 전시카테고리 기본정보 탭 컨트롤러
displayCategoryApp.controller("ctrlTab1", function($compile, $window, $rootScope, $scope, $filter, $location,
		displayCategoryService, displayService, commonService, displayTreeService, gridService) {
	
	$rootScope.tab1 = "on";
	$rootScope.tab2 = "";
	
	// 객체 생성
	$scope.search = {};	
	
	// 전시카테고리 기본정보	
	$rootScope.$watch('categoryArray', function(newList, oldList) {
		$scope.displayCategoryInfo = newList;
		$scope.selectedCategory = newList;
	});	

	common.resizeIframe();//iframe높이 조절
	
	// 전시카테고리 기본정보 수정
	$scope.updateDisplayCategory = function(categoryArray) {
		if (confirm($rootScope.MESSAGES["common.label.confirm.save"])) {
			if (categoryArray.templateId == null) {
				alert($rootScope.MESSAGES["dms.displaycategory.update.template.validation"]);
			} else {
				displayCategoryService.updateDisplayCategoryInfo($scope.displayCategoryInfo, function(response) {				
					if (response.content != "fail") {						
						$rootScope.reloadTreeAndSelectItem(categoryArray);
						
						alert($rootScope.MESSAGES["common.label.alert.save"]);
					} else {
						
					}
				});
			}
		}		
	}
	
	// 전시카테고리 삭제
	$scope.deleteDisplayCategory = function(categoryArray) {
		if (confirm($rootScope.MESSAGES["common.label.confirm.delete"])) {
			var deleteBoolean = true;
			
			// 하위 카테고리가 있으면 카테고리 삭제 불가
			angular.forEach($rootScope.displayCategories, function(category) {				
				if (category.upperDisplayCategoryId == categoryArray.displayCategoryId) {
					alert($rootScope.MESSAGES["dms.displaycategory.delete.validation1"]);
					deleteBoolean = false;
					return;
				}
				return;
			});
			
			if (deleteBoolean) {				
				// 카테고리 템플릿 > 코너에 속한 상품이 있으면 카테고리 삭제 불가
				categoryArray.displayItemDivId = categoryArray.displayCategoryId;
				displayCategoryService.getDisplayCategoryCornerItems(categoryArray, function(response) {
					if (response != null && response.length > 0) {
						alert($rootScope.MESSAGES["dms.displaycategory.delete.validation2"]);
					} else {
						displayCategoryService.deleteDisplayCategory(categoryArray.displayCategoryId, function(response) {
							if (response.content != "fail") {
								displayTreeService.deleteNodeTree($rootScope.displayCategories, "displayCategoryId", categoryArray.displayCategoryId);
															
								$rootScope.reloadTreeAndSelectItem(categoryArray);
//								$rootScope.initDpCategory($scope, 'displayCategoryInfo');
//								
//								$location.url('/empty');
									
								alert($scope.MESSAGES["common.label.alert.delete"]);				
							} else {
									
							}
						});	
					}
				});				
			}
		}
	}
});


//전시카테고리 코너정보 탭 컨트롤러
displayCategoryApp.controller("ctrlTab2", function($compile, $window, $rootScope, $scope, $filter, $location,
		displayCategoryService, displayService, commonService, displayTreeService, gridService, commonPopupService) {
	
	$rootScope.tab1 = "";
	$rootScope.tab2 = "on";
	
	$rootScope.itemGridShow = false;
	
	// 객체 생성
	$scope.search = {};	
	var categoryArray = $rootScope.categoryArray;
	
	// 코너 목록 그리드
	var gridName1 = 'grid_displayCategoryCornerList';
	var cornerGrid_columnDefs =  [        
	    { field: 'displayId', colKey: "c.dmsDisplay.cornerId", vKey: "c.dmsDisplay.cornerId" }
		, { field: 'name', colKey: "c.dmsDisplay.cornerName" }
		, { field: 'displayItemType', colKey: "c.dmsDisplay.corner.cornerType", vKey: "c.dmsDisplay.corner.cornerType" }
		, { field: 'updId', colKey: "c.grid.column.updId" }
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
			$scope.search.templateId = categoryArray.templateId;
			$scope.corner_grid.loadGridData();
		}
	};
	$scope.corner_grid = new gridService.NgGrid(cornerGrid_Param);
	
	// 상품 목록 그리드
	var gridName2 = 'grid_cornerItemList';		
	$scope.cornerItemGrid_Param = {
		scope : $scope, 
		gridName : gridName2,
		url : '',
		searchKey : "search", 
		columnDefs : [],
		gridOptions : {
		},
		callbackFn : function() {
		}
	};
	$scope.cornerItem_grid = new gridService.NgGrid($scope.cornerItemGrid_Param);
	
	
	// 코너 상세
	$scope.getCornerDetail = function(data) {
		$scope.search.displayId = data.displayId;
		$scope.search.displayItemDivId = categoryArray.displayCategoryId;
		$scope.displayCategoryInfo.displayItemTypeCd = data.displayItemTypeCd;
		
		var gridUrl = '';
		var columnDefs = [];
	
	    var gridUrl =  displayService.getCornerItemUrl(data.displayItemTypeCd);
	    var columnDefs = displayService.getCornerItemColDef($rootScope,data.displayItemTypeCd);
		    
		$scope.cornerItemGrid_Param.url = gridUrl;
		$scope.cornerItemGrid_Param.columnDefs = columnDefs;
		$scope.cornerItemGrid_Param.callbackFn = function() {
			$scope.cornerItem_grid.loadGridData();
			$rootScope.itemGridShow = true;
		};
		
		$scope.cornerItem_grid = new gridService.NgGrid($scope.cornerItemGrid_Param);
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
	
	// 상품 목록 그리드 엑셀 다운로드
	$scope.exportCornerItemExcel = function() {
		$scope.cornerItem_grid.exportExcel('cornerItem', function(){
			alert("수정완료!");
		});
	}
	
	// 코너 상품 삭제
	$scope.deleteGridData = function() {
		var pUrl = Rest.context.path + "/dms/corner/items/"+categoryArray.displayItemTypeCd+"/delete";
		$scope.cornerItem_grid.deleteGridData();
	}
	
	// 코너 상품 등록
	$scope.saveGridData = function() {
		$scope.cornerItem_grid.saveGridData(null, function(response) {
			$scope.cornerItem_grid.loadGridData();
		});
	}
	
});


// 전시카테고리 등록 팝업 컨트롤러
displayCategoryApp.controller("dms_displayCategoryinsertPopApp_controller", function($window, $rootScope, $scope,
		displayCategoryService, commonService, displayTreeService) {
	
	pScope = $window.opener.$scope;
	// 객체생성
	$scope.search = {};
	
	// 전시카테고리 등록 팝업 init Method
	$scope.popupDisplayCategoryInfo = {};
	this.initInsertPopup = function() {
		$scope.popupDisplayCategoryInfo = pScope.popupDisplayCategoryInfo;		
		$scope.templateList = pScope.templateList;
		
		$scope.popupDisplayCategoryInfo.displayYn = "Y";
		if ($scope.popupDisplayCategoryInfo.upperDisplayCategoryId != null && $scope.popupDisplayCategoryInfo.leafYn == 'N') {
			$scope.popupDisplayCategoryInfo.leafYn = "Y";
		} else {
			$scope.popupDisplayCategoryInfo.leafYn = "N";
		}	
	}
	
	// 전시카테고리 등록
	this.insertDisplayCategory = function() {
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			displayCategoryService.insertDisplayCategory($scope.popupDisplayCategoryInfo, function(response) {
				if (response.content != "fail") {
					$scope.popupDisplayCategoryInfo.displayCategoryId = response.content;	   
					displayTreeService.insertNodeToTree(pScope.displayCategories, 
							$scope.popupDisplayCategoryInfo, "displayCategoryId", "upperDisplayCategoryId");

					pScope.initDpCategory(pScope, 'displayCategoryInfo');
					
					pScope.$apply();			
					$window.close();
				} else {
					
				}
			});
		}
	}
	
	// 취소(닫기)
	this.close = function(){
		$window.close();
	}
});
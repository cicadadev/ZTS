function calcHeight(frameId)
{
  //find the height of the internal page
  var the_height= $("#roles").height();
  parent.document.getElementById(frameId).height= the_height;
}

var roleApp = angular.module("roleApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule']);

//메시지
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

roleApp.controller("ccs_roleManagerApp_controller", function($window, $scope, $filter, roleService, commonService, gridService) {

	// 객체 생성
	$scope.search = {};
	$scope.searchFunc = {};
	$scope.gridData = [];
	/*$scope.checkFunctions = [];*/
	
	// 팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});	
	
	// 권한그룹 조회
	$scope.roleGroup = {};
	this.getRoleGroup = function() {
		roleService.getRoleGroup(function(response) {
			$scope.roleGroup = response;
		});
		$scope.initGrid();
	}
	
	// 그리드 초기화
	$scope.initGrid = function() {
		var columnDefs =  [
       	    { field: 'menuId', width: '50%', colKey: "c.ccs.role.menu", 	 	  vKey: "ccsMenu.menuId" }, 
       		{ field: 'useYn',  width: '50%', colKey: "c.ccs.role.menuUseYn", vKey: "c.ccs.role.menuRoleYn", enableCellEdit:true, cellFilter:'useYnFilter' }
       	];
       		
       	var gridParam = {
       		scope : $scope,
       		gridName : 'grid_role',
       		url: '/api/ccs/role/menu',
       		searchKey: 'search',
       		columnDefs : columnDefs,
       		gridOptions : {
       			checkBoxEnable: false,
       			enableRowSelection : true,
       			noUnselect : true,
       			pagination : false,
       			rowSelectionFn : function(row){
					$scope.divRoleDetail(row.entity);
				}
       		}, 
       		callbackFn : function() {
       			common.resizeIframe();
       			calcHeight(__pageId + "_module");
       			/*var gridScope = $scope.role_grid.getGridScope();
       			gridScope.columnDefs[2].cellTemplate = '<label ng-repeat="menuFunction in grid.appScope[\'menuFunctions\'+grid.appScope.getRowIndex(row.entity)]">'
       									+ '<div ng-if="grid.appScope.checkFunction(row.entity, menuFunction)==true">'
       										+ '<input type="checkbox" checklist-model="grid.appScope.checkFunctions" checklist-value="grid.appScope.search.roleId+\'_\'+menuFunction.menuId+\'_\'+menuFunction.functionId" style="margin-bottom:10px">{{menuFunction.name}}'
       									+ '</div></label>';*/
       		}
       	};
       	$scope.role_grid = new gridService.NgGrid(gridParam);
       	$scope.gridShow = false;
	}
	
	// 권한그룹 등록
	this.roleGroupPopup = function() {
		$scope.detailShow = true;
		$scope.initGrid();
		$scope.initGrid2();
		$scope.search = {};
//		$scope.search.useYn = 'Y';
		$scope.gridShow = false;
		$scope.gridShow2 = false;
		common.resizeIframe();
		
	}
	
	// 권한그룹 상세
	$scope.getRoleDetail = function(e, role) {
		if(e != undefined){
			var aBtn = $(e.target);
			
			//선택
			if(!aBtn.hasClass("active")){
				$(".category .list_dep a").removeClass("active");
				aBtn.addClass("active");
			}
		}
		
		for (i in $scope.roleGroup) {
			if ($scope.roleGroup[i].roleId == role.roleId) {
				$scope.search = angular.copy(role);
				i = $scope.roleGroup.length;	// break;
			}
		}
		$scope.gridShow = true;
		$scope.detailShow = true;
		$scope.gridShow2 = false;
		$scope.role_grid.loadGridData();
		common.resizeIframe();
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
	
	// 그리드 데이터 scope에 세팅
	$scope.$watchCollection('grid_role', function(newList, oldList) {
		$scope.gridData = newList.data;
		$scope.getGridData();
	}, true);
	
	
	// 권한 그룹별 메뉴&기능 선택
	//$scope.allFunctions = [];
	$scope.getGridData = function() {		
		if ($scope.gridData != null) {
			for (i in $scope.gridData) {
				var menu = $scope.gridData[i];
				
				if (menu.ccsFunctions != null && menu.ccsFunctions.length > 0) {
					$scope['menuFunctions'+i] = [];
					
					for (f in menu.ccsFunctions) {
						var functions = menu.ccsFunctions[f];
						if (functions.menuId == menu.originMenuId) {
							$scope['menuFunctions'+i].push({
								menuId: functions.menuId,
								functionId: functions.functionId,
								name: functions.name,
								useYn: functions.useYn
							});
						}
					}
				}
			}
		}
	}
	
	// 그리드 row index
	$scope.getRowIndex = function(data) {
		var hash = data.$$hashKey;
		var menuId = data.originMenuId;
		
		for (i in $scope.gridData) {
			if ($scope.gridData[i].$$hashKey == hash && $scope.gridData[i].originMenuId == menuId) {
				return i;
			}
		}
	}
	
	// 권한그룹 기본정보 수정
	this.saveRole = function(role) {
		
		if(role.roleId == undefined){
			
			// 확인 메세지
			if(!confirm("권한그룹을 생성하시겠습니까?")){
				return;
			}
			
			roleService.insertRole($scope.search, function(response) {
				role.roleId = response.content;
				if (response.content != "fail") {						
					alert('권한그룹이 생성되었습니다.');
					roleService.getRoleGroup(function(response) {
						$scope.roleGroup = response;
						for (i in $scope.roleGroup) {
							if($scope.roleGroup[i].name == role.name){
								$scope.roleGroup[i].active = 'Y';
								$scope.getRoleDetail(null, $scope.roleGroup[i]);
							}
						}
					});
					$scope.initGrid();
				}
			});
		}
		else{
			
			// 확인 메세지
			commonService.getMessages(function(response){
				$scope.MESSAGES = response;
			});

			
			roleService.updateRole($scope.search, function(response) {				
				if (response.content != "fail") {
					roleService.getRoleGroup(function(response) {
						$scope.roleGroup = response;
						for (i in $scope.roleGroup) {
							if($scope.roleGroup[i].roleId == role.roleId){
								$scope.roleGroup[i].active = 'Y';
								$scope.getRoleDetail(null, $scope.roleGroup[i]);
							}
						}
					});
					alert($scope.MESSAGES["common.label.alert.save"]);
				}
			});
		}
	}
	
	// 권한그룹 취소
	this.cancelRole = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		alert($scope.MESSAGES["common.label.alert.cancel"]);

		$scope.search = {};
		$scope.initGrid();
		$scope.initGrid2();
		$scope.gridShow = false;
		$scope.gridShow2 = false;
		$scope.detailShow = false;
		common.resizeIframe();
	}
	
	// 권한그룹 삭제
	this.deleteRole = function(role) {

		// 확인 메세지
		if (!confirm($scope.MESSAGES["common.label.confirm.delete"])) {
			return;
		}

		
		roleService.deleteCheck($scope.search, function(response){
			if(angular.isUndefined(response.success) || angular.isDefined(response) && response.success){
				roleService.deleteRole($scope.search, function(response) {
					if (response.content != "fail") {
						roleService.getRoleGroup(function(response) {
							$scope.roleGroup = response;
							$scope.search = {};
							$scope.initGrid();
							$scope.initGrid2();
							$scope.detailShow = false;
							$scope.gridShow2 = false;
							common.resizeIframe();
							alert($scope.MESSAGES["common.label.alert.delete"]);
						});		
					}
				});
			}else{
				if (window.confirm("하위기능권한이 존재합니다 삭제 하시겠습니까?")) {
					roleService.deleteRole($scope.search, function(response) {
						if (response.content != "fail") {
							roleService.getRoleGroup(function(response) {
								$scope.roleGroup = response;
								$scope.search = {};
								$scope.initGrid();
								$scope.initGrid2();
								$scope.detailShow = false;
								$scope.gridShow2 = false;
								common.resizeIframe();
								alert($scope.MESSAGES["common.label.alert.delete"]);
							});		
						}
					});
				}
			}
		});
	}
	

	
	// 권한 페이지 리스트 그리드 수정
	this.saveGrid = function() {
		// 메뉴 권한여부
		var gridApi = $scope.grid_role.gridApi;
		var dirtyRows;
		if (gridApi && gridApi.rowEdit) {
			dirtyRows = gridApi.rowEdit.getDirtyRows();
		}
		if(dirtyRows.length > 0){
			
			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}

			
			var menuList = [];			
			for (i in $scope.gridData) {
				menuList.push({
					roleId: $scope.search.roleId,
					menuId: $scope.gridData[i].originMenuId,
					menuRoleYn: $scope.gridData[i].useYn
				});
			}
			
			roleService.updateRoleMenu(menuList, function(response) {
				if (response != "fail") {
					$scope.role_grid.loadGridData();
					alert($scope.MESSAGES["common.label.alert.save"]);
				}
			});
		}else{
			alert("수정한 항목이 없습니다.");
		}
	}
	
	
	$scope.divRoleDetail = function(row) {
		$scope.searchFunc.menuId = row.originMenuId;
		$scope.searchFunc.menuUseYn = row.useYn;
		$scope.searchFunc.roleId = $scope.search.roleId;
		$scope.initGrid2();
		$scope.searchFuncGrid();
		$scope.gridShow2 = true;
		common.resizeIframe();
	}
	
	$scope.initGrid2 = function() {
		var columnFuncDefs =  [
//		                       { field: 'functionId', 	width: '20%',		displayName : "기능ID"},
		                       { field: 'groupName', 	width: '20%',		colKey: "c.ccs.role.func.groupName", 	vKey: "ccsFunction.menuId"},
		                       { field: 'name', 		width: '40%',		colKey: "c.ccs.role.func.name", 		vKey: "ccsFunction.name"},
			                   { field: 'useYn',  		width: '20%', 		colKey: "c.ccs.role.useFunctions", 	vKey: "ccsFunction.useYn", 		enableCellEdit:true,   cellFilter:'useYnFilter' }
			       	];
       		
		var gridFuncParam = {
	       		scope : $scope,
	       		gridName : "myFuncGrid",
	       		url: '/api/ccs/role/func',
	       		searchKey : "searchFunc",
	       		columnDefs : columnFuncDefs,
	       		gridOptions : {
	       			checkBoxEnable: false,
	       			pagination : false
	       		},
	       		callbackFn : function() {
	       			common.resizeIframe();
	       		}
	       	};
		
		$scope.funcGrid = new gridService.NgGrid(gridFuncParam);
	}
		
		// 권한 기능 검색
		$scope.searchFuncGrid = function(){
			$scope.funcGrid.loadGridData();
			common.resizeIframe();
		}
		
		// 기능 수정
		this.saveFuncGrid = function(){
			
			var gridApi = $scope.myFuncGrid.gridApi;
			var dirtyRows;
			if (gridApi && gridApi.rowEdit) {
				dirtyRows = gridApi.rowEdit.getDirtyRows();
			}
			if(dirtyRows.length > 0){
				
				// 확인 메세지
				if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
					return;
				}

				roleService.menuCheck($scope.searchFunc, function(response) {
					if(angular.isUndefined(response.success) || angular.isDefined(response) && response.success){
						$scope.funcGridDate = $scope.funcGrid.getData();
						var functionList = [];
						for (i in $scope.funcGridDate) {
							functionList.push({
								roleId: $scope.search.roleId,
								groupName: $scope.funcGridDate[i].groupName,
								menuId: $scope.funcGridDate[i].menuId,
								functionId: $scope.funcGridDate[i].functionId,
								useYn: $scope.funcGridDate[i].useYn,
							});
						}
						roleService.updateRoleMenuFunction(functionList, function(response) {
							if (response != null) {
								$scope.funcGrid.loadGridData();
								alert($scope.MESSAGES["common.label.alert.save"]);
							}
						});
					}
					else{
						alert("메뉴권환이 허용되지 않았습니다.");
						return;
					}
				});
			}else{
				alert("수정한 항목이 없습니다.");
			}
		}		
});
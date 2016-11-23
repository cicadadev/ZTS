function calcHeight(frameId) // iframe 높이 재설정
{
  //find the height of the internal page
  var the_height= $("#menu").height();
  parent.document.getElementById(frameId).height= the_height;
}

var menuManagerApp = angular.module("menuManagerApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule']);

// 메시지
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

menuManagerApp.controller("ccs_menuManagerApp_controller", function($compile, $window, $scope, menuService, commonService, displayTreeService, gridService) {
	
	// 객체 생성
	$scope.search = {};
	
	$scope.menuInfo = {menuGroupId:'', name:'', sortNo:'', useYn:'Y', insId:'', insDt:'', updId:'', updDt:''};
	
	// 팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	// selectBox options
	$scope.templateList = [];
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	

	// 메뉴그룹 조회
	$scope.MenuGroupTree = [];
	this.getMenuGroupTree = function() {
		menuService.getMenuTree($scope.search, function(response) {
			$scope.MenuGroupTree = response;
		});
		$scope.initGrid();
	}
	
	// 그리드 초기화
	$scope.initGrid = function() {
		var columnMenuDefs =  [
			                   { field: 'menuId', 					type : 'number',					colKey: "c.ccs.menu.no"			/*enableCellEdit: true*/},
			                   //{ field: 'menuGroupId', 				colKey: "c.ccs.menu.menugroupid",	vKey : "ccsMenu.menuGroupId"},
			                   { field: 'name', 					colKey: "c.ccs.menu.name",			vKey : "ccsMenu.name", 				enableCellEdit: true},
			                   { field: 'sortNo', 					colKey: "c.ccs.menu.sortno",		enableCellEdit: true, 				type:'number'},
			                   { field: 'url', 						colKey: "c.ccs.menu.url",			vKey : "ccsMenu.url", 				enableCellEdit: true},
			                   { field: 'insId',	 				userFilter :'insId,insName',		colKey: "c.grid.column.insId",			vKey : "ccsCodegroup.insId"},
			                   { field: 'insDt', 					colKey: "c.grid.column.insDt",		vKey : "ccsCodegroup.insDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
			                   { field: 'updId', 					userFilter :'updId,updName',		colKey: "c.grid.column.updId",			vKey : "ccsCodegroup.updId"},
			                   { field: 'updDt', 					colKey: "c.grid.column.updDt",		vKey : "ccsCodegroup.updDt",			cellFilter: "date:\'yyyy-MM-dd\'"}
			                   //{ field: 'useYn', 					colKey: "c.ccs.menu.useYn", 		vKey : "ccsMenu.useYn",				cellFilter: "useYnFilter", 		enableCellEdit: true}
			       	]

			       	
			var gridMenuParam = {
			       		scope : $scope,
			       		gridName : "myMenuGrid",
			       		url : '/api/ccs/menu/menu',
			       		searchKey : "searchMenu",
			       		columnDefs : columnMenuDefs,
			       		gridOptions : {
			       			noUnselect : true,
			       			pagination : false
			       		},
			       		callbackFn : function() {
			       			common.resizeIframe();
			       			calcHeight(__pageId + "_module");
			       			//$scope.myGrid.loadGridData();
			       		}
			       	};
		
		$scope.menuGrid = new gridService.NgGrid(gridMenuParam);
		$scope.gridShow = false;
	}
	
	this.getMenuGroup = function(e, menuGroupArray) {
		var aBtn = $(e.target);
		//선택
		if(!aBtn.hasClass("active")){
			$(".category .list_dep a").removeClass("active");
			aBtn.addClass("active");
		}
		$scope.menuInfo.menuGroupId = menuGroupArray.menuGroupId;
		$scope.menuInfo.name = menuGroupArray.name;
		$scope.menuInfo.sortNo = menuGroupArray.sortNo;
		$scope.menuInfo.useYn = menuGroupArray.useYn;
		
		$scope.menuInfo.insId = menuGroupArray.insId;
		$scope.menuInfo.insDt = menuGroupArray.insDt;
		$scope.menuInfo.updId = menuGroupArray.updId;
		$scope.menuInfo.updDt = menuGroupArray.updDt;
		
//		$scope.originSortNo = menuGroupArray.sortNo;
		
		$scope.gridShow = true;
		$scope.detailShow = true;
		$scope.searchMenu.menuGroupId = menuGroupArray.menuGroupId;
//		$scope.searchMenuGrid();
		$scope.menuGrid.loadGridData();
		common.resizeIframe();
	}
	
	
	
	
	// 메뉴 검색
	$scope.searchMenuGrid = function(){
		$scope.menuGrid.loadGridData();
	}
	
	// 메뉴그룹 등록
	this.register = function() {
		$scope.initGrid();
		$scope.detailShow = true;		
		$scope.menuInfo = {};
		$scope.menuInfo.useYn = 'Y';
		$scope.gridShow = false;
		$(".category .list_dep a").removeClass("active");
		common.resizeIframe();
	}
	
	// 메뉴그룹 취소
	this.canceMenuGroup = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		alert($scope.MESSAGES["common.label.alert.cancel"]);

		$scope.menuInfo = {};
		$scope.gridShow = false;
		$scope.detailShow = false;
		$scope.initGrid();
	}
	
	// 메뉴그룹 기본정보 생성 및 수정
	this.updateMenuGroup = function(menuGroupArray) {
		if(menuGroupArray.menuGroupId == undefined){
			if(menuGroupArray.sortNo == null || menuGroupArray.sortNo == ''){
				alert("유효하지 않는 항목이 있습니다.");
				return;
			}
			
			// 확인 메세지
			if(!confirm("메뉴 그룹을 생성하시겠습니까?")){
				return;
			}
			
			// 메뉴그룹 생성
			menuService.insertMenuGroup(menuGroupArray, function(response) {
				if(response.content){
					menuGroupArray.menuGroupId = response.content;
					menuService.getMenuTree($scope.search, function(response) {
						$scope.MenuGroupTree = response;
						for (i in $scope.MenuGroupTree) {
							if($scope.MenuGroupTree[i].menuGroupId == response.menuGroupId){
								$scope.MenuGroupTree[i].active = 'Y';
								$scope.getMenuGroup(null, $scope.MenuGroupTree[i]);
							}
						}
					});
					alert("메뉴 그룹이 생성되었습니다.");
				}
				else{
					var msg = response.resultMessage==null || response.resultMessage==''?"Save Error!":response.resultMessage;
					alert(msg);
					return;
				}
			})
		}else{
			var updateInfo = {
					menuGroupId: menuGroupArray.menuGroupId,
					name: menuGroupArray.name,
					sortNo: menuGroupArray.sortNo
			};
			if(updateInfo.name == null || updateInfo.name == '' || updateInfo.sortNo == null || updateInfo.sortNo == ''){
				alert("유효하지 않는 항목이 있습니다.");
				return;
			}
			
			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
			
			// 메뉴그룹정보 수정
			menuService.updateMenuGroupInfo(updateInfo, function(response) {
				if(response.success){
					menuService.getMenuTree($scope.search, function(response) {
						$scope.MenuGroupTree = response;
						for (i in $scope.MenuGroupTree) {
							if($scope.MenuGroupTree[i].menuGroupId == menuGroupArray.menuGroupId){
								$scope.MenuGroupTree[i].active = 'Y';
								$scope.getMenuGroup(null, $scope.MenuGroupTree[i]);
							}
						}
					});
					alert($scope.MESSAGES["common.label.alert.save"]);
				}
			});
		}
	}		
	
	// 메뉴그룹 삭제
	this.deleteMenuGroup = function(menuGroupArray) {
		if (confirm($scope.MESSAGES["common.label.confirm.delete"])) {
			menuService.deleteMenuGroup(menuGroupArray.menuGroupId, function(response) {
				if (response.content != null) {
					displayTreeService.deleteNodeTree($scope.MenuGroupTree, "menuGroupId", menuGroupArray.menuGroupId);
				}
				menuService.getMenuTree($scope.search, function(response) {
					$scope.MenuGroupTree = response;
					$scope.menuInfo = {};
					$scope.initGrid();
					$scope.gridShow = false;
					$scope.detailShow = false;
				});
				alert($scope.MESSAGES["common.label.alert.delete"]);
			});
		}
	}
	
	//그리드에 추가
	this.addMenuRow = function(){
		$scope.menuGrid.addRow({
			menuGroupId : $scope.searchMenu.menuGroupId,
			useYn : 'Y',
			crudType : 'C'
		});
	}
	
	// 메뉴 등록
	this.saveMenuGrpGrid = function(){
		$scope.menuGrid.saveGridData(null, function(data){
			$scope.searchMenuGrid();
		});
	}
	
	// 메뉴 삭제
	this.deleteMenuGrpGrid = function(){
		$scope.menuGrid.deleteGridData(null, function(){
			$scope.searchMenuGrid();
		});
	}

});
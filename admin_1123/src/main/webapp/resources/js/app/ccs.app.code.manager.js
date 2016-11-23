function calcHeight(frameId)
{
  //find the height of the internal page
  var the_height= $("#code").height();
  parent.document.getElementById(frameId).height= the_height;
}

var codeManagerApp = angular.module("codeManagerApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

codeManagerApp.controller("ccs_codeManagerApp_controller", function($window, $scope, $filter, codeService, commonService, gridService, commonPopupService){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	$scope.searchCode = {};
	
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	$scope.initGrid = function() {
			var columnCodeGrpDefs =  [
			                          { field: 'cdGroupCd', 				colKey: "c.ccs.code.group.code",	vKey : "ccsCodegroup.cdGroupCd",		enableCellEdit: true,
			                        	  cellEditableCondition: function ($scope){
			                                  return $scope.row.entity.insId == null ? true : false; 
			                              }},
			                          { field: 'name', 						colKey: "c.ccs.code.group.name",	vKey : "ccsCodegroup.name", 			enableCellEdit: true},
			                          { field: 'insId',	 					userFilter :'insId,insName',		colKey: "c.grid.column.insId",		vKey : "ccsCodegroup.insId"},
			                          { field: 'insDt', 					displayName : "등록일시", 				colKey: "c.grid.column.insDt",			vKey : "ccsCodegroup.insDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
			                          { field: 'updId', 					userFilter :'updId,updName',		colKey: "c.grid.column.updId",		vKey : "ccsCodegroup.updId"},
			                          { field: 'updDt', 					displayName : "최종수정일시", 			colKey: "c.grid.column.updDt",			vKey : "ccsCodegroup.updDt",			cellFilter: "date:\'yyyy-MM-dd\'"}
			                          ];
			
			var gridCodeGrpParam = {
					scope : $scope,
					gridName : "myCodeGrpGrid",
					url : '/api/ccs/code/codegroup',
					searchKey : "search",
					columnDefs : columnCodeGrpDefs,
					gridOptions : {
						enableRowSelection : true,
						noUnselect : true,
						rowSelectionFn : function(row){
							$scope.divCodeDetail(row.entity);
						}
					},
					callbackFn : function() {
						common.resizeIframe();
						calcHeight(__pageId + "_module");
						//$scope.myGrid.loadGridData();
					}
			};
			$scope.codeGrpGrid = new gridService.NgGrid(gridCodeGrpParam);
		}
	
		$scope.initGrid2 = function() {
			
			var columnCodeDefs =  [
			                       { field: 'cd', 						colKey: "c.ccs.code.cd",				vKey : "ccsCode.cd", 				enableCellEdit: true,
			                    	   cellEditableCondition: function ($scope){
			                                  return $scope.row.entity.insId == null ? true : false; 
			                              }},
			                       { field: 'name', 					colKey: "c.ccs.code.name",				vKey : "ccsCode.name", 				enableCellEdit: true},
			                       { field: 'sortNo', 					colKey: "c.ccs.code.sortno",			vKey : "ccsCode.sortNo",			enableCellEdit: true},
			                       { field: 'useYn', 					colKey: "c.ccs.code.useyn", 			vKey : "ccsCode.useYn",				cellFilter: "useYnFilter", 		enableCellEdit: true},
			                       { field: 'insId',	 				userFilter :'insId,insName',			colKey: "c.grid.column.insId",			vKey : "ccsCode.insId"},
			                       { field: 'insDt', 					displayName : "등록일시", 					colKey: "c.grid.column.insDt",			vKey : "ccsCode.insDt",				cellFilter: "date:\'yyyy-MM-dd\'"},
			                       { field: 'updId', 					userFilter :'updId,updName',			colKey: "c.grid.column.updId",			vKey : "ccsCode.updId"},
			                       { field: 'updDt', 					displayName : "최종수정일시", 				colKey: "c.grid.column.updDt",			vKey : "ccsCode.updDt",				cellFilter: "date:\'yyyy-MM-dd\'"}
			                       ];
			
			
			var gridCodeParam = {
					scope : $scope,
					gridName : "myCodeGrid",
					url : '/api/ccs/code',
					searchKey : "searchCode",
					columnDefs : columnCodeDefs,
					gridOptions : {
					},
					callbackFn : function() {
						common.resizeIframe();
						//$scope.myGrid.loadGridData();
					}
			};
			
			$scope.codeGrid = new gridService.NgGrid(gridCodeParam);
		}
	
	


		
			//$scope.codeGrpGrid.toggleRowSelection();
			//=================== search	
			$scope.searchCodeGrpGrid = function(){
				$scope.initGrid();
				$scope.codeGrpGrid.loadGridData();
				$scope.initGrid2();
				$scope.codeGridList = false;
//				$scope.initGrid();
			}
			
			//================= reset
			this.reset = function(){		
				commonService.reset_search($scope,'search');
				$scope.initGrid();
				$scope.initGrid2();
				$scope.codeGridList = false;
			}
			
			//그리드에 추가
			this.addCodeGrpRow = function(){
				$scope.codeGrpGrid.addRow({
				});
			}
			
			// 코드그룹 등록
			this.saveCodeGrpGrid = function(){
				$scope.codeGrpGrid.saveGridData(null, function(data){
					$scope.initGrid();
					$scope.codeGrpGrid.loadGridData();
					$scope.initGrid2();
					$scope.codeGridList = false;
				});
			}
			
			// 코드그룹 삭제
			this.deleteCodeGrpGrid = function(){
				$scope.codeGrpGrid.deleteGridData(null, function(){	
					$scope.initGrid();
					$scope.searchCodeGrid();
					$scope.initGrid2();
					$scope.codeGridList = false;
				});
			}
					
			$scope.cdGroupCd = '';
			
			// 코드 검색
			/*$scope.linkFunction = function(field, row) {
				$scope.searchCode.cdGroupCd = row.cdGroupCd;
				$scope.searchCodeGrid();
			}*/
			
			$scope.divCodeDetail = function(row) {
				if(row.newRow != true){
					$scope.searchCode.cdGroupCd = row.cdGroupCd;
					if (angular.isDefined(row.cdGroupCd)) {
						$scope.initGrid2();
						$scope.searchCodeGrid();
						$scope.codeGridList = true;
					}
				}else{
					$scope.initGrid2();
					$scope.codeGridList = false;
					$scope.myCodeGrid.data = [];// 그리드 초기화
				}
			}

				
			// 코드 검색
			$scope.searchCodeGrid = function(){
				$scope.codeGrid.loadGridData();
			}
			
			//그리드에 추가
			this.addCodeRow = function(){
				$scope.codeGrid.addRow({
					cdGroupCd : $scope.searchCode.cdGroupCd
				});
			}
			
			// 코드 등록
			this.saveCodeGrid = function(){
				if($scope.searchCode.cdGroupCd != null && $scope.searchCode.cdGroupCd.length > 0){
					$scope.codeGrid.saveGridData(null, function(){
						$scope.codeGrid.loadGridData();
					});
				}else{
					alert("코드그룹이 선택되지 않았습니다.");
				}
			}
			
			// 코드 삭제
			this.deleteCodeGrid = function(){
				$scope.codeGrid.deleteGridData(null, function(){					
					$scope.codeGrid.loadGridData();
				});
			}
});

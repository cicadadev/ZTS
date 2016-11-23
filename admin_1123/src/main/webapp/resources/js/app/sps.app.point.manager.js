
var pointManagerApp = angular.module("pointManagerApp", ['commonServiceModule', 'gridServiceModule', 'commonPopupServiceModule'
                                                         , 'spsServiceModule', 'ccsServiceModule'
                                                         , 'ui.date'
                                                         ]);

Constants.message_keys = ["common.label.confirm.save", "sps.common.enter.apply.target", "sps.common.promotion.stop", "sps.common.promotion.run"
                          , "common.label.alert.save", "sps.common.promotion.run.complete", "sps.common.promotion.stop.complete", "sps.common.invalid.delete"
                          ];

pointManagerApp.controller("pms_pointManagerApp_controller", function($window, $scope, $filter, commonService, pointService, gridService, commonPopupService){
	
	//******************************** 포인트 관리
	
	$scope.search = {};

	$scope.infoType = [
	                    {val : 'ID',text : '프로모션 번호'},
	                    {val : 'NAME',text : '프로모션 명'}
		              ];
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	//=============== init
	
	//document
	angular.element(document).ready(function () {		
//		var date = new Date();
//		date.setHours(00, 00, 00, 00);
//		$scope.search.startDate = $filter('date')(date.setDate(date.getDate()-7), Constants.date_format_1);
//		
//		var date2 = new Date();
//		date2 = date2.setHours(23, 59, 59, 00);
//		$scope.search.endDate = $filter('date')(date2, Constants.date_format_1);
		
		commonService.init_search($scope,'search');
	});	
	
	//================ grid	
	var columnDefs = [
		             { field: 'pointSaveId', 							colKey:"spsPointsave.pointSaveId", linkFunction:"linkFunction" , type:"number"},		             
		             { field: 'name',									colKey:"spsPointsave.name", linkFunction:"linkFunction" },
		             { field: 'pointSaveStateName',						colKey:"spsPointsave.pointSaveStateCd"},
		             { field: 'startDt',								colKey:"spsPointsave.startDt"},
		             { field: 'endDt',									colKey:"spsPointsave.endDt"},
		             { field: 'insId', 									colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
		             { field: 'insDt', 									colKey: "c.grid.column.insDt"  },
		             { field: 'updId', 									colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
		             { field: 'updDt', 									colKey: "c.grid.column.updDt"  }	
		             
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_point",	//mandatory
			url :  '/api/sps/point',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
//				checkBoxEnable : false
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.pointGrid = new gridService.NgGrid(gridParam);
	
	//=================== search	
	$scope.searchPoint = function(){
		$scope.pointGrid.loadGridData();
	}
	
	//================= reset
	this.reset = function(){		
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}

	this.deletePoint = function() {
		var check = true;
		var rows = $scope.pointGrid.getSelectedRows();
		for(var i=0; i<rows.length; i++) {
			if(rows[i].pointSaveStateCd != "POINT_SAVE_STATE_CD.READY") {
				check = false;
			} 
		}
		if(check) {
			$scope.pointGrid.deleteGridData();
		} else {
			alert($scope.MESSAGES["sps.common.invalid.delete"]);
		}
	}
	
	$scope.linkFunction = function(field,row){		
//		$scope.pointInsertPopup(row[field]);
		$scope.pointInsertPopup(row.pointSaveId);
	}
	
	//================= 포인트상세
	$scope.pointInsertPopup = function(pointSaveId){
		$scope._pointSaveId = "";
		if(angular.isDefined(pointSaveId)){
			$scope._pointSaveId = pointSaveId;	
		}
		$window.$scope = $scope;
		var winName='pointInsert';		
		var url = Rest.context.path + '/sps/point/popup/detail';
		popupwindow(url,winName,1100,600);
	}
		
}).controller("sps_pointDetailPopupController", function($window, $scope, $filter, commonService, pointService, applyService, gridService, commonPopupService){
	
	var pScope = $window.opener.$scope;
	
	$window.$scope = $scope;
	
	$scope.spsPointsave = {};	
	
	$scope.changeFlag = true;
	
	$scope.searchPoint = function(pointSaveId){
		$scope.spsPointsave.pointSaveId = pointSaveId;
		
		pointService.getPointDetail($scope.spsPointsave, function(response){
			
			$scope.spsPointsave = response;
			
			//포인트 적립유형
			if($scope.spsPointsave.pointSaveTypeCd == "POINT_SAVE_TYPE_CD.MULTIPLY"){
				$scope.spsPointsave.pointValueM = $scope.spsPointsave.pointValue;
			}else{
				$scope.spsPointsave.pointValueA = $scope.spsPointsave.pointValue;
			}
			
			// 사은품 프로모션 생성 후 진행 , 변경은 'name'과 '프로모션 기간' 만 가능하다
			if($scope.spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.RUN') {
				$scope.changeFlag = false;
				
				angular.element('[name="form"]').find('input, button').attr('readonly', true);
				angular.element('[name="form"]').find('input, button').attr('disabled', true);
				
				angular.element('[name="targetSave"]').attr('disabled', true);
				angular.element('[name="targetSave"]').attr('readonly', true);
				
				angular.element('[name="name"]').attr('disabled', false);
				angular.element('[name="name"]').attr('readonly', false);
				
				// 수정 불가 - 사용자 일때
				if(response.controlNo != null) {
					angular.element('[name="controlButton"]').attr('disabled', false);
					angular.element('[name="controlButton"]').attr('readonly', false);
				}
				
				angular.element('[ng-model="spsPointsave.startDt"]').find('input').attr('disabled', false);
				angular.element('[ng-model="spsPointsave.startDt"]').find('input').attr('readonly', false);
				angular.element('[ng-model="spsPointsave.startDt"]').find('button').attr('disabled', false);
				angular.element('[ng-model="spsPointsave.startDt"]').find('button').attr('readonly', false);
				
				angular.element('[ng-model="spsPointsave.endDt"]').find('input').attr('disabled', false);
				angular.element('[ng-model="spsPointsave.endDt"]').find('input').attr('readonly', false);
				angular.element('[ng-model="spsPointsave.endDt"]').find('button').attr('disabled', false);
				angular.element('[ng-model="spsPointsave.endDt"]').find('button').attr('readonly', false);
				
			} else if($scope.spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.STOP') {
				angular.element('[name="form"]').find('input, button').attr('readonly', true);
				angular.element('[name="form"]').find('input, button').attr('disabled', true);
				angular.element('[name="gridForm"]').find('input, button').attr('readonly', true);
				angular.element('[name="gridForm"]').find('input, button').attr('disabled', true);
				
				angular.element('[name="mainSave"]').attr('disabled', true);
				angular.element('[name="mainStop"]').attr('disabled', true);
			} 
			
			//포인트 적용대상
			var targetTypeCd = $scope.spsPointsave.ccsApply.targetTypeCd;
			
			var gridNameTarget = "";
			var gridNameEx = "";
			if(targetTypeCd == "TARGET_TYPE_CD.ALL"){			
				$scope.div_ex = true;	
				gridNameEx = "grid_ex";
			}else if(targetTypeCd == "TARGET_TYPE_CD.PRODUCT"){
				$scope.div_pr = true;
				gridNameTarget = "grid_pr";
			}else if(targetTypeCd == "TARGET_TYPE_CD.CATEGORY"){
				$scope.div_ca = true;
				$scope.div_ex = true;
				gridNameTarget = "grid_ca";
				gridNameEx = "grid_ex";
			}else if(targetTypeCd == "TARGET_TYPE_CD.BRAND"){
				$scope.div_br = true;
				$scope.div_ex = true;
				gridNameTarget = "grid_br";
				gridNameEx = "grid_ex";
			}
			
			if(angular.isDefined($scope.spsPointsave.ccsApply.ccsApplytargets)
					&& $scope.spsPointsave.ccsApply.ccsApplytargets != null
					&& $scope.spsPointsave.ccsApply.ccsApplytargets.length > 0){
				var target = $scope.spsPointsave.ccsApply.ccsApplytargets;
				$scope[gridNameTarget].data = target; 					
			}
			
			if(angular.isDefined($scope.spsPointsave.ccsApply.ccsExcproducts)
					&& $scope.spsPointsave.ccsApply.ccsExcproducts != null
					&& $scope.spsPointsave.ccsApply.ccsExcproducts.length > 0){
				var ex = $scope.spsPointsave.ccsApply.ccsExcproducts;
				$scope[gridNameEx].data = ex; 					
			}
			
			// 기존 사용대상 정보 삭제
			$scope.spsPointsave.ccsApply.ccsApplytargets = null;
			$scope.spsPointsave.ccsApply.ccsExcproducts = null;
			
			common.safeApply($scope);
		});
	}
	
	//document
	angular.element(document).ready(function () {
		if(angular.isDefined(pScope._pointSaveId) 
				&& pScope._pointSaveId != ""){
			$scope.searchPoint(pScope._pointSaveId);
		} else {
//			var date = new Date();
//			date.setHours(00, 00, 00, 00);
//			
//			var date2 = new Date();
//			date2 = date2.setHours(23, 59, 59, 00);
//			
//			$scope.spsPointsave.startDt = $filter('date')(date, Constants.date_format_1);
//			$scope.spsPointsave.endDt = $filter('date')(date2, Constants.date_format_1);
		}
	});
	
	//================ grid	ex
	var columnDefs = [
		             { field: 'pmsProduct.productId'				, colKey:"pmsProduct.productId"		,linkFunction:"productDetailPop", type:"number"},		             
		             { field: 'pmsProduct.name'						, colKey:"pmsProduct.name"			,linkFunction:"productDetailPop"},
		             { field: 'pmsProduct.productTypeCd'			, colKey:"pmsProduct.productTypeCd" ,dropdownCodeEditor : "PRODUCT_TYPE_CD", cellFilter : "productTypeFilter"},
		             { field: 'pmsProduct.saleStateCd'			, colKey:"pmsProduct.saleStateCd"	,dropdownCodeEditor : "SALE_STATE_CD.SALE", cellFilter : "saleStateFilter"},
		             { field: 'pmsProduct.salePrice'				, colKey:"pmsProduct.salePrice"},
		             { field: 'pmsProduct.pmsBrand.name'			, colKey:"pmsBrand.name"},
		             { field: 'pmsProduct.displayYn'				, colKey: "c.pms.product.display.yn", cellFilter : "brandDisplayFilter"},
                     { field: 'pmsProduct.saleStartDt'				, colKey: "c.sps.common.pms.saleStartDt"},
                     { field: 'pmsProduct.saleEndDt'				, colKey: "c.sps.common.pms.saleEndDt"}
//                     ,
//		             { field: 'insId'								, colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
//		             { field: 'insDt'								, colKey: "c.grid.column.insDt"  },
//		             { field: 'updId'								, colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
//		             { field: 'updDt'								, colKey: "c.grid.column.updDt"  }
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_ex",	//mandatory			
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : true, pagination : false
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.exGrid = new gridService.NgGrid(gridParam);
	
	//================ grid	pr
	columnDefs = [
		             { field: 'pmsProduct.productId'			, colKey:"pmsProduct.productId"		,linkFunction:"productDetailPop", type:"number"},		             
		             { field: 'pmsProduct.name'					, colKey:"pmsProduct.name"			,linkFunction:"productDetailPop"},
		             { field: 'pmsProduct.productTypeCd'		, colKey:"pmsProduct.productTypeCd" ,dropdownCodeEditor : "PRODUCT_TYPE_CD", cellFilter : "productTypeFilter"},
		             { field: 'pmsProduct.saleStateCd'		, colKey:"pmsProduct.saleStateCd"	,dropdownCodeEditor : "SALE_STATE_CD.SALE", cellFilter : "saleStateFilter"},
		             { field: 'pmsProduct.salePrice'			, colKey:"pmsProduct.salePrice"},
		             { field: 'pmsProduct.pmsBrand.name'		, colKey:"pmsBrand.name"},
		             { field: 'pmsProduct.displayYn'			, colKey: "c.pms.product.display.yn"},
		             { field: 'pmsProduct.displayYn'				, colKey: "c.pms.product.display.yn"},
                     { field: 'pmsProduct.saleStartDt'				, colKey: "c.sps.common.pms.saleStartDt"},
                     { field: 'pmsProduct.saleEndDt'				, colKey: "c.sps.common.pms.saleEndDt"}
//                     ,
//		             { field: 'insId'							, colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
//		             { field: 'insDt'							, colKey: "c.grid.column.insDt"  },
//		             { field: 'updId'							, colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
//		             { field: 'updDt'							, colKey: "c.grid.column.updDt"  }	
		         ];
	
	gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_pr",	//mandatory			
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : true, pagination : false
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.prGrid = new gridService.NgGrid(gridParam);
	
	//================ grid	ca	
	columnDefs = [
			             { field: 'dmsDisplaycategory.displayCategoryId', 	 width:'150', colKey: "dmsDisplaycategory.displayCategoryId" ,linkFunction:"categoryDetailPop", type:"number"},
	                     { field: 'dmsDisplaycategory.name',				 width:'150', colKey: "dmsDisplaycategory.name"				,linkFunction:"categoryDetailPop"}
//			             ,
//			             { field: 'insId', 									colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
//			             { field: 'insDt', 									colKey: "c.grid.column.insDt"  },
//			             { field: 'updId', 									colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
//			             { field: 'updDt', 									colKey: "c.grid.column.updDt"  }	
			         ];
		
	gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_ca",	//mandatory			
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : true, pagination : false
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.caGrid = new gridService.NgGrid(gridParam);
		
	
	//================ grid	br	
	columnDefs = [
			             { field: 'pmsBrand.brandId', 						 width:'150', colKey: "pmsBrand.brandId"		,linkFunction:"brandDetailPop", type:"number"},		             
			             { field: 'pmsBrand.name',							 width:'150', colKey: "pmsBrand.name"			,linkFunction:"brandDetailPop"}
//			             ,
//			             { field: 'insId', 									colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
//			             { field: 'insDt', 									colKey: "c.grid.column.insDt"  },
//			             { field: 'updId', 									colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
//			             { field: 'updDt', 									colKey: "c.grid.column.updDt"  }	
		             ];
	
	gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_br",	//mandatory			
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : true, pagination : false
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.brGrid = new gridService.NgGrid(gridParam);
	
	
	//적용대상
	openTarget = function(value){
		$scope.div_ex = false;
		$scope.div_pr = false;
		$scope.div_ca = false;
		$scope.div_br = false;
		
		clearGridData(value);
	}
	
	//초기화
	clearGridData = function(value){
		if(value == "TARGET_TYPE_CD.ALL"){			
			$scope.div_ex = true;	
			$scope.grid_pr.data =[];
			$scope.grid_br.data =[];
			$scope.grid_ca.data =[];
		}else if(value == "TARGET_TYPE_CD.PRODUCT"){
			$scope.div_pr = true;
			
			$scope.grid_ex.data =[];			
			$scope.grid_br.data =[];
			$scope.grid_ca.data =[];
		}else if(value == "TARGET_TYPE_CD.CATEGORY"){
			$scope.div_ca = true;
			$scope.div_ex = true;
						
			$scope.grid_pr.data =[];
			$scope.grid_br.data =[];			
		}else if(value == "TARGET_TYPE_CD.BRAND"){
			$scope.div_br = true;
			$scope.div_ex = true;
			
			$scope.grid_pr.data =[];						
			$scope.grid_ca.data =[];
		}
	}
	
	$scope.$watch('spsPointsave.ccsApply.targetTypeCd',function(value){
		openTarget(value);
	});
	
	$scope.$watch('spsPointsave.pointSaveTypeCd',function(value){
		if(value == "POINT_SAVE_TYPE_CD.MULTIPLY"){
			$scope.spsPointsave.pointValueA = "";
		}else{
			$scope.spsPointsave.pointValueM = "";			
		}
	});
	
	//추가팝업
	this.addPopup = function(gubun){
		$scope.gubun = gubun;
		
		if(gubun == "ca"){
			var url = '/dms/displayCategory/popup/search';
			commonPopupService.categoryPopup($scope, "callback_addpopup", false, url);
		}else if(gubun == "br"){
			commonPopupService.brandPopup($scope,"callback_addpopup", true);
		}else if(gubun == "pr" || gubun == "ex"){
			commonPopupService.productPopup($scope,"callback_addpopup", true);
		}
		
	}
	
	$scope.popupData = {};
	$scope.callback_addpopup = function(data){
		$scope.popupData = angular.copy(data);
		common.safeApply($scope);
		
		var gridName = $scope['grid_'+$scope.gubun];
		var ngGrid = $scope[$scope.gubun+'Grid'];
		
		if($scope.gubun != "ca"){
			// 상품 (제외 /적용 )
			if($scope.gubun == "ex" || $scope.gubun == "pr"){
				for (var i=0; i<$scope.popupData.length; i++ ) {
					var flag = true;
					for (var j = 0; j < gridName.data.length; j++) {
						if ( gridName.data[j].pmsProduct.productId == $scope.popupData[i].productId) {
							flag = false;
							break;
						}
					}
					if (flag) {
						console.log($scope.popupData);
						ngGrid.addRow({
							targetId : $scope.popupData[i].productId			// 적용대상 ID
							, productId : $scope.popupData[i].productId			// 제외상품 ID
							, pmsProduct : {	
								productId : $scope.popupData[i].productId
								, name : $scope.popupData[i].name
								, productTypeCd : $scope.popupData[i].productTypeCd
								, saleStateCd : $scope.popupData[i].saleStateCd
								, salePrice : $scope.popupData[i].salePrice
								, displayYn : $scope.popupData[i].displayYn
								, saleStartDt : $scope.popupData[i].saleStartDt
								, saleEndDt : $scope.popupData[i].saleEndDt
								, pmsBrand : {
									name : $scope.popupData[i].brandName
								}
							}
						});				
					}
				}
			}	// 상품 end
			//브랜드
			else if($scope.gubun == "br"){
				for (var i=0; i<$scope.popupData.length; i++ ) {
					var flag = true;
					for (var j = 0; j < gridName.data.length; j++) {
						if ( gridName.data[j].pmsBrand.brandId == $scope.popupData[i].brandId) {
							flag = false;
							break;
						}
					}
					if (flag) {
						ngGrid.addRow({
							targetId : $scope.popupData[i].brandId
							, brandId : $scope.popupData[i].brandId
							, applyNo : $scope.spsPointsave.ccsApply.applyNo
							, saveType : 'I'
								, pmsBrand : {
									brandId : $scope.popupData[i].brandId
									, name : $scope.popupData[i].name
								}
						});
					}
				}
			}	// 브랜드 end
			
		}
		// 카테고리
		else{
			var flag = true;
			for (var j = 0; j < gridName.data.length; j++) {
				if ( gridName.data[j].dmsDisplaycategory.displayCategoryId == $scope.popupData.displayCategoryId) {
					flag = false;
					break;
				}
			}
			if (flag) {
				ngGrid.addRow({
					targetId : $scope.popupData.displayCategoryId
					, displayCategoryId : $scope.popupData.displayCategoryId
					, upperDisplayCategoryId : $scope.popupData.upperDisplayCategoryId
					, applyNo : $scope.spsPointsave.ccsApply.applyNo
					, saveType : 'I'
					, dmsDisplaycategory : {
						displayCategoryId : $scope.popupData.displayCategoryId
						, name : $scope.popupData.name
					}
				});
			}
		}
	}
	
	//삭제
	this.deleteGridData = function(gubun){
		var gridName = gubun+"Grid";		
		$scope[gridName].deleteRow();
	}
	
	
	// 포인트 프로모션 진행
	this.pointRun = function(){
		$scope.spsPointsave.pointSaveStateCd = "POINT_SAVE_STATE_CD.RUN";
		this.updatePointSate("run");
	};
	
	// 포인트 프로모션 정지
	this.pointStop = function(){
		$scope.spsPointsave.pointSaveStateCd = "POINT_SAVE_STATE_CD.STOP";
		this.updatePointSate("stop");
	};	
	
	this.updatePointSate = function(gb) {
//		if(!$scope.paramCheck()) return false;
		
		var msg = "";
		var preState = "";
		var afterState = "";
		if(gb == "run") {
			preState = "대기";
			afterState = "진행";
		} else if(gb == "stop") {
			preState = "진행";
			afterState = "중지";
		}
		
		msg = "'"+ preState +"상태'를 '" + afterState + "상태' (으)로 변경하시겠습니까?";
		
		if (confirm(msg)) {
			pointService.updatePointState($scope.spsPointsave, function(response) {
				if(response.content != null) {
					
					alert("'"+afterState+"상태'(으)로 변경되었습니다.");
					
		        	pScope._pointSaveId = response.content;
		        	pScope.searchPoint();
		    		$window.location.reload();

				} else {
					alert(pScope.MESSAGES["common.label.alert.fail"]);
				}
			});
		} else {
			if(gb == "run") {
				$scope.spsPointsave.pointSaveStateCd = "POINT_SAVE_STATE_CD.READY";
				return true;
			} else if(gb == "stop") {
				$scope.spsPointsave.pointSaveStateCd = "POINT_SAVE_STATE_CD.RUN";
				return true;
			}
			return false;
		}
		common.safeApply($scope);
	}
	
	//저장
	this.savePoint = function(){
		
		// 파라메터 체크 및 세팅
		if(!$scope.paramCheck()) return false;
		
		if (window.confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			pointService.savePoint($scope.spsPointsave,function(response){
				if(response.content != null) {
					alert(pScope.MESSAGES["common.label.alert.save"]);
					
					pScope._pointSaveId = response.content;
					pScope.searchPoint();
					$window.location.reload();
					
				} else {
					alert(pScope.MESSAGES["common.label.alert.fail"]);
				}
			});
		}
	}

	
	// 파라메터 체크 및 세팅
	$scope.paramCheck = function () {
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return false;
		}
		
		if(!$scope.spsPointsave.startDt || !$scope.spsPointsave.endDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		// 적용대상 
		$scope.spsPointsave.ccsApply.ccsExcproducts = [];
		$scope.spsPointsave.ccsApply.ccsApplytargets = [];
		
		var new_targetType = $scope.spsPointsave.ccsApply.targetTypeCd;
		var gridName = "";
		if(new_targetType == "TARGET_TYPE_CD.ALL"){
			$scope.spsPointsave.ccsApply.ccsExcproducts = $scope.grid_ex.data;			
		}else if(new_targetType == "TARGET_TYPE_CD.PRODUCT"){
			if($scope.grid_pr.data.length == 0) {
				gridName = "grid_pr";
			} else {
			}
			$scope.spsPointsave.ccsApply.ccsApplytargets = $scope.grid_pr.data;			
		}else if(new_targetType == "TARGET_TYPE_CD.BRAND"){
			if($scope.grid_br.data.length == 0) {
				gridName = "grid_br";
			} else {
				$scope.spsPointsave.ccsApply.ccsApplytargets = $scope.grid_br.data;
				$scope.spsPointsave.ccsApply.ccsExcproducts = $scope.grid_ex.data;
			}
		}else if(new_targetType == "TARGET_TYPE_CD.CATEGORY"){
			if($scope.grid_ca.data.length == 0) {
				gridName = "grid_ca";
			} else {
				$scope.spsPointsave.ccsApply.ccsApplytargets = $scope.grid_ca.data;
				$scope.spsPointsave.ccsApply.ccsExcproducts = $scope.grid_ex.data;
			}
		}
		
		if(gridName != "") {
			alert(pScope.MESSAGES["sps.common.enter.apply.target"]);
			angular.element("[data-ui-grid=\'"+gridName+"\']").focus();
			return false;
		}
		
		if($scope.spsPointsave.pointSaveTypeCd == "POINT_SAVE_TYPE_CD.MULTIPLY"){
			$scope.spsPointsave.pointValue = $scope.spsPointsave.pointValueM;
		}else{
			$scope.spsPointsave.pointValue = $scope.spsPointsave.pointValueA;
		}
		
		return true;
	}
	
	//취소
	this.close = function(){
//		if(angular.isDefined($scope.spsPointsave.pointSaveId)){
//			$window.opener.$scope.pointGrid.loadGridData();	
//		}		
		window.close();
	}
	$scope.currentTab = "";
	//excel upload
	this.addExcel = function(value){
		$scope.currentTab = value;
		var param = "";
		if(value == "pr" || value=="ex"){
			param = "product";
		}else if(value == "br"){
			param = "brand";
		}else if(value == "ca"){
			param = "category";	
		}
		commonPopupService.gridbulkuploadPopup($scope,"callback_grid",param);
	}
	
	$scope.callback_grid = function(response){
		$scope[$scope.currentTab+'Grid'].setData(response.resultList,true);			
	}
	
	/* ******************** 사용대상 링크 팝업 정의 ********************** */
	$scope.productDetailPop = function(field, row) {
		commonPopupService.openProductDetailPopup($scope, row.pmsProduct.productId);
	}
	$scope.brandDetailPop = function(field, row) {
		commonPopupService.openBrandDetailPopup($scope, row.pmsBrand.brandId);
	}
	$scope.categoryDetailPop = function(field, row) {
		$scope.paramCategory = row.dmsDisplaycategory;
		$scope.openPop("/dms/displayCategory/manager", "전시카테고리상세");
	}
	
	$scope.openPop = function(url, winName) {
		popupwindow(url,winName,1200,600);
	}
}).filter('productTypeFilter', function() {// PRODUCT_TYPE_CD 
	
	var comboHash = {
		    'PRODUCT_TYPE_CD.GENERAL': '일반상품',
		    'PRODUCT_TYPE_CD.OPTION': '추가구성상품',
		    'PRODUCT_TYPE_CD.PRESENT': '사은품',
		    'PRODUCT_TYPE_CD.SET': '세트상품'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('saleStateFilter', function() {// SALEPRODUCT_STATE_CD 
	
	var comboHash = {
			'SALE_STATE_CD.END': '영구종료',
		    'SALE_STATE_CD.SALE': '판매중',
		    'SALE_STATE_CD.SOLDOUT': '품절',
		    'SALE_STATE_CD.STOP': '판매중지'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('brandDisplayFilter', function() {// SALEPRODUCT_STATE_CD 
	
	var comboHash = {
			'Y': '노출',
		    'N': '비노출'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
});
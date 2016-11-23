//message init
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.delete", "common.label.alert.fail"
                          ,"common.label.confirm.save", "sps.common.enter.apply.deal","sps.common.enter.apply.target"
                          , "sps.present.apply.product", "sps.common.invalid.delete"
                          , "sps.present.enter.min.amt", "sps.present.enter.max.amt", "sps.present.enter.select.qty"
                          , "sps.common.promotion.run", "sps.common.promotion.stop"
                          ,"sps.common.promotion.run.complete", "sps.common.promotion.stop.complete"
                          ,"sps.present.invalid.order.amt"
                          ];

var presentPromotionApp = angular.module("presentPromotionApp", [	'commonServiceModule', 'gridServiceModule', "commonPopupServiceModule",
                                               	'spsServiceModule', 'ccsServiceModule',
                                               	'ui.date',
                                               	'ngCkeditor'	// 삭제예정
                                               	]);
presentPromotionApp.controller("sps_presentPromotionListApp_controller", function($window, $scope, $filter, promotionPresentService, commonService, gridService) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	$scope.infoType = [
	                    {val : 'ID',text : '프로모션 번호'},
	                    {val : 'NAME',text : '프로모션 명'}
		              ];
	
	// 초기화
	angular.element(document).ready(function () {
//		var date = new Date();
//		$scope.search.startDate = $filter('date')(date.setDate(date.getDate()-7), Constants.date_format_2);
//		
//		var date2 = new Date();
//		date2 = date2.setHours(23, 59, 59, 00);
//		$scope.search.endDate = $filter('date')(date2, Constants.date_format_2);
		
		commonService.init_search($scope,'search');
	});			
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	var columnDefs = [
	                 { field: 'presentId'		, width:'100', colKey: "c.sps.present.grid.col.present.id", linkFunction: "detailPresent" , type:"number"},
	                 { field: 'name'			, width:'100', colKey: "spsPresent.name" , linkFunction: "detailPresent" },
	                 { field: 'presentTypeName'	, width:'100', colKey: "spsPresent.presentTypeCd"	,vKey: 'spsPresent.presentTypeCd'  },
	                 { field: 'presentStateName', width:'100', colKey: "c.sps.present.grid.col.present.state.cd"	,vKey: 'spsPresent.presentStateCd'  },
	                 { field: 'startDt'			, width:'100', colKey: "c.sps.present.grid.col.start.dt" , cellFilter: "date:\'yyyy-MM-dd\'" },
	                 { field: 'endDt'			, width:'100', colKey: "c.sps.present.grid.col.end.dt", cellFilter: "date:\'yyyy-MM-dd\'" },
	                 { field: 'insId'			, width:'100', colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
	                 { field: 'insDt'			, width:'100', colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
	                 { field: 'updId'			, width:'100', colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
	                 { field: 'updDt'			, width:'100', colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
		            ];
	
	var gridParam = {
			scope : $scope, 				//mandatory
			gridName : "gridOpt",			//mandatory
			url :  '/api/sps/present',  	//mandatory
			searchKey : "search",	//mandatory
			columnDefs : columnDefs,		//mandatory
			gridOptions : {             //optional
//				checkBoxEnable : false
			}
	};
	
	//그리드 초기화
	$scope.gridOptions = new gridService.NgGrid(gridParam);

// CK_EDITOR 옵션값
//	$scope.ckOption = {
//		language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
//		, filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
//	}
	
	$scope.searchPresent = function() {
		$scope.gridOptions.loadGridData();
	};
	
	this.resetData = function() {
		/* search Data 초기화 */
//		angular.element(".day_group").find('button').removeClass("on");
		
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	this.deletePresent = function() {
		var check = true;
		var rows = $scope.gridOptions.getSelectedRows();
		for(var i=0; i<rows.length; i++) {
			if(rows[i].presentStateCd != "PRESENT_STATE_CD.READY") {
				check = false;
			} 
		}
		if(check) {
			$scope.gridOptions.deleteGridData();
		} else {
			alert($scope.MESSAGES["sps.common.invalid.delete"]);
		}
	}
	
	// 상세 팝업 호출
	$scope.detailPresent = function(field, row) {
		$scope.popOpen(row.presentId);
	}
	
	// 등록 팝업 호출
	this.openPopupInsert = function(){
		$scope.popOpen();
	}
	
	$scope.popOpen = function(id) {
		$scope.presentId = "";
		if(angular.isDefined(id) && id != '') {
			$scope.presentId = id;
			winName = "사은품 프로모션 상세";
		} else {
			winName = "사은품 프로모션 등록";
		}
		
		url = "/sps/present/popup/detail";
		popupwindow(url,winName,1200,600);
	}
	
}).controller("sps_presentPromotionDetailPopApp_controller", function($window, $scope, $filter, promotionPresentService, applyService, commonService, gridService, commonPopupService) {
/* ****************** detail POPUP Controller *************************/
	// 팝업에서 부모 scope 접근하기 위함.		
	pScope = $window.opener.$scope;
	$window.$scope = $scope;
	
	$scope.spsPresent = {};
	$scope.spsPresent.ccsApply = {};
	
	$scope.changeFlag = true;
	
	$scope.targetTypeCds = [
	            {val : 'TARGET_TYPE_CD.ALL',text : '전체'},
	            {val : 'TARGET_TYPE_CD.PRODUCT',text : '상품'},
	            {val : 'TARGET_TYPE_CD.CATEGORY',text : '전시카테고리'},
	            {val : 'TARGET_TYPE_CD.BRAND',text : '브랜드'}
	           ];
	
	// 딜전문관 코드배열 저장
	$scope.dealTypeCd = [];
	$scope.tmpCd = ["DEAL_TYPE_CD.PREMIUM","DEAL_TYPE_CD.MEMBER","DEAL_TYPE_CD.SHOCKDEAL","DEAL_TYPE_CD.EMPLOYEE","DEAL_TYPE_CD.CHILDREN"];
	
	$scope.searchPresent = function(presentId) {
		$scope.spsPresent.presentId = presentId;
		
		promotionPresentService.getPresentPromotionDetail($scope.spsPresent, function(response){
			
			// 기존 그리드 화면 저장
			$scope.spsPresent = response;
			$scope.spsPresent.orgTargetTypeCd = response.targetTypeCd;

			// 딜전문관 적용
			if(response.dealApplyYn == "Y") {
				if(response.spsDeals.length == $scope.tmpCd.length) {
					$scope.state1 = true;
					for(var i=0; i<$scope.tmpCd.length; i++ ) {
						$scope["state"+(i+2)] = true;
					}
					$scope.dealTypeCd = angular.copy($scope.tmpCd);
				} else {
					for(var i=0; i<response.spsDeals.length; i++ ) {
						$scope.dealTypeCd.push(response.spsDeals[i].dealTypeCd);
						switch(response.spsDeals[i].dealTypeCd) {
							case 'DEAL_TYPE_CD.PREMIUM' : $scope.state2 = true; break;
							case 'DEAL_TYPE_CD.MEMBER' : $scope.state3 = true; break;
							case 'DEAL_TYPE_CD.SHOCKDEAL' : $scope.state4 = true; break;
							case 'DEAL_TYPE_CD.EMPLOYEE' : $scope.state5 = true; break;
							case 'DEAL_TYPE_CD.CHILDREN' : $scope.state6 = true; break;
						}
					}
				}
				// 코드 String 저장
				var resultCd = ""
					for(var i=0; i<$scope.dealTypeCd.length; i++) {
						resultCd += "'" + $scope.dealTypeCd[i] + "',";
					}
				$scope.spsPresent.dealTypeCds = resultCd.slice(0,-1);
			}
			
			// 사은품 프로모션 생성 후 진행 , 변경은 'name'과 '프로모션 기간' 만 가능하다
			if($scope.spsPresent.presentStateCd == 'PRESENT_STATE_CD.RUN') {
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
				
				angular.element('[ng-model="spsPresent.startDt"]').find('input').attr('disabled', false);
				angular.element('[ng-model="spsPresent.startDt"]').find('input').attr('readonly', false);
				angular.element('[ng-model="spsPresent.startDt"]').find('button').attr('disabled', false);
				angular.element('[ng-model="spsPresent.startDt"]').find('button').attr('readonly', false);
				
				angular.element('[ng-model="spsPresent.endDt"]').find('input').attr('disabled', false);
				angular.element('[ng-model="spsPresent.endDt"]').find('input').attr('readonly', false);
				angular.element('[ng-model="spsPresent.endDt"]').find('button').attr('disabled', false);
				angular.element('[ng-model="spsPresent.endDt"]').find('button').attr('readonly', false);
				
				// 변경대상 : 이름,발급기간,최대발급 매수
				$scope.spsPresent.editAll = false;
			} else if($scope.spsPresent.presentStateCd == 'PRESENT_STATE_CD.STOP') {
				$scope.changeFlag = false;
				angular.element('[name="form"]').find('input, button').attr('readonly', true);
				angular.element('[name="form"]').find('input, button').attr('disabled', true);
				angular.element('[name="gridForm"]').find('input, button').attr('readonly', true);
				angular.element('[name="gridForm"]').find('input, button').attr('disabled', true);
				
				angular.element('[name="mainSave"]').attr('disabled', true);
				angular.element('[name="mainStop"]').attr('disabled', true);
			} 
			else {
				// 변경대상 : 전체
				$scope.spsPresent.editAll = true;
			}
			
			// 그리드 데이터 조회
			var value = response.targetTypeCd;
			
			var gridNameTarget = "";
			var gridNameEx = "";
			if (value == "TARGET_TYPE_CD.ALL") {
				gridNameEx = "excludeProduct";
			} else if (value == "TARGET_TYPE_CD.PRODUCT") {
				gridNameTarget = "applyTargetPr";
			} else if (value == "TARGET_TYPE_CD.CATEGORY") {
				gridNameTarget = "applyTargetCa";
				gridNameEx = "excludeProduct";
			} else if (value == "TARGET_TYPE_CD.BRAND") {
				gridNameTarget = "applyTargetBr";
				gridNameEx = "excludeProduct";
			}
			
			if(angular.isDefined($scope.spsPresent.ccsApply.ccsApplytargets)
					&& $scope.spsPresent.ccsApply.ccsApplytargets != null
					&& $scope.spsPresent.ccsApply.ccsApplytargets.length > 0){
				var target = $scope.spsPresent.ccsApply.ccsApplytargets;
				$scope[gridNameTarget].data = target; 					
			}
			
			if(angular.isDefined($scope.spsPresent.ccsApply.ccsExcproducts)
					&& $scope.spsPresent.ccsApply.ccsExcproducts != null
					&& $scope.spsPresent.ccsApply.ccsExcproducts.length > 0){
				
				var ex = $scope.spsPresent.ccsApply.ccsExcproducts;
				$scope[gridNameEx].data = ex;
			}
			
			$scope.presentProduct.data = $scope.spsPresent.spsPresentproducts;
			
			// 기존 사용대상, 제공사은품 정보 삭제
			$scope.spsPresent.ccsApply.ccsApplytargets = null;
			$scope.spsPresent.ccsApply.ccsExcproducts = null;
			$scope.spsPresent.spsPresentproducts = null;
			
			common.safeApply($scope);
		});
	};
	
	// insert / detail 분기
	angular.element(document).ready(function () {
		if(angular.isDefined(pScope.presentId) 
				&& pScope.presentId != ""){
			$scope.searchPresent(pScope.presentId);
		} else {
//			var date = new Date();
//			date.setHours(00, 00, 00, 00);
//			
//			var date2 = new Date();
//			date2 = date2.setHours(23, 59, 59, 00);
//			
//			$scope.spsPresent.startDt = $filter('date')(date, Constants.date_format_1);
//			$scope.spsPresent.endDt = $filter('date')(date2, Constants.date_format_1);
		}
	});
	
	// 그리드 param function
	function gridParam (nm, key, col) {
		var gridParam = {};
		gridParam = {
				scope : $scope, 
				gridName : nm,
				searchKey : key, 
				columnDefs : col,
				gridOptions : {             		//optional
					pagination : false
				},
				callbackFn : function(){//optional
				}
		};
		return gridParam
	};

	/* ******************** 그리드 컬럼 정의 ********************** */
	var presentColumn = [
		                   { field: 'pmsProduct.productId'			, colKey: "spsPresent.presentId"	,linkFunction:"presentDetailPop", type:"number"},
		                   { field: 'pmsProduct.name'				, colKey: "c.pms.present.name"		,linkFunction:"presentDetailPop"},
		                   { field: 'pmsSaleproduct.realStockQty'	, colKey: "c.sps.present.stock.qty"	},
		                   { field: 'pmsProduct.useYn'				, colKey: "pmsProduct.useYn",	cellFilter:"useYnFilter"		}
		       			
//		                   ,
//		                   { field: 'insId'							, colKey: "c.grid.column.insId"  	, userFilter :'insId,insName'},
//		                   { field: 'insDt'							, colKey: "c.grid.column.insDt" 	, cellFilter: "date:\'yyyy-MM-dd\'" },
//                           { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//                           { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
		                   ];
	var excludeProductColumn = [
	     	                   { field: 'pmsProduct.productId'			, colKey: "c.sps.coupon.product.id"		,linkFunction:"productDetailPop", type:"number"},
	     	                   { field: 'pmsProduct.name'				, colKey: "c.sps.coupon.product.name"	,linkFunction:"productDetailPop"},
	     	                   { field: 'pmsProduct.productTypeCd'	, colKey: "c.sps.coupon.product.type"	,dropdownCodeEditor : "PRODUCT_TYPE_CD", cellFilter : "productTypeFilter"},
	     	                   { field: 'pmsProduct.saleStateCd'		, colKey: "c.sps.coupon.sale.state"		,dropdownCodeEditor : "SALE_STATE_CD.SALE", cellFilter : "saleStateFilter"},
	     	                   { field: 'pmsProduct.salePrice'			, colKey: "pmsProduct.salePrice"			},
	     	                   { field: 'pmsBrand.name'					, colKey: "c.sps.coupon.brand"			},
	     	                   { field: 'pmsProduct.displayYn'			, colKey: "c.pms.product.display.yn", cellFilter : "brandDisplayFilter"			},
	                            { field: 'pmsProduct.saleStartDt'		, colKey: "c.sps.common.pms.saleStartDt"			},
	                            { field: 'pmsProduct.saleEndDt'			, colKey: "c.sps.common.pms.saleEndDt"			}
//	                            ,
//	     	                   { field: 'insId'			, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//	     	                   { field: 'insDt'			, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//	                            { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//	                            { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
	     	                   ];
 	var applyProduct = [
 	                   { field: 'pmsProduct.productId'			, colKey: "c.sps.coupon.product.id"		,linkFunction:"productDetailPop", type:"number"},
 	                   { field: 'pmsProduct.name'				, colKey: "c.sps.coupon.product.name"	,linkFunction:"productDetailPop"},
 	                   { field: 'pmsProduct.productTypeCd'	, colKey: "c.sps.coupon.product.type"	,dropdownCodeEditor : "PRODUCT_TYPE_CD", cellFilter : "productTypeFilter"},
 	                   { field: 'pmsProduct.saleStateCd'		, colKey: "c.sps.coupon.sale.state"		,dropdownCodeEditor : "SALE_STATE_CD.SALE", cellFilter : "saleStateFilter"},
 	                   { field: 'pmsProduct.salePrice'			, colKey: "pmsProduct.salePrice"			},
 	                   { field: 'pmsBrand.name'					, colKey: "c.sps.coupon.brand"			},
 	                   { field: 'pmsProduct.displayYn'			, colKey: "c.pms.product.display.yn"			},
                       { field: 'pmsProduct.saleStartDt'		, colKey: "c.sps.common.pms.saleStartDt"			},
                       { field: 'pmsProduct.saleEndDt'			, colKey: "c.sps.common.pms.saleEndDt"			}
//                       ,
// 	                   { field: 'insId'			, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
// 	                   { field: 'insDt'			, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//                       { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//                       { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
 	                   ];
	var applyBrandColumn = [
							{ field: 'pmsBrand.brandId'		, width:'150', colKey: "pmsProduct.brandId"	,linkFunction:"brandDetailPop", type:"number"},
							{ field: 'pmsBrand.name'		, width:'150', colKey: "pmsBrand.name"		,linkFunction:"brandDetailPop"}
//							,
//							{ field: 'insId'				, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//							{ field: 'insDt'				, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//                            { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//                            { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
							];
	var applyCategoryColumn = [
	                           { field: 'dmsDisplaycategory.displayCategoryId', 	 width:'150', colKey: "dmsDisplaycategory.displayCategoryId" ,linkFunction:"categoryDetailPop", type:"number"},
	                           { field: 'dmsDisplaycategory.name',					 width:'150', colKey: "dmsDisplaycategory.name" ,linkFunction:"categoryDetailPop"}
//	                           ,
//	                           { field: 'insId'					,	colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//	                           { field: 'insDt'					,	colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd hh:mm:ss\'" },
//	                            { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//	                            { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
	                           ];
	
	/* 그리드 컬럼주입 */
	// 제외상품
	$scope.excludeProductGrid = new gridService.NgGrid(
			gridParam('excludeProduct', 'spsPresent' , excludeProductColumn)
	);
	
	// 브랜드
	$scope.applyTargetBrGrid = new gridService.NgGrid(
			gridParam('applyTargetBr', 'spsPresent', applyBrandColumn)
	);
	// 카테고리
	$scope.applyTargetCaGrid = new gridService.NgGrid(
			gridParam('applyTargetCa', 'spsPresent', applyCategoryColumn)
	);
	// 상품
	$scope.applyTargetPrGrid = new gridService.NgGrid(
			gridParam('applyTargetPr', 'spsPresent', applyProduct)
	);
	// 제공 사은품 그리드 On 
	$scope.presentProductGrid = new gridService.NgGrid(
			gridParam('presentProduct', 'spsPresent', presentColumn)
	);

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
			$scope.applyTargetPr.data =[];
			$scope.applyTargetBr.data =[];
			$scope.applyTargetCa.data =[];
		}else if(value == "TARGET_TYPE_CD.PRODUCT"){
			$scope.div_pr = true;
			
			$scope.excludeProduct.data =[];
			$scope.applyTargetBr.data =[];
			$scope.applyTargetCa.data =[];
		}else if(value == "TARGET_TYPE_CD.CATEGORY"){
			$scope.div_ca = true;
			$scope.div_ex = true;
						
			$scope.applyTargetPr.data =[];
			$scope.applyTargetBr.data =[];			
		}else if(value == "TARGET_TYPE_CD.BRAND"){
			$scope.div_br = true;
			$scope.div_ex = true;
			
			$scope.applyTargetPr.data =[];						
			$scope.applyTargetCa.data =[];
		}
	}
	
	$scope.$watch('spsPresent.presentTypeCd',function(value){
		if(value == 'PRESENT_TYPE_CD.PRODUCT') {
			$scope.spsPresent.ccsApply.targetTypeCd = "TARGET_TYPE_CD.PRODUCT";
			// 주문금액 초기화
			$scope.spsPresent.minOrderAmt = "";
			$scope.spsPresent.maxOrderAmt = "";
		}
	});
	
	$scope.$watch('spsPresent.ccsApply.targetTypeCd',function(value){
		openTarget(value);
	});
	
	// 주문금액 초기화
	$scope.spsPresent.minOrderAmt = "";
	$scope.spsPresent.maxOrderAmt = "";
	
	// 선택 사은품 수량 초기화
	$scope.spsPresent.selectQty = "";
	
	// 사은품 프로모션 진행
	this.presentRun = function(){
		$scope.spsPresent.presentStateCd = "PRESENT_STATE_CD.RUN";
		this.updatePresentSate("run")
	};	
	
	// 사은품 프로모션 정지
	this.presentStop = function(){
		$scope.spsPresent.presentStateCd = "PRESENT_STATE_CD.STOP";
		this.updatePresentSate("stop")
	};	
	
	this.updatePresentSate = function(gb) {
		
		var msg = ""
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
			promotionPresentService.updatePresentState($scope.spsPresent, function(response) {
				if(response.content != null) {
					alert("'"+afterState+"상태'(으)로 변경되었습니다.");
					pScope.presentId = response.content;
					pScope.searchPresent();
		    		$window.location.reload();
				} else {
					alert(pScope.MESSAGES["common.label.alert.fail"]);
				}
			});
		} else {
			if(gb == "run") {
				$scope.spsPresent.presentStateCd = "PRESENT_STATE_CD.READY";
				return true;
			} else if(gb == "stop") {
				$scope.spsPresent.presentStateCd = "PRESENT_STATE_CD.RUN";
				return true;
			}
			return false;
		}
		common.safeApply($scope);
	}
	
	this.updatePresentPromotion = function() {

		if(!$scope.paramCheck()) return false;
		
		if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			promotionPresentService.updatePresent($scope.spsPresent, function(response){
				if(response.content != null) {
					alert(pScope.MESSAGES["common.label.alert.save"]);
		        	
					pScope.presentId = response.content;
					pScope.searchPresent();
		    		$window.close();
				} else {
					alert(pScope.MESSAGES["common.label.alert.fail"]);
				}
			
			});
		}
	}
	
	// 파라메터 체크 및 세팅
	$scope.paramCheck = function() {
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return false;
		}
		
		if(!$scope.spsPresent.startDt || !$scope.spsPresent.endDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		// 주문사은품
		if($scope.spsPresent.presentTypeCd == 'PRESENT_TYPE_CD.ORDER') {
			if(angular.isUndefined($scope.spsPresent.minOrderAmt) || $scope.spsPresent.minOrderAmt == '') {
				alert(pScope.MESSAGES["sps.present.enter.min.amt"]);
				angular.element("#minAmt").focus();
				return false;
			}
			if(angular.isUndefined($scope.spsPresent.maxOrderAmt) || $scope.spsPresent.maxOrderAmt == '') {
				alert(pScope.MESSAGES["sps.present.enter.max.amt"]);
				angular.element("#maxAmt").focus();
				return false;
			}
			
			if(Number($scope.spsPresent.minOrderAmt) > Number($scope.spsPresent.maxOrderAmt))  {
				alert(pScope.MESSAGES["sps.present.invalid.order.amt"]);
				angular.element("#minAmt").focus();
				return false
			}
			
		} 
		// 상품사은품
		else if($scope.spsPresent.presentTypeCd == 'PRESENT_TYPE_CD.PRODUCT') {
			$scope.spsPresent.minOrderAmt = "";
			$scope.spsPresent.maxOrderAmt = "";
		}
		
		// 딜 적용 가능 여부
		if ($scope.spsPresent.dealApplyYn == "Y") {
			if(angular.isUndefined($scope.spsPresent.dealTypeCds) || $scope.spsPresent.dealTypeCds == "") {
				alert(pScope.MESSAGES["sps.common.enter.apply.deal"]);
				angular.element('#dealChk').focus();
				return false;
			}
		} else {
			$scope.spsPresent.dealTypeCds = "";
		}
		
		// 선택 사은품 입력
		if($scope.spsPresent.presentSelectTypeCd == "PRESENT_SELECT_TYPE_CD.SELECT") {
			if(angular.isUndefined($scope.spsPresent.selectQty) || $scope.spsPresent.selectQty == '' || Number($scope.spsPresent.selectQty) < 1) {
				alert(pScope.MESSAGES["sps.present.enter.select.qty"]);
				angular.element("#inQty").focus();
				return false;
			}
		}
		// 선택 사은품 유형 전체
		else if($scope.spsPresent.presentSelectTypeCd == "PRESENT_SELECT_TYPE_CD.ALL") {
			$scope.spsPresent.selectQty = "";
		}
		
		var new_targetType = $scope.spsPresent.ccsApply.targetTypeCd;
		var gridName = "";
		// 그리드 데이터 복사
		if(new_targetType == 'TARGET_TYPE_CD.ALL') {
			$scope.spsPresent.ccsApply.ccsExcproducts = $scope.excludeProduct.data;
		} else if (new_targetType == 'TARGET_TYPE_CD.PRODUCT') {
			if($scope.applyTargetPr.data.length == 0) {
				gridName = "applyTargetPr";
			} else {
				$scope.spsPresent.ccsApply.ccsApplytargets = $scope.applyTargetPr.data;
			}
		} else if (new_targetType == 'TARGET_TYPE_CD.BRAND') {
			if($scope.applyTargetBr.data.length == 0) {
				gridName = "applyTargetBr";
			} else {
				$scope.spsPresent.ccsApply.ccsApplytargets = $scope.applyTargetBr.data;
				$scope.spsPresent.ccsApply.ccsExcproducts = $scope.excludeProduct.data;
			}
		} else if (new_targetType == 'TARGET_TYPE_CD.CATEGORY') {
			if($scope.applyTargetCa.data.length == 0) {
				gridName = "applyTargetCa";
			} else {
				$scope.spsPresent.ccsApply.ccsApplytargets = $scope.applyTargetCa.data;
				$scope.spsPresent.ccsApply.ccsExcproducts = $scope.excludeProduct.data;
			}
		}
		
		if(gridName != "") {
			alert(pScope.MESSAGES["sps.common.enter.apply.target"]);
			angular.element("[data-ui-grid=\'"+gridName+"\']").focus();
			return false;
		}
		
		if($scope.presentProduct.data.length < 1) {
			alert(pScope.MESSAGES["sps.present.apply.product"]);
			angular.element('[data-ui-grid="presentProduct"]').focus();
			return false;
		} else {
			$scope.spsPresent.spsPresentproducts = $scope.presentProduct.data
		}
		
		return true;
	}
	
	this.stateChk = function(data, chk) {
		// 전체
		if(data == 'ALL') {
			var boolean;
			if($scope[chk]) {
				boolean = true;
				$scope.dealTypeCd = angular.copy($scope.tmpCd); 
			} else {
				boolean = false;
				$scope.dealTypeCd = [];
			}
			
			for(var i=0; i<$scope.tmpCd.length; i++ ) {
				$scope["state"+(i+2)] = boolean;
			}
		}
		// 각각
		else {
			// 코드 유/무 Check
			var index = $scope.dealTypeCd.indexOf(data);
			if (index >= 0) {
				$scope.dealTypeCd.splice(index, 1);
			} else {
				$scope.dealTypeCd.push(data);
			}
			
			// 전체 Check
			if($scope.dealTypeCd.length == $scope.tmpCd.length) {
				$scope.state1 = true;
			} else {
				$scope.state1 = false;
			}
			
		}
		// 코드 String 저장
		var resultCd = ""
			for(var i=0; i<$scope.dealTypeCd.length; i++) {
				resultCd += "'" + $scope.dealTypeCd[i] + "',";
			}
		$scope.spsPresent.dealTypeCds = resultCd.slice(0,-1);
	}	
	
/* *********************** 취소버튼 *************************/
/** TODO 취소시 부모창 grid data refresh*/
	this.cancelPopup = function() {
//		$window.opener.$scope.gridOptions.loadGridData();	
		$window.close();
	}
	
/* ******************** 각 그리드 버튼 ***************************/
	this.deleteGridData = function(gridName) {
		$scope[gridName].deleteRow();
	}
	
	$scope.popupData = {};
	
	//상품 검색 팝업
	this.productPopup = function(gubun){
		if(angular.isUndefined(gubun)) {
			$scope.gubun = "apply";
		} else {
			$scope.gubun = gubun;
		}
		commonPopupService.productPopup($scope,"callback_product", true);
	}
	$scope.callback_product = function(data){
		$scope.popupData = angular.copy(data);
		common.safeApply($scope);
		
		var nm = "";
		if ($scope.gubun == "apply") {
			// 적용대상 상품 추가
			nm = "applyTargetPr";
		} else if ($scope.gubun == "exclude") {
			// 제외대상 상품 추가
			nm = "excludeProduct";
		}

		for (var i=0; i<$scope.popupData.length; i++ ) {
			var flag = true;
			for (var j = 0; j < $scope[nm].data.length; j++) {
				if ( $scope[nm].data[j].pmsProduct.productId == $scope.popupData[i].productId) {
					flag = false;
					break;
				}
			}
			if (flag) {
				$scope[nm+"Grid"].addRow({
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
				, saveType  : 'I'
					, applyNo : $scope.spsPresent.ccsApply.applyNo
				});
			}
		}
	}
	
	// 브랜드 검색 팝업 
	this.brandPopup = function(){
		commonPopupService.brandPopup($scope,"callback_brand", true);
	}
	$scope.callback_brand = function(data){
		$scope.popupData = angular.copy(data);
		common.safeApply($scope);
		
		$scope.search = {};
		var brandIds = [];
		for (var i=0; i<$scope.popupData.length; i++ ) {
			brandIds.push($scope.popupData[i].brandId);
		}
		$scope.search.idArray = brandIds;
		$scope.search.applyNo = $scope.spsPresent.ccsApply.applyNo;
		
		for (var i=0; i<$scope.popupData.length; i++ ) {
			
			var flag = true;
			for (var j = 0; j < $scope.applyTargetBr.data.length; j++) {
				if ( $scope.applyTargetBr.data[j].pmsBrand.brandId == $scope.popupData[i].brandId) {
					flag = false;
					break;
				}
			}
			if (flag) {
				$scope.applyTargetBrGrid.addRow({
					targetId : $scope.popupData[i].brandId
					, brandId : $scope.popupData[i].brandId
					, applyNo : $scope.spsPresent.ccsApply.applyNo
					, saveType : 'I'
					, pmsBrand : {
						brandId : $scope.popupData[i].brandId
						, name : $scope.popupData[i].name
					}
				});
			}
		}
	}
	
	// 카테고리 검색 팝업
	this.categoryPopup = function() {
		var url = '/dms/displayCategory/popup/search';
		commonPopupService.categoryPopup($scope, "callback_category", false, url);
	}
	$scope.callback_category = function(data) {
		$scope.popupData = angular.copy(data);
		common.safeApply($scope);
		
		var flag = true;
		for (var j = 0; j < $scope.applyTargetCa.data.length; j++) {
			if ( $scope.applyTargetCa.data[j].dmsDisplaycategory.categoryId == $scope.popupData.displayCategoryId) {
				flag = false;
				break;
			}
		}
		if (flag) {
			$scope.applyTargetCaGrid.addRow({
				targetId : $scope.popupData.displayCategoryId
				, displayCategoryId : $scope.popupData.displayCategoryId
				, upperDisplayCategoryId : $scope.popupData.upperDisplayCategoryId
				, applyNo : $scope.spsPresent.ccsApply.applyNo
				, saveType : 'I'
					, dmsDisplaycategory : {
						displayCategoryId : $scope.popupData.displayCategoryId
						, name : $scope.popupData.name
					}
			});
		}

	}
	
	// 사은품 검색 팝업
	this.presentPopup = function() {
		commonPopupService.presentPopup($scope, "callback_present", true);
	}
	$scope.callback_present = function(data) {
		$scope.popupData = angular.copy(data);
		common.safeApply($scope);
		
		$scope.search = {};
		var productIds = [];
		for (var i=0; i<$scope.popupData.length; i++ ) {
			productIds.push($scope.popupData[i].productId);
		}
		$scope.search.idArray = productIds;
		$scope.search.presentId = $scope.spsPresent.presentId;
		
		for (var i=0; i<$scope.popupData.length; i++ ) {
			
			var flag = true;
			for (var j = 0; j < $scope.presentProduct.data.length; j++) {
				if ( $scope.presentProduct.data[j].pmsProduct.productId == $scope.popupData[i].productId) {
					flag = false;
					break;
				}
			}
			if (flag) {
				$scope.presentProductGrid.addRow({
					presentId : $scope.spsPresent.presentId
					, productId : $scope.popupData[i].productId	//TODO java Service 수정후 삭제예정,
					, pmsProduct : {
						productId : $scope.popupData[i].productId
						, name : $scope.popupData[i].name
						, useYn : $scope.popupData[i].useYn
						, pmsSaleproduct : {
							realStockQty : $scope.popupData[i].pmsSaleproduct.realStockQty	
						}
					}
				, saveType  : 'I'
					, applyNo : $scope.spsPresent.ccsApply.applyNo
				});
			}
		}
		
//		promotionPresentService.checkIdDuplicatePresent($scope.search, function(response) {
//			for (var k = 0; k < response.length; k++) {
//				for (var i=0; i<$scope.popupData.length; i++ ) {
//					if (response[k] == $scope.popupData[i].productId) {
//					}
//				}
//			}
//		});
	}

	$scope.currentTab = "";
	this.addExcel = function(value){
		var param = "";
		if(value == "ex") {
			param = "product";
			$scope.currentTab = "excludeProduct";
		} else {
			if(value == "Pr"){
				param = "product";
			}else if(value == "Br"){
				param = "brand";
			}else if(value == "Ca"){
				param = "category";	
			}
			$scope.currentTab = "applyTarget" + value;
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
	$scope.presentDetailPop = function(field, row) {
		$scope.productId = row.pmsProduct.productId;
		$scope.isPromotion = true;
		$scope.openPop("/pms/present/popup/detail", "사은품상세");
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
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.delete", "common.label.alert.fail"
                          ,"sps.common.enter.apply.target", "sps.common.enter.apply.deal", "c.common.copy.reg", "common.label.confirm.save"
                          ,"sps.common.promotion.stop", "sps.common.promotion.run", "sps.common.invalid.delete"
                          ,"sps.common.promotion.run.complete", "sps.common.promotion.stop.complete"
                          ,"sps.coupon.use.stop" ,"sps.coupon.empty" ,"sps.coupon.disable.certi" ,"sps.coupon.available.certi"
                          ,"sps.coupon.enter.certi" ,"sps.coupon.play.duplicate.certi" ,"sps.coupon.enter.dc.amt" 
                          ,"sps.coupon.enter.min.amt" ,"sps.coupon.enter.dc.rate" ,"sps.coupon.enter.max.dc.amt" 
                          ,"sps.coupon.enter.term.days" ,"sps.coupon.promotion.stop" ,"sps.coupon.select.empty" 
                          ,"sps.coupon.issue.stop" ,"sps.coupon.issue.success" ,"sps.coupon.issue.max"
                          ,"sps.coupon.invaild.certi1" , "sps.coupon.invaild.certi2", "sps.coupon.enter.max.qty"
                          ,"sps.coupon.enter.person.qty", "sps.coupon.invalid.max.qty", "sps.coupon.select.business"
                          ,"sps.coupon.select.burden.rate"
                          ];

var couponApp = angular.module("couponApp", [	'commonServiceModule', 'gridServiceModule', ,'commonPopupServiceModule',
                                               	'spsServiceModule', 'ccsServiceModule', 
                                               	'ui.date'
                                               	]);
couponApp.controller("sps_couponListApp_controller", function($window, $scope, $filter, $compile
		, commonService, gridService, commonPopupService ) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	if(!common.isEmpty($("#pageId").val())){
		$scope.pageId = $("#pageId").val();
	}

	
	// PO여부
	$scope.isPo = global.session.businessId == "" ? false : true;
	
	// MD여부
	$scope.isMd = global.session.mdYn == "N" ? false : true;
	
	var cellTemplate = "<div class=\"ui-grid-cell-contents\" title=\"\"><button class=\"btn_type2\" ng-click=\"grid.appScope.copyReg(row.entity)\"><b>복사등록</b></button></div>";
	var	columnDefs = [];
	columnDefs.push({ field: 'couponId',		colKey:"c.sps.coupon.coupon.id",		width:'100', linkFunction:"linkFunction", type:"number"});      
	columnDefs.push({ field: 'name',			colKey:"spsCoupon.name",				width:'100', linkFunction:"linkFunction"});      
	columnDefs.push({ field: 'couponTypeName',	colKey:"c.sps.coupon.pop.coupon.type",	width:'100'		});      
	columnDefs.push({ field: 'chgDcValue',		colKey:"spsCoupon.dcValue",				width:'100'		});      
	columnDefs.push({ field: 'couponStateName',	colKey:"spsCoupon.couponStateCd",		width:'100'		});      
	columnDefs.push({ field: 'issueStartDt',	colKey:"spsCoupon.issueStartDt",		width:'100'		});      
	columnDefs.push({ field: 'issueEndDt',		colKey:"spsCoupon.issueEndDt",			width:'100'	});      
//	columnDefs.push({ field: 'couponCopyReg',	colKey:"c.sps.coupon.coupon.copy.reg", 	width:'100', linkFunction:"copyReg"});
	columnDefs.push({ field: 'couponCopyReg',	colKey:"c.sps.coupon.coupon.copy.reg", 	width:'100', cellTemplate :cellTemplate});
	
	if($scope.isPo) {
		// PO 는 자신회사 쿠폰만 볼 수 있음
		$scope.search.poBusinessId = global.session.businessId;
	} else {
		columnDefs.push({ field: 'businessInfo', colKey:"c.sps.coupon.businessId", width:'100'});
	}
	
	columnDefs.push({ field: 'insId', colKey:"c.grid.column.insId", width:'100', userFilter :'insId,insName'});      
	columnDefs.push({ field: 'insDt', colKey:"c.grid.column.insDt", width:'100'});      
	columnDefs.push({ field: 'updId', colKey:"c.grid.column.updId", width:'100', userFilter :'updId,updName'});      
	columnDefs.push({ field: 'updDt', colKey:"c.grid.column.updDt", width:'100'});      
	
	$scope.infoType = [
	                    {val : 'ID',text : '쿠폰번호'},
	                    {val : 'NAME',text : '쿠폰명'}
		              ];
	
	angular.element(document).ready(function () {

		commonService.init_search($scope,'search');
	});
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
//	var cellTemplateStrStop = '<div style=\'text-align:center; padding-top:4px\' ng-if="row.couponStateCd==\'COUPON_STATE_CD.STOP\'">'
//	              + '<font color="red" >{{COL_FIELD}}</font>'
//	              + '</div>'
//	              + '<div style=\'text-align:center; padding-top:4px\' ng-if="row.couponStateCd!=\'COUPON_STATE_CD.STOP\'">'
//	              + '<font>{{COL_FIELD}}</font>'
//	              + '</div>';
	
//	var cellTemplateCopy = "<div class=\"ui-grid-cell-contents\" title=\"\"><button class=\"btn_type2\" ng-click=\"grid.appScope.copyReg(row.entity)\"><b>"
//		+"복사등록</b></button></div>";
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "gridOptions",	//mandatory
			url :  '/api/sps/coupon',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
			}
	};
	
	//그리드 초기화
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	$scope.searchCoupon = function(){
		$scope.myGrid.loadGridData();
	};
	
	this.resetCoupon = function(){
		/* search Data 초기화 */
//		angular.element(".day_group").find('button').removeClass("on");
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	this.deleteCoupon = function() {
		var check = true;
		var rows = $scope.myGrid.getSelectedRows();
		for(var i=0; i<rows.length; i++) {
			if(rows[i].couponStateCd != "COUPON_STATE_CD.READY") {
				check = false;
			} 
		}
		if(check) {
			$scope.myGrid.deleteGridData();
		} else {
			alert($scope.MESSAGES["sps.common.invalid.delete"]);
		}
	}
	
	$scope.savePopupOpen = function(){
		$scope.popOpen(null, null);
	}
	
	$scope.copyReg = function(row) {
//		$scope.couponId = row.couponId;
		$scope.couponCopy = true;
		
		$scope.popOpen(row.couponId, $scope.couponCopy);
	}
	
	$scope.linkFunction = function(field, row) {
//		$scope.couponId = row.couponId;
		$scope.popOpen(row.couponId, null);
	}
	
	$scope.popOpen = function(id, copy) {
		$scope.couponId = "";
		var winName='';
		
		// 복사등록
		if(copy != null) {
			$scope.couponId = id;
			winName='Coupon Copy';
		} else {
			
			// 상세&수정
			if(id != null && id != '') {
				$scope.couponId = id;
				winName='Coupon Modify';
			}
			// 신규등록
			else {
				// 제휴업체 쿠폰 등록
				if($scope.isPo) {
					
				}
				// 자사등록
				else {
					// MD 등록
					if($scope.isMd) {
						winName='Coupon MD Create';
					}
					// 그 외 등록
					else {
						winName='Coupon MARKETING Create';
					}
				}
			}
		}
		
		var url = '';
		if($scope.isPo) {
			var url= Rest.context.path + '/sps/coupon/popup/poDetail';
		}else{
			var url= Rest.context.path + '/sps/coupon/popup/detail';
		}
		
		popupwindow(url,winName,1200,600);
	}
	
	// 제휴업체 검색 팝업
	this.businessPop = function() {
		commonPopupService.businessPopup($scope,"callback_business",false);
		$scope.callback_business = function(data){
			$scope.search.businessId = data[0].businessId;
			$scope.$apply();		
		}
	}
	
	// 지우기 버튼
	this.businessEraser = function() {
		$scope.search.businessId = "";
	}
	
}).controller("sps_couponDetailPopApp_controller", function($window, $scope, $filter, $compile
		, couponService, applyService, displayTreeService, commonService, gridService,  commonPopupService) {
	/* ****************** detail POPUP Controller *************************/
	// 팝업에서 부모 scope 접근하기 위함.		
	pScope = $window.opener.$scope;
	$window.$scope = $scope;
	

	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}

	
//	$scope.duplicateCheckYn = "N";
	$scope.spsCoupon = {};
	$scope.spsCoupon.ccsApply = {};
	
	$scope.changeFlag = true;
	
	$scope.targetTypeCds = [
	            {val : 'TARGET_TYPE_CD.ALL',text : '전체'},
	            {val : 'TARGET_TYPE_CD.PRODUCT',text : '상품'},
	            {val : 'TARGET_TYPE_CD.CATEGORY',text : '전시카테고리'},
	            {val : 'TARGET_TYPE_CD.BRAND',text : '브랜드'}
	           ];
	
	if(pScope.isMd) {
		$scope.couponDivCd = "MD";
	} else {
		$scope.couponDivCd = "MARKETING";
	}
	
	$scope.isPo = pScope.isPo;
	if ($scope.isPo) {
		$scope.spsCoupon.businessId = pScope.search.poBusinessId;
		$scope.spsCoupon.businessBurdenRate = 100;
	}
//	else {
//		$scope.spsCoupon.businessBurdenRate = "";
//	}
	
	// 딜전문관 코드배열 저장
	$scope.dealTypeCd = [];
//	$scope.tmpCd = ["DEAL_TYPE_CD.PREMIUM","DEAL_TYPE_CD.MEMBER","DEAL_TYPE_CD.SHOCKDEAL","DEAL_TYPE_CD.EMPLOYEE","DEAL_TYPE_CD.CHILDREN"];
	$scope.tmpCd = ["DEAL_TYPE_CD.PREMIUM","DEAL_TYPE_CD.MEMBER","DEAL_TYPE_CD.SHOCKDEAL","DEAL_TYPE_CD.EMPLOYEE"];
	
	// tab 이동
	this.moveTab = function() {
		if($scope.spsCoupon.couponStateCd != "COUPON_STATE_CD.READY") {
			pScope.couponId = $scope.spsCoupon.couponId;
			pScope.publicCin = $scope.spsCoupon.publicCin;
			pScope.maxCouponIssue = $scope.spsCoupon.maxIssueQty;
			pScope.totalCnt = $scope.totalCnt;
			
			$window.location.href = Rest.context.path +"/sps/coupon/popup/issued";
		} else {
			alert(pScope.MESSAGES["sps.coupon.empty"]);
			angular.element("#setTab1").addClass("on");
			angular.element("#issuedTab2").removeClass("on");
		}
	}
	
	$scope.searchCoupon = function(couponId) {

		$scope.spsCoupon.couponId = couponId;
		
		// 총 쿠폰 발급 매수, 총 사용수
		couponService.getCouponCount($scope.spsCoupon, function(response){
			var valArr = response.content.split(",");
			// 0 전체 1 미등록 2 등록 3 사용 4 중지
			$scope.totalCnt = valArr[0];
			$scope.issueCnt = valArr[1];
			$scope.regCnt = valArr[2];
			$scope.useCnt = valArr[3];
			$scope.stopCnt = valArr[4];
			
			// row Data Load
			couponService.getCouponDetail($scope.spsCoupon,function(response){
				$scope.spsCoupon = response;
				$scope.spsCoupon.termDays = $scope.spsCoupon.termDays+'';
				// 적용대상 기존 정보 저장
				$scope.spsCoupon.orgTargetTypeCd = response.targetTypeCd;

				// 쿠폰할인 값 정률/적액 적용
				if(response.dcApplyTypeCd == "DC_APPLY_TYPE_CD.AMT") {
					$scope.spsCoupon.dcValueAMT = response.dcValue;
				} else {
					$scope.spsCoupon.dcValueRATE = response.dcValue;
				}
				
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
//								case 'DEAL_TYPE_CD.CHILDREN' : $scope.state6 = true; break;
							}
						}
					}
					
					// 코드 String 저장
					var resultCd = ""
						for(var i=0; i<$scope.dealTypeCd.length; i++) {
							resultCd += "'" + $scope.dealTypeCd[i] + "',";
						}
					$scope.spsCoupon.dealTypeCds = resultCd.slice(0,-1);
				}
				
				// 인증번호 사용여부
				if($scope.spsCoupon.couponIssueTypeCd == "COUPON_ISSUE_TYPE_CD.SYSTEM") {
					$scope.spsCoupon.publicCin = 'N'
				} else {
					$scope.spsCoupon.publicCin = 'Y'
//					$scope.duplicateCheckYn = "Y";		// 중복체크여부
					// 유형 복수 - 인증코드 제거
					if($scope.spsCoupon.couponIssueTypeCd == "COUPON_ISSUE_TYPE_CD.PRIVATE") {
						$scope.spsCoupon.privateCin = "";
					}
				}
				
				// 쿠폰이 생성 후 진행, 변경은 'name'과 '발급 기간', '최대발급매수' 만 가능하다
				if($scope.spsCoupon.couponStateCd == 'COUPON_STATE_CD.RUN' && angular.isUndefined(pScope.couponCopy)) {
					$scope.disableFlag = true;	// 진행중 허용설정 변경 불가
					$scope.changeFlag = false;
					
					angular.element('[name="form"]').find('input, button').attr('readonly', true);
					angular.element('[name="form"]').find('input, button').attr('disabled', true);
					
					angular.element('[name="targetSave"]').attr('disabled', true);
					angular.element('[name="targetSave"]').attr('readonly', true);
					
					angular.element('[name="couponName"]').attr('disabled', false);
					angular.element('[name="couponName"]').attr('readonly', false);
					
//					angular.element('[name="couponState"]').find('input').attr('disabled', false);
//					angular.element('[name="couponState"]').find('input').attr('readonly', false);

					angular.element('[ng-model="spsCoupon.maxIssueQty"]').attr('disabled', false);
					angular.element('[ng-model="spsCoupon.maxIssueQty"]').attr('readonly', false);
					
					// 수정 불가 - 사용자 일때
					if(response.controlNo != null) {
						angular.element('[name="controlButton"]').attr('disabled', false);
						angular.element('[name="controlButton"]').attr('readonly', false);
					}
					
					angular.element('[ng-model="spsCoupon.issueStartDt"]').find('input').attr('disabled', false);
					angular.element('[ng-model="spsCoupon.issueStartDt"]').find('input').attr('readonly', false);
					angular.element('[ng-model="spsCoupon.issueStartDt"]').find('button').attr('disabled', false);
					angular.element('[ng-model="spsCoupon.issueStartDt"]').find('button').attr('readonly', false);
					
					angular.element('[ng-model="spsCoupon.issueEndDt"]').find('input').attr('disabled', false);
					angular.element('[ng-model="spsCoupon.issueEndDt"]').find('input').attr('readonly', false);
					angular.element('[ng-model="spsCoupon.issueEndDt"]').find('button').attr('disabled', false);
					angular.element('[ng-model="spsCoupon.issueEndDt"]').find('button').attr('readonly', false);
					
				} else if($scope.spsCoupon.couponStateCd == 'COUPON_STATE_CD.STOP' && angular.isUndefined(pScope.couponCopy)) {
					$scope.changeFlag = false;
					angular.element('[name="form"]').find('input, button').attr('readonly', true);
					angular.element('[name="form"]').find('input, button').attr('disabled', true);
					angular.element('[name="gridForm"]').find('input, button').attr('readonly', true);
					angular.element('[name="gridForm"]').find('input, button').attr('disabled', true);
					
				} 

				// 복사등록 여부
				if(angular.isDefined(pScope.couponCopy)) {
					$scope.disableFlag = false;	// 복사 등록의 경우 허용설정 체크
					
					if($scope.spsCoupon.couponStateCd != 'COUPON_STATE_CD.READY') {
						$scope.spsCoupon.couponStateCd = 'COUPON_STATE_CD.READY';
					}
					
					$scope.spsCoupon.couponCopy = pScope.couponCopy;
					if($scope.spsCoupon.couponCopy) {
						$scope.totalCnt = 0;
						$scope.useCnt = 0;
						
						delete $scope.spsCoupon.couponId;
						//복사등록 쿠폰명 설정
						//$scope.spsCoupon.name = "";
						$scope.spsCoupon.privateCin = "";
						$scope.changeFlag = true;
					}
					// copy flag initial
					delete pScope.couponCopy;
				}

				
				// 그리드 데이터 조회
				var value = response.targetTypeCd;
				var gridNameTarget = "";
				var gridNameEx = "";
				
				// 그리드 초기화
//				openTarget(value);
				
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
				
				if(angular.isDefined($scope.spsCoupon.ccsApply.ccsApplytargets)
						&& $scope.spsCoupon.ccsApply.ccsApplytargets != null
						&& $scope.spsCoupon.ccsApply.ccsApplytargets.length > 0){
					var target = $scope.spsCoupon.ccsApply.ccsApplytargets;
					$scope[gridNameTarget].data = target; 					
				}
				
				if(angular.isDefined($scope.spsCoupon.ccsApply.ccsExcproducts)
						&& $scope.spsCoupon.ccsApply.ccsExcproducts != null
						&& $scope.spsCoupon.ccsApply.ccsExcproducts.length > 0){
					
					var ex = $scope.spsCoupon.ccsApply.ccsExcproducts;
					$scope[gridNameEx].data = ex;
				}
				
				// 기존 사용대상 정보 삭제
				$scope.spsCoupon.ccsApply.ccsApplytargets = null;
				$scope.spsCoupon.ccsApply.ccsExcproducts = null;
				
				common.safeApply($scope);
			});
		});
	}
	
	// insert / detail 분기
	angular.element(document).ready(function () {
		if(angular.isDefined(pScope.couponId) 
				&& pScope.couponId != ""){
			$scope.searchCoupon(pScope.couponId);
		} else {
			$scope.spsCoupon.maxIssueQty = 999;
			
			$scope.totalCnt = 0;
			$scope.issueCnt = 0;
			$scope.regCnt = 0;
			$scope.useCnt = 0;
			$scope.stopCnt = 0;
			
			$scope.spsCoupon.maxDcAmt = "";
			$scope.spsCoupon.minOrderAmt = "";
			
			//openTarget("TARGET_TYPE_CD.ALL");
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
	var excludeProductColumn = [
	                            { field: 'pmsProduct.productId'			, colKey: "c.sps.coupon.product.id"		,linkFunction:"productDetailPop", type:"number"},
	                            { field: 'pmsProduct.name'				, colKey: "c.sps.coupon.product.name"	,linkFunction:"productDetailPop"},
	                            { field: 'pmsProduct.productTypeCd'	, colKey: "c.sps.coupon.product.type"	,dropdownCodeEditor : "PRODUCT_TYPE_CD", cellFilter : "productTypeFilter"},
	                            { field: 'pmsProduct.saleStateCd'		, colKey: "c.sps.coupon.sale.state"		,dropdownCodeEditor : "SALE_STATE_CD.SALE", cellFilter : "saleStateFilter"},
	                            { field: 'pmsProduct.salePrice'			, colKey: "pmsProduct.salePrice"			},
	                            { field: 'pmsProduct.pmsBrand.name'		, colKey: "c.sps.coupon.brand"			},
	                            { field: 'pmsProduct.displayYn'			, colKey: "c.pms.product.display.yn"			},
	                            { field: 'pmsProduct.saleStartDt'		, colKey: "c.sps.common.pms.saleStartDt"			},
	                            { field: 'pmsProduct.saleEndDt'			, colKey: "c.sps.common.pms.saleEndDt"			}
//	                            ,
//	                            { field: 'insId'						, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//	                            { field: 'insDt'						, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//	                            { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//	                            { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
	                            ];
	var applyProduct = [
	                    { field: 'pmsProduct.productId'			, colKey: "c.sps.coupon.product.id"		,linkFunction:"productDetailPop", type:"number"},
	                    { field: 'pmsProduct.name'				, colKey: "c.sps.coupon.product.name"	,linkFunction:"productDetailPop"},
	                    { field: 'pmsProduct.productTypeCd'	, colKey: "c.sps.coupon.product.type"	,dropdownCodeEditor : "PRODUCT_TYPE_CD", cellFilter : "productTypeFilter"},
	                    { field: 'pmsProduct.saleStateCd'		, colKey: "c.sps.coupon.sale.state"		,dropdownCodeEditor : "SALE_STATE_CD.SALE", cellFilter : "saleStateFilter"},
	                    { field: 'pmsProduct.salePrice'			, colKey: "pmsProduct.salePrice"			},
	                    { field: 'pmsProduct.pmsBrand.name'		, colKey: "c.sps.coupon.brand"			},
	                    { field: 'pmsProduct.displayYn'			, colKey: "c.pms.product.display.yn", cellFilter : "brandDisplayFilter"			},
                        { field: 'pmsProduct.saleStartDt'		, colKey: "c.sps.common.pms.saleStartDt"			},
                        { field: 'pmsProduct.saleEndDt'			, colKey: "c.sps.common.pms.saleEndDt"			}
//                        ,
//	                    { field: 'insId'						, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//	                    { field: 'insDt'						, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//	                    { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//	                    { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
	                    ];
	var applyBrandColumn = [
	                        { field: 'pmsBrand.brandId'		, width:'150', colKey: "pmsProduct.brandId"	,linkFunction:"brandDetailPop", type:"number"},
	                        { field: 'pmsBrand.name'		, width:'150', colKey: "pmsBrand.name"		,linkFunction:"brandDetailPop"}
//	                        ,
//	                        { field: 'insId'				, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//	                        { field: 'insDt'				, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//	                        { field: 'updId'				, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//	                        { field: 'updDt'				, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
	                        ];
	var applyCategoryColumn = [
	                           { field: 'dmsDisplaycategory.displayCategoryId', 	width:'150', colKey: "dmsDisplaycategory.displayCategoryId" ,linkFunction:"categoryDetailPop", type:"number"},
	                           { field: 'dmsDisplaycategory.name',					width:'150', colKey: "dmsDisplaycategory.name" ,linkFunction:"categoryDetailPop"}
//	                           ,
//		                        { field: 'insId'				, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
//		                        { field: 'insDt'				, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
//	                           { field: 'updId'					,	colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
//	                           { field: 'updDt'					,	colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd HH:mm:ss\'" }
	                           ];
	
	/* 그리드 컬럼주입 */
	// 제외상품
	$scope.excludeProductGrid = new gridService.NgGrid(
			gridParam('excludeProduct' , 'spsCoupon' , excludeProductColumn)
	);
	
	// 브랜드
	$scope.applyTargetBrGrid = new gridService.NgGrid(
			gridParam('applyTargetBr', 'spsCoupon', applyBrandColumn)
	);
	// 카테고리
	$scope.applyTargetCaGrid = new gridService.NgGrid(
			gridParam('applyTargetCa', 'spsCoupon', applyCategoryColumn)
	);
	// 상품
	$scope.applyTargetPrGrid = new gridService.NgGrid(
			gridParam('applyTargetPr', 'spsCoupon', applyProduct)
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
		if (value == "TARGET_TYPE_CD.ALL") {
			$scope.div_ex = true;
			
			$scope.applyTargetPr.data = [];
			$scope.applyTargetBr.data = [];
			$scope.applyTargetCa.data = [];
		} else if (value == "TARGET_TYPE_CD.PRODUCT") {
			$scope.div_pr = true;

			$scope.excludeProduct.data = [];
			$scope.applyTargetBr.data = [];
			$scope.applyTargetCa.data = [];
		} else if (value == "TARGET_TYPE_CD.CATEGORY") {
			$scope.div_ca = true;
			$scope.div_ex = true;

			$scope.applyTargetPr.data = [];
			$scope.applyTargetBr.data = [];
		} else if (value == "TARGET_TYPE_CD.BRAND") {
			$scope.div_br = true;
			$scope.div_ex = true;

			$scope.applyTargetPr.data = [];
			$scope.applyTargetCa.data = [];
		}
	}
	
	$scope.$watch('spsCoupon.ccsApply.targetTypeCd',function(newVal, oldVal){
//		$scope.originTargetTypeCd = oldVal;
//		openTarget(targetTypeCd);
		openTarget(newVal);
	}, true);
	
//	this.couponTargetTypeCd = function() {
//		var targetTypeCd = $scope.spsCoupon.ccsApply.targetTypeCd;
//		if($scope.originTargetTypeCd != targetTypeCd) {
//			openTarget(targetTypeCd);
//		}
//	}

	// 할인 금액 초기화
	$scope.valueInital = function() {
		$scope.spsCoupon.dcValueAMT = "";
		$scope.spsCoupon.dcValueRATE = "";
		$scope.spsCoupon.maxDcAmt = "";
		$scope.spsCoupon.minOrderAmt = "";		
	}
	
	$scope.$watch('spsCoupon.couponTypeCd',function(newVal, oldVal){
		$scope.originCouponType = oldVal;
	}, true);
	this.couponRestrictType = function() {
		var couponType = $scope.spsCoupon.couponTypeCd;
		
		if($scope.originCouponType != couponType) {
			$scope.valueInital();
			
			if(couponType != "COUPON_TYPE_CD.PRODUCT") {
				if(couponType =="COUPON_TYPE_CD.DELIVERY") {
					$scope.spsCoupon.dcValue = 0;
				}
				
				// 배송 & 포장 & 주문 쿠폰은 사용대상 전체
				if(couponType != "COUPON_TYPE_CD.PLUS") {
					$scope.spsCoupon.ccsApply.targetTypeCd = "TARGET_TYPE_CD.ALL";
					$scope.spsCoupon.singleApplyYn = "N";
				}
				
				if($scope.spsCoupon.downShowYn == 'Y') {
					$scope.spsCoupon.downShowYn = "N";
					this.couponRestrictDownShow();
				}
				
				$scope.spsCoupon.affiliateYn = "N";
			} else {
				$scope.spsCoupon.ccsApply.targetTypeCd = "TARGET_TYPE_CD.PRODUCT";
			}
			common.safeApply($scope);
		}
	}
	
	$scope.$watch('spsCoupon.downShowYn',function(newVal, oldVal){
		$scope.originDownShowYn = oldVal;
	}, true);
	this.couponRestrictDownShow = function() {
		var template = "";
		if($scope.originDownShowYn != $scope.spsCoupon.downShowYn) {
			if(!confirm("허용설정이 초기화 됩니다. 진행하시겠습니까?")) {
				$scope.spsCoupon.downShowYn = $scope.originDownShowYn;
				return;
			}
			
			if($scope.spsCoupon.downShowYn == "Y"){
				template = $compile('<control-set model-name="spsCoupon" lebels="전체,사용자설정" except="mgrade,mtype,channel"></control-set>')($scope);
			} else {
				template = $compile('<control-set model-name="spsCoupon" lebels="전체,사용자설정" ></control-set>')($scope);
			}
			angular.element('#controlArea').html(template);
			
			// 쿠폰유형 변경 X , 상세다운로드 노출 변경 O
			if($scope.spsCoupon.couponTypeCd == "COUPON_TYPE_CD.PRODUCT" && $scope.spsCoupon.downShowYn == "Y") {
				$scope.spsCoupon.ccsApply.targetTypeCd = "TARGET_TYPE_CD.PRODUCT";
			} else {
				$scope.spsCoupon.ccsApply.targetTypeCd = "TARGET_TYPE_CD.ALL";
			}
			$scope.spsCoupon.isAllPermit = "Y";
			common.safeApply($scope);
		}		
		
	}
	
	
	// 발급 일자 초기화
	$scope.$watch('spsCoupon.termTypeCd',function(value){
		$scope.spsCoupon.termDays = "";
	});
	
	// 쿠폰 진행
	this.couponRun = function(){
		$scope.spsCoupon.couponStateCd = "COUPON_STATE_CD.RUN";
		this.updateCouponState("run");
	};
	
	// 쿠폰 정지
	this.couponStop = function(){
		$scope.spsCoupon.couponStateCd = "COUPON_STATE_CD.STOP";
		this.updateCouponState("stop");
	};	
	
	this.updateCouponState = function(gb) {
		
		if(!$scope.paramCheck()) return false;
		
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
			couponService.updateCouponState($scope.spsCoupon, function(response) {
				if(response.content != null) {
					alert("'"+afterState+"상태'(으)로 변경되었습니다.");
					
		        	pScope.couponId = response.content;
		        	pScope.searchCoupon();
		    		$window.location.reload();
				} else {
					alert(pScope.MESSAGES["common.label.alert.fail"]);
				}
			});
		} else {
			if(gb == "run") {
				$scope.spsCoupon.couponStateCd = "COUPON_STATE_CD.READY";
				return true;
			} else if(gb == "stop") {
				$scope.spsCoupon.couponStateCd = "COUPON_STATE_CD.RUN";
				return true;
			}
		}
		common.safeApply($scope);
	}
	
	// 쿠폰 저장
	this.updateCouponPromotion = function(gb) {
		if(!$scope.paramCheck()) return false;
		
		if($scope.spsCoupon.isAllPermit == 'N') {
			if($scope.spsCoupon.couponTypeCd == 'COUPON_TYPE_CD.PRODUCT' 
				&& $scope.spsCoupon.downShowYn == 'Y') {
				if(angular.isDefined($scope.spsCoupon.ccsControl)) {
					$scope.spsCoupon.ccsControl.memberTypeArr = [];
					$scope.spsCoupon.ccsControl.memGradeArr = [];
					$scope.spsCoupon.ccsControl.channelIdArr = [];
					$scope.spsCoupon.ccsControl.channelControlCd = 'CHANNEL_CONTROL_CD.ALL';
				}
			}
		}
		
		if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			couponService.updateCoupon($scope.spsCoupon, function(response) {
				if(response.content != null) {
					alert(pScope.MESSAGES["common.label.alert.save"]);
					
		        	pScope.couponId = response.content;
		        	pScope.searchCoupon();
		    		$window.location.reload();
				} else {
					alert(pScope.MESSAGES["common.label.alert.fail"]);
				}
			});
		}
		
		common.safeApply($scope);
	}

	// 제휴업체 검색 팝업
	this.businessPop = function() {
		commonPopupService.businessPopup($scope,"callback_business",false);
		$scope.callback_business = function(data) {
			$scope.spsCoupon.businessId = data[0].businessId;
			$scope.$apply();		
		}
	}
	// 지우기 버튼
	this.businessEraser = function() {
		$scope.spsCoupon.businessId = "";
	}
	
//	// 인증코드중복여부 버튼
//	$scope.duplicateCheck = function() {
//		// 1. 인증번호사용 Y 
//		// 2. A. 인증번호유형 단일_PUBLIC --> 하나의 인증번호를 복수의 회원이 사용함
//		// 2. B. 인증번호유형 복수_PRIVATE --> 쿠폰을 발급받는 회원의 수와 인증번호의 수가 같음 
//		if(angular.isDefined($scope.spsCoupon.privateCin) && $scope.spsCoupon.privateCin != "") {
//			var privateCin = ($scope.spsCoupon.privateCin).toUpperCase();
//			
//			if(privateCin.replace(/[A-Z|0-9]/g, '').length != 0) {
//				alert(pScope.MESSAGES["sps.coupon.invaild.certi2"]);
//				angular.element('#privateCin').val("");
//				angular.element('#privateCin').focus();
//				return false;
//			}
//			else if(privateCin.length != 14) {
//				alert(pScope.MESSAGES["sps.coupon.invaild.certi1"]);
//				angular.element('#privateCin').val("");
//				angular.element('#privateCin').focus();
//				return false;
//			} else {
//				var checkRtnVal = "";
//				couponService.duplicateCheck($scope.spsCoupon,function(res) {
//					checkRtnVal = res.content;
//					
//					// rturun 값이 USED이면 사용중이기때문에 사용불가이고
//					// NOT이면 사용가능하다
//					if (checkRtnVal == 'USED') {
//						alert(pScope.MESSAGES["sps.coupon.disable.certi"]);
//						angular.element('#privateCin').focus();
//						return false;
//					} else {
//						$scope.duplicateCheckYn = "Y";
//						alert(pScope.MESSAGES["sps.coupon.available.certi"]);
//						$scope.spsCoupon.privateCin = privateCin;
//						//다음 input tag에 focussing
//						angular.element('#maxIssueQty').focus();
//					}
//				});
//			}
//		} else {
//			alert(pScope.MESSAGES["sps.coupon.enter.certi"]);
//		}
//	}
	$scope.createPrivateCin = function() {
		// 쿠폰인증번호 초기화
		if(angular.isDefined($scope.spsCoupon.privateCin)) {
			$scope.spsCoupon.privateCin = '';
		}
		couponService.createPrivateCin(null, function(res) {
			$scope.spsCoupon.privateCin = res.content;
			common.safeApply($scope);
		});
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
		$scope.spsCoupon.dealTypeCds = resultCd.slice(0,-1);
	}

	// 파라메터 검증
	$scope.paramCheck = function() {
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return false;
		}
		
		if(!$scope.spsCoupon.issueStartDt || !$scope.spsCoupon.issueEndDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		
		if( $scope.spsCoupon.couponTypeCd == 'COUPON_TYPE_CD.PLUS') {
			$scope.spsCoupon.discountDupYn = "Y"; 		//TODO 할인중복여부는 plus 쿠폰일때만 허용되나?
		} else {
			$scope.spsCoupon.discountDupYn = "N";
		}
		
//		// 인증번호 중복여부
//		var publicCinYn = $scope.spsCoupon.publicCin;
//		if (publicCinYn == "Y") {
//			if ($scope.spsCoupon.couponIssueTypeCd == "COUPON_ISSUE_TYPE_CD.PUBLIC") {
//				if ($scope.duplicateCheckYn == "N") {
//					alert(pScope.MESSAGES["sps.coupon.play.duplicate.certi"]);
//					angular.element('#privateCin').focus();
//					return false;
//				};
//			}
//			
//			// 쿠폰상태 진행 
//			if($scope.spsCoupon.couponStateCd == 'COUPON_STATE_CD.RUN') {
//				// 수정proccess && 발급매수 감소
//				var totalCnt = Number($scope.totalCnt);
//				var maxIssueQty = Number($scope.spsCoupon.maxIssueQty);
//				if(totalCnt > 0 && totalCnt > maxIssueQty) {
//					var minus = totalCnt - maxIssueQty;
//					// 수정가능한 미등록 쿠폰 수보다 발급매수 감소량이 더 큼
//					if(Number($scope.issueCnt) < minus) {
//						alert(pScope.MESSAGES["sps.coupon.invalid.max.qty"]);
//						return false;
//					}
//				}
//			}
//		}
//		// 인증번호 미사용시
//		else {
//			$scope.spsCoupon.couponIssueTypeCd = "COUPON_ISSUE_TYPE_CD.SYSTEM";
//		}
		
		// 인증번호 중복여부
		var publicCinYn = $scope.spsCoupon.publicCin;
		if (publicCinYn == "Y") {
			if($scope.spsCoupon.couponIssueTypeCd == "COUPON_ISSUE_TYPE_CD.PUBLIC") {
				if(angular.isUndefined($scope.spsCoupon.privateCin) || $scope.spsCoupon.privateCin == '') {
					alert("인증번호를 생성해주세요.");
					angular.element('#privateCin').focus();
					return false;
				}
			} else {
				$scope.spsCoupon.privateCin = "";
			}
			
			// 쿠폰상태 진행 
			if($scope.spsCoupon.couponStateCd == 'COUPON_STATE_CD.RUN') {
				// 수정proccess && 발급매수 감소
				var totalCnt = Number($scope.totalCnt);
				var maxIssueQty = Number($scope.spsCoupon.maxIssueQty);
				if(totalCnt > 0 && totalCnt > maxIssueQty) {
					var minus = totalCnt - maxIssueQty;
					// 수정가능한 미등록 쿠폰 수보다 발급매수 감소량이 더 큼
					if(Number($scope.issueCnt) < minus) {
						alert(pScope.MESSAGES["sps.coupon.invalid.max.qty"]);
						return false;
					}
				}
			}
		}
		// 인증번호 미사용시
		else {
			$scope.spsCoupon.couponIssueTypeCd = "COUPON_ISSUE_TYPE_CD.SYSTEM";
			$scope.spsCoupon.privateCin = '';
		}
		
		// 비용부담 율
		if(angular.isDefined($scope.spsCoupon.businessBurdenRate)) {
			var rate = Number($scope.spsCoupon.businessBurdenRate);
			if(rate > 100) {
				alert("비율은 0 ~ 100% 사이 입니다.");
				return false;
			}
		}
		
		// BO 일때 한정
		if(!$scope.isPo) {
			
			// 제휴쿠폰
			if($scope.spsCoupon.affiliateYn == "N") {
				$scope.spsCoupon.businessId = "";
			} else if($scope.spsCoupon.affiliateYn == "Y") {
				if(angular.isUndefined($scope.spsCoupon.businessId) || $scope.spsCoupon.businessId =="") {
					alert(pScope.MESSAGES["sps.coupon.select.business"]);
					return false;
				} 
//				else if(angular.isUndefined($scope.spsCoupon.businessBurdenRate) || $scope.spsCoupon.businessBurdenRate == "" ) {
//					alert(pScope.MESSAGES["sps.coupon.select.burden.rate"]);
//					return false;
//				}
			}
			
			// 딜 적용 가능 여부
			if ($scope.spsCoupon.dealApplyYn == "Y") {
				if(angular.isUndefined($scope.spsCoupon.dealTypeCds) || $scope.spsCoupon.dealTypeCds == "" ) {
					alert(pScope.MESSAGES["sps.common.enter.apply.deal"]);
					angular.element('#dealChk').focus();
					return false;
				}
			} else {
				if(angular.isDefined($scope.spsCoupon.dealTypeCds)) {
					$scope.spsCoupon.dealTypeCds = "";
				}
			}
			
			// 수수료 10% : default 'N'
			if($scope.spsCoupon.couponTypeCd != 'COUPON_TYPE_CD.PRODUCT' 
				&& $scope.spsCoupon.couponTypeCd != 'COUPON_TYPE_CD.PLUS') {
				$scope.spsCoupon.feeLimitApplyYn = 'N'
			}
		}
		
		// TODO 배송/포장쿠폰 정책
		if($scope.spsCoupon.couponTypeCd == "COUPON_TYPE_CD.DELIVERY" || $scope.spsCoupon.couponTypeCd =="COUPON_TYPE_CD.WRAP") {
			// 배송 / 포장은 무료쿠폰
			// 배송은 한 주문에 하나의 배송지에만 무료로 적용됨
			// 포장은 한 주문 안에 하나의 상품에 적용됨
			if($scope.spsCoupon.couponTypeCd =="COUPON_TYPE_CD.WRAP") {
				$scope.spsCoupon.dcValue = 2000;
			} else {
				$scope.spsCoupon.dcValue = 0;
			}
			
			$scope.spsCoupon.dcApplyTypeCd = "DC_APPLY_TYPE_CD.AMT";
		} else {

			// 할인구분
			// 정액
			if ($scope.spsCoupon.dcApplyTypeCd == "DC_APPLY_TYPE_CD.AMT") {
				if(angular.isUndefined($scope.spsCoupon.dcValueAMT) || $scope.spsCoupon.dcValueAMT == "" || Number($scope.spsCoupon.dcValueAMT) < 1) {
					alert(pScope.MESSAGES["sps.coupon.enter.dc.amt"]);
					angular.element('#inAmt').focus();
					return false;				
				}
				if(!$scope.isPo) {
					// 장바구니쿠폰(주문할인쿠폰) 외에는 최소 구매금액 제한 없음
					if($scope.spsCoupon.couponTypeCd == "COUPON_TYPE_CD.ORDER") {
						if(angular.isUndefined($scope.spsCoupon.minOrderAmt) || $scope.spsCoupon.minOrderAmt == "" || Number($scope.spsCoupon.minOrderAmt) < 1) {
							alert(pScope.MESSAGES["sps.coupon.enter.min.amt"]);
							angular.element('#minAmt').focus();
							return false;
						}
					} 
				}
				$scope.spsCoupon.dcValue = $scope.spsCoupon.dcValueAMT;
			}
			// 정률 
			else {
				if(angular.isUndefined($scope.spsCoupon.dcValueRATE) || $scope.spsCoupon.dcValueRATE == "" || Number($scope.spsCoupon.dcValueRATE) < 1) {
					alert(pScope.MESSAGES["sps.coupon.enter.dc.rate"]);
					angular.element('#inRate').focus();
					return false;
				}
				
				if(!$scope.isPo) {
					// 장바구니쿠폰(주문할인쿠폰) 외에는 최소 구매금액 제한 없음
					if($scope.spsCoupon.couponTypeCd == "COUPON_TYPE_CD.ORDER") {
						if(angular.isUndefined($scope.spsCoupon.minOrderAmt) || $scope.spsCoupon.minOrderAmt == "" || Number($scope.spsCoupon.minOrderAmt) < 1) {
							alert(pScope.MESSAGES["sps.coupon.enter.min.amt"]);
							angular.element('#minAmt').focus();
							return false;
						}
					}
					
					if($scope.spsCoupon.couponTypeCd != "COUPON_TYPE_CD.PRODUCT" && $scope.spsCoupon.downShowYn != "Y") {
						if(angular.isUndefined($scope.spsCoupon.maxDcAmt) || $scope.spsCoupon.maxDcAmt == "" || Number($scope.spsCoupon.maxDcAmt) < 1) {
							alert(pScope.MESSAGES["sps.coupon.enter.max.dc.amt"]);
							angular.element('#maxDcAmt').focus();
							return false;
						}
					}	
				}
				$scope.spsCoupon.dcValue = $scope.spsCoupon.dcValueRATE;
			}
		}
		
		// 최대 발급 매수
		if(angular.isUndefined($scope.spsCoupon.maxIssueQty) || $scope.spsCoupon.maxIssueQty == "" || Number($scope.spsCoupon.maxIssueQty) < 1) {
			alert(pScope.MESSAGES["sps.coupon.enter.max.qty"]);
			angular.element('#maxIssueQty').focus();
			return false;
		}
		
		// 복수 발급 매수
		if(angular.isUndefined($scope.spsCoupon.maxMemIssueQty) || $scope.spsCoupon.maxMemIssueQty == "" || Number($scope.spsCoupon.maxMemIssueQty) < 1) {
			alert(pScope.MESSAGES["sps.coupon.enter.person.qty"]);
			angular.element('#maxMemIssueQty').focus();
			return false;
		}
		
		// ID 당 발급량
		if(Number($scope.spsCoupon.maxIssueQty) < Number($scope.spsCoupon.maxMemIssueQty)) {
			alert("ID당 발급 수량이 최대 발급 수량보다 큽니다.");
			angular.element('#maxMemIssueQty').focus();
			return false;
		}
		
		// 유효기간
		if($scope.spsCoupon.termTypeCd == "TERM_TYPE_CD.LASTDAY" || $scope.spsCoupon.termTypeCd == "TERM_TYPE_CD.WEEK" || $scope.spsCoupon.termTypeCd == "TERM_TYPE_CD.TODAY" ) {
			// 시작일 : 등록일
			// 종료일 : 등록월 마지막 일
			$scope.spsCoupon.termStartDt = "";
			$scope.spsCoupon.termEndDt = "";
			$scope.spsCoupon.termDays = "";
		} else {
			if ($scope.spsCoupon.termTypeCd == "TERM_TYPE_CD.DAYS") {
				if(common.isEmpty($scope.spsCoupon.termDays)) {
					alert(pScope.MESSAGES["sps.coupon.enter.term.days"]);
					angular.element('#termDays').focus();
					return false;
				}
				
				$scope.spsCoupon.termStartDt = "";
				$scope.spsCoupon.termEndDt = "";
			} else if($scope.spsCoupon.termTypeCd == "TERM_TYPE_CD.TERM") {
				
				if(common.isEmpty($scope.spsCoupon.termStartDt) || common.isEmpty($scope.spsCoupon.termEndDt)) {
					alert("유효기간의 시작일, 종료일을 모두 설정해주세요");
					angular.element('#termStartDt').focus();
					return false;
				}
				
				var termEndDt = new Date($scope.spsCoupon.termEndDt);
				var issueEndDt = new Date($scope.spsCoupon.issueEndDt);
				
				// 쿠폰 사용기간 <  쿠폰 발급기간
				if(termEndDt < issueEndDt) {
					alert("쿠폰의 유효기간 종료일은 발급기간 종료일 보다 이전 일 수 없습니다.");
					angular.element('#termEndDt').focus();
					return false;					
				}
				$scope.spsCoupon.termDays = "";
			}
		}

		
		
		var new_targetType = $scope.spsCoupon.ccsApply.targetTypeCd;
		var gridName = "";
		// 그리드 데이터 복사
		if(new_targetType == 'TARGET_TYPE_CD.ALL') {
			$scope.spsCoupon.ccsApply.ccsExcproducts = $scope.excludeProduct.data;
		} else if (new_targetType == 'TARGET_TYPE_CD.PRODUCT') {
			if($scope.applyTargetPr.data.length == 0) {
				gridName = "applyTargetPr";
			} else {
				$scope.spsCoupon.ccsApply.ccsApplytargets = $scope.applyTargetPr.data;
			}
		} else if (new_targetType == 'TARGET_TYPE_CD.BRAND') {
			if($scope.applyTargetBr.data.length == 0) {
				gridName = "applyTargetBr";
			} else {
				$scope.spsCoupon.ccsApply.ccsApplytargets = $scope.applyTargetBr.data;
				$scope.spsCoupon.ccsApply.ccsExcproducts = $scope.excludeProduct.data;
			}
		} else if (new_targetType == 'TARGET_TYPE_CD.CATEGORY') {
			if($scope.applyTargetCa.data.length == 0) {
				gridName = "applyTargetCa";
			} else {
				$scope.spsCoupon.ccsApply.ccsApplytargets = $scope.applyTargetCa.data;
				$scope.spsCoupon.ccsApply.ccsExcproducts = $scope.excludeProduct.data;
			}
		}
		
		if(gridName != "") {
			alert(pScope.MESSAGES["sps.common.enter.apply.target"]);
			angular.element("[data-ui-grid=\'"+gridName+"\']").focus();
			return false;
		}
		
		return true;
	}
	
	/* *********************** 취소버튼 *************************/
		this.closePopup = function() {
//			$window.opener.$scope.myGrid.loadGridData();	
			$window.close();
		}
		
	/* ******************** 각 그리드 버튼 ***************************/
//	var reLoad = function(gridName) {
//		$scope[gridName].loadGridData();
//	};
//	this.saveGridData = function(gridName) {
//		$scope[gridName].saveGridData(null, function(){
//			reLoad(gridName);
//		});
//	};
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
					, applyNo : $scope.spsCoupon.ccsApply.applyNo
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
					, applyNo : $scope.spsCoupon.ccsApply.applyNo
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
			if ( $scope.applyTargetCa.data[j].dmsDisplaycategory.displayCategoryId == $scope.popupData.displayCategoryId) {
				flag = false;
				break;
			}
		}
		if (flag) {
			$scope.applyTargetCaGrid.addRow({
				targetId : $scope.popupData.displayCategoryId
				, displayCategoryId : $scope.popupData.displayCategoryId
				, upperDisplayCategoryId : $scope.popupData.upperDisplayCategoryId
				, applyNo : $scope.spsCoupon.ccsApply.applyNo
				, saveType : 'I'
					, dmsDisplaycategory : {
						displayCategoryId : $scope.popupData.displayCategoryId
						, name : $scope.popupData.name
					}
			});
		}
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
	
	$scope.openPop = function(url, winName) {
		popupwindow(url,winName,1200,600);
	}
	
}).controller("sps_couponIssuedPopApp_controller", function($window, $scope, $filter, commonService, couponService, gridService, commonPopupService) {
	pScope = $window.opener.$scope;
	$window.$scope = $scope;


	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}

	
	$scope.search = {};
	
	$scope.infoType = [
	                    {val : 'ID',text : '회원ID'},
	                    {val : 'NAME',text : '회원명'}
		              ];
	
	var couponCountChk = function() {
		$scope.issueStop = true;						// 수동,엑셀 발행 버튼 사용 여부
		$scope.maxCouponIssue = pScope.maxCouponIssue;	// 최대 발급 수량
		var currentTotalCNT = pScope.totalCnt;	// 현재 발급 수량
		
		if($scope.maxCouponIssue > currentTotalCNT) {
			$scope.issueStop = false;
		} else {
			// 인증번호 사용시 메시지 처리 pass
			if(pScope.publicCin == "N") {
				alert(pScope.MESSAGES["sps.coupon.issue.max"]);
			}
		}
	}
	
	angular.element(document).ready(function () {
		$scope.search.couponId = pScope.couponId;
		if(angular.isDefined(pScope.publicCin)) {
			$scope.search.publicCin = pScope.publicCin;
			couponCountChk();
		} else {
			$scope.issueStop = false;
		}
		
		$scope.search.infoType = 'ID';
		// 발급 가능여부 확인
		
		commonService.init_search($scope,'search');
	});
	
	this.resetIssuedCoupon = function(){
		/* search Data 초기화 */
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	// tab 이동
	this.moveTab = function() {
		$window.location.href = Rest.context.path +"/sps/coupon/popup/detail";
	}
	
	var issueColumnDefs = [
			             { field: 'privateCin'					, colKey: "spsCouponissue.privateCin"	},
			             { field: 'regDt'						, colKey: "c.sps.coupon.issue.date" },
			             { field: 'memberInfo'					, colKey: "c.sps.coupon.pop.issue.mem"  },
			             { field: 'useStartDt'					, colKey: "spsCouponissue.useStartDt" },
			             { field: 'useEndDt'					, colKey: "spsCouponissue.useEndDt" },
			             { field: 'couponIssueStateName'		, colKey: "c.sps.coupon.pop.coupon.state"},
			             { field: 'useDt'						, colKey: "spsCouponissue.useDt" },
			             { field: 'omsOrdercoupon.orderId'		, colKey: "c.grid.column.use", linkFunction:"viewOrderDetail" },
	                     { field: 'insId'						, colKey: "c.grid.column.insId" , userFilter :'insId,insName' },
	                     { field: 'insDt'						, colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
                         { field: 'updId'						, colKey: "c.grid.column.updId" , userFilter :'updId,updName' },
                         { field: 'updDt'						, colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd HH:mm:ss\'" }
			            
			         ];
	
	var gridParam = {
		scope : $scope, 			//mandatory
		gridName : "gridIssuedCoupon",	//mandatory
		url :  '/api/sps/coupon/issued',  //mandatory
		searchKey : "search",       //mandatory
		columnDefs : issueColumnDefs,    //mandatory
		gridOptions : {             //optional
			checkBoxEnable : true
		},
		callbackFn : function(){//optional
			$scope.myGrid.loadGridData();
		}
	};
	
	//그리드 초기화
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	// 발급 내역 검색
	this.searchIssuedCoupon = function(){
		$scope.myGrid.loadGridData();
	}
	
	// 쿠폰 사용 중지
	this.stopIssuedCoupon = function() {
		var data = $scope.myGrid.getSelectedRows();
		if(data == [] || data == "") {
			alert(pScope.MESSAGES["sps.coupon.select.empty"]);
		} else {
			if (confirm(pScope.MESSAGES["sps.coupon.issue.stop"])) {
				couponService.stopIssueCoupon(data, function() {
					$scope.myGrid.loadGridData();
				});
			} else {
				return false;
			}
		}
	}
	
	// 수동 쿠폰 발급
	this.issueCoupon = function() {
		$scope.excel = false;
		$scope.isPeriod = false;
		commonPopupService.memberPopup($scope, "callback_member", false);
	}
	// 쿠폰 엑셀 일괄발급
	$scope.currentTab = "";
	this.addExcel = function(){
		$scope.excel = true;
		commonPopupService.gridbulkuploadPopup($scope,"callback_member", "coupon");
	}
	$scope.callback_member = function(data) {
		if($scope.excel) {
			$scope.myGrid.loadGridData();
		} else {
			$scope.spsCouponissue = {
					couponId : $scope.search.couponId,
					memberNo : data[0].memberNo
			}
			
			// 수동 쿠폰발급
			couponService.issueCoupon($scope.spsCouponissue, function(response) {
				if(response.resultCode == "0000") {
					alert(pScope.MESSAGES["sps.coupon.issue.success"]);
					$scope.myGrid.loadGridData();
				} else {
					alert(response.resultMsg);
				}
			});
		}
	}

	// 사용주문번호 상세
	$scope.viewOrderDetail = function(field, row) {
//TODO 사용주문번호 링크
//		if( row.couponStateCd == 'COUPON_STATE_CD.STOP' ){
//			alert("중지된 쿠폰입니다. 사용하실 수 없습니다.\n재등록 바랍니다. ");
//		} else {
//			$scope.couponId = {couponId : row.couponId};
//			
//			var winName='Coupon ModiFy';
//			var url= Rest.context.path + '/sps/coupon/popup/detail';
//			
//			popupwindow(url,winName,1100,600);
//		}
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
		    'SALE_STATE_CD.STOP': '판매중지',
	    	'SALE_STATE_CD.REQ': '승인전',
	    	'SALE_STATE_CD.APPROVAL1': 'QC검수요청',
	    	'SALE_STATE_CD.APPROVAL2': 'QC검수완료',
	    	'SALE_STATE_CD.REJECT': '반려',
	    	'SALE_STATE_CD.MDSTOP': 'MD정지'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('brandDisplayFilter', function() {// SALEPRODUCT_STATE_CD 
	
	var comboHash = {
			'Y': '노출',
		    'N': '비노출'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
});
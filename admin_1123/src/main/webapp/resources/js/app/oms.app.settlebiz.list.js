var settlebizApp = angular.module("settlebizApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'omsServiceModule',
                                           	'ui.date' ]);
//메시지
Constants.message_keys = [];

settlebizApp.controller("oms_settlebizListApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService) {
	//PO로그인일 경우 업체ID
	var poBusinessId = global.session.businessId=='null' ? null : global.session.businessId;
	$scope.poBusinessId = poBusinessId;

	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};

	$scope.yearList = [];
	$scope.monthList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
		//년도 설정
		$scope.getYears(-5, 6);

		$scope.search.saleYear = new Date().getFullYear();
		$scope.search.saleMonth = new Date().getMonth() + 1;
	});

	var	columnBizDefs = [];
		//PO가 아닌 경우
		if(common.isEmpty(poBusinessId)){
			columnBizDefs.push({ field: 'businessName'		, width: '15%',	colKey: 'c.oms.settlebiz.businessName', cellClass: 'alignC' });	//공급업체명
		}
		columnBizDefs.push({ field: 'finalSaleAmt'			, width: '10%',	colKey: 'c.oms.settlebiz.finalSaleAmt', cellClass: 'alignR', cellFilter: 'number' });	//합계
		columnBizDefs.push({ field: 'finalCardAmt'			, width: '10%',	colKey: 'c.oms.settlebiz.finalCardAmt', cellClass: 'alignR', cellFilter: 'number' }); //신용카드
		columnBizDefs.push({ field: 'finalCashAmt'			, width: '10%',	colKey: 'c.oms.settlebiz.finalCashAmt', cellClass: 'alignR', cellFilter: 'number' }); //현금
		columnBizDefs.push({ field: 'finalMobileAmt'		, width: '10%',	colKey: 'c.oms.settlebiz.finalMobileAmt', cellClass: 'alignR', cellFilter: 'number' });	//휴대폰
		columnBizDefs.push({ field: 'finalEtcAmt'			, width: '10%',	colKey: 'c.oms.settlebiz.finalEtcAmt', cellClass: 'alignR', cellFilter: 'number' });	//기타
		columnBizDefs.push({ field: 'finalOwnCouponAmt'		, width: '10%',	colKey: 'c.oms.settlebiz.finalOwnCouponAmt', cellClass: 'alignR', cellFilter: 'number' });	//자사쿠폰금액
		columnBizDefs.push({ field: 'finalBizCouponAmt'		, width: '10%',	colKey: 'c.oms.settlebiz.finalBizCouponAmt', cellClass: 'alignR', cellFilter: 'number' }); //업체쿠폰금액
		columnBizDefs.push({ field: 'finalSaleChargeAmt'	, width: '10%',	colKey: 'c.oms.settlebiz.saleCharge', cellClass: 'alignR', cellFilter: 'number' }); //위탁판매수수료
		columnBizDefs.push({ field: 'finalSupportAmt'		, width: '10%',	colKey: 'c.oms.settlebiz.finalSupportAmt', cellClass: 'alignR', cellFilter: 'number' }); //최종지급액

	var gridBizParam = {
		scope : $scope, 			//mandatory
		gridName : "grid_settlebiz",	//mandatory
		url :  '/api/oms/settlebiz',  //mandatory
		searchKey : 'search',     //mandatory
		columnDefs : columnBizDefs,    //mandatory
		gridOptions : {             //optional
			enableCellEdit : false,		//셀 수정 가능여부
			checkBoxEnable : false,	//row header에 check box 노출여부
			enableRowSelection : true,
			isRowSelectable : function(row) {
				var isRowSelectable = true;
				if (isRowSelectable) {
					row.cursor = 'pointer';
				}
				return isRowSelectable;
			},
			rowSelectionFn : function(row) {
//				console.log(row);
				$scope.items = {
					businessId : row.entity.businessId,
					saleYear : $scope.search.saleYear,
					saleMonth : $scope.search.saleMonth
				}
				$scope.myGridBizDet.loadGridData();
			}
		}
	};

	var	columnBizDetDefs = [];
		columnBizDetDefs.push({ field: 'saleStandardDt'		, width: '9%',	colKey: 'c.oms.settle.standardDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" }); //매출기준일
		columnBizDetDefs.push({ field: 'orderDt'			, width: '8%',	colKey: 'c.oms.settle.orderDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" }); //주문일
		columnBizDetDefs.push({ field: 'orderId'			, width: '10%',	colKey: 'c.oms.pgsettle.orderId', cellClass: 'alignC' }); //주문번호
        //PO가 아닌 경우
     	if(common.isEmpty(poBusinessId)){
     		columnBizDetDefs.push({ field: 'businessName'	, width: '10%',	colKey: 'c.oms.settle.bizName', cellClass: 'alignC' });	//공급사명
     	}
     	columnBizDetDefs.push({ field: 'erpBusinessId'		, width: '10%',	colKey: 'c.oms.settle.erpBizId', cellClass: 'alignC' }); //ERP업체코드
     	columnBizDetDefs.push({ field: 'productId'			, width: '8%',	colKey: 'c.oms.settle.productId', cellClass: 'alignC' }); //상품번호
     	columnBizDetDefs.push({ field: 'productName'		, width: '15%',	colKey: 'c.oms.settle.productName', cellClass: 'alignC' }); //상품명
     	columnBizDetDefs.push({ field: 'saleproductId'		, width: '8%',	colKey: 'c.oms.settle.saleproductId', cellClass: 'alignC' }); //단품번호
     	columnBizDetDefs.push({ field: 'saleproductName'	, width: '15%',	colKey: 'c.oms.settle.saleproductName', cellClass: 'alignC' }); //단품명
     	columnBizDetDefs.push({ field: 'supplyPrice'		, width: '9%',	colKey: 'c.oms.settle.supplyPrice', cellClass: 'alignR', cellFilter: 'number' }); //공급단가
     	columnBizDetDefs.push({ field: 'salePrice'			, width: '9%',	colKey: 'c.oms.settle.salePrice', cellClass: 'alignR', cellFilter: 'number' }); //판매단가
     	columnBizDetDefs.push({ field: 'supplyAmt'			, width: '9%',	colKey: 'c.oms.settle.supplyAmt', cellClass: 'alignR', cellFilter: 'number' }); //공급금액
     	columnBizDetDefs.push({ field: 'saleAmt'			, width: '9%',	colKey: 'c.oms.settle.saleAmt', cellClass: 'alignR', cellFilter: 'number' }); //판매금액
     	columnBizDetDefs.push({ field: 'strCommissionRate'	, width: '9%',	colKey: 'c.oms.settle.chargeRate', cellClass: 'alignR' }); //수수료율
     	columnBizDetDefs.push({ field: 'chargeAmt'			, width: '8%',	colKey: 'c.oms.settlebiz.chargeAmt', cellClass: 'alignR', cellFilter: 'number' }); //수수료
     	columnBizDetDefs.push({ field: 'paymentMethodName'	, width: '10%',	colKey: 'c.oms.settlebiz.paymentMethod', cellClass: 'alignR' }); //결제수단
     	columnBizDetDefs.push({ field: 'paymentAmt'			, width: '10%',	colKey: 'c.oms.settle.paymentAmt', cellClass: 'alignR', cellFilter: 'number' }); //고객결제금액
     	columnBizDetDefs.push({ field: 'etcAmt'				, width: '10%',	colKey: 'c.oms.settlebiz.etcAmt', cellClass: 'alignR', cellFilter: 'number' }); //기타금액
     	columnBizDetDefs.push({ field: 'ownCouponAmt'		, width: '10%',	colKey: 'c.oms.settle.ownCouponAmt', cellClass: 'alignR', cellFilter: 'number' }); //자사쿠폰금액
     	columnBizDetDefs.push({ field: 'bizCouponAmt'		, width: '10%',	colKey: 'c.oms.settle.bizCouponAmt', cellClass: 'alignR', cellFilter: 'number' }); //업체쿠폰금액
     	columnBizDetDefs.push({ field: 'supportAmt'			, width: '10%',	colKey: 'c.oms.settlebiz.supportAmt', cellClass: 'alignR', cellFilter: 'number' }); //지급액
     	columnBizDetDefs.push({ field: 'qty'				, width: '6%',	colKey: 'c.oms.settle.orderQty', cellClass: 'alignR', cellFilter: 'number' }); //수량
     	columnBizDetDefs.push({ field: 'categoryName'		, width: '10%',	colKey: 'c.oms.settle.category', cellClass: 'alignC' }); //표준카테고리
     	columnBizDetDefs.push({ field: 'taxTypeName'		, width: '6%',	colKey: 'c.oms.settle.taxType', cellClass: 'alignC' }); //과세구분
     	columnBizDetDefs.push({ field: 'claimTypeName'		, width: '15%',	colKey: 'c.oms.settle.claimType', cellClass: 'alignC' }); //클레임유형명
     	columnBizDetDefs.push({ field: 'claimNo'			, width: '8%',	colKey: 'c.oms.settle.claimNo', cellClass: 'alignC' }); //클레임번호

	var gridBizDetParam = {
		scope : $scope, 			//mandatory
		gridName : "grid_settlebizdet",	//mandatory
		url :  '/api/oms/settlebiz/detail',  //mandatory
		searchKey : 'items',     //mandatory
		columnDefs : columnBizDetDefs,    //mandatory
		gridOptions : {             //optional
			enableCellEdit : false,		//셀 수정 가능여부
			checkBoxEnable : false	//row header에 check box 노출여부
		},
		callbackFn : function() {	//optional
		}
	};

	//그리드 객체생성
	$scope.myGridBiz = new gridService.NgGrid(gridBizParam);
	$scope.myGridBizDet = new gridService.NgGrid(gridBizDetParam);

	//message 정의
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	$scope.getYears = function(offset, range) {
		var currentYear = new Date().getFullYear();
		for (var i= 0; i < range; i++) {
			$scope.yearList.push(currentYear + offset + i);
		}
	}

	//업체 검색 팝업
	this.searchBusiness = function() {
		commonPopupService.businessPopup($scope,"callback_business", false);
		$scope.callback_business = function(data) {
			$scope.search.businessId = data[0].businessId;
			$scope.search.businessName = data[0].name;
			$scope.$apply();		
		}
	}

	//지우개
	this.eraser = function(name) {
		$scope.search[name+'Id'] = "";
		$scope.search[name+'Name'] = "";
	}

	this.reset = function() {
		commonService.reset_search($scope, 'search');
//		console.log("scope==> ", $scope);
		$scope.search.saleYear = new Date().getFullYear();
		$scope.search.saleMonth = new Date().getMonth() + 1;
	}
});
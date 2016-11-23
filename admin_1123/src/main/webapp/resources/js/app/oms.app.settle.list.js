var settleApp = angular.module("settleApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'omsServiceModule',
                                           	'ui.date' ]);
//메시지
Constants.message_keys = [];

settleApp.controller("oms_settleListApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService, logisticsService, settleService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	
	$scope.orderType = [
		                    {val: 'ALL',	  text: '전체'},
		                    {val: 'GENERAL',  text: '일반가'},
		                    {val: 'EMPLOYEE', text: '임직원가'},
		                    {val: 'SHOCKDEAL', text: '쇼킹제로가'},
		                    {val: 'MEMBER',	  text: '멤버십가'},
		                    {val: 'PREMIUM',  text: '프리미엄멤버십가'},
		                    {val: 'CHILDREN', text: '다자녀가'},
		                    {val: 'B2E',	  text: '타사B2E가'},
		                    {val: 'B2B',	  text: 'B2B(외부몰/중국몰)'}
		               ];

	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
	});
	
	var	columnDefs = [
		                 { field: 'orderType'		, width: '8%',	colKey: 'c.oms.settle.orderType', cellClass: 'alignC' },	//주문구분
		                 { field: 'saleStandardDt'	, width: '10%',	colKey: 'c.oms.settle.standardDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" },	//매출기준일
		                 { field: 'orderDt'			, width: '10%',	colKey: 'c.oms.settle.orderDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" }, //주문일
		                 { field: 'orderId'			, width: '12%',	colKey: 'c.oms.pgsettle.orderId', cellClass: 'alignC' }, //주문번호
		                 { field: 'settleType'		, width: '6%',	colKey: 'c.oms.settle.settleType', cellClass: 'alignC' },	//정산유형
		                 { field: 'businessName'	, width: '8%',	colKey: 'c.oms.settle.bizName', cellClass: 'alignC' },	//공급사명
		                 { field: 'erpBusinessId'	, width: '8%',	colKey: 'c.oms.settle.erpBizId', cellClass: 'alignC' },	//ERP업체코드
		                 { field: 'productId'		, width: '7%',	colKey: 'c.oms.settle.productId', cellClass: 'alignC' },	//상품번호
		                 { field: 'productName'		, width: '15%',	colKey: 'c.oms.settle.productName', cellClass: 'alignC' }, //상품명
		                 { field: 'saleproductId'	, width: '7%',	colKey: 'c.oms.settle.saleproductId', cellClass: 'alignC' }, //단품번호
		                 { field: 'saleproductName'	, width: '15%',	colKey: 'c.oms.settle.saleproductName', cellClass: 'alignC' }, //단품명
		                 { field: 'supplyPrice'		, width: '8%',	colKey: 'c.oms.settle.supplyPrice', cellClass: 'alignR', cellFilter: 'number' }, //공급단가
		                 { field: 'salePrice'		, width: '8%',	colKey: 'c.oms.settle.salePrice', cellClass: 'alignR', cellFilter: 'number' }, //판매단가
		                 { field: 'supplyAmt'		, width: '8%',	colKey: 'c.oms.settle.supplyAmt', cellClass: 'alignR', cellFilter: 'number' }, //공급금액
		                 { field: 'saleAmt'			, width: '8%',	colKey: 'c.oms.settle.saleAmt', cellClass: 'alignR', cellFilter: 'number' }, //판매금액
		                 { field: 'strCommissionRate'	, width: '8%',	colKey: 'c.oms.settle.chargeRate', cellClass: 'alignR' }, //수수료율
		                 { field: 'paymentAmt'		, width: '10%',	colKey: 'c.oms.settle.paymentAmt', cellClass: 'alignR', cellFilter: 'number' }, //고객결제금액
		                 { field: 'purchaseSales'	, width: '10%',	colKey: 'c.oms.settle.purchaseSales', cellClass: 'alignR', cellFilter: 'number' }, //사입 회계매출
		                 { field: 'consignSales'	, width: '10%',	colKey: 'c.oms.settle.consignSales', cellClass: 'alignR', cellFilter: 'number' }, //위탁 회계매출
		                 { field: 'maeilPoint'		, width: '9%',	colKey: 'c.oms.settle.maeilPoint', cellClass: 'alignR', cellFilter: 'number' }, //매일포인트
		                 { field: 'depositAmt'		, width: '10%',	colKey: 'c.oms.settle.balanceAmt', cellClass: 'alignR', cellFilter: 'number' }, //예치금사용액
		                 { field: 'ownCouponAmt'	, width: '10%',	colKey: 'c.oms.settle.ownCouponAmt', cellClass: 'alignR', cellFilter: 'number' }, //자사쿠폰금액
		                 { field: 'bizCouponAmt'	, width: '10%',	colKey: 'c.oms.settle.bizCouponAmt', cellClass: 'alignR', cellFilter: 'number' }, //업체쿠폰금액
		                 { field: 'plusCouponAmt'	, width: '10%',	colKey: 'c.oms.settle.plusCouponAmt', cellClass: 'alignR', cellFilter: 'number' }, //플러스쿠폰금액
		                 { field: 'basketCouponAmt'	, width: '10%',	colKey: 'c.oms.settle.basketCouponAmt', cellClass: 'alignR', cellFilter: 'number' }, //장바구니쿠폰금액
		                 { field: 'qty'				, width: '5%',	colKey: 'c.oms.settle.orderQty', cellClass: 'alignR', cellFilter: 'number' }, //수량
		                 { field: 'siteName'		, width: '8%',	colKey: 'c.oms.settle.site', cellClass: 'alignC' }, //사이트명
		                 { field: 'categoryName'	, width: '10%',	colKey: 'c.oms.settle.category', cellClass: 'alignC' }, //표준카테고리
		                 { field: 'taxTypeName'		, width: '7%',	colKey: 'c.oms.settle.taxType', cellClass: 'alignC' }, //과세구분
		                 { field: 'claimTypeName'	, width: '15%',	colKey: 'c.oms.settle.claimType', cellClass: 'alignC' }, //클레임유형명
		                 { field: 'claimNo'			, width: '9%',	colKey: 'c.oms.settle.claimNo', cellClass: 'alignC' }, //클레임번호
		                 { field: 'shipDt'			, width: '10%',	colKey: 'c.oms.settle.outDoneDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" }, //출고완료일
		                 { field: 'returnDt'		, width: '10%',	colKey: 'c.oms.settle.inDoneDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" }, //입고완료일
		                 { field: 'claimDt'			, width: '10%',	colKey: 'c.oms.settle.claimDoneDt', cellClass: 'alignC', cellFilter: "date:\'yyyy-MM-dd\'" }, //클레임완료일
		                 { field: 'channelName'		, width: '15%',	colKey: 'c.oms.settle.channel', cellClass: 'alignC' }, //채널
		                 { field: 'okcashbagNo'		, width: '15%',	colKey: 'c.oms.settle.okcashbagNo', cellClass: 'alignC' } //OK캐쉬백번호
		             ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_settle",	//mandatory
			url :  '/api/oms/settle',  //mandatory
			searchKey : 'search',     //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				enableCellEdit : false,		//셀 수정 가능여부
				checkBoxEnable : false	//row header에 check box 노출여부
			},
			callbackFn : function() {	//optional
			}
	};

	//그리드 객체생성
	$scope.myGrid = new gridService.NgGrid(gridParam);

	//message 정의
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	//전체 사이트정보 조회
	logisticsService.getAllSiteList(function(response) {
//		console.log(response);
		$scope.siteList = response;
	});

	//전체 채널정보 조회
	settleService.getAllChannelList(function(response) {
//		console.log(response);
		$scope.channelList = response;
	});

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
		angular.element(".day_group").find('button:first').addClass("on");
	}
});
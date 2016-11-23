var pgListApp = angular.module("pgListApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'omsServiceModule',
                                           	'ui.date' ]);
//메시지
Constants.message_keys = [];

pgListApp.controller("oms_pgSettleListApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	
	$scope.periodType = [
		                    {val: 'APPROVE',	text: '승인일'},
		                    {val: 'ORDER',		text: '주문일'},
		                    {val: 'DEPOSIT',	text: '입금예정일'}
		                ];

	$scope.lguShopIds = [
		                    {val: '',		text: '전체'},
		                    {val: 'ZTS_00',	text: 'ZTS_00'},
		                    {val: 'ZTS_01',	text: 'ZTS_01'},
		                    {val: 'ZTS_10',	text: 'ZTS_10'},
		                    {val: 'ZTS_11',	text: 'ZTS_11'},
		                    {val: 'ZTS_20',	text: 'ZTS_20'},
		                    {val: 'ZTS_21',	text: 'ZTS_21'},
		                    {val: 'ZTS_30',	text: 'ZTS_30'},
		                    {val: 'ZTS_40',	text: 'ZTS_40'}
			            ];

	$scope.kakaoShopIds = [
		                    {val: '',			text: '전체'},
		                    {val: 'cnstest25m',	text: 'cnstest25m'}
			            ];

	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
		$scope.pgShopIds = $scope.lguShopIds;
	});
	
	var	columnDefs = [
		                 { field: 'pgCompany'			, width: '6%',	colKey: 'c.oms.pgsettle.pgCompany' },	//PG사
		                 { field: 'pgApprovalNo'		, width: '20%',	colKey: 'c.oms.pgsettle.approvalNo' },	//승인번호
		                 { field: 'paymentDt'			, width: '10%',	colKey: 'c.oms.pgsettle.approvalDate', cellFilter: "date:\'yyyy-MM-dd\'" },	//승인일시
		                 { field: 'orderId'				, width: '15%',	colKey: 'c.oms.pgsettle.orderId', linkFunction : 'func.popup.order' },		//주문번호
		                 { field: 'orderDt'				, width: '10%',	colKey: 'c.oms.pgsettle.orderDate', cellFilter: "date:\'yyyy-MM-dd\'" },	//주문일시
		                 { field: 'pgShopId'			, width: '10%',	colKey: 'c.oms.pgsettle.pgStoreId' },	//PG 상점ID
		                 { field: 'paymentMethodName'	, width: '10%',	colKey: 'c.oms.pgsettle.paymentMethod' },	//결제수단
		                 { field: 'approvalAmt'			, width: '10%',	colKey: 'c.oms.pgsettle.approvalAmt' },	//결제금액
		                 { field: 'paymentAmt'			, width: '10%',	colKey: 'c.oms.pgsettle.paymentAmt' },	//매입금액
		                 { field: 'depositAmt'			, width: '10%',	colKey: 'c.oms.pgsettle.depositAmt' },	//입금예정금액
		                 { field: 'settleDt'			, width: '10%',	colKey: 'c.oms.pgsettle.depositDate', cellFilter: "date:\'yyyy-MM-dd\'" },	//입금예정일
		                 { field: 'errorAmt'			, width: '10%',	colKey: 'c.oms.pgsettle.errorAmt' },	//오차금액
		             ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_pgsettle",	//mandatory
			url :  '/api/oms/pgsettle',  //mandatory
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

	this.reset = function() {
		commonService.reset_search($scope, 'search');
//		console.log("scope==> ", $scope);
		angular.element(".day_group").find('button:first').addClass("on");
	}

	$scope.$on("radioChanged", function(event, args) {
		$scope.$apply(function() {
			var target = args.target;
			console.log("target: ", target);
			if(target.value == 'Y') { //LG U+
				$scope.pgShopIds = $scope.lguShopIds;
			} else { //Kakaopay
				$scope.pgShopIds = $scope.kakaoShopIds;
			}
		});
	});

	//Grid: Order ID 선택 호출
//	$scope.getOrderDetail = function(fieldValue, rowEntity) {
//		$scope.orderId = rowEntity.orderId;
//		console.log($scope.orderId);
//		
//		var url = Rest.context.path + '/oms/order/popup/detail';
//		popupwindow(url, "Order Detail", 1300, 800);
//	}
});
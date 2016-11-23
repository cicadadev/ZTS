var termOrderApp = angular.module("termOrderApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'omsServiceModule', 'ui.date' ]);
//메시지
Constants.message_keys = ["oms.uploadconf.insert.noSiteId", "oms.uploadconf.insert.already", "oms.uploadconf.insert.success"];

termOrderApp.controller("oms_termOrderManagerApp_controller", function($window, $scope, commonService, gridService, termOrderService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	$scope.omsReceiveordermapping = {};

	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
	});

	var	columnDefs = [
		                 { field: 'orderId'			, width: '11%',	colKey: 'c.oms.termorder.siteOrderNo', enableCellEdit: true },
		                 { field: 'orderDate'		, width: '10%',	colKey: 'c.oms.termorder.siteOrderDt' },
		                 { field: 'siteName'		, width: '10%',	colKey: 'c.oms.termorder.site' },
		                 { field: 'userName'		, width: '8%',	colKey: 'c.oms.termorder.orderName', enableCellEdit: true },
		                 { field: 'receiveName'		, width: '8%',	colKey: 'c.oms.termorder.receiveName', enableCellEdit: true },
		                 { field: 'receiveTel'		, width: '10%',	colKey: 'c.oms.termorder.receiveTel', enableCellEdit: true },
		                 { field: 'receiveCel'		, width: '10%',	colKey: 'c.oms.termorder.receiveHp', enableCellEdit: true },
		                 { field: 'receiveZipcode'	, width: '8%',	colKey: 'c.oms.termorder.receiveZip', enableCellEdit: true },
		                 { field: 'receiveAddr'		, width: '10%',	colKey: 'c.oms.termorder.receiveAddr', enableCellEdit: true },
		                 { field: 'productName'		, width: '10%',	colKey: 'c.oms.termorder.sitePrdNm' },
		                 { field: 'skuValue'		, width: '10%',	colKey: 'c.oms.termorder.siteOptNm' },
		                 { field: 'compaynyGoodsCd'	, width: '8%',	colKey: 'c.oms.termorder.custPrdCd' },
		                 { field: 'skuAlias'		, width: '8%',	colKey: 'c.oms.termorder.saleproductId', enableCellEdit: true },
		                 { field: 'delvMsg'			, width: '8%',	colKey: 'c.oms.termorder.note' },
		                 { field: 'orderEtc2'		, width: '8%',	colKey: 'c.oms.termorder.presentId', enableCellEdit: true },
		                 { field: 'boYn'			, width: '5%',	colKey: 'c.oms.termorder.boYn' },
		                 { field: 'boMsg'			, width: '15%',	colKey: 'c.oms.termorder.errorMsg' }
		             ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_termorder",	//mandatory
			url :  '/api/oms/termorder',  //mandatory
			searchKey : "search",     //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				enableCellEdit : false,	//셀 수정 가능여부
				checkBoxEnable : true	//row header에 check box 노출여부
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

	//국내 외부몰, 중국몰 사이트정보 조회
	termOrderService.getExternalSiteList(function(response) {
//		console.log(response);
		$scope.siteList = response;
	});

	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}

	//사방넷 -> 터미널
	this.fromSbnToTerm = function() {
		termOrderService.receiveSbnOrder($scope.search, function(response) {
			console.log(response);
			if ("F" == response.content) {
				alert('사방넷 호출 실패!!');
			} else {
				alert('사방넷 주문수집이 완료되었습니다. 호출결과: ' + response.content);
			}
		});
	}

	//터미널(사방넷) -> BO
	this.fromTermSbnToBo = function() {
		termOrderService.sbnOrderToBo(function(response) {
			console.log(response);
			alert(response.content);
			$scope.myGrid.loadGridData();
		});
	}

	//Tmall -> 터미널
	this.fromTmallToTerm = function() {
		termOrderService.receiveTmallOrder($scope.search, function(response) {
			console.log(response);
			if ("F" == response.content) {
				alert('Tmall 주문수집 실패!!');
			} else {
				alert('Tmall 주문수집이 완료되었습니다. 호출결과: ' + response.content);
			}
		});
	}

	//터미널(Tmall) -> BO
	this.fromTermTmallToBo = function() {
		termOrderService.tmallOrderToBo(function(response) {
			console.log(response);
			alert(response.content);
			$scope.myGrid.loadGridData();
		});
	}

	//선택된 Grid 삭제
	this.deleteExcelOrder = function() {
//		console.log("selected_grid: ", $scope.myGrid.getSelectedRows());
		termOrderService.deleteReceiveOrder($scope.myGrid.getSelectedRows(), function(response) {
			console.log("Delete Result: ", response);
			if (response.content == 'Y') {
				$scope.myGrid.loadGridData();
			} else {
				alert('실패');
			}
		});
	}

	//선택된 Grid 저장
	this.saveExcelOrder = function() {
		//변경저장
		$scope.myGrid.saveGridData(null, function() {					
			$scope.myGrid.loadGridData();
		});
	}
});
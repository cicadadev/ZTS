var sendgoodsApp = angular.module("sendgoodsApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'pmsServiceModule',
                                           	'ui.date' ]);
//메시지
Constants.message_keys = ["pms.sendgoods.send.success"];

sendgoodsApp.controller("pms_sendGoodsListApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService, sendgoodsService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};

	$scope.dateType = [
	                    {val : 'REG',  text : '상품등록일'},
	                    {val : 'SEND', text : '상품전송일'}
		              ];
	$scope.infoType = [
	                    {val : 'ID',   text : '상품번호'},
	                    {val : 'NAME', text : '상품명'}
		              ];

	
	angular.element(document).ready(function () {
		
		commonService.init_search($scope, 'search');
	});

	var	columnDefs = [
			             { field: 'productId', 			width:100, 				colKey: "c.pms.sendgoods.productNo", linkFunction: "getProductNo" },
			             { field: 'name', 				width:100, 				colKey: "pmsProduct.name", linkFunction: "getProductNo" },
			             { field: 'businessInfo',		width:100, 				colKey: "c.pms.sendgoods.bizInfo" },
			             { field: 'saleStateName',	    width:100, 				colKey: "c.pms.sendgoods.saleState" },
			             { field: 'productTypeName', 	width:100, 				colKey: "c.pms.sendgoods.productType" },
			             { field: 'outSendYn',	    	width:100, 				colKey: "c.pms.sendgoods.sendYn" },
			             { field: 'insDt',				width:100, 				colKey: "c.pms.sendgoods.regDate" },
			             { field: 'outSendDt',			width:100, 				colKey: "c.pms.sendgoods.sendDate" },
			             { field: 'maker',       		width:100, 				colKey: "c.pms.sendgoods.maker" },
			             { field: 'taxTypeName',		width:100, 				colKey: "c.pms.sendgoods.taxType" },
			             { field: 'erpProductId',		width:100, 				colKey: "pmsProduct.erpProductId" },
			             { field: 'listPrice',			type : 'number', 		width:100, 				colKey: "pmsProduct.listPrice" },
			             { field: 'salePrice',			type : 'number', 		width:100, 				colKey: "pmsProduct.salePrice" },
			             { field: 'supplyPrice',		type : 'number', 		width:100, 				colKey: "pmsProduct.supplyPrice" },
			             { field: 'saveType',	    	width:100, 				colKey: "c.pms.sendgoods.successYn" },
			             
			             { field: 'insId', 				width:100, 				colKey: "c.grid.column.insId" , userFilter :'insId,insName'},
			             { field: 'insDt', 				width:100, 				colKey: "c.grid.column.insDt" },
			             { field: 'updId', 				width:100, 				colKey: "c.grid.column.updId" , userFilter :'updId,updName'},
			             { field: 'updDt', 				width:100, 				colKey: "c.grid.column.updDt" }
			         ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_sendgoods",	//mandatory
			url :  '/api/pms/sendgoods',  //mandatory
			searchKey : "search",     //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				enableCellEdit : false	//셀 수정 가능여부
				//enableHorizontalScrollbar : 2
				//enableSorting : false
//				checkMultiSelect : true
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
		angular.element(".day_group").find('button:first').addClass("on");
	}

	//각 검색 팝업
	this.openPopup = function(kindOf) {
		if(kindOf == 'md') {
			//담당MD 검색 팝업
			commonPopupService.userPopup($scope,"callback_md", false, "USER_TYPE_CD.MD");
			$scope.callback_md = function(data) {
				$scope.search.mdId = data[0].userId;
				$scope.search.mdName = data[0].name;
				$scope.$apply();
			}
		} else if(kindOf == 'business') {
			//업체 검색 팝업
			commonPopupService.businessPopup($scope,"callback_business", false);
			$scope.callback_business = function(data) {
				$scope.search.businessId = data[0].businessId;
				$scope.search.businessName = data[0].name;
				$scope.$apply();		
			}
		} else if(kindOf == 'brand') {
			//브랜드 검색 팝업
			commonPopupService.brandPopup($scope,"callback_brand", false);
			$scope.callback_brand = function(data) {
				$scope.search.brandId = data[0].brandId;
				$scope.search.brandName = data[0].name;
				$scope.$apply();		
			}
		}
	}
	//지우개
	this.eraser = function(name) {
		$scope.search[name+'Id'] = "";
		$scope.search[name+'Name'] = "";
	}

//	//Grid: 상품번호 선택 호출
//	$scope.getProductNo = function(fieldValue, rowEntity) {
////		$scope.productId = { productId : rowEntity.productId };
//		$scope.selectedProductId = rowEntity.productId;
//		
//		var url = Rest.context.path + '/pms/product/popup/detail';
//		popupwindow(url, "Product Detail", 1300, 800);
//	}

	//상품 상세 팝업
	$scope.getProductNo = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}

	//사방넷 전송을 위한 테이블 등록
	this.sendSabangnet = function() {
//		console.log("grid: ", $scope.grid_sendgoods);
		$scope.pmsProduct = [];
//		var gridCnt = $scope.grid_sendgoods.data.length;
//		for (var i = 0; i < gridCnt; i++) {
//			if ($scope.grid_sendgoods.data[i].checked) {
//				$scope.pmsProduct.push($scope.grid_sendgoods.data[i]);
//			}
//		}
		$scope.pmsProduct = $scope.myGrid.getSelectedRows();
//		console.log("product: ", $scope.pmsProduct);
		sendgoodsService.insertSendgoods($scope.pmsProduct, function(response) {
//			console.log("response: ", response);

			alert(response.content);
//			alert($scope.MESSAGES["pms.sendgoods.send.success"]);
			$scope.myGrid.loadGridData();
		});
	}

});
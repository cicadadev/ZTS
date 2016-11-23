var uploadConfApp = angular.module("uploadConfApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'omsServiceModule', 'ui.date' ]);
//메시지
Constants.message_keys = ["oms.uploadconf.insert.noSiteId", "oms.uploadconf.insert.already", "oms.uploadconf.insert.success", "oms.uploadconf.insert.fail", 
                          "oms.uploadconf.update.success", "oms.uploadconf.update.fail"];

uploadConfApp.controller("oms_uploadConfListApp_controller", function($window, $scope, commonService, gridService, uploadConfService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
	});

	var	columnDefs = [
		                 { field: 'siteName'		, width: '10%',	colKey: 'ccsSite.name', linkFunction: "uploadconfDetail" },
		                 { field: 'siteId'			, width: '10%',	colKey: 'c.oms.uploadconf.siteId' },
		                 { field: 'titleRow'		, width: '10%',	colKey: 'omsUploadconf.titleRow' },
		                 { field: 'dataRow'			, width: '10%',	colKey: 'omsUploadconf.dataRow' },
		                 { field: 'siteOrderId'		, width: '10%',	colKey: 'c.oms.uploadconf.siteOrderId' },
		                 { field: 'saleproductId1'	, width: '10%',	colKey: 'c.oms.uploadconf.saleproductId1' },
		                 { field: 'saleproductId2'	, width: '10%',	colKey: 'c.oms.uploadconf.saleproductId2' },
		                 { field: 'saleproductId3'	, width: '10%',	colKey: 'c.oms.uploadconf.saleproductId3' },
		                 { field: 'saleproductId4'	, width: '10%',	colKey: 'c.oms.uploadconf.saleproductId4' },
		                 { field: 'saleproductId5'	, width: '10%',	colKey: 'c.oms.uploadconf.saleproductId5' },
		                 { field: 'salePrice'		, width: '10%',	colKey: 'c.oms.uploadconf.salePrice' },
		                 { field: 'orderQty'		, width: '10%',	colKey: 'c.oms.uploadconf.orderQty' },
		                 { field: 'zipCd'			, width: '10%',	colKey: 'omsUploadconf.zipCd' },
		                 { field: 'address1'		, width: '10%',	colKey: 'omsUploadconf.address1' },
		                 { field: 'address2'		, width: '10%',	colKey: 'omsUploadconf.address2' },
		                 { field: 'phone1'			, width: '10%',	colKey: 'c.oms.uploadconf.phone1' },
		                 { field: 'phone2'			, width: '10%',	colKey: 'c.oms.uploadconf.phone2' },
		                 { field: 'name'			, width: '10%',	colKey: 'c.oms.uploadconf.name' },
		                 { field: 'note'			, width: '10%',	colKey: 'c.oms.uploadconf.note' },
		                 { field: 'currencyCd'		, width: '10%',	colKey: 'omsUploadconf.currencyCd' },
		                 { field: 'currencyPrice'	, width: '10%',	colKey: 'c.oms.uploadconf.currencyPrice' },
		                 { field: 'lpNo'			, width: '10%',	colKey: 'c.oms.uploadconf.lpNo' },
		                 { field: 'localDelivery'	, width: '5%',	colKey: 'c.oms.uploadconf.bondYn' }
		             ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_uploadconf",	//mandatory
			url :  '/api/oms/uploadconf',  //mandatory
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

	//외부몰,중국몰 사이트정보 조회
	uploadConfService.getExternalSiteList(function(response) {
//		console.log(response);
		$scope.siteList = response;
	});

	this.reset = function() {
		commonService.reset_search($scope, 'search');
	}

//	this.searchUploadconfList = function() {
//		uploadConfService.getUploadconfList($scope.search.siteId);
//	}

	//Grid: 사이트명 선택 호출
	$scope.uploadconfDetail = function(fieldValue, rowEntity) {
//		console.log("Selected ID: " + rowEntity.siteId);
		$scope.siteId = rowEntity.siteId;
		uploadConfService.openUploadconfDetailPopup();
	}

	//등록 팝업 호출
	this.insertUploadconfPopup = function() {
		var url = Rest.context.path + '/oms/uploadconf/popup/insert';
		popupwindow(url, "Uploadconf Insert", 1200, 550);
	}

	//선택된 Grid 삭제
//	this.deleteGridData = function() {
//		var checkEventState = true;
//		
//		var selectRows = $scope.myGrid.getSelectedRows();
//		angular.forEach(selectRows, function(row) {
//			if (checkEventState) {	// alert 한번만 띄우기 위해.
//				alert($scope.MESSAGES["sps.event.delete.validation"]);
//				checkEventState = false;
//			}
//		});
//		
//		if (checkEventState) {
//			$scope.myGrid.deleteGridData();
//		}	
//	}
}).controller("oms_uploadConfUpdPopupApp_controller", function($window, $scope, commonService, commonPopupService, uploadConfService) { //업로드설정 상세 PopUp Controller
	//현재 팝업에서 부모 scope 접근하기 위한 변수 설정
	pScope = $window.opener.$scope;

	//현재 팝업 정보 설정: 속성값 팝업에서 사용
//	$window.$scope = $scope;

	//업로드설정 변수
	$scope.omsUploadconf = {};
	
	this.detInit = function() {
		//업로드설정 상세정보 조회
//		console.log("siteId: ", pScope.siteId);
		uploadConfService.getUploadconfDetail(pScope.siteId, function(response) {
	    	$scope.omsUploadconf = response;
//	    	console.log("result: ", $scope.omsUploadconf);
	    });
	};

	//변경된 업로드설정 저장
	this.saveUploadconf = function() {
		//폼 체크
		if(!commonService.checkForm($scope.frmUploadconfDetail)) {
			return;
		}

//		console.log("config: " + $scope.omsUploadconf);
		uploadConfService.updateUploadconf($scope.omsUploadconf, function(response) {
			if(null != response.content && '' != response.content) {
				alert(pScope.MESSAGES["oms.uploadconf.update.success"]);
				pScope.myGrid.loadGridData();
			} else {
				alert(pScope.MESSAGES["oms.uploadconf.update.fail"]);
			}
			
			$window.close();
	    });
	}

	this.close = function() {
		$window.close();
	}
}).controller("oms_uploadConfInsPopApp_controller", function($window, $scope, commonService, commonPopupService, uploadConfService) { //업로드설정 등록 PopUp Controller
	//현재 팝업에서 부모 scope 접근하기 위한 변수 설정
	pScope = $window.opener.$scope;
	
	//현재 팝업 정보 설정: 속성값 팝업에서 사용
//	$window.$scope = $scope;

	//업로드설정 변수
	$scope.omsUploadconf = {};

	//외부몰,중국몰 사이트정보 조회
	uploadConfService.getExternalSiteList(function(response) {
//		console.log(response);
		$scope.siteList = response;
	});

	//신규 업로드설정 저장
	this.saveNewUploadconf = function() {
		//사이트코드 체크
		if(common.isEmpty($scope.omsUploadconf.siteId)) {
			alert(pScope.MESSAGES["oms.uploadconf.insert.noSiteId"]);
			return;
		}

		//폼 체크
		if(!commonService.checkForm($scope.frmUploadconfInsert)) {
			return;
		}

		//기등록여부 테이블 검색
		uploadConfService.checkDuplication($scope.omsUploadconf, function(response) {
//			console.log("checkDuplication response: ", response.content);
			if ("Y" == response.content) { //기등록
				alert(pScope.MESSAGES["oms.uploadconf.insert.already"]);
				return;
			} else { //신규
				uploadConfService.insertUploadconf($scope.omsUploadconf, function(response) {
					if(null != response.content && '' != response.content) {
						alert(pScope.MESSAGES["oms.uploadconf.insert.success"]);
						pScope.myGrid.loadGridData();
					} else {
						alert(pScope.MESSAGES["oms.uploadconf.insert.fail"]);
					}

					$window.close();
			    });
			}
		});

		
	}

	this.close = function() {
		$window.close();
	}
});
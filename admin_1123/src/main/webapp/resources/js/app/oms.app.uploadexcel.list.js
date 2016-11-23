var uploadExcelApp = angular.module("uploadExcelApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'omsServiceModule', 'ui.date' ]);
//메시지
Constants.message_keys = ["common.search.commbo.default", "oms.uploadexcel.insert.success"];

uploadExcelApp.controller("oms_uploadExcelListApp_controller", function($window, $scope, commonService, gridService, uploadExcelService, uploadConfService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
	});

	var	columnDefs = [
		                 { field: 'siteName'		, width: '8%',	colKey: 'ccsSite.name' },
		                 { field: 'siteId'			, width: '8%',	colKey: 'c.oms.uploadconf.siteId' },
		                 { field: 'titleRow'		, width: '6%',	colKey: 'omsUploadconf.titleRow' },
		                 { field: 'dataRow'			, width: '7%',	colKey: 'omsUploadconf.dataRow' },
		                 { field: 'siteOrderId'		, width: '15%',	colKey: 'c.oms.uploadconf.siteOrderId' },
		                 { field: 'saleproductId1'	, width: '8%',	colKey: 'c.oms.uploadconf.saleproductId1' },
		                 { field: 'saleproductId2'	, width: '8%',	colKey: 'c.oms.uploadconf.saleproductId2' },
		                 { field: 'saleproductId3'	, width: '8%',	colKey: 'c.oms.uploadconf.saleproductId3' },
		                 { field: 'saleproductId4'	, width: '8%',	colKey: 'c.oms.uploadconf.saleproductId4' },
		                 { field: 'saleproductId5'	, width: '8%',	colKey: 'c.oms.uploadconf.saleproductId5' },
		                 { field: 'productName'		, width: '15%',	colKey: 'c.oms.uploadconf.productName' },
		                 { field: 'salePrice'		, width: '8%',	colKey: 'c.oms.uploadconf.salePrice', cellClass: 'alignR', cellFilter: 'number' },
		                 { field: 'orderQty'		, width: '5%',	colKey: 'c.oms.uploadconf.orderQty', cellClass: 'alignR', cellFilter: 'number' },
		                 { field: 'zipCd'			, width: '8%',	colKey: 'omsUploadconf.zipCd' },
		                 { field: 'address1'		, width: '20%',	colKey: 'omsUploadconf.address1' },
		                 { field: 'address2'		, width: '10%',	colKey: 'omsUploadconf.address2' },
		                 { field: 'phone1'			, width: '10%',	colKey: 'c.oms.uploadconf.phone1' },
		                 { field: 'phone2'			, width: '10%',	colKey: 'c.oms.uploadconf.phone2' },
		                 { field: 'name'			, width: '10%',	colKey: 'c.oms.uploadconf.name' },
		                 { field: 'note'			, width: '10%',	colKey: 'c.oms.uploadconf.note' },
		                 { field: 'currencyCd'		, width: '7%',	colKey: 'omsUploadconf.currencyCd' },
		                 { field: 'currencyPrice'	, width: '8%',	colKey: 'omsUploadconf.currencyPrice' },
		                 { field: 'lpNo'			, width: '12%',	colKey: 'c.oms.uploadconf.lpNo' },
		                 { field: 'localDeliveryYn'	, width: '5%',	colKey: 'c.oms.uploadconf.bondYn' }
		             ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_uploadexcel",	//mandatory
			url :  '/api/oms/uploadexcel',  //mandatory
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

	//listen for the file selected event
	$scope.files = [];
	$scope.$on('fileSelected', function(event, args) {
//		alert(args.file.name);
		$scope.$apply(function() {
			//add the file object to the scope's files collection
			if (!/(\.xlsx|\.xls)$/i.test(args.file.name)) {
				alert('엑셀 파일이 아닙니다.');
			} else {
				$scope.files.length = 0;
				$scope.files.push(args.file);
			}
		});
	});

	//파일경로 지우기
	this.eraser = function() {
		$scope.filePath = '';
		$scope.files.length = 0;
	}

	// 파일 업로드
	this.uploadExcel = function() {
		//Grid 초기화
		$scope.myGrid.initGrid();
		console.log("file: ", $scope.files[0]);
		if ($scope.files[0] != null) {
			//Grid 초기화
			$scope.myGrid.initGrid();

			var file = $scope.files[0];
			var url = Rest.context.path + '/api/oms/uploadexcel/bulk/' + $scope.search.siteId;
			commonService.uploadFileToUrl(file, null, url, function(response) {
//				console.log(response);
				var result = JSON.parse(response);
//				console.log(result);
//				$scope.result = result;
				$scope.totalCnt = result.totalCnt;
//				$scope.successCnt = result.successCnt;
//				$scope.failCnt = result.failCnt;
//				$scope.excelPath = result.excelPath;
				$scope.resultList = result.resultList;

				common.safeApply($scope);

				if (common.isEmpty(result.resultList)) {
					if (result.totalCnt == 0) {
						alert('등록된 주문업로드설정이 없습니다.');
						return;
					} else if (result.totalCnt < 0) {
						alert('엑셀업로드 중 오류가 발생했습니다.');
						return;
					}
				}

				var data = result.resultList;
//				console.log("data: ", data);
				//판매단가 체크: 소숫점 존재여부
				var check = false;
				for (var k = 0; k < data.length; k++) {
					var salePrice = data[k].salePrice;
					if (salePrice.toString().indexOf(".") != -1) {
						check = true;
						break;
					}
				}
				if (check) {
					alert('판매단가에 소숫점이 존재합니다');
					return;
				}

				alert('업로드를 완료하였습니다.');
				
				for (var i = data.length-1; i >= 0; i--) {
					$scope.myGrid.addRow({
						siteName		: data[i].siteName,
						siteId			: data[i].siteId,
						titleRow		: data[i].titleRow,
						dataRow			: data[i].dataRow,
						siteOrderId		: data[i].siteOrderId,
						saleproductId1	: data[i].saleproductId1,
						saleproductId2	: data[i].saleproductId2,
						saleproductId3	: data[i].saleproductId3,
						saleproductId4	: data[i].saleproductId4,
						saleproductId5	: data[i].saleproductId5,
						salePrice		: data[i].salePrice,
						orderQty		: data[i].orderQty,
						zipCd			: data[i].zipCd,
						address1		: data[i].address1,
						address2		: data[i].address2,
						phone1			: data[i].phone1,
						phone2			: data[i].phone2,
						name			: data[i].name,
						note			: data[i].note,
						currencyCd		: data[i].currencyCd,
						currencyPrice	: data[i].currencyPrice,
						lpNo			: data[i].lpNo,
						localDeliveryYn : data[i].localDeliveryYn
					});
				}
				$scope.grid_uploadexcel.totalItems = result.totalCnt;
			});
		} else {
			alert($scope.MESSAGES["common.search.commbo.default"]);
		}
	}

	//선택된 Grid 저장
	this.saveExcelOrder = function() {
		var dataList = $scope.myGrid.getSelectedRows();
//		console.log("selected Row: ", dataList);
		if (dataList.length > 0) {
			uploadExcelService.insertExcelOrder(dataList, function(response) {
//				console.log("Insert Result: ", response);
				if (response.content == 'Y') {
					alert($scope.MESSAGES["oms.uploadexcel.insert.success"]);
					$scope.deleteGridRow();
				} else {
					alert(response.content);
				}
			});
		} else {
			alert($scope.MESSAGES["common.search.commbo.default"]);
			return;
		}
	}

	//정상 저장된 data 그리드에서 제거
	$scope.deleteGridRow = function(dataList) {
//		console.log("grid: ", $scope.grid_uploadexcel);
		var gridData = $scope.grid_uploadexcel.data;
		var selectedRows = $scope.myGrid.getSelectedRows();

		$scope.grid_uploadexcel.gridApi.rowEdit.setRowsClean(selectedRows);

		//그리드 행 삭제
		for (var i = gridData.length - 1; i >= 0; i--) {
			if (gridData[i].checked) {
				gridData.splice(i, 1);
				$scope.grid_uploadexcel.totalItems--;
			}
		}
	}
});
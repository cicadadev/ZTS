(function(){
	
	// message init
	Constants.message_keys = [];
	
	var shippingProcessApp = angular.module('shippingProcessApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule']);
	
	shippingProcessApp.controller('listCtrl', function($window, $scope, $filter, $templateCache, $timeout, logisticsService, commonService, gridService, uiGridValidateService, templateService) {
		
		$templateCache.put('custom/checkBoxHeader', templateService.checkBoxHeader);
		
		var columnDefs = [
	                  		  { field : 'checked'                                              ,width : '40' ,cellTemplate : templateService.checkBox       
                  			    ,headerCellTemplate : "custom/checkBoxHeader"  
							  },
			                  { field : 'logisticsInoutNo'                                     ,width : '100', colKey : 'c.oms.logistics.delivery.No'             ,cellClass : 'alignC' },    // 배송번호
		                	  { field : 'insDt'                                                ,width : '150', colKey : 'c.oms.logistics.delivery.ins_dt'         ,cellClass : 'alignC' },    // 배송승인일시
		                	  { field : 'omsOrderproduct.orderDate'                            ,width : '100', colKey : 'c.oms.logistics.order.date'              ,cellClass : 'alignC' },    // 주문일
		                	  { field : 'omsOrderproduct.omsOrder.orderTypeName'               ,width : '120', colKey : 'c.oms.logistics.order_type'              ,cellClass : 'alignC' },    // 주문구분
		                	  { field : 'omsOrderproduct.siteName'                             ,width : '100', colKey : 'c.oms.logistics.site'                    ,cellClass : 'alignC' },    // 사이트
		                	  { field : 'omsOrderproduct.omsOrder.name1'                       ,width : '120', colKey : 'c.oms.logistics.orderer_name'            ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsOrder.phone2'                      ,width : '150', colKey : 'c.oms.logistics.orderer_mobile'          ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.name1'             ,width : '120', colKey : 'c.oms.logistics.receiver_name'           ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.phone1'            ,width : '150', colKey : 'c.oms.logistics.receiver_tel'            ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.phone2'            ,width : '150', colKey : 'c.oms.logistics.receiver_mobile'         ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.zipCd'             ,width : '120', colKey : 'c.oms.logistics.zipCode'                 ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.deliveryAddress'   ,width : '300', colKey : 'c.oms.logistics.address'                 						}, 
		                	  { field : 'invoiceNo'                                            ,width : '120', colKey : 'c.oms.logistics.invoiceNo'               ,cellClass : 'alignC' },    // 운송장번호
		                	  { field : 'omsOrderproduct.productId'                            ,width : '120', colKey : 'c.oms.logistics.product_id'              ,cellClass : 'alignC' },    // 상품번호
		                	  { field : 'omsOrderproduct.productName'                          ,width : '300', colKey : 'c.oms.logistics.product_name'            						},    // 상품명
		                	  { field : 'omsOrderproduct.saleproductName'                      ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'        						},    // 단품명
		                	  { field : 'omsOrderproduct.erpSaleproductId'                     ,width : '120', colKey : 'c.oms.logistics.erp.barcode'             ,cellClass : 'alignC' },    // ERP바코드
		                	  { field : 'omsOrderproduct.dualWrapYn'                           ,width : '120', colKey : 'c.oms.logistics.invoice.double.wrap.yn'  ,cellClass : 'alignC' },    // 이중포장여부
		                	  { field : 'outReserveQty'                                        ,width : '100', colKey : 'c.oms.logistics.delivery_quantity'       ,cellClass : 'alignR' },    // 배송수량
		                	  { field : 'outQty'                                               ,width : '100', colKey : 'c.oms.logistics.real.delivery_quantity'  ,cellClass : 'alignR'  
	                		    ,enableCellEdit : true                                         ,vKey:"omsLogistics.outQty"                                        ,type : 'number'
                		    	,validators : {compared : true, maxLength : 100}
                		      },                                                                                                                                                              // 실배송수량
		                	  { field : 'cancelDeliveryReasonCd'                               ,width : '150', colKey : 'c.oms.logistics.shipping.cancel_reason'  ,cellClass : 'alignC' 
		                		,cellFilter : 'cancelDeliveryReasonFilter'                     ,dropdownCodeEditor : 'CANCEL_DELIVERY_REASON_CD'                  ,enableCellEdit : true
		                		,vKey:"omsLogistics.cancelDeliveryReasonCd"                    ,validators : {required2 : true}
		                	  },                                                                                                                                                              // 미출고 사유
		                	  { field : 'orderId'                                              ,width : '120', colKey : 'c.oms.logistics.order_id'                ,cellClass : 'alignC' },    // 주문번호
		                	  { field : 'lpNo'                                                 ,width : '150', colKey : 'c.oms.logistics.lp_no'                   ,cellClass : 'alignC' }     // LP_NO
                  	    ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_shipping',
			url : '/api/oms/logistics/shipping/process',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
		 		pagination : false,
		 		checkBoxEnable : false,
				isRowSelectable : function(row) {
					var isRowSelectable = true;
					if (row.entity.orderId == null) {
						isRowSelectable = false;
					}
					return isRowSelectable;
				},
				virtualizationThreshold : 50
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchShippingList = function() {
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
			$scope.totalOrders = 0;
			$scope.totalProducts = 0;
			$scope.clickNum = 1;
		};
		
		logisticsService.getAllSiteList(function(response) {
			$scope.siteList = response;
		});
		
		this.shippingConfirm = function(){
			var selectedRows = $scope.myGrid.getSelectedRows();
			var rows = $scope.myGrid.getData();
			
			if(!$scope.myGrid.isChecked()){
				return false;
			}
			
			if (!this.validate(selectedRows)) {
				return false;
			}
			
			if(selectedRows.length != rows.length){
				for(var i = 0; i < selectedRows.length; i++){
					var orderId = selectedRows[i].orderId;
					var orderProductNo = selectedRows[i].orderProductNo;
					var logisticsInoutNo = selectedRows[i].logisticsInoutNo;
					
					for(var j = 0; j < rows.length; j++){
						if(rows[j].orderId == orderId && rows[j].orderProductNo == orderProductNo && rows[j].logisticsInoutNo == logisticsInoutNo && !rows[j].checked){
							alert('선물포장비가 선택되지 않았습니다.');
							return false;
						}
					}
				}
			}
			
			if (window.confirm('정말로 저장하시겠습니까 ?')) {
				logisticsService.shippingConfirm(selectedRows, this.callBack);
			} else {
				return false;
			}
		};
		
		this.callBack = function(response) {
			if (response.success) {
				$scope.myGrid.loadGridData();
				$scope.countOrderData();
				alert(response.resultMessage);
			} else {
				alert(response.resultMessage);
			}
		}
		
		this.bulk = {
			upload : {
				excel : function() {
					var winName = 'shipping Bulk Upload';
					var winURL = Rest.context.path + "/oms/logistics/shipping/popup/bulkupload";

					popupwindow(winURL, winName, 1100, 400);
				}
			}
		}
		
		$scope.checkBoxAll = function(checked) {
			var grid = $scope.grid_shipping.gridApi.grid;
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = checked;
				}
			}
			if(!checked){
				$scope.clickNum = 1;
			}
			$scope.countOrderData();
		}
		
		$scope.checkOneHundred = function() {
			var grid = $scope.grid_shipping.gridApi.grid;
			var rows = grid.rows;
			
			if(common.isEmpty(rows)){
				return false;
			}
			
			var selectRatio = 100;
			var selectScope = selectRatio * $scope.clickNum;
			var selectNum = selectScope > rows.length ? rows.length : selectScope;
			for (var i = 0; i < selectNum; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = true;
				}
			}
			$scope.clickNum++;
			$scope.countOrderData();
			
			if(selectScope > rows.length){
				$timeout(function() {
					alert('모두 선택하였습니다.');
				}, 100);
			}
		}
		
		$scope.countOrderData = function(){
			var list = $scope.myGrid.getSelectedRows();
			var orderIds = [];
			
			for (var i = 0; i < list.length; i++) {
				if(orderIds.indexOf(list[i].orderId) < 0){
					orderIds.push(list[i].orderId);
				}
			}
			$scope.totalOrders = orderIds.length;
			$scope.totalProducts = list.length;
		}
		
		$scope.selectCheck = function(grid, rowEntity){
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				if (rowEntity.orderId == rows[i].entity.orderId && rowEntity.orderProductNo == rows[i].entity.orderProductNo && rowEntity.logisticsInoutNo == rows[i].entity.logisticsInoutNo) {// find same product
					if(rowEntity.checked){
						rows[i].entity.checked = true;
					}else{
						rows[i].entity.checked = false;
					}
				}
			}
			$scope.countOrderData();
		}
		
		// 그리드 유효성 체크
		this.validate = function(dirtyRows) {
			
			var isValidGrid = true;

			for (var i = 0; i < dirtyRows.length; i++) {
				var rowEntity = dirtyRows[i];
				var outReserveQty = rowEntity.outReserveQty;
				var outQty = rowEntity.outQty;
				
				for (var j = 0; j < columnDefs.length; j++) {

					var colDef = columnDefs[j];
					var validator = colDef.validators;
					var isEmpty = false;
					
					if (validator != null && angular.isDefined(validator)) {
						var cellValue = '';
						var listIdx = colDef['field'].indexOf('[0].');
						
						if (listIdx > -1) {
							var field = colDef['field'].substring(listIdx + 4);
							var paramArray = colDef['field'].substring(0, listIdx);
							cellValue = rowEntity[paramArray][0][field];
						} else {
							cellValue = rowEntity[colDef['field']];
						}

						if((cellValue == '' || cellValue == null || cellValue == undefined || ( cellValue != null && typeof cellValue == "object" && !Object.keys(cellValue).length )) && cellValue !== 0){
							isEmpty = true;
						}
						
						if (validator.required && isEmpty) {// 필수값 체크하여 invalid 설정
							uiGridValidateService.setInvalid(dirtyRows[i], colDef);
						}
						
						if(validator.required2 && !common.isEmpty(outQty)){// 비교값 체크하여 invalid 설정
							if(Number(outReserveQty) > Number(outQty)){
								if(isEmpty){
									uiGridValidateService.setInvalid(dirtyRows[i], colDef);
								}else{
									uiGridValidateService.setValid(dirtyRows[i], colDef);
								}
							}else if(Number(outQty) >= Number(outReserveQty)){
								if(!isEmpty){
									uiGridValidateService.setInvalid(dirtyRows[i], colDef);
								}else{
									uiGridValidateService.setValid(dirtyRows[i], colDef);
								}
							}
						}
						
						if (validator.compared && isEmpty) {// 비교값 체크하여 invalid 설정
							uiGridValidateService.setInvalid(dirtyRows[i], colDef);
						}else if(validator.compared && !isEmpty){
							if(Number(outQty) > Number(outReserveQty)){
								uiGridValidateService.setInvalid(dirtyRows[i], colDef);
							}
						}
						
						var isInvalid = uiGridValidateService.isInvalid(dirtyRows[i], colDef);

						if (isInvalid) {
							isValidGrid = false;
						}
					}
				}
			}

			if (!isValidGrid) {
				alert('그리드에 유효하지 않은 값이 존재합니다.');
				return false;
			}

			return true;
		};
		
	}).controller("bulkUploadCtrl", function($window, $scope, logisticsService, commonService) {
		
		var pScope = $window.opener.$scope; // 부모의 scope
		var upPath = 'shipping';

		$scope.files = [];
		// listen for the file selected event
		$scope.$on("fileSelected", function(event, args) {
			$scope.$apply(function() {
				$scope.files.push(args.file);
			});
		});

		// 팝업 닫기
		this.close = function() {
			$window.close();
		}

		// 파일 경로 지우기
		this.eraser = function() {
			$scope.filePath = "";
		}

		// 파일 일괄 업로드
		uploadfile = function(file, url){
			commonService.uploadFileToUrl(file, null, url, function(response) {
				var result = JSON.parse(response);
				$scope.totalCnt = result.totalCnt;
				$scope.successCnt = result.successCnt;
				$scope.failCnt = result.failCnt;
				$scope.excelPath = result.excelPath;
				// 업로드한 엑셀 그리드로 표시
				$scope.setUploadDataToGrid(result);
				
				if ($scope.$$phase != '$apply' && $scope.$$phase != '$digest') {
					$scope.$apply();
				}
			});
		}
		
		this.checkExcel = function() {
			var file = $scope.files[0];
			if (file != null) {
				if (!/(\.xlsx|\.xls)$/i.test(file.name)) {
					$scope.filePath = "";
					alert("엑셀 파일이 아닙니다.");
					return false;
				}
				var url = Rest.context.path + '/api/oms/logistics/bulk/' + upPath;
				uploadfile(file, url);
			}
		}

		// 오류데이터 다운로드
		this.downFailDataExcel = function() {
			if ($scope.excelPath) {
				$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + $scope.excelPath;
			}
		}
		
		$scope.setUploadDataToGrid = function(result) {
			if(result.successCnt > 0){
				pScope.myGrid.setData(result.successList, false);
				pScope.grid_shipping.totalItems = result.successCnt;
				pScope.checkBoxAll(true);
				
				alert('총 ' + result.successCnt + '건을 성공적으로 업로드 완료하였습니다.');
			}else{
				alert('유효한 업로드 데이터가 없습니다.');
			}
		}
		
	}).filter('cancelDeliveryReasonFilter', function() { 
		
		var comboHash = {
			    'CANCEL_DELIVERY_REASON_CD.NO_STOCK': '재고부족',
			    'CANCEL_DELIVERY_REASON_CD.CANCEL': '출고전취소요청',
			    'CANCEL_DELIVERY_REASON_CD.IF_RESEND': '인터페이스재전송',
			    'CANCEL_DELIVERY_REASON_CD.ETC': '기타'
			};
		
		return function(input) { return !input ? '' :  comboHash[input]; };
	});
})();

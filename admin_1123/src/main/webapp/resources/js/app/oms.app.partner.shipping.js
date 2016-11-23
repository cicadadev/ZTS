(function(){
	
	// message init
	Constants.message_keys = [];
	
	var partnerShippingApp = angular.module('partnerShippingApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	partnerShippingApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService, uiGridValidateService) {
		
		// PO일경우 업체ID
		var poBusinessId = global.session.businessId;
		$scope.poBusinessId = poBusinessId;
		
		var columnDefs = [
	                  		  { field : 'logisticsInoutNo'                                       ,width : '100', colKey : 'c.oms.logistics.delivery.No'                ,cellClass : 'alignC' },    // 배송번호
	                  		  { field : 'insDt'                                                  ,width : '120', colKey : 'c.oms.logistics.delivery.ins_dt'            ,cellClass : 'alignC' },    // 배송승인일시  
	                  		  { field : 'omsOrderproduct.omsOrder.name1'                         ,width : '100', colKey : 'c.oms.logistics.orderer_name'               ,cellClass : 'alignC' }, 
	                  		  { field : 'omsOrderproduct.omsOrder.phone2'                        ,width : '130', colKey : 'c.oms.logistics.orderer_mobile'             ,cellClass : 'alignC' }, 
	                  		  { field : 'omsOrderproduct.omsDeliveryaddress.name1'               ,width : '100', colKey : 'c.oms.logistics.receiver_name'              ,cellClass : 'alignC' }, 
	                  		  { field : 'omsOrderproduct.omsDeliveryaddress.phone1'              ,width : '130', colKey : 'c.oms.logistics.receiver_tel'               ,cellClass : 'alignC' }, 
	                  		  { field : 'omsOrderproduct.omsDeliveryaddress.phone2'              ,width : '130', colKey : 'c.oms.logistics.receiver_mobile'            ,cellClass : 'alignC' }, 
	                  		  { field : 'omsOrderproduct.omsDeliveryaddress.zipCd'               ,width : '100', colKey : 'c.oms.logistics.zipCode'                    ,cellClass : 'alignC' }, 
	                  		  { field : 'omsOrderproduct.omsDeliveryaddress.deliveryAddress'     ,width : '300', colKey : 'c.oms.logistics.address'                                          }, 
	                  		  { field : 'invoiceNo'                                              ,width : '120', colKey : 'c.oms.logistics.invoiceNo'                  ,cellClass : 'alignC',     
	                  			enableCellEdit : true,     validators : {required : true}       ,vKey : "omsLogistics.invoiceNo" 
	                  		  },                                                                                                                                                                    // 운송장번호
	                  		  { field : 'deliveryServiceCd'                                      ,width : '120', colKey : 'c.oms.logistics.delivery.service.biz.name'  ,cellClass : 'alignC',    
                  			    enableCellEdit : true,     dropdownCodeEditor : 'DELIVERY_SERVICE_CD' , cellFilter : 'deliveryServiceFilter', validators : {required : true},
                  			    vKey : "omsLogistics.deliveryServiceCd"
              			      },                                                                                                                                                                    // 배송업체
			                  { field : 'omsOrderproduct.businessName'                           ,width : '200', colKey : 'c.oms.logistics.business_name'              ,cellClass : 'alignC' },     // 공급업체
			                  { field : 'omsOrderproduct.productId'                              ,width : '120', colKey : 'c.oms.logistics.product_id'                 ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.productName'                            ,width : '300', colKey : 'c.oms.logistics.product_name'                                     }, 
			                  { field : 'omsOrderproduct.saleproductName'                        ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'                                 }, 
			                  { field : 'outReserveQty'                                          ,width : '100', colKey : 'c.oms.logistics.delivery_quantity'          ,cellClass : 'alignR' },     // 배송수량 
			                  { field : 'outQty'                                                 ,width : '100', colKey : 'c.oms.logistics.real.delivery_quantity'     ,cellClass : 'alignR'
			                	,enableCellEdit : true,     validators : {compared : true, maxLength : 100}       ,vKey : "omsLogistics.outQty"                       ,type : 'number'
		                	  },                                                                                                                                                                    // 실배송수량 
			                  { field : 'cancelDeliveryReasonCd'                                 ,width : '150', colKey : 'c.oms.logistics.shipping.cancel_reason'     ,cellClass : 'alignC',    
		                		enableCellEdit : true,     dropdownCodeEditor : 'CANCEL_DELIVERY_REASON_CD' , cellFilter : 'cancelDeliveryReasonFilter',     validators : {required2 : true},
		                		vKey : "omsLogistics.cancelDeliveryReasonCd",			
			                  },                                                                                                                                                                    // 미출고사유
			                  { field : 'orderId'                                                ,width : '120', colKey : 'c.oms.logistics.order_id'                   ,cellClass : 'alignC' }      // 주문번호
	                  	 ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_partnerShipping',
			url : '/api/oms/logistics/partner/shipping',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
		};
		
		this.searchOrderProduct = function() {
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.deliveryApprovalOptions = [
		                                	{key : 'Y', name : '승인'},
		                                	{key : 'N', name : '미승인'}
		                               ];
		
		this.shippingConfirm = function(){
			var selectedRows = $scope.myGrid.getSelectedRows();
			
			if(!$scope.myGrid.isChecked()){
				return false;
			}
			
			if (!this.validate(selectedRows)) {
				return false;
			}
			
			if (window.confirm("정말로 저장하시겠습니까 ?")) {
				logisticsService.shippingConfirm(selectedRows, this.callBack);
			} else {
				return false;
			}
		};
		
		this.callBack = function(response) {
			if (response.success) {
				if($scope.grid_partnerShipping.allCheck == true){
					$scope.grid_partnerShipping.allCheck = false;
					$scope.allCheck = false;
				}
				alert(response.resultMessage);
				$scope.myGrid.loadGridData();
			} else {
				alert(response.resultMessage);
			}
		}
		
		this.openPopup = function(kindOf) {
			if(kindOf == 'business') {
				commonPopupService.businessPopup($scope,"callback_business",false);
				$scope.callback_business = function(data){
					$scope.search.businessId = data[0].businessId;
					$scope.search.businessName = data[0].name;
					$scope.$apply();		
				}
			}
		}
		
		this.eraser = function() {
			$scope.filePath = "";
		}
		
		this.eraser = function(val1, val2) {
			$scope.search[val1] = "";
			
			if(angular.isDefined(val2)) {
				$scope.search[val2] = "";
			}
		}
		
		this.bulk = {
			upload : {
				excel : function() {
					var winName = 'shipping Bulk Upload';
					var winURL = Rest.context.path + "/oms/logistics/partner/popup/bulkupload";

					popupwindow(winURL, winName, 1100, 400);
				}
			}
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
					
					if (validator != null && angular.isDefined(validator)) {
						var cellValue = '';
						var listIdx = colDef['field'].indexOf('[0].');
						var isEmpty = false;
						
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
		
		$scope.triggerClick = function () {
			$timeout(function() {
				var el = angular.element('.check_header > input').get(0); 
				angular.element(el).trigger('click');
			}, 0);
		};
		
	}).controller("bulkUploadCtrl", function($window, $scope, logisticsService, commonService) {
		
		var pScope = $window.opener.$scope; // 부모의 scope
		var upPath = 'partnerShipping';

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
				
				pScope.myGrid.setData(result.successList, false);
				pScope.grid_shipping.totalItems = result.successCnt;
				alert('업로드를 완료하였습니다.');
				
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
		
	}).filter('deliveryServiceFilter', function() { 
		
		var comboHash = {
			    'DELIVERY_SERVICE_CD.01': '우체국',
			    'DELIVERY_SERVICE_CD.04': 'CJ대한통운',
			    'DELIVERY_SERVICE_CD.05': '한진택배',
			    'DELIVERY_SERVICE_CD.06': '로젠택배',
			    'DELIVERY_SERVICE_CD.08': '현대택배',
			    'DELIVERY_SERVICE_CD.10': 'KGB택배',
			    'DELIVERY_SERVICE_CD.11': '일양로지스',
			    'DELIVERY_SERVICE_CD.12': 'EMS',
			    'DELIVERY_SERVICE_CD.13': 'DHL',
			    'DELIVERY_SERVICE_CD.15': 'GTX',
			    'DELIVERY_SERVICE_CD.17': '천일택배',
			    'DELIVERY_SERVICE_CD.22': '대신택배',
			    'DELIVERY_SERVICE_CD.23': '경동택배',
			    'DELIVERY_SERVICE_CD.32': '합동택배',
			    'DELIVERY_SERVICE_CD.39': 'KG로지스',
			    'DELIVERY_SERVICE_CD.ETC': '기타'
			};
		
		return function(input) { return !input ? '' :  comboHash[input]; };
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

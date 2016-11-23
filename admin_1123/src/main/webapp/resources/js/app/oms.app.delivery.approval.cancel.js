(function(){
	
	// message init
	Constants.message_keys = [];
	
	var deliveryApprovalApp = angular.module('deliveryApprovalCancelApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	deliveryApprovalApp.controller('listCtrl', function($window, $scope, $filter, $templateCache, $timeout, logisticsService, commonService, gridService, commonPopupService, uiGridValidateService, templateService) {
		
		// PO일경우 업체ID
		var poBusinessId = global.session.businessId;
		$scope.poBusinessId = poBusinessId;
		// 유저의 권한ID
		var roleId = global.session.roleId;
		$scope.roleId = roleId;
		
		$templateCache.put('custom/checkBoxHeader', templateService.checkBoxHeader);
		
		var columnDefs = [
		   					  { field : 'checked'                                                 ,width : '40' , cellTemplate : templateService.checkBox       
								,headerCellTemplate : "custom/checkBoxHeader"  
							  },	
			                  { field : 'logisticsInoutNo'                                        ,width : '100', colKey : 'c.oms.logistics.delivery.No'         ,cellClass : 'alignC' }, 
		                	  { field : 'insDt'                                                   ,width : '150', colKey : 'c.oms.logistics.delivery.ins_dt'     ,cellClass : 'alignC' },    // 배송승인일시
		                	  { field : 'deliveryCancelReasonCd'                                  ,width : '150', colKey : 'c.oms.logistics.cancel_reason'       ,cellClass : 'alignC', 
		                		cellFilter : 'cancelReasonFilter'                                 ,dropdownCodeEditor : 'DELIVERY_CANCEL_REASON_CD'              ,enableCellEdit : true,
		                		validators : {required : true}             
		                	  }, 
		                	  { field : 'omsOrderproduct.orderId'                                 ,width : '120', colKey : 'c.oms.logistics.order_id'            ,cellClass : 'alignC' },    // 주문번호
		                	  { field : 'omsOrderproduct.omsOrder.orderTypeName'                  ,width : '120', colKey : 'c.oms.logistics.order_type'          ,cellClass : 'alignC' },    // 주문구분
		                	  { field : 'omsOrderproduct.siteName'                                ,width : '100', colKey : 'c.oms.logistics.site'                ,cellClass : 'alignC' },    // 사이트
		                	  { field : 'omsOrderproduct.omsOrder.name1'                          ,width : '120', colKey : 'c.oms.logistics.orderer_name'        ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsOrder.phone2'                         ,width : '150', colKey : 'c.oms.logistics.orderer_mobile'      ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.name1'                ,width : '120', colKey : 'c.oms.logistics.receiver_name'       ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.phone1'               ,width : '150', colKey : 'c.oms.logistics.receiver_tel'        ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.phone2'               ,width : '150', colKey : 'c.oms.logistics.receiver_mobile'     ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.zipCd'                ,width : '120', colKey : 'c.oms.logistics.zipCode'             ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.deliveryAddress'      ,width : '300', colKey : 'c.oms.logistics.address'                                   }, 
		                	  { field : 'omsOrderproduct.businessName'                            ,width : '200', colKey : 'c.oms.logistics.business_name'       ,cellClass : 'alignC' },    // 공급업체
		                	  { field : 'omsOrderproduct.productId'                               ,width : '120', colKey : 'c.oms.logistics.product_id'          ,cellClass : 'alignC' },    // 상품번호
		                	  { field : 'omsOrderproduct.productName'                             ,width : '300', colKey : 'c.oms.logistics.product_name'                              },    // 상품명
		                	  { field : 'omsOrderproduct.saleproductName'                         ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'                          },    // 단품명
		                	  { field : 'omsOrderproduct.deliveryMethod'                          ,width : '120', colKey : 'c.oms.logistics.delivery_method'     ,cellClass : 'alignC' },    // 배송방법
		                	  { field : 'outReserveQty'                                           ,width : '100', colKey : 'c.oms.logistics.delivery_quantity'   ,cellClass : 'alignR' },    // 배송수량
		                	  { field : 'omsOrderproduct.omsOrder.siteOrderId'                    ,width : '150', colKey : 'c.oms.logistics.site.order_id'       ,cellClass : 'alignC' }     // 제휴주문번호
	                  	 ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_cancelApproval',
			url : '/api/oms/logistics/cancel',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				pagination : false,
				checkBoxEnable : false,
				virtualizationThreshold : 50
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchOrderProduct = function() {
			if(!common.isEmpty(poBusinessId)){
				$scope.search.deliveryMethod = 'CONSIGN';
			}else if(common.isEmpty(poBusinessId) && roleId == '5'){
				$scope.search.deliveryMethod = 'PURCHASE';
			}
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
			$scope.deliveryCancelReasons=[];
			$scope.totalOrders = 0;
			$scope.totalProducts = 0;
		};
		
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
		
		this.cancel = function(){
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
						if(rows[j].logisticsInoutNo == logisticsInoutNo && rows[j].orderId == orderId && rows[j].orderProductNo == orderProductNo && !rows[j].checked){
							alert('동일한 상품이 모두 선택되지 않았습니다.');
							return false;
						}
					}
				}
			}
			
			if (window.confirm("정말로 승인취소를 하시겠습니까?")) {
				logisticsService.cancel(selectedRows, this.callBack);
			} else {
				return false;
			}
		}
		
		commonService.getCodeList({cdGroupCd : 'DELIVERY_CANCEL_REASON_CD'}).then(function(data) {
			$scope.deliveryCancelReasons = data;
		});
		
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
					var winName = 'approvalCancel Bulk Upload';
					var winURL = Rest.context.path + "/oms/logistics/delivery/popup/bulkupload";

					popupwindow(winURL, winName, 1100, 400);
				}
			}
		}
		
		$scope.checkBoxAll = function(checked) {
			var grid = $scope.grid_cancelApproval.gridApi.grid;
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
				if (rowEntity.logisticsInoutNo == rows[i].entity.logisticsInoutNo && rowEntity.orderId == rows[i].entity.orderId && rowEntity.orderProductNo == rows[i].entity.orderProductNo) {// find same product
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
						
						var isInvalid = uiGridValidateService.isInvalid(dirtyRows[i], colDef);

						if (isInvalid) {
							isValidGrid = false;
						}
					}
				}
			}

			if (!isValidGrid) {
				alert('배송승인취소 사유를 입력하세요.');
				return false;
			}

			return true;
		};
		
	}).controller("bulkUploadCtrl", function($window, $scope, logisticsService, commonService) {
		
		var pScope = $window.opener.$scope; // 부모의 scope
		var upPath = 'cancelApproval';

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
				pScope.grid_cancelApproval.totalItems = result.successCnt;
				pScope.checkBoxAll(true);
				
				alert('총 ' + result.successCnt + '건을 성공적으로 업로드 완료하였습니다.');
			}else{
				alert('유효한 업로드 데이터가 없습니다.');
			}
		}
		
	}).filter('cancelReasonFilter', function() { 
		
		var comboHash = {
			    'DELIVERY_CANCEL_REASON_CD.REPROCESS': '배송승인재처리',
			    'DELIVERY_CANCEL_REASON_CD.BANKRUPT': '수송사부도',
			    'DELIVERY_CANCEL_REASON_CD.SOLDOUT': '품절',
			    'DELIVERY_CANCEL_REASON_CD.CHANGE_HEART': '고객변심',
			    'DELIVERY_CANCEL_REASON_CD.CHANGE_OPTION': '옵션변경',
			    'DELIVERY_CANCEL_REASON_CD.ETC': '기타'
			};
		return function(input) { return !input ? '' :  comboHash[input]; };
	});
	
})();

(function(){
	
	// message init
	Constants.message_keys = [];
	
	var returnListApp = angular.module('returnListApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	returnListApp.controller('listCtrl', function($window, $scope, $filter, $templateCache, logisticsService, commonService, gridService, commonPopupService, uiGridValidateService, templateService) {
		
		$templateCache.put('custom/checkBoxHeader', templateService.checkBoxHeader);
		
		var columnDefs = [
							  { field : 'checked'                                                     ,width : '40', cellTemplate : templateService.checkBox,
								headerCellTemplate : "custom/checkBoxHeader"  
							  },	
			                  { field : 'omsOrderproduct.orderDate'                                   ,width : '100', colKey : 'c.oms.logistics.datetime_order'             ,cellClass : 'alignC' },    // 주문일시
			                  { field : 'orderId'                                                     ,width : '120', colKey : 'c.oms.logistics.order_id'                   ,cellClass : 'alignC' },    // 주문번호
		                	  { field : 'omsClaimproduct.omsClaim.acceptDt'                           ,width : '120', colKey : 'c.oms.claim.datetime_accept'                ,cellClass : 'alignC' },    // 클레임접수일
		                	  { field : 'claimNo'                                                     ,width : '100', colKey : 'c.oms.claim.id'                             ,cellClass : 'alignC' },    // 클레임번호
		                	  { field : 'omsOrderproduct.siteName'                                    ,width : '100', colKey : 'c.oms.logistics.site'                       ,cellClass : 'alignC' },    // 사이트
		                	  { field : 'omsOrderproduct.omsOrder.name1'                              ,width : '100', colKey : 'c.oms.logistics.orderer_name'               ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsOrder.phone2'                             ,width : '150', colKey : 'c.oms.logistics.orderer_mobile'             ,cellClass : 'alignC' },
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.returnName'        ,width : '100', colKey : 'c.oms.logistics.return.sender.name'         ,cellClass : 'alignC' },    // 발송인명
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.returnPhone1'      ,width : '150', colKey : 'c.oms.logistics.return.sender.phone'        ,cellClass : 'alignC' },    // 발송인전화번호
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.returnPhone2'      ,width : '150', colKey : 'c.oms.logistics.return.sender.mobile'       ,cellClass : 'alignC' },    // 발송인휴대폰번호
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.returnZipCd'       ,width : '100', colKey : 'c.oms.logistics.zipCode'                    ,cellClass : 'alignC' }, 
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.deliveryAddress'   ,width : '250', colKey : 'c.oms.logistics.address'                    ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.productId'                                   ,width : '100', colKey : 'c.oms.logistics.product_id'                 ,cellClass : 'alignC' },    // 상품번호
		                	  { field : 'omsOrderproduct.productName'                                 ,width : '300', colKey : 'c.oms.logistics.product_name'               ,cellClass : 'alignC' },    // 상품명
		                	  { field : 'omsOrderproduct.saleproductId'                               ,width : '100', colKey : 'c.pms.sendgoods.saleproductNo'              ,cellClass : 'alignC' },    // 단품번호
		                	  { field : 'omsOrderproduct.saleproductName'                             ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'           ,cellClass : 'alignC' },    // 단품명
		                	  { field : 'inReserveQty'                                                ,width : '100', colKey : 'c.oms.order.return_qty'                     ,cellClass : 'alignC' },    // 반품수량
		                	  { field : 'omsOrderproduct.locationId'                                  ,width : '100', colKey : 'c.oms.logistics.product_location_id'        ,cellClass : 'alignC' },    // 로케이션명
		                	  { field : 'logisticsStateName'                                          ,width : '100', colKey : 'c.oms.logistics.return.logisticsState'      ,cellClass : 'alignC' },    // 입고상태
		                	  { field : 'goodInQty'                                                   ,width : '100', colKey : 'c.oms.logistics.return.good.qty'            ,cellClass : 'alignC', 
		                		enableCellEdit : true,          validators : {compared2 : true, maxLength : 100}      ,vKey:"omsLogistics.goodInQty"                       ,type : 'number'
		                	  },                                                                                                                                                                        // 정상수량
		                	  { field : 'badInQty'                                                    ,width : '100', colKey : 'c.oms.logistics.return.bad.qty'             ,cellClass : 'alignC', 
		                		enableCellEdit : true,          validators : {compared2 : true, maxLength : 100}      ,vKey:"omsLogistics.badInQty"                        ,type : 'number' 
		                	  },                                                                                                                                                                        // 불량수량
		                	  { field : 'virtualInQty'                                                ,width : '100', colKey : 'c.oms.logistics.return.virtual.qty'         ,cellClass : 'alignC', 
		                		enableCellEdit : true,          validators : {compared2 : true, maxLength : 100}      ,vKey:"omsLogistics.virtualInQty"                    ,type : 'number'
		                	  },                                                                                                                                                                        // 가상수량
		                	  { field : 'omsOrderproduct.omsOrder.siteOrderId'                        ,width : '120', colKey : 'c.oms.order.site_order_id'                  ,cellClass : 'alignC' },    // 제휴주문아이디
		                	  { field : 'omsOrderproduct.lpNo'                                        ,width : '120', colKey : 'c.oms.logistics.lp_no'                      ,cellClass : 'alignC' }     // LP_NO
                  	    ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_return',
			url : '/api/oms/logistics/return',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : false,
				multiSelect : true,
				isRowSelectable : function(row) {
					var isRowSelectable = true;
					if (row.entity.logisticsStateCd == 'LOGISTICS_STATE_CD.RETURN') {
						isRowSelectable = false;
					}
					return isRowSelectable;
				},
				cellEditableCondition: function( $scope ) {
					return $scope.row.entity.logisticsStateCd != 'LOGISTICS_STATE_CD.RETURN'; 
				}
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
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
			$scope.countOrderData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		logisticsService.getAllSiteList(function(response) {
			$scope.siteList = response;
		});
		
		this.checkBoxAll = function(checked) {
			var grid = $scope.grid_return.gridApi.grid;
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = checked;
				}
			}
			$scope.countOrderData();
		}
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {saleTypeCd : 'SALE_TYPE_CD.PURCHASE'};
			$scope.totalOrders = 0;
			$scope.totalProducts = 0;
		};
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchReturnList = function() {
			$scope.myGrid.loadGridData();
		}

		this.returnConfirm = function(type){
			var selectedRows = $scope.myGrid.getSelectedRows();
			
			if(!$scope.myGrid.isChecked()){
				return false;
			}
			
			if (!this.validate(selectedRows)) {
				return false;
			}
			
			if (window.confirm("반품처리 하시겠습니까?")) {
				
				logisticsService.returnConfirm(selectedRows, this.callBack);
			} else {
				return false;
			}
		}
		
		this.callBack = function(response) {
			if (response.success) {
				if($scope.allCheck == true){
					$scope.allCheck = false;
				}
				$scope.myGrid.loadGridData();
				alert(response.resultMessage);
			} else {
				alert(response.resultMessage);
			}
		}
		
		// 그리드 유효성 체크
		this.validate = function(dirtyRows) {
			var isValidGrid = true;

			for (var i = 0; i < dirtyRows.length; i++) {
				var rowEntity = dirtyRows[i];
				var inReserveQty = rowEntity.inReserveQty;
				var goodInQty = rowEntity.goodInQty;
				var badInQty = rowEntity.badInQty;
				var virtualInQty = rowEntity.virtualInQty;
				var isValid = true;
				
				if(((goodInQty == '' || goodInQty == null || goodInQty == undefined || ( goodInQty != null && typeof goodInQty == "object" && !Object.keys(goodInQty).length )) && goodInQty !== 0)
					|| ((badInQty == '' || badInQty == null || badInQty == undefined || ( badInQty != null && typeof badInQty == "object" && !Object.keys(badInQty).length )) && badInQty !== 0)
					|| ((virtualInQty == '' || virtualInQty == null || virtualInQty == undefined || ( virtualInQty != null && typeof virtualInQty == "object" && !Object.keys(virtualInQty).length )) && virtualInQty !== 0)){
					isValid = false;
				}
				
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
						
						if(validator.compared2 && isEmpty){
							uiGridValidateService.setInvalid(dirtyRows[i], colDef);
						}else if(validator.compared2 && isValid && !isEmpty){// 비교값 체크하여 invalid 설정
							var inQty = goodInQty + badInQty + virtualInQty; 
							
							if(Number(inQty) == Number(inReserveQty)){
								uiGridValidateService.setValid(dirtyRows[i], colDef);
							}else{
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
		
	})
	
})();

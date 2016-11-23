(function(){
	
	// message init
	Constants.message_keys = [];
	
	var partnerReturnListApp = angular.module('partnerReturnListApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	partnerReturnListApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService, uiGridValidateService) {
		
		// PO일경우 업체ID
		var poBusinessId = global.session.businessId;
		$scope.poBusinessId = poBusinessId;
		
		var columnDefs = [
			                  { field : 'omsOrderproduct.orderDate'                                  ,width : '100', colKey : 'c.oms.logistics.datetime_order'             ,cellClass : 'alignC' },    // 주문일시
			                  { field : 'orderId'                                                    ,width : '120', colKey : 'c.oms.logistics.order_id'                   ,cellClass : 'alignC' },    // 주문번호
		                	  { field : 'omsClaimproduct.omsClaim.acceptDt'                          ,width : '120', colKey : 'c.oms.claim.datetime_accept'                ,cellClass : 'alignC' },    // 클레임접수일
		                	  { field : 'claimNo'                                                    ,width : '100', colKey : 'c.oms.claim.id'                             ,cellClass : 'alignC' },    // 클레임번호
		                	  { field : 'omsOrderproduct.omsOrder.name1'                             ,width : '120', colKey : 'c.oms.logistics.orderer_name'               ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsOrder.phone2'                            ,width : '140', colKey : 'c.oms.logistics.orderer_mobile'             ,cellClass : 'alignC' },
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.returnName'       ,width : '120', colKey : 'c.oms.logistics.return.sender.name'         ,cellClass : 'alignC' },    // 발송인명
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.phone1'           ,width : '140', colKey : 'c.oms.logistics.return.sender.phone'        ,cellClass : 'alignC' },    // 발송인전화번호
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.phone2'           ,width : '140', colKey : 'c.oms.logistics.return.sender.mobile'       ,cellClass : 'alignC' },    // 발송인휴대폰번호
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.returnZipCd'      ,width : '120', colKey : 'c.oms.logistics.zipCode'                    ,cellClass : 'alignC' }, 
		                	  { field : 'omsClaimproduct.omsClaim.omsClaimdelivery.deliveryAddress'  ,width : '250', colKey : 'c.oms.logistics.address'                                          }, 
		                	  { field : 'omsOrderproduct.productId'                                  ,width : '120', colKey : 'c.oms.logistics.product_id'                 ,cellClass : 'alignC' },    // 상품번호
		                	  { field : 'omsOrderproduct.productName'                                ,width : '300', colKey : 'c.oms.logistics.product_name'                                     },    // 상품명
		                	  { field : 'omsOrderproduct.saleproductName'                            ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'                                 },    // 단품명
		                	  { field : 'inReserveQty'                                               ,width : '120', colKey : 'c.oms.logistics.partner.inReserve.qty'      ,cellClass : 'alignR' },    // 반품예정수량
		                	  { field : 'goodInQty'                                                  ,width : '100', colKey : 'c.oms.logistics.partner.return.qty'         ,cellClass : 'alignR', 
	                		  	enableCellEdit : true                                                ,validators : {compared : true, maxLength : 100}                     ,type : 'number'  
		                	  },                                                                                                                                                                       // 입고수량
		                	  { field : 'virtualInQty'                                               ,width : '100', colKey : 'c.oms.logistics.return.virtual.qty'         ,cellClass : 'alignC' },
		                	  { field : 'omsClaimproduct.claimReasonName'                            ,width : '200', colKey : 'c.oms.logistics.claim.reason'               ,cellClass : 'alignC' }    // 클레임사유
                  	    ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_partnerReturn',
			url : '/api/oms/logistics/partner/return',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : true,
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
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchReturnList = function() {
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {saleTypeCd : 'SALE_TYPE_CD.CONSIGN'};
		};
		
		this.returnConfirm = function(){
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
		
		// 그리드 유효성 체크
		this.validate = function(dirtyRows) {
			var isValidGrid = true;

			for (var i = 0; i < dirtyRows.length; i++) {
				var rowEntity = dirtyRows[i];
				var inReserveQty = rowEntity.inReserveQty;
				var goodInQty = rowEntity.goodInQty;
				
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
						
						if(validator.compared && isEmpty){
							uiGridValidateService.setInvalid(dirtyRows[i], colDef);
						}else if(validator.compared && !isEmpty){// 비교값 체크하여 invalid 설정
							if(Number(goodInQty) == Number(inReserveQty)){
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
		
		$scope.checkBoxAll = function(grid, evt) {
			var allCheck = $(evt.target).is(":checked");
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = allCheck;
				}
			}
		}
		
	});
	
})();

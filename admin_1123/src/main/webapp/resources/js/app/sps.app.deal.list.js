//메시지
Constants.message_keys = ["common.label.alert.save"];

var dealApp = angular.module("dealApp", ['ui.date',	'commonServiceModule', 'gridServiceModule', "commonPopupServiceModule",	'spsServiceModule', 'pmsServiceModule']);
dealApp.controller("sps_dealListApp_controller", function($window, $scope, $filter, commonService, gridService) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	$scope.dealSearch = {};
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	// 초기화
	angular.element(document).ready(function () {		
		commonService.init_search($scope,'dealSearch');
	});			
	
	var columnDefs = [
	                  	{ field: 'dealId'	, colKey: "c.spsDeal.deal.id"		, linkFunction:'detailDeal'	, enableCellEdit:false  		},
	                  	{ field: 'name'		, colKey: "spsDeal.name"  			, linkFunction:'detailDeal'									},
	                  	{ field: 'displayYn', colKey: "c.spsDeal.deal.displayYn", enableCellEdit:false		, cellFilter: "displayYnFilter" },
	                  	{ field: 'insId'	, colKey: "c.grid.column.insId"  	, userFilter :'insId,insName'								},
	                  	{ field: 'insDt'	, colKey: "c.grid.column.insDt" 	, cellFilter: "date:\'yyyy-MM-dd\'" 						},
	                  	{ field: 'updId'	, colKey: "c.grid.column.updId"		, userFilter :'updId,updName'								},
	                  	{ field: 'updDt'	, colKey: "c.grid.column.updDt" 	, cellFilter: "date:\'yyyy-MM-dd\'" 						}
		            ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_deal",		//mandatory
			url :  '/api/sps/deal/',  	//mandatory
			searchKey : "dealSearch",   //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				pagination : false
			},
			callbackFn : function(){	//optional
				$scope.myGrid.loadGridData();
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	$scope.detailDeal = function(field, row) {
		$scope.dealId = row.dealId;
		var winURL = Rest.context.path +"/sps/deal/popup/detail";
		popupwindow(winURL, "deal", 1100, 361);
	}
	
}).controller("sps_dealDetailPopApp_controller", function($window, $scope, $filter, commonService, commonPopupService, dealService) {
	var pScope = $window.opener.$scope;
	$window.$scope = $scope;
	
	angular.element(document).ready(function () {		
		window.resizeTo(1100, 361);
	});
	
	// INIT
	this.init = function() {
		dealService.getDealDetail(pScope.dealId, function(response) {
			$scope.spsDeal = response;
			pScope.dealTypeCd = response.dealTypeCd;
		});
	}
	
	// 제한 설정 팝업
	this.restrictPop = function() {
		commonPopupService.restrictPopup($scope, true, true, false, true);
	}
	
	// 딜 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// 딜 상세 저장
	this.save = function() {
		
		//폼 체크
		if (!commonService.checkForm($scope.spsDealForm)) {
			return;
		}
		
		if (confirm("저장하시겠습니까?")) {
			dealService.updateDeal($scope.spsDeal, function() {
				alert(pScope.MESSAGES["common.label.alert.save"]);
				$window.location.reload();
			});
		}
	}
	
	// 탭이동
	this.moveTab = function(param) {
		if ("divTitleManager" == param) {
			 $window.location.href = Rest.context.path +"/sps/deal/popup/divTitleManager";
		} else if ("dealProduct" == param) {
			$window.location.href = Rest.context.path +"/sps/deal/popup/productManager";
		}
	}
	
}).controller("sps_dealDivTitleManagerPopApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService) {
	var pScope = $window.opener.$scope;
	$window.$scope = $scope;
	
	$scope.dealSearch1 = {};
	$scope.dealSearch2 = {};
	$scope.depth1Rows=0;
	
	angular.element(document).ready(function () {		
		window.resizeTo(1200, 535);
	});

	var columnDefs1 = [
		                 { field: 'sortNo'		, colKey: "c.spsDeal.deal.sortNo"		, enableCellEdit:true	, type : "number"				},
		                 { field: 'name'		, colKey: "c.spsDeal.deal.1depthName"	, enableCellEdit:true	, vKey: "spsDealgroup.name"		},
		                 { field: 'displayYn'	, colKey: "c.spsDeal.deal.displayYn"	, enableCellEdit:true	, cellFilter: "displayYnFilter" }
			          ];
		
		var gridParam1 = {
				scope : $scope, 						//mandatory
				gridName : "grid_depth1",				//mandatory
				url :  '/api/sps/deal/oneDepthDivTit',  //mandatory
				searchKey : "dealSearch1",   			//mandatory
				columnDefs : columnDefs1,    			//mandatory
				gridOptions : {             			//optional
					// row 선택시 호출 함수 정의
					enableRowSelection: true,
					noUnselect : true,
					multiSelect: false,
					pagination : false,
					rowSelectionFn : function(row){
						$scope.twoDepthList(row.entity);
						$scope.depth1Rows=1;
					}
				},
				callbackFn : function(){				//optional
					$scope.dealSearch1.dealId = pScope.dealId;
					$scope.myGrid1.loadGridData();
				}
		};
//		
//		<input type="file" id="logoImg" name="fileModel" image-key="pmsBrand.logoImg" file-upload />
//		<input type="text" id="logoImgPath" placeholder="" required />
//		<button type="button" class="btn_type2 btn_addFile">
//			<b><spring:message code="c.common.file.search" /></b>
//		</button>
//		<span><spring:message code="common.imgUpload.text1"/></span>
//		<p class="information" ng-show="pmsBrand.logoImg == null">필수 입력 항목 입니다.</p>
//		//이미지 미리보기 삭제

		$scope.deletelogoImg = function(row) {
			console.log(row);
			commonService.deleteFile(row.img, function(data) {
				row.img = null;
				$scope.grid_depth2.gridApi.rowEdit.setRowsDirty([row]);
		    });
		}
		
	     
		var retHtml = '';
		retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents filebox" title> ';
//		retHtml += '	<div class="filebox"> ';
		retHtml += '	{{row.entity.img == null || row.entity.img == "" ? "이미지 없음" : row.entity.img.slice(row.entity.img.lastIndexOf("/")+1)}}<label for="ex_file{{row.uid}}">찾아보기</label> ';
		retHtml += '	<input type="file" id="ex_file{{row.uid}}" ng-model="row.entity.img" hash-key="{{row.entity.$$hashKey}}" grid-key="row" ng-disabled="row.entity.img" file-upload />';
		retHtml += '	<button style="float:right;" class="btn_type2" type="button" ng-click=\"grid.appScope.deletelogoImg(row.entity)\">X</button>';
		retHtml += '</div>';
		
		$scope.myGrid1 = new gridService.NgGrid(gridParam1);
		
		var columnDefs2 = [
							{ field: 'sortNo'		, colKey: "c.spsDeal.deal.sortNo"		, enableCellEdit:true , type : "number"				  },
							{ field: 'name'			, colKey: "c.spsDeal.deal.2depthName"  	, enableCellEdit:true , vKey: "spsDealgroup.name"	  },
							{ field: 'img'			, displayName : '이미지 업로드'   		, enableCellHiddenInputEdit: true,	
								width: '50%', cellTemplate : retHtml},
							{ field: 'displayYn'	, colKey: "c.spsDeal.deal.displayYn"  	, enableCellEdit:true , cellFilter: 'displayYnFilter' }
				          ];
			
		var gridParam2 = {
				scope : $scope, 						//mandatory
				gridName : "grid_depth2",				//mandatory
				url :  '/api/sps/deal/twoDepthDivTit',  //mandatory
				searchKey : "dealSearch2",   			//mandatory
				columnDefs : columnDefs2,    			//mandatory
				gridOptions : {             			//optional
					pagination : false
				},
				callbackFn : function(){				//optional
					$scope.dealSearch2.dealId = pScope.dealId;
				}
		};
			
		$scope.myGrid2 = new gridService.NgGrid(gridParam2);
		
		
		
	
		// 1depth 등록
		this.addOneDepth = function() {
			$scope.dealSearch2.dealGroupNo = "";
			$scope.myGrid1.addRow({
				sortNo 	  : '1',
				displayYn : 'Y',
				crudType  : 'I',
				dealId    : pScope.dealId
			});
		}
		
		// 2depth 등록
		this.addTwoDepth = function() {
			if ($scope.dealSearch2.dealGroupNo != "" && $scope.dealSearch2.dealGroupNo != undefined) {
				$scope.myGrid2.addRow({
					sortNo 	  : '1',
					displayYn : 'Y',
					crudType  : 'I',
					dealId    : pScope.dealId,
					img		  : '',
					upperDealGroupNo : $scope.dealSearch2.dealGroupNo
				});
			}
		}
		
		$scope.twoDepthList = function(row) {
			if (row.dealGroupNo != '' && row.dealGroupNo != undefined) {
				$scope.dealSearch2.dealGroupNo = row.dealGroupNo;
				$scope.myGrid2.loadGridData();
			}
		}
		
		// 탭이동
		this.moveTab = function(param) {
			if ("dealDetail" == param) {
				 $window.location.href = Rest.context.path +"/sps/deal/popup/detail";
			} else if ("dealProduct" == param) {
				 $window.location.href = Rest.context.path +"/sps/deal/popup/productManager";
			}
		}
		
		// oneDepth 구분 타이틀 삭제
		this.deleteOneDepth = function() {
			$scope.myGrid1.deleteGridData();
			$scope.grid_depth2.data = [];
		}
		
		// twoDepth 구분 타이틀 삭제
		this.deleteTwoDepth = function() {
			$scope.myGrid2.deleteGridData();
		}
		
		
		$scope.oneDepthReload = function() {
			$scope.myGrid1.loadGridData();
		}
		$scope.twoDepthReload = function() {
//			$scope.grid_depth2.data = [];
			$scope.myGrid2.loadGridData();
		}
		
		$scope.changeMode = function(param) {
			if (param) {
				$scope.depth1Rows = 0;
				$scope.grid_depth2.data = [];
			}
			$scope.myGrid1.toggleRowSelection();
		}
		

}).controller("sps_dealProductManagerPopApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService, dealService, productService) {
	var pScope = $window.opener.$scope;
	$window.$scope = $scope;
	$scope.productSearch = {};
	$scope.tmp = {};
	$scope.selectedReserveObj = {};
	
	angular.element(document).ready(function () {
		commonService.init_search($scope, 'productSearch');
		window.resizeTo(1100, 800);
	});
	
	var changeDealPrice = function(row) {
		if (row.salePrice != '') {
			var grossProfitAmount = Number(row.salePrice) - Number(row.supplyPrice);
			row.grossProfitAmount = grossProfitAmount;
		} else {
			row.grossProfitAmount = "";
		}
		
		if(row.salePrice > 0 && row.supplyPrice > 0){
			var commission = (row.salePrice-row.supplyPrice) / row.salePrice * 100;
//			row.commissionRate= $filter('number')(commission, 1);
			row.commissionRate= commission.toFixed(1);
		}else{
			row.commissionRate = 0;
		}
	}
	
	changeDealStockQty = function(row) {
		row.dealStockQty = row.totalDealStockQty;
	}
	
	$scope.changeDeliveryFeeFree = function(row, param) {
		//console.log(param)
		//alert(param.deliveryFeeFreeYn)
		//alert(row.deliveryFeeFreeYn)
		//row.deliveryFeeFreeYn = param.deliveryFeeFreeYn;
		$scope.grid_product.gridApi.rowEdit.setRowsDirty([row]);
	}
	
	$scope.changeDeliveryFeeFreeInit = function(row, param) {
		param.deliveryFeeFreeYn = row.deliveryFeeFreeYn;
	}
	this.disabledDeliveryFeeFree = function(row, param) {
		if (row.crudType != null && row.crudType != undefined && row.crudType == 'I') {
			if (row.deliveryFeeFreeYn == 'N') {
				param.deliveryFeeFreeYn = "N";
				return false;
			} else {
				return true;
			}
		} else {
			if (row.pmsProduct.deliveryFeeFreeYn == 'N') {
				param.deliveryFeeFreeYn = "N";
				return false;
			} else {
				return true;
			}
		} 
	}
	
	$scope.saleStateCds = [
	                       {cd:'SALE_STATE_CD.SALE', name:'판매중'} 
	                       ,{cd:'SALE_STATE_CD.SOLDOUT', name:'품절'} 
	                       ,{cd:'SALE_STATE_CD.STOP', name:'일시정지'} 
	                       ,{cd:'SALE_STATE_CD.MDSTOP', name:'MD정지'} 
	                       ,{cd:'SALE_STATE_CD.END', name:'영구종료'} 
	                       ];
	
	$scope.makeCodeString = function(nm) {
		var resultCd = ""
		if(angular.isDefined($scope.tmp[nm])) {
			for(var i=0; i<$scope.tmp[nm].length; i++) {
				resultCd += "'" + $scope.tmp[nm][i] + "',";
			}
			$scope.productSearch[nm] = resultCd.slice(0,-1);		
		}
	}
	
	this.checkAll = function(model, yn) {
		if($scope[model+"All"]) {
			$scope.tmp[model] = $scope[model].map(function(item) { return item.cd});
		} else {
			$scope.tmp[model] = [];
		}

		$scope.makeCodeString(model);
	};

	this.checked = function(cd, model, yn) {
		if($scope.tmp[model].indexOf(cd) == $scope[model].length-1) {
			$scope[model+'All'] = true; 
		} else {
			$scope[model+'All'] = false; 
		}
		
		$scope.makeCodeString(model);
	}
	
	var cellTemplate1 = "<div class=\"ui-grid-cell-contents\" title=\"\"><button class=\"btn_type2\" ng-click=\"grid.appScope.saleProductPricePopup(row.entity)\"><b style=\"{{row.entity.saleProductAddPriceYn=='Y' ? 'color:red;' : ''}}\">설정</b></button></div>";
	var cellTemplate2 ="";
	if (pScope.dealTypeCd == 'DEAL_TYPE_CD.MEMBER') {
		cellTemplate2 = "<div class=\"ui-grid-cell-contents\" title=\"\"><button class=\"btn_type2\" ng-click=\"grid.appScope.gradeBenefitPopup(row.entity)\"><b style=\"{{row.entity.gradeBenefitYn=='Y' ? 'color:red;' : ''}}\">설정</b></button></div>";
	}
//	var cellTemplate3 = "<div class=\"ui-grid-cell-contents\" title=\"\" style=\"text-align:center;\"><input type=\"checkbox\" ng-disabled=\"grid.appScope.ctrl.disabledDeliveryFeeFree(row.entity, this);\" ng-init=\"grid.appScope.changeDeliveryFeeFreeInit(row.entity, this);\"ng-model=\"deliveryFeeFreeYn\" ng-true-value=\"'Y'\" ng-false-value=\"'N'\"/ ng-change=\"grid.appScope.changeDeliveryFeeFree(row.entity, this)\" ></div>";
	var cellTemplate3 = "<div class=\"ui-grid-cell-contents\" title=\"\" style=\"text-align:center;\"><input type=\"checkbox\" ng-model=\"row.entity.deliveryFeeFreeYn\"  ng-disabled=\"grid.appScope.ctrl.disabledDeliveryFeeFree(row.entity, this);\" ng-true-value=\"'Y'\" ng-false-value=\"'N'\"  ng-change=\"grid.appScope.changeDeliveryFeeFree(row.entity, this)\"  / ></div>";
	
	var columnDefs = [
		               { field: 'productId'					, width: '10%'	, colKey: "c.spsDealproduct.productId"				, linkFunction:'productDetail' 		  										  												},
		               { field: 'productName'				, width: '20%'	, colKey: "c.spsDealproduct.name"					, linkFunction:'productDetail'										  		  												},
		               { field: 'startDt'					, width: '15%'	, colKey: "c.spsDealproduct.startDt"				, vKey:"spsDealproduct.startDt"			, type:'datetime'		, periodStart : true 	, validators:{required:true}  		},
		               { field: 'endDt'						, width: '15%'	, colKey: "c.spsDealproduct.endDt"					, vKey:"spsDealproduct.endDt"			, type:'datetime'		, periodEnd : true 		, validators:{required:true} 		},
		               { field: 'divTitDepth'				, width: '18%'	, colKey: "c.spsDealproduct.divTitleDepth"			, linkFunction:'depthDivTitPopup' 	  													, validators:{required:true}		},
		               { field: 'listPrice'					, type : 'number', 		width: '10%'	, colKey: "spsDealproduct.listPrice"				, vKey:"spsDealproduct.listPrice"	 	, enableCellEdit:true									  												},
		               { field: 'supplyPrice'				, type : 'number', 		width: '10%'	, colKey: "spsDealproduct.supplyPrice"				, vKey:"spsDealproduct.supplyPrice"		, enableCellEdit:true 	, afterCellEdit: changeDealPrice	  						},
		               { field: 'salePrice'					, type : 'number', 		width: '10%'	, colKey: "spsDealproduct.salePrice"				, vKey:"spsDealproduct.salePrice"		, enableCellEdit:true	, afterCellEdit: changeDealPrice 	  						},
		               { field: 'commissionRate'			, type : 'number', 		width: '10%'	, colKey: "spsDealproduct.commissionRate"			 																						, validators:{required:true}		},
		               { field: 'grossProfitAmount'			, width: '10%'	, colKey: "c.spsDealproduct.grossProfitAmount"												  																					},
		               { field: 'pointSaveRate'				, width: '10%'	, colKey: "spsDealproduct.pointSaveRate"			, vKey:"spsDealproduct.pointSaveRate" 	, enableCellEdit:true 										  						},
		               { field: 'deliveryFeeFreeYn'			, width: '10%'	, colKey: "c.spsDealproduct.deliveryFeeFreeYn"		, enableCellHiddenInputEdit:true		, cellTemplate :cellTemplate3			  											},
		               { field: 'sortNo'					, width: '10%'	, colKey: "c.spsDealproduct.sortNo"					, enableCellEdit:true 					, type : "number" 																	},
		               { field: 'dealStockQty'				, width: '10%'	, colKey: "c.spsDealproduct.dealStockQty"													  																					},
		               { field: 'totalDealStockQty'			, width: '10%'	, colKey: "c.spsDealproduct.totalDealStockQty"		, enableCellEdit:true					, validators:{required:true}	, afterCellEdit: changeDealStockQty				  	},
		               { field: 'saleProductTotalStock'		, width: '10%'	, colKey: "c.spsDealproduct.stockQty"														  																					},
		               { field: 'saleProductAddPriceManager', width: '10%'	, colKey: "c.spsDealproduct.saleProductdAddPrice"	, cellTemplate :cellTemplate1, width :'130'																					},
		               { field: 'gradeBenefitManager'		, width: '10%'	, colKey: "c.spsDealproduct.gradeBenefit"			, cellTemplate :cellTemplate2, width :'130'																					},
		               { field: 'insDt'						, width: '15%'	, colKey: "c.grid.column.insDt"						, cellFilter: "date:\'yyyy-MM-dd\'"	  																						},
			           { field: 'insId'						, width: '10%'	, colKey: "c.grid.column.insId"						, userFilter :'insId,insName'		  																						},
			           { field: 'updDt'						, width: '15%'	, colKey: "c.grid.column.updDt"						, cellFilter: "date:\'yyyy-MM-dd\'"	  																						},
			           { field: 'updId'						, width: '10%'	, colKey: "c.grid.column.updId"						, userFilter :'updId,updName'		  																						}
			        ];
	
		var gridParam = {
				scope : $scope, 						//mandatory
				gridName : "grid_product",				//mandatory
				url :  '/api/sps/deal/product',  		//mandatory
				searchKey : "productSearch",   			//mandatory
				columnDefs : columnDefs,    			//mandatory
				gridOptions : {             			//optional
				},
				callbackFn : function(){				//optional
//					$scope.myGrid.loadGridData();
				}
		};
		
		$scope.productSearch.dealId = pScope.dealId;
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		$scope.productInfoType = [
  	                            {val : 'PRODUCTID',text : '상품번호'},
		  	                   	{val : 'NAME',text : '상품명'}
		  		              ];
		
		
		
		// 탭이동
		this.moveTab = function(param) {
			if ("dealDetail" == param) {
				 $window.location.href = Rest.context.path +"/sps/deal/popup/detail";
			} else if ("divTitleManager" == param) {
				 $window.location.href = Rest.context.path +"/sps/deal/popup/divTitleManager";
			}
		}
		
		// 검색조건 초기화
		this.reset = function() {
			commonService.reset_search($scope, 'productSearch');
			angular.element(".day_group").find('button:first').addClass("on");
		}
		
		
		this.setCommonsionRate = function(salePrice, supplyPrice) {
			var commissionRate = 0;
			if(salePrice > 0 && supplyPrice > 0){
				commissionRate = (Number(salePrice)-Number(supplyPrice)) / Number(salePrice) * 100;
				
			}else{
				commissionRate = 0;
			}
			return commissionRate;
		}
		this.setGrossProfitAmount = function(salePrice, supplyPrice) {
			var grossProfitAmount = 0;
			
			if (salePrice != '') {
				grossProfitAmount = Number(salePrice) - Number(supplyPrice);
			} else {
				grossProfitAmount = 0;
			}
			return grossProfitAmount;
		}
		
		
		// 상품 검색 팝업
		this.searchProduct = function() {
			commonPopupService.productPopup($scope, "callback_product", true);

			$scope.callback_product = function(callbackData) {
				
				var P = [];
				for(var i = 0 ; i < callbackData.length ; i++){
					P.push({productId : callbackData[i].productId});
				}
				
				productService.getProductListWithSaleStock(P, function(response) {
					console.log("response", response);
					for (var i=0; i<response.length; i++ ) {
						
						$scope.myGrid.addRow({
							productId 				: response[i].productId,
							productName				: response[i].name,
							startDt		    		: response[i].startDt,
							endDt    				: response[i].endDt,
							displayYn  				: "Y",
							divTitDepth				: "설정",
							listPrice				: response[i].listPrice,
							supplyPrice				: response[i].supplyPrice,
							salePrice				: response[i].salePrice,
							commissionRate			: $scope.ctrl.setCommonsionRate(response[i].salePrice, response[i].supplyPrice),
							grossProfitAmount   	: $scope.ctrl.setGrossProfitAmount(response[i].salePrice, response[i].supplyPrice),
							pointSaveRate			: response[i].pointSaveRate,
							deliveryFeeFreeYn		: response[i].deliveryFeeFreeYn,
							sortNo					: 1,
							personQty				: "",
							dealStockQty			: "",
							totalDealStockQty		: "",
							saleProductTotalStock	: response[i].saleProductTotalStock,
							dealStateCd				: "DEAL_STATE_CD.RUN",
							dealStateName			: "대기",
							dealId 					: pScope.dealId,
							crudType 				: 'I',
						});
					}
				});
				
			}
		}
		
		
		$scope.calcCommission = function(targetObject) {
			if(targetObject.salePrice > 0 && targetObject.supplyPrice > 0){
				var commission = (targetObject.salePrice-targetObject.supplyPrice) / targetObject.salePrice * 100;
				targetObject.commissionRate= $filter('number')(commission, 1);
			}else{
				targetObject.commissionRate = 0;
			}
			return targetObject.commissionRate;
		}
		
		
		// 상품 일괄 업로드
		this.batchProductUpload = function() {
			commonPopupService.gridbulkuploadPopup($scope,"excelUpload_callback","product");
		}
		
		$scope.excelUpload_callback = function(response) {
			
			var data = response.resultList;
			var product = [];
			for (var i = 0; i < data.length; i++) {
				product.push(data[i].pmsProduct)
			}
			
			productService.getProductListWithSaleStock(product, function(response) {
				for (var i=0; i<response.length; i++ ) {
					
					$scope.myGrid.addRow({
						productId 				: response[i].productId,
						productName				: response[i].name,
						startDt		    		: response[i].startDt,
						endDt    				: response[i].endDt,
						displayYn  				: "Y",
						divTitDepth				: "설정",
						listPrice				: response[i].listPrice,
						supplyPrice				: response[i].supplyPrice,
						salePrice				: response[i].salePrice,
						commissionRate			: $scope.ctrl.setCommonsionRate(response[i].salePrice, response[i].supplyPrice),
						grossProfitAmount   	: $scope.ctrl.setGrossProfitAmount(response[i].salePrice, response[i].supplyPrice),
						pointSaveRate			: "",
						deliveryFeeFreeYn		: response[i].deliveryFeeFreeYn,
						sortNo					: 1,
						personQty				: "",
						dealStockQty			: "",
						totalDealStockQty		: "",
						saleProductTotalStock	: response[i].saleProductTotalStock,
						dealStateCd				: "DEAL_STATE_CD.RUN",
						dealStateName			: "대기",
						dealId 					: pScope.dealId,
						crudType 				: 'I',
					});
				}
			});
		}
			
		
		// 브랜드 검색 팝업
		this.brandSearch = function() {
			commonPopupService.brandPopup($scope, "callback_brand", false);
		}
		
		$scope.callback_brand = function(data) {
			$scope.productSearch.brandId = data[0].brandId;
			$scope.productSearch.brandName = data[0].name;
			common.safeApply($scope);
 		}
		
		// 브랜드 삭제
		this.eraser = function(param) {
			if (param == 'brand') {
				$scope.productSearch.brandId = "";
				$scope.productSearch.brandName = "";
			} else {
				$scope.productSearch.businessId = "";
				$scope.productSearch.businessName = "";
			}
		}
		
		// 공급업체 검색 팝업
		this.businessSearch = function() {
			commonPopupService.businessPopup($scope,"callback_business",false);
		}
		
		$scope.callback_business = function(data){
			$scope.productSearch.businessId = data[0].businessId;
			$scope.productSearch.businessName = data[0].name;
			common.safeApply($scope);	
		}
		
		// 상품 상세 팝업
		$scope.productDetail = function(field, row) {
			commonPopupService.openProductDetailPopup($scope, row.productId);
		}
		
		// 구분타이틀 Depth 설정 팝업
		$scope.depthDivTitPopup = function(field, row) {
			var index = $scope.grid_product.data.indexOf(row);
			console.log("index", index);
			$scope.clickDealIndex = index;
			var winURL = Rest.context.path +"/sps/deal/popup/divTitleTree";
			popupwindow(winURL, "구분타이틀Depth설정팝업", 600, 350);
		}
		
		$scope.divTitleDepth = function() {
			$scope.myGrid.changeData($scope.clickDealIndex, "divTitDepth", $scope.depthTree.displayDepthName);
			$scope.myGrid.changeData($scope.clickDealIndex, "dealGroupNo", $scope.depthTree.selectedGroupNo);
		}
		
		$scope.changeSalePrice = function(data) {
			$scope.popupData = {};
			$scope.popupData = angular.copy(data);
			var spsDealsaleproductprices = [];
			for (var i=0; i<$scope.popupData.length; i++) {
				spsDealsaleproductprices.push({"saleproductId" : $scope.popupData[i].saleproductId, "addSalePrice" : $scope.popupData[i].addSalePrice});
			}
			$scope.selectedReserveObj.spsDealsaleproductprices = spsDealsaleproductprices;
			$scope.selectedReserveObj.saleProductAddPriceYn = 'Y';
			// set dirty
			$scope.grid_product.gridApi.rowEdit.setRowsDirty([$scope.selectedReserveObj]);
			common.safeApply($scope);
			
		}
		$scope.changeGradeBenefit = function(data) {
			$scope.popupData = {};
			$scope.popupData = angular.copy(data);
			var spsDealMember = [];
			for (var i=0; i<$scope.popupData.length; i++) {
				spsDealMember.push({"memGradeCd" : $scope.popupData[i].memGradeCd, "memGradeName": $scope.popupData[i].memGradeName, "preOpenDays" : $scope.popupData[i].preOpenDays, "addSalePrice" : $scope.popupData[i].addSalePrice});
			}
			$scope.selectedReserveObj.spsDealmembers = spsDealMember;
			$scope.selectedReserveObj.gradeBenefitYn = 'Y';
			
			// set dirty
			$scope.grid_product.gridApi.rowEdit.setRowsDirty([$scope.selectedReserveObj]);
			common.safeApply($scope);
			
		}
		
		// 회원등급별 혜택 팝업
		$scope.gradeBenefitPopup = function(entity) {
			var index = $scope.grid_product.data.indexOf(entity);
			$scope.clickDealIndex = index;
			$scope.clickDealProductId = entity.productId;
			$scope.selectedReserveObj = entity;
			
			var winURL = Rest.context.path +"/sps/deal/popup/gradeBenefit";
			popupwindow(winURL, "구분타이틀Depth설정팝업", 600, 350);
		}
		
		// 단품추가 금액 설정 팝업
		$scope.saleProductPricePopup = function(entity) {
			var index = $scope.grid_product.data.indexOf(entity);
			$scope.clickDealIndex = index;
			$scope.clickDealProductId = entity.productId;
			$scope.selectedReserveObj = entity;
			
			var winURL = Rest.context.path +"/sps/deal/popup/saleProductPrice";
			popupwindow(winURL, "단품추가금액설정팝업", 600, 350);
		}
		
		this.saveDealProduct = function() {
			$scope.myGrid.saveGridData(null, function() {
				$scope.saveProduct_callback();
			});
		}
		
		$scope.saveProduct_callback = function() {
			$scope.myGrid.loadGridData();
		}
		
}).filter('dealStateFilter', function() {// DEAL_STATE_CD 
	
	var comboHash = {
			'DEAL_STATE_CD.READY': '대기',
			'DEAL_STATE_CD.RUN': '진행중',
			'DEAL_STATE_CD.STOP': '중지'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };		
		
// 구분타이틀 저장 팝업
}).controller("sps_dealDivTitleTreePopApp_controller", function($window, $scope, $filter, commonService, commonPopupService, dealService,displayTreeService) {
	var pScope = $window.opener.$scope;	
	$scope.search = {};
	
	this.getDealGroupTree = function() {
		$scope.search = angular.copy(pScope.productSearch);
		dealService.getDealGroupTree($scope.search, function(data) {
			$scope.dealGropTree = data;
		});
	}
	
	//트리 하위 열기/닫기 
	this.openFolder = function(index, icon){
		displayTreeService.openTree(index, icon, $scope.dealGropTree, "dealGroupNo", "upperDealGroupNo");
	}
	
	this.selectDepth = function(depth, delGroupNo, name, upperDealGroupNo, upperName) {
		$scope.depthTree = {};
		if (depth == 1) {
			$scope.depthTree.selectedGroupNo = delGroupNo;
			$scope.depthTree.selectedGroupName = name;
			$scope.depthTree.displayDepthName = name;
		} else {
			$scope.depthTree.selectedGroupNo = delGroupNo;
			$scope.depthTree.selectedGroupName = name;
			$scope.depthTree.displayDepthName = upperName + " > " + name;
		}
		pScope.depthTree = $scope.depthTree;
		pScope.divTitleDepth();
		$window.close();
	}

	// 등급별 오픈일, 할인 조회
}).controller("sps_dealGradeBenefitPopApp_controller", function($window, $scope, $filter, commonService, commonPopupService, dealService,displayTreeService) {	
	var pScope = $window.opener.$scope;	
	$scope.spsDealmember = [];
	$scope.preSpsDealmember = [];
	$scope.memGradeCds = [];
	$scope.dealMember = {};
	$scope.gradeBenefit = {};
	
	// init
	this.init = function() {
		$scope.index = angular.copy(pScope.clickDealIndex);
		var index = pScope.clickDealIndex;
		var dealGroupNo = pScope.grid_product.data[index].dealGroupNo;
		var dealProductNo = pScope.grid_product.data[index].dealProductNo;
		var dealId = pScope.grid_product.data[index].dealId;
		
		
		// 딜 상품 신규 등록

//		alert(pScope.grid_product.data[index].crudType)
		if (pScope.grid_product.data[index].crudType == "I") {
			
			
			if (pScope.grid_product.data[index].spsDealmembers != undefined) {
				
				var temp = pScope.grid_product.data[index].spsDealmembers;
				$scope.spsDealmember = angular.copy(temp);
//				for (var i = 0; i < temp.length; i++) {
//					$scope.spsDealmember.push({"memGradeCd" : temp[i].memGradeCd, "memGradeName" : temp[i].memGradeName,  "preOpenDays" : Number(temp[i].preOpenDays), "addSalePrice" : Number(temp[i].addSalePrice)});
//				}
			}else{
				commonService.getCodeList({cdGroupCd :'MEM_GRADE_CD' }).then( function(data) {
					for(i = 0 ; i < data.length ; i++){
						//console.log(data[i]);
						$scope.spsDealmember.push({memGradeCd: data[i].cd, memGradeName :  data[i].name,  preOpenDays : 0 , addSalePrice : 0});
					}
				});
			}
			
			
		} else {
			if (pScope.grid_product.data[index].spsDealmembers != undefined) {
				var temp = pScope.grid_product.data[index].spsDealmembers;
				$scope.spsDealmember = angular.copy(temp);
//				for (var i = 0; i < temp.length; i++) {
//					$scope.spsDealmember.push({"memGradeCd" : temp[i].memGradeCd, "preOpenDays" : Number(temp[i].preOpenDays), "addSalePrice" : Number(temp[i].addSalePrice)});
//				}
			} else {
				data = {dealId : dealId, dealProductNo : dealProductNo};
				
				dealService.getDealMemberGradeBenefit(data, function(response) {
					for(var i=0; i < response.length; i++) {
						$scope.spsDealmember.push(response[i]);	
					}
				});
			}
		}
	}
	
	// 닫기
	this.cancel = function() {
		$window.close();
	}
	
	// 저장
	this.save = function() {
//		$scope.spsDealmember = [];
//		for (var i = 0; i < $scope.memGradeCds.length; i++) {
//			var preOpenDays="";
//			var addSalePrice="";
//			if (angular.element("#preOpenDays" + i).val() != "") {
//				preOpenDays = Number(angular.element("#preOpenDays" + i).val());
//			}
//			if (angular.element("#addSalePrice" + i).val() != "") {
//				addSalePrice = Number(angular.element("#addSalePrice" + i).val());
//			}
//			if (angular.element("#preOpenDays" + i).val() != "" || angular.element("#addSalePrice" + i).val() != "") {
//				var memGradeCd = angular.element("#preOpenDays" + i).data("cd");
//				$scope.spsDealmember.push({"memGradeCd" : memGradeCd, "preOpenDays" : preOpenDays, "addSalePrice" : addSalePrice});	
//			}
//		}
		
		//폼 체크
		if(!commonService.checkForm($scope.gradeForm)){
			return;
		}
		
		pScope.changeGradeBenefit($scope.spsDealmember);
		
		$window.close();
	}
	
}).controller("sps_dealSaleProductPricePopApp_controller", function($window, $scope, $filter, commonService, commonPopupService, dealService) {
	var pScope = $window.opener.$scope;	
	$scope.search = {}
	// 단품 목록
	$scope.saleProducts = [];
	$scope.spsDealsaleproductprices = [];
	$scope.preSpsDealsaleproductprices = [];
	
	
	
	// init
	this.init = function() {
		var index = pScope.clickDealIndex;
		var dealGroupNo = pScope.grid_product.data[index].dealGroupNo;
		var dealProductNo = pScope.grid_product.data[index].dealProductNo;
		var dealId = pScope.grid_product.data[index].dealId;
		$scope.search.productId = pScope.clickDealProductId;
		
		dealService.getSaleProdcutList($scope.search, function(data) {
			$scope.saleProducts = data;
		});
		
		if (pScope.grid_product.data[index].crudType == "I") {
			if (pScope.grid_product.data[index].spsDealsaleproductprices != undefined) {
				var temp = pScope.grid_product.data[index].spsDealsaleproductprices;
				console.log("temp", temp);
				for (var i = 0; i < temp.length; i++) {
					$scope.spsDealsaleproductprices.push({"saleproductId" : Number(temp[i].saleproductId), "addSalePrice" : temp[i].addSalePrice});
				}
			} 
		} else {
			if (pScope.grid_product.data[index].spsDealsaleproductprices != undefined) {
				var temp = pScope.grid_product.data[index].spsDealsaleproductprices;
				console.log("temp", temp);
				for (var i = 0; i < temp.length; i++) {
					$scope.spsDealsaleproductprices.push({"saleproductId" : Number(temp[i].saleproductId), "addSalePrice" : temp[i].addSalePrice});
				}
			} else {
				data = {dealId : dealId, dealProductNo : dealProductNo};
				
				dealService.getDealSaleProductPrice(data, function(response) {
					for(var i=0; i < response.length; i++) {
						$scope.spsDealsaleproductprices.push(response[i]);	
					}
				});
			}
		}
	}
	
	// 닫기
	this.cancel = function() {
		$window.close();
	}
	
	// 저장
	this.save = function() {
		$scope.spsDealsaleproductprices = [];
		for (var i = 0; i < $scope.saleProducts.length; i++) {
			var saleProductId="";
			var addPrice="";
			if (angular.element("#addSalePrice" + i).val() == "") {
				addPrice = 0;
			} else {
				addPrice = Number(angular.element("#addSalePrice" + i).val());
			}
				saleProductId = angular.element("#addSalePrice" + i).data("productid");
				$scope.spsDealsaleproductprices.push({"saleproductId" : saleProductId, "addSalePrice" : addPrice});	
		}
		
		pScope.changeSalePrice($scope.spsDealsaleproductprices);
		
		$window.close();
	}
});

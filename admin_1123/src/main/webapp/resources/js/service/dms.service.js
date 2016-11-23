var dmsServiceModule = angular.module("dmsServiceModule", ['ngResource']);
dmsServiceModule.service("displayService",  function(restFactory, commonPopupService, productService){// 전시 일반 서비스
	return {
			//타입별 템플릿 목록 조회
			getTemplateListByType : function(param, callback) {
				var url = Rest.context.path+"/api/dms/common/template";
				restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null, callback);
			},	// 전시코너 아이템 등록 팝업 : 상품
			initCornerItemReg : function($scope, param){
				
				$scope.reloadItemGrid = function(){
					$scope[param.gridName].loadGridData();
					common.safeApply($scope);
				}
				
				if(param.itemType=='product'){
					commonPopupService.productPopup($scope,"callbackProductPopup", true);
					
					//상품 검색 후 그리드에 추가
					$scope.callbackProductPopup = function(callbackData){
						console.log(callbackData);
						var P = [];
						for(var i = 0 ; i < callbackData.length ; i++){
							P.push({productId : callbackData[i].productId});
						}
						
						
						productService.getProductListWithSaleStock(P, function(data){
							console.log(data);
							for(var i = 0 ; i < data.length ; i++){
								var insertFlag = true;
								console.log($scope);
								for (var j=0; j < $scope.grid_cornerItemList.data.length; j++) {
									if (data[i].productId == $scope.grid_cornerItemList.data[j].displayItemId) {
										insertFlag = false;
										break;
									}
								}
								if (insertFlag) {
									$scope[param.gridName].addRow({
										displayYn : 'Y',
										saveType  : 'I',
										displayId : param.displayId,
										displayItemId : data[i].productId,
										productName : data[i].name,
										productTypeName : data[i].productTypeName,
										saleStateName : data[i].saleStateName, 
										salePrice : data[i].salePrice, 
										brandName : data[i].brandName,
										stock : data[i].saleProductTotalStock,
										displayItemDivId : param.displayItemDivId
									});
								}
							}
						});
					}
				}else if(param.itemType=='exhibit'){
					//기획전 검색 팝업 호출
					commonPopupService.exhibitPopup($scope,"callbackExhibitPopup", false);
					
					//기획전 검색 후 그리드에 추가
					$scope.callbackExhibitPopup = function(data){
						$scope.gridData = $scope[param.gridName].getData();
						for(var i = 0 ; i < data.length ; i++){
							var flag = true;
							for(j in $scope.gridData){
								if ( $scope.gridData[j].displayItemId == data[i].exhibitId) {
									flag = false;
									break;
								}
							}
							if (flag) {
								$scope[param.gridName].addRow({
									displayYn : 'Y',
									saveType  : 'I',
									displayId : param.displayId,
									displayItemId : data[i].exhibitId,
									exhibitName : data[i].name, 
									exhibitStateName : data[i].exhibitStateName, 
									exhibitStartDt :data[i].startDt, 
									exhibitEndDt : data[i].endDt,
									displayItemDivId : param.displayItemDivId
								});
							}
						}
					}
				}else if(param.itemType=='html'){
					// HTML 등록 팝업 호출
					
					$scope.search.displayItemNo = "";
					
					var url = "/dms/corner/popup/htmlDetail";
					popupwindow(url, "HTML등록", 1200, 800);
					
//					$scope.addItem = function(dmsDisplayitem){
//						
//						$scope[param.gridName].addRow({
//							displayYn : 'Y',
//							saveType  : 'I',
//							displayId : param.displayId,
//							title : dmsDisplayitem.title,
//							html1 : dmsDisplayitem.html1,
//							html2 : dmsDisplayitem.html2,
//							startDt : dmsDisplayitem.startDt,
//							endDt : dmsDisplayitem.endDt,
//							sortNo : dmsDisplayitem.sortNo,
//							displayItemDivId : param.displayItemDivId
//						});
//					}

				}else if(param.itemType=='img'){
					// 배너 등록 팝업 호출
					var url = "/dms/corner/popup/imageDetail";
					popupwindow(url, "배너등록", 1000, 600);
					/*$scope.addItem = function(dmsDisplayitem){
						$scope[param.gridName].addRow({
							displayYn : 'Y',
							saveType  : 'I',
							displayId : param.displayId,
							img1 : dmsDisplayitem.img1,
							url1 : dmsDisplayitem.url1,
							text1 : dmsDisplayitem.text1,
							startDt : dmsDisplayitem.startDt,
							endDt : dmsDisplayitem.endDt,
							sortNo : dmsDisplayitem.sortNo,
							displayItemDivId : param.displayItemDivId
						});
					}*/
					
				}else if(param.itemType=='text'){
					
					$scope[param.gridName].addRow({
						displayYn : 'Y',
						saveType  : 'I',
						title : '',
						displayId : param.displayId,
						displayItemDivId : param.displayItemDivId
					});
					
					
/*					var url = "/dms/corner/popup/textInsert";
					popupwindow(url, "TEXT등록", 900, 500);
					$scope.addItem = function(dmsDisplayitem){
						$scope[param.gridName].addRow({
							displayYn : dmsDisplayitem.displayYn,
							saveType  : 'I',
							displayId : param.displayId,
							title : dmsDisplayitem.title,
							startDt : dmsDisplayitem.startDt,
							endDt : dmsDisplayitem.endDt,
							sortNo : dmsDisplayitem.sortNo,
							displayItemDivId : param.displayItemDivId
						});
					}*/
				}
			},
			// 전시 코너 아이템 공통 그리드 columnDefs 조회
			getCornerItemColDef : function(scope, displayItemTypeCd){
				var template = {
						dateClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							var cellcss = 'alignC';
//							if (!grid.options.isRowHidetable(row)) {
//								cellcss = 'alignC hide-date';
//							}
							return cellcss;
						}
					};
				
				var columnDefs = [];
			    if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.PRODUCT'){// 아이템 타입 : 상품
					
					columnDefs = [
				             { field: 'displayItemId', 					colKey: "dmsDisplayitem.displayItemId", vKey: "dmsDisplayitem.displayItemId",  	enableCellEdit: true,  validators : {required :true},	linkFunction : "openProductPopup"},
				             { field: 'productName', 					colKey: "pmsProduct.name",  	     linkFunction : "openProductPopup",			enableCellEdit: false},
				             { field: 'sortNo', 						colKey: "dmsDisplayitem.sortNo", 		vKey: "dmsDisplayitem.sortNo",  		enableCellEdit: true},
				             { field: 'addValue', 						colKey: "dmsDisplayitem.addValue", 		vKey: "dmsDisplayitem.addValue",  		enableCellEdit: true},
				             { field: 'startDt', 						colKey: "dmsDisplayitem.startDt",	width : 150, 	vKey: "dmsDisplayitem.startDt",  
				            	 periodStart : true,
				            	 enableCellHiddenInputEdit: true,
				            	 validators:{required:true},
				            	 cellClass : template.dateClass,
			       				 headerCellClass : 'edit_column',
			       				 type : 'datetime'},
				             { field: 'endDt', 							colKey: "dmsDisplayitem.endDt",		width : 150,	vKey: "dmsDisplayitem.endDt",    
			       					periodEnd : true,
					            	 enableCellHiddenInputEdit: true,
					            	 validators:{required:true},
					            	 cellClass : template.dateClass,
				       				 headerCellClass : 'edit_column',
				       				 type : 'datetime'},
				             { field: 'productTypeName', 				colKey: "pmsProduct.productTypeCd",  											enableCellEdit: false},
				             { field: 'saleStateName', 					colKey: "pmsProduct.saleStateCd",  												enableCellEdit: false},
				             { field: 'salePrice', 						colKey: "pmsProduct.salePrice",  												enableCellEdit: false},
			                 { field: 'brandName', 						colKey: "pmsBrand.name",  														enableCellEdit: false},
				             { field: 'stock', 							colKey: "pmsSaleproduct.realStockQty",  										enableCellEdit: false},
			                 { field: 'displayYn', 						colKey: "dmsDisplayitem.displayYn", 	vKey: "dmsDisplayitem.displayYn",   	enableCellEdit: true,  cellFilter: 'displayYnFilter' }
				         ];
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.EXHIBIT'){//기획전
			    	
			    	columnDefs = [
				             { field: 'displayItemId', 					colKey: "dmsDisplayitem.displayItemId", vKey: "dmsDisplayitem.displayItemId", 	enableCellEdit: true,  validators : {required :true},	linkFunction : 'openExhibitPopup'},
				             { field: 'exhibitName', 					colKey: "dmsExhibit.name",	linkFunction : 'openExhibitPopup'  													},
				             { field: 'exhibitStateName', 				colKey: "dmsExhibit.exhibitStateCd",     							 			},
				             { field: 'sortNo', 						colKey: "dmsDisplayitem.sortNo", 		vKey: "dmsDisplayitem.sortNo",  		enableCellEdit: true},
				             { field: 'addValue', 						colKey: "dmsDisplayitem.addValue", 		vKey: "dmsDisplayitem.addValue",  		enableCellEdit: true},
				             { field: 'startDt', 						colKey: "dmsDisplayitem.startDt",  	width : 150,	
				            	 periodStart : true,
				            	 enableCellHiddenInputEdit: true,
				            	 validators:{required:true},
				            	 cellClass : template.dateClass,
			       				 headerCellClass : 'edit_column',
			       				 type : 'datetime'},
				             { field: 'endDt', 							colKey: "dmsDisplayitem.endDt", 	width : 150,
		       					 periodEnd : true,
				            	 enableCellHiddenInputEdit: true,
				            	 validators:{required:true},
				            	 cellClass : template.dateClass,
			       				 headerCellClass : 'edit_column',
			       				 type : 'datetime'},
				             { field: 'exhibitStartDt', 				colKey: "dmsExhibit.startDt",  													},
				             { field: 'exhibitEndDt', 					colKey: "dmsExhibit.endDt",  													},
				             { field: 'displayYn', 						colKey: "dmsDisplayitem.displayYn", 	vKey: "dmsDisplayitem.displayYn",   	enableCellEdit: true,  cellFilter: 'displayYnFilter'}
				         ];		    	
			    	
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.IMG'){//이미지
			    	
			    	columnDefs = [{ field : 'displayItemNo', 	colKey : 'dmsDisplayitem.displayItemNo', linkFunction : 'openImgPopup'},
			    	              { field : 'text1',	colKey : "dmsDisplayitem.text1"},
			    	              { field : 'url1', 	colKey : "dmsDisplayitem.url1", width : '25%', linkFunction : 'linkUrl'},
			    	              { field : 'sortNo',	colKey : "dmsDisplayitem.sortNo",		vKey: "dmsDisplayitem.sortNo"},
			    	              { field: 'addValue', 	colKey: "dmsDisplayitem.addValue", 		vKey: "dmsDisplayitem.addValue",  		enableCellEdit: true},
						          { field: 'startDt', 	colKey: "dmsDisplayitem.startDt",	width : 150, 	vKey: "dmsDisplayitem.startDt",  enableCellEdit: true, type:'datetime', periodStart : true},
						          { field: 'endDt', 	colKey: "dmsDisplayitem.endDt",		width : 150,	vKey: "dmsDisplayitem.endDt",    enableCellEdit: true, type:'datetime', periodEnd : true },
			    	              { field : 'displayYn',	colKey: "dmsDisplayitem.displayYn", 	vKey: "dmsDisplayitem.displayYn",   	enableCellEdit: true,  cellFilter: 'displayYnFilter'}
			    	              ];	    	
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.HTML'){
			    	
			    	columnDefs = [{ field : 'title',    	colKey : "dmsDisplayitem.title", linkFunction : 'openHtmlPopup', validators : {required :true}},
			    	              { field : 'sortNo',	colKey : "dmsDisplayitem.sortNo",	vKey: "dmsDisplayitem.sortNo",	enableCellEdit: true},
			    	              { field: 'addValue', 		colKey: "dmsDisplayitem.addValue", 		vKey: "dmsDisplayitem.addValue",  		enableCellEdit: true},
						          { field: 'startDt', 						colKey: "dmsDisplayitem.startDt",	width : 150, 	vKey: "dmsDisplayitem.startDt",  enableCellEdit: true, type:'datetime', periodStart : true},
						          { field: 'endDt', 							colKey: "dmsDisplayitem.endDt",		width : 150,	vKey: "dmsDisplayitem.endDt",    enableCellEdit: true, type:'datetime', periodEnd : true },
			    	              { field : 'displayYn',	colKey: "dmsDisplayitem.displayYn", 	vKey: "dmsDisplayitem.displayYn",   	enableCellEdit: true,  cellFilter: 'displayYnFilter'}
			    	              ];
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.TEXT'){
			    	
			    	columnDefs = [{ field : 'title',    colKey : "dmsDisplayitem.title",enableCellEdit: true, validators : {required :true}},
			    	              { field : 'sortNo',	colKey : "dmsDisplayitem.sortNo",	vKey: "dmsDisplayitem.sortNo",	enableCellEdit: true},
			    	              { field : 'url1', 	displayName : "URL",  	width : '25%', linkFunction : 'linkUrl',	enableCellEdit: true},
			    	              { field: 'addValue', 	colKey: "dmsDisplayitem.addValue", 		vKey: "dmsDisplayitem.addValue",  		enableCellEdit: true},
						          { field: 'startDt', 	colKey: "dmsDisplayitem.startDt",	width : 150, 	vKey: "dmsDisplayitem.startDt",  
			    	            	  	periodStart : true,
						            	 enableCellHiddenInputEdit: true,
						            	 validators:{required:true},
						            	 cellClass : template.dateClass,
					       				 headerCellClass : 'edit_column',
					       				 type : 'datetime'},
						          { field: 'endDt', 	colKey: "dmsDisplayitem.endDt",		width : 150,	vKey: "dmsDisplayitem.endDt",    
			    	            		  periodEnd : true,
						            	 enableCellHiddenInputEdit: true,
						            	 validators:{required:true},
						            	 cellClass : template.dateClass,
					       				 headerCellClass : 'edit_column',
					       				 type : 'datetime'},
			    	              { field : 'displayYn',	colKey: "dmsDisplayitem.displayYn", 	vKey: "dmsDisplayitem.displayYn",   	enableCellEdit: true,  cellFilter: 'displayYnFilter'}
			    	              ];
			    }
				
				//상품 상세 팝업
				scope.openProductPopup = function(fieldValue, rowEntity){
					commonPopupService.openProductDetailPopup($scope, rowEntity.displayItemId);
				}
			    //기획전 상세 팝업
			    scope.openExhibitPopup = function(fieldValue, rowEntity){
					scope.exhibitId = rowEntity.displayItemId;
					popupwindow("/dms/exhibit/popup/detail", "기획전상세", 1200, 800);
			    }	
			    //HTML 상세 팝업
			    scope.openHtmlPopup = function(fieldValue, rowEntity){
			    	scope.search.displayId = rowEntity.displayId;
					scope.search.displayItemNo = rowEntity.displayItemNo;
					popupwindow("/dms/corner/popup/htmlDetail", "HTML 상세", 1200, 800);
			    }
			    //배너 상세 팝업
			    scope.openImgPopup = function(fieldValue, rowEntity){
			    	scope.search.displayId = rowEntity.displayId;
					scope.search.displayItemNo = rowEntity.displayItemNo;
					popupwindow("/dms/corner/popup/imageDetail", "배너 상세", 1000, 600);
			    }
			    
			    // Url 팝업
				scope.linkUrl = function(field, row) {
					popupwindow(row.url1, "배너 상세", 1000, 1000);
				}
			    return columnDefs;
			},
			// 전시 코너 아이템 공통 그리드 URL 조회
			getCornerItemUrl : function(displayItemTypeCd){
				var gridUrl = null;
			    if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.PRODUCT'){// 아이템 타입 : 상품
					gridUrl =  '/api/dms/corner/items/product';
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.EXHIBIT'){//기획전
			    	gridUrl =  '/api/dms/corner/items/exhibit';
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.IMG'){//이미지
			    	gridUrl =  '/api/dms/corner/items/img';
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.HTML'){
			    	gridUrl =  '/api/dms/corner/items/html';
			    }else if(displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.TEXT'){
			    	gridUrl =  '/api/dms/corner/items/text';
			    }
			    return gridUrl;
			}
	}
}).service("cornerService",  function(restFactory, productService){// 전시 코너 서비스
	return{
		getDisplayTreeList : function (callback) {
			var url = Rest.context.path+"/api/dms/corner/tree";
			restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, null, null, callback);
		},
		getCornerDetail : function (data, callback) {
			var url = Rest.context.path+"/api/dms/corner/:displayId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, null, data, callback);
		},
		getId : function(url) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, null, null, null);
		},
		insertCorner : function(data, callback){
			var url = Rest.context.path+"/api/dms/corner";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		checkDeleteCorner : function(data, callback){
			var url = Rest.context.path+"/api/dms/corner/:displayId/check";
			restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, null, data, callback);
		},
		deleteCorner : function(data, callback){
			var url = Rest.context.path+"/api/dms/corner/delete";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		updateCorner : function(data, callback){
			var url = Rest.context.path+"/api/dms/corner/"+data.displayId;
			restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, null, data, callback);
		},
/*		saveCornerItems : function(data, displayId, callback){// 전시 코너 아이템 저장
			var url = Rest.context.path+"/api/dms/corner/"+displayId+"/items";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},*/
		getCornerItems : function(data, displayId, callback){// 전시 코너 아이템 조회
			var url = Rest.context.path+"/api/dms/corner/items/search";
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		deleteCornerItems : function(data, callback){// 전시 코너 아이템 삭제
			var url = Rest.context.path+"/api/dms/corner/items/delete";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		getDisplayItem : function(data, callback) {
			var url = Rest.context.path+"/api/dms/corner/:displayId/item/:displayItemNo";
			restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, null, data, callback);		
		},
		saveCornerItem : function(data, callback){
			var url = Rest.context.path+"/api/dms/corner/item";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		}

	}
}).service("displayCategoryService",  function(restFactory){
	return{
		//전시 카테고리 트리 목록 조회
		getDisplayCategories : function (search, callback) {			
			var url = Rest.context.path + "/api/dms/category/tree";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, search, callback);
		},
		getDisplayCategoryList : function (callback) {			
			var url = Rest.context.path + "/api/dms/category/list";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, null, null, callback);
		},
		getDisplayCategoryCornerItems : function (data, callback) {			
			var url = Rest.context.path + "/api/dms/category/corner/item/check";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback); 
		},
		insertDisplayCategory : function(data, callback) {
			var url = Rest.context.path + "/api/dms/category/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback); 
		},
		updateDisplayCategoryInfo : function(data, callback) {
			var url = Rest.context.path + "/api/dms/category/update";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		deleteDisplayCategory : function(displayCategoryId, callback) {
			var url = Rest.context.path + "/api/dms/category/"+displayCategoryId+"/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, null, callback);
		}
	}
}).service("exhibitService",  function(restFactory, $http) {
	return{
		deleteExhibit : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/delete";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		updateExhibit : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/update";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		updateExhibitStatus : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/updateExhibitStatus";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		insertExhibit : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/insert";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		insertDivTitle : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/insertDivTitle";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		getDivTitleDetail : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/getDivTitleDetail";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		deleteDivTitle : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/deleteDivTitle";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		checkExhibitDivTitleProduct : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/checkExhibitDivTitleProduct";
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		checkExhibitCoupon : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/checkExhibitCoupon";
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		getExhibitProductList : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/exhibitProduct/list";
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		insertDivTitleProduct : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/divTitlePoduct/save";
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		getExhibitDetail : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/"+data;
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		deleteDivTitleProduct : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/deleteDivTitleProduct";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		insertExhibitMainProduct : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/insertExhibitMainProduct";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		getExhibitMainProductList : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/getExhibitMainProductList";
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		deleteBatchExhibitCoupon : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/coupon/deleteBatch";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		deleteBatchMainProduct : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/mainProduct/deleteBatch";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		deleteBatchExhibitOffshop : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/offshop/deleteBatch";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		getMainProductDetail : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/mainProduct/detail";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		checkMainProduct : function(data, callback) {
			var url = Rest.context.path+"/api/dms/exhibit/checkMainProduct";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		uploadExcel : function(file, callback) {
			var uploadUrl = Rest.context.path+"/api/ccs/common/excelFileUpload";
			var fd = new FormData();
			var decodedString = "";
			fd.append('file', file);
			
			$http.post(uploadUrl, fd, {
				withCredentials : false,
				transformRequest : angular.identity,
				headers : {
					'Content-Type' : undefined
				},
				responseType: "arraybuffer"
			}).success(function(response, status, headers, config) {
				var dataView = new DataView(response);
		        var decoder = new TextDecoder();
		        decodedString = decoder.decode(dataView);
		        callback(decodedString);
			}).error(function(error, status, headers, config) {
				alert("Fail");
				console.log(error.stack);
			});
		}
		
	}
}).service("catalogService",  function(restFactory){
	return {
		insertCatalog : function(data, callback) {
			var url = Rest.context.path + "/api/dms/catalog/insert";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		updateCatalog : function(data, callback) {
			var param = [];
			param.push(data);
			var url = Rest.context.path + "/api/dms/catalog/save";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, param, callback);
		},
		getCatalogDetail : function(data, callback) {
			var param = {catalogId : data};
			var url = Rest.context.path + "/api/dms/catalog/:catalogId";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, null, callback);
		},
		insertCatalogImg : function(data, callback) {
			var url = Rest.context.path + "/api/dms/catalog/img/insert";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		getCatalogImgDetail : function(data, callback) {
			var url = Rest.context.path + "/api/dms/catalog/img/catalogImgDetail";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		updateCatalogImg : function(data, callback) {
			var param = [];
			param.push(data);
			var url = Rest.context.path + "/api/dms/catalog/img/save";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, param, callback);
		},
		insertCatalogImgs : function(data, callback) {
			var param = data;
			var url = Rest.context.path + "/api/dms/catalog/multi/img/save";
			restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, param, callback);
		}
	}
});

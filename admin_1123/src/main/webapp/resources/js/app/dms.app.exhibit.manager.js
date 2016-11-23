// 기획전 화면 모듈
var exhibitApp = angular.module("exhibitApp", ["commonServiceModule", "ccsServiceModule", "dmsServiceModule", "pmsServiceModule", "gridServiceModule", "commonPopupServiceModule",
                                               "ui.date", "ngCkeditor"]);

//메시지
Constants.message_keys = ["common.label.alert.save", "dms.exhibit.oneday.date.validate", "common.label.confirm.save"];

// 기획전 관리
exhibitApp.controller("dms_exhibitManagerApp_controller", function($compile, $window, $scope, $filter, exhibitService, gridService, commonService, commonPopupService) {
	$window.$scope = $scope;
	$scope.exhibitSearch = {};
	
	var	columnDefs = [
	   	              	{ field: 'exhibitId'		, width: '10%'	, colKey: "dmsExhibit.exhibitId"	, enableCellEdit:false				, linkFunction:'detail'							},
			            { field: 'name'				, width: '15%'	, colKey: "dmsExhibit.name"			, vKey:"dmsExhibit.name"			, linkFunction:'detail'	 , enableCellEdit: false},
			            { field: 'exhibitTypeCd' 	, width: '10%'	, colKey: "c.dmsExhibit.type"		, vKey:"dmsExhibit.exhibitTypeName" , enableCellEdit:false 
			            							, dropdownCodeEditor : "EXHIBIT_TYPE_CD"			, cellFilter :'exhibitTypeFilter' 	, validators:{required:true}					},
			            { field: 'exhibitStateCd'	, width: '10%'	, colKey: "c.dmsExhibit.state"	, enableCellEdit: false
			            							, dropdownCodeEditor : "EXHIBIT_STATE_CD"			, cellFilter :'exhibitStateFilter' 	, validators:{required:true}					},
			            { field: 'sortNo'			, width: '10%'	, colKey: "c.dmsExhibit.sortNo"		, enableCellEdit: true				, type:'number'									},
			            { field: 'startDt'			, width: '15%'	, colKey: "dmsExhibit.startDt"		, vKey:"dmsExhibit.startDt" 														},
			            { field: 'endDt'			, width: '15%'	, colKey: "dmsExhibit.endDt"		, vKey:"dmsExhibit.endDt"															},
			            { field: 'insId'			, width: '10%'	, colKey: "c.grid.column.insId"		, enableCellEdit:false				, userFilter :'insId,insName'					},
			            { field: 'insDt'			, width: '15%'	, colKey: "c.grid.column.insDt"		, enableCellEdit:false																},
			            { field: 'updId'			, width: '10%'	, colKey: "c.grid.column.updId"		, enableCellEdit:false				, userFilter :'updId,updName'					},
			            { field: 'updDt'			, width: '15%'	, colKey: "c.grid.column.updDt"		, enableCellEdit:false																}
			         ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_exhibit",			//mandatory
			url :  '/api/dms/exhibit',  		//mandatory
			searchKey : "exhibitSearch",        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	$scope.productInfoType = [
	                   	{val : 'NAME',text : '상품명'},
	                    {val : 'PRODUCTID',text : '상품번호'}
		              ];
	$scope.exhibitInfoType = [
	                          {val : 'NAME',text : '기획전명'},
	                          {val : 'EXHIBITID',text : '기획전ID'}
	                          ];
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'exhibitSearch');
	});
	
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'exhibitSearch');
		angular.element(".day_group").find('button:first').addClass("on");
//		$scope.exhibitSearch.startDt ="";
//		$scope.exhibitSearch.endDt = "";
	}
	
	// 기획전 리스트 조회
	this.getExhibitList = function() {
		if ($scope.exhibitSearch.exhibitIds) {
			$scope.exhibitSearch.scExhibitIds = $scope.exhibitSearch.exhibitIds.split(",");
		}
		$scope.myGrid.loadGridData();
	}
	
	// 기획전 등록 팝업
	this.addExhibit = function() {
		$scope.exhibitId = "";
		var winName='기획전 등록';
		var winURL = Rest.context.path +"/dms/exhibit/popup/detail";
		popupwindow(winURL,winName,1250,800);
	}
	
	$scope.detail = function(field, row) {
		$scope.exhibitId = row.exhibitId;
		var winName='기획전 상세';
		var winURL = Rest.context.path +"/dms/exhibit/popup/detail";
		popupwindow(winURL,winName,1250,800);
	}
	
	// 기획전 삭제
	this.deleteExhibit = function() {
		var checkList = [];
		var checkList = $scope.myGrid.getSelectedRows();
		var delFlag = true;
		for (var i=0; i < checkList.length; i++) {
			if (checkList[i].exhibitStateCd != 'EXHIBIT_STATE_CD.READY') {
				delFlag = false;
				break;
			}
		}
		
		if (delFlag) {
			$scope.myGrid.deleteGridData();
		} else {
			alert("대기 상태가 아닌 기획전이 포함되어 삭제가 불가합니다.");
		}
	}
	
// 기획전 상세
}).controller("dms_exhibitDetailPopApp_controller", function($window, $scope, $compile,ccsService, commonService, commonPopupService, displayService, gridService, exhibitService) {
	$window.$scope = $scope;
	var pScope = $window.opener.$scope;//부모창의 scope
	$scope.couponSearch = {};
	$scope.mainProductSearch = {};
	$scope.offshopSearch = {};
	$scope.dmsExhibit = {};
	var selectedDaysWeek = [];
	$scope.ckOption = {
			language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		  , filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	// 쿠폰
	var	couponColumn = [
			   	            { field: 'couponId'					, colKey:"c.sps.coupon.coupon.id"	, linkFunction:'couponDetail', aggregationHideLabel: true	},
				            { field: 'name'						, colKey:"spsCoupon.name"																		},
				            { field: 'issueStartDt'				, colKey:"spsCoupon.issueStartDt"	, cellFilter: "date:\'yyyy-MM-dd\'"							},
				            { field: 'issueEndDt'				, colKey:"spsCoupon.issueEndDt"		, cellFilter: "date:\'yyyy-MM-dd\'"							},
				            { field: 'insDt'					, colKey:"c.grid.column.insDt"		, cellFilter: "date:\'yyyy-MM-dd\'"							},
				            { field: 'insId'					, colKey:"c.grid.column.insId"		, userFilter :'insId,insName'								},
				            { field: 'updDt'					, colKey:"c.grid.column.updDt"		, cellFilter: "date:\'yyyy-MM-dd\'"							},
				            { field: 'updId'					, colKey:"c.grid.column.updId"		, userFilter :'updId,updName'								}
			            ];
	
	var gridParam1 = {
			scope : $scope, 					//mandatory
			gridName : "grid_coupon",			//mandatory
			url :  '/api/dms/exhibit/coupon',	//mandatory
			searchKey : "couponSearch",   		//mandatory
			columnDefs : couponColumn,    		//mandatory
			gridOptions : {             		//optional
				pagination : false
			},callbackFn : function () {
			}
	};
	
	$scope.myGrid1 = new gridService.NgGrid(gridParam1);
	
	// 대표상품
	var	mainProductColumn = [
	   	                	{ field: 'productId' 					, colKey: "c.dmsExhibitproduct.productId" 		 , linkFunction:'productDetail'		},
				            { field: 'pmsProduct.name'				, colKey: "c.dmsExhibitproduct.name" 			 , linkFunction:'productDetail'		},
				            { field: 'name'							, colKey: "c.dmsExhibitproduct.mainProduct.name" , vKey:"dmsExhibitmainproduct.name", linkFunction:'mainProductDetail'},
				            { field: 'img'							, colKey: "c.dmsExhibitproduct.mainProduct.img"	 									},
				            { field: 'sortNo'						, colKey: "c.dmsExhibitproduct.sortNo"	 		 									},
				            { field: 'pmsProduct.productTypeName'	, colKey: "c.dmsExhibitproduct.productTypeCd" 	 , vKey:"pmsProduct.productTypeName"	},
				            { field: 'pmsProduct.saleStateName'		, colKey: "c.dmsExhibitproduct.saleStateCd"	 	 , vKey:"pmsProduct.saleStateName"	},
				            { field: 'pmsProduct.salePrice'			, colKey: "c.dmsExhibitproduct.salePrice" 		 									},
				            { field: 'pmsProduct.brandName'			, colKey: "c.dmsExhibitproduct.brandName" 		 , vKey:"pmsProduct.brandName"		},
				            { field: 'pmsProduct.saleStartDt'		, colKey: "pmsProduct.saleStartDt" 				 , cellFilter: "date:\'yyyy-MM-dd\'"	},
				            { field: 'pmsProduct.saleEndDt'			, colKey: "pmsProduct.saleEndDt" 				 , cellFilter: "date:\'yyyy-MM-dd\'"	},
	   	                ];
	
	var gridParam2 = {
			scope : $scope, 					//mandatory
			gridName : "grid_mainProduct",		//mandatory
			url :  '/api/dms/exhibit/mainProduct',	//mandatory
			searchKey : "mainProductSearch", 	//mandatory
			columnDefs : mainProductColumn,		//mandatory
			gridOptions : {             		//optional
				pagination : false
			},callbackFn : function () {
			}
	};
	
	$scope.myGrid2 = new gridService.NgGrid(gridParam2);
	
	// 오프라인매장
	var	offshopColumn = [
	   	                     { field: 'offshopId'		, colKey: "c.ccs.offShop.offShopId"			},
	   	                     { field: 'offshopTypeName'	, colKey: "c.ccs.offShop.offShopTypeCd"		},
	   	                     { field: 'name'			, colKey: "c.ccs.offShop.name"				},
	   	                     { field: 'offshopBrands'	, colKey: "c.ccs.offShop.brand"				},
	   	                     { field: 'address'			, colKey: "c.ccs.offShop.address"			}
	   	                     
	   	                     
	   	                     ];
	
	var gridParam3 = {
			scope : $scope, 					//mandatory
			gridName : "grid_offshop",		//mandatory
			url :  '/api/dms/exhibit/offshop',	//mandatory
			searchKey : "offshopSearch", 	//mandatory
			columnDefs : offshopColumn,		//mandatory
			gridOptions : {             		//optional
				pagination : false
			},callbackFn : function () {
			}
	};
	
	$scope.myGrid3 = new gridService.NgGrid(gridParam3);
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	this.moveTab = function($event, param) {
		if ("title" == param) {
			if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
				$window.location.href = Rest.context.path +"/dms/exhibit/popup/divTitleInfo";
			}
		} 
	}
	
	$window.onload = function(e) {
		var exhibitId = pScope.exhibitId;
		if (exhibitId != '' && exhibitId != undefined) {
			// 기획전 상세 조회
			exhibitService.getExhibitDetail(exhibitId, function(response) {
				$scope.dmsExhibit = response;
				
				// 요일 세팅
				if ($scope.dmsExhibit.daysWeek != '' && $scope.dmsExhibit.daysWeek != null) {
					var daysWeek = [];
					daysWeek = $scope.dmsExhibit.daysWeek.split(",");
					if (daysWeek.length == 7) {
						$scope.dmsExhibit["daysWeek_all"] = true;
					}
					for (var i = 0; i < daysWeek.length; i++) {
						$scope.dmsExhibit["daysWeek_" + daysWeek[i]] = true;
						selectedDaysWeek.push(daysWeek[i]);
						common.safeApply($scope);
					}
				}
				
				// 전시카테고리
				$scope.dmsExhibit.displayCategoryName = response.dmsExhibitdisplaycategorys[0].displayCategoryName;
				$scope.dmsExhibit.displayCategoryId = response.dmsExhibitdisplaycategorys[0].displayCategoryId;
			
				$scope.brandCnt = response.dmsExhibitbrands.length;
				
				// 브랜드
				for (var i = 0; i < response.dmsExhibitbrands.length; i++) {
					if (i == 0) {
						$scope.dmsExhibit.brandId0 = response.dmsExhibitbrands[0].brandId;
						$scope.dmsExhibit.brandName0 = response.dmsExhibitbrands[0].brandName;
					} else {
						$scope.dmsExhibit["brandId"+i] = response.dmsExhibitbrands[i].brandId;
						$scope.dmsExhibit["brandName"+i] = response.dmsExhibitbrands[i].brandName;
						
						var strHtml = '<div><input type="text" data-ng-model="dmsExhibit.brandId'+i+'" style="width:29%;" readonly disabled index=""/> <input type="text" data-ng-model="dmsExhibit.brandName'+i+'" style="width:30%;" readonly disabled/> ';
						strHtml += ' <button type="button" class="btn_type2" ng-click="ctrl.brandSearch($event)" data-index="'+i+'"><b>검색</b></button>';
						strHtml += ' <button type="button" class="btn_eraser" ng-class="{&#39;btn_eraser_disabled&#39; : dmsExhibit.brandId'+i+' == null || dmsExhibit.brandId'+i+' == &#39;&#39;}" ng-click="ctrl.deleteBrand('+i+')">지우개</button>';
						strHtml += ' <button type="button" class="btn_minus" ng-click="ctrl.remBrandField('+i+')">추가</button></div>';
						angular.element(document.querySelector('#td_brand')).append($compile(strHtml)($scope));
					}
				}
				
				if ($scope.dmsExhibit.exhibitTypeCd =='EXHIBIT_TYPE_CD.ONEDAY') {
					$scope.mainProductSearch.exhibitId = exhibitId;
					$scope.myGrid2.loadGridData();
				} else if ($scope.dmsExhibit.exhibitTypeCd =='EXHIBIT_TYPE_CD.COUPON') {
					$scope.couponSearch.exhibitId = exhibitId;
					$scope.myGrid1.loadGridData();
				} else if ($scope.dmsExhibit.exhibitTypeCd =='EXHIBIT_TYPE_CD.OFFSHOP') {
					$scope.offshopSearch.exhibitId = exhibitId;
					$scope.myGrid3.loadGridData();
				}
				$scope.dmsExhibit.ordExhibitTypeCd = $scope.dmsExhibit.exhibitTypeCd;
				
				if ($scope.dmsExhibit.exhibitStateCd == 'EXHIBIT_STATE_CD.RUN') {
					angular.element('[ng-model="dmsExhibit.exhibitTypeCd"]').find('input').attr('disabled', true);
				}
				
			});
		} else {
			var daysweek = "";
			$scope.dmsExhibit["daysWeek_all"] = true;
			for (var i=1; i <= 7; i++) {
				selectedDaysWeek.push(i+1);
				daysweek += i+ ","
				$scope.dmsExhibit["daysWeek_"+i] = true;
			}
			$scope.dmsExhibit["daysWeek_all"] = true;
			$scope.dmsExhibit.daysWeek = daysweek.slice(0,-1);
		}
	}
	
	
	// INIT
	this.init = function() {
		
	}
	
	// 기획전 대표 상품 조회
	this.getExhibitMainProductList = function() {
		$scope.myGrid2.loadGridData();
	}
	
	// 제한 설정 팝업 open
	this.restrictPop = function() {
		commonPopupService.restrictPopup($scope, true, true, true, true);
	}
	
	// 전시 카테고리 검색 팝업
	this.categorySearch = function() {
		$scope.exhibit = true;
		commonPopupService.categoryPopup($scope, "callback_category", false, '/dms/displayCategory/popup/search');
	}
	
	$scope.callback_category = function(data) {
		console.log(data);
		$scope.dmsExhibit.displayCategoryName = data.name;
		$scope.dmsExhibit.displayCategoryId = data.displayCategoryId;
		$scope.$apply();
	}
	
	// 브랜드 검색 팝업
	this.brandSearch = function($event) {
		$scope.brandSearchIndex = $event.currentTarget.attributes["data-index"].value;
		commonPopupService.brandPopup($scope, "callback_brand", false);
	}
	
	var brandIds = [];
	// 브랜드 callback
	$scope.callback_brand = function(data) {
		
		var index = $scope.brandSearchIndex;
		var flag = true;
		for (var i=0; i < index; i++) {
			if ($scope.dmsExhibit["brandId"+i] == data[0].brandId) {
				flag = false;
				break;
			}
		}
		
		if (flag) {
			var brandId = "brandId" + index;
			var brandName = "brandName" + index;
			$scope.dmsExhibit[brandId] = data[0].brandId;
			$scope.dmsExhibit[brandName] = data[0].name;
			$scope.$apply();
		}
		var divCnt = angular.element(document.querySelector('#td_brand')).find('div').length;
		brandIds = [];
		for (var i=0; i < divCnt; i++) {
			var brandId = $scope.dmsExhibit["brandId"+i];
			if (brandId != '' && brandId != null) {
				brandIds.push(brandId);
			}
		}
		$scope.dmsExhibit.brandIds = brandIds;
	}
	
	// 카테고리 데이터 지우기
	this.deleteCategory = function() {
		$scope.dmsExhibit.displayCategoryId = "";
		$scope.dmsExhibit.displayCategoryName = "";
	}
	
	// 브랜드 데이터 지우기
	this.deleteBrand = function(index) {
		var brandId = $scope.dmsExhibit["brandId"+index];
		$scope.dmsExhibit["brandId"+index] = "";
		$scope.dmsExhibit["brandName"+index] = "";
		
		var index = brandIds.indexOf(brandId);
		brandIds.splice(index, 1);
	}
	
	// 브랜드 필드 삭제
	this.remBrandField = function(index) {
		var brandId = $scope.dmsExhibit["brandId"+index];
		$scope.dmsExhibit["brandId"+index] = "";
		$scope.dmsExhibit["brandName"+index] = "";
		
		var index = brandIds.indexOf(brandId);
		brandIds.splice(index, 1);
		angular.element(document.querySelector('#td_brand')).find('div').eq(index).remove();
	}
	
	// 브랜드 input 필드 추가
	this.addBrand = function() {
		
		var flag = true;
		var divCnt = angular.element(document.querySelector('#td_brand')).find('div').length;
		console.log("divCnt", divCnt);
		var dataIndex = angular.element(document.querySelector('#td_brand')).find('div').last().find('.btn_type2').data("index"); 
		console.log("dataIndex", dataIndex);
		for (var i=0; i < divCnt; i++) {
			if (angular.element(document.querySelector('#td_brand')).find('div').eq(i).find('input').eq(0).val() == "" 
				|| angular.element(document.querySelector('#td_brand')).find('div').eq(i).find('input').eq(0).val() == undefined 
				|| angular.element(document.querySelector('#td_brand')).find('div').eq(i).find('input').eq(0).val() == 'undefined') {
				console.log(flag);
				flag = false;
				break;
			}
		}
		if (flag) {
			var strHtml = '<div><input type="text" data-ng-model="dmsExhibit.brandId'+Number(dataIndex + 1)+'" style="width:29%;" readonly disabled index=""/> <input type="text" data-ng-model="dmsExhibit.brandName'+Number(dataIndex + 1)+'" style="width:30%;" readonly disabled/> ';
			strHtml += ' <button type="button" class="btn_type2" ng-click="ctrl.brandSearch($event)" data-index="'+Number(dataIndex + 1)+'"><b>검색</b></button>';
			strHtml += ' <button type="button" class="btn_eraser" ng-class="{&#39;btn_eraser_disabled&#39; : dmsExhibit.brandId'+Number(dataIndex + 1)+' == null || dmsExhibit.brandId'+Number(dataIndex + 1)+' == &#39;&#39;}" ng-click="ctrl.deleteBrand('+Number(dataIndex + 1)+')">지우개</button>';
			strHtml += ' <button type="button" class="btn_minus" ng-click="ctrl.remBrandField('+Number(dataIndex + 1)+')">추가</button></div>';
			angular.element(document.querySelector('#td_brand')).append($compile(strHtml)($scope));
		}
	}
	
	// 쿠폰 검색 팝업
	this.addCoupon = function() {
		commonPopupService.couponPopup($scope, "callback_coupon", true);
	}
	// 쿠폰 팝업 callBack
	$scope.callback_coupon = function(data) {
		var couponIds = [];
		for (var i=0; i<data.length; i++ ) {
			couponIds.push(data[i].couponId);
		}
		$scope.couponSearch.couponIds = couponIds;
		$scope.couponSearch.exhibitId = pScope.exhibitId;
		
		var duplicateCnt = 0;
		for (var i=0; i<data.length; i++ ) {
			var flag = true;
			for (var j = 0; j < $scope.grid_coupon.data.length; j++) {
				if ($scope.grid_coupon.data[j].couponId == data[i].couponId) {
					flag = false;
					duplicateCnt++;
					break;
				}
			}
			if (flag) {
				$scope.myGrid1.addRow({
					couponId  : data[i].couponId,
					insId     : data[i].insId,
					insDt 	  : data[i].insDt,
					updId     : data[i].updId,
					updDt 	  : data[i].updDt,
					exhibitId : pScope.exhibitId,
					crudType  : 'I',
					name : data[i].name,
					issueStartDt : data[i].issueStartDt,
					issueEndDt : data[i].issueEndDt
				});
			}
		}
		if (duplicateCnt > 0) {
			alert("이미 등록된 쿠폰을 제외하고 추가 되었습니다.");
		}
		$scope.$apply();
	}
	
	$scope.couponDetail = function(field, row) {
		$scope.couponId = row.couponId;
		var url= Rest.context.path + '/sps/coupon/popup/detail';
		popupwindow(url,"couponDetail",1100,600);
	}
	
	// 대표 상품 등록
	this.addMainProduct = function() {
		var winName='Main Product Register';
		var winURL = Rest.context.path +"/dms/exhibit/popup/mainProductInsert";
		popupwindow(winURL,winName,1100,430);
	}
	
	// 상품 상세 팝업
	$scope.productDetail = function(field, row) {
		commonPopupService.openProductDetailPopup($scope, row.productId);
	}
	
	// 대표 상품 상세 팝업
	$scope.mainProductDetail = function(field, row) {
		$scope.dmsExhibit.mainProductId = row.productId;
		$scope.dmsExhibit.productName = row.pmsProduct.name;
		var url= Rest.context.path + '/dms/exhibit/popup/mainProductDetail';
		popupwindow(url,"대표상품상세",1100,420);
	}
	
	this.addRowMainProduct = function(data) {
		$scope.myGrid2.addRow({
			productId : data.productId,
			name	  : data.name,
			img		  : data.img,
			sortNo	  : data.sortNo,
			exhibitId : pScope.exhibitId,
			insDt 	  : "",
			updId     : "",
			updDt 	  : "",
			pmsProduct : {name : data.productName, productTypeName : data.productTypeName, 
						  saleStateName : data.saleStateName, salePrice : data.salePrice, brandName : data.brandName,
						  saleStartDt : data.saleStartDt, saleEndDt : data.saleEndDt},
		});
	}
	
	// 기획전 상세 저장
	this.saveExhibit = function() {
		
		//폼 체크
		if (!commonService.checkForm($scope.exhibitDetailForm)) {
			return;
		}
		
		if ("EXHIBIT_TYPE_CD.COUPON" == $scope.dmsExhibit.exhibitTypeCd) {
			if ($scope.grid_coupon.data.length == 0) {
				alert("유효하지 않는 항목이 있습니다.");
				return;
			}
		} else if ("EXHIBIT_TYPE_CD.ONEDAY" == $scope.dmsExhibit.exhibitTypeCd) {
			if ($scope.grid_mainProduct.data.length == 0) {
				alert("유효하지 않는 항목이 있습니다.");
				return;
			}
		} else if ("EXHIBIT_TYPE_CD.OFFSHOP" == $scope.dmsExhibit.exhibitTypeCd) {
			if ($scope.grid_offshop.data.length == 0) {
				alert("유효하지 않는 항목이 있습니다.");
				return;
			}
		}
		
		if (window.confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			
			if ("EXHIBIT_TYPE_CD.COUPON" == $scope.dmsExhibit.exhibitTypeCd) {
				$scope.dmsExhibit.dmsExhibitcoupons = $scope.myGrid1.getData();
			} else if ("EXHIBIT_TYPE_CD.ONEDAY" == $scope.dmsExhibit.exhibitTypeCd) {
				$scope.dmsExhibit.dmsExhibitmainproducts = $scope.myGrid2.getData();
				$scope.dmsExhibit.daysWeek = "";
			} else if ("EXHIBIT_TYPE_CD.OFFSHOP" == $scope.dmsExhibit.exhibitTypeCd) {
				$scope.dmsExhibit.dmsExhibitoffshops = $scope.myGrid3.getData();
			}
			
			if (pScope.exhibitId == '' || pScope.exhibitId == undefined || pScope.exhibitId == null) {
				$scope.dmsExhibit.exhibitStateCd = 'EXHIBIT_STATE_CD.READY';
				exhibitService.insertExhibit($scope.dmsExhibit, function(response) {
					alert(pScope.MESSAGES["common.label.alert.save"]);
					pScope.exhibitId = response.content;
					$window.location.href = Rest.context.path +"/dms/exhibit/popup/detail";
				});
			} else {
				exhibitService.updateExhibit($scope.dmsExhibit, function(response) {
					alert(pScope.MESSAGES["common.label.alert.save"]);
					$window.location.reload();
				});
			}
			
		}
	}
	
	// 기획전 상세 닫기
	this.close = function() {
		$window.close();
	}
	
	this.clickExhibitType = function($event) {
		if ($scope.dmsExhibit.ordExhibitTypeCd != "" && $scope.dmsExhibit.ordExhibitTypeCd != $scope.dmsExhibit.exhibitTypeCd) {
			var result;
			if ($scope.dmsExhibit.ordExhibitTypeCd == 'EXHIBIT_TYPE_CD.ONEDAY' && $scope.myGrid2.getData().length > 0 
					|| $scope.dmsExhibit.ordExhibitTypeCd == 'EXHIBIT_TYPE_CD.COUPON' && $scope.myGrid1.getData().length > 0
					|| $scope.dmsExhibit.ordExhibitTypeCd == 'EXHIBIT_TYPE_CD.OFFSHOP' && $scope.myGrid3.getData().length > 0) {
				result = confirm("기획전 유형을 변경시 등록되어 있는 항목은 초기화 됩니다. 변경 하시겠습니까?");
			} else {
				result = true;
			}
			if (result == true) {
				
				if ($scope.dmsExhibit.ordExhibitTypeCd == 'EXHIBIT_TYPE_CD.ONEDAY') {
					exhibitService.deleteBatchMainProduct($scope.dmsExhibit, function() {
						$scope.myGrid2.setData([], false);
					});
				} else if ($scope.dmsExhibit.ordExhibitTypeCd == 'EXHIBIT_TYPE_CD.COUPON') {
					exhibitService.deleteBatchExhibitCoupon($scope.dmsExhibit, function() {
						$scope.myGrid1.setData([], false);
					});
				} else if ($scope.dmsExhibit.ordExhibitTypeCd == 'EXHIBIT_TYPE_CD.OFFSHOP') {
					exhibitService.deleteBatchExhibitOffshop($scope.dmsExhibit, function() {
						$scope.myGrid3.setData([], false);
					});
				}
				
				$scope.dmsExhibit.ordExhibitTypeCd ="";
				
			} else {
				$scope.dmsExhibit.exhibitTypeCd = $scope.dmsExhibit.ordExhibitTypeCd;
			}
		}
	}
	

	// 요일 클릭
	this.daysweekClick = function($event) {
		var id = $event.currentTarget.id;
		
		if (id=="all") {
			if ($event.currentTarget.checked) {
				selectedDaysWeek = [];
				selectedDaysWeek = ["1","2","3","4","5","6","7"]; // 오라클 요일
				
				for (var i = 0; i < angular.element(document.querySelector('#daysweek')).find("input").length; i++) {
					$scope.dmsExhibit["daysWeek_"+selectedDaysWeek[i]] = true;
				}
			} else {
				selectedDaysWeek = ["1","2","3","4","5","6","7"];
				for (var i = 0; i < angular.element(document.querySelector('#daysweek')).find("input").length; i++) {
					$scope.dmsExhibit["daysWeek_"+selectedDaysWeek[i]] = false;
				}
				selectedDaysWeek = [];
			}
		} else {
			if (angular.element(document.querySelector('#daysweek')).find("input").eq(0).is(":checked")) {
				$scope.dmsExhibit["daysWeek_all"] = false;
			} else {
				if (angular.element(document.querySelector('#daysweek')).find("input:checked").length == 7) {
					$scope.dmsExhibit["daysWeek_all"] = true;
				}
			}
			
			var index = selectedDaysWeek.indexOf(id);
			if (index >= 0) {
				selectedDaysWeek.splice(index, 1);
			} else {
				selectedDaysWeek.push(id);
			}
		}
		var daysweek = "";
		for(var i=0; i<selectedDaysWeek.length; i++) {
			daysweek += selectedDaysWeek[i] + ",";
		}
		
		$scope.dmsExhibit.daysWeek = daysweek.slice(0,-1);
		
		console.log("$scope.dmsExhibit.daysWeek", $scope.dmsExhibit.daysWeek);
		console.log("selectedDaysWeek", selectedDaysWeek);
	}
	
	$scope.uploadPcImageCallback = function(path) {
		$scope.dmsExhibit.img1 = path;
	}
	
	$scope.uploadMbImageCallback = function(path) {
		$scope.dmsExhibit.img2 = path;
	}
	
	// 이미지 삭제
	this.deletePcImage = function() {
		//신규 템프 파일은 바이너리 바로 삭제
		commonService.deleteFile($scope.dmsExhibit.img1, function(data){
			//모델에서 삭제
			$scope.dmsExhibit.img1 = "";
	    });
	}
	
	// 이미지 삭제
	this.deleteMbImage = function() {
		//신규 템프 파일은 바이너리 바로 삭제
		commonService.deleteFile($scope.dmsExhibit.img2, function(data){
			//모델에서 삭제
			$scope.dmsExhibit.img2 = "";
		});
	}
	
	// 기획전 상태 변경
	this.changeState = function(param) {
		var msg = ""
		var preState = "";
		var afterState = "";
		
		$scope.chgExhibit = {};
		$scope.chgExhibit.exhibitId = $scope.dmsExhibit.exhibitId;
		
		if (param == 'run') {
			$scope.chgExhibit.exhibitStateCd = 'EXHIBIT_STATE_CD.RUN';
			preState = "대기";
			afterState = "진행";
		} else if (param == 'stop') {
			$scope.chgExhibit.exhibitStateCd = 'EXHIBIT_STATE_CD.STOP';
			preState = "진행";
			afterState = "중지";
		}
		
		msg = "'"+ preState +"상태'를 '" + afterState + "상태' (으)로 변경하시겠습니까?";
			
		if (confirm(msg)) {
			exhibitService.updateExhibitStatus($scope.chgExhibit, function() {
				alert("'"+afterState+"상태'(으)로 변경되었습니다.");
				pScope.myGrid.loadGridData();
				$window.location.reload();
			});
		}
	}
	
	// 오프라인 매장 찾기
	this.addOffshop = function() {
		commonPopupService.offshopPopup($scope, "callback_offshop", true);
	}
	
	var duplicateCnt = 0;
	$scope.callback_offshop = function(data) {
		for(var i=0; i < data.length; i++) {
			var insertFlag = true;
			for(var j=0; j < $scope.grid_offshop.data.length; j++) {
				if (data[i].offshopId == $scope.grid_offshop.data[j].offshopId) {
					insertFlag = false;
					duplicateCnt++;
					break;
				}
			}
			if (insertFlag) {
				$scope.myGrid3.addRow({
					offshopId 		: data[i].offshopId,
					offshopTypeCd 	: data[i].offshopTypeCd,
					offshopTypeName	: data[i].offshopTypeName,
					name		 	: data[i].name,
					offshopBrands 	: data[i].offshopBrands,
					address		  	: data[i].address,
					exhibitId : pScope.exhibitId,
				});
			}
			if (duplicateCnt > 0) {
				alert("이미 등록된 매장을 제외하고 추가 되었습니다.");
			}
		}
	}
	
	// 오프라인 매장 일괄 등록
	this.batchOffshop = function() {
		commonPopupService.gridbulkuploadPopup($scope,"excelUpload_callback","offshop");
	}
	
	$scope.excelUpload_callback = function(response) {
		var duplicateCnt = 0;
		var data = response.resultList;
		for(var i=0; i < data.length; i++) {
			var insertFlag = true;
			for(var j=0; j < $scope.grid_offshop.data.length; j++) {
				if (data[i].ccsOffshop.offshopId == $scope.grid_offshop.data[j].offshopId) {
					insertFlag = false;
					duplicateCnt++;
					break;
				}
			}
			if (insertFlag) {
				$scope.myGrid3.addRow({
					offshopId 		: data[i].ccsOffshop.offshopId,
					offshopTypeCd 	: data[i].ccsOffshop.offshopTypeCd,
					offshopTypeName	: data[i].ccsOffshop.offshopTypeName,
					name		 	: data[i].ccsOffshop.name,
					offshopBrands 	: data[i].ccsOffshop.offshopBrands,
					address		  	: data[i].ccsOffshop.address,
					exhibitId : pScope.exhibitId
				});
			}
		}
		if (duplicateCnt > 0) {
			alert("이미 등록된 매장을 제외하고 추가 되었습니다.");
		}
	}
	
// 구분타이틀 팝업		
}).controller("dms_exhibitDivTitleInfoPopApp_controller", function($window, $scope, $compile,ccsService, commonPopupService, commonService, gridService, exhibitService) {
	$window.$scope = $scope;
	pScope = $window.opener.$scope;//부모창의 scope
	
	$scope.divTitSearch = {};
	$scope.productSearch = {};
	
	// 구분 타이틀 GRID
	var cellTemplateStr = "<U><a href='#' data-ng-click='grid.appScope.detail(row)' >{{COL_FIELD}}</a></U>";	
	var	divTitleColumn1 = [
	   	                    { field: 'groupNo'					, width: '10%'	, colKey: "dmsExhibitgroup.groupNo"					, enableCellEdit:false  , linkFunction:'divTitleDetailPop' 										},
		   	              	{ field: 'name'						, width: '15%'	, colKey: "c.dmsExhibitgroup.name"					, enableCellEdit:true	, linkFunction:'divTitleDetailPop' , vKey:"dmsExhibitgroup.name"		},
				            { field: 'groupTypeCd'				, width: '10%'	, colKey: "c.dmsExhibitgroup.groupType"				, enableCellEdit:true,	dropdownCodeEditor : "GROUP_TYPE_CD", cellFilter : "groupTypeFilter" , validators:{required:true}},
				            { field: 'sortNo'					, width: '10%'	, colKey: "dmsExhibitgroup.sortNo"					, enableCellEdit:true	, type:"number"														},
				            { field: 'url1'						, width: '10%'	, colKey: "c.dmsExhibitgroup.pcUrl"					, enableCellEdit:true																		},
				            { field: 'url2'						, width: '10%'	, colKey: "c.dmsExhibitgroup.mbUrl"					, enableCellEdit:true																		},
				            { field: 'displayYn'				, width: '10%'	, colKey: "dmsExhibitgroup.displayYn"				, enableCellEdit:true,  cellFilter : "displayYnFilter"	},
				            { field: 'productDisplayType1Cd'	, width: '10%'	, colKey: "c.dmsExhibitgroup.productDisplayType1Cd"	, enableCellEdit:true, 	dropdownCodeEditor : "PRODUCT_DISPLAY_TYPE1_CD", cellFilter :'productDisplayType1Filter'	, validators:{required:true}},
				            { field: 'productDisplayType2Cd'	, width: '10%'	, colKey: "c.dmsExhibitgroup.productDisplayType2Cd"	, enableCellEdit:true, 	dropdownCodeEditor : "PRODUCT_DISPLAY_TYPE2_CD", cellFilter :'productDisplayType2Filter'	, validators:{required:true}},
				            { field: 'insDt'					, width: '15%'	, colKey: "c.grid.column.insDt"						, enableCellEdit:false	, cellFilter: "date:\'yyyy-MM-dd\'"									},
				            { field: 'insId'					, width: '10%'	, colKey: "c.grid.column.insId"						, enableCellEdit:false	, userFilter :'insId,insName'										},
				            { field: 'updDt'					, width: '15%'	, colKey: "c.grid.column.updDt"						, enableCellEdit:false	, cellFilter: "date:\'yyyy-MM-dd\'"									},
				            { field: 'updId'					, width: '10%'	, colKey: "c.grid.column.updId"						, enableCellEdit:false	, userFilter :'updId,updName'										}
			             ];
	
	var gridParam1 = {
			scope : $scope, 					//mandatory
			gridName : "grid_divTitle",			//mandatory
			url :  '/api/dms/exhibit/divTitle',	//mandatory
			searchKey : "divTitSearch",   		//mandatory
			columnDefs : divTitleColumn1,    	//mandatory
			gridOptions : {             		//optional
				// row 선택시 호출 함수 정의
				enableRowSelection: true,
				noUnselect : true,
				multiSelect: false,
				pagination : false,
				
				rowSelectionFn : function(row){
					$scope.divTitleDetail(row.entity);
				}
			},callbackFn : function () {
				$scope.divTitSearch.exhibitId = pScope.exhibitId;
				$scope.myGrid1.loadGridData();
			}
	};
	$scope.myGrid1 = new gridService.NgGrid(gridParam1);
	
	// 구분 타이틀 상품
	var	productColumn2 = [
		   	              	{ field: 'pmsProduct.productId'			, width: '10%'	, colKey: "c.dmsExhibitproduct.productId"		, enableCellEdit:false	, linkFunction:'productDetail'		},
				            { field: 'pmsProduct.name'				, width: '15%'	, colKey: "c.dmsExhibitproduct.name"			, enableCellEdit:false	, linkFunction:'productDetail'									},
				            { field: 'sortNo'						, width: '10%'	, colKey: "c.dmsExhibitproduct.sortNo"			, enableCellEdit:true	, type:"number"									},
				            { field: 'displayYn'					, width: '10%'	, colKey: "c.dmsExhibitproduct.displayYn"		, enableCellEdit:true 	, cellFilter: 'displayYnFilter'		},
				            { field: 'pmsProduct.productTypeName'	, width: '10%'	, colKey: "c.dmsExhibitproduct.productTypeCd"	, enableCellEdit:false										},
				            { field: 'pmsProduct.saleStateName'		, width: '10%'	, colKey: "c.dmsExhibitproduct.saleStateCd"		, enableCellEdit:false										},
				            { field: 'pmsProduct.salePrice'			, width: '10%'	, colKey: "c.dmsExhibitproduct.salePrice"		, enableCellEdit:false										},
				            { field: 'pmsProduct.brandName'			, width: '15%'	, colKey: "c.dmsExhibitproduct.brandName"		, enableCellEdit:false										},
				            { field: 'insDt'						, width: '15%'	, colKey: "c.grid.column.insDt"					, enableCellEdit:false	, cellFilter: "date:\'yyyy-MM-dd\'"	},
				            { field: 'insId'						, width: '10%'	, colKey: "c.grid.column.insId"					, enableCellEdit:false	, userFilter :'insId,insName'		},
				            { field: 'updDt'						, width: '15%'	, colKey: "c.grid.column.updDt"					, enableCellEdit:false	, cellFilter: "date:\'yyyy-MM-dd\'"	},
				            { field: 'updId'						, width: '10%'	, colKey: "c.grid.column.updId"					, enableCellEdit:false	, userFilter :'updId,updName'		}
			            ];
	
	var gridParam2 = {
			scope : $scope, 					//mandatory
			gridName : "grid_product",			//mandatory
			url :  '/api/dms/exhibit/divTitlePoduct',	//mandatory
			searchKey : "productSearch",   		//mandatory
			columnDefs : productColumn2,    		//mandatory
			gridOptions : {             		//optional
				pagination : false
			},callbackFn : function () {
			}
	};
	
	$scope.myGrid2 = new gridService.NgGrid(gridParam2);
	
	$scope.divTitleDetailPop = function(field, row) {
		$scope.exhibitId = row.exhibitId;
		$scope.detailGroupNo = row.groupNo;
		var winName='DivTitle Insert';
		var winURL = Rest.context.path +"/dms/exhibit/popup/divTitleInsert";
		popupwindow(winURL,winName,1100,430);
	}
	
	
	// 구분 타이틀 상품 그리드
	$scope.divTitleDetail = function(row) {
		$scope.productSearch.exhibitId = row.exhibitId; 
		$scope.productSearch.groupNo = row.groupNo; 
		// 상품 그리드 show
		$scope.product = true;
		$scope.myGrid2.loadGridData();
	}
	
	this.insertDivTitle = function() {
		$scope.detailGroupNo = "";
		var winName='DivTitle Insert';
		var winURL = Rest.context.path +"/dms/exhibit/popup/divTitleInsert";
		popupwindow(winURL,winName,1100,430);
	}
	
	this.deleteDivTitle = function() {
		$scope.myGrid1.deleteGridData();
		$scope.product = false;
	}
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
				$window.location.href = Rest.context.path +"/dms/exhibit/popup/detail";
			}
		}
	}
	
	$scope.changeMode = function(param) {
		if (param) {
			$scope.product = false;
			$scope.grid_product.data = [];
		}
		$scope.myGrid1.toggleRowSelection();
	}
	
	// 상품 상세 팝업
	$scope.productDetail = function(field, row) {
		commonPopupService.openProductDetailPopup($scope, row.productId);
	}
	
	// 상품 검색 팝업
	this.searchProduct = function() {
		commonPopupService.productPopup($scope, "callback_product", true);
	}
	
	$scope.callback_product = function(data) {
		var productIds = [];
		var divTitleProduct = [];
		for (var i=0; i<data.length; i++ ) {
			productIds.push(data[i].productId);
		}
		$scope.productSearch.productIds = productIds;
		
		// DB에 이미 저장되어 있는 데이터인지 체크
		exhibitService.checkExhibitDivTitleProduct($scope.productSearch, function(response) {
			// GRID에 이미 있는 중복 데이터인지 체크
			for (var k = 0; k < response.length; k++) {
				for (var i=0; i < productIds.length; i++ ) {
					if (response[k].productId == productIds[i]) {
						var product = {
								exhibitId	: $scope.productSearch.exhibitId,
								groupNo 	: $scope.productSearch.groupNo,
								sortNo    	: '1',
								displayYn  	: 'Y',
								productId 	: productIds[i],
								crudType 	: 'I',
						};
						divTitleProduct.push(product);
					}
				}
			}
			if (divTitleProduct.length > 0) {
				exhibitService.insertDivTitleProduct(divTitleProduct, function(response) {
					alert("저장되었습니다.");
					$scope.myGrid2.loadGridData();
				});
			}
		});
	}
	
	 $scope.files = [];

	    //listen for the file selected event
	    $scope.$on("fileSelected", function (event, args) {
	        $scope.$apply(function () {            
	            //add the file object to the scope's files collection
	            $scope.files.push(args.file);
	        });
	    });
	
	// 상품 일괄 업로드 EX
	this.batchProduct = function() {
		commonPopupService.gridbulkuploadPopup($scope,"excelUpload_callback","product");
	}
	
	$scope.excelUpload_callback = function(response) {
		var data = response.resultList;
		var productIds = [];
		var divTitleProduct = [];
		for (var i=0; i<data.length; i++ ) {
			productIds.push(data[i].pmsProduct.productId);
		}
		$scope.productSearch.productIds = productIds;
		
		// DB에 이미 저장되어 있는 데이터인지 체크
		exhibitService.checkExhibitDivTitleProduct($scope.productSearch, function(response) {
			// GRID에 이미 있는 중복 데이터인지 체크
			for (var k = 0; k < response.length; k++) {
				for (var i=0; i<data.length; i++ ) {
					if (response[k].productId == data[i].pmsProduct.productId) {
						var product = {
								exhibitId	: $scope.productSearch.exhibitId,
								groupNo 	: $scope.productSearch.groupNo,
								sortNo    	: '1',
								displayYn  	: 'Y',
								productId 	: data[i].pmsProduct.productId,
								crudType 	: 'I',
						};
						divTitleProduct.push(product);
					}
				}
			}
			if (divTitleProduct.length > 0) {
				exhibitService.insertDivTitleProduct(divTitleProduct, function(response) {
					alert("저장되었습니다.");
					$scope.myGrid2.loadGridData();
				});
			}
		});
	}
	
	this.close = function() {
		$window.close();
	}
}).filter('exhibitTypeFilter', function() {// EXHIBIT_TYPE_CD 
	
	var comboHash = {
		    'EXHIBIT_TYPE_CD.EXHIBIT': '일반기획전',
		    'EXHIBIT_TYPE_CD.COUPON': '쿠폰받기',
		    'EXHIBIT_TYPE_CD.TIMESALE': '타임세일',
		    'EXHIBIT_TYPE_CD.ONEDAY': '원데이',
		    'EXHIBIT_TYPE_CD.OFFSHOP': '오프라인매장'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('exhibitStateFilter', function() {// EXHIBIT_STATE_CD 
	
	var comboHash = {
			'EXHIBIT_STATE_CD.READY' : '대기',
			'EXHIBIT_STATE_CD.RUN' : '진행중',
			'EXHIBIT_STATE_CD.STOP' : '중지'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('groupTypeFilter', function() {// GROUP_TYPE_CD 
	
	var comboHash = {
			'GROUP_TYPE_CD.IMG': '이미지',
			'GROUP_TYPE_CD.TEXT': '텍스트'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('productDisplayType1Filter', function() {// PRODUCT_DISPLAY_TYPE1_CD 
	
	var comboHash = {
		    'PRODUCT_DISPLAY_TYPE1_CD.5': '5',
		    'PRODUCT_DISPLAY_TYPE1_CD.4': '4',
		    'PRODUCT_DISPLAY_TYPE1_CD.3': '3'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('productDisplayType2Filter', function() {// PRODUCT_DISPLAY_TYPE2_CD 
	
	var comboHash = {
		    'PRODUCT_DISPLAY_TYPE2_CD.2': '2',
		    'PRODUCT_DISPLAY_TYPE2_CD.1': '1'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
});


exhibitApp.controller("dms_exhibitDivTitleInsertPopApp_controller", function($window, $scope, $compile, exhibitService, commonService) {
	
	var popScope = $window.opener.$scope;//부모창의 scope
	$scope.dmsExhibitgroup = {};
//	$scope.dmsExhibitgroup.img ="";
//	$scope.dmsExhibitgroup.pcUrlLinkUseYn="";
//	$scope.dmsExhibitgroup.mbUrlLinkUseYn="";
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	
	this.init = function() {
		var groupNo = popScope.detailGroupNo;
		if (groupNo != '') {
			$scope.dmsExhibitgroup.groupNo = groupNo; 
			$scope.dmsExhibitgroup.exhibitId = popScope.exhibitId; 
			exhibitService.getDivTitleDetail($scope.dmsExhibitgroup, function(data) {
				$scope.dmsExhibitgroup = data;
				if (data.url1 == null) {
					$scope.dmsExhibitgroup.pcUrlLinkUseYn="N";
				} else {
					$scope.dmsExhibitgroup.pcUrlLinkUseYn="Y";	
				}
				if (data.url2 == null) {
					$scope.dmsExhibitgroup.mbUrlLinkUseYn="N";
				} else {
					$scope.dmsExhibitgroup.mbUrlLinkUseYn="Y";
				}
			});
		}
	}
	
	
	// 구분 타이틀 저장
	this.insertDivTitle = function() {
		//폼 체크
		if (!commonService.checkForm($scope.divTitleForm)) {
			return;
		}
		
		if ($scope.dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.IMG') {
			if ($scope.dmsExhibitgroup.img == "" || $scope.dmsExhibitgroup.img == undefined) {
				alert("유효하지 않은 항목이 존재합니다.");
				return;
			}
		} else if ($scope.dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.TEXT') {
			if ($scope.dmsExhibitgroup.name == "" || $scope.dmsExhibitgroup.name == undefined) {
				alert("유효하지 않은 항목이 존재합니다.");
				return;
			}
		}
		
		if (window.confirm($scope.MESSAGES["common.label.confirm.save"])) {
			$scope.dmsExhibitgroup.exhibitId = popScope.divTitSearch.exhibitId;
			exhibitService.insertDivTitle($scope.dmsExhibitgroup, function(response) {
				popScope.myGrid1.loadGridData();
				alert($scope.MESSAGES["common.label.alert.save"]);
				$window.close();
			});
		}
	}
	
	// 구분 타이틀 등록 팝업 닫기
	this.close = function() {
		popScope.detailGroupNo = "";
		$window.close();
	}
	
	$scope.uploadImageCallback = function(path) {
		$scope.dmsExhibitgroup.img = path;
		
	}
	
	// 이미지 삭제
	this.deleteImage = function() {
		//신규 템프 파일은 바이너리 바로 삭제
		commonService.deleteFile($scope.dmsExhibitgroup.img, function(data){
			//모델에서 삭제
			$scope.dmsExhibitgroup.img = "";
	    });
	}
	
// 대표 상품 등록 팝업
}).controller("dms_exhibitMainProductInsertPopApp_controller", function($window, $scope, $compile, exhibitService, commonService, commonPopupService) {
	$window.$scope = $scope;
	var popScope = $window.opener.$scope;//부모창의 scope
	$scope.dmsExhibitMainProduct = {};
	$scope.dmsExhibitMainProduct.img = "";
	$scope.checkProduct = {};
	
	// 상품 검색 팝업
	this.searchProduct = function() {
		commonPopupService.productPopup($scope, "callback_product", false);
	}
	
	$scope.callback_product = function(data) {
		console.log(data);
		$scope.checkProduct.productId = data[0].productId; 
		exhibitService.checkMainProduct($scope.checkProduct, function(response) {
			if (response.content == "true") {
				alert("이미 등록 되었습니다.");
			} else {
				$scope.dmsExhibitMainProduct.productId = data[0].productId; 
				$scope.dmsExhibitMainProduct.productName = data[0].name;
				$scope.dmsExhibitMainProduct.productTypeName = data[0].productTypeName;
				$scope.dmsExhibitMainProduct.saleStateName = data[0].saleStateName;
				$scope.dmsExhibitMainProduct.salePrice = data[0].salePrice;
				$scope.dmsExhibitMainProduct.brandName = data[0].brandName;
				$scope.dmsExhibitMainProduct.saleStartDt = data[0].saleStartDt;
				$scope.dmsExhibitMainProduct.saleEndDt = data[0].saleEndDt;
			}
		}); 
	}
	
	// 구분 타이틀 등록 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// 상품Id, 상품명 삭제
	this.delText = function() {
		$scope.dmsExhibitMainProduct.productId = ""; 
		$scope.dmsExhibitMainProduct.productName = ""; 
	}
	
	// 대표 상품 저장
	this.saveMainProduct = function() {
		//폼 체크
		if (!commonService.checkForm($scope.mainProductInsertForm)) {
			return;
		}
		$scope.dmsExhibitMainProduct.crudType = "I";
		popScope.ctrl.addRowMainProduct($scope.dmsExhibitMainProduct);
		$window.close();
	}
	
	$scope.uploadDefaultImageCallback = function(path) {
		$scope.dmsExhibitMainProduct.img = path;		
	}
	
	// 이미지 삭제
	this.deleteImage = function() {
		//신규 템프 파일은 바이너리 바로 삭제
		commonService.deleteFile($scope.dmsExhibitMainProduct.img, function(data){
			//모델에서 삭제
			$scope.dmsExhibitMainProduct.img = "";
	    });
	}
// 기획전 대표상품 상세 팝업	
}).controller("dms_exhibitMainProductDetailPopApp_controller", function($window, $scope, $compile, exhibitService, commonService, commonPopupService) {
	popScope = $window.opener.$scope;//부모창의 scope
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	this.init = function() {
		var mainProductId = popScope.dmsExhibit.mainProductId;
		
		for (var i=0; i < popScope.grid_mainProduct.data.length; i++) {
			if (popScope.grid_mainProduct.data[i].productId == mainProductId ) {
				$scope.dmsExhibitMainProduct = angular.copy(popScope.grid_mainProduct.data[i]);
				$scope.dmsExhibitMainProduct.productName = popScope.grid_mainProduct.data[i].pmsProduct.name;
				break;
			}
		}
	}
	
	// 대표 상품 상세 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// 대표상품 수정
	this.saveMainProduct = function() {
		var mainProductId = popScope.dmsExhibit.mainProductId;
		for (var i=0; i < popScope.grid_mainProduct.data.length; i++) {
			if (popScope.grid_mainProduct.data[i].productId == mainProductId ) {
				popScope.grid_mainProduct.data[i].name = $scope.dmsExhibitMainProduct.name;
				popScope.grid_mainProduct.data[i].sortNo = $scope.dmsExhibitMainProduct.sortNo;
				popScope.grid_mainProduct.data[i].img = $scope.dmsExhibitMainProduct.img;
				popScope.$apply();
				break;
			}
		}
		$window.close();
	}
	
	$scope.uploadDefaultImageCallback = function(path) {
		if ($scope.dmsExhibitMainProduct.img != null && $scope.dmsExhibitMainProduct.img != '' && $scope.dmsExhibitMainProduct.img != undefined) {
			$scope.ctrl.deleteImage(function() {
				$scope.dmsExhibitMainProduct.img = path;		
			});
		} else {
			$scope.dmsExhibitMainProduct.img = path;
		}
	}
	
	// 이미지 삭제
	this.deleteImage = function() {
		//신규 템프 파일은 바이너리 바로 삭제
		commonService.deleteFile($scope.dmsExhibitMainProduct.img, function(data){
			//모델에서 삭제
			$scope.dmsExhibitMainProduct.img = "";
	    });
	}
	
});